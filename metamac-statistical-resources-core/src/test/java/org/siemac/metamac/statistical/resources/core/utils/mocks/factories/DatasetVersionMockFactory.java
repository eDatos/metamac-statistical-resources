package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import static org.siemac.metamac.statistical.resources.core.utils.DatasetLifecycleTestUtils.fillAsProductionValidation;
import static org.siemac.metamac.statistical.resources.core.utils.DatasetLifecycleTestUtils.fillAsPublished;
import static org.siemac.metamac.statistical.resources.core.utils.DatasetLifecycleTestUtils.prepareToDiffusionValidation;
import static org.siemac.metamac.statistical.resources.core.utils.DatasetLifecycleTestUtils.prepareToProductionValidation;
import static org.siemac.metamac.statistical.resources.core.utils.DatasetLifecycleTestUtils.prepareToPublished;
import static org.siemac.metamac.statistical.resources.core.utils.DatasetLifecycleTestUtils.prepareToValidationRejected;
import static org.siemac.metamac.statistical.resources.core.utils.DatasetLifecycleTestUtils.prepareToVersioning;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_04_FULL_FILLED_WITH_1_DATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_93_NOT_VISIBLE_HAS_PART_NOT_VISIBLE_DATASET_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_29_CHECK_COMPAT_DATASET_86_OK_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_30_CHECK_COMPAT_DATASET_86_LESS_DIMENSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_31_CHECK_COMPAT_DATASET_86_MORE_DIMENSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_32_CHECK_COMPAT_DATASET_86_MORE_CODES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_34_CHECK_COMPAT_DATASET_87_INVALID_QUERY_TYPE_AUTOINC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_35_CHECK_COMPAT_DATASET_87_INVALID_QUERY_TYPE_LATEST_DATA_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_48_NOT_VISIBLE_REQUIRES_DATASET_VERSION_NOT_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.buildQueryVersionMockSimpleWithFixedDatasetVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.buildSelectionItemWithDimensionAndCodes;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.createQueryVersionFromTemplate;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks.mockAttributeValuesWithIdentifiers;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockDescriptor;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockProvider;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.dataset.domain.AttributeValue;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DataSourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.DatasetLifecycleTestUtils;
import org.siemac.metamac.statistical.resources.core.utils.LifecycleTestUtils;
import org.siemac.metamac.statistical.resources.core.utils.mocks.DatasetMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.DatasetVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.QueryVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

@MockProvider
@SuppressWarnings("unused")
public class DatasetVersionMockFactory extends StatisticalResourcesMockFactory<DatasetVersion> {

    public static final String               DATASET_VERSION_01_BASIC_NAME                                                                                 = "DATASET_VERSION_01_BASIC";

    public static final String               DATASET_VERSION_02_BASIC_NAME                                                                                 = "DATASET_VERSION_02_BASIC";

    public static final String               DATASET_VERSION_03_FOR_DATASET_03_NAME                                                                        = "DATASET_VERSION_03_FOR_DATASET_03";

    public static final String               DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME                                                       = "DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION";

    public static final String               DATASET_VERSION_05_FOR_DATASET_04_NAME                                                                        = "DATASET_VERSION_05_FOR_DATASET_04";

    public static final String               DATASET_VERSION_06_FOR_QUERIES_NAME                                                                           = "DATASET_VERSION_06_FOR_QUERIES";

    public static final String               DATASET_VERSION_07_VALID_CODE_000001_NAME                                                                     = "DATASET_VERSION_07_VALID_CODE_000001";

    public static final String               DATASET_VERSION_08_VALID_CODE_000002_NAME                                                                     = "DATASET_VERSION_08_VALID_CODE_000002";

    public static final String               DATASET_VERSION_09_OPER_0001_CODE_000003_NAME                                                                 = "DATASET_VERSION_09_OPER_0001_CODE_000003";

    public static final String               DATASET_VERSION_10_OPER_0002_CODE_000001_NAME                                                                 = "DATASET_VERSION_10_OPER_0002_CODE_000001";

    public static final String               DATASET_VERSION_11_OPER_0002_CODE_000002_NAME                                                                 = "DATASET_VERSION_11_OPER_0002_CODE_000002";

    public static final String               DATASET_VERSION_12_OPER_0002_MAX_CODE_NAME                                                                    = "DATASET_VERSION_12_OPER_0002_MAX_CODE";

    public static final String               DATASET_VERSION_13_OPER_0002_CODE_000003_PROD_VAL_NAME                                                        = "DATASET_VERSION_13_OPER_0002_CODE_000003_PROD_VAL";

    public static final String               DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED_NAME                                                             = "DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED";

    public static final String               DATASET_VERSION_15_DRAFT_NOT_READY_NAME                                                                       = "DATASET_VERSION_15_DRAFT_NOT_READY";

    public static final String               DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME                                                 = "DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION";

    public static final String               DATASET_VERSION_17_VERSION_RATIONALE_TYPE_MINOR_ERRATA_NAME                                                   = "DATASET_VERSION_17_VERSION_RATIONALE_TYPE_MINOR_ERRATA";

    public static final String               DATASET_VERSION_18_NEXT_VERSION_NOT_SCHEDULED_DATE_FILLED_NAME                                                = "DATASET_VERSION_18_NEXT_VERSION_NOT_SCHEDULED_DATE_FILLED";

    public static final String               DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME                                                       = "DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY";

    public static final String               DATASET_VERSION_20_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME                                  = "DATASET_VERSION_20_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION";

    public static final String               DATASET_VERSION_21_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME                                   = "DATASET_VERSION_21_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED";

    public static final String               DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME                                                           = "DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05";

    public static final String               DATASET_VERSION_23_V2_PUBLISHED_FOR_DATASET_05_NAME                                                           = "DATASET_VERSION_23_V2_PUBLISHED_FOR_DATASET_05";

    public static final String               DATASET_VERSION_24_V3_PUBLISHED_FOR_DATASET_05_NAME                                                           = "DATASET_VERSION_24_V3_PUBLISHED_FOR_DATASET_05";

    public static final String               DATASET_VERSION_25_V1_PUBLISHED_FOR_DATASET_06_NAME                                                           = "DATASET_VERSION_25_V1_PUBLISHED_FOR_DATASET_06";

    public static final String               DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06_NAME                                                = "DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06";

    public static final String               DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME                                                                  = "DATASET_VERSION_27_WITH_COVERAGE_FILLED";

    public static final String               DATASET_VERSION_28_WITHOUT_DATASOURCES_IMPORTING_DATA_NAME                                                    = "DATASET_VERSION_28_WITHOUT_DATASOURCES_IMPORTING_DATA";

    public static final String               DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME                                                                   = "DATASET_VERSION_29_WITHOUT_DATASOURCES";

    public static final String               DATASET_VERSION_30_WITH_DATASOURCE_NAME                                                                       = "DATASET_VERSION_30_WITH_DATASOURCE";

    public static final String               DATASET_VERSION_32_WITH_MULTIPLE_DATASOURCES_LINKED_TO_FILE_NAME                                              = "DATASET_VERSION_32_WITH_MULTIPLE_DATASOURCES_LINKED_TO_FILE";

    public static final String               DATASET_VERSION_33_WITH_SINGLE_DATASOURCE_LINKED_TO_FILE_WITH_UNDERSCORE_NAME                                 = "DATASET_VERSION_33_WITH_SINGLE_DATASOURCE_LINKED_TO_FILE_WITH_UNDERSCORE";

    public static final String               DATASET_VERSION_34_FOR_IMPORT_IN_OPERATION_0001_NAME                                                          = "DATASET_VERSION_34_FOR_IMPORT_IN_OPERATION_0001";

    public static final String               DATASET_VERSION_35_FOR_IMPORT_IN_OPERATION_0001_NAME                                                          = "DATASET_VERSION_35_FOR_IMPORT_IN_OPERATION_0001";

    public static final String               DATASET_VERSION_36_FOR_IMPORT_IN_OPERATION_0002_NAME                                                          = "DATASET_VERSION_36_FOR_IMPORT_IN_OPERATION_0002";

    public static final String               DATASET_VERSION_37_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME                                              = "DATASET_VERSION_37_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001";

    public static final String               DATASET_VERSION_38_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME                                              = "DATASET_VERSION_38_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001";

    public static final String               DATASET_VERSION_39_VERSION_RATIONALE_TYPE_MAJOR_NEW_RESOURCE_NAME                                             = "DATASET_VERSION_39_VERSION_RATIONALE_TYPE_MAJOR_NEW_RESOURCE";

    public static final String               DATASET_VERSION_40_VERSION_RATIONALE_TYPE_MAJOR_ESTIMATORS_NAME                                               = "DATASET_VERSION_40_VERSION_RATIONALE_TYPE_MAJOR_ESTIMATORS";

    public static final String               DATASET_VERSION_41_VERSION_RATIONALE_TYPE_MINOR_ERRATAS_NAME                                                  = "DATASET_VERSION_41_VERSION_RATIONALE_TYPE_MINOR_ERRATAS";

    public static final String               DATASET_VERSION_42_VERSION_RATIONALE_TYPE_MINOR_ERRATAS_AND_MAJOR_ESTIMATORS_NAME                             = "DATASET_VERSION_42_VERSION_RATIONALE_TYPE_MINOR_ERRATAS_AND_MAJOR_ESTIMATORS";

    public static final String               DATASET_VERSION_43_NEXT_VERSION_NO_UPDATES_NAME                                                               = "DATASET_VERSION_43_NEXT_VERSION_NO_UPDATES";

    public static final String               DATASET_VERSION_44_NEXT_VERSION_NON_SCHEDULED_UPDATE_NAME                                                     = "DATASET_VERSION_44_NEXT_VERSION_NON_SCHEDULED_UPDATE";

    public static final String               DATASET_VERSION_45_NEXT_VERSION_SCHEDULED_UPDATE_JANUARY_NAME                                                 = "DATASET_VERSION_45_NEXT_VERSION_SCHEDULED_UPDATE_JANUARY";

    public static final String               DATASET_VERSION_46_NEXT_VERSION_SCHEDULED_UPDATE_JULY_NAME                                                    = "DATASET_VERSION_46_NEXT_VERSION_SCHEDULED_UPDATE_JULY";

    public static final String               DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME                                                      = "DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES";

    public static final String               DATASET_VERSION_48_WITH_TEMPORAL_COVERAGE_FILLED_NAME                                                         = "DATASET_VERSION_48_WITH_TEMPORAL_COVERAGE_FILLED";

    public static final String               DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME                                 = "DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH";

    public static final String               DATASET_VERSION_50_WITH_DATASOURCE_FROM_PX_WITH_USER_NEXT_UPDATE_IN_ONE_MONTH_NAME                            = "DATASET_VERSION_50_WITH_DATASOURCE_FROM_PX_WITH_USER_NEXT_UPDATE_IN_ONE_MONTH";

    public static final String               DATASET_VERSION_51_IN_DRAFT_WITH_DATASOURCE_NAME                                                              = "DATASET_VERSION_51_IN_DRAFT_WITH_DATASOURCE";

    public static final String               DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE_NAME                                              = "DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE";

    public static final String               DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME                                               = "DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE";

    public static final String               DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME                                                = "DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE";

    public static final String               DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME                                                             = "DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE";

    public static final String               DATASET_VERSION_56_DRAFT_WITH_DATASOURCE_AND_QUERIES_NAME                                                     = "DATASET_VERSION_56_DRAFT_WITH_DATASOURCE_AND_QUERIES";

    public static final String               DATASET_VERSION_57_DRAFT_INITIAL_VERSION_NAME                                                                 = "DATASET_VERSION_57_DRAFT_INITIAL_VERSION";

    public static final String               DATASET_VERSION_58_PRODUCTION_VALIDATION_INITIAL_VERSION_NAME                                                 = "DATASET_VERSION_58_PRODUCTION_VALIDATION_INITIAL_VERSION";

    public static final String               DATASET_VERSION_59_DIFFUSION_VALIDATION_INITIAL_VERSION_NAME                                                  = "DATASET_VERSION_59_DIFFUSION_VALIDATION_INITIAL_VERSION";

    public static final String               DATASET_VERSION_60_VALIDATION_REJECTED_INITIAL_VERSION_NAME                                                   = "DATASET_VERSION_60_VALIDATION_REJECTED_INITIAL_VERSION";

    public static final String               DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION_NAME                                                             = "DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION";

    public static final String               DATASET_VERSION_62_DRAFT_NOT_INITIAL_VERSION_NAME                                                             = "DATASET_VERSION_62_DRAFT_NOT_INITIAL_VERSION";

    public static final String               DATASET_VERSION_63_PRODUCTION_VALIDATION_NOT_INITIAL_VERSION_NAME                                             = "DATASET_VERSION_63_PRODUCTION_VALIDATION_NOT_INITIAL_VERSION";

    public static final String               DATASET_VERSION_64_DIFFUSION_VALIDATION_NOT_INITIAL_VERSION_NAME                                              = "DATASET_VERSION_64_DIFFUSION_VALIDATION_NOT_INITIAL_VERSION";

    public static final String               DATASET_VERSION_65_VALIDATION_REJECTED_NOT_INITIAL_VERSION_NAME                                               = "DATASET_VERSION_65_VALIDATION_REJECTED_NOT_INITIAL_VERSION";

    public static final String               DATASET_VERSION_66_PUBLISHED_NOT_INITIAL_VERSION_NAME                                                         = "DATASET_VERSION_66_PUBLISHED_NOT_INITIAL_VERSION";

    public static final String               DATASET_VERSION_67_WITH_DATASOURCES_AND_COMPUTED_FIELDS_FILLED_NAME                                           = "DATASET_VERSION_67_WITH_DATASOURCES_AND_COMPUTED_FIELDS_FILLED";

    public static final String               DATASET_VERSION_68_WITH_DATASOURCES_AND_COMPUTED_FIELDS_FILLED_AND_USER_MODIFIED_DATE_NEXT_UPDATE_NAME        = "DATASET_VERSION_68_WITH_DATASOURCES_AND_COMPUTED_FIELDS_FILLED_AND_USER_MODIFIED_DATE_NEXT_UPDATE";

    public static final String               DATASET_VERSION_69_PUBLISHED_NO_ROOT_MAINTAINER_NAME                                                          = "DATASET_VERSION_69_PUBLISHED_NO_ROOT_MAINTAINER";

    public static final String               DATASET_VERSION_70_PREPARED_TO_PUBLISH_EXTERNAL_ITEM_FULL_NAME                                                = "DATASET_VERSION_70_PREPARED_TO_PUBLISH_EXTERNAL_ITEM_FULL";

    public static final String               DATASET_VERSION_71_RELATED_RESOURCES_UNPUBLISHED_NAME                                                         = "DATASET_VERSION_71_RELATED_RESOURCES_UNPUBLISHED";

    public static final String               DATASET_VERSION_72_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION_NAME                                             = "DATASET_VERSION_72_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION";

    public static final String               DATASET_VERSION_73_V01_FOR_DATASET_11_NAME                                                                    = "DATASET_VERSION_73_V01_FOR_DATASET_11";

    public static final String               DATASET_VERSION_74_V02_FOR_DATASET_11_NAME                                                                    = "DATASET_VERSION_74_V02_FOR_DATASET_11";

    public static final String               DATASET_VERSION_75_V01_FOR_DATASET_12_NAME                                                                    = "DATASET_VERSION_75_V01_FOR_DATASET_12";

    public static final String               DATASET_VERSION_76_V02_FOR_DATASET_12_NAME                                                                    = "DATASET_VERSION_76_V02_FOR_DATASET_12";

    public static final String               DATASET_VERSION_77_NO_PUB_REPLACES_DATASET_78_NAME                                                            = "DATASET_VERSION_77_NO_PUB_REPLACES_DATASET_78";

    public static final String               DATASET_VERSION_78_PUB_IS_REPLACED_BY_DATASET_77_NAME                                                         = "DATASET_VERSION_78_PUB_IS_REPLACED_BY_DATASET_77";

    public static final String               DATASET_VERSION_79_NO_PUB_REPLACES_DATASET_80_NAME                                                            = "DATASET_VERSION_79_NO_PUB_REPLACES_DATASET_80";

    public static final String               DATASET_VERSION_80_NO_PUB_IS_REPLACED_BY_DATASET_79_NAME                                                      = "DATASET_VERSION_80_NO_PUB_IS_REPLACED_BY_DATASET_79";

    public static final String               DATASET_VERSION_81_PUB_NOT_VISIBLE_REPLACES_DATASET_82_NAME                                                   = "DATASET_VERSION_81_PUB_NOT_VISIBLE_REPLACES_DATASET_82";

    public static final String               DATASET_VERSION_82_PUB_IS_REPLACED_BY_DATASET_81_NAME                                                         = "DATASET_VERSION_82_PUB_IS_REPLACED_BY_DATASET_81";

    public static final String               DATASET_VERSION_83_PUB_REPLACES_DATASET_84_NAME                                                               = "DATASET_VERSION_83_PUB_REPLACES_DATASET_84";

    public static final String               DATASET_VERSION_84_PUB_IS_REPLACED_BY_DATASET_83_NAME                                                         = "DATASET_VERSION_84_PUB_IS_REPLACED_BY_DATASET_83";

    public static final String               DATASET_VERSION_85_LAST_VERSION_NOT_PUBLISHED__IS_PART_OF_PUBLICATIONS_NAME                                   = "DATASET_VERSION_85_LAST_VERSION_NOT_PUBLISHED__IS_PART_OF_PUBLICATIONS";

    public static final String               DATASET_VERSION_86_WITH_TEMPORAL_DIMENSION_NAME                                                               = "DATASET_VERSION_86_WITH_TEMPORAL_DIMENSION";

    public static final String               DATASET_VERSION_87_WITH_NO_TEMPORAL_DIMENSION_NAME                                                            = "DATASET_VERSION_87_WITH_NO_TEMPORAL_DIMENSION";

    public static final String               DATASET_VERSION_88_PUBLISHED_WITH_CATEGORISATIONS_NAME                                                        = "DATASET_VERSION_88_PUBLISHED_WITH_CATEGORISATIONS";

    public static final String               DATASET_VERSION_89_WITH_ONE_DATASOURCE_NAME                                                                   = "DATASET_VERSION_89_WITH_ONE_DATASOURCE";
    public static final String               DATASET_VERSION_90_WITH_TWO_DATASOURCES_NAME                                                                  = "DATASET_VERSION_90_WITH_TWO_DATASOURCES";
    public static final String               DATASET_VERSION_91_SINGLE_VERSION_PUBLISHED_NOT_VISIBLE_IN_NOT_VISIBLE_PUB_AND_USED_BY_NOT_VISIBLE_QUERY_NAME = "DATASET_VERSION_91_SINGLE_VERSION_PUBLISHED_NOT_VISIBLE_IN_NOT_VISIBLE_PUB_AND_USED_BY_NOT_VISIBLE_QUERY";

    public static final String               DATASET_VERSION_92_NOT_VISIBLE_FOR_DATASET_30_NAME                                                            = "DATASET_VERSION_92_NOT_VISIBLE_FOR_DATASET_30";
    public static final String               DATASET_VERSION_93_PUBLISHED_FOR_DATASET_31_NAME                                                              = "DATASET_VERSION_93_PUBLISHED_FOR_DATASET_31";
    public static final String               DATASET_VERSION_94_NOT_VISIBLE_FOR_DATASET_31_NAME                                                            = "DATASET_VERSION_94_NOT_VISIBLE_FOR_DATASET_31";

    public static final String               DATASET_VERSION_95_PUBLISHED_FOR_DATASET_32                                                                   = "DATASET_VERSION_95_PUBLISHED_FOR_DATASET_32";
    public static final String               DATASET_VERSION_96_NOT_VISIBLE_FOR_DATASET_32_NAME                                                            = "DATASET_VERSION_96_NOT_VISIBLE_FOR_DATASET_32";

    public static final String               DATASET_VERSION_97_NOT_VISIBLE_REPLACED_BY_DATASET_98_NOT_VISIBLE_NAME                                        = "DATASET_VERSION_97_NOT_VISIBLE_REPLACED_BY_DATASET_98_NOT_VISIBLE";
    public static final String               DATASET_VERSION_98_NOT_VISIBLE_REPLACES_DATASET_97_NOT_VISIBLE_NAME                                           = "DATASET_VERSION_98_NOT_VISIBLE_REPLACES_DATASET_97_NOT_VISIBLE";

    public static final String               DATASET_VERSION_99_NOT_VISIBLE_SINGLE_VERSION_NAME                                                            = "DATASET_VERSION_99_NOT_VISIBLE_SINGLE_VERSION";

    public static final String               DATASET_VERSION_100_WITH_STATISTIC_OFFICIALITY_NAME                                                           = "DATASET_VERSION_100_WITH_STATISTIC_OFFICIALITY";

    public static final String               DATASET_VERSION_101_TO_DELETE_WITH_PREVIOUS_VERSION_NAME                                                      = "DATASET_VERSION_101_TO_DELETE_WITH_PREVIOUS_VERSION";

    public static final String               DATASET_VERSION_102_PREPARED_TO_PUBLISH_WITH_ATTRIBUTE_VALUES_NAME                                            = "DATASET_VERSION_102_PREPARED_TO_PUBLISH_WITH_ATTRIBUTE_VALUES";

    public static final String               DATASET_VERSION_103_NO_KEEP_DATA_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION_NAME                               = "DATASET_VERSION_103_NO_KEEP_DATA_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION";
    public static final String               DATASET_VERSION_104_NO_KEEP_DATA_PREPARED_TO_PUBLISH_INITIAL_VERSION_NAME                                     = "DATASET_VERSION_104_NO_KEEP_DATA_PREPARED_TO_PUBLISH_INITIAL_VERSION";

    public static final String               DATASET_VERSION_105_MAXIMUM_VERSION_REACHED                                                                   = "DATASET_VERSION_105_MAXIMUM_VERSION_REACHED";

    public static final String               DATASET_VERSION_106_MAXIMUM_MINOR_VERSION_REACHED                                                             = "DATASET_VERSION_106_MAXIMUM_MINOR_VERSION_REACHED";

    public static final String               DATASET_VERSION_107_DRAFT_NOT_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME                                        = "DATASET_VERSION_107_DRAFT_NOT_INITIAL_VERSION_WITHOUT_DATASOURCES";
    public static final String               DATASET_VERSION_108_PUBLISHED_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME                                        = "DATASET_VERSION_108_PUBLISHED_INITIAL_VERSION_WITHOUT_DATASOURCES";

    public static final String               DATASET_VERSION_109_DRAFT_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME                                            = "DATASET_VERSION_109_DRAFT_INITIAL_VERSION_WITHOUT_DATASOURCES";
    public static final String               DATASET_VERSION_110_PRODUCTION_VALIDATION_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME                            = "DATASET_VERSION_110_PRODUCTION_VALIDATION_INITIAL_VERSION_WITHOUT_DATASOURCES";
    public static final String               DATASET_VERSION_111_DIFFUSION_VALIDATION_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME                             = "DATASET_VERSION_111_DIFFUSION_VALIDATION_INITIAL_VERSION_WITHOUT_DATASOURCES";
    public static final String               DATASET_VERSION_112_VALIDATION_REJECTED_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME                              = "DATASET_VERSION_112_VALIDATION_REJECTED_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME";

    public static final String               DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME                                                                  = "DATASET_VERSION_113_DATABASE_TYPE_BASIC";
    public static final String               DATASET_VERSION_114_DATABASE_DATASET_WITH_DATASOURCE_NAME                                                     = "DATASET_VERSION_114_DATABASE_DATASET_WITH_DATASOURCE";
    public static final String               DATASET_VERSION_115_DATABASE_TYPE_BASIC_NAME                                                                  = "DATASET_VERSION_115_DATABASE_TYPE_BASIC";
    public static final String               DATASET_VERSION_116_PRODUCTION_VALIDATION_DATABASE_DATASET_NAME                                               = "DATASET_VERSION_116_PRODUCTION_VALIDATION_DATABASE_DATASET";
    public static final String               DATASET_VERSION_117_DIFUSSION_VALIDATION_DATASET_NAME                                                         = "DATASET_VERSION_117_DIFUSSION_VALIDATION_DATASET";
    public static final String               DATASET_VERSION_118_PUBLISHED_DATABASE_DATASET_NAME                                                           = "DATASET_VERSION_118_PUBLISHED_DATABASE_DATASET";
    public static final String               DATASET_VERSION_119_DRAFT_DATABASE_DATASET_NAME                                                               = "DATASET_VERSION_119_DRAFT_DATABASE_DATASET";
    public static final String               DATASET_VERSION_120_VALIDATION_REJECTED_DATABASE_DATASET_NAME                                                 = "DATASET_VERSION_120_VALIDATION_REJECTED_DATABASE_DATASET";

    private static DatasetVersionMockFactory instance                                                                                                      = null;

    private DatasetVersionMockFactory() {
    }

    public static DatasetVersionMockFactory getInstance() {
        if (instance == null) {
            instance = new DatasetVersionMockFactory();
        }
        return instance;
    }

    private static DatasetVersion getDatasetVersion01Basic() {
        return createDatasetVersionWithSequence(1);
    }

    private static DatasetVersion getDatasetVersion02Basic() {
        return createDatasetVersionWithSequence(1);
    }

    private static MockDescriptor getDatasetVersion03ForDataset03() {
        MockDescriptor mock = getDatasetMockDescriptor(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        return new MockDescriptor(getDatasetVersionMock(DATASET_VERSION_03_FOR_DATASET_03_NAME), mock);
    }

    private static MockDescriptor getDatasetVersion04ForDataset03AndLastVersion() {
        MockDescriptor mock = getDatasetMockDescriptor(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        return new MockDescriptor(getDatasetVersionMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME), mock);
    }

    private static MockDescriptor getDatasetVersion05ForDataset04() {
        MockDescriptor mock = getDatasetMockDescriptor(DATASET_04_FULL_FILLED_WITH_1_DATASET_VERSIONS_NAME);
        return new MockDescriptor(getDatasetVersionMock(DATASET_VERSION_05_FOR_DATASET_04_NAME), mock);
    }

    private static DatasetVersion getDatasetVersion06ForQueries() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(2);
        datasetVersion.getDimensionsCoverage().clear();
        addCodesToDimensionCoverage(datasetVersion, "DIM_01", "CODE_01", "CODE_02");
        addCodesToDimensionCoverage(datasetVersion, "DIM_02", "CODE_11", "CODE_12");
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion07ValidCode000001() {
        return createDatasetVersionInSpecificOperation(OPERATION_01_CODE, 1);
    }

    private static DatasetVersion getDatasetVersion08ValidCode000002() {
        return createDatasetVersionInSpecificOperation(OPERATION_01_CODE, 2);
    }

    private static DatasetVersion getDatasetVersion09Oper0001Code000003() {
        return createDatasetVersionInSpecificOperation(OPERATION_01_CODE, 3);
    }

    private static DatasetVersion getDatasetVersion10Oper0002Code000001() {
        return createDatasetVersionInSpecificOperation(OPERATION_02_CODE, 1);
    }

    private static DatasetVersion getDatasetVersion11Oper0002Code000002() {
        return createDatasetVersionInSpecificOperation(OPERATION_02_CODE, 2);
    }

    private static DatasetVersion getDatasetVersion12Oper0002MaxCode() {
        return createDatasetVersionInSpecificOperation(OPERATION_02_CODE, 999999);
    }

    private static DatasetVersion getDatasetVersion13Oper0002Code000003ProdVal() {
        DatasetVersion datasetVersion = createDatasetVersionInSpecificOperation(OPERATION_02_CODE, 3);
        fillAsProductionValidation(datasetVersion);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion14Oper03Code01Published() {
        DatasetVersion datasetVersion = createDatasetVersionInSpecificOperation(OPERATION_03_CODE, 1);
        fillAsPublished(datasetVersion);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion15DraftNotReady() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);
        datasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion16DraftReadyForProductionValidation() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        prepareToProductionValidation(datasetVersion);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion17VersionRationaleTypeMinorErrata() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        prepareToProductionValidation(datasetVersion);
        datasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().add(new VersionRationaleType(VersionRationaleTypeEnum.MINOR_ERRATA));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion18NextVersionNotScheduledDateFilled() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        prepareToProductionValidation(datasetVersion);
        datasetVersion.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);
        datasetVersion.getSiemacMetadataStatisticalResource().setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(10).toDate()));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion19ProductionValidationNotReady() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        prepareToDiffusionValidation(datasetVersion);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion20ProductionValidationReadyForDiffusionValidation() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        prepareToDiffusionValidation(datasetVersion);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion21ProductionValidationReadyForValidationRejected() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        prepareToValidationRejected(datasetVersion);
        return datasetVersion;
    }

    private static MockDescriptor getDatasetVersion22V1PublishedForDataset05() {
        MockDescriptor mock = getDatasetMockDescriptor(DATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME);
        return new MockDescriptor(getDatasetVersionMock(DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME), mock);
    }

    private static MockDescriptor getDatasetVersion23V2PublishedForDataset05() {
        MockDescriptor mock = getDatasetMockDescriptor(DATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME);
        return new MockDescriptor(getDatasetVersionMock(DATASET_VERSION_23_V2_PUBLISHED_FOR_DATASET_05_NAME), mock);
    }

    private static MockDescriptor getDatasetVersion24V3PublishedForDataset05() {
        MockDescriptor mock = getDatasetMockDescriptor(DATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME);
        return new MockDescriptor(getDatasetVersionMock(DATASET_VERSION_24_V3_PUBLISHED_FOR_DATASET_05_NAME), mock);
    }

    private static MockDescriptor getDatasetVersion25V1PublishedForDataset06() {
        MockDescriptor mock = getDatasetMockDescriptor(DATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME);
        return new MockDescriptor(getDatasetVersionMock(DATASET_VERSION_25_V1_PUBLISHED_FOR_DATASET_06_NAME), mock);
    }

    private static MockDescriptor getDatasetVersion26V2PublishedNoVisibleForDataset06() {
        MockDescriptor mock = getDatasetMockDescriptor(DATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME);
        return new MockDescriptor(getDatasetVersionMock(DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06_NAME), mock);
    }

    private static DatasetVersion getDatasetVersion27WithCoverageFilled() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(2);

        prepareToProductionValidation(datasetVersion);

        datasetVersion.getDimensionsCoverage().clear();
        datasetVersion.addDimensionsCoverage(new CodeDimension("dim1", "code-d1-1"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("dim1", "code-d1-2"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("dim2", "code-d2-1"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("dim2", "code-d2-2"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("dim3", "code-d3-1"));

        datasetVersion.getAttributesCoverage().clear();
        datasetVersion.addAttributesCoverage(new AttributeValue("attr1", "value-a1-1"));
        datasetVersion.addAttributesCoverage(new AttributeValue("attr1", "value-a1-2"));
        datasetVersion.addAttributesCoverage(new AttributeValue("attr2", "value-a2-1"));
        datasetVersion.addAttributesCoverage(new AttributeValue("attr2", "value-a2-2"));
        datasetVersion.addAttributesCoverage(new AttributeValue("attr3", "value-a3-1"));

        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion28WithoutDatasourcesImportingData() {
        return createDatasetVersionWithSequence(2);
    }

    private static DatasetVersion getDatasetVersion29WithoutDatasources() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);
        datasetVersion.setRelatedDsd(StatisticalResourcesDoMocks.mockDsdExternalItem());
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion30WithDatasource() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);
        datasetVersion.addDatasource(DatasourceMockFactory.generateDatasource("datasource_06.px"));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion32WithMultipleDatasourcesLinkedToFile() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);
        datasetVersion.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        datasetVersion.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        datasetVersion.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        datasetVersion.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        datasetVersion.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion33WithSingleDatasourceLinkedToFileWithUnderscore() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);
        datasetVersion.addDatasource(DatasourceMockFactory.generateDatasource("datasource_underscore.px"));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion34ForImportInOperation0001() {
        DatasetVersionMock template = new DatasetVersionMock();
        template.setSequentialId(1);
        template.setStatisticalOperationCode("OPER_0001");
        template.setRelatedDsd(StatisticalResourcesDoMocks.mockDsdExternalItem());
        return createDatasetVersionFromTemplate(template);
    }

    private static DatasetVersion getDatasetVersion35ForImportInOperation0001() {
        DatasetVersion datasetVersion = createDatasetVersionInSpecificOperation("OPER_0001", 2);
        datasetVersion.setRelatedDsd(StatisticalResourcesDoMocks.mockDsdExternalItem());
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion36ForImportInOperation0002() {
        DatasetVersion datasetVersion = createDatasetVersionInSpecificOperation("OPER_0002", 1);
        datasetVersion.setRelatedDsd(StatisticalResourcesDoMocks.mockDsdExternalItem());
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion37WithSingleDatasourceInOperation0001() {
        DatasetVersion datasetVersion = createDatasetVersionInSpecificOperation("OPER_0001", 1);
        datasetVersion.setRelatedDsd(StatisticalResourcesDoMocks.mockDsdExternalItem());
        datasetVersion.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion38WithSingleDatasourceInOperation0001() {
        DatasetVersion datasetVersion = createDatasetVersionInSpecificOperation("OPER_0001", 1);
        datasetVersion.setRelatedDsd(StatisticalResourcesDoMocks.mockDsdExternalItem());
        datasetVersion.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion39VersionRationaleTypeMajorNewResource() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        datasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().add(new VersionRationaleType(VersionRationaleTypeEnum.MAJOR_NEW_RESOURCE));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion40VersionRationaleTypeMajorEstimators() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.SECOND_VERSION);
        datasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().add(new VersionRationaleType(VersionRationaleTypeEnum.MAJOR_ESTIMATORS));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion41VersionRationaleTypeMinorErratas() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.SECOND_VERSION);
        datasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().add(new VersionRationaleType(VersionRationaleTypeEnum.MINOR_ERRATA));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion42VersionRationaleTypeMinorErratasAndMajorEstimators() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.SECOND_VERSION);
        datasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().add(new VersionRationaleType(VersionRationaleTypeEnum.MINOR_ERRATA));
        datasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().add(new VersionRationaleType(VersionRationaleTypeEnum.MAJOR_ESTIMATORS));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion43NextVersionNoUpdates() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        datasetVersion.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.NO_UPDATES);
        datasetVersion.getSiemacMetadataStatisticalResource().setNextVersionDate(null);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion44NextVersionNonScheduledUpdate() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        datasetVersion.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);
        datasetVersion.getSiemacMetadataStatisticalResource().setNextVersionDate(null);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion45NextVersionScheduledUpdateJanuary() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        datasetVersion.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.SCHEDULED_UPDATE);
        datasetVersion.getSiemacMetadataStatisticalResource().setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime(2013, 1, 15, 12, 0, 0, 0).toDate()));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion46NextVersionScheduledUpdateJuly() {
        DatasetVersion datasetVersion = createDatasetVersionEmpty();
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        datasetVersion.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.SCHEDULED_UPDATE);
        datasetVersion.getSiemacMetadataStatisticalResource().setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime(2013, 7, 15, 12, 0, 0, 0).toDate()));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion47WithCoverageFilledWithTitles() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(2);

        prepareToProductionValidation(datasetVersion);

        datasetVersion.getDimensionsCoverage().clear();
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2011", "2011"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2010", "2010"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2010-M02", "Febrero 2010"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2010-M01", "Enero 2010"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES", "España"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES61", "Andalucia"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES70", "Canarias"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES45", "Cataluña"));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion48WithTemporalCoverageFilled() {
        DatasetVersionMock template = buildDatasetVersionWithSequenceAndVersion(1, StatisticalResourcesMockFactory.INIT_VERSION);
        template.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2012", "2012"));
        template.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2011", "2011"));
        template.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2010", "2010"));
        template.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES", "España"));
        template.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES61", "Andalucia"));
        template.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES70", "Canarias"));
        template.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES45", "Cataluña"));
        DatasetVersion datasetVersion = createDatasetVersionFromTemplate(template);

        prepareToProductionValidation(datasetVersion);

        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion49WithDatasourceFromPxWithNextUpdateInOneMonth() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);

        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2012", "2012"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2011", "2011"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2010", "2010"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES", "España"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES61", "Andalucia"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES70", "Canarias"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES45", "Cataluña"));

        datasetVersion.addTemporalCoverage(StatisticalResourcesDoMocks.mockTemporalCode("2012", "2012"));
        datasetVersion.addTemporalCoverage(StatisticalResourcesDoMocks.mockTemporalCode("2011", "2011"));
        datasetVersion.addTemporalCoverage(StatisticalResourcesDoMocks.mockTemporalCode("2010", "2010"));

        datasetVersion.addDatasource(DatasourceMockFactory.generatePxDatasource(new DateTime().plusMonths(1)));

        datasetVersion.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.SCHEDULED_UPDATE);
        datasetVersion.setDateNextUpdate(CoreCommonUtil.jodaDateTime2IsoDate(datasetVersion.getDatasources().get(0).getDateNextUpdate().toDate()));
        datasetVersion.setUserModifiedDateNextUpdate(false);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion50WithDatasourceFromPxWithUserNextUpdateInOneMonth() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);

        datasetVersion.setDateNextUpdate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusMonths(1).toDate()));
        datasetVersion.setUserModifiedDateNextUpdate(true);

        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2012", "2012"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2011", "2011"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2010", "2010"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES", "España"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES61", "Andalucia"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES70", "Canarias"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES45", "Cataluña"));

        datasetVersion.addTemporalCoverage(StatisticalResourcesDoMocks.mockTemporalCode("2012", "2012"));
        datasetVersion.addTemporalCoverage(StatisticalResourcesDoMocks.mockTemporalCode("2011", "2011"));
        datasetVersion.addTemporalCoverage(StatisticalResourcesDoMocks.mockTemporalCode("2010", "2010"));

        datasetVersion.addDatasource(DatasourceMockFactory.generatePxDatasource(new DateTime().plusMonths(1)));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion51InDraftWithDatasource() {
        return createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DRAFT);
    }

    private static DatasetVersion getDatasetVersion52InProductionValidationWithDatasource() {
        return createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.PRODUCTION_VALIDATION);
    }

    private static DatasetVersion getDatasetVersion53InDiffusionValidationWithDatasource() {
        return createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DIFFUSION_VALIDATION);
    }

    private static DatasetVersion getDatasetVersion54InValidationRejectedWithDatasource() {
        return createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.VALIDATION_REJECTED);
    }

    private static DatasetVersion getDatasetVersion55PublishedWithDatasource() {
        return createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.PUBLISHED);
    }

    private static DatasetVersion getDatasetVersion57DraftInitialVersion() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DRAFT);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion58ProductionValidationInitialVersion() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.PRODUCTION_VALIDATION);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion59DiffusionValidationInitialVersion() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DIFFUSION_VALIDATION);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion60ValidationRejectedInitialVersion() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.VALIDATION_REJECTED);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion61PublishedInitialVersion() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.PUBLISHED);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion62DraftNotInitialVersion() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DRAFT);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.NOT_INITIAL_VERSION);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion63ProductionValidationNotInitialVersion() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.PRODUCTION_VALIDATION);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.NOT_INITIAL_VERSION);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion64DiffusionValidationNotInitialVersion() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DIFFUSION_VALIDATION);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.NOT_INITIAL_VERSION);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion65ValidationRejectedNotInitialVersion() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.VALIDATION_REJECTED);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.NOT_INITIAL_VERSION);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion66PublishedNotInitialVersion() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.PUBLISHED);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.NOT_INITIAL_VERSION);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion67WithDatasourcesAndComputedFieldsFilled() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);

        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2012", "2012"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2011", "2011"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2010", "2010"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES", "España"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES61", "Andalucia"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES70", "Canarias"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES45", "Cataluña"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("MEAS_DIM", "C01", "Concept 01"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("MEAS_DIM", "C02", "Concept 02"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("MEAS_DIM", "C03", "Concept 03"));

        datasetVersion.addTemporalCoverage(StatisticalResourcesDoMocks.mockTemporalCode("2012", "2012"));
        datasetVersion.addTemporalCoverage(StatisticalResourcesDoMocks.mockTemporalCode("2011", "2011"));
        datasetVersion.addTemporalCoverage(StatisticalResourcesDoMocks.mockTemporalCode("2010", "2010"));

        datasetVersion.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("ES"));
        datasetVersion.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("ES61"));
        datasetVersion.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("ES70"));
        datasetVersion.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("ES45"));

        datasetVersion.addMeasureCoverage(StatisticalResourcesDoMocks.mockConceptExternalItem("C01"));
        datasetVersion.addMeasureCoverage(StatisticalResourcesDoMocks.mockConceptExternalItem("C02"));
        datasetVersion.addMeasureCoverage(StatisticalResourcesDoMocks.mockConceptExternalItem("C03"));

        datasetVersion.setDateStart(new DateTime(2010, 1, 31, 0, 0, 0, 0));
        datasetVersion.setDateEnd(new DateTime(2012, 12, 31, 23, 59, 59, 999));

        datasetVersion.setFormatExtentDimensions(3);
        datasetVersion.setFormatExtentObservations(36L);

        datasetVersion.getSiemacMetadataStatisticalResource().setLastUpdate(new DateTime().minusDays(1));

        datasetVersion.setDatasetRepositoryId(datasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        datasetVersion.addDatasource(DatasourceMockFactory.generatePxDatasource(new DateTime().plusMonths(1)));
        datasetVersion.setDateNextUpdate(CoreCommonUtil.jodaDateTime2IsoDate(datasetVersion.getDatasources().get(0).getDateNextUpdate().toDate()));
        datasetVersion.setDateLastTimeDataImport(new DateTime());
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion68WithDatasourcesAndComputedFieldsFilledAndUserModifiedDateNextUpdate() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);

        datasetVersion.setDateNextUpdate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusMonths(1).toDate()));
        datasetVersion.setUserModifiedDateNextUpdate(true);

        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2012", "2012"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2011", "2011"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2010", "2010"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES", "España"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES61", "Andalucia"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES70", "Canarias"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES45", "Cataluña"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("MEAS_DIM", "C01", "Concept 01"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("MEAS_DIM", "C02", "Concept 02"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("MEAS_DIM", "C03", "Concept 03"));

        datasetVersion.addTemporalCoverage(StatisticalResourcesDoMocks.mockTemporalCode("2012", "2012"));
        datasetVersion.addTemporalCoverage(StatisticalResourcesDoMocks.mockTemporalCode("2011", "2011"));
        datasetVersion.addTemporalCoverage(StatisticalResourcesDoMocks.mockTemporalCode("2010", "2010"));

        datasetVersion.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("ES"));
        datasetVersion.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("ES61"));
        datasetVersion.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("ES70"));
        datasetVersion.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("ES45"));

        datasetVersion.addMeasureCoverage(StatisticalResourcesDoMocks.mockConceptExternalItem("C01"));
        datasetVersion.addMeasureCoverage(StatisticalResourcesDoMocks.mockConceptExternalItem("C02"));
        datasetVersion.addMeasureCoverage(StatisticalResourcesDoMocks.mockConceptExternalItem("C03"));

        datasetVersion.setDateStart(new DateTime(2010, 1, 31, 0, 0, 0, 0));
        datasetVersion.setDateEnd(new DateTime(2012, 12, 31, 23, 59, 59, 999));

        datasetVersion.setFormatExtentDimensions(3);
        datasetVersion.setFormatExtentObservations(36L);

        datasetVersion.getSiemacMetadataStatisticalResource().setLastUpdate(new DateTime().minusDays(1));

        datasetVersion.addDatasource(DatasourceMockFactory.generatePxDatasource(new DateTime().plusMonths(1)));
        datasetVersion.setDateLastTimeDataImport(new DateTime());
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion69PublishedNoRootMaintainer() {
        DatasetVersionMock template = new DatasetVersionMock();
        template.setSequentialId(1);
        template.setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        template.getSiemacMetadataStatisticalResource().setMaintainer(StatisticalResourcesDoMocks.mockAgencyExternalItem("agency01", "SIEMAC.agency01"));
        DatasetVersion datasetVersion = createDatasetVersionFromTemplate(template);
        prepareToVersioning(datasetVersion);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion70PreparedToPublishExternalItemFull() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);
        prepareToPublished(datasetVersion);
        fillOptionalExternalItems(datasetVersion);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion71RelatedResourcesUnpublished() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequenceAndVersion(1, StatisticalResourcesMockFactory.INIT_VERSION);
        prepareToPublished(datasetVersion);

        fillOptionalRelatedResourcesNotPublished(datasetVersion);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion72PreparedToPublishWithPreviousVersion() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequenceAndVersion(1, StatisticalResourcesMockFactory.SECOND_VERSION);
        prepareToPublished(datasetVersion);

        fillOptionalExternalItems(datasetVersion);
        fillOptionalRelatedResourcesPublished(datasetVersion);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion77NoPubReplacesDataset78() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);

        DatasetVersion datasetVersionToReplace = createDatasetVersionWithSequence(2);
        prepareToVersioning(datasetVersionToReplace);
        registerDatasetVersionMock(DATASET_VERSION_78_PUB_IS_REPLACED_BY_DATASET_77_NAME, datasetVersionToReplace);

        datasetVersion.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersionToReplace));
        datasetVersionToReplace.getSiemacMetadataStatisticalResource().setIsReplacedBy(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersion));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion79NoPubReplacesDataset80() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);

        DatasetVersion datasetVersionToReplace = createDatasetVersionWithSequence(2);
        registerDatasetVersionMock(DATASET_VERSION_80_NO_PUB_IS_REPLACED_BY_DATASET_79_NAME, datasetVersionToReplace);

        datasetVersion.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersionToReplace));
        datasetVersionToReplace.getSiemacMetadataStatisticalResource().setIsReplacedBy(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersion));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion81PubNotVisibleReplacesDataset82() {
        DatasetVersionMock template = new DatasetVersionMock();
        template.setSequentialId(1);
        template.setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(1));
        DatasetVersion datasetVersion = createDatasetVersionFromTemplate(template);

        DatasetVersion datasetVersionToReplace = createDatasetVersionWithSequence(2);
        prepareToVersioning(datasetVersionToReplace);
        registerDatasetVersionMock(DATASET_VERSION_82_PUB_IS_REPLACED_BY_DATASET_81_NAME, datasetVersionToReplace);

        datasetVersion.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersionToReplace));
        datasetVersionToReplace.getSiemacMetadataStatisticalResource().setIsReplacedBy(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersion));
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion83PubReplacesDataset84() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);
        prepareToVersioning(datasetVersion);

        DatasetVersion datasetVersionToReplace = createDatasetVersionWithSequence(2);
        prepareToVersioning(datasetVersionToReplace);
        registerDatasetVersionMock(DATASET_VERSION_84_PUB_IS_REPLACED_BY_DATASET_83_NAME, datasetVersionToReplace);

        datasetVersion.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersionToReplace));
        datasetVersionToReplace.getSiemacMetadataStatisticalResource().setIsReplacedBy(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersion));
        return datasetVersion;
    }

    private static MockDescriptor getDatasetVersion85LastVersionNotPublishedIsPartOfPublications() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);
        datasetVersion.getSiemacMetadataStatisticalResource().setLastVersion(true);

        // Publication created in PublicationVersionMockFactory
        PublicationVersion pub01 = buildDraftPublicationWithDatasetInFirstLevel(1, datasetVersion.getDataset());
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_43_DRAFT_HAS_PART_DATASET_VERSION_85_FIRST_LEVEL_NAME, pub01);

        PublicationVersion pub02 = buildDraftPublicationWithDatasetNotInFirstLevel(2, datasetVersion.getDataset());
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_44_DRAFT_HAS_PART_DATASET_VERSION_85_NO_FIRST_LEVEL_NAME, pub02);

        PublicationVersion pub03 = buildDraftPublicationWithDatasetInMultipleCubes(3, datasetVersion.getDataset());
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_45_DRAFT_HAS_PART_DATASET_VERSION_85_MULTI_CUBE_NAME, pub03);

        return new MockDescriptor(datasetVersion, pub01, pub02, pub03);
    }

    private static MockDescriptor getDatasetVersion86WithTemporalDimension() {
        DatasetVersionMock datasetVersionMock = new DatasetVersionMock();
        addCodesToDimensionCoverage(datasetVersionMock, "DIM_01", "D1_C01", "D1_C02", "D1_C03");
        addCodesToDimensionCoverage(datasetVersionMock, "DIM_02", "D2_C01", "D2_C02", "D2_C03");
        addCodesToDimensionCoverage(datasetVersionMock, "TIME_PERIOD", "2010", "2011");
        DatasetVersion datasetVersion = createDatasetVersionFromTemplate(datasetVersionMock);

        // OK
        QueryVersion queryOk = null;
        {
            QueryVersionMock queryMock = buildQueryVersionMockSimpleWithFixedDatasetVersion("QUERY_01");
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "D1_C01"));
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "D2_C01"));
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2010", "2011"));
            queryOk = createQueryVersionFromTemplate(queryMock);
            registerQueryVersionMock(QUERY_VERSION_29_CHECK_COMPAT_DATASET_86_OK_NAME, queryOk);
        }

        // Less dimensions than Dataset
        QueryVersion queryLessDimensions = null;
        {
            QueryVersionMock queryMock = buildQueryVersionMockSimpleWithFixedDatasetVersion("QUERY_01");
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "D1_C01"));
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2010", "2011"));
            queryLessDimensions = createQueryVersionFromTemplate(queryMock);
            registerQueryVersionMock(QUERY_VERSION_30_CHECK_COMPAT_DATASET_86_LESS_DIMENSIONS_NAME, queryLessDimensions);
        }

        // More dimensions in query
        QueryVersion queryMoreDimensions = null;
        {
            QueryVersionMock queryMock = buildQueryVersionMockSimpleWithFixedDatasetVersion("QUERY_02");
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "D1_C01"));
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "D1_C02"));
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_NOT_IN_DATASET", "CODE"));
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2010", "2011"));
            queryMoreDimensions = createQueryVersionFromTemplate(queryMock);
            registerQueryVersionMock(QUERY_VERSION_31_CHECK_COMPAT_DATASET_86_MORE_DIMENSIONS_NAME, queryMoreDimensions);
        }

        // NOT EXISTANT CODES
        QueryVersion queryMoreCodes = null;
        {
            QueryVersionMock queryMock = buildQueryVersionMockSimpleWithFixedDatasetVersion("QUERY_03");
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "D1_C01", "MADE_UP_CODE"));
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "D1_C02"));
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2010", "2011"));
            queryMoreCodes = createQueryVersionFromTemplate(queryMock);
            registerQueryVersionMock(QUERY_VERSION_32_CHECK_COMPAT_DATASET_86_MORE_CODES_NAME, queryMoreCodes);
        }

        return new MockDescriptor(datasetVersion, queryLessDimensions, queryMoreDimensions, queryMoreCodes);
    }

    private static MockDescriptor getDatasetVersion87WithNoTemporalDimension() {
        DatasetVersionMock datasetVersionMock = new DatasetVersionMock();
        addCodesToDimensionCoverage(datasetVersionMock, "DIM_01", "D1_C01", "D1_C02", "D1_C03");
        addCodesToDimensionCoverage(datasetVersionMock, "DIM_02", "D2_C01", "D2_C02", "D2_C03");
        DatasetVersion datasetVersion = createDatasetVersionFromTemplate(datasetVersionMock);

        QueryVersion queryMoreDimensionsAutoInc = null;
        {
            QueryVersionMock queryMock = buildQueryVersionMockSimpleWithFixedDatasetVersion("QUERY_01");
            queryMock.setType(QueryTypeEnum.AUTOINCREMENTAL);
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "D1_C01"));
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "D2_C01"));
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2010", "2011"));
            queryMoreDimensionsAutoInc = createQueryVersionFromTemplate(queryMock);
            registerQueryVersionMock(QUERY_VERSION_34_CHECK_COMPAT_DATASET_87_INVALID_QUERY_TYPE_AUTOINC_NAME, queryMoreDimensionsAutoInc);
        }

        QueryVersion queryLatestData = null;
        {
            QueryVersionMock queryMock = buildQueryVersionMockSimpleWithFixedDatasetVersion("QUERY_02");
            queryMock.setType(QueryTypeEnum.LATEST_DATA);
            queryMock.setLatestDataNumber(5);
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "D1_C01"));
            queryMock.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "D2_C01"));
            queryLatestData = createQueryVersionFromTemplate(queryMock);
            registerQueryVersionMock(QUERY_VERSION_35_CHECK_COMPAT_DATASET_87_INVALID_QUERY_TYPE_LATEST_DATA_NAME, queryLatestData);
        }

        return new MockDescriptor(datasetVersion, queryMoreDimensionsAutoInc, queryLatestData);
    }

    private static DatasetVersion getDatasetVersion88PublishedWithCategorisations() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.PUBLISHED);
        datasetVersion.addCategorisation(CategorisationMockFactory.createCategorisation("cat_data_1001", "category01", datasetVersion));
        datasetVersion.addCategorisation(CategorisationMockFactory.createCategorisation("cat_data_1002", "category02", datasetVersion));
        datasetVersion.addCategorisation(CategorisationMockFactory.createCategorisation("cat_data_1003", "category03", datasetVersion));
        return datasetVersion;
    }

    public static DatasetVersion getDatasetVersion89WithOneDatasource() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DRAFT);
        return datasetVersion;
    }

    public static DatasetVersion getDatasetVersion90WithTwoDatasources() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(2);
        datasetVersion.setDatasetRepositoryId(StatisticalResourcesPersistedDoMocks.mockString(10));
        datasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        datasetVersion.addDatasource(DatasourceMockFactory.generateSimpleDatasource());
        datasetVersion.addDatasource(DatasourceMockFactory.generateSimpleDatasource());

        return datasetVersion;
    }

    public static MockDescriptor getDatasetVersion91SingleVersionPublishedNotVisibleInNotVisiblePubAndUsedByNotVisibleQuery() {
        DateTime datasetValidFrom = new DateTime().plusDays(2);
        DatasetVersionMock datasetVersion = buildDatasetVersionWithSequenceAndVersion(1, StatisticalResourcesMockFactory.INIT_VERSION);
        datasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(datasetValidFrom);
        addCodesToDimensionCoverage(datasetVersion, "DIM_01", "D1_C01", "D1_C02", "D1_C03");
        addCodesToDimensionCoverage(datasetVersion, "DIM_02", "D2_C01", "D2_C02", "D2_C03");
        addCodesToDimensionCoverage(datasetVersion, "TIME_PERIOD", "2010", "2011");
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersion);
        fillDatasetVersionInStatusWithGeneratedDatasource(datasetVersion, ProcStatusEnum.PUBLISHED);

        PublicationVersionMock publicationVersion = PublicationVersionMockFactory.buildPublishedReadyPublicationVersion(new PublicationMock(), StatisticalResourcesMockFactory.INIT_VERSION,
                datasetValidFrom.plusDays(1), null, true);
        PublicationVersionMockFactory.createDatasetCubeElementLevel(publicationVersion, datasetVersion.getDataset());
        PublicationVersionMockFactory.createPublicationVersionInStatus(publicationVersion, ProcStatusEnum.PUBLISHED);
        registerPublicationVersionMock(PUBLICATION_VERSION_93_NOT_VISIBLE_HAS_PART_NOT_VISIBLE_DATASET_VERSION_NAME, publicationVersion);

        QueryVersionMock queryMock = new QueryVersionMock();
        queryMock.setFixedDatasetVersion(datasetVersion);
        QueryVersionMockFactory.buildSelectionItemWithDimensionAndCodes("DIM_01", "D1_C01", "D1_C02");
        QueryVersionMockFactory.buildSelectionItemWithDimensionAndCodes("DIM_02", "D2_C01");
        QueryVersionMockFactory.buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2011");
        queryMock.getLifeCycleStatisticalResource().setValidFrom(datasetValidFrom.plusDays(1));
        QueryVersionMockFactory.createQueryVersionInStatus(queryMock, ProcStatusEnum.PUBLISHED);
        registerQueryVersionMock(QUERY_VERSION_48_NOT_VISIBLE_REQUIRES_DATASET_VERSION_NOT_VISIBLE_NAME, queryMock);

        return new MockDescriptor(datasetVersion, publicationVersion, queryMock);
    }

    public static String getDatasetVersion97NotVisibleReplacedByDataset98NotVisibleName() {
        return DATASET_VERSION_97_NOT_VISIBLE_REPLACED_BY_DATASET_98_NOT_VISIBLE_NAME;
    }

    public static MockDescriptor getDatasetVersion97NotVisibleReplacedByDataset98NotVisible() {
        DateTime datasetValidFrom = new DateTime().plusDays(2);
        DatasetVersionMock datasetVersion = buildDatasetVersionWithSequenceAndVersion(1, StatisticalResourcesMockFactory.INIT_VERSION);
        datasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(datasetValidFrom);
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersion);
        fillDatasetVersionInStatusWithGeneratedDatasource(datasetVersion, ProcStatusEnum.PUBLISHED);

        DateTime datasetReplacesValidFrom = new DateTime().plusDays(3);
        DatasetVersionMock datasetReplacesVersion = buildDatasetVersionWithSequenceAndVersion(2, StatisticalResourcesMockFactory.INIT_VERSION);
        datasetReplacesVersion.getSiemacMetadataStatisticalResource().setValidFrom(datasetReplacesValidFrom);
        datasetReplacesVersion.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesDoMocks.mockDatasetVersionRelated(datasetVersion));
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetReplacesVersion);
        fillDatasetVersionInStatusWithGeneratedDatasource(datasetReplacesVersion, ProcStatusEnum.PUBLISHED);
        registerDatasetVersionMock(DATASET_VERSION_98_NOT_VISIBLE_REPLACES_DATASET_97_NOT_VISIBLE_NAME, datasetReplacesVersion);

        return new MockDescriptor(datasetVersion, datasetReplacesVersion);
    }

    public static DatasetVersion getDatasetVersion99NotVisibleSingleVersion() {
        DateTime datasetValidFrom = new DateTime().plusDays(2);
        DatasetVersionMock datasetVersion = buildDatasetVersionWithSequenceAndVersion(1, StatisticalResourcesMockFactory.INIT_VERSION);
        datasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(datasetValidFrom);
        getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(datasetVersion);
        fillDatasetVersionInStatusWithGeneratedDatasource(datasetVersion, ProcStatusEnum.PUBLISHED);
        return datasetVersion;
    }

    public static DatasetVersion getDatasetVersion100WithStatisticOfficiality() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);

        StatisticOfficiality statisticOfficiality = new StatisticOfficiality();
        statisticOfficiality.setDescription(StatisticalResourcesDoMocks.mockInternationalString());
        statisticOfficiality.setIdentifier("STAT_OFFICIALITY_PRIVATE");

        datasetVersion.setStatisticOfficiality(statisticOfficiality);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion101ToDeleteWithPreviousVersion() {
        DatasetVersion datasetVersionToReplace = createDatasetVersionWithSequenceAndVersion(1, StatisticalResourcesMockFactory.INIT_VERSION);
        prepareToVersioning(datasetVersionToReplace);

        DatasetVersion datasetVersion = createDatasetVersionWithSequenceAndVersion(1, StatisticalResourcesMockFactory.SECOND_VERSION);
        registerDatasetVersionMock(DATASET_VERSION_101_TO_DELETE_WITH_PREVIOUS_VERSION_NAME, datasetVersion);

        datasetVersion.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersionToReplace));
        datasetVersionToReplace.getSiemacMetadataStatisticalResource().setIsReplacedByVersion(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(datasetVersion));
        fillDatasetVersionInStatusWithGeneratedDatasource(datasetVersion, ProcStatusEnum.DRAFT);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion102PreparedToPublishWithAttributeValues() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);
        prepareToPublished(datasetVersion);

        datasetVersion.getAttributesCoverage().clear();
        datasetVersion.getAttributesCoverage().addAll(mockAttributeValuesWithIdentifiers(datasetVersion, "attr1", "value-a1-1", "value-a1-2"));
        datasetVersion.getAttributesCoverage().addAll(mockAttributeValuesWithIdentifiers(datasetVersion, "attr2", "value-a2-1", "value-a2-2"));
        datasetVersion.getAttributesCoverage().addAll(mockAttributeValuesWithIdentifiers(datasetVersion, "attr3", "value-a3-1"));

        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion103NoKeepDataPreparedToPublishWithPreviousVersion() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequenceAndVersion(1, SECOND_VERSION);
        datasetVersion.setKeepAllData(Boolean.FALSE);

        prepareToPublished(datasetVersion);

        fillOptionalExternalItems(datasetVersion);
        fillOptionalRelatedResourcesPublished(datasetVersion);

        datasetVersion.getSiemacMetadataStatisticalResource().getReplacesVersion().getDatasetVersion().setKeepAllData(Boolean.FALSE);

        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion104NoKeepDataPreparedToPublishInitialVersion() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequenceAndVersion(1, INIT_VERSION);
        datasetVersion.setKeepAllData(Boolean.FALSE);
        prepareToPublished(datasetVersion);

        fillOptionalExternalItems(datasetVersion);

        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion105MaximumVersionReached() {
        DatasetVersion datasetVersion = createDatasetVersionInSpecificOperation(OPERATION_03_CODE, 1);
        fillAsPublished(datasetVersion);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.MAXIMUM_VERSION_AVAILABLE);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion106MaximumMinorVersionReached() {
        DatasetVersion datasetVersion = createDatasetVersionInSpecificOperation(OPERATION_03_CODE, 1);
        fillAsPublished(datasetVersion);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.MAXIMUM_MINOR_VERSION_AVAILABLE);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion107DraftNotInitialVersionWithoutDatasources() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DRAFT);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.NOT_INITIAL_VERSION);
        datasetVersion.removeAllDatasources();
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion108PublishedInitialVersionWithoutDatasources() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.PUBLISHED);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        datasetVersion.removeAllDatasources();
        return datasetVersion;
    }
    private static DatasetVersion getDatasetVersion109DraftInitialVersionWithoutDatasources() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DRAFT);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        datasetVersion.removeAllDatasources();
        return datasetVersion;
    }
    private static DatasetVersion getDatasetVersion110ProductionValidationInitialVersionWithoutDatasources() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.PRODUCTION_VALIDATION);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        datasetVersion.removeAllDatasources();
        return datasetVersion;
    }
    private static DatasetVersion getDatasetVersion111DiffusionValidationInitialVersionWithoutDatasources() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DIFFUSION_VALIDATION);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        datasetVersion.removeAllDatasources();
        return datasetVersion;
    }
    private static DatasetVersion getDatasetVersion112ValidationRejectedInitialVersionWithoutDatasourcesName() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.VALIDATION_REJECTED);
        datasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        datasetVersion.removeAllDatasources();
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion113DatabaseTypeBasic() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);
        datasetVersion.setRelatedDsd(StatisticalResourcesDoMocks.mockDsdExternalItem());
        datasetVersion.setDataSourceType(DataSourceTypeEnum.DATABASE);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion114DatabaseDatasetWithDatasource() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);
        datasetVersion.setRelatedDsd(StatisticalResourcesDoMocks.mockDsdExternalItem());
        datasetVersion.setDataSourceType(DataSourceTypeEnum.DATABASE);
        datasetVersion.addDatasource(DatasourceMockFactory.generateSimpleDatasource());

        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion115DatabaseTypeBasic() {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(1);
        datasetVersion.setRelatedDsd(StatisticalResourcesDoMocks.mockDsdExternalItem());
        datasetVersion.setDataSourceType(DataSourceTypeEnum.DATABASE);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion116ProductionValidationDatabaseDataset() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.PRODUCTION_VALIDATION);
        datasetVersion.setDataSourceType(DataSourceTypeEnum.DATABASE);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion117DifussionValidationDataset() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DIFFUSION_VALIDATION);
        datasetVersion.setDataSourceType(DataSourceTypeEnum.DATABASE);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion118PublishedDatabaseDataset() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.PUBLISHED);
        datasetVersion.setDataSourceType(DataSourceTypeEnum.DATABASE);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion119DraftDatabaseDataset() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DRAFT);
        datasetVersion.getDatasources().clear();
        datasetVersion.setDataSourceType(DataSourceTypeEnum.DATABASE);
        return datasetVersion;
    }

    private static DatasetVersion getDatasetVersion120ValidationRejectedDatabaseDataset() {
        DatasetVersion datasetVersion = createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.VALIDATION_REJECTED);
        datasetVersion.getDatasources().clear();
        datasetVersion.setDataSourceType(DataSourceTypeEnum.DATABASE);
        return datasetVersion;
    }

    // -----------------------------------------------------------------
    // BUILDERS
    // -----------------------------------------------------------------

    private static DatasetVersion createDatasetVersionWithSequence(Integer sequentialId) {
        return createDatasetVersionWithSequenceAndVersion(sequentialId, StatisticalResourcesMockFactory.INIT_VERSION);
    }

    private static DatasetVersion createDatasetVersionWithSequenceAndVersion(Integer sequentialId, String version) {
        return createDatasetVersionFromTemplate(buildDatasetVersionWithSequenceAndVersion(sequentialId, version));
    }

    private static DatasetVersionMock buildDatasetVersionWithSequenceAndVersion(Integer sequentialId, String version) {
        DatasetVersionMock template = new DatasetVersionMock();
        template.setSequentialId(sequentialId);
        template.setVersionLogic(version);
        return template;
    }

    private static DatasetVersion createDatasetVersionEmpty() {
        return getStatisticalResourcesPersistedDoMocks().mockDatasetVersion();
    }

    private static DatasetVersion createDatasetVersionInSpecificOperation(String operationCode, Integer sequentialId) {
        DatasetVersionMock template = new DatasetVersionMock();
        template.setSequentialId(sequentialId);
        template.setStatisticalOperationCode(operationCode);

        return createDatasetVersionFromTemplate(template);
    }

    private static DatasetVersion createDatasetVersionFromTemplate(DatasetVersionMock template) {
        return getStatisticalResourcesPersistedDoMocks().mockDatasetVersion(template);
    }

    public static DatasetVersion createDatasetVersionInStatusWithGeneratedDatasource(int sequentialId, ProcStatusEnum procStatus) {
        DatasetVersion datasetVersion = createDatasetVersionWithSequence(sequentialId);
        return fillDatasetVersionInStatusWithGeneratedDatasource(datasetVersion, procStatus);
    }

    public static DatasetVersion fillDatasetVersionInStatusWithGeneratedDatasource(DatasetVersion datasetVersion, ProcStatusEnum procStatus) {
        datasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(procStatus);
        datasetVersion.addDatasource(DatasourceMockFactory.generateSimpleDatasource());

        switch (procStatus) {
            case DRAFT:
                DatasetLifecycleTestUtils.prepareToProductionValidation(datasetVersion);
                break;
            case PRODUCTION_VALIDATION:
                DatasetLifecycleTestUtils.fillAsProductionValidation(datasetVersion);
                break;
            case DIFFUSION_VALIDATION:
                DatasetLifecycleTestUtils.fillAsDiffusionValidation(datasetVersion);
                break;
            case VALIDATION_REJECTED:
                DatasetLifecycleTestUtils.fillAsValidationRejected(datasetVersion);
                break;
            case PUBLISHED:
                DatasetLifecycleTestUtils.fillAsPublished(datasetVersion);
                break;
            default:
                break;
        }
        return datasetVersion;
    }

    private static PublicationVersion buildDraftPublicationWithDatasetInFirstLevel(Integer sequentialId, Dataset dataset) {
        PublicationVersion publicationVersion = PublicationVersionMockFactory.buildPublicationVersionDraft(sequentialId);

        ElementLevel elementLevel01 = PublicationVersionMockFactory.createDatasetCubeElementLevel(publicationVersion, dataset);
        elementLevel01.setOrderInLevel(Long.valueOf(1));

        return publicationVersion;
    }

    private static PublicationVersion buildDraftPublicationWithDatasetNotInFirstLevel(Integer sequentialId, Dataset dataset) {
        PublicationVersion publicationVersion = PublicationVersionMockFactory.buildPublicationVersionDraft(sequentialId);

        ElementLevel elementLevel01 = PublicationVersionMockFactory.createChapterElementLevel(publicationVersion);
        elementLevel01.setOrderInLevel(Long.valueOf(1));

        ElementLevel elementLevel01_01 = PublicationVersionMockFactory.createDatasetCubeElementLevel(publicationVersion, dataset, elementLevel01);
        elementLevel01_01.setOrderInLevel(Long.valueOf(1));

        return publicationVersion;
    }

    private static PublicationVersion buildDraftPublicationWithDatasetInMultipleCubes(Integer sequentialId, Dataset dataset) {
        PublicationVersion publicationVersion = PublicationVersionMockFactory.buildPublicationVersionDraft(sequentialId);

        ElementLevel elementLevel01 = PublicationVersionMockFactory.createDatasetCubeElementLevel(publicationVersion, dataset);
        elementLevel01.setOrderInLevel(Long.valueOf(1));

        ElementLevel elementLevel02 = PublicationVersionMockFactory.createDatasetCubeElementLevel(publicationVersion, dataset);
        elementLevel02.setOrderInLevel(Long.valueOf(2));

        ElementLevel elementLevel03 = PublicationVersionMockFactory.createDatasetCubeElementLevel(publicationVersion, dataset);
        elementLevel03.setOrderInLevel(Long.valueOf(3));

        return publicationVersion;
    }

    public static DatasetVersionMock buildSimpleVersion(DatasetMock dataset, String initVersion) {
        DatasetVersionMock datasetVersionMock = new DatasetVersionMock();
        datasetVersionMock.setDataset(dataset);
        datasetVersionMock.setVersionLogic(initVersion);
        return datasetVersionMock;
    }

    public static DatasetVersionMock buildVersion(String statOper, int sequentialId, String initVersion) {
        DatasetVersionMock datasetVersionMock = new DatasetVersionMock();
        datasetVersionMock.setStatisticalOperationCode(statOper);
        datasetVersionMock.setSequentialId(sequentialId);
        datasetVersionMock.setVersionLogic(initVersion);
        return datasetVersionMock;
    }

    // -----------------------------------------------------------------
    // PRIVATE UTILS
    // -----------------------------------------------------------------

    public static void addCodesToDimensionCoverage(DatasetVersion datasetVersion, String dimensionId, String... codes) {
        for (String code : codes) {
            datasetVersion.addDimensionsCoverage(new CodeDimension(dimensionId, code));
        }
    }

    // -----------------------------------------------------------------
    // LIFE CYCLE PREPARATIONS
    // -----------------------------------------------------------------

    private static void fillOptionalRelatedResourcesNotPublished(DatasetVersion datasetVersion) {
        datasetVersion.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(createDatasetVersionWithSequence(2)));
    }

    private static void fillOptionalRelatedResourcesPublished(DatasetVersion datasetVersion) {
        DateTime currentValidFrom = datasetVersion.getSiemacMetadataStatisticalResource().getValidFrom();

        DatasetVersionMock replacesVersionTemplate = new DatasetVersionMock();
        replacesVersionTemplate.setSequentialId(1);
        replacesVersionTemplate.setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        replacesVersionTemplate.setDataset(datasetVersion.getDataset());
        replacesVersionTemplate.getSiemacMetadataStatisticalResource().setValidFrom(currentValidFrom.minusDays(3));
        replacesVersionTemplate.getSiemacMetadataStatisticalResource().setPublicationDate(currentValidFrom.minusDays(3));

        DatasetVersion previousDatasetVersion = createDatasetVersionFromTemplate(replacesVersionTemplate);

        DatasetLifecycleTestUtils.prepareToVersioning(previousDatasetVersion);

        datasetVersion.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(previousDatasetVersion));

        DatasetVersionMock replacesTemplate = new DatasetVersionMock();
        replacesTemplate.setSequentialId(2);
        replacesVersionTemplate.setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        replacesTemplate.getSiemacMetadataStatisticalResource().setValidFrom(currentValidFrom.minusDays(3));
        replacesTemplate.getSiemacMetadataStatisticalResource().setPublicationDate(currentValidFrom.minusDays(3));
        DatasetVersion replacesDatasetVersion = createDatasetVersionFromTemplate(replacesTemplate);

        prepareToVersioning(replacesDatasetVersion);

        datasetVersion.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockDatasetVersionRelated(replacesDatasetVersion));
    }

    private static void fillOptionalExternalItems(DatasetVersion datasetVersion) {
        LifecycleTestUtils.fillOptionalExternalItemsSiemacResource(datasetVersion);

        datasetVersion.getStatisticalUnit().clear();
        datasetVersion.addStatisticalUnit(StatisticalResourcesPersistedDoMocks.mockConceptExternalItem());
        datasetVersion.addStatisticalUnit(StatisticalResourcesPersistedDoMocks.mockConceptExternalItem());

        datasetVersion.addCategorisation(CategorisationMockFactory.createCategorisation("cat_data_1", "category01", datasetVersion));
        datasetVersion.addCategorisation(CategorisationMockFactory.createCategorisation("cat_data_2", "category02", datasetVersion));
        datasetVersion.addCategorisation(CategorisationMockFactory.createCategorisation("cat_data_3", "category03", datasetVersion));
    }

}
