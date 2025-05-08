package org.siemac.metamac.statistical.resources.core.enume.utils;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.time.TimeSdmx;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;

public class IstacTimeUtils {
    /**
     * Guess time granularity of a time value
     */
    public static IstacTimeGranularityCodeEnum guessTimeGranularity(String value) throws MetamacException {
        TimeSdmx parseTime = SdmxTimeUtils.parseTime(value);
        if (parseTime == null) {
            throw new MetamacException(CommonServiceExceptionType.PARAMETER_INCORRECT, value);
        }

        switch (parseTime.getType()) {
            case REPORTING_TIME_PERIOD_YEAR:
                return IstacTimeGranularityCodeEnum.YEARLY;
            case REPORTING_TIME_PERIOD_SEMESTER:
                return IstacTimeGranularityCodeEnum.BIYEARLY;
            case REPORTING_TIME_PERIOD_TRIMESTER:
                // 3 groups of 4 months per year.
                return IstacTimeGranularityCodeEnum.FOUR_MONTHLY;
            case REPORTING_TIME_PERIOD_QUARTER:
                // 4 groups of 3 months per year.
                return IstacTimeGranularityCodeEnum.QUARTERLY;
            case REPORTING_TIME_PERIOD_MONTH:
                return IstacTimeGranularityCodeEnum.MONTHLY;
            case REPORTING_TIME_PERIOD_WEEK:
                return IstacTimeGranularityCodeEnum.WEEKLY;
            case REPORTING_TIME_PERIOD_DAY:
                return IstacTimeGranularityCodeEnum.DAILY;
            case DATETIME:
                return IstacTimeGranularityCodeEnum.HOURLY;
            case GREGORIAN_TIME_YEAR:
                return IstacTimeGranularityCodeEnum.YEARLY;
            case GREGORIAN_TIME_MONTH:
                return IstacTimeGranularityCodeEnum.MONTHLY;
            case GREGORIAN_TIME_DATE:
                return IstacTimeGranularityCodeEnum.DAILY;
            case TIME_RANGE_DATE:
                // NOT SUPPORTED: Possible WorkAround: Using the calculated duration, enclose the time in the upper slot
                break;
            case TIME_RANGE_DATETIME:
                // NOT SUPPORTED: Possible WorkAround: Using the calculated duration, enclose the time in the upper slot
                break;
            default:
                break;
        }
        throw new MetamacException(CommonServiceExceptionType.PARAMETER_INCORRECT, value);
    }
}
