package org.siemac.metamac.statistical_resources.rest.common.v1_0.mapper.utils;

import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestApiCommonUtils.patternAfter;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestApiCommonUtils.patternLast;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestApiCommonUtils.patternRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;

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
    }

    public static List<String> calculateEffectiveTemporalSelectionValues(List<String> temporalCoverageValues, List<String> selectedValues) {

        ArrayList<String> results = new ArrayList<String>();
        for (String value : selectedValues) {
            Matcher matcherAfter = patternAfter.matcher(value);
            if (matcherAfter.matches()) {
                String startRange = matcherAfter.group(1);

                results.addAll(extractTemporalRangeValues(temporalCoverageValues, startRange, null));

                continue;
            }

            Matcher matcherLast = patternLast.matcher(value);
            if (matcherLast.matches()) {
                int lastN = Integer.parseInt(matcherLast.group(1));

                // return N data
                int codeLastIndexToReturn = -1;
                if (temporalCoverageValues.size() < lastN) {
                    codeLastIndexToReturn = temporalCoverageValues.size(); // there is not N data, so return all
                } else {
                    codeLastIndexToReturn = lastN;
                }
                results.addAll(temporalCoverageValues.subList(0, codeLastIndexToReturn));
                continue;
            }

            Matcher matcherRange = patternRange.matcher(value);
            if (matcherRange.matches()) {
                String startRange = matcherRange.group(1);
                String endRange = matcherRange.group(2);

                results.addAll(extractTemporalRangeValues(temporalCoverageValues, startRange, endRange));

                continue;
            }

            results.add(value);
        }

        return results;
    }

    private static List<String> extractTemporalRangeValues(List<String> temporalCoverageValues, String startRange, String endRange) {
        // TemporalCoverages come sorted from newest to oldest (2012, 2011, 2010...)

        List<String> partialResults = new ArrayList<String>(temporalCoverageValues);

        if (startRange == null) {
            startRange = temporalCoverageValues.get(temporalCoverageValues.size() - 1);
        }
        if (endRange == null) {
            endRange = temporalCoverageValues.get(0);
        }

        if (!partialResults.contains(startRange)) {
            partialResults.add(startRange);
        }
        if (!partialResults.contains(endRange)) {
            partialResults.add(endRange);
        }

        // sortTimeList sorts from oldest to newest
        partialResults = SdmxTimeUtils.sortTimeList(partialResults);

        int startIndex = partialResults.indexOf(startRange);
        int endIndex = partialResults.indexOf(endRange) + 1; // sublist toIndex is exclusive
        partialResults = partialResults.subList(startIndex, endIndex);

        if (!temporalCoverageValues.contains(startRange)) {
            partialResults.remove(startRange);
        }
        if (!temporalCoverageValues.contains(endRange)) {
            partialResults.remove(endRange);
        }

        // We reverse the results to conserve the original newest to oldest order
        Collections.reverse(partialResults);

        return partialResults;
    }

    public static boolean isTemporalDimension(String dimensionId) {
        return StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID.equals(dimensionId);
    }
}
