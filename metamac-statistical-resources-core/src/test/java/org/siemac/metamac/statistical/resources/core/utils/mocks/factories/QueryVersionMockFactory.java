package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_03_FOR_DATASET_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockDescriptor;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockProvider;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.DatasetLifecycleTestUtils;
import org.siemac.metamac.statistical.resources.core.utils.QueryLifecycleTestUtils;
import org.siemac.metamac.statistical.resources.core.utils.mocks.DatasetMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.DatasetVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.QueryVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesNotPersistedDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

@MockProvider
@SuppressWarnings("unused")
public class QueryVersionMockFactory extends StatisticalResourcesMockFactory<QueryVersion> {

    public static final String             QUERY_VERSION_01_WITH_SELECTION_NAME                                                                                       = "QUERY_VERSION_01_WITH_SELECTION";

    public static final String             QUERY_VERSION_02_BASIC_ORDERED_01_NAME                                                                                     = "QUERY_VERSION_02_BASIC_ORDERED_01";

    public static final String             QUERY_VERSION_03_BASIC_ORDERED_02_NAME                                                                                     = "QUERY_VERSION_03_BASIC_ORDERED_02";

    public static final String             QUERY_VERSION_04_BASIC_ORDERED_03_NAME                                                                                     = "QUERY_VERSION_04_BASIC_ORDERED_03";

    public static final String             QUERY_VERSION_05_BASIC_NAME                                                                                                = "QUERY_VERSION_05_BASIC";

    public static final String             QUERY_VERSION_06_BASIC_ACTIVE_NAME                                                                                         = "QUERY_VERSION_06_BASIC_ACTIVE";

    public static final String             QUERY_VERSION_07_BASIC_ACTIVE_NAME                                                                                         = "QUERY_VERSION_07_BASIC_ACTIVE";

    public static final String             QUERY_VERSION_08_BASIC_DISCONTINUED_NAME                                                                                   = "QUERY_VERSION_08_BASIC_DISCONTINUED";

    public static final String             QUERY_VERSION_10_ACTIVE_LATEST_DATA_5_NAME                                                                                 = "QUERY_VERSION_10_ACTIVE_LATEST_DATA_5";

    public static final String             QUERY_VERSION_11_DRAFT_NAME                                                                                                = "QUERY_VERSION_11_DRAFT";

    public static final String             QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME                                                                                = "QUERY_VERSION_12_PRODUCTION_VALIDATION";

    public static final String             QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME                                                                                 = "QUERY_VERSION_13_DIFFUSION_VALIDATION";

    public static final String             QUERY_VERSION_14_VALIDATION_REJECTED_NAME                                                                                  = "QUERY_VERSION_14_VALIDATION_REJECTED";

    public static final String             QUERY_VERSION_15_PUBLISHED_NAME                                                                                            = "QUERY_VERSION_15_PUBLISHED";

    public static final String             QUERY_VERSION_19_WITH_CODE_AND_URN_QUERY01_NAME                                                                            = "QUERY_VERSION_19_WITH_CODE_AND_URN_QUERY01";

    public static final String             QUERY_VERSION_20_WITH_CODE_AND_URN_QUERY02_NAME                                                                            = "QUERY_VERSION_20_WITH_CODE_AND_URN_QUERY02";

    public static final String             QUERY_VERSION_21_FOR_QUERY_03_NAME                                                                                         = "QUERY_VERSION_21_FOR_QUERY_03";

    public static final String             QUERY_VERSION_22_FOR_QUERY_03_AND_LAST_VERSION_NAME                                                                        = "QUERY_VERSION_22_FOR_QUERY_03_AND_LAST_VERSION";

    public static final String             QUERY_VERSION_26_V3_PUBLISHED_FOR_QUERY_05_NAME                                                                            = "QUERY_VERSION_26_V3_PUBLISHED_FOR_QUERY_05";

    public static final String             QUERY_VERSION_27_V1_PUBLISHED_FOR_QUERY_06_NAME                                                                            = "QUERY_VERSION_27_V1_PUBLISHED_FOR_QUERY_06";

    public static final String             QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME                                                                 = "QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06";

    public static final String             QUERY_VERSION_29_CHECK_COMPAT_DATASET_86_OK_NAME                                                                           = "QUERY_VERSION_29_CHECK_COMPAT_DATASET_86_OK";

    public static final String             QUERY_VERSION_30_CHECK_COMPAT_DATASET_86_LESS_DIMENSIONS_NAME                                                              = "QUERY_VERSION_30_CHECK_COMPAT_DATASET_86_LESS_DIMENSIONS";

    public static final String             QUERY_VERSION_31_CHECK_COMPAT_DATASET_86_MORE_DIMENSIONS_NAME                                                              = "QUERY_VERSION_31_CHECK_COMPAT_DATASET_86_MORE_DIMENSIONS";

    public static final String             QUERY_VERSION_32_CHECK_COMPAT_DATASET_86_MORE_CODES_NAME                                                                   = "QUERY_VERSION_32_CHECK_COMPAT_DATASET_86_MORE_CODES";

    public static final String             QUERY_VERSION_34_CHECK_COMPAT_DATASET_87_INVALID_QUERY_TYPE_AUTOINC_NAME                                                   = "QUERY_VERSION_34_CHECK_COMPAT_DATASET_87_INVALID_QUERY_TYPE_AUTOINC";

    public static final String             QUERY_VERSION_35_CHECK_COMPAT_DATASET_87_INVALID_QUERY_TYPE_LATEST_DATA_NAME                                               = "QUERY_VERSION_35_CHECK_COMPAT_DATASET_87_INVALID_QUERY_TYPE_LATEST_DATA";

    public static final String             QUERY_VERSION_36_LINKED_TO_DATASET_NAME                                                                                    = "QUERY_VERSION_36_LINKED_TO_DATASET";

    public static final String             QUERY_VERSION_37_PREPARED_TO_PUBLISH_NAME                                                                                  = "QUERY_VERSION_37_PREPARED_TO_PUBLISH";

    public static final String             QUERY_VERSION_38_TO_PUBLISH_WITH_FUTURE_DATE_WITH_DATASET_VERSION_VISIBLE_BEFORE_NAME                                      = "QUERY_VERSION_38_TO_PUBLISH_WITH_FUTURE_DATE_WITH_DATASET_VERSION_VISIBLE_BEFORE";
    public static final String             QUERY_VERSION_39_TO_PUBLISH_WITH_FUTURE_DATE_WITH_DATASET_VERSION_VISIBLE_AFTER_NAME                                       = "QUERY_VERSION_39_TO_PUBLISH_WITH_FUTURE_DATE_WITH_DATASET_VERSION_VISIBLE_AFTER";
    public static final String             QUERY_VERSION_40_TO_PUBLISH_WITH_DATASET_VERSION_NOT_PUBLISHED_NAME                                                        = "QUERY_VERSION_40_TO_PUBLISH_WITH_DATASET_VERSION_NOT_PUBLISHED";
    public static final String             QUERY_VERSION_41_TO_PUBLISH_WITH_DATASET_WITH_NO_PUBLISHED_VERSION_NAME                                                    = "QUERY_VERSION_41_TO_PUBLISH_WITH_DATASET_WITH_NO_PUBLISHED_VERSION";
    public static final String             QUERY_VERSION_42_TO_PUBLISH_WITH_DATASET_WITH_LAST_VERSION_NOT_PUBLISHED_PREVIOUS_COMPATIBLE_NAME                          = "QUERY_VERSION_42_TO_PUBLISH_WITH_DATASET_WITH_LAST_VERSION_NOT_PUBLISHED_PREVIOUS_COMPATIBLE";
    public static final String             QUERY_VERSION_43_TO_PUBLISH_WITH_DATASET_WITH_LAST_VERSION_NOT_PUBLISHED_PREVIOUS_INCOMPATIBLE_NAME                        = "QUERY_VERSION_43_TO_PUBLISH_WITH_DATASET_WITH_LAST_VERSION_NOT_PUBLISHED_PREVIOUS_INCOMPATIBLE";
    public static final String             QUERY_VERSION_44_TO_PUBLISH_WITH_FUTURE_DATE_WITH_DATASET_WITH_LAST_VERSION_VISIBLE_BEFORE_QUERY_NAME                      = "QUERY_VERSION_44_TO_PUBLISH_WITH_FUTURE_DATE_WITH_DATASET_WITH_LAST_VERSION_VISIBLE_BEFORE_QUERY";
    public static final String             QUERY_VERSION_45_TO_PUBLISH_WITH_FUTURE_DATE_WITH_DATASET_WITH_LAST_VERSION_VISIBLE_AFTER_QUERY_NO_PREVIOUS_NAME           = "QUERY_VERSION_45_TO_PUBLISH_WITH_FUTURE_DATE_WITH_DATASET_WITH_LAST_VERSION_VISIBLE_AFTER_QUERY_NO_PREVIOUS";
    public static final String             QUERY_VERSION_46_TO_PUBLISH_WITH_FUTURE_DATE_WITH_DATASET_WITH_LAST_VERSION_VISIBLE_AFTER_QUERY_PREVIOUS_COMPATIBLE_NAME   = "QUERY_VERSION_46_TO_PUBLISH_WITH_FUTURE_DATE_WITH_DATASET_WITH_LAST_VERSION_VISIBLE_AFTER_QUERY_PREVIOUS_COMPATIBLE";
    public static final String             QUERY_VERSION_47_TO_PUBLISH_WITH_FUTURE_DATE_WITH_DATASET_WITH_LAST_VERSION_VISIBLE_AFTER_QUERY_PREVIOUS_INCOMPATIBLE_NAME = "QUERY_VERSION_47_TO_PUBLISH_WITH_FUTURE_DATE_WITH_DATASET_WITH_LAST_VERSION_VISIBLE_AFTER_QUERY_PREVIOUS_INCOMPATIBLE";

    public static final String             QUERY_VERSION_48_NOT_VISIBLE_REQUIRES_DATASET_VERSION_NOT_VISIBLE_NAME                                                     = "QUERY_VERSION_48_NOT_VISIBLE_REQUIRES_DATASET_VERSION_NOT_VISIBLE";

    public static final String             QUERY_VERSION_49_NOT_VISIBLE_REQUIRES_FIXED_DATASET_VERSION_NOT_VISIBLE_NAME                                               = "QUERY_VERSION_49_NOT_VISIBLE_REQUIRES_FIXED_DATASET_VERSION_NOT_VISIBLE";

    public static final String             QUERY_VERSION_50_NOT_VISIBLE_REQUIRES_DATASET_NOT_VISIBLE_NAME                                                             = "QUERY_VERSION_50_NOT_VISIBLE_REQUIRES_DATASET_NOT_VISIBLE";
    public static final String             QUERY_VERSION_51_NOT_VISIBLE_REQUIRES_FIXED_DATASET_VERSION_NOT_VISIBLE_NAME                                               = "QUERY_VERSION_51_NOT_VISIBLE_REQUIRES_FIXED_DATASET_VERSION_NOT_VISIBLE";

    public static final String             QUERY_VERSION_52_NOT_VISIBLE_IS_PART_OF_NOT_VISIBLE_PUBLICATION_NAME                                                       = "QUERY_VERSION_52_NOT_VISIBLE_IS_PART_OF_NOT_VISIBLE_PUBLICATION";
    public static final String             QUERY_VERSION_53_NOT_VISIBLE_IS_PART_OF_EMPTY_NAME                                                                         = "QUERY_VERSION_53_NOT_VISIBLE_IS_PART_OF_EMPTY";

    public static final String             QUERY_VERSION_55_PREPARED_TO_PUBLISH_STATUS_ACTIVE_NAME                                                                    = "QUERY_VERSION_55_PREPARED_TO_PUBLISH_STATUS_ACTIVE";
    public static final String             QUERY_VERSION_56_PREPARED_TO_PUBLISH_STATUS_DISCONTINUED_NAME                                                              = "QUERY_VERSION_56_PREPARED_TO_PUBLISH_STATUS_DISCONTINUED";

    public static final String             QUERY_VERSION_57_REPLACES_QUERY_58_NAME                                                                                    = "QUERY_VERSION_57_REPLACES_QUERY_58";
    public static final String             QUERY_VERSION_58_IS_REPLACED_BY_QUERY_57_NAME                                                                              = "QUERY_VERSION_58_IS_REPLACED_BY_QUERY_57";

    public static final String             QUERY_VERSION_59_MAXIMUM_VERSION_REACHED                                                                                   = "QUERY_VERSION_59_MAXIMUM_VERSION_REACHED";

    public static final String             QUERY_VERSION_60_MAXIMUM_MINOR_VERSION_REACHED                                                                             = "QUERY_VERSION_60_MAXIMUM_MINOR_VERSION_REACHED";

    public static final String             QUERY_VERSION_FOR_CACHE_RESOURCE                                                                                           = "QUERY_VERSION_FOR_CACHE_RESOURCE";

    private static QueryVersionMockFactory instance                                                                                                                   = null;

    private QueryVersionMockFactory() {
    }

    public static QueryVersionMockFactory getInstance() {
        if (instance == null) {
            instance = new QueryVersionMockFactory();
        }
        return instance;
    }

    private static QueryVersion getQueryVersion01WithSelection() {
        return createQueryWithGeneratedDatasetVersion();
    }

    private static QueryVersion getQueryVersion02BasicOrdered01() {
        return createQueryWithGeneratedDatasetVersion("a");
    }

    private static QueryVersion getQueryVersion03BasicOrdered02() {
        return createQueryWithGeneratedDatasetVersion("b");
    }

    private static QueryVersion getQueryVersion04BasicOrdered03() {
        return createQueryWithGeneratedDatasetVersion("c");
    }

    private static QueryVersion getQueryVersion05Basic() {
        return createQueryWithGeneratedDatasetVersion();
    }

    private static QueryVersion getQueryVersion06BasicActive() {
        return createQueryWithGeneratedDatasetVersion();
    }

    private static QueryVersion getQueryVersion07BasicActive() {
        DatasetVersion datasetVersion04 = getDatasetVersionMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME);
        QueryVersion queryVersion = createQueryWithDatasetVersion(datasetVersion04, true);
        return queryVersion;
    }

    private static QueryVersion getQueryVersion08BasicDiscontinued() {
        DatasetVersion datasetVersion03 = getDatasetVersionMock(DATASET_VERSION_03_FOR_DATASET_03_NAME);
        QueryVersion queryVersion = createQueryWithDatasetVersion(datasetVersion03, false);
        queryVersion.setStatus(QueryStatusEnum.DISCONTINUED);
        return queryVersion;
    }

    private static QueryVersion getQueryVersion10ActiveLatestData5() {
        DatasetVersion datasetVersion04 = getDatasetVersionMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME);
        QueryVersion queryVersion = createQueryWithDatasetVersion(datasetVersion04, true);
        queryVersion.setType(QueryTypeEnum.LATEST_DATA);
        queryVersion.setLatestDataNumber(Integer.valueOf(5));
        queryVersion.getSelection().clear();
        queryVersion.addSelection(buildSelectionItemWithDimensionAndCodes("DIM01", "C01", "C02"));
        queryVersion.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD"));
        return queryVersion;
    }

    private static QueryVersion getQueryVersion11Draft() {
        QueryVersion queryVersion = createQueryWithGeneratedDatasetVersion();
        prepareToProductionValidation(queryVersion);
        return queryVersion;
    }

    private static QueryVersion getQueryVersion12ProductionValidation() {
        QueryVersion queryVersion = createQueryWithGeneratedDatasetVersion();
        prepareToDiffusionValidation(queryVersion);
        return queryVersion;
    }

    private static QueryVersion getQueryVersion13DiffusionValidation() {
        QueryVersion queryVersion = createQueryWithGeneratedDatasetVersion();
        prepareToValidationRejected(queryVersion);
        queryVersion.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
        return queryVersion;
    }

    private static QueryVersion getQueryVersion14ValidationRejected() {
        QueryVersion queryVersion = createQueryWithGeneratedDatasetVersion();
        prepareToDiffusionValidation(queryVersion);
        queryVersion.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.VALIDATION_REJECTED);
        return queryVersion;
    }

    private static QueryVersion getQueryVersion15Published() {
        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion();
        datasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(datasetVersion);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        QueryVersion queryVersion = createQueryWithDatasetVersion(datasetVersion, true);
        queryVersion.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        queryVersion.getLifeCycleStatisticalResource().setValidFrom(new DateTime().minusDays(1));
        return queryVersion;
    }

    private static QueryVersion getQueryVersion19WithCodeAndUrnQuery01() {
        QueryVersionMock queryMock = new QueryVersionMock();
        queryMock.getLifeCycleStatisticalResource().setCode("QUERY01");
        queryMock.getLifeCycleStatisticalResource().setMaintainer(StatisticalResourcesPersistedDoMocks.mockAgencyExternalItem("agency01", "agency01"));
        return createQueryWithGeneratedDatasetVersion(queryMock);
    }

    private static QueryVersion getQueryVersion20WithCodeAndUrnQuery02() {
        QueryVersionMock queryMock = new QueryVersionMock();
        queryMock.getLifeCycleStatisticalResource().setCode("QUERY02");
        queryMock.getLifeCycleStatisticalResource().setMaintainer(StatisticalResourcesPersistedDoMocks.mockAgencyExternalItem("agency01", "agency01"));
        return createQueryWithGeneratedDatasetVersion(queryMock);
    }

    private static MockDescriptor getQueryVersion28V2PublishedNoVisibleForQuery06() {
        MockDescriptor query = getQueryMockDescriptor(QueryMockFactory.QUERY_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME);
        return new MockDescriptor(getQueryVersionMock(QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME), query);

    }

    private static QueryVersion getQueryVersion37PreparedToPublish() {
        QueryVersionMock queryMock = buildQueryVersionMockSimpleWithFixedDatasetVersion("Q01");
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        DatasetVersion datasetVersion = queryMock.getFixedDatasetVersion();
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        setQuerySelectionBasedOnDatasetVersion(queryMock, datasetVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);
        return queryMock;
    }

    private static QueryVersion getQueryVersion38ToPublishWithFutureDateWithDatasetVersionVisibleBefore() {
        QueryVersionMock queryMock = buildQueryVersionMockSimpleWithFixedDatasetVersion("Q01");
        queryMock.getLifeCycleStatisticalResource().setValidFrom(new DateTime().plusDays(2));
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        DatasetVersion datasetVersion = queryMock.getFixedDatasetVersion();
        datasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(1));
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        setQuerySelectionBasedOnDatasetVersion(queryMock, datasetVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);
        return queryMock;
    }

    private static QueryVersion getQueryVersion39ToPublishWithFutureDateWithDatasetVersionVisibleAfter() {
        QueryVersionMock queryMock = buildQueryVersionMockSimpleWithFixedDatasetVersion("Q01");
        queryMock.getLifeCycleStatisticalResource().setValidFrom(new DateTime().plusDays(2));
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        DatasetVersion datasetVersion = queryMock.getFixedDatasetVersion();
        datasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(3));
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        setQuerySelectionBasedOnDatasetVersion(queryMock, datasetVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);

        return queryMock;
    }

    private static QueryVersion getQueryVersion40ToPublishWithDatasetVersionNotPublished() {
        QueryVersionMock queryMock = buildQueryVersionMockSimpleWithFixedDatasetVersion("Q01");
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        DatasetVersion datasetVersion = queryMock.getFixedDatasetVersion();

        setQuerySelectionBasedOnDatasetVersion(queryMock, datasetVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);
        return queryMock;
    }

    public static QueryVersion getQueryVersion41ToPublishWithDatasetWithNoPublishedVersion() {
        QueryVersionMock queryMock = buildQueryVersionMockSimple("Q01");
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        Dataset dataset = DatasetMockFactory.generateDatasetWithGeneratedVersion();
        queryMock.setDataset(dataset);

        DatasetVersion lastDatasetVersion = dataset.getVersions().get(0);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(lastDatasetVersion);

        setQuerySelectionBasedOnDatasetVersion(queryMock, lastDatasetVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);
        return queryMock;
    }

    public static QueryVersion getQueryVersion42ToPublishWithDatasetWithLastVersionNotPublishedPreviousCompatible() {
        QueryVersionMock queryMock = buildQueryVersionMockSimple("Q01");
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        Dataset dataset = DatasetMockFactory.createPublishedAndDraftVersionsForDataset(1);
        queryMock.setDataset(dataset);

        DatasetVersion publishedVersion = dataset.getVersions().get(0);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(publishedVersion);
        StatisticalResourcesPersistedDoMocks.computeCoverageRelatedMetadata(publishedVersion);

        DatasetVersion lastVersion = dataset.getVersions().get(1);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(lastVersion);
        StatisticalResourcesPersistedDoMocks.computeCoverageRelatedMetadata(lastVersion);

        setQuerySelectionBasedOnDatasetVersion(queryMock, lastVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);
        return queryMock;
    }

    public static QueryVersion getQueryVersion43ToPublishWithDatasetWithLastVersionNotPublishedPreviousIncompatible() {
        QueryVersionMock queryMock = buildQueryVersionMockSimple("Q01");
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        Dataset dataset = createPublishedAndDraftVersionsForDatasetIncompatibleVersions(1);
        queryMock.setDataset(dataset);

        DatasetVersion lastVersion = dataset.getVersions().get(1);

        setQuerySelectionBasedOnDatasetVersion(queryMock, lastVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);
        return queryMock;
    }

    public static QueryVersion getQueryVersion44ToPublishWithFutureDateWithDatasetWithLastVersionVisibleBeforeQuery() {
        QueryVersionMock queryMock = buildQueryVersionMockSimple("Q01");
        queryMock.getLifeCycleStatisticalResource().setValidFrom(new DateTime().plusDays(2));
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        Dataset dataset = DatasetMockFactory.createPublishedAndNotVisibleVersionsForDataset(1, new DateTime().plusDays(3));
        queryMock.setDataset(dataset);

        DatasetVersion publishedVersion = dataset.getVersions().get(0);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(publishedVersion);
        StatisticalResourcesPersistedDoMocks.computeCoverageRelatedMetadata(publishedVersion);

        DatasetVersion lastVersion = dataset.getVersions().get(1);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(lastVersion);
        StatisticalResourcesPersistedDoMocks.computeCoverageRelatedMetadata(lastVersion);

        setQuerySelectionBasedOnDatasetVersion(queryMock, lastVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);
        return queryMock;
    }

    public static QueryVersion getQueryVersion45ToPublishWithFutureDateWithDatasetWithLastVersionVisibleAfterQueryNoPrevious() {
        QueryVersionMock queryMock = buildQueryVersionMockSimple("Q01");
        queryMock.getLifeCycleStatisticalResource().setValidFrom(new DateTime().plusDays(2));
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        DatasetVersion datasetVersion = DatasetMockFactory.createDatasetVersionPublishedLastVersion(1, INIT_VERSION, new DateTime().plusDays(3));
        queryMock.setDataset(datasetVersion.getDataset());

        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(datasetVersion);
        StatisticalResourcesPersistedDoMocks.computeCoverageRelatedMetadata(datasetVersion);

        setQuerySelectionBasedOnDatasetVersion(queryMock, datasetVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);
        return queryMock;
    }

    public static QueryVersion getQueryVersion46ToPublishWithFutureDateWithDatasetWithLastVersionVisibleAfterQueryPreviousCompatible() {
        QueryVersionMock queryMock = buildQueryVersionMockSimple("Q01");
        queryMock.getLifeCycleStatisticalResource().setValidFrom(new DateTime().plusDays(2));
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        Dataset dataset = DatasetMockFactory.createPublishedAndNotVisibleVersionsForDataset(1, new DateTime().plusDays(3));
        queryMock.setDataset(dataset);

        DatasetVersion publishedVersion = dataset.getVersions().get(0);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(publishedVersion);
        StatisticalResourcesPersistedDoMocks.computeCoverageRelatedMetadata(publishedVersion);

        DatasetVersion lastVersion = dataset.getVersions().get(1);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(lastVersion);
        StatisticalResourcesPersistedDoMocks.computeCoverageRelatedMetadata(publishedVersion);

        setQuerySelectionBasedOnDatasetVersion(queryMock, lastVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);
        return queryMock;
    }

    public static QueryVersion getQueryVersion47ToPublishWithFutureDateWithDatasetWithLastVersionVisibleAfterQueryPreviousIncompatible() {
        QueryVersionMock queryMock = buildQueryVersionMockSimple("Q01");
        queryMock.getLifeCycleStatisticalResource().setValidFrom(new DateTime().plusDays(2));
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        Dataset dataset = DatasetMockFactory.createPublishedAndNotVisibleVersionsForDataset(1, new DateTime().plusDays(3));
        queryMock.setDataset(dataset);

        DatasetVersion publishedVersion = dataset.getVersions().get(0);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(publishedVersion);
        // new dimension to make it incompatible
        publishedVersion.addDimensionsCoverage(new CodeDimension("DIM_NEW", "CODE01"));
        StatisticalResourcesPersistedDoMocks.computeCoverageRelatedMetadata(publishedVersion);

        DatasetVersion lastVersion = dataset.getVersions().get(1);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(lastVersion);
        StatisticalResourcesPersistedDoMocks.computeCoverageRelatedMetadata(publishedVersion);

        setQuerySelectionBasedOnDatasetVersion(queryMock, lastVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);
        return queryMock;
    }

    public static QueryVersion getQueryVersionForCacheResource(boolean isLastVersion) {

        QueryVersionMock queryMock = buildQueryVersionMockSimple("Q01_geo_cache");
        queryMock.getLifeCycleStatisticalResource().setValidFrom(new DateTime().plusDays(2));
        queryMock.getLifeCycleStatisticalResource().setLastVersion(isLastVersion);
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        Dataset dataset = DatasetMockFactory.createPublishedAndNotVisibleVersionsForDataset(1, new DateTime().plusDays(3));
        queryMock.setDataset(dataset);

        DatasetVersion publishedVersion = dataset.getVersions().get(0);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(publishedVersion);
        StatisticalResourcesPersistedDoMocks.computeCoverageRelatedMetadata(publishedVersion);

        DatasetVersion lastVersion = dataset.getVersions().get(1);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(lastVersion);
        StatisticalResourcesPersistedDoMocks.computeCoverageRelatedMetadata(publishedVersion);

        setQuerySelectionBasedOnDatasetVersion(queryMock, lastVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);
        return queryMock;
    }

    public static MockDescriptor getQueryVersion52NotVisibleIsPartOfNotVisiblePublication() {
        Dataset dataset = DatasetMockFactory.createPublishedAndNotVisibleVersionsForDataset(1, new DateTime().plusDays(3));

        DateTime queryValidFrom = new DateTime().plusDays(2);
        QueryVersionMock queryMock = buildQueryVersionMockSimple("Q01");
        queryMock.setDataset(dataset);
        queryMock.getLifeCycleStatisticalResource().setValidFrom(queryValidFrom);
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());
        createQueryVersionInStatus(queryMock, ProcStatusEnum.PUBLISHED);

        PublicationVersionMock publicationVersion = PublicationVersionMockFactory.buildPublishedReadyPublicationVersion(new PublicationMock(), INIT_VERSION, queryValidFrom.plusDays(1), null, true);
        PublicationVersionMockFactory.createQueryCubeElementLevel(publicationVersion, queryMock.getQuery());
        PublicationVersionMockFactory.createPublicationVersionInStatus(publicationVersion, ProcStatusEnum.PUBLISHED);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_97_NOT_VISIBLE_HAS_PART_NOT_VISIBLE_QUERY_NAME, publicationVersion);

        return new MockDescriptor(queryMock, publicationVersion);
    }

    public static QueryVersion getQueryVersion53NotVisibleIsPartOfEmpty() {
        Dataset dataset = DatasetMockFactory.createPublishedAndNotVisibleVersionsForDataset(1, new DateTime().plusDays(3));

        DateTime queryValidFrom = new DateTime().plusDays(2);
        QueryVersionMock queryMock = buildQueryVersionMockSimple("Q01");
        queryMock.setDataset(dataset);
        queryMock.getLifeCycleStatisticalResource().setValidFrom(queryValidFrom);
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());
        createQueryVersionInStatus(queryMock, ProcStatusEnum.PUBLISHED);

        return queryMock;
    }

    private static QueryVersion getQueryVersion55PreparedToPublishStatusActive() {
        QueryVersionMock queryMock = buildQueryVersionMockSimpleWithFixedDatasetVersion("Q01");
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        DatasetVersion datasetVersion = queryMock.getFixedDatasetVersion();
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        setQuerySelectionBasedOnDatasetVersion(queryMock, datasetVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);
        queryMock.setStatus(QueryStatusEnum.ACTIVE);
        return queryMock;
    }

    private static QueryVersion getQueryVersion56PreparedToPublishStatusDiscontinued() {
        QueryVersionMock queryMock = buildQueryVersionMockSimpleWithFixedDatasetVersion("Q01");
        queryMock.setQuery(QueryMockFactory.generateQueryWithoutGeneratedVersion());

        DatasetVersion datasetVersion = queryMock.getFixedDatasetVersion();
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        setQuerySelectionBasedOnDatasetVersion(queryMock, datasetVersion);

        QueryLifecycleTestUtils.prepareToPublished(queryMock);
        queryMock.setStatus(QueryStatusEnum.DISCONTINUED);
        return queryMock;
    }

    private static QueryVersion getQueryVersion59MaximumVersionReached() {
        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion();
        datasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(datasetVersion);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        QueryVersion queryVersion = createQueryWithDatasetVersion(datasetVersion, true);
        queryVersion.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        queryVersion.getLifeCycleStatisticalResource().setValidFrom(new DateTime().minusDays(1));
        queryVersion.getLifeCycleStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.MAXIMUM_VERSION_AVAILABLE);
        return queryVersion;
    }

    private static QueryVersion getQueryVersion60MaximumMinorVersionReached() {
        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion();
        datasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(datasetVersion);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);

        QueryVersion queryVersion = createQueryWithDatasetVersion(datasetVersion, true);
        queryVersion.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        queryVersion.getLifeCycleStatisticalResource().setValidFrom(new DateTime().minusDays(1));
        queryVersion.getLifeCycleStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.MAXIMUM_MINOR_VERSION_AVAILABLE);
        return queryVersion;
    }

    private static MockDescriptor getQueryVersion57ReplacesQuery58() {
        QueryVersion queryVersionReplaced = createQueryWithGeneratedDatasetVersion();
        QueryVersion queryVersionReplaces = createQueryWithGeneratedDatasetVersion();
        queryVersionReplaces.getLifeCycleStatisticalResource().setVersionLogic(SECOND_VERSION);

        registerQueryVersionMock(QUERY_VERSION_58_IS_REPLACED_BY_QUERY_57_NAME, queryVersionReplaced);

        queryVersionReplaces.getLifeCycleStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockQueryVersionRelated(queryVersionReplaced));
        queryVersionReplaced.getLifeCycleStatisticalResource().setIsReplacedByVersion(StatisticalResourcesPersistedDoMocks.mockQueryVersionRelated(queryVersionReplaces));

        return new MockDescriptor(queryVersionReplaces, queryVersionReplaced);
    }
    private static void setQuerySelectionBasedOnDatasetVersion(QueryVersionMock queryMock, DatasetVersion datasetVersion) {
        Map<String, QuerySelectionItem> selectionItems = new HashMap<String, QuerySelectionItem>();
        for (CodeDimension codeDim : datasetVersion.getDimensionsCoverage()) {
            QuerySelectionItem item = selectionItems.get(codeDim.getDsdComponentId());
            if (item == null) {
                item = new QuerySelectionItem();
                item.setDimension(codeDim.getDsdComponentId());
                selectionItems.put(codeDim.getDsdComponentId(), item);
            }
            CodeItem codeItem = new CodeItem();
            codeItem.setCode(codeDim.getIdentifier());
            codeItem.setTitle(codeDim.getTitle());
            item.addCode(codeItem);
        }
        for (String dimensionId : selectionItems.keySet()) {
            queryMock.addSelection(selectionItems.get(dimensionId));
        }
        queryMock.setType(QueryTypeEnum.FIXED);
    }

    // Public builders
    public static QueryVersionMock buildQueryVersionMockSimpleWithFixedDatasetVersion(String code) {
        QueryVersionMock template = new QueryVersionMock();
        template.getLifeCycleStatisticalResource().setCode(code);
        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion();
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(datasetVersion);
        template.setFixedDatasetVersion(datasetVersion);
        template.setStatus(QueryStatusEnum.ACTIVE);
        return template;
    }

    public static QueryVersionMock buildQueryVersionMockSimple(String code) {
        QueryVersionMock template = new QueryVersionMock();
        template.getLifeCycleStatisticalResource().setCode(code);
        template.setStatus(QueryStatusEnum.ACTIVE);
        return template;
    }

    public static QuerySelectionItem buildSelectionItemWithDimensionAndCodes(String dimensionId, String... codes) {
        QuerySelectionItem item = new QuerySelectionItem();
        item.setDimension(dimensionId);
        for (String code : codes) {
            CodeItem codeItem = new CodeItem();
            codeItem.setCode(code);
            codeItem.setTitle(code);
            item.addCode(codeItem);
        }
        return item;
    }

    private static QueryVersion createQueryWithDatasetVersion(DatasetVersion datasetVersion, boolean isLastDatasetVersion) {
        return getStatisticalResourcesPersistedDoMocks().mockQueryVersionWithDatasetVersion(datasetVersion, isLastDatasetVersion);
    }

    private static QueryVersion createQueryWithGeneratedDatasetVersion() {
        return getStatisticalResourcesPersistedDoMocks().mockQueryVersionWithGeneratedDatasetVersion();
    }

    private static QueryVersion createQueryWithGeneratedDatasetVersion(String code) {
        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion();
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(datasetVersion);

        QueryVersionMock template = new QueryVersionMock();
        template.setFixedDatasetVersion(datasetVersion);
        template.setStatus(QueryStatusEnum.ACTIVE);
        template.getLifeCycleStatisticalResource().setCode(code);
        return createQueryVersionFromTemplate(template);
    }

    private static QueryVersion createQueryWithGeneratedDatasetVersion(QueryVersionMock template) {
        DatasetVersion datasetVersion = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion();
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(datasetVersion);
        template.setFixedDatasetVersion(datasetVersion);
        template.setStatus(QueryStatusEnum.ACTIVE);
        return createQueryVersionFromTemplate(template);
    }

    public static QueryVersion createQueryVersionFromTemplate(QueryVersionMock template) {
        return getStatisticalResourcesPersistedDoMocks().mockQueryVersion(template);
    }

    public static QueryVersion createQueryVersionInStatus(QueryVersion queryVersion, ProcStatusEnum status) {
        queryVersion = getStatisticalResourcesPersistedDoMocks().mockQueryVersion(queryVersion);

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

    // -----------------------------------------------------------------
    // LIFE CYCLE PREPARATIONS
    // -----------------------------------------------------------------

    private static void prepareToProductionValidation(QueryVersion queryVersion) {
        QueryLifecycleTestUtils.prepareToProductionValidation(queryVersion);
    }

    private static void prepareToDiffusionValidation(QueryVersion queryVersion) {
        QueryLifecycleTestUtils.prepareToDiffusionValidation(queryVersion);
    }

    private static void prepareToValidationRejected(QueryVersion queryVersion) {
        QueryLifecycleTestUtils.prepareToValidationRejected(queryVersion);
    }

    // -----------------------------------------------------------------
    // UTILS
    // -----------------------------------------------------------------

    private static DatasetMock createPublishedAndDraftVersionsForDatasetIncompatibleVersions(int sequentialId) {
        DatasetMock dataset = new DatasetMock();
        dataset.setSequentialId(sequentialId);

        // V01
        DatasetVersionMock template = DatasetVersionMockFactory.buildSimpleVersion(dataset, INIT_VERSION);
        template.getSiemacMetadataStatisticalResource().setLastVersion(false);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime());
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(template);
        template.addDimensionsCoverage(new CodeDimension("DIM_NEW", "CODE_NEW"));
        StatisticalResourcesPersistedDoMocks.computeCoverageRelatedMetadata(template);
        DatasetVersion datasetVersionPublished = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(template);
        DatasetLifecycleTestUtils.fillAsPublished(datasetVersionPublished);

        // V02
        DatasetVersionMock templateLatest = DatasetVersionMockFactory.buildSimpleVersion(dataset, SECOND_VERSION);
        templateLatest.getSiemacMetadataStatisticalResource().setLastVersion(true);
        StatisticalResourcesPersistedDoMocks.mockDatasetVersionCoverages(templateLatest);
        DatasetVersion datasetVersionLatest = getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(templateLatest);

        datasetVersionLatest.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesNotPersistedDoMocks.mockRelatedResourceLinkedToDatasetVersion(datasetVersionPublished));

        return dataset;
    }

}