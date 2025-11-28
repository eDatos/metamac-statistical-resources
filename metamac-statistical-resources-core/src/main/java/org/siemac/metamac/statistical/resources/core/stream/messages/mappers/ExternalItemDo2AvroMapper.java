package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.stream.messages.ExternalItemAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalItemDo2AvroMapper {

    protected static ConfigurationService          configurationService;
    private static final Logger                    LOGGER = LoggerFactory.getLogger(ExternalItemDo2AvroMapper.class);
    protected ExternalItemDo2AvroMapper() {
    }

    public static ConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = ApplicationContextProvider.getApplicationContext().getBean(ConfigurationService.class);
        }
        return configurationService;
    }

    public static ExternalItemAvro do2Avro(ExternalItem source, String apiExternalEndpoint) {
        ExternalItemAvro target = null;
        if (source != null) {
            try {
                target = ExternalItemAvro.newBuilder().setCode(source.getCode()).setCodeNested(source.getCodeNested()).setManagementAppUrl(source.getManagementAppUrl())
                        .setTitle(InternationalStringDo2AvroMapper.do2Avro(source.getTitle())).setType(TypeExternalArtefactsEnumDo2AvroMapper.do2Avro(source.getType())).setUrn(source.getUrn())
                        .setUrnProvider(source.getUrnProvider()).setSelfLink(AvroMapperUtils.getSelfLinkExternalItems(source, apiExternalEndpoint)).build();
            } catch (MetamacException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return target;

    }

    public static ExternalItemAvro do2AvroStructuralResources(ExternalItem source) {
        ExternalItemAvro target = null;
        if (source != null) {
            try {
                String srmApiExternalEndpoint = getConfigurationService().retrieveSrmExternalApiUrlBase();
                target = ExternalItemAvro.newBuilder().setCode(source.getCode()).setCodeNested(source.getCodeNested()).setManagementAppUrl(source.getManagementAppUrl())
                        .setTitle(InternationalStringDo2AvroMapper.do2Avro(source.getTitle())).setType(TypeExternalArtefactsEnumDo2AvroMapper.do2Avro(source.getType())).setUrn(source.getUrn())
                        .setUrnProvider(source.getUrnProvider()).setSelfLink(AvroMapperUtils.getSelfLinkExternalItems(source, srmApiExternalEndpoint)).build();
            } catch (MetamacException e) {
            }
        }
        return target;

    }

}
