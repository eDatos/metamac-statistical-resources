package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.jsonstat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatCategory;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatDimension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatDimensionExtension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatExtension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatUnit;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Quantity;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.dataset.domain.AttributeValue;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentations;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimensions;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedAttributeValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedAttributeValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedDimensionValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedDimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.external.service.utils.DsdExternalProcessor;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.DsdProcessorResult;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base.CommonDo2RestMapperV10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonDo2JsonStatRestMapperV10Impl implements CommonDo2JsonStatRestMapperV10 {

    public static final List<DsdExternalProcessor.DsdComponentType> DIMENSIONLIKE_ATTRIBUTES = Arrays.asList(DsdExternalProcessor.DsdComponentType.MEASURE,
            DsdExternalProcessor.DsdComponentType.TEMPORAL, DsdExternalProcessor.DsdComponentType.SPATIAL);

    private static final Logger                                     LOGGER                   = LoggerFactory.getLogger(CommonDo2JsonStatRestMapperV10Impl.class);

    @Autowired
    private StatisticalResourcesConfiguration                       configurationService;

    @Autowired
    private CommonDo2RestMapperV10                                  commonDo2RestMapper;

    @Override
    public Map<String, JsonStatDimension> toJsonStatDatasetDimensions(Dimensions dimensions, DimensionRepresentations dimensionRepresentations, DsdProcessorResult dsdProcessorResult,
                                                                      Attributes attributes, DataAttributes dataAttributes, String selectedLanguage) throws Exception {
        String unitMeasureId = configurationService.retrieveUnitMeasure();
        String unitMeasureMultiplierId = configurationService.retrieveUnitMeasureMultiplier();

        Attribute unitMeasureAttribute = findAttributeById(attributes, unitMeasureId);
        Attribute unitMeasureMultiplierAttribute = findAttributeById(attributes, unitMeasureMultiplierId);

        List<Concept> measureConcepts = getConcepts(dimensions);

        Map<String, JsonStatDimension> jsonStatDimensionMap = new HashMap<>();
        for (DimensionRepresentation dimension : dimensionRepresentations.getDimensions()) {
            JsonStatDimension jsonStatDimension = new JsonStatDimension();
            jsonStatDimension.setLabel(toDimensionI18nName(dimensions, dimension.getDimensionId(), selectedLanguage));
            jsonStatDimension.setCategory(new JsonStatCategory());

            Map<String, Long> indexMap = new HashMap<>();
            Map<String, String> labelMap = new HashMap<>();
            Map<String, JsonStatUnit> unitMap = new HashMap<>();

            boolean isMeasureDimension = isMeasure(dimensions, dimension);

            for (CodeRepresentation category : dimension.getRepresentations().getRepresentations()) {
                indexMap.put(category.getCode(), category.getIndex());
                labelMap.put(category.getCode(), toCategoryI18nName(dimensions, dimension.getDimensionId(), category, selectedLanguage));
                Concept concept = findConceptById(measureConcepts, category);

                if (isMeasureDimension) {
                    setUnitIfExists(unitMap, category, concept, unitMeasureAttribute, unitMeasureMultiplierAttribute, dataAttributes, selectedLanguage);
                }
            }

            jsonStatDimension.getCategory().setIndex(indexMap);
            jsonStatDimension.getCategory().setLabel(labelMap);
            if (!unitMap.isEmpty()) {
                jsonStatDimension.getCategory().setUnit(unitMap);
            }

            jsonStatDimensionMap.put(dimension.getDimensionId(), jsonStatDimension);
        }

        for (DsdExternalProcessor.DsdAttribute dsdAttribute : dsdProcessorResult.getAttributes()) {
            if (DIMENSIONLIKE_ATTRIBUTES.contains(dsdAttribute.getType())) {
                JsonStatDimension jsonStatDimension = new JsonStatDimension();
                jsonStatDimension.setLabel(toI18nValue(dsdAttribute.getConceptIdentity().getName(), selectedLanguage));
                jsonStatDimension.setCategory(new JsonStatCategory());

                Map<String, Long> indexMap = new HashMap<>();
                Map<String, String> labelMap = new HashMap<>();
                Map<String, JsonStatUnit> unitMap = new HashMap<>();

                Attribute attribute = getAttributeFromDsd(attributes, dsdAttribute);
                Map<String, InternationalString> attributeValueMap = getAttributeValues(attribute);
                if (attributeValueMap.size() != 1) {
                    // attributes that function as a dimension should only have one value
                    LOGGER.debug("Attribute `{}` has {} values, it should have only 1", dsdAttribute.getComponentId(), attributeValueMap.size());
                    continue;
                }

                Map.Entry<String, InternationalString> attributeValueEntry = attributeValueMap.entrySet().iterator().next();
                indexMap.put(attributeValueEntry.getKey(), 0L);
                labelMap.put(attributeValueEntry.getKey(), toI18nValue(attributeValueEntry.getValue(), selectedLanguage));
                Concept concept = findConceptById(measureConcepts, dsdAttribute);
                if (isMeasure(dsdAttribute)) {
                    setUnitIfExists(unitMap, attributeValueEntry, concept, unitMeasureAttribute, unitMeasureMultiplierAttribute, dataAttributes, selectedLanguage);
                }

                jsonStatDimension.getCategory().setIndex(indexMap);
                jsonStatDimension.getCategory().setLabel(labelMap);
                if (!unitMap.isEmpty()) {
                    jsonStatDimension.getCategory().setUnit(unitMap);
                }

                jsonStatDimensionMap.put(dsdAttribute.getComponentId(), jsonStatDimension);
            }
        }

        return jsonStatDimensionMap;
    }

    private Attribute getAttributeFromDsd(Attributes attributes, DsdExternalProcessor.DsdAttribute dsdAttribute) {
        if (attributes != null && attributes.getAttributes() != null) {
            for (Attribute attribute : attributes.getAttributes()) {
                if (attribute.getId().equals(dsdAttribute.getComponentId())) {
                    return attribute;
                }
            }
        }
        return null;
    }

    private Concept findConceptById(List<Concept> measureConcepts, DsdExternalProcessor.DsdAttribute dsdAttribute) {
        for (Concept concept : measureConcepts) {
            if (concept.getId().equals(dsdAttribute.getComponentId())) {
                return concept;
            }
        }
        return null;
    }

    private void setUnitIfExists(Map<String, JsonStatUnit> unitMap, Map.Entry<String, InternationalString> attributeValueEntry, Concept concept, Attribute unitMeasureAttribute,
            Attribute unitMeasureMultiplierAttribute, DataAttributes dataAttributes, String selectedLanguage) {
        if ((unitMeasureAttribute != null && unitMeasureAttribute.getAttributeValues() != null)
                || (unitMeasureMultiplierAttribute != null && unitMeasureMultiplierAttribute.getAttributeValues() != null)) {
            unitMap.put(attributeValueEntry.getKey(), toUnit(0, unitMeasureAttribute, unitMeasureMultiplierAttribute, selectedLanguage, dataAttributes));
        } else if (concept != null && concept.getQuantity() != null) {
            unitMap.put(attributeValueEntry.getKey(), toUnit(concept, selectedLanguage));
        }
    }

    private boolean isMeasure(DsdExternalProcessor.DsdAttribute dsdAttribute) {
        return dsdAttribute.getType().equals(DsdExternalProcessor.DsdComponentType.MEASURE);
    }

    private void setUnitIfExists(Map<String, JsonStatUnit> unitMap, CodeRepresentation category, Concept concept, Attribute unitMeasureAttribute, Attribute unitMeasureMultiplierAttribute,
                                 DataAttributes dataAttributes, String selectedLanguage) {

        if ((unitMeasureAttribute != null && unitMeasureAttribute.getAttributeValues() != null)
                || (unitMeasureMultiplierAttribute != null && unitMeasureMultiplierAttribute.getAttributeValues() != null)) {
            unitMap.put(category.getCode(), toUnit((int) category.getIndex(), unitMeasureAttribute, unitMeasureMultiplierAttribute, selectedLanguage, dataAttributes));
        } else if (concept != null && concept.getQuantity() != null) {
            unitMap.put(category.getCode(), toUnit(concept, selectedLanguage));
        }
    }

    private static Concept findConceptById(List<Concept> measureConcepts, CodeRepresentation category) {
        for (Concept concept : measureConcepts) {
            if (concept.getId().equals(category.getCode())) {
                return concept;
            }
        }
        return null;
    }

    private List<Concept> getConcepts(Dimensions dimensions) {
        List<Concept> measureConcepts = new ArrayList<>();
        for (Dimension dim : dimensions.getDimensions()) {
            if (dim.getType().equals(DimensionType.MEASURE_DIMENSION)) {
                Concept measure = commonDo2RestMapper.toConcept(((EnumeratedDimensionValues) dim.getDimensionValues()).getValues().get(0).getUrn());
                measureConcepts.add(measure);
            }
        }
        return measureConcepts;
    }

    private static boolean isMeasure(Dimensions dimensions, DimensionRepresentation dimension) {
        boolean isMeasureDimension = false;
        for (Dimension dim : dimensions.getDimensions()) {
            if (dim.getId().equals(dimension.getDimensionId()) && dim.getType() == DimensionType.MEASURE_DIMENSION) {
                isMeasureDimension = true;
                break;
            }
        }
        return isMeasureDimension;
    }

    private Attribute findAttributeById(Attributes attributes, String id) {
        if (attributes != null && attributes.getAttributes() != null) {
            for (Attribute attribute : attributes.getAttributes()) {
                if (attribute.getId().equals(id)) {
                    return attribute;
                }
            }
        }
        return null;
    }

    private JsonStatUnit toUnit(Concept concept, String selectedLanguage) {
        JsonStatUnit unit = new JsonStatUnit();
        Quantity quantity = concept.getQuantity();
        unit.setLabel(quantity.getUnitCode() != null ? toI18nValue(quantity.getUnitCode().getName(), selectedLanguage) : null);
        unit.setMultiplier(quantity.getUnitMultiplier() != null ? toI18nValue(quantity.getUnitMultiplier().getName(), selectedLanguage) : null);
        unit.setPosition(quantity.getUnitSymbolPosition() != null ? quantity.getUnitSymbolPosition().value().toLowerCase() : null);
        unit.setDecimalPlaces(quantity.getDecimalPlaces() != null ? quantity.getDecimalPlaces() : null);
        return unit;
    }
    
    private JsonStatUnit toUnit(int index, Attribute unitMeasureAttribute, Attribute unitMeasureMultiplierAttribute, String selectedLanguage, DataAttributes dataAttributes) {
        JsonStatUnit unit = new JsonStatUnit();

        String unitLabel = processAttribute(index, unitMeasureAttribute, selectedLanguage, dataAttributes);
        if (unitLabel != null) {
            unit.setLabel(unitLabel);
        }
        String unitMultiplier = processAttribute(index, unitMeasureMultiplierAttribute, selectedLanguage, dataAttributes);
        if (unitMultiplier != null) {
            unit.setMultiplier(unitMultiplier);
        }

        return unit;
    }

    private String processAttribute(int index, Attribute attribute, String selectedLanguage, DataAttributes dataAttributes) {
        // On the matter about why we need Attribute and DataAttributes:
        // Attributes contain the actual value information needed for Json-STAT. However, they are not
        // sorted. To know the order in which they are associated to value dimensions, DataAttributes are needed.
        // They are adequately sorted but do not provide more information than the ID, which for Json-STAT is
        // not enough.
        // So we obtain the info from the Attribute class, and adequately associate the attributes to their respective
        // dimension values though the order in DataAttributes class.

        if (attribute != null && attribute.getAttributeValues() != null) {
            String[] dataAttributeValues = null;
            for (DataAttribute dataAtt : dataAttributes.getAttributes()) {
                if (dataAtt.getId().equals(attribute.getId())) {
                    dataAttributeValues = dataAtt.getValue().split(" \\| ");
                    break;
                }
            }

            if (dataAttributeValues != null && dataAttributeValues.length > index) {
                EnumeratedAttributeValues attributeValues = (EnumeratedAttributeValues) attribute.getAttributeValues();
                String attributeValueId = dataAttributeValues[index];
                for (EnumeratedAttributeValue attributeValue : attributeValues.getValues()) {
                    if (attributeValue.getId().equals(attributeValueId)) {
                        InternationalString label = attributeValue.getName();
                        return toI18nValue(label, selectedLanguage);
                    }
                }

            }
        }
        return null;
    }


    private static Map<String, InternationalString> getAttributeValues(Attribute attribute) {
        Map<String, InternationalString> attributes = new HashMap<>();
        if (attribute.getAttributeValues() instanceof EnumeratedAttributeValues) {
            for (EnumeratedAttributeValue attributeValue : ((EnumeratedAttributeValues) attribute.getAttributeValues()).getValues()) {
                attributes.put(attributeValue.getId(), attributeValue.getName());
            }
        } else if (attribute.getAttributeValues() instanceof NonEnumeratedAttributeValues) {
            for (NonEnumeratedAttributeValue attributeValue : ((NonEnumeratedAttributeValues) attribute.getAttributeValues()).getValues()) {
                attributes.put(attributeValue.getId(), attributeValue.getName());
            }
        }
        return attributes;
    }

    private String toCategoryI18nName(Dimensions dimensions, String dimensionId, CodeRepresentation category, String selectedLanguage) {
        for (Dimension dimension : dimensions.getDimensions()) {
            if (Objects.equals(dimension.getId(), dimensionId)) {
                DimensionValues dimensionValues = dimension.getDimensionValues();
                if (dimensionValues instanceof EnumeratedDimensionValues) {
                    for (EnumeratedDimensionValue value : ((EnumeratedDimensionValues) dimensionValues).getValues()) {
                        if (Objects.equals(value.getId(), category.getCode())) {
                            return toI18nValue(value.getName(), selectedLanguage);
                        }
                    }
                } else if (dimensionValues instanceof NonEnumeratedDimensionValues) {
                    for (NonEnumeratedDimensionValue value : ((NonEnumeratedDimensionValues) dimensionValues).getValues()) {
                        if (Objects.equals(value.getId(), category.getCode())) {
                            return toI18nValue(value.getName(), selectedLanguage);
                        }
                    }
                }
            }
        }
        return null;
    }

    public String toDimensionI18nName(Dimensions dimensions, String dimensionId, String selectedLanguage) {
        for (Dimension dimension : dimensions.getDimensions()) {
            if (Objects.equals(dimension.getId(), dimensionId)) {
                return toI18nValue(dimension.getName(), selectedLanguage);
            }
        }
        return null;
    }

    @Override
    public String getSelectedLanguage(DatasetVersion source, List<String> selectedLanguages) {
        String selectedLanguage = selectedLanguages.isEmpty() ? null : selectedLanguages.get(0);

        String sourceLang = source.getSiemacMetadataStatisticalResource().getLanguage().getCode();
        if (selectedLanguage == null && sourceLang != null) {
            selectedLanguage = sourceLang.toLowerCase();
        }

        return selectedLanguage;
    }

    @Override
    public List<String> toJsonStatNote(DatasetVersion source, Data data, Dimensions dimensions, Attributes attributes, DsdProcessorResult dsdProcessorResult, String selectedLanguage) {
        List<String> notes = new ArrayList<>();
        // Discard attributes that are at observation level, we do not want those to appear on the notes
        List<DsdExternalProcessor.DsdAttribute> attributesThatAreNotAtObservationLevel = new ArrayList<>();
        for (DsdExternalProcessor.DsdAttribute dsdAttribute : dsdProcessorResult.getAttributes()) {
            if (!dsdAttribute.isAttributeAtObservationLevel()) {
                attributesThatAreNotAtObservationLevel.add(dsdAttribute);
            }
        }
        for (DsdExternalProcessor.DsdAttribute attribute : attributesThatAreNotAtObservationLevel) {
            if (attribute.getAttributeRelationship().getNone() != null) {
                notes.addAll(getNotesForDatasetLevelAttribute(attribute, source));
            } else if (!attribute.getAttributeRelationship().getDimensions().isEmpty() || attribute.getAttributeRelationship().getGroup() != null) {
                notes.addAll(getNotesForAttributesAssociatedToCategories(attribute, dimensions, attributes, selectedLanguage, data, dsdProcessorResult));
            }
        }
        return notes;
    }

    private List<String> getNotesForDatasetLevelAttribute(DsdExternalProcessor.DsdAttribute attribute, DatasetVersion source) {
        if (DIMENSIONLIKE_ATTRIBUTES.contains(attribute.getType())) {
            return Collections.emptyList(); // don't include in the notes dsdAttributes of spatial, measure or temporal type
        }

        List<String> notes = new ArrayList<>();
        // To find the value of the attribute we need to look up the dataset attribute coverage.
        AttributeValue attributeCoverage = getAttributeCoverageByComponentId(source.getAttributesCoverage(), attribute.getComponentId());
        if (attributeCoverage != null) {
            String note = attributeCoverage.getTitle();
            notes.add(note);
        }
        return notes;
    }

    private AttributeValue getAttributeCoverageByComponentId(List<AttributeValue> attributesCoverage, String componentId) {
        if (componentId == null) {
            return null;
        }
        for (AttributeValue attributeCoverage : attributesCoverage) {
            if (Objects.equals(attributeCoverage.getDsdComponentId(), componentId)) {
                return attributeCoverage;
            }
        }
        return null;
    }

    private List<String> getNotesForAttributesAssociatedToCategories(DsdExternalProcessor.DsdAttribute attribute, Dimensions dimensions, Attributes attributes, String selectedLanguage, Data data,
            DsdProcessorResult dsdProcessorResult) {
        List<String> notes = new ArrayList<>();
        List<DimensionRepresentation> attributeAssociatedDimensions = getDimensionsAssociatedToAttribute(attribute, data, dsdProcessorResult);
        DataAttribute dataAttribute = getAttributeFromDatasetData(attribute, data);
        if (dataAttribute == null) {
            return notes;
        }
        List<String> attributeValues = new ArrayList<>();
        for (String attributeValue : dataAttribute.getValue().split("\\|")) {
            attributeValues.add(toAttributeI18nName(attributes, attribute.getComponentId(), attributeValue.trim(), selectedLanguage));
        }
        notes.addAll(getAttributeValuesNotes(dimensions, selectedLanguage, attributeAssociatedDimensions, attributeValues));
        return notes;
    }

    private List<DimensionRepresentation> getDimensionsAssociatedToAttribute(DsdExternalProcessor.DsdAttribute attribute, Data data, DsdProcessorResult dsdProcessorResult) {
        List<DimensionRepresentation> attributeAssociatedDimensions = new ArrayList<>();
        if (!attribute.getAttributeRelationship().getDimensions().isEmpty()) {
            // get dimensions when those have been declared directly in the attribute
            for (DimensionRepresentation dimensionRepresentation : data.getDimensions().getDimensions()) {
                if (attribute.getAttributeRelationship().getDimensions().contains(dimensionRepresentation.getDimensionId())) {
                    attributeAssociatedDimensions.add(dimensionRepresentation);
                }
            }
        } else if (attribute.getAttributeRelationship().getGroup() != null) {
            // process dimensions when those have been declared as a group
            for (DimensionRepresentation dimensionRepresentation : data.getDimensions().getDimensions()) {
                if (dsdProcessorResult.getGroups().get(attribute.getAttributeRelationship().getGroup()).contains(dimensionRepresentation.getDimensionId())) {
                    attributeAssociatedDimensions.add(dimensionRepresentation);
                }
            }
        }
        return attributeAssociatedDimensions;
    }

    private DataAttribute getAttributeFromDatasetData(DsdExternalProcessor.DsdAttribute attribute, Data data) {
        DataAttribute dataAttribute = null;
        if (data.getAttributes() != null) {
            for (DataAttribute da : data.getAttributes().getAttributes()) {
                if (Objects.equals(da.getId(), attribute.getComponentId())) {
                    dataAttribute = da;
                    break;
                }
            }
        }
        return dataAttribute;
    }

    private List<String> getAttributeValuesNotes(Dimensions dimensions, String selectedLanguage, List<DimensionRepresentation> attributeAssociatedDimensions, List<String> attributeValues) {
        // The attribute values are in "row-major" order, based on the dimensions we processed from the dataset data.
        // That means dimensions order is important to map the categories to the right value.

        // Since attribute values can be attached to any amount of dimensions, we need to generalize the access of those
        // values. For example, consider 2 dimensions (A and B), each with 2 and 3 categories, respectively. Values are
        // in row-major order, we iterate over the values A1B1, A1B2, A1B3, A2B1, A2B2 and A2B3.
        int[] size = new int[attributeAssociatedDimensions.size()];
        for (int i = 0; i < attributeAssociatedDimensions.size(); i++) {
            DimensionRepresentation dimension = attributeAssociatedDimensions.get(i);
            int categoriesSize = dimension.getRepresentations().getTotal().intValue();
            size[i] = categoriesSize;
        }

        int[] index = new int[size.length];

        List<String> notes = new ArrayList<>();
        populateNotes(0, size, index, dimensions, selectedLanguage, notes, attributeAssociatedDimensions, attributeValues);
        return notes;
    }

    private void populateNotes(int currentDimension, int size[], int index[], Dimensions dimensions, String selectedLanguage, List<String> notes,
            List<DimensionRepresentation> attributeAssociatedDimensions, List<String> attributeValues) {
        if (currentDimension >= size.length) {
            String note = getNoteFromAttributePossition(dimensions, selectedLanguage, attributeAssociatedDimensions, attributeValues, index);
            if (note != null) {
                notes.add(note);
            }
            return;
        }
        for (int i = 0; i < size[currentDimension]; i++) {
            index[currentDimension] = i;
            populateNotes(currentDimension + 1, size, index, dimensions, selectedLanguage, notes, attributeAssociatedDimensions, attributeValues);
        }
    }

    private String toAttributeI18nName(Attributes attributes, String attributeId, String attributeCode, String selectedLanguage) {
        if (attributes != null && attributes.getAttributes() != null) {
            for (Attribute att : attributes.getAttributes()) {
                if (Objects.equals(att.getId(), attributeId)) {
                    AttributeValues attributeValues = att.getAttributeValues();
                    if (attributeValues instanceof EnumeratedAttributeValues) {
                        for (EnumeratedAttributeValue value : ((EnumeratedAttributeValues) attributeValues).getValues()) {
                            if (Objects.equals(value.getId(), attributeCode)) {
                                return toI18nValue(value.getName(), selectedLanguage);
                            }
                        }
                    } else if (attributeValues instanceof NonEnumeratedAttributeValues) {
                        for (NonEnumeratedAttributeValue value : ((NonEnumeratedAttributeValues) attributeValues).getValues()) {
                            if (Objects.equals(value.getId(), attributeCode)) {
                                return toI18nValue(value.getName(), selectedLanguage);
                            }
                        }
                    }
                }
            }
        }
        return attributeCode; // some attributes are not declared as i18n
    }

    private String getNoteFromAttributePossition(Dimensions dimensions, String selectedLanguage, List<DimensionRepresentation> attributeAssociatedDimensions, List<String> attributeValues,
            int[] index) {
        String value = getValueFromPosition(attributeAssociatedDimensions, attributeValues, index);
        if (!StringUtils.isBlank(value)) {
            List<String> categories = new ArrayList<>();
            for (int i = 0; i < attributeAssociatedDimensions.size(); i++) {
                DimensionRepresentation dimension = attributeAssociatedDimensions.get(i);
                CodeRepresentation category = dimension.getRepresentations().getRepresentations().get(index[i]);
                categories.add(toCategoryI18nName(dimensions, dimension.getDimensionId(), category, selectedLanguage));
            }
            return String.join(", ", categories) + ". " + value;
        }
        return null;
    }

    @Override
    public String getValueFromPosition(List<DimensionRepresentation> dimensions, List<String> values, int... position) {
        int index = 0;
        for (int i = 0; i < position.length; i++) {
            int multiplicator = position[i];
            int j = i + 1;
            while (j < position.length) {
                multiplicator *= dimensions.get(j).getRepresentations().getTotal().intValueExact();
                j++;
            }
            index += multiplicator;
        }
        return values.get(index).trim();
    }

    @Override
    public List<String> getJsonStatId(Data data, DsdProcessorResult dsdAttributes) {
        List<String> id = new ArrayList<>();
        for (DimensionRepresentation dim : data.getDimensions().getDimensions()) {
            id.add(dim.getDimensionId());
        }
        for (DsdExternalProcessor.DsdAttribute dsdAttribute : dsdAttributes.getAttributes()) {
            if (DIMENSIONLIKE_ATTRIBUTES.contains(dsdAttribute.getType())) {
                id.add(dsdAttribute.getComponentId());
            }
        }
        return id;
    }

    @Override
    public List<Long> toJsonStatSize(Data data, DsdProcessorResult dsdProcessorResult, Attributes attributes) {
        List<Long> dimensionSizes = new ArrayList<>();
        for (DimensionRepresentation dimension : data.getDimensions().getDimensions()) {
            long size = dimension.getRepresentations().getTotal().longValue();
            dimensionSizes.add(size);
        }
        for (DsdExternalProcessor.DsdAttribute dsdAttribute : dsdProcessorResult.getAttributes()) {
            if (DIMENSIONLIKE_ATTRIBUTES.contains(dsdAttribute.getType())) {
                Map<String, InternationalString> attributeValues = getAttributeValues(getAttributeFromDsd(attributes, dsdAttribute));
                dimensionSizes.add((long) attributeValues.size());
            }
        }
        return dimensionSizes;
    }

    @Override
    public JsonStatExtension toJsonStatExtension(DatasetVersion source, Dimensions dimensions, String selectedLanguage) {
        JsonStatExtension extension = new JsonStatExtension();
        extension.setDatasetId(source.getSiemacMetadataStatisticalResource().getCode());
        extension.setDatasetUrn(source.getSiemacMetadataStatisticalResource().getUrn());
        extension.setSurvey(toI18nValue(source.getSiemacMetadataStatisticalResource().getStatisticalOperation().getTitle(), selectedLanguage));
        extension.setLang(joinExternalItemCodes(source.getSiemacMetadataStatisticalResource().getLanguages()));
        extension.setPublishers(joinExternalItemTitles(source.getSiemacMetadataStatisticalResource().getPublisher(), selectedLanguage));
        extension.setDataProviders(joinExternalItemTitles(source.getSiemacMetadataStatisticalResource().getDataProvider(), selectedLanguage));
        extension.setDataProvidersAnnotations(toI18nValue(source.getSiemacMetadataStatisticalResource().getDataProviderAnnotations(), selectedLanguage));
        extension.setDimension(toJsonStatDimensionExtensionMap(dimensions));
        return extension;
    }

    private Map<String, JsonStatDimensionExtension> toJsonStatDimensionExtensionMap(Dimensions dimensions) {
        Map<String, JsonStatDimensionExtension> dimensionExtensions = new HashMap<>();

        List<Dimension> geographicDimensions = findDimensions(dimensions, DimensionType.GEOGRAPHIC_DIMENSION);
        for (Dimension geographicDimension : geographicDimensions) {
            JsonStatDimensionExtension jsonStatDimensionExtension = toJsonStatEnumeratedDimensionExtensionGranularity(geographicDimension);
            dimensionExtensions.put(geographicDimension.getId(), jsonStatDimensionExtension);
        }


        List<Dimension> temporalDimensions = findDimensions(dimensions, DimensionType.TIME_DIMENSION);
        for (Dimension temporalDimension : temporalDimensions) {
            JsonStatDimensionExtension jsonStatDimensionExtension = toJsonStatNonEnumeratedDimensionExtensionGranularity(temporalDimension);
            dimensionExtensions.put(temporalDimension.getId(), jsonStatDimensionExtension);
        }

        return dimensionExtensions;
    }

    private List<Dimension> findDimensions(Dimensions dimensions, DimensionType type) {
        List<Dimension> filteredDimensions = new ArrayList<>();
        for (Dimension dimension : dimensions.getDimensions()) {
            if (dimension.getType() == type) {
                filteredDimensions.add(dimension);
            }
        }
        return filteredDimensions;
    }

    private String joinExternalItemCodes(List<ExternalItem> externalItemList) {
        if (CollectionUtils.isEmpty(externalItemList)) {
            return null;
        }

        StringJoiner joiner = new StringJoiner(",");
        for (ExternalItem externalItem : externalItemList) {
            String title = externalItem.getCode();
            joiner.add(title);
        }
        return joiner.toString();
    }

    private JsonStatDimensionExtension toJsonStatEnumeratedDimensionExtensionGranularity(Dimension dimensions) {
        if (!(dimensions.getDimensionValues() instanceof EnumeratedDimensionValues)) {
            return null;
        }

        JsonStatDimensionExtension jsonStatDimensionExtension = new JsonStatDimensionExtension();
        Map<String, String> geographicalGranularity = new HashMap<>();
        for (EnumeratedDimensionValue dimValue : ((EnumeratedDimensionValues) dimensions.getDimensionValues()).getValues()) {
            ResourceStatisticalResourceBase geographicGranularity = dimValue.getGeographicGranularity();
            geographicalGranularity.put(dimValue.getId(), geographicGranularity.getId());
        }
        jsonStatDimensionExtension.setGeographicalGranularity(geographicalGranularity);
        return jsonStatDimensionExtension;
    }

    private JsonStatDimensionExtension toJsonStatNonEnumeratedDimensionExtensionGranularity(Dimension dimensions) {
        if (!(dimensions.getDimensionValues() instanceof NonEnumeratedDimensionValues)) {
            return null;
        }

        JsonStatDimensionExtension jsonStatDimensionExtension = new JsonStatDimensionExtension();
        Map<String, String> temporalGranularities = new HashMap<>();
        for (NonEnumeratedDimensionValue dimValue : ((NonEnumeratedDimensionValues) dimensions.getDimensionValues()).getValues()) {
            String temporalGranularity = dimValue.getTemporalGranularity();
            temporalGranularities.put(dimValue.getId(), temporalGranularity);
        }
        jsonStatDimensionExtension.setTemporalGranularity(temporalGranularities);
        return jsonStatDimensionExtension;
    }

    private String joinExternalItemTitles(List<ExternalItem> externalItemList, String selectedLanguage) {
        if (CollectionUtils.isEmpty(externalItemList)) {
            return null;
        }

        StringJoiner joiner = new StringJoiner(", ");
        for (ExternalItem externalItem : externalItemList) {
            String title = toI18nValue(externalItem.getTitle(), selectedLanguage);
            joiner.add(title);
        }
        return joiner.toString();
    }

    @Override
    public Map<String, List<String>> toJsonStatRoles(DsdProcessorResult dsdProcessorResult) throws MetamacException {
        Map<String, List<String>> roles = new HashMap<>();
        for (DsdExternalProcessor.DsdDimension dimension : dsdProcessorResult.getDimensions()) {
            setRoles(roles, dimension);
        }
        for (DsdExternalProcessor.DsdAttribute attribute : dsdProcessorResult.getAttributes()) {
            setRoles(roles, attribute);
        }
        return roles;
    }

    private void setRoles(Map<String, List<String>> roles, DsdExternalProcessor.DsdComponent dsdComponent) {
        DsdExternalProcessor.DsdComponentType type = dsdComponent.getType();
        String componentId = dsdComponent.getComponentId();
        if (type == DsdExternalProcessor.DsdComponentType.MEASURE) {
            initializeDimensionRole(roles, METRIC_ROLE);
            roles.get(METRIC_ROLE).add(componentId);
        } else if (type == DsdExternalProcessor.DsdComponentType.SPATIAL) {
            initializeDimensionRole(roles, GEO_ROLE);
            roles.get(GEO_ROLE).add(componentId);
        } else if (type == DsdExternalProcessor.DsdComponentType.TEMPORAL) {
            initializeDimensionRole(roles, TIME_ROLE);
            roles.get(TIME_ROLE).add(componentId);
        }
    }

    private void initializeDimensionRole(Map<String, List<String>> roles, String key) {
        if (!roles.containsKey(key)) {
            roles.put(key, new ArrayList<>());
        }
    }

    @Override
    public List<String> toJsonStatDatasetValues(Data data) throws Exception {
        List<String> stringObservations = new ArrayList<>();
        for (String observation : data.getObservations().split("\\|")) {
            stringObservations.add(StringUtils.isBlank(observation) ? null : observation.trim());
        }
        return stringObservations;
    }

    @Override
    public String toI18nValue(InternationalString source, String selectedLanguage) {
        if (source == null || source.getTexts() == null || source.getTexts().isEmpty() || selectedLanguage == null) {
            return null;
        }
        for (org.siemac.metamac.rest.common.v1_0.domain.LocalisedString text : source.getTexts()) {
            if (Objects.equals(text.getLang(), selectedLanguage)) {
                return text.getValue();
            }
        }
        return null;
    }

    @Override
    public String toI18nValue(org.siemac.metamac.statistical.resources.core.common.domain.InternationalString source, String selectedLanguage) {
        if (source == null) {
            return null;
        }

        InternationalString internationalString = new InternationalString();
        for (LocalisedString item : source.getTexts()) {
            org.siemac.metamac.rest.common.v1_0.domain.LocalisedString localisedString = new org.siemac.metamac.rest.common.v1_0.domain.LocalisedString();
            localisedString.setValue(item.getLabel());
            localisedString.setLang(item.getLocale());
            internationalString.getTexts().add(localisedString);
        }
        return toI18nValue(internationalString, selectedLanguage);
    }
}
