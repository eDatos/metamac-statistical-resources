package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.DatasetSelection.FIXED_DIMENSIONS_START_POSITION;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.DatasetSelection.LEFT_DIMENSIONS_START_POSITION;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.DatasetSelection.TOP_DIMENSIONS_START_POSITION;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.LabelVisualisationModeEnum.CODE_AND_LABEL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.CodeRepresentations;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DataStructureDefinition;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DimensionRepresentations;
import org.siemac.metamac.statistical_resources.rest.external.exception.RestServiceExceptionType;

public class DatasetSelectionMapper {

    private static final int MAX_SIZE_URL = 2000;

    /**
     * @param dimensionRepresentations
     * @param relatedDsd
     * @param exportationBody
     *            We generate the dimensions and attributes object with the one that compose the dataset, where it´s data has been previously
     *            filtered previously when it was retrieved. We enrich the object with the exportationBody, to take into account the user input
     */
    public static DatasetSelection datasetToDatasetSelection(DimensionRepresentations dimensionRepresentations, DataStructureDefinition relatedDsd) {
        Map<String, DatasetSelectionDimension> selectionDimensionsMap = null;

        List<DatasetSelectionDimension> dimensions = dimensionsToDatasetSelectionDimensions(dimensionRepresentations, selectionDimensionsMap, relatedDsd);
        return new DatasetSelection(dimensions, false);
    }

    private static List<DatasetSelectionDimension> dimensionsToDatasetSelectionDimensions(DimensionRepresentations dimensionRepresentations,
            Map<String, DatasetSelectionDimension> selectionDimensionsMap, DataStructureDefinition dataStructureDefinition) {
        List<DatasetSelectionDimension> datasetSelectionDimensions = new ArrayList<DatasetSelectionDimension>();
        for (DimensionRepresentation dimension : dimensionRepresentations.getDimensions()) {
            final DatasetSelectionDimension selectionDimension = selectionDimensionsMap != null ? selectionDimensionsMap.get(dimension.getDimensionId()) : null;
            datasetSelectionDimensions.add(dimensionToDatasetSelectionDimension(dimension, dataStructureDefinition, selectionDimension));
        }
        return datasetSelectionDimensions;
    }

    private static DatasetSelectionDimension dimensionToDatasetSelectionDimension(DimensionRepresentation dimension, DataStructureDefinition dataStructureDefinition,
            DatasetSelectionDimension selectionDimension) {
        DatasetSelectionDimension datasetSelectionDimension = new DatasetSelectionDimension(dimension.getDimensionId());

        // Default values
        LabelVisualisationModeEnum labelVisualizationMode = CODE_AND_LABEL;
        Integer position = dataStructureDefinitionToPosition(dimension.getDimensionId(), dataStructureDefinition);

        // If we have data sent via api, use that instead
        if (selectionDimension != null) {
            if (selectionDimension.getLabelVisualisationMode() != null) {
                labelVisualizationMode = toLabelVisualisationMode(selectionDimension.getLabelVisualisationMode());
            }
            if (selectionDimension.getPosition() != null) {
                position = selectionDimension.getPosition();
            }
        }

        datasetSelectionDimension.setLabelVisualisationMode(labelVisualizationMode);
        datasetSelectionDimension.setPosition(position);
        datasetSelectionDimension.setSelectedDimensionValues(codeRepresentationsToSelectedDimensionValues(dimension.getRepresentations()));
        return datasetSelectionDimension;
    }

    private static Integer dataStructureDefinitionToPosition(String id, DataStructureDefinition dataStructureDefinition) {
        if (dataStructureDefinition.getHeading().getDimensionIds().contains(id)) {
            return TOP_DIMENSIONS_START_POSITION + dataStructureDefinition.getHeading().getDimensionIds().indexOf(id);
        } else if (dataStructureDefinition.getStub().getDimensionIds().contains(id)) {
            return LEFT_DIMENSIONS_START_POSITION + dataStructureDefinition.getStub().getDimensionIds().indexOf(id);
        } else {
            return FIXED_DIMENSIONS_START_POSITION;
        }
    }

    private static List<String> codeRepresentationsToSelectedDimensionValues(CodeRepresentations codeRepresentations) {
        List<String> selectedDimensionValues = new ArrayList<String>();
        for (CodeRepresentation dimensionValue : codeRepresentations.getRepresentations()) {
            selectedDimensionValues.add(dimensionValue.getCode());
        }
        return selectedDimensionValues;
    }

    private static LabelVisualisationModeEnum toLabelVisualisationMode(LabelVisualisationModeEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case LABEL:
                return LabelVisualisationModeEnum.LABEL;
            case CODE:
                return LabelVisualisationModeEnum.CODE;
            case CODE_AND_LABEL:
                return LabelVisualisationModeEnum.CODE_AND_LABEL;
            default:
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

}