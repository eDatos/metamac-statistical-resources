package org.siemac.metamac.statistical.resources.web.server.stream;

import org.apache.avro.specific.SpecificRecordBase;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.utils.InternationalStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.edatos.dataset.repository.dto.ExternalItemCodesDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ExternalItemDto;

@org.springframework.stereotype.Component("kafkaMapper")
public class KafkaMapperImpl implements KafkaMapper {

    private final SrmRestInternalService srmService;

    @Autowired
    public KafkaMapperImpl(SrmRestInternalService srmService) {
        this.srmService = srmService;
    }

    @Override
    public ExternalItemDto kafkaMessageToRepositoryExternalItemDto(ServiceContext ctx, SpecificRecordBase messageSource) throws MetamacException {
        if (messageSource instanceof CodelistAvro) {
            return processSrmCodelistKafkaMessage(ctx, messageSource);
        } else if (messageSource instanceof ConceptSchemeAvro) {
            return processSrmConceptSchemeKafkaMessage(ctx, messageSource);
        }
        return null;
    }

    private ExternalItemDto processSrmCodelistKafkaMessage(ServiceContext ctx, SpecificRecordBase message) throws MetamacException {

        CodelistAvro codelistAvro = (CodelistAvro) message;

        ExternalItemDto externalItemDto = new ExternalItemDto();

        externalItemDto.setType(TypeExternalArtefactsEnum.CODELIST.getName());
        externalItemDto.setUrn(codelistAvro.getUrn());

        Codes codes = srmService.retrieveCodesOfCodelistEfficiently(codelistAvro.getUrn());
        for (CodeResourceInternal srmCode : codes.getCodes()) {
            externalItemDto.addCode(itemResourceInternalToExternalItemCodesDto(srmCode));
        }

        return externalItemDto;

    }

    private ExternalItemDto processSrmConceptSchemeKafkaMessage(ServiceContext ctx, SpecificRecordBase message) throws MetamacException {

        ConceptSchemeAvro conceptSchemeAvro = (ConceptSchemeAvro) message;

        ExternalItemDto externalItemDto = new ExternalItemDto();

        externalItemDto.setType(TypeExternalArtefactsEnum.CONCEPT_SCHEME.getName());
        externalItemDto.setUrn(conceptSchemeAvro.getUrn());

        Concepts concepts = srmService.retrieveConceptsOfConceptSchemeEfficiently(conceptSchemeAvro.getUrn());
        for (ConceptResourceInternal srmConcept : concepts.getConcepts()) {
            externalItemDto.addCode(itemResourceInternalToExternalItemCodesDto(srmConcept));
        }

        return externalItemDto;
    }

    private ExternalItemCodesDto itemResourceInternalToExternalItemCodesDto(ItemResourceInternal item) {
        ExternalItemCodesDto externalItemCodeDto = new ExternalItemCodesDto();
        externalItemCodeDto.setCode(item.getId());
        externalItemCodeDto.setTitle(InternationalStringUtils.buildDatasetRepositoryInternationalStringDtoFromCommonInternationalStringDto(item.getName()));

        return externalItemCodeDto;
    }

}
