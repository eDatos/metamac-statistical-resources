package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import static org.siemac.metamac.statistical.resources.core.utils.PublicationLifecycleTestUtils.prepareToDiffusionValidation;
import static org.siemac.metamac.statistical.resources.core.utils.PublicationLifecycleTestUtils.prepareToProductionValidation;
import static org.siemac.metamac.statistical.resources.core.utils.PublicationLifecycleTestUtils.prepareToPublished;
import static org.siemac.metamac.statistical.resources.core.utils.PublicationLifecycleTestUtils.prepareToValidationRejected;
import static org.siemac.metamac.statistical.resources.core.utils.PublicationLifecycleTestUtils.prepareToVersioning;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.MultidatasetVersionMock.buildBasicSingleVersionWithSequence;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CubeMockFactory.CUBE_08_EMPTY_IN_PUBLICATION_VERSION_90_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_25_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_26_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_27_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_28_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_29_PUBLISHED_NOT_VISIBLE_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createDatasetVersionPublishedLastVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createPublishedAndDraftVersionsForDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createPublishedAndNotVisibleVersionsForDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.createTwoPublishedVersionsForDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_08_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_09_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_10_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_11_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationMockFactory.PUBLICATION_04_STRUCTURED_WITH_2_PUBLICATION_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationMockFactory.PUBLICATION_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationMockFactory.PUBLICATION_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_16_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_17_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_18_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_19_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_20_PUBLISHED_NOT_VISIBLE_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.createPublishedQueryLinkedToDataset;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.generateQueryWithGeneratedVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.generateUrl;
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
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.Publication;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.utils.LifecycleTestUtils;
import org.siemac.metamac.statistical.resources.core.utils.PublicationLifecycleTestUtils;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.PublicationVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

@MockProvider
@SuppressWarnings("unused")
public class PublicationVersionMockFactory extends StatisticalResourcesMockFactory<PublicationVersion> {

    public static final String                   PUBLICATION_VERSION_01_BASIC_NAME                                                                                  = "PUBLICATION_VERSION_01_BASIC";

    public static final String                   PUBLICATION_VERSION_02_BASIC_NAME                                                                                  = "PUBLICATION_VERSION_02_BASIC";

    public static final String                   PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME                                                                     = "PUBLICATION_VERSION_03_FOR_PUBLICATION_03";

    public static final String                   PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME                                                    = "PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION";

    public static final String                   PUBLICATION_VERSION_05_OPERATION_0001_CODE_000001_NAME                                                             = "PUBLICATION_VERSION_05_OPERATION_0001_CODE_000001";

    public static final String                   PUBLICATION_VERSION_06_OPERATION_0001_CODE_000002_NAME                                                             = "PUBLICATION_VERSION_06_OPERATION_0001_CODE_000002";

    public static final String                   PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME                                                             = "PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003";

    public static final String                   PUBLICATION_VERSION_08_OPERATION_0002_CODE_000001_NAME                                                             = "PUBLICATION_VERSION_08_OPERATION_0002_CODE_000001";

    public static final String                   PUBLICATION_VERSION_09_OPERATION_0002_CODE_000002_NAME                                                             = "PUBLICATION_VERSION_09_OPERATION_0002_CODE_000002";

    public static final String                   PUBLICATION_VERSION_10_OPERATION_0002_CODE_000003_NAME                                                             = "PUBLICATION_VERSION_10_OPERATION_0002_CODE_000003";

    public static final String                   PUBLICATION_VERSION_11_OPERATION_0002_CODE_MAX_NAME                                                                = "PUBLICATION_VERSION_11_OPERATION_0002_CODE_MAX";

    public static final String                   PUBLICATION_VERSION_12_DRAFT_NAME                                                                                  = "PUBLICATION_VERSION_12_DRAFT";

    public static final String                   PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME                                                                  = "PUBLICATION_VERSION_13_PRODUCTION_VALIDATION";

    public static final String                   PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME                                                                   = "PUBLICATION_VERSION_14_DIFFUSION_VALIDATION";

    public static final String                   PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME                                                                    = "PUBLICATION_VERSION_15_VALIDATION_REJECTED";

    public static final String                   PUBLICATION_VERSION_16_PUBLISHED_NAME                                                                              = "PUBLICATION_VERSION_16_PUBLISHED";

    public static final String                   PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME                                              = "PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04";

    public static final String                   PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME                             = "PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION";

    public static final String                   PUBLICATION_VERSION_19_WITH_STRUCTURE_PRODUCTION_VALIDATION_NAME                                                   = "PUBLICATION_VERSION_19_WITH_STRUCTURE_PRODUCTION_VALIDATION";

    public static final String                   PUBLICATION_VERSION_20_WITH_STRUCTURE_DIFFUSION_VALIDATION_NAME                                                    = "PUBLICATION_VERSION_20_WITH_STRUCTURE_DIFFUSION_VALIDATION";

    public static final String                   PUBLICATION_VERSION_21_WITH_STRUCTURE_VALIDATION_REJECTED_NAME                                                     = "PUBLICATION_VERSION_21_WITH_STRUCTURE_VALIDATION_REJECTED";

    public static final String                   PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME                                                           = "PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT";

    public static final String                   PUBLICATION_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME                                           = "PUBLICATION_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION";

    public static final String                   PUBLICATION_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME                                            = "PUBLICATION_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION";

    public static final String                   PUBLICATION_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME                                             = "PUBLICATION_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED";

    public static final String                   PUBLICATION_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME                                                       = "PUBLICATION_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED";

    public static final String                   PUBLICATION_VERSION_27_V1_PUBLISHED_FOR_PUBLICATION_05_NAME                                                        = "PUBLICATION_VERSION_27_V1_PUBLISHED_FOR_PUBLICATION_05";

    public static final String                   PUBLICATION_VERSION_28_V2_PUBLISHED_FOR_PUBLICATION_05_NAME                                                        = "PUBLICATION_VERSION_28_V2_PUBLISHED_FOR_PUBLICATION_05";

    public static final String                   PUBLICATION_VERSION_29_V3_PUBLISHED_FOR_PUBLICATION_05_NAME                                                        = "PUBLICATION_VERSION_29_V3_PUBLISHED_FOR_PUBLICATION_05";

    public static final String                   PUBLICATION_VERSION_30_V1_PUBLISHED_FOR_PUBLICATION_06_NAME                                                        = "PUBLICATION_VERSION_30_V1_PUBLISHED_FOR_PUBLICATION_06";

    public static final String                   PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME                                             = "PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06";

    public static final String                   PUBLICATION_VERSION_32_DRAFT_NOT_READY_NAME                                                                        = "PUBLICATION_VERSION_32_DRAFT_NOT_READY";

    public static final String                   PUBLICATION_VERSION_33_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME                                                  = "PUBLICATION_VERSION_33_DRAFT_READY_FOR_PRODUCTION_VALIDATION";

    public static final String                   PUBLICATION_VERSION_34_VERSION_RATIONALE_TYPE_MINOR_ERRATA_NAME                                                    = "PUBLICATION_VERSION_34_VERSION_RATIONALE_TYPE_MINOR_ERRATA";

    public static final String                   PUBLICATION_VERSION_35_NEXT_VERSION_NOT_SCHEDULED_DATE_FILLED_NAME                                                 = "PUBLICATION_VERSION_35_NEXT_VERSION_NOT_SCHEDULED_DATE_FILLED";

    public static final String                   PUBLICATION_VERSION_36_PRODUCTION_VALIDATION_NOT_READY_NAME                                                        = "PUBLICATION_VERSION_36_PRODUCTION_VALIDATION_NOT_READY";

    public static final String                   PUBLICATION_VERSION_37_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME                                   = "PUBLICATION_VERSION_37_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION";

    public static final String                   PUBLICATION_VERSION_38_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME                                    = "PUBLICATION_VERSION_38_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED";

    public static final String                   PUBLICATION_VERSION_39_PUBLISHED_WITH_NO_ROOT_MAINTAINER_NAME                                                      = "PUBLICATION_VERSION_39_PUBLISHED_WITH_NO_ROOT_MAINTAINER";

    public static final String                   PUBLICATION_VERSION_40_PREPARED_TO_PUBLISH_EXTERNAL_ITEM_FULL_NAME                                                 = "PUBLICATION_VERSION_40_PREPARED_TO_PUBLISH_EXTERNAL_ITEM_FULL";

    public static final String                   PUBLICATION_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42_NAME                                                = "PUBLICATION_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42";

    public static final String                   PUBLICATION_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41_NAME                                                      = "PUBLICATION_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41";

    public static final String                   PUBLICATION_VERSION_43_DRAFT_HAS_PART_DATASET_VERSION_85_FIRST_LEVEL_NAME                                          = "PUBLICATION_VERSION_43_DRAFT_HAS_PART_DATASET_VERSION_85_FIRST_LEVEL";

    public static final String                   PUBLICATION_VERSION_44_DRAFT_HAS_PART_DATASET_VERSION_85_NO_FIRST_LEVEL_NAME                                       = "PUBLICATION_VERSION_44_DRAFT_HAS_PART_DATASET_VERSION_85_NO_FIRST_LEVEL";

    public static final String                   PUBLICATION_VERSION_45_DRAFT_HAS_PART_DATASET_VERSION_85_MULTI_CUBE_NAME                                           = "PUBLICATION_VERSION_45_DRAFT_HAS_PART_DATASET_VERSION_85_MULTI_CUBE";

    public static final String                   PUBLICATION_VERSION_46_PUBLISHED_V01_FOR_PUBLICATION_07_NAME                                                       = "PUBLICATION_VERSION_46_PUBLISHED_V01_FOR_PUBLICATION_07";

    public static final String                   PUBLICATION_VERSION_47_PUBLISHED_V02_FOR_PUBLICATION_07_NAME                                                       = "PUBLICATION_VERSION_47_PUBLISHED_V02_FOR_PUBLICATION_07";

    public static final String                   PUBLICATION_VERSION_48_PUBLISHED_V01_FOR_PUBLICATION_08_NAME                                                       = "PUBLICATION_VERSION_48_PUBLISHED_V01_FOR_PUBLICATION_08";

    public static final String                   PUBLICATION_VERSION_49_PUBLISHED_NOT_VISIBLE_V02_FOR_PUBLICATION_08_NAME                                           = "PUBLICATION_VERSION_49_PUBLISHED_NOT_VISIBLE_V02_FOR_PUBLICATION_08";

    public static final String                   PUBLICATION_VERSION_50_DRAFT_V01_FOR_PUBLICATION_09_NAME                                                           = "PUBLICATION_VERSION_50_DRAFT_V01_FOR_PUBLICATION_09";

    public static final String                   PUBLICATION_VERSION_51_PUBLISHED_V01_FOR_PUBLICATION_10_NAME                                                       = "PUBLICATION_VERSION_51_PUBLISHED_V01_FOR_PUBLICATION_10";

    public static final String                   PUBLICATION_VERSION_52_PUBLISHED_V02_FOR_PUBLICATION_10_NAME                                                       = "PUBLICATION_VERSION_52_PUBLISHED_V02_FOR_PUBLICATION_10";

    public static final String                   PUBLICATION_VERSION_53_PUBLISHED_V01_FOR_PUBLICATION_11_NAME                                                       = "PUBLICATION_VERSION_53_PUBLISHED_V01_FOR_PUBLICATION_11";

    public static final String                   PUBLICATION_VERSION_54_PUBLISHED_NOT_VISIBLE_V02_FOR_PUBLICATION_11_NAME                                           = "PUBLICATION_VERSION_54_PUBLISHED_NOT_VISIBLE_V02_FOR_PUBLICATION_11";

    public static final String                   PUBLICATION_VERSION_55_DRAFT_V01_FOR_PUBLICATION_12_NAME                                                           = "PUBLICATION_VERSION_55_DRAFT_V01_FOR_PUBLICATION_12";

    public static final String                   PUBLICATION_VERSION_56_PUBLISHED_V01_FOR_PUBLICATION_13_NAME                                                       = "PUBLICATION_VERSION_56_PUBLISHED_V01_FOR_PUBLICATION_13";

    public static final String                   PUBLICATION_VERSION_57_PUBLISHED_V02_FOR_PUBLICATION_13_NAME                                                       = "PUBLICATION_VERSION_57_PUBLISHED_V02_FOR_PUBLICATION_13";

    public static final String                   PUBLICATION_VERSION_58_PUBLISHED_V01_FOR_PUBLICATION_14_NAME                                                       = "PUBLICATION_VERSION_58_PUBLISHED_V01_FOR_PUBLICATION_14";

    public static final String                   PUBLICATION_VERSION_59_PUBLISHED_NOT_VISIBLE_V02_FOR_PUBLICATION_14_NAME                                           = "PUBLICATION_VERSION_59_PUBLISHED_NOT_VISIBLE_V02_FOR_PUBLICATION_14";

    public static final String                   PUBLICATION_VERSION_60_DRAFT_V01_FOR_PUBLICATION_15_NAME                                                           = "PUBLICATION_VERSION_60_DRAFT_V01_FOR_PUBLICATION_15";

    public static final String                   PUBLICATION_VERSION_61_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_10_NAME                                        = "PUBLICATION_VERSION_61_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_10";

    public static final String                   PUBLICATION_VERSION_62_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10_NAME                                               = "PUBLICATION_VERSION_62_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10";

    public static final String                   PUBLICATION_VERSION_63_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_11_NAME                                        = "PUBLICATION_VERSION_63_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_11";

    public static final String                   PUBLICATION_VERSION_64_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_11_NAME                                               = "PUBLICATION_VERSION_64_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_11";

    public static final String                   PUBLICATION_VERSION_65_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION__LINKED_TO_QUERY_11_NAME                               = "PUBLICATION_VERSION_65_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION__LINKED_TO_QUERY_11";

    public static final String                   PUBLICATION_VERSION_66_PUBLISHED_MULTI_VERSION_V01__LINKED_TO_QUERY_11_NAME                                        = "PUBLICATION_VERSION_66_PUBLISHED_MULTI_VERSION_V01__LINKED_TO_QUERY_11";

    public static final String                   PUBLICATION_VERSION_67_PUBLISHED_NOT_VISIBLE_MULTI_VERSION_V02__LINKED_TO_QUERY_11_NAME                            = "PUBLICATION_VERSION_67_PUBLISHED_NOT_VISIBLE_MULTI_VERSION_V02__LINKED_TO_QUERY_11";

    public static final String                   PUBLICATION_VERSION_68_DRAFT_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME                                                = "PUBLICATION_VERSION_68_DRAFT_SINGLE_VERSION_LINKED_TO_QUERY_12";
    public static final String                   PUBLICATION_VERSION_69_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME                                = "PUBLICATION_VERSION_69_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION_LINKED_TO_QUERY_12";
    public static final String                   PUBLICATION_VERSION_70_PUBLISHED_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME                                            = "PUBLICATION_VERSION_70_PUBLISHED_SINGLE_VERSION_LINKED_TO_QUERY_12";

    public static final String                   PUBLICATION_VERSION_71_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_DRAFT_LINKED_TO_QUERY_13_NAME               = "PUBLICATION_VERSION_71_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_DRAFT_LINKED_TO_QUERY_13";
    public static final String                   PUBLICATION_VERSION_72_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME       = "PUBLICATION_VERSION_72_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_PUBLISHED_LINKED_TO_QUERY_14";
    public static final String                   PUBLICATION_VERSION_73_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15_NAME                 = "PUBLICATION_VERSION_73_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15";
    public static final String                   PUBLICATION_VERSION_74_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15_NAME                     = "PUBLICATION_VERSION_74_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15";

    public static final String                   PUBLICATION_VERSION_75_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_LAST_LINKED_TO_QUERY_13_NAME    = "PUBLICATION_VERSION_75_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_LAST_LINKED_TO_QUERY_13";
    public static final String                   PUBLICATION_VERSION_76_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME = "PUBLICATION_VERSION_76_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_PUBLISHED_LINKED_TO_QUERY_14";
    public static final String                   PUBLICATION_VERSION_77_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15_NAME           = "PUBLICATION_VERSION_77_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15";
    public static final String                   PUBLICATION_VERSION_78_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15_NAME         = "PUBLICATION_VERSION_78_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15";

    public static final String                   PUBLICATION_VERSION_79_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__ONLY_LAST_LINKED_TO_QUERY_13_NAME               = "PUBLICATION_VERSION_79_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__ONLY_LAST_LINKED_TO_QUERY_13";
    public static final String                   PUBLICATION_VERSION_80_PREVIOUS_VERSION_V01_IN_PUB_WITH_TWO_PUBLISHED__ONLY_PREVIOUS_LINKED_TO_QUERY_14_NAME       = "PUBLICATION_VERSION_80_PREVIOUS_VERSION_V01_IN_PUB_WITH_TWO_PUBLISHED__ONLY_PREVIOUS_LINKED_TO_QUERY_14";
    public static final String                   PUBLICATION_VERSION_81_PREVIOUS_VERSION_V01_IN_PUB_WITH_TWO_PUBLISHED__BOTH_LINKED_TO_QUERY_15_NAME                = "PUBLICATION_VERSION_81_PREVIOUS_VERSION_V01_IN_PUB_WITH_TWO_PUBLISHED__BOTH_LINKED_TO_QUERY_15";
    public static final String                   PUBLICATION_VERSION_82_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__BOTH_LINKED_TO_QUERY_15_NAME                    = "PUBLICATION_VERSION_82_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__BOTH_LINKED_TO_QUERY_15";

    public static final String                   PUBLICATION_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME                                    = "PUBLICATION_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL";
    public static final String                   PUBLICATION_VERSION_84_PUBLISHED_FOR_PUBLICATION_07_NAME                                                           = "PUBLICATION_VERSION_84_PUBLISHED_FOR_PUBLICATION_07_NAME";
    public static final String                   PUBLICATION_VERSION_85_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION_EXTERNAL_ITEM_FULL_FOR_PUBLICATION_07_NAME        = "PUBLICATION_VERSION_85_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION_EXTERNAL_ITEM_FULL_FOR_PUBLICATION_07_NAME";

    public static final String                   PUBLICATION_VERSION_86_WITH_ELEMENT_LEVELS_NOT_PUBLISHED_NAME                                                      = "PUBLICATION_VERSION_86_WITH_ELEMENT_LEVELS_NOT_PUBLISHED";
    public static final String                   PUBLICATION_VERSION_87_WITH_RELATED_RESOURCES_NOT_PUBLISHED_NAME                                                   = "PUBLICATION_VERSION_87_WITH_RELATED_RESOURCES_NOT_PUBLISHED";
    public static final String                   PUBLICATION_VERSION_88_WITH_NO_CUBES_NAME                                                                          = "PUBLICATION_VERSION_88_WITH_NO_CUBES";
    public static final String                   PUBLICATION_VERSION_89_WITH_EMPTY_CHAPTER_NAME                                                                     = "PUBLICATION_VERSION_89_WITH_EMPTY_CHAPTER";
    public static final String                   PUBLICATION_VERSION_90_WITH_EMPTY_CUBE_NAME                                                                        = "PUBLICATION_VERSION_90_WITH_EMPTY_CUBE";

    public static final String                   PUBLICATION_VERSION_91_REPLACES_PUBLICATION_92_NAME                                                                = "PUBLICATION_VERSION_91_REPLACES_PUBLICATION_92";
    public static final String                   PUBLICATION_VERSION_92_IS_REPLACED_BY_PUBLICATION_91_NAME                                                          = "PUBLICATION_VERSION_92_IS_REPLACED_BY_PUBLICATION_91";

    public static final String                   PUBLICATION_VERSION_93_NOT_VISIBLE_HAS_PART_NOT_VISIBLE_DATASET_VERSION_NAME                                       = "PUBLICATION_VERSION_93_NOT_VISIBLE_HAS_PART_NOT_VISIBLE_DATASET_VERSION";

    public static final String                   PUBLICATION_VERSION_94_NOT_VISIBLE_NAME                                                                            = "PUBLICATION_VERSION_94_NOT_VISIBLE";
    public static final String                   PUBLICATION_VERSION_95_NOT_VISIBLE_IS_REPLACED_BY_PUBLICATION_VERSION_96_NAME                                      = "PUBLICATION_VERSION_95_NOT_VISIBLE_IS_REPLACED_BY_PUBLICATION_VERSION_96";
    public static final String                   PUBLICATION_VERSION_96_NOT_VISIBLE_REPLACES_PUBLICATION_VERSION_95_NAME                                            = "PUBLICATION_VERSION_96_NOT_VISIBLE_REPLACES_PUBLICATION_VERSION_95";

    public static final String                   PUBLICATION_VERSION_97_NOT_VISIBLE_HAS_PART_NOT_VISIBLE_QUERY_NAME                                                 = "PUBLICATION_VERSION_97_NOT_VISIBLE_HAS_PART_NOT_VISIBLE_QUERY";
    public static final String                   PUBLICATION_VERSION_98_TO_DELETE_WITH_PREVIOUS_VERSION_NAME                                                        = "PUBLICATION_VERSION_98_TO_DELETE_WITH_PREVIOUS_VERSION";

    public static final String                   PUBLICATION_VERSION_99_WITH_PUBLISHED_MULTIDATASET_NAME                                                            = "PUBLICATION_VERSION_99_WITH_PUBLISHED_MULTIDATASET";

    public static final String                   PUBLICATION_VERSION_100_MAXIMUM_VERSION_REACHED                                                                    = "PUBLICATION_VERSION_100_MAXIMUM_VERSION_REACHED";

    public static final String                   PUBLICATION_VERSION_101_MAXIMUM_MINOR_VERSION_REACHED                                                              = "PUBLICATION_VERSION_101_MAXIMUM_MINOR_VERSION_REACHED";
    
    public static final String                   PUBLICATION_VERSION_102_WITH_COMPLEX_STRUCTURE_FOR_URL_PUBLICATION                                                 = "PUBLICATION_VERSION_102_WITH_COMPLEX_STRUCTURE_FOR_URL_PUBLICATION";


    private static PublicationVersionMockFactory instance                                                                                                           = null;

    private PublicationVersionMockFactory() {
    }

    public static PublicationVersionMockFactory getInstance() {
        if (instance == null) {
            instance = new PublicationVersionMockFactory();
        }
        return instance;
    }

    private static PublicationVersion getPublicationVersion01Basic() {
        return createPublicationVersion();
    }

    private static PublicationVersion getPublicationVersion02Basic() {
        return createPublicationVersion();
    }

    private static MockDescriptor getPublicationVersion03ForPublication03() {
        MockDescriptor pubMockDesc = getPublicationMockDescriptor(PublicationMockFactory.PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME);
        return new MockDescriptor(getPublicationVersionMock(PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME), pubMockDesc);
    }

    private static MockDescriptor getPublicationVersion04ForPublication03AndLastVersion() {
        MockDescriptor pubMockDesc = getPublicationMockDescriptor(PublicationMockFactory.PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME);
        return new MockDescriptor(getPublicationVersionMock(PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME), pubMockDesc);
    }

    private static PublicationVersion getPublicationVersion05Operation0001Code000001() {
        return createPublicationVersionInOperation(OPERATION_01_CODE, 1);
    }

    private static PublicationVersion getPublicationVersion06Operation0001Code000002() {
        return createPublicationVersionInOperation(OPERATION_01_CODE, 2);
    }

    private static PublicationVersion getPublicationVersion07Operation0001Code000003() {
        return createPublicationVersionInOperation(OPERATION_01_CODE, 3);
    }

    private static PublicationVersion getPublicationVersion08Operation0002Code000001() {
        return createPublicationVersionInOperation(OPERATION_02_CODE, 1);
    }

    private static PublicationVersion getPublicationVersion09Operation0002Code000002() {
        return createPublicationVersionInOperation(OPERATION_02_CODE, 2);
    }

    private static PublicationVersion getPublicationVersion10Operation0002Code000003() {
        return createPublicationVersionInOperation(OPERATION_02_CODE, 3);
    }

    private static PublicationVersion getPublicationVersion11Operation0002CodeMax() {
        return createPublicationVersionInOperation(OPERATION_01_CODE, 999999);
    }

    private static PublicationVersion getPublicationVersion12Draft() {
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion13ProductionValidation() {
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PRODUCTION_VALIDATION);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion14DiffusionValidation() {
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion15ValidationRejected() {
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.VALIDATION_REJECTED);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion16Published() {
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        publicationVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        PublicationLifecycleTestUtils.fillAsPublished(publicationVersion);
        return publicationVersion;
    }

    private static MockDescriptor getPublicationVersion17WithStructureForPublicationVersion04() {
        MockDescriptor publication = getPublicationMockDescriptor(PUBLICATION_04_STRUCTURED_WITH_2_PUBLICATION_VERSIONS_NAME);
        return new MockDescriptor(getPublicationVersionMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME), publication);
    }

    private static MockDescriptor getPublicationVersion18WithStructureForPublicationVersion04AndLastVersion() {
        MockDescriptor publication = getPublicationMockDescriptor(PUBLICATION_04_STRUCTURED_WITH_2_PUBLICATION_VERSIONS_NAME);
        return new MockDescriptor(getPublicationVersionMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME), publication);
    }

    private static PublicationVersion getPublicationVersion19WithStructureProductionValidation() {
        // General metadata
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setVersionLogic(INIT_VERSION);
        publicationVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(1));
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PRODUCTION_VALIDATION);

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

        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion20WithStructureDiffusionValidation() {
        // General metadata
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setVersionLogic(INIT_VERSION);
        publicationVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(1));
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);

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

        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion21WithStructureValidationRejected() {
        // General metadata
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setVersionLogic(INIT_VERSION);
        publicationVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(1));
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.VALIDATION_REJECTED);

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

        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion22WithComplexStructureDraft() {
        PublicationVersion publicationVersion = createComplexStructure();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion102WithComplexStructureForUrlPublication() {
        PublicationVersion publicationVersion = createComplexStructure2();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        return publicationVersion;
    }
    
    private static PublicationVersion getPublicationVersion23WithComplexStructureProductionValidation() {
        PublicationVersion publicationVersion = createComplexStructure();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PRODUCTION_VALIDATION);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion24WithComplexStructureDiffusionValidation() {
        PublicationVersion publicationVersion = createComplexStructure();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion25WithComplexStructureValidationRejected() {
        PublicationVersion publicationVersion = createComplexStructure();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.VALIDATION_REJECTED);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion26WithComplexStructurePublished() {
        PublicationVersion publicationVersion = createComplexStructure();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        publicationVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        return publicationVersion;
    }

    private static MockDescriptor getPublicationVersion27V1PublishedForPublication05() {
        MockDescriptor publication = getPublicationMockDescriptor(PublicationMockFactory.PUBLICATION_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME);
        return new MockDescriptor(getPublicationVersionMock(PUBLICATION_VERSION_27_V1_PUBLISHED_FOR_PUBLICATION_05_NAME), publication);
    }

    private static MockDescriptor getPublicationVersion28V2PublishedForPublication05() {
        MockDescriptor publication = getPublicationMockDescriptor(PUBLICATION_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME);
        return new MockDescriptor(getPublicationVersionMock(PUBLICATION_VERSION_28_V2_PUBLISHED_FOR_PUBLICATION_05_NAME), publication);
    }

    private static MockDescriptor getPublicationVersion29V3PublishedForPublication05() {
        MockDescriptor publication = getPublicationMockDescriptor(PUBLICATION_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME);
        return new MockDescriptor(getPublicationVersionMock(PUBLICATION_VERSION_29_V3_PUBLISHED_FOR_PUBLICATION_05_NAME), publication);
    }

    private static MockDescriptor getPublicationVersion30V1PublishedForPublication06() {
        MockDescriptor publication = getPublicationMockDescriptor(PUBLICATION_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME);
        return new MockDescriptor(getPublicationVersionMock(PUBLICATION_VERSION_30_V1_PUBLISHED_FOR_PUBLICATION_06_NAME), publication);
    }

    private static MockDescriptor getPublicationVersion31V2PublishedNoVisibleForPublication06() {
        MockDescriptor publication = getPublicationMockDescriptor(PUBLICATION_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME);
        return new MockDescriptor(getPublicationVersionMock(PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME), publication);
    }

    private static PublicationVersion getPublicationVersion32DraftNotReady() {
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion33DraftReadyForProductionValidation() {
        PublicationVersion publicationVersion = createPublicationVersion();
        prepareToProductionValidation(publicationVersion);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion34VersionRationaleTypeMinorErrata() {
        PublicationVersion publicationVersion = createPublicationVersion();
        prepareToProductionValidation(publicationVersion);
        publicationVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().add(new VersionRationaleType(VersionRationaleTypeEnum.MINOR_ERRATA));
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion35NextVersionNotScheduledDateFilled() {
        PublicationVersion publicationVersion = createPublicationVersion();
        prepareToProductionValidation(publicationVersion);
        publicationVersion.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);
        publicationVersion.getSiemacMetadataStatisticalResource().setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(10).toDate()));
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion36ProductionValidationNotReady() {
        PublicationVersion publicationVersion = createPublicationVersion();
        prepareToDiffusionValidation(publicationVersion);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion37ProductionValidationReadyForDiffusionValidation() {
        PublicationVersion publicationVersion = createPublicationVersion();
        prepareToDiffusionValidation(publicationVersion);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion38ProductionValidationReadyForValidationRejected() {
        PublicationVersion publicationVersion = createPublicationVersion();
        prepareToValidationRejected(publicationVersion);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion39PublishedWithNoRootMaintainer() {
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setMaintainer(StatisticalResourcesDoMocks.mockAgencyExternalItem("agency01", "ISTAC.agency01"));
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        publicationVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion41PubNotVisibleReplacesPubVersion42() {
        PublicationVersionMock template = new PublicationVersionMock();
        template.setSequentialId(1);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(1));
        PublicationVersion publicationVersion = createPublicationVersionFromTemplate(template);

        PublicationVersion publicationToReplace = createPublicationVersionWithSequenceAndVersion(2, INIT_VERSION);
        prepareToVersioning(publicationToReplace);
        registerPublicationVersionMock(PUBLICATION_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41_NAME, publicationToReplace);

        publicationVersion.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationToReplace));
        publicationToReplace.getSiemacMetadataStatisticalResource().setIsReplacedBy(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationVersion));
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion83PreparedToPublishOnlyVersionExternalItemFull() {
        PublicationVersionMock template = new PublicationVersionMock();
        template.setSequentialId(1);
        PublicationVersion publicationVersion = createPublicationVersionFromTemplate(template);

        fillOptionalExternalItems(publicationVersion);

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

        // multidatasets
        Multidataset multidatasetPublished = MultidatasetVersionMockFactory
                .createMultidatasetVersionPublishedLastVersion(MultidatasetMockFactory.createMultidatasetToAddVersions(1), INIT_VERSION, new DateTime()).getMultidataset();

        List<Multidataset> multidatasets = Arrays.asList(multidatasetPublished);

        populatePublicationWithStructureWithCubesOnRoot(publicationVersion, datasets, queries, multidatasets);

        prepareToPublished(publicationVersion);

        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion86WithElementLevelsNotPublished() {
        PublicationVersionMock template = new PublicationVersionMock();
        template.setSequentialId(1);
        PublicationVersion publicationVersion = createPublicationVersionFromTemplate(template);

        fillOptionalExternalItems(publicationVersion);

        // datasets
        Dataset datasetSingleVersionDraft = DatasetVersionMockFactory.createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.DRAFT).getDataset();
        registerDatasetMock(DATASET_25_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME, datasetSingleVersionDraft);

        Dataset datasetSingleVersionProductionValidation = DatasetVersionMockFactory.createDatasetVersionInStatusWithGeneratedDatasource(2, ProcStatusEnum.PRODUCTION_VALIDATION).getDataset();
        registerDatasetMock(DATASET_26_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME, datasetSingleVersionProductionValidation);

        Dataset datasetSingleVersionDiffusionValidation = DatasetVersionMockFactory.createDatasetVersionInStatusWithGeneratedDatasource(3, ProcStatusEnum.DIFFUSION_VALIDATION).getDataset();
        registerDatasetMock(DATASET_27_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME, datasetSingleVersionDiffusionValidation);

        Dataset datasetSingleVersionValidationRejected = DatasetVersionMockFactory.createDatasetVersionInStatusWithGeneratedDatasource(4, ProcStatusEnum.VALIDATION_REJECTED).getDataset();
        registerDatasetMock(DATASET_28_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME, datasetSingleVersionValidationRejected);

        Dataset datasetSingleVersionPublishedNotVisible = createDatasetVersionPublishedLastVersion(5, INIT_VERSION, new DateTime().plusDays(1)).getDataset();
        registerDatasetMock(DATASET_29_PUBLISHED_NOT_VISIBLE_USED_IN_PUBLICATION_VERSION_86_NAME, datasetSingleVersionPublishedNotVisible);

        List<Dataset> datasets = Arrays.asList(datasetSingleVersionDraft, datasetSingleVersionProductionValidation, datasetSingleVersionDiffusionValidation, datasetSingleVersionValidationRejected,
                datasetSingleVersionPublishedNotVisible);

        // queries
        Query queryDraft = QueryVersionMockFactory.createQueryVersionInStatus(buildQueryVersionMockSimpleWithFixedDatasetVersion("Q01"), ProcStatusEnum.DRAFT).getQuery();
        registerQueryMock(QUERY_16_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME, queryDraft);

        Query queryProductionValidation = QueryVersionMockFactory.createQueryVersionInStatus(buildQueryVersionMockSimpleWithFixedDatasetVersion("Q02"), ProcStatusEnum.PRODUCTION_VALIDATION)
                .getQuery();
        registerQueryMock(QUERY_17_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME, queryProductionValidation);

        Query queryDiffusionValidation = QueryVersionMockFactory.createQueryVersionInStatus(buildQueryVersionMockSimpleWithFixedDatasetVersion("Q03"), ProcStatusEnum.DIFFUSION_VALIDATION).getQuery();
        registerQueryMock(QUERY_18_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME, queryDiffusionValidation);

        Query queryValidationRejected = QueryVersionMockFactory.createQueryVersionInStatus(buildQueryVersionMockSimpleWithFixedDatasetVersion("Q04"), ProcStatusEnum.VALIDATION_REJECTED).getQuery();
        registerQueryMock(QUERY_19_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME, queryValidationRejected);

        DatasetVersion datasetVersionLinkedToQuery = createDatasetVersionPublishedLastVersion(5, INIT_VERSION, new DateTime().minusDays(3));
        Query queryPublishedNotVisible = QueryMockFactory.createPublishedQueryLinkedToDatasetVersion("Q05", datasetVersionLinkedToQuery, new DateTime().plusDays(1));
        registerQueryMock(QUERY_20_PUBLISHED_NOT_VISIBLE_USED_IN_PUBLICATION_VERSION_86_NAME, queryPublishedNotVisible);

        List<Query> queries = Arrays.asList(queryDraft, queryProductionValidation, queryDiffusionValidation, queryValidationRejected, queryPublishedNotVisible);

        // multidatasets
        Multidataset multidatasetDraft = MultidatasetVersionMockFactory.createMultidatasetVersionInStatus(buildBasicSingleVersionWithSequence(1), ProcStatusEnum.DRAFT).getMultidataset();
        registerMultidatasetMock(MULTIDATASET_08_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME, multidatasetDraft);

        Multidataset multidatasetProductionValidation = MultidatasetVersionMockFactory.createMultidatasetVersionInStatus(buildBasicSingleVersionWithSequence(2), ProcStatusEnum.PRODUCTION_VALIDATION)
                .getMultidataset();
        registerMultidatasetMock(MULTIDATASET_09_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME, multidatasetProductionValidation);

        Multidataset multidatasetDiffusionValidation = MultidatasetVersionMockFactory.createMultidatasetVersionInStatus(buildBasicSingleVersionWithSequence(3), ProcStatusEnum.DIFFUSION_VALIDATION)
                .getMultidataset();
        registerMultidatasetMock(MULTIDATASET_10_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME, multidatasetDiffusionValidation);

        Multidataset multidatasetValidationRejected = MultidatasetVersionMockFactory.createMultidatasetVersionInStatus(buildBasicSingleVersionWithSequence(4), ProcStatusEnum.VALIDATION_REJECTED)
                .getMultidataset();
        registerMultidatasetMock(MULTIDATASET_11_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME, multidatasetValidationRejected);

        List<Multidataset> multidatasets = Arrays.asList(multidatasetDraft, multidatasetProductionValidation, multidatasetDiffusionValidation, multidatasetValidationRejected);

        populatePublicationWithStructureWithCubesOnRoot(publicationVersion, datasets, queries, multidatasets);

        prepareToPublished(publicationVersion);

        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion87WithRelatedResourcesNotPublished() {
        PublicationVersionMock template = new PublicationVersionMock();
        template.setSequentialId(1);
        PublicationVersion publicationVersion = createPublicationVersionFromTemplate(template);
        fillOptionalExternalItems(publicationVersion);
        fillOptionalRelatedResourcesNotPublished(publicationVersion);
        prepareToPublished(publicationVersion);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion88WithNoCubes() {
        PublicationVersionMock template = new PublicationVersionMock();
        template.setSequentialId(1);
        PublicationVersion publicationVersion = createPublicationVersionFromTemplate(template);
        fillOptionalExternalItems(publicationVersion);
        prepareToPublished(publicationVersion);

        publicationVersion.getChildrenAllLevels().clear();
        publicationVersion.getChildrenFirstLevel().clear();

        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion89WithEmptyChapter() {
        PublicationVersionMock template = new PublicationVersionMock();
        template.setSequentialId(1);
        PublicationVersion publicationVersion = createPublicationVersionFromTemplate(template);
        fillOptionalExternalItems(publicationVersion);

        ElementLevel level = createDatasetCubeElementLevel(publicationVersion, DatasetMockFactory.createTwoPublishedVersionsForDataset(20));
        level.setOrderInLevel(1L);

        ElementLevel level02 = createChapterElementLevel(publicationVersion);
        level02.setOrderInLevel(2L);
        registerChapterMock(ChapterMockFactory.CHAPTER_05_EMPTY_IN_PUBLICATION_VERSION_89_NAME, level02.getChapter());

        prepareToPublished(publicationVersion);

        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion90WithEmptyCube() {
        PublicationVersionMock template = new PublicationVersionMock();
        template.setSequentialId(1);
        PublicationVersion publicationVersion = createPublicationVersionFromTemplate(template);
        fillOptionalExternalItems(publicationVersion);

        ElementLevel level = createDatasetCubeElementLevel(publicationVersion, null);
        level.setOrderInLevel(1L);
        registerCubeMock(CUBE_08_EMPTY_IN_PUBLICATION_VERSION_90_NAME, level.getCube());

        prepareToPublished(publicationVersion);

        return publicationVersion;
    }

    private static MockDescriptor getPublicationVersion91ReplacesPublication92() {
        PublicationVersionMock template = new PublicationVersionMock();
        template.setSequentialId(1);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(1));
        PublicationVersion publicationVersionReplaces = createPublicationVersionFromTemplate(template);

        PublicationVersion publicationReplaced = createPublicationVersionWithSequenceAndVersion(2, INIT_VERSION);
        registerPublicationVersionMock(PUBLICATION_VERSION_92_IS_REPLACED_BY_PUBLICATION_91_NAME, publicationReplaced);

        publicationVersionReplaces.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationReplaced));
        publicationReplaced.getSiemacMetadataStatisticalResource().setIsReplacedBy(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationVersionReplaces));

        return new MockDescriptor(publicationVersionReplaces, publicationReplaced);
    }

    private static MockDescriptor getPublicationVersion92IsReplacedByPublication91() {
        PublicationVersionMock template = new PublicationVersionMock();
        template.setSequentialId(1);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(1));
        PublicationVersion publicationVersionReplaces = createPublicationVersionFromTemplate(template);

        PublicationVersion publicationReplaced = createPublicationVersionWithSequenceAndVersion(2, INIT_VERSION);
        registerPublicationVersionMock(PUBLICATION_VERSION_91_REPLACES_PUBLICATION_92_NAME, publicationVersionReplaces);

        publicationVersionReplaces.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationReplaced));
        publicationReplaced.getSiemacMetadataStatisticalResource().setIsReplacedBy(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationVersionReplaces));

        return new MockDescriptor(publicationReplaced, publicationVersionReplaces);
    }

    private static PublicationVersion getPublicationVersion94NotVisible() {
        PublicationVersionMock template = new PublicationVersionMock();
        template.setSequentialId(1);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(1));
        PublicationVersion publicationVersion = createPublicationVersionInStatus(template, ProcStatusEnum.PUBLISHED);
        return publicationVersion;
    }

    private static MockDescriptor getPublicationVersion95NotVisibleIsReplacedByPublicationVersion96() {
        PublicationVersionMock template = new PublicationVersionMock();
        template.setSequentialId(1);
        template.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(1));
        PublicationVersion publicationReplaced = createPublicationVersionInStatus(template, ProcStatusEnum.PUBLISHED);

        PublicationVersionMock templateReplaces = new PublicationVersionMock();
        templateReplaces.setSequentialId(2);
        templateReplaces.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().plusDays(2));
        templateReplaces.getSiemacMetadataStatisticalResource().setReplaces(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationReplaced));
        PublicationVersion publicationVersionReplaces = createPublicationVersionInStatus(templateReplaces, ProcStatusEnum.PUBLISHED);

        registerPublicationVersionMock(PUBLICATION_VERSION_96_NOT_VISIBLE_REPLACES_PUBLICATION_VERSION_95_NAME, publicationVersionReplaces);

        return new MockDescriptor(publicationReplaced, publicationVersionReplaces);
    }

    private static PublicationVersion getPublicationVersion98ToDeleteWithPreviousVersion() {
        PublicationVersion publicationVersionToReplace = createPublicationVersionWithSequenceAndVersion(1, INIT_VERSION);
        prepareToVersioning(publicationVersionToReplace);

        PublicationVersion publicationVersion = createPublicationVersionWithSequenceAndVersion(1, SECOND_VERSION);
        registerPublicationVersionMock(PUBLICATION_VERSION_98_TO_DELETE_WITH_PREVIOUS_VERSION_NAME, publicationVersion);

        publicationVersion.getSiemacMetadataStatisticalResource().setReplacesVersion(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationVersionToReplace));
        publicationVersionToReplace.getSiemacMetadataStatisticalResource().setIsReplacedByVersion(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(publicationVersion));

        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion99WithPublishedMultidataset() {
        PublicationVersion publicationVersion = createStructureWithPublishedMultidataset();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        publicationVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion100MaximumVersionReached() {
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        publicationVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        PublicationLifecycleTestUtils.fillAsPublished(publicationVersion);
        publicationVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.MAXIMUM_VERSION_AVAILABLE);
        return publicationVersion;
    }

    private static PublicationVersion getPublicationVersion101MaximumMinorVersionReached() {
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        publicationVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime().minusDays(2));
        PublicationLifecycleTestUtils.fillAsPublished(publicationVersion);
        publicationVersion.getSiemacMetadataStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.MAXIMUM_MINOR_VERSION_AVAILABLE);
        return publicationVersion;
    }

    // -----------------------------------------------------------------
    // BUILDERS
    // -----------------------------------------------------------------

    protected static PublicationVersion buildPublicationVersionDraft(int sequentialId) {
        PublicationVersionMock version01Mock = new PublicationVersionMock();
        version01Mock.setVersionLogic(INIT_VERSION);
        version01Mock.setSequentialId(sequentialId);
        version01Mock.getSiemacMetadataStatisticalResource().setLastVersion(Boolean.TRUE);
        version01Mock.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(1));
        return getStatisticalResourcesPersistedDoMocks().mockPublicationVersion(version01Mock);
    }

    private static PublicationVersion createPublicationVersion() {
        return getStatisticalResourcesPersistedDoMocks().mockPublicationVersion();
    }

    private static PublicationVersion createPublicationVersionInOperation(String operationCode, int sequentialId) {
        PublicationVersionMock template = new PublicationVersionMock();
        template.setSequentialId(sequentialId);
        template.setStatisticalOperationCode(operationCode);
        return createPublicationVersionFromTemplate(template);
    }

    private static PublicationVersion createPublicationVersionWithSequenceAndVersion(Integer sequentialId, String version) {
        PublicationVersionMock template = new PublicationVersionMock();
        template.setSequentialId(sequentialId);
        template.setVersionLogic(version);
        return createPublicationVersionFromTemplate(template);
    }

    private static PublicationVersion createPublicationVersionFromTemplate(PublicationVersionMock template) {
        return getStatisticalResourcesPersistedDoMocks().mockPublicationVersion(template);
    }

    public static PublicationVersion createPublicationVersionPublishedPreviousVersion(PublicationMock publication, String version, DateTime validFrom, DateTime validTo) {
        PublicationVersionMock publicationVersion = PublicationVersionMockFactory.buildPublishedReadyPublicationVersion(publication, version, validFrom, validTo, false);
        return createPublicationVersionInStatus(publicationVersion, ProcStatusEnum.PUBLISHED);
    }

    public static PublicationVersion createPublicationVersionPublishedLastVersion(PublicationMock publication, String version, DateTime validFrom) {
        PublicationVersionMock publicationVersion = PublicationVersionMockFactory.buildPublishedReadyPublicationVersion(publication, version, validFrom, null, true);
        return createPublicationVersionInStatus(publicationVersion, ProcStatusEnum.PUBLISHED);
    }

    public static PublicationVersionMock buildPublishedReadyPublicationVersion(PublicationMock publication, String version, DateTime validFrom, DateTime validTo, boolean lastVersion) {
        PublicationVersionMock publicationVersion = new PublicationVersionMock();
        publicationVersion.setPublication(publication);
        publicationVersion.setVersionLogic(version);
        publicationVersion.getSiemacMetadataStatisticalResource().setLastVersion(lastVersion);

        if (validFrom.isAfterNow()) {
            publicationVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime());
        } else {
            publicationVersion.getSiemacMetadataStatisticalResource().setCreationDate(validFrom.minusHours(1));
        }
        publicationVersion.getSiemacMetadataStatisticalResource().setValidFrom(validFrom);
        publicationVersion.getSiemacMetadataStatisticalResource().setValidTo(validTo);

        return publicationVersion;
    }

    public static PublicationVersion createPublicationVersionInStatus(PublicationVersionMock publicationVersionMock, ProcStatusEnum status) {
        PublicationVersion publicationVersion = getStatisticalResourcesPersistedDoMocks().mockPublicationVersion(publicationVersionMock);

        switch (status) {
            case PRODUCTION_VALIDATION:
                PublicationLifecycleTestUtils.fillAsProductionValidation(publicationVersion);
                break;
            case DIFFUSION_VALIDATION:
                PublicationLifecycleTestUtils.fillAsDiffusionValidation(publicationVersion);
                break;
            case VALIDATION_REJECTED:
                PublicationLifecycleTestUtils.fillAsValidationRejected(publicationVersion);
                break;
            case PUBLISHED:
                PublicationLifecycleTestUtils.fillAsPublished(publicationVersion);
                break;
            case DRAFT:
                break;
            default:
                throw new IllegalArgumentException("Unsupported status " + status);
        }
        return publicationVersion;
    }

    public static PublicationVersionMock buildPublicationVersionSimplePreviousVersion(Publication publication, String version) {
        return buildPublicationVersionSimple(publication, version, false);
    }

    public static PublicationVersionMock buildPublicationVersionSimpleLastVersion(Publication publication, String version) {
        return buildPublicationVersionSimple(publication, version, true);
    }

    public static PublicationVersionMock buildPublicationVersionSimple(Publication publication, String version, boolean isLastVersion) {
        PublicationVersionMock publicationVersion = new PublicationVersionMock();
        publicationVersion.setPublication(publication);
        publicationVersion.setVersionLogic(version);
        publicationVersion.getSiemacMetadataStatisticalResource().setLastVersion(isLastVersion);
        return publicationVersion;
    }

    public static PublicationVersionMock createPublicationVersionSimple(Publication publication, String version, boolean isLastVersion) {
        PublicationVersionMock publicationVersion = buildPublicationVersionSimple(publication, version, isLastVersion);
        getStatisticalResourcesPersistedDoMocks().mockPublicationVersion(publicationVersion);
        return publicationVersion;
    }

    // -----------------------------------------------------------------
    // PUBLICATION STRUCTURE
    // -----------------------------------------------------------------

    public static ElementLevel createUrlCubeElementLevel(PublicationVersion publicationVersion, String url) {
        return getStatisticalResourcesPersistedDoMocks().mockUrlCubeElementLevel(publicationVersion, url);
    }

    protected static ElementLevel createUrlCubeElementLevel(PublicationVersion publicationVersion, String url, ElementLevel parentElementLevel) {
        return getStatisticalResourcesPersistedDoMocks().mockUrlCubeElementLevel(publicationVersion, url, parentElementLevel);
    }
    
    public static ElementLevel createQueryCubeElementLevel(PublicationVersion publicationVersion, Query query) {
        return getStatisticalResourcesPersistedDoMocks().mockQueryCubeElementLevel(publicationVersion, query);
    }

    protected static ElementLevel createQueryCubeElementLevel(PublicationVersion publicationVersion, Query query, ElementLevel parentElementLevel) {
        return getStatisticalResourcesPersistedDoMocks().mockQueryCubeElementLevel(publicationVersion, query, parentElementLevel);
    }

    protected static ElementLevel createDatasetCubeElementLevel(PublicationVersion publicationVersion, Dataset dataset, ElementLevel parentElementLevel) {
        return getStatisticalResourcesPersistedDoMocks().mockDatasetCubeElementLevel(publicationVersion, dataset, parentElementLevel);
    }

    public static ElementLevel createDatasetCubeElementLevel(PublicationVersion publicationVersion, Dataset dataset) {
        return getStatisticalResourcesPersistedDoMocks().mockDatasetCubeElementLevel(publicationVersion, dataset, null);
    }

    protected static ElementLevel createChapterElementLevel(PublicationVersion publicationVersion, ElementLevel parentElementLevel) {
        return getStatisticalResourcesPersistedDoMocks().mockChapterElementLevel(publicationVersion, parentElementLevel);
    }

    protected static ElementLevel createChapterElementLevel(PublicationVersion publicationVersion) {
        return getStatisticalResourcesPersistedDoMocks().mockChapterElementLevel(publicationVersion);
    }

    private static ElementLevel createMultidatasetCuveElementLevel(PublicationVersion publicationVersion, Multidataset multidataset) {
        return getStatisticalResourcesPersistedDoMocks().mockMultidatasetCuveElementLevel(publicationVersion, multidataset);
    }

    private static ElementLevel createMultidatasetCuveElementLevel(PublicationVersion publicationVersion, Multidataset multidataset, ElementLevel elementLevel) {
        return getStatisticalResourcesPersistedDoMocks().mockMultidatasetCuveElementLevel(publicationVersion, multidataset, elementLevel);
    }

    public static PublicationVersion populatePublicationWithStructureWithCubesOnRoot(PublicationVersion publicationVersion, List<Dataset> datasets, List<Query> queries,
            List<Multidataset> multidatasets) {

        for (Dataset dataset : datasets) {
            ElementLevel elementLevel = createDatasetCubeElementLevel(publicationVersion, dataset);
            elementLevel.setOrderInLevel(Long.valueOf(publicationVersion.getChildrenFirstLevel().size() + 1));
        }

        for (Query query : queries) {
            ElementLevel elementLevel = createQueryCubeElementLevel(publicationVersion, query);
            elementLevel.setOrderInLevel(Long.valueOf(publicationVersion.getChildrenFirstLevel().size() + 1));
        }

        for (Multidataset multidataset : multidatasets) {
            ElementLevel elementLevel = createMultidatasetCuveElementLevel(publicationVersion, multidataset);
            elementLevel.setOrderInLevel(Long.valueOf(publicationVersion.getChildrenFirstLevel().size() + 1));
        }

        return publicationVersion;
    }

    public static PublicationVersion populatePublicationWithStructureWithCubesOnChapters(PublicationVersion publicationVersion, List<Dataset> datasets, List<Query> queries) {

        for (Dataset dataset : datasets) {
            ElementLevel chapterLevel01 = createChapterElementLevel(publicationVersion);
            chapterLevel01.setOrderInLevel(Long.valueOf(publicationVersion.getChildrenFirstLevel().size() + 1));

            ElementLevel elementLevel = createDatasetCubeElementLevel(publicationVersion, dataset, chapterLevel01);
            elementLevel.setOrderInLevel(1L);
        }

        for (Query query : queries) {
            ElementLevel chapterLevel01 = createChapterElementLevel(publicationVersion);
            chapterLevel01.setOrderInLevel(Long.valueOf(publicationVersion.getChildrenFirstLevel().size() + 1));

            ElementLevel elementLevel = createQueryCubeElementLevel(publicationVersion, query, chapterLevel01);
            elementLevel.setOrderInLevel(1L);
        }

        return publicationVersion;
    }

    public static PublicationVersion populatePublicationWithStructureWithCubesOnChaptersNested(PublicationVersion publicationVersion, List<Dataset> datasets, List<Query> queries) {
        ElementLevel chapterLevel01 = createChapterElementLevel(publicationVersion);

        int level02Elements = 0;
        for (Dataset dataset : datasets) {
            ElementLevel chapterLevel = createChapterElementLevel(publicationVersion, chapterLevel01);
            chapterLevel.setOrderInLevel(Long.valueOf(level02Elements + 1));
            level02Elements++;

            ElementLevel elementLevel = createDatasetCubeElementLevel(publicationVersion, dataset, chapterLevel01);
            elementLevel.setOrderInLevel(1L);
        }

        for (Query query : queries) {
            ElementLevel chapterLevel = createChapterElementLevel(publicationVersion, chapterLevel01);
            chapterLevel.setOrderInLevel(Long.valueOf(level02Elements + 1));
            level02Elements++;

            ElementLevel elementLevel = createQueryCubeElementLevel(publicationVersion, query, chapterLevel01);
            elementLevel.setOrderInLevel(1L);
        }

        return publicationVersion;
    }

    public static PublicationVersion createComplexStructure() {
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setVersionLogic(INIT_VERSION);
        publicationVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(2));

        // Structure
        // Chapter 01
        ElementLevel elementLevel01 = createChapterElementLevel(publicationVersion);
        elementLevel01.setOrderInLevel(Long.valueOf(1));
        // --> Chapter 01.01
        ElementLevel elementLevel01_01 = createChapterElementLevel(publicationVersion, elementLevel01);
        elementLevel01_01.setOrderInLevel(Long.valueOf(1));
        // ----> Cube 01.01.01
        ElementLevel elementLevel01_01_01 = createDatasetCubeElementLevel(publicationVersion, DatasetMockFactory.generateDatasetWithGeneratedVersion(), elementLevel01_01);
        elementLevel01_01_01.setOrderInLevel(Long.valueOf(1));
        // --> Chapter 01.02
        ElementLevel elementLevel01_02 = createChapterElementLevel(publicationVersion, elementLevel01);
        elementLevel01_02.setOrderInLevel(Long.valueOf(2));
        // ----> Cube 01.02.01
        ElementLevel elementLevel01_02_01 = createQueryCubeElementLevel(publicationVersion, generateQueryWithGeneratedVersion(), elementLevel01_02);
        elementLevel01_02_01.setOrderInLevel(Long.valueOf(1));
     // ----> Cube 01.02.02
        ElementLevel elementLevel01_03 = createDatasetCubeElementLevel(publicationVersion, DatasetMockFactory.generateDatasetWithGeneratedVersion(), elementLevel01);
        elementLevel01_03.setOrderInLevel(Long.valueOf(3));
        // Chapter 02
        ElementLevel elementLevel02 = createChapterElementLevel(publicationVersion);
        elementLevel02.setOrderInLevel(Long.valueOf(2));
        // --> Cube 02.01
        ElementLevel elementLevel02_01 = createQueryCubeElementLevel(publicationVersion, generateQueryWithGeneratedVersion(), elementLevel02);
        elementLevel02_01.setOrderInLevel(Long.valueOf(1));
        // Chapter 03
        ElementLevel elementLevel03 = createChapterElementLevel(publicationVersion);
        elementLevel03.setOrderInLevel(Long.valueOf(3));
        // Cube 04
        ElementLevel elementLevel04 = createQueryCubeElementLevel(publicationVersion, generateQueryWithGeneratedVersion());
        elementLevel04.setOrderInLevel(Long.valueOf(4));
        return publicationVersion;
    }
    
    public static PublicationVersion createComplexStructure2() {
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setVersionLogic(INIT_VERSION);
        publicationVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime().minusDays(2));

        // Structure
        // Chapter 01
        ElementLevel elementLevel01 = createChapterElementLevel(publicationVersion);
        elementLevel01.setOrderInLevel(Long.valueOf(1));
        // --> Chapter 01.01
        ElementLevel elementLevel01_01 = createChapterElementLevel(publicationVersion, elementLevel01);
        elementLevel01_01.setOrderInLevel(Long.valueOf(1));
        // ----> Cube 01.01.01
        ElementLevel elementLevel01_01_01 = createDatasetCubeElementLevel(publicationVersion, DatasetMockFactory.generateDatasetWithGeneratedVersion(), elementLevel01_01);
        elementLevel01_01_01.setOrderInLevel(Long.valueOf(1));
        // --> Chapter 01.02
        ElementLevel elementLevel01_02 = createChapterElementLevel(publicationVersion, elementLevel01);
        elementLevel01_02.setOrderInLevel(Long.valueOf(2));
        // ----> Cube 01.02.01
        ElementLevel elementLevel01_02_01 = createQueryCubeElementLevel(publicationVersion, generateQueryWithGeneratedVersion(), elementLevel01_02);
        elementLevel01_02_01.setOrderInLevel(Long.valueOf(1));
     // ----> Cube 01.02.02
        ElementLevel elementLevel01_02_02 = createUrlCubeElementLevel(publicationVersion, generateUrl(false), elementLevel01_02);
        elementLevel01_02_02.setOrderInLevel(Long.valueOf(2));
        // --> Cube 01.03
        ElementLevel elementLevel01_03 = createDatasetCubeElementLevel(publicationVersion, DatasetMockFactory.generateDatasetWithGeneratedVersion(), elementLevel01);
        elementLevel01_03.setOrderInLevel(Long.valueOf(3));
        // Chapter 02
        ElementLevel elementLevel02 = createChapterElementLevel(publicationVersion);
        elementLevel02.setOrderInLevel(Long.valueOf(2));
        // --> Cube 02.01
        ElementLevel elementLevel02_01 = createQueryCubeElementLevel(publicationVersion, generateQueryWithGeneratedVersion(), elementLevel02);
        elementLevel02_01.setOrderInLevel(Long.valueOf(1));
        // Chapter 03
        ElementLevel elementLevel03 = createChapterElementLevel(publicationVersion);
        elementLevel03.setOrderInLevel(Long.valueOf(3));
        // Cube 04
        ElementLevel elementLevel04 = createQueryCubeElementLevel(publicationVersion, generateQueryWithGeneratedVersion());
        elementLevel04.setOrderInLevel(Long.valueOf(4));
     // Cube 04
        ElementLevel elementLevel05 = createUrlCubeElementLevel(publicationVersion, generateUrl(false));
        elementLevel05.setOrderInLevel(Long.valueOf(5));
        return publicationVersion;
    }

    public static PublicationVersion createStructureWithPublishedMultidataset() {
        PublicationVersion publicationVersion = createPublicationVersion();
        publicationVersion.getSiemacMetadataStatisticalResource().setVersionLogic(INIT_VERSION);
        publicationVersion.getSiemacMetadataStatisticalResource().setCreationDate(new DateTime());

        ElementLevel elementLevel = createChapterElementLevel(publicationVersion);
        elementLevel.setOrderInLevel(Long.valueOf(1));

        ElementLevel elementLevelMultidataset = createMultidatasetCuveElementLevel(publicationVersion, MultidatasetVersionMockFactory
                .createMultidatasetVersionPublishedLastVersion(MultidatasetMockFactory.createMultidatasetToAddVersions(1), INIT_VERSION, new DateTime()).getMultidataset(), elementLevel);
        elementLevelMultidataset.setOrderInLevel(Long.valueOf(1));

        return publicationVersion;
    }

    // -----------------------------------------------------------------
    // LIFE CYCLE PREPARATIONS
    // -----------------------------------------------------------------

    // -----------------------------------------------------------------
    // UTILS
    // -----------------------------------------------------------------
    public static void fillOptionalExternalItems(PublicationVersion publicationVersion) {
        LifecycleTestUtils.fillOptionalExternalItemsSiemacResource(publicationVersion);

        // Not specific external items for Publication Version
    }

    private static void fillOptionalRelatedResourcesNotPublished(PublicationVersion publicationVersion) {
        publicationVersion.getSiemacMetadataStatisticalResource()
                .setReplaces(StatisticalResourcesPersistedDoMocks.mockPublicationVersionRelated(createPublicationVersionWithSequenceAndVersion(1, INIT_VERSION)));
    }

}
