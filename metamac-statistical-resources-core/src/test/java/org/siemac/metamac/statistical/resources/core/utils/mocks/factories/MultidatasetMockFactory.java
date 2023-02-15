package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import static org.siemac.metamac.statistical.resources.core.utils.MultidatasetLifecycleTestUtils.prepareToPublished;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createDatasetVersionPublishedLastVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createPublishedAndDraftVersionsForDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createPublishedAndNotVisibleVersionsForDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createTwoPublishedVersionsForDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_03_C1_FOR_MULTIDATASET_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_04_C1_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_17_WITH_STRUCTURE_FOR_MULTIDATASET_VERSION_04_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_18_WITH_STRUCTURE_FOR_MULTIDATASET_VERSION_04_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_27_V1_PUBLISHED_FOR_MULTIDATASET_05_C1_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_27_V1_PUBLISHED_FOR_MULTIDATASET_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_28_V2_PUBLISHED_FOR_MULTIDATASET_05_C1_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_28_V2_PUBLISHED_FOR_MULTIDATASET_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_29_V3_PUBLISHED_FOR_MULTIDATASET_05_C1_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_29_V3_PUBLISHED_FOR_MULTIDATASET_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_30_V1_PUBLISHED_FOR_MULTIDATASET_06_C1_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_30_V1_PUBLISHED_FOR_MULTIDATASET_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_C1_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_84_PUBLISHED_FOR_MULTIDATASET_07_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_85_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION_EXTERNAL_ITEM_FULL_FOR_MULTIDATASET_07_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.createMultidatasetVersionInStatus;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.createMultidatasetVersionPublishedLastVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.createMultidatasetVersionPublishedPreviousVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.createMultidatasetVersionSimple;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.createPublishedQueryLinkedToDataset;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockProvider;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetCube;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.utils.mocks.MultidatasetMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.MultidatasetVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

@MockProvider
@SuppressWarnings("unused")
public class MultidatasetMockFactory extends StatisticalResourcesMockFactory<Multidataset> {

    public static final String             MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME                           = "MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION";

    public static final String             MULTIDATASET_02_C1_BASIC_WITH_GENERATED_VERSION_NAME                        = "MULTIDATASET_02_C1_BASIC_WITH_GENERATED_VERSION";
    
    public static final String             MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME                     = "MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS";
    
    public static final String             MULTIDATASET_03_BASIC_WITH_2_C1_MULTIDATASET_VERSIONS_NAME                  = "MULTIDATASET_03_BASIC_WITH_2_C1_MULTIDATASET_VERSIONS";

    public static final String             MULTIDATASET_04_STRUCTURED_WITH_2_MULTIDATASET_VERSIONS_NAME                = "MULTIDATASET_04_STRUCTURED_WITH_2_MULTIDATASET_VERSIONS";

    public static final String             MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME                       = "MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS";

    public static final String             MULTIDATASET_05_C1_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME                    = "MULTIDATASET_05_C1_WITH_MULTIPLE_PUBLISHED_VERSIONS";
    
    public static final String             MULTIDATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME = "MULTIDATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE";

    public static final String             MULTIDATASET_06_C1_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME = "MULTIDATASET_06_C1_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE";
    
    public static final String             MULTIDATASET_07_WITH_TWO_VERSIONS_LAST_ONE_READY_TO_PUBLISHED_NAME          = "MULTIDATASET_07_WITH_TWO_VERSIONS_LAST_ONE_READY_TO_PUBLISHED";

    public static final String             MULTIDATASET_08_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME                   = "MULTIDATASET_08_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME";
    public static final String             MULTIDATASET_09_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME   = "MULTIDATASET_09_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME";
    public static final String             MULTIDATASET_10_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME    = "MULTIDATASET_10_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME";
    public static final String             MULTIDATASET_11_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME     = "MULTIDATASET_11_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME";

    private static MultidatasetMockFactory instance                                                                    = null;

    private MultidatasetMockFactory() {
    }

    public static MultidatasetMockFactory getInstance() {
        if (instance == null) {
            instance = new MultidatasetMockFactory();
        }
        return instance;
    }

    private static Multidataset getMultidataset02BasicWithGeneratedVersion() {
        return createMultidatasetWithGeneratedMultidatasetVersions();
    }

    private static Multidataset getMultidataset02C1BasicWithGeneratedVersion() {
        return createMultidatasetWithGeneratedMultidatasetVersions();
    }
    
    private static Multidataset getMultidataset03BasicWith2MultidatasetVersions() {
        return getMultidataset03BasicWith2MultidatasetVersionsBase(MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME, MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME);
    }

    private static Multidataset getMultidataset03BasicWith2C1MultidatasetVersions() {
        return getMultidataset03BasicWith2MultidatasetVersionsBase(MULTIDATASET_VERSION_03_C1_FOR_MULTIDATASET_03_NAME, MULTIDATASET_VERSION_04_C1_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME);
    }
    
    private static Multidataset getMultidataset03BasicWith2MultidatasetVersionsBase(String multidatasetId1, String multidatasetId2) {
        MultidatasetMock multidataset = createMultidatasetToAddVersions(1);

        MultidatasetVersion version01 = createMultidatasetVersionPublishedPreviousVersion(multidataset, INIT_VERSION, new DateTime().minusDays(2), null);
        registerMultidatasetVersionMock(multidatasetId1, version01);

        MultidatasetVersion version02 = createMultidatasetVersionLastVersionInStatus(multidataset, SECOND_VERSION, ProcStatusEnum.DRAFT);
        registerMultidatasetVersionMock(multidatasetId2, version02);

        // Relations
        version02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(version01));
        return multidataset;
    }
    
    private static Multidataset getMultidataset04StructuredWith2MultidatasetVersions() {

        MultidatasetMock multidataset = createMultidatasetToAddVersions(1);

        MultidatasetVersion version01 = createMultidataset04Version01(multidataset, new DateTime().minusDays(2));
        registerMultidatasetVersionMock(MULTIDATASET_VERSION_17_WITH_STRUCTURE_FOR_MULTIDATASET_VERSION_04_NAME, version01);

        MultidatasetVersion version02 = createMultidataset04Version02(multidataset);
        registerMultidatasetVersionMock(MULTIDATASET_VERSION_18_WITH_STRUCTURE_FOR_MULTIDATASET_VERSION_04_AND_LAST_VERSION_NAME, version02);

        version02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(version01));
        return multidataset;
    }

    private static MultidatasetVersion createMultidataset04Version01(MultidatasetMock multidataset, DateTime validFrom) {
        MultidatasetVersionMock multidatasetVersion = MultidatasetVersionMockFactory.buildPublishedReadyMultidatasetVersion(multidataset, INIT_VERSION, validFrom, null, false);

        Dataset dataset01 = DatasetMockFactory.generateDatasetWithGeneratedVersion();
        registerDatasetMock(DatasetMockFactory.DATASET_22_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME, dataset01);

        Query query01 = QueryMockFactory.generateQueryWithGeneratedVersion();
        registerQueryMock(QueryMockFactory.QUERY_09_SINGLE_VERSION_USED_IN_PUB_VERSION_17_NAME, query01);

        Dataset dataset02 = DatasetMockFactory.generateDatasetWithGeneratedVersion();
        registerDatasetMock(DatasetMockFactory.DATASET_23_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME, dataset02);

        MultidatasetCube cube01 = createDatasetMultidatasetCube(multidatasetVersion, dataset01);
        cube01.setOrderInMultidataset(Long.valueOf(1));

        MultidatasetCube cube02 = createQueryMultidatasetCube(multidatasetVersion, query01);
        cube02.setOrderInMultidataset(Long.valueOf(2));

        MultidatasetCube cube03 = createDatasetMultidatasetCube(multidatasetVersion, dataset02);
        cube03.setOrderInMultidataset(Long.valueOf(3));

        MultidatasetCube cube04 = createQueryMultidatasetCube(multidatasetVersion, query01);
        cube04.setOrderInMultidataset(Long.valueOf(4));

        MultidatasetCube cube05 = createQueryMultidatasetCube(multidatasetVersion, query01);
        cube05.setOrderInMultidataset(Long.valueOf(5));

        MultidatasetVersionMockFactory.createMultidatasetVersionInStatus(multidatasetVersion, ProcStatusEnum.PUBLISHED);

        return multidatasetVersion;
    }

    private static MultidatasetVersion createMultidataset04Version02(MultidatasetMock multidataset) {
        // General metadata
        MultidatasetVersionMock multidatasetVersion = MultidatasetVersionMock.buildSimpleVersion(multidataset, SECOND_VERSION);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(1));

        return createMultidatasetVersionInStatus(multidatasetVersion, ProcStatusEnum.DRAFT);
    }

    private static Multidataset getMultidataset05WithMultiplePublishedVersions() {
        return getMultidataset05WithMultiplePublishedVersionsBase(MULTIDATASET_VERSION_27_V1_PUBLISHED_FOR_MULTIDATASET_05_NAME, MULTIDATASET_VERSION_28_V2_PUBLISHED_FOR_MULTIDATASET_05_NAME,
                MULTIDATASET_VERSION_29_V3_PUBLISHED_FOR_MULTIDATASET_05_NAME);
    }

    private static Multidataset getMultidataset05C1WithMultiplePublishedVersions() {
        return getMultidataset05WithMultiplePublishedVersionsBase(MULTIDATASET_VERSION_27_V1_PUBLISHED_FOR_MULTIDATASET_05_C1_NAME, MULTIDATASET_VERSION_28_V2_PUBLISHED_FOR_MULTIDATASET_05_C1_NAME,
                MULTIDATASET_VERSION_29_V3_PUBLISHED_FOR_MULTIDATASET_05_C1_NAME);
    }
    
    private static Multidataset getMultidataset05WithMultiplePublishedVersionsBase(String multidatasetId1, String multidatasetId2, String multidatasetId3) {
        MultidatasetMock multidataset = createMultidatasetToAddVersions(1);

        DateTime secondVersionPublishTime = new DateTime().minusDays(2);
        DateTime thirdVersionPublishTime = new DateTime().minusDays(1);

        MultidatasetVersion multidatasetVersion01 = createMultidatasetVersionPublishedPreviousVersion(multidataset, INIT_VERSION, new DateTime().minusDays(3), secondVersionPublishTime);
        registerMultidatasetVersionMock(multidatasetId1, multidatasetVersion01);

        MultidatasetVersion multidatasetVersion02 = createMultidatasetVersionPublishedPreviousVersion(multidataset, SECOND_VERSION, new DateTime().minusDays(2), thirdVersionPublishTime);
        registerMultidatasetVersionMock(multidatasetId2, multidatasetVersion02);

        MultidatasetVersion multidatasetVersion03 = createMultidatasetVersionPublishedLastVersion(multidataset, THIRD_VERSION, new DateTime().minusDays(1));
        registerMultidatasetVersionMock(multidatasetId3, multidatasetVersion03);

        multidatasetVersion03.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetVersion02));
        multidatasetVersion02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetVersion01));
        return multidataset;
    }
    
    private static Multidataset getMultidataset06WithMultiplePublishedVersionsAndLatestNoVisible() {
        return getMultidataset06WithMultiplePublishedVersionsAndLatestNoVisibleBase(MULTIDATASET_VERSION_30_V1_PUBLISHED_FOR_MULTIDATASET_06_NAME, MultidatasetVersionMockFactory.MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME);
    }

    private static Multidataset getMultidataset06C1WithMultiplePublishedVersionsAndLatestNoVisible() {
        return getMultidataset06WithMultiplePublishedVersionsAndLatestNoVisibleBase(MULTIDATASET_VERSION_30_V1_PUBLISHED_FOR_MULTIDATASET_06_C1_NAME, MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_C1_NAME);
    }
    
    private static Multidataset getMultidataset06WithMultiplePublishedVersionsAndLatestNoVisibleBase(String multidatasetId1, String multidatasetId2) {
        MultidatasetMock multidataset = createMultidatasetToAddVersions(1);

        DateTime secondVersionPublishTime = new DateTime().plusDays(1);

        MultidatasetVersion multidatasetVersion01 = createMultidatasetVersionPublishedPreviousVersion(multidataset, INIT_VERSION, new DateTime().minusDays(1), secondVersionPublishTime);
        registerMultidatasetVersionMock(multidatasetId1, multidatasetVersion01);

        MultidatasetVersion multidatasetVersion02 = createMultidatasetVersionPublishedLastVersion(multidataset, SECOND_VERSION, secondVersionPublishTime);
        registerMultidatasetVersionMock(multidatasetId2, multidatasetVersion02);

        multidatasetVersion02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetVersion01));
        multidatasetVersion01.getSiemacMetadataStatisticalResource().setIsReplacedByVersion(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetVersion02));

        return multidataset;
    }

    private static Multidataset getMultidataset07WithTwoVersionsLastOneReadyToPublished() {
        MultidatasetMock multidataset = createMultidatasetToAddVersions(1);

        DateTime secondVersionPublishTime = new DateTime().plusDays(1);

        MultidatasetVersion multidatasetVersion01 = createMultidatasetVersionPublishedPreviousVersion(multidataset, INIT_VERSION, new DateTime().minusDays(1), secondVersionPublishTime);
        registerMultidatasetVersionMock(MULTIDATASET_VERSION_84_PUBLISHED_FOR_MULTIDATASET_07_NAME, multidatasetVersion01);

        MultidatasetVersionMock multidatasetVersion02 = MultidatasetVersionMockFactory.buildMultidatasetVersionSimpleLastVersion(multidataset, SECOND_VERSION);

        MultidatasetVersionMockFactory.fillOptionalExternalItems(multidatasetVersion02);

        // datasets
        Dataset datasetSingleVersionPublished = createDatasetVersionPublishedLastVersion(1, INIT_VERSION, new DateTime().minusDays(3)).getDataset();
        Dataset datasetWithPublishedAndDraft = createPublishedAndDraftVersionsForDataset(2);
        Dataset datasetWithPublishedAndNotVisible = createPublishedAndNotVisibleVersionsForDataset(3, new DateTime().plusDays(3));
        Dataset datasetWithTwoPublishedVersions = createTwoPublishedVersionsForDataset(4);

        List<Dataset> datasets = Arrays.asList(datasetSingleVersionPublished, datasetWithPublishedAndNotVisible, datasetWithTwoPublishedVersions);

        // queries
        Query queryPublishedLinkedToDataset = createPublishedQueryLinkedToDataset("Q01", datasetSingleVersionPublished,
                datasetSingleVersionPublished.getVersions().get(0).getSiemacMetadataStatisticalResource().getValidFrom());

        DatasetVersion datasetVersionLinkedToQuery = createDatasetVersionPublishedLastVersion(5, INIT_VERSION, new DateTime().minusDays(3));
        Query queryPublishedLinekdToDatasetVersion = QueryMockFactory.createPublishedQueryLinkedToDatasetVersion("Q02", datasetVersionLinkedToQuery,
                datasetVersionLinkedToQuery.getSiemacMetadataStatisticalResource().getValidFrom());

        List<Query> queries = Arrays.asList(queryPublishedLinkedToDataset, queryPublishedLinekdToDatasetVersion);

        MultidatasetVersionMockFactory.populateMultidatasetWithCubes(multidatasetVersion02, datasets, queries);

        getStatisticalResourcesPersistedDoMocks().mockMultidatasetVersion(multidatasetVersion02);
        prepareToPublished(multidatasetVersion02);
        registerMultidatasetVersionMock(MULTIDATASET_VERSION_85_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION_EXTERNAL_ITEM_FULL_FOR_MULTIDATASET_07_NAME, multidatasetVersion02);

        multidatasetVersion02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetVersion01));

        return multidatasetVersion02.getMultidataset();
    }

    // Public builders

    public static Multidataset createMultidatasetWithTwoVersionsPublished(int sequentialId) {
        MultidatasetMock multidataset = createMultidatasetToAddVersions(sequentialId);

        DateTime secondVersionPublishTime = new DateTime().minusDays(1);

        MultidatasetVersion multidatasetVersion01 = createMultidatasetVersionPublishedPreviousVersion(multidataset, INIT_VERSION, new DateTime().minusDays(2), secondVersionPublishTime);

        MultidatasetVersion multidatasetVersion02 = createMultidatasetVersionPublishedLastVersion(multidataset, SECOND_VERSION, secondVersionPublishTime);

        multidatasetVersion02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetVersion01));

        return multidataset;
    }

    public static Multidataset createMultidatasetWithTwoVersionsOnePublishedLastNotVisible(int sequentialId) {
        MultidatasetMock multidataset = createMultidatasetToAddVersions(sequentialId);

        DateTime secondVersionPublishTime = new DateTime().plusDays(1);

        MultidatasetVersion multidatasetVersion01 = createMultidatasetVersionPublishedPreviousVersion(multidataset, INIT_VERSION, new DateTime().minusDays(2), secondVersionPublishTime);

        MultidatasetVersion multidatasetVersion02 = createMultidatasetVersionPublishedLastVersion(multidataset, SECOND_VERSION, secondVersionPublishTime);

        multidatasetVersion02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetVersion01));

        return multidataset;
    }

    public static Multidataset createMultidatasetWithTwoVersionsOnePublishedLastDraft(int sequentialId) {
        MultidatasetMock multidataset = createMultidatasetToAddVersions(sequentialId);

        MultidatasetVersion multidatasetVersion01 = createMultidatasetVersionPublishedPreviousVersion(multidataset, INIT_VERSION, new DateTime().minusDays(2), null);

        MultidatasetVersion multidatasetVersion02 = createMultidatasetVersionLastVersionInStatus(multidataset, SECOND_VERSION, ProcStatusEnum.DRAFT);

        multidatasetVersion02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetVersion01));

        return multidataset;
    }

    public static Multidataset createMultidatasetWithSingleVersionPublished(int sequentialId) {
        MultidatasetMock multidataset = createMultidatasetToAddVersions(sequentialId);

        MultidatasetVersion multidatasetVersion01 = createMultidatasetVersionPublishedLastVersion(multidataset, INIT_VERSION, new DateTime().minusDays(2));

        return multidataset;
    }

    public static Multidataset createMultidatasetWithSingleVersionPublishedNotVisible(int sequentialId) {
        MultidatasetMock multidataset = createMultidatasetToAddVersions(sequentialId);

        MultidatasetVersion multidatasetVersion01 = createMultidatasetVersionPublishedLastVersion(multidataset, INIT_VERSION, new DateTime().plusDays(2));

        return multidataset;
    }

    public static Multidataset createMultidatasetWithSingleVersionDraft(int sequentialId) {
        MultidatasetMock multidataset = createMultidatasetToAddVersions(sequentialId);

        MultidatasetVersion multidatasetVersion01 = createMultidatasetVersionLastVersionInStatus(multidataset, INIT_VERSION, ProcStatusEnum.DRAFT);

        return multidataset;
    }

    // INTERNAL BUILDERS

    private static MultidatasetVersion createMultidatasetVersionLastVersionInStatus(Multidataset multidataset, String version, ProcStatusEnum status) {
        MultidatasetVersionMock multidatasetVersionMock = createMultidatasetVersionSimple(multidataset, version, true);
        return createMultidatasetVersionInStatus(multidatasetVersionMock, status);
    }

    public static MultidatasetCube createQueryMultidatasetCube(MultidatasetVersion multidatasetVersion, Query query) {
        return getStatisticalResourcesPersistedDoMocks().mockQueryMultidatasetCube(multidatasetVersion, query);
    }

    private static MultidatasetCube createDatasetMultidatasetCube(MultidatasetVersionMock multidatasetVersion, Dataset dataset) {
        return getStatisticalResourcesPersistedDoMocks().mockDatasetMultidatasetCube(multidatasetVersion, dataset);
    }

    // Creations
    private static Multidataset createMultidatasetWithGeneratedMultidatasetVersions() {
        return getStatisticalResourcesPersistedDoMocks().mockMultidatasetWithGeneratedMultidatasetVersion();
    }

    public static MultidatasetMock createMultidatasetToAddVersions(Integer sequential) {
        MultidatasetMock multidataset = new MultidatasetMock();
        multidataset.setSequentialId(sequential);
        getStatisticalResourcesPersistedDoMocks().mockMultidataset(multidataset);
        return multidataset;
    }

}
