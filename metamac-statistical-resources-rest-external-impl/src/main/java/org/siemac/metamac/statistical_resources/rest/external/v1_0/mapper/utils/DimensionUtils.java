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
    public static Map<String, List<String>> filterDimensions(Map<String, List<String>> availableDimensions, Map<String, List<String>> selectedDimensions) {
        if (availableDimensions == null) {
            return null;
        }
        if (selectedDimensions == null) {
            return availableDimensions;
        }
        Map<String, List<String>> filteredDimensionValues = new HashMap<String, List<String>>(availableDimensions.size());
        for (Entry<String, List<String>> queryDimension : availableDimensions.entrySet()) {
            List<String> intersectedValues = null;
            List<String> selectedValues = selectedDimensions.get(queryDimension.getKey());
            if (selectedValues != null) {
                intersectedValues = (List<String>) CollectionUtils.intersection(queryDimension.getValue(), selectedValues);
            } else {
                intersectedValues = queryDimension.getValue();
            }
            filteredDimensionValues.put(queryDimension.getKey(), intersectedValues);
        }
        return filteredDimensionValues;
    };
}
