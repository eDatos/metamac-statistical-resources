package org.siemac.metamac.rest.statistical_resources.v1_0.domain;

import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
public class JsonStatCategory {

    private Map<String, Long> index;
    private Map<String, String> label;
    private Map<String, JsonStatUnit> unit;

    public Map<String, Long> getIndex() {
        return index;
    }

    public void setIndex(Map<String, Long> index) {
        this.index = index;
    }

    public Long getIndex(String key) {
        return index.get(key);
    }

    public Map<String, String> getLabel() {
        return label;
    }

    public void setLabel(Map<String, String> label) {
        this.label = label;
    }

    public String getLabel(String key) {
        return label.get(key);
    }

    public Map<String, JsonStatUnit> getUnit() {
        return unit;
    }

    public void setUnit(Map<String, JsonStatUnit> unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
