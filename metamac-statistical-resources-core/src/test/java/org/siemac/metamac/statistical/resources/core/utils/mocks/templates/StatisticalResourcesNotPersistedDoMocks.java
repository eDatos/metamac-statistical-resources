package org.siemac.metamac.statistical.resources.core.utils.mocks.templates;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.StatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DataSourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.mocks.DatasetMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.DatasetVersionMock;

public class StatisticalResourcesNotPersistedDoMocks extends StatisticalResourcesDoMocks {

    private static StatisticalResourcesPersistedDoMocks persistedMocks = StatisticalResourcesPersistedDoMocks.getInstance();

    private StatisticalResourcesNotPersistedDoMocks() {
    }

    public static StatisticalResourcesNotPersistedDoMocks getInstance() {
        return new StatisticalResourcesNotPersistedDoMocks();
    }

    // -----------------------------------------------------------------
    // QUERY
    // -----------------------------------------------------------------

    // -----------------------------------------------------------------
    // QUERY VERSION
    // -----------------------------------------------------------------

    @Override
    public QueryVersion mockQueryVersion(DatasetVersion datasetVersion, boolean isDatasetLastVersion) {
        QueryVersion queryVersion = new QueryVersion();
        queryVersion.setFixedDatasetVersion(datasetVersion);
        return mockQueryVersion(queryVersion);
    }

    @Override
    public QueryVersion mockQueryVersion(Dataset dataset) {
        QueryVersion queryVersion = new QueryVersion();
        queryVersion.setDataset(dataset);
        return mockQueryVersion(queryVersion);
    }

    private QueryVersion mockQueryVersion(QueryVersion queryVersion) {
        queryVersion.setLifeCycleStatisticalResource(mockLifeCycleStatisticalResource(new LifeCycleStatisticalResource(), TypeRelatedResourceEnum.QUERY_VERSION));

        // Mock code
        queryVersion.getLifeCycleStatisticalResource().setCode("resource-" + mockString(10));

        if (queryVersion.getSelection().isEmpty() && getDatasetVersionInQueryVersion(queryVersion) != null) {
            mockQuerySelectionFromDatasetVersion(queryVersion, getDatasetVersionInQueryVersion(queryVersion));
        }

        queryVersion.setQueryType(QueryTypeEnum.FIXED);

        return queryVersion;
    }

    @Override
    public QueryVersion mockQueryVersionWithGeneratedDatasetVersion() {
        QueryVersion queryVersion = mockQueryVersion(persistedMocks.mockDatasetVersion(), true);
        return queryVersion;
    }

    public QueryVersion mockQueryVersionWithStatisticalResourceNull(DatasetVersion datasetVersion) {
        QueryVersion queryVersion = mockQueryVersionWithDatasetVersion(datasetVersion, true);
        queryVersion.setLifeCycleStatisticalResource(null);
        return queryVersion;
    }

    public QueryVersion mockQueryVersionWithSelectionNull(DatasetVersion datasetVersion) {
        QueryVersion queryVersion = mockQueryVersionWithDatasetVersion(datasetVersion, true);
        queryVersion.getSelection().clear();
        return queryVersion;
    }

    public QueryVersion mockQueryVersionWithSelectionIncorrectDimensionNull(DatasetVersion datasetVersion) {
        QueryVersion queryVersion = mockQueryVersionWithDatasetVersion(datasetVersion, true);
        QuerySelectionItem querySelectionItem = new QuerySelectionItem();
        querySelectionItem.setDimension(null);
        CodeItem code = new CodeItem();
        code.setCode("CODE01");
        querySelectionItem.addCode(code);
        queryVersion.addSelection(querySelectionItem);
        return queryVersion;
    }

    public QueryVersion mockQueryVersionLatestDataWithTimeSelectionNotEmpty(DatasetVersion datasetVersion) {
        QueryVersion queryVersion = mockQueryVersionWithDatasetVersion(datasetVersion, true);
        queryVersion.setQueryType(QueryTypeEnum.LATEST_DATA);
        queryVersion.setLatestDataNumber(5);
        {
            QuerySelectionItem querySelectionItem = new QuerySelectionItem();
            querySelectionItem.setDimension("DIM_1");
            CodeItem code = new CodeItem();
            code.setCode("CODE01");
            querySelectionItem.addCode(code);
            queryVersion.addSelection(querySelectionItem);
        }
        {
            QuerySelectionItem querySelectionItem = new QuerySelectionItem();
            querySelectionItem.setDimension("TIME_PERIOD");
            CodeItem code = new CodeItem();
            code.setCode("TIME_CODE_01");
            querySelectionItem.addCode(code);
            queryVersion.addSelection(querySelectionItem);
        }
        return queryVersion;
    }

    public QueryVersion mockQueryVersionWithSelectionIncorrectCodesNull(DatasetVersion datasetVersion) {
        QueryVersion query = mockQueryVersionWithDatasetVersion(datasetVersion, true);
        QuerySelectionItem querySelectionItem = new QuerySelectionItem();
        querySelectionItem.setDimension("DIMENSION01");
        querySelectionItem.getCodes().clear();
        query.addSelection(querySelectionItem);
        return query;
    }

    // -----------------------------------------------------------------
    // DATASOURCE
    // -----------------------------------------------------------------
    public Datasource mockDatasourceWithIdentifiableAndDatasetVersionNull(DatasetVersion datasetVersion) {
        Datasource datasource = mockDatasource(new Datasource());
        datasource.setIdentifiableStatisticalResource(null);
        datasource.setDatasetVersion(null);

        return datasource;
    }

    public Datasource mockDatasourceForPersist() {
        return mockDatasource(new Datasource());
    }

    // -----------------------------------------------------------------
    // DATASET
    // -----------------------------------------------------------------
    @Override
    public void mockDataset(DatasetMock dataset) {
    }

    // -----------------------------------------------------------------
    // DATASET VERSION
    // -----------------------------------------------------------------
    @Override
    public DatasetVersion mockDatasetVersion() {
        DatasetVersionMock datasetVersion = new DatasetVersionMock();
        return mockDatasetVersion(datasetVersion);
    }

    @Override
    public DatasetVersion mockDatasetVersion(DatasetVersionMock datasetVersion) {
        datasetVersion.setDataset(null);
        datasetVersion.setRelatedDsd(mockDsdExternalItem());
        datasetVersion.setSiemacMetadataStatisticalResource(mockSiemacMetadataStatisticalResource(datasetVersion.getSiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.DATASET_VERSION));
        datasetVersion.setKeepAllData(Boolean.TRUE);
        datasetVersion.setDataSourceType(DataSourceTypeEnum.FILE);
        datasetVersion.getSiemacMetadataStatisticalResource().setPatch(0);
        return datasetVersion;
    }

    public DatasetVersion mockDatasetVersionWithNullableSiemacStatisticalResource() {
        DatasetVersion datasetVersion = mockDatasetVersion();
        datasetVersion.setSiemacMetadataStatisticalResource(null);
        return datasetVersion;
    }

    // -----------------------------------------------------------------
    // CATEGORISATION
    // -----------------------------------------------------------------
    public Categorisation mockCategorisation(String maintainerCode, String categoryCode) {
        Categorisation categorisation = new Categorisation();
        categorisation.setCategory(StatisticalResourcesDoMocks.mockCategoryExternalItem(categoryCode));
        categorisation.setVersionableStatisticalResource(mockVersionableStatisticalResource(new VersionableStatisticalResource(), TypeRelatedResourceEnum.CATEGORISATION));
        categorisation.getVersionableStatisticalResource().setTitle(null); // autogenerated by service
        categorisation.setMaintainer(StatisticalResourcesDoMocks.mockAgencyExternalItem(maintainerCode, "ISTAC." + maintainerCode));
        return categorisation;
    }

    // -----------------------------------------------------------------
    // PUBLICATION VERSION
    // -----------------------------------------------------------------
    @Override
    public PublicationVersion mockPublicationVersion() {
        PublicationVersion publicationVersion = new PublicationVersion();
        publicationVersion.setPublication(null);
        publicationVersion.setSiemacMetadataStatisticalResource(mockSiemacMetadataStatisticalResource(new SiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.PUBLICATION_VERSION));
        return publicationVersion;
    }

    public PublicationVersion mockPublicationVersionWithNullableSiemacStatisticalResource() {
        PublicationVersion publicationVersion = mockPublicationVersion();
        publicationVersion.setSiemacMetadataStatisticalResource(null);
        return publicationVersion;
    }

    public RelatedResource mockRelatedResourceLinkedToMockedDatasetVersion(String datasetVersionUrn) throws MetamacException {
        DatasetVersion previousDatasetVersion = persistedMocks.mockDatasetVersion();
        previousDatasetVersion.getSiemacMetadataStatisticalResource().setUrn(datasetVersionUrn);

        RelatedResource target = new RelatedResource();
        target.setDatasetVersion(previousDatasetVersion);
        target.setType(TypeRelatedResourceEnum.DATASET_VERSION);
        return target;
    }

    public static RelatedResource mockRelatedResourceLinkedToDatasetVersion(DatasetVersion linkedDatasetVersion) {
        RelatedResource target = new RelatedResource();
        target.setDatasetVersion(linkedDatasetVersion);
        target.setType(TypeRelatedResourceEnum.DATASET_VERSION);
        return target;
    }

    public static RelatedResource mockRelatedResourceLinkedToPublicationVersion(PublicationVersion linkedPublicationVersion) {
        RelatedResource target = new RelatedResource();
        target.setPublicationVersion(linkedPublicationVersion);
        target.setType(TypeRelatedResourceEnum.PUBLICATION_VERSION);
        return target;
    }

    public static RelatedResource mockRelatedResourceLinkedToQueryVersion(QueryVersion linkedQueryVersion) {
        RelatedResource target = new RelatedResource();
        target.setQueryVersion(linkedQueryVersion);
        target.setType(TypeRelatedResourceEnum.QUERY_VERSION);
        return target;
    }

    // -----------------------------------------------------------------
    // MULTIDATASET VERSION
    // -----------------------------------------------------------------
    @Override
    public MultidatasetVersion mockMultidatasetVersion() {
        MultidatasetVersion multidatasetVersion = new MultidatasetVersion();
        multidatasetVersion.setMultidataset(null);
        multidatasetVersion.setSiemacMetadataStatisticalResource(mockSiemacMetadataStatisticalResource(new SiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.MULTIDATASET_VERSION));
        return multidatasetVersion;
    }

    public MultidatasetVersion mockMultidatasetVersionWithNullableSiemacStatisticalResource() {
        MultidatasetVersion multidatasetVersion = mockMultidatasetVersion();
        multidatasetVersion.setSiemacMetadataStatisticalResource(null);
        return multidatasetVersion;
    }

    // -----------------------------------------------------------------
    // BASE HIERARCHY
    // -----------------------------------------------------------------

    @Override
    protected void setSpecialCasesSiemacMetadataStatisticalResourceMock(SiemacMetadataStatisticalResource resource) {
        // NOTHING
    }

    @Override
    protected void setSpecialCasesLifeCycleStatisticalResourceMock(LifeCycleStatisticalResource resource, TypeRelatedResourceEnum artefactType) {
        // NOTHING
    }

    @Override
    protected void setSpecialCasesVersionableStatisticalResourceMock(VersionableStatisticalResource resource) {
        if (resource.getPatch() == null) {
            resource.setPatch(0);
        }
    }

    @Override
    protected void setSpecialCasesIdentifiableStatisticalResourceMock(IdentifiableStatisticalResource resource, TypeRelatedResourceEnum artefactType) {
        // NOTHING
    }

    @Override
    protected void setSpecialCasesStatisticalResourceMock(StatisticalResource resource) {
        // NOTHING
    }

    @Override
    protected void setSpecialCasesQueryVersionMock(QueryVersion queryVersion) {
        // NOTHING
    }

    @Override
    protected void setSpecialCasesStatisticOfficialityMock(StatisticOfficiality officiality) {
        // NOTHING
    }

}
