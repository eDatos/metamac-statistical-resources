package org.siemac.metamac.rest.statistical_resources.v1_0.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class JsonStatDimensionExtension {

    private Map<String, String> temporalGranularity;
    private Map<String, String> geographicalGranularity;

    public Map<String, String> getTemporalGranularity() {
        return temporalGranularity;
    }

    public void setTemporalGranularity(Map<String, String> temporalGranularity) {
        this.temporalGranularity = temporalGranularity;
    }

    public Map<String, String> getGeographicalGranularity() {
        return geographicalGranularity;
    }

    public void setGeographicalGranularity(Map<String, String> geographicalGranularity) {
        this.geographicalGranularity = geographicalGranularity;
    }
}
