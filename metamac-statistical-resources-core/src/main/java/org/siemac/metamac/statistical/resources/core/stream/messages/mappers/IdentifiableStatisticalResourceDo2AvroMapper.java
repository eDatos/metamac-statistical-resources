package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.stream.messages.IdentifiableStatisticalResourceAvro;

public class IdentifiableStatisticalResourceDo2AvroMapper {

    protected static ConfigurationService configurationService;

    public static ConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = ApplicationContextProvider.getApplicationContext().getBean(ConfigurationService.class);
        }
        return configurationService;
    }
    protected IdentifiableStatisticalResourceDo2AvroMapper() {
    }

    public static IdentifiableStatisticalResourceAvro do2Avro(IdentifiableStatisticalResource source) {
        IdentifiableStatisticalResourceAvro target = null;
        if (source != null) {
            try {
                String operationsApiExternalEndpoint = getConfigurationService().retrieveStatisticalOperationsExternalApiUrlBase();
                target = IdentifiableStatisticalResourceAvro.newBuilder().setCode(source.getCode()).setUrn(source.getUrn())
                    .setStatisticalOperation(ExternalItemDo2AvroMapper.do2Avro(source.getStatisticalOperation(), operationsApiExternalEndpoint)).build();
            } catch (MetamacException e) {
               
            }
        }
        return target;
    }

}
