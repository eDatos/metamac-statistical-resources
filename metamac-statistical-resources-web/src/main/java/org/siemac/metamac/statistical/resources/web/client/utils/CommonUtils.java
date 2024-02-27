package org.siemac.metamac.statistical.resources.web.client.utils;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getCoreMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionRationaleTypeDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyPartDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyValueDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.RegionValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdDimensionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.StatisticOfficialityDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.TemporalCodeDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.constraint.domain.KeyPartTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.AttributeRelationshipTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.AttributeRepresentationTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DataSourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.enums.DatasetConstraintInclusionTypeEnum;
import org.siemac.metamac.statistical.resources.web.shared.dtos.RangeDto;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDatePickerItem.DateFormatTypeEnum;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.google.gwt.event.shared.HasHandlers;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;

public class CommonUtils {

    private static String                               metamacPortalBaseUrl;
    private static Integer                              maxNumberOfUpdatedDatasetInGroup;
    private static Map<String, StatisticOfficialityDto> statisticOfficialitiesMap;

    // -----------------------------------------------------------------------------------------
    // DATASET CONSTRAINTS
    // -----------------------------------------------------------------------------------------

    /**
     * Returns the {@link KeyValueDto} of the selected dimension (all the {@link KeyPartDto} in a {@link KeyValueDto} belongs to the same dimension).
     *
     * @param keyValueDto
     * @return
     */
    public static KeyValueDto getKeyValueOfDimension(DsdDimensionDto dsdDimensionDto, RegionValueDto regionValueDto) {
        return getKeyValueOfDimension(dsdDimensionDto.getDimensionId(), regionValueDto);
    }

    private static KeyValueDto getKeyValueOfDimension(String dimensionId, RegionValueDto regionValueDto) {
        if (regionValueDto != null) {
            for (KeyValueDto keyValueDto : regionValueDto.getKeys()) {
                for (KeyPartDto keyPartDto : keyValueDto.getParts()) {
                    if (StringUtils.equals(dimensionId, keyPartDto.getIdentifier())) {
                        return keyValueDto;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns the {@link KeyPartTypeEnum} of the {@link KeyValueDto}. All the {@link KeyPartDto} of a {@link KeyValueDto} have the save type.
     *
     * @param keyValueDto
     * @return
     */
    public static KeyPartTypeEnum getKeyPartTypeOfKeyValue(KeyValueDto keyValueDto) {
        for (KeyPartDto keyPartDto : keyValueDto.getParts()) {
            return keyPartDto.getType();
        }
        return null;
    }

    public static List<String> getValuesOfKeyValue(KeyValueDto keyValueDto) {
        List<String> values = new ArrayList<String>();
        for (KeyPartDto keyPartDto : keyValueDto.getParts()) {
            if (KeyPartTypeEnum.NORMAL.equals(keyPartDto.getType())) {
                values.add(keyPartDto.getValue());
            }
        }
        return values;
    }

    public static RangeDto buildRangeDto(KeyPartDto keyPartDto) {
        RangeDto rangeDto = new RangeDto();
        if (!StringUtils.isBlank(keyPartDto.getBeforePeriod())) {
            rangeDto.setToValue(keyPartDto.getBeforePeriod());
        }
        if (!StringUtils.isBlank(keyPartDto.getAfterPeriod())) {
            rangeDto.setFromValue(keyPartDto.getAfterPeriod());
        }
        if (!StringUtils.isBlank(keyPartDto.getStartPeriod())) {
            rangeDto.setFromValue(keyPartDto.getStartPeriod());
        }
        if (!StringUtils.isBlank(keyPartDto.getEndPeriod())) {
            rangeDto.setToValue(keyPartDto.getEndPeriod());
        }
        return rangeDto;
    }

    public static KeyPartDto buildKeyPartDto(RangeDto rangeDto) {
        KeyPartDto keyPartDto = new KeyPartDto();
        if (!StringUtils.isBlank(rangeDto.getFromValue())) {
            keyPartDto.setStartPeriod(rangeDto.getFromValue());
            keyPartDto.setStartPeriodInclusive(true);
        }
        if (!StringUtils.isBlank(rangeDto.getToValue())) {
            keyPartDto.setEndPeriod(rangeDto.getToValue());
            keyPartDto.setEndPeriodInclusive(true);
        }
        return keyPartDto;
    }

    public static RegionValueDto removeKeyValueOfDimension(RegionValueDto regionValueDto, DsdDimensionDto dsdDimensionDto) {
        if (regionValueDto != null) {
            KeyValueDto keyValueDto = getKeyValueOfDimension(dsdDimensionDto.getDimensionId(), regionValueDto);
            if (keyValueDto != null) {
                regionValueDto.removeKey(keyValueDto);
            }
        }
        return regionValueDto;
    }

    // -----------------------------------------------------------------------------------------
    // PROC STATUS
    // -----------------------------------------------------------------------------------------

    public static String getProcStatusName(LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto) {
        return lifeCycleStatisticalResourceDto != null ? getProcStatusName(lifeCycleStatisticalResourceDto.getProcStatus()) : null;
    }

    public static String getProcStatusName(LifeCycleStatisticalResourceBaseDto lifeCycleStatisticalResourceBaseDto) {
        return lifeCycleStatisticalResourceBaseDto != null ? getProcStatusName(lifeCycleStatisticalResourceBaseDto.getProcStatus()) : null;
    }

    public static String getProcStatusName(ProcStatusEnum procStatusEnum) {
        return procStatusEnum != null ? getCoreMessages().getString(getCoreMessages().statisticalResourceProcStatusEnum() + procStatusEnum.getName()) : null;
    }

    public static LinkedHashMap<String, String> getProcStatusHashMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (ProcStatusEnum procStatusEnum : ProcStatusEnum.values()) {
            valueMap.put(procStatusEnum.name(), getProcStatusName(procStatusEnum));
        }
        return valueMap;
    }

    public static ProcStatusEnum getProcStatusEnum(String procStatusName) {
        if (!StringUtils.isBlank(procStatusName)) {
            try {
                return ProcStatusEnum.valueOf(procStatusName);
            } catch (Exception e) {
            }
        }
        return null;
    }

    // -----------------------------------------------------------------------------------------
    // STREAM MESSAGE STATUS
    // -----------------------------------------------------------------------------------------
    public static String getPublicationStreamStatusName(LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto) {
        return lifeCycleStatisticalResourceDto != null ? getPublicationsStreamStatusName(lifeCycleStatisticalResourceDto.getPublicationStreamStatus()) : null;
    }

    public static String getPublicationStreamStatusName(LifeCycleStatisticalResourceBaseDto lifeCycleStatisticalResourceBaseDto) {
        return lifeCycleStatisticalResourceBaseDto != null ? getPublicationsStreamStatusName(lifeCycleStatisticalResourceBaseDto.getPublicationStreamStatus()) : null;
    }

    public static String getPublicationsStreamStatusName(StreamMessageStatusEnum streamMessageStatusEnum) {
        String name = null;
        if (streamMessageStatusEnum != null) {
            name = getCoreMessages().getString(getCoreMessages().statisticalResourceStreamMsgStatus() + streamMessageStatusEnum.getName());
        }
        return name;
    }

    public static StreamMessageStatusEnum getPublicationStreamStatusEnum(String valueAsString) {
        if (!StringUtils.isBlank(valueAsString)) {
            try {
                return StreamMessageStatusEnum.valueOf(valueAsString);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static LinkedHashMap<String, String> getPublicationStreamStatusHashMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (StreamMessageStatusEnum streamMessageStatusEnum : StreamMessageStatusEnum.values()) {
            valueMap.put(streamMessageStatusEnum.name(), getPublicationsStreamStatusName(streamMessageStatusEnum));
        }
        return valueMap;
    }

    public static FormItemIcon getPublicationStreamStatusIcon(StreamMessageStatusEnum status) {
        if (status == null) {
            return null;
        }

        FormItemIcon icon = new FormItemIcon();
        if (StreamMessageStatusEnum.FAILED.equals(status)) {
            icon.setSrc(GlobalResources.RESOURCE.errorSmart().getURL());
        } else if (StreamMessageStatusEnum.PENDING.equals(status)) {
            icon.setSrc(GlobalResources.RESOURCE.warn().getURL());
        } else if (StreamMessageStatusEnum.SENT.equals(status)) {
            icon.setSrc(GlobalResources.RESOURCE.success().getURL());
        }

        return icon;
    }

    // -----------------------------------------------------------------------------------------
    // QUERY TYPE
    // -----------------------------------------------------------------------------------------

    public static String getQueryTypeName(QueryVersionDto queryDto) {
        return queryDto != null && queryDto.getType() != null ? getQueryTypeName(queryDto.getType()) : null;
    }

    public static String getQueryTypeName(QueryVersionBaseDto queryDto) {
        return queryDto != null && queryDto.getType() != null ? getQueryTypeName(queryDto.getType()) : null;
    }

    public static QueryTypeEnum getQueryTypeEnum(String value) {
        if (!StringUtils.isBlank(value)) {
            try {
                return QueryTypeEnum.valueOf(value);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static LinkedHashMap<String, String> getQueryTypeHashMap() {
        LinkedHashMap<String, String> queryTypeHashMap = new LinkedHashMap<String, String>();
        queryTypeHashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (QueryTypeEnum queryTypeEnum : QueryTypeEnum.values()) {
            queryTypeHashMap.put(queryTypeEnum.name(), getQueryTypeName(queryTypeEnum));
        }
        return queryTypeHashMap;
    }

    private static String getQueryTypeName(QueryTypeEnum queryType) {
        return queryType != null ? getCoreMessages().getString(getCoreMessages().queryTypeEnum() + queryType.getName()) : null;
    }

    // -----------------------------------------------------------------------------------------
    // QUERY STATUS
    // -----------------------------------------------------------------------------------------

    public static LinkedHashMap<String, String> getQueryStatusHashMap() {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (QueryStatusEnum queryStatusEnum : QueryStatusEnum.values()) {
            hashMap.put(queryStatusEnum.name(), getQueryStatusName(queryStatusEnum));
        }
        return hashMap;
    }

    public static QueryStatusEnum getQueryStatusEnum(String value) {
        if (!StringUtils.isBlank(value)) {
            try {
                return QueryStatusEnum.valueOf(value);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static String getQueryStatusName(QueryVersionDto queryDto) {
        return queryDto != null ? getQueryStatusName(queryDto.getStatus()) : null;
    }

    public static String getQueryStatusName(QueryVersionBaseDto queryDto) {
        return queryDto != null ? getQueryStatusName(queryDto.getStatus()) : null;
    }

    public static String getQueryStatusName(QueryStatusEnum queryStatusEnum) {
        return queryStatusEnum != null ? getCoreMessages().getString(getCoreMessages().queryStatusEnum() + queryStatusEnum.name()) : null;
    }

    // -----------------------------------------------------------------------------------------
    // STATISTICAL RESOURCE TYPE
    // -----------------------------------------------------------------------------------------

    public static LinkedHashMap<String, String> getStatisticalResourceTypeHashMap() {
        LinkedHashMap<String, String> statisticalResourceTypeHashMap = new LinkedHashMap<String, String>();
        statisticalResourceTypeHashMap.put(new String(), new String());
        for (StatisticalResourceTypeEnum c : StatisticalResourceTypeEnum.values()) {
            String value = getCoreMessages().getString(getCoreMessages().statisticalResourceTypeEnum() + c.getName());
            statisticalResourceTypeHashMap.put(c.toString(), value);
        }
        return statisticalResourceTypeHashMap;
    }

    public static LinkedHashMap<String, String> getStatisticalResourceTypeThatCanBeAddIntoACubeInMultidatasetHashMap() {
        LinkedHashMap<String, String> statisticalResourceTypeHashMap = new LinkedHashMap<String, String>();
        statisticalResourceTypeHashMap.put(StatisticalResourceTypeEnum.DATASET.name(), getCoreMessages().statisticalResourceTypeEnumDATASET());
        statisticalResourceTypeHashMap.put(StatisticalResourceTypeEnum.QUERY.name(), getCoreMessages().statisticalResourceTypeEnumQUERY());
        return statisticalResourceTypeHashMap;
    }

    public static LinkedHashMap<String, String> getStatisticalResourceTypeThatCanBeAddIntoACubeInPublicationHashMap() {
        LinkedHashMap<String, String> statisticalResourceTypeHashMap = new LinkedHashMap<String, String>();
        statisticalResourceTypeHashMap.put(StatisticalResourceTypeEnum.DATASET.name(), getCoreMessages().statisticalResourceTypeEnumDATASET());
        statisticalResourceTypeHashMap.put(StatisticalResourceTypeEnum.QUERY.name(), getCoreMessages().statisticalResourceTypeEnumQUERY());
        statisticalResourceTypeHashMap.put(StatisticalResourceTypeEnum.MULTIDATASET.name(), getCoreMessages().statisticalResourceTypeEnumMULTIDATASET());
        statisticalResourceTypeHashMap.put(StatisticalResourceTypeEnum.URL.name(), getCoreMessages().statisticalResourceTypeEnumURL());
        return statisticalResourceTypeHashMap;
    }

    public static String getStatisticalResourceTypeName(StatisticalResourceTypeEnum statisticalResourceTypeEnum) {
        return statisticalResourceTypeEnum != null ? getCoreMessages().getString(getCoreMessages().statisticalResourceTypeEnum() + statisticalResourceTypeEnum.name()) : null;
    }

    // -----------------------------------------------------------------------------------------
    // STATISTICAL RESOURCE VERSION RATIONALE TYPE
    // -----------------------------------------------------------------------------------------

    public static LinkedHashMap<String, String> getVersionRationaleTypeHashMap() {
        LinkedHashMap<String, String> statisticalResourceVersionRationaleTypeHashMap = new LinkedHashMap<String, String>();
        statisticalResourceVersionRationaleTypeHashMap.put(new String(), new String());
        for (VersionRationaleTypeEnum v : VersionRationaleTypeEnum.values()) {
            statisticalResourceVersionRationaleTypeHashMap.put(v.toString(), getVersionRationaleTypeName(v));
        }
        return statisticalResourceVersionRationaleTypeHashMap;
    }

    public static String getVersionRationaleTypeName(VersionRationaleTypeEnum versionRationaleType) {
        return versionRationaleType != null ? getCoreMessages().getString(getCoreMessages().statisticalResourceVersionRationaleTypeEnum() + versionRationaleType.name()) : null;
    }

    public static String getStatisticalResourceVersionRationaleTypeNames(List<VersionRationaleTypeDto> versionRationaleTypeDtos) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < versionRationaleTypeDtos.size(); i++) {
            builder.append(i != 0 ? ",  " : StringUtils.EMPTY).append(getVersionRationaleTypeName(versionRationaleTypeDtos.get(i).getValue()));
        }
        return builder.toString();
    }

    public static List<VersionRationaleTypeDto> getVersionRationaleTypeValues() {
        List<VersionRationaleTypeDto> versionRationaleTypeDtos = new ArrayList<VersionRationaleTypeDto>();
        for (VersionRationaleTypeEnum value : VersionRationaleTypeEnum.values()) {
            VersionRationaleTypeDto versionRationaleTypeDto = new VersionRationaleTypeDto();
            versionRationaleTypeDto.setValue(value);
            versionRationaleTypeDtos.add(versionRationaleTypeDto);
        }
        return versionRationaleTypeDtos;
    }

    public static boolean containsMinorErrataVersionRationaleType(List<VersionRationaleTypeDto> versionRationaleTypeDtos) {
        if (versionRationaleTypeDtos != null) {
            for (VersionRationaleTypeDto type : versionRationaleTypeDtos) {
                if (VersionRationaleTypeEnum.MINOR_ERRATA.equals(type.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    // -----------------------------------------------------------------------------------------
    // VERSION TYPE
    // -----------------------------------------------------------------------------------------

    public static LinkedHashMap<String, String> getVersionTypeHashMap() {
        LinkedHashMap<String, String> versionTypeHashMap = new LinkedHashMap<String, String>();
        versionTypeHashMap.put(new String(), new String());
        for (VersionTypeEnum v : VersionTypeEnum.values()) {
            versionTypeHashMap.put(v.toString(), getVersionTypeName(v));
        }
        return versionTypeHashMap;
    }

    public static String getVersionTypeName(VersionTypeEnum versionTypeEnum) {
        return versionTypeEnum != null ? getCoreMessages().getString(getCoreMessages().versionTypeEnum() + versionTypeEnum.name()) : null;
    }

    // -----------------------------------------------------------------------------------------
    // DATASET CONSTRAINT INCLUSION TYPE
    // -----------------------------------------------------------------------------------------

    public static LinkedHashMap<String, String> getConstraintInclusionTypeHashMap() {
        LinkedHashMap<String, String> typeHashMap = new LinkedHashMap<String, String>();
        for (DatasetConstraintInclusionTypeEnum type : DatasetConstraintInclusionTypeEnum.values()) {
            typeHashMap.put(type.toString(), getConstraintInclusionTypeName(type));
        }
        return typeHashMap;
    }

    public static String getConstraintInclusionTypeName(DatasetConstraintInclusionTypeEnum typeEnum) {
        return typeEnum != null ? getCoreMessages().getString(getCoreMessages().datasetConstraintInclusionTypeEnum() + typeEnum.name()) : null;
    }

    public static DatasetConstraintInclusionTypeEnum getDatasetConstraintInclusionTypeEnum(String value) {
        if (!StringUtils.isBlank(value)) {
            try {
                return DatasetConstraintInclusionTypeEnum.valueOf(value);
            } catch (Exception e) {
            }
        }
        return null;
    }

    // -----------------------------------------------------------------------------------------
    // DATASET CONSTRAINT KEY PART TYPE
    // -----------------------------------------------------------------------------------------

    public static LinkedHashMap<String, String> getConstraintKeyPartTypeHashMap() {
        LinkedHashMap<String, String> typeHashMap = new LinkedHashMap<String, String>();
        for (KeyPartTypeEnum type : KeyPartTypeEnum.values()) {
            typeHashMap.put(type.toString(), getConstraintKeyPartTypeName(type));
        }
        return typeHashMap;
    }

    public static String getConstraintKeyPartTypeName(KeyPartTypeEnum typeEnum) {
        return typeEnum != null ? getCoreMessages().getString(getCoreMessages().keyPartTypeEnum() + typeEnum.name()) : null;
    }

    public static KeyPartTypeEnum getDatasetConstraintKeyPartTypeEnum(String value) {
        if (!StringUtils.isBlank(value)) {
            try {
                return KeyPartTypeEnum.valueOf(value);
            } catch (Exception e) {
            }
        }
        return null;
    }

    // -----------------------------------------------------------------------------------------
    // STATISTICAL RESOURCE NEXT VERSION
    // -----------------------------------------------------------------------------------------

    public static LinkedHashMap<String, String> getStatisticalResourceNextVersionHashMap() {
        LinkedHashMap<String, String> statisticalResourceNextVersionHashMap = new LinkedHashMap<String, String>();
        statisticalResourceNextVersionHashMap.put(new String(), new String());
        for (NextVersionTypeEnum s : NextVersionTypeEnum.values()) {
            statisticalResourceNextVersionHashMap.put(s.name(), getStatisticalResourceNextVersionName(s));
        }
        return statisticalResourceNextVersionHashMap;
    }

    public static String getStatisticalResourceNextVersionName(NextVersionTypeEnum statisticalResourceNextVersionEnum) {
        return statisticalResourceNextVersionEnum != null ? getCoreMessages().getString(getCoreMessages().statisticalResourceNextVersionEnum() + statisticalResourceNextVersionEnum.name()) : null;
    }

    public static NextVersionTypeEnum getNextVersionTypeEnum(String nextVersionTypeName) {
        if (!StringUtils.isBlank(nextVersionTypeName)) {
            try {
                return NextVersionTypeEnum.valueOf(nextVersionTypeName);
            } catch (Exception e) {
            }
        }
        return null;
    }

    // -----------------------------------------------------------------------------------------
    // STATISTIC OFFICIALITY
    // -----------------------------------------------------------------------------------------

    public static String getStatisticOfficialityName(StatisticOfficialityDto statisticOfficialityDto) {
        if (statisticOfficialityDto != null) {
            return CommonWebUtils.getElementName(statisticOfficialityDto.getIdentifier(), statisticOfficialityDto.getDescription());
        }
        return StringUtils.EMPTY;
    }

    public static LinkedHashMap<String, String> getStatisticOfficialityHashMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(new String(), new String());
        if (statisticOfficialitiesMap != null) {
            for (Entry<String, StatisticOfficialityDto> entry : statisticOfficialitiesMap.entrySet()) {
                map.put(entry.getKey(), getStatisticOfficialityName(entry.getValue()));
            }
        }
        return map;
    }

    public static StatisticOfficialityDto getStatisticOfficialityByIdentifier(String identifier) {
        return statisticOfficialitiesMap.get(identifier);
    }

    public static void setStatisticOfficialities(List<StatisticOfficialityDto> statisticOfficialities) {
        CommonUtils.statisticOfficialitiesMap = new HashMap<String, StatisticOfficialityDto>();
        for (StatisticOfficialityDto officiality : statisticOfficialities) {
            CommonUtils.statisticOfficialitiesMap.put(officiality.getIdentifier(), officiality);
        }
    }

    // -----------------------------------------------------------------------------------------
    // TEMPORAL CODES
    // -----------------------------------------------------------------------------------------

    public static String getTemporalCodeName(TemporalCodeDto dto) {
        if (dto != null) {
            return CommonWebUtils.getElementName(dto.getIdentifier(), dto.getTitle());
        }
        return StringUtils.EMPTY;
    }

    public static String getTemporalCodesNames(List<TemporalCodeDto> dtos) {
        if (dtos != null) {
            List<String> names = new ArrayList<String>();
            for (TemporalCodeDto dto : dtos) {
                names.add(getTemporalCodeName(dto));
            }
            return CommonWebUtils.getStringListToString(names);
        }
        return StringUtils.EMPTY;
    }

    // -----------------------------------------------------------------------------------------
    // DSD ATTRIBUTES
    // -----------------------------------------------------------------------------------------

    public static String getAttributeRelationshipTypeName(AttributeRelationshipTypeEnum attributeRelationshipTypeEnum) {
        return attributeRelationshipTypeEnum != null ? getCoreMessages().getString(getCoreMessages().attributeRelationshipTypeEnum() + attributeRelationshipTypeEnum.name()) : null;
    }

    public static boolean hasEnumeratedRepresentation(DsdAttributeDto dsdAttributeDto) {
        return AttributeRepresentationTypeEnum.ENUMERATION.equals(dsdAttributeDto.getAttributeRepresentation().getRepresentationType());
    }

    public static boolean hasNonEnumeratedRepresentation(DsdAttributeDto dsdAttributeDto) {
        return AttributeRepresentationTypeEnum.TEXT_FORMAT.equals(dsdAttributeDto.getAttributeRepresentation().getRepresentationType());
    }

    public static boolean hasObservationRelationshipType(DsdAttributeDto dsdAttributeDto) {
        return AttributeRelationshipTypeEnum.PRIMARY_MEASURE_RELATIONSHIP.equals(dsdAttributeDto.getAttributeRelationship().getRelationshipType());
    }

    public static boolean hasDatasetRelationshipType(DsdAttributeDto dsdAttributeDto) {
        return AttributeRelationshipTypeEnum.NO_SPECIFIED_RELATIONSHIP.equals(dsdAttributeDto.getAttributeRelationship().getRelationshipType());
    }

    public static boolean hasDimensionRelationshipType(DsdAttributeDto dsdAttributeDto) {
        return AttributeRelationshipTypeEnum.DIMENSION_RELATIONSHIP.equals(dsdAttributeDto.getAttributeRelationship().getRelationshipType());
    }

    public static boolean hasGroupRelationshipType(DsdAttributeDto dsdAttributeDto) {
        return AttributeRelationshipTypeEnum.GROUP_RELATIONSHIP.equals(dsdAttributeDto.getAttributeRelationship().getRelationshipType());
    }

    // -----------------------------------------------------------------------------------------
    // METAMAC PORTAL BASE URL
    // -----------------------------------------------------------------------------------------

    public static void setMetamacPortalBaseUrl(String baseUrl) {
        metamacPortalBaseUrl = baseUrl;
    }

    public static String getMetamacPortalBaseUrl() {
        return metamacPortalBaseUrl;
    }

    public static String getStatisticalOperationCodeFromDatasetVersion(DatasetVersionDto datasetVersionDto) {
        return datasetVersionDto.getStatisticalOperation().getCode();
    }

    //
    // COMMON UTILITIES
    //

    public static LinkedHashMap<String, String> getBooleanHashMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(Boolean.TRUE.toString(), MetamacWebCommon.getConstants().yes());
        map.put(Boolean.FALSE.toString(), MetamacWebCommon.getConstants().no());
        return map;
    }

    public static LinkedHashMap<String, String> getEditionLanguagesHashMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (String locale : ApplicationEditionLanguages.getLocales()) {
            map.put(locale, locale);
        }
        return map;
    }

    public static boolean isResourceInProductionValidationOrGreaterProcStatus(ProcStatusEnum procStatus) {
        return ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus) || ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus) || ProcStatusEnum.PUBLISHED.equals(procStatus);
    }

    public static boolean isUrnFromSelectedStatisticalOperation(String urn) {
        ExternalItemDto selectedStatisticalOperation = StatisticalResourcesDefaults.getSelectedStatisticalOperation();
        if (selectedStatisticalOperation != null) {
            if (!StringUtils.isBlank(urn)) {
                return StringUtils.equals(urn, selectedStatisticalOperation.getUrn());
            }
        }
        return false;
    }

    public static String generateStatisticalOperationUrn(String operationCode) {
        return UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_OPERATION_PREFIX, operationCode);
    }

    public static String generateDatasetUrn(String datasetIdentifier) {
        return UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_DATASET_PREFIX, datasetIdentifier);
    }

    public static String generatePublicationUrn(String publicationIdentifier) {
        return UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_COLLECTION_PREFIX, publicationIdentifier);
    }

    public static String generateQueryUrn(String queryIdentifier) {
        return UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_QUERY_PREFIX, queryIdentifier);
    }

    public static String generateMultidatasetUrn(String publicationIdentifier) {
        return UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_MULTIDATASET_PREFIX, publicationIdentifier);
    }

    public static void showMessageAfterResourceLifeCycleUpdate(HasHandlers source, MetamacWebException notificationException, String message) {
        if (notificationException == null) {
            ShowMessageEvent.fireSuccessMessage(source, message);
        } else {
            String warningMessage = message + StatisticalResourcesWeb.getMessages().notificationsSendingError();
            ShowMessageEvent.fireWarningMessageWithError(source, warningMessage, notificationException);
        }
    }

    public static void showMessageAfterMultipleResourcesLifeCycleUpdate(HasHandlers source, MetamacWebException notificationException, String message) {
        if (notificationException == null) {
            ShowMessageEvent.fireSuccessMessage(source, message);
        } else {
            ShowMessageEvent.fireWarningMessageWithError(source, message, notificationException);
        }
    }

    public static LinkedHashMap<String, String> getDataSourceTypeHashMap() { // NOSONAR
        LinkedHashMap<String, String> dataSourceTypeHashMap = new LinkedHashMap<String, String>(); // NOSONAR
        for (DataSourceTypeEnum dataSourceTypeEnum : DataSourceTypeEnum.values()) {
            dataSourceTypeHashMap.put(dataSourceTypeEnum.name(), getDataSourceName(dataSourceTypeEnum));
        }
        return dataSourceTypeHashMap;
    }

    private static String getDataSourceName(DataSourceTypeEnum dataSourceTypeEnum) {
        return dataSourceTypeEnum != null ? getCoreMessages().getString(getCoreMessages().dataSourceTypeEnum() + dataSourceTypeEnum.getName()) : null;
    }

    public static LinkedHashMap<String, String> getDateFormatTypeHashMap() {
        LinkedHashMap<String, String> dateFormatTypeHashMap = new LinkedHashMap<String, String>();
        for (DateFormatTypeEnum a : DateFormatTypeEnum.values()) {
            String value = getCoreMessages().getString(getCoreMessages().dateFormatTypeEnum() + a.getName());
            dateFormatTypeHashMap.put(a.name(), value);
        }

        return dateFormatTypeHashMap;
    }

    /**
     * Returns {@link LinkedHashMap} of {@link AnnotationTypeDto}
     * 
     * @return
     */
    public static LinkedHashMap<String, String> getRestrictionsHashMap(List<ExternalItemDto> restrictions) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        for (ExternalItemDto type : restrictions) {
            String description = InternationalStringUtils.getLocalisedString(type.getTitle());
            if (description == null) {
                description = type.getCode();
            }
            hashMap.put(type.getCode(), description);
        }

        return hashMap;
    }

    public static Integer getMaxNumberOfUpdatedDatasetInGroup() {
        return maxNumberOfUpdatedDatasetInGroup;
    }

    public static void setMaxNumberOfUpdatedDatasetInGroup(Integer maxNumberOfUpdatedDatasetInGroup) {
        CommonUtils.maxNumberOfUpdatedDatasetInGroup = maxNumberOfUpdatedDatasetInGroup;
    }

}
