package org.siemac.metamac.rest.statistical_resources.v1_0.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * This class is used for mapping JSON-stat files to Java objects, based on a subset of fields extracted from
 * the documentation on the JSON-stat format available at <a href="https://json-stat.org/full/">JSON-stat website</a>.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
public class JsonStatData {

    private String version;

    @JsonProperty("class")
    private String clazz;

    private String label;
    private String updated;
    private JsonStatExtension extension;
    private List<String> note;
    private List<String> value = new ArrayList<>();
    private List<String> id;
    private List<Long> size;
    private Map<String, List<String>> role;
    private Map<String, JsonStatDimension> dimension;

    public void addAllValues(List<String> values) {
        value.addAll(values);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public List<Long> getSize() {
        return size;
    }

    public void setSize(List<Long> size) {
        this.size = size;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public Map<String, List<String>> getRole() {
        return role;
    }

    public void setRole(Map<String, List<String>> role) {
        this.role = role;
    }

    public List<String> getNote() {
        return note;
    }

    public void setNote(List<String> note) {
        this.note = note;
    }

    public Map<String, JsonStatDimension> getDimension() {
        return dimension;
    }

    public void setDimension(Map<String, JsonStatDimension> dimension) {
        this.dimension = dimension;
    }

    public JsonStatDimension getDimension(String key) {
        return dimension.get(key);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public JsonStatExtension getExtension() {
        return extension;
    }

    public void setExtension(JsonStatExtension extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
