package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import java.util.List;

public class PxLineContainer {

    private PxKeysEnum   pxKey;
    private List<String> indexedValue;
    private String       lang;
    private Object       value;
    private Boolean      multiline = true;

    public PxKeysEnum getPxKey() {
        return pxKey;
    }

    public void setPxKey(PxKeysEnum pxKey) {
        this.pxKey = pxKey;
    }

    public List<String> getIndexedValue() {
        return indexedValue;
    }

    public void setIndexedValue(List<String> indexedValue) {
        this.indexedValue = indexedValue;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Boolean isMultiline() {
        return multiline;
    }

    public void setIsMultiline(Boolean isMultiline) {
        this.multiline = isMultiline;
    }

}
