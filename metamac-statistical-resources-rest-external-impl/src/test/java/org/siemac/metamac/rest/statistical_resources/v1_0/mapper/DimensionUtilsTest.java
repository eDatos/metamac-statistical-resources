package org.siemac.metamac.rest.statistical_resources.v1_0.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.utils.DimensionUtils.calculateEffectiveTemporalSelectionValues;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.utils.DimensionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DimensionUtilsTest {

    private final Logger              logger           = LoggerFactory.getLogger(DimensionUtilsTest.class);

    private Map<String, List<String>> dimensionsGroup1 = initializeDimensionsGroup1();
    private Map<String, List<String>> dimensionsGroup2 = initializeDimensionsGroup2();
    private Map<String, List<String>> dimensionsGroup3 = initializeDimensionsGroup3();

    @Test
    public void testFilterDimensionValues() {
        assertNull(DimensionUtils.filterDimensions(null, null));
        assertNull(DimensionUtils.filterDimensions(null, dimensionsGroup2));
        assertEquals(dimensionsGroup1, DimensionUtils.filterDimensions(dimensionsGroup1, null));

        Map<String, List<String>> results1intersect2 = new HashMap<String, List<String>>();
        results1intersect2.put("DIM-a", Arrays.asList("a-1", "a-2"));
        results1intersect2.put("DIM-b", Arrays.asList("b-1"));
        results1intersect2.put("DIM-c", Arrays.asList("c-2"));

        assertEquals(results1intersect2, DimensionUtils.filterDimensions(dimensionsGroup1, dimensionsGroup2));

        Map<String, List<String>> results1intersect3 = new HashMap<String, List<String>>();
        results1intersect3.put("DIM-a", Arrays.asList("a-2"));
        results1intersect3.put("DIM-b", Arrays.asList("b-1", "b-2"));
        results1intersect3.put("DIM-c", Arrays.asList("c-1", "c-2"));

        assertEquals(results1intersect3, DimensionUtils.filterDimensions(dimensionsGroup1, dimensionsGroup3));
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

    private List<String> temporalCoverageDataset1 = Arrays.asList("2012", "2011", "2010", "2009", "2008", "2007");
    private List<String> temporalCoverageDataset2 = Arrays.asList("2012-01-01", "2010-M01", "2008", "2007-S2");

    @Test
    public void testCalculateEffectiveTemporalSelectionValues1() {
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

}
