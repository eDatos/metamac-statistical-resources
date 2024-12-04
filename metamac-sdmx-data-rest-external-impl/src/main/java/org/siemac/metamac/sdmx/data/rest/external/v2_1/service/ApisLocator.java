package org.siemac.metamac.sdmx.data.rest.external.v2_1.service;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.sdmx.resources.sdmxml.schemas.v2_1.message.Structure;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataStructureType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataStructuresType;
import org.siemac.edatos.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.sdmx.data.rest.external.conf.DataConfigurationConstants;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.rest.external.v2_1.service.SdmxSrmRestExternalFacadeV21;

@Component
public class ApisLocator {

    private final String                      DETAIL_STUBS                 = "referencestubs";
    private final String                      REFERENCES_NONE              = "none";

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    private SdmxSrmRestExternalFacadeV21      sdmxSrmRestExternalFacadeV21 = null;

    private SdmxSrmRestExternalFacadeV21 getSdmxSrmRestExternalFacadeV21() throws MetamacException {

        if (sdmxSrmRestExternalFacadeV21 == null) {
            String baseApi = configurationService.getProperties().getProperty(DataConfigurationConstants.ENDPOINT_SDMX_SRM_EXTERNAL_API);
            sdmxSrmRestExternalFacadeV21 = JAXRSClientFactory.create(baseApi, SdmxSrmRestExternalFacadeV21.class, null, true); // true to do thread safe
        }
        String apiKey = configurationService.retrieveStatisticalResourcesApiKey();
        // reset thread context
        WebClient.client(sdmxSrmRestExternalFacadeV21).reset();
        WebClient.client(sdmxSrmRestExternalFacadeV21).accept("application/xml").header(CoreCommonConstants.API_KEY_PARAMETER, apiKey);

        return sdmxSrmRestExternalFacadeV21;
    }

    public DataStructureType retrieveDsdByUrn(String urn) throws MetamacException {
        try {
            String[] dataStructureComponents = GeneratorUrnUtils.extractVersionableArtefactParts(urn);
            String agencyId = dataStructureComponents[0];
            String dsdId = dataStructureComponents[1];
            String version = dataStructureComponents[2];
            Structure structure = getSdmxSrmRestExternalFacadeV21().findDataStructureDefinition(agencyId, dsdId, version, DETAIL_STUBS, REFERENCES_NONE);

            DataStructuresType dataStructures = structure.getStructures().getDataStructures();

            return dataStructures.getDataStructures().iterator().next();
        } catch (Exception e) {
            throw manageSrmInternalRestException(e);
        }
    }

    // -------------------------------------------------------------------------------------------------
    // PRIVATE UTILS
    // -------------------------------------------------------------------------------------------------

    private MetamacException manageSrmInternalRestException(Exception e) throws MetamacException {
        return ServiceExceptionUtils.manageMetamacRestException(e, ServiceExceptionParameters.API_SRM_SDMX, getSdmxSrmRestExternalFacadeV21());
    }
}
