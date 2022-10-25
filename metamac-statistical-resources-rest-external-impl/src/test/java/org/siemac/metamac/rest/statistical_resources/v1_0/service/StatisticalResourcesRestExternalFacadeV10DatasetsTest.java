package org.siemac.metamac.rest.statistical_resources.v1_0.service;

import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.DATASET_1_CODE;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.VERSION_1;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.Arrays;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.junit.Test;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Datasets;
import org.siemac.metamac.statistical_resources.rest.external.StatisticalResourcesRestExternalConstants;
import org.siemac.metamac.statistical_resources.rest.external.exception.RestServiceExceptionType;

public class StatisticalResourcesRestExternalFacadeV10DatasetsTest extends StatisticalResourcesRestExternalFacadeV10BaseTest {

    @Test
    public void testFindDatasets() throws Exception {
        Datasets datasets = statisticalResourcesRestExternalFacadeClientXml.findDatasets(null, null, null, null, null);

        assertEquals(4, datasets.getDatasets().size());
        assertEquals(StatisticalResourcesRestExternalConstants.KIND_DATASETS, datasets.getKind());
    }

    @Test
    public void testFindDatasetsXml() throws Exception {
        String requestUri = getFindDatasetsUri(AGENCY_1, DATASET_1_CODE, null, null, null, "es");
        InputStream responseExpected = StatisticalResourcesRestExternalFacadeV10DatasetsTest.class.getResourceAsStream("/responses/datasets/findDatasets.xml");
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveDataset() throws Exception {
        {
            Dataset dataset = statisticalResourcesRestExternalFacadeClientXml.retrieveDataset(AGENCY_1, DATASET_1_CODE, VERSION_1, defaultLanguages, null, null, null);

            assertEquals(DATASET_1_CODE, dataset.getId());
            assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=agency1:dataset1(01.000)", dataset.getUrn());
            assertEquals(StatisticalResourcesRestExternalConstants.KIND_DATASET, dataset.getKind());
            assertEquals("http://data.istac.es/apis/statistical-resources/v1.0/datasets/agency1/dataset1/01.000", dataset.getSelfLink().getHref());
            MetamacRestAsserts.assertEqualsInternationalString("es", "title-dataset1 en Espanol", null, null, dataset.getName());

            assertEquals("GEO_DIM", dataset.getData().getDimensions().getDimensions().get(0).getDimensionId());
            assertEquals("santa-cruz-tenerife", dataset.getData().getDimensions().getDimensions().get(0).getRepresentations().getRepresentations().get(0).getCode());
            assertEquals("lanzarote", dataset.getData().getDimensions().getDimensions().get(0).getRepresentations().getRepresentations().get(12).getCode());

            assertEquals("TIME_PERIOD", dataset.getData().getDimensions().getDimensions().get(1).getDimensionId());
            assertEquals("2011", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(0).getCode());
            assertEquals("2014", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(3).getCode());

            assertEquals("measure01", dataset.getData().getDimensions().getDimensions().get(2).getDimensionId());
            assertEquals("measure01-conceptScheme01-concept01", dataset.getData().getDimensions().getDimensions().get(2).getRepresentations().getRepresentations().get(0).getCode());
            assertEquals("measure01-conceptScheme01-concept05", dataset.getData().getDimensions().getDimensions().get(2).getRepresentations().getRepresentations().get(2).getCode());

            assertEquals("dim01", dataset.getData().getDimensions().getDimensions().get(3).getDimensionId());
            assertEquals("dim01-codelist01-code01", dataset.getData().getDimensions().getDimensions().get(3).getRepresentations().getRepresentations().get(0).getCode());
            assertEquals("dim01-codelist01-code04", dataset.getData().getDimensions().getDimensions().get(3).getRepresentations().getRepresentations().get(2).getCode());
        }
        {
            Dataset dataset = statisticalResourcesRestExternalFacadeClientXml.retrieveDataset(AGENCY_1, DATASET_1_CODE, VERSION_1, defaultLanguages, null, null, null);
            assertNull(dataset.getKeywords());
        }
        {
            String fields = StringUtils.join(Arrays.asList(StatisticalResourcesRestExternalConstants.FIELD_INCLUDE_KEYWORDS), ",");
            Dataset dataset = statisticalResourcesRestExternalFacadeClientXml.retrieveDataset(AGENCY_1, DATASET_1_CODE, VERSION_1, defaultLanguages, fields, null, null);
            assertNotNull(dataset.getKeywords());
        }
        {
            String dims = "GEO_DIM:lanzarote:TIME_PERIOD:2014:measure01:measure01-conceptScheme01-concept05:dim01:dim01-codelist01-code04";
            Dataset dataset = statisticalResourcesRestExternalFacadeClientXml.retrieveDataset(AGENCY_1, DATASET_1_CODE, VERSION_1, defaultLanguages, null, dims, null);

            assertEquals("GEO_DIM", dataset.getData().getDimensions().getDimensions().get(0).getDimensionId());
            assertEquals(BigInteger.valueOf(1), dataset.getData().getDimensions().getDimensions().get(0).getRepresentations().getTotal());
            assertEquals("lanzarote", dataset.getData().getDimensions().getDimensions().get(0).getRepresentations().getRepresentations().get(0).getCode());

            assertEquals("TIME_PERIOD", dataset.getData().getDimensions().getDimensions().get(1).getDimensionId());
            assertEquals(BigInteger.valueOf(1), dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getTotal());
            assertEquals("2014", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(0).getCode());

            assertEquals("measure01", dataset.getData().getDimensions().getDimensions().get(2).getDimensionId());
            assertEquals(BigInteger.valueOf(1), dataset.getData().getDimensions().getDimensions().get(2).getRepresentations().getTotal());
            assertEquals("measure01-conceptScheme01-concept05", dataset.getData().getDimensions().getDimensions().get(2).getRepresentations().getRepresentations().get(0).getCode());

            assertEquals("dim01", dataset.getData().getDimensions().getDimensions().get(3).getDimensionId());
            assertEquals(BigInteger.valueOf(1), dataset.getData().getDimensions().getDimensions().get(3).getRepresentations().getTotal());
            assertEquals("dim01-codelist01-code04", dataset.getData().getDimensions().getDimensions().get(3).getRepresentations().getRepresentations().get(0).getCode());
        }
        {
            String representations = "GEO_DIM[lanzarote]:TIME_PERIOD[2014]:measure01[measure01-conceptScheme01-concept05]:dim01[dim01-codelist01-code04]";
            Dataset dataset = statisticalResourcesRestExternalFacadeClientXml.retrieveDataset(AGENCY_1, DATASET_1_CODE, VERSION_1, defaultLanguages, null, null, representations);

            assertEquals("GEO_DIM", dataset.getData().getDimensions().getDimensions().get(0).getDimensionId());
            assertEquals(BigInteger.valueOf(1), dataset.getData().getDimensions().getDimensions().get(0).getRepresentations().getTotal());
            assertEquals("lanzarote", dataset.getData().getDimensions().getDimensions().get(0).getRepresentations().getRepresentations().get(0).getCode());

            assertEquals("TIME_PERIOD", dataset.getData().getDimensions().getDimensions().get(1).getDimensionId());
            assertEquals(BigInteger.valueOf(1), dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getTotal());
            assertEquals("2014", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(0).getCode());

            assertEquals("measure01", dataset.getData().getDimensions().getDimensions().get(2).getDimensionId());
            assertEquals(BigInteger.valueOf(1), dataset.getData().getDimensions().getDimensions().get(2).getRepresentations().getTotal());
            assertEquals("measure01-conceptScheme01-concept05", dataset.getData().getDimensions().getDimensions().get(2).getRepresentations().getRepresentations().get(0).getCode());

            assertEquals("dim01", dataset.getData().getDimensions().getDimensions().get(3).getDimensionId());
            assertEquals(BigInteger.valueOf(1), dataset.getData().getDimensions().getDimensions().get(3).getRepresentations().getTotal());
            assertEquals("dim01-codelist01-code04", dataset.getData().getDimensions().getDimensions().get(3).getRepresentations().getRepresentations().get(0).getCode());
        }
        {
            String dims = "TIME_PERIOD:~last=1";
            Dataset dataset = statisticalResourcesRestExternalFacadeClientXml.retrieveDataset(AGENCY_1, DATASET_1_CODE, VERSION_1, defaultLanguages, null, dims, null);

            assertEquals("GEO_DIM", dataset.getData().getDimensions().getDimensions().get(0).getDimensionId());
            assertEquals(BigInteger.valueOf(13), dataset.getData().getDimensions().getDimensions().get(0).getRepresentations().getTotal());

            assertEquals("TIME_PERIOD", dataset.getData().getDimensions().getDimensions().get(1).getDimensionId());
            assertEquals(BigInteger.valueOf(1), dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getTotal());
            assertEquals("2014", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(0).getCode());

            assertEquals("measure01", dataset.getData().getDimensions().getDimensions().get(2).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(2).getRepresentations().getTotal());

            assertEquals("dim01", dataset.getData().getDimensions().getDimensions().get(3).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(3).getRepresentations().getTotal());
        }
        {
            String representations = "TIME_PERIOD[~last=1]";
            Dataset dataset = statisticalResourcesRestExternalFacadeClientXml.retrieveDataset(AGENCY_1, DATASET_1_CODE, VERSION_1, defaultLanguages, null, null, representations);

            assertEquals("GEO_DIM", dataset.getData().getDimensions().getDimensions().get(0).getDimensionId());
            assertEquals(BigInteger.valueOf(13), dataset.getData().getDimensions().getDimensions().get(0).getRepresentations().getTotal());

            assertEquals("TIME_PERIOD", dataset.getData().getDimensions().getDimensions().get(1).getDimensionId());
            assertEquals(BigInteger.valueOf(1), dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getTotal());
            assertEquals("2014", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(0).getCode());

            assertEquals("measure01", dataset.getData().getDimensions().getDimensions().get(2).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(2).getRepresentations().getTotal());

            assertEquals("dim01", dataset.getData().getDimensions().getDimensions().get(3).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(3).getRepresentations().getTotal());
        }
        {
            String dims = "TIME_PERIOD:~after=2013";
            Dataset dataset = statisticalResourcesRestExternalFacadeClientXml.retrieveDataset(AGENCY_1, DATASET_1_CODE, VERSION_1, defaultLanguages, null, dims, null);

            assertEquals("GEO_DIM", dataset.getData().getDimensions().getDimensions().get(0).getDimensionId());
            assertEquals(BigInteger.valueOf(13), dataset.getData().getDimensions().getDimensions().get(0).getRepresentations().getTotal());

            assertEquals("TIME_PERIOD", dataset.getData().getDimensions().getDimensions().get(1).getDimensionId());
            assertEquals(BigInteger.valueOf(2), dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getTotal());
            assertEquals("2014", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(0).getCode());
            assertEquals("2013", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(1).getCode());

            assertEquals("measure01", dataset.getData().getDimensions().getDimensions().get(2).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(2).getRepresentations().getTotal());

            assertEquals("dim01", dataset.getData().getDimensions().getDimensions().get(3).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(3).getRepresentations().getTotal());
        }
        {
            String representations = "TIME_PERIOD[~after=2013]";
            Dataset dataset = statisticalResourcesRestExternalFacadeClientXml.retrieveDataset(AGENCY_1, DATASET_1_CODE, VERSION_1, defaultLanguages, null, null, representations);

            assertEquals("GEO_DIM", dataset.getData().getDimensions().getDimensions().get(0).getDimensionId());
            assertEquals(BigInteger.valueOf(13), dataset.getData().getDimensions().getDimensions().get(0).getRepresentations().getTotal());

            assertEquals("TIME_PERIOD", dataset.getData().getDimensions().getDimensions().get(1).getDimensionId());
            assertEquals(BigInteger.valueOf(2), dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getTotal());
            assertEquals("2014", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(0).getCode());
            assertEquals("2013", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(1).getCode());

            assertEquals("measure01", dataset.getData().getDimensions().getDimensions().get(2).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(2).getRepresentations().getTotal());

            assertEquals("dim01", dataset.getData().getDimensions().getDimensions().get(3).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(3).getRepresentations().getTotal());
        }
        {
            String dims = "TIME_PERIOD:~range=2011;2013";
            Dataset dataset = statisticalResourcesRestExternalFacadeClientXml.retrieveDataset(AGENCY_1, DATASET_1_CODE, VERSION_1, defaultLanguages, null, dims, null);

            assertEquals("GEO_DIM", dataset.getData().getDimensions().getDimensions().get(0).getDimensionId());
            assertEquals(BigInteger.valueOf(13), dataset.getData().getDimensions().getDimensions().get(0).getRepresentations().getTotal());

            assertEquals("TIME_PERIOD", dataset.getData().getDimensions().getDimensions().get(1).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getTotal());
            assertEquals("2013", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(0).getCode());
            assertEquals("2012", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(1).getCode());
            assertEquals("2011", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(2).getCode());

            assertEquals("measure01", dataset.getData().getDimensions().getDimensions().get(2).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(2).getRepresentations().getTotal());

            assertEquals("dim01", dataset.getData().getDimensions().getDimensions().get(3).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(3).getRepresentations().getTotal());
        }
        {
            String representations = "TIME_PERIOD[~range=2011;2013]";
            Dataset dataset = statisticalResourcesRestExternalFacadeClientXml.retrieveDataset(AGENCY_1, DATASET_1_CODE, VERSION_1, defaultLanguages, null, null, representations);

            assertEquals("GEO_DIM", dataset.getData().getDimensions().getDimensions().get(0).getDimensionId());
            assertEquals(BigInteger.valueOf(13), dataset.getData().getDimensions().getDimensions().get(0).getRepresentations().getTotal());

            assertEquals("TIME_PERIOD", dataset.getData().getDimensions().getDimensions().get(1).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getTotal());
            assertEquals("2013", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(0).getCode());
            assertEquals("2012", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(1).getCode());
            assertEquals("2011", dataset.getData().getDimensions().getDimensions().get(1).getRepresentations().getRepresentations().get(2).getCode());

            assertEquals("measure01", dataset.getData().getDimensions().getDimensions().get(2).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(2).getRepresentations().getTotal());

            assertEquals("dim01", dataset.getData().getDimensions().getDimensions().get(3).getDimensionId());
            assertEquals(BigInteger.valueOf(3), dataset.getData().getDimensions().getDimensions().get(3).getRepresentations().getTotal());
        }
    }

    @Test
    public void testRetrieveDatasetAnotherLanguage() throws Exception {
        Dataset dataset = statisticalResourcesRestExternalFacadeClientXml.retrieveDataset(AGENCY_1, DATASET_1_CODE, VERSION_1, Arrays.asList("en"), null, null, null);

        MetamacRestAsserts.assertEqualsInternationalString("es", "title-dataset1 en Espanol", "en", "title-dataset1 in English", dataset.getName());
    }

    @Test
    public void testRetrieveDatasetXml() throws Exception {
        String requestBase = getRetrieveDatasetUri(AGENCY_1, DATASET_1_CODE, VERSION_1, null, null);
        String[] requestUris = new String[]{requestBase + "?lang=es", requestBase + ".xml?lang=es", requestBase + "?_type=xml&lang=es"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i] + "&fields=+keywords";
            InputStream responseExpected = StatisticalResourcesRestExternalFacadeV10DatasetsTest.class.getResourceAsStream("/responses/datasets/retrieveDataset.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveDatasetJson() throws Exception {
        String requestBase = getRetrieveDatasetUri(AGENCY_1, DATASET_1_CODE, VERSION_1, null, null);
        String[] requestUris = new String[]{requestBase + "?lang=es", requestBase + ".json?lang=es", requestBase + "?_type=json&lang=es"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i] + "&fields=+keywords";
            InputStream responseExpected = StatisticalResourcesRestExternalFacadeV10DatasetsTest.class.getResourceAsStream("/responses/datasets/retrieveDataset.id1.json");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_JSON, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveDatasetErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = VERSION_1;
        try {
            statisticalResourcesRestExternalFacadeClientXml.retrieveDataset(agencyID, resourceID, version, null, null, null, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(statisticalResourcesRestExternalFacadeClientXml, e);
            assertEquals(RestServiceExceptionType.DATASET_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Dataset " + resourceID + " not found in version " + version + " from Agency " + agencyID, exception.getMessage());
            assertEquals(3, exception.getParameters().getParameters().size());
            assertEquals(resourceID, exception.getParameters().getParameters().get(0));
            assertEquals(version, exception.getParameters().getParameters().get(1));
            assertEquals(agencyID, exception.getParameters().getParameters().get(2));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    public String getRetrieveDatasetUri(String agencyID, String resourceID, String version, String fields, String langs) throws Exception {
        return getRetrieveResourceUri(StatisticalResourcesRestExternalConstants.LINK_SUBPATH_DATASETS, agencyID, resourceID, version, fields, langs);
    }

    public String getFindDatasetsUri(String agencyID, String resourceID, String query, String limit, String offset, String langs) throws Exception {
        return getFindResourcesUri(StatisticalResourcesRestExternalConstants.LINK_SUBPATH_DATASETS, agencyID, resourceID, query, limit, offset, langs);
    }

}