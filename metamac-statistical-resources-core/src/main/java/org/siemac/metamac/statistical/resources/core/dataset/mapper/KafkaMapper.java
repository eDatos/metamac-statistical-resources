package org.siemac.metamac.statistical.resources.core.dataset.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.edatos.dataset.repository.dto.ExternalItemDto;

public interface KafkaMapper {

    // external item
    public ExternalItemDto kafkaMessageToRepositoryExternalItemDto(ServiceContext ctx, SpecificRecordBase messageSource) throws MetamacException;

}
