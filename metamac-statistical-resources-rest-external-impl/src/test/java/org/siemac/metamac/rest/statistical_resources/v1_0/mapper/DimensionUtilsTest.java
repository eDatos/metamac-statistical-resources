package org.siemac.metamac.rest.statistical_resources.v1_0.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.utils.DimensionUtils;

public class DimensionUtilsTest {

    private Map<String, List<String>> dimensionsGroup1 = initializeDimensionsGroup1();
    private Map<String, List<String>> dimensionsGroup2 = initializeDimensionsGroup2();
    private Map<String, List<String>> dimensionsGroup3 = initializeDimensionsGroup3();

    @Test
    public void testIntersectValues() {
        assertNull(DimensionUtils.intersectValues(null, null));
        assertNull(DimensionUtils.intersectValues(null, dimensionsGroup2));
        assertEquals(DimensionUtils.intersectValues(dimensionsGroup1, null), dimensionsGroup1);

        Map<String, List<String>> results1intersect2 = new HashMap<String, List<String>>();
        results1intersect2.put("DIM-a", Arrays.asList("a-1", "a-2"));
        results1intersect2.put("DIM-b", Arrays.asList("b-1"));
        results1intersect2.put("DIM-c", Arrays.asList("c-2"));

        assertEquals(DimensionUtils.intersectValues(dimensionsGroup1, dimensionsGroup2), results1intersect2);

        Map<String, List<String>> results1intersect3 = new HashMap<String, List<String>>();
        results1intersect3.put("DIM-a", Arrays.asList("a-2"));
        results1intersect3.put("DIM-b", Arrays.asList("b-1", "b-2"));
        results1intersect3.put("DIM-c", Arrays.asList("c-1", "c-2"));

        assertEquals(DimensionUtils.intersectValues(dimensionsGroup1, dimensionsGroup3), results1intersect3);
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
}
