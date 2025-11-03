package org.siemac.metamac.statistical.resources.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;

public class TemporalDimensionUtils {

    public static List<String> calculateEffectiveTemporalDimensionValuesToQuery(QueryVersion queryVersion, List<String> temporalCoverageCodes, List<String> selectionCodes) throws MetamacException {
        List<String> sortedTemporalCoverageCodes = sortTimeListFromRecentToOldest(temporalCoverageCodes);
        QueryTypeEnum type = queryVersion.getType();
        if (QueryTypeEnum.FIXED.equals(type)) {
            // We return exactly the selected codes, but first, we sort them so all three methods (FIXED, AUTOINCREMENTAL and LATEST_DATA) return the same order, equal to the coverage
            return sortTimeListFromRecentToOldest(selectionCodes);
        } else if (QueryTypeEnum.AUTOINCREMENTAL.equals(type)) {
            List<String> effectiveDimensionValues = new ArrayList<>();
            List<String> sortedSelectionCodes = sortTimeListFromRecentToOldest(selectionCodes);

            String latestSelectionCode = sortedSelectionCodes.get(0);
            int indexLatestSelectionCode = sortedTemporalCoverageCodes.indexOf(latestSelectionCode);

            effectiveDimensionValues.addAll(sortedSelectionCodes);
            if (indexLatestSelectionCode >= 0) {
                // add codes added after lastest selected code
                List<String> temporalCodesAddedAfterLatestSelectedCodeString = sortedTemporalCoverageCodes.subList(0, indexLatestSelectionCode);
                effectiveDimensionValues.addAll(0, temporalCodesAddedAfterLatestSelectedCodeString);
            }

            return effectiveDimensionValues;
        } else if (QueryTypeEnum.LATEST_DATA.equals(type)) {
            // return N data
            int codeLastIndexToReturn = Math.min(sortedTemporalCoverageCodes.size(), queryVersion.getLatestDataNumber());
            return sortedTemporalCoverageCodes.subList(0, codeLastIndexToReturn);
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "QueryTypeEnum unsupported: " + queryVersion);
        }
    }

    public static List<String> sortTimeListFromRecentToOldest(List<String> temporalValues) {
        List<String> sortedValues = SdmxTimeUtils.sortTimeList(temporalValues);
        Collections.reverse(sortedValues);
        return sortedValues;
    }
}
