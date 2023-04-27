package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import static org.siemac.metamac.statistical.resources.core.utils.PublicationLifecycleTestUtils.prepareToPublished;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createDatasetVersionPublishedLastVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createPublishedAndDraftVersionsForDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createPublishedAndNotVisibleVersionsForDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createTwoPublishedVersionsForDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_17_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_18_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_27_V1_PUBLISHED_FOR_PUBLICATION_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_28_V2_PUBLISHED_FOR_PUBLICATION_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_29_V3_PUBLISHED_FOR_PUBLICATION_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_30_V1_PUBLISHED_FOR_PUBLICATION_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_84_PUBLISHED_FOR_PUBLICATION_07_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_85_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION_EXTERNAL_ITEM_FULL_FOR_PUBLICATION_07_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.createChapterElementLevel;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.createDatasetCubeElementLevel;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.createPublicationVersionInStatus;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.createPublicationVersionPublishedLastVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.createPublicationVersionPublishedPreviousVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.createPublicationVersionSimple;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.createQueryCubeElementLevel;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.createPublishedQueryLinkedToDataset;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockProvider;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.Publication;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

@MockProvider
@SuppressWarnings("unused")
public class PublicationMockFactory extends StatisticalResourcesMockFactory<Publication> {

    public static final String            PUBLICATION_02_BASIC_WITH_GENERATED_VERSION_NAME                           = "PUBLICATION_02_BASIC_WITH_GENERATED_VERSION";

    public static final String            PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME                      = "PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS";

    public static final String            PUBLICATION_04_STRUCTURED_WITH_2_PUBLICATION_VERSIONS_NAME                 = "PUBLICATION_04_STRUCTURED_WITH_2_PUBLICATION_VERSIONS";
    
    public static final String            PUBLICATION_04_C1_STRUCTURED_WITH_2_PUBLICATION_VERSIONS_NAME              = "PUBLICATION_04_C1_STRUCTURED_WITH_2_PUBLICATION_VERSIONS";
    
    public static final String            PUBLICATION_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME                       = "PUBLICATION_05_WITH_MULTIPLE_PUBLISHED_VERSIONS";

    public static final String            PUBLICATION_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME = "PUBLICATION_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE";

    public static final String            PUBLICATION_07_WITH_TWO_VERSIONS_LAST_ONE_READY_TO_PUBLISHED_NAME          = "PUBLICATION_07_WITH_TWO_VERSIONS_LAST_ONE_READY_TO_PUBLISHED";
    
    private static PublicationMockFactory instance                                                                   = null;

    private PublicationMockFactory() {
    }

    public static PublicationMockFactory getInstance() {
        if (instance == null) {
            instance = new PublicationMockFactory();
        }
        return instance;
    }

    private static Publication getPublication02BasicWithGeneratedVersion() {
        return createPublicationWithGeneratedPublicationVersions();
    }

    private static Publication getPublication03BasicWith2PublicationVersions() {
        PublicationMock publication = createPublicationToAddVersions(1);

        PublicationVersion version01 = createPublicationVersionPublishedPreviousVersion(publication, INIT_VERSION, new DateTime().minusDays(2), null);
        registerPublicationVersionMock(PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME, version01);

        PublicationVersion version02 = createPublicationVersionLastVersionInStatus(publication, SECOND_VERSION, ProcStatusEnum.DRAFT);
        registerPublicationVersionMock(PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME, version02);

        // Relations
        version02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(version01));
        return publication;
    }

    private static Publication getPublication04StructuredWith2PublicationVersions() {

        PublicationMock publication = createPublicationToAddVersions(1);

        PublicationVersion version01 = createPublication04Version01(publication, new DateTime().minusDays(2));
        registerPublicationVersionMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME, version01);

        PublicationVersion version02 = createPublication04Version02(publication);
        registerPublicationVersionMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME, version02);

        version02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(version01));
        return publication;
    }

    private static Publication getPublication04C1StructuredWith2PublicationVersions() {

        PublicationMock publication = createPublicationToAddVersions(1);

        PublicationVersion version01 = createPublication04C1Version01(publication, new DateTime().minusDays(2));
        registerPublicationVersionMock(PUBLICATION_VERSION_17_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME, version01);

        PublicationVersion version02 = createPublication04Version02(publication);
        registerPublicationVersionMock(PUBLICATION_VERSION_18_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME, version02);

        version02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(version01));
        return publication;
    }
    
    private static PublicationVersion createPublication04Version01Base(PublicationMock publication, DateTime validFrom, String query, String dataset1, String dataset2) {
        PublicationVersionMock publicationVersion = PublicationVersionMockFactory.buildPublishedReadyPublicationVersion(publication, INIT_VERSION, validFrom, null, false);

        Query query01 = QueryMockFactory.generateQueryWithGeneratedVersion();
        registerQueryMock(query, query01);

        // Structure
        // Chapter 01
        ElementLevel elementLevel01 = createChapterElementLevel(publicationVersion);
        elementLevel01.setOrderInLevel(Long.valueOf(1));
        // --> Chapter 01.01
        ElementLevel elementLevel01_01 = createChapterElementLevel(publicationVersion, elementLevel01);
        elementLevel01_01.setOrderInLevel(Long.valueOf(1));
        // ----> Cube 01.01.01
        Dataset dataset01 = DatasetMockFactory.generateDatasetWithGeneratedVersion();
        registerDatasetMock(dataset1, dataset01);
        ElementLevel elementLevel01_01_01 = createDatasetCubeElementLevel(publicationVersion, dataset01, elementLevel01_01);
        elementLevel01_01_01.setOrderInLevel(Long.valueOf(1));

        // --> Chapter 01.02
        ElementLevel elementLevel01_02 = createChapterElementLevel(publicationVersion, elementLevel01);
        elementLevel01_02.setOrderInLevel(Long.valueOf(2));
        // ----> Cube 01.02.01
        ElementLevel elementLevel01_02_01 = createQueryCubeElementLevel(publicationVersion, query01, elementLevel01_02);
        elementLevel01_02_01.setOrderInLevel(Long.valueOf(1));

        // --> Cube 01.03
        Dataset dataset02 = DatasetMockFactory.generateDatasetWithGeneratedVersion();
        registerDatasetMock(dataset2, dataset02);
        ElementLevel elementLevel01_03 = createDatasetCubeElementLevel(publicationVersion, dataset02, elementLevel01);
        elementLevel01_03.setOrderInLevel(Long.valueOf(3));

        // Chapter 02
        ElementLevel elementLevel02 = createChapterElementLevel(publicationVersion);
        elementLevel02.setOrderInLevel(Long.valueOf(2));
        // --> Cube 02.01
        ElementLevel elementLevel02_01 = createQueryCubeElementLevel(publicationVersion, query01, elementLevel02);
        elementLevel02_01.setOrderInLevel(Long.valueOf(1));
        // Cube 03
        ElementLevel elementLevel03 = createQueryCubeElementLevel(publicationVersion, query01);
        elementLevel03.setOrderInLevel(Long.valueOf(3));

        PublicationVersionMockFactory.createPublicationVersionInStatus(publicationVersion, ProcStatusEnum.PUBLISHED);

        return publicationVersion;
    }

    private static PublicationVersion createPublication04Version01(PublicationMock publication, DateTime validFrom) {
        return createPublication04Version01Base(publication, validFrom, QueryMockFactory.QUERY_09_SINGLE_VERSION_USED_IN_PUB_VERSION_17_NAME,
                DatasetMockFactory.DATASET_22_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME, DatasetMockFactory.DATASET_23_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME);
    }

    private static PublicationVersion createPublication04C1Version01(PublicationMock publication, DateTime validFrom) {
        return createPublication04Version01Base(publication, validFrom, QueryMockFactory.QUERY_09_C1_SINGLE_VERSION_USED_IN_PUB_VERSION_17_NAME,
                DatasetMockFactory.DATASET_22_C1_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME, DatasetMockFactory.DATASET_23_C1_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME);
    }
    
    private static PublicationVersion createPublication04Version02(PublicationMock publication) {
        // General metadata
        PublicationVersionMock publicationVersion = PublicationVersionMock.buildSimpleVersion(publication, SECOND_VERSION);
        publicationVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(1));

        // Structure
        // Chapter 01
        ElementLevel elementLevel01 = createChapterElementLevel(publicationVersion);
        elementLevel01.setOrderInLevel(Long.valueOf(1));
        // Chapter 02
        ElementLevel elementLevel02 = createChapterElementLevel(publicationVersion);
        elementLevel02.setOrderInLevel(Long.valueOf(2));
        // --> Chapter 02.01
        ElementLevel elementLevel02_01 = createChapterElementLevel(publicationVersion, elementLevel02);
        elementLevel02_01.setOrderInLevel(Long.valueOf(1));

        return createPublicationVersionInStatus(publicationVersion, ProcStatusEnum.DRAFT);
    }

    private static Publication getPublication05WithMultiplePublishedVersions() {
        PublicationMock publication = createPublicationToAddVersions(1);

        DateTime secondVersionPublishTime = new DateTime().minusDays(2);
        DateTime thirdVersionPublishTime = new DateTime().minusDays(1);

        PublicationVersion publicationVersion01 = createPublicationVersionPublishedPreviousVersion(publication, INIT_VERSION, new DateTime().minusDays(3), secondVersionPublishTime);
        registerPublicationVersionMock(PUBLICATION_VERSION_27_V1_PUBLISHED_FOR_PUBLICATION_05_NAME, publicationVersion01);

        PublicationVersion publicationVersion02 = createPublicationVersionPublishedPreviousVersion(publication, SECOND_VERSION, new DateTime().minusDays(2), thirdVersionPublishTime);
        registerPublicationVersionMock(PUBLICATION_VERSION_28_V2_PUBLISHED_FOR_PUBLICATION_05_NAME, publicationVersion02);

        PublicationVersion publicationVersion03 = createPublicationVersionPublishedLastVersion(publication, THIRD_VERSION, new DateTime().minusDays(1));
        registerPublicationVersionMock(PUBLICATION_VERSION_29_V3_PUBLISHED_FOR_PUBLICATION_05_NAME, publicationVersion03);

        publicationVersion03.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationVersion02));
        publicationVersion02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationVersion01));
        return publication;
    }

    private static Publication getPublication06WithMultiplePublishedVersionsAndLatestNoVisible() {
        PublicationMock publication = createPublicationToAddVersions(1);

        DateTime secondVersionPublishTime = new DateTime().plusDays(1);

        PublicationVersion publicationVersion01 = createPublicationVersionPublishedPreviousVersion(publication, INIT_VERSION, new DateTime().minusDays(1), secondVersionPublishTime);
        registerPublicationVersionMock(PUBLICATION_VERSION_30_V1_PUBLISHED_FOR_PUBLICATION_06_NAME, publicationVersion01);

        PublicationVersion publicationVersion02 = createPublicationVersionPublishedLastVersion(publication, SECOND_VERSION, secondVersionPublishTime);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME, publicationVersion02);

        publicationVersion02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationVersion01));
        publicationVersion01.getSiemacMetadataStatisticalResource().setIsReplacedByVersion(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationVersion02));

        return publication;
    }

    private static Publication getPublication07WithTwoVersionsLastOneReadyToPublished() {
        PublicationMock publication = createPublicationToAddVersions(1);

        DateTime secondVersionPublishTime = new DateTime().plusDays(1);

        PublicationVersion publicationVersion01 = createPublicationVersionPublishedPreviousVersion(publication, INIT_VERSION, new DateTime().minusDays(1), secondVersionPublishTime);
        registerPublicationVersionMock(PUBLICATION_VERSION_84_PUBLISHED_FOR_PUBLICATION_07_NAME, publicationVersion01);

        PublicationVersionMock publicationVersion02 = PublicationVersionMockFactory.buildPublicationVersionSimpleLastVersion(publication, SECOND_VERSION);

        PublicationVersionMockFactory.fillOptionalExternalItems(publicationVersion02);

        // datasets
        Dataset datasetSingleVersionPublished = createDatasetVersionPublishedLastVersion(1, INIT_VERSION, new DateTime().minusDays(3)).getDataset();
        Dataset datasetWithPublishedAndDraft = createPublishedAndDraftVersionsForDataset(2);
        Dataset datasetWithPublishedAndNotVisible = createPublishedAndNotVisibleVersionsForDataset(3, new DateTime().plusDays(3));
        Dataset datasetWithTwoPublishedVersions = createTwoPublishedVersionsForDataset(4);

        List<Dataset> datasets = Arrays.asList(datasetSingleVersionPublished, datasetWithPublishedAndNotVisible, datasetWithTwoPublishedVersions);

        // queries
        Query queryPublishedLinkedToDataset = createPublishedQueryLinkedToDataset("Q01", datasetSingleVersionPublished, datasetSingleVersionPublished.getVersions().get(0)
                .getSiemacMetadataStatisticalResource().getValidFrom());

        DatasetVersion datasetVersionLinkedToQuery = createDatasetVersionPublishedLastVersion(5, INIT_VERSION, new DateTime().minusDays(3));
        Query queryPublishedLinekdToDatasetVersion = QueryMockFactory.createPublishedQueryLinkedToDatasetVersion("Q02", datasetVersionLinkedToQuery, datasetVersionLinkedToQuery
                .getSiemacMetadataStatisticalResource().getValidFrom());

        List<Query> queries = Arrays.asList(queryPublishedLinkedToDataset, queryPublishedLinekdToDatasetVersion);

        PublicationVersionMockFactory.populatePublicationWithStructureWithCubesOnChapters(publicationVersion02, datasets, queries);

        getStatisticalResourcesPersistedDoMocks().mockPublicationVersion(publicationVersion02);
        prepareToPublished(publicationVersion02);
        registerPublicationVersionMock(PUBLICATION_VERSION_85_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION_EXTERNAL_ITEM_FULL_FOR_PUBLICATION_07_NAME, publicationVersion02);

        publicationVersion02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationVersion01));

        return publicationVersion02.getPublication();
    }

    // Public builders

    public static Publication createPublicationWithTwoVersionsPublished(int sequentialId) {
        PublicationMock publication = createPublicationToAddVersions(sequentialId);

        DateTime secondVersionPublishTime = new DateTime().minusDays(1);

        PublicationVersion publicationVersion01 = createPublicationVersionPublishedPreviousVersion(publication, INIT_VERSION, new DateTime().minusDays(2), secondVersionPublishTime);

        PublicationVersion publicationVersion02 = createPublicationVersionPublishedLastVersion(publication, SECOND_VERSION, secondVersionPublishTime);

        publicationVersion02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationVersion01));

        return publication;
    }

    public static Publication createPublicationWithTwoVersionsOnePublishedLastNotVisible(int sequentialId) {
        PublicationMock publication = createPublicationToAddVersions(sequentialId);

        DateTime secondVersionPublishTime = new DateTime().plusDays(1);

        PublicationVersion publicationVersion01 = createPublicationVersionPublishedPreviousVersion(publication, INIT_VERSION, new DateTime().minusDays(2), secondVersionPublishTime);

        PublicationVersion publicationVersion02 = createPublicationVersionPublishedLastVersion(publication, SECOND_VERSION, secondVersionPublishTime);

        publicationVersion02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationVersion01));

        return publication;
    }

    public static Publication createPublicationWithTwoVersionsOnePublishedLastDraft(int sequentialId) {
        PublicationMock publication = createPublicationToAddVersions(sequentialId);

        PublicationVersion publicationVersion01 = createPublicationVersionPublishedPreviousVersion(publication, INIT_VERSION, new DateTime().minusDays(2), null);

        PublicationVersion publicationVersion02 = createPublicationVersionLastVersionInStatus(publication, SECOND_VERSION, ProcStatusEnum.DRAFT);

        publicationVersion02.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationVersion01));

        return publication;
    }

    public static Publication createPublicationWithSingleVersionPublished(int sequentialId) {
        PublicationMock publication = createPublicationToAddVersions(sequentialId);

        PublicationVersion publicationVersion01 = createPublicationVersionPublishedLastVersion(publication, INIT_VERSION, new DateTime().minusDays(2));

        return publication;
    }

    public static Publication createPublicationWithSingleVersionPublishedNotVisible(int sequentialId) {
        PublicationMock publication = createPublicationToAddVersions(sequentialId);

        PublicationVersion publicationVersion01 = createPublicationVersionPublishedLastVersion(publication, INIT_VERSION, new DateTime().plusDays(2));

        return publication;
    }

    public static Publication createPublicationWithSingleVersionDraft(int sequentialId) {
        PublicationMock publication = createPublicationToAddVersions(sequentialId);

        PublicationVersion publicationVersion01 = createPublicationVersionLastVersionInStatus(publication, INIT_VERSION, ProcStatusEnum.DRAFT);

        return publication;
    }

    // INTERNAL BUILDERS

    private static PublicationVersion createPublicationVersionLastVersionInStatus(Publication publication, String version, ProcStatusEnum status) {
        PublicationVersionMock publicationVersionMock = createPublicationVersionSimple(publication, version, true);
        return createPublicationVersionInStatus(publicationVersionMock, status);
    }

    // Creations
    private static Publication createPublicationWithGeneratedPublicationVersions() {
        return getStatisticalResourcesPersistedDoMocks().mockPublicationWithGeneratedPublicationVersion();
    }

    public static PublicationMock createPublicationToAddVersions(Integer sequential) {
        PublicationMock publication = new PublicationMock();
        publication.setSequentialId(sequential);
        getStatisticalResourcesPersistedDoMocks().mockPublication(publication);
        return publication;
    }

}
