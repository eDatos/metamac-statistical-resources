package org.siemac.metamac.statistical_resources.rest.external.service.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class StatisticalResourcesRestApiExternalUtils {

    public static final String  TIME_PERIOD_REGEX   = "[\\w-]+";

    private static final String RANGE_PATTERN_REGEX = "~range=(" + TIME_PERIOD_REGEX + ");(" + TIME_PERIOD_REGEX + ")";

    private static final String AFTER_PATTERN_REGEX = "~after=(" + TIME_PERIOD_REGEX + ")";

    private static final String LAST_PATTERN_REGEX  = "~last=(\\d+)";

    public static final String  CODE                = TIME_PERIOD_REGEX;

    public static final Pattern patternDimension    = Pattern.compile("(\\w+):(([\\w\\|-])+)");

    // To truly validate this, patternCode would include org.siemac.edatos.core.common.constants.shared.SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD
    // But currently that would mean to include core-common here... and that would be too heavy now
    public static final Pattern patternCodes        = Pattern.compile("(" + CODE + ")\\|?");

    // Dates after or equals to a date: dim=TIME_PERIOD:~after=1999
    public static final Pattern patternAfter        = Pattern.compile(AFTER_PATTERN_REGEX);

    // Date range, inclusive: dim=TIME_PERIOD:~range=2009;2010
    public static final Pattern patternRange        = Pattern.compile(RANGE_PATTERN_REGEX);

    // Last n elements: dim=TIME_PERIOD:~last=2
    public static final Pattern patternLast         = Pattern.compile(LAST_PATTERN_REGEX);

    protected StatisticalResourcesRestApiExternalUtils() {

    }

    /**
     * Parse dimension expression from request
     * Sample: MOTIVOS_ESTANCIA:000|001|002:ISLAS_DESTINO_PRINCIPAL:005|006
     */
    public static Map<String, List<String>> parseDimensionExpression(String dimExpression) {
        if (StringUtils.isBlank(dimExpression)) {
            return Collections.emptyMap();
        }

        Matcher matcherDimension = patternDimension.matcher(dimExpression);
        Map<String, List<String>> selectedDimension = new HashMap<String, List<String>>();
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
        }
        return selectedDimension;
    }
}
