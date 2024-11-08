package org.siemac.metamac.rest.statistical_resources.v1_0.service;

import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.COLLECTION_1_CODE;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.COLLECTION_2_CODE;

import java.io.InputStream;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.siemac.metamac.statistical_resources.rest.common.StatisticalResourcesRestConstants;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceWithRelatedResources;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Resources;
import org.siemac.metamac.statistical_resources.rest.internal.StatisticalResourcesRestInternalConstants;

public class StatisticalResourcesRestInternalFacadeV10ResourcesTest extends StatisticalResourcesRestInternalFacadeV10BaseTest {

    @Test
    public void testFindResources() {
        String query = "GEOCOV_VARELEM_ID EQ " + "'variableElement01'";
        Resources resources = statisticalResourcesRestInternalFacadeClientXml.findResources(query, null, null, null, null, null);

        assertEquals(2, resources.getResources().size());
        assertEquals(StatisticalResourcesRestInternalConstants.KIND_RESOURCES, resources.getKind());
    }

    @Test
    public void testFindGeoCacheByRelatedResourcesXml() throws Exception {
        String query = "GEOCOV_VARELEM_ID EQ " + "'variableElement01'";
        String requestUri = getFindResourcesUri(query, null, null, "es", null);
        InputStream responseExpected = StatisticalResourcesRestInternalConstants.class.getResourceAsStream("/responses/resources/findResources.xml");
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindGeoCacheByRelatedResourcesExcludingRelatedResourcesWithoutSelectedTerritoryXml() throws Exception {
        String query = "GEOCOV_VARELEM_ID EQ " + "'variableElement01'";
        String fields = StatisticalResourcesRestConstants.FIELD_EXCLUDE_RELATED_RESOURCES_WITHOUT_SELECTED_CRITERIA;
        String requestUri = getFindResourcesUri(query, null, null, "es", fields);
        InputStream responseExpected = StatisticalResourcesRestInternalConstants.class.getResourceAsStream("/responses/resources/findResourcesExcludingRelatedResources.xml");
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    public String getFindResourcesUri(String query, String limit, String offset, String langs, String fields) throws Exception {
        return getFindResourcesUri(StatisticalResourcesRestInternalConstants.LINK_SUBPATH_RESOURCES, null, null, query, limit, offset, langs, fields);
    }

    @Test
    public void testRetrieveResource() {
        String query = "GEOCOV_VARELEM_ID EQ " + "'variableElement01'";
        Resources resources = statisticalResourcesRestInternalFacadeClientXml.findResources(query, null, null, null, null, null);

        assertEquals(2, resources.getResources().size());
        assertEquals(StatisticalResourcesRestInternalConstants.KIND_RESOURCES, resources.getKind());

        ResourceWithRelatedResources resource = resources.getResources().get(0);

        assertEquals(COLLECTION_1_CODE, resource.getMainResource().getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Collection=agency1:collection1", resource.getMainResource().getUrn());

        assertEquals(StatisticalResourcesRestInternalConstants.KIND_COLLECTION, resource.getMainResource().getKind());
        assertNotNull(resource.getMainResource().getStatisticalOperation());
        assertEquals(
                "http://localhost:8080/statistical-visualizer/visualizer/data.html?resourceType=publication&agencyId=urn&resourceId=siemac:org.siemac.metamac.infomodel.statisticalresources.Collection=agency1:collection1#",

                resource.getMainResource().getVisualizerHtmlLink());
        assertNotNull(resource.getMainResource().getStatisticalOperation().getId());

        assertEquals(2, resource.getRelatedResources().getResources().size());

        resource = resources.getResources().get(1);

        assertEquals(COLLECTION_2_CODE, resource.getMainResource().getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Collection=agency1:collection2", resource.getMainResource().getUrn());

        assertEquals(StatisticalResourcesRestInternalConstants.KIND_COLLECTION, resource.getMainResource().getKind());
        assertNotNull(resource.getMainResource().getStatisticalOperation());
        assertEquals(
                "http://localhost:8080/statistical-visualizer/visualizer/data.html?resourceType=publication&agencyId=urn&resourceId=siemac:org.siemac.metamac.infomodel.statisticalresources.Collection=agency1:collection2#",
                resource.getMainResource().getVisualizerHtmlLink());
        assertNotNull(resource.getMainResource().getStatisticalOperation().getId());

        assertEquals(2, resource.getRelatedResources().getResources().size());

    }

    @Test
    public void testRetrieveResourceExcludingRelatedResourcesWithoutSelectedTerritory() {
        String query = "GEOCOV_VARELEM_ID EQ " + "'variableElement01'";
        String fields = StatisticalResourcesRestConstants.FIELD_EXCLUDE_RELATED_RESOURCES_WITHOUT_SELECTED_CRITERIA;
        Resources resources = statisticalResourcesRestInternalFacadeClientXml.findResources(query, null, null, null, null, fields);

        assertEquals(2, resources.getResources().size());
        assertEquals(StatisticalResourcesRestInternalConstants.KIND_RESOURCES, resources.getKind());

        ResourceWithRelatedResources resource = resources.getResources().get(0);

        assertEquals(COLLECTION_1_CODE, resource.getMainResource().getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Collection=agency1:collection1", resource.getMainResource().getUrn());

        assertEquals(StatisticalResourcesRestInternalConstants.KIND_COLLECTION, resource.getMainResource().getKind());
        assertNotNull(resource.getMainResource().getStatisticalOperation());
        assertEquals(
                "http://localhost:8080/statistical-visualizer/visualizer/data.html?resourceType=publication&agencyId=urn&resourceId=siemac:org.siemac.metamac.infomodel.statisticalresources.Collection=agency1:collection1#",

                resource.getMainResource().getVisualizerHtmlLink());
        assertNotNull(resource.getMainResource().getStatisticalOperation().getId());

        assertEquals(1, resource.getRelatedResources().getResources().size());

        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=agency1:dataset1(01.000)", resource.getRelatedResources().getResources().get(0).getUrn());

        resource = resources.getResources().get(1);

        assertEquals(COLLECTION_2_CODE, resource.getMainResource().getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Collection=agency1:collection2", resource.getMainResource().getUrn());

        assertEquals(StatisticalResourcesRestInternalConstants.KIND_COLLECTION, resource.getMainResource().getKind());
        assertNotNull(resource.getMainResource().getStatisticalOperation());
        assertEquals(
                "http://localhost:8080/statistical-visualizer/visualizer/data.html?resourceType=publication&agencyId=urn&resourceId=siemac:org.siemac.metamac.infomodel.statisticalresources.Collection=agency1:collection2#",
                resource.getMainResource().getVisualizerHtmlLink());
        assertNotNull(resource.getMainResource().getStatisticalOperation().getId());

        assertEquals(1, resource.getRelatedResources().getResources().size());

        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=agency1:dataset2(01.000)", resource.getRelatedResources().getResources().get(0).getUrn());

    }

    @Test
    public void testRetrieveResourceJson() throws Exception {
        String query = "query=GEOCOV_VARELEM_ID%20EQ%20'variableElement01'%20";
        String requestBase = getFindResourcesUri(null, null, null, null, null);
        String[] requestUris = new String[]{requestBase + "?lang=es&" + query, requestBase + ".json?lang=es&" + query, requestBase + "?_type=json&lang=es&" + query};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = StatisticalResourcesRestInternalFacadeV10DatasetsTest.class.getResourceAsStream("/responses/resources/retrieveResource.id1.json");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_JSON, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveResourceExcludingRelatedResourcesWithoutSelectedTerritoryJson() throws Exception {
        String query = "query=GEOCOV_VARELEM_ID%20EQ%20'variableElement01'%20";
        String fields = StatisticalResourcesRestConstants.FIELD_EXCLUDE_RELATED_RESOURCES_WITHOUT_SELECTED_CRITERIA;
        String requestBase = getFindResourcesUri(null, null, null, null, null);

        String[] requestUris = new String[]{requestBase + "?lang=es&" + query + "&fields=" + fields, requestBase + ".json?lang=es&" + query + "&fields=" + fields,
                requestBase + "?_type=json&lang=es&" + query + "&fields=" + fields};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = StatisticalResourcesRestInternalFacadeV10DatasetsTest.class.getResourceAsStream("/responses/resources/retrieveResourceExcludingRelatedResources.id1.json");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_JSON, Status.OK, responseExpected);
        }
    }
}