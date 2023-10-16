package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import static org.siemac.metamac.statistical.resources.core.utils.MultidatasetLifecycleTestUtils.prepareToDiffusionValidation;
import static org.siemac.metamac.statistical.resources.core.utils.MultidatasetLifecycleTestUtils.prepareToProductionValidation;
import static org.siemac.metamac.statistical.resources.core.utils.MultidatasetLifecycleTestUtils.prepareToPublished;
import static org.siemac.metamac.statistical.resources.core.utils.MultidatasetLifecycleTestUtils.prepareToValidationRejected;
import static org.siemac.metamac.statistical.resources.core.utils.MultidatasetLifecycleTestUtils.prepareToVersioning;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_33_DRAFT_USED_IN_MULTIDATASET_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_34_PRODUCTION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_35_DIFFUSION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_36_VALIDATION_REJECTED_USED_IN_MULTIDATASET_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_37_PUBLISHED_NOT_VISIBLE_USED_IN_MULTIDATASET_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createDatasetVersionPublishedLastVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createPublishedAndDraftVersionsForDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createPublishedAndNotVisibleVersionsForDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createTwoPublishedVersionsForDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetCubeMockFactory.MULTIDATASET_CUBE_06_EMPTY_IN_MULTIDATASET_VERSION_90_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_04_STRUCTURED_WITH_2_MULTIDATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_06_C1_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_21_DRAFT_USED_IN_MULTIDATASET_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_22_PRODUCTION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_23_DIFFUSION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_24_VALIDATION_REJECTED_USED_IN_MULTIDATASET_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_25_PUBLISHED_NOT_VISIBLE_USED_IN_MULTIDATASET_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.createPublishedQueryLinkedToDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.buildQueryVersionMockSimpleWithFixedDatasetVersion;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockDescriptor;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockProvider;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetCube;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.utils.LifecycleTestUtils;
import org.siemac.metamac.statistical.resources.core.utils.MultidatasetLifecycleTestUtils;
import org.siemac.metamac.statistical.resources.core.utils.mocks.MultidatasetMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.MultidatasetVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

@MockProvider
@SuppressWarnings("unused")
public class MultidatasetVersionMockFactory extends StatisticalResourcesMockFactory<MultidatasetVersion> {

    public static final String                    MULTIDATASET_VERSION_01_BASIC_NAME                                                                                  = "MULTIDATASET_VERSION_01_BASIC";

    public static final String                    MULTIDATASET_VERSION_02_BASIC_NAME                                                                                  = "MULTIDATASET_VERSION_02_BASIC";

    public static final String                    MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME                                                                    = "MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03";

    public static final String                    MULTIDATASET_VERSION_03_C1_FOR_MULTIDATASET_03_NAME                                                                 = "MULTIDATASET_VERSION_03_C1_FOR_MULTIDATASET_03";

    public static final String                    MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME                                                   = "MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION";

    public static final String                    MULTIDATASET_VERSION_04_C1_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME                                                = "MULTIDATASET_VERSION_04_C1_FOR_MULTIDATASET_03_AND_LAST_VERSION";

    public static final String                    MULTIDATASET_VERSION_05_OPERATION_0001_CODE_000001_NAME                                                             = "MULTIDATASET_VERSION_05_OPERATION_0001_CODE_000001";

    public static final String                    MULTIDATASET_VERSION_06_OPERATION_0001_CODE_000002_NAME                                                             = "MULTIDATASET_VERSION_06_OPERATION_0001_CODE_000002";

    public static final String                    MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME                                                             = "MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003";

    public static final String                    MULTIDATASET_VERSION_08_OPERATION_0002_CODE_000001_NAME                                                             = "MULTIDATASET_VERSION_08_OPERATION_0002_CODE_000001";

    public static final String                    MULTIDATASET_VERSION_09_OPERATION_0002_CODE_000002_NAME                                                             = "MULTIDATASET_VERSION_09_OPERATION_0002_CODE_000002";

    public static final String                    MULTIDATASET_VERSION_10_OPERATION_0002_CODE_000003_NAME                                                             = "MULTIDATASET_VERSION_10_OPERATION_0002_CODE_000003";

    public static final String                    MULTIDATASET_VERSION_11_OPERATION_0002_CODE_MAX_NAME                                                                = "MULTIDATASET_VERSION_11_OPERATION_0002_CODE_MAX";

    public static final String                    MULTIDATASET_VERSION_12_DRAFT_NAME                                                                                  = "MULTIDATASET_VERSION_12_DRAFT";

    public static final String                    MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME                                                                  = "MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION";

    public static final String                    MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME                                                                   = "MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION";

    public static final String                    MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME                                                                    = "MULTIDATASET_VERSION_15_VALIDATION_REJECTED";

    public static final String                    MULTIDATASET_VERSION_16_PUBLISHED_NAME                                                                              = "MULTIDATASET_VERSION_16_PUBLISHED";

    public static final String                    MULTIDATASET_VERSION_17_WITH_STRUCTURE_FOR_MULTIDATASET_VERSION_04_NAME                                             = "MULTIDATASET_VERSION_17_WITH_STRUCTURE_FOR_MULTIDATASET_VERSION_04";

    public static final String                    MULTIDATASET_VERSION_18_WITH_STRUCTURE_FOR_MULTIDATASET_VERSION_04_AND_LAST_VERSION_NAME                            = "MULTIDATASET_VERSION_18_WITH_STRUCTURE_FOR_MULTIDATASET_VERSION_04_AND_LAST_VERSION";

    public static final String                    MULTIDATASET_VERSION_19_WITH_STRUCTURE_PRODUCTION_VALIDATION_NAME                                                   = "MULTIDATASET_VERSION_19_WITH_STRUCTURE_PRODUCTION_VALIDATION";

    public static final String                    MULTIDATASET_VERSION_20_WITH_STRUCTURE_DIFFUSION_VALIDATION_NAME                                                    = "MULTIDATASET_VERSION_20_WITH_STRUCTURE_DIFFUSION_VALIDATION";

    public static final String                    MULTIDATASET_VERSION_21_WITH_STRUCTURE_VALIDATION_REJECTED_NAME                                                     = "MULTIDATASET_VERSION_21_WITH_STRUCTURE_VALIDATION_REJECTED";

    public static final String                    MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME                                                           = "MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT";

    public static final String                    MULTIDATASET_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME                                           = "MULTIDATASET_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION";

    public static final String                    MULTIDATASET_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME                                            = "MULTIDATASET_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION";

    public static final String                    MULTIDATASET_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME                                             = "MULTIDATASET_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED";

    public static final String                    MULTIDATASET_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME                                                       = "MULTIDATASET_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED";

    public static final String                    MULTIDATASET_VERSION_27_V1_PUBLISHED_FOR_MULTIDATASET_05_NAME                                                       = "MULTIDATASET_VERSION_27_V1_PUBLISHED_FOR_MULTIDATASET_05";

    public static final String                    MULTIDATASET_VERSION_27_V1_PUBLISHED_FOR_MULTIDATASET_05_C1_NAME                                                    = "MULTIDATASET_VERSION_27_V1_PUBLISHED_FOR_MULTIDATASET_05_C1";

    public static final String                    MULTIDATASET_VERSION_28_V2_PUBLISHED_FOR_MULTIDATASET_05_NAME                                                       = "MULTIDATASET_VERSION_28_V2_PUBLISHED_FOR_MULTIDATASET_05";

    public static final String                    MULTIDATASET_VERSION_28_V2_PUBLISHED_FOR_MULTIDATASET_05_C1_NAME                                                    = "MULTIDATASET_VERSION_28_V2_PUBLISHED_FOR_MULTIDATASET_05_C1";

    public static final String                    MULTIDATASET_VERSION_29_V3_PUBLISHED_FOR_MULTIDATASET_05_NAME                                                       = "MULTIDATASET_VERSION_29_V3_PUBLISHED_FOR_MULTIDATASET_05";

    public static final String                    MULTIDATASET_VERSION_29_V3_PUBLISHED_FOR_MULTIDATASET_05_C1_NAME                                                    = "MULTIDATASET_VERSION_29_V3_PUBLISHED_FOR_MULTIDATASET_05_C1";

    public static final String                    MULTIDATASET_VERSION_30_V1_PUBLISHED_FOR_MULTIDATASET_06_NAME                                                       = "MULTIDATASET_VERSION_30_V1_PUBLISHED_FOR_MULTIDATASET_06";

    public static final String                    MULTIDATASET_VERSION_30_V1_PUBLISHED_FOR_MULTIDATASET_06_C1_NAME                                                    = "MULTIDATASET_VERSION_30_V1_PUBLISHED_FOR_MULTIDATASET_06_C1";

    public static final String                    MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME                                            = "MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06";

    public static final String                    MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_C1_NAME                                         = "MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_C1";

    public static final String                    MULTIDATASET_VERSION_32_DRAFT_NOT_READY_NAME                                                                        = "MULTIDATASET_VERSION_32_DRAFT_NOT_READY";

    public static final String                    MULTIDATASET_VERSION_33_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME                                                  = "MULTIDATASET_VERSION_33_DRAFT_READY_FOR_PRODUCTION_VALIDATION";

    public static final String                    MULTIDATASET_VERSION_34_VERSION_RATIONALE_TYPE_MINOR_ERRATA_NAME                                                    = "MULTIDATASET_VERSION_34_VERSION_RATIONALE_TYPE_MINOR_ERRATA";

    public static final String                    MULTIDATASET_VERSION_35_NEXT_VERSION_NOT_SCHEDULED_DATE_FILLED_NAME                                                 = "MULTIDATASET_VERSION_35_NEXT_VERSION_NOT_SCHEDULED_DATE_FILLED";

    public static final String                    MULTIDATASET_VERSION_36_PRODUCTION_VALIDATION_NOT_READY_NAME                                                        = "MULTIDATASET_VERSION_36_PRODUCTION_VALIDATION_NOT_READY";

    public static final String                    MULTIDATASET_VERSION_37_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME                                   = "MULTIDATASET_VERSION_37_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION";

    public static final String                    MULTIDATASET_VERSION_38_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME                                    = "MULTIDATASET_VERSION_38_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED";

    public static final String                    MULTIDATASET_VERSION_39_PUBLISHED_WITH_NO_ROOT_MAINTAINER_NAME                                                      = "MULTIDATASET_VERSION_39_PUBLISHED_WITH_NO_ROOT_MAINTAINER";

    public static final String                    MULTIDATASET_VERSION_40_PREPARED_TO_PUBLISH_EXTERNAL_ITEM_FULL_NAME                                                 = "MULTIDATASET_VERSION_40_PREPARED_TO_PUBLISH_EXTERNAL_ITEM_FULL";

    public static final String                    MULTIDATASET_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42_NAME                                                = "MULTIDATASET_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42";

    public static final String                    MULTIDATASET_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41_NAME                                                      = "MULTIDATASET_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41";

    public static final String                    MULTIDATASET_VERSION_43_DRAFT_HAS_PART_DATASET_VERSION_85_FIRST_LEVEL_NAME                                          = "MULTIDATASET_VERSION_43_DRAFT_HAS_PART_DATASET_VERSION_85_FIRST_LEVEL";

    public static final String                    MULTIDATASET_VERSION_44_DRAFT_HAS_PART_DATASET_VERSION_85_NO_FIRST_LEVEL_NAME                                       = "MULTIDATASET_VERSION_44_DRAFT_HAS_PART_DATASET_VERSION_85_NO_FIRST_LEVEL";

    public static final String                    MULTIDATASET_VERSION_45_DRAFT_HAS_PART_DATASET_VERSION_85_MULTI_CUBE_NAME                                           = "MULTIDATASET_VERSION_45_DRAFT_HAS_PART_DATASET_VERSION_85_MULTI_CUBE";

    public static final String                    MULTIDATASET_VERSION_46_PUBLISHED_V01_FOR_MULTIDATASET_07_NAME                                                      = "MULTIDATASET_VERSION_46_PUBLISHED_V01_FOR_MULTIDATASET_07";

    public static final String                    MULTIDATASET_VERSION_47_PUBLISHED_V02_FOR_MULTIDATASET_07_NAME                                                      = "MULTIDATASET_VERSION_47_PUBLISHED_V02_FOR_MULTIDATASET_07";

    public static final String                    MULTIDATASET_VERSION_48_PUBLISHED_V01_FOR_MULTIDATASET_08_NAME                                                      = "MULTIDATASET_VERSION_48_PUBLISHED_V01_FOR_MULTIDATASET_08";

    public static final String                    MULTIDATASET_VERSION_49_PUBLISHED_NOT_VISIBLE_V02_FOR_MULTIDATASET_08_NAME                                          = "MULTIDATASET_VERSION_49_PUBLISHED_NOT_VISIBLE_V02_FOR_MULTIDATASET_08";

    public static final String                    MULTIDATASET_VERSION_50_DRAFT_V01_FOR_MULTIDATASET_09_NAME                                                          = "MULTIDATASET_VERSION_50_DRAFT_V01_FOR_MULTIDATASET_09";

    public static final String                    MULTIDATASET_VERSION_51_PUBLISHED_V01_FOR_MULTIDATASET_10_NAME                                                      = "MULTIDATASET_VERSION_51_PUBLISHED_V01_FOR_MULTIDATASET_10";

    public static final String                    MULTIDATASET_VERSION_52_PUBLISHED_V02_FOR_MULTIDATASET_10_NAME                                                      = "MULTIDATASET_VERSION_52_PUBLISHED_V02_FOR_MULTIDATASET_10";

    public static final String                    MULTIDATASET_VERSION_53_PUBLISHED_V01_FOR_MULTIDATASET_11_NAME                                                      = "MULTIDATASET_VERSION_53_PUBLISHED_V01_FOR_MULTIDATASET_11";

    public static final String                    MULTIDATASET_VERSION_54_PUBLISHED_NOT_VISIBLE_V02_FOR_MULTIDATASET_11_NAME                                          = "MULTIDATASET_VERSION_54_PUBLISHED_NOT_VISIBLE_V02_FOR_MULTIDATASET_11";

    public static final String                    MULTIDATASET_VERSION_55_DRAFT_V01_FOR_MULTIDATASET_12_NAME                                                          = "MULTIDATASET_VERSION_55_DRAFT_V01_FOR_MULTIDATASET_12";

    public static final String                    MULTIDATASET_VERSION_56_PUBLISHED_V01_FOR_MULTIDATASET_13_NAME                                                      = "MULTIDATASET_VERSION_56_PUBLISHED_V01_FOR_MULTIDATASET_13";

    public static final String                    MULTIDATASET_VERSION_57_PUBLISHED_V02_FOR_MULTIDATASET_13_NAME                                                      = "MULTIDATASET_VERSION_57_PUBLISHED_V02_FOR_MULTIDATASET_13";

    public static final String                    MULTIDATASET_VERSION_58_PUBLISHED_V01_FOR_MULTIDATASET_14_NAME                                                      = "MULTIDATASET_VERSION_58_PUBLISHED_V01_FOR_MULTIDATASET_14";

    public static final String                    MULTIDATASET_VERSION_59_PUBLISHED_NOT_VISIBLE_V02_FOR_MULTIDATASET_14_NAME                                          = "MULTIDATASET_VERSION_59_PUBLISHED_NOT_VISIBLE_V02_FOR_MULTIDATASET_14";

    public static final String                    MULTIDATASET_VERSION_60_DRAFT_V01_FOR_MULTIDATASET_15_NAME                                                          = "MULTIDATASET_VERSION_60_DRAFT_V01_FOR_MULTIDATASET_15";

    public static final String                    MULTIDATASET_VERSION_61_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_10_NAME                                        = "MULTIDATASET_VERSION_61_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_10";

    public static final String                    MULTIDATASET_VERSION_62_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10_NAME                                               = "MULTIDATASET_VERSION_62_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10";

    public static final String                    MULTIDATASET_VERSION_63_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_11_NAME                                        = "MULTIDATASET_VERSION_63_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_11";

    public static final String                    MULTIDATASET_VERSION_64_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_11_NAME                                               = "MULTIDATASET_VERSION_64_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_11";

    public static final String                    MULTIDATASET_VERSION_65_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION__LINKED_TO_QUERY_11_NAME                               = "MULTIDATASET_VERSION_65_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION__LINKED_TO_QUERY_11";

    public static final String                    MULTIDATASET_VERSION_66_PUBLISHED_MULTI_VERSION_V01__LINKED_TO_QUERY_11_NAME                                        = "MULTIDATASET_VERSION_66_PUBLISHED_MULTI_VERSION_V01__LINKED_TO_QUERY_11";

    public static final String                    MULTIDATASET_VERSION_67_PUBLISHED_NOT_VISIBLE_MULTI_VERSION_V02__LINKED_TO_QUERY_11_NAME                            = "MULTIDATASET_VERSION_67_PUBLISHED_NOT_VISIBLE_MULTI_VERSION_V02__LINKED_TO_QUERY_11";

    public static final String                    MULTIDATASET_VERSION_68_DRAFT_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME                                                = "MULTIDATASET_VERSION_68_DRAFT_SINGLE_VERSION_LINKED_TO_QUERY_12";
    public static final String                    MULTIDATASET_VERSION_69_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME                                = "MULTIDATASET_VERSION_69_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION_LINKED_TO_QUERY_12";
    public static final String                    MULTIDATASET_VERSION_70_PUBLISHED_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME                                            = "MULTIDATASET_VERSION_70_PUBLISHED_SINGLE_VERSION_LINKED_TO_QUERY_12";

    public static final String                    MULTIDATASET_VERSION_71_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_DRAFT_LINKED_TO_QUERY_13_NAME               = "MULTIDATASET_VERSION_71_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_DRAFT_LINKED_TO_QUERY_13";
    public static final String                    MULTIDATASET_VERSION_72_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME       = "MULTIDATASET_VERSION_72_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_PUBLISHED_LINKED_TO_QUERY_14";
    public static final String                    MULTIDATASET_VERSION_73_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15_NAME                 = "MULTIDATASET_VERSION_73_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15";
    public static final String                    MULTIDATASET_VERSION_74_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15_NAME                     = "MULTIDATASET_VERSION_74_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15";

    public static final String                    MULTIDATASET_VERSION_75_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_LAST_LINKED_TO_QUERY_13_NAME    = "MULTIDATASET_VERSION_75_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_LAST_LINKED_TO_QUERY_13";
    public static final String                    MULTIDATASET_VERSION_76_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME = "MULTIDATASET_VERSION_76_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_PUBLISHED_LINKED_TO_QUERY_14";
    public static final String                    MULTIDATASET_VERSION_77_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15_NAME           = "MULTIDATASET_VERSION_77_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15";
    public static final String                    MULTIDATASET_VERSION_78_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15_NAME         = "MULTIDATASET_VERSION_78_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15";

    public static final String                    MULTIDATASET_VERSION_79_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__ONLY_LAST_LINKED_TO_QUERY_13_NAME               = "MULTIDATASET_VERSION_79_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__ONLY_LAST_LINKED_TO_QUERY_13";
    public static final String                    MULTIDATASET_VERSION_80_PREVIOUS_VERSION_V01_IN_PUB_WITH_TWO_PUBLISHED__ONLY_PREVIOUS_LINKED_TO_QUERY_14_NAME       = "MULTIDATASET_VERSION_80_PREVIOUS_VERSION_V01_IN_PUB_WITH_TWO_PUBLISHED__ONLY_PREVIOUS_LINKED_TO_QUERY_14";
    public static final String                    MULTIDATASET_VERSION_81_PREVIOUS_VERSION_V01_IN_PUB_WITH_TWO_PUBLISHED__BOTH_LINKED_TO_QUERY_15_NAME                = "MULTIDATASET_VERSION_81_PREVIOUS_VERSION_V01_IN_PUB_WITH_TWO_PUBLISHED__BOTH_LINKED_TO_QUERY_15";
    public static final String                    MULTIDATASET_VERSION_82_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__BOTH_LINKED_TO_QUERY_15_NAME                    = "MULTIDATASET_VERSION_82_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__BOTH_LINKED_TO_QUERY_15";

    public static final String                    MULTIDATASET_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME                                    = "MULTIDATASET_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL";
    public static final String                    MULTIDATASET_VERSION_84_PUBLISHED_FOR_MULTIDATASET_07_NAME                                                          = "MULTIDATASET_VERSION_84_PUBLISHED_FOR_MULTIDATASET_07_NAME";
    public static final String                    MULTIDATASET_VERSION_85_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION_EXTERNAL_ITEM_FULL_FOR_MULTIDATASET_07_NAME       = "MULTIDATASET_VERSION_85_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION_EXTERNAL_ITEM_FULL_FOR_MULTIDATASET_07_NAME";

    public static final String                    MULTIDATASET_VERSION_86_WITH_ELEMENT_LEVELS_NOT_PUBLISHED_NAME                                                      = "MULTIDATASET_VERSION_86_WITH_ELEMENT_LEVELS_NOT_PUBLISHED";
    public static final String                    MULTIDATASET_VERSION_87_WITH_RELATED_RESOURCES_NOT_PUBLISHED_NAME                                                   = "MULTIDATASET_VERSION_87_WITH_RELATED_RESOURCES_NOT_PUBLISHED";
    public static final String                    MULTIDATASET_VERSION_88_WITH_NO_CUBES_NAME                                                                          = "MULTIDATASET_VERSION_88_WITH_NO_CUBES";
    public static final String                    MULTIDATASET_VERSION_89_WITH_EMPTY_CHAPTER_NAME                                                                     = "MULTIDATASET_VERSION_89_WITH_EMPTY_CHAPTER";
    public static final String                    MULTIDATASET_VERSION_90_WITH_EMPTY_CUBE_NAME                                                                        = "MULTIDATASET_VERSION_90_WITH_EMPTY_CUBE";

    public static final String                    MULTIDATASET_VERSION_91_REPLACES_MULTIDATASET_92_NAME                                                               = "MULTIDATASET_VERSION_91_REPLACES_MULTIDATASET_92";
    public static final String                    MULTIDATASET_VERSION_92_IS_REPLACED_BY_MULTIDATASET_91_NAME                                                         = "MULTIDATASET_VERSION_92_IS_REPLACED_BY_MULTIDATASET_91";

    public static final String                    MULTIDATASET_VERSION_93_NOT_VISIBLE_HAS_PART_NOT_VISIBLE_DATASET_VERSION_NAME                                       = "MULTIDATASET_VERSION_93_NOT_VISIBLE_HAS_PART_NOT_VISIBLE_DATASET_VERSION";

    public static final String                    MULTIDATASET_VERSION_94_NOT_VISIBLE_NAME                                                                            = "MULTIDATASET_VERSION_94_NOT_VISIBLE";
    public static final String                    MULTIDATASET_VERSION_95_NOT_VISIBLE_IS_REPLACED_BY_MULTIDATASET_VERSION_96_NAME                                     = "MULTIDATASET_VERSION_95_NOT_VISIBLE_IS_REPLACED_BY_MULTIDATASET_VERSION_96";
    public static final String                    MULTIDATASET_VERSION_96_NOT_VISIBLE_REPLACES_MULTIDATASET_VERSION_95_NAME                                           = "MULTIDATASET_VERSION_96_NOT_VISIBLE_REPLACES_MULTIDATASET_VERSION_95";

    public static final String                    MULTIDATASET_VERSION_97_NOT_VISIBLE_HAS_PART_NOT_VISIBLE_QUERY_NAME                                                 = "MULTIDATASET_VERSION_97_NOT_VISIBLE_HAS_PART_NOT_VISIBLE_QUERY";

    public static final String                    MULTIDATASET_VERSION_98_TO_DELETE_WITH_PREVIOUS_VERSION_NAME                                                        = "MULTIDATASET_VERSION_98_TO_DELETE_WITH_PREVIOUS_VERSION";

    public static final String                    MULTIDATASET_VERSION_99_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10_NAME                                               = "MULTIDATASET_VERSION_99_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10";

    public static final String                    MULTIDATASET_VERSION_100_DRAFT_SINGLE_VERSION__LINKED_TO_DATASET_85_NAME                                            = "MULTIDATASET_VERSION_99_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10";

    public static final String                    MULTIDATASET_VERSION_101_LAST_VERSION_NOT_PUBLISHED_IS_PART_OF_PUBLICATIONS_NAME                                    = "MULTIDATASET_VERSION_101_LAST_VERSION_NOT_PUBLISHED_IS_PART_OF_PUBLICATIONS";

    public static final String                    MULTIDATASET_VERSION_102_LAST_VERSION_PUBLISHED_IS_PART_OF_PUBLICATIONS_NAME                                        = "MULTIDATASET_VERSION_102_LAST_VERSION_PUBLISHED_IS_PART_OF_PUBLICATIONS";

    private static MultidatasetVersionMockFactory instance                                                                                                            = null;

    private MultidatasetVersionMockFactory() {
    }

    public static MultidatasetVersionMockFactory getInstance() {
        if (instance == null) {
            instance = new MultidatasetVersionMockFactory();
        }
        return instance;
    }

    private static MultidatasetVersion getMultidatasetVersion01Basic() {
        return createMultidatasetVersion();
    }

    private static MultidatasetVersion getMultidatasetVersion02Basic() {
        return createMultidatasetVersion();
    }

    private static MockDescriptor getMultidatasetVersion03ForMultidataset03() {
        MockDescriptor pubMockDesc = getMultidatasetMockDescriptor(MultidatasetMockFactory.MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME);
        return new MockDescriptor(getMultidatasetVersionMock(MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME), pubMockDesc);
    }

    private static MockDescriptor getMultidatasetVersion04ForMultidataset03AndLastVersion() {
        MockDescriptor pubMockDesc = getMultidatasetMockDescriptor(MultidatasetMockFactory.MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME);
        return new MockDescriptor(getMultidatasetVersionMock(MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME), pubMockDesc);
    }

    private static MultidatasetVersion getMultidatasetVersion05Operation0001Code000001() {
        return createMultidatasetVersionInOperation(OPERATION_01_CODE, 1);
    }

    private static MultidatasetVersion getMultidatasetVersion06Operation0001Code000002() {
        return createMultidatasetVersionInOperation(OPERATION_01_CODE, 2);
    }

    private static MultidatasetVersion getMultidatasetVersion07Operation0001Code000003() {
        return createMultidatasetVersionInOperation(OPERATION_01_CODE, 3);
    }

    private static MultidatasetVersion getMultidatasetVersion08Operation0002Code000001() {
        return createMultidatasetVersionInOperation(OPERATION_02_CODE, 1);
    }

    private static MultidatasetVersion getMultidatasetVersion09Operation0002Code000002() {
        return createMultidatasetVersionInOperation(OPERATION_02_CODE, 2);
    }

    private static MultidatasetVersion getMultidatasetVersion10Operation0002Code000003() {
        return createMultidatasetVersionInOperation(OPERATION_02_CODE, 3);
    }

    private static MultidatasetVersion getMultidatasetVersion11Operation0002CodeMax() {
        return createMultidatasetVersionInOperation(OPERATION_01_CODE, 999999);
    }

    private static MultidatasetVersion getMultidatasetVersion12Draft() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion13ProductionValidation() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PRODUCTION_VALIDATION);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion14DiffusionValidation() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion15ValidationRejected() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.VALIDATION_REJECTED);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion16Published() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        MultidatasetLifecycleTestUtils.fillAsPublished(multidatasetVersion);
        return multidatasetVersion;
    }

    private static MockDescriptor getMultidatasetVersion17WithStructureForMultidatasetVersion04() {
        MockDescriptor multidataset = getMultidatasetMockDescriptor(MULTIDATASET_04_STRUCTURED_WITH_2_MULTIDATASET_VERSIONS_NAME);
        return new MockDescriptor(getMultidatasetVersionMock(MULTIDATASET_VERSION_17_WITH_STRUCTURE_FOR_MULTIDATASET_VERSION_04_NAME), multidataset);
    }

    private static MockDescriptor getMultidatasetVersion18WithStructureForMultidatasetVersion04AndLastVersion() {
        MockDescriptor multidataset = getMultidatasetMockDescriptor(MULTIDATASET_04_STRUCTURED_WITH_2_MULTIDATASET_VERSIONS_NAME);
        return new MockDescriptor(getMultidatasetVersionMock(MULTIDATASET_VERSION_18_WITH_STRUCTURE_FOR_MULTIDATASET_VERSION_04_AND_LAST_VERSION_NAME), multidataset);
    }

    private static MultidatasetVersion getMultidatasetVersion19WithStructureProductionValidation() {
        // General metadata
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(INIT_VERSION);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(1));
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PRODUCTION_VALIDATION);

        fillWithCubes(multidatasetVersion);

        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion20WithStructureDiffusionValidation() {
        // General metadata
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(INIT_VERSION);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(1));
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);

        fillWithCubes(multidatasetVersion);

        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion21WithStructureValidationRejected() {
        // General metadata
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(INIT_VERSION);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(1));
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.VALIDATION_REJECTED);

        fillWithCubes(multidatasetVersion);

        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion22WithComplexStructureDraft() {
        MultidatasetVersion multidatasetVersion = createComplexStructure();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion23WithComplexStructureProductionValidation() {
        MultidatasetVersion multidatasetVersion = createComplexStructure();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PRODUCTION_VALIDATION);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion24WithComplexStructureDiffusionValidation() {
        MultidatasetVersion multidatasetVersion = createComplexStructure();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion25WithComplexStructureValidationRejected() {
        MultidatasetVersion multidatasetVersion = createComplexStructure();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.VALIDATION_REJECTED);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion26WithComplexStructurePublished() {
        MultidatasetVersion multidatasetVersion = createComplexStructure();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        return multidatasetVersion;
    }

    private static MockDescriptor getMultidatasetVersion27V1PublishedForMultidataset05() {
        MockDescriptor multidataset = getMultidatasetMockDescriptor(MultidatasetMockFactory.MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME);
        return new MockDescriptor(getMultidatasetVersionMock(MULTIDATASET_VERSION_27_V1_PUBLISHED_FOR_MULTIDATASET_05_NAME), multidataset);
    }

    private static MockDescriptor getMultidatasetVersion28V2PublishedForMultidataset05() {
        MockDescriptor multidataset = getMultidatasetMockDescriptor(MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME);
        return new MockDescriptor(getMultidatasetVersionMock(MULTIDATASET_VERSION_28_V2_PUBLISHED_FOR_MULTIDATASET_05_NAME), multidataset);
    }

    private static MockDescriptor getMultidatasetVersion29V3PublishedForMultidataset05() {
        MockDescriptor multidataset = getMultidatasetMockDescriptor(MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME);
        return new MockDescriptor(getMultidatasetVersionMock(MULTIDATASET_VERSION_29_V3_PUBLISHED_FOR_MULTIDATASET_05_NAME), multidataset);
    }

    private static MockDescriptor getMultidatasetVersion30V1PublishedForMultidataset06() {
        MockDescriptor multidataset = getMultidatasetMockDescriptor(MULTIDATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME);
        return new MockDescriptor(getMultidatasetVersionMock(MULTIDATASET_VERSION_30_V1_PUBLISHED_FOR_MULTIDATASET_06_NAME), multidataset);
    }

    private static MockDescriptor getMultidatasetVersion31V2PublishedNoVisibleForMultidataset06() {
        MockDescriptor multidataset = getMultidatasetMockDescriptor(MULTIDATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME);
        return new MockDescriptor(getMultidatasetVersionMock(MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME), multidataset);
    }

    private static MockDescriptor getMultidatasetVersion31V2PublishedNoVisibleForMultidataset06C1() {
        MockDescriptor multidataset = getMultidatasetMockDescriptor(MULTIDATASET_06_C1_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME);
        return new MockDescriptor(getMultidatasetVersionMock(MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_C1_NAME), multidataset);
    }

    private static MultidatasetVersion getMultidatasetVersion32DraftNotReady() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion33DraftReadyForProductionValidation() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        prepareToProductionValidation(multidatasetVersion);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion34VersionRationaleTypeMinorErrata() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        prepareToProductionValidation(multidatasetVersion);
        multidatasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().add(new VersionRationaleType(VersionRationaleTypeEnum.MINOR_ERRATA));
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion35NextVersionNotScheduledDateFilled() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        prepareToProductionValidation(multidatasetVersion);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(10).toDate()));
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion36ProductionValidationNotReady() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        prepareToDiffusionValidation(multidatasetVersion);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion37ProductionValidationReadyForDiffusionValidation() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        prepareToDiffusionValidation(multidatasetVersion);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion38ProductionValidationReadyForValidationRejected() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        prepareToValidationRejected(multidatasetVersion);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion39PublishedWithNoRootMaintainer() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setMaintainer(StatisticalResourcesDoMocks.mockAgencyExternalItem("agency01", "ISTAC.agency01"));
        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion41PubNotVisibleReplacesPubVersion42() {
        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setSequentialId(1);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(1));
        MultidatasetVersion multidatasetVersion = createMultidatasetVersionFromTemplate(template);

        MultidatasetVersion multidatasetToReplace = createMultidatasetVersionWithSequenceAndVersion(2, INIT_VERSION);
        prepareToVersioning(multidatasetToReplace);
        registerMultidatasetVersionMock(MULTIDATASET_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41_NAME, multidatasetToReplace);

        multidatasetVersion.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetToReplace));
        multidatasetToReplace.getSiemacMetadataStatisticalResource().setIsReplacedBy(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetVersion));
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion83PreparedToPublishOnlyVersionExternalItemFull() {
        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setSequentialId(1);
        MultidatasetVersion multidatasetVersion = createMultidatasetVersionFromTemplate(template);

        fillOptionalExternalItems(multidatasetVersion);

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

        populateMultidatasetWithCubes(multidatasetVersion, datasets, queries);

        prepareToPublished(multidatasetVersion);

        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion86WithElementLevelsNotPublished() {
        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setSequentialId(1);
        MultidatasetVersion multidatasetVersion = createMultidatasetVersionFromTemplate(template);

        fillOptionalExternalItems(multidatasetVersion);

        // datasets
        Dataset datasetSingleVersionDraft = DatasetVersionMockFactory.createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DRAFT).getDataset();
        registerDatasetMock(DATASET_33_DRAFT_USED_IN_MULTIDATASET_VERSION_86_NAME, datasetSingleVersionDraft);

        Dataset datasetSingleVersionProductionValidation = DatasetVersionMockFactory.createDatasetVersionInStatusWithGeneratedDatasource(2, ProcStatusEnum.PRODUCTION_VALIDATION).getDataset();
        registerDatasetMock(DATASET_34_PRODUCTION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME, datasetSingleVersionProductionValidation);

        Dataset datasetSingleVersionDiffusionValidation = DatasetVersionMockFactory.createDatasetVersionInStatusWithGeneratedDatasource(3, ProcStatusEnum.DIFFUSION_VALIDATION).getDataset();
        registerDatasetMock(DATASET_35_DIFFUSION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME, datasetSingleVersionDiffusionValidation);

        Dataset datasetSingleVersionValidationRejected = DatasetVersionMockFactory.createDatasetVersionInStatusWithGeneratedDatasource(4, ProcStatusEnum.VALIDATION_REJECTED).getDataset();
        registerDatasetMock(DATASET_36_VALIDATION_REJECTED_USED_IN_MULTIDATASET_VERSION_86_NAME, datasetSingleVersionValidationRejected);

        Dataset datasetSingleVersionPublishedNotVisible = createDatasetVersionPublishedLastVersion(5, INIT_VERSION, new DateTime().plusDays(1)).getDataset();
        registerDatasetMock(DATASET_37_PUBLISHED_NOT_VISIBLE_USED_IN_MULTIDATASET_VERSION_86_NAME, datasetSingleVersionPublishedNotVisible);

        List<Dataset> datasets = Arrays.asList(datasetSingleVersionDraft, datasetSingleVersionProductionValidation, datasetSingleVersionDiffusionValidation, datasetSingleVersionValidationRejected,
                datasetSingleVersionPublishedNotVisible);

        // queries

        Query queryDraft = QueryVersionMockFactory.createQueryVersionInStatus(buildQueryVersionMockSimpleWithFixedDatasetVersion("Q01"), ProcStatusEnum.DRAFT).getQuery();
        registerQueryMock(QUERY_21_DRAFT_USED_IN_MULTIDATASET_VERSION_86_NAME, queryDraft);

        Query queryProductionValidation = QueryVersionMockFactory.createQueryVersionInStatus(buildQueryVersionMockSimpleWithFixedDatasetVersion("Q02"), ProcStatusEnum.PRODUCTION_VALIDATION)
                .getQuery();
        registerQueryMock(QUERY_22_PRODUCTION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME, queryProductionValidation);

        Query queryDiffusionValidation = QueryVersionMockFactory.createQueryVersionInStatus(buildQueryVersionMockSimpleWithFixedDatasetVersion("Q03"), ProcStatusEnum.DIFFUSION_VALIDATION).getQuery();
        registerQueryMock(QUERY_23_DIFFUSION_VALIDATION_USED_IN_MULTIDATASET_VERSION_86_NAME, queryDiffusionValidation);

        Query queryValidationRejected = QueryVersionMockFactory.createQueryVersionInStatus(buildQueryVersionMockSimpleWithFixedDatasetVersion("Q04"), ProcStatusEnum.VALIDATION_REJECTED).getQuery();
        registerQueryMock(QUERY_24_VALIDATION_REJECTED_USED_IN_MULTIDATASET_VERSION_86_NAME, queryValidationRejected);

        DatasetVersion datasetVersionLinkedToQuery = createDatasetVersionPublishedLastVersion(5, INIT_VERSION, new DateTime().minusDays(3));
        Query queryPublishedNotVisible = QueryMockFactory.createPublishedQueryLinkedToDatasetVersion("Q05", datasetVersionLinkedToQuery, new DateTime().plusDays(1));
        registerQueryMock(QUERY_25_PUBLISHED_NOT_VISIBLE_USED_IN_MULTIDATASET_VERSION_86_NAME, queryPublishedNotVisible);

        List<Query> queries = Arrays.asList(queryDraft, queryProductionValidation, queryDiffusionValidation, queryValidationRejected, queryPublishedNotVisible);

        populateMultidatasetWithCubes(multidatasetVersion, datasets, queries);

        prepareToPublished(multidatasetVersion);

        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion87WithRelatedResourcesNotPublished() {
        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setSequentialId(1);
        MultidatasetVersion multidatasetVersion = createMultidatasetVersionFromTemplate(template);
        fillOptionalExternalItems(multidatasetVersion);
        fillOptionalRelatedResourcesNotPublished(multidatasetVersion);
        prepareToPublished(multidatasetVersion);
        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion88WithNoCubes() {
        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setSequentialId(1);
        MultidatasetVersion multidatasetVersion = createMultidatasetVersionFromTemplate(template);
        fillOptionalExternalItems(multidatasetVersion);
        prepareToPublished(multidatasetVersion);

        multidatasetVersion.getCubes().clear();

        return multidatasetVersion;
    }

    private static MultidatasetVersion getMultidatasetVersion90WithEmptyCube() {
        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setSequentialId(1);
        MultidatasetVersion multidatasetVersion = createMultidatasetVersionFromTemplate(template);
        fillOptionalExternalItems(multidatasetVersion);

        MultidatasetCube cube = createDatasetMultidatasetCube(multidatasetVersion, null);
        cube.setOrderInMultidataset(1L);
        registerMultidatasetCubeMock(MULTIDATASET_CUBE_06_EMPTY_IN_MULTIDATASET_VERSION_90_NAME, cube);

        prepareToPublished(multidatasetVersion);

        return multidatasetVersion;
    }

    private static MockDescriptor getMultidatasetVersion92IsReplacedByMultidataset91() {
        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setSequentialId(1);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(1));

        MultidatasetVersion multidatasetVersionReplaces = createMultidatasetVersionFromTemplate(template);
        MultidatasetVersion multidatasetReplaced = createMultidatasetVersionWithSequenceAndVersion(2, INIT_VERSION);

        registerMultidatasetVersionMock(MULTIDATASET_VERSION_91_REPLACES_MULTIDATASET_92_NAME, multidatasetVersionReplaces);

        multidatasetVersionReplaces.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetReplaced));
        multidatasetReplaced.getSiemacMetadataStatisticalResource().setIsReplacedBy(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetVersionReplaces));

        return new MockDescriptor(multidatasetReplaced, multidatasetVersionReplaces);
    }

    private static MockDescriptor getMultidatasetVersion91ReplacesMultidataset92() {
        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setSequentialId(1);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(1));

        MultidatasetVersion multidatasetVersionReplaces = createMultidatasetVersionFromTemplate(template);
        MultidatasetVersion multidatasetReplaced = createMultidatasetVersionWithSequenceAndVersion(2, INIT_VERSION);

        registerMultidatasetVersionMock(MULTIDATASET_VERSION_92_IS_REPLACED_BY_MULTIDATASET_91_NAME, multidatasetReplaced);

        multidatasetVersionReplaces.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetReplaced));
        multidatasetReplaced.getSiemacMetadataStatisticalResource().setIsReplacedBy(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetVersionReplaces));

        return new MockDescriptor(multidatasetVersionReplaces, multidatasetReplaced);
    }

    private static MultidatasetVersion getMultidatasetVersion94NotVisible() {
        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setSequentialId(1);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(1));
        MultidatasetVersion multidatasetVersion = createMultidatasetVersionInStatus(template, ProcStatusEnum.PUBLISHED);
        return multidatasetVersion;
    }

    private static MockDescriptor getMultidatasetVersion95NotVisibleIsReplacedByMultidatasetVersion96() {
        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setSequentialId(1);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(1));
        MultidatasetVersion multidatasetReplaced = createMultidatasetVersionInStatus(template, ProcStatusEnum.PUBLISHED);

        MultidatasetVersionMock templateReplaces = new MultidatasetVersionMock();
        templateReplaces.setSequentialId(2);
        templateReplaces.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(2));
        templateReplaces.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetReplaced));
        MultidatasetVersion multidatasetVersionReplaces = createMultidatasetVersionInStatus(templateReplaces, ProcStatusEnum.PUBLISHED);

        registerMultidatasetVersionMock(MULTIDATASET_VERSION_96_NOT_VISIBLE_REPLACES_MULTIDATASET_VERSION_95_NAME, multidatasetVersionReplaces);

        return new MockDescriptor(multidatasetReplaced, multidatasetVersionReplaces);
    }

    private static MultidatasetVersion getMultidatasetVersion98ToDeleteWithPreviousVersion() {
        MultidatasetVersion multidatasetVersionToReplace = createMultidatasetVersionWithSequenceAndVersion(1, INIT_VERSION);
        prepareToVersioning(multidatasetVersionToReplace);

        MultidatasetVersion multidatasetVersion = createMultidatasetVersionWithSequenceAndVersion(1, SECOND_VERSION);
        registerMultidatasetVersionMock(MULTIDATASET_VERSION_98_TO_DELETE_WITH_PREVIOUS_VERSION_NAME, multidatasetVersion);

        multidatasetVersion.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetVersionToReplace));
        multidatasetVersionToReplace.getSiemacMetadataStatisticalResource().setIsReplacedByVersion(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(multidatasetVersion));

        multidatasetVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        return multidatasetVersion;
    }

    private static MockDescriptor getMultidatasetVersion101LastVersionNotPublishedIsPartOfPublications() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersionWithSequenceAndVersion(1, INIT_VERSION);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setLastVersion(true);

        // Publication created in PublicationVersionMockFactory
        PublicationVersion pub01 = buildDraftPublicationWithMultidatasetInFirstLevel(1, multidatasetVersion.getMultidataset());
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_103_DRAFT_HAS_PART_MULTIDATASET_VERSION_101_FIRST_LEVEL_NAME, pub01);

        return new MockDescriptor(multidatasetVersion, pub01);
    }

    private static MockDescriptor getMultidatasetVersion102LastVersionPublishedIsPartOfPublications() {
        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setSequentialId(1);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        MultidatasetVersion multidatasetVersion = createMultidatasetVersionInStatus(template, ProcStatusEnum.PUBLISHED);

        PublicationVersionMock publicationVersion = PublicationVersionMockFactory.buildPublishedReadyPublicationVersion(new PublicationMock(), StatisticalResourcesMockFactory.INIT_VERSION,
                new DateTime().minusDays(1), null, true);
        PublicationVersionMockFactory.createMultidatasetCubeElementLevel(publicationVersion, multidatasetVersion.getMultidataset());
        PublicationVersionMockFactory.createPublicationVersionInStatus(publicationVersion, ProcStatusEnum.PUBLISHED);
        registerPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_104_PUBLISHED_HAS_PART_MULTIDATASET_VERSION_102_FIRST_LEVEL_NAME, publicationVersion);

        return new MockDescriptor(multidatasetVersion, publicationVersion);
    }

    // -----------------------------------------------------------------
    // BUILDERS
    // -----------------------------------------------------------------

    protected static MultidatasetVersion buildMultidatasetVersionDraft(int sequentialId) {
        MultidatasetVersionMock version01Mock = new MultidatasetVersionMock();
        version01Mock.setVersionLogic(INIT_VERSION);
        version01Mock.setSequentialId(sequentialId);
        version01Mock.getSiemacMetadataStatisticalResource().setLastVersion(Boolean.TRUE);
        version01Mock.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(1));
        return getStatisticalResourcesPersistedDoMocks().mockMultidatasetVersion(version01Mock);
    }

    private static MultidatasetVersion createMultidatasetVersion() {
        return getStatisticalResourcesPersistedDoMocks().mockMultidatasetVersion();
    }

    private static MultidatasetVersion createMultidatasetVersionInOperation(String operationCode, int sequentialId) {
        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setSequentialId(sequentialId);
        template.setStatisticalOperationCode(operationCode);
        return createMultidatasetVersionFromTemplate(template);
    }

    private static MultidatasetVersion createMultidatasetVersionWithSequenceAndVersion(Integer sequentialId, String version) {
        MultidatasetVersionMock template = new MultidatasetVersionMock();
        template.setSequentialId(sequentialId);
        template.setVersionLogic(version);
        return createMultidatasetVersionFromTemplate(template);
    }

    private static MultidatasetVersion createMultidatasetVersionFromTemplate(MultidatasetVersionMock template) {
        return getStatisticalResourcesPersistedDoMocks().mockMultidatasetVersion(template);
    }

    public static MultidatasetVersion createMultidatasetVersionPublishedPreviousVersion(MultidatasetMock multidataset, String version, DateTime validFrom, DateTime validTo) {
        MultidatasetVersionMock multidatasetVersion = MultidatasetVersionMockFactory.buildPublishedReadyMultidatasetVersion(multidataset, version, validFrom, validTo, false);
        return createMultidatasetVersionInStatus(multidatasetVersion, ProcStatusEnum.PUBLISHED);
    }

    public static MultidatasetVersion createMultidatasetVersionPublishedLastVersion(MultidatasetMock multidataset, String version, DateTime validFrom) {
        MultidatasetVersionMock multidatasetVersion = MultidatasetVersionMockFactory.buildPublishedReadyMultidatasetVersion(multidataset, version, validFrom, null, true);
        return createMultidatasetVersionInStatus(multidatasetVersion, ProcStatusEnum.PUBLISHED);
    }

    public static MultidatasetVersionMock buildPublishedReadyMultidatasetVersion(MultidatasetMock multidataset, String version, DateTime validFrom, DateTime validTo, boolean lastVersion) {
        MultidatasetVersionMock multidatasetVersion = new MultidatasetVersionMock();
        multidatasetVersion.setMultidataset(multidataset);
        multidatasetVersion.setVersionLogic(version);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setLastVersion(lastVersion);

        if (validFrom.isAfterNow()) {
            multidatasetVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime());
        } else {
            multidatasetVersion.getSiemacMetadataStatisticalResource().setCreationDate(validFrom.minusHours(1));
        }
        multidatasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(validFrom);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setValidTo(validTo);

        return multidatasetVersion;
    }

    public static MultidatasetVersion createMultidatasetVersionInStatus(MultidatasetVersionMock multidatasetVersionMock, ProcStatusEnum status) {
        MultidatasetVersion multidatasetVersion = getStatisticalResourcesPersistedDoMocks().mockMultidatasetVersion(multidatasetVersionMock);

        switch (status) {
            case PRODUCTION_VALIDATION:
                MultidatasetLifecycleTestUtils.fillAsProductionValidation(multidatasetVersion);
                break;
            case DIFFUSION_VALIDATION:
                MultidatasetLifecycleTestUtils.fillAsDiffusionValidation(multidatasetVersion);
                break;
            case VALIDATION_REJECTED:
                MultidatasetLifecycleTestUtils.fillAsValidationRejected(multidatasetVersion);
                break;
            case PUBLISHED:
                MultidatasetLifecycleTestUtils.fillAsPublished(multidatasetVersion);
                break;
            case DRAFT:
                break;
            default:
                throw new IllegalArgumentException("Unsupported status " + status);
        }
        return multidatasetVersion;
    }

    public static MultidatasetVersionMock buildMultidatasetVersionSimplePreviousVersion(Multidataset multidataset, String version) {
        return buildMultidatasetVersionSimple(multidataset, version, false);
    }

    public static MultidatasetVersionMock buildMultidatasetVersionSimpleLastVersion(Multidataset multidataset, String version) {
        return buildMultidatasetVersionSimple(multidataset, version, true);
    }

    public static MultidatasetVersionMock buildMultidatasetVersionSimple(Multidataset multidataset, String version, boolean isLastVersion) {
        MultidatasetVersionMock multidatasetVersion = new MultidatasetVersionMock();
        multidatasetVersion.setMultidataset(multidataset);
        multidatasetVersion.setVersionLogic(version);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setLastVersion(isLastVersion);
        return multidatasetVersion;
    }

    public static MultidatasetVersionMock createMultidatasetVersionSimple(Multidataset multidataset, String version, boolean isLastVersion) {
        MultidatasetVersionMock multidatasetVersion = buildMultidatasetVersionSimple(multidataset, version, isLastVersion);
        getStatisticalResourcesPersistedDoMocks().mockMultidatasetVersion(multidatasetVersion);
        return multidatasetVersion;
    }

    private static PublicationVersion buildDraftPublicationWithMultidatasetInFirstLevel(Integer sequentialId, Multidataset multidataset) {
        PublicationVersion publicationVersion = PublicationVersionMockFactory.buildPublicationVersionDraft(sequentialId);

        ElementLevel elementLevel01 = PublicationVersionMockFactory.createMultidatasetCubeElementLevel(publicationVersion, multidataset);
        elementLevel01.setOrderInLevel(Long.valueOf(1));

        return publicationVersion;
    }

    // -----------------------------------------------------------------
    // MULTIDATASET STRUCTURE
    // -----------------------------------------------------------------

    public static MultidatasetCube createQueryMultidatasetCube(MultidatasetVersion multidatasetVersion, Query query) {
        return getStatisticalResourcesPersistedDoMocks().mockQueryMultidatasetCube(multidatasetVersion, query);
    }

    public static MultidatasetCube createDatasetMultidatasetCube(MultidatasetVersion multidatasetVersion, Dataset dataset) {
        return getStatisticalResourcesPersistedDoMocks().mockDatasetMultidatasetCube(multidatasetVersion, dataset);
    }

    public static MultidatasetVersion populateMultidatasetWithCubes(MultidatasetVersion multidatasetVersion, List<Dataset> datasets, List<Query> queries) {

        for (Dataset dataset : datasets) {
            MultidatasetCube cube = createDatasetMultidatasetCube(multidatasetVersion, dataset);
            cube.setOrderInMultidataset(Long.valueOf(multidatasetVersion.getCubes().size() + 1));
        }

        for (Query query : queries) {
            MultidatasetCube cube = createQueryMultidatasetCube(multidatasetVersion, query);
            cube.setOrderInMultidataset(Long.valueOf(multidatasetVersion.getCubes().size() + 1));
        }

        return multidatasetVersion;
    }

    public static MultidatasetVersion createComplexStructure() {
        MultidatasetVersion multidatasetVersion = createMultidatasetVersion();
        multidatasetVersion.getSiemacMetadataStatisticalResource().setVersionLogic(INIT_VERSION);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(2));

        fillWithCubes(multidatasetVersion);

        return multidatasetVersion;
    }

    // -----------------------------------------------------------------
    // LIFE CYCLE PREPARATIONS
    // -----------------------------------------------------------------

    // -----------------------------------------------------------------
    // UTILS
    // -----------------------------------------------------------------
    public static void fillOptionalExternalItems(MultidatasetVersion multidatasetVersion) {
        LifecycleTestUtils.fillOptionalExternalItemsSiemacResource(multidatasetVersion);

        // Not specific external items for Multidataset Version
    }

    private static void fillOptionalRelatedResourcesNotPublished(MultidatasetVersion multidatasetVersion) {
        multidatasetVersion.getSiemacMetadataStatisticalResource()
                .setReplaces(StatisticalResourcesPersistedDoMocks.mockMultidatasetVersionRelated(createMultidatasetVersionWithSequenceAndVersion(1, INIT_VERSION)));
    }

    private static void fillWithCubes(MultidatasetVersion multidatasetVersion) {
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
    }

}
