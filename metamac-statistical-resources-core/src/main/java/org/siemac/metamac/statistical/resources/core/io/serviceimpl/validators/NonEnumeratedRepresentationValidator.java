package org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators;

import static org.siemac.edatos.core.common.constants.shared.RegularExpressionConstants.END;
import static org.siemac.edatos.core.common.constants.shared.RegularExpressionConstants.START;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.TextFormat;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;

public class NonEnumeratedRepresentationValidator {

    protected static final Pattern PATTERN_VALIDATE_OBS = Pattern.compile(START + "[\\-]?\\d+\\.?\\d*" + END);

    public static void checkTextFormatType(TextFormat textFormat, String key, String value, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkFacets(textFormat.getTextType(), textFormat, key, value, exceptions);
    }

    public static void checkExtraValidationForPrimaryMeasure(String key, String value, List<MetamacExceptionItem> exceptions) throws MetamacException {
        Matcher matching = PATTERN_VALIDATE_OBS.matcher(value);

        if (!matching.matches()) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_OBSERVATION_NOT_NUMERIC, value, key));
        }
    }

    /**************************************************************************
     * PRIVATES
     **************************************************************************/

    private static void checkFacets(DataType dataType, TextFormat textFormat, String key, String value, List<MetamacExceptionItem> exceptions) throws MetamacException {

        switch (dataType) {
            // String
            case STRING:
            case ALPHA:
            case ALPHA_NUMERIC:
            case XHTML:
            case INTERNATIONAL_STRING:
                validateText(textFormat, key, value, exceptions);
                break;
            // Numeric
            case NUMERIC:
            case BIG_INTEGER:
            case INTEGER:
            case LONG:
            case SHORT:
            case COUNT:
            case DECIMAL:
            case FLOAT:
            case DOUBLE:
            case INCLUSIVE_VALUE_RANGE:
            case EXCLUSIVE_VALUE_RANGE:
            case INCREMENTAL:
                validateNumeric(textFormat, key, value, exceptions);
                break;
            // Boolean
            case BOOLEAN:
                break;
            case URI:
                validateText(textFormat, key, value, exceptions);
                break;
            // DateTime
            case OBSERVATIONAL_TIME_PERIOD:
            case STANDARD_TIME_PERIOD:
            case BASIC_TIME_PERIOD:
            case GREGORIAN_TIME_PERIOD:
            case GREGORIAN_YEAR:
            case GREGORIAN_YEAR_MONTH:
            case GREGORIAN_DAY:
            case REPORTING_TIME_PERIOD:
            case REPORTING_YEAR:
            case REPORTING_SEMESTER:
            case REPORTING_TRIMESTER:
            case REPORTING_QUARTER:
            case REPORTING_MONTH:
            case REPORTING_WEEK:
            case REPORTING_DAY:
            case DATE_TIME:
            case TIME_RANGE:
            case MONTH:
            case MONTH_DAY:
            case DAY:
            case TIME:
                validateTime(textFormat, key, value, exceptions);
                break;
            default:
                throw new IllegalArgumentException("dataType unsupported " + dataType);
        }
    }

    /**************************************************************************
     * NUMERIC VALIDATION
     **************************************************************************/

    private static void validateNumeric(TextFormat representation, String key, String value, List<MetamacExceptionItem> exceptions) throws MetamacException {

        BigDecimal bigDecimalValue = new BigDecimal(value);

        // isSequence: true | false (indicates a sequentially increasing value) => Validation not implemented.

        // minLength: positive integer (# of characters/digits)
        validateMinLength(representation.getMinLength(), key, value, exceptions);

        // maxLength: positive integer (# of characters/digits)
        validateMaxLength(representation.getMaxLength(), key, value, exceptions);

        // starValue: decimal (for numeric sequence) => Validation not implemented.

        // endValue: decimal (for numeric sequence) => Validation not implemented.

        // interval: decimal (for numeric sequence) => Validation not implemented.

        // timeInterval: duration => Validation not implemented.

        // startTime: BasicTimePeriod (for time range)

        // endTime: BasicTimePeriod (for time range)

        // minValue: decimal (for numeric range)
        validateMinValue(representation.getMinValue(), key, value, bigDecimalValue, exceptions);

        // maxValue: decimal (for numeric range)
        validateMaxValue(representation.getMaxValue(), key, value, bigDecimalValue, exceptions);

        // decimal: Integer (# of digits to right of decimal point)
        validateDecimal(representation.getTextType(), representation.getDecimals(), key, value, exceptions);

        // pattern: (a regular expression, as per W3C XML Schema)
        validatePattern(representation.getPattern(), key, value, exceptions);

        // isMultiLingual: boolean (for specifying text can occur in more than one language) => No for SimpleComponentTextFormatType
    }

    /**************************************************************************
     * TEXT VALIDATION
     **************************************************************************/

    private static void validateText(TextFormat representation, String key, String value, List<MetamacExceptionItem> exceptions) throws MetamacException {

        // isSequence: true | false (indicates a sequentially increasing value) => Validation not implemented.

        // minLength: positive integer (# of characters/digits)
        validateMinLength(representation.getMinLength(), key, value, exceptions);

        // maxLength: positive integer (# of characters/digits)
        validateMaxLength(representation.getMaxLength(), key, value, exceptions);

        // starValue: decimal (for numeric sequence) => for numeric sequence

        // endValue: decimal (for numeric sequence) => for numeric sequence

        // interval: decimal (for numeric sequence) => for numeric sequence

        // timeInterval: duration => for duration

        // startTime: BasicTimePeriod => for time range

        // endTime: BasicTimePeriod => for time range

        // minValue: decimal => for numeric range

        // maxValue: decimal => for numeric range

        // decimal: Integer (# of digits to right of decimal point) => for numerics

        // pattern: (a regular expression, as per W3C XML Schema)
        validatePattern(representation.getPattern(), key, value, exceptions);

        // isMultiLingual: boolean (for specifying text can occur in more than one language) => No for SimpleComponentTextFormatType
    }

    /**************************************************************************
     * TIME VALIDATION
     **************************************************************************/

    private static void validateTime(TextFormat representation, String key, String value, List<MetamacExceptionItem> exceptions) throws MetamacException {

        // Check valid time format.
        if (!SdmxTimeUtils.isObservationalTimePeriod(value)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_OBSERVATION_NONENUMERATED_TEMPORAL_PATTERN, value, key));
            return;
        }

        // isSequence: true | false (indicates a sequentially increasing value) => Validation not implemented.

        // minLength: positive integer (# of characters/digits) => for numeric or text

        // maxLength: positive integer (# of characters/digits) => for numeric or text

        // starValue: decimal (for numeric sequence) => for numeric sequence

        // endValue: decimal (for numeric sequence) => for numeric sequence

        // interval: decimal (for numeric sequence) => for numeric sequence

        // timeInterval: duration => Validation not implemented.

        // startTime = BasicTimePeriod (for time range)
        // endTime = BasicTimePeriod (for time range)
        validateStartTimesAndEndTimes(representation.getStartTime(), representation.getEndTime(), key, value, exceptions);

        // minValue: decimal => for numeric range

        // maxValue: decimal => for numeric range

        // decimal: Integer (# of digits to right of decimal point) => for numeric

        // pattern: (a regular expression, as per W3C XML Schema)
        validatePattern(representation.getPattern(), key, value, exceptions);

        // isMultiLingual = boolean (for specifying text can occur in more than one language) => No for SimpleComponentTextFormatType
    }

    /**************************************************************************
     * VALIDATORS
     **************************************************************************/

    private static void validateMinLength(BigInteger representationMinLength, String key, String value, List<MetamacExceptionItem> exceptions) {
        if (representationMinLength != null && representationMinLength.toString().length() > value.length()) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_OBSERVATION_NONENUMERATED_MINLENGTH, value, representationMinLength.toString().length(), key));
        }
    }

    private static void validateMaxLength(BigInteger representationMaxLength, String key, String value, List<MetamacExceptionItem> exceptions) {
        if (representationMaxLength != null && representationMaxLength.toString().length() > value.length()) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_OBSERVATION_NONENUMERATED_MAXLENGTH, value, representationMaxLength.toString().length(), key));
        }
    }

    private static void validateMinValue(BigDecimal representationMinValue, String key, String value, BigDecimal bigDecimalValue, List<MetamacExceptionItem> exceptions) {
        if (representationMinValue != null) {
            if (representationMinValue.compareTo(bigDecimalValue) == 1) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_OBSERVATION_NONENUMERATED_MINVALUE, value, representationMinValue.toString(), key));
            }
        }
    }

    private static void validateMaxValue(BigDecimal representationMaxValue, String key, String value, BigDecimal bigDecimalValue, List<MetamacExceptionItem> exceptions) {
        if (representationMaxValue != null) {
            if (representationMaxValue.compareTo(bigDecimalValue) == -1) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_OBSERVATION_NONENUMERATED_MAXVALUE, value, representationMaxValue.toString(), key));
            }
        }
    }

    private static void validateDecimal(DataType dataType, BigInteger representationDecimals, String key, String value, List<MetamacExceptionItem> exceptions) {
        if (representationDecimals != null) {
            if (DataType.DECIMAL.equals(dataType) || DataType.FLOAT.equals(dataType) || DataType.DOUBLE.equals(dataType)) {
                String subStrAfterDecimalPoint = StringUtils.substringAfter(value, ".");
                int numberPlacesOfDecimal = (subStrAfterDecimalPoint == null) ? 0 : subStrAfterDecimalPoint.length();
                if (representationDecimals.compareTo(new BigInteger(String.valueOf(numberPlacesOfDecimal))) != 0) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_OBSERVATION_NONENUMERATED_DECIMAL, value, representationDecimals.toString(), key));
                }
            }
        }
    }

    private static void validatePattern(String representationPattern, String key, String value, List<MetamacExceptionItem> exceptions) {
        if (StringUtils.isNotBlank(representationPattern)) {
            Pattern p = Pattern.compile(representationPattern);
            Matcher m = p.matcher(value);
            if (!m.matches()) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_OBSERVATION_NONENUMERATED_PATTERN, value, representationPattern, key));
            }
        }
    }

    private static void validateStartTimesAndEndTimes(String representationStartTime, String representationEndTime, String key, String value, List<MetamacExceptionItem> exceptions) {
        // startTime: BasicTimePeriod (for time range)
        DateTime[] valueDateTimes = SdmxTimeUtils.calculateDateTimes(value);
        if (representationStartTime != null) {
            DateTime[] representationDateTimes = SdmxTimeUtils.calculateDateTimes(representationStartTime);
            if (representationDateTimes == null || representationDateTimes[0] == null || representationDateTimes[0].isAfter(valueDateTimes[0])) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_OBSERVATION_NONENUMERATED_STARTTIMES, value, representationStartTime, key));
            }
        }

        // endTime: BasicTimePeriod (for time range)
        if (representationEndTime != null) {
            DateTime[] representationDateTimes = SdmxTimeUtils.calculateDateTimes(representationEndTime);
            if (representationDateTimes == null || representationDateTimes[0] == null || representationDateTimes[0].isBefore(valueDateTimes[0])) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_OBSERVATION_NONENUMERATED_ENDTIMES, value, representationEndTime, key));
            }
        }
    }
}
