package org.siemac.metamac.statistical.resources.core.dataset.mapper;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdAttribute;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.GranularityAttributeInstanceDto;

public interface StatRepoDto2StatisticalResourcesDtoMapper {

    public static final String BEAN_ID = "statRepoDto2StatisticalResourcesDtoMapper";

    public DsdAttributeInstanceDto attributeDtoToDsdAttributeInstanceDto(Long datasetVersionId, DsdAttribute attribute, AttributeInstanceDto source) throws MetamacException;

    public List<DsdAttributeInstanceDto> attributeDtosToDsdAttributeInstanceDtos(Long datasetVersionId, DsdAttribute attribute, List<AttributeInstanceDto> sources) throws MetamacException;

    public DsdGranularityAttributeInstanceDto granularityAttributeInstanceDtoToDsdGranularityAttributeInstanceDto(Long datasetVersionId, DsdAttribute dsdAttribute, GranularityAttributeInstanceDto source) throws MetamacException;

    public List<DsdGranularityAttributeInstanceDto> granularityAttributeInstanceDtosToDsdGranularityAttributeInstanceDtos(Long datasetVersionId, DsdAttribute dsdAttribute, List<GranularityAttributeInstanceDto> sources) throws MetamacException;
}
