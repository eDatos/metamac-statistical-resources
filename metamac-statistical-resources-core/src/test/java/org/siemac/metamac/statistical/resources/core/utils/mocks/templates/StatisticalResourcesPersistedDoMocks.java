package org.siemac.metamac.statistical.resources.core.utils.mocks.templates;

import static org.siemac.edatos.core.common.util.shared.UrnUtils.splitUrnItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.HasLifecycle;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.StatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.dataset.utils.DatasetVersionUtils;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DataSourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.Chapter;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.Publication;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesCollectionUtils;
import org.siemac.metamac.statistical.resources.core.utils.mocks.DatasetMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.DatasetVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.MultidatasetMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.MultidatasetVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.QueryMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.QueryVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticalResourcesMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.transformers.CodeDimensionToCodeStringTransformer;

public class StatisticalResourcesPersistedDoMocks extends StatisticalResourcesDoMocks {

    private static StatisticalResourcesPersistedDoMocks instance;

    private StatisticalResourcesPersistedDoMocks() {
    }

    public static StatisticalResourcesPersistedDoMocks getInstance() {
        if (instance == null) {
            instance = new StatisticalResourcesPersistedDoMocks();
        }
        return instance;
    }

    // -----------------------------------------------------------------
    // QUERY
    // -----------------------------------------------------------------
    public Query mockQuery(QueryMock query) {
        if (query.getIdentifiableStatisticalResource() == null) {
            query.setIdentifiableStatisticalResource(new IdentifiableStatisticalResource());
        }

        fillNeededMetadataToGenerateQueryUrn(query);

        mockIdentifiableStatisticalResource(query.getIdentifiableStatisticalResource(), TypeRelatedResourceEnum.QUERY);

        String[] maintainerAgencyId = new String[]{query.getMaintainerCode()};

        query.getIdentifiableStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceQueryUrn(maintainerAgencyId, query.getIdentifiableStatisticalResource().getCode()));

        return query;
    }

    private void fillNeededMetadataToGenerateQueryUrn(QueryMock query) {
        if (query.getMaintainerCode() == null) {
            query.setMaintainerCode(mockString(10));
        }
    }

    public Query mockQueryWithGeneratedQueryVersion(QueryMock queryMock) {
        String maintainerId = mockString(10);

        Query query = mockQuery(queryMock);

        QueryVersionMock queryVersionTemplate = new QueryVersionMock();
        queryVersionTemplate.setQuery(query);
        queryVersionTemplate.getLifeCycleStatisticalResource().setCode(query.getIdentifiableStatisticalResource().getCode());
        queryVersionTemplate.getLifeCycleStatisticalResource().setMaintainer(mockAgencyExternalItem(maintainerId, maintainerId));
        DatasetVersion datasetVersion = mockDatasetVersion();
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(datasetVersion);
        queryVersionTemplate.setDataset(datasetVersion.getDataset());
        queryVersionTemplate.setStatus(QueryStatusEnum.ACTIVE);

        mockQueryVersion(queryVersionTemplate);

        return query;
    }

    public Query mockQueryWithGeneratedQueryVersion() {
        return mockQueryWithGeneratedQueryVersion(new QueryMock());
    }

    // -----------------------------------------------------------------
    // QUERY VERSION
    // -----------------------------------------------------------------
    @Override
    public QueryVersion mockQueryVersion(DatasetVersion datasetVersion, boolean isDatasetLastVersion) {
        return mockQueryVersion(null, datasetVersion, isDatasetLastVersion);
    }

    @Override
    public QueryVersion mockQueryVersion(Dataset dataset) {
        return mockQueryVersion(null, dataset);
    }

    public QueryVersion mockQueryVersion(Query query, DatasetVersion datasetVersion, boolean isDatasetLastVersion) {
        QueryVersion template = new QueryVersion();
        template.setQuery(query);
        template.setFixedDatasetVersion(datasetVersion);
        template.setStatus(isDatasetLastVersion ? QueryStatusEnum.ACTIVE : QueryStatusEnum.DISCONTINUED);
        return mockQueryVersion(template);
    }

    public QueryVersion mockQueryVersion(Query query, Dataset dataset) {
        QueryVersion template = new QueryVersion();
        template.setQuery(query);
        template.setDataset(dataset);
        template.setStatus(QueryStatusEnum.ACTIVE);
        return mockQueryVersion(template);
    }

    public QueryVersion mockQueryVersion(QueryVersion queryVersion) {
        LifeCycleStatisticalResource lifecycleResource = queryVersion.getLifeCycleStatisticalResource();
        if (lifecycleResource == null) {
            lifecycleResource = new LifeCycleStatisticalResource();
        }

        if (queryVersion.getQuery() != null && queryVersion.getQuery().getIdentifiableStatisticalResource() != null) {
            lifecycleResource.setCode(queryVersion.getQuery().getIdentifiableStatisticalResource().getCode());
        }

        queryVersion.setLifeCycleStatisticalResource(mockLifeCycleStatisticalResource(lifecycleResource, TypeRelatedResourceEnum.QUERY_VERSION));

        if (queryVersion.getQuery() == null) {
            QueryMock queryTemplate = new QueryMock();
            queryTemplate.getIdentifiableStatisticalResource().setCode(queryVersion.getLifeCycleStatisticalResource().getCode());
            queryTemplate.setMaintainerCode(queryVersion.getLifeCycleStatisticalResource().getMaintainer().getCodeNested());
            queryVersion.setQuery(mockQuery(queryTemplate));
        }

        if (!queryVersion.getQuery().getVersions().contains(queryVersion)) {
            queryVersion.getQuery().addVersion(queryVersion);
        }

        if (queryVersion.getFixedDatasetVersion() == null && queryVersion.getDataset() == null) {
            throw new IllegalArgumentException("Can not create a Query with no datasetversion or dataset linked");
        }

        if (queryVersion.getStatus() == null) {
            queryVersion.setStatus(QueryStatusEnum.ACTIVE);
        }

        if (queryVersion.getSelection().isEmpty()) {
            mockQuerySelectionFromDatasetVersion(queryVersion, getDatasetVersionInQueryVersion(queryVersion));
        }

        if (queryVersion.getType() == null) {
            queryVersion.setType(QueryTypeEnum.FIXED);
        }

        return queryVersion;
    }

    // -----------------------------------------------------------------
    // DATASOURCE
    // -----------------------------------------------------------------
    @Override
    public Datasource mockDatasourceWithGeneratedDatasetVersion() {
        return mockDatasourceWithDatasetVersion(mockDatasetVersion());
    }

    public Datasource mockDatasourceWithDatasetVersion(DatasetVersion datasetVersion) {
        Datasource datasource = new Datasource();
        datasource.setDatasetVersion(datasetVersion);
        return mockDatasource(datasource);
    }

    // -----------------------------------------------------------------
    // DATASET
    // -----------------------------------------------------------------

    @Override
    public void mockDataset(DatasetMock dataset) {
        if (dataset.getIdentifiableStatisticalResource() == null) {
            dataset.setIdentifiableStatisticalResource(new IdentifiableStatisticalResource());
        }

        fillNeededMetadataToGenerateDatasetCode(dataset);

        String code = buildSequentialResourceCode(dataset.getIdentifiableStatisticalResource().getStatisticalOperation().getCode(), dataset.getSequentialId());
        dataset.getIdentifiableStatisticalResource().setCode(code);
        dataset.setViewCode(DatasetVersionUtils.generateViewCode(code));

        mockIdentifiableStatisticalResource(dataset.getIdentifiableStatisticalResource(), TypeRelatedResourceEnum.DATASET);

        String maintainerId = dataset.getMaintainerCode();

        String[] maintainerAgencyId = new String[]{maintainerId};

        dataset.getIdentifiableStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceDatasetUrn(maintainerAgencyId, dataset.getIdentifiableStatisticalResource().getCode()));

    }

    private void fillNeededMetadataToGenerateDatasetCode(DatasetMock dataset) {
        if (dataset.getSequentialId() == null) {
            dataset.setSequentialId(1);
        }

        if (dataset.getIdentifiableStatisticalResource().getStatisticalOperation() == null) {
            dataset.getIdentifiableStatisticalResource().setStatisticalOperation(mockStatisticalOperationExternalItem(mockString(10)));
        }
    }

    public Dataset mockDatasetWithGeneratedDatasetVersion(DatasetMock dataset) {
        if (dataset == null) {
            dataset = new DatasetMock();
        }
        if (dataset.getMaintainerCode() == null) {
            dataset.setMaintainerCode(mockString(10));
        }
        mockDataset(dataset);

        DatasetVersionMock template = new DatasetVersionMock();
        template.setDataset(dataset);
        template.setMaintainerCode(dataset.getMaintainerCode());
        DatasetVersion datasetVersion = mockDatasetVersion(template);

        return datasetVersion.getDataset();
    }

    public Dataset mockDatasetWithGeneratedDatasetVersion() {
        return mockDatasetWithGeneratedDatasetVersion(null);
    }

    // -----------------------------------------------------------------
    // DATASET VERSION
    // -----------------------------------------------------------------
    @Override
    public DatasetVersion mockDatasetVersion() {
        return mockDatasetVersion(null);
    }

    @Override
    public DatasetVersion mockDatasetVersion(DatasetVersionMock datasetVersion) {
        if (datasetVersion == null) {
            datasetVersion = new DatasetVersionMock();
        }

        Dataset dataset = datasetVersion.getDataset();

        // Statistical Operation
        if (dataset != null && dataset.getIdentifiableStatisticalResource() != null && dataset.getIdentifiableStatisticalResource().getStatisticalOperation() != null) {
            datasetVersion.getSiemacMetadataStatisticalResource()
                    .setStatisticalOperation(mockStatisticalOperationExternalItem(dataset.getIdentifiableStatisticalResource().getStatisticalOperation().getCode()));
        }

        // CODE
        if (dataset != null && dataset.getIdentifiableStatisticalResource() != null && dataset.getIdentifiableStatisticalResource().getCode() != null) {
            datasetVersion.getSiemacMetadataStatisticalResource().setCode(datasetVersion.getDataset().getIdentifiableStatisticalResource().getCode());
        } else {
            fillNeededMetadataToGenerateDatasetVersionCode(datasetVersion);
            String datasetCode = buildSequentialResourceCode(datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(), datasetVersion.getSequentialId());
            datasetVersion.getSiemacMetadataStatisticalResource().setCode(datasetCode);
        }

        datasetVersion.setSiemacMetadataStatisticalResource(mockSiemacMetadataStatisticalResource(datasetVersion.getSiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.DATASET_VERSION));

        if (datasetVersion.getBibliographicCitation() == null) {
            datasetVersion.setBibliographicCitation(mockInternationalStringMetadata(datasetVersion.getSiemacMetadataStatisticalResource().getCode(), "bibliographicCitation"));
        }

        if (datasetVersion.getRelatedDsd() == null) {
            datasetVersion.setRelatedDsd(mockDsdExternalItem());
        }

        // DATASET CODE
        String datasetCode = datasetVersion.getSiemacMetadataStatisticalResource().getCode();
        if (datasetVersion.getDataset() == null) {
            DatasetMock template = new DatasetMock();
            template.setCode(datasetCode);
            template.setMaintainerCode(getMaintainerCode(datasetVersion));
            template.addVersion(datasetVersion);
            mockDataset(template);
        } else {
            if (!datasetVersion.getDataset().getVersions().contains(datasetVersion)) {
                datasetVersion.getDataset().addVersion(datasetVersion);
            }
        }

        datasetVersion.getDataset().setViewCode(DatasetVersionUtils.generateViewCode(datasetCode));

        computeCoverageRelatedMetadata(datasetVersion);

        datasetVersion.setKeepAllData(Boolean.TRUE);
        datasetVersion.setDataSourceType(DataSourceTypeEnum.FILE);

        return datasetVersion;
    }

    public static void mockDatasetVersionCoverages(DatasetVersion datasetVersion) {
        datasetVersion.getDimensionsCoverage().clear();

        datasetVersion.addDimensionsCoverage(new CodeDimension("DIM01", "C01"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("DIM01", "C02"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("DIM01", "C03"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("DIM02", "C01"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("DIM02", "C02"));
    }

    public static void mockDatasetVersionCoveragesWithTemporalAndRelated(DatasetVersion datasetVersion) {
        datasetVersion.getDimensionsCoverage().clear();

        datasetVersion.addDimensionsCoverage(new CodeDimension("DIM01", "C01"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("DIM01", "C02"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("DIM01", "C03"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2010"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2011"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2012"));
    }

    public static void computeCoverageRelatedMetadata(DatasetVersion datasetVersion) {
        Map<String, List<CodeDimension>> codesMap = buildCodeDimensionsMap(datasetVersion.getDimensionsCoverage());

        if (datasetVersion.getTemporalCoverage().isEmpty()) {
            List<CodeDimension> temporalCodes = codesMap.get(StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID);
            if (temporalCodes != null) {
                for (CodeDimension codeDim : temporalCodes) {
                    datasetVersion.addTemporalCoverage(StatisticalResourcesDoMocks.mockTemporalCode(codeDim.getIdentifier(), codeDim.getIdentifier()));
                }
                List<String> sortedTempCodes = new ArrayList<String>();
                StatisticalResourcesCollectionUtils.mapCollection(temporalCodes, sortedTempCodes, new CodeDimensionToCodeStringTransformer());
                sortedTempCodes = SdmxTimeUtils.sortTimeList(sortedTempCodes);
                datasetVersion.setDateStart(SdmxTimeUtils.calculateDateTimes(sortedTempCodes.get(0))[0]);
                datasetVersion.setDateEnd(SdmxTimeUtils.calculateDateTimes(sortedTempCodes.get(sortedTempCodes.size() - 1))[0]);
            }
        }

        if (datasetVersion.getGeographicCoverage().isEmpty()) {
            List<CodeDimension> geoCodes = codesMap.get("GEO_DIM");
            if (geoCodes != null) {
                for (CodeDimension codeDim : geoCodes) {
                    datasetVersion.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem(codeDim.getIdentifier()));
                }
            }
        }

        if (datasetVersion.getMeasureCoverage().isEmpty()) {
            List<CodeDimension> measureCodes = codesMap.get("MEAS_DIM");
            if (measureCodes != null) {
                for (CodeDimension codeDim : measureCodes) {
                    datasetVersion.addMeasureCoverage(StatisticalResourcesDoMocks.mockConceptExternalItem(codeDim.getIdentifier()));
                }
            }
        }

        // FORMAT EXTENTS
        if (codesMap.size() > 0) {
            datasetVersion.setFormatExtentDimensions(codesMap.size());

            long observations = 1;
            for (String dimension : codesMap.keySet()) {
                observations *= codesMap.get(dimension).size();
            }

            datasetVersion.setFormatExtentObservations(observations);
        }
    }

    private static Map<String, List<CodeDimension>> buildCodeDimensionsMap(List<CodeDimension> codeDimensions) {
        Map<String, List<CodeDimension>> map = new HashMap<String, List<CodeDimension>>();
        for (CodeDimension codeDimension : codeDimensions) {
            List<CodeDimension> codes = map.get(codeDimension.getDsdComponentId());
            if (codes == null) {
                codes = new ArrayList<CodeDimension>();
                map.put(codeDimension.getDsdComponentId(), codes);
            }
            codes.add(codeDimension);
        }
        return map;
    }

    private void fillNeededMetadataToGenerateDatasetVersionCode(DatasetVersionMock datasetVersion) {
        if (datasetVersion.getSequentialId() == null) {
            datasetVersion.setSequentialId(1);
        }

        if (datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation() == null) {
            datasetVersion.getSiemacMetadataStatisticalResource().setStatisticalOperation(mockStatisticalOperationExternalItem(mockString(10)));
        }
    }

    private static String buildSequentialResourceCode(String operationCode, int sequentialId) {
        return operationCode + "_" + String.format("%06d", sequentialId);
    }

    // -----------------------------------------------------------------
    // CATEGORISATION
    // -----------------------------------------------------------------
    public Categorisation mockCategorisationWithGeneratedCategory(DatasetVersion datasetVersion, ExternalItem maintainer, String categoryCode, String categorisationCode) {
        return mockCategorisationWithDatasetVersionAndCategory(datasetVersion, maintainer, mockCategoryExternalItem(categoryCode), categorisationCode);
    }

    public Categorisation mockCategorisationWithDatasetVersionAndCategory(DatasetVersion datasetVersion, ExternalItem maintainer, ExternalItem category, String categorisationCode) {
        Categorisation categorisation = new Categorisation();
        categorisation.setVersionableStatisticalResource(new VersionableStatisticalResource());
        categorisation.getVersionableStatisticalResource().setCode(categorisationCode);
        categorisation.getVersionableStatisticalResource()
                .setUrn(GeneratorUrnUtils.generateSdmxCategorisationUrn(new String[]{maintainer.getCodeNested()}, categorisationCode, StatisticalResourcesMockFactory.INIT_VERSION));
        categorisation.setDatasetVersion(datasetVersion);
        categorisation.setCategory(category);
        categorisation.setMaintainer(maintainer);
        return mockCategorisation(categorisation);
    }

    // -----------------------------------------------------------------
    // PUBLICATION
    // -----------------------------------------------------------------

    public void mockPublication(PublicationMock publication) {
        if (publication.getIdentifiableStatisticalResource() == null) {
            publication.setIdentifiableStatisticalResource(new IdentifiableStatisticalResource());
        }

        fillNeededMetadataToGeneratePublicationCode(publication);

        publication.getIdentifiableStatisticalResource()
                .setCode(buildSequentialResourceCode(publication.getIdentifiableStatisticalResource().getStatisticalOperation().getCode(), publication.getSequentialId()));

        mockIdentifiableStatisticalResource(publication.getIdentifiableStatisticalResource(), TypeRelatedResourceEnum.PUBLICATION);

        String maintainerId = publication.getMaintainerCode();

        String[] maintainerAgencyId = new String[]{maintainerId};

        publication.getIdentifiableStatisticalResource()
                .setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceCollectionUrn(maintainerAgencyId, publication.getIdentifiableStatisticalResource().getCode()));

    }

    private void fillNeededMetadataToGeneratePublicationCode(PublicationMock publication) {
        if (publication.getSequentialId() == null) {
            publication.setSequentialId(1);
        }

        if (publication.getIdentifiableStatisticalResource().getStatisticalOperation() == null) {
            publication.getIdentifiableStatisticalResource().setStatisticalOperation(mockStatisticalOperationExternalItem(mockString(10)));
        }
    }

    public Publication mockPublicationWithGeneratedDatasetVersion(PublicationMock publication) {
        if (publication == null) {
            publication = new PublicationMock();
        }
        if (publication.getMaintainerCode() == null) {
            publication.setMaintainerCode(mockString(10));
        }
        mockPublication(publication);

        PublicationVersionMock template = new PublicationVersionMock();
        template.setPublication(publication);
        template.setMaintainerCode(publication.getMaintainerCode());
        template.getSiemacMetadataStatisticalResource().setLastVersion(true);
        PublicationVersion publicationVersion = mockPublicationVersion(template);

        return publicationVersion.getPublication();
    }

    public Publication mockPublicationWithGeneratedPublicationVersion() {
        return mockPublicationWithGeneratedDatasetVersion(null);
    }

    // -----------------------------------------------------------------
    // PUBLICATION VERSION
    // -----------------------------------------------------------------
    @Override
    public PublicationVersion mockPublicationVersion() {
        return mockPublicationVersion(null);
    }

    public PublicationVersion mockPublicationVersion(PublicationVersionMock publicationVersion) {
        if (publicationVersion == null) {
            publicationVersion = new PublicationVersionMock();
        }

        Publication publication = publicationVersion.getPublication();

        // Statistical Operation
        if (publication != null && publication.getIdentifiableStatisticalResource() != null && publication.getIdentifiableStatisticalResource().getStatisticalOperation() != null) {
            publicationVersion.getSiemacMetadataStatisticalResource()
                    .setStatisticalOperation(mockStatisticalOperationExternalItem(publication.getIdentifiableStatisticalResource().getStatisticalOperation().getCode()));
        }

        // CODE
        if (publication != null && publication.getIdentifiableStatisticalResource() != null && publication.getIdentifiableStatisticalResource().getCode() != null) {
            publicationVersion.getSiemacMetadataStatisticalResource().setCode(publicationVersion.getPublication().getIdentifiableStatisticalResource().getCode());
        } else {
            fillNeededMetadataToGeneratePublicationVersionCode(publicationVersion);
            String publicationCode = buildSequentialResourceCode(publicationVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(), publicationVersion.getSequentialId());
            publicationVersion.getSiemacMetadataStatisticalResource().setCode(publicationCode);
        }

        publicationVersion
                .setSiemacMetadataStatisticalResource(mockSiemacMetadataStatisticalResource(publicationVersion.getSiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.PUBLICATION_VERSION));

        // PUBLICATION CODE
        String publicationCode = publicationVersion.getSiemacMetadataStatisticalResource().getCode();
        if (publicationVersion.getPublication() == null) {
            PublicationMock template = new PublicationMock();
            template.setCode(publicationCode);
            template.addVersion(publicationVersion);
            template.setMaintainerCode(getMaintainerCode(publicationVersion));
            mockPublication(template);
        } else {
            if (!publicationVersion.getPublication().getVersions().contains(publicationVersion)) {
                publicationVersion.getPublication().addVersion(publicationVersion);
            }
        }

        return publicationVersion;
    }

    private void fillNeededMetadataToGeneratePublicationVersionCode(PublicationVersionMock publicationVersion) {
        if (publicationVersion.getSequentialId() == null) {
            publicationVersion.setSequentialId(1);
        }

        if (publicationVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation() == null) {
            publicationVersion.getSiemacMetadataStatisticalResource().setStatisticalOperation(mockStatisticalOperationExternalItem(mockString(10)));
        }
    }

    private String getMaintainerCode(HasLifecycle resourceWithLifecycle) {
        if (resourceWithLifecycle != null && resourceWithLifecycle.getLifeCycleStatisticalResource() != null && resourceWithLifecycle.getLifeCycleStatisticalResource().getMaintainer() != null) {
            return resourceWithLifecycle.getLifeCycleStatisticalResource().getMaintainer().getCodeNested();
        }
        return null;
    }

    public Chapter mockChapterWithParent() {
        return mockChapterInParentElementLevel(mockChapterElementLevel(mockPublicationVersion()));
    }

    public Cube mockDatasetCubeWithParent(Dataset mockDatasetWithGeneratedDatasetVersions) {
        Cube cube = mockDatasetCube(mockDatasetWithGeneratedDatasetVersions);
        cube.getElementLevel().setParent(mockChapter().getElementLevel());
        return cube;
    }

    public Cube mockQueryCubeWithParent(Query mockQueryWithGeneratedQueryVersions) {
        Cube cube = mockQueryCube(mockQueryWithGeneratedQueryVersions);
        cube.getElementLevel().setParent(mockChapter().getElementLevel());
        return cube;
    }

    // -----------------------------------------------------------------
    // PUBLICATION
    // -----------------------------------------------------------------

    public void mockMultidataset(MultidatasetMock multidataset) {
        if (multidataset.getIdentifiableStatisticalResource() == null) {
            multidataset.setIdentifiableStatisticalResource(new IdentifiableStatisticalResource());
        }

        fillNeededMetadataToGenerateMultidatasetCode(multidataset);

        multidataset.getIdentifiableStatisticalResource()
                .setCode(buildSequentialResourceCode(multidataset.getIdentifiableStatisticalResource().getStatisticalOperation().getCode(), multidataset.getSequentialId()));

        mockIdentifiableStatisticalResource(multidataset.getIdentifiableStatisticalResource(), TypeRelatedResourceEnum.MULTIDATASET);

        String maintainerId = multidataset.getMaintainerCode();

        String[] maintainerAgencyId = new String[]{maintainerId};

        multidataset.getIdentifiableStatisticalResource()
                .setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceMultidatasetUrn(maintainerAgencyId, multidataset.getIdentifiableStatisticalResource().getCode()));

    }

    private void fillNeededMetadataToGenerateMultidatasetCode(MultidatasetMock multidataset) {
        if (multidataset.getSequentialId() == null) {
            multidataset.setSequentialId(1);
        }

        if (multidataset.getIdentifiableStatisticalResource().getStatisticalOperation() == null) {
            multidataset.getIdentifiableStatisticalResource().setStatisticalOperation(mockStatisticalOperationExternalItem(mockString(10)));
        }
    }

    public Multidataset mockMultidatasetWithGeneratedDatasetVersion(MultidatasetMock multidataset) {
        if (multidataset == null) {
            multidataset = new MultidatasetMock();
        }
        if (multidataset.getMaintainerCode() == null) {
            multidataset.setMaintainerCode(mockString(10));
        }
        mockMultidataset(multidataset);

        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setMultidataset(multidataset);
        template.setMaintainerCode(multidataset.getMaintainerCode());
        template.getSiemacMetadataStatisticalResource().setLastVersion(true);
        MultidatasetVersion multidatasetVersion = mockMultidatasetVersion(template);

        return multidatasetVersion.getMultidataset();
    }

    public Multidataset mockMultidatasetWithGeneratedMultidatasetVersion() {
        return mockMultidatasetWithGeneratedDatasetVersion(null);
    }

    // -----------------------------------------------------------------
    // MULTIDATASET VERSION
    // -----------------------------------------------------------------
    @Override
    public MultidatasetVersion mockMultidatasetVersion() {
        return mockMultidatasetVersion(null);
    }

    public MultidatasetVersion mockMultidatasetVersion(MultidatasetVersionMock multidatasetVersion) {
        if (multidatasetVersion == null) {
            multidatasetVersion = new MultidatasetVersionMock();
        }

        Multidataset multidataset = multidatasetVersion.getMultidataset();

        // Statistical Operation
        if (multidataset != null && multidataset.getIdentifiableStatisticalResource() != null && multidataset.getIdentifiableStatisticalResource().getStatisticalOperation() != null) {
            multidatasetVersion.getSiemacMetadataStatisticalResource()
                    .setStatisticalOperation(mockStatisticalOperationExternalItem(multidataset.getIdentifiableStatisticalResource().getStatisticalOperation().getCode()));
        }

        // CODE
        if (multidataset != null && multidataset.getIdentifiableStatisticalResource() != null && multidataset.getIdentifiableStatisticalResource().getCode() != null) {
            multidatasetVersion.getSiemacMetadataStatisticalResource().setCode(multidatasetVersion.getMultidataset().getIdentifiableStatisticalResource().getCode());
        } else {
            fillNeededMetadataToGenerateMultidatasetVersionCode(multidatasetVersion);
            String multidatasetCode = buildSequentialResourceCode(multidatasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(),
                    multidatasetVersion.getSequentialId());
            multidatasetVersion.getSiemacMetadataStatisticalResource().setCode(multidatasetCode);
        }

        multidatasetVersion
                .setSiemacMetadataStatisticalResource(mockSiemacMetadataStatisticalResource(multidatasetVersion.getSiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.MULTIDATASET_VERSION));

        // MULTIDATASET CODE
        String multidatasetCode = multidatasetVersion.getSiemacMetadataStatisticalResource().getCode();
        if (multidatasetVersion.getMultidataset() == null) {
            MultidatasetMock template = new MultidatasetMock();
            template.setCode(multidatasetCode);
            template.addVersion(multidatasetVersion);
            template.setMaintainerCode(getMaintainerCode(multidatasetVersion));
            mockMultidataset(template);
        } else {
            if (!multidatasetVersion.getMultidataset().getVersions().contains(multidatasetVersion)) {
                multidatasetVersion.getMultidataset().addVersion(multidatasetVersion);
            }
        }

        return multidatasetVersion;
    }

    private void fillNeededMetadataToGenerateMultidatasetVersionCode(MultidatasetVersionMock multidatasetVersion) {
        if (multidatasetVersion.getSequentialId() == null) {
            multidatasetVersion.setSequentialId(1);
        }

        if (multidatasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation() == null) {
            multidatasetVersion.getSiemacMetadataStatisticalResource().setStatisticalOperation(mockStatisticalOperationExternalItem(mockString(10)));
        }
    }

    // -----------------------------------------------------------------
    // STATISTICAL OFFICIALITY
    // -----------------------------------------------------------------

    // -----------------------------------------------------------------
    // BASE HIERARCHY
    // -----------------------------------------------------------------

    @Override
    protected void setSpecialCasesSiemacMetadataStatisticalResourceMock(SiemacMetadataStatisticalResource resource) {
        if (resource.getLastUpdate() == null && resource.getResourceCreatedDate() == null) {
            DateTime today = new DateTime();
            resource.setResourceCreatedDate(today.minusDays(1));
            resource.setLastUpdate(today);
        } else if (resource.getLastUpdate() == null) {
            DateTime update = resource.getResourceCreatedDate().plusDays(1);
            if (update.isAfterNow()) {
                update = new DateTime();
            }
            resource.setLastUpdate(update);
        } else if (resource.getResourceCreatedDate() == null) {
            resource.setResourceCreatedDate(resource.getLastUpdate().minusDays(1));
        }
    }

    @Override
    protected void setSpecialCasesLifeCycleStatisticalResourceMock(LifeCycleStatisticalResource resource, TypeRelatedResourceEnum artefactType) {
        if (resource.getLastVersion() == null) {
            resource.setLastVersion(true);
        }
        if (resource.getProcStatus() == null) {
            resource.setProcStatus(ProcStatusEnum.DRAFT);
        }
        if (resource.getCreationDate() == null) {
            resource.setCreationDate(new DateTime());
        }
        if (resource.getCreationUser() == null) {
            resource.setCreationUser(USER_MOCK);
        }

        String code = resource.getCode();
        String[] maintainerAgencyId = new String[]{resource.getMaintainer().getCodeNested()};
        String version = resource.getVersionLogic();

        // URN
        switch (artefactType) {
            case DATASET:
                resource.setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceDatasetUrn(maintainerAgencyId, code));
                break;
            case DATASET_VERSION:
                resource.setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceDatasetVersionUrn(maintainerAgencyId, code, version));
                break;
            case PUBLICATION:
                resource.setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceCollectionUrn(maintainerAgencyId, code));
                break;
            case PUBLICATION_VERSION:
                resource.setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceCollectionVersionUrn(maintainerAgencyId, code, version));
                break;
            case QUERY:
                resource.setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceQueryUrn(maintainerAgencyId, code));
                break;
            case QUERY_VERSION:
                resource.setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceQueryVersionUrn(maintainerAgencyId, code, version));
                break;
            case MULTIDATASET:
                resource.setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceMultidatasetUrn(maintainerAgencyId, code));
                break;
            case MULTIDATASET_VERSION:
                resource.setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceMultidatasetVersionUrn(maintainerAgencyId, code, version));
                break;
            default:
                // It's setting by fillIdentiyAndAuditMetadata in DBMockPersisterBase
                break;
        }
    }

    @Override
    protected void setSpecialCasesVersionableStatisticalResourceMock(VersionableStatisticalResource resource) {
        if (resource.getVersionLogic() == null) {
            resource.setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        }
    }

    @Override
    protected void setSpecialCasesIdentifiableStatisticalResourceMock(IdentifiableStatisticalResource resource, TypeRelatedResourceEnum artefactType) {
        if (resource.getCode() == null) {
            resource.setCode("resource-" + mockString(10));
        }
        if (resource.getUrn() == null) {
            switch (artefactType) {
                case DATASET:
                case DATASET_VERSION:
                case PUBLICATION:
                case PUBLICATION_VERSION:
                case QUERY:
                case QUERY_VERSION:
                case MULTIDATASET:
                case MULTIDATASET_VERSION:
                    // NOTHING, will be built in lifecycleResource
                    break;
                default:
                    // It's setting by fillIdentiyAndAuditMetadata in DBMockPersisterBase
                    break;
            }
        }
    }

    @Override
    protected void setSpecialCasesStatisticalResourceMock(StatisticalResource resource) {
        if (resource.getStatisticalOperation() == null) {
            resource.setStatisticalOperation(mockStatisticalOperationExternalItem(mockString(10)));
        }
    }

    @Override
    protected void setSpecialCasesQueryVersionMock(QueryVersion queryVersion) {
        // has to be discontinued because the related dataset is not final
        queryVersion.setStatus(QueryStatusEnum.DISCONTINUED);
    }

    @Override
    protected void setSpecialCasesStatisticOfficialityMock(StatisticOfficiality officiality) {
        officiality.setVersion(0L);
    }
    
    public static String getHtmlLink(String urn) {
        String htmlLink = "";
        String[] splitUrnItem = splitUrnItem(urn);
        String agencyID = splitUrnItem[0];
        String[] agenciesID = agencyID.contains(".") ? agencyID.split(".") : new String[]{agencyID};
        String resourceID = splitUrnItem[1];
        String version = splitUrnItem[2];
        
        return StatisticalResourcesPersistedDoMocks.mockPortalDatasetHtmlLink(agencyID, resourceID, version);
    }
    

}
