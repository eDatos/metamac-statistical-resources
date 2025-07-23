package org.siemac.metamac.statistical.resources.web.client.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.siemac.edatos.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.statistical.resources.core.dto.IdentifiableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionRationaleTypeDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionableStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyValueDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.RegionValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.AttributeValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdDimensionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.TemporalCodeDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetCubeDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.ChapterDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.CubeDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.ElementLevelDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.record.DatasetRecord;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.record.DatasourceRecord;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.record.DimensionConstraintsRecord;
import org.siemac.metamac.statistical.resources.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.statistical.resources.web.client.model.record.CodeItemRecord;
import org.siemac.metamac.statistical.resources.web.client.model.record.DsdAttributeInstanceRecord;
import org.siemac.metamac.statistical.resources.web.client.model.record.DsdAttributeRecord;
import org.siemac.metamac.statistical.resources.web.client.model.record.IdentifiableResourceRecord;
import org.siemac.metamac.statistical.resources.web.client.model.record.LifeCycleResourceRecord;
import org.siemac.metamac.statistical.resources.web.client.model.record.NameableResourceRecord;
import org.siemac.metamac.statistical.resources.web.client.model.record.SiemacMetadataRecord;
import org.siemac.metamac.statistical.resources.web.client.model.record.TemporalCodeRecord;
import org.siemac.metamac.statistical.resources.web.client.model.record.VersionRationaleTypeRecord;
import org.siemac.metamac.statistical.resources.web.client.model.record.VersionableResourceRecord;
import org.siemac.metamac.statistical.resources.web.client.multidataset.model.ds.MultidatasetCubeTreeNode;
import org.siemac.metamac.statistical.resources.web.client.multidataset.model.record.MultidatasetRecord;
import org.siemac.metamac.statistical.resources.web.client.publication.model.ds.ElementLevelDS;
import org.siemac.metamac.statistical.resources.web.client.publication.model.record.ElementLevelTreeNode;
import org.siemac.metamac.statistical.resources.web.client.publication.model.record.PublicationRecord;
import org.siemac.metamac.statistical.resources.web.client.query.model.record.QueryRecord;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class StatisticalResourcesRecordUtils extends RecordUtils {

    //
    // IDENTIFIABLE
    //

    public static IdentifiableResourceRecord getIdentifiableResourceRecord(IdentifiableResourceRecord record, IdentifiableStatisticalResourceDto dto) {
        record.setCode(dto.getCode());
        record.setUrn(dto.getUrn());
        return record;
    }

    //
    // NAMEABLE
    //

    public static NameableResourceRecord getNameableResourceRecord(NameableResourceRecord record, NameableStatisticalResourceDto dto) {
        record = (NameableResourceRecord) getIdentifiableResourceRecord(record, dto);
        record.setTitle(getLocalisedString(dto.getTitle()));
        return record;
    }

    public static NameableResourceRecord getNameableResourceRecord(NameableResourceRecord record, NameableStatisticalResourceBaseDto dto) {
        record = (NameableResourceRecord) getIdentifiableResourceRecord(record, dto);
        record.setTitle(getLocalisedString(dto.getTitle()));
        return record;
    }

    //
    // VERSIONABLE
    //

    public static VersionableResourceRecord getVersionableResourceRecord(VersionableResourceRecord record, VersionableStatisticalResourceDto dto) {
        record = (VersionableResourceRecord) getNameableResourceRecord(record, dto);
        record.setVersionLogic(dto.getVersionLogic());
        return record;
    }

    public static VersionableResourceRecord getVersionableResourceRecord(VersionableResourceRecord record, VersionableStatisticalResourceBaseDto dto) {
        record = (VersionableResourceRecord) getNameableResourceRecord(record, dto);
        record.setVersionLogic(dto.getVersionLogic());
        return record;
    }

    //
    // LIFECYCLE
    //

    public static LifeCycleResourceRecord getLifeCycleResourceRecord(LifeCycleResourceRecord record, LifeCycleStatisticalResourceDto dto) {
        record = (LifeCycleResourceRecord) getVersionableResourceRecord(record, dto);
        record.setProcStatus(CommonUtils.getProcStatusName(dto));
        record.setCreationDate(DateUtils.getFormattedDate(dto.getCreationDate()));
        record.setPublicationDate(DateUtils.getFormattedDate(dto.getPublicationDate()));
        record.setPublicationStreamStatus(CommonUtils.getPublicationStreamStatusIcon(dto.getPublicationStreamStatus()));
        return record;
    }

    public static LifeCycleResourceRecord getLifeCycleResourceRecord(LifeCycleResourceRecord record, LifeCycleStatisticalResourceBaseDto dto) {
        record = (LifeCycleResourceRecord) getVersionableResourceRecord(record, dto);
        record.setProcStatus(CommonUtils.getProcStatusName(dto));
        record.setCreationDate(DateUtils.getFormattedDate(dto.getCreationDate()));
        record.setPublicationDate(DateUtils.getFormattedDate(dto.getPublicationDate()));
        record.setPublicationStreamStatus(CommonUtils.getPublicationStreamStatusIcon(dto.getPublicationStreamStatus()));
        return record;
    }

    //
    // SIEMAC METADATA
    //

    public static SiemacMetadataRecord getSiemacMetadataRecord(SiemacMetadataRecord record, SiemacMetadataStatisticalResourceDto dto) {
        return (SiemacMetadataRecord) getLifeCycleResourceRecord(record, dto);
    }

    public static SiemacMetadataRecord getSiemacMetadataRecord(SiemacMetadataRecord record, SiemacMetadataStatisticalResourceBaseDto dto) {
        return (SiemacMetadataRecord) getLifeCycleResourceRecord(record, dto);
    }

    //
    // DATASETS
    //

    public static DatasetRecord getDatasetRecord(DatasetVersionBaseDto datasetVersionBaseDto) {
        DatasetRecord record = (DatasetRecord) getSiemacMetadataRecord(new DatasetRecord(), datasetVersionBaseDto);
        record.setRelatedDSD(datasetVersionBaseDto.getRelatedDsd());
        if (datasetVersionBaseDto.getStatisticOfficiality() != null) {
            record.setStatisticOfficiality(getLocalisedString(datasetVersionBaseDto.getStatisticOfficiality().getDescription()));
        }
        record.setDatasetVersionBaseDto(datasetVersionBaseDto);
        return record;
    }

    public static DatasetRecord[] getDatasetRecords(List<DatasetVersionBaseDto> datasetVersionBaseDtos) {
        DatasetRecord[] records = new DatasetRecord[datasetVersionBaseDtos.size()];
        for (int i = 0; i < datasetVersionBaseDtos.size(); i++) {
            records[i] = getDatasetRecord(datasetVersionBaseDtos.get(i));
        }
        return records;
    }

    public static DatasourceRecord getDatasourceRecord(DatasourceDto datasourceDto) {
        DatasourceRecord record = new DatasourceRecord(datasourceDto.getId(), datasourceDto.getCode(), datasourceDto.getUrn(), datasourceDto);
        return record;
    }

    public static List<DatasetVersionBaseDto> getDatasetVersionBaseDtosFromListGridRecords(ListGridRecord[] records) {
        List<DatasetVersionBaseDto> datasetVersionBaseDtos = new ArrayList<DatasetVersionBaseDto>();
        if (records != null) {
            for (ListGridRecord record : records) {
                DatasetRecord datasetRecord = (DatasetRecord) record;
                datasetVersionBaseDtos.add(datasetRecord.getDatasetVersionBaseDto());
            }
        }
        return datasetVersionBaseDtos;
    }

    public static List<String> getDatasetVersionUrnsFromListGridRecords(ListGridRecord[] records) {
        List<String> datasetVersionUrns = new ArrayList<String>();
        if (records != null) {
            for (ListGridRecord record : records) {
                DatasetRecord datasetRecord = (DatasetRecord) record;
                datasetVersionUrns.add(datasetRecord.getUrn());
            }
        }
        return datasetVersionUrns;
    }

    public static CategorisationRecord getCategorisationRecord(CategorisationDto categorisationDto) {
        CategorisationRecord record = new CategorisationRecord(categorisationDto.getId(), categorisationDto.getCode(), InternationalStringUtils.getLocalisedString(categorisationDto.getTitle()),
                categorisationDto.getCategory(), categorisationDto.getUrn(), categorisationDto.getMaintainer(), categorisationDto.getValidFrom(), categorisationDto.getValidTo(), categorisationDto);
        return record;
    }

    public static CategorisationRecord[] getCategorisationRecords(List<CategorisationDto> categorisationDtos) {
        CategorisationRecord[] records = new CategorisationRecord[categorisationDtos.size()];
        int index = 0;
        for (CategorisationDto categorisationDto : categorisationDtos) {
            records[index++] = getCategorisationRecord(categorisationDto);
        }
        return records;
    }

    public static DimensionConstraintsRecord[] getDimensionConstraintsRecords(List<DsdDimensionDto> dimensions, RegionValueDto regionValueDto) {
        DimensionConstraintsRecord[] records = new DimensionConstraintsRecord[dimensions.size()];
        for (int i = 0; i < dimensions.size(); i++) {
            DimensionConstraintsRecord record = new DimensionConstraintsRecord();
            record.setDimensionId(dimensions.get(i).getDimensionId());
            record.setDsdDimensionDto(dimensions.get(i));
            updateRecordWithKeyPartsValues(record, dimensions.get(i), regionValueDto);
            records[i] = record;
        }
        return records;
    }

    public static void updateRecordWithKeyPartsValues(DimensionConstraintsRecord record, DsdDimensionDto dsdDimensionDto, RegionValueDto regionValueDto) {
        KeyValueDto keyValueDto = CommonUtils.getKeyValueOfDimension(dsdDimensionDto, regionValueDto);
        if (keyValueDto != null) {
            record.setInclusionType(keyValueDto.getIncluded());
            record.setValues(keyValueDto.getParts());
        }
    }

    //
    // PUBLICATIONS
    //

    public static PublicationRecord[] getPublicationRecords(List<PublicationVersionBaseDto> publicationVersionBaseDtos) {
        PublicationRecord[] records = new PublicationRecord[publicationVersionBaseDtos.size()];
        for (int i = 0; i < publicationVersionBaseDtos.size(); i++) {
            records[i] = getPublicationRecord(publicationVersionBaseDtos.get(i));
        }
        return records;
    }

    public static PublicationRecord getPublicationRecord(PublicationVersionBaseDto publicationVersionBaseDto) {
        PublicationRecord record = (PublicationRecord) getSiemacMetadataRecord(new PublicationRecord(), publicationVersionBaseDto);
        record.setPublicationBaseDto(publicationVersionBaseDto);
        return record;
    }

    public static ElementLevelTreeNode getElementLevelNode(ElementLevelDto elementLevelDto) {

        ElementLevelTreeNode elementLevelNode = new ElementLevelTreeNode();
        NameableStatisticalResourceDto element = elementLevelDto.getChapter() != null ? elementLevelDto.getChapter() : elementLevelDto.getCube();
        elementLevelNode.setID(element.getUrn());
        elementLevelNode.setUrn(element.getUrn());
        elementLevelNode.setTitle(InternationalStringUtils.getLocalisedString(element.getTitle()));

        if (elementLevelDto.getCube() != null) {
            CubeDto cubeDto = elementLevelDto.getCube();
            String linkedResource = "";
            String queryDataset = "";

            if (cubeDto.getDatasetUrn() != null) {
                linkedResource = cubeDto.getDatasetUrn();
            } else if (cubeDto.getQueryUrn() != null) {
                linkedResource = cubeDto.getQueryUrn();
                queryDataset = cubeDto.getRelatedDatasetUrnInQuery();
            } else if (cubeDto.getMultidatasetUrn() != null) {
                linkedResource = cubeDto.getMultidatasetUrn();
            } else if (cubeDto.getCollectionUrn() != null) {
                linkedResource = cubeDto.getCollectionUrn();
            } else if (cubeDto.getUrl() != null) {
                linkedResource = cubeDto.getUrl();
            }

            if (linkedResource != null) {
                elementLevelNode.setLinkedResource(linkedResource);
                elementLevelNode.setLinkedResourceType(getResourceType(linkedResource));
            }
            if (queryDataset != null) {
                elementLevelNode.setQueryDataset(queryDataset);
            }

            elementLevelNode.setDSD(ResourceFieldUtils.formatExternalItemValue(cubeDto.getRelatedDSD(), true, false));

        }

        elementLevelNode.setDescription(InternationalStringUtils.getLocalisedString(element.getDescription()));
        if (element instanceof ChapterDto) {
            elementLevelNode.setOrderInLevel(((ChapterDto) element).getOrderInLevel());
            elementLevelNode.setParentChapterUrn(((ChapterDto) element).getParentChapterUrn());
        } else if (element instanceof CubeDto) {
            elementLevelNode.setOrderInLevel(((CubeDto) element).getOrderInLevel());
            elementLevelNode.setParentChapterUrn(((CubeDto) element).getParentChapterUrn());
            elementLevelNode.setIcon(org.siemac.metamac.statistical.resources.web.client.resources.GlobalResources.RESOURCE.treeFile().getURL());
        }
        elementLevelNode.setElementLevelDto(elementLevelDto);
        elementLevelNode.setAttribute(ElementLevelDS.INFO, GlobalResources.RESOURCE.info().getURL());
        return elementLevelNode;
    }

    private static String getResourceType(String value) {
        Map<String, String> statisticalResourceTypeMap = CommonUtils.getStatisticalResourceTypeThatCanBeAddIntoACubeInPublicationHashMap();

        int lastDotIndex = value.lastIndexOf(UrnConstants.DOT);
        int equalsIndex = value.indexOf(UrnConstants.EQUAL);

        if (lastDotIndex < 0 || equalsIndex < 0 || equalsIndex <= lastDotIndex) {
            return statisticalResourceTypeMap.get(StatisticalResourceTypeEnum.URL.name());
        }

        String resourceTypeInUrn = value.substring(lastDotIndex + 1, equalsIndex);

        return statisticalResourceTypeMap.get(resourceTypeInUrn.toUpperCase());
    }

    public static ElementLevelTreeNode getPublicationVersionRootNode(String schemeNodeName, PublicationVersionBaseDto publicationVersion) {
        ElementLevelTreeNode elementLevelTreeNode = new ElementLevelTreeNode();
        elementLevelTreeNode.setID(schemeNodeName);
        elementLevelTreeNode.setTitle(InternationalStringUtils.getLocalisedString(publicationVersion.getTitle()));

        return elementLevelTreeNode;
    }

    public static List<PublicationVersionBaseDto> getPublicationVersionBaseDtosFromListGridRecords(ListGridRecord[] records) {
        List<PublicationVersionBaseDto> publicationVersionBaseDtos = new ArrayList<PublicationVersionBaseDto>();
        if (records != null) {
            for (ListGridRecord record : records) {
                PublicationRecord datasetRecord = (PublicationRecord) record;
                publicationVersionBaseDtos.add(datasetRecord.getPublicationVersionBaseDto());
            }
        }
        return publicationVersionBaseDtos;
    }

    //
    // QUERIES
    //

    public static QueryRecord[] getQueryRecords(List<QueryVersionBaseDto> queryVersionBaseDtos) {
        QueryRecord[] records = new QueryRecord[queryVersionBaseDtos.size()];
        for (int i = 0; i < queryVersionBaseDtos.size(); i++) {
            records[i] = getQueryRecord(queryVersionBaseDtos.get(i));
        }
        return records;
    }

    public static QueryRecord getQueryRecord(QueryVersionBaseDto queryDto) {
        QueryRecord record = (QueryRecord) getLifeCycleResourceRecord(new QueryRecord(), queryDto);
        record.setRelatedDataset(queryDto.getRelatedDatasetVersion());
        record.setStatus(CommonUtils.getQueryStatusName(queryDto));
        record.setType(CommonUtils.getQueryTypeName(queryDto));
        record.setQueryVersionBaseDto(queryDto);
        record.setXPublicationStatus(CommonUtils.getXStatusIcon(queryDto.getXStreamStatus()));
        return record;
    }

    public static List<QueryVersionBaseDto> getQueryVersionDtosFromListGridRecords(ListGridRecord[] records) {
        List<QueryVersionBaseDto> queryVersionBaseDtos = new ArrayList<QueryVersionBaseDto>();
        if (records != null) {
            for (ListGridRecord record : records) {
                QueryRecord datasetRecord = (QueryRecord) record;
                queryVersionBaseDtos.add(datasetRecord.getQueryVersionBaseDto());
            }
        }
        return queryVersionBaseDtos;
    }

    //
    // Multidatasets
    //

    public static MultidatasetRecord[] getMultidatasetRecords(List<MultidatasetVersionBaseDto> multidatasetVersionBaseDtos) {
        MultidatasetRecord[] records = new MultidatasetRecord[multidatasetVersionBaseDtos.size()];
        for (int i = 0; i < multidatasetVersionBaseDtos.size(); i++) {
            records[i] = getMultidatasetRecord(multidatasetVersionBaseDtos.get(i));
        }
        return records;
    }

    public static MultidatasetRecord getMultidatasetRecord(MultidatasetVersionBaseDto multidatasetVersionBaseDto) {
        MultidatasetRecord record = (MultidatasetRecord) getSiemacMetadataRecord(new MultidatasetRecord(), multidatasetVersionBaseDto);
        record.setMultidatasetBaseDto(multidatasetVersionBaseDto);
        return record;
    }

    public static List<MultidatasetVersionBaseDto> getMultidatasetVersionBaseDtosFromListGridRecords(ListGridRecord[] records) {
        List<MultidatasetVersionBaseDto> multidatasetVersionBaseDtos = new ArrayList<MultidatasetVersionBaseDto>();
        if (records != null) {
            for (ListGridRecord record : records) {
                MultidatasetRecord datasetRecord = (MultidatasetRecord) record;
                multidatasetVersionBaseDtos.add(datasetRecord.getMultidatasetVersionBaseDto());
            }
        }
        return multidatasetVersionBaseDtos;
    }

    // Codes

    public static CodeItemRecord getCodeItemRecord(CodeItemDto codeItemDto) {
        return getCodeItemRecord(null, codeItemDto);
    }

    public static CodeItemRecord getCodeItemRecord(String dimensionId, CodeItemDto codeItemDto) {
        CodeItemRecord record = new CodeItemRecord();
        record.setCode(codeItemDto.getCode());
        record.setTitle(codeItemDto.getTitle());
        record.setDimensionId(dimensionId);
        record.setCodeItemDto(codeItemDto);
        return record;
    }

    public static CodeItemRecord[] getCodeItemRecords(List<CodeItemDto> codeItemDtos) {
        CodeItemRecord[] records = new CodeItemRecord[codeItemDtos.size()];
        for (int i = 0; i < codeItemDtos.size(); i++) {
            records[i] = getCodeItemRecord(codeItemDtos.get(i));
        }
        return records;
    }

    public static TemporalCodeRecord getTemporalCodeRecord(TemporalCodeDto temporalCodeDto) {
        TemporalCodeRecord record = new TemporalCodeRecord();
        record.setCode(temporalCodeDto.getIdentifier());
        record.setTitle(temporalCodeDto.getTitle());
        record.setTemporalCodeDto(temporalCodeDto);
        return record;
    }

    public static List<TemporalCodeRecord> getTemporalCodeRecords(List<TemporalCodeDto> dtos) {
        List<TemporalCodeRecord> records = new ArrayList<TemporalCodeRecord>();
        for (TemporalCodeDto dto : dtos) {
            records.add(getTemporalCodeRecord(dto));
        }
        return records;
    }

    //
    // VERSION RATIONALE TYPES
    //

    public static VersionRationaleTypeRecord getVersionRationaleTypeRecord(VersionRationaleTypeDto versionRationaleTypeDto) {
        VersionRationaleTypeRecord record = new VersionRationaleTypeRecord();
        record.setValue(CommonUtils.getVersionRationaleTypeName(versionRationaleTypeDto.getValue()));
        record.setVersionRationaleTypeDto(versionRationaleTypeDto);
        return record;
    }

    public static VersionRationaleTypeRecord[] getVersionRationaleTypeRecords(List<VersionRationaleTypeDto> versionRationaleTypeDtos) {
        VersionRationaleTypeRecord[] records = new VersionRationaleTypeRecord[versionRationaleTypeDtos.size()];
        for (int i = 0; i < versionRationaleTypeDtos.size(); i++) {
            records[i] = getVersionRationaleTypeRecord(versionRationaleTypeDtos.get(i));
        }
        return records;
    }

    //
    // DSD ATTRIBUTES
    //

    public static DsdAttributeRecord getDsdAttributeRecord(DsdAttributeDto dsdAttributeDto) {
        DsdAttributeRecord record = new DsdAttributeRecord();
        record.setIdentifier(dsdAttributeDto.getIdentifier());
        record.setRelationshipType(CommonUtils.getAttributeRelationshipTypeName(dsdAttributeDto.getAttributeRelationship().getRelationshipType()));
        record.setDsdAttributeDto(dsdAttributeDto);
        return record;
    }

    public static DsdAttributeRecord[] getDsdAttributeRecords(List<DsdAttributeDto> dsdAttributeDtos) {
        DsdAttributeRecord[] records = new DsdAttributeRecord[dsdAttributeDtos.size()];
        for (int i = 0; i < dsdAttributeDtos.size(); i++) {
            records[i] = getDsdAttributeRecord(dsdAttributeDtos.get(i));
        }
        return records;
    }

    //
    // DSD ATTRIBUTE INSTANCES
    //

    public static DsdAttributeInstanceRecord getDsdAttributeInstanceRecord(DsdAttributeInstanceDto dsdAttributeInstanceDto, DsdAttributeDto dsdAttributeDto) {
        DsdAttributeInstanceRecord record = new DsdAttributeInstanceRecord();
        if (dsdAttributeInstanceDto.getValue() != null) {
            if (dsdAttributeInstanceDto.getValue().getExternalItemValue() != null) {
                record.setExternalItemValue(dsdAttributeInstanceDto.getValue().getExternalItemValue());
            } else if (Boolean.TRUE.equals(dsdAttributeDto.getAttributeRepresentation().getMultilingualType())) {
                record.setStringValue(getTitleFromInternationalStringValue(dsdAttributeInstanceDto.getValue()));
            } else {
                record.setStringValue(dsdAttributeInstanceDto.getValue().getStringValue());
            }
        }
        record.setDsdAttributeInstaceDto(dsdAttributeInstanceDto);
        record.setUuid(dsdAttributeInstanceDto.getUuid());
        return record;
    }

    private static String getTitleFromInternationalStringValue(AttributeValueDto attributeValueDto) {
        if (attributeValueDto != null && attributeValueDto.getInternationalStringValue() != null) {
            return InternationalStringUtils.getLocalisedString(attributeValueDto.getInternationalStringValue(), ApplicationEditionLanguages.getCurrentLocale());
        }
        return "-";
    }

    public static DsdAttributeInstanceRecord[] getDsdAttributeInstanceRecords(List<DsdAttributeInstanceDto> dsdAttributeInstanceDtos, DsdAttributeDto dsdAttributeDto) {
        DsdAttributeInstanceRecord[] records = new DsdAttributeInstanceRecord[dsdAttributeInstanceDtos.size()];
        for (int i = 0; i < dsdAttributeInstanceDtos.size(); i++) {
            records[i] = getDsdAttributeInstanceRecord(dsdAttributeInstanceDtos.get(i), dsdAttributeDto);
        }
        return records;
    }

    // MULTIDATASET CUBE TREE NODE

    public static MultidatasetCubeTreeNode getMultidatasetCubeNode(MultidatasetCubeDto multidatasetCubeDto) {
        MultidatasetCubeTreeNode multidatasetCubeNode = new MultidatasetCubeTreeNode();

        multidatasetCubeNode.setID(multidatasetCubeDto.getUrn());
        multidatasetCubeNode.setUrn(multidatasetCubeDto.getUrn());
        multidatasetCubeNode.setIdentifier(multidatasetCubeDto.getIdentifier());
        multidatasetCubeNode.setTitle(InternationalStringUtils.getLocalisedString(multidatasetCubeDto.getTitle()));
        multidatasetCubeNode.setDescription(InternationalStringUtils.getLocalisedString(multidatasetCubeDto.getDescription()));

        String linkedResource = "";
        String queryDataset = "";

        if (multidatasetCubeDto.getDatasetUrn() != null) {
            linkedResource = multidatasetCubeDto.getDatasetUrn();
        } else if (multidatasetCubeDto.getQueryUrn() != null) {
            linkedResource = multidatasetCubeDto.getQueryUrn();
            queryDataset = multidatasetCubeDto.getRelatedDatasetUrnInQuery();
        }

        if (linkedResource != null) {
            multidatasetCubeNode.setLinkedResource(linkedResource);
            multidatasetCubeNode.setLinkedTypeResource(getResourceType(linkedResource));
        }
        if (queryDataset != null) {
            multidatasetCubeNode.setQueryDataset(queryDataset);
        }

        multidatasetCubeNode.setDSD(ResourceFieldUtils.formatExternalItemValue(multidatasetCubeDto.getRelatedDSD(), true, false));


        multidatasetCubeNode.setOrderInMultidataset(multidatasetCubeDto.getOrderInMultidataset());
        multidatasetCubeNode.setIcon(org.siemac.metamac.statistical.resources.web.client.resources.GlobalResources.RESOURCE.treeFile().getURL());

        multidatasetCubeNode.setAttribute(ElementLevelDS.INFO, GlobalResources.RESOURCE.info().getURL());

        multidatasetCubeNode.setMultidatasetCubeDto(multidatasetCubeDto);

        return multidatasetCubeNode;
    }

    public static MultidatasetCubeTreeNode getMultidatasetVersionRootNode(String schemeNodeName, MultidatasetVersionDto multidatasetVersion) {
        MultidatasetCubeTreeNode multidatasetCubeTreeNode = new MultidatasetCubeTreeNode();
        multidatasetCubeTreeNode.setID(schemeNodeName);
        multidatasetCubeTreeNode.setTitle(InternationalStringUtils.getLocalisedString(multidatasetVersion.getTitle()));
        return multidatasetCubeTreeNode;
    }
}
