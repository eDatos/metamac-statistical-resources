package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import static org.siemac.metamac.statistical.resources.core.utils.DatasetLifecycleTestUtils.fillAsPublished;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_03_C1_FOR_DATASET_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_03_FOR_DATASET_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_04_FOR_DATASET_03_C1_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_05_FOR_DATASET_04_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_23_V2_PUBLISHED_FOR_DATASET_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_24_V3_PUBLISHED_FOR_DATASET_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_25_V1_PUBLISHED_FOR_DATASET_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_74_V02_FOR_DATASET_11_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_75_V01_FOR_DATASET_12_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_76_V02_FOR_DATASET_12_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_95_PUBLISHED_FOR_DATASET_32;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_96_NOT_VISIBLE_FOR_DATASET_32_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.addCodesToDimensionCoverage;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.fillDatasetVersionInStatusWithGeneratedDatasource;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_49_NOT_VISIBLE_REQUIRES_FIXED_DATASET_VERSION_NOT_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_50_NOT_VISIBLE_REQUIRES_DATASET_NOT_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_51_NOT_VISIBLE_REQUIRES_FIXED_DATASET_VERSION_NOT_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.buildSelectionItemWithDimensionAndCodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockDescriptor;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockProvider;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.Publication;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.DatasetLifecycleTestUtils;
import org.siemac.metamac.statistical.resources.core.utils.QueryLifecycleTestUtils;
import org.siemac.metamac.statistical.resources.core.utils.mocks.DatasetMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.DatasetVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.QueryVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesNotPersistedDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

@MockProvider
@SuppressWarnings("unused")
public class DatasetMockFactory extends StatisticalResourcesMockFactory<Dataset> {

    public static final String DATASET_01_BASIC_NAME = "DATASET_01_BASIC";

    public static final String DATASET_02_BASIC_WITH_GENERATED_VERSION_NAME = "DATASET_02_BASIC_WITH_GENERATED_VERSION";

    public static final String DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME = "DATASET_03_WITH_2_DATASET_VERSIONS";
    
    public static final String DATASET_03_BASIC_WITH_2_C1_DATASET_VERSIONS_NAME = "DATASET_03_WITH_2_C1_DATASET_VERSIONS";

    public static final String DATASET_04_FULL_FILLED_WITH_1_DATASET_VERSIONS_NAME = "DATASET_04_FULL_FILLED_WITH_1_DATASET_VERSIONS";

    public static final String DATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME = "DATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS";

    public static final String DATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME = "DATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE";

    public static final String DATASET_07_WITH_SINGLE_VERSION_AND_SINGLE_DATASOURCE_LINKED_TO_FILE_NAME = "DATASET_07_WITH_SINGLE_VERSION_AND_SINGLE_DATASOURCE_LINKED_TO_FILE";

    public static final String DATASET_08_WITH_SINGLE_VERSION_AND_MULTIPLE_DATASOURCES_LINKED_TO_FILE_NAME = "DATASET_08_WITH_SINGLE_VERSION_AND_MULTIPLE_DATASOURCES_LINKED_TO_FILE";

    public static final String DATASET_09_WITH_SINGLE_VERSION_AND_SINGLE_DATASOURCE_LINKED_TO_FILE_WITH_UNDERSCORE_NAME = "DATASET_09_WITH_SINGLE_VERSION_AND_SINGLE_DATASOURCE_LINKED_TO_FILE_WITH_UNDERSCORE";

    public static final String DATASET_10_WITH_DRAFT_VERSION__WITH_ONE_QUERY_LINKED_TO_DATASET_NAME = "DATASET_10_WITH_DRAFT_VERSION__WITH_ONE_QUERY_LINKED_TO_DATASET";

    public static final String DATASET_11_WITH_NOT_VISIBLE_VERSION_WITH_TWO_QUERIES_DRAFT_AND_NOT_VISIBLE_LINKED_TO_DATASET_NAME = "DATASET_11_WITH_NOT_VISIBLE_VERSION_WITH_TWO_QUERIES_DRAFT_AND_NOT_VISIBLE_LINKED_TO_DATASET";

    public static final String DATASET_12_WITH_PUBLISHED_VERSION_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET_NAME = "DATASET_12_WITH_PUBLISHED_VERSION_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET";

    public static final String DATASET_13_WITH_PUBLISHED_AND_DRAFT_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET_NAME = "DATASET_13_WITH_PUBLISHED_AND_DRAFT_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET";

    public static final String DATASET_14_WITH_PUBLISHED_AND_NOT_VISIBLE_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET_NAME = "DATASET_14_WITH_PUBLISHED_AND_NOT_VISIBLE_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET";

    public static final String DATASET_15_WITH_TWO_PUBLISHED_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET_NAME = "DATASET_15_WITH_TWO_PUBLISHED_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET";

    public static final String DATASET_16_WITH_PUBLISHED_AND_DRAFT_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET_AND_ONE_QUERY_DRAFT_LINKED_TO_VERSION_NAME = "DATASET_16_WITH_PUBLISHED_AND_DRAFT_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET_AND_ONE_QUERY_DRAFT_LINKED_TO_VERSION";

    public static final String DATASET_17_WITH_PUBLISHED_AND_NOT_VISIBLE_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET_AND_TWO_QUERIES_DRAFT_NOT_VISIBLE_LINKED_TO_VERSION_NAME = "DATASET_17_WITH_PUBLISHED_AND_NOT_VISIBLE_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET_AND_TWO_QUERIES_DRAFT_NOT_VISIBLE_LINKED_TO_VERSION";

    public static final String DATASET_18_WITH_TWO_PUBLISHED_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET_AND_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_VERSION_NAME = "DATASET_18_WITH_TWO_PUBLISHED_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET_AND_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_VERSION";

    public static final String DATASET_19_WITH_DRAFT_AND_PUBLISHED__IS_PART_OF_PUBLICATIONS_DIFFERENT_STATUS_NAME = "DATASET_19_WITH_DRAFT_AND_PUBLISHED__IS_PART_OF_PUBLICATIONS_DIFFERENT_STATUS";

    public static final String DATASET_20_WITH_PUBLISHED_AND_NOT_VISIBLE_VERSIONS__IS_PART_OF_PUBLICATIONS_DIFFERENT_STATUS_NAME = "DATASET_20_WITH_PUBLISHED_AND_NOT_VISIBLE_VERSIONS__IS_PART_OF_PUBLICATIONS_DIFFERENT_STATUS";

    public static final String DATASET_21_WITH_TWO_PUBLISHED_VERSIONS__IS_PART_OF_PUBLICATIONS_DIFFERENT_STATUS_NAME = "DATASET_21_WITH_TWO_PUBLISHED_VERSIONS__IS_PART_OF_PUBLICATIONS_DIFFERENT_STATUS";

    public static final String DATASET_22_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME = "DATASET_22_SIMPLE_LINKED_TO_PUB_VERSION_17";
    public static final String DATASET_23_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME = "DATASET_23_SIMPLE_LINKED_TO_PUB_VERSION_17";
    public static final String DATASET_22_C1_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME = "DATASET_22_C1_SIMPLE_LINKED_TO_PUB_VERSION_17";
    public static final String DATASET_23_C1_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME = "DATASET_23_C1_SIMPLE_LINKED_TO_PUB_VERSION_17";
    public static final String DATASET_24_SIMPLE_WITH_TWO_VERSIONS_WITH_QUERY_LINKED_TO_DATASET_NAME = "DATASET_24_SIMPLE_WITH_TWO_VERSIONS_WITH_QUERY_LINKED_TO_DATASET";

    public static final String DATASET_25_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME = "DATASET_25_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME";
    public static final String DATASET_26_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME = "DATASET_26_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME";
    public static final String DATASET_27_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME = "DATASET_27_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME";
    public static final String DATASET_28_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME = "DATASET_28_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME";
    public static final String DATASET_29_PUBLISHED_NOT_VISIBLE_USED_IN_PUBLICATION_VERSION_86_NAME = "DATASET_29_PUBLISHED_NOT_VISIBLE_USED_IN_PUBLICATION_VERSION_86_NAME";
    public static final String DATASET_30_LAST_VERSION_NOT_VISIBLE_WITH_PUBLICATION_AND_QUERIES_NOT_VISIBLE_BOTH_NOT_COMPATIBLE_NAME = "DATASET_30_LAST_VERSION_NOT_VISIBLE_WITH_PUBLICATION_AND_QUERIES_NOT_VISIBLE_BOTH_NOT_COMPATIBLE";
    public static final String DATASET_31_LAST_VERSION_NOT_VISIBLE_WITH_PUBLICATION_AND_QUERIES_NOT_VISIBLE_BOTH_COMPATIBLE_NAME = "DATASET_31_LAST_VERSION_NOT_VISIBLE_WITH_PUBLICATION_AND_QUERIES_NOT_VISIBLE_BOTH_COMPATIBLE";
    public static final String DATASET_32_LAST_VERSION_NOT_VISIBLE_WITH_PUBLICATION_AND_QUERY_NOT_VISIBLE_COMPATIBLE_NAME = "DATASET_32_LAST_VERSION_NOT_VISIBLE_WITH_PUBLICATION_AND_QUERY_NOT_VISIBLE_COMPATIBLE";

    public static final String DATASET_33_DRAFT_USED_IN_MULTIDATASET_VERSION_86_NAME = "DATASET_33_DRAFT_USED_IN_MULTIDATASET_VERSION_86_NAME";
    public static final String DATASET_34_PRODUCTION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME = "DATASET_34_PRODUCTION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME";
    public static final String DATASET_35_DIFFUSION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME = "DATASET_35_DIFFUSION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME";
    public static final String DATASET_36_VALIDATION_REJECTED_USED_IN_MULTIDATASET_VERSION_86_NAME = "DATASET_36_VALIDATION_REJECTED_USED_IN_MULTIDATASET_VERSION_86_NAME";
    public static final String DATASET_37_PUBLISHED_NOT_VISIBLE_USED_IN_MULTIDATASET_VERSION_86_NAME = "DATASET_37_PUBLISHED_NOT_VISIBLE_USED_IN_MULTIDATASET_VERSION_86_NAME";

    private static DatasetMockFactory instance = null;

    private DatasetMockFactory() {
    }

    public static DatasetMockFactory getInstance() {
        if (instance == null) {
            instance = new DatasetMockFactory();
        }
        return instance;
    }

    private static Dataset getDataset01Basic() {
        return generateDatasetWithGeneratedVersion();
    }

    private static Dataset getDataset02BasicWithGeneratedVersion() {
        return generateDatasetWithGeneratedVersion();
    }

    private static Dataset getDataset03With2DatasetVersions() {
        return getDataset03With2DatasetVersionsBase(DATASET_VERSION_03_FOR_DATASET_03_NAME, DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME);
    }
    
    private static Dataset getDataset03With2C1DatasetVersions() {
        return getDataset03With2DatasetVersionsBase(DATASET_VERSION_03_C1_FOR_DATASET_03_NAME, DATASET_VERSION_04_FOR_DATASET_03_C1_AND_LAST_VERSION_NAME);
    }
    
    private static Dataset getDataset03With2DatasetVersionsBase(String datasetId1, String datasetId2) {
        DatasetMock dataset = buildDatasetSimpleMock(1);
        getStatisticalResourcesPersistedDoMocks().mockDataset(dataset);

        DatasetVersionMock templateV1 = DatasetVersionMockFactory.buildSimpleVersion(dataset, INIT_VERSION);
        // not last version
        templateV1.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(4));
        templateV1.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(3));
        templateV1.getSiemacMetadataStatisticalResource().setLastVersion(false);
        // datasources
        templateV1.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        templateV1.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        templateV1.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        templateV1.setDatasetRepositoryId(StatisticalResourcesPersistedDoMocks.mockString(10));

        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(templateV1);
        DatasetVersion datasetVersionV1 = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(templateV1);
        fillAsPublished(datasetVersionV1);
        registerDatasetVersionMock(datasetId1, datasetVersionV1);

        DatasetVersionMock templateV2 = DatasetVersionMockFactory.buildSimpleVersion(dataset, SECOND_VERSION);
        // last version
        templateV2.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(1));
        templateV2.getSiemacMetadataStatisticalResource().setLastVersion(true);
        // datasources
        templateV2.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        templateV2.setDatasetRepositoryId(StatisticalResourcesPersistedDoMocks.mockString(10));

        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoveragesWithTemporalAndRelated(templateV2);
        DatasetVersion datasetVersionV2 = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(templateV2);
        registerDatasetVersionMock(datasetId2, datasetVersionV2);

        datasetVersionV2.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesNotPersistedDoMocks.mockRelatedResourceLinkedToDatasetVersion(datasetVersionV1));

        return dataset;
    }

    private static Dataset getDataset04FullFilledWith1DatasetVersions() {
        DatasetVersionMock template = DatasetVersionMockFactory.buildVersion("statOper02", 1, INIT_VERSION);
        template.getSiemacMetadataStatisticalResource().setLastVersion(true);

        template.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem());

        template.getTemporalCoverage().add(StatisticalResourcesDoMocks.mockTemporalCode());

        template.getMeasureCoverage().add(StatisticalResourcesDoMocks.mockConceptExternalItem());
        template.addGeographicGranularity(StatisticalResourcesDoMocks.mockCodeExternalItem());

        template.addTemporalGranularity(StatisticalResourcesDoMocks.mockCodeExternalItem());

        template.addStatisticalUnit(StatisticalResourcesDoMocks.mockConceptExternalItem());
        template.addStatisticalUnit(StatisticalResourcesDoMocks.mockConceptExternalItem());

        template.setDateStart("2012-11-25");

        template.setRelatedDsd(StatisticalResourcesDoMocks.mockDsdExternalItem());
        template.setUpdateFrequency(StatisticalResourcesDoMocks.mockCodeExternalItem());

        template.setFormatExtentDimensions(3);
        template.setFormatExtentObservations(1354L);
        template.setDateNextUpdate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusMonths(1).toDate()));
        template.setBibliographicCitation(StatisticalResourcesDoMocks.mockInternationalString("es", "biblio"));

        template.getSiemacMetadataStatisticalResource().setCreatedBy(StatisticalResourcesDoMocks.mockString(10));
        template.getSiemacMetadataStatisticalResource().setCreatedDate(StatisticalResourcesDoMocks.mockDateTime());
        template.getSiemacMetadataStatisticalResource().setLastUpdatedBy(StatisticalResourcesDoMocks.mockString(10));
        template.getSiemacMetadataStatisticalResource().setLastUpdated(StatisticalResourcesDoMocks.mockDateTime());

        template.setDatasetRepositoryId(StatisticalResourcesPersistedDoMocks.mockString(10));

        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(template);
        registerDatasetVersionMock(DATASET_VERSION_05_FOR_DATASET_04_NAME, datasetVersion);

        return datasetVersion.getDataset();
    }

    private static Dataset getDataset05WithMultiplePublishedVersions() {
        DatasetMock dataset = buildDatasetSimpleMock("oper01", 1);
        getStatisticalResourcesPersistedDoMocks().mockDataset(dataset);

        // v1
        DatasetVersionMock datasetVersionv1 = DatasetVersionMockFactory.buildSimpleVersion(dataset, INIT_VERSION);
        // not last version
        datasetVersionv1.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(3));
        datasetVersionv1.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(3));
        datasetVersionv1.getSiemacMetadataStatisticalResource().setValidTo(new DateTime().minusDays(2));
        datasetVersionv1.getSiemacMetadataStatisticalResource().setLastVersion(false);
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionv1);
        DatasetLifecycleTestUtils.prepareToVersioning(datasetVersionv1);
        registerDatasetVersionMock(DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME, datasetVersionv1);

        // v2
        DatasetVersionMock datasetVersionv2 = DatasetVersionMockFactory.buildSimpleVersion(dataset, SECOND_VERSION);
        // not last version
        datasetVersionv2.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(2));
        datasetVersionv2.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        datasetVersionv2.getSiemacMetadataStatisticalResource().setValidTo(new DateTime().minusDays(1));
        datasetVersionv2.getSiemacMetadataStatisticalResource().setLastVersion(false);
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionv2);
        DatasetLifecycleTestUtils.prepareToVersioning(datasetVersionv2);
        registerDatasetVersionMock(DATASET_VERSION_23_V2_PUBLISHED_FOR_DATASET_05_NAME, datasetVersionv2);

        // v3
        DatasetVersionMock datasetVersionv3 = DatasetVersionMockFactory.buildSimpleVersion(dataset, THIRD_VERSION);
        // last version
        datasetVersionv3.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(1));
        datasetVersionv3.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(1));
        datasetVersionv3.getSiemacMetadataStatisticalResource().setLastVersion(true);
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionv3);
        DatasetLifecycleTestUtils.prepareToVersioning(datasetVersionv3);
        registerDatasetVersionMock(DATASET_VERSION_24_V3_PUBLISHED_FOR_DATASET_05_NAME, datasetVersionv3);

        // Relations
        datasetVersionv1.getSiemacMetadataStatisticalResource().setIsReplacedByVersion(StatisticalResourcesNotPersistedDoMocks.mockRelatedResourceLinkedToDatasetVersion(datasetVersionv2));
        datasetVersionv2.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesNotPersistedDoMocks.mockRelatedResourceLinkedToDatasetVersion(datasetVersionv1));
        datasetVersionv2.getSiemacMetadataStatisticalResource().setIsReplacedByVersion(StatisticalResourcesNotPersistedDoMocks.mockRelatedResourceLinkedToDatasetVersion(datasetVersionv3));
        datasetVersionv3.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesNotPersistedDoMocks.mockRelatedResourceLinkedToDatasetVersion(datasetVersionv2));

        return dataset;
    }

    private static Dataset getDataset06WithMultiplePublishedVersionsAndLatestNoVisible() {
        DatasetMock dataset = buildDatasetSimpleMock("oper01", 1);
        getStatisticalResourcesPersistedDoMocks().mockDataset(dataset);

        // v1
        DatasetVersionMock datasetVersionv1 = DatasetVersionMockFactory.buildSimpleVersion(dataset, INIT_VERSION);
        // last version
        datasetVersionv1.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(1));
        datasetVersionv1.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(1));
        datasetVersionv1.getSiemacMetadataStatisticalResource().setLastVersion(false);
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionv1);
        DatasetLifecycleTestUtils.prepareToVersioning(datasetVersionv1);
        registerDatasetVersionMock(DATASET_VERSION_25_V1_PUBLISHED_FOR_DATASET_06_NAME, datasetVersionv1);

        // v2
        DatasetVersionMock datasetVersionv2 = DatasetVersionMockFactory.buildSimpleVersion(dataset, SECOND_VERSION);
        // last version
        datasetVersionv2.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime());
        datasetVersionv2.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(1));
        datasetVersionv2.getSiemacMetadataStatisticalResource().setLastVersion(true);
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionv2);
        DatasetLifecycleTestUtils.prepareToVersioning(datasetVersionv2);
        registerDatasetVersionMock(DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06_NAME, datasetVersionv2);

        datasetVersionv2.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesNotPersistedDoMocks.mockRelatedResourceLinkedToDatasetVersion(datasetVersionv1));

        return dataset;
    }

    private static Dataset getDataset07WithSingleVersionAndSingleDatasourceLinkedToFile() {
        DatasetVersionMock datasetVersionMock = new DatasetVersionMock();
        datasetVersionMock.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionMock);

        return datasetVersion.getDataset();
    }

    private static Dataset getDataset08WithSingleVersionAndMultipleDatasourcesLinkedToFile() {
        DatasetVersionMock datasetVersionMock = new DatasetVersionMock();
        datasetVersionMock.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        datasetVersionMock.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionMock);

        return datasetVersion.getDataset();
    }

    private static Dataset getDataset09WithSingleVersionAndSingleDatasourceLinkedToFileWithUnderscore() {
        Datasource datasource = new Datasource();
        datasource.setSourceName("datasource_underscore.px");
        datasource = getStatisticalResourcesPersistedDoMocks().mockDatasource(datasource);

        DatasetVersionMock datasetVersionMock = new DatasetVersionMock();
        datasetVersionMock.addDatasource(datasource);
        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionMock);

        return datasetVersion.getDataset();
    }

    private static MockDescriptor getDataset10WithDraftVersionWithOneQueryLinkedToDataset() {
        DatasetVersion datasetVersion = generateDatasetWithGeneratedVersion().getVersions().get(0);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(datasetVersion);

        QueryVersion query01 = buildQueryLinkedToDataset("Q01", datasetVersion.getDataset());

        return new MockDescriptor(datasetVersion.getDataset(), query01.getQuery());
    }

    private static MockDescriptor getDataset11WithNotVisibleVersionWithTwoQueriesDraftAndNotVisibleLinkedToDataset() {
        DatasetMock dataset = buildDatasetSimpleMock(1);
        DateTime publishingTimeV2 = new DateTime().plusDays(2);

        DatasetVersionMock template = DatasetVersionMockFactory.buildSimpleVersion(dataset, INIT_VERSION);
        template.getSiemacMetadataStatisticalResource().setLastVersion(true);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(2));
        DatasetVersion datasetVersionLatestPublished = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(template);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersionLatestPublished);
        registerDatasetVersionMock(DATASET_VERSION_74_V02_FOR_DATASET_11_NAME, datasetVersionLatestPublished);

        QueryVersion query01 = buildQueryLinkedToDataset("Q01", dataset);

        QueryVersion query02 = buildQueryLinkedToDataset("Q02", dataset);
        query02.getLifeCycleStatisticalResource().setValidFrom(new DateTime().plusDays(3));
        QueryLifecycleTestUtils.fillAsPublished(query02);

        return new MockDescriptor(dataset, query01, query02);
    }

    private static MockDescriptor getDataset12WithPublishedVersionWithThreeQueriesDraftNotVisibleAndPublishedLinkedToDataset() {
        DatasetMock dataset = buildDatasetSimpleMock(1);

        DatasetVersionMock template = DatasetVersionMockFactory.buildSimpleVersion(dataset, INIT_VERSION);
        template.getSiemacMetadataStatisticalResource().setLastVersion(true);
        DatasetVersion datasetVersionLatestVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(template);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersionLatestVersion);
        registerDatasetVersionMock(DATASET_VERSION_76_V02_FOR_DATASET_12_NAME, datasetVersionLatestVersion);

        List<QueryVersion> queries = buildQueriesDraftNotVisibleAndPublishedLinkedToDataset(dataset);

        return new MockDescriptor(dataset, queries);
    }

    private static MockDescriptor getDataset13WithPublishedAndDraftVersionsWithThreeQueriesDraftNotVisibleAndPublishedLinkedToDataset() {
        DatasetMock dataset = buildDatasetSimpleMock(1);

        dataset = createPublishedAndDraftVersionsForDataset(dataset);

        List<QueryVersion> queries = buildQueriesDraftNotVisibleAndPublishedLinkedToDataset(dataset);

        return new MockDescriptor(dataset, queries);
    }

    private static MockDescriptor getDataset14WithPublishedAndNotVisibleVersionsWithThreeQueriesDraftNotVisibleAndPublishedLinkedToDataset() {
        DatasetMock dataset = buildDatasetSimpleMock(1);

        dataset = createPublishedAndNotVisibleVersionsForDataset(dataset);

        List<QueryVersion> queries = buildQueriesDraftNotVisibleAndPublishedLinkedToDataset(dataset);

        return new MockDescriptor(dataset, queries);
    }

    private static MockDescriptor getDataset15WithTwoPublishedVersionsWithThreeQueriesDraftNotVisibleAndPublishedLinkedToDataset() {
        DatasetMock dataset = buildDatasetSimpleMock(1);

        dataset = createTwoPublishedVersionsForDataset(dataset);

        List<QueryVersion> queries = buildQueriesDraftNotVisibleAndPublishedLinkedToDataset(dataset);

        return new MockDescriptor(dataset, queries);
    }

    private static MockDescriptor getDataset16WithPublishedAndDraftVersionsWithThreeQueriesDraftNotVisibleAndPublishedLinkedToDatasetAndOneQueryDraftLinkedToVersion() {
        DatasetMock dataset = buildDatasetSimpleMock(1);

        dataset = createPublishedAndDraftVersionsForDataset(dataset);

        DatasetVersion datasetVersionPublished = dataset.getVersions().get(0);

        List<QueryVersion> queries = new ArrayList<QueryVersion>();

        queries.addAll(buildQueriesDraftNotVisibleAndPublishedLinkedToDataset(dataset));

        queries.add(buildQueryLinkedToDatasetVersion("Q04", datasetVersionPublished));

        return new MockDescriptor(dataset, queries);
    }

    private static MockDescriptor getDataset17WithPublishedAndNotVisibleVersionsWithThreeQueriesDraftNotVisibleAndPublishedLinkedToDatasetAndTwoQueriesDraftNotVisibleLinkedToVersion() {
        DatasetMock dataset = buildDatasetSimpleMock(1);

        dataset = createPublishedAndNotVisibleVersionsForDataset(dataset);

        DatasetVersion datasetVersionPublished = dataset.getVersions().get(0);

        List<QueryVersion> queries = new ArrayList<QueryVersion>();

        queries.addAll(buildQueriesDraftNotVisibleAndPublishedLinkedToDataset(dataset));

        queries.add(buildQueryLinkedToDatasetVersion("Q04", datasetVersionPublished));

        QueryVersion query05 = buildQueryLinkedToDatasetVersion("Q05", datasetVersionPublished);
        query05.getLifeCycleStatisticalResource().setValidFrom(datasetVersionPublished.getSiemacMetadataStatisticalResource().getValidFrom().plusDays(1));
        QueryLifecycleTestUtils.fillAsPublished(query05);

        queries.add(query05);

        return new MockDescriptor(dataset, queries);
    }

    private static MockDescriptor getDataset18WithTwoPublishedVersionsWithThreeQueriesDraftNotVisibleAndPublishedLinkedToDatasetAndThreeQueriesDraftNotVisibleAndPublishedLinkedToVersion() {
        DatasetMock dataset = buildDatasetSimpleMock(1);

        dataset = createTwoPublishedVersionsForDataset(dataset);

        DatasetVersion datasetVersionPublished = dataset.getVersions().get(0);

        List<QueryVersion> queries = new ArrayList<QueryVersion>();

        queries.addAll(buildQueriesDraftNotVisibleAndPublishedLinkedToDataset(dataset));

        queries.add(buildQueryLinkedToDatasetVersion("Q04", datasetVersionPublished));

        QueryVersion query05 = buildQueryLinkedToDatasetVersion("Q05", datasetVersionPublished);
        query05.getLifeCycleStatisticalResource().setValidFrom(new DateTime().plusDays(1));
        QueryLifecycleTestUtils.fillAsPublished(query05);
        queries.add(query05);

        QueryVersion query06 = buildQueryLinkedToDatasetVersion("Q06", datasetVersionPublished);
        QueryLifecycleTestUtils.fillAsPublished(query06);
        queries.add(query06);

        return new MockDescriptor(dataset, queries);
    }

    private static MockDescriptor getDataset19WithDraftAndPublishedIsPartOfPublicationsDifferentStatus() {
        DatasetVersion datasetVersion = createDatasetVersionPublished(null, INIT_VERSION, new DateTime().minusDays(5), null);

        Dataset dataset = datasetVersion.getDataset();

        DatasetVersionMock template = new DatasetVersionMock();
        template.setDataset(datasetVersion.getDataset());
        template.setVersionLogic(SECOND_VERSION);
        template.getSiemacMetadataStatisticalResource().setLastVersion(true);
        DatasetVersion datasetVersionLatestVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(template);

        datasetVersionLatestVersion.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersion));

        Publication pub01 = createPublicationWithTwoVersionsPublishedBothLinkedToDataset(1, dataset);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_46_PUBLISHED_V01_FOR_PUBLICATION_07_NAME, pub01.getVersions().get(0));
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_47_PUBLISHED_V02_FOR_PUBLICATION_07_NAME, pub01.getVersions().get(1));

        Publication pub02 = createPublicationWithTwoVersionsOnePublishedLastNotVisibleBothLinkedToDataset(2, dataset);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_48_PUBLISHED_V01_FOR_PUBLICATION_08_NAME, pub02.getVersions().get(0));
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_49_PUBLISHED_NOT_VISIBLE_V02_FOR_PUBLICATION_08_NAME, pub02.getVersions().get(1));

        Publication pub03 = createPublicationWithSingleVersionDraftLinkedToDataset(3, dataset);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_50_DRAFT_V01_FOR_PUBLICATION_09_NAME, pub03.getVersions().get(0));

        return new MockDescriptor(dataset, pub01, pub02, pub03);
    }

    private static MockDescriptor getDataset20WithPublishedAndNotVisibleVersionsIsPartOfPublicationsDifferentStatus() {
        DateTime secondVersionValidFrom = new DateTime().plusDays(1);

        DatasetVersion datasetVersion = createDatasetVersionPublished(null, INIT_VERSION, new DateTime().minusDays(5), secondVersionValidFrom);

        Dataset dataset = datasetVersion.getDataset();

        DatasetVersion datasetVersionLatestVersion = createDatasetVersionPublishedLastVersion(datasetVersion.getDataset(), SECOND_VERSION, secondVersionValidFrom);

        datasetVersionLatestVersion.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersion));

        Publication pub01 = createPublicationWithTwoVersionsPublishedBothLinkedToDataset(1, dataset);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_51_PUBLISHED_V01_FOR_PUBLICATION_10_NAME, pub01.getVersions().get(0));
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_52_PUBLISHED_V02_FOR_PUBLICATION_10_NAME, pub01.getVersions().get(1));

        Publication pub02 = createPublicationWithTwoVersionsOnePublishedLastNotVisibleBothLinkedToDataset(2, dataset);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_53_PUBLISHED_V01_FOR_PUBLICATION_11_NAME, pub02.getVersions().get(0));
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_54_PUBLISHED_NOT_VISIBLE_V02_FOR_PUBLICATION_11_NAME, pub02.getVersions().get(1));

        Publication pub03 = createPublicationWithSingleVersionDraftLinkedToDataset(3, dataset);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_55_DRAFT_V01_FOR_PUBLICATION_12_NAME, pub03.getVersions().get(0));

        return new MockDescriptor(dataset, pub01, pub02, pub03);
    }

    private static MockDescriptor getDataset21WithTwoPublishedVersionsIsPartOfPublicationsDifferentStatus() {
        DateTime secondVersionValidFrom = new DateTime().minusDays(1);

        DatasetVersion datasetVersion = createDatasetVersionPublished(null, INIT_VERSION, new DateTime().minusDays(5), secondVersionValidFrom);
        Dataset dataset = datasetVersion.getDataset();

        DatasetVersion datasetVersionLatestVersion = createDatasetVersionPublishedLastVersion(datasetVersion.getDataset(), SECOND_VERSION, secondVersionValidFrom);

        datasetVersionLatestVersion.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersion));

        Publication pub01 = createPublicationWithTwoVersionsPublishedBothLinkedToDataset(1, dataset);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_56_PUBLISHED_V01_FOR_PUBLICATION_13_NAME, pub01.getVersions().get(0));
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_57_PUBLISHED_V02_FOR_PUBLICATION_13_NAME, pub01.getVersions().get(1));

        Publication pub02 = createPublicationWithTwoVersionsOnePublishedLastNotVisibleBothLinkedToDataset(2, dataset);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_58_PUBLISHED_V01_FOR_PUBLICATION_14_NAME, pub02.getVersions().get(0));
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_59_PUBLISHED_NOT_VISIBLE_V02_FOR_PUBLICATION_14_NAME, pub02.getVersions().get(1));

        Publication pub03 = createPublicationWithSingleVersionDraftLinkedToDataset(3, dataset);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_60_DRAFT_V01_FOR_PUBLICATION_15_NAME, pub03.getVersions().get(0));

        return new MockDescriptor(dataset, pub01, pub02, pub03);
    }

    public static MockDescriptor getDataset24SimpleWithTwoVersionsWithQueryLinkedToDataset() {
        DatasetVersionMock previousTemplate = new DatasetVersionMock();
        previousTemplate.setVersionLogic(INIT_VERSION);
        previousTemplate.getSiemacMetadataStatisticalResource().setLastVersion(false);
        previousTemplate.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(5));
        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(previousTemplate);
        DatasetLifecycleTestUtils.prepareToVersioning(datasetVersion);
        registerDatasetVersionMock(DATASET_VERSION_75_V01_FOR_DATASET_12_NAME, datasetVersion);

        DatasetVersionMock template = new DatasetVersionMock();
        template.setDataset(datasetVersion.getDataset());
        template.setVersionLogic(SECOND_VERSION);
        template.getSiemacMetadataStatisticalResource().setLastVersion(true);
        template.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersion));
        DatasetVersion datasetVersionLatestVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(template);
        DatasetLifecycleTestUtils.prepareToPublished(datasetVersionLatestVersion);
        registerDatasetVersionMock(DATASET_VERSION_76_V02_FOR_DATASET_12_NAME, datasetVersionLatestVersion);

        // Query 1
        QueryVersionMock queryV1Template = new QueryVersionMock();
        queryV1Template.getLifeCycleStatisticalResource().setCode("Q01");
        queryV1Template.setMaintainerCode("agency01");
        queryV1Template.setStatisticalOperationCode("OPER01");
        queryV1Template.setStatus(QueryStatusEnum.ACTIVE);
        queryV1Template.setDataset(datasetVersion.getDataset());
        queryV1Template.setLastVersion(true);
        queryV1Template.setVersionLogic(INIT_VERSION);
        queryV1Template.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        QueryVersion query01 = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(queryV1Template);
        registerQueryVersionMock(QueryVersionMockFactory.QUERY_VERSION_36_LINKED_TO_DATASET_NAME, query01);

        return new MockDescriptor(datasetVersion.getDataset(), query01);
    }

    public static MockDescriptor getDataset30LastVersionNotVisibleWithPublicationAndQueriesNotVisibleBothNotCompatible() {

        DatasetMock dataset = new DatasetMock();
        dataset.setSequentialId(1);

        DateTime datasetValidFrom = new DateTime().plusDays(2);

        // published
        DatasetVersionMock datasetVersionPublished = DatasetVersionMockFactory.buildSimpleVersion(dataset, INIT_VERSION);
        datasetVersionPublished.getSiemacMetadataStatisticalResource().setLastVersion(false);
        addCodesToDimensionCoverage(datasetVersionPublished, "DIM_01", "D1_C01", "D1_C02", "D1_C03");
        addCodesToDimensionCoverage(datasetVersionPublished, "DIM_02", "D2_C01", "D2_C02", "D2_C03");
        addCodesToDimensionCoverage(datasetVersionPublished, "TIME_PERIOD", "2010");
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionPublished);
        DatasetVersionMockFactory.fillDatasetVersionInStatusWithGeneratedDatasource(datasetVersionPublished, ProcStatusEnum.PUBLISHED);

        // published not visible
        DatasetVersionMock datasetVersionNotVisible = DatasetVersionMockFactory.buildSimpleVersion(dataset, SECOND_VERSION);
        datasetVersionNotVisible.getSiemacMetadataStatisticalResource().setValidFrom(datasetValidFrom);
        datasetVersionNotVisible.getSiemacMetadataStatisticalResource().setLastVersion(true);
        datasetVersionNotVisible.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesDoMocks.mockDatasetVersionRelated(datasetVersionPublished));
        addCodesToDimensionCoverage(datasetVersionNotVisible, "DIM_01", "D1_C01", "D1_C02", "D1_C03");
        addCodesToDimensionCoverage(datasetVersionNotVisible, "DIM_02", "D2_C01", "D2_C02", "D2_C03");
        addCodesToDimensionCoverage(datasetVersionNotVisible, "TIME_PERIOD", "2010", "2011");
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionNotVisible);
        fillDatasetVersionInStatusWithGeneratedDatasource(datasetVersionNotVisible, ProcStatusEnum.PUBLISHED);
        registerDatasetVersionMock(DatasetVersionMockFactory.DATASET_VERSION_92_NOT_VISIBLE_FOR_DATASET_30_NAME, datasetVersionNotVisible);

        PublicationVersionMock publicationVersion = PublicationVersionMockFactory.buildPublishedReadyPublicationVersion(new PublicationMock(), INIT_VERSION, datasetValidFrom.plusDays(1), null, true);
        PublicationVersionMockFactory.createDatasetCubeElementLevel(publicationVersion, dataset);
        PublicationVersionMockFactory.createPublicationVersionInStatus(publicationVersion, ProcStatusEnum.PUBLISHED);

        // Query fixed
        QueryVersionMock queryFixedMock = new QueryVersionMock();
        queryFixedMock.setFixedDatasetVersion(datasetVersionNotVisible);
        queryFixedMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "D1_C01", "D1_C02"));
        queryFixedMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "D2_C01"));
        queryFixedMock.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2011"));
        queryFixedMock.getLifeCycleStatisticalResource().setValidFrom(datasetValidFrom.plusDays(1));
        QueryVersionMockFactory.createQueryVersionInStatus(queryFixedMock, ProcStatusEnum.PUBLISHED);
        registerQueryVersionMock(QUERY_VERSION_49_NOT_VISIBLE_REQUIRES_FIXED_DATASET_VERSION_NOT_VISIBLE_NAME, queryFixedMock);

        // Query To Dataset
        QueryVersionMock queryMock = new QueryVersionMock();
        queryMock.setDataset(dataset);
        queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "D1_C01", "D1_C02"));
        queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "D2_C01"));
        queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2011"));
        queryMock.getLifeCycleStatisticalResource().setValidFrom(datasetValidFrom.plusDays(1));
        QueryVersionMockFactory.createQueryVersionInStatus(queryMock, ProcStatusEnum.PUBLISHED);
        registerQueryVersionMock(QUERY_VERSION_50_NOT_VISIBLE_REQUIRES_DATASET_NOT_VISIBLE_NAME, queryMock);

        return new MockDescriptor(dataset, publicationVersion, queryFixedMock, queryMock);
    }

    public static MockDescriptor getDataset31LastVersionNotVisibleWithPublicationAndQueriesNotVisibleBothCompatible() {

        DatasetMock dataset = new DatasetMock();
        dataset.setSequentialId(1);

        DateTime datasetValidFrom = new DateTime().plusDays(2);

        // published
        DatasetVersionMock datasetVersionPublished = DatasetVersionMockFactory.buildSimpleVersion(dataset, INIT_VERSION);
        datasetVersionPublished.getSiemacMetadataStatisticalResource().setLastVersion(false);
        addCodesToDimensionCoverage(datasetVersionPublished, "DIM_01", "D1_C01", "D1_C02", "D1_C03");
        addCodesToDimensionCoverage(datasetVersionPublished, "DIM_02", "D2_C01", "D2_C02", "D2_C03");
        addCodesToDimensionCoverage(datasetVersionPublished, "TIME_PERIOD", "2010");
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionPublished);
        DatasetVersionMockFactory.fillDatasetVersionInStatusWithGeneratedDatasource(datasetVersionPublished, ProcStatusEnum.PUBLISHED);

        // published not visible
        DatasetVersionMock datasetVersionNotVisible = DatasetVersionMockFactory.buildSimpleVersion(dataset, SECOND_VERSION);
        datasetVersionNotVisible.getSiemacMetadataStatisticalResource().setValidFrom(datasetValidFrom);
        datasetVersionNotVisible.getSiemacMetadataStatisticalResource().setLastVersion(true);
        datasetVersionNotVisible.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesDoMocks.mockDatasetVersionRelated(datasetVersionPublished));
        addCodesToDimensionCoverage(datasetVersionNotVisible, "DIM_01", "D1_C01", "D1_C02", "D1_C03");
        addCodesToDimensionCoverage(datasetVersionNotVisible, "DIM_02", "D2_C01", "D2_C02", "D2_C03");
        addCodesToDimensionCoverage(datasetVersionNotVisible, "TIME_PERIOD", "2010", "2011");
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionNotVisible);
        fillDatasetVersionInStatusWithGeneratedDatasource(datasetVersionNotVisible, ProcStatusEnum.PUBLISHED);
        registerDatasetVersionMock(DatasetVersionMockFactory.DATASET_VERSION_94_NOT_VISIBLE_FOR_DATASET_31_NAME, datasetVersionNotVisible);

        PublicationVersionMock publicationVersion = PublicationVersionMockFactory.buildPublishedReadyPublicationVersion(new PublicationMock(), INIT_VERSION, datasetValidFrom.plusDays(1), null, true);
        PublicationVersionMockFactory.createDatasetCubeElementLevel(publicationVersion, dataset);
        PublicationVersionMockFactory.createPublicationVersionInStatus(publicationVersion, ProcStatusEnum.PUBLISHED);

        // Query fixed
        QueryVersionMock queryFixedMock = new QueryVersionMock();
        queryFixedMock.setFixedDatasetVersion(datasetVersionNotVisible);
        queryFixedMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "D1_C01", "D1_C02"));
        queryFixedMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "D2_C01"));
        queryFixedMock.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2010"));
        queryFixedMock.getLifeCycleStatisticalResource().setValidFrom(datasetValidFrom.plusDays(1));
        QueryVersionMockFactory.createQueryVersionInStatus(queryFixedMock, ProcStatusEnum.PUBLISHED);
        registerQueryVersionMock(QUERY_VERSION_51_NOT_VISIBLE_REQUIRES_FIXED_DATASET_VERSION_NOT_VISIBLE_NAME, queryFixedMock);

        // Query To Dataset
        QueryVersionMock queryMock = new QueryVersionMock();
        queryMock.setDataset(dataset);
        queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "D1_C01", "D1_C02"));
        queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "D2_C01"));
        queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2010"));
        queryMock.getLifeCycleStatisticalResource().setValidFrom(datasetValidFrom.plusDays(1));
        QueryVersionMockFactory.createQueryVersionInStatus(queryMock, ProcStatusEnum.PUBLISHED);

        return new MockDescriptor(dataset, publicationVersion, queryFixedMock, queryMock);
    }

    public static MockDescriptor getDataset32LastVersionNotVisibleWithPublicationAndQueryNotVisibleCompatible() {

        DatasetMock dataset = new DatasetMock();
        dataset.setSequentialId(1);

        DateTime datasetValidFrom = new DateTime().plusDays(2);

        // published
        DatasetVersionMock datasetVersionPublished = DatasetVersionMockFactory.buildSimpleVersion(dataset, INIT_VERSION);
        datasetVersionPublished.getSiemacMetadataStatisticalResource().setLastVersion(false);
        addCodesToDimensionCoverage(datasetVersionPublished, "DIM_01", "D1_C01", "D1_C02", "D1_C03");
        addCodesToDimensionCoverage(datasetVersionPublished, "DIM_02", "D2_C01", "D2_C02", "D2_C03");
        addCodesToDimensionCoverage(datasetVersionPublished, "TIME_PERIOD", "2010");
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionPublished);
        DatasetVersionMockFactory.fillDatasetVersionInStatusWithGeneratedDatasource(datasetVersionPublished, ProcStatusEnum.PUBLISHED);
        registerDatasetVersionMock(DATASET_VERSION_95_PUBLISHED_FOR_DATASET_32, datasetVersionPublished);

        // published not visible
        DatasetVersionMock datasetVersionNotVisible = DatasetVersionMockFactory.buildSimpleVersion(dataset, SECOND_VERSION);
        datasetVersionNotVisible.getSiemacMetadataStatisticalResource().setValidFrom(datasetValidFrom);
        datasetVersionNotVisible.getSiemacMetadataStatisticalResource().setLastVersion(true);
        datasetVersionNotVisible.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesDoMocks.mockDatasetVersionRelated(datasetVersionPublished));
        addCodesToDimensionCoverage(datasetVersionNotVisible, "DIM_01", "D1_C01", "D1_C02", "D1_C03");
        addCodesToDimensionCoverage(datasetVersionNotVisible, "DIM_02", "D2_C01", "D2_C02", "D2_C03");
        addCodesToDimensionCoverage(datasetVersionNotVisible, "TIME_PERIOD", "2010", "2011");
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersionNotVisible);
        fillDatasetVersionInStatusWithGeneratedDatasource(datasetVersionNotVisible, ProcStatusEnum.PUBLISHED);
        registerDatasetVersionMock(DATASET_VERSION_96_NOT_VISIBLE_FOR_DATASET_32_NAME, datasetVersionNotVisible);

        PublicationVersionMock publicationVersion = PublicationVersionMockFactory.buildPublishedReadyPublicationVersion(new PublicationMock(), INIT_VERSION, datasetValidFrom.plusDays(1), null, true);
        PublicationVersionMockFactory.createDatasetCubeElementLevel(publicationVersion, dataset);
        PublicationVersionMockFactory.createPublicationVersionInStatus(publicationVersion, ProcStatusEnum.PUBLISHED);

        // Query To Dataset
        QueryVersionMock queryMock = new QueryVersionMock();
        queryMock.setDataset(dataset);
        queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "D1_C01", "D1_C02"));
        queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "D2_C01"));
        queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2010"));
        queryMock.getLifeCycleStatisticalResource().setValidFrom(datasetValidFrom.plusDays(1));
        QueryVersionMockFactory.createQueryVersionInStatus(queryMock, ProcStatusEnum.PUBLISHED);

        return new MockDescriptor(dataset, publicationVersion, queryMock);
    }

    // *****************************************************
    // BUILDERS
    // *****************************************************

    private static DatasetMock buildDatasetSimpleMock(String statOper, int sequentialId) {
        DatasetMock datasetMock = buildDatasetSimpleMock(sequentialId);
        datasetMock.setStatisticalOperationCode(statOper);
        return datasetMock;
    }

    private static DatasetMock buildDatasetSimpleMock(int sequentialId) {
        DatasetMock datasetMock = new DatasetMock();
        datasetMock.setSequentialId(sequentialId);
        return datasetMock;
    }

    public static DatasetMock createPublishedAndDraftVersionsForDataset(int sequentialId) {
        DatasetMock dataset = new DatasetMock();
        dataset.setSequentialId(sequentialId);
        return createPublishedAndDraftVersionsForDataset(dataset);
    }

    private static DatasetMock createPublishedAndDraftVersionsForDataset(DatasetMock dataset) {
        // V01
        DatasetVersionMock template = DatasetVersionMockFactory.buildSimpleVersion(dataset, INIT_VERSION);
        template.getSiemacMetadataStatisticalResource().setLastVersion(false);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime());
        DatasetVersion datasetVersionPublished = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(template);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersionPublished);

        // V02
        DatasetVersionMock templateLatest = DatasetVersionMockFactory.buildSimpleVersion(dataset, SECOND_VERSION);
        templateLatest.getSiemacMetadataStatisticalResource().setLastVersion(true);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(templateLatest);
        DatasetVersion datasetVersionLatest = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(templateLatest);

        datasetVersionLatest.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesNotPersistedDoMocks.mockRelatedResourceLinkedToDatasetVersion(datasetVersionPublished));
        datasetVersionPublished.getSiemacMetadataStatisticalResource().setIsReplacedByVersion(StatisticalResourcesNotPersistedDoMocks.mockRelatedResourceLinkedToDatasetVersion(datasetVersionLatest));

        return dataset;
    }

    public static DatasetMock createPublishedAndNotVisibleVersionsForDataset(int sequentialId, DateTime validFrom) {
        DatasetMock dataset = new DatasetMock();
        dataset.setSequentialId(sequentialId);
        return createPublishedAndNotVisibleVersionsForDataset(dataset, validFrom);
    }

    private static DatasetMock createPublishedAndNotVisibleVersionsForDataset(DatasetMock dataset) {
        return createPublishedAndNotVisibleVersionsForDataset(dataset, new DateTime().plusDays(2));
    }

    public static DatasetMock createPublishedAndNotVisibleVersionsForDataset(DatasetMock dataset, DateTime validFrom) {
        DateTime secondPublishedTime = new DateTime().plusDays(2);

        // V01
        DatasetVersionMock template = DatasetVersionMockFactory.buildSimpleVersion(dataset, INIT_VERSION);
        template.getSiemacMetadataStatisticalResource().setLastVersion(false);
        template.getSiemacMetadataStatisticalResource().setValidTo(validFrom);
        DatasetVersion datasetVersionPublished = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(template);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersionPublished);

        // V02
        DatasetVersionMock templateLatest = DatasetVersionMockFactory.buildSimpleVersion(dataset, SECOND_VERSION);
        templateLatest.getSiemacMetadataStatisticalResource().setLastVersion(true);
        templateLatest.getSiemacMetadataStatisticalResource().setValidFrom(validFrom);
        DatasetVersion datasetVersionLatest = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(templateLatest);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersionLatest);

        datasetVersionLatest.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesNotPersistedDoMocks.mockRelatedResourceLinkedToDatasetVersion(datasetVersionPublished));

        return dataset;
    }

    public static DatasetMock createTwoPublishedVersionsForDataset(int sequentialId) {
        DatasetMock dataset = new DatasetMock();
        dataset.setSequentialId(sequentialId);
        return createTwoPublishedVersionsForDataset(dataset);
    }

    private static DatasetMock createTwoPublishedVersionsForDataset(DatasetMock dataset) {
        DateTime firstPublishedTime = new DateTime();
        DateTime secondPublishedTime = new DateTime();

        // V01
        DatasetVersionMock template = DatasetVersionMockFactory.buildSimpleVersion(dataset, INIT_VERSION);
        template.getSiemacMetadataStatisticalResource().setLastVersion(false);
        template.getSiemacMetadataStatisticalResource().setValidFrom(firstPublishedTime);
        template.getSiemacMetadataStatisticalResource().setValidTo(secondPublishedTime);
        DatasetVersion datasetVersionPublished = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(template);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersionPublished);

        // V02
        DatasetVersionMock templateLatest = DatasetVersionMockFactory.buildSimpleVersion(dataset, SECOND_VERSION);
        templateLatest.getSiemacMetadataStatisticalResource().setLastVersion(true);
        templateLatest.getSiemacMetadataStatisticalResource().setValidFrom(secondPublishedTime);
        DatasetVersion datasetVersionLatest = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(templateLatest);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersionLatest);

        datasetVersionLatest.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesNotPersistedDoMocks.mockRelatedResourceLinkedToDatasetVersion(datasetVersionPublished));
        datasetVersionPublished.getSiemacMetadataStatisticalResource().setIsReplacedByVersion(StatisticalResourcesNotPersistedDoMocks.mockRelatedResourceLinkedToDatasetVersion(datasetVersionLatest));

        return dataset;
    }

    public static DatasetVersion createDatasetVersionPublished(Dataset dataset, String version, DateTime validFrom, DateTime validTo) {
        return createDatasetVersionPublished(dataset, version, validFrom, validTo, false);
    }

    public static DatasetVersion createDatasetVersionPublishedLastVersion(int sequentialId, String version, DateTime validFrom) {
        DatasetMock dataset = new DatasetMock();
        dataset.setSequentialId(sequentialId);
        return createDatasetVersionPublished(dataset, version, validFrom, null, true);
    }

    public static DatasetVersion createDatasetVersionPublishedLastVersion(Dataset dataset, String version, DateTime validFrom) {
        return createDatasetVersionPublished(dataset, version, validFrom, null, true);
    }

    private static DatasetVersion createDatasetVersionPublished(Dataset dataset, String version, DateTime validFrom, DateTime validTo, boolean isLastVersion) {
        DatasetVersionMock template = new DatasetVersionMock();
        template.setDataset(dataset);
        template.setVersionLogic(version);
        template.getSiemacMetadataStatisticalResource().setLastVersion(isLastVersion);
        template.getSiemacMetadataStatisticalResource().setValidFrom(validFrom);
        template.getSiemacMetadataStatisticalResource().setValidTo(validTo);
        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(template);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);
        return datasetVersion;
    }

    public static Dataset generateDatasetWithGeneratedVersion() {
        DatasetMock template = new DatasetMock();
        return getStatisticalResourcesPersistedDoMocks().mockDatasetWithGeneratedDatasetVersion(template);
    }

    // *****************************************************
    // UTILS
    // *****************************************************

    private static List<QueryVersion> buildQueriesDraftNotVisibleAndPublishedLinkedToDataset(Dataset dataset) {
        // draft
        QueryVersion query01 = buildQueryLinkedToDataset("Q01", dataset);

        // not visible
        QueryVersion query02 = buildQueryLinkedToDataset("Q02", dataset);
        query02.getLifeCycleStatisticalResource().setValidFrom(new DateTime().plusDays(3));
        QueryLifecycleTestUtils.fillAsPublished(query02);

        // published
        QueryVersion query03 = buildQueryLinkedToDataset("Q03", dataset);
        QueryLifecycleTestUtils.fillAsPublished(query03);

        return Arrays.asList(query01, query02, query03);
    }

    private static Query createQueryWithOneVersionDiscontinued(String identifier, DatasetVersion datasetVersion) {
        QueryVersionMock queryV1Template = new QueryVersionMock();
        queryV1Template.getLifeCycleStatisticalResource().setCode(identifier);
        queryV1Template.setStatus(QueryStatusEnum.DISCONTINUED);
        queryV1Template.setFixedDatasetVersion(datasetVersion);
        queryV1Template.setLastVersion(true);
        queryV1Template.setVersionLogic(INIT_VERSION);
        queryV1Template.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        queryV1Template.getLifeCycleStatisticalResource().setValidFrom(datasetVersion.getSiemacMetadataStatisticalResource().getValidFrom());
        QueryVersion query02Discontinued = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(queryV1Template);
        return query02Discontinued.getQuery();
    }

    private static QueryVersion createQueryWithOneVersionInDraft(String identifier, DatasetVersion datasetVersion) {
        QueryVersionMock queryV1Template = new QueryVersionMock();
        queryV1Template.getLifeCycleStatisticalResource().setCode(identifier);
        queryV1Template.setStatus(QueryStatusEnum.ACTIVE);
        queryV1Template.setDataset(datasetVersion.getDataset());
        queryV1Template.setLastVersion(true);
        queryV1Template.setVersionLogic(INIT_VERSION);
        queryV1Template.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        QueryVersion query02Discontinued = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(queryV1Template);
        return query02Discontinued;
    }

    private static QueryVersion buildQueryLinkedToDatasetVersion(String identifier, DatasetVersion datasetVersion) {
        QueryVersionMock queryV1Template = new QueryVersionMock();
        queryV1Template.getLifeCycleStatisticalResource().setCode(identifier);
        queryV1Template.setStatus(QueryStatusEnum.DISCONTINUED);
        queryV1Template.setFixedDatasetVersion(datasetVersion);
        queryV1Template.setLastVersion(true);
        queryV1Template.setVersionLogic(INIT_VERSION);
        QueryVersion query02Discontinued = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(queryV1Template);
        return query02Discontinued;
    }

    private static QueryVersion buildQueryLinkedToDataset(String identifier, Dataset dataset) {
        QueryVersionMock queryV1Template = new QueryVersionMock();
        queryV1Template.getLifeCycleStatisticalResource().setCode(identifier);
        queryV1Template.setStatus(QueryStatusEnum.ACTIVE);
        queryV1Template.setDataset(dataset);
        queryV1Template.setLastVersion(true);
        queryV1Template.setVersionLogic(INIT_VERSION);
        QueryVersion query02Discontinued = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(queryV1Template);
        return query02Discontinued;
    }

    private static Query createQueryWithTwoVersionsLinkedEachOneToADifferentDatasetVersion(String identifier, DatasetVersion datasetVersion, DatasetVersion datasetVersionLatestPublished) {
        DateTime latestQueryVersionPublishingTime = datasetVersionLatestPublished.getSiemacMetadataStatisticalResource().getValidFrom();

        QueryVersionMock queryV1Template = new QueryVersionMock();
        queryV1Template.setStatus(QueryStatusEnum.DISCONTINUED);
        queryV1Template.setFixedDatasetVersion(datasetVersion);
        queryV1Template.setLastVersion(false);
        queryV1Template.setVersionLogic(INIT_VERSION);
        queryV1Template.getLifeCycleStatisticalResource().setCode(identifier);
        queryV1Template.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        queryV1Template.getLifeCycleStatisticalResource().setValidFrom(datasetVersion.getSiemacMetadataStatisticalResource().getValidFrom());
        queryV1Template.getLifeCycleStatisticalResource().setValidTo(latestQueryVersionPublishingTime);
        QueryVersion query01V1 = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(queryV1Template);

        QueryVersionMock queryV2Template = new QueryVersionMock();
        queryV2Template.setQuery(query01V1.getQuery());
        queryV2Template.setStatus(QueryStatusEnum.ACTIVE);
        queryV2Template.setDataset(datasetVersionLatestPublished.getDataset());
        queryV2Template.setLastVersion(true);
        queryV2Template.setVersionLogic(SECOND_VERSION);
        queryV2Template.getLifeCycleStatisticalResource().setCode(identifier);
        queryV2Template.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        queryV2Template.getLifeCycleStatisticalResource().setValidFrom(datasetVersionLatestPublished.getSiemacMetadataStatisticalResource().getValidFrom());
        QueryVersion query01V2 = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(queryV2Template);
        return query01V1.getQuery();
    }

    private static Publication createPublicationWithTwoVersionsPublishedBothLinkedToDataset(int sequentialId, Dataset dataset) {
        Publication pub = PublicationMockFactory.createPublicationWithTwoVersionsPublished(sequentialId);
        linkDatasetInPublicationVersionFirstLevel(dataset, pub.getVersions().get(0));
        linkDatasetInPublicationVersionFirstLevel(dataset, pub.getVersions().get(1));
        return pub;
    }

    private static Publication createPublicationWithTwoVersionsOnePublishedLastNotVisibleBothLinkedToDataset(int sequentialId, Dataset dataset) {
        Publication pub = PublicationMockFactory.createPublicationWithTwoVersionsOnePublishedLastNotVisible(sequentialId);
        linkDatasetInPublicationVersionFirstLevel(dataset, pub.getVersions().get(0));
        linkDatasetInPublicationVersionFirstLevel(dataset, pub.getVersions().get(1));
        return pub;
    }

    private static Publication createPublicationWithSingleVersionDraftLinkedToDataset(int sequentialId, Dataset dataset) {
        Publication pub = PublicationMockFactory.createPublicationWithSingleVersionDraft(sequentialId);
        linkDatasetInPublicationVersionFirstLevel(dataset, pub.getVersions().get(0));
        return pub;
    }

    private static void linkDatasetInPublicationVersionFirstLevel(Dataset dataset, PublicationVersion publicationVersion) {
        ElementLevel elementLevelV01 = PublicationVersionMockFactory.createDatasetCubeElementLevel(publicationVersion, dataset);
        elementLevelV01.setOrderInLevel(Long.valueOf(publicationVersion.getChildrenFirstLevel().size() + 1));
    }

}
