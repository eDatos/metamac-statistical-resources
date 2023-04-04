package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

public class DataOrderingStackElement {

    private final String dimensionId;
    private final int    dimensionPosition;
    private final String dimensionCodeId;

    public DataOrderingStackElement(String dimensionId, int dimensionPosition, String dimensionCodeId) {
        super();
        this.dimensionId = dimensionId;
        this.dimensionPosition = dimensionPosition;
        this.dimensionCodeId = dimensionCodeId;
    }

    public String getDimensionId() {
        return dimensionId;
    }

    public int getDimensionPosition() {
        return dimensionPosition;
    }

    public String getDimensionCodeId() {
        return dimensionCodeId;
    }
}