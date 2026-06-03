package org.siemac.metamac.statistical.resources.core.dataset.mapper;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.GranularityAttributeInstanceDto;

public interface StatisticalResourcesDto2StatRepoDtoMapper {

    public static final String BEAN_ID = "statisticalResourcesDto2StatRepoDtoMapper";

    public AttributeInstanceDto dsdAttributeInstanceDtoToAttributeInstanceDto(DsdAttributeInstanceDto source) throws MetamacException;

    public GranularityAttributeInstanceDto dsdGranularityAttributeInstanceDtoToGranularityAttributeInstanceDto(DsdGranularityAttributeInstanceDto source) throws MetamacException;
}
