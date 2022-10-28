package org.siemac.metamac.statistical_resources.rest.common.service.utils;

import static org.siemac.metamac.core.common.util.rest.RequestUtil.PATTERN_AFTER;
import static org.siemac.metamac.core.common.util.rest.RequestUtil.PATTERN_LAST;
import static org.siemac.metamac.core.common.util.rest.RequestUtil.PATTERN_RANGE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.ListUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;
import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StatisticalResourcesRestImplCommonUtils extends StatisticalResourcesRestApiCommonUtils {

    private final static Logger logger = LoggerFactory.getLogger(StatisticalResourcesRestApiCommonUtils.class);

    private StatisticalResourcesRestImplCommonUtils() {
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
                intersectedValues = ListUtils.intersection(queryDimension.getValue(), selectedValues);
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
            Matcher matcherAfter = PATTERN_AFTER.matcher(value);
            if (matcherAfter.matches()) {
                String startRange = matcherAfter.group(1);

                results.addAll(extractTemporalRangeValues(temporalCoverageValues, startRange, null));

                continue;
            }

            Matcher matcherLast = PATTERN_LAST.matcher(value);
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

            Matcher matcherRange = PATTERN_RANGE.matcher(value);
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

    /**
     * Throws response error, logging exception
     */
    public static RestException manageException(Exception e) {
        logger.error("Error", e);
        if (e instanceof RestException) {
            return (RestException) e;
        } else {
            // do not show information details about exception to user
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestCommonServiceExceptionType.UNKNOWN);
            return new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static DateTime isDateAfterNowSetNull(DateTime checkValidTo) {
        if (checkValidTo == null || checkValidTo.isAfterNow()) {
            return null;
        } else {
            return checkValidTo;
        }
    }

}
