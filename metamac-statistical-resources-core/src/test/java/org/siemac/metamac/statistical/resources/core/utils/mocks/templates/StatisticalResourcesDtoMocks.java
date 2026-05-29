package org.siemac.metamac.statistical.resources.core.utils.mocks.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.common.test.constants.ConfigurationMockConstants;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.dto.IdentifiableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.StatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionRationaleTypeDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.AttributeValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.StatisticOfficialityDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetCubeDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.ChapterDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.CubeDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DataSourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticalResourcesMockFactory;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;

public class StatisticalResourcesDtoMocks extends MetamacMocks {

    private static final String URN_RELATED_RESOURCE_MOCK = "urn:lorem.ipsum.dolor.infomodel.package.Resource=" + mockString(10);

    // -----------------------------------------------------------------
    // QUERY
    // -----------------------------------------------------------------

    public static QueryVersionDto mockQueryVersionDto(DatasetVersion datasetVersion) {
        QueryVersionDto queryVersionDto = new QueryVersionDto();

        mockLifeCycleStatisticalResourceDto(queryVersionDto);

        // code is not setting in nameable becasuse some resources have generated code
        queryVersionDto.setCode(mockString(8));

        queryVersionDto.setRelatedDatasetVersion(mockPersistedRelatedResourceDatasetVersionDto(datasetVersion));
        queryVersionDto.setQueryType(QueryTypeEnum.FIXED);

        Map<String, List<CodeItemDto>> selection = new HashMap<String, List<CodeItemDto>>();
        selection.put("SEX", Arrays.asList(mockCodeItemDto("FEMALE", "Female")));
        selection.put("REGION", Arrays.asList(mockCodeItemDto("TENERIFE", "Tenerife"), mockCodeItemDto("LA_GOMERA", "La gomera")));
        queryVersionDto.setSelection(selection);

        return queryVersionDto;
    }

    // -----------------------------------------------------------------
    // CODE ITEMS
    // -----------------------------------------------------------------

    public static CodeItemDto mockCodeItemDto() {
        return mockCodeItemDto("code-" + mockString(5), mockString(10));
    }

    public static List<CodeItemDto> mockCodeItemDtos() {
        List<CodeItemDto> codeItemDtos = new ArrayList<CodeItemDto>();
        codeItemDtos.add(mockCodeItemDto());
        codeItemDtos.add(mockCodeItemDto());
        codeItemDtos.add(mockCodeItemDto());
        return codeItemDtos;
    }

    private static Map<String, List<CodeItemDto>> mockCodeIdemMap() {
        Map<String, List<CodeItemDto>> map = new HashMap<String, List<CodeItemDto>>();
        map.put(mockString(8), mockCodeItemDtos());
        map.put(mockString(8), mockCodeItemDtos());
        map.put(mockString(8), mockCodeItemDtos());
        return map;
    }

    public static CodeItemDto mockCodeItemDto(String code, String title) {
        CodeItemDto codeItem = new CodeItemDto();
        codeItem.setCode(code);
        codeItem.setTitle(title);
        return codeItem;
    }

    public static List<CodeItemDto> mockCodeItemDtosWithIdentifiers(String... identifiers) {
        List<CodeItemDto> codes = new ArrayList<CodeItemDto>();
        for (String identifier : identifiers) {
            codes.add(mockCodeItemDto(identifier, identifier));
        }
        return codes;
    }

    // -----------------------------------------------------------------
    // DATASOURCES
    // -----------------------------------------------------------------

    public static DatasourceDto mockDatasourceDto() {
        DatasourceDto datasourceDto = new DatasourceDto();

        mockIdentifiableStatisticalResourceDto(datasourceDto);

        datasourceDto.setCode(mockString(8));
        datasourceDto.setDatasetVersionUrn(URN_RELATED_RESOURCE_MOCK);

        return datasourceDto;
    }

    public static DatasourceDto mockDatasourceDto(String code, DatasetVersionDto datasetVersionDto) {
        DatasourceDto datasourceDto = new DatasourceDto();

        mockIdentifiableStatisticalResourceDto(datasourceDto);

        datasourceDto.setCode(code);
        datasourceDto.setDatasetVersionUrn(datasetVersionDto.getUrn());
        datasourceDto.setStatisticalOperation(datasetVersionDto.getStatisticalOperation());

        return datasourceDto;
    }

    // -----------------------------------------------------------------
    // ATTRIBUTES
    // -----------------------------------------------------------------

    public static DsdAttributeInstanceDto mockDsdAttributeInstanceDtoWithNonNumeratedValue() {
        DsdAttributeInstanceDto dsdAttributeInstanceDto = mockDsdAttributeInstanceDtoWithoutValue();
        dsdAttributeInstanceDto.setValue(mockNonNumeratedAttributeValue());
        return dsdAttributeInstanceDto;
    }

    public static DsdAttributeInstanceDto mockDsdAttributeInstanceDtoWithEnumeratedValue() {
        DsdAttributeInstanceDto dsdAttributeInstanceDto = mockDsdAttributeInstanceDtoWithoutValue();
        dsdAttributeInstanceDto.setValue(mockEnumeratedAttributeValue());
        return dsdAttributeInstanceDto;
    }

    public static AttributeInstanceDto mockAttributeInstanceDto() {
        AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
        attributeInstanceDto.setAttributeId("attribute_" + mockString(8));

        attributeInstanceDto.setValue(new es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto());
        es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto localisedValue = new es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto();
        localisedValue.setLocale(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE);
        localisedValue.setLabel(mockString(10));
        attributeInstanceDto.getValue().addText(localisedValue);

        attributeInstanceDto.setCodesByDimension(mockStringListMap());

        return attributeInstanceDto;
    }

    private static Map<String, List<String>> mockStringListMap() {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put(mockString(8), mockStringList(3));
        return map;
    }

    private static List<String> mockStringList(int numberOfElemets) {
        List<String> stringList = new ArrayList<String>();
        for (int i = 0; i < numberOfElemets; i++) {
            stringList.add(mockString(8));
        }
        return stringList;
    }

    private static DsdAttributeInstanceDto mockDsdAttributeInstanceDtoWithoutValue() {
        DsdAttributeInstanceDto dsdAttributeInstanceDto = new DsdAttributeInstanceDto();
        dsdAttributeInstanceDto.setAttributeId("attribute_" + mockString(8));
        dsdAttributeInstanceDto.setUuid(mockString(10));
        dsdAttributeInstanceDto.setCodeDimensions(mockCodeIdemMap());
        return dsdAttributeInstanceDto;
    }

    private static AttributeValueDto mockNonNumeratedAttributeValue() {
        AttributeValueDto attributeValueDto = new AttributeValueDto();
        attributeValueDto.setStringValue(mockString(5));
        return attributeValueDto;
    }

    private static AttributeValueDto mockEnumeratedAttributeValue() {
        AttributeValueDto attributeValueDto = new AttributeValueDto();
        attributeValueDto.setExternalItemValue(mockExternalItemDto("CODE_01", null, ConfigurationMockConstants.SRM_INTERNAL_API_URL_BASE + CoreCommonConstants.URL_SEPARATOR + "URI_01",
                "URN_01_provider", "URN_01", TypeExternalArtefactsEnum.CODE));
        return attributeValueDto;
    }

    // -----------------------------------------------------------------
    // DATASETS
    // -----------------------------------------------------------------

    public static DatasetVersionDto mockDatasetVersionDto(StatisticOfficiality officiality) {
        DatasetVersionDto datasetVersionDto = new DatasetVersionDto();

        datasetVersionDto.addGeographicGranularity(mockCodeExternalItemDto());
        datasetVersionDto.addGeographicGranularity(mockCodeExternalItemDto());

        datasetVersionDto.addTemporalGranularity(mockCodeExternalItemDto());
        datasetVersionDto.addTemporalGranularity(mockCodeExternalItemDto());

        datasetVersionDto.setDateStart(mockSDMXDate());
        datasetVersionDto.setDateEnd(mockSDMXDate());

        datasetVersionDto.addStatisticalUnit(mockConceptExternalItemDto());
        datasetVersionDto.addStatisticalUnit(mockConceptExternalItemDto());

        datasetVersionDto.setRelatedDsd(mockDsdExternalItemDto());

        datasetVersionDto.setFormatExtentDimensions(5);
        datasetVersionDto.setFormatExtentTableSize(8L);
        datasetVersionDto.setFormatExtentObservations(8L);

        datasetVersionDto.setDateNextUpdate(mockSDMXDate());
        datasetVersionDto.setUpdateFrequency(mockCodeExternalItemDto());
        datasetVersionDto.setStatisticOfficiality(createStatisticOfficialityDtoFromDo(officiality));
        datasetVersionDto.setBibliographicCitation(mockInternationalStringDto());

        mockSiemacMetadataStatisticalResource(datasetVersionDto, StatisticalResourceTypeEnum.DATASET);

        datasetVersionDto.setKeepAllData(Boolean.TRUE);
        datasetVersionDto.setDataSourceType(DataSourceTypeEnum.FILE);

        return datasetVersionDto;
    }

    private static String mockSDMXDate() {
        return CoreCommonUtil.jodaDateTime2IsoDate(mockDate());
    }

    // -----------------------------------------------------------------
    // CATEGORISATIONS
    // -----------------------------------------------------------------

    public static CategorisationDto mockCategorisationDto() {
        CategorisationDto categorisationDto = new CategorisationDto();
        categorisationDto.setMaintainer(mockAgencyExternalItemDto());
        categorisationDto.setCategory(mockCategoryExternalItemDto());
        return categorisationDto;
    }

    // -----------------------------------------------------------------
    // PUBLICATIONS
    // -----------------------------------------------------------------

    public static PublicationVersionDto mockPublicationVersionDto() {
        PublicationVersionDto publicationVersionDto = new PublicationVersionDto();

        publicationVersionDto.setFormatExtentResources(RandomUtils.nextInt(999));

        mockSiemacMetadataStatisticalResource(publicationVersionDto, StatisticalResourceTypeEnum.COLLECTION);

        return publicationVersionDto;
    }

    public static ChapterDto mockChapterDto() {
        ChapterDto chapterDto = mockChapterDtoWithParent(null);

        return chapterDto;
    }

    public static ChapterDto mockChapterDtoWithParent(String parentChapterUrn) {
        ChapterDto chapterDto = new ChapterDto();

        chapterDto.setOrderInLevel(Long.valueOf(2));
        chapterDto.setParentChapterUrn(parentChapterUrn);

        mockNameableStatisticalResorceDto(chapterDto);

        return chapterDto;
    }

    public static CubeDto mockDatasetCubeDto(String datasetUrn) {
        CubeDto cubeDto = mockCubeDto(null, datasetUrn, null, null);
        return cubeDto;
    }

    public static CubeDto mockDatasetCubeDtoWithParent(String parentChapterUrn, String datasetUrn) {
        CubeDto cubeDto = mockCubeDto(parentChapterUrn, datasetUrn, null, null);
        return cubeDto;
    }

    public static CubeDto mockQueryCubeDto(String queryUrn) {
        CubeDto cubeDto = mockCubeDto(null, null, queryUrn, null);
        return cubeDto;
    }

    public static CubeDto mockQueryCubeDtoWithParent(String parentChapterUrn, String queryUrn) {
        CubeDto cubeDto = mockCubeDto(parentChapterUrn, null, queryUrn, null);
        return cubeDto;
    }

    private static CubeDto mockCubeDto(String parentChapterUrn, String datasetUrn, String queryUrn, String multidatasetUrn) {
        CubeDto cubeDto = new CubeDto();

        cubeDto.setDatasetUrn(datasetUrn);
        cubeDto.setQueryUrn(queryUrn);
        cubeDto.setMultidatasetUrn(multidatasetUrn);
        cubeDto.setOrderInLevel(Long.valueOf(2));
        cubeDto.setParentChapterUrn(parentChapterUrn);

        mockNameableStatisticalResorceDto(cubeDto);

        return cubeDto;
    }

    // -----------------------------------------------------------------
    // MULTIDATASETS
    // -----------------------------------------------------------------

    public static MultidatasetVersionDto mockMultidatasetVersionDto() {
        MultidatasetVersionDto multidatasetVersionDto = new MultidatasetVersionDto();

        multidatasetVersionDto.setFormatExtentResources(RandomUtils.nextInt(999));

        mockSiemacMetadataStatisticalResource(multidatasetVersionDto, StatisticalResourceTypeEnum.MULTIDATASET);

        return multidatasetVersionDto;
    }

    public static MultidatasetCubeDto mockDatasetMultidatasetCubeDto(String datasetUrn) {
        MultidatasetCubeDto multidatasetCubeDto = mockMultidatasetCubeDto(datasetUrn, null);
        return multidatasetCubeDto;
    }

    public static MultidatasetCubeDto mockQueryMultidatasetCubeDto(String queryUrn) {
        MultidatasetCubeDto multidatasetCubeDto = mockMultidatasetCubeDto(null, queryUrn);
        return multidatasetCubeDto;
    }

    private static MultidatasetCubeDto mockMultidatasetCubeDto(String datasetUrn, String queryUrn) {
        MultidatasetCubeDto multidatasetCubeDto = new MultidatasetCubeDto();

        multidatasetCubeDto.setIdentifier("id-" + MetamacMocks.mockString(10));
        multidatasetCubeDto.setDatasetUrn(datasetUrn);
        multidatasetCubeDto.setQueryUrn(queryUrn);
        multidatasetCubeDto.setOrderInMultidataset(Long.valueOf(2));

        mockNameableStatisticalResorceDto(multidatasetCubeDto);

        return multidatasetCubeDto;
    }

    // -----------------------------------------------------------------
    // BASE HIERARCHY
    // -----------------------------------------------------------------

    @SuppressWarnings("deprecation")
    private static void mockSiemacMetadataStatisticalResource(SiemacMetadataStatisticalResourceDto siemacMetadataStatisticalResourceDto, StatisticalResourceTypeEnum type) {
        String langCode = mockString(5);
        siemacMetadataStatisticalResourceDto.setLanguage(mockCodeExternalItemDto(langCode));
        siemacMetadataStatisticalResourceDto.addLanguage(mockCodeExternalItemDto(langCode));
        siemacMetadataStatisticalResourceDto.addLanguage(mockCodeExternalItemDto());

        // siemacMetadataStatisticalResourceDto.setStatisticalOperation(mockStatisticalOperationItem());

        siemacMetadataStatisticalResourceDto.getStatisticalOperationInstances().clear();
        siemacMetadataStatisticalResourceDto.getStatisticalOperationInstances().add(mockStatisticalOperationInstanceExternalItemDto());

        siemacMetadataStatisticalResourceDto.setSubtitle(mockInternationalStringDto());
        siemacMetadataStatisticalResourceDto.setTitleAlternative(mockInternationalStringDto());
        siemacMetadataStatisticalResourceDto.setAbstractLogic(mockInternationalStringDto());
        siemacMetadataStatisticalResourceDto.setKeywords(mockInternationalStringDto());
        siemacMetadataStatisticalResourceDto.setType(type);

        siemacMetadataStatisticalResourceDto.setCreator(mockOrganizationUnitExternalItemDto());
        siemacMetadataStatisticalResourceDto.addDataProvider(mockDataProviderExternalItemDto());
        siemacMetadataStatisticalResourceDto.setDataProviderAnnotations(mockInternationalStringDto());
        siemacMetadataStatisticalResourceDto.addContributor(mockOrganizationUnitExternalItemDto());
        siemacMetadataStatisticalResourceDto.setResourceCreatedDate(mockDate());
        siemacMetadataStatisticalResourceDto.setLastUpdate(mockDate());
        siemacMetadataStatisticalResourceDto.setConformsTo(mockInternationalStringDto());
        siemacMetadataStatisticalResourceDto.setConformsToInternal(mockInternationalStringDto());

        siemacMetadataStatisticalResourceDto.addPublisher(mockOrganizationUnitExternalItemDto());
        siemacMetadataStatisticalResourceDto.addPublisherContributor(mockOrganizationUnitExternalItemDto());
        siemacMetadataStatisticalResourceDto.addMediator(mockOrganizationUnitExternalItemDto());
        siemacMetadataStatisticalResourceDto.setNewnessUntilDate(mockDate());

        siemacMetadataStatisticalResourceDto.setReplacesVersion(null);
        siemacMetadataStatisticalResourceDto.setIsReplacedByVersion(null);

        siemacMetadataStatisticalResourceDto.setCommonMetadata(mockCommonMetadataExternalItemDto());
        siemacMetadataStatisticalResourceDto.setCopyrightedDate(mockDate().getYear());
        siemacMetadataStatisticalResourceDto.setAccessRights(mockInternationalStringDto());

        mockLifeCycleStatisticalResourceDto(siemacMetadataStatisticalResourceDto);
    }

    private static void mockLifeCycleStatisticalResourceDto(LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto) {

        lifeCycleStatisticalResourceDto.setCreationDate(new DateTime().toDate());
        lifeCycleStatisticalResourceDto.setCreationUser(mockString(10));
        lifeCycleStatisticalResourceDto.setProductionValidationDate(new DateTime().toDate());
        lifeCycleStatisticalResourceDto.setProductionValidationUser(mockString(10));
        lifeCycleStatisticalResourceDto.setDiffusionValidationDate(new DateTime().toDate());
        lifeCycleStatisticalResourceDto.setDiffusionValidationUser(mockString(10));
        lifeCycleStatisticalResourceDto.setRejectValidationDate(new DateTime().toDate());
        lifeCycleStatisticalResourceDto.setRejectValidationUser(mockString(10));
        lifeCycleStatisticalResourceDto.setPublicationDate(new DateTime().toDate());
        lifeCycleStatisticalResourceDto.setPublicationUser(mockString(10));

        lifeCycleStatisticalResourceDto.setMaintainer(mockAgencyExternalItemDto());

        // Can not mock replacedByVersion and replacesVersion, we don't know the type of resource

        mockVersionableStatisticalResourceDto(lifeCycleStatisticalResourceDto);
    }

    private static void mockVersionableStatisticalResourceDto(VersionableStatisticalResourceDto versionableStatisticalResourceDto) {
        versionableStatisticalResourceDto.setVersionLogic(StatisticalResourcesMockFactory.ANOTHER_NOT_INITIAL_VERSION);
        versionableStatisticalResourceDto.setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().toDate()));
        versionableStatisticalResourceDto.setValidFrom(new DateTime().toDate());
        versionableStatisticalResourceDto.setValidTo(new DateTime().toDate());

        versionableStatisticalResourceDto.addVersionRationaleType(new VersionRationaleTypeDto(VersionRationaleTypeEnum.MINOR_OTHER));
        versionableStatisticalResourceDto.setVersionRationale(mockInternationalStringDto());

        versionableStatisticalResourceDto.setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);

        mockNameableStatisticalResorceDto(versionableStatisticalResourceDto);
    }

    private static void mockNameableStatisticalResorceDto(NameableStatisticalResourceDto nameableResourceDto) {
        nameableResourceDto.setTitle(mockInternationalStringDto());
        nameableResourceDto.setDescription(mockInternationalStringDto());

        mockIdentifiableStatisticalResourceDto(nameableResourceDto);
    }

    private static void mockIdentifiableStatisticalResourceDto(IdentifiableStatisticalResourceDto resource) {
        mockStatisticalResourceDto(resource);
    }

    private static void mockStatisticalResourceDto(StatisticalResourceDto resource) {
        // resource.setOperation(mockExternalItemDto(URN_RELATED_RESOURCE_MOCK, TypeExternalArtefactsEnum.STATISTICAL_OPERATION));
    }

    // EXTERNAL ITEMS DTOs

    public static ExternalItemDto mockExternalItemDto(String code, String codeNested, String uri, String urnProvider, String urn, TypeExternalArtefactsEnum type) {
        ExternalItemDto target = new ExternalItemDto();
        target.setCode(code);
        target.setCodeNested(codeNested);
        target.setUri(uri);
        target.setUrn(urn);
        target.setUrnProvider(urnProvider);
        target.setType(type);
        return target;
    }

    // RELATED RESOURCE DTOs

    public static RelatedResourceDto mockNotPersistedRelatedResourceDatasetVersionDto(DatasetVersion datasetVersion) {
        RelatedResourceDto resource = new RelatedResourceDto();
        populateNotPersistedRelatedResourceIdentifiable(resource, datasetVersion.getSiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.DATASET_VERSION);

        resource.setLastVersion(datasetVersion.getLifeCycleStatisticalResource().getLastVersion());
        resource.setVersionLogic(datasetVersion.getLifeCycleStatisticalResource().getVersionLogic());

        return resource;
    }

    public static RelatedResourceDto mockNotPersistedRelatedResourcePublicationVersionDto(PublicationVersion publicationVersion) {
        RelatedResourceDto resource = new RelatedResourceDto();
        populateNotPersistedRelatedResourceIdentifiable(resource, publicationVersion.getSiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.PUBLICATION_VERSION);
        return resource;
    }

    public static RelatedResourceDto mockPersistedRelatedResourceDatasetVersionDto(DatasetVersion datasetVersion) {
        RelatedResourceDto resource = new RelatedResourceDto();
        populatePersistedRelatedResourceNameable(resource, datasetVersion.getSiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.DATASET_VERSION);

        resource.setLastVersion(datasetVersion.getLifeCycleStatisticalResource().getLastVersion());
        resource.setVersionLogic(datasetVersion.getLifeCycleStatisticalResource().getVersionLogic());

        return resource;
    }

    private static void populateNotPersistedRelatedResourceIdentifiable(RelatedResourceDto relatedDto, IdentifiableStatisticalResource identifiable, TypeRelatedResourceEnum type) {
        relatedDto.setUrn(identifiable.getUrn());
        relatedDto.setType(type);
    }

    private static void populatePersistedRelatedResourceIdentifiable(RelatedResourceDto relatedDto, IdentifiableStatisticalResource identifiable, TypeRelatedResourceEnum type) {
        relatedDto.setUrn(identifiable.getUrn());
        relatedDto.setType(type);
        relatedDto.setCode(identifiable.getCode());
    }

    private static void populatePersistedRelatedResourceNameable(RelatedResourceDto relatedDto, NameableStatisticalResource nameable, TypeRelatedResourceEnum type) {
        populatePersistedRelatedResourceIdentifiable(relatedDto, nameable, type);
        relatedDto.setTitle(createInternationalStringDtoFromDo(nameable.getTitle()));
    }

    // STATISTIC OFFICIALITY DTOs

    public static StatisticOfficialityDto createStatisticOfficialityDtoFromDo(StatisticOfficiality officiality) {
        if (officiality != null) {
            StatisticOfficialityDto dto = new StatisticOfficialityDto();
            dto.setIdentifier(officiality.getIdentifier());
            dto.setId(officiality.getId());
            dto.setDescription(createInternationalStringDtoFromDo(officiality.getDescription()));
            return dto;
        }
        return null;
    }

    private static InternationalStringDto createInternationalStringDtoFromDo(InternationalString intString) {
        InternationalStringDto dto = new InternationalStringDto();

        dto.setId(intString.getId());
        for (LocalisedString loc : intString.getTexts()) {
            LocalisedStringDto locDto = new LocalisedStringDto();
            locDto.setId(loc.getId());
            locDto.setIsUnmodifiable(loc.getIsUnmodifiable());
            locDto.setLabel(loc.getLabel());
            locDto.setLocale(loc.getLocale());
            locDto.setVersion(loc.getVersion());
            dto.addText(locDto);
        }
        return dto;
    }

    // UTILS

    private static Date mockDate() {
        return mockDateTime().toDate();
    }

}