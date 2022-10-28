package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.jsonstat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Data;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dimension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DimensionRepresentations;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.EnumeratedDimensionValue;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.EnumeratedDimensionValues;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatCategory;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatData;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatDimension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatExtension;
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
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.dataset.DatasetsDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.isTemporalDimension;

@Component
public class JsonStatDo2RestMapperV10Impl implements JsonStatDo2RestMapperV10 {
    @Autowired
    private CommonDo2RestMapperV10 commonDo2RestMapper;

    @Autowired
    private DatasetVersionRepository datasetVersionRepository;

    @Override
    public JsonStatData toJsonStatDataset(DatasetVersion source, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages, Set<String> parsedFields) throws Exception {
        if (source == null) {
            return null;
        }

        DsdProcessorResult dsdProcessorResult = commonDo2RestMapper.processDataStructure(source.getRelatedDsd().getUrn());
        Data data = toDatasetData(source, dsdProcessorResult, selectedDimensions, selectedLanguages);

        // too slow but necessary for category and dimension translations
        Dimensions dimensions = commonDo2RestMapper.toDimensions(source.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, null, selectedLanguages, parsedFields);

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
        target.setNote(toJsonStatNote(source, dsdProcessorResult, selectedLanguage));

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
        target.setNote(toJsonStatNote(datasetVersion, dsdProcessorResult, selectedLanguage));

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
            if (Objects.equals(dimension.getId(), dimensionRepresentation.getDimensionId()) && dimension.getDimensionValues() instanceof EnumeratedDimensionValues) {
                for (EnumeratedDimensionValue value : ((EnumeratedDimensionValues) dimension.getDimensionValues()).getValues()) {
                    if (Objects.equals(value.getId(), category.getCode())) {
                        return commonDo2RestMapper.toI18nValue(value.getName(), selectedLanguage);
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

    private List<String> toJsonStatNote(DatasetVersion source, DsdProcessorResult dsdProcessorResult, String selectedLanguage) {
        Map<String, InternationalString> attributesConceptNames = new HashMap<>();
        for (DsdExternalProcessor.DsdAttribute attribute : dsdProcessorResult.getAttributes()) {
            if (!attribute.isAttributeAtObservationLevel()) {
                String key = attribute.getComponentId();
                InternationalString value = attribute.getConceptIdentity().getName();
                attributesConceptNames.put(key, value);
            }
        }

        List<String> notes = new ArrayList<>();
        for (AttributeValue attributeValue : source.getAttributesCoverage()) {
            if (attributesConceptNames.containsKey(attributeValue.getDsdComponentId())) {
                InternationalString internationalString = attributesConceptNames.get(attributeValue.getDsdComponentId());
                String note = commonDo2RestMapper.toI18nValue(internationalString, selectedLanguage) + ". " + attributeValue.getTitle();
                notes.add(note);
            }
        }

        return notes;
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
