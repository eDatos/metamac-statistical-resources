package org.siemac.metamac.rest.statistical_resources.v1_0.service;

import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.DATASET_1_CODE;

import java.io.InputStream;
import java.util.Arrays;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Resources;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ExtendedResource;
import org.siemac.metamac.statistical_resources.rest.internal.StatisticalResourcesRestInternalConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticalResourcesRestInternalFacadeV10ResourcesTest extends StatisticalResourcesRestInternalFacadeV10BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticalResourcesRestInternalFacadeV10ResourcesTest.class);

    @Test
    public void testFindResources() throws Exception {
        String query = "GEOCOV_VARELEM_ID EQ " + "'variableElement01'" + " AND IS_LAST_VERSION EQ 'true'";
        Resources resources = statisticalResourcesRestInternalFacadeClientXml.findResources(query, null, null, null, null);
        
        assertEquals(4, resources.getResources().size());
        assertEquals(StatisticalResourcesRestInternalConstants.KIND_RESOURCES, resources.getKind());
    }
    
    @Test
    public void testFindDatasetsXml() throws Exception {
        String query = "GEOCOV_VARELEM_ID EQ " + "'variableElement01'" + " AND IS_LAST_VERSION EQ 'true'";
        String requestUri = getFindResourcesUri(query, null, null, "es");
        InputStream responseExpected = StatisticalResourcesRestInternalFacadeV10ResourcesTest.class.getResourceAsStream("/responses/resources/findResources.xml");
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }
    
    public String getFindResourcesUri(String query, String limit, String offset, String langs) throws Exception {
        return getFindResourcesUri(StatisticalResourcesRestInternalConstants.LINK_SUBPATH_RESOURCES, null, null, query, limit, offset, langs);
    }

    @Test
    public void testRetrieveResource() throws Exception {
        String query = "IS_LAST_VERSION EQ 'true'";
        Resources resources = statisticalResourcesRestInternalFacadeClientXml.findResources(query, null, null, null, null);
        
        ExtendedResource resource = resources.getResources().get(0);
        
        assertEquals(DATASET_1_CODE, resource.getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=agency1:dataset1(01.000)", resource.getUrn());
        assertEquals(StatisticalResourcesRestInternalConstants.KIND_RESOURCE, resource.getKind());
        assertNotNull(resource.getStatisticalOperation());
        assertEquals("http://localhost:8080/statistical-visualizer/visualizer/data.html?resourceType=dataset&agencyId=agency1&resourceId=dataset1&version=01.000#", resource.getVisualizerHtmlLink());
        assertNotNull(resource.getStatisticalOperation().getId());  
    }
    
    @Test
    public void testRetrieveResourceJson() throws Exception {
        String query = "query=GEOCOV_VARELEM_ID%20EQ%20'variableElement01'%20AND%20IS_LAST_VERSION%20EQ%20'true'";
        String requestBase = getFindResourcesUri(null, null, null, null);
        String[] requestUris = new String[]{requestBase + "?lang=es&" + query, requestBase + ".json?lang=es&" + query, requestBase + "?_type=json&lang=es&" + query};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = StatisticalResourcesRestInternalFacadeV10ResourcesTest.class.getResourceAsStream("/responses/resources/retrieveResource.id1.json");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_JSON, Status.OK, responseExpected);
        }
    } 
    
    @Test
    public void testRetrieveResourceAnotherLanguage() throws Exception {
        String query = "GEOCOV_VARELEM_ID EQ " + "'variableElement01'" + " AND IS_LAST_VERSION EQ 'true'";
        Resources resources = statisticalResourcesRestInternalFacadeClientXml.findResources(query, null, null, null, Arrays.asList("en"));

        MetamacRestAsserts.assertEqualsInternationalString("es", "title-dataset1 en Espanol", "en", "title-dataset1 in English", resources.getResources().get(0).getName());
    }
}