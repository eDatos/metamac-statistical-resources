package org.siemac.metamac.statistical.resources.core.utils.mocks.templates;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.enume.utils.TypeExternalArtefactsEnumUtils;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.StatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.AttributeValue;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DimensionRepresentationMapping;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.dataset.utils.DatasetVersionUtils;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetCube;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.Chapter;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.Publication;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.mocks.DatasetMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.DatasetVersionMock;

public abstract class StatisticalResourcesDoMocks extends MetamacMocks {

    public static final String    DEFAULT_DATA_LOCALE = "es";
    protected static final String USER_MOCK           = "MockedUser";

    // -----------------------------------------------------------------
    // QUERY VERSION
    // -----------------------------------------------------------------
    public abstract QueryVersion mockQueryVersion(DatasetVersion datasetVersion, boolean isDatasetLastVersion);

    public abstract QueryVersion mockQueryVersion(Dataset dataset);

    public QueryVersion mockQueryVersionWithGeneratedDatasetVersion() {
        DatasetVersionMock template = new DatasetVersionMock();
        template.addDimensionsCoverage(new CodeDimension("DIM01", "CODE01"));
        template.addDimensionsCoverage(new CodeDimension("DIM01", "CODE02"));
        template.addDimensionsCoverage(new CodeDimension("DIM02", "CODE01"));
        template.addDimensionsCoverage(new CodeDimension("DIM02", "CODE02"));

        DatasetVersion datasetVersion = mockDatasetVersion(template);
        QueryVersion query = mockQueryVersion(datasetVersion, true);
        return query;
    }

    public QueryVersion mockQueryVersionWithDatasetVersion(DatasetVersion datasetVersion, boolean isDatasetLastVersion) {
        return mockQueryVersion(datasetVersion, isDatasetLastVersion);
    }

    protected void mockQuerySelectionFromDatasetVersion(QueryVersion queryVersion, DatasetVersion datasetVersion) {
        List<CodeDimension> codes = datasetVersion.getDimensionsCoverage();

        if (codes.isEmpty()) {
            throw new IllegalArgumentException("Can't create a query linked to a dataset with no coverages");
        }

        Map<String, QuerySelectionItem> selections = new HashMap<String, QuerySelectionItem>();
        for (CodeDimension code : codes) {
            String dimensionId = code.getDsdComponentId();
            QuerySelectionItem selectionItem = selections.get(dimensionId);
            if (selectionItem == null) {
                selectionItem = new QuerySelectionItem();
                selectionItem.setDimension(dimensionId);
                selections.put(dimensionId, selectionItem);
            }
            selectionItem.addCode(new CodeItem(code.getIdentifier()));
        }
        queryVersion.getSelection().clear();
        for (String dimensionId : selections.keySet()) {
            queryVersion.addSelection(selections.get(dimensionId));
        }
    }

    // -----------------------------------------------------------------
    // DATASOURCE
    // -----------------------------------------------------------------
    public Datasource mockDatasource(Datasource datasource) {
        if (datasource.getIdentifiableStatisticalResource() == null) {
            datasource.setIdentifiableStatisticalResource(new IdentifiableStatisticalResource());
        }

        if (datasource.getSourceName() == null) {
            datasource.setSourceName(mockString(10));
        }

        if (datasource.getIdentifiableStatisticalResource().getCode() == null) {
            datasource.getIdentifiableStatisticalResource().setCode(datasource.getSourceName() + "_" + new DateTime().toString());
        }

        datasource.setIdentifiableStatisticalResource(mockIdentifiableStatisticalResource(datasource.getIdentifiableStatisticalResource(), TypeRelatedResourceEnum.DATASOURCE));

        return datasource;
    }

    protected Datasource mockDatasourceWithGeneratedDatasetVersion() {
        Datasource datasource = new Datasource();
        datasource.setSourceName(mockString(10));
        datasource.setIdentifiableStatisticalResource(mockIdentifiableStatisticalResource(new IdentifiableStatisticalResource(), TypeRelatedResourceEnum.DATASOURCE));
        datasource.getIdentifiableStatisticalResource().setCode(datasource.getSourceName() + "_" + new DateTime().toString());

        return datasource;
    }

    // -----------------------------------------------------------------
    // DATASET
    // -----------------------------------------------------------------
    public abstract void mockDataset(DatasetMock dataset);

    // -----------------------------------------------------------------
    // DATASET VERSION
    // -----------------------------------------------------------------
    public abstract DatasetVersion mockDatasetVersion();

    public abstract DatasetVersion mockDatasetVersion(DatasetVersionMock datasetVersion);

    // -----------------------------------------------------------------
    // CATEGORISATION
    // -----------------------------------------------------------------
    public Categorisation mockCategorisation(Categorisation categorisation) {
        if (categorisation.getVersionableStatisticalResource() == null) {
            categorisation.setVersionableStatisticalResource(new VersionableStatisticalResource());
        }

        if (categorisation.getVersionableStatisticalResource().getCode() == null) {
            categorisation.getVersionableStatisticalResource().setCode(StatisticalResourcesConstants.CATEGORISATION_CODE_PREFIX + new DateTime().toString());
        }

        categorisation.setVersionableStatisticalResource(mockVersionableStatisticalResource(categorisation.getVersionableStatisticalResource(), TypeRelatedResourceEnum.CATEGORISATION));

        return categorisation;
    }

    // -----------------------------------------------------------------
    // PUBLICATION VERSION
    // -----------------------------------------------------------------

    public abstract PublicationVersion mockPublicationVersion();

    // CHAPTER
    public Chapter mockChapter() {
        return mockChapter(mockElementLevel());
    }

    public Chapter mockChapterInParentElementLevel(ElementLevel parentElementLevel) {
        Chapter chapter = mockChapter(mockElementLevel());
        chapter.getElementLevel().setParent(parentElementLevel);
        return chapter;
    }

    public Chapter mockChapter(ElementLevel elementLevel) {
        Chapter chapter = new Chapter();

        // Element level
        chapter.setElementLevel(elementLevel);
        elementLevel.setChapter(chapter);

        // Metadata
        chapter.setNameableStatisticalResource(mockNameableStatisticalResorce(TypeRelatedResourceEnum.CHAPTER));

        return chapter;
    }

    // CUBE
    private Cube mockCube() {
        return mockCube(mockElementLevel());
    }

    private Cube mockCube(ElementLevel elementLevel) {
        Cube cube = new Cube();

        // Element level
        cube.setElementLevel(elementLevel);
        elementLevel.setCube(cube);

        // Metadata
        cube.setNameableStatisticalResource(mockNameableStatisticalResorce(TypeRelatedResourceEnum.CUBE));
        return cube;
    }

    public Cube mockDatasetCube(Dataset dataset) {
        Cube cube = mockCube();
        cube.setDataset(dataset);
        return cube;
    }

    public Cube mockQueryCube(Query query) {
        Cube cube = mockCube();
        cube.setQuery(query);
        return cube;
    }

    public Cube mockMultidatasetCube(Multidataset multidataset) {
        Cube cube = mockCube();
        cube.setMultidataset(multidataset);
        return cube;
    }

    public Cube mockUrlCube(String url) {
        Cube cube = mockCube();
        cube.setUrl(url);
        return cube;
    }

    // ELEMENT LEVEL
    private ElementLevel mockElementLevel() {
        return mockElementLevel(null, null);
    }

    private ElementLevel mockElementLevel(PublicationVersion publicationVersion, ElementLevel parentElementLevel) {
        ElementLevel elementLevel = new ElementLevel();

        elementLevel.setOrderInLevel(Long.valueOf(1));

        if (publicationVersion == null) {
            publicationVersion = mockPublicationVersion();
        }

        // Relation with publicationVersion
        elementLevel.setPublicationVersion(publicationVersion);
        publicationVersion.addChildrenAllLevel(elementLevel);

        if (parentElementLevel == null) {
            elementLevel.setPublicationVersionFirstLevel(publicationVersion);
            publicationVersion.addChildrenFirstLevel(elementLevel);
        } else {
            elementLevel.setParent(parentElementLevel);
            parentElementLevel.addChildren(elementLevel);
        }

        return elementLevel;
    }

    public ElementLevel mockChapterElementLevel(PublicationVersion publicationVersion) {
        return mockChapterElementLevel(publicationVersion, null);
    }

    public ElementLevel mockChapterElementLevel(PublicationVersion publicationVersion, ElementLevel parentElementLevel) {
        ElementLevel elementLevel = mockElementLevel(publicationVersion, parentElementLevel);

        // Chapter relation
        elementLevel.setChapter(mockChapter(elementLevel));

        return elementLevel;
    }

    public ElementLevel mockDatasetCubeElementLevel(PublicationVersion publicationVersion, Dataset dataset) {
        ElementLevel elementLevel = mockDatasetCubeElementLevel(publicationVersion, dataset, null);
        return elementLevel;
    }

    public ElementLevel mockDatasetCubeElementLevel(PublicationVersion publicationVersion, Dataset dataset, ElementLevel parentElementLevel) {
        ElementLevel elementLevel = mockCubeElementLevel(publicationVersion, parentElementLevel, mockDatasetCube(dataset));
        return elementLevel;
    }

    public ElementLevel mockQueryCubeElementLevel(PublicationVersion publicationVersion, Query query) {
        return mockQueryCubeElementLevel(publicationVersion, query, null);
    }

    public ElementLevel mockUrlCubeElementLevel(PublicationVersion publicationVersion, String url) {
        return mockUrlCubeElementLevel(publicationVersion, url, null);
    }

    public ElementLevel mockUrlCubeElementLevel(PublicationVersion publicationVersion, String url, ElementLevel parentElementLevel) {
        ElementLevel elementLevel = mockCubeElementLevel(publicationVersion, parentElementLevel, mockUrlCube(url));
        return elementLevel;
    }

    public ElementLevel mockQueryCubeElementLevel(PublicationVersion publicationVersion, Query query, ElementLevel parentElementLevel) {
        ElementLevel elementLevel = mockCubeElementLevel(publicationVersion, parentElementLevel, mockQueryCube(query));
        return elementLevel;
    }

    public ElementLevel mockMultidatasetCuveElementLevel(PublicationVersion publicationVersion, Multidataset multidataset) {
        return mockMultidatasetCuveElementLevel(publicationVersion, multidataset, null);
    }

    public ElementLevel mockMultidatasetCuveElementLevel(PublicationVersion publicationVersion, Multidataset multidataset, ElementLevel parentElementLevel) {
        return mockCubeElementLevel(publicationVersion, parentElementLevel, mockMultidatasetCube(multidataset));
    }

    private ElementLevel mockCubeElementLevel(PublicationVersion publicationVersion, ElementLevel parentElementLevel, Cube cube) {
        ElementLevel elementLevel = mockElementLevel(publicationVersion, parentElementLevel);

        // Cube relation
        elementLevel.setCube(cube);
        cube.setElementLevel(elementLevel);

        return elementLevel;
    }

    // Code dimension
    public static CodeDimension mockCodeDimension(DatasetVersion datasetVersion, String dsdComponentId, String identifier, String title) {
        CodeDimension codeDimension = new CodeDimension();
        codeDimension.setDatasetVersion(datasetVersion);
        codeDimension.setDsdComponentId(dsdComponentId);
        codeDimension.setIdentifier(identifier);
        codeDimension.setTitle(title);
        return codeDimension;
    }

    public static List<CodeDimension> mockCodeDimensionsWithIdentifiers(DatasetVersion datasetVersion, String dsdComponentId, String... identifiers) {
        List<CodeDimension> codes = new ArrayList<CodeDimension>();
        for (String identifier : identifiers) {
            codes.add(mockCodeDimension(datasetVersion, dsdComponentId, identifier, identifier));
        }
        return codes;
    }

    // Attribute values
    public static AttributeValue mockAttributeValue(DatasetVersion datasetVersion, String dsdComponentId, String identifier, String title) {
        AttributeValue attrValue = new AttributeValue();
        attrValue.setDatasetVersion(datasetVersion);
        attrValue.setDsdComponentId(dsdComponentId);
        attrValue.setIdentifier(identifier);
        attrValue.setTitle(title);
        return attrValue;
    }

    public static List<AttributeValue> mockAttributeValuesWithIdentifiers(DatasetVersion datasetVersion, String dsdComponentId, String... identifiers) {
        List<AttributeValue> values = new ArrayList<AttributeValue>();
        for (String identifier : identifiers) {
            values.add(mockAttributeValue(datasetVersion, dsdComponentId, identifier, identifier));
        }
        return values;
    }

    // Multidataset
    public MultidatasetCube mockQueryMultidatasetCube(MultidatasetVersion multidatasetVersion, Query query) {
        MultidatasetCube cube = mockMultidatasetCube();
        cube.setQuery(query);
        cube.setMultidatasetVersion(multidatasetVersion);
        multidatasetVersion.addCube(cube);
        return cube;
    }

    public MultidatasetCube mockDatasetMultidatasetCube(MultidatasetVersion multidatasetVersion, Dataset dataset) {
        MultidatasetCube cube = mockMultidatasetCube();
        cube.setDataset(dataset);
        cube.setMultidatasetVersion(multidatasetVersion);
        multidatasetVersion.addCube(cube);
        return cube;
    }

    // -----------------------------------------------------------------
    // BASE HIERARCHY
    // -----------------------------------------------------------------

    protected SiemacMetadataStatisticalResource mockSiemacMetadataStatisticalResource(SiemacMetadataStatisticalResource resource, TypeRelatedResourceEnum artefactType) {
        if (resource == null) {
            resource = new SiemacMetadataStatisticalResource();
        }

        mockLifeCycleStatisticalResource(resource, artefactType);

        String resourceCode = resource.getCode();

        if (resource.getLanguage() == null) {
            resource.setLanguage(mockCodeExternalItem("language01"));
        }
        if (resource.getLanguages().isEmpty()) {
            resource.addLanguage(mockCodeExternalItem("language01"));
            resource.addLanguage(mockCodeExternalItem("language02"));
        }
        if (resource.getSubtitle() == null) {
            resource.setSubtitle(mockInternationalStringMetadata(resourceCode, "subtitle"));
        }
        if (resource.getTitleAlternative() == null) {
            resource.setTitleAlternative(mockInternationalStringMetadata(resourceCode, "titleAlternative"));
        }
        if (resource.getAbstractLogic() == null) {
            resource.setAbstractLogic(mockInternationalStringMetadata(resourceCode, "abstract"));
        }
        if (resource.getUserModifiedKeywords() == null) {
            resource.setUserModifiedKeywords(false);
        }
        if (resource.getKeywords() == null) {
            resource.setKeywords(mockInternationalStringMetadata(resourceCode, "keyword1 keyword2 keyword3"));
        }

        switch (artefactType) {
            case DATASET:
                resource.setType(StatisticalResourceTypeEnum.DATASET);
                break;
            case DATASET_VERSION:
                resource.setType(StatisticalResourceTypeEnum.DATASET);
                break;
            case PUBLICATION:
                resource.setType(StatisticalResourceTypeEnum.COLLECTION);
                break;
            case PUBLICATION_VERSION:
                resource.setType(StatisticalResourceTypeEnum.COLLECTION);
                break;
            case QUERY:
                resource.setType(StatisticalResourceTypeEnum.QUERY);
                break;
            case QUERY_VERSION:
                resource.setType(StatisticalResourceTypeEnum.QUERY);
                break;
            case MULTIDATASET:
                resource.setType(StatisticalResourceTypeEnum.MULTIDATASET);
                break;
            case MULTIDATASET_VERSION:
                resource.setType(StatisticalResourceTypeEnum.MULTIDATASET);
            default:
                break;
        }
        if (resource.getCommonMetadata() == null) {
            resource.setCommonMetadata(mockCommonConfigurationExternalItem());
        }

        if (resource.getCreator() == null) {
            resource.setCreator(mockOrganizationUnitExternalItem("creator"));
        }
        if (resource.getDataProvider().isEmpty()) {
            resource.addDataProvider(mockDataProviderExternalItem("dataProvider01"));
            resource.addDataProvider(mockDataProviderExternalItem("dataProvider02"));
        }
        if (resource.getDataProviderAnnotations() == null) {
            resource.setDataProviderAnnotations(mockInternationalStringMetadata(resourceCode, "dataProviderAnnotations"));
        }
        if (resource.getContributor().isEmpty()) {
            resource.addContributor(mockOrganizationUnitExternalItem("contributor01"));
            resource.addContributor(mockOrganizationUnitExternalItem("contributor02"));
        }
        if (resource.getConformsTo() == null) {
            resource.setConformsTo(mockInternationalStringMetadata(resourceCode, "conformsTo"));
        }
        if (resource.getConformsToInternal() == null) {
            resource.setConformsToInternal(mockInternationalStringMetadata(resourceCode, "conformsToInternal"));
        }
        if (resource.getPublisher().isEmpty()) {
            resource.addPublisher(mockOrganizationUnitExternalItem("publisher01"));
            resource.addPublisher(mockOrganizationUnitExternalItem("publisher02"));
        }
        if (resource.getPublisherContributor().isEmpty()) {
            resource.addPublisherContributor(mockOrganizationUnitExternalItem("publisherContributor01"));
            resource.addPublisherContributor(mockOrganizationUnitExternalItem("publisherContributor02"));
        }
        if (resource.getMediator().isEmpty()) {
            resource.addMediator(mockOrganizationUnitExternalItem("mediator01"));
            resource.addMediator(mockOrganizationUnitExternalItem("mediator02"));
        }
        if (resource.getCopyrightedDate() == null) {
            resource.setCopyrightedDate(new DateTime().getYear());
        }
        if (resource.getAccessRights() == null) {
            resource.setAccessRights(mockInternationalStringMetadata(resourceCode, "accessRights"));
        }

        setSpecialCasesSiemacMetadataStatisticalResourceMock(resource);

        return resource;
    }

    protected LifeCycleStatisticalResource mockLifeCycleStatisticalResource(LifeCycleStatisticalResource resource, TypeRelatedResourceEnum artefactType) {
        mockVersionableStatisticalResource(resource, artefactType);

        if (resource.getMaintainer() == null) {
            resource.setMaintainer(mockAgencyExternalItem(mockString(10)));
        }

        setSpecialCasesLifeCycleStatisticalResourceMock(resource, artefactType);
        return resource;
    }

    protected VersionableStatisticalResource mockVersionableStatisticalResource(VersionableStatisticalResource resource, TypeRelatedResourceEnum artefactType) {
        mockNameableStatisticalResorce(resource, artefactType);

        setSpecialCasesVersionableStatisticalResourceMock(resource);
        return resource;
    }

    protected NameableStatisticalResource mockNameableStatisticalResorce(TypeRelatedResourceEnum artefactType) {
        NameableStatisticalResource nameableResource = new NameableStatisticalResource();
        mockNameableStatisticalResorce(nameableResource, artefactType);

        return nameableResource;
    }

    protected NameableStatisticalResource mockNameableStatisticalResorce(NameableStatisticalResource resource, TypeRelatedResourceEnum artefactType) {
        mockIdentifiableStatisticalResource(resource, artefactType);
        String resourceCode = resource.getCode();

        if (resource.getTitle() == null) {
            resource.setTitle(mockInternationalStringMetadata(resourceCode, "title"));
        }

        if (resource.getDescription() == null) {
            resource.setDescription(mockInternationalStringMetadata(resourceCode, "description"));
        }

        return resource;
    }

    protected IdentifiableStatisticalResource mockIdentifiableStatisticalResource(IdentifiableStatisticalResource resource, TypeRelatedResourceEnum artefactType) {
        mockStatisticalResource(resource);

        setSpecialCasesIdentifiableStatisticalResourceMock(resource, artefactType);
        return resource;
    }

    protected StatisticalResource mockStatisticalResource(StatisticalResource resource) {
        setSpecialCasesStatisticalResourceMock(resource);
        return resource;
    }

    // -----------------------------------------------------------------
    // STATISTICAL OFFICIALITY
    // -----------------------------------------------------------------

    public StatisticOfficiality mockStatisticOfficiality(String identifier) {
        StatisticOfficiality mock = new StatisticOfficiality();
        mock.setDescription(mockInternationalStringMetadata(identifier, "statisticOfficiality"));
        mock.setIdentifier(identifier);

        setSpecialCasesStatisticOfficialityMock(mock);

        return mock;
    }

    // -----------------------------------------------------------------
    // DIMENSION REPRESENTATION MAPPING
    // -----------------------------------------------------------------

    public DimensionRepresentationMapping mockDimensionRepresentationMapping(Dataset dataset) {
        DimensionRepresentationMapping mock = new DimensionRepresentationMapping();
        mock.setDataset(dataset);
        mock.setDatasourceFilename("filename");
        mock.setMapping("DIM1=urn");
        return mock;
    }

    public DimensionRepresentationMapping mockDimensionRepresentationMapping() {
        DimensionRepresentationMapping mock = new DimensionRepresentationMapping();

        DatasetMock dataset = new DatasetMock();
        mockDataset(dataset);
        mock.setDataset(dataset);
        mock.setDatasourceFilename("filename");
        mock.setMapping("DIM1=urn");
        return mock;
    }

    public DimensionRepresentationMapping mockDimensionRepresentationMapping(Dataset dataset, String datasourceFilename, Map<String, String> mapping) {
        DimensionRepresentationMapping mock = new DimensionRepresentationMapping();
        mock.setDataset(dataset);
        mock.setDatasourceFilename(datasourceFilename);
        mock.setMapping(DatasetVersionUtils.dimensionRepresentationMapToString(mapping));
        return mock;
    }

    // -----------------------------------------------------------------
    // ABSTRACT METHODS
    // -----------------------------------------------------------------

    protected abstract void setSpecialCasesSiemacMetadataStatisticalResourceMock(SiemacMetadataStatisticalResource resource);

    protected abstract void setSpecialCasesLifeCycleStatisticalResourceMock(LifeCycleStatisticalResource resource, TypeRelatedResourceEnum artefactType);

    protected abstract void setSpecialCasesVersionableStatisticalResourceMock(VersionableStatisticalResource resource);

    protected abstract void setSpecialCasesIdentifiableStatisticalResourceMock(IdentifiableStatisticalResource resource, TypeRelatedResourceEnum artefactType);

    protected abstract void setSpecialCasesStatisticalResourceMock(StatisticalResource resource);

    protected abstract void setSpecialCasesQueryVersionMock(QueryVersion query);

    protected abstract void setSpecialCasesStatisticOfficialityMock(StatisticOfficiality officiality);

    // -----------------------------------------------------------------
    // INTERNATIONAL STRING
    // -----------------------------------------------------------------

    public static InternationalString mockInternationalString() {
        return mockInternationalStringMetadata(null, mockString(10));
    }

    public static InternationalString mockInternationalStringMetadata(String resource, String metadata) {
        assertNotNull(metadata);

        InternationalString internationalString = new InternationalString();
        internationalString.setVersion(Long.valueOf(0));
        {
            LocalisedString es = new LocalisedString();
            if (resource != null) {
                es.setLabel(metadata + "-" + resource + " en Espanol");
            } else {
                es.setLabel(metadata + " en Espanol");
            }
            es.setLocale("es");
            es.setVersion(Long.valueOf(0));
            internationalString.addText(es);
        }
        {
            LocalisedString en = new LocalisedString();
            if (resource != null) {
                en.setLabel(metadata + "-" + resource + " in English");
            } else {
                en.setLabel(metadata + " in English");
            }
            en.setLocale("en");
            en.setVersion(Long.valueOf(0));
            internationalString.addText(en);
        }
        return internationalString;
    }

    /**
     * Mock an InternationalString with one locale
     */
    public static InternationalString mockInternationalString(String locale, String label) {
        InternationalString target = new InternationalString();
        LocalisedString localisedString = new LocalisedString();
        localisedString.setLocale(locale);
        localisedString.setLabel(label);
        target.addText(localisedString);
        return target;
    }

    /**
     * Mock an InternationalString with two locales
     */
    public static InternationalString mockInternationalString(String locale01, String label01, String locale02, String label02) {
        InternationalString target = new InternationalString();
        LocalisedString localisedString01 = new LocalisedString();
        localisedString01.setLocale(locale01);
        localisedString01.setLabel(label01);
        target.addText(localisedString01);

        LocalisedString localisedString02 = new LocalisedString();
        localisedString02.setLocale(locale02);
        localisedString02.setLabel(label02);
        target.addText(localisedString02);
        return target;
    }

    /**
     * Mock an InternationalString with two locales
     */
    public static Map<String, String> mockInternationalStringAsMap(String locale01, String label01, String locale02, String label02) {
        Map<String, String> target = new HashMap<String, String>();
        if (locale01 != null) {
            target.put(locale01, label01);
        }
        if (locale02 != null) {
            target.put(locale02, label02);
        }
        return target;
    }

    // -----------------------------------------------------------------
    // EXTERNAL ITEM
    // -----------------------------------------------------------------

    public static ExternalItem mockCommonConfigurationExternalItem() {
        String code = mockCode();
        return mockExternalItem(code, mockCommonConfigurationUrn(code), TypeExternalArtefactsEnum.CONFIGURATION);
    }

    public static ExternalItem mockStatisticalOperationExternalItem() {
        String code = mockCode();
        return mockStatisticalOperationExternalItem(code);
    }

    public static ExternalItem mockStatisticalOperationExternalItem(String code) {
        return mockExternalItem(code, mockStatisticalOperationUrn(code), TypeExternalArtefactsEnum.STATISTICAL_OPERATION);
    }

    public static String mockStatisticalOperationUrn(String code) {
        return GeneratorUrnUtils.generateSiemacStatisticalOperationUrn(code);
    }

    public static String mockPortalDatasetHtmlLink(String agencyId, String resourceId, String version) {
        return "http://localhost:8080/statistical-visualizer/visualizer/data.html?resourceType=dataset&agencyId=" + agencyId + "&resourceId=" + resourceId + "&version=" + version + "#";
    }

    public static String mockPortalQueryHtmlLink(String agencyId, String resourceId) {
        return "http://localhost:8080/statistical-visualizer/visualizer/data.html?resourceType=query&agencyId=" + agencyId + "&resourceId=" + resourceId + "#";
    }

    public static String mockPortalCollectionHtmlLink(String agencyId, String resourceId) {
        return "http://localhost:8080/statistical-visualizer/visualizer/data.html?resourceType=publication&agencyId=" + agencyId + "&resourceId=" + resourceId + "#";
    }

    public static ExternalItem mockVariableElementExternalItem(String variableId, String variableElementId) {
        return mockExternalItem(variableElementId, GeneratorUrnUtils.generateSiemacStructuralResourcesVariableElementUrn(variableId, variableElementId), TypeExternalArtefactsEnum.VARIABLE_ELEMENT);
    }

    public static ExternalItem mockStatisticalOperationInstanceExternalItem() {
        String code = mockCode();
        return mockStatisticalOperationInstanceExternalItem(code);
    }

    public static ExternalItem mockStatisticalOperationInstanceExternalItem(String code) {
        return mockExternalItem(code, mockStatisticalOperationInstanceUrn(code, code), TypeExternalArtefactsEnum.STATISTICAL_OPERATION_INSTANCE);
    }

    public static ExternalItem mockAgencyExternalItem() {
        String code = mockCode();
        return mockAgencyExternalItem(code);
    }

    public static ExternalItem mockAgencyExternalItem(String code) {
        return mockExternalItem(code, mockAgencyUrn(code), TypeExternalArtefactsEnum.AGENCY);
    }

    public static ExternalItem mockAgencyExternalItem(String code, String codeNested) {
        return mockExternalItem(code, codeNested, mockAgencyUrn(code), TypeExternalArtefactsEnum.AGENCY);
    }

    public static ExternalItem mockOrganizationUnitExternalItem() {
        String code = mockCode();
        return mockOrganizationUnitExternalItem(code);
    }

    public static ExternalItem mockOrganizationUnitExternalItem(String code) {
        return mockExternalItem(code, mockOrganizationUnitUrn(code), TypeExternalArtefactsEnum.ORGANISATION_UNIT);
    }

    public static ExternalItem mockDataProviderExternalItem() {
        String code = mockCode();
        return mockDataProviderExternalItem(code);
    }

    public static ExternalItem mockDataProviderExternalItem(String code) {
        return mockExternalItem(code, mockDataProviderUrn(code), TypeExternalArtefactsEnum.DATA_PROVIDER);
    }

    public static ExternalItem mockConceptExternalItem() {
        String code = mockCode();
        return mockConceptExternalItem(code);
    }

    public static ExternalItem mockConceptExternalItem(String code) {
        return mockExternalItem(code, mockConceptUrn(code), TypeExternalArtefactsEnum.CONCEPT);
    }

    public static ExternalItem mockConceptSchemeExternalItem() {
        String code = mockCode();
        return mockExternalItem(code, mockConceptSchemeUrn(code), TypeExternalArtefactsEnum.CONCEPT_SCHEME);
    }

    public static ExternalItem mockCodeListSchemeExternalItem() {
        String code = mockCode();
        return mockExternalItem(code, mockCodeListUrn(code), TypeExternalArtefactsEnum.CODELIST);
    }

    public static ExternalItem mockCodeExternalItem() {
        String code = mockCode();
        return mockCodeExternalItem(code);
    }

    public static ExternalItem mockCodeExternalItem(String code) {
        return mockExternalItem(code, mockCodeUrn(code), TypeExternalArtefactsEnum.CODE);
    }

    public static ExternalItem mockCodeExternalItem(String code, String title) {
        return mockExternalItem(code, mockCodeUrn(code), TypeExternalArtefactsEnum.CODE);
    }

    public static ExternalItem mockCategoryExternalItem(String code) {
        return mockExternalItem(code, mockCategoryUrn(code), TypeExternalArtefactsEnum.CATEGORY);
    }

    public static ExternalItem mockDsdExternalItem() {
        String code = mockCode();
        return mockDsdExternalItem(code);
    }

    public static ExternalItem mockDsdExternalItem(String code) {
        return mockExternalItem(code, mockDsdUrn(code), TypeExternalArtefactsEnum.DATASTRUCTURE);
    }

    public static ExternalItem mockDimensionExternalItem() {
        String code = mockCode();
        return mockExternalItem(code, mockDimensionUrn(code), TypeExternalArtefactsEnum.DIMENSION);
    }

    public static ExternalItem mockConfigurationExternalItem() {
        String code = mockCode();
        return mockExternalItem(code, mockCommonConfigurationUrn(code), TypeExternalArtefactsEnum.CONFIGURATION);
    }

    public static ExternalItem mockExternalItem(String code, String codeNested, String uri, String urnProvider, String urn, TypeExternalArtefactsEnum type) {
        ExternalItem target = new ExternalItem();
        target.setVersion(Long.valueOf(0));
        target.setCode(code);
        target.setCodeNested(codeNested);
        target.setUri(uri);
        target.setUrn(urn);
        target.setUrnProvider(urnProvider);
        target.setType(type);
        return target;
    }

    public static ExternalItem mockExternalItem(String code, String codeNested, String uri, String urnProvider, String urn, TypeExternalArtefactsEnum type, InternationalString title,
            String managementAppUrl) {
        ExternalItem target = mockExternalItem(code, codeNested, uri, urnProvider, urn, type);
        target.setTitle(title);
        target.setManagementAppUrl(managementAppUrl);
        return target;
    }

    private static ExternalItem mockExternalItem(String code, String urn, TypeExternalArtefactsEnum type) {
        return mockExternalItem(code, null, urn, type);
    }

    private static ExternalItem mockExternalItem(String code, String codeNested, String urn, TypeExternalArtefactsEnum type) {
        String uri = CoreCommonConstants.API_LATEST_WITH_SLASHES + code;
        String urnProvider = urn + ":provider";
        InternationalString title = mockInternationalStringMetadata(code, "title");
        String managementAppUrl = CoreCommonConstants.URL_SEPARATOR + code;

        if (TypeExternalArtefactsEnumUtils.isExternalItemOfCommonMetadataApp(type) || TypeExternalArtefactsEnum.VARIABLE_ELEMENT.equals(type)) {
            urnProvider = null;
        } else if (TypeExternalArtefactsEnumUtils.isExternalItemOfStatisticalOperationsApp(type)) {
            urnProvider = null;
        } else if (TypeExternalArtefactsEnumUtils.isExternalItemOfSrmApp(type)) {
            // nothing to do with urnInternal because it's ok for SrmExternalItems
            if (StringUtils.isBlank(codeNested)) {
                if (TypeExternalArtefactsEnum.AGENCY.equals(type) || TypeExternalArtefactsEnum.CATEGORY.equals(type)) {
                    codeNested = code;
                }
            }
        } else {
            fail("Unexpected type of ExternalItem:" + type);
        }

        ExternalItem item = mockExternalItem(code, codeNested, uri, urnProvider, urn, type, title, managementAppUrl);
        return item;
    }

    public static ExternalItem buildCodeExternalItemFromCodeResourceInternal(CodeResourceInternal codeResourceInternal) {
        return mockExternalItem(codeResourceInternal.getId(), null, codeResourceInternal.getUrn(), TypeExternalArtefactsEnum.CODE);
    }

    // -----------------------------------------------------------------
    // Related Resource
    // -----------------------------------------------------------------

    public static RelatedResource mockDatasetVersionRelated(DatasetVersion datasetVersion) {
        RelatedResource resource = new RelatedResource(TypeRelatedResourceEnum.DATASET_VERSION);
        resource.setDatasetVersion(datasetVersion);
        resource.setVersion(Long.valueOf(0));
        return resource;
    }

    public static RelatedResource mockDatasetRelated(Dataset dataset) {
        RelatedResource resource = new RelatedResource(TypeRelatedResourceEnum.DATASET);
        resource.setDataset(dataset);
        resource.setVersion(Long.valueOf(0));
        return resource;
    }

    public static RelatedResource mockPublicationVersionRelated(PublicationVersion pubVersion) {
        RelatedResource resource = new RelatedResource(TypeRelatedResourceEnum.PUBLICATION_VERSION);
        resource.setPublicationVersion(pubVersion);
        resource.setVersion(Long.valueOf(0));
        return resource;
    }

    public static RelatedResource mockQueryVersionRelated(QueryVersion queryVersion) {
        RelatedResource resource = new RelatedResource(TypeRelatedResourceEnum.QUERY_VERSION);
        resource.setQueryVersion(queryVersion);
        resource.setVersion(Long.valueOf(0));
        return resource;
    }

    public static RelatedResource mockQueryRelated(Query query) {
        RelatedResource resource = new RelatedResource(TypeRelatedResourceEnum.QUERY);
        resource.setQuery(query);
        resource.setVersion(Long.valueOf(0));
        return resource;
    }

    public static RelatedResource mockMultidatasetVersionRelated(MultidatasetVersion multidataset) {
        RelatedResource resource = new RelatedResource(TypeRelatedResourceEnum.MULTIDATASET_VERSION);
        resource.setMultidatasetVersion(multidataset);
        resource.setVersion(Long.valueOf(0));
        return resource;
    }

    public static RelatedResource mockMultidatasetRelated(Multidataset multidatasetVersion) {
        RelatedResource resource = new RelatedResource(TypeRelatedResourceEnum.MULTIDATASET);
        resource.setMultidataset(multidatasetVersion);
        resource.setVersion(Long.valueOf(0));
        return resource;
    }

    public static RelatedResource mockPublicationRelated(Publication publicationVersion) {
        RelatedResource resource = new RelatedResource(TypeRelatedResourceEnum.PUBLICATION);
        resource.setPublication(publicationVersion);
        resource.setVersion(Long.valueOf(0));
        return resource;
    }

    // -----------------------------------------------------------------
    // Temporal code
    // -----------------------------------------------------------------
    public static TemporalCode mockTemporalCode() {
        return mockTemporalCode(mockString(10), mockString(20));
    }

    public static TemporalCode mockTemporalCode(String identifier, String title) {
        TemporalCode code = new TemporalCode();
        code.setIdentifier(identifier);
        code.setTitle(title);
        return code;
    }

    // -----------------------------------------------------------------
    // MULTIDATASET VERSION
    // -----------------------------------------------------------------

    public abstract MultidatasetVersion mockMultidatasetVersion();

    // MULTIDATASETCUBE

    private MultidatasetCube mockMultidatasetCube() {
        MultidatasetCube cube = new MultidatasetCube();

        // Identifier
        cube.setIdentifier("id-" + MetamacMocks.mockString(10));

        // Metadata
        cube.setOrderInMultidataset(Long.valueOf(1));
        cube.setNameableStatisticalResource(mockNameableStatisticalResorce(TypeRelatedResourceEnum.MULTIDATASET_CUBE));
        cube.setMultidatasetVersion(mockMultidatasetVersion());
        return cube;
    }

    public MultidatasetCube mockMultidatasetCube(Dataset dataset) {
        MultidatasetCube cube = mockMultidatasetCube();
        cube.setDataset(dataset);
        return cube;
    }

    public MultidatasetCube mockMultidatasetCube(Query query) {
        MultidatasetCube cube = mockMultidatasetCube();
        cube.setQuery(query);
        return cube;
    }

    // -----------------------------------------------------------------
    // UTILS
    // -----------------------------------------------------------------
    public static DatasetVersion getDatasetVersionInQueryVersion(QueryVersion queryVersion) {
        if (queryVersion.getFixedDatasetVersion() != null) {
            return queryVersion.getFixedDatasetVersion();
        } else if (queryVersion.getDataset() != null) {
            return queryVersion.getDataset().getVersions().get(queryVersion.getDataset().getVersions().size() - 1);
        }
        return null;
    }
}
