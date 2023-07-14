package org.siemac.metamac.statistical_resources.rest.common.impl.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.siemac.metamac.statistical_resources.rest.common.impl.export.enume.LabelVisualisationModeEnum;

public class DatasetSelection {

    // We only support a max of 20 dimensions each type
    public static final int                              LEFT_DIMENSIONS_START_POSITION  = 0;
    public static final int                              TOP_DIMENSIONS_START_POSITION   = 20;
    public static final int                              FIXED_DIMENSIONS_START_POSITION = 40;

    private boolean                                      userSelection                   = true;

    private List<DatasetSelectionDimension>              dimensions                      = new ArrayList<DatasetSelectionDimension>();
    private final Map<String, DatasetSelectionDimension> dimensionsMap                   = new HashMap<String, DatasetSelectionDimension>();

    private final Map<String, DatasetSelectionAttribute> attributesMap                   = new HashMap<String, DatasetSelectionAttribute>();

    private final Map<String, Integer>                   multipliers                     = new HashMap<String, Integer>();

    public DatasetSelection(List<DatasetSelectionDimension> dimensions, List<DatasetSelectionAttribute> attributes, boolean userSelection) {
        this(dimensions, attributes);
        this.userSelection = userSelection;
    }

    public DatasetSelection(List<DatasetSelectionDimension> dimensions, List<DatasetSelectionAttribute> attributes) {
        if (dimensions != null) {
            this.dimensions = new ArrayList<DatasetSelectionDimension>(dimensions);
            for (DatasetSelectionDimension dimension : dimensions) {
                dimensionsMap.put(dimension.getId(), dimension);
            }
        }

        if (attributes != null) {
            for (DatasetSelectionAttribute attribute : attributes) {
                attributesMap.put(attribute.getId(), attribute);
            }
        }

        recalculateMultipliers();
    }

    private void recalculateMultipliers() {
        initializeMultipliers(getLeftDimensions());
        initializeMultipliers(getTopDimensions());
        initializedFixedMultipliers();
    }

    public boolean isUserSelection() {
        return userSelection;
    }

    public DatasetSelectionDimension getDimension(String dimensionId) {
        return dimensionsMap.get(dimensionId);
    }

    public List<DatasetSelectionDimension> getDimensions() {
        return new ArrayList<DatasetSelectionDimension>(dimensions);
    }

    public LabelVisualisationModeEnum getDimensionLabelVisualisationModel(String dimensionId) {
        DatasetSelectionDimension dimension = getDimension(dimensionId);
        return dimension != null ? dimension.getLabelVisualisationMode() : null;
    }

    private void initializeMultipliers(List<DatasetSelectionDimension> dimensions) {
        ListIterator<DatasetSelectionDimension> dimensionsListInterator = dimensions.listIterator(dimensions.size());
        int incrementCounter = 1;

        // Iterate the list in reverse order: right to left or down to up in the display table for calculate cell spacing
        while (dimensionsListInterator.hasPrevious()) {
            DatasetSelectionDimension dimension = dimensionsListInterator.previous();
            multipliers.put(dimension.getId(), incrementCounter);
            incrementCounter *= dimension.getSelectedDimensionValues().size();
        }
    }

    private void initializedFixedMultipliers() {
        List<DatasetSelectionDimension> fixedDimensions = getFixedDimensions();
        for (DatasetSelectionDimension dimension : fixedDimensions) {
            multipliers.put(dimension.getId(), 1);
        }
    }

    /**
     * Calculate rows
     *
     * @return the number of total rows in the displayed data table
     */
    public int getRows() {
        List<DatasetSelectionDimension> leftDimensions = getLeftDimensions();
        if (!leftDimensions.isEmpty()) {
            DatasetSelectionDimension biggerLeftDimension = leftDimensions.get(0);
            return multipliers.get(biggerLeftDimension.getId()) * biggerLeftDimension.getSelectedDimensionValues().size();
        } else {
            return 1;
        }
    }

    /**
     * Calculate columns
     *
     * @return the number of total columns in the displayed data table
     */
    public int getColumns() {
        List<DatasetSelectionDimension> topDimensions = getTopDimensions();
        if (!topDimensions.isEmpty()) {
            DatasetSelectionDimension biggerTopDimension = topDimensions.get(0);
            return multipliers.get(biggerTopDimension.getId()) * biggerTopDimension.getSelectedDimensionValues().size();
        } else {
            return 1;
        }
    }

    public List<DatasetSelectionDimension> getLeftDimensions() {
        return getDimensionsInPositionRange(LEFT_DIMENSIONS_START_POSITION, TOP_DIMENSIONS_START_POSITION);
    }

    public List<DatasetSelectionDimension> getTopDimensions() {
        return getDimensionsInPositionRange(TOP_DIMENSIONS_START_POSITION, FIXED_DIMENSIONS_START_POSITION);
    }

    public List<DatasetSelectionDimension> getFixedDimensions() {
        return getDimensionsInPositionRange(FIXED_DIMENSIONS_START_POSITION, FIXED_DIMENSIONS_START_POSITION + 20);
    }

    private List<DatasetSelectionDimension> getDimensionsInPositionRange(int from, int to) {
        List<DatasetSelectionDimension> dimensionsInRange = new ArrayList<DatasetSelectionDimension>();
        for (DatasetSelectionDimension dimension : getDimensions()) {
            if (dimension.getPosition() >= from && dimension.getPosition() < to) {
                dimensionsInRange.add(dimension);
            }
        }
        sortDimensionsByPosition(dimensionsInRange);
        return dimensionsInRange;
    }

    private void sortDimensionsByPosition(List<DatasetSelectionDimension> dimensions) {
        Collections.sort(dimensions, new Comparator<DatasetSelectionDimension>() {

            @Override
            public int compare(DatasetSelectionDimension datasetSelectionDimension, DatasetSelectionDimension datasetSelectionDimension2) {
                return datasetSelectionDimension.getPosition() - datasetSelectionDimension2.getPosition(); // Ascending sort
            }
        });
    }

    /**
     * Calculate the key permutation for a cell of data
     *
     * @param row
     * @param column
     * @return
     */
    public Map<String, String> permutationAtCell(int row, int column) {
        Map<String, String> permutation = new HashMap<String, String>();

        for (DatasetSelectionDimension dimension : getLeftDimensions()) {
            Integer multiplier = multipliers.get(dimension.getId());
            int selectedCategoryIndex = (row / multiplier) % dimension.getSelectedDimensionValues().size();
            permutation.put(dimension.getId(), dimension.getSelectedDimensionValues().get(selectedCategoryIndex));
        }

        for (DatasetSelectionDimension dimension : getTopDimensions()) {
            Integer multiplier = multipliers.get(dimension.getId());
            int selectedCategoryIndex = (column / multiplier) % dimension.getSelectedDimensionValues().size();
            permutation.put(dimension.getId(), dimension.getSelectedDimensionValues().get(selectedCategoryIndex));
        }

        for (DatasetSelectionDimension dimension : getFixedDimensions()) {
            if (!dimension.getSelectedDimensionValues().isEmpty()) {
                permutation.put(dimension.getId(), dimension.getSelectedDimensionValues().get(0));
            }
        }

        return permutation;
    }

    public LabelVisualisationModeEnum getAttributeLabelVisualisationModel(String attributeId) {
        DatasetSelectionAttribute attribute = getAttribute(attributeId);
        return attribute != null ? attribute.getLabelVisualisationMode() : null;
    }

    public DatasetSelectionAttribute getAttribute(String attributeId) {
        return attributesMap.get(attributeId);
    }
}
