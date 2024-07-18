package org.siemac.metamac.statistical.resources.core.invocation.service;

import javax.annotation.PostConstruct;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.edatos.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.common_metadata.rest.external.v1_0.service.CommonMetadataV1_0;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.notices.rest.internal.v1_0.service.NoticesV1_0;
import org.siemac.metamac.srm.rest.internal.v1_0.service.SrmRestInternalFacadeV10;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical_operations.rest.internal.v1_0.service.StatisticalOperationsRestInternalFacadeV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetamacApisLocator {

    @Autowired
    private StatisticalResourcesConfiguration          configurationService;

    private SrmRestInternalFacadeV10                   srmRestInternalFacadeV10                   = null;
    private StatisticalOperationsRestInternalFacadeV10 statisticalOperationsRestInternalFacadeV10 = null;
    private CommonMetadataV1_0                         commonMetadataRestExternalFacadeV10        = null;
    private NoticesV1_0                                noticesV10                                 = null;
    private String                                     apiKey;

    @PostConstruct
    public void initService() throws Exception {
        apiKey = configurationService.retrieveStatisticalResourcesApiKey();

    }

    public SrmRestInternalFacadeV10 getSrmRestInternalFacadeV10() throws MetamacException {
        if (srmRestInternalFacadeV10 == null) {
            String baseApi = configurationService.retrieveSrmInternalApiUrlBase();
            srmRestInternalFacadeV10 = JAXRSClientFactory.create(baseApi, SrmRestInternalFacadeV10.class, null, true); // true to do thread safe
        }
        // reset thread context
        WebClient.client(srmRestInternalFacadeV10).reset();
        WebClient.client(srmRestInternalFacadeV10).accept("application/xml").header(CoreCommonConstants.API_KEY_PARAMETER, apiKey);

        return srmRestInternalFacadeV10;
    }

    public StatisticalOperationsRestInternalFacadeV10 getStatisticalOperationsRestInternalFacadeV10() throws MetamacException {
        if (statisticalOperationsRestInternalFacadeV10 == null) {
            String baseApi = configurationService.retrieveStatisticalOperationsInternalApiUrlBase();
            statisticalOperationsRestInternalFacadeV10 = JAXRSClientFactory.create(baseApi, StatisticalOperationsRestInternalFacadeV10.class, null, true); // true to do thread safe
        }
        // reset thread context
        WebClient.client(statisticalOperationsRestInternalFacadeV10).reset();
        WebClient.client(statisticalOperationsRestInternalFacadeV10).accept("application/xml").header(CoreCommonConstants.API_KEY_PARAMETER, apiKey);

        return statisticalOperationsRestInternalFacadeV10;
    }

    public CommonMetadataV1_0 getCommonMetadataRestExternalFacadeV10() throws MetamacException {
        if (commonMetadataRestExternalFacadeV10 == null) {
            String baseApi = configurationService.retrieveCommonMetadataExternalApiUrlBase();
            commonMetadataRestExternalFacadeV10 = JAXRSClientFactory.create(baseApi, CommonMetadataV1_0.class, null, true); // true to do thread safe
        }
        // reset thread context
        WebClient.client(commonMetadataRestExternalFacadeV10).reset();
        WebClient.client(commonMetadataRestExternalFacadeV10).accept("application/xml");

        return commonMetadataRestExternalFacadeV10;
    }

    public NoticesV1_0 getNoticesRestInternalFacadeV10() throws MetamacException {
        if (noticesV10 == null) {
            String baseApi = configurationService.retrieveNoticesInternalApiUrlBase();
            noticesV10 = JAXRSClientFactory.create(baseApi, NoticesV1_0.class, null, true); // true to do thread safe
        }
        // reset thread context
        WebClient.client(noticesV10).reset();
        WebClient.client(noticesV10).accept("application/xml").header(CoreCommonConstants.API_KEY_PARAMETER, apiKey);

        return noticesV10;
    }
}
