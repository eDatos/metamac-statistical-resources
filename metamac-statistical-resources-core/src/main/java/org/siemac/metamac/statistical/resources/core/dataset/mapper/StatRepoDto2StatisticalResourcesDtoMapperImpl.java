package org.siemac.metamac.statistical.resources.core.dataset.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdAttribute;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimensionRepository;
import org.siemac.metamac.statistical.resources.core.dto.datasets.AttributeValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesExternalItemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;

@Component(StatRepoDto2StatisticalResourcesDtoMapper.BEAN_ID)
public class StatRepoDto2StatisticalResourcesDtoMapperImpl implements StatRepoDto2StatisticalResourcesDtoMapper {

    @Autowired
    private SrmRestInternalService  srmRestInternalService;

    @Autowired
    private CodeDimensionRepository codeDimensionRepository;

    @Override
    public DsdAttributeInstanceDto attributeDtoToDsdAttributeInstanceDto(Long datasetVersionId, DsdAttribute dsdAttribute, AttributeInstanceDto source) throws MetamacException {
        DsdAttributeInstanceDto target = new DsdAttributeInstanceDto();
        target.setAttributeId(source.getAttributeId());
        target.setCodeDimensions(attributeInstanceCodeDimensionsDto2DsdAttributeInstanceCodeDimensionsDto(datasetVersionId, source));
        target.setValue(attributeInstanceValue2AttributeValueDto(dsdAttribute, source));
        target.setUuid(source.getUuid());
        return target;
    }

    @Override
    public List<DsdAttributeInstanceDto> attributeDtosToDsdAttributeInstanceDtos(Long datasetVersionId, DsdAttribute attribute, List<AttributeInstanceDto> sources) throws MetamacException {
        List<DsdAttributeInstanceDto> targets = new ArrayList<DsdAttributeInstanceDto>(sources.size());
        for (AttributeInstanceDto source : sources) {
            targets.add(attributeDtoToDsdAttributeInstanceDto(datasetVersionId, attribute, source));
        }
        return targets;
    }

    private AttributeValueDto attributeInstanceValue2AttributeValueDto(DsdAttribute dsdAttribute, AttributeInstanceDto source) throws MetamacException {
        // TODO EDATOS-4945 ver aquí si se aborda atributos a nivel de observación
        String attrValue = source.getValue().getLocalisedLabel(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE);
        AttributeValueDto attributeValueDto = new AttributeValueDto();
        if (dsdAttribute.getCodelistRepresentationUrn() != null) {
            attributeValueDto.setExternalItemValue(retrieveCodeExternalItem(dsdAttribute.getCodelistRepresentationUrn(), attrValue));
        } else if (dsdAttribute.getConceptSchemeRepresentationUrn() != null) {
            attributeValueDto.setExternalItemValue(retrieveConceptExternalItem(dsdAttribute.getConceptSchemeRepresentationUrn(), attrValue));
        } else {
            attributeValueDto.setStringValue(attrValue);
        }
        return attributeValueDto;
    }

    private ExternalItemDto retrieveConceptExternalItem(String conceptSchemeRepresentationUrn, String conceptCode) throws MetamacException {
        String[] triplet = UrnUtils.splitUrnItemScheme(conceptSchemeRepresentationUrn);
        String conceptUrn = GeneratorUrnUtils.generateSdmxConceptUrn(new String[]{triplet[0]}, triplet[1], triplet[2], conceptCode);
        Concept concept = srmRestInternalService.retrieveConceptByUrn(conceptUrn);
        ExternalItemDto externalItemDto = StatisticalResourcesExternalItemUtils.buildExternalItemDtoFromConcept(concept);
        return externalItemDto;
    }

    private ExternalItemDto retrieveCodeExternalItem(String codelistRepresentationUrn, String codeCode) throws MetamacException {
        String[] triplet = UrnUtils.splitUrnItemScheme(codelistRepresentationUrn);
        String codeUrn = GeneratorUrnUtils.generateSdmxCodeUrn(new String[]{triplet[0]}, triplet[1], triplet[2], codeCode);
        Code code = srmRestInternalService.retrieveCodeByUrn(codeUrn);
        ExternalItemDto externalItemDto = StatisticalResourcesExternalItemUtils.buildExternalItemDtoFromCode(code);
        return externalItemDto;
    }

    private Map<String, List<CodeItemDto>> attributeInstanceCodeDimensionsDto2DsdAttributeInstanceCodeDimensionsDto(Long datasetVersionId, AttributeInstanceDto source) throws MetamacException {
        if (source.getCodesByDimension() == null) {
            return null;
        }
        Map<String, List<CodeItemDto>> target = new HashMap<String, List<CodeItemDto>>();

        for (String dimensionId : source.getCodesByDimension().keySet()) {
            List<CodeItemDto> codeItems = new ArrayList<CodeItemDto>();
            for (String codeDimIdentifier : source.getCodesByDimension().get(dimensionId)) {
                codeItems.add(new CodeItemDto(codeDimIdentifier, codeDimensionRepository.findTitleForDatasetVersionByDimensionIdAndIdentifier(datasetVersionId, dimensionId, codeDimIdentifier)));
            }
            target.put(dimensionId, codeItems);
        }
        return target;
    }
}
