package org.siemac.metamac.statistical_resources.rest.common.impl.export;

import static org.siemac.metamac.statistical_resources.rest.common.impl.export.utils.ExportUtils.buildMapDimensionsValuesLabels;
import static org.siemac.metamac.statistical_resources.rest.common.impl.export.utils.ExportUtils.dataToDataArray;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.enume.LabelVisualisationModeEnum;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.utils.ExportUtils;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeAttachmentLevelType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DatasetBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimensions;

public class ResourceAccess {

    private static final int                              MAX_PX_MATRIX_LENGTH = 8;

    private DatasetSelection                              datasetSelection;
    private List<String>                                  selectedLanguages;

    // Metadata

    private Map<String, Map<String, InternationalString>> dimensionsValuesCurrentLocaleLabels;

    private Map<String, Map<String, InternationalString>> attributesValuesCurrentLocaleLabels;
    private Map<String, LabelVisualisationModeEnum>       attributesLabelVisualisationMode;
    private List<String>                                  attributeIds         = new ArrayList<String>();

    // Data
    private String[]                                      observations;
    private Map<String, String[]>                         attributesValuesByAttributeId;
    private List<String>                                  dimensionsOrderedForData;

    private final Map<String, Integer>                    multipliers          = new HashMap<String, Integer>();
    private final Map<String, Map<String, Long>>          representationIndex  = new HashMap<String, Map<String, Long>>(); // Map<Dimension, Map<Code, Index>

    public ResourceAccess(DatasetBase dataset, DatasetSelection datasetSelection, List<String> selectedLanguages) throws MetamacException {

        this.datasetSelection = datasetSelection;

        initialize(dataset.getData(), dataset.getMetadata().getDimensions(), dataset.getMetadata().getAttributes(), datasetSelection, selectedLanguages);
    }

    private void initialize(Data data, Dimensions dimensions, Attributes attributes, DatasetSelection datasetSelection, List<String> selectedLanguages) throws MetamacException {
        setSelectedLanguages(selectedLanguages);

        initializeDimensions(dimensions, datasetSelection);
        initializeAttributes(data, attributes, datasetSelection);
        initializeObservations(data);
        initializeDimensionsForData(data);
        initializeMultipliers(data);
        initializeIndex(data);
    }

    public List<String> getAttributeAttachmentLevelIds() {
        return attributeIds;
    }

    public DatasetSelection getDataSelection() {
        return datasetSelection;
    }

    public InternationalString getDimensionValueLabelCurrentLocale(String dimensionId, String dimensionValueId) {
        return dimensionsValuesCurrentLocaleLabels.get(dimensionId).get(dimensionValueId);
    }

    public InternationalString getAttributeValueLabelCurrentLocale(String attributeId, String attributeValue) {
        return attributesValuesCurrentLocaleLabels.get(attributeId).get(attributeValue);
    }

    public LabelVisualisationModeEnum getAttributeLabelVisualisationMode(String attributeId) {
        return attributesLabelVisualisationMode.get(attributeId);
    }

    public String[] getObservations() {
        return observations;
    }

    public String[] getAttributeValues(String attributeId) {
        return attributesValuesByAttributeId.get(attributeId);
    }

    public List<String> getDimensionsOrderedForData() {
        return dimensionsOrderedForData;
    }

    /**
     * Init dimensions and dimensions values
     */
    private void initializeDimensions(Dimensions dimensions, DatasetSelection datasetSelection) throws MetamacException {
        List<Dimension> dimensionsMetadata = dimensions.getDimensions();

        Map<String, Map<String, InternationalString>> valuesCurrentLocaleLabels = new HashMap<String, Map<String, InternationalString>>();

        for (Dimension dimension : dimensionsMetadata) {
            if (datasetSelection.getDimensionLabelVisualisationModel(dimension.getId()).isLabel()) {
                valuesCurrentLocaleLabels.put(dimension.getId(), buildMapDimensionsValuesLabels(dimension));
            }
        }

        dimensionsValuesCurrentLocaleLabels = valuesCurrentLocaleLabels;
    }

    /**
     * Init observations values
     */
    private void initializeObservations(Data data) {
        observations = dataToDataArray(data.getObservations());
    }

    /**
     * Init dimensions and dimensions values. Builds a map with dimensions values to get order provided in DATA, because observations are retrieved in API with this order
     */
    private void initializeDimensionsForData(Data data) throws MetamacException {
        List<DimensionRepresentation> dimensionRepresentations = data.getDimensions().getDimensions();
        dimensionsOrderedForData = new ArrayList<String>(dimensionRepresentations.size());
        for (DimensionRepresentation dimensionRepresentation : dimensionRepresentations) {
            String dimensionId = dimensionRepresentation.getDimensionId();
            dimensionsOrderedForData.add(dimensionId);
        }
    }

    /**
     * Retrieve the observation for a specific key <param>permutation</param>
     *
     * @param observation at permutation key
     * @return
     */
    public String observationAtPermutation(Map<String, String> permutation) {
        int offset = calculateOffsetAtPermutation(permutation);

        String observation = getObservations()[offset];
        if (!observation.trim().isEmpty()) {
            return observation;
        } else {
            return null;
        }
    }

    public String measureAttributeValueAtPermutation(String attributeId, Map<String, String> permutation) {
        int offset = calculateOffsetAtPermutation(permutation);
        String[] attributeValues = getAttributeValues(attributeId);
        String attributeValue = null;
        if (attributeValues != null) {
            attributeValue = attributeValues[offset];
        }
        return attributeValue;
    }

    private void initializeMultipliers(Data data) {
        List<DimensionRepresentation> dimensionsRepresentation = data.getDimensions().getDimensions();
        ListIterator<DimensionRepresentation> dimensionsListIterator = dimensionsRepresentation.listIterator(dimensionsRepresentation.size());
        int incrementCounter = 1;

        // Iterate the list in reverse order: right to left or down to up in the display table for calculate cell spacing
        while (dimensionsListIterator.hasPrevious()) {
            DimensionRepresentation dimension = dimensionsListIterator.previous();
            multipliers.put(dimension.getDimensionId(), incrementCounter);
            incrementCounter *= dimension.getRepresentations().getRepresentations().size();
        }
    }

    /**
     * Calculate a map indexed by dimension with map as value. The value map is indexed by code and its value is a index.
     */
    private void initializeIndex(Data data) {
        List<DimensionRepresentation> dimensionsRepresentation = data.getDimensions().getDimensions();
        for (DimensionRepresentation dimension : dimensionsRepresentation) {
            Map<String, Long> representationIndexMap = new HashMap<String, Long>();
            List<CodeRepresentation> representations = dimension.getRepresentations().getRepresentations();
            for (CodeRepresentation representation : representations) {
                representationIndexMap.put(representation.getCode(), representation.getIndex());
            }
            representationIndex.put(dimension.getDimensionId(), representationIndexMap);
        }
    }

    private int calculateOffsetAtPermutation(Map<String, String> permutation) {
        int offset = 0;
        for (Map.Entry<String, String> permutationEntry : permutation.entrySet()) {
            String dimensionId = permutationEntry.getKey();
            String representationId = permutationEntry.getValue();

            long index = representationIndex.get(dimensionId).get(representationId);
            int multiplier = multipliers.get(dimensionId);
            offset += index * multiplier;
        }
        return offset;
    }

    /**
     * Init definitions and values of attributes
     *
     * @param attributes
     */
    private void initializeAttributes(Data data, Attributes attributes, DatasetSelection datasetSelection) throws MetamacException {
        List<Attribute> attributesMetadata;
        if (attributes == null) {
            attributesMetadata = new ArrayList<Attribute>();
        } else {
            attributesMetadata = attributes.getAttributes();
        }

        // Attribute Instances
        attributesValuesByAttributeId = new HashMap<String, String[]>(attributesMetadata.size());
        for (Attribute attribute : attributesMetadata) {
            if (AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attribute.getAttachmentLevel())) {
                // only observation attachment level
                attributeIds.add(attribute.getId());
            }

            if (data.getAttributes() != null) {
                for (DataAttribute dataAttribute : data.getAttributes().getAttributes()) {
                    if (dataAttribute.getId().equals(attribute.getId())) {
                        attributesValuesByAttributeId.put(attribute.getId(), ExportUtils.dataToDataArray(dataAttribute.getValue()));
                    }
                }
            }
        }

        attributesLabelVisualisationMode = ExportUtils.buildMapAttributesLabelVisualisationMode(datasetSelection, attributesMetadata);
        attributesValuesCurrentLocaleLabels = new HashMap<String, Map<String, InternationalString>>();
        for (Attribute attribute : attributesMetadata) {
            attributesValuesCurrentLocaleLabels.put(attribute.getId(), ExportUtils.buildMapAttributesValuesLabels(attribute));
        }
    }

    public List<String> getSelectedLanguages() {
        return selectedLanguages;
    }

    public void setSelectedLanguages(List<String> selectedLanguages) {
        this.selectedLanguages = selectedLanguages;
    }

    public static String generateMatrixFromString(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes()).substring(0, MAX_PX_MATRIX_LENGTH);
    }

    // return number of observations + 1 (header row)
    public Long getObservationsNumber() {
        Long dimensionRows = Long.valueOf(getDataSelection().getRows());
        Long dimensionColumns = Long.valueOf(getDataSelection().getColumns());
        return dimensionRows * dimensionColumns + 1;
    }
}