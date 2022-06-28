package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

public final class DimensionUtils {

    private DimensionUtils() {
    }

    @SuppressWarnings("unchecked")
    public static Map<String, List<String>> intersectValues(Map<String, List<String>> queryDimensions, Map<String, List<String>> selectedDimensions) {
        if (queryDimensions == null) {
            return null;
        }
        if (selectedDimensions == null) {
            return queryDimensions;
        }
        Map<String, List<String>> intersectedDimensionValues = new HashMap<String, List<String>>(queryDimensions.size());
        for (Entry<String, List<String>> queryDimension : queryDimensions.entrySet()) {
            List<String> intersectedValues = null;
            List<String> selectedValues = selectedDimensions.get(queryDimension.getKey());
            if (selectedValues != null) {
                intersectedValues = (List<String>) CollectionUtils.intersection(queryDimension.getValue(), selectedValues);
            } else {
                intersectedValues = queryDimension.getValue();
            }
            intersectedDimensionValues.put(queryDimension.getKey(), intersectedValues);
        }
        return intersectedDimensionValues;
    };
}
