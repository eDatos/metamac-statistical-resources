package org.siemac.metamac.statistical_resources.rest.common.impl.export.utils;

import java.util.List;

public class DimensionsFilter {

    private String       temporalDimensionId;
    private String       geographicDimensionId;
    private List<String> temporalDimensionValuesIds;
    private List<String> temporalDimensionValuesQueriesIds;
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

    public List<String> getTemporalDimensionValuesQueriesIds() {
        return temporalDimensionValuesQueriesIds;
    }

    public void setTemporalDimensionValuesQueriesIds(List<String> temporalDimensionValuesQueriesIds) {
        this.temporalDimensionValuesQueriesIds = temporalDimensionValuesQueriesIds;
    }

    public List<String> getGeographicDimensionValuesIds() {
        return geographicDimensionValuesIds;
    }

    public void setGeographicDimensionValuesIds(List<String> geographicDimensionValuesIds) {
        this.geographicDimensionValuesIds = geographicDimensionValuesIds;
    }

}
