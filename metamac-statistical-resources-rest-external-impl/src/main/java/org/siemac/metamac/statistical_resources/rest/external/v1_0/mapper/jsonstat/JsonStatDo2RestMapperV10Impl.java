package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.jsonstat;

import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.isTemporalDimension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;
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
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatData;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatDimension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatExtension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.NonEnumeratedAttributeValue;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.NonEnumeratedAttributeValues;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.NonEnumeratedDimensionValue;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.NonEnumeratedDimensionValues;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.AttributeValue;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils;
import org.siemac.metamac.statistical_resources.rest.external.StatisticalResourcesRestExternalConstants;
import org.siemac.metamac.statistical_resources.rest.external.service.utils.DsdExternalProcessor;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.DsdProcessorResult;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base.CommonDo2RestMapperV10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonStatDo2RestMapperV10Impl implements JsonStatDo2RestMapperV10 {
    @Autowired
    private CommonDo2RestMapperV10 commonDo2RestMapper;

    @Autowired
    private DatasetVersionRepository datasetVersionRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonStatDo2RestMapperV10Impl.class);

    @Override
    public JsonStatData toJsonStatDataset(DatasetVersion source, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages, Set<String> parsedFields) throws Exception {
        if (source == null) {
            return null;
        }

        DsdProcessorResult dsdProcessorResult = commonDo2RestMapper.processDataStructure(source.getRelatedDsd().getUrn());
        Data data = toDatasetData(source, dsdProcessorResult, selectedDimensions, selectedLanguages);

        // too slow but necessary for category and dimension translations
        Dimensions dimensions = commonDo2RestMapper.toDimensions(source.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, null, selectedLanguages, parsedFields);

        // too slow but necessary for category and atributes translations
        Attributes attributes = commonDo2RestMapper.toAttributes(source.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, selectedLanguages);

        // for now, JSON-stat only takes the first selected lang since InternationalStrings are not supported
        String selectedLanguage = getSelectedLanguage(source, selectedLanguages);

        // ********************************************
        // ***** See https://json-stat.org/full/ ******
        // ********************************************

        JsonStatData target = new JsonStatData();

        target.setVersion("2.0");
        target.setClazz("dataset");
        target.addAllValues(toJsonStatDatasetValues(data));
        target.setDimension(toJsonStatDatasetDimensions(dimensions, data.getDimensions(), selectedLanguage));
        target.setRole(toJsonStatRoles(dsdProcessorResult));
        target.setId(getJsonStatId(data));
        target.setSize(toJsonStatSize(data));
        target.setLabel(commonDo2RestMapper.toI18nValue(source.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguage));
        target.setUpdated(source.getSiemacMetadataStatisticalResource().getLastUpdate().toString());
        target.setExtension(toJsonStatExtension(source, selectedLanguage));
        target.setNote(toJsonStatNote(source, data, dimensions, attributes, dsdProcessorResult, selectedLanguage));

        return target;
    }

    @Override
    public JsonStatData toJsonStatQuery(QueryVersion source, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages, Set<String> parsedFields) throws Exception {
        if (source == null) {
            return null;
        }

        DatasetVersion datasetVersion = getQueryRelatedDatasetVersionEffective(source);
        DsdProcessorResult dsdProcessorResult = commonDo2RestMapper.processDataStructure(datasetVersion.getRelatedDsd().getUrn());
        Data data = toQueryData(source, datasetVersion, dsdProcessorResult, selectedDimensions, selectedLanguages);

        // too slow but necessary for category and dimension translations
        Dimensions dimensions = commonDo2RestMapper.toDimensions(datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult,
                calculateEffectiveDimensionValuesToQuery(source, datasetVersion), selectedLanguages, null);

        // too slow but necessary for category and atributes translations
        Attributes attributes = commonDo2RestMapper.toAttributes(datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, selectedLanguages);

        // for now, JSON-stat only takes the first selected lang since InternationalStrings are not supported
        String selectedLanguage = getSelectedLanguage(datasetVersion, selectedLanguages);

        // ********************************************
        // ***** See https://json-stat.org/full/ ******
        // ********************************************

        JsonStatData target = new JsonStatData();

        target.setVersion("2.0");
        target.setClazz("dataset");
        target.addAllValues(toJsonStatDatasetValues(data));
        target.setDimension(toJsonStatDatasetDimensions(dimensions, data.getDimensions(), selectedLanguage));
        target.setRole(toJsonStatRoles(dsdProcessorResult));
        target.setId(getJsonStatId(data));
        target.setSize(toJsonStatSize(data));
        target.setLabel(commonDo2RestMapper.toI18nValue(datasetVersion.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguage));
        target.setUpdated(datasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate().toString());
        target.setExtension(toJsonStatExtension(datasetVersion, selectedLanguage));
        target.setNote(toJsonStatNote(datasetVersion, data, dimensions, attributes, dsdProcessorResult, selectedLanguage));

        return target;
    }

    // calculateEffectiveSelectionValues, calculateEffectiveDimensionValuesToQuery and calculateEffectiveDimensionValuesToDataset are similar, except that
    // - calculateEffectiveSelectionValues, applies the api selection with their special parameters to the previously queried results
    // - calculateEffectiveDimensionValuesToQuery, applies the query selection with their special parameters to the "whole" dataset (technicaly, only to the temporal coverage)
    // - calculateEffectiveDimensionValuesToDataset, applies the api selection with their special parameters to the "whole" dataset (technicaly, only to the temporal coverage)
    public Map<String, List<String>> calculateEffectiveSelectionValues(Map<String, List<String>> selectedDimensions, Map<String, List<String>> effectiveQueryDimensionValuesToDataByDimension) {
        Map<String, List<String>> effectiveDimensions = new HashMap<String, List<String>>(selectedDimensions.size());
        for (Map.Entry<String, List<String>> selectedDimension : selectedDimensions.entrySet()) {
            String dimensionId = selectedDimension.getKey();
            List<String> selectedValues = selectedDimension.getValue();
            if (StatisticalResourcesRestImplCommonUtils.isTemporalDimension(dimensionId)) {
                List<String> temporalCoverageValues = effectiveQueryDimensionValuesToDataByDimension.get(StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID);
                List<String> effectiveValues = StatisticalResourcesRestImplCommonUtils.calculateEffectiveTemporalSelectionValues(temporalCoverageValues, selectedValues);
                effectiveDimensions.put(dimensionId, effectiveValues);
            } else {
                effectiveDimensions.put(dimensionId, selectedValues);
            }
        }
        return effectiveDimensions;
    }

    private Data toQueryData(QueryVersion source, DatasetVersion datasetVersion, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages)
            throws Exception {
        if (source == null) {
            return null;
        }
        Map<String, List<String>> effectiveQueryDimensionValuesToDataByDimension = calculateEffectiveDimensionValuesToQuery(source, datasetVersion);
        Map<String, List<String>> effectiveSelectionValues = calculateEffectiveSelectionValues(selectedDimensions, effectiveQueryDimensionValuesToDataByDimension);
        Map<String, List<String>> effectiveDimensionValuesToDataByDimension = StatisticalResourcesRestImplCommonUtils.filterDimensions(effectiveQueryDimensionValuesToDataByDimension,
                effectiveSelectionValues);
        return commonDo2RestMapper.toData(datasetVersion, dsdProcessorResult, effectiveDimensionValuesToDataByDimension, selectedLanguages);
    }

    private List<String> calculateEffectiveTemporalDimensionValuesToQuery(QueryVersion source, List<String> temporalCoverageCodes, List<String> selectionCodes) {
        QueryTypeEnum type = source.getType();
        if (QueryTypeEnum.FIXED.equals(type)) {
            // return exactly
            return selectionCodes;
        } else if (QueryTypeEnum.AUTOINCREMENTAL.equals(type)) {
            List<String> effectiveDimensionValues = new ArrayList<String>();

            List<String> sortedSelectionCodes = SdmxTimeUtils.sortTimeList(selectionCodes);
            List<String> sortedTemporalCoverageCodes = SdmxTimeUtils.sortTimeList(temporalCoverageCodes);

            String latestSelectionCode = sortedSelectionCodes.get(sortedSelectionCodes.size() - 1);
            int indexLatestSelectionCode = sortedTemporalCoverageCodes.indexOf(latestSelectionCode);

            effectiveDimensionValues.addAll(selectionCodes);
            if (indexLatestSelectionCode >= 0) {
                // add codes added after lastest selected code
                List<String> temporalCodesAddedAfterLatestSelectedCodeString = sortedTemporalCoverageCodes.subList(indexLatestSelectionCode, sortedTemporalCoverageCodes.size());

                for (String code : temporalCodesAddedAfterLatestSelectedCodeString) {
                    if (!effectiveDimensionValues.contains(code)) {
                        effectiveDimensionValues.add(code);
                    }
                }
            }

            // We reverse the array to restore the order after the sortTimeList invocation
            Collections.reverse(effectiveDimensionValues);

            return effectiveDimensionValues;
        } else if (QueryTypeEnum.LATEST_DATA.equals(type)) {
            // return N data
            int codeLastIndexToReturn = -1;
            if (temporalCoverageCodes.size() < source.getLatestDataNumber()) {
                codeLastIndexToReturn = temporalCoverageCodes.size(); // there is not N data, so return all
            } else {
                codeLastIndexToReturn = source.getLatestDataNumber();
            }
            return temporalCoverageCodes.subList(0, codeLastIndexToReturn);
        } else {
            throw commonDo2RestMapper.buildRestException("QueryTypeEnum unsupported: " + source);
        }
    }

    private Map<String, List<String>> calculateEffectiveDimensionValuesToQuery(QueryVersion source, DatasetVersion datasetVersion) {
        Map<String, List<String>> dimensionValuesSelected = new HashMap<String, List<String>>(source.getSelection().size());
        for (QuerySelectionItem selection : source.getSelection()) {
            String dimensionId = selection.getDimension();
            List<String> selectionCodes = commonDo2RestMapper.codeItemToString(selection.getCodes());
            if (StatisticalResourcesRestImplCommonUtils.isTemporalDimension(dimensionId)) {
                List<String> temporalCoverageCodes = commonDo2RestMapper.temporalCoverageToString(datasetVersion.getTemporalCoverage());
                List<String> dimensionValues = calculateEffectiveTemporalDimensionValuesToQuery(source, temporalCoverageCodes, selectionCodes);
                dimensionValuesSelected.put(dimensionId, dimensionValues);
            } else {
                dimensionValuesSelected.put(dimensionId, selectionCodes);
            }
        }
        return dimensionValuesSelected;
    }

    private DatasetVersion getQueryRelatedDatasetVersionEffective(QueryVersion source) throws MetamacException {
        if (source.getFixedDatasetVersion() != null) {
            return source.getFixedDatasetVersion();
        } else {
            if (StatisticalResourcesRestExternalConstants.IS_INTERNAL_API) {
                return datasetVersionRepository.retrieveLastVersion(source.getDataset().getIdentifiableStatisticalResource().getUrn());
            } else {
                return datasetVersionRepository.retrieveLastPublishedVersion(source.getDataset().getIdentifiableStatisticalResource().getUrn());
            }
        }
    }

    private Map<String, JsonStatDimension> toJsonStatDatasetDimensions(Dimensions dimensions, DimensionRepresentations dimensionRepresentations, String selectedLanguage) throws Exception {
        Map<String, JsonStatDimension> jsonStatDimensionMap = new HashMap<>();
        for (DimensionRepresentation dimension: dimensionRepresentations.getDimensions()) {
            JsonStatDimension jsonStatDimension = new JsonStatDimension();
            jsonStatDimension.setLabel(toDimensionI18nName(dimensions, dimension, selectedLanguage));
            jsonStatDimension.setCategory(new JsonStatCategory());

            Map<String, Long> indexMap = new HashMap<>();
            Map<String, String> labelMap = new HashMap<>();

            for (CodeRepresentation category : dimension.getRepresentations().getRepresentations()) {
                indexMap.put(category.getCode(), category.getIndex());
                labelMap.put(category.getCode(), toCategoryI18nName(dimensions, dimension, category, selectedLanguage));
            }

            jsonStatDimension.getCategory().setIndex(indexMap);
            jsonStatDimension.getCategory().setLabel(labelMap);

            jsonStatDimensionMap.put(dimension.getDimensionId(), jsonStatDimension);
        }

        return jsonStatDimensionMap;
    }

    private String toCategoryI18nName(Dimensions dimensions, DimensionRepresentation dimensionRepresentation, CodeRepresentation category, String selectedLanguage) {
        for (Dimension dimension : dimensions.getDimensions()) {
            if (Objects.equals(dimension.getId(), dimensionRepresentation.getDimensionId())) {
                DimensionValues dimensionValues = dimension.getDimensionValues();
                if (dimensionValues instanceof EnumeratedDimensionValues) {
                    for (EnumeratedDimensionValue value : ((EnumeratedDimensionValues) dimensionValues).getValues()) {
                        if (Objects.equals(value.getId(), category.getCode())) {
                            return commonDo2RestMapper.toI18nValue(value.getName(), selectedLanguage);
                        }
                    }
                } else if (dimensionValues instanceof NonEnumeratedDimensionValues) {
                    for (NonEnumeratedDimensionValue value : ((NonEnumeratedDimensionValues) dimensionValues).getValues()) {
                        if (Objects.equals(value.getId(), category.getCode())) {
                            return commonDo2RestMapper.toI18nValue(value.getName(), selectedLanguage);
                        }
                    }
                }
            }
        }
        return null;
    }

    private String toDimensionI18nName(Dimensions dimensions, DimensionRepresentation dimensionRepresentation, String selectedLanguage) {
        for (Dimension dimension : dimensions.getDimensions()) {
            if (Objects.equals(dimension.getId(), dimensionRepresentation.getDimensionId())) {
                return commonDo2RestMapper.toI18nValue(dimension.getName(), selectedLanguage);
            }
        }

        return null;
    }

    private String getSelectedLanguage(DatasetVersion source, List<String> selectedLanguages) {
        // TODO EDATOS-3662 treatment of unavailable selected language? how about an intersection of source.languages and selectedLanguages to discover common languages?
        String selectedLanguage = selectedLanguages.isEmpty() ? null : selectedLanguages.get(0);

        String sourceLang = source.getSiemacMetadataStatisticalResource().getLanguage().getCode();
        if (selectedLanguage == null && sourceLang != null) {
            selectedLanguage = sourceLang.toLowerCase();
        }

        return selectedLanguage;
    }

    private List<String> toJsonStatNote(DatasetVersion source, Data data, Dimensions dimensions, Attributes attributes, DsdProcessorResult dsdProcessorResult, String selectedLanguage) {
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
                notes.addAll(getNotesForDatasetLevelAttribute(attribute, source, selectedLanguage));
            } else if (!attribute.getAttributeRelationship().getDimensions().isEmpty() || attribute.getAttributeRelationship().getGroup() != null) {
                notes.addAll(getNotesForAttributesAssociatedToCategories(attribute, dimensions, attributes, selectedLanguage, data, dsdProcessorResult));
            }
        }
        return notes;
    }

    private List<String> getNotesForDatasetLevelAttribute(DsdExternalProcessor.DsdAttribute attribute, DatasetVersion source, String selectedLanguage) {
        List<String> notes = new ArrayList<>();
        // To find the value of the attribute we need to look up the dataset attribute coverage.
        AttributeValue attributeCoverage = getAttributeCoverageByComponentId(source.getAttributesCoverage(), attribute.getComponentId());
        if (attributeCoverage != null) {
            String note = commonDo2RestMapper.toI18nValue(attribute.getConceptIdentity().getName(), selectedLanguage) + ". " + attributeCoverage.getTitle();
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
        // From dataset data, get dimensions declared by the attribute
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
        } else {
            return notes;
        }

        // From dataset data, get the attribute that matches the id from the DSD
        DataAttribute dataAttribute = null;
        for (DataAttribute da : data.getAttributes().getAttributes()) {
            if (Objects.equals(da.getId(), attribute.getComponentId())) {
                dataAttribute = da;
                break;
            }
        }
        if (dataAttribute == null) {
            return notes;
        }

        // Split values of the attribute to an array
        List<String> attributeValues = new ArrayList<>();
        for (String attributeValue : dataAttribute.getValue().split("\\|")) {
            attributeValues.add(toAttributeI18nName(attributes, attribute, attributeValue.trim(), selectedLanguage));
        }

        // The attribute values are in "row-major" order, based on the dimensions we processed from the dataset data.
        // That means dimensions order is important to map the categories to the right value.

        // Since attribute values can be attached to any amount of dimensions, we need to generalize the access of those
        // values. For example, consider 2 dimensions (A and B), each with 2 and 3 categories, respectively. Values are
        // in row-major order, we iterate over the values A1B1, A1B2, A1B3, A2B1, A2B2 and A3B3.

        //  A1B1, A1B2, A1B3
        //  A2B1, A2B2, A3B3

        // This behavior can be generalized to any number of dimensions/categories.

        int[] size = new int[attributeAssociatedDimensions.size()];
        for (int i = 0; i < attributeAssociatedDimensions.size(); i++) {
            DimensionRepresentation dimension = attributeAssociatedDimensions.get(i);
            int categoriesSize = dimension.getRepresentations().getTotal().intValue();
            size[i] = categoriesSize - 1; // minus one because array index starts at 0 and ends at arrayLength - 1
        }

        int[] index = new int[size.length];
        for (int i = 0; i < attributeAssociatedDimensions.size(); i++) {
            index[i] = 0; // array initialization at 0
        }

        // starts at [0, 0, 0,...]
        getNoteFromAttributePossition(dimensions, selectedLanguage, notes, attributeAssociatedDimensions, attributeValues, index);
        while (!ArrayUtils.isEquals(index, size)) {
            // continues at [0, 0, 0, ..., 1], [0, 0, 0, ..., 2], ...
            //              [0, 0, ..., 1, ..., 0], [0, 0, ..., 1, ..., 1], ...
            //              ...

            int amountOfDimensions = size.length;
            int j = amountOfDimensions - 1;

            while (j >= 0) {
                if (index[j] < size[j]) {
                    index[j] += 1;
                    break;
                } else {
                    index[j] = 0;
                    j = j - 1;
                }
            }
            getNoteFromAttributePossition(dimensions, selectedLanguage, notes, attributeAssociatedDimensions, attributeValues, index);
        }

        return notes;
    }

    private String toAttributeI18nName(Attributes attributes, DsdExternalProcessor.DsdAttribute attribute, String attributeCode, String selectedLanguage) {
        for (Attribute att : attributes.getAttributes()) {
            if (Objects.equals(att.getId(), attribute.getComponentId())) {
                AttributeValues attributeValues = att.getAttributeValues();
                if (attributeValues instanceof EnumeratedAttributeValues) {
                    for (EnumeratedAttributeValue value : ((EnumeratedAttributeValues) attributeValues).getValues()) {
                        if (Objects.equals(value.getId(), attributeCode)) {
                            return commonDo2RestMapper.toI18nValue(value.getName(), selectedLanguage);
                        }
                    }
                } else if (attributeValues instanceof NonEnumeratedAttributeValues) {
                    for (NonEnumeratedAttributeValue value : ((NonEnumeratedAttributeValues) attributeValues).getValues()) {
                        if (Objects.equals(value.getId(), attributeCode)) {
                            return commonDo2RestMapper.toI18nValue(value.getName(), selectedLanguage);
                        }
                    }
                }
            }
        }
        return attributeCode; // some attributes are not declared as i18n
    }

    private void getNoteFromAttributePossition(Dimensions dimensions, String selectedLanguage, List<String> notes, List<DimensionRepresentation> attributeAssociatedDimensions, List<String> attributeValues, int[] index) {
        String value = getValueFromPosition(attributeAssociatedDimensions, attributeValues, index);
        if (!StringUtils.isBlank(value)) {
            List<String> categories = new ArrayList<>();
            for (int i = 0; i < attributeAssociatedDimensions.size(); i++) {
                DimensionRepresentation dimension = attributeAssociatedDimensions.get(i);
                CodeRepresentation category = dimension.getRepresentations().getRepresentations().get(index[i]);
                categories.add(toCategoryI18nName(dimensions, dimension, category, selectedLanguage));
            }
            notes.add(String.join(", ", categories) + ". " + value);
        }
    }

    private String getValueFromPosition(List<DimensionRepresentation> dimensions, List<String> values, int... position) {
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
        return values.get(index);
    }

    private List<String> getJsonStatId(Data data) {
        List<String> id = new ArrayList<>();
        for (DimensionRepresentation dim : data.getDimensions().getDimensions()) {
            String dimensionId = dim.getDimensionId();
            id.add(dimensionId);
        }
        return id;
    }

    private List<Long> toJsonStatSize(Data data) {
        List<Long> dimensionSizes = new ArrayList<>();
        for (DimensionRepresentation dimension : data.getDimensions().getDimensions()) {
            long size = dimension.getRepresentations().getTotal().longValue();
            dimensionSizes.add(size);
        }
        return dimensionSizes;
    }

    private JsonStatExtension toJsonStatExtension(DatasetVersion source, String selectedLanguage) {
        JsonStatExtension extension = new JsonStatExtension();
        extension.setDatasetId(source.getSiemacMetadataStatisticalResource().getCode());
        extension.setDatasetUrn(source.getSiemacMetadataStatisticalResource().getUrn());
        extension.setSurvey(commonDo2RestMapper.toI18nValue(source.getSiemacMetadataStatisticalResource().getStatisticalOperation().getTitle(), selectedLanguage));
        extension.setLang(joinExternalItemCodes(source.getSiemacMetadataStatisticalResource().getLanguages()));
        extension.setPublishers(joinExternalItemTitles(source.getSiemacMetadataStatisticalResource().getPublisher(), selectedLanguage));
        extension.setDataProviders(joinExternalItemTitles(source.getSiemacMetadataStatisticalResource().getDataProvider(), selectedLanguage));
        extension.setDataProvidersAnnotations(commonDo2RestMapper.toI18nValue(source.getSiemacMetadataStatisticalResource().getDataProviderAnnotations(), selectedLanguage));
        return extension;
    }

    private String joinExternalItemCodes(List<ExternalItem> externalItemList) {
        if (externalItemList == null || externalItemList.isEmpty()) {
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
        if (externalItemList == null || externalItemList.isEmpty()) {
            return null;
        }

        StringJoiner joiner = new StringJoiner(", ");
        for (ExternalItem externalItem : externalItemList) {
            String title = commonDo2RestMapper.toI18nValue(externalItem.getTitle(), selectedLanguage);
            joiner.add(title);
        }
        return joiner.toString();
    }

    private Map<String, List<String>> toJsonStatRoles(DsdProcessorResult dsdProcessorResult) throws MetamacException {
        Map<String, List<String>> roles = new HashMap<>();

        for (DsdExternalProcessor.DsdDimension dimension : dsdProcessorResult.getDimensions()) {
            if (dimension.getType() == DsdExternalProcessor.DsdComponentType.MEASURE) {
                initializeDimensionRole(roles, "metric");
                roles.get("metric").add(dimension.getComponentId());
            } else if (dimension.getType() == DsdExternalProcessor.DsdComponentType.SPATIAL) {
                initializeDimensionRole(roles, "geo");
                roles.get("geo").add(dimension.getComponentId());
            } else if (dimension.getType() == DsdExternalProcessor.DsdComponentType.TEMPORAL) {
                initializeDimensionRole(roles, "time");
                roles.get("time").add(dimension.getComponentId());
            }
        }

        return roles;
    }

    private void initializeDimensionRole(Map<String, List<String>> roles, String key) {
        if (!roles.containsKey(key)) {
            roles.put(key, new ArrayList<>());
        }
    }

    private List<String> toJsonStatDatasetValues(Data data) throws Exception {
        List<String> stringObservations = new ArrayList<>();
        for (String observation : data.getObservations().split("\\|")) {
            stringObservations.add(StringUtils.isBlank(observation) ? null : observation.trim());
        }
        return stringObservations;
    }

    public Map<String, List<String>> calculateEffectiveDimensionValuesToDataset(Map<String, List<String>> selectedDimensions, DatasetVersion datasetVersion) {
        Map<String, List<String>> dimensionValuesSelected = new HashMap<String, List<String>>(selectedDimensions.size());
        for (Map.Entry<String, List<String>> selectedDimension : selectedDimensions.entrySet()) {
            String dimensionId = selectedDimension.getKey();
            List<String> selectedValues = selectedDimension.getValue();
            if (isTemporalDimension(dimensionId)) {
                List<String> temporalCoverageValues = commonDo2RestMapper.temporalCoverageToString(datasetVersion.getTemporalCoverage());
                List<String> effectiveValues = StatisticalResourcesRestImplCommonUtils.calculateEffectiveTemporalSelectionValues(temporalCoverageValues, selectedValues);
                dimensionValuesSelected.put(dimensionId, effectiveValues);
            } else {
                dimensionValuesSelected.put(dimensionId, selectedValues);
            }
        }
        return dimensionValuesSelected;
    }

    private Data toDatasetData(DatasetVersion source, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> dimensionValuesSelected, List<String> selectedLanguages) throws Exception {

        if (source == null) {
            return null;
        }
        Map<String, List<String>> effectiveSelectionValues = calculateEffectiveDimensionValuesToDataset(dimensionValuesSelected, source);
        return commonDo2RestMapper.toData(source, dsdProcessorResult, effectiveSelectionValues, selectedLanguages);
    }
}
