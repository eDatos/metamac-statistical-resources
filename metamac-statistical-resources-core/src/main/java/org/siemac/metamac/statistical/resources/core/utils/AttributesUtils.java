package org.siemac.metamac.statistical.resources.core.utils;

import java.util.regex.Pattern;

import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataType;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdAttribute;

public class AttributesUtils {

    private static final Pattern patternDataSeparator = Pattern.compile(" \\| ");

    public static Boolean isMultilingualAttributeTextFormatType(DsdAttribute dsdAttribute) {
        return dsdAttribute.getTextFormatRepresentation() != null && dsdAttribute.getTextFormatRepresentation().getTextType() != null
                && DataType.INTERNATIONAL_STRING.equals(dsdAttribute.getTextFormatRepresentation().getTextType());
    }

    public static String escapeValueToData(String value) {
        if (value == null) {
            return null;
        }
        return patternDataSeparator.matcher(value).replaceAll("\\\\ | \\\\");
    }

    public static String escapeValueForTsv(String value) {
        if (value == null) {
            return null;
        }
        // Escape tabs and newlines that could break TSV structure, then trim whitespace
        return value.replace("\t", " ").replace("\r", " ").replace("\n", " ").trim();
    }
}
