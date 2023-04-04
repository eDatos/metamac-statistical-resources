package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.constants;

import java.util.HashMap;
import java.util.Map;

public class ExportConstants {

    public final static String QUOTE = "\"";
    public final static String SPACE = " ";
    public final static String COMMA = ",";
    public final static String SEMICOLON = ";";
    public final static String EQUALS = "=";
    public final static String LEFT_PARENTHESES = "(";
    public final static String RIGHT_PARENTHESES = ")";
    public final static String NEW_LINE = "\n";
    public final static String LEFT_BRACE = "[";
    public final static String RIGHT_BRACE = "]";

    public static enum ResourceType {

        //@formatter:off
        DATASET("dataset"),
        QUERY("query"),
        INDICATOR("indicator"),
        INDICATOR_INSTANCE("indicatorInstance"),
        COLLECTION("collection"),
        MULTIDATASET("multidataset");
        //@formatter:on

        private static Map<String, ResourceType> identifierMap = new HashMap<String, ResourceType>();

        static {
            for (ResourceType value : ResourceType.values()) {
                identifierMap.put(value.getName(), value);
            }
        }

        public static ResourceType fromValue(String value) {
            ResourceType result = identifierMap.get(value);
            if (result == null) {
                throw new IllegalArgumentException("No ResourceType for value: " + value);
            }
            return result;
        }

        private String name;

        ResourceType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
