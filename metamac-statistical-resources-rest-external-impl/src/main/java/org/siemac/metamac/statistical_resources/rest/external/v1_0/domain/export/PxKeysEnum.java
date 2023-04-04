package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum PxKeysEnum implements Serializable {
    // @formatter:off

    AGGREGALLOWED("AGGREGALLOWED", false, 18, false, "YES"),
    ATTRIBUTE_ID("ATTRIBUTE-ID", false, 83, false, null),
    ATTRIBUTE_TEXT("ATTRIBUTE-TEXT", false, 84, true, null),
    ATTRIBUTES("ATTRIBUTES", false, 85, false, null),
    AUTOPEN("AUTOPEN", false, 19, false, "NO"),
    AXIS_VERSION("AXIS-VERSION", false, 2, false, null),
    BASEPERIOD("BASEPERIOD", false, 55, true, null),
    CELLNOTE("CELLNOTE", false, 70, true, null),
    CELLNOTEX("CELLNOTEX", false, 69, true, null),
    CFPRICES("CELLNOTE", false, 49, true, null),
    CHARSET("CHARSET", false, 1, false, null),
    CODEPAGE("CODEPAGE", false, 3, false, "iso-8859-1"),
    CODES("CODES", false, 34, true, null),
    CONFIDENTIAL("CONFIDENTIAL", false, 22, false, null),
    CONTACT("CONTACT", false, 53, true, null),
    CONTENTS("CONTENTS", true, 27, true, null),
    CONTVARIABLE("CONTVARIABLE", false, 31, true, null),
    COPYRIGHT("COPYRIGHT", false, 23, false, "NO"),
    CREATION_DATE("CREATION-DATE", false, 6, false, null),
    DATA("DATA", true, 87, false, null),
    DATABASE("DATABASE", false, 56, true, null),
    DATANOTE("DATANOTE", false, 81, true, null),
    DATANOTECELL("DATANOTECELL", false, 79, true, null),
    DATANOTESUM("DATANOTESUM", false, 80, true, null),
    DATASYMBOL1("DATASYMBOL1", false, 71, true, "."),
    DATASYMBOL2("DATASYMBOL2", false, 72, true, ".."),
    DATASYMBOL3("DATASYMBOL3", false, 73, true, "..."),
    DATASYMBOL4("DATASYMBOL4", false, 74, true, "...."),
    DATASYMBOL5("DATASYMBOL5", false, 75, true, "....."),
    DATASYMBOL6("DATASYMBOL6", false, 76, true, "......"),
    DATASYMBOLNIL("DATASYMBOLNIL", false, 78, true, null),
    DATASYMBOLSUM("DATASYMBOLSUM", false, 77, true, null),
    DAYADJ("DAYADJ", false, 50, true, "NO"),
    DECIMALS("DECIMALS", true, 14, false, null),
    DEFAULT_GRAPH("DEFAULT-GRAPH", false, 13, false, null),
    DESCRIPTION("DESCRIPTION", false, 24, true, null),
    DESCRIPTIONDEFAULT("DESCRIPTIONDEFAULT", false, 26, false, null),
    DIRECTORY_PATH("DIRECTORY-PATH", false, 9, false, null),
    DOMAIN("DOMAIN", false, 37, true, null),
    DOUBLECOLUMN("DOUBLECOLUMN", false, 35, true, "NO"),
    ELIMINATION("ELIMINATION", false, 45, true, "NO"),
    FIRST_PUBLISHED("FIRST-PUBLISHED", false, 61, false, null),
    HEADING("HEADING", false, 30, true, null),
    HIERARCHIES("HIERARCHIES", false, 39, true, null),
    HIERARCHYLEVELS("HIERARCHYLEVELS", false, 40, true, null),
    HIERARCHYLEVELSOPEN("HIERARCHYLEVELSOPEN", false, 41, true, null),
    HIERARCHYNAMES("HIERARCHYNAMES", false, 42, true, null),
    INFO("INFO", false, 64, true, null),
    INFOFILE("INFOFILE", false, 60, true, null),
    KEYS("KEYS", false, 82, true, null),
    LANGUAGE("LANGUAGE", false, 4, false, null),
    LANGUAGES("LANGUAGES", false, 5, false, null),
    LAST_UPDATED("LAST-UPDATED", false, 47, true, null),
    LINK("LINK", false, 59, true, null),
    MAP("MAP", false, 43, true, null),
    MATRIX("MATRIX", true, 17, false, null),
    META_ID("META-ID", false, 62, true, null),
    NEXT_UPDATE("NEXT-UPDATE", false, 7, false, null),
    NOTE("NOTE", false, 65, true, null),
    NOTEX("NOTEX", false, 66, true, null),
    OFFICIAL_STATISTICS("OFFICIAL-STATISTICS", false, 63, false, "NO"),
    PARTITIONED("PARTITIONED", false, 44, true, null),
    PRECISION("PRECISION", false, 86, true, null),
    PRESTEXT("PRESTEXT", false, 36, true, "1"),
    PX_SERVER("PX-SERVER", false, 8, false, null),
    REFPERIOD("REFPERIOD", false, 54, true, null),
    ROUNDING("ROUNDING", false, 16, false, null),
    SEASADJ("SEASADJ", false, 51, true, "NO"),
    SHOWDECIMALS("SHOWDECIMALS", false, 15, false, null),
    SOURCE("SOURCE", false, 57, true, null),
    STOCKFA("STOCKFA", false, 48, true, null),
    STUB("STUB", true, 29, true, null),
    SUBJECT_AREA("SUBJECT-AREA", true, 21, true, null),
    SUBJECT_CODE("SUBJECT-CODE", true, 20, false, null),
    SURVEY("SURVEY", false, 58, true, null),
    SYNONYMS("SYNONYMS", false, 12, false, null),
    TABLEID("TABLEID", false, 11, false, null),
    TIMEVAL("TIMEVAL", false, 33, true, null),
    TITLE("TITLE", true, 25, true, null),
    UNITS("UNITS", true, 52, true, null),
    UPDATE_FREQUENCY("UPDATE-FREQUENCY", false, 10, false, null),
    VALUENOTE("VALUENOTE", false, 67, true, null),
    VALUENOTEX("VALUENOTEX", false, 68, true, null),
    VALUES("VALUES", false, 32, true, null),
    VARIABLE_TYPE("VARIABLE-TYPE", false, 38, true, null);

    // @formatter:on

    private static Map<String, PxKeysEnum> identifierMap = new HashMap<String, PxKeysEnum>();

    static {
        for (PxKeysEnum value : PxKeysEnum.values()) {
            identifierMap.put(value.getKeyword(), value);
        }
    }

    private String                         keyword;
    private boolean                        mandatory;
    private int                            proposeOrder;
    private boolean                        languageDependent;
    private String                         defaultValue;

    /**
     */
    private PxKeysEnum(String keyword, boolean mandatory, int proposeOrder, boolean languageDependent, String defaultValue) {
        this.keyword = keyword;
        this.mandatory = mandatory;
        this.proposeOrder = proposeOrder;
        this.languageDependent = languageDependent;
        this.defaultValue = defaultValue;
    }

    public static PxKeysEnum fromKeyword(String value) {
        return identifierMap.get(value);
    }

    public String getName() {
        return name();
    }

    public String getKeyword() {
        return keyword;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public int getProposeOrder() {
        return proposeOrder;
    }

    public boolean isLanguageDependent() {
        return languageDependent;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
