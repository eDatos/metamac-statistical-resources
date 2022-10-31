package org.siemac.metamac.statistical_resources.rest.common.service.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestApiCommonUtils.parseDimensionExpression;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.calculateEffectiveTemporalSelectionValues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticalResourcesRestImplCommonUtilsTest {

    private final Logger              logger           = LoggerFactory.getLogger(StatisticalResourcesRestImplCommonUtilsTest.class);

    private Map<String, List<String>> dimensionsGroup1 = initializeDimensionsGroup1();
    private Map<String, List<String>> dimensionsGroup2 = initializeDimensionsGroup2();
    private Map<String, List<String>> dimensionsGroup3 = initializeDimensionsGroup3();

    @Test
    public void testFilterDimensionValues() {
        assertNull(StatisticalResourcesRestImplCommonUtils.filterDimensions(null, null));
        assertNull(StatisticalResourcesRestImplCommonUtils.filterDimensions(null, dimensionsGroup2));
        assertEquals(dimensionsGroup1, StatisticalResourcesRestImplCommonUtils.filterDimensions(dimensionsGroup1, null));

        Map<String, List<String>> results1intersect2 = new HashMap<String, List<String>>();
        results1intersect2.put("DIM-a", Arrays.asList("a-1", "a-2"));
        results1intersect2.put("DIM-b", Arrays.asList("b-1"));
        results1intersect2.put("DIM-c", Arrays.asList("c-2"));

        assertEquals(results1intersect2, StatisticalResourcesRestImplCommonUtils.filterDimensions(dimensionsGroup1, dimensionsGroup2));

        Map<String, List<String>> results1intersect3 = new HashMap<String, List<String>>();
        results1intersect3.put("DIM-a", Arrays.asList("a-2"));
        results1intersect3.put("DIM-b", Arrays.asList("b-1", "b-2"));
        results1intersect3.put("DIM-c", Arrays.asList("c-1", "c-2"));

        assertEquals(results1intersect3, StatisticalResourcesRestImplCommonUtils.filterDimensions(dimensionsGroup1, dimensionsGroup3));
    }

    private Map<String, List<String>> initializeDimensionsGroup1() {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("DIM-a", Arrays.asList("a-1", "a-2"));
        map.put("DIM-b", Arrays.asList("b-1", "b-2"));
        map.put("DIM-c", Arrays.asList("c-1", "c-2"));
        return map;
    }

    private Map<String, List<String>> initializeDimensionsGroup2() {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("DIM-a", Arrays.asList("a-1", "a-2", "a-3"));
        map.put("DIM-b", Arrays.asList("b-1"));
        map.put("DIM-c", Arrays.asList("c-2"));
        return map;
    }

    private Map<String, List<String>> initializeDimensionsGroup3() {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("DIM-a", Arrays.asList("a-2"));
        return map;
    }

    @Test
    public void testCalculateEffectiveTemporalSelectionValues1() {
        List<String> temporalCoverageDataset1 = Arrays.asList("2012", "2011", "2010", "2009", "2008", "2007");

        assertEquals(new ArrayList<String>(), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, new ArrayList<String>()));
        assertEquals(Arrays.asList("2000"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("2000")));

        // Range
        assertEquals(Arrays.asList("2009", "2008", "2007"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~range=2006;2009")));

        try {
            // We don´t support ~range=2011;2008 because subList don´t support startIndex > endIndex, but if something changes inside, this is the correct value
            assertEquals(Arrays.asList("2011", "2010", "2009", "2008", "2007"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~range=2011;2008")));
        } catch (Exception e) {
            logger.info("We don´t support ~range=2011;2008 because subList don´t support startIndex > endIndex");
            assertTrue(true);
        }

        assertEquals(Arrays.asList("2009", "2008", "2007"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~range=2006-M12;2009")));
        assertEquals(Arrays.asList("2009", "2008", "2007"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~range=2007-M12;2009")));
        assertEquals(Arrays.asList("2008", "2007"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~range=2007-M12;2009-M01")));
        assertEquals(Arrays.asList("2008", "2007"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~range=2007-01-01;2009-M01")));

        // Last
        assertEquals(Arrays.asList("2012", "2011"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~last=2")));
        assertEquals(Arrays.asList("2012", "2011", "2010", "2009", "2008", "2007"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~last=10")));
        assertEquals(Arrays.asList(), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~last=0")));

        // After
        assertEquals(Arrays.asList("2012", "2011", "2010", "2009"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~after=2009")));
        assertEquals(Arrays.asList("2012", "2011", "2010", "2009"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~after=2009-M01")));
        assertEquals(Arrays.asList(), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~after=2013")));
        assertEquals(Arrays.asList("2012", "2011", "2010", "2009", "2008", "2007"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~after=2005")));

        // Combinations. We allow multiple values, they´ll be additive. EXPECT DUPLICATES on this step, but for performance, we are
        // cleaning them on org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.query.QueriesDo2RestMapperV10Impl.toQueryData
        // on a later step
        assertEquals(Arrays.asList("2012", "2012", "2011", "2009", "2008", "2007"),
                calculateEffectiveTemporalSelectionValues(temporalCoverageDataset1, Arrays.asList("~last=1", "~after=2011", "~range=2008;2009", "2007")));
    }

    @Test
    public void testCalculateEffectiveTemporalSelectionValues2() {
        List<String> temporalCoverageDataset2 = Arrays.asList("2012-01-01", "2010-M01", "2008", "2007-S2");

        // Range
        assertEquals(Arrays.asList("2008", "2007-S2"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset2, Arrays.asList("~range=2006;2009")));

        try {
            // We don´t support ~range=2011;2008 because subList don´t support startIndex > endIndex, but if something changes inside, this is the correct value
            assertEquals(Arrays.asList("2010-M01", "2008"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset2, Arrays.asList("~range=2011;2008")));
        } catch (Exception e) {
            logger.info("We don´t support ~range=2011;2008 because subList don´t support startIndex > endIndex");
            assertTrue(true);
        }

        assertEquals(Arrays.asList("2008", "2007-S2"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset2, Arrays.asList("~range=2006-M12;2009")));
        assertEquals(Arrays.asList("2008", "2007-S2"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset2, Arrays.asList("~range=2007-M12;2009")));
        assertEquals(Arrays.asList("2008", "2007-S2"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset2, Arrays.asList("~range=2007-M12;2009-M01")));
        assertEquals(Arrays.asList("2008", "2007-S2"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset2, Arrays.asList("~range=2007-01-01;2009-M01")));

        // Last
        assertEquals(Arrays.asList("2012-01-01", "2010-M01"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset2, Arrays.asList("~last=2")));
        assertEquals(Arrays.asList("2012-01-01", "2010-M01", "2008", "2007-S2"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset2, Arrays.asList("~last=10")));
        assertEquals(Arrays.asList(), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset2, Arrays.asList("~last=0")));

        // After
        assertEquals(Arrays.asList("2012-01-01", "2010-M01"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset2, Arrays.asList("~after=2009")));
        assertEquals(Arrays.asList("2012-01-01", "2010-M01"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset2, Arrays.asList("~after=2009-M01")));
        assertEquals(Arrays.asList(), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset2, Arrays.asList("~after=2013")));
        assertEquals(Arrays.asList("2012-01-01", "2010-M01", "2008", "2007-S2"), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset2, Arrays.asList("~after=2005")));
    }

    @Test
    public void testCalculateEffectiveTemporalSelectionValues3() {

        List<String> temporalCoverageDataset3 = Arrays.asList("2019-09-30T23:00:00", "2019-09-30T22:00:00", "2019-08-01T01:00:00", "2019-08-01T00:00:00", "2019-06-01T00:00:00");

        // Range
        assertEquals(temporalCoverageDataset3, calculateEffectiveTemporalSelectionValues(temporalCoverageDataset3, Arrays.asList("~range=2018;2019")));
        assertEquals(Arrays.asList("2019-09-30T23:00:00", "2019-09-30T22:00:00", "2019-08-01T01:00:00", "2019-08-01T00:00:00"),
                calculateEffectiveTemporalSelectionValues(temporalCoverageDataset3, Arrays.asList("~range=2019-M07;2019-M09")));
        assertEquals(new ArrayList<String>(), calculateEffectiveTemporalSelectionValues(temporalCoverageDataset3, Arrays.asList("~range=2019-M08;2019-M08")));
        assertEquals(temporalCoverageDataset3, calculateEffectiveTemporalSelectionValues(temporalCoverageDataset3, Arrays.asList("~range=2019-06-01T00:00:00;2019-09-30T23:00:00")));
        assertEquals(Arrays.asList("2019-09-30T23:00:00", "2019-09-30T22:00:00", "2019-08-01T01:00:00", "2019-08-01T00:00:00"),
                calculateEffectiveTemporalSelectionValues(temporalCoverageDataset3, Arrays.asList("~range=2019-06-01T00:00:01;2019-09-30T23:00:00")));
    }

    @Test
    public void testParseDimensionExpression() {
        {
            Map<String, List<String>> parseDimensionExpression = parseDimensionExpression("");

            assertParamExpression(parseDimensionExpression);
        }

        {
            Map<String, List<String>> parseDimensionExpression = parseDimensionExpression(null);

            assertParamExpression(parseDimensionExpression);
        }

        {
            Map<String, List<String>> parseDimensionExpression = parseDimensionExpression("MOTIVOS_ESTANCIA:000|001|002:ISLAS_DESTINO_PRINCIPAL:005|006");

            assertParamExpression(parseDimensionExpression, "MOTIVOS_ESTANCIA", "ISLAS_DESTINO_PRINCIPAL");
            assertCodes(parseDimensionExpression.get("MOTIVOS_ESTANCIA"), "000", "001", "002");
            assertCodes(parseDimensionExpression.get("ISLAS_DESTINO_PRINCIPAL"), "005", "006");
        }

        {
            Map<String, List<String>> parseDimensionExpression = parseDimensionExpression("MEASURE:ABSOLUTE:TIME:2012");

            assertParamExpression(parseDimensionExpression, "MEASURE", "TIME");
            assertCodes(parseDimensionExpression.get("MEASURE"), "ABSOLUTE");
            assertCodes(parseDimensionExpression.get("TIME"), "2012");
        }

        {
            Map<String, List<String>> parsedParamExpression = parseDimensionExpression("MEASURE:ABSOLUTE:TIME:2020-M06");

            assertParamExpression(parsedParamExpression, "MEASURE", "TIME");
            assertCodes(parsedParamExpression.get("MEASURE"), "ABSOLUTE");
            assertCodes(parsedParamExpression.get("TIME"), "2020-M06");
        }

        {
            // REPORTING_YEAR_TYPE - REPORTING_SEMESTER_TYPE - REPORTING_TRIMESTER_TYPE - REPORTING_QUARTER_TYPE
            // @formatter:off
            Map<String, List<String>> parsedParamExpression = parseDimensionExpression(
                    "TIME1:2020-A1|2021-A1|2022-A1:"
                    + "TIME2:2001-S1|2001-S2|2002-S1|2002-S2:"
                    + "TIME3:2002-T1|2002-T2|2002-T3:"
                    + "TIME4:2003-Q1|2003-Q2|2003-Q3|2003-Q4");
            // @formatter:on

            assertParamExpression(parsedParamExpression, "TIME1", "TIME2", "TIME3", "TIME4");
            assertCodes(parsedParamExpression.get("TIME1"), "2020-A1", "2021-A1", "2022-A1");
            assertCodes(parsedParamExpression.get("TIME2"), "2001-S1", "2001-S2", "2002-S1", "2002-S2");
            assertCodes(parsedParamExpression.get("TIME3"), "2002-T1", "2002-T2", "2002-T3");
            assertCodes(parsedParamExpression.get("TIME4"), "2003-Q1", "2003-Q2", "2003-Q3", "2003-Q4");
        }

        {
            // REPORTING_MONTH_TYPE - REPORTING_WEEK_TYPE - REPORTING_DAY_TYPE
            // @formatter:off
            Map<String, List<String>> parsedParamExpression = parseDimensionExpression(
                    "TYPE1:2004-M12|2005-M01|2005-M02|2005-M03|2005-M04|2005-M05:"
                    + "TYPE2:2005-W52|2006-W01|2006-W02|2006-W03|2006-W04:"
                    + "TYPE3:2006-D010|2006-D011|2006-D012|2006-D365");
            // @formatter:on

            assertParamExpression(parsedParamExpression, "TYPE1", "TYPE2", "TYPE3");
            assertCodes(parsedParamExpression.get("TYPE1"), "2004-M12", "2005-M01", "2005-M02", "2005-M03", "2005-M04", "2005-M05");
            assertCodes(parsedParamExpression.get("TYPE2"), "2005-W52", "2006-W01", "2006-W02", "2006-W03", "2006-W04");
            assertCodes(parsedParamExpression.get("TYPE3"), "2006-D010", "2006-D011", "2006-D012", "2006-D365");
        }

        {
            // @formatter:off
            Map<String, List<String>> parsedParamExpression = parseDimensionExpression(
                    "MULTIPLE_TIME_VALUES_B:2020-A1|2021-A1|2022-A1:"
                    + "MULTIPLE_TIME_VALUES_A:005|006:"
                    + "MULTIPLE_TIME_VALUES_A:2006-D010|2006-D011|2006-D012|2006-D365:"
                    + "MULTIPLE_TIME_VALUES_B:000|001|002");
            // @formatter:off

            assertParamExpression(parsedParamExpression, "MULTIPLE_TIME_VALUES_A", "MULTIPLE_TIME_VALUES_B");
            assertCodes(parsedParamExpression.get("MULTIPLE_TIME_VALUES_A"), "2006-D010", "2006-D011", "2006-D012", "2006-D365", "005", "006");
            assertCodes(parsedParamExpression.get("MULTIPLE_TIME_VALUES_B"), "000", "001", "002", "2020-A1", "2021-A1", "2022-A1");
        }

        {
            // ~range - ~last - ~after
            // @formatter:off
            Map<String, List<String>> parsedParamExpression = parseDimensionExpression(
                    "TIME1:~range=2006;2009|~range=2011;2008|~range=2006-M12;2009|~range=2007-M12;2009-M01|~range=2007-01-01;2009-M01:"
                    + "TIME2:~last=2|~last=10|~last=0:"
                    + "TIME3:~after=2009|~after=2009-M01:"
                 // This will appear as infos on the log.
                    + "IGNORE_UNMATCHED_CODES:~invalidvalue|2009");
            // @formatter:on

            assertParamExpression(parsedParamExpression, "TIME1", "TIME2", "TIME3");
            assertCodes(parsedParamExpression.get("TIME1"), "~range=2006;2009", "~range=2011;2008", "~range=2006-M12;2009", "~range=2007-M12;2009-M01", "~range=2007-01-01;2009-M01");
            assertCodes(parsedParamExpression.get("TIME2"), "~last=2", "~last=10", "~last=0");
            assertCodes(parsedParamExpression.get("TIME3"), "~after=2009", "~after=2009-M01");
            // If one value is not valid the whole dimension is ignored!
            assertNull(parsedParamExpression.get("IGNORE_UNMATCHED_CODES"));
        }

    }

    private void assertParamExpression(Map<String, List<String>> parsedParamExpression, String... expectedParamExpressions) {
        assertEquals(expectedParamExpressions.length, parsedParamExpression.size());
        for (String expectedParamExpression : expectedParamExpressions) {
            assertTrue(parsedParamExpression.containsKey(expectedParamExpression));
        }
    }

    private void assertCodes(List<String> parsedCodes, String... expectedParsedCodes) {
        assertEquals(expectedParsedCodes.length, parsedCodes.size());
        for (String expectedParsedCode : expectedParsedCodes) {
            assertTrue(parsedCodes.contains(expectedParsedCode));
        }
    }

}
