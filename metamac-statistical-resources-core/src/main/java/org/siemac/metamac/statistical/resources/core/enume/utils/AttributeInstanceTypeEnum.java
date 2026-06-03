package org.siemac.metamac.statistical.resources.core.enume.utils;

/**
 * Type of attribute instance used in the TSV attributes export/import format.
 */
public enum AttributeInstanceTypeEnum {

    VALUE("VALUE"),
    GRANULARITY("GRANULARITY");

    private final String value;

    AttributeInstanceTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isGranularity(String value) {
        return GRANULARITY.getValue().equals(value);
    }
}
