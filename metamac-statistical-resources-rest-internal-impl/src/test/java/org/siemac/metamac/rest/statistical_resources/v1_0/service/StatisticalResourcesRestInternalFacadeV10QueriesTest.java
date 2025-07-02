package org.siemac.metamac.rest.statistical_resources.v1_0.service;

import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.QUERY_1_CODE;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.QUERY_2_CODE;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.QUERY_3_CODE;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.QUERY_4_CODE;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.Arrays;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.ConnectionType;
import org.junit.Test;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Query;
import org.siemac.metamac.statistical_resources.rest.common.impl.test.utils.CommonDatasetUtils;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentations;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedDimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Queries;
import org.siemac.metamac.statistical_resources.rest.internal.StatisticalResourcesRestInternalConstants;
import org.siemac.metamac.statistical_resources.rest.internal.exception.RestServiceExceptionType;

public class StatisticalResourcesRestInternalFacadeV10QueriesTest extends StatisticalResourcesRestInternalFacadeV10BaseTest {

    @Test
    public void testFindQueries() throws Exception {
        Queries queries = statisticalResourcesRestInternalFacadeClientXml.findQueries(null, null, null, null, null, null);

        assertEquals(4, queries.getQueries().size());
        assertEquals(StatisticalResourcesRestInternalConstants.KIND_QUERIES, queries.getKind());
    }

    @Test
    public void testFindQueriesXml() throws Exception {
        String requestUri = getFindQueriesUri(AGENCY_1, null, null, null, "es");
        InputStream responseExpected = StatisticalResourcesRestInternalFacadeV10QueriesTest.class.getResourceAsStream("/responses/queries/findQueries.xml");
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindQueriesXmlWithQueryConditionsMatcherExternal() throws Exception {
        // Use argument Matcher for this test;
        mockitoMockConfig.setApplyArgumentMatcher(false); // Skip testing
        { // All data: specific with general format (StructureSpecificData)
            WebClient create = WebClient.create(baseApi);

            ClientConfiguration config = WebClient.getConfig(create);
            HTTPConduit conduit = config.getHttpConduit();
            conduit.getClient().setConnectionTimeout(3000000);
            conduit.getClient().setReceiveTimeout(7000000);
            conduit.getClient().setConnection(ConnectionType.CLOSE);

            create.path("queries/agency1");
            create.query("lang", "es");
            create.query("query", "RELATED_DATASET_URN EQ 'URN_TEST'");
            Response response = create.get();
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        }
        mockitoMockConfig.resetToDefaultConfiguration();
    }

    @Test
    public void testRetrieveQuery() throws Exception {
        Query query = statisticalResourcesRestInternalFacadeClientXml.retrieveQuery(AGENCY_1, QUERY_1_CODE, defaultLanguages, null, null, null, null);

        assertEquals(QUERY_1_CODE, query.getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Query=agency1:query1", query.getUrn());
        assertEquals(StatisticalResourcesRestInternalConstants.KIND_QUERY, query.getKind());
        assertEquals("http://data.istac.es/apis/statistical-resources/v1.0/queries/agency1/query1", query.getSelfLink().getHref());
        MetamacRestAsserts.assertEqualsInternationalString("es", "title-query1 en Espanol", null, null, query.getName());
        assertEquals("http://data.istac.es/apis/statistical-resources/v1.0/datasets/agency1/dataset01/01.000", query.getMetadata().getRelatedDataset().getSelfLink().getHref());

        assertEquals(query.getData().getDimensions().getDimensions().get(0).getDimensionId(), "GEO_DIM");
        assertEquals(query.getData().getDimensions().getDimensions().get(0).getRepresentations().getTotal(), BigInteger.valueOf(8));
        assertEquals(query.getData().getDimensions().getDimensions().get(1).getDimensionId(), "TIME_PERIOD");
        assertEquals(query.getData().getDimensions().getDimensions().get(1).getRepresentations().getTotal(), BigInteger.valueOf(2));
        assertEquals(query.getData().getDimensions().getDimensions().get(2).getDimensionId(), "measure01");
        assertEquals(query.getData().getDimensions().getDimensions().get(2).getRepresentations().getTotal(), BigInteger.valueOf(3));
        assertEquals(query.getData().getDimensions().getDimensions().get(3).getDimensionId(), "dim01");
        assertEquals(query.getData().getDimensions().getDimensions().get(3).getRepresentations().getTotal(), BigInteger.valueOf(1));

        {
            // Dimensions and representations order must match
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(1);
            {
                NonEnumeratedDimensionValues dimensionValues = (NonEnumeratedDimensionValues) dimension.getDimensionValues();
                assertEquals("2013", dimensionValues.getValues().get(0).getId());
                assertEquals("2011", dimensionValues.getValues().get(1).getId());
            }
            {
                CodeRepresentations dimensionValues = query.getData().getDimensions().getDimensions().get(1).getRepresentations();
                // Order here must be the same as order of dimensionValues
                assertEquals("2013", dimensionValues.getRepresentations().get(0).getCode());
                assertEquals("2011", dimensionValues.getRepresentations().get(1).getCode());
            }
        }

        // Ultimo elemento de las observaciones
        assertEquals(StringUtils.splitByWholeSeparatorPreserveAllTokens(query.getData().getObservations(), StatisticalResourcesRestInternalConstants.DATA_SEPARATOR).length, 8 * 2 * 3 * 1);
        assertEquals(StringUtils.splitByWholeSeparatorPreserveAllTokens(query.getData().getObservations(), StatisticalResourcesRestInternalConstants.DATA_SEPARATOR)[8 * 2 * 3 * 1 - 1], "403");
    }

    @Test
    public void testRetrieveQueryTsv() throws Exception {
        String requestUri = getRetrieveQueryUri(AGENCY_1, QUERY_1_CODE, null, null);
        InputStream responseExpected = StatisticalResourcesRestInternalFacadeV10DatasetsTest.class.getResourceAsStream("/responses/queries/retrieveQuery.id1.tsv");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, "text/tab-separated-values", Status.OK, responseExpected);

    }

    @Test
    public void testRetrieveQueryTsvDataCheck() throws Exception {
        String requestUri = getRetrieveQueryUri(AGENCY_1, QUERY_1_CODE, null, null);
        CommonDatasetUtils.checkTsvDatasetResponse(requestUri);
    }

    @Test
    public void testRetrieveQueryCsv() throws Exception {

        String requestUri = getRetrieveQueryUri(AGENCY_1, QUERY_1_CODE, null, null);
        InputStream responseExpected = StatisticalResourcesRestInternalFacadeV10DatasetsTest.class.getResourceAsStream("/responses/queries/retrieveQuery.id1.csv");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, "text/csv", Status.OK, responseExpected);

    }

    @Test
    public void testRetrieveQueryCsvDataCheck() throws Exception {
        String requestUri = getRetrieveQueryUri(AGENCY_1, QUERY_1_CODE, null, null);
        CommonDatasetUtils.checkCsvDatasetResponse(requestUri);
    }

    @Test
    public void testRetrieveQueryXlsxDataCheck() throws Exception {
        String requestUri = getRetrieveQueryUri(AGENCY_1, QUERY_1_CODE, null, null);
        // @formatter:off
        String[] expectedValuesFirstRow = {
                null, null,
                "santa-cruz-tenerife en Español", "santa-cruz-tenerife",
                null, null,
                "2013", "2013",
                null, null,
                "measure01-conceptScheme01-concept01 en Español", "measure01-conceptScheme01-concept01",
                null, null,
                "dim01-codelist01-code01 en Español", "dim01-codelist01-code01",
                "19",
                null, null
            };
        // @formatter:on
        CommonDatasetUtils.checkXlsDatasetResponse(requestUri, expectedValuesFirstRow);
    }

    @Test
    public void testRetrieveQueryWithDimensions() throws Exception {
        {
            Query query = statisticalResourcesRestInternalFacadeClientXml.retrieveQuery(AGENCY_1, QUERY_1_CODE, defaultLanguages, null, buildDimParamsForQuery_1(), null, null);

            assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Query=agency1:query1", query.getUrn());

            assertEquals(query.getData().getDimensions().getDimensions().get(0).getDimensionId(), "GEO_DIM");
            assertEquals(query.getData().getDimensions().getDimensions().get(0).getRepresentations().getTotal(), BigInteger.valueOf(1));
            assertEquals(query.getData().getDimensions().getDimensions().get(1).getDimensionId(), "TIME_PERIOD");
            assertEquals(query.getData().getDimensions().getDimensions().get(1).getRepresentations().getTotal(), BigInteger.valueOf(1));
            assertEquals(query.getData().getDimensions().getDimensions().get(2).getDimensionId(), "measure01");
            assertEquals(query.getData().getDimensions().getDimensions().get(2).getRepresentations().getTotal(), BigInteger.valueOf(1));
            assertEquals(query.getData().getDimensions().getDimensions().get(3).getDimensionId(), "dim01");
            assertEquals(query.getData().getDimensions().getDimensions().get(3).getRepresentations().getTotal(), BigInteger.valueOf(1));

            // Ultimo elemento de las observaciones
            assertEquals(StringUtils.splitByWholeSeparatorPreserveAllTokens(query.getData().getObservations(), StatisticalResourcesRestInternalConstants.DATA_SEPARATOR).length, 1);
            assertEquals(StringUtils.splitByWholeSeparatorPreserveAllTokens(query.getData().getObservations(), StatisticalResourcesRestInternalConstants.DATA_SEPARATOR)[0], "421");
        }
        {
            Query query = statisticalResourcesRestInternalFacadeClientXml.retrieveQuery(AGENCY_1, QUERY_1_CODE, defaultLanguages, null, null, buildRepresentationsParamsForQuery_1(), null);

            assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Query=agency1:query1", query.getUrn());

            assertEquals(query.getData().getDimensions().getDimensions().get(0).getDimensionId(), "GEO_DIM");
            assertEquals(query.getData().getDimensions().getDimensions().get(0).getRepresentations().getTotal(), BigInteger.valueOf(1));
            assertEquals(query.getData().getDimensions().getDimensions().get(1).getDimensionId(), "TIME_PERIOD");
            assertEquals(query.getData().getDimensions().getDimensions().get(1).getRepresentations().getTotal(), BigInteger.valueOf(1));
            assertEquals(query.getData().getDimensions().getDimensions().get(2).getDimensionId(), "measure01");
            assertEquals(query.getData().getDimensions().getDimensions().get(2).getRepresentations().getTotal(), BigInteger.valueOf(1));
            assertEquals(query.getData().getDimensions().getDimensions().get(3).getDimensionId(), "dim01");
            assertEquals(query.getData().getDimensions().getDimensions().get(3).getRepresentations().getTotal(), BigInteger.valueOf(1));

            // Ultimo elemento de las observaciones
            assertEquals(StringUtils.splitByWholeSeparatorPreserveAllTokens(query.getData().getObservations(), StatisticalResourcesRestInternalConstants.DATA_SEPARATOR).length, 1);
            assertEquals(StringUtils.splitByWholeSeparatorPreserveAllTokens(query.getData().getObservations(), StatisticalResourcesRestInternalConstants.DATA_SEPARATOR)[0], "421");
        }
    }

    private String buildDimParamsForQuery_1() {
        //@formatter:off
        return "GEO_DIM" + ":" + "fuerteventura" + ":"
                + "dim01" + ":" + "dim01-codelist01-code01" + ":"
                + "measure01" + ":" + "measure01-conceptScheme01-concept05" + ":"
                + "TIME_PERIOD" + ":" + "2013" + ":"
                ;
        //@formatter:on
    }

    private String buildRepresentationsParamsForQuery_1() {
        //@formatter:off
        return "GEO_DIM" + "[" + "fuerteventura" + "]" + ":"
                + "dim01" + "[" + "dim01-codelist01-code01" + "]" + ":"
                + "measure01" + "[" + "measure01-conceptScheme01-concept05" + "]" + ":"
                + "TIME_PERIOD" + "[" + "2013" + "]"
                ;
        //@formatter:on
    }

    @Test
    public void testRetrieveQueryWithDatasetGlobal() throws Exception {
        Query query = statisticalResourcesRestInternalFacadeClientXml.retrieveQuery(AGENCY_1, QUERY_2_CODE, defaultLanguages, null, null, null, null);

        assertEquals(QUERY_2_CODE, query.getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Query=agency1:query2", query.getUrn());
        assertEquals("http://data.istac.es/apis/statistical-resources/v1.0/datasets/agency1/dataset01/~latest", query.getMetadata().getRelatedDataset().getSelfLink().getHref());
    }

    @Test
    public void testRetrieveQueryAnotherLanguage() throws Exception {
        Query query = statisticalResourcesRestInternalFacadeClientXml.retrieveQuery(AGENCY_1, QUERY_1_CODE, Arrays.asList("en"), null, null, null, null);

        MetamacRestAsserts.assertEqualsInternationalString("es", "title-query1 en Espanol", "en", "title-query1 in English", query.getName());
    }

    @Test
    public void testRetrieveQueryXml() throws Exception {
        String requestBase = getRetrieveQueryUri(AGENCY_1, QUERY_1_CODE, null, null);
        String[] requestUris = new String[]{requestBase + "?lang=es", requestBase + ".xml?lang=es", requestBase + "?_type=xml&lang=es"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = StatisticalResourcesRestInternalFacadeV10QueriesTest.class.getResourceAsStream("/responses/queries/retrieveQuery.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveQueryJson() throws Exception {
        String requestBase = getRetrieveQueryUri(AGENCY_1, QUERY_1_CODE, null, null);
        String[] requestUris = new String[]{requestBase + "?lang=es", requestBase + ".json?lang=es", requestBase + "?_type=json&lang=es"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = StatisticalResourcesRestInternalFacadeV10QueriesTest.class.getResourceAsStream("/responses/queries/retrieveQuery.id1.json");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_JSON, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveQueryFixed() throws Exception {
        Query query = statisticalResourcesRestInternalFacadeClientXml.retrieveQuery(AGENCY_1, QUERY_1_CODE, null, null, null, null, null);

        // Metadata
        assertEquals(4, query.getMetadata().getDimensions().getDimensions().size());
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(0);
            assertEquals("GEO_DIM", dimension.getId());
            assertTrue(dimension.getDimensionValues() instanceof EnumeratedDimensionValues);
            {
                EnumeratedDimensionValues dimensionValues = (EnumeratedDimensionValues) dimension.getDimensionValues();
                assertEquals(8, dimensionValues.getValues().size());
                int i = 0;
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("santa-cruz-tenerife", dimensionValue.getId());
                    assertEquals(null, dimensionValue.getVisualisationParent());
                }
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("tenerife", dimensionValue.getId());
                    assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agency01:GEO_DIM-codelist01(01.000).santa-cruz-tenerife", dimensionValue.getVisualisationParent());
                }
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("la-laguna", dimensionValue.getId());
                    assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agency01:GEO_DIM-codelist01(01.000).tenerife", dimensionValue.getVisualisationParent());
                }
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("santa-cruz", dimensionValue.getId());
                    assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agency01:GEO_DIM-codelist01(01.000).tenerife", dimensionValue.getVisualisationParent());
                }
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("la-palma", dimensionValue.getId());
                    assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agency01:GEO_DIM-codelist01(01.000).santa-cruz-tenerife", dimensionValue.getVisualisationParent());
                }
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("los-llanos-de-aridane", dimensionValue.getId());
                    assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agency01:GEO_DIM-codelist01(01.000).la-palma", dimensionValue.getVisualisationParent());
                }
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("las-palmas-gran-canaria", dimensionValue.getId());
                    assertEquals(null, dimensionValue.getVisualisationParent());
                }
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("fuerteventura", dimensionValue.getId());
                    assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agency01:GEO_DIM-codelist01(01.000).las-palmas-gran-canaria", dimensionValue.getVisualisationParent());
                }
                assertEquals(dimensionValues.getValues().size(), i);
            }
        }
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(1);
            assertEquals("TIME_PERIOD", dimension.getId());
            assertTrue(dimension.getDimensionValues() instanceof NonEnumeratedDimensionValues);
            {
                NonEnumeratedDimensionValues dimensionValues = (NonEnumeratedDimensionValues) dimension.getDimensionValues();
                assertEquals(2, dimensionValues.getValues().size());
                assertEquals("2013", dimensionValues.getValues().get(0).getId());
                assertEquals("2011", dimensionValues.getValues().get(1).getId());
            }
        }
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(2);
            assertEquals("measure01", dimension.getId());
            assertTrue(dimension.getDimensionValues() instanceof EnumeratedDimensionValues);
            {
                EnumeratedDimensionValues dimensionValues = (EnumeratedDimensionValues) dimension.getDimensionValues();
                assertEquals(3, dimensionValues.getValues().size());
                assertEquals("measure01-conceptScheme01-concept01", dimensionValues.getValues().get(0).getId());
                assertEquals("measure01-conceptScheme01-concept02", dimensionValues.getValues().get(1).getId());
                assertEquals("measure01-conceptScheme01-concept05", dimensionValues.getValues().get(2).getId());
            }
        }
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(3);
            assertEquals("dim01", dimension.getId());
            assertTrue(dimension.getDimensionValues() instanceof EnumeratedDimensionValues);
            {
                EnumeratedDimensionValues dimensionValues = (EnumeratedDimensionValues) dimension.getDimensionValues();
                assertEquals(1, dimensionValues.getValues().size());
                assertEquals("dim01-codelist01-code01", dimensionValues.getValues().get(0).getId());
            }
        }

        // Data
        assertEquals(48, StringUtils.splitByWholeSeparatorPreserveAllTokens(query.getData().getObservations(), StatisticalResourcesRestInternalConstants.DATA_SEPARATOR).length);
    }

    @Test
    public void testRetrieveQueryFixedChangingParentVisualisations() throws Exception {
        Query query = statisticalResourcesRestInternalFacadeClientXml.retrieveQuery(AGENCY_1, QUERY_4_CODE, null, null, null, null, null);

        // Metadata
        assertEquals(4, query.getMetadata().getDimensions().getDimensions().size());
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(0);
            assertEquals("GEO_DIM", dimension.getId());
            assertTrue(dimension.getDimensionValues() instanceof EnumeratedDimensionValues);
            {
                EnumeratedDimensionValues dimensionValues = (EnumeratedDimensionValues) dimension.getDimensionValues();
                assertEquals(7, dimensionValues.getValues().size());
                int i = 0;
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("santa-cruz-tenerife", dimensionValue.getId());
                    assertEquals(null, dimensionValue.getVisualisationParent());
                }
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("tenerife", dimensionValue.getId());
                    assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agency01:GEO_DIM-codelist01(01.000).santa-cruz-tenerife", dimensionValue.getVisualisationParent());
                }
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("la-laguna", dimensionValue.getId());
                    assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agency01:GEO_DIM-codelist01(01.000).tenerife", dimensionValue.getVisualisationParent());
                }
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("santa-cruz", dimensionValue.getId());
                    assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agency01:GEO_DIM-codelist01(01.000).tenerife", dimensionValue.getVisualisationParent());
                }
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("los-llanos-de-aridane", dimensionValue.getId());
                    assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agency01:GEO_DIM-codelist01(01.000).santa-cruz-tenerife", dimensionValue.getVisualisationParent());
                }
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("las-palmas-gran-canaria", dimensionValue.getId());
                    assertEquals(null, dimensionValue.getVisualisationParent());
                }
                {
                    EnumeratedDimensionValue dimensionValue = dimensionValues.getValues().get(i++);
                    assertEquals("fuerteventura", dimensionValue.getId());
                    assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agency01:GEO_DIM-codelist01(01.000).las-palmas-gran-canaria", dimensionValue.getVisualisationParent());
                }
                assertEquals(dimensionValues.getValues().size(), i);
            }
        }
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(1);
            assertEquals("TIME_PERIOD", dimension.getId());
        }
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(2);
            assertEquals("measure01", dimension.getId());
        }
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(3);
            assertEquals("dim01", dimension.getId());
        }

        // Data
        assertEquals(42, StringUtils.splitByWholeSeparatorPreserveAllTokens(query.getData().getObservations(), StatisticalResourcesRestInternalConstants.DATA_SEPARATOR).length);
    }

    @Test
    public void testRetrieveQueryAutoincremental() throws Exception {
        Query query = statisticalResourcesRestInternalFacadeClientXml.retrieveQuery(AGENCY_1, QUERY_2_CODE, null, null, null, null, null);

        // Metadata
        assertEquals(4, query.getMetadata().getDimensions().getDimensions().size());
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(0);
            assertEquals("GEO_DIM", dimension.getId());
        }
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(1);
            assertEquals("TIME_PERIOD", dimension.getId());
            assertTrue(dimension.getDimensionValues() instanceof NonEnumeratedDimensionValues);
            {
                NonEnumeratedDimensionValues dimensionValues = (NonEnumeratedDimensionValues) dimension.getDimensionValues();
                assertEquals(4, dimensionValues.getValues().size());
                assertEquals("2014", dimensionValues.getValues().get(0).getId());
                assertEquals("2013", dimensionValues.getValues().get(1).getId());
                assertEquals("2012", dimensionValues.getValues().get(2).getId());
                assertEquals("2011", dimensionValues.getValues().get(3).getId());
            }
        }
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(2);
            assertEquals("measure01", dimension.getId());
        }
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(3);
            assertEquals("dim01", dimension.getId());
        }

        // Data
        assertEquals(24, StringUtils.splitByWholeSeparatorPreserveAllTokens(query.getData().getObservations(), StatisticalResourcesRestInternalConstants.DATA_SEPARATOR).length);

        {
            DimensionRepresentation dimension = query.getData().getDimensions().getDimensions().get(1);
            assertEquals("TIME_PERIOD", dimension.getDimensionId());
            assertTrue(dimension.getRepresentations() instanceof CodeRepresentations);
            {
                CodeRepresentations dimensionValues = dimension.getRepresentations();
                assertEquals(BigInteger.valueOf(4), dimensionValues.getTotal());
                // Order here must be the same as order of dimensionValues
                assertEquals("2014", dimensionValues.getRepresentations().get(0).getCode());
                assertEquals("2013", dimensionValues.getRepresentations().get(1).getCode());
                assertEquals("2012", dimensionValues.getRepresentations().get(2).getCode());
                assertEquals("2011", dimensionValues.getRepresentations().get(3).getCode());

            }
        }
    }

    @Test
    public void testRetrieveQueryLatestData() throws Exception {
        Query query = statisticalResourcesRestInternalFacadeClientXml.retrieveQuery(AGENCY_1, QUERY_3_CODE, null, null, null, null, null);

        // Metadata
        assertEquals(4, query.getMetadata().getDimensions().getDimensions().size());
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(0);
            assertEquals("GEO_DIM", dimension.getId());
        }
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(1);
            assertEquals("TIME_PERIOD", dimension.getId());
            assertTrue(dimension.getDimensionValues() instanceof NonEnumeratedDimensionValues);
            {
                NonEnumeratedDimensionValues dimensionValues = (NonEnumeratedDimensionValues) dimension.getDimensionValues();
                assertEquals(2, dimensionValues.getValues().size());
                assertEquals("2014", dimensionValues.getValues().get(0).getId());
                assertEquals("2013", dimensionValues.getValues().get(1).getId());
            }
        }
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(2);
            assertEquals("measure01", dimension.getId());
        }
        {
            Dimension dimension = query.getMetadata().getDimensions().getDimensions().get(3);
            assertEquals("dim01", dimension.getId());
        }

        // Data
        assertEquals(12, StringUtils.splitByWholeSeparatorPreserveAllTokens(query.getData().getObservations(), StatisticalResourcesRestInternalConstants.DATA_SEPARATOR).length);

        {
            DimensionRepresentation dimension = query.getData().getDimensions().getDimensions().get(1);
            assertEquals("TIME_PERIOD", dimension.getDimensionId());
            assertTrue(dimension.getRepresentations() instanceof CodeRepresentations);
            {
                CodeRepresentations dimensionValues = dimension.getRepresentations();
                assertEquals(BigInteger.valueOf(2), dimensionValues.getTotal());
                // Order here must be the same as order of dimensionValues
                assertEquals("2014", dimensionValues.getRepresentations().get(0).getCode());
                assertEquals("2013", dimensionValues.getRepresentations().get(1).getCode());
            }
        }
    }

    @Test
    public void testRetrieveQueryErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        try {
            statisticalResourcesRestInternalFacadeClientXml.retrieveQuery(agencyID, resourceID, null, null, null, null, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(statisticalResourcesRestInternalFacadeClientXml, e);
            assertEquals(RestServiceExceptionType.QUERY_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Query " + resourceID + " not found from Agency " + agencyID, exception.getMessage());
            assertEquals(2, exception.getParameters().getParameters().size());
            assertEquals(resourceID, exception.getParameters().getParameters().get(0));
            assertEquals(agencyID, exception.getParameters().getParameters().get(1));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    public String getRetrieveQueryUri(String agencyID, String resourceID, String fields, String langs) throws Exception {
        return getRetrieveResourceUri(StatisticalResourcesRestInternalConstants.LINK_SUBPATH_QUERIES, agencyID, resourceID, null, fields, langs);
    }

    public String getFindQueriesUri(String agencyID, String query, String limit, String offset, String langs) throws Exception {
        return getFindResourcesUri(StatisticalResourcesRestInternalConstants.LINK_SUBPATH_QUERIES, agencyID, null, query, limit, offset, langs);
    }

}