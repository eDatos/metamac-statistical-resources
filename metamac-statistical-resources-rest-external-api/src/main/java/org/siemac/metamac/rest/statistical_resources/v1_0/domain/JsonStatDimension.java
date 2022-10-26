package org.siemac.metamac.rest.statistical_resources.v1_0.domain;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
public class JsonStatDimension {

    @JsonProperty
    private String label;

    @JsonProperty
    private JsonStatCategory category;

    public String getLabel() {
        return label;
    }

    public JsonStatCategory getCategory() {
        return category;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setCategory(JsonStatCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
