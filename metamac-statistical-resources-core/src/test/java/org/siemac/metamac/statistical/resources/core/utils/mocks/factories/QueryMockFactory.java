package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_56_DRAFT_WITH_DATASOURCE_AND_QUERIES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_63_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_11_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_64_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_11_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_65_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION__LINKED_TO_QUERY_11_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_66_PUBLISHED_MULTI_VERSION_V01__LINKED_TO_QUERY_11_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_68_DRAFT_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_69_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_70_PUBLISHED_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_71_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_DRAFT_LINKED_TO_QUERY_13_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_72_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_73_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_74_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_75_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_LAST_LINKED_TO_QUERY_13_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_76_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_77_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_78_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_79_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__ONLY_LAST_LINKED_TO_QUERY_13_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_80_PREVIOUS_VERSION_V01_IN_PUB_WITH_TWO_PUBLISHED__ONLY_PREVIOUS_LINKED_TO_QUERY_14_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_21_FOR_QUERY_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_22_FOR_QUERY_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_26_V3_PUBLISHED_FOR_QUERY_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_27_V1_PUBLISHED_FOR_QUERY_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockDescriptor;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockProvider;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetCube;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.Publication;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.DatasetLifecycleTestUtils;
import org.siemac.metamac.statistical.resources.core.utils.QueryLifecycleTestUtils;
import org.siemac.metamac.statistical.resources.core.utils.mocks.QueryMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.QueryVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesNotPersistedDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

@MockProvider
@SuppressWarnings("unused")
public class QueryMockFactory extends StatisticalResourcesMockFactory<Query> {

    public static final String      QUERY_01_SIMPLE_NAME                                                                    = "QUERY_01_SIMPLE";

    public static final String      QUERY_02_BASIC_WITH_GENERATED_VERSION_NAME                                              = "QUERY_02_BASIC_WITH_GENERATED_VERSION";

    public static final String      QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME                                               = "QUERY_03_WITH_2_QUERY_VERSIONS";

    public static final String      QUERY_04_FULL_FILLED_WITH_1_QUERY_VERSIONS_NAME                                         = "QUERY_04_FULL_FILLED_WITH_1_QUERY_VERSIONS_NAME";

    public static final String      QUERY_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME                                          = "QUERY_05_WITH_MULTIPLE_PUBLISHED_VERSIONS";

    public static final String      QUERY_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME                    = "QUERY_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE";

    public static final String      QUERY_07_SIMPLE_MULTI_VERSION_NAME                                                      = "QUERY_07_SIMPLE_MULTI_VERSION";

    public static final String      QUERY_08_SINGLE_VERSION_USED_IN_MULTIPLE_PUBLICATIONS_NAME                              = "QUERY_08_SINGLE_VERSION_USED_IN_MULTIPLE_PUBLICATIONS";

    public static final String      QUERY_09_C1_SINGLE_VERSION_USED_IN_PUB_VERSION_17_NAME                                  = "QUERY_09_C1_SINGLE_VERSION_USED_IN_PUB_17";

    public static final String      QUERY_09_SINGLE_VERSION_USED_IN_PUB_VERSION_17_NAME                                     = "QUERY_09_SINGLE_VERSION_USED_IN_PUB_17";

    public static final String      QUERY_10_SINGLE_VERSION_DRAFT_USED_IN_PUBLICATIONS_NAME                                 = "QUERY_10_SINGLE_VERSION_DRAFT_USED_IN_PUBLICATIONS";

    public static final String      QUERY_11_SINGLE_VERSION_NOT_VISIBLE_USED_IN_PUBLICATIONS_NAME                           = "QUERY_11_SINGLE_VERSION_NOT_VISIBLE_USED_IN_PUBLICATIONS";

    public static final String      QUERY_12_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_WITH_SINGLE_VERSIONS_NAME        = "QUERY_12_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_WITH_SINGLE_VERSIONS";

    public static final String      QUERY_13_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_LAST_VERSIONS_NAME     = "QUERY_13_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_LAST_VERSIONS";

    public static final String      QUERY_14_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_PREVIOUS_VERSIONS_NAME = "QUERY_14_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_PREVIOUS_VERSIONS";
    public static final String      QUERY_15_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_BOTH_VERSIONS_NAME          = "QUERY_15_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_BOTH_VERSIONS";

    public static final String      QUERY_16_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME                                      = "QUERY_16_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME";
    public static final String      QUERY_17_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME                      = "QUERY_17_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME";
    public static final String      QUERY_18_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME                       = "QUERY_18_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME";
    public static final String      QUERY_19_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME                        = "QUERY_19_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME";
    public static final String      QUERY_20_PUBLISHED_NOT_VISIBLE_USED_IN_PUBLICATION_VERSION_86_NAME                      = "QUERY_20_PUBLISHED_NOT_VISIBLE_USED_IN_PUBLICATION_VERSION_86_NAME";

    public static final String      QUERY_21_DRAFT_USED_IN_MULTIDATASET_VERSION_86_NAME                                     = "QUERY_21_DRAFT_USED_IN_MULTIDATASET_VERSION_86_NAME";
    public static final String      QUERY_22_PRODUCTION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME                     = "QUERY_22_PRODUCTION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME";
    public static final String      QUERY_23_DIFFUSION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME                      = "QUERY_23_DIFFUSION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME";
    public static final String      QUERY_24_VALIDATION_REJECTED_USED_IN_MULTIDATASET_VERSION_86_NAME                       = "QUERY_24_VALIDATION_REJECTED_USED_IN_MULTIDATASET_VERSION_86_NAME";
    public static final String      QUERY_25_PUBLISHED_NOT_VISIBLE_USED_IN_MULTIDATASET_VERSION_86_NAME                     = "QUERY_25_PUBLISHED_NOT_VISIBLE_USED_IN_MULTIDATASET_VERSION_86_NAME";

    private static QueryMockFactory instance                                                                                = null;

    private QueryMockFactory() {
    }

    public static QueryMockFactory getInstance() {
        if (instance == null) {
            instance = new QueryMockFactory();
        }
        return instance;
    }

    private static Query getQuery01Simple() {
        return createQueryWithGeneratedVersion();
    }

    private static Query getQuery02BasicWithGeneratedVersion() {
        Query query = createQueryWithGeneratedVersion();
        query.getVersions().get(0).getLifeCycleStatisticalResource().setLastVersion(Boolean.TRUE);
        return query;
    }

    private static Query getQuery03With2QueryVersions() {
        Query query = generateQueryWithoutGeneratedVersion();

        Dataset dataset = DatasetMockFactory.createPublishedAndDraftVersionsForDataset(1);

        DatasetVersion publishedDatasetVersion = dataset.getVersions().get(0);
        DatasetVersion draftDatasetVersion = dataset.getVersions().get(1);

        QueryVersion queryV01 = mockQuery03V01Published(query, dataset, publishedDatasetVersion);
        registerQueryVersionMock(QUERY_VERSION_21_FOR_QUERY_03_NAME, queryV01);

        QueryVersion queryV02 = mockQuery03V02(query, draftDatasetVersion);
        registerQueryVersionMock(QUERY_VERSION_22_FOR_QUERY_03_AND_LAST_VERSION_NAME, queryV02);

        queryV02.getLifeCycleStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockQueryVersionRelated(queryV01));
        return query;
    }

    private static QueryVersion mockQuery03V01Published(Query query, Dataset dataset, DatasetVersion lastPublishedVersion) {
        QueryVersionMock mock = buildSimpleMock(query, dataset, INIT_VERSION);

        // not last version
        mock.getLifeCycleStatisticalResource().setCreationDate(new DateTime().minusDays(2));
        mock.getLifeCycleStatisticalResource().setValidFrom(lastPublishedVersion.getSiemacMetadataStatisticalResource().getValidFrom());
        mock.getLifeCycleStatisticalResource().setLastVersion(Boolean.FALSE);

        QueryVersion queryVersion = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(mock);
        QueryLifecycleTestUtils.fillAsPublished(queryVersion);
        return queryVersion;
    }

    private static QueryVersion mockQuery03V02(Query query, DatasetVersion datasetVersion) {
        QueryVersionMock mock = buildSimpleMock(query, datasetVersion, SECOND_VERSION);

        // Is last version
        mock.getLifeCycleStatisticalResource().setCreationDate(new DateTime().minusDays(1));
        mock.getLifeCycleStatisticalResource().setLastVersion(Boolean.TRUE);

        return getStatisticalResourcesPersistedDoMocks().mockQueryVersion(mock);
    }

    private static Query getQuery04With1QueryVersions() {
        Query query = generateQueryWithoutGeneratedVersion();
        QueryVersion queryVersion = mockQuery04V01(query, getStatisticalResourcesPersistedDoMocks().mockDatasetVersion());
        return query;
    }

    private static QueryVersion mockQuery04V01(Query query, DatasetVersion datasetVersion) {
        QueryVersionMock mock = buildSimpleMock(query, datasetVersion, INIT_VERSION);
        mock.getLifeCycleStatisticalResource().setCreatedBy(StatisticalResourcesDoMocks.mockString(10));
        mock.getLifeCycleStatisticalResource().setCreatedDate(StatisticalResourcesDoMocks.mockDateTime());
        mock.getLifeCycleStatisticalResource().setLastUpdatedBy(StatisticalResourcesDoMocks.mockString(10));
        mock.getLifeCycleStatisticalResource().setLastUpdated(StatisticalResourcesDoMocks.mockDateTime());
        return getStatisticalResourcesPersistedDoMocks().mockQueryVersion(mock);
    }

    private static Query getQuery05WithMultiplePublishedVersions() {
        Query query = generateQueryWithoutGeneratedVersion();

        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion();
        datasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(1));
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(datasetVersion);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        Dataset dataset = datasetVersion.getDataset();
        DateTime validFrom = datasetVersion.getSiemacMetadataStatisticalResource().getValidFrom();

        QueryVersion queryV01 = mockQuery05V01Published(query, dataset, validFrom);
        QueryVersion queryV02 = mockQuery05V02Published(query, dataset, validFrom.plusMinutes(2));
        QueryVersion queryV03 = mockQuery05V03Published(query, dataset, validFrom.plusMinutes(4));
        registerQueryVersionMock(QUERY_VERSION_26_V3_PUBLISHED_FOR_QUERY_05_NAME, queryV03);

        queryV02.getLifeCycleStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockQueryVersionRelated(queryV01));
        queryV03.getLifeCycleStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockQueryVersionRelated(queryV02));

        return query;
    }

    private static QueryVersion mockQuery05V01Published(Query query, Dataset dataset, DateTime validFrom) {
        QueryVersionMock mock = buildPublishedQueryVersionMockLinkedToDataset(query, dataset, INIT_VERSION, validFrom);
        mock.getLifeCycleStatisticalResource().setValidTo(validFrom.plusMinutes(2));
        mock.getLifeCycleStatisticalResource().setLastVersion(false);

        QueryVersion queryVersion = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(mock);
        QueryLifecycleTestUtils.prepareToVersioning(queryVersion);
        return queryVersion;
    }

    private static QueryVersion mockQuery05V02Published(Query query, Dataset dataset, DateTime validFrom) {
        QueryVersionMock mock = buildPublishedQueryVersionMockLinkedToDataset(query, dataset, SECOND_VERSION, validFrom);
        mock.getLifeCycleStatisticalResource().setValidTo(validFrom.plusMinutes(2));
        mock.getLifeCycleStatisticalResource().setLastVersion(false);

        QueryVersion queryVersion = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(mock);
        QueryLifecycleTestUtils.prepareToVersioning(queryVersion);
        return queryVersion;
    }

    private static QueryVersion mockQuery05V03Published(Query query, Dataset dataset, DateTime validFrom) {
        QueryVersionMock mock = buildPublishedQueryVersionMockLinkedToDataset(query, dataset, THIRD_VERSION, validFrom);
        mock.getLifeCycleStatisticalResource().setLastVersion(true);

        QueryVersion queryVersion = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(mock);
        QueryLifecycleTestUtils.prepareToVersioning(queryVersion);
        return queryVersion;
    }

    private static Query getQuery06WithMultiplePublishedVersionsAndLatestNoVisible() {
        Query query = generateQueryWithoutGeneratedVersion();

        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion();
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(datasetVersion);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        QueryVersion queryV01 = mockQuery06V01(query, datasetVersion.getDataset(), datasetVersion.getSiemacMetadataStatisticalResource().getValidFrom());
        registerQueryVersionMock(QUERY_VERSION_27_V1_PUBLISHED_FOR_QUERY_06_NAME, queryV01);

        QueryVersion queryV02 = mockQuery06V02(query, datasetVersion.getDataset(), datasetVersion.getSiemacMetadataStatisticalResource().getValidFrom().plusDays(2));
        registerQueryVersionMock(QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME, queryV02);

        queryV02.getLifeCycleStatisticalResource().setReplacesVersion(StatisticalResourcesNotPersistedDoMocks.mockRelatedResourceLinkedToQueryVersion(queryV01));

        return query;
    }

    private static QueryVersion mockQuery06V01(Query query, Dataset dataset, DateTime validFrom) {
        QueryVersionMock mock = buildPublishedQueryVersionMockLinkedToDataset(query, dataset, INIT_VERSION, new DateTime().minusDays(1));
        // last version
        mock.getLifeCycleStatisticalResource().setLastUpdated(new DateTime().minusHours(22));
        mock.getLifeCycleStatisticalResource().setValidFrom(validFrom);
        mock.getLifeCycleStatisticalResource().setLastVersion(false);

        QueryVersion queryVersion = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(mock);
        QueryLifecycleTestUtils.fillAsPublished(queryVersion);
        return queryVersion;
    }

    private static QueryVersion mockQuery06V02(Query query, Dataset dataset, DateTime validFrom) {
        QueryVersionMock mock = buildPublishedQueryVersionMockLinkedToDataset(query, dataset, SECOND_VERSION, new DateTime().plusDays(1));
        // last version
        mock.getLifeCycleStatisticalResource().setLastUpdated(new DateTime().minusHours(1));
        mock.getLifeCycleStatisticalResource().setValidFrom(validFrom);
        mock.getLifeCycleStatisticalResource().setLastVersion(true);

        QueryVersion queryVersion = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(mock);
        QueryLifecycleTestUtils.prepareToVersioning(queryVersion);
        return queryVersion;
    }

    private static Query getQuery07SimpleMultiVersion() {
        Query query = generateQueryWithoutGeneratedVersion();

        DatasetVersion datasetVersion = DatasetVersionMockFactory.createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DRAFT);
        registerDatasetVersionMock(DATASET_VERSION_56_DRAFT_WITH_DATASOURCE_AND_QUERIES_NAME, datasetVersion);

        QueryVersion queryV01 = mockQueryVersionSimple(query, datasetVersion, INIT_VERSION);
        QueryVersion queryV02 = mockQueryVersionSimple(query, datasetVersion, SECOND_VERSION);

        queryV02.getLifeCycleStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockQueryVersionRelated(queryV01));

        return query;
    }

    private static QueryVersion mockQueryVersionSimple(Query query, DatasetVersion datasetVersion, String version) {
        QueryVersionMock mock = buildSimpleMock(query, datasetVersion, version);
        return getStatisticalResourcesPersistedDoMocks().mockQueryVersion(mock);
    }

    private static Query getQuery08SingleVersionUsedInMultiplePublications() {
        Query query = generateQueryWithoutGeneratedVersion();

        DatasetVersion datasetVersion = generateDatasetVersionWithCoverages();

        QueryVersion queryV01 = mockQueryVersionSimple(query, datasetVersion, INIT_VERSION);
        QueryVersion queryV02 = mockQueryVersionSimple(query, datasetVersion, SECOND_VERSION);

        queryV02.getLifeCycleStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockQueryVersionRelated(queryV01));

        return query;
    }

    public static MockDescriptor getQuery10SingleVersionDraftUsedInPublications() {
        Query query = generateQueryWithoutGeneratedVersion();

        DatasetVersion datasetVersion = generateDatasetVersionWithCoverages();

        QueryVersion queryV01 = mockQueryVersionSimple(query, datasetVersion, INIT_VERSION);

        Publication publication01 = createPublicationWithTwoVersionsOnePublishedLastDraftLinkedToQueries(1, null, query);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_61_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_10_NAME, publication01.getVersions().get(1));

        Publication publication02 = createPublicationWithSingleVersionDraftLinkedToQuery(1, query);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_62_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10_NAME, publication02.getVersions().get(0));

        Multidataset multidataset01 = createMultidatasetWithSingleVersionDraftLinkedToQuery(1, query);
        registerMultidatasetVersionMock(MultidatasetVersionMockFactory.MULTIDATASET_VERSION_99_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10_NAME, multidataset01.getVersions().get(0));

        return new MockDescriptor(query, publication01, publication02, multidataset01);
    }

    public static MockDescriptor getQuery11SingleVersionNotVisibleUsedInPublications() {
        Query query = generateQueryWithoutGeneratedVersion();

        DatasetVersion datasetVersion = generateDatasetVersionWithCoverages();
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        QueryVersionMock queryVersionMock = buildSimpleMock(query, datasetVersion, INIT_VERSION);
        queryVersionMock.getLifeCycleStatisticalResource().setValidFrom(new DateTime().plusHours(1));
        QueryVersion queryV01 = createQueryVersionInStatus(queryVersionMock, ProcStatusEnum.PUBLISHED);

        Publication publication01 = createPublicationWithTwoVersionsOnePublishedLastDraftLinkedToQueries(1, null, query);
        registerPublicationVersionMock(PUBLICATION_VERSION_63_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_11_NAME, publication01.getVersions().get(1));

        Publication publication02 = createPublicationWithSingleVersionDraftLinkedToQuery(2, query);
        registerPublicationVersionMock(PUBLICATION_VERSION_64_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_11_NAME, publication02.getVersions().get(0));

        Publication publication03 = createPublicationWithSingleVersionPublishedNotVisibleLinkedToQuery(3, query);
        registerPublicationVersionMock(PUBLICATION_VERSION_65_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION__LINKED_TO_QUERY_11_NAME, publication03.getVersions().get(0));

        Publication publication04 = createPublicationWithTwoVersionsOnePublishedLastNotVisibleLinkedToQueries(3, null, query);
        registerPublicationVersionMock(PUBLICATION_VERSION_66_PUBLISHED_MULTI_VERSION_V01__LINKED_TO_QUERY_11_NAME, publication04.getVersions().get(1));

        return new MockDescriptor(query, publication01, publication02, publication03, publication04);
    }

    public static MockDescriptor getQuery12SingleVersionPublishedUsedInPublicationsWithSingleVersions() {
        Query query = generateQueryWithoutGeneratedVersion();

        DatasetVersion datasetVersion = generateDatasetVersionWithCoverages();
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        QueryVersionMock queryVersionMock = buildSimpleMock(query, datasetVersion, INIT_VERSION);
        QueryVersion queryV01 = createQueryVersionInStatus(queryVersionMock, ProcStatusEnum.PUBLISHED);

        // Single versions
        Publication publicationSingleDraft = createPublicationWithSingleVersionDraftLinkedToQuery(1, query);
        registerPublicationVersionMock(PUBLICATION_VERSION_68_DRAFT_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME, publicationSingleDraft.getVersions().get(0));

        Publication publicationSinglePublishedNotVisible = createPublicationWithSingleVersionPublishedNotVisibleLinkedToQuery(2, query);
        registerPublicationVersionMock(PUBLICATION_VERSION_69_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME, publicationSinglePublishedNotVisible.getVersions().get(0));

        Publication publicationSinglePublished = createPublicationWithSingleVersionPublishedLinkedToQuery(3, query);
        registerPublicationVersionMock(PUBLICATION_VERSION_70_PUBLISHED_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME, publicationSinglePublished.getVersions().get(0));

        return new MockDescriptor(query, publicationSingleDraft, publicationSinglePublishedNotVisible, publicationSinglePublished);
    }

    public static MockDescriptor getQuery13SingleVersionPublishedUsedInPublicationsLinkOnlyLastVersions() {
        Query query = generateQueryWithoutGeneratedVersion();

        DatasetVersion datasetVersion = generateDatasetVersionWithCoverages();
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        QueryVersionMock queryVersionMock = buildSimpleMock(query, datasetVersion, INIT_VERSION);
        QueryVersion queryV01 = createQueryVersionInStatus(queryVersionMock, ProcStatusEnum.PUBLISHED);

        // PUBLISHED + DRAFT
        Publication publicationMultiDraft = createPublicationWithTwoVersionsOnePublishedLastDraftLinkedToQueries(1, null, query);
        registerPublicationVersionMock(PUBLICATION_VERSION_71_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_DRAFT_LINKED_TO_QUERY_13_NAME, publicationMultiDraft.getVersions().get(1));

        // PUBLISHED + NOT VISIBLE
        Publication publicationMultiNotVisible = createPublicationWithTwoVersionsOnePublishedLastNotVisibleLinkedToQueries(7, null, query);
        registerPublicationVersionMock(PUBLICATION_VERSION_75_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_LAST_LINKED_TO_QUERY_13_NAME,
                publicationMultiNotVisible.getVersions().get(1));

        // PUBLISHED + PUBLISHED
        Publication publicationMultiLastPublished = createPublicationWithTwoVersionsPublishedLinkedToQueries(10, null, query);
        registerPublicationVersionMock(PUBLICATION_VERSION_79_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__ONLY_LAST_LINKED_TO_QUERY_13_NAME, publicationMultiLastPublished.getVersions().get(1));

        return new MockDescriptor(query, publicationMultiDraft, publicationMultiNotVisible, publicationMultiLastPublished);
    }

    public static MockDescriptor getQuery14SingleVersionPublishedUsedInPublicationsLinkOnlyPreviousVersions() {
        Query query = generateQueryWithoutGeneratedVersion();

        DatasetVersion datasetVersion = generateDatasetVersionWithCoverages();
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        QueryVersionMock queryVersionMock = buildSimpleMock(query, datasetVersion, INIT_VERSION);
        QueryVersion queryV01 = createQueryVersionInStatus(queryVersionMock, ProcStatusEnum.PUBLISHED);

        // PUBLISHED + DRAFT
        Publication publication01 = createPublicationWithTwoVersionsOnePublishedLastDraftLinkedToQueries(5, query, null);
        registerPublicationVersionMock(PUBLICATION_VERSION_72_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME, publication01.getVersions().get(0));

        // PUBLISHED + NOT VISIBLE
        Publication publication02 = createPublicationWithTwoVersionsOnePublishedLastNotVisibleLinkedToQueries(8, query, null);
        registerPublicationVersionMock(PUBLICATION_VERSION_76_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME, publication02.getVersions().get(0));

        // PUBLISHED + PUBLISHED
        Publication publication03 = createPublicationWithTwoVersionsPublishedLinkedToQueries(10, query, null);
        registerPublicationVersionMock(PUBLICATION_VERSION_80_PREVIOUS_VERSION_V01_IN_PUB_WITH_TWO_PUBLISHED__ONLY_PREVIOUS_LINKED_TO_QUERY_14_NAME, publication03.getVersions().get(0));

        return new MockDescriptor(query, publication01, publication02, publication03);
    }

    public static MockDescriptor getQuery15SingleVersionPublishedUsedInPublicationsLinkBothVersions() {
        Query query = generateQueryWithoutGeneratedVersion();

        DatasetVersion datasetVersion = generateDatasetVersionWithCoverages();
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        QueryVersionMock queryVersionMock = buildSimpleMock(query, datasetVersion, INIT_VERSION);
        QueryVersion queryV01 = createQueryVersionInStatus(queryVersionMock, ProcStatusEnum.PUBLISHED);

        // PUBLISHED + DRAFT
        Publication publicationLastDraft = createPublicationWithTwoVersionsOnePublishedLastDraftLinkedToQueries(6, query, query);
        registerPublicationVersionMock(PUBLICATION_VERSION_73_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15_NAME, publicationLastDraft.getVersions().get(0));
        registerPublicationVersionMock(PUBLICATION_VERSION_74_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15_NAME, publicationLastDraft.getVersions().get(1));

        // PUBLISHED + NOT VISIBLE
        Publication publicationLastNotVisible = createPublicationWithTwoVersionsOnePublishedLastNotVisibleLinkedToQueries(9, query, query);
        registerPublicationVersionMock(PUBLICATION_VERSION_77_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15_NAME, publicationLastNotVisible.getVersions().get(0));
        registerPublicationVersionMock(PUBLICATION_VERSION_78_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15_NAME, publicationLastNotVisible.getVersions().get(1));

        // PUBLISHED + PUBLISHED

        Publication publicationLastPublished = createPublicationWithTwoVersionsPublishedLinkedToQueries(12, query, query);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_81_PREVIOUS_VERSION_V01_IN_PUB_WITH_TWO_PUBLISHED__BOTH_LINKED_TO_QUERY_15_NAME,
                publicationLastPublished.getVersions().get(0));
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_82_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__BOTH_LINKED_TO_QUERY_15_NAME,
                publicationLastPublished.getVersions().get(1));
        return new MockDescriptor(query, publicationLastDraft, publicationLastNotVisible, publicationLastPublished);
    }

    // -----------------------------------------------------------------
    // PRIVATE UTILS
    // -----------------------------------------------------------------

    private static Publication createPublicationWithTwoVersionsPublishedLinkedToQueries(int sequentialId, Query queryForV01, Query queryForV02) {
        Publication pub = PublicationMockFactory.createPublicationWithTwoVersionsPublished(sequentialId);
        if (queryForV01 != null) {
            linkQueryInPublicationVersionFirstLevel(queryForV01, pub.getVersions().get(0));
        }
        if (queryForV02 != null) {
            linkQueryInPublicationVersionFirstLevel(queryForV02, pub.getVersions().get(1));
        }
        return pub;
    }

    private static Publication createPublicationWithTwoVersionsOnePublishedLastDraftLinkedToQueries(int sequentialId, Query queryForV01, Query queryForV02) {
        Publication pub = PublicationMockFactory.createPublicationWithTwoVersionsOnePublishedLastDraft(sequentialId);
        if (queryForV01 != null) {
            linkQueryInPublicationVersionFirstLevel(queryForV01, pub.getVersions().get(0));
        }
        if (queryForV02 != null) {
            linkQueryInPublicationVersionFirstLevel(queryForV02, pub.getVersions().get(1));
        }
        return pub;
    }

    private static Publication createPublicationWithTwoVersionsOnePublishedLastNotVisibleLinkedToQueries(int sequentialId, Query queryForV01, Query queryForV02) {
        Publication pub = PublicationMockFactory.createPublicationWithTwoVersionsOnePublishedLastNotVisible(sequentialId);
        if (queryForV01 != null) {
            linkQueryInPublicationVersionFirstLevel(queryForV01, pub.getVersions().get(0));
        }
        if (queryForV02 != null) {
            linkQueryInPublicationVersionFirstLevel(queryForV02, pub.getVersions().get(1));
        }
        return pub;
    }

    private static Publication createPublicationWithSingleVersionDraftLinkedToQuery(int sequentialId, Query query) {
        Publication pub = PublicationMockFactory.createPublicationWithSingleVersionDraft(sequentialId);
        if (query != null) {
            linkQueryInPublicationVersionFirstLevel(query, pub.getVersions().get(0));
        }
        return pub;
    }

    private static Multidataset createMultidatasetWithSingleVersionDraftLinkedToQuery(int sequentialId, Query query) {
        Multidataset mul = MultidatasetMockFactory.createMultidatasetWithSingleVersionDraft(sequentialId);
        if (query != null) {
            linkQueryInMultidatasetVersion(query, mul.getVersions().get(0));
        }
        return mul;
    }

    private static Publication createPublicationWithSingleVersionPublishedNotVisibleLinkedToQuery(int sequentialId, Query query) {
        Publication pub = PublicationMockFactory.createPublicationWithSingleVersionPublishedNotVisible(sequentialId);
        if (query != null) {
            linkQueryInPublicationVersionFirstLevel(query, pub.getVersions().get(0));
        }
        return pub;
    }

    private static Publication createPublicationWithSingleVersionPublishedLinkedToQuery(int sequentialId, Query query) {
        Publication pub = PublicationMockFactory.createPublicationWithSingleVersionPublished(sequentialId);
        if (query != null) {
            linkQueryInPublicationVersionFirstLevel(query, pub.getVersions().get(0));
        }
        return pub;
    }

    private static void linkQueryInPublicationVersionFirstLevel(Query query, PublicationVersion publicationVersion) {
        ElementLevel elementLevelV01 = PublicationVersionMockFactory.createQueryCubeElementLevel(publicationVersion, query);
        elementLevelV01.setOrderInLevel(Long.valueOf(publicationVersion.getChildrenFirstLevel().size() + 1));
    }

    private static void linkQueryInMultidatasetVersion(Query query, MultidatasetVersion multidatasetVersion) {
        MultidatasetCube cube = MultidatasetVersionMockFactory.createQueryMultidatasetCube(multidatasetVersion, query);
        cube.setOrderInMultidataset(Long.valueOf(multidatasetVersion.getCubes().size() + 1));
    }

    private static DatasetVersion generateDatasetVersionWithCoverages() {
        DatasetVersion datasetVersion = DatasetMockFactory.generateDatasetWithGeneratedVersion().getVersions().get(0);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(datasetVersion);
        return datasetVersion;
    }

    // -----------------------------------------------------------------
    // BUILDERS
    // -----------------------------------------------------------------

    private static QueryVersion createQueryVersionInStatus(QueryVersionMock queryVersionMock, ProcStatusEnum status) {
        QueryVersion queryVersion = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(queryVersionMock);

        switch (status) {
            case PRODUCTION_VALIDATION:
                QueryLifecycleTestUtils.fillAsProductionValidation(queryVersion);
                break;
            case DIFFUSION_VALIDATION:
                QueryLifecycleTestUtils.fillAsDiffusionValidation(queryVersion);
                break;
            case VALIDATION_REJECTED:
                QueryLifecycleTestUtils.fillAsValidationRejected(queryVersion);
                break;
            case PUBLISHED:
                QueryLifecycleTestUtils.fillAsPublished(queryVersion);
                break;
            case DRAFT:
                break;
            default:
                throw new IllegalArgumentException("Unsupported status " + status);
        }
        return queryVersion;
    }

    public static String generateUrl(boolean isHttps, String element) {
        String url = "";
        if (StringUtils.isEmpty(element)) {
            url = "pruebas.com";
        } else {
            url = element + " - pruebas.com";
        }
        return isHttps ? "https://www." + url : "http://www." + url;
    }

    public static Query generateQueryWithGeneratedVersion() {
        return getStatisticalResourcesPersistedDoMocks().mockQueryWithGeneratedQueryVersion();
    }

    private static Query createQueryWithGeneratedVersion() {
        return getStatisticalResourcesPersistedDoMocks().mockQueryWithGeneratedQueryVersion();
    }

    public static Query generateQueryWithoutGeneratedVersion() {
        return getStatisticalResourcesPersistedDoMocks().mockQuery(new QueryMock());
    }

    private static QueryVersionMock buildSimpleMock(String identifier, DatasetVersion datasetVersion, String versionLogic) {
        QueryMock query = new QueryMock();
        query.getIdentifiableStatisticalResource().setCode(identifier);
        return buildSimpleMock(query, datasetVersion, versionLogic);
    }

    private static QueryVersionMock buildSimpleMock(Query query, DatasetVersion datasetVersion, String versionLogic) {
        QueryVersionMock mock = new QueryVersionMock();
        mock.setQuery(query);
        mock.setFixedDatasetVersion(datasetVersion);
        mock.setVersionLogic(versionLogic);
        mock.setStatus(QueryStatusEnum.DISCONTINUED);
        return mock;
    }

    private static QueryVersionMock buildSimpleMock(Query query, Dataset dataset, String versionLogic) {
        QueryVersionMock mock = new QueryVersionMock();
        mock.setQuery(query);
        mock.setDataset(dataset);
        mock.setVersionLogic(versionLogic);
        mock.setStatus(QueryStatusEnum.ACTIVE);
        return mock;
    }

    public static Query createPublishedQueryLinkedToDatasetVersion(String code, DatasetVersion datasetVersion, DateTime validFrom) {
        QueryMock query = new QueryMock();
        query.getIdentifiableStatisticalResource().setCode(code);
        QueryVersionMock queryVersion = buildPublishedQueryVersionMockLinkedToDatasetVersion(query, datasetVersion, INIT_VERSION, validFrom);
        getStatisticalResourcesPersistedDoMocks().mockQueryVersion(queryVersion);
        QueryLifecycleTestUtils.fillAsPublished(queryVersion);
        return queryVersion.getQuery();
    }

    private static QueryVersionMock buildPublishedQueryVersionMockLinkedToDatasetVersion(Query query, DatasetVersion datasetVersion, String versionLogic, DateTime validFrom) {
        QueryVersionMock mock = new QueryVersionMock();
        mock.setQuery(query);
        mock.setFixedDatasetVersion(datasetVersion);
        mock.setStatus(QueryStatusEnum.DISCONTINUED);
        mock.setVersionLogic(versionLogic);
        if (validFrom.isAfterNow()) {
            mock.getLifeCycleStatisticalResource().setCreationDate(new DateTime());
        } else {
            mock.getLifeCycleStatisticalResource().setCreationDate(validFrom.minusHours(1));
        }
        mock.getLifeCycleStatisticalResource().setValidFrom(validFrom);
        return mock;
    }

    public static Query createPublishedQueryLinkedToDataset(String code, Dataset dataset, DateTime validFrom) {
        QueryMock query = new QueryMock();
        query.getIdentifiableStatisticalResource().setCode(code);
        QueryVersionMock queryVersion = buildPublishedQueryVersionMockLinkedToDataset(query, dataset, INIT_VERSION, validFrom);
        getStatisticalResourcesPersistedDoMocks().mockQueryVersion(queryVersion);
        QueryLifecycleTestUtils.fillAsPublished(queryVersion);
        return queryVersion.getQuery();
    }

    private static QueryVersionMock buildPublishedQueryVersionMockLinkedToDataset(Query query, Dataset dataset, String versionLogic, DateTime validFrom) {
        QueryVersionMock mock = new QueryVersionMock();
        mock.setQuery(query);
        mock.setDataset(dataset);
        mock.setStatus(QueryStatusEnum.ACTIVE);
        mock.setVersionLogic(versionLogic);
        if (validFrom.isAfterNow()) {
            mock.getLifeCycleStatisticalResource().setCreationDate(new DateTime());
        } else {
            mock.getLifeCycleStatisticalResource().setCreationDate(validFrom.minusHours(1));
        }
        mock.getLifeCycleStatisticalResource().setValidFrom(validFrom);
        return mock;
    }

}