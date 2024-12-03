package org.siemac.metamac.statistical_resources.rest.external.invocation;

import javax.annotation.PostConstruct;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.edatos.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.common_metadata.rest.external.v1_0.service.CommonMetadataV1_0;
import org.siemac.metamac.srm.rest.external.v1_0.service.SrmRestExternalFacadeV10;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical_operations.rest.external.v1_0.service.StatisticalOperationsV1_0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metamacApisLocatorRestExternal")
public class MetamacApisLocator {

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    private SrmRestExternalFacadeV10          srmRestExternalFacadeV10            = null;

    private CommonMetadataV1_0                commonMetadataRestExternalFacadeV10 = null;

    private StatisticalOperationsV1_0         statisticalOperationsV1_0           = null;
    private String                            apiKey;

    @PostConstruct
    public void initService() throws Exception {
        apiKey = configurationService.retrieveStatisticalResourcesApiKey();

        String srmExternalApi = configurationService.retrieveSrmExternalApiUrlBase();
        srmRestExternalFacadeV10 = JAXRSClientFactory.create(srmExternalApi, SrmRestExternalFacadeV10.class, null, true); // true to do thread safe

        String commonMetadataExternalApi = configurationService.retrieveCommonMetadataExternalApiUrlBase();
        commonMetadataRestExternalFacadeV10 = JAXRSClientFactory.create(commonMetadataExternalApi, CommonMetadataV1_0.class, null, true); // true to do thread safe

        String statisticalOperationsInternalApi = configurationService.retrieveStatisticalOperationsExternalApiUrlBase();
        statisticalOperationsV1_0 = JAXRSClientFactory.create(statisticalOperationsInternalApi, StatisticalOperationsV1_0.class, null, true);
    }

    public SrmRestExternalFacadeV10 getSrmRestExternalFacadeV10() {
        // reset thread context
        WebClient.client(srmRestExternalFacadeV10).reset();
        WebClient.client(srmRestExternalFacadeV10).accept("application/xml").header(CoreCommonConstants.API_KEY_PARAMETER, apiKey);

        return srmRestExternalFacadeV10;
    }

    public CommonMetadataV1_0 getCommonMetadataRestExternalFacadeV10() {
        // reset thread context
        WebClient.client(commonMetadataRestExternalFacadeV10).reset();
        WebClient.client(commonMetadataRestExternalFacadeV10).accept("application/xml").header(CoreCommonConstants.API_KEY_PARAMETER, apiKey);

        return commonMetadataRestExternalFacadeV10;
    }

    public StatisticalOperationsV1_0 getStatisticalOperationsV1_0() {
        // Reset thread context
        WebClient.client(statisticalOperationsV1_0).reset();
        WebClient.client(statisticalOperationsV1_0).accept("application/xml").header(CoreCommonConstants.API_KEY_PARAMETER, apiKey);
        return statisticalOperationsV1_0;
    }
}
