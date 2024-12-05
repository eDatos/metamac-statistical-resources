package org.siemac.metamac.statistical_resources.rest.internal.v1_0.domain;

import java.util.List;

public class DimensionValueByIdDimension {

    private String       temporalDimensionId;
    private String       geographicDimensionId;
    private List<String> temporalDimensionValuesIds;
    private List<String> geographicDimensionValuesIds;

    public String getTemporalDimensionId() {
        return temporalDimensionId;
    }

    public void setTemporalDimensionId(String temporalDimensionId) {
        this.temporalDimensionId = temporalDimensionId;
    }

    public String getGeographicDimensionId() {
        return geographicDimensionId;
    }

    public void setGeographicDimensionId(String geographicDimensionId) {
        this.geographicDimensionId = geographicDimensionId;
    }

    public List<String> getTemporalDimensionValuesIds() {
        return temporalDimensionValuesIds;
    }

    public void setTemporalDimensionValuesIds(List<String> temporalDimensionValuesIds) {
        this.temporalDimensionValuesIds = temporalDimensionValuesIds;
    }

    public List<String> getGeographicDimensionValuesIds() {
        return geographicDimensionValuesIds;
    }

    public void setGeographicDimensionValuesIds(List<String> geographicDimensionValuesIds) {
        this.geographicDimensionValuesIds = geographicDimensionValuesIds;
    }

}
