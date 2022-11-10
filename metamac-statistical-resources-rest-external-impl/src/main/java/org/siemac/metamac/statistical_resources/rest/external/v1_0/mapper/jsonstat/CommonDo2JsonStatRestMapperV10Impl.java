package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.jsonstat;

import java.util.ArrayList;
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
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Attribute;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.AttributeValues;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Attributes;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Data;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DataAttribute;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dimension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DimensionRepresentations;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DimensionValues;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.EnumeratedAttributeValue;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.EnumeratedAttributeValues;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.EnumeratedDimensionValue;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.EnumeratedDimensionValues;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatCategory;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatDimension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatExtension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.NonEnumeratedAttributeValue;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.NonEnumeratedAttributeValues;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.NonEnumeratedDimensionValue;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.NonEnumeratedDimensionValues;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.dataset.domain.AttributeValue;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical_resources.rest.external.service.utils.DsdExternalProcessor;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.DsdProcessorResult;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base.CommonDo2RestMapperV10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonDo2JsonStatRestMapperV10Impl implements CommonDo2JsonStatRestMapperV10 {

    @Autowired
    private CommonDo2RestMapperV10 commonDo2RestMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDo2JsonStatRestMapperV10Impl.class);

    @Override
    public Map<String, JsonStatDimension> toJsonStatDatasetDimensions(Dimensions dimensions, DimensionRepresentations dimensionRepresentations, String selectedLanguage) throws Exception {
        Map<String, JsonStatDimension> jsonStatDimensionMap = new HashMap<>();
        for (DimensionRepresentation dimension: dimensionRepresentations.getDimensions()) {
            JsonStatDimension jsonStatDimension = new JsonStatDimension();
            jsonStatDimension.setLabel(toDimensionI18nName(dimensions, dimension.getDimensionId(), selectedLanguage));
            jsonStatDimension.setCategory(new JsonStatCategory());

            Map<String, Long> indexMap = new HashMap<>();
            Map<String, String> labelMap = new HashMap<>();

            for (CodeRepresentation category : dimension.getRepresentations().getRepresentations()) {
                indexMap.put(category.getCode(), category.getIndex());
                labelMap.put(category.getCode(), toCategoryI18nName(dimensions, dimension.getDimensionId(), category, selectedLanguage));
            }

            jsonStatDimension.getCategory().setIndex(indexMap);
            jsonStatDimension.getCategory().setLabel(labelMap);

            jsonStatDimensionMap.put(dimension.getDimensionId(), jsonStatDimension);
        }

        return jsonStatDimensionMap;
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
        // TODO EDATOS-3662 treatment of unavailable selected language? how about an intersection of source.languages and selectedLanguages to discover common languages?
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

    private List<String> getNotesForAttributesAssociatedToCategories(DsdExternalProcessor.DsdAttribute attribute, Dimensions dimensions, Attributes attributes, String selectedLanguage, Data data, DsdProcessorResult dsdProcessorResult) {
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
        for (DataAttribute da : data.getAttributes().getAttributes()) {
            if (Objects.equals(da.getId(), attribute.getComponentId())) {
                dataAttribute = da;
                break;
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

    private void populateNotes(int currentDimension, int size[], int index[], Dimensions dimensions, String selectedLanguage, List<String> notes, List<DimensionRepresentation> attributeAssociatedDimensions, List<String> attributeValues) {
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
        return attributeCode; // some attributes are not declared as i18n
    }

    private String getNoteFromAttributePossition(Dimensions dimensions, String selectedLanguage, List<DimensionRepresentation> attributeAssociatedDimensions, List<String> attributeValues, int[] index) {
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
    public List<String> getJsonStatId(Data data) {
        List<String> id = new ArrayList<>();
        for (DimensionRepresentation dim : data.getDimensions().getDimensions()) {
            id.add(dim.getDimensionId());
        }
        return id;
    }

    @Override
    public List<Long> toJsonStatSize(Data data) {
        List<Long> dimensionSizes = new ArrayList<>();
        for (DimensionRepresentation dimension : data.getDimensions().getDimensions()) {
            long size = dimension.getRepresentations().getTotal().longValue();
            dimensionSizes.add(size);
        }
        return dimensionSizes;
    }

    @Override
    public JsonStatExtension toJsonStatExtension(DatasetVersion source, String selectedLanguage) {
        JsonStatExtension extension = new JsonStatExtension();
        extension.setDatasetId(source.getSiemacMetadataStatisticalResource().getCode());
        extension.setDatasetUrn(source.getSiemacMetadataStatisticalResource().getUrn());
        extension.setSurvey(toI18nValue(source.getSiemacMetadataStatisticalResource().getStatisticalOperation().getTitle(), selectedLanguage));
        extension.setLang(joinExternalItemCodes(source.getSiemacMetadataStatisticalResource().getLanguages()));
        extension.setPublishers(joinExternalItemTitles(source.getSiemacMetadataStatisticalResource().getPublisher(), selectedLanguage));
        extension.setDataProviders(joinExternalItemTitles(source.getSiemacMetadataStatisticalResource().getDataProvider(), selectedLanguage));
        extension.setDataProvidersAnnotations(toI18nValue(source.getSiemacMetadataStatisticalResource().getDataProviderAnnotations(), selectedLanguage));
        return extension;
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
            if (dimension.getType() == DsdExternalProcessor.DsdComponentType.MEASURE) {
                initializeDimensionRole(roles, METRIC_ROLE);
                roles.get(METRIC_ROLE).add(dimension.getComponentId());
            } else if (dimension.getType() == DsdExternalProcessor.DsdComponentType.SPATIAL) {
                initializeDimensionRole(roles, GEO_ROLE);
                roles.get(GEO_ROLE).add(dimension.getComponentId());
            } else if (dimension.getType() == DsdExternalProcessor.DsdComponentType.TEMPORAL) {
                initializeDimensionRole(roles, TIME_ROLE);
                roles.get(TIME_ROLE).add(dimension.getComponentId());
            }
        }

        return roles;
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
        if (source == null || source.getTexts() == null || source.getTexts().isEmpty()) {
            return null;
        }

        if (selectedLanguage == null) {
            // given that we know texts is not empty we can get the first one
            return source.getTexts().stream().findFirst().get().getValue(); // TODO EDATOS-3662: default language?
        }

        return commonDo2RestMapper.toInternationalString(source, Collections.singletonList(selectedLanguage)).getTexts().get(0).getValue(); // TODO EDATOS-3662 multiples languages?
    }

    @Override
    public String toI18nValue(org.siemac.metamac.statistical.resources.core.common.domain.InternationalString source, String selectedLanguage) {
        if (source == null) {
            return null;
        }

        org.siemac.metamac.rest.common.v1_0.domain.InternationalString internationalString = new org.siemac.metamac.rest.common.v1_0.domain.InternationalString();
        for (LocalisedString item : source.getTexts()) {
            org.siemac.metamac.rest.common.v1_0.domain.LocalisedString localisedString = new org.siemac.metamac.rest.common.v1_0.domain.LocalisedString();
            localisedString.setValue(item.getLabel());
            localisedString.setLang(item.getLocale());
            internationalString.getTexts().add(localisedString);
        }
        return toI18nValue(internationalString, selectedLanguage);
    }
}
