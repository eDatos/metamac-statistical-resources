package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.utils.ExportUtils.buildMapDimensionToMapDimensionsLabelVisualisationMode;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.utils.ExportUtils.buildMapDimensionsValuesLabels;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.utils.ExportUtils.buildMapDimensionsValuesLocalisedLabels;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.utils.ExportUtils.dataToDataArray;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Attribute;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.AttributeAttachmentLevelType;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Attributes;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.ComponentType;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Data;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DataAttribute;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DatasetMetadata;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dimension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DimensionType;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dimensions;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.enume.LabelVisualisationModeEnum;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.utils.ExportUtils;

public class ResourceAccess {

    private static final int MAX_PX_MATRIX_LENGTH = 8;

    private DatasetSelection datasetSelection;

    private List<String> selectedLanguages;

    private Data data;
    private Dimensions dimensions;
    private Attributes attributes;

    private InternationalString name;
    private DatasetMetadata metadata;
    private String urn;
    private String id;
    private String uniqueId;
    private InternationalString description;

    // Metadata
    private List<Dimension> dimensionsMetadata;
    private Map<String, Dimension> dimensionsMetadataMap;
    private Dimension measureDimension;
    private Map<String, InternationalString> dimensionLabelsCurrentLocale;
    private Map<String, InternationalString> dimensionLabelsDefaultLocale;
    private Map<String, Map<String, InternationalString>> dimensionsValuesCurrentLocaleLabels;
    private Map<String, Map<String, InternationalString>> dimensionsValuesLabels;
    private Map<String, LabelVisualisationModeEnum> dimensionsLabelVisualisationMode;

    private List<Attribute> attributesMetadata;
    private Map<String, Attribute> attributesMetadataMap;
    private Attribute measureAttribute;
    private Map<String, InternationalString> attributesLabels;
    private Map<String, Map<String, InternationalString>> attributesValuesCurrentLocaleLabels;
    private Map<String, Map<String, InternationalString>> attributesValuesLabels;
    private Map<String, LabelVisualisationModeEnum> attributesLabelVisualisationMode;

    // Data
    private String[] observations;
    private Map<String, String[]> attributesValuesByAttributeId;
    private List<String> dimensionsOrderedForData;
    private Map<String, List<String>> dimensionValuesOrderedForDataByDimensionId;

    private Dataset dataset;

    private final Map<String, Integer> multipliers = new HashMap<String, Integer>();
    private final Map<String, Map<String, Long>> representationIndex = new HashMap<String, Map<String, Long>>(); // Map<Dimension, Map<Code, Index>

    private int primaryMeasureAttributesCount = 0;

    public ResourceAccess(Dataset dataset, DatasetSelection datasetSelection, List<String> selectedLanguages) throws MetamacException {

        data = dataset.getData();
        dimensions = dataset.getMetadata().getDimensions();
        attributes = dataset.getMetadata().getAttributes();
        this.datasetSelection = datasetSelection;

        uniqueId = dataset.getId();
        if (datasetSelection != null && datasetSelection.isUserSelection()) {
            uniqueId = generateMatrixFromString(dataset.getId());
        }

        name = dataset.getName();
        id = dataset.getId();
        urn = dataset.getUrn();
        description = dataset.getDescription();

        this.dataset = dataset;
        metadata = dataset.getMetadata();

        initialize(data, dimensions, attributes, datasetSelection, selectedLanguages);
    }

    private void initialize(Data data, Dimensions dimensions, Attributes attributes, DatasetSelection datasetSelection, List<String> selectedLanguages) throws MetamacException {
        this.setSelectedLanguages(selectedLanguages);

        initializeDimensions(dimensions, datasetSelection);
        initializeAttributes(data, attributes, datasetSelection);
        initializeObservations(data);
        initializeDimensionsForData(data);
        initializeMultipliers();
        initializeIndex();
    }

    public DatasetSelection getDataSelection() {
        return datasetSelection;
    }

    public Data getData() {
        return data;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public InternationalString getName() {
        return name;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public DatasetMetadata getMetadata() {
        return metadata;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getId() {
        return id;
    }

    public String getUrn() {
        return urn;
    }

    public InternationalString getDescription() {
        return description;
    }

    public List<Dimension> getDimensionsMetadata() {
        return dimensionsMetadata;
    }

    public Map<String, Dimension> getDimensionsMetadataMap() {
        return dimensionsMetadataMap;
    }

    public List<Attribute> getAttributesMetadata() {
        return attributesMetadata;
    }

    public Map<String, Attribute> getAttributesMetadataMap() {
        return attributesMetadataMap;
    }

    public Attribute getMeasureAttribute() {
        return measureAttribute;
    }

    public Dimension getMeasureDimension() {
        return measureDimension;
    }

    public InternationalString getDimensionLabelCurrentLocale(String dimensionId) {
        return dimensionLabelsCurrentLocale.get(dimensionId);
    }

    public InternationalString getDimensionLabelDefaultLocale(String dimensionId) {
        return dimensionLabelsDefaultLocale.get(dimensionId);
    }

    public InternationalString getDimensionValueLabelCurrentLocale(String dimensionId, String dimensionValueId) {
        return dimensionsValuesCurrentLocaleLabels.get(dimensionId).get(dimensionValueId);
    }

    public InternationalString getDimensionValueLabel(String dimensionId, String dimensionValueId) {
        return dimensionsValuesLabels.get(dimensionId).get(dimensionValueId);
    }

    public InternationalString getAttributeLabel(String attributeId) {
        return attributesLabels.get(attributeId);
    }

    public InternationalString getAttributeValueLabelCurrentLocale(String attributeId, String attributeValue) {
        return attributesValuesCurrentLocaleLabels.get(attributeId).get(attributeValue);
    }

    public InternationalString getAttributeValue(String attributeId, String attributeValue) {
        return attributesValuesLabels.get(attributeId).get(attributeValue);
    }

    public LabelVisualisationModeEnum getDimensionLabelVisualisationMode(String dimensionId) {
        return dimensionsLabelVisualisationMode.get(dimensionId);
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

    public List<String> getDimensionValuesOrderedForData(String dimensionId) {
        return dimensionValuesOrderedForDataByDimensionId.get(dimensionId);
    }

    /**
     * Init dimensions and dimensions values
     */
    private void initializeDimensions(Dimensions dimensions, DatasetSelection datasetSelection) throws MetamacException {
        dimensionsMetadata = dimensions.getDimensions();

        Map<String, Dimension> metadataMap = new HashMap<String, Dimension>(dimensionsMetadata.size());
        Map<String, LabelVisualisationModeEnum> labelVisualisationsMode = new HashMap<String, LabelVisualisationModeEnum>(dimensionsMetadata.size());
        Map<String, Map<String, InternationalString>> valuesCurrentLocaleLabels = new HashMap<String, Map<String, InternationalString>>(dimensionsMetadata.size());
        Map<String, Map<String, InternationalString>> valuesLabels = new HashMap<String, Map<String, InternationalString>>(dimensionsMetadata.size());
        Map<String, InternationalString> dimensionsLabelsCurrentLocale = new HashMap<String, InternationalString>(dimensionsMetadata.size());
        Map<String, InternationalString> dimensionsLabelsDefaultLocale = new HashMap<String, InternationalString>(dimensionsMetadata.size());

        for (Dimension dimension : dimensionsMetadata) {
            String dimensionId = dimension.getId();

            metadataMap.put(dimensionId, dimension);
            labelVisualisationsMode.put(dimensionId, buildMapDimensionToMapDimensionsLabelVisualisationMode(datasetSelection, dimension));
            valuesCurrentLocaleLabels.put(dimension.getId(), buildMapDimensionsValuesLabels(dimension));
            valuesLabels.put(dimension.getId(), buildMapDimensionsValuesLocalisedLabels(dimension));
            dimensionsLabelsCurrentLocale.put(dimensionId, dimension.getName());
            dimensionsLabelsDefaultLocale.put(dimensionId, dimension.getName());

            if (DimensionType.MEASURE_DIMENSION.equals(dimension.getType())) {
                measureDimension = dimension;
            }
        }

        this.dimensionsMetadataMap = metadataMap;
        dimensionsLabelVisualisationMode = labelVisualisationsMode;
        this.dimensionsValuesCurrentLocaleLabels = valuesCurrentLocaleLabels;
        this.dimensionsValuesLabels = valuesLabels;
        dimensionLabelsCurrentLocale = dimensionsLabelsCurrentLocale;
        dimensionLabelsDefaultLocale = dimensionsLabelsDefaultLocale;
    }

    private int calculateNonEmptyCount(String[] strings) {
        if (strings == null) {
            return 0;
        }
        int totalNonEmpty = 0;
        for (int i = 0; i < strings.length; i++) {
            if (StringUtils.isNotBlank(strings[i])) {
                totalNonEmpty++;
            }
        }
        return totalNonEmpty;
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
        dimensionValuesOrderedForDataByDimensionId = new HashMap<String, List<String>>(dimensionRepresentations.size());
        for (DimensionRepresentation dimensionRepresentation : dimensionRepresentations) {
            String dimensionId = dimensionRepresentation.getDimensionId();
            dimensionsOrderedForData.add(dimensionId);

            List<CodeRepresentation> codesRepresentations = dimensionRepresentation.getRepresentations().getRepresentations();
            dimensionValuesOrderedForDataByDimensionId.put(dimensionId, new ArrayList<String>(codesRepresentations.size()));
            for (CodeRepresentation codeRepresentation : codesRepresentations) {
                dimensionValuesOrderedForDataByDimensionId.get(dimensionId).add(codeRepresentation.getCode());
            }
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

    private void initializeMultipliers() {
        List<DimensionRepresentation> dimensionsRepresentation = getData().getDimensions().getDimensions();
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
    private void initializeIndex() {
        List<DimensionRepresentation> dimensionsRepresentation = getData().getDimensions().getDimensions();
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

    public boolean existsContVariable() {
        return getMeasureDimension() != null;
    }

    public int getPrimaryMeasureAttributesCount() {
        return primaryMeasureAttributesCount;
    }

    /**
     * Init definitions and values of attributes
     *
     * @param attributes
     */
    private void initializeAttributes(Data data, Attributes attributes, DatasetSelection datasetSelection) throws MetamacException {
        if (attributes == null) {
            attributesMetadata = new ArrayList<Attribute>();
        } else {
            attributesMetadata = attributes.getAttributes();
        }

        Map<String, Attribute> attrMetadataMap = new HashMap<String, Attribute>(attributesMetadata.size());

        // Attribute Instances
        attributesValuesByAttributeId = new HashMap<String, String[]>(attributesMetadata.size());
        for (Attribute attribute : attributesMetadata) {
            if (data.getAttributes() != null) {
                for (DataAttribute dataAttribute : data.getAttributes().getAttributes()) {
                    if (dataAttribute.getId().equals(attribute.getId())) {
                        attributesValuesByAttributeId.put(attribute.getId(), ExportUtils.dataToDataArray(dataAttribute.getValue()));
                    }
                }
            }

            if (AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attribute.getAttachmentLevel())) {
                primaryMeasureAttributesCount += calculateNonEmptyCount(attributesValuesByAttributeId.get(attribute.getId()));
            }

            // Measure Attribute
            if (ComponentType.MEASURE.equals(attribute.getType())) {
                measureAttribute = attribute;
            }

            // Attributes Metadata Map
            attrMetadataMap.put(attribute.getId(), attribute);
        }

        this.attributesMetadataMap = attrMetadataMap;
        attributesLabelVisualisationMode = ExportUtils.buildMapAttributesLabelVisualisationMode(datasetSelection, attributesMetadata);
        attributesValuesCurrentLocaleLabels = ExportUtils.buildMapAttributesValuesLabels(attributesMetadata);
        attributesValuesLabels = ExportUtils.buildMapAttributesValuesLocalisedLabels(attributesMetadata);
        attributesLabels = ExportUtils.buildMapAttributesLabels(attributesMetadata);
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
}