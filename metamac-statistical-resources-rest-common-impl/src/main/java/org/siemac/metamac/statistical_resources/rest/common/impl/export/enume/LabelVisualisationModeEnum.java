package org.siemac.metamac.statistical_resources.rest.common.impl.export.enume;

public enum LabelVisualisationModeEnum {

    CODE, LABEL, CODE_AND_LABEL;

    public String value() {
        return name();
    }

    public static LabelVisualisationModeEnum fromValue(String v) {
        return valueOf(v);
    }

    public boolean isLabel() {
        return CODE_AND_LABEL.equals(this) || LABEL.equals(this);
    }

    public boolean isCode() {
        return CODE_AND_LABEL.equals(this) || CODE.equals(this);
    }

    public boolean isLabelAndCode() {
        return CODE_AND_LABEL.equals(this);
    }

}
