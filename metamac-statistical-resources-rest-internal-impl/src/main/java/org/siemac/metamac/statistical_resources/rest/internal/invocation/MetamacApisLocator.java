package org.siemac.metamac.statistical_resources.rest.internal.invocation;

import javax.annotation.PostConstruct;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.edatos.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.common_metadata.rest.external.v1_0.service.CommonMetadataV1_0;
import org.siemac.metamac.srm.rest.internal.v1_0.service.SrmRestInternalFacadeV10;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical_operations.rest.internal.v1_0.service.StatisticalOperationsRestInternalFacadeV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metamacApisLocatorRest")
public class MetamacApisLocator {

    @Autowired
    private StatisticalResourcesConfiguration          configurationService;

    private SrmRestInternalFacadeV10                   srmRestInternalFacadeV10                    = null;

    private CommonMetadataV1_0                         commonMetadataRestExternalFacadeV10         = null;

    private StatisticalOperationsRestInternalFacadeV10 statisticalOperationsRestInternalFacadeV1_0 = null;
    private String                                     apiKey;

    @PostConstruct
    public void initService() throws Exception {
        apiKey = configurationService.retrieveStatisticalResourcesApiKey();

        String srmInternalApi = configurationService.retrieveSrmInternalApiUrlBase();
        srmRestInternalFacadeV10 = JAXRSClientFactory.create(srmInternalApi, SrmRestInternalFacadeV10.class, null, true); // true to do thread safe

        String commonMetadataExternalApi = configurationService.retrieveCommonMetadataExternalApiUrlBase();
        commonMetadataRestExternalFacadeV10 = JAXRSClientFactory.create(commonMetadataExternalApi, CommonMetadataV1_0.class, null, true); // true to do thread safe

        String statisticalOperationsInternalApi = configurationService.retrieveStatisticalOperationsInternalApiUrlBase();
        statisticalOperationsRestInternalFacadeV1_0 = JAXRSClientFactory.create(statisticalOperationsInternalApi, StatisticalOperationsRestInternalFacadeV10.class, null, true);
    }

    public SrmRestInternalFacadeV10 getSrmRestExternalFacadeV10() {
        // reset thread context
        WebClient.client(srmRestInternalFacadeV10).reset();
        WebClient.client(srmRestInternalFacadeV10).accept("application/xml").header(CoreCommonConstants.API_KEY_PARAMETER, apiKey);

        return srmRestInternalFacadeV10;
    }

    public CommonMetadataV1_0 getCommonMetadataRestExternalFacadeV10() {
        // reset thread context
        WebClient.client(commonMetadataRestExternalFacadeV10).reset();
        WebClient.client(commonMetadataRestExternalFacadeV10).accept("application/xml").header(CoreCommonConstants.API_KEY_PARAMETER, apiKey);

        return commonMetadataRestExternalFacadeV10;
    }

    public StatisticalOperationsRestInternalFacadeV10 getStatisticalOperationsRestInternalFacadeV10() {
        // reset thread context
        WebClient.client(statisticalOperationsRestInternalFacadeV1_0).reset();
        WebClient.client(statisticalOperationsRestInternalFacadeV1_0).accept("application/xml").header(CoreCommonConstants.API_KEY_PARAMETER, apiKey);

        return statisticalOperationsRestInternalFacadeV1_0;
    }
}
