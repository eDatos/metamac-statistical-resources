package org.siemac.metamac.statistical_resources.rest.common.service.utils;

import static org.siemac.metamac.core.common.util.rest.RequestUtil.LAST_PATTERN_REGEX;
import static org.siemac.metamac.core.common.util.rest.RequestUtil.parseFields;
import static org.siemac.metamac.core.common.util.rest.RequestUtil.removeCapturing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.statistical_resources.rest.common.StatisticalResourcesRestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticalResourcesRestApiCommonUtils {

    private final static Logger  logger                = LoggerFactory.getLogger(StatisticalResourcesRestApiCommonUtils.class);

    // To truly validate this, TIME_PERIOD_REGEX would be org.siemac.edatos.core.common.constants.shared.SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD
    // But currently that would mean to include core-common here... and that would be too heavy now
    private static final String  TIME_PERIOD_REGEX     = "[\\w-]+";

    private static final String  NON_TIME_PERIOD_REGEX = "[\\w-]+";

    private static final String  RANGE_PATTERN_REGEX   = "~range=(" + TIME_PERIOD_REGEX + ");(" + TIME_PERIOD_REGEX + ")";

    private static final String  AFTER_PATTERN_REGEX   = "~after=(" + TIME_PERIOD_REGEX + ")";

    private static final String  CODE                  = removeCapturing(RANGE_PATTERN_REGEX) + "|" + removeCapturing(AFTER_PATTERN_REGEX) + "|" + removeCapturing(LAST_PATTERN_REGEX) + "|"
            + NON_TIME_PERIOD_REGEX + "|" + TIME_PERIOD_REGEX;

    private static final Pattern patternDimension      = Pattern.compile("(\\w+):((" + CODE + "|" + "\\|" + ")+):?");

    private static final Pattern patternCodes          = Pattern.compile("(" + CODE + ")\\|?");

    private static final Pattern patternDataSeparator  = Pattern.compile(" \\| ");

    protected StatisticalResourcesRestApiCommonUtils() {

    }

    /**
     * Parse dimension expression from request
     * Sample: MOTIVOS_ESTANCIA:000|001|002:ISLAS_DESTINO_PRINCIPAL:005|006
     */
    @Deprecated
    public static Map<String, List<String>> parseDimensionExpression(String dimExpression) {
        if (StringUtils.isBlank(dimExpression)) {
            return Collections.emptyMap();
        }

        Matcher matcherDimension = patternDimension.matcher(dimExpression);
        Map<String, List<String>> selectedDimension = new HashMap<String, List<String>>();
        String unmatchedText = dimExpression;
        while (matcherDimension.find()) {
            String dimensionIdentifier = matcherDimension.group(1);
            String codes = matcherDimension.group(2);
            Matcher matcherCode = patternCodes.matcher(codes);
            while (matcherCode.find()) {
                List<String> codeDimensions = selectedDimension.get(dimensionIdentifier);
                if (codeDimensions == null) {
                    codeDimensions = new ArrayList<String>();
                    selectedDimension.put(dimensionIdentifier, codeDimensions);
                }
                String codeIdentifier = matcherCode.group(1);
                codeDimensions.add(codeIdentifier);
            }
            unmatchedText = unmatchedText.replace(matcherDimension.group(0), "");
        }
        if (unmatchedText.length() > 0) {
            logger.info(String.format("The following text (%s) was unmatched when parsing (%s)", unmatchedText, dimExpression));
        }
        return selectedDimension;
    }

    public static Set<String> parseFieldsStatisticalResources(String fieldsParam) {
        Set<String> validFields = new HashSet<>();
        validFields.add(StatisticalResourcesRestConstants.FIELD_EXCLUDE_METADATA);
        validFields.add(StatisticalResourcesRestConstants.FIELD_EXCLUDE_DATA);
        validFields.add(StatisticalResourcesRestConstants.FIELD_INCLUDE_DIMENSION_DESCRIPTION);
        validFields.add(StatisticalResourcesRestConstants.FIELD_INCLUDE_KEYWORDS);
        return parseFields(fieldsParam, validFields);
    }

    public static Set<String> parseFieldsStatisticalResourcesListEndpoints(String fieldsParam) {
        Set<String> validFields = new HashSet<>();
        validFields.add(StatisticalResourcesRestConstants.FIELD_INCLUDE_STATISTICAL_OPERATION);
        return parseFields(fieldsParam, validFields);
    }

    public static String escapeValueToData(String value) {
        if (value == null) {
            return null;
        }
        return patternDataSeparator.matcher(value).replaceAll("\\\\ | \\\\");
    }

}
