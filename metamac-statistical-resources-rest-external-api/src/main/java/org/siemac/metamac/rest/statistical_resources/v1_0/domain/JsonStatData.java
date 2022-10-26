package org.siemac.metamac.rest.statistical_resources.v1_0.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.math.NumberUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * This class and those in the same package are used for mapping JSON-stat files to Java objects.
 * They are based on an interpretation of the documentation on the JSON-stat format available at
 * <a href="https://json-stat.org/format/">JSON-stat website</a>.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
public class JsonStatData {

    @JsonProperty
    private String version;

    @JsonProperty("class")
    private String clazz;

    @JsonProperty
    private String label;

    @JsonProperty
    private String updated;

    @JsonProperty
    private JsonStatExtension extension;

    @JsonProperty
    private List<String> note;

    @JsonProperty
    private List<Number> value = new ArrayList<>(); // TODO EDATOS-3662: could there be more values than Integer.MAX_VALUE?

    @JsonProperty
    private Map<String, String> status;

    @JsonProperty
    private List<String> id;

    @JsonProperty
    private List<Long> size;

    @JsonProperty
    private Map<String, List<String>> role;

    @JsonProperty
    private Map<String, JsonStatDimension> dimension;

    public void addAllValues(List<String> values) {
        for (String strValue : values) {
            value.add(parseNumber(strValue));
        }
    }

    private Number parseNumber(String str) {
        if (str == null || StringUtils.isBlank(str)) {
            return null; // missing value, N/A, represented as null
        }

        if (!NumberUtils.isNumber(str)) {
            throw new NumberFormatException("Invalid number: " + str);
        }

        if (str.contains(".")) {
            return Double.parseDouble(str);
        }
        return Long.parseLong(str);
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

    public List<Number> getValue() {
        return value;
    }

    public void setValue(List<Number> value) {
        this.value = value;
    }

    public Map<String, String> getStatus() {
        return status;
    }

    public void setStatus(Map<String, String> status) {
        this.status = status;
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
