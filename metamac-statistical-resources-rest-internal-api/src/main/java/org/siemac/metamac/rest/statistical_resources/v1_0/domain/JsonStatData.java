package org.siemac.metamac.rest.statistical_resources.v1_0.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;

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

    // * Problem:
    // When no MIME type has been defined for a request/response, JAX-RS chooses to serve JSON-stat by
    // default, instead of XML which was the default behavior before implementing JSON-stat.
    //
    // * Cause:
    // We use two identical resource methods to serve datasets/queries, only distinguished when
    // a MIME type has been defined. XML/JSON calls a specific resource method (i.e. retrieveDataset),
    // JSON-stat calls a different one (i.e. retrieveJsonStatDataset).
    //
    // JAX-RS will factor the resource path, for example, into consideration when multiple resource methods
    // are elegible, to choose the adecuate one. But MIME type isn't factored into resource method preference,
    // meaning that when no MIME type is specified for request/response, JAX-RS doesn't prioritize one method
    // over the other, nor does it allow it.
    //
    // * Fix:
    // One of the factors that JAX-RS considers to choose a method over another is if the method comes from a
    // subresource, giving it less priority. That way we can de-prioritize the method that serves JSON-stat,
    // fixing the problem.
    //
    // * Reference:
    // https://web.archive.org/web/20090214182513/http://cwiki.apache.org/CXF20DOC/jax-rs.html#JAX-RS-Overviewoftheselectionalgorithm.
    //    see "Overview of the selection algorithm" chapter.
    //
    // https://docs.jboss.org/resteasy/docs/1.0.1.GA/userguide/html/JAX-RS_Resource_Locators_and_Sub_Resources.html
    @GET
    public JsonStatData get() {
        return this;
    }

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
