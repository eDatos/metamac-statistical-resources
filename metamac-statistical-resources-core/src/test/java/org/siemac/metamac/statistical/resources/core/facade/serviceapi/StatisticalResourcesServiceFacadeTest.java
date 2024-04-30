package org.siemac.metamac.statistical.resources.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDay;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsInternationalStringDto;
import static org.siemac.metamac.statistical.resources.core.error.utils.ServiceExceptionParametersUtils.addParameter;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.CommonAsserts.assertEqualsCollectionByField;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsCategorisation;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDataset;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasetVersion;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasetVersionBase;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasource;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasourceDoAndDtoCollection;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDimensionRepresentationMapping;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.MultidatasetsAsserts.assertEqualsMultidataset;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.MultidatasetsAsserts.assertEqualsMultidatasetCube;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.MultidatasetsAsserts.assertEqualsMultidatasetVersion;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.MultidatasetsAsserts.assertEqualsMultidatasetVersionBase;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertEqualsChapter;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertEqualsCube;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertEqualsPublication;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertEqualsPublicationVersion;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertEqualsPublicationVersionBase;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertEqualsPublicationVersionStructure;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.QueryAsserts.assertEqualsQuery;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.QueryAsserts.assertEqualsQuerySelection;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.QueryAsserts.assertEqualsQueryVersion;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.QueryAsserts.assertEqualsQueryVersionDoAndDtoCollection;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_01_DATASET_VERSION_01_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_01_DATASET_VERSION_03_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_02_DATASET_VERSION_01_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_MAINTAINER_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_SEQUENCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.ChapterMockFactory.CHAPTER_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CubeMockFactory.CUBE_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CubeMockFactory.CUBE_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_04_FULL_FILLED_WITH_1_DATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_03_FOR_DATASET_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_05_FOR_DATASET_04_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_06_FOR_QUERIES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_09_OPER_0001_CODE_000003_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_10_OPER_0002_CODE_000001_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_11_OPER_0002_CODE_000002_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_12_OPER_0002_MAX_CODE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_13_OPER_0002_CODE_000003_PROD_VAL_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_20_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_39_VERSION_RATIONALE_TYPE_MAJOR_NEW_RESOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_40_VERSION_RATIONALE_TYPE_MAJOR_ESTIMATORS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_41_VERSION_RATIONALE_TYPE_MINOR_ERRATAS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_42_VERSION_RATIONALE_TYPE_MINOR_ERRATAS_AND_MAJOR_ESTIMATORS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_43_NEXT_VERSION_NO_UPDATES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_44_NEXT_VERSION_NON_SCHEDULED_UPDATE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_45_NEXT_VERSION_SCHEDULED_UPDATE_JANUARY_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_46_NEXT_VERSION_SCHEDULED_UPDATE_JULY_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_48_WITH_TEMPORAL_COVERAGE_FILLED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_70_PREPARED_TO_PUBLISH_EXTERNAL_ITEM_FULL_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_88_PUBLISHED_WITH_CATEGORISATIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasourceMockFactory.DATASOURCE_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DimensionRepresentationMappingMockFactory.DIMENSION_REPRESENTATION_MAPPING_01_DATASET_01_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetCubeMockFactory.MULTIDATASET_CUBE_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetCubeMockFactory.MULTIDATASET_CUBE_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_04_STRUCTURED_WITH_2_MULTIDATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_05_OPERATION_0001_CODE_000001_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_06_OPERATION_0001_CODE_000002_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_08_OPERATION_0002_CODE_000001_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_09_OPERATION_0002_CODE_000002_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_10_OPERATION_0002_CODE_000003_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_12_DRAFT_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_16_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_18_WITH_STRUCTURE_FOR_MULTIDATASET_VERSION_04_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_33_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_37_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_38_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationMockFactory.PUBLICATION_02_BASIC_WITH_GENERATED_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationMockFactory.PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_05_OPERATION_0001_CODE_000001_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_06_OPERATION_0001_CODE_000002_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_08_OPERATION_0002_CODE_000001_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_09_OPERATION_0002_CODE_000002_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_10_OPERATION_0002_CODE_000003_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_12_DRAFT_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_16_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_33_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_37_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_38_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_01_SIMPLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_02_BASIC_WITH_GENERATED_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_01_WITH_SELECTION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_02_BASIC_ORDERED_01_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_03_BASIC_ORDERED_02_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_04_BASIC_ORDERED_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_06_BASIC_ACTIVE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_07_BASIC_ACTIVE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_08_BASIC_DISCONTINUED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_10_ACTIVE_LATEST_DATA_5_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_11_DRAFT_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_14_VALIDATION_REJECTED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_15_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_19_WITH_CODE_AND_URN_QUERY01_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_20_WITH_CODE_AND_URN_QUERY02_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_21_FOR_QUERY_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_22_FOR_QUERY_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_37_PREPARED_TO_PUBLISH_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticOfficialityMockFactory.STATISTIC_OFFICIALITY_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticOfficialityMockFactory.STATISTIC_OFFICIALITY_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDtoMocks.mockCodeItemDtosWithIdentifiers;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDtoMocks.mockQueryVersionDto;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructureComponents;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.common.criteria.enums.StatisticalResourcesCriteriaOrderEnum;
import org.siemac.metamac.statistical.resources.core.common.criteria.enums.StatisticalResourcesCriteriaPropertyEnum;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DimensionRepresentationMapping;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.dataset.utils.DatasetVersionUtils;
import org.siemac.metamac.statistical.resources.core.dto.BasicVersionableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionMainCoveragesDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DimensionRepresentationMappingDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.StatisticOfficialityDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetCubeDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.ChapterDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.CubeDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationStructureDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionSingleParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetCube;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.Chapter;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItemRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItemRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.stream.serviceapi.StreamMessagingServiceFacade;
import org.siemac.metamac.statistical.resources.core.utils.DataMockUtils;
import org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticalResourcesMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDtoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesNotPersistedDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/stream-messaging-service-facade-mockito.xml",
        "classpath:spring/statistical-resources/include/dataset-repository-mockito.xml", "classpath:spring/statistical-resources/include/rest-services-mockito.xml",
        "classpath:spring/statistical-resources/include/external-item-checker-mockito.xml", "classpath:spring/statistical-resources/include/task-mockito.xml",
        "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class StatisticalResourcesServiceFacadeTest extends StatisticalResourcesBaseTest implements StatisticalResourcesServiceFacadeTestBase {

    private static final String               SIEMAC_METADATA_URN_FIELD = "siemacMetadataStatisticalResource.urn";
    private static final String               URN_FIELD                 = "urn";

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    @Autowired
    private QuerySelectionItemRepository      querySelectionItemRepository;

    @Autowired
    private CodeItemRepository                codeItemRepository;

    @Autowired
    private SrmRestInternalService            srmRestInternalService;

    @Autowired
    private DatasetRepositoriesServiceFacade  datasetRepositoriesServiceFacade;

    @Autowired
    StreamMessagingServiceFacade              streamMessagingServiceFacade;

    @Before
    public void onBeforeTest() throws Exception {
        DataStructure emptyDsd = new DataStructure();
        emptyDsd.setDataStructureComponents(new DataStructureComponents());

        Mockito.when(srmRestInternalService.retrieveDsdByUrn(Mockito.anyString())).thenReturn(emptyDsd);
    }

    // ------------------------------------------------------------------------
    // QUERIES
    // ------------------------------------------------------------------------

    @Override
    @Test
    @MetamacMock({QUERY_02_BASIC_WITH_GENERATED_VERSION_NAME, QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME})
    public void testFindQueriesByCondition() throws Exception {
        QueryVersion latestQueryVersionQuery02 = queryMockFactory.retrieveMock(QUERY_02_BASIC_WITH_GENERATED_VERSION_NAME).getVersions().get(0);
        QueryVersion latestQueryVersionQuery03 = queryVersionMockFactory.retrieveMock(QUERY_VERSION_22_FOR_QUERY_03_AND_LAST_VERSION_NAME);

        MetamacCriteria metamacCriteria = new MetamacCriteria();

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findQueriesByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(2, results.size());

        assertEqualsQuery(latestQueryVersionQuery02, results.get(0));
        assertEqualsQuery(latestQueryVersionQuery03, results.get(1));
    }

    @Test
    @MetamacMock({QUERY_02_BASIC_WITH_GENERATED_VERSION_NAME, QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME})
    public void testFindQueriesByConditionTitle() throws Exception {
        QueryVersion latestQueryVersionQuery03 = queryVersionMockFactory.retrieveMock(QUERY_VERSION_22_FOR_QUERY_03_AND_LAST_VERSION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        String title = latestQueryVersionQuery03.getLifeCycleStatisticalResource().getTitle().getLocalisedLabel("es");
        setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.TITLE, OperationType.LIKE, title);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findQueriesByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsQuery(latestQueryVersionQuery03, results.get(0));
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testFindQueriesByConditionProcStatusDraft() throws Exception {
        QueryVersion expectedResult = queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DRAFT);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findQueriesByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsQuery(expectedResult, results.get(0));
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testFindQueriesByConditionProcStatusProductionValidation() throws Exception {
        QueryVersion expectedResult = queryVersionMockFactory.retrieveMock(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PRODUCTION_VALIDATION);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findQueriesByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsQuery(expectedResult, results.get(0));
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testFindQueriesByConditionProcStatusDiffusionValidation() throws Exception {
        QueryVersion expectedResult = queryVersionMockFactory.retrieveMock(QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DIFFUSION_VALIDATION);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findQueriesByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsQuery(expectedResult, results.get(0));
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testFindQueriesByConditionProcStatusValidationRejected() throws Exception {
        QueryVersion expectedResult = queryVersionMockFactory.retrieveMock(QUERY_VERSION_14_VALIDATION_REJECTED_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.VALIDATION_REJECTED);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findQueriesByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsQuery(expectedResult, results.get(0));
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME})
    public void testFindQueriesByConditionProcStatusPublished() throws Exception {
        QueryVersion expectedResult = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PUBLISHED);
        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findQueriesByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsQuery(expectedResult, results.get(0));
    }

    // ------------------------------------------------------------------------
    // QUERIES VERSIONS
    // ------------------------------------------------------------------------

    @Override
    @Test
    @MetamacMock({QUERY_VERSION_01_WITH_SELECTION_NAME, QUERY_VERSION_02_BASIC_ORDERED_01_NAME})
    public void testRetrieveQueryVersionByUrn() throws Exception {
        QueryVersionDto actual = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(),
                queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME).getLifeCycleStatisticalResource().getUrn());
        assertEqualsQueryVersion(queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME), actual);
    }

    @Override
    @Test
    @MetamacMock({QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME})
    public void testRetrieveLatestQueryVersion() throws Exception {
        String queryUrn = queryMockFactory.retrieveMock(QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_22_FOR_QUERY_03_AND_LAST_VERSION_NAME);
        QueryVersionDto actual = statisticalResourcesServiceFacade.retrieveLatestQueryVersion(getServiceContextAdministrador(), queryUrn);
        assertEqualsQueryVersion(expected, actual);
    }

    @Override
    @Test
    @MetamacMock({QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME})
    public void testRetrieveLatestPublishedQueryVersion() throws Exception {
        String queryUrn = queryMockFactory.retrieveMock(QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_21_FOR_QUERY_03_NAME);
        QueryVersionDto actual = statisticalResourcesServiceFacade.retrieveLatestPublishedQueryVersion(getServiceContextAdministrador(), queryUrn);
        assertEqualsQueryVersion(expected, actual);
    }

    @Override
    @Test
    @MetamacMock({QUERY_VERSION_02_BASIC_ORDERED_01_NAME, QUERY_VERSION_03_BASIC_ORDERED_02_NAME, QUERY_VERSION_04_BASIC_ORDERED_03_NAME, QUERY_VERSION_10_ACTIVE_LATEST_DATA_5_NAME,
            QUERY_VERSION_01_WITH_SELECTION_NAME})
    public void testRetrieveQueriesVersions() throws Exception {
        List<QueryVersion> expected = queryVersionMockFactory.retrieveMocks(QUERY_VERSION_02_BASIC_ORDERED_01_NAME, QUERY_VERSION_03_BASIC_ORDERED_02_NAME, QUERY_VERSION_04_BASIC_ORDERED_03_NAME,
                QUERY_VERSION_10_ACTIVE_LATEST_DATA_5_NAME, QUERY_VERSION_01_WITH_SELECTION_NAME);
        List<QueryVersionBaseDto> actual = statisticalResourcesServiceFacade.retrieveQueriesVersions(getServiceContextAdministrador());

        assertEqualsQueryVersionDoAndDtoCollection(expected, actual);
    }

    @Test(expected = AssertionError.class)
    @MetamacMock({QUERY_VERSION_02_BASIC_ORDERED_01_NAME, QUERY_VERSION_03_BASIC_ORDERED_02_NAME, QUERY_VERSION_04_BASIC_ORDERED_03_NAME})
    public void testRetrieveQueriesErrorDifferentResponse() throws Exception {
        List<QueryVersion> expected = queryVersionMockFactory.retrieveMocks(QUERY_VERSION_02_BASIC_ORDERED_01_NAME, QUERY_VERSION_03_BASIC_ORDERED_02_NAME);
        List<QueryVersionBaseDto> actual = statisticalResourcesServiceFacade.retrieveQueriesVersions(getServiceContextAdministrador());

        assertEqualsQueryVersionDoAndDtoCollection(expected, actual);
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_06_FOR_QUERIES_NAME)
    public void testCreateQuery() throws Exception {
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto();

        QueryVersionDto queryToPersist = mockQueryVersionDto(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME));
        queryToPersist.getSelection().clear();
        queryToPersist.getSelection().put("DIM_01", Arrays.asList(new CodeItemDto("CODE_01", "code 01")));
        queryToPersist.getSelection().put("DIM_02", Arrays.asList(new CodeItemDto("CODE_11", "code 11")));

        QueryVersionDto persistedQuery = statisticalResourcesServiceFacade.createQuery(getServiceContextAdministrador(), queryToPersist, statisticalOperation);
        assertNotNull(persistedQuery);
        assertNotNull(persistedQuery.getUrn());
    }

    @MetamacMock(DATASET_VERSION_06_FOR_QUERIES_NAME)
    @Test
    public void testCreateQueryHasExpectedUrn() throws Exception {
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto();
        ExternalItemDto maintainer = StatisticalResourcesDtoMocks.mockAgencyExternalItemDto("SIEMAC");
        QueryVersionDto queryVersionDto = StatisticalResourcesDtoMocks.mockQueryVersionDto(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME));
        queryVersionDto.setMaintainer(maintainer);
        queryVersionDto.getSelection().clear();
        queryVersionDto.getSelection().put("DIM_01", Arrays.asList(new CodeItemDto("CODE_01", "code 01")));
        queryVersionDto.getSelection().put("DIM_02", Arrays.asList(new CodeItemDto("CODE_11", "code 11")));
        QueryVersionDto queryVersionGenerated = statisticalResourcesServiceFacade.createQuery(getServiceContextAdministrador(), queryVersionDto, statisticalOperation);
        String persistedQueryUrn = queryVersionGenerated.getUrn();
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Query=SIEMAC:" + statisticalOperation.getCode() + "_000001(1.0)", persistedQueryUrn);
    }

    @Test
    public void testUpdateQueryVersionErrorDuplicatedUrn() throws Exception {
        // Code and maintainer can not be modified after creation
    }

    @Override
    @Test
    @MetamacMock({QUERY_VERSION_01_WITH_SELECTION_NAME, QUERY_VERSION_02_BASIC_ORDERED_01_NAME})
    public void testUpdateQueryVersion() throws Exception {
        QueryVersionDto expectedQuery = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(),
                queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME).getLifeCycleStatisticalResource().getUrn());
        expectedQuery.setTitle(StatisticalResourcesDoMocks.mockInternationalStringDto());

        QueryVersionDto actualQuery = statisticalResourcesServiceFacade.updateQueryVersion(getServiceContextAdministrador(), expectedQuery);
        assertNotNull(actualQuery);
        assertEqualsInternationalStringDto(expectedQuery.getTitle(), actualQuery.getTitle());
    }

    @Test
    @MetamacMock(QUERY_VERSION_01_WITH_SELECTION_NAME)
    public void testUpdateQueryVersionSelection() throws Exception {
        QueryVersionDto expectedQuery = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(),
                queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME).getLifeCycleStatisticalResource().getUrn());

        expectedQuery.getSelection().clear();
        expectedQuery.getSelection().put("DIM01", Arrays.asList(new CodeItemDto("CODE02", "CODE02")));
        expectedQuery.getSelection().put("DIM02", Arrays.asList(new CodeItemDto("CODE01", "CODE01"), new CodeItemDto("CODE02", "CODE02")));

        // Service operation
        QueryVersionDto actualQuery = statisticalResourcesServiceFacade.updateQueryVersion(getServiceContextAdministrador(), expectedQuery);

        // Checks
        int querySelectionItemsAfter = querySelectionItemRepository.findAll().size();
        int codeItemsAfter = codeItemRepository.findAll().size();

        assertEquals(2, querySelectionItemsAfter);
        assertEquals(3, codeItemsAfter);
        assertNotNull(actualQuery);
        assertEqualsQuerySelection(expectedQuery.getSelection(), actualQuery.getSelection());
    }

    @Test
    @MetamacMock({QUERY_VERSION_01_WITH_SELECTION_NAME, QUERY_VERSION_02_BASIC_ORDERED_01_NAME})
    public void testUpdateQueryVersionDontUpdateStatus() throws Exception {
        QueryVersionDto expectedQuery = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(),
                queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME).getLifeCycleStatisticalResource().getUrn());
        QueryStatusEnum expectedStatus = expectedQuery.getStatus();
        expectedQuery.setStatus(QueryStatusEnum.DISCONTINUED);
        assertTrue(!expectedStatus.equals(expectedQuery.getStatus()));

        QueryVersionDto actualQuery = statisticalResourcesServiceFacade.updateQueryVersion(getServiceContextAdministrador(), expectedQuery);
        assertEquals(expectedStatus, actualQuery.getStatus());
    }

    @Test
    @MetamacMock({QUERY_VERSION_01_WITH_SELECTION_NAME})
    public void testUpdateQueryVersionIgnoreChangeMaintainer() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME);
        String originalMaintainerCode = queryVersion.getLifeCycleStatisticalResource().getMaintainer().getCodeNested();

        QueryVersionDto queryDto = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(), queryVersion.getLifeCycleStatisticalResource().getUrn());
        ExternalItemDto maintainer = StatisticalResourcesDtoMocks.mockAgencyExternalItemDto();
        queryDto.setMaintainer(maintainer);

        QueryVersionDto updatedQuery = statisticalResourcesServiceFacade.updateQueryVersion(getServiceContextAdministrador(), queryDto);
        assertNotNull(updatedQuery);
        assertEquals(originalMaintainerCode, updatedQuery.getMaintainer().getCodeNested());
    }

    @Test
    @MetamacMock({QUERY_VERSION_01_WITH_SELECTION_NAME})
    public void testUpdateQueryVersionIgnoreChangeStatisticalOperation() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME);
        String originalStatisticalOperationCode = queryVersion.getLifeCycleStatisticalResource().getStatisticalOperation().getCode();

        QueryVersionDto queryVersionDto = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(), queryVersion.getLifeCycleStatisticalResource().getUrn());
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto();
        queryVersionDto.setStatisticalOperation(statisticalOperation);

        QueryVersionDto updatedQueryVersion = statisticalResourcesServiceFacade.updateQueryVersion(getServiceContextAdministrador(), queryVersionDto);
        assertNotNull(updatedQueryVersion);
        assertEquals(originalStatisticalOperationCode, updatedQueryVersion.getStatisticalOperation().getCode());
    }

    @Test
    @MetamacMock({QUERY_VERSION_19_WITH_CODE_AND_URN_QUERY01_NAME, QUERY_VERSION_20_WITH_CODE_AND_URN_QUERY02_NAME})
    public void testUpdateQueryVersionIgnoreChangeCodeForNonPublishedQueryVersion() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_19_WITH_CODE_AND_URN_QUERY01_NAME);
        QueryVersionDto queryVersionDto = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(), queryVersion.getLifeCycleStatisticalResource().getUrn());

        queryVersion.getLifeCycleStatisticalResource().setCode("newCode");
        QueryVersionDto updatedQueryVersion = statisticalResourcesServiceFacade.updateQueryVersion(getServiceContextAdministrador(), queryVersionDto);
        assertEquals(queryVersionDto.getCode(), updatedQueryVersion.getCode());
    }

    @Test
    @MetamacMock({QUERY_VERSION_15_PUBLISHED_NAME})
    public void testUpdateQueryVersionIgnoreChangeCodeForPublishedQueryVersion() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);
        QueryVersionDto queryVersionDto = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(), queryVersion.getLifeCycleStatisticalResource().getUrn());

        queryVersion.getLifeCycleStatisticalResource().setCode("newCode");
        QueryVersionDto updatedQueryVersion = statisticalResourcesServiceFacade.updateQueryVersion(getServiceContextAdministrador(), queryVersionDto);
        assertEquals(queryVersionDto.getCode(), updatedQueryVersion.getCode());
    }

    @Override
    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_01_WITH_SELECTION_NAME})
    public void testDeleteQueryVersion() throws Exception {
        String urn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME).getLifeCycleStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.QUERY_NOT_FOUND, urn));

        statisticalResourcesServiceFacade.deleteQueryVersion(getServiceContextAdministrador(), urn);
        statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(), urn);
    }

    @Override
    @Test
    @MetamacMock({QUERY_VERSION_02_BASIC_ORDERED_01_NAME, QUERY_VERSION_03_BASIC_ORDERED_02_NAME, QUERY_VERSION_04_BASIC_ORDERED_03_NAME})
    public void testFindQueriesVersionsByCondition() throws Exception {
        // Find all
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);

            MetamacCriteriaResult<QueryVersionBaseDto> queriesPagedResult = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(3, queriesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(3, queriesPagedResult.getResults().size());
            assertTrue(queriesPagedResult.getResults().get(0) instanceof QueryVersionBaseDto);

            assertEquals(queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME).getLifeCycleStatisticalResource().getUrn(), queriesPagedResult.getResults().get(0).getUrn());
            assertEquals(queryVersionMockFactory.retrieveMock(QUERY_VERSION_03_BASIC_ORDERED_02_NAME).getLifeCycleStatisticalResource().getUrn(), queriesPagedResult.getResults().get(1).getUrn());
            assertEquals(queryVersionMockFactory.retrieveMock(QUERY_VERSION_04_BASIC_ORDERED_03_NAME).getLifeCycleStatisticalResource().getUrn(), queriesPagedResult.getResults().get(2).getUrn());
        }

        // Find code
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);

            // Restrictions
            String code = queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME).getLifeCycleStatisticalResource().getCode();

            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.CODE, OperationType.EQ, code);

            MetamacCriteriaResult<QueryVersionBaseDto> queriesPagedResult = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(1, queriesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(1, queriesPagedResult.getResults().size());
            assertTrue(queriesPagedResult.getResults().get(0) instanceof QueryVersionBaseDto);

            assertEquals(queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME).getLifeCycleStatisticalResource().getUrn(), queriesPagedResult.getResults().get(0).getUrn());
        }

        // Find URN
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);

            // Restrictions
            String urn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME).getLifeCycleStatisticalResource().getUrn();

            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.URN, OperationType.EQ, urn);

            MetamacCriteriaResult<QueryVersionBaseDto> queriesPagedResult = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(1, queriesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(1, queriesPagedResult.getResults().size());
            assertTrue(queriesPagedResult.getResults().get(0) instanceof QueryVersionBaseDto);

            assertEquals(queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME).getLifeCycleStatisticalResource().getUrn(), queriesPagedResult.getResults().get(0).getUrn());
        }

        // Find title
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);

            // Restrictions
            String titleQuery = queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME).getLifeCycleStatisticalResource().getTitle().getLocalisedLabel("es");

            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.TITLE, OperationType.EQ, titleQuery);

            MetamacCriteriaResult<QueryVersionBaseDto> queriesPagedResult = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(1, queriesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(1, queriesPagedResult.getResults().size());
            assertTrue(queriesPagedResult.getResults().get(0) instanceof QueryVersionBaseDto);

            assertEquals(queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME).getLifeCycleStatisticalResource().getUrn(), queriesPagedResult.getResults().get(0).getUrn());
        }
    }

    @Test
    @MetamacMock({QUERY_VERSION_02_BASIC_ORDERED_01_NAME, QUERY_VERSION_03_BASIC_ORDERED_02_NAME, QUERY_VERSION_04_BASIC_ORDERED_03_NAME})
    public void testFindQueriesVersionsByConditionWithMetamacCriteriaDontThrowError() throws Exception {
        statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), null);
    }

    @Test
    @MetamacMock({QUERY_VERSION_02_BASIC_ORDERED_01_NAME, QUERY_VERSION_03_BASIC_ORDERED_02_NAME})
    public void testFindQueriesVersionsByConditionCheckLastUpdatedIsDefaultOrder() throws Exception {
        String urnQueryVersion03 = queryVersionMockFactory.retrieveMock(QUERY_VERSION_03_BASIC_ORDERED_02_NAME).getLifeCycleStatisticalResource().getUrn();

        MetamacCriteria metamacCriteria = new MetamacCriteria();

        MetamacCriteriaResult<QueryVersionBaseDto> queriesPagedResult = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);

        // Update queryVersion 03
        QueryVersionDto queryVersionDto03 = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(), urnQueryVersion03);
        statisticalResourcesServiceFacade.updateQueryVersion(getServiceContextAdministrador(), queryVersionDto03);

        // Search
        queriesPagedResult = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);

        // Validate
        assertEquals(2, queriesPagedResult.getPaginatorResult().getTotalResults().intValue());
        assertEquals(2, queriesPagedResult.getResults().size());
        assertTrue(queriesPagedResult.getResults().get(0) instanceof QueryVersionBaseDto);

        Date lastUpdated01 = queriesPagedResult.getResults().get(0).getLastUpdated();
        Date lastUpdated02 = queriesPagedResult.getResults().get(1).getLastUpdated();
        assertTrue(lastUpdated01.before(lastUpdated02));

        assertEquals(queryVersionDto03.getUrn(), queriesPagedResult.getResults().get(1).getUrn());
    }
    @Test
    @MetamacMock({QUERY_VERSION_06_BASIC_ACTIVE_NAME, QUERY_VERSION_07_BASIC_ACTIVE_NAME, QUERY_VERSION_08_BASIC_DISCONTINUED_NAME})
    public void testFindQueriesVersionsByConditionUsingStatus() throws Exception {

        // ACTIVE
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();

            setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.QUERY_STATUS, OperationType.EQ, QueryStatusEnum.ACTIVE);

            MetamacCriteriaResult<QueryVersionBaseDto> queriesPagedResult = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(2, queriesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(2, queriesPagedResult.getResults().size());
            assertTrue(queriesPagedResult.getResults().get(0) instanceof QueryVersionBaseDto);
        }

        // DISCONTINUED
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();

            setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.QUERY_STATUS, OperationType.EQ, QueryStatusEnum.DISCONTINUED);

            MetamacCriteriaResult<QueryVersionBaseDto> queriesPagedResult = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(1, queriesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(1, queriesPagedResult.getResults().size());
            assertTrue(queriesPagedResult.getResults().get(0) instanceof QueryVersionBaseDto);
        }

        // ACTIVE or DISCONTINUED
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();

            MetamacCriteriaDisjunctionRestriction disjunction = new MetamacCriteriaDisjunctionRestriction();
            disjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.QUERY_STATUS.name(), QueryStatusEnum.ACTIVE, OperationType.EQ));
            disjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.QUERY_STATUS.name(), QueryStatusEnum.DISCONTINUED, OperationType.EQ));
            metamacCriteria.setRestriction(disjunction);

            MetamacCriteriaResult<QueryVersionBaseDto> queriesPagedResult = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(3, queriesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(3, queriesPagedResult.getResults().size());
            assertTrue(queriesPagedResult.getResults().get(0) instanceof QueryVersionBaseDto);
        }
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testFindQueriesVersionsByConditionProcStatusDraft() throws Exception {
        QueryVersion expectedResult = queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DRAFT);

        MetamacCriteriaResult<QueryVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<QueryVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testFindQueriesVersionsByConditionProcStatusProductionValidation() throws Exception {
        QueryVersion expectedResult = queryVersionMockFactory.retrieveMock(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PRODUCTION_VALIDATION);

        MetamacCriteriaResult<QueryVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<QueryVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testFindQueriesVersionsByConditionProcStatusDiffusionValidation() throws Exception {
        QueryVersion expectedResult = queryVersionMockFactory.retrieveMock(QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DIFFUSION_VALIDATION);

        MetamacCriteriaResult<QueryVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<QueryVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testFindQueriesVersionsByConditionProcStatusValidationRejected() throws Exception {
        QueryVersion expectedResult = queryVersionMockFactory.retrieveMock(QUERY_VERSION_14_VALIDATION_REJECTED_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.VALIDATION_REJECTED);

        MetamacCriteriaResult<QueryVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<QueryVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME})
    public void testFindQueriesVersionsByConditionProcStatusPublished() throws Exception {
        QueryVersion expectedResult = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PUBLISHED);
        MetamacCriteriaResult<QueryVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findQueriesVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<QueryVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Override
    @Test
    @MetamacMock(QUERY_VERSION_11_DRAFT_NAME)
    public void testSendQueryVersionToProductionValidation() throws Exception {
        String queryVersionUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME).getLifeCycleStatisticalResource().getUrn();
        QueryVersionDto queryVersionDto = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(), queryVersionUrn);

        QueryVersionDto updatedQueryVersion = statisticalResourcesServiceFacade.sendQueryVersionToProductionValidation(getServiceContextAdministrador(), queryVersionDto);
        assertNotNull(updatedQueryVersion);
        assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, updatedQueryVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedQueryVersion.getProductionValidationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedQueryVersion.getProductionValidationDate()));
    }

    @Override
    @Test
    @MetamacMock(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME)
    public void testSendQueryVersionToDiffusionValidation() throws Exception {
        String queryVersionUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME).getLifeCycleStatisticalResource().getUrn();
        QueryVersionDto queryVersionDto = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(), queryVersionUrn);

        QueryVersionDto updatedQueryVersion = statisticalResourcesServiceFacade.sendQueryVersionToDiffusionValidation(getServiceContextAdministrador(), queryVersionDto);
        assertNotNull(updatedQueryVersion);
        assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, updatedQueryVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedQueryVersion.getDiffusionValidationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedQueryVersion.getDiffusionValidationDate()));
    }

    @Override
    @Test
    @MetamacMock(QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME)
    public void testSendQueryVersionToValidationRejected() throws Exception {
        String queryVersionUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME).getLifeCycleStatisticalResource().getUrn();
        QueryVersionDto queryVersionDto = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(), queryVersionUrn);

        QueryVersionDto updatedQueryVersion = statisticalResourcesServiceFacade.sendQueryVersionToValidationRejected(getServiceContextAdministrador(), queryVersionDto);
        assertNotNull(updatedQueryVersion);
        assertEquals(ProcStatusEnum.VALIDATION_REJECTED, updatedQueryVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedQueryVersion.getRejectValidationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedQueryVersion.getRejectValidationDate()));

        assertNotNull(updatedQueryVersion.getProductionValidationUser());
        assertNotNull(updatedQueryVersion.getProductionValidationDate());
    }

    @Override
    @Test
    @MetamacMock(QUERY_VERSION_37_PREPARED_TO_PUBLISH_NAME)
    public void testPublishQueryVersion() throws Exception {
        String queryVersionUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_37_PREPARED_TO_PUBLISH_NAME).getLifeCycleStatisticalResource().getUrn();
        QueryVersionDto queryVersionDto = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(), queryVersionUrn);

        QueryVersionDto updatedQueryVersion = statisticalResourcesServiceFacade.publishQueryVersion(getServiceContextAdministrador(), queryVersionDto);
        assertNotNull(updatedQueryVersion);
        assertEquals(ProcStatusEnum.PUBLISHED, updatedQueryVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedQueryVersion.getPublicationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedQueryVersion.getPublicationDate()));
    }

    @Override
    public void testResendPublishedQueryVersionStreamMessage() throws Exception {

    }

    @Override
    @Test
    @MetamacMock(QUERY_VERSION_15_PUBLISHED_NAME)
    public void testVersioningQueryVersion() throws Exception {
        String queryVersionUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME).getLifeCycleStatisticalResource().getUrn();
        QueryVersionDto previousQueryVersionDto = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(), queryVersionUrn);

        QueryVersionDto newQueryVersion = statisticalResourcesServiceFacade.versioningQueryVersion(getServiceContextAdministrador(), previousQueryVersionDto, VersionTypeEnum.MAJOR);
        assertNotNull(newQueryVersion);
        assertEquals(ProcStatusEnum.DRAFT, newQueryVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), newQueryVersion.getCreationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(newQueryVersion.getCreationDate()));
        assertEqualsQuerySelection(previousQueryVersionDto.getSelection(), newQueryVersion.getSelection());
    }

    @Test
    @MetamacMock(QUERY_VERSION_15_PUBLISHED_NAME)
    public void testVersioningQueryVersionErrorAlreadyExistsAVersion() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.QUERY_VERSION__LAST_VERSION));

        String queryVersionUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME).getLifeCycleStatisticalResource().getUrn();
        QueryVersionDto originalQueryVersion = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(), queryVersionUrn);

        QueryVersionDto newQueryVersion = statisticalResourcesServiceFacade.versioningQueryVersion(getServiceContextAdministrador(), originalQueryVersion, VersionTypeEnum.MAJOR);
        assertNotNull(newQueryVersion);

        // We have to retrieve query version to avoid optimistic locking error
        QueryVersionDto originalQueryVersionV2 = statisticalResourcesServiceFacade.retrieveQueryVersionByUrn(getServiceContextAdministrador(), queryVersionUrn);
        statisticalResourcesServiceFacade.versioningQueryVersion(getServiceContextAdministrador(), originalQueryVersionV2, VersionTypeEnum.MINOR);
    }

    // ------------------------------------------------------------------------
    // DATASOURCES
    // ------------------------------------------------------------------------

    @Override
    @Test
    @Ignore
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, DATASET_VERSION_02_BASIC_NAME})
    // Datasources can not longer be created from facade
    public void testCreateDatasource() throws Exception {
        DatasourceDto persistedDatasource = statisticalResourcesServiceFacade.createDatasource(getServiceContextAdministrador(),
                datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn(), StatisticalResourcesDtoMocks.mockDatasourceDto());
        assertNotNull(persistedDatasource);
        assertNotNull(persistedDatasource.getUrn());
    }

    @Override
    @Test
    @MetamacMock({DATASOURCE_01_BASIC_NAME, DATASET_VERSION_02_BASIC_NAME})
    public void testUpdateDatasource() throws Exception {
        DatasourceDto updatedDatasource = statisticalResourcesServiceFacade.retrieveDatasourceByUrn(getServiceContextAdministrador(),
                datasourceMockFactory.retrieveMock(DATASOURCE_01_BASIC_NAME).getIdentifiableStatisticalResource().getUrn());
        String oldCode = updatedDatasource.getCode();
        updatedDatasource.setCode("newCode" + StatisticalResourcesDtoMocks.mockString(5));

        DatasourceDto actualDatasource = statisticalResourcesServiceFacade.updateDatasource(getServiceContextAdministrador(), updatedDatasource);
        assertNotNull(actualDatasource);
        assertEquals(oldCode, actualDatasource.getCode());
    }

    @Override
    @Test
    @MetamacMock({DATASOURCE_01_BASIC_NAME, DATASET_VERSION_02_BASIC_NAME})
    public void testRetrieveDatasourceByUrn() throws Exception {
        DatasourceDto actual = statisticalResourcesServiceFacade.retrieveDatasourceByUrn(getServiceContextAdministrador(),
                datasourceMockFactory.retrieveMock(DATASOURCE_01_BASIC_NAME).getIdentifiableStatisticalResource().getUrn());
        assertEqualsDatasource(datasourceMockFactory.retrieveMock(DATASOURCE_01_BASIC_NAME), actual);
    }

    @Override
    @Test
    @MetamacMock({DATASOURCE_01_BASIC_NAME, DATASET_VERSION_02_BASIC_NAME})
    public void testDeleteDatasource() throws Exception {
        mockDsdAndDataRepositorySimpleDimensions();

        String datasourceUrn = datasourceMockFactory.retrieveMock(DATASOURCE_01_BASIC_NAME).getIdentifiableStatisticalResource().getUrn();

        statisticalResourcesServiceFacade.deleteDatasource(getServiceContextAdministrador(), datasourceUrn, Boolean.TRUE);

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASOURCE_NOT_FOUND, datasourceUrn));

        statisticalResourcesServiceFacade.retrieveDatasourceByUrn(getServiceContextAdministrador(), datasourceUrn);
    }

    @Override
    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testRetrieveDatasourcesByDatasetVersion() throws Exception {
        // Version DATASET_VERSION_03_ASSOCIATED_WITH_DATASET_03
        {
            String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME).getSiemacMetadataStatisticalResource().getUrn();
            List<Datasource> expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME).getDatasources();

            List<DatasourceDto> actual = statisticalResourcesServiceFacade.retrieveDatasourcesByDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn);
            assertEqualsDatasourceDoAndDtoCollection(expected, actual);
        }

        // Version DATASET_VERSION_03_ASSOCIATED_WITH_DATASET_03
        {
            String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn();
            List<Datasource> expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME).getDatasources();

            List<DatasourceDto> actual = statisticalResourcesServiceFacade.retrieveDatasourcesByDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn);
            assertEqualsDatasourceDoAndDtoCollection(expected, actual);
        }
    }

    @Test(expected = AssertionError.class)
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testRetrieveDatasourcesByDatasetVersionErrorDifferentResponse() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME).getSiemacMetadataStatisticalResource().getUrn();
        List<Datasource> expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME).getDatasources();
        expected.remove(0);

        List<DatasourceDto> actual = statisticalResourcesServiceFacade.retrieveDatasourcesByDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn);
        assertEqualsDatasourceDoAndDtoCollection(expected, actual);
    }

    @Test
    @Override
    @MetamacMock({DATASET_01_BASIC_NAME, DATASET_VERSION_01_BASIC_NAME, DIMENSION_REPRESENTATION_MAPPING_01_DATASET_01_NAME})
    public void testRetrieveDimensionRepresentationMappings() throws Exception {

        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("DIMENSION01", "urn:codelist");
        mapping.put("DIMENSION02", "urn:conceptScheme");
        mapping.put("DIMENSION03", "urn:codelist");

        DimensionRepresentationMapping expected = dimensionRepresentationMappingMockFactory.retrieveMock(DIMENSION_REPRESENTATION_MAPPING_01_DATASET_01_NAME);

        DimensionRepresentationMappingDto actual = statisticalResourcesServiceFacade.retrieveDimensionRepresentationMappings(getServiceContextAdministrador(),
                expected.getDataset().getVersions().get(0).getSiemacMetadataStatisticalResource().getUrn(), "filename");

        assertEqualsDimensionRepresentationMapping(expected, actual);
    }

    // ------------------------------------------------------------------------
    // DATASETS
    // ------------------------------------------------------------------------

    @Override
    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, DATASET_04_FULL_FILLED_WITH_1_DATASET_VERSIONS_NAME})
    public void testFindDatasetsByCondition() throws Exception {
        DatasetVersion latestDatasetVersionDataset03 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME);
        DatasetVersion latestDatasetVersionDataset04 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_05_FOR_DATASET_04_NAME);

        MetamacCriteria metamacCriteria = new MetamacCriteria();

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(2, results.size());

        assertEqualsDataset(latestDatasetVersionDataset03, results.get(0));
        assertEqualsDataset(latestDatasetVersionDataset04, results.get(1));
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, DATASET_04_FULL_FILLED_WITH_1_DATASET_VERSIONS_NAME})
    public void testFindDatasetsByConditionTitle() throws Exception {
        DatasetVersion latestDatasetVersionDataset03 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        String title = latestDatasetVersionDataset03.getSiemacMetadataStatisticalResource().getTitle().getLocalisedLabel("es");
        setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.TITLE, OperationType.LIKE, title);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsDataset(latestDatasetVersionDataset03, results.get(0));
    }

    @Test
    @MetamacMock({DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME, DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME,
            DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME, DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME, DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME,
            DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06_NAME})
    public void testFindDatasetsByConditionProcStatusDraft() throws Exception {
        DatasetVersion expectedResult = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DRAFT);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsDataset(expectedResult, results.get(0));
    }

    @Test
    @MetamacMock({DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME, DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME,
            DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME, DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME, DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME,
            DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06_NAME})
    public void testFindDatasetsByConditionProcStatusProductionValidation() throws Exception {
        DatasetVersion expectedResult = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PRODUCTION_VALIDATION);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsDataset(expectedResult, results.get(0));
    }

    @Test
    @MetamacMock({DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME, DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME,
            DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME, DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME, DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME,
            DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06_NAME})
    public void testFindDatasetsByConditionProcStatusDiffusionValidation() throws Exception {
        DatasetVersion expectedResult = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DIFFUSION_VALIDATION);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsDataset(expectedResult, results.get(0));
    }

    @Test
    @MetamacMock({DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME, DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME,
            DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME, DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME, DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME,
            DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06_NAME})
    public void testFindDatasetsByConditionProcStatusValidationRejected() throws Exception {
        DatasetVersion expectedResult = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.VALIDATION_REJECTED);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsDataset(expectedResult, results.get(0));
    }

    @Test
    @MetamacMock({DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME, DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME,
            DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME, DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME, DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME})
    public void testFindDatasetsByConditionProcStatusPublished() throws Exception {
        DatasetVersion expectedResult = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PUBLISHED);
        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsDataset(expectedResult, results.get(0));
    }

    // ------------------------------------------------------------------------
    // DATASETS VERSIONS
    // ------------------------------------------------------------------------

    @Override
    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME})
    public void testRetrieveDatasetVersionByUrn() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        DatasetVersionDto dataset = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEqualsDatasetVersion(datasetVersion, dataset);
    }

    @Override
    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testRetrieveLatestDatasetVersion() throws Exception {
        String datasetUrn = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        DatasetVersion expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME);
        DatasetVersionDto actual = statisticalResourcesServiceFacade.retrieveLatestDatasetVersion(getServiceContextAdministrador(), datasetUrn);
        assertEqualsDatasetVersion(expected, actual);
    }

    @Override
    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testRetrieveLatestPublishedDatasetVersion() throws Exception {
        String datasetUrn = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        DatasetVersion expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME);
        DatasetVersionDto actual = statisticalResourcesServiceFacade.retrieveLatestPublishedDatasetVersion(getServiceContextAdministrador(), datasetUrn);
        assertEqualsDatasetVersion(expected, actual);
    }

    @Override
    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testRetrieveDatasetVersions() throws Exception {
        DatasetVersion datasetVersionFirst = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME).getVersions().get(0);
        DatasetVersion datasetVersionLast = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME).getVersions().get(1);
        // Version in urn does not care
        {
            List<DatasetVersionBaseDto> datasets = statisticalResourcesServiceFacade.retrieveDatasetVersions(getServiceContextAdministrador(),
                    datasetVersionLast.getSiemacMetadataStatisticalResource().getUrn());
            assertNotNull(datasets);
            assertEquals(2, datasets.size());
            assertEqualsDatasetVersionBase(datasetVersionFirst, find(datasets, datasetVersionFirst.getSiemacMetadataStatisticalResource().getUrn()));
            assertEqualsDatasetVersionBase(datasetVersionLast, find(datasets, datasetVersionLast.getSiemacMetadataStatisticalResource().getUrn()));
        }
        {
            List<DatasetVersionBaseDto> datasets = statisticalResourcesServiceFacade.retrieveDatasetVersions(getServiceContextAdministrador(),
                    datasetVersionFirst.getSiemacMetadataStatisticalResource().getUrn());
            assertNotNull(datasets);
            assertEquals(2, datasets.size());
            assertEqualsDatasetVersionBase(datasetVersionFirst, find(datasets, datasetVersionFirst.getSiemacMetadataStatisticalResource().getUrn()));
            assertEqualsDatasetVersionBase(datasetVersionLast, find(datasets, datasetVersionLast.getSiemacMetadataStatisticalResource().getUrn()));
        }
    }

    @Override
    @Test
    @MetamacMock(STATISTIC_OFFICIALITY_01_BASIC_NAME)
    public void testCreateDataset() throws Exception {
        StatisticOfficiality officiality = statisticOfficialityMockFactory.retrieveMock(STATISTIC_OFFICIALITY_01_BASIC_NAME);
        DatasetVersionDto datasetVersionDto = StatisticalResourcesDtoMocks.mockDatasetVersionDto(officiality);
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto();

        mockDsdAndCreateDatasetRepository(datasetVersionDto, statisticalOperation);

        DatasetVersionDto newDatasetVersionDto = statisticalResourcesServiceFacade.createDataset(getServiceContextAdministrador(), datasetVersionDto, statisticalOperation);
        assertNotNull(newDatasetVersionDto);
        assertNotNull(newDatasetVersionDto.getUrn());
        assertNotNull(newDatasetVersionDto.getViewCode());
        assertEquals(DatasetVersionUtils.generateViewCode(newDatasetVersionDto.getCode()), newDatasetVersionDto.getViewCode());
    }

    @MetamacMock(STATISTIC_OFFICIALITY_01_BASIC_NAME)
    @Test
    public void testCreateDatasetHasExpectedUrn() throws Exception {
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto(StatisticalResourcesMockFactory.OPERATION_01_CODE);
        ExternalItemDto maintainer = StatisticalResourcesDtoMocks.mockAgencyExternalItemDto("SIEMAC");

        StatisticOfficiality officiality = statisticOfficialityMockFactory.retrieveMock(STATISTIC_OFFICIALITY_01_BASIC_NAME);
        DatasetVersionDto datasetVersionDto = StatisticalResourcesDtoMocks.mockDatasetVersionDto(officiality);
        datasetVersionDto.setMaintainer(maintainer);

        mockDsdAndCreateDatasetRepository(datasetVersionDto, statisticalOperation);

        String persistedDatasetVersionUrn = statisticalResourcesServiceFacade.createDataset(getServiceContextAdministrador(), datasetVersionDto, statisticalOperation).getUrn();
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=SIEMAC:C00025A_000001(1.0)", persistedDatasetVersionUrn);
    }

    @Test
    public void testCreateDatasetErrorDuplicatedUrn() throws Exception {
        // Codes are setting automatically, so URN can not be duplicated
    }

    @Test
    @MetamacMock({STATISTIC_OFFICIALITY_01_BASIC_NAME, DATASET_VERSION_01_BASIC_NAME, DATASET_VERSION_02_BASIC_NAME})
    public void testUpdateDatasetVersionErrorDuplicatedUrn() throws Exception {
        // Codes are setting automatically, so URN can not be duplicated
    }

    @Override
    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME})
    public void testUpdateDatasetVersion() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);

        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(),
                datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        datasetVersionDto.setTitle(StatisticalResourcesDtoMocks.mockInternationalStringDto("es", "Mi titulo"));

        DatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);
        assertNotNull(updatedDataset);
        assertEqualsInternationalStringDto(datasetVersionDto.getTitle(), updatedDataset.getTitle());
    }

    @Test
    @MetamacMock({DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME})
    public void testUpdateDatasetVersionWithLanguages() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();

        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);
        datasetVersionDto.setTitle(StatisticalResourcesDtoMocks.mockInternationalStringDto("es", "Mi titulo"));

        DatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);
        assertNotNull(updatedDataset);
        assertEqualsInternationalStringDto(datasetVersionDto.getTitle(), updatedDataset.getTitle());
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME})
    public void testUpdateDatasetVersionChangeCodeNotAllowed() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);

        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(),
                datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        datasetVersionDto.setCode("CHANGED");

        DatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);
        assertNotNull(updatedDataset);
        assertEquals(datasetVersion.getSiemacMetadataStatisticalResource().getCode(), updatedDataset.getCode());
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME})
    public void testUpdateDatasetVersionChangeStatisticalOperationNotAllowed() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        String originalStatisticalOperationCode = datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(),
                datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto();
        datasetVersionDto.setStatisticalOperation(statisticalOperation);

        DatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);
        assertNotNull(updatedDataset);
        assertEquals(originalStatisticalOperationCode, updatedDataset.getStatisticalOperation().getCode());
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME})
    public void testUpdateDatasetVersionChangeMaintainerNotAllowed() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        String originalMaintainerCode = datasetVersion.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested();

        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(),
                datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        ExternalItemDto maintainer = StatisticalResourcesDtoMocks.mockAgencyExternalItemDto();
        datasetVersionDto.setMaintainer(maintainer);

        DatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);
        assertNotNull(updatedDataset);
        assertEquals(originalMaintainerCode, updatedDataset.getMaintainer().getCodeNested());
    }

    @Test
    @MetamacMock(DATASET_VERSION_45_NEXT_VERSION_SCHEDULED_UPDATE_JANUARY_NAME)
    public void testUpdateDatasetVersionChangeNextVersionToNonScheduledSetNextVersionDateNull() throws Exception {
        // Retrieve dataset version
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_45_NEXT_VERSION_SCHEDULED_UPDATE_JANUARY_NAME).getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        // Check original premises are true
        assertNotNull(datasetVersionDto.getNextVersionDate());
        assertEquals(NextVersionTypeEnum.SCHEDULED_UPDATE, datasetVersionDto.getNextVersion());

        // Update dataset version
        datasetVersionDto.setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);
        DatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);

        // Check the result is the one that we expect
        assertEquals(NextVersionTypeEnum.NON_SCHEDULED_UPDATE, updatedDataset.getNextVersion());
        assertNull(updatedDataset.getNextVersionDate());
    }

    @Test
    @MetamacMock(DATASET_VERSION_45_NEXT_VERSION_SCHEDULED_UPDATE_JANUARY_NAME)
    public void testUpdateDatasetVersionChangeNextVersionToNoUpdatesSetNextVersionDateNull() throws Exception {
        // Retrieve dataset version
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_45_NEXT_VERSION_SCHEDULED_UPDATE_JANUARY_NAME).getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        // Check original premises are true
        assertNotNull(datasetVersionDto.getNextVersionDate());
        assertEquals(NextVersionTypeEnum.SCHEDULED_UPDATE, datasetVersionDto.getNextVersion());

        // Update dataset version
        datasetVersionDto.setNextVersion(NextVersionTypeEnum.NO_UPDATES);
        DatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);

        // Check the result is the one that we expect
        assertEquals(NextVersionTypeEnum.NO_UPDATES, updatedDataset.getNextVersion());
        assertNull(updatedDataset.getNextVersionDate());
    }

    @Test
    @MetamacMock(DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME)
    public void testUpdateDatasetVersionChangeNextVersionToNonScheduledSetDateNextUpdateNull() throws Exception {
        // Retrieve dataset version
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME).getSiemacMetadataStatisticalResource()
                .getUrn();
        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        // Check original premises are true
        assertNotNull(datasetVersionDto.getDateNextUpdate());
        assertEquals(NextVersionTypeEnum.SCHEDULED_UPDATE, datasetVersionDto.getNextVersion());

        // Update dataset version
        datasetVersionDto.setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);
        DatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);

        // Check the result is the one that we expect
        assertEquals(NextVersionTypeEnum.NON_SCHEDULED_UPDATE, updatedDataset.getNextVersion());
        assertNull(updatedDataset.getDateNextUpdate());
    }

    @Test
    @MetamacMock(DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME)
    public void testUpdateDatasetVersionChangeNextVersionToNoUpdatesSetDateNextUpdateNull() throws Exception {
        // Retrieve dataset version
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME).getSiemacMetadataStatisticalResource()
                .getUrn();
        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        // Check original premises are true
        assertNotNull(datasetVersionDto.getDateNextUpdate());
        assertEquals(NextVersionTypeEnum.SCHEDULED_UPDATE, datasetVersionDto.getNextVersion());

        // Update dataset version
        datasetVersionDto.setNextVersion(NextVersionTypeEnum.NO_UPDATES);
        DatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);

        // Check the result is the one that we expect
        assertEquals(NextVersionTypeEnum.NO_UPDATES, updatedDataset.getNextVersion());
        assertNull(updatedDataset.getDateNextUpdate());
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME})
    public void testUpdateDatasetVersionIgnoreDateNextVersion() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        String originalDateNextVersion = datasetVersion.getSiemacMetadataStatisticalResource().getNextVersionDate();

        {
            DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(),
                    datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
            datasetVersionDto.setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(1).toDate()));
            datasetVersionDto.setNextVersion(NextVersionTypeEnum.NO_UPDATES);

            DatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);
            assertNotNull(updatedDataset);
            assertEquals(originalDateNextVersion, updatedDataset.getNextVersionDate());
        }
        {
            DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(),
                    datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
            datasetVersionDto.setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(1).toDate()));
            datasetVersionDto.setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);

            DatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);
            assertNotNull(updatedDataset);
            assertEquals(originalDateNextVersion, updatedDataset.getNextVersionDate());
        }
        {
            DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(),
                    datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
            datasetVersionDto.setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(1).toDate()));
            datasetVersionDto.setNextVersion(NextVersionTypeEnum.SCHEDULED_UPDATE);

            DatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);
            assertNotNull(updatedDataset);
            assertEquals(datasetVersionDto.getNextVersionDate(), updatedDataset.getNextVersionDate());
        }
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME})
    public void testUpdateDatasetVersionNotAllowedMetadata() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);

        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(),
                datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        String oldCreator = datasetVersionDto.getCreatedBy();

        datasetVersionDto.setCreatedBy("My user");

        DatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);
        assertNotNull(updatedDataset);
        assertEquals(oldCreator, updatedDataset.getCreatedBy());
    }

    @Override
    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME})
    public void testDeleteDatasetVersion() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        assertNotNull(statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn()));
        statisticalResourcesServiceFacade.deleteDatasetVersion(getServiceContextAdministrador(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, datasetVersion.getSiemacMetadataStatisticalResource().getUrn()));
        statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
    }

    @Override
    @Test
    @MetamacMock({DATASET_VERSION_09_OPER_0001_CODE_000003_NAME, DATASET_VERSION_10_OPER_0002_CODE_000001_NAME, DATASET_VERSION_11_OPER_0002_CODE_000002_NAME})
    public void testFindDatasetsVersionsByCondition() throws Exception {
        DatasetVersion dsOper1Code3 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_09_OPER_0001_CODE_000003_NAME);
        DatasetVersion dsOper2Code1 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_10_OPER_0002_CODE_000001_NAME);
        DatasetVersion dsOper2Code2 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_11_OPER_0002_CODE_000002_NAME);

        // Find All
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(3, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<DatasetVersionBaseDto> results = pagedResults.getResults();
            assertEquals(3, results.size());

            assertEquals(dsOper1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
            assertEquals(dsOper2Code1.getSiemacMetadataStatisticalResource().getUrn(), results.get(1).getUrn());
            assertEquals(dsOper2Code2.getSiemacMetadataStatisticalResource().getUrn(), results.get(2).getUrn());
        }
    }

    @Test
    @MetamacMock({DATASET_VERSION_09_OPER_0001_CODE_000003_NAME, DATASET_VERSION_10_OPER_0002_CODE_000001_NAME})
    public void testFindDatasetsVersionsByConditionCheckLastUpdatedIsDefaultOrder() throws Exception {
        String dsOper2Code1Urn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_10_OPER_0002_CODE_000001_NAME).getLifeCycleStatisticalResource().getUrn();

        MetamacCriteria metamacCriteria = new MetamacCriteria();

        MetamacCriteriaResult<DatasetVersionBaseDto> datasetsPagedResult = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);

        // Update queryVersion 02
        DatasetVersionDto dsOper2Code1 = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), dsOper2Code1Urn);
        statisticalResourcesServiceFacade.updateDatasetVersion(getServiceContextAdministrador(), dsOper2Code1);

        // Search
        datasetsPagedResult = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);

        // Validate
        assertEquals(2, datasetsPagedResult.getPaginatorResult().getTotalResults().intValue());
        assertEquals(2, datasetsPagedResult.getResults().size());
        assertTrue(datasetsPagedResult.getResults().get(0) instanceof DatasetVersionBaseDto);

        Date lastUpdated01 = datasetsPagedResult.getResults().get(0).getLastUpdated();
        Date lastUpdated02 = datasetsPagedResult.getResults().get(1).getLastUpdated();
        assertTrue(lastUpdated01.before(lastUpdated02));

        assertEquals(dsOper2Code1Urn, datasetsPagedResult.getResults().get(1).getUrn());
    }

    @Test
    @MetamacMock({DATASET_VERSION_09_OPER_0001_CODE_000003_NAME, DATASET_VERSION_10_OPER_0002_CODE_000001_NAME, DATASET_VERSION_11_OPER_0002_CODE_000002_NAME})
    public void testFindDatasetsVersionsByConditionByCode() throws Exception {
        DatasetVersion dsOper1Code3 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_09_OPER_0001_CODE_000003_NAME);

        // Find CODE
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.CODE, OperationType.EQ, dsOper1Code3.getSiemacMetadataStatisticalResource().getCode());

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<DatasetVersionBaseDto> results = pagedResults.getResults();
            assertEquals(1, results.size());

            assertEquals(dsOper1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
        }
    }

    @Test
    @MetamacMock({DATASET_VERSION_09_OPER_0001_CODE_000003_NAME, DATASET_VERSION_10_OPER_0002_CODE_000001_NAME, DATASET_VERSION_11_OPER_0002_CODE_000002_NAME})
    public void testFindDatasetsVersionsByConditionByUrn() throws Exception {
        DatasetVersion dsOper1Code3 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_09_OPER_0001_CODE_000003_NAME);

        // Find URN
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.URN, OperationType.EQ, dsOper1Code3.getSiemacMetadataStatisticalResource().getUrn());

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(dsOper1Code3, dsOper1Code3), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }
    }

    @Test
    @MetamacMock({DATASET_VERSION_09_OPER_0001_CODE_000003_NAME, DATASET_VERSION_10_OPER_0002_CODE_000001_NAME, DATASET_VERSION_11_OPER_0002_CODE_000002_NAME,
            DATASET_VERSION_13_OPER_0002_CODE_000003_PROD_VAL_NAME})
    public void testFindDatasetsVersionsByConditionByProcStatus() throws Exception {
        DatasetVersion dsOper1Code3 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_09_OPER_0001_CODE_000003_NAME);
        DatasetVersion dsOper2Code1 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_10_OPER_0002_CODE_000001_NAME);
        DatasetVersion dsOper2Code2 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_11_OPER_0002_CODE_000002_NAME);
        DatasetVersion dsOper2Code3ProdVal = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_13_OPER_0002_CODE_000003_PROD_VAL_NAME);

        // Find PROC STATUS
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DRAFT);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(3, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(dsOper1Code3, dsOper2Code1, dsOper2Code2), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PRODUCTION_VALIDATION);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(dsOper2Code3ProdVal), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME})
    public void testFindDatasetsVersionsByConditionByData() throws Exception {
        DatasetVersion datasetWithNoData = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        DatasetVersion datasetWithData = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME);

        // Find no data
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaBooleanPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.DATA, OperationType.EQ, Boolean.FALSE);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(datasetWithNoData), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }
        // Find data
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaBooleanPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.DATA, OperationType.EQ, Boolean.TRUE);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(datasetWithData), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }
    }

    @Test
    @MetamacMock({DATASET_VERSION_39_VERSION_RATIONALE_TYPE_MAJOR_NEW_RESOURCE_NAME, DATASET_VERSION_40_VERSION_RATIONALE_TYPE_MAJOR_ESTIMATORS_NAME,
            DATASET_VERSION_41_VERSION_RATIONALE_TYPE_MINOR_ERRATAS_NAME, DATASET_VERSION_42_VERSION_RATIONALE_TYPE_MINOR_ERRATAS_AND_MAJOR_ESTIMATORS_NAME})
    public void testFindDatasetsVersionsByConditionByVersionRationaleType() throws Exception {
        DatasetVersion datasetVersionMajorNewResource = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_39_VERSION_RATIONALE_TYPE_MAJOR_NEW_RESOURCE_NAME);
        DatasetVersion datasetVersionMajorEstimators = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_40_VERSION_RATIONALE_TYPE_MAJOR_ESTIMATORS_NAME);
        DatasetVersion datasetVersionMinorErratas = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_41_VERSION_RATIONALE_TYPE_MINOR_ERRATAS_NAME);
        DatasetVersion datasetVersionMinorErratasAndMajorEstimators = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_42_VERSION_RATIONALE_TYPE_MINOR_ERRATAS_AND_MAJOR_ESTIMATORS_NAME);

        // Find VERSION RATIONALE TYPE
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.VERSION_RATIONALE_TYPE, OperationType.EQ, VersionRationaleTypeEnum.MAJOR_NEW_RESOURCE);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(datasetVersionMajorNewResource), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.VERSION_RATIONALE_TYPE, OperationType.EQ, VersionRationaleTypeEnum.MINOR_ERRATA);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(datasetVersionMinorErratas, datasetVersionMinorErratasAndMajorEstimators), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }

        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.VERSION_RATIONALE_TYPE, OperationType.EQ, VersionRationaleTypeEnum.MAJOR_ESTIMATORS);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(datasetVersionMajorEstimators, datasetVersionMinorErratasAndMajorEstimators), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }
    }

    @Test
    @MetamacMock({DATASET_VERSION_43_NEXT_VERSION_NO_UPDATES_NAME, DATASET_VERSION_44_NEXT_VERSION_NON_SCHEDULED_UPDATE_NAME, DATASET_VERSION_45_NEXT_VERSION_SCHEDULED_UPDATE_JANUARY_NAME,
            DATASET_VERSION_46_NEXT_VERSION_SCHEDULED_UPDATE_JULY_NAME})
    public void testFindDatasetsVersionsByConditionByNextVersion() throws Exception {
        DatasetVersion datasetVersionNoUpdates = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_43_NEXT_VERSION_NO_UPDATES_NAME);
        DatasetVersion datasetVersionNonScheduledUpdate = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_44_NEXT_VERSION_NON_SCHEDULED_UPDATE_NAME);
        DatasetVersion datasetVersionScheduledUpdateJanuary = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_45_NEXT_VERSION_SCHEDULED_UPDATE_JANUARY_NAME);
        DatasetVersion datasetVersionScheduledUpdateJuly = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_46_NEXT_VERSION_SCHEDULED_UPDATE_JULY_NAME);

        // Find NEXT_VERSION
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.NEXT_VERSION, OperationType.EQ, NextVersionTypeEnum.NO_UPDATES);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(datasetVersionNoUpdates), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.NEXT_VERSION, OperationType.EQ, NextVersionTypeEnum.NON_SCHEDULED_UPDATE);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(datasetVersionNonScheduledUpdate), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }

        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.NEXT_VERSION, OperationType.EQ, NextVersionTypeEnum.SCHEDULED_UPDATE);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(datasetVersionScheduledUpdateJanuary, datasetVersionScheduledUpdateJuly), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }
    }

    @Test
    @MetamacMock({DATASET_VERSION_43_NEXT_VERSION_NO_UPDATES_NAME, DATASET_VERSION_44_NEXT_VERSION_NON_SCHEDULED_UPDATE_NAME, DATASET_VERSION_45_NEXT_VERSION_SCHEDULED_UPDATE_JANUARY_NAME,
            DATASET_VERSION_46_NEXT_VERSION_SCHEDULED_UPDATE_JULY_NAME})
    public void testFindDatasetsVersionsByConditionByNextVersionDate() throws Exception {
        DatasetVersion datasetVersionNoUpdates = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_43_NEXT_VERSION_NO_UPDATES_NAME);
        DatasetVersion datasetVersionNonScheduledUpdate = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_44_NEXT_VERSION_NON_SCHEDULED_UPDATE_NAME);
        DatasetVersion datasetVersionScheduledUpdateJanuary = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_45_NEXT_VERSION_SCHEDULED_UPDATE_JANUARY_NAME);
        DatasetVersion datasetVersionScheduledUpdateJuly = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_46_NEXT_VERSION_SCHEDULED_UPDATE_JULY_NAME);

        // Find NEXT_VERSION_DATE null
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.NEXT_VERSION_DATE, OperationType.IS_NULL, null);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(datasetVersionNoUpdates, datasetVersionNonScheduledUpdate), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }

        // Find NEXT_VERSION_DATE january
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.NEXT_VERSION_DATE, OperationType.EQ,
                    CoreCommonUtil.jodaDateTime2IsoDate(new DateTime(2013, 1, 15, 0, 0, 0, 0).toDate()));

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(datasetVersionScheduledUpdateJanuary), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }

        // Find NEXT_VERSION_DATE january and july
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.NEXT_VERSION_DATE, OperationType.GT,
                    CoreCommonUtil.jodaDateTime2IsoDate(new DateTime(2012, 12, 1, 0, 0, 0, 0).toDate()));

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
            assertEqualsCollectionByField(Arrays.asList(datasetVersionScheduledUpdateJanuary, datasetVersionScheduledUpdateJuly), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
        }
    }

    @Test
    @MetamacMock({DATASET_VERSION_43_NEXT_VERSION_NO_UPDATES_NAME, DATASET_VERSION_44_NEXT_VERSION_NON_SCHEDULED_UPDATE_NAME, DATASET_VERSION_45_NEXT_VERSION_SCHEDULED_UPDATE_JANUARY_NAME,
            DATASET_VERSION_46_NEXT_VERSION_SCHEDULED_UPDATE_JULY_NAME})
    public void testFindDatasetsVersionsByConditionByTitleAlternative() throws Exception {
        DatasetVersion datasetVersionTitleAlternative = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_43_NEXT_VERSION_NO_UPDATES_NAME);

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
        setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.TITLE_ALTERNATIVE, OperationType.EQ,
                datasetVersionTitleAlternative.getSiemacMetadataStatisticalResource().getTitleAlternative().getLocalisedLabel("es"));

        MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        assertEqualsCollectionByField(Arrays.asList(datasetVersionTitleAlternative), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
    }

    @Test
    @MetamacMock({DATASET_VERSION_09_OPER_0001_CODE_000003_NAME, DATASET_VERSION_10_OPER_0002_CODE_000001_NAME, DATASET_VERSION_11_OPER_0002_CODE_000002_NAME,
            DATASET_VERSION_12_OPER_0002_MAX_CODE_NAME})
    public void testFindDatasetsVersionsByConditionByStatisticalOperationUrn() throws Exception {
        DatasetVersion datasetOper2Code1 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_10_OPER_0002_CODE_000001_NAME);
        DatasetVersion datasetOper2Code2 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_11_OPER_0002_CODE_000002_NAME);
        DatasetVersion datasetOper2CodeMax = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_12_OPER_0002_MAX_CODE_NAME);
        DatasetVersion datasetOper1Code3 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_09_OPER_0001_CODE_000003_NAME);

        // Find STATISTICAL_OPERATION
        {
            String statisticalOperationUrn = StatisticalResourcesDoMocks.mockStatisticalOperationUrn(DatasetVersionMockFactory.OPERATION_02_CODE);

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.STATISTICAL_OPERATION_URN, OperationType.EQ, statisticalOperationUrn);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(3, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<DatasetVersionBaseDto> results = pagedResults.getResults();
            assertEquals(3, results.size());

            assertEquals(datasetOper2Code1.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
            assertEquals(datasetOper2Code2.getSiemacMetadataStatisticalResource().getUrn(), results.get(1).getUrn());
            assertEquals(datasetOper2CodeMax.getSiemacMetadataStatisticalResource().getUrn(), results.get(2).getUrn());
        }
        {
            String statisticalOperationUrn = StatisticalResourcesDoMocks.mockStatisticalOperationUrn(DatasetVersionMockFactory.OPERATION_01_CODE);

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.STATISTICAL_OPERATION_URN, OperationType.EQ, statisticalOperationUrn);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<DatasetVersionBaseDto> results = pagedResults.getResults();
            assertEquals(1, results.size());

            assertEquals(datasetOper1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
        }

        {
            String statisticalOperationUrn = URN_NOT_EXISTS;

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.STATISTICAL_OPERATION_URN, OperationType.EQ, statisticalOperationUrn);

            MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(0, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<DatasetVersionBaseDto> results = pagedResults.getResults();
            assertEquals(0, results.size());
        }
    }

    @Test
    @MetamacMock({DATASET_VERSION_09_OPER_0001_CODE_000003_NAME, DATASET_VERSION_10_OPER_0002_CODE_000001_NAME, DATASET_VERSION_11_OPER_0002_CODE_000002_NAME,
            DATASET_VERSION_12_OPER_0002_MAX_CODE_NAME})
    public void testFindDatasetsVersionsByConditionOrderByStatisticalOperationUrn() throws Exception {
        DatasetVersion datasetOper1Code3 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_09_OPER_0001_CODE_000003_NAME);

        // FIND CODE
        String code = "000003";

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.STATISTICAL_OPERATION_URN, OrderTypeEnum.ASC);
        setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
        setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.CODE, OperationType.ILIKE, code);

        MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<DatasetVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(datasetOper1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({DATASET_VERSION_48_WITH_TEMPORAL_COVERAGE_FILLED_NAME})
    public void testFindDatasetsVersionsByConditionByGeographicGranularity() throws Exception {
        DatasetVersion datasetVersionGeographicCoverage = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_48_WITH_TEMPORAL_COVERAGE_FILLED_NAME);

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
        setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.DATASET_GEOGRAPHIC_GRANULARITY_URN, OperationType.EQ,
                datasetVersionGeographicCoverage.getGeographicGranularities().iterator().next().getUrn());

        MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        assertEqualsCollectionByField(Arrays.asList(datasetVersionGeographicCoverage), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
    }

    @Test
    @MetamacMock({DATASET_VERSION_48_WITH_TEMPORAL_COVERAGE_FILLED_NAME})
    public void testFindDatasetsVersionsByConditionByTemporalGranularity() throws Exception {
        DatasetVersion datasetVersionGeographicCoverage = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_48_WITH_TEMPORAL_COVERAGE_FILLED_NAME);

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
        setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.DATASET_TEMPORAL_GRANULARITY_URN, OperationType.EQ,
                datasetVersionGeographicCoverage.getTemporalGranularities().iterator().next().getUrn());

        MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        assertEqualsCollectionByField(Arrays.asList(datasetVersionGeographicCoverage), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
    }

    @Test
    @MetamacMock({DATASET_VERSION_48_WITH_TEMPORAL_COVERAGE_FILLED_NAME})
    public void testFindDatasetsVersionsByConditionByTemporalGranularityAndGeographicGranaularity() throws Exception {
        DatasetVersion datasetVersionGeographicCoverage = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_48_WITH_TEMPORAL_COVERAGE_FILLED_NAME);

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setConjunctionCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.DATASET_TEMPORAL_GRANULARITY_URN,
                StatisticalResourcesCriteriaPropertyEnum.DATASET_GEOGRAPHIC_GRANULARITY_URN, OperationType.EQ, datasetVersionGeographicCoverage.getTemporalGranularities().iterator().next().getUrn(),
                datasetVersionGeographicCoverage.getGeographicGranularities().iterator().next().getUrn());

        MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        assertEqualsCollectionByField(Arrays.asList(datasetVersionGeographicCoverage), pagedResults.getResults(), SIEMAC_METADATA_URN_FIELD, URN_FIELD);
    }

    @Test
    @MetamacMock({DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME, DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME,
            DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME, DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME, DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME,
            DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06_NAME})
    public void testFindDatasetsVersionsByConditionProcStatusDraft() throws Exception {
        DatasetVersion expectedResult = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DRAFT);

        MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<DatasetVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME, DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME,
            DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME, DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME, DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME,
            DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06_NAME})
    public void testFindDatasetsVersionsByConditionProcStatusProductionValidation() throws Exception {
        DatasetVersion expectedResult = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PRODUCTION_VALIDATION);

        MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<DatasetVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME, DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME,
            DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME, DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME, DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME,
            DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06_NAME})
    public void testFindDatasetsVersionsByConditionProcStatusDiffusionValidation() throws Exception {
        DatasetVersion expectedResult = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DIFFUSION_VALIDATION);

        MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<DatasetVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME, DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME,
            DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME, DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME, DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME,
            DATASET_VERSION_26_V2_PUBLISHED_NO_VISIBLE_FOR_DATASET_06_NAME})
    public void testFindDatasetsVersionsByConditionProcStatusValidationRejected() throws Exception {
        DatasetVersion expectedResult = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.VALIDATION_REJECTED);

        MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<DatasetVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME, DATASET_VERSION_19_PRODUCTION_VALIDATION_NOT_READY_NAME,
            DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME, DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME, DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME})
    public void testFindDatasetsVersionsByConditionProcStatusPublished() throws Exception {
        DatasetVersion expectedResult = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PUBLISHED);
        MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<DatasetVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({DATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME, DATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME})
    public void testFindDatasetsVersionsByConditionLastVersionFalse() throws Exception {
        int expectedResult = datasetMockFactory.retrieveMock(DATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME).getVersions().size();
        expectedResult = expectedResult + datasetMockFactory.retrieveMock(DATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME).getVersions().size();

        // Without Restrictions: we want last versions and not last versions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);

        assertEquals(expectedResult, pagedResults.getPaginatorResult().getTotalResults().intValue());
    }

    @Test
    @MetamacMock({DATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME, DATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME})
    public void testFindDatasetsVersionsByConditionLastVersionTrue() throws Exception {
        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaBooleanPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.LAST_VERSION, OperationType.EQ, Boolean.TRUE);
        MetamacCriteriaResult<DatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(getServiceContextAdministrador(), metamacCriteria);

        assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME)
    public void testRetrieveDatasetVersionMainCoverages() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersionMainCoveragesDto mainCoveragesDto = statisticalResourcesServiceFacade.retrieveDatasetVersionMainCoverages(getServiceContextAdministrador(), urn);
        DatasetsAsserts.assertEqualsDatasetVersionMainCoverages(mainCoveragesDto, datasetVersion.getGeographicCoverage(), datasetVersion.getTemporalCoverage(), datasetVersion.getMeasureCoverage());
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_70_PREPARED_TO_PUBLISH_EXTERNAL_ITEM_FULL_NAME)
    public void testPublishDatasetVersion() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_70_PREPARED_TO_PUBLISH_EXTERNAL_ITEM_FULL_NAME).getLifeCycleStatisticalResource().getUrn();
        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        DatasetVersionDto updatedDatasetVersion = statisticalResourcesServiceFacade.publishDatasetVersion(getServiceContextAdministrador(), datasetVersionDto);
        assertNotNull(updatedDatasetVersion);
        assertEquals(ProcStatusEnum.PUBLISHED, updatedDatasetVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedDatasetVersion.getPublicationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedDatasetVersion.getPublicationDate()));
    }

    @Override
    public void testResendPublishedDatasetVersionStreamMessage() throws Exception {
        // TODO METAMAC-2504 NO Kafka Test
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED_NAME)
    public void testVersioningDatasetVersion() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED_NAME).getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        DatasetVersionDto newVersion = statisticalResourcesServiceFacade.versioningDatasetVersion(getServiceContextAdministrador(), datasetVersionDto, VersionTypeEnum.MINOR);
        assertNotNull(newVersion);
    }

    @Test
    @MetamacMock(DATASET_VERSION_88_PUBLISHED_WITH_CATEGORISATIONS_NAME)
    public void testVersioningDatasetVersionCheckCategorisationsAutogeneratedMetadata() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_88_PUBLISHED_WITH_CATEGORISATIONS_NAME).getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);
        List<CategorisationDto> categorisations = statisticalResourcesServiceFacade.retrieveCategorisationsByDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn);

        DatasetVersionDto newVersion = statisticalResourcesServiceFacade.versioningDatasetVersion(getServiceContextAdministrador(), datasetVersionDto, VersionTypeEnum.MINOR);
        assertNotNull(newVersion);
        List<CategorisationDto> categorisationsNewVersion = statisticalResourcesServiceFacade.retrieveCategorisationsByDatasetVersion(getServiceContextAdministrador(), newVersion.getUrn());
        assertEquals(categorisations.size(), categorisationsNewVersion.size());
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=ISTAC.agency01:cat_data_1(1.0)", categorisationsNewVersion.get(0).getUrn());
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=ISTAC.agency01:cat_data_2(1.0)", categorisationsNewVersion.get(1).getUrn());
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=ISTAC.agency01:cat_data_3(1.0)", categorisationsNewVersion.get(2).getUrn());
        for (int i = 0; i < categorisations.size(); i++) {
            CategorisationDto categorisationExpected = categorisations.get(i);
            CategorisationDto categorisationActual = categorisationsNewVersion.get(i);
            assertEquals(datasetVersionDto.getUrn(), categorisationExpected.getDatasetVersion().getUrn());
            assertEquals(newVersion.getUrn(), categorisationActual.getDatasetVersion().getUrn());
            assertEquals(categorisationActual.getCategory().getUrn(), categorisationExpected.getCategory().getUrn());
            assertEquals(categorisationActual.getMaintainer().getUrn(), categorisationExpected.getMaintainer().getUrn());
            assertEquals("Categoría " + categorisationActual.getCode(), categorisationActual.getTitle().getLocalisedLabel("es"));
        }
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME)
    public void testSendDatasetVersionToProductionValidation() throws Exception {
        DataMockUtils.mockDsdAndDataRepositorySimpleDimensionsNoAttributes(datasetRepositoriesServiceFacade, srmRestInternalService);

        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        DatasetVersionDto updatedDatasetVersion = statisticalResourcesServiceFacade.sendDatasetVersionToProductionValidation(getServiceContextAdministrador(), datasetVersionDto);
        assertNotNull(updatedDatasetVersion);
        assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, updatedDatasetVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedDatasetVersion.getProductionValidationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedDatasetVersion.getProductionValidationDate()));
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_20_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME)
    public void testSendDatasetVersionToDiffusionValidation() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_20_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        DatasetVersionDto updatedDatasetVersion = statisticalResourcesServiceFacade.sendDatasetVersionToDiffusionValidation(getServiceContextAdministrador(), datasetVersionDto);
        assertNotNull(updatedDatasetVersion);
        assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, updatedDatasetVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedDatasetVersion.getDiffusionValidationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedDatasetVersion.getDiffusionValidationDate()));
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_20_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME)
    public void testSendDatasetVersionToValidationRejected() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_20_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        DatasetVersionDto updatedDatasetVersion = statisticalResourcesServiceFacade.sendDatasetVersionToValidationRejected(getServiceContextAdministrador(), datasetVersionDto);
        assertNotNull(updatedDatasetVersion);
        assertEquals(ProcStatusEnum.VALIDATION_REJECTED, updatedDatasetVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedDatasetVersion.getRejectValidationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedDatasetVersion.getRejectValidationDate()));

        assertNotNull(updatedDatasetVersion.getProductionValidationUser());
        assertNotNull(updatedDatasetVersion.getProductionValidationDate());
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME)
    public void testRetrieveCoverageForDatasetVersionDimension() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME);
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        {
            List<CodeItemDto> codeDimensions = statisticalResourcesServiceFacade.retrieveCoverageForDatasetVersionDimension(getServiceContextAdministrador(), datasetVersionUrn, "dim-none");
            Assert.assertEquals(0, codeDimensions.size());
        }
        {
            List<CodeItemDto> codeDimensions = statisticalResourcesServiceFacade.retrieveCoverageForDatasetVersionDimension(getServiceContextAdministrador(), datasetVersionUrn, "dim1");
            DatasetsAsserts.assertEqualsCodeItemDtosCollection(mockCodeItemDtosWithIdentifiers("code-d1-1", "code-d1-2"), codeDimensions);
        }
        {
            List<CodeItemDto> codeDimensions = statisticalResourcesServiceFacade.retrieveCoverageForDatasetVersionDimension(getServiceContextAdministrador(), datasetVersionUrn, "dim2");
            DatasetsAsserts.assertEqualsCodeItemDtosCollection(mockCodeItemDtosWithIdentifiers("code-d2-1", "code-d2-2"), codeDimensions);
        }
        {
            List<CodeItemDto> codeDimensions = statisticalResourcesServiceFacade.retrieveCoverageForDatasetVersionDimension(getServiceContextAdministrador(), datasetVersionUrn, "dim3");
            DatasetsAsserts.assertEqualsCodeItemDtosCollection(mockCodeItemDtosWithIdentifiers("code-d3-1"), codeDimensions);
        }
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME)
    public void testRetrieveDatasetVersionDimensionsIds() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME);

        List<String> dimensionIds = statisticalResourcesServiceFacade.retrieveDatasetVersionDimensionsIds(getServiceContextAdministrador(),
                datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(dimensionIds, Arrays.asList("dim1", "dim2", "dim3"));
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME)
    public void testFilterCoverageForDatasetVersionDimension() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME);
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        {
            List<CodeItemDto> codeDimensions = statisticalResourcesServiceFacade.filterCoverageForDatasetVersionDimension(getServiceContextAdministrador(), datasetVersionUrn, "TIME_PERIOD", "Enero", null);
            Assert.assertEquals(1, codeDimensions.size());
        }
    }

    @Override
    @Test
    @MetamacMock({STATISTIC_OFFICIALITY_01_BASIC_NAME, STATISTIC_OFFICIALITY_02_BASIC_NAME})
    public void testFindStatisticOfficialities() throws Exception {
        List<StatisticOfficialityDto> officiality = statisticalResourcesServiceFacade.findStatisticOfficialities(getServiceContextAdministrador());
        assertEquals(2, officiality.size());
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME)
    public void testImportDatasourcesInDatasetVersion() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME).getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        URL url = new URL("file", null, "myfile.px");

        HashMap<String, String> mappings = new HashMap<String, String>();
        statisticalResourcesServiceFacade.importDatasourcesInDatasetVersion(getServiceContextAdministrador(), datasetVersionDto, Arrays.asList(url), mappings, false,
                new BasicVersionableStatisticalResourceDto());
    }

    @Override
    public void testImportDatasourcesInStatisticalOperation() throws Exception {
        // See testImportDatasourcesInStatisticalOperation in DatasetServiceTest
    }

    // ------------------------------------------------------------------------
    // ATTRIBUTES
    // ------------------------------------------------------------------------

    @Override
    public void testCreateAttributeInstance() throws Exception {
        // TODO testCreateAttributeInstance (METAMAC-2143)

    }

    @Override
    public void testUpdateAttributeInstance() throws Exception {
        // TODO testCreateAttributeInstance (METAMAC-2143)

    }

    @Override
    public void testDeleteAttributeInstance() throws Exception {
        // TODO testDeleteAttributeInstance (METAMAC-2143)
    }

    @Override
    public void testRetrieveAttributeInstances() throws Exception {
        // TODO testRetrieveAttributeInstances (METAMAC-2143)

    }

    @Override
    public void testUpdateGeographicCoverageVariableElementsCache() throws Exception {
        // no test
    }

    @Override
    public void testUpdateAllGeographicCoverageVariableElementsCache() throws Exception {
        // no test
    }

    @Override
    public void testUpdateGeographicCoverageExternalPublicationVariableElementsCache() throws Exception {
        // no test
    }

    // ------------------------------------------------------------------------
    // CATEGORISATIONS
    // ------------------------------------------------------------------------

    @Override
    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, CATEGORISATION_SEQUENCE_NAME, CATEGORISATION_MAINTAINER_NAME})
    public void testCreateCategorisation() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        CategorisationDto dto = StatisticalResourcesDtoMocks.mockCategorisationDto();

        CategorisationDto persisted = statisticalResourcesServiceFacade.createCategorisation(getServiceContextAdministrador(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), dto);
        assertNotNull(persisted);
        assertNotNull(persisted.getUrn());
    }

    @Override
    @Test
    @MetamacMock({CATEGORISATION_01_DATASET_VERSION_01_NAME})
    public void testRetrieveCategorisationByUrn() throws Exception {
        Categorisation expected = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_01_NAME);
        CategorisationDto actual = statisticalResourcesServiceFacade.retrieveCategorisationByUrn(getServiceContextAdministrador(), expected.getVersionableStatisticalResource().getUrn());
        assertEqualsCategorisation(expected, actual);
    }

    @Override
    @Test
    @MetamacMock({CATEGORISATION_01_DATASET_VERSION_01_NAME})
    public void testDeleteCategorisation() throws Exception {
        Categorisation expected = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_01_NAME);
        String urn = expected.getVersionableStatisticalResource().getUrn();

        // Delete
        statisticalResourcesServiceFacade.deleteCategorisation(getServiceContextAdministrador(), urn);

        expectedMetamacException(new MetamacException(ServiceExceptionType.CATEGORISATION_NOT_FOUND, urn));
        statisticalResourcesServiceFacade.retrieveCategorisationByUrn(getServiceContextAdministrador(), urn);
    }

    @Override
    @Test
    @MetamacMock({CATEGORISATION_01_DATASET_VERSION_03_PUBLISHED_NAME})
    public void testEndCategorisationValidity() throws Exception {
        Categorisation expected = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_03_PUBLISHED_NAME);
        String urn = expected.getVersionableStatisticalResource().getUrn();
        assertNull(expected.getVersionableStatisticalResource().getValidTo());
        assertNull(expected.getValidToEffective());

        DateTime validTo = new DateTime().plusDays(2);
        CategorisationDto actual = statisticalResourcesServiceFacade.endCategorisationValidity(getServiceContextAdministrador(), urn, validTo.toDate());
        MetamacAsserts.assertEqualsDate(validTo, actual.getValidTo());

        DatasetVersionDto actualDatasetVersion = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), actual.getDatasetVersion().getUrn());
        assertEqualsDate(expected.getDatasetVersion().getSiemacMetadataStatisticalResource().getValidTo(), actualDatasetVersion.getValidTo());
    }

    @Override
    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, CATEGORISATION_01_DATASET_VERSION_01_NAME, CATEGORISATION_02_DATASET_VERSION_01_NAME})
    public void testRetrieveCategorisationsByDatasetVersion() throws Exception {
        DatasetVersion datasetVersion1 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        Categorisation categorisation1Dataset1 = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_01_NAME);
        Categorisation categorisation2Dataset1 = categorisationMockFactory.retrieveMock(CATEGORISATION_02_DATASET_VERSION_01_NAME);

        List<CategorisationDto> categorisations = statisticalResourcesServiceFacade.retrieveCategorisationsByDatasetVersion(getServiceContextAdministrador(),
                datasetVersion1.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(2, categorisations.size());
        assertEquals(categorisation1Dataset1.getVersionableStatisticalResource().getUrn(), categorisations.get(0).getUrn());
        assertEquals(categorisation2Dataset1.getVersionableStatisticalResource().getUrn(), categorisations.get(1).getUrn());
    }

    // ------------------------------------------------------------------------
    // PUBLICATIONS
    // ------------------------------------------------------------------------

    @Override
    @Test
    @MetamacMock({PUBLICATION_02_BASIC_WITH_GENERATED_VERSION_NAME, PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testFindPublicationsByCondition() throws Exception {
        PublicationVersion latestPublicationVersionPublication02 = publicationMockFactory.retrieveMock(PUBLICATION_02_BASIC_WITH_GENERATED_VERSION_NAME).getVersions().get(0);
        PublicationVersion latestPublicationVersionPublication03 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME);

        MetamacCriteria metamacCriteria = new MetamacCriteria();

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findPublicationsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(2, results.size());

        assertEqualsPublication(latestPublicationVersionPublication02, results.get(0));
        assertEqualsPublication(latestPublicationVersionPublication03, results.get(1));
    }

    @Test
    @MetamacMock({PUBLICATION_02_BASIC_WITH_GENERATED_VERSION_NAME, PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testFindPublicationsByConditionTitle() throws Exception {
        PublicationVersion latestPublicationVersionPublication03 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        String title = latestPublicationVersionPublication03.getSiemacMetadataStatisticalResource().getTitle().getLocalisedLabel("es");
        setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.TITLE, OperationType.LIKE, title);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findPublicationsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsPublication(latestPublicationVersionPublication03, results.get(0));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME, PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME})
    public void testFindPublicationsByConditionProcStatusDraft() throws Exception {
        PublicationVersion expectedResult = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_12_DRAFT_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DRAFT);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findPublicationsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsPublication(expectedResult, results.get(0));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME, PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME})
    public void testFindPublicationsByConditionProcStatusProductionValidation() throws Exception {
        PublicationVersion expectedResult = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PRODUCTION_VALIDATION);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findPublicationsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsPublication(expectedResult, results.get(0));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME, PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME})
    public void testFindPublicationsByConditionProcStatusDiffusionValidation() throws Exception {
        PublicationVersion expectedResult = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DIFFUSION_VALIDATION);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findPublicationsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsPublication(expectedResult, results.get(0));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME, PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME})
    public void testFindPublicationsByConditionProcStatusValidationRejected() throws Exception {
        PublicationVersion expectedResult = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.VALIDATION_REJECTED);

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findPublicationsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsPublication(expectedResult, results.get(0));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME})
    public void testFindPublicationsByConditionProcStatusPublished() throws Exception {
        PublicationVersion expectedResult = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_16_PUBLISHED_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PUBLISHED);
        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findPublicationsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEqualsPublication(expectedResult, results.get(0));
    }

    // ------------------------------------------------------------------------
    // PUBLICATIONS VERSIONS
    // ------------------------------------------------------------------------

    @Override
    @Test
    public void testCreatePublication() throws Exception {
        PublicationVersionDto publicationVersionDto = StatisticalResourcesDtoMocks.mockPublicationVersionDto();
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto();

        PublicationVersionDto newPublicationVersionDto = statisticalResourcesServiceFacade.createPublication(getServiceContextAdministrador(), publicationVersionDto, statisticalOperation);
        assertNotNull(newPublicationVersionDto);
        assertNotNull(newPublicationVersionDto.getUrn());
    }

    @Test
    public void testCreatePublicationHasExpectedUrn() throws Exception {
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto(StatisticalResourcesMockFactory.OPERATION_01_CODE);
        ExternalItemDto maintainer = StatisticalResourcesDtoMocks.mockAgencyExternalItemDto("SIEMAC");

        PublicationVersionDto publicationVersionDto = StatisticalResourcesDtoMocks.mockPublicationVersionDto();
        publicationVersionDto.setMaintainer(maintainer);

        String persistedPublicationUrn = statisticalResourcesServiceFacade.createPublication(getServiceContextAdministrador(), publicationVersionDto, statisticalOperation).getUrn();
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Collection=SIEMAC:C00025A_000001(1.0)", persistedPublicationUrn);
    }

    @Test
    public void testCreatePublicationErrorDuplicatedUrn() throws Exception {
        // Codes are setting automatically, so URN can not be duplicated
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME, PUBLICATION_VERSION_02_BASIC_NAME})
    public void testUpdatePublicationVersionErrorDuplicatedUrn() throws Exception {
        // Codes are setting automatically, so URN can not be duplicated
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME, PUBLICATION_VERSION_02_BASIC_NAME})
    public void testUpdatePublicationVersion() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();

        PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(), publicationVersionUrn);
        publicationVersionDto.setTitle(StatisticalResourcesDtoMocks.mockInternationalStringDto("es", "Mi titulo"));

        PublicationVersionDto updatedPublicationVersion = statisticalResourcesServiceFacade.updatePublicationVersion(getServiceContextAdministrador(), publicationVersionDto);
        assertNotNull(updatedPublicationVersion);
        assertEqualsInternationalStringDto(publicationVersionDto.getTitle(), updatedPublicationVersion.getTitle());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testUpdatePublicationVersionIgnoreChangeCode() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME);

        PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        publicationVersionDto.setCode("CHANGED_CODE");

        PublicationVersionDto updatedPublicationVersion = statisticalResourcesServiceFacade.updatePublicationVersion(getServiceContextAdministrador(), publicationVersionDto);
        assertNotNull(updatedPublicationVersion);
        assertEquals(publicationVersion.getSiemacMetadataStatisticalResource().getCode(), updatedPublicationVersion.getCode());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testUpdatePublicationVersionIgnoreChangeStatisticalOperation() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME);
        String originalStatisticalOperationCode = publicationVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto();
        publicationVersionDto.setStatisticalOperation(statisticalOperation);

        PublicationVersionDto updatedPublicationVersion = statisticalResourcesServiceFacade.updatePublicationVersion(getServiceContextAdministrador(), publicationVersionDto);
        assertNotNull(updatedPublicationVersion);
        assertEquals(originalStatisticalOperationCode, updatedPublicationVersion.getStatisticalOperation().getCode());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testUpdatePublicationVersionIgnoreChangeMaintainer() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME);
        String originalMaintainerCode = publicationVersion.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested();

        PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        ExternalItemDto maintainer = StatisticalResourcesDtoMocks.mockAgencyExternalItemDto();
        publicationVersionDto.setMaintainer(maintainer);

        PublicationVersionDto updatedPublicationVersion = statisticalResourcesServiceFacade.updatePublicationVersion(getServiceContextAdministrador(), publicationVersionDto);
        assertNotNull(updatedPublicationVersion);
        assertEquals(originalMaintainerCode, updatedPublicationVersion.getMaintainer().getCodeNested());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testUpdatePublicationVersionIgnoreDateNextVersionIfItsNotAllowed() throws Exception {
        // DATE_NEXT_VERSION can only be modified if dateNextVersionType is SCHEDULED_UPDATE

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME);
        String originalDateNextVersion = publicationVersion.getSiemacMetadataStatisticalResource().getNextVersionDate();

        {
            PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                    publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
            publicationVersionDto.setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(1).toDate()));
            publicationVersionDto.setNextVersion(NextVersionTypeEnum.NO_UPDATES);

            PublicationVersionDto updatedPublicationVersionDto = statisticalResourcesServiceFacade.updatePublicationVersion(getServiceContextAdministrador(), publicationVersionDto);
            assertNotNull(updatedPublicationVersionDto);
            assertEquals(originalDateNextVersion, updatedPublicationVersionDto.getNextVersionDate());
        }
        {
            PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                    publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
            publicationVersionDto.setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(1).toDate()));
            publicationVersionDto.setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);

            PublicationVersionDto updatedPublicationVersionDto = statisticalResourcesServiceFacade.updatePublicationVersion(getServiceContextAdministrador(), publicationVersionDto);
            assertNotNull(updatedPublicationVersionDto);
            assertEquals(originalDateNextVersion, updatedPublicationVersionDto.getNextVersionDate());
        }
        {
            PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                    publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
            publicationVersionDto.setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(1).toDate()));
            publicationVersionDto.setNextVersion(NextVersionTypeEnum.SCHEDULED_UPDATE);

            PublicationVersionDto updatedPublicationVersionDto = statisticalResourcesServiceFacade.updatePublicationVersion(getServiceContextAdministrador(), publicationVersionDto);
            assertNotNull(updatedPublicationVersionDto);
            assertEquals(publicationVersionDto.getNextVersionDate(), updatedPublicationVersionDto.getNextVersionDate());
        }
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testUpdatePublicationVersionIgnoreChangeCreatorMetadata() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();

        PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(), publicationVersionUrn);
        String originalCreator = publicationVersionDto.getCreatedBy();

        publicationVersionDto.setCreatedBy("My user");

        PublicationVersionDto updatedDataset = statisticalResourcesServiceFacade.updatePublicationVersion(getServiceContextAdministrador(), publicationVersionDto);
        assertNotNull(updatedDataset);
        assertEquals(originalCreator, updatedDataset.getCreatedBy());
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME, PUBLICATION_VERSION_02_BASIC_NAME})
    public void testDeletePublicationVersion() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();
        statisticalResourcesServiceFacade.deletePublicationVersion(getServiceContextAdministrador(), publicationVersionUrn);

        expectedMetamacException(new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND, publicationVersionUrn));
        statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(), publicationVersionUrn);
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_05_OPERATION_0001_CODE_000001_NAME, PUBLICATION_VERSION_06_OPERATION_0001_CODE_000002_NAME, PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME,
            PUBLICATION_VERSION_08_OPERATION_0002_CODE_000001_NAME, PUBLICATION_VERSION_09_OPERATION_0002_CODE_000002_NAME, PUBLICATION_VERSION_10_OPERATION_0002_CODE_000003_NAME})
    public void testFindPublicationVersionByCondition() throws Exception {
        PublicationVersion publicationVersionOperation1Code1 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_05_OPERATION_0001_CODE_000001_NAME);
        PublicationVersion publicationVersionOperation1Code2 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_06_OPERATION_0001_CODE_000002_NAME);
        PublicationVersion publicationVersionOperation1Code3 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME);
        PublicationVersion publicationVersionOperation2Code1 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_08_OPERATION_0002_CODE_000001_NAME);
        PublicationVersion publicationVersionOperation2Code2 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_09_OPERATION_0002_CODE_000002_NAME);
        PublicationVersion publicationVersionOperation2Code3 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_10_OPERATION_0002_CODE_000003_NAME);

        // Find All
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);

            MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(6, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<PublicationVersionBaseDto> results = pagedResults.getResults();
            assertEquals(6, results.size());

            assertEquals(publicationVersionOperation1Code1.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
            assertEquals(publicationVersionOperation1Code2.getSiemacMetadataStatisticalResource().getUrn(), results.get(1).getUrn());
            assertEquals(publicationVersionOperation1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(2).getUrn());
            assertEquals(publicationVersionOperation2Code1.getSiemacMetadataStatisticalResource().getUrn(), results.get(3).getUrn());
            assertEquals(publicationVersionOperation2Code2.getSiemacMetadataStatisticalResource().getUrn(), results.get(4).getUrn());
            assertEquals(publicationVersionOperation2Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(5).getUrn());
        }
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_05_OPERATION_0001_CODE_000001_NAME, PUBLICATION_VERSION_06_OPERATION_0001_CODE_000002_NAME})
    public void testFindPublicationsVersionsByConditionCheckLastUpdatedIsDefaultOrder() throws Exception {
        String publicationVersionOperation1Code2Urn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_06_OPERATION_0001_CODE_000002_NAME).getSiemacMetadataStatisticalResource()
                .getUrn();

        MetamacCriteria metamacCriteria = new MetamacCriteria();

        MetamacCriteriaResult<PublicationVersionBaseDto> publicationsPagedResult = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(),
                metamacCriteria);

        // Update
        PublicationVersionDto publicationVersionOperation1Code2 = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersionOperation1Code2Urn);
        statisticalResourcesServiceFacade.updatePublicationVersion(getServiceContextAdministrador(), publicationVersionOperation1Code2);

        // Search again and validate
        publicationsPagedResult = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);

        // Validate
        assertEquals(2, publicationsPagedResult.getPaginatorResult().getTotalResults().intValue());
        assertEquals(2, publicationsPagedResult.getResults().size());
        assertTrue(publicationsPagedResult.getResults().get(0) instanceof PublicationVersionBaseDto);

        Date lastUpdated01 = publicationsPagedResult.getResults().get(0).getLastUpdated();
        Date lastUpdated02 = publicationsPagedResult.getResults().get(1).getLastUpdated();
        assertTrue(lastUpdated01.before(lastUpdated02));

        assertEquals(publicationVersionOperation1Code2.getUrn(), publicationsPagedResult.getResults().get(1).getUrn());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_05_OPERATION_0001_CODE_000001_NAME, PUBLICATION_VERSION_06_OPERATION_0001_CODE_000002_NAME, PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME,
            PUBLICATION_VERSION_08_OPERATION_0002_CODE_000001_NAME, PUBLICATION_VERSION_09_OPERATION_0002_CODE_000002_NAME, PUBLICATION_VERSION_10_OPERATION_0002_CODE_000003_NAME})
    public void testFindPublicationsVersionByConditionByCode() throws Exception {
        PublicationVersion publicationVersionOperation1Code3 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME);

        // Find CODE
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OperationType.EQ,
                    publicationVersionOperation1Code3.getSiemacMetadataStatisticalResource().getCode());

            MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<PublicationVersionBaseDto> results = pagedResults.getResults();
            assertEquals(1, results.size());

            assertEquals(publicationVersionOperation1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
        }
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_05_OPERATION_0001_CODE_000001_NAME, PUBLICATION_VERSION_06_OPERATION_0001_CODE_000002_NAME, PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME,
            PUBLICATION_VERSION_08_OPERATION_0002_CODE_000001_NAME, PUBLICATION_VERSION_09_OPERATION_0002_CODE_000002_NAME, PUBLICATION_VERSION_10_OPERATION_0002_CODE_000003_NAME})
    public void testFindPublicationVersionByConditionByUrn() throws Exception {
        PublicationVersion publicationVersionOperation1Code3 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME);

        // Find URN
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.URN, OperationType.EQ,
                    publicationVersionOperation1Code3.getSiemacMetadataStatisticalResource().getUrn());

            MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<PublicationVersionBaseDto> results = pagedResults.getResults();
            assertEquals(1, results.size());

            assertEquals(publicationVersionOperation1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
            assertEquals(publicationVersionOperation1Code3.getSiemacMetadataStatisticalResource().getCode(), results.get(0).getCode());
        }
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME})
    public void testFindPublicationVersionByConditionByProcStatus() throws Exception {
        PublicationVersion publicationVersionDraft = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_12_DRAFT_NAME);
        PublicationVersion publicationVersionProductionValidation = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME);
        PublicationVersion publicationVersionValidationRejected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME);

        // Find PROC STATUS
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.PROC_STATUS, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setDisjunctionCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DRAFT,
                    ProcStatusEnum.VALIDATION_REJECTED);

            MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<PublicationVersionBaseDto> results = pagedResults.getResults();
            assertEquals(2, results.size());

            assertEquals(publicationVersionDraft.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
            assertEquals(publicationVersionValidationRejected.getSiemacMetadataStatisticalResource().getUrn(), results.get(1).getUrn());
        }
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PRODUCTION_VALIDATION);

            MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<PublicationVersionBaseDto> results = pagedResults.getResults();
            assertEquals(1, results.size());

            assertEquals(publicationVersionProductionValidation.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
        }
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_05_OPERATION_0001_CODE_000001_NAME, PUBLICATION_VERSION_06_OPERATION_0001_CODE_000002_NAME, PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME,
            PUBLICATION_VERSION_08_OPERATION_0002_CODE_000001_NAME, PUBLICATION_VERSION_09_OPERATION_0002_CODE_000002_NAME, PUBLICATION_VERSION_10_OPERATION_0002_CODE_000003_NAME})
    public void testFindPublicationsVersionByConditionByStatisticalOperationUrn() throws Exception {
        PublicationVersion publicationOperation1Code1 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_05_OPERATION_0001_CODE_000001_NAME);
        PublicationVersion publicationOperation1Code2 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_06_OPERATION_0001_CODE_000002_NAME);
        PublicationVersion publicationOperation1Code3 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME);
        PublicationVersion publicationOperation2Code1 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_08_OPERATION_0002_CODE_000001_NAME);
        PublicationVersion publicationOperation2Code2 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_09_OPERATION_0002_CODE_000002_NAME);
        PublicationVersion publicationOperation2Code3 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_10_OPERATION_0002_CODE_000003_NAME);

        // Find STATISTICAL_OPERATION
        {
            String statisticalOperationUrn = StatisticalResourcesDoMocks.mockStatisticalOperationUrn(DatasetVersionMockFactory.OPERATION_01_CODE);

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.STATISTICAL_OPERATION_URN, OperationType.EQ, statisticalOperationUrn);

            MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(3, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<PublicationVersionBaseDto> results = pagedResults.getResults();
            assertEquals(3, results.size());

            assertEquals(publicationOperation1Code1.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
            assertEquals(publicationOperation1Code2.getSiemacMetadataStatisticalResource().getUrn(), results.get(1).getUrn());
            assertEquals(publicationOperation1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(2).getUrn());
        }
        {
            String statisticalOperationUrn = StatisticalResourcesDoMocks.mockStatisticalOperationUrn(DatasetVersionMockFactory.OPERATION_02_CODE);

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.STATISTICAL_OPERATION_URN, OperationType.EQ, statisticalOperationUrn);

            MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(3, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<PublicationVersionBaseDto> results = pagedResults.getResults();
            assertEquals(3, results.size());

            assertEquals(publicationOperation2Code1.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
            assertEquals(publicationOperation2Code2.getSiemacMetadataStatisticalResource().getUrn(), results.get(1).getUrn());
            assertEquals(publicationOperation2Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(2).getUrn());
        }

        {
            String statisticalOperationUrn = URN_NOT_EXISTS;

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.STATISTICAL_OPERATION_URN, OperationType.EQ, statisticalOperationUrn);

            MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(0, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<PublicationVersionBaseDto> results = pagedResults.getResults();
            assertEquals(0, results.size());
        }
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_05_OPERATION_0001_CODE_000001_NAME, PUBLICATION_VERSION_06_OPERATION_0001_CODE_000002_NAME, PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME,
            PUBLICATION_VERSION_08_OPERATION_0002_CODE_000001_NAME, PUBLICATION_VERSION_09_OPERATION_0002_CODE_000002_NAME, PUBLICATION_VERSION_10_OPERATION_0002_CODE_000003_NAME})
    public void testFindPublicationVersionByConditionOrderByStatisticalOperationUrn() throws Exception {
        PublicationVersion publicationOperation1Code3 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME);
        PublicationVersion publicationOperation2Code3 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_10_OPERATION_0002_CODE_000003_NAME);

        // FIND CODE
        String code = "000003";

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.STATISTICAL_OPERATION_URN, OrderTypeEnum.ASC);
        setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
        setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.CODE, OperationType.ILIKE, code);

        MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<PublicationVersionBaseDto> results = pagedResults.getResults();
        assertEquals(2, results.size());

        assertEquals(publicationOperation1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
        assertEquals(publicationOperation2Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(1).getUrn());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME, PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME})
    public void testFindPublicationsVersionsByConditionProcStatusDraft() throws Exception {
        PublicationVersion expectedResult = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_12_DRAFT_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DRAFT);

        MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<PublicationVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME, PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME})
    public void testFindPublicationsVersionsByConditionProcStatusProductionValidation() throws Exception {
        PublicationVersion expectedResult = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PRODUCTION_VALIDATION);

        MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<PublicationVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME, PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME})
    public void testFindPublicationsVersionsByConditionProcStatusDiffusionValidation() throws Exception {
        PublicationVersion expectedResult = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DIFFUSION_VALIDATION);

        MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<PublicationVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME, PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME})
    public void testFindPublicationsVersionsByConditionProcStatusValidationRejected() throws Exception {
        PublicationVersion expectedResult = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.VALIDATION_REJECTED);

        MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<PublicationVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME})
    public void testFindPublicationsVersionsByConditionProcStatusPublished() throws Exception {
        PublicationVersion expectedResult = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_16_PUBLISHED_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PUBLISHED);
        MetamacCriteriaResult<PublicationVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findPublicationVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<PublicationVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME, PUBLICATION_VERSION_02_BASIC_NAME})
    public void testRetrievePublicationVersionByUrn() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME);
        PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEqualsPublicationVersion(publicationVersion, publicationVersionDto);
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testRetrieveLatestPublicationVersion() throws Exception {
        String publicationUrn = publicationMockFactory.retrieveMock(PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        PublicationVersion expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME);
        PublicationVersionDto actual = statisticalResourcesServiceFacade.retrieveLatestPublicationVersion(getServiceContextAdministrador(), publicationUrn);
        assertEqualsPublicationVersion(expected, actual);
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testRetrieveLatestPublishedPublicationVersion() throws Exception {
        String publicationUrn = publicationMockFactory.retrieveMock(PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        PublicationVersion expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME);
        PublicationVersionDto actual = statisticalResourcesServiceFacade.retrieveLatestPublishedPublicationVersion(getServiceContextAdministrador(), publicationUrn);
        assertEqualsPublicationVersion(expected, actual);
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME)
    public void testRetrievePublicationVersions() throws Exception {
        PublicationVersion publicationVersionFirst = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME);
        PublicationVersion publicationVersionLast = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME);
        // Version in urn does not care
        {
            List<PublicationVersionBaseDto> publicationsVersions = statisticalResourcesServiceFacade.retrievePublicationVersions(getServiceContextAdministrador(),
                    publicationVersionLast.getSiemacMetadataStatisticalResource().getUrn());
            assertNotNull(publicationsVersions);
            assertEquals(2, publicationsVersions.size());
            assertEqualsPublicationVersionBase(publicationVersionFirst, publicationsVersions.get(0));
            assertEqualsPublicationVersionBase(publicationVersionLast, publicationsVersions.get(1));
        }
        {
            List<PublicationVersionBaseDto> publicationsVersions = statisticalResourcesServiceFacade.retrievePublicationVersions(getServiceContextAdministrador(),
                    publicationVersionFirst.getSiemacMetadataStatisticalResource().getUrn());
            assertNotNull(publicationsVersions);
            assertEquals(2, publicationsVersions.size());
            assertEqualsPublicationVersionBase(publicationVersionFirst, publicationsVersions.get(0));
            assertEqualsPublicationVersionBase(publicationVersionLast, publicationsVersions.get(1));
        }
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_33_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME)
    public void testSendPublicationVersionToProductionValidation() throws Exception {
        DataStructure emptyDsd = new DataStructure();
        emptyDsd.setDataStructureComponents(new DataStructureComponents());
        Mockito.when(srmRestInternalService.retrieveDsdByUrn(Mockito.anyString())).thenReturn(emptyDsd);

        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_33_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(), publicationVersionUrn);

        PublicationVersionDto updatedPublicationVersion = statisticalResourcesServiceFacade.sendPublicationVersionToProductionValidation(getServiceContextAdministrador(), publicationVersionDto);
        assertNotNull(updatedPublicationVersion);
        assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, updatedPublicationVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedPublicationVersion.getProductionValidationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedPublicationVersion.getProductionValidationDate()));
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_37_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME)
    public void testSendPublicationVersionToDiffusionValidation() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_37_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME)
                .getSiemacMetadataStatisticalResource().getUrn();
        PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(), publicationVersionUrn);

        PublicationVersionDto updatedPublicationVersion = statisticalResourcesServiceFacade.sendPublicationVersionToDiffusionValidation(getServiceContextAdministrador(), publicationVersionDto);
        assertNotNull(updatedPublicationVersion);
        assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, updatedPublicationVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedPublicationVersion.getDiffusionValidationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedPublicationVersion.getDiffusionValidationDate()));
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_38_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME)
    public void testSendPublicationVersionToValidationRejected() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_38_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME)
                .getSiemacMetadataStatisticalResource().getUrn();
        PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(), publicationVersionUrn);

        PublicationVersionDto updatedPublicationVersion = statisticalResourcesServiceFacade.sendPublicationVersionToValidationRejected(getServiceContextAdministrador(), publicationVersionDto);
        assertNotNull(updatedPublicationVersion);
        assertEquals(ProcStatusEnum.VALIDATION_REJECTED, updatedPublicationVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedPublicationVersion.getRejectValidationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedPublicationVersion.getRejectValidationDate()));

        assertNotNull(updatedPublicationVersion.getProductionValidationUser());
        assertNotNull(updatedPublicationVersion.getProductionValidationDate());
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME)
    public void testPublishPublicationVersion() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME).getLifeCycleStatisticalResource()
                .getUrn();
        PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(), publicationVersionUrn);

        PublicationVersionDto updatedPublicationVersion = statisticalResourcesServiceFacade.publishPublicationVersion(getServiceContextAdministrador(), publicationVersionDto);
        assertNotNull(updatedPublicationVersion);
        assertEquals(ProcStatusEnum.PUBLISHED, updatedPublicationVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedPublicationVersion.getPublicationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedPublicationVersion.getPublicationDate()));
    }

    @Override
    public void testResendPublishedPublicationVersionStreamMessage() throws Exception {
        // No Kafka test
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_16_PUBLISHED_NAME)
    public void testVersioningPublicationVersion() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_16_PUBLISHED_NAME).getSiemacMetadataStatisticalResource().getUrn();
        PublicationVersionDto publicationVersionDto = statisticalResourcesServiceFacade.retrievePublicationVersionByUrn(getServiceContextAdministrador(), publicationVersionUrn);

        PublicationVersionDto newVersion = statisticalResourcesServiceFacade.versioningPublicationVersion(getServiceContextAdministrador(), publicationVersionDto, VersionTypeEnum.MINOR);
        assertNotNull(newVersion);
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testRetrievePublicationVersionStructure() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        PublicationStructureDto publicationStructureDto = statisticalResourcesServiceFacade.retrievePublicationVersionStructure(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEqualsPublicationVersionStructure(publicationVersion, publicationStructureDto);
    }

    @Test
    @Override
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testImportPublicationVersionStructure() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_NONEXISTENT_QUERY, 5, "C00031A_000001"));
        String locale = "es";
        File file = loadTSVFile("publication_structure-3-levels.tsv");
        URL fileURL = file.toURI().toURL();

        PublicationVersion expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME);

        statisticalResourcesServiceFacade.importPublicationVersionStructure(getServiceContextAdministrador(), expected.getLifeCycleStatisticalResource().getUrn(), fileURL, locale);
    }

    // ------------------------------------------------------------
    // CHAPTER
    // ------------------------------------------------------------

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testCreateChapter() throws Exception {
        String publicationUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        ChapterDto expected = StatisticalResourcesDtoMocks.mockChapterDto();
        ChapterDto actual = statisticalResourcesServiceFacade.createChapter(getServiceContextAdministrador(), publicationUrn, expected);
        assertNotNull(actual);
        assertNotNull(actual.getUrn());
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateChapter() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        ChapterDto expected = statisticalResourcesServiceFacade.retrieveChapter(getServiceContextAdministrador(),
                publicationVersion.getChildrenFirstLevel().get(0).getChapter().getNameableStatisticalResource().getUrn());
        expected.setTitle(StatisticalResourcesNotPersistedDoMocks.mockInternationalStringDto());
        ChapterDto actual = statisticalResourcesServiceFacade.updateChapter(getServiceContextAdministrador(), expected);
        assertEqualsInternationalStringDto(expected.getTitle(), actual.getTitle());
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateChapterLocation() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        String chapterUrn = publicationVersion.getChildrenFirstLevel().get(0).getChapter().getNameableStatisticalResource().getUrn();
        String parentChapterUrn = publicationVersion.getChildrenFirstLevel().get(1).getChapter().getNameableStatisticalResource().getUrn();
        ChapterDto chapterDto = statisticalResourcesServiceFacade.updateChapterLocation(getServiceContextAdministrador(), chapterUrn, parentChapterUrn, Long.valueOf(1));
        assertEquals(Long.valueOf(1), chapterDto.getOrderInLevel());
        assertEquals(parentChapterUrn, chapterDto.getParentChapterUrn());
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testRetrieveChapter() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Chapter expected = publicationVersion.getChildrenFirstLevel().get(0).getChapter();
        ChapterDto actual = statisticalResourcesServiceFacade.retrieveChapter(getServiceContextAdministrador(), expected.getNameableStatisticalResource().getUrn());
        assertEqualsChapter(expected, actual);
    }

    @Override
    @Test
    @MetamacMock(CHAPTER_01_BASIC_NAME)
    public void testDeleteChapter() throws Exception {
        String chapterUrn = chapterMockFactory.retrieveMock(CHAPTER_01_BASIC_NAME).getNameableStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND, chapterUrn));

        statisticalResourcesServiceFacade.deleteChapter(getServiceContextAdministrador(), chapterUrn);
        statisticalResourcesServiceFacade.retrieveChapter(getServiceContextAdministrador(), chapterUrn);
    }

    // ------------------------------------------------------------
    // CUBES
    // ------------------------------------------------------------

    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_01_BASIC_NAME})
    public void testCreateCube() throws Exception {
        String publicationUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        String datasetUrn = datasetMockFactory.retrieveMock(DATASET_01_BASIC_NAME).getIdentifiableStatisticalResource().getUrn();
        CubeDto expected = StatisticalResourcesDtoMocks.mockDatasetCubeDto(datasetUrn);
        CubeDto actual = statisticalResourcesServiceFacade.createCube(getServiceContextAdministrador(), publicationUrn, expected);
        assertNotNull(actual);
        assertNotNull(actual.getUrn());
    }

    @Override
    @Test
    @MetamacMock(CUBE_01_BASIC_NAME)
    public void testUpdateCube() throws Exception {
        String cubeUrn = cubeMockFactory.retrieveMock(CUBE_01_BASIC_NAME).getNameableStatisticalResource().getUrn();
        CubeDto expected = statisticalResourcesServiceFacade.retrieveCube(getServiceContextAdministrador(), cubeUrn);
        expected.setTitle(StatisticalResourcesNotPersistedDoMocks.mockInternationalStringDto());
        CubeDto actual = statisticalResourcesServiceFacade.updateCube(getServiceContextAdministrador(), expected);
        assertEqualsInternationalStringDto(expected.getTitle(), actual.getTitle());
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateCubeLocation() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        String cubeUrn = publicationVersion.getChildrenFirstLevel().get(3).getCube().getNameableStatisticalResource().getUrn();
        String parentChapterUrn = publicationVersion.getChildrenFirstLevel().get(2).getChapter().getNameableStatisticalResource().getUrn();
        CubeDto cubeDto = statisticalResourcesServiceFacade.updateCubeLocation(getServiceContextAdministrador(), cubeUrn, parentChapterUrn, Long.valueOf(1));
        assertEquals(Long.valueOf(1), cubeDto.getOrderInLevel());
        assertEquals(parentChapterUrn, cubeDto.getParentChapterUrn());
    }

    @Test
    @MetamacMock({CUBE_01_BASIC_NAME, DATASET_01_BASIC_NAME})
    public void testUpdateCubeDataset() throws Exception {
        String cubeUrn = cubeMockFactory.retrieveMock(CUBE_01_BASIC_NAME).getNameableStatisticalResource().getUrn();
        CubeDto expected = statisticalResourcesServiceFacade.retrieveCube(getServiceContextAdministrador(), cubeUrn);
        expected.setDatasetUrn(datasetMockFactory.retrieveMock(DATASET_01_BASIC_NAME).getIdentifiableStatisticalResource().getUrn());
        expected.setQueryUrn(null);
        expected.setMultidatasetUrn(null);
        CubeDto actual = statisticalResourcesServiceFacade.updateCube(getServiceContextAdministrador(), expected);
        assertEqualsInternationalStringDto(expected.getTitle(), actual.getTitle());
    }

    @Test
    @MetamacMock({CUBE_01_BASIC_NAME, QUERY_01_SIMPLE_NAME})
    public void testUpdateCubeQuery() throws Exception {
        String cubeUrn = cubeMockFactory.retrieveMock(CUBE_01_BASIC_NAME).getNameableStatisticalResource().getUrn();
        CubeDto expected = statisticalResourcesServiceFacade.retrieveCube(getServiceContextAdministrador(), cubeUrn);
        expected.setQueryUrn(queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME).getIdentifiableStatisticalResource().getUrn());
        expected.setDatasetUrn(null);
        expected.setMultidatasetUrn(null);
        CubeDto actual = statisticalResourcesServiceFacade.updateCube(getServiceContextAdministrador(), expected);
        assertEqualsInternationalStringDto(expected.getTitle(), actual.getTitle());
    }

    @Test
    @MetamacMock({CUBE_01_BASIC_NAME})
    public void testUpdateCubeUrl() throws Exception {
        String cubeUrn = cubeMockFactory.retrieveMock(CUBE_01_BASIC_NAME).getNameableStatisticalResource().getUrn();
        CubeDto expected = statisticalResourcesServiceFacade.retrieveCube(getServiceContextAdministrador(), cubeUrn);
        expected.setQueryUrn(null);
        expected.setDatasetUrn(null);
        expected.setMultidatasetUrn(null);
        expected.setUrl("http://www.pruebasUpdate.com");
        CubeDto actual = statisticalResourcesServiceFacade.updateCube(getServiceContextAdministrador(), expected);
        assertEquals(expected.getUrl(), actual.getUrl());
    }

    @Override
    @Test
    @MetamacMock({CUBE_01_BASIC_NAME, CUBE_02_BASIC_NAME})
    public void testRetrieveCube() throws Exception {
        Cube expected = cubeMockFactory.retrieveMock(CUBE_01_BASIC_NAME);
        CubeDto actual = statisticalResourcesServiceFacade.retrieveCube(getServiceContextAdministrador(), expected.getNameableStatisticalResource().getUrn());
        assertEqualsCube(expected, actual);
    }

    @Override
    @Test
    @MetamacMock(CUBE_01_BASIC_NAME)
    public void testDeleteCube() throws Exception {
        String urn = cubeMockFactory.retrieveMock(CUBE_01_BASIC_NAME).getNameableStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.CUBE_NOT_FOUND, urn));

        statisticalResourcesServiceFacade.deleteCube(getServiceContextAdministrador(), urn);
        statisticalResourcesServiceFacade.retrieveCube(getServiceContextAdministrador(), urn);
    }

    // ------------------------------------------------------------
    // CONSTRAINTS
    // ------------------------------------------------------------

    @Override
    public void testCreateContentConstraint() throws Exception {
        // Without test in facade
    }

    @Override
    public void testRetrieveContentConstraintByUrn() throws Exception {
        // Without test in facade
    }

    @Override
    public void testDeleteContentConstraint() throws Exception {
        // Without test in facade
    }

    @Override
    public void testFindContentConstraintsForArtefact() throws Exception {
        // Without test in facade
    }

    @Override
    public void testRetrieveRegionForContentConstraint() throws Exception {
        // Without test in facade
    }

    @Override
    public void testSaveRegionForContentConstraint() throws Exception {
        // Without test in facade
    }

    @Override
    public void testDeleteRegion() throws Exception {
        // Without test in facade
    }

    // ------------------------------------------------------------
    // CRITERIA UTILS
    // ------------------------------------------------------------
    @SuppressWarnings("rawtypes")
    private void addOrderToCriteria(MetamacCriteria metamacCriteria, Enum property, OrderTypeEnum orderType) {
        if (metamacCriteria.getOrdersBy() == null) {
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        }
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(orderType);
        order.setPropertyName(property.name());
        metamacCriteria.getOrdersBy().add(order);
    }

    private void setCriteriaPaginator(MetamacCriteria metamacCriteria, int firstResult, int maxResultSize, Boolean countTotalResults) {
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(firstResult);
        metamacCriteria.getPaginator().setMaximumResultSize(maxResultSize);
        metamacCriteria.getPaginator().setCountTotalResults(countTotalResults);
    }

    @SuppressWarnings("rawtypes")
    private void setCriteriaEnumPropertyRestriction(MetamacCriteria metamacCriteria, Enum property, OperationType operationType, Enum enumValue) {
        MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction();
        propertyRestriction.setPropertyName(property.name());
        propertyRestriction.setOperationType(operationType);
        propertyRestriction.setEnumValue(enumValue);
        metamacCriteria.setRestriction(propertyRestriction);
    }

    @SuppressWarnings("rawtypes")
    private void setConjunctionCriteriaStringPropertyRestriction(MetamacCriteria metamacCriteria, Enum property01, Enum property02, OperationType operationType, String stringValue01,
            String stringValue02) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();
        conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(property01.name(), stringValue01, OperationType.EQ));
        conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(property02.name(), stringValue02, OperationType.EQ));
        metamacCriteria.setRestriction(conjunctionRestriction);
    }

    @SuppressWarnings("rawtypes")
    private void setDisjunctionCriteriaEnumPropertyRestriction(MetamacCriteria metamacCriteria, Enum property, OperationType operationType, Enum enumValue01, Enum enumValue02) {
        MetamacCriteriaDisjunctionRestriction disjunctionRestriction = new MetamacCriteriaDisjunctionRestriction();
        disjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(property.name(), enumValue01, OperationType.EQ));
        disjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(property.name(), enumValue02, OperationType.EQ));
        metamacCriteria.setRestriction(disjunctionRestriction);
    }

    @SuppressWarnings("rawtypes")
    private void setCriteriaStringPropertyRestriction(MetamacCriteria metamacCriteria, Enum property, OperationType operationType, String stringValue) {
        MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction();
        propertyRestriction.setPropertyName(property.name());
        propertyRestriction.setOperationType(operationType);
        propertyRestriction.setStringValue(stringValue);
        metamacCriteria.setRestriction(propertyRestriction);
    }

    @SuppressWarnings("rawtypes")
    private void setCriteriaBooleanPropertyRestriction(MetamacCriteria metamacCriteria, Enum property, OperationType operationType, Boolean booleanValue) {
        MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction();
        propertyRestriction.setPropertyName(property.name());
        propertyRestriction.setOperationType(operationType);
        propertyRestriction.setBooleanValue(booleanValue);
        metamacCriteria.setRestriction(propertyRestriction);
    }

    @SuppressWarnings("rawtypes")
    private void setCriteriaDatePropertyRestriction(MetamacCriteria metamacCriteria, Enum property, OperationType operationType, Date date) {
        MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction();
        propertyRestriction.setPropertyName(property.name());
        propertyRestriction.setOperationType(operationType);
        propertyRestriction.setDateValue(date);
        metamacCriteria.setRestriction(propertyRestriction);
    }

    private void mockDsdAndDataRepositorySimpleDimensions() throws Exception {
        DataMockUtils.mockDsdAndDataRepositorySimpleDimensionsNoAttributes(datasetRepositoriesServiceFacade, srmRestInternalService);
    }

    private void mockDsdAndCreateDatasetRepository(DatasetVersionDto expected, ExternalItemDto statisticalOperation) throws Exception, ApplicationException {
        String urn = buildDatasetUrn(expected.getMaintainer().getCodeNested(), statisticalOperation.getCode(), 1, StatisticalResourcesMockFactory.INIT_VERSION);
        DataMockUtils.mockDsdAndCreateDatasetRepository(datasetRepositoriesServiceFacade, srmRestInternalService, urn);
    }

    // ------------------------------------------------------------------------
    // MULTIDATASETS VERSIONS
    // ------------------------------------------------------------------------

    @Override
    @Test
    public void testCreateMultidataset() throws Exception {
        MultidatasetVersionDto multidatasetVersionDto = StatisticalResourcesDtoMocks.mockMultidatasetVersionDto();
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto();

        MultidatasetVersionDto newMultidatasetVersionDto = statisticalResourcesServiceFacade.createMultidataset(getServiceContextAdministrador(), multidatasetVersionDto, statisticalOperation);
        assertNotNull(newMultidatasetVersionDto);
        assertNotNull(newMultidatasetVersionDto.getUrn());
    }

    @Test
    public void testCreateMultidatasetHasExpectedUrn() throws Exception {
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto(StatisticalResourcesMockFactory.OPERATION_04_CODE);
        ExternalItemDto maintainer = StatisticalResourcesDtoMocks.mockAgencyExternalItemDto("SIEMAC");

        MultidatasetVersionDto multidatasetVersionDto = StatisticalResourcesDtoMocks.mockMultidatasetVersionDto();
        multidatasetVersionDto.setMaintainer(maintainer);

        String persistedMultidatasetUrn = statisticalResourcesServiceFacade.createMultidataset(getServiceContextAdministrador(), multidatasetVersionDto, statisticalOperation).getUrn();
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Multidataset=SIEMAC:C00025D_000001(1.0)", persistedMultidatasetUrn);
    }

    @Test
    public void testCreateMultidatasetErrorDuplicatedUrn() throws Exception {
        // Codes are setting automatically, so URN can not be duplicated
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_01_BASIC_NAME, MULTIDATASET_VERSION_02_BASIC_NAME})
    public void testUpdateMultidatasetVersionErrorDuplicatedUrn() throws Exception {
        // Codes are setting automatically, so URN can not be duplicated
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_VERSION_01_BASIC_NAME, MULTIDATASET_VERSION_02_BASIC_NAME})
    public void testUpdateMultidatasetVersion() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();

        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(), multidatasetVersionUrn);
        multidatasetVersionDto.setTitle(StatisticalResourcesDtoMocks.mockInternationalStringDto("es", "Mi titulo"));

        MultidatasetVersionDto updatedMultidatasetVersion = statisticalResourcesServiceFacade.updateMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionDto);
        assertNotNull(updatedMultidatasetVersion);
        assertEqualsInternationalStringDto(multidatasetVersionDto.getTitle(), updatedMultidatasetVersion.getTitle());
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_01_BASIC_NAME})
    public void testUpdateMultidatasetVersionIgnoreChangeCode() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_01_BASIC_NAME);

        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(),
                multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        multidatasetVersionDto.setCode("CHANGED_CODE");

        MultidatasetVersionDto updatedMultidatasetVersion = statisticalResourcesServiceFacade.updateMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionDto);
        assertNotNull(updatedMultidatasetVersion);
        assertEquals(multidatasetVersion.getSiemacMetadataStatisticalResource().getCode(), updatedMultidatasetVersion.getCode());
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_01_BASIC_NAME})
    public void testUpdateMultidatasetVersionIgnoreChangeStatisticalOperation() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_01_BASIC_NAME);
        String originalStatisticalOperationCode = multidatasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(),
                multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto();
        multidatasetVersionDto.setStatisticalOperation(statisticalOperation);

        MultidatasetVersionDto updatedMultidatasetVersion = statisticalResourcesServiceFacade.updateMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionDto);
        assertNotNull(updatedMultidatasetVersion);
        assertEquals(originalStatisticalOperationCode, updatedMultidatasetVersion.getStatisticalOperation().getCode());
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_01_BASIC_NAME})
    public void testUpdateMultidatasetVersionIgnoreChangeMaintainer() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_01_BASIC_NAME);
        String originalMaintainerCode = multidatasetVersion.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested();

        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(),
                multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        ExternalItemDto maintainer = StatisticalResourcesDtoMocks.mockAgencyExternalItemDto();
        multidatasetVersionDto.setMaintainer(maintainer);

        MultidatasetVersionDto updatedMultidatasetVersion = statisticalResourcesServiceFacade.updateMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionDto);
        assertNotNull(updatedMultidatasetVersion);
        assertEquals(originalMaintainerCode, updatedMultidatasetVersion.getMaintainer().getCodeNested());
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_01_BASIC_NAME})
    public void testUpdateMultidatasetVersionIgnoreDateNextVersionIfItsNotAllowed() throws Exception {
        // DATE_NEXT_VERSION can only be modified if dateNextVersionType is SCHEDULED_UPDATE

        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_01_BASIC_NAME);
        String originalDateNextVersion = multidatasetVersion.getSiemacMetadataStatisticalResource().getNextVersionDate();

        {
            MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(),
                    multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());
            multidatasetVersionDto.setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(1).toDate()));
            multidatasetVersionDto.setNextVersion(NextVersionTypeEnum.NO_UPDATES);

            MultidatasetVersionDto updatedMultidatasetVersionDto = statisticalResourcesServiceFacade.updateMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionDto);
            assertNotNull(updatedMultidatasetVersionDto);
            assertEquals(originalDateNextVersion, updatedMultidatasetVersionDto.getNextVersionDate());
        }
        {
            MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(),
                    multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());
            multidatasetVersionDto.setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(1).toDate()));
            multidatasetVersionDto.setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);

            MultidatasetVersionDto updatedMultidatasetVersionDto = statisticalResourcesServiceFacade.updateMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionDto);
            assertNotNull(updatedMultidatasetVersionDto);
            assertEquals(originalDateNextVersion, updatedMultidatasetVersionDto.getNextVersionDate());
        }
        {
            MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(),
                    multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());
            multidatasetVersionDto.setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(1).toDate()));
            multidatasetVersionDto.setNextVersion(NextVersionTypeEnum.SCHEDULED_UPDATE);

            MultidatasetVersionDto updatedMultidatasetVersionDto = statisticalResourcesServiceFacade.updateMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionDto);
            assertNotNull(updatedMultidatasetVersionDto);
            assertEquals(multidatasetVersionDto.getNextVersionDate(), updatedMultidatasetVersionDto.getNextVersionDate());
        }
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_01_BASIC_NAME})
    public void testUpdateMultidatasetVersionIgnoreChangeCreatorMetadata() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();

        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(), multidatasetVersionUrn);
        String originalCreator = multidatasetVersionDto.getCreatedBy();

        multidatasetVersionDto.setCreatedBy("My user");

        MultidatasetVersionDto updatedDataset = statisticalResourcesServiceFacade.updateMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionDto);
        assertNotNull(updatedDataset);
        assertEquals(originalCreator, updatedDataset.getCreatedBy());
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_VERSION_01_BASIC_NAME, MULTIDATASET_VERSION_02_BASIC_NAME})
    public void testDeleteMultidatasetVersion() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();
        statisticalResourcesServiceFacade.deleteMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionUrn);

        expectedMetamacException(new MetamacException(ServiceExceptionType.MULTIDATASET_VERSION_NOT_FOUND, multidatasetVersionUrn));
        statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(), multidatasetVersionUrn);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_VERSION_05_OPERATION_0001_CODE_000001_NAME, MULTIDATASET_VERSION_06_OPERATION_0001_CODE_000002_NAME, MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME,
            MULTIDATASET_VERSION_08_OPERATION_0002_CODE_000001_NAME, MULTIDATASET_VERSION_09_OPERATION_0002_CODE_000002_NAME, MULTIDATASET_VERSION_10_OPERATION_0002_CODE_000003_NAME})
    public void testFindMultidatasetVersionByCondition() throws Exception {
        MultidatasetVersion multidatasetVersionOperation1Code1 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_05_OPERATION_0001_CODE_000001_NAME);
        MultidatasetVersion multidatasetVersionOperation1Code2 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_06_OPERATION_0001_CODE_000002_NAME);
        MultidatasetVersion multidatasetVersionOperation1Code3 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME);
        MultidatasetVersion multidatasetVersionOperation2Code1 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_08_OPERATION_0002_CODE_000001_NAME);
        MultidatasetVersion multidatasetVersionOperation2Code2 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_09_OPERATION_0002_CODE_000002_NAME);
        MultidatasetVersion multidatasetVersionOperation2Code3 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_10_OPERATION_0002_CODE_000003_NAME);

        // Find All
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);

            MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(6, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
            assertEquals(6, results.size());

            assertEquals(multidatasetVersionOperation1Code1.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
            assertEquals(multidatasetVersionOperation1Code2.getSiemacMetadataStatisticalResource().getUrn(), results.get(1).getUrn());
            assertEquals(multidatasetVersionOperation1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(2).getUrn());
            assertEquals(multidatasetVersionOperation2Code1.getSiemacMetadataStatisticalResource().getUrn(), results.get(3).getUrn());
            assertEquals(multidatasetVersionOperation2Code2.getSiemacMetadataStatisticalResource().getUrn(), results.get(4).getUrn());
            assertEquals(multidatasetVersionOperation2Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(5).getUrn());
        }
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_05_OPERATION_0001_CODE_000001_NAME, MULTIDATASET_VERSION_06_OPERATION_0001_CODE_000002_NAME})
    public void testFindMultidatasetsVersionsByConditionCheckLastUpdatedIsDefaultOrder() throws Exception {
        String multidatasetVersionOperation1Code2Urn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_06_OPERATION_0001_CODE_000002_NAME).getSiemacMetadataStatisticalResource()
                .getUrn();

        MetamacCriteria metamacCriteria = new MetamacCriteria();

        MetamacCriteriaResult<MultidatasetVersionBaseDto> multidatasetsPagedResult = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(),
                metamacCriteria);

        // Update
        MultidatasetVersionDto multidatasetVersionOperation1Code2 = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(),
                multidatasetVersionOperation1Code2Urn);
        statisticalResourcesServiceFacade.updateMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionOperation1Code2);

        // Search again and validate
        multidatasetsPagedResult = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);

        // Validate
        assertEquals(2, multidatasetsPagedResult.getPaginatorResult().getTotalResults().intValue());
        assertEquals(2, multidatasetsPagedResult.getResults().size());
        assertTrue(multidatasetsPagedResult.getResults().get(0) instanceof MultidatasetVersionBaseDto);

        Date lastUpdated01 = multidatasetsPagedResult.getResults().get(0).getLastUpdated();
        Date lastUpdated02 = multidatasetsPagedResult.getResults().get(1).getLastUpdated();
        assertTrue(lastUpdated01.before(lastUpdated02));

        assertEquals(multidatasetVersionOperation1Code2.getUrn(), multidatasetsPagedResult.getResults().get(1).getUrn());
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_05_OPERATION_0001_CODE_000001_NAME, MULTIDATASET_VERSION_06_OPERATION_0001_CODE_000002_NAME, MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME,
            MULTIDATASET_VERSION_08_OPERATION_0002_CODE_000001_NAME, MULTIDATASET_VERSION_09_OPERATION_0002_CODE_000002_NAME, MULTIDATASET_VERSION_10_OPERATION_0002_CODE_000003_NAME})
    public void testFindMultidatasetsVersionByConditionByCode() throws Exception {
        MultidatasetVersion multidatasetVersionOperation1Code3 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME);

        // Find CODE
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OperationType.EQ,
                    multidatasetVersionOperation1Code3.getSiemacMetadataStatisticalResource().getCode());

            MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
            assertEquals(1, results.size());

            assertEquals(multidatasetVersionOperation1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
        }
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_05_OPERATION_0001_CODE_000001_NAME, MULTIDATASET_VERSION_06_OPERATION_0001_CODE_000002_NAME, MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME,
            MULTIDATASET_VERSION_08_OPERATION_0002_CODE_000001_NAME, MULTIDATASET_VERSION_09_OPERATION_0002_CODE_000002_NAME, MULTIDATASET_VERSION_10_OPERATION_0002_CODE_000003_NAME})
    public void testFindMultidatasetVersionByConditionByUrn() throws Exception {
        MultidatasetVersion multidatasetVersionOperation1Code3 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME);

        // Find URN
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.URN, OperationType.EQ,
                    multidatasetVersionOperation1Code3.getSiemacMetadataStatisticalResource().getUrn());

            MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
            assertEquals(1, results.size());

            assertEquals(multidatasetVersionOperation1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
            assertEquals(multidatasetVersionOperation1Code3.getSiemacMetadataStatisticalResource().getCode(), results.get(0).getCode());
        }
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME})
    public void testFindMultidatasetVersionByConditionByProcStatus() throws Exception {
        MultidatasetVersion multidatasetVersionDraft = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_12_DRAFT_NAME);
        MultidatasetVersion multidatasetVersionProductionValidation = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME);
        MultidatasetVersion multidatasetVersionValidationRejected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME);

        // Find PROC STATUS
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.PROC_STATUS, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setDisjunctionCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DRAFT,
                    ProcStatusEnum.VALIDATION_REJECTED);

            MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
            assertEquals(2, results.size());

            assertEquals(multidatasetVersionDraft.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
            assertEquals(multidatasetVersionValidationRejected.getSiemacMetadataStatisticalResource().getUrn(), results.get(1).getUrn());
        }
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PRODUCTION_VALIDATION);

            MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
            assertEquals(1, results.size());

            assertEquals(multidatasetVersionProductionValidation.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
        }
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_05_OPERATION_0001_CODE_000001_NAME, MULTIDATASET_VERSION_06_OPERATION_0001_CODE_000002_NAME, MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME,
            MULTIDATASET_VERSION_08_OPERATION_0002_CODE_000001_NAME, MULTIDATASET_VERSION_09_OPERATION_0002_CODE_000002_NAME, MULTIDATASET_VERSION_10_OPERATION_0002_CODE_000003_NAME})
    public void testFindMultidatasetsVersionByConditionByStatisticalOperationUrn() throws Exception {
        MultidatasetVersion multidatasetOperation1Code1 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_05_OPERATION_0001_CODE_000001_NAME);
        MultidatasetVersion multidatasetOperation1Code2 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_06_OPERATION_0001_CODE_000002_NAME);
        MultidatasetVersion multidatasetOperation1Code3 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME);
        MultidatasetVersion multidatasetOperation2Code1 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_08_OPERATION_0002_CODE_000001_NAME);
        MultidatasetVersion multidatasetOperation2Code2 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_09_OPERATION_0002_CODE_000002_NAME);
        MultidatasetVersion multidatasetOperation2Code3 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_10_OPERATION_0002_CODE_000003_NAME);

        // Find STATISTICAL_OPERATION
        {
            String statisticalOperationUrn = StatisticalResourcesDoMocks.mockStatisticalOperationUrn(DatasetVersionMockFactory.OPERATION_01_CODE);

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.STATISTICAL_OPERATION_URN, OperationType.EQ, statisticalOperationUrn);

            MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(3, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
            assertEquals(3, results.size());

            assertEquals(multidatasetOperation1Code1.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
            assertEquals(multidatasetOperation1Code2.getSiemacMetadataStatisticalResource().getUrn(), results.get(1).getUrn());
            assertEquals(multidatasetOperation1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(2).getUrn());
        }
        {
            String statisticalOperationUrn = StatisticalResourcesDoMocks.mockStatisticalOperationUrn(DatasetVersionMockFactory.OPERATION_02_CODE);

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.STATISTICAL_OPERATION_URN, OperationType.EQ, statisticalOperationUrn);

            MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(3, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
            assertEquals(3, results.size());

            assertEquals(multidatasetOperation2Code1.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
            assertEquals(multidatasetOperation2Code2.getSiemacMetadataStatisticalResource().getUrn(), results.get(1).getUrn());
            assertEquals(multidatasetOperation2Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(2).getUrn());
        }

        {
            String statisticalOperationUrn = URN_NOT_EXISTS;

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.CODE, OrderTypeEnum.ASC);
            setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
            setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.STATISTICAL_OPERATION_URN, OperationType.EQ, statisticalOperationUrn);

            MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(0, pagedResults.getPaginatorResult().getTotalResults().intValue());
            List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
            assertEquals(0, results.size());
        }
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_05_OPERATION_0001_CODE_000001_NAME, MULTIDATASET_VERSION_06_OPERATION_0001_CODE_000002_NAME, MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME,
            MULTIDATASET_VERSION_08_OPERATION_0002_CODE_000001_NAME, MULTIDATASET_VERSION_09_OPERATION_0002_CODE_000002_NAME, MULTIDATASET_VERSION_10_OPERATION_0002_CODE_000003_NAME})
    public void testFindMultidatasetVersionByConditionOrderByStatisticalOperationUrn() throws Exception {
        MultidatasetVersion multidatasetOperation1Code3 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME);
        MultidatasetVersion multidatasetOperation2Code3 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_10_OPERATION_0002_CODE_000003_NAME);

        // FIND CODE
        String code = "000003";

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        addOrderToCriteria(metamacCriteria, StatisticalResourcesCriteriaOrderEnum.STATISTICAL_OPERATION_URN, OrderTypeEnum.ASC);
        setCriteriaPaginator(metamacCriteria, 0, Integer.MAX_VALUE, Boolean.TRUE);
        setCriteriaStringPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.CODE, OperationType.ILIKE, code);

        MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
        assertEquals(2, results.size());

        assertEquals(multidatasetOperation1Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(0).getUrn());
        assertEquals(multidatasetOperation2Code3.getSiemacMetadataStatisticalResource().getUrn(), results.get(1).getUrn());
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME, MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME})
    public void testFindMultidatasetsVersionsByConditionProcStatusDraft() throws Exception {
        MultidatasetVersion expectedResult = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_12_DRAFT_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DRAFT);

        MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME, MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME})
    public void testFindMultidatasetsVersionsByConditionProcStatusProductionValidation() throws Exception {
        MultidatasetVersion expectedResult = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PRODUCTION_VALIDATION);

        MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME, MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME})
    public void testFindMultidatasetsVersionsByConditionProcStatusDiffusionValidation() throws Exception {
        MultidatasetVersion expectedResult = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.DIFFUSION_VALIDATION);

        MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME, MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME})
    public void testFindMultidatasetsVersionsByConditionProcStatusValidationRejected() throws Exception {
        MultidatasetVersion expectedResult = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.VALIDATION_REJECTED);

        MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME})
    public void testFindMultidatasetsVersionsByConditionProcStatusPublished() throws Exception {
        MultidatasetVersion expectedResult = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_16_PUBLISHED_NAME);

        // Restrictions
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        setCriteriaEnumPropertyRestriction(metamacCriteria, StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS, OperationType.EQ, ProcStatusEnum.PUBLISHED);
        MetamacCriteriaResult<MultidatasetVersionBaseDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetVersionByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<MultidatasetVersionBaseDto> results = pagedResults.getResults();
        assertEquals(1, results.size());

        assertEquals(expectedResult.getLifeCycleStatisticalResource().getUrn(), results.get(0).getUrn());
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_VERSION_01_BASIC_NAME, MULTIDATASET_VERSION_02_BASIC_NAME})
    public void testRetrieveMultidatasetVersionByUrn() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_01_BASIC_NAME);
        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(),
                multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEqualsMultidatasetVersion(multidatasetVersion, multidatasetVersionDto);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testRetrieveLatestMultidatasetVersion() throws Exception {
        String multidatasetUrn = multidatasetMockFactory.retrieveMock(MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        MultidatasetVersion expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME);
        MultidatasetVersionDto actual = statisticalResourcesServiceFacade.retrieveLatestMultidatasetVersion(getServiceContextAdministrador(), multidatasetUrn);
        assertEqualsMultidatasetVersion(expected, actual);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testRetrieveLatestPublishedMultidatasetVersion() throws Exception {
        String multidatasetUrn = multidatasetMockFactory.retrieveMock(MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        MultidatasetVersion expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME);
        MultidatasetVersionDto actual = statisticalResourcesServiceFacade.retrieveLatestPublishedMultidatasetVersion(getServiceContextAdministrador(), multidatasetUrn);
        assertEqualsMultidatasetVersion(expected, actual);
    }

    @Override
    @Test
    @MetamacMock(MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME)
    public void testRetrieveMultidatasetVersions() throws Exception {
        MultidatasetVersion multidatasetVersionFirst = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME);
        MultidatasetVersion multidatasetVersionLast = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME);
        // Version in urn does not care
        {
            List<MultidatasetVersionBaseDto> multidatasetsVersions = statisticalResourcesServiceFacade.retrieveMultidatasetVersions(getServiceContextAdministrador(),
                    multidatasetVersionLast.getSiemacMetadataStatisticalResource().getUrn());
            assertNotNull(multidatasetsVersions);
            assertEquals(2, multidatasetsVersions.size());
            assertEqualsMultidatasetVersionBase(multidatasetVersionFirst, multidatasetsVersions.get(0));
            assertEqualsMultidatasetVersionBase(multidatasetVersionLast, multidatasetsVersions.get(1));
        }
        {
            List<MultidatasetVersionBaseDto> multidatasetsVersions = statisticalResourcesServiceFacade.retrieveMultidatasetVersions(getServiceContextAdministrador(),
                    multidatasetVersionFirst.getSiemacMetadataStatisticalResource().getUrn());
            assertNotNull(multidatasetsVersions);
            assertEquals(2, multidatasetsVersions.size());
            assertEqualsMultidatasetVersionBase(multidatasetVersionFirst, multidatasetsVersions.get(0));
            assertEqualsMultidatasetVersionBase(multidatasetVersionLast, multidatasetsVersions.get(1));
        }
    }

    @Override
    @Test
    @MetamacMock(MULTIDATASET_VERSION_33_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME)
    public void testSendMultidatasetVersionToProductionValidation() throws Exception {
        DataStructure emptyDsd = new DataStructure();
        emptyDsd.setDataStructureComponents(new DataStructureComponents());
        Mockito.when(srmRestInternalService.retrieveDsdByUrn(Mockito.anyString())).thenReturn(emptyDsd);

        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_33_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(), multidatasetVersionUrn);

        MultidatasetVersionDto updatedMultidatasetVersion = statisticalResourcesServiceFacade.sendMultidatasetVersionToProductionValidation(getServiceContextAdministrador(), multidatasetVersionDto);
        assertNotNull(updatedMultidatasetVersion);
        assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, updatedMultidatasetVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedMultidatasetVersion.getProductionValidationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedMultidatasetVersion.getProductionValidationDate()));
    }

    @Override
    @Test
    @MetamacMock(MULTIDATASET_VERSION_37_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME)
    public void testSendMultidatasetVersionToDiffusionValidation() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_37_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME)
                .getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(), multidatasetVersionUrn);

        MultidatasetVersionDto updatedMultidatasetVersion = statisticalResourcesServiceFacade.sendMultidatasetVersionToDiffusionValidation(getServiceContextAdministrador(), multidatasetVersionDto);
        assertNotNull(updatedMultidatasetVersion);
        assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, updatedMultidatasetVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedMultidatasetVersion.getDiffusionValidationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedMultidatasetVersion.getDiffusionValidationDate()));
    }

    @Override
    @Test
    @MetamacMock(MULTIDATASET_VERSION_38_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME)
    public void testSendMultidatasetVersionToValidationRejected() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_38_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME)
                .getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(), multidatasetVersionUrn);

        MultidatasetVersionDto updatedMultidatasetVersion = statisticalResourcesServiceFacade.sendMultidatasetVersionToValidationRejected(getServiceContextAdministrador(), multidatasetVersionDto);
        assertNotNull(updatedMultidatasetVersion);
        assertEquals(ProcStatusEnum.VALIDATION_REJECTED, updatedMultidatasetVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedMultidatasetVersion.getRejectValidationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedMultidatasetVersion.getRejectValidationDate()));

        assertNotNull(updatedMultidatasetVersion.getProductionValidationUser());
        assertNotNull(updatedMultidatasetVersion.getProductionValidationDate());
    }

    @Override
    @Test
    @MetamacMock(MULTIDATASET_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME)
    public void testPublishMultidatasetVersion() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME).getLifeCycleStatisticalResource()
                .getUrn();
        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(), multidatasetVersionUrn);

        MultidatasetVersionDto updatedMultidatasetVersion = statisticalResourcesServiceFacade.publishMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionDto);
        assertNotNull(updatedMultidatasetVersion);
        assertEquals(ProcStatusEnum.PUBLISHED, updatedMultidatasetVersion.getProcStatus());
        assertEquals(getServiceContextAdministrador().getUserId(), updatedMultidatasetVersion.getPublicationUser());
        assertEqualsDay(new DateTime().toDateTime(), new DateTime(updatedMultidatasetVersion.getPublicationDate()));
    }

    @Override
    public void testResendPublishedMultidatasetVersionStreamMessage() throws Exception {
        // No Kafka test
    }

    @Override
    @Test
    @MetamacMock(MULTIDATASET_VERSION_16_PUBLISHED_NAME)
    public void testVersioningMultidatasetVersion() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_16_PUBLISHED_NAME).getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(), multidatasetVersionUrn);

        MultidatasetVersionDto newVersion = statisticalResourcesServiceFacade.versioningMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionDto, VersionTypeEnum.MINOR);
        assertNotNull(newVersion);
    }

    // ------------------------------------------------------------
    // MULTIDATASET CUBES
    // ------------------------------------------------------------

    @Override
    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_01_BASIC_NAME})
    public void testCreateMultidatasetCube() throws Exception {
        String multidatasetUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        String datasetUrn = datasetMockFactory.retrieveMock(DATASET_01_BASIC_NAME).getIdentifiableStatisticalResource().getUrn();
        MultidatasetCubeDto expected = StatisticalResourcesDtoMocks.mockDatasetMultidatasetCubeDto(datasetUrn);
        MultidatasetCubeDto actual = statisticalResourcesServiceFacade.createMultidatasetCube(getServiceContextAdministrador(), multidatasetUrn, expected);
        assertNotNull(actual);
        assertNotNull(actual.getUrn());
    }

    @Override
    @Test
    @MetamacMock(MULTIDATASET_CUBE_01_BASIC_NAME)
    public void testUpdateMultidatasetCube() throws Exception {
        String multidatasetCubeUrn = multidatasetCubeMockFactory.retrieveMock(MULTIDATASET_CUBE_01_BASIC_NAME).getNameableStatisticalResource().getUrn();
        MultidatasetCubeDto expected = statisticalResourcesServiceFacade.retrieveMultidatasetCube(getServiceContextAdministrador(), multidatasetCubeUrn);
        expected.setTitle(StatisticalResourcesNotPersistedDoMocks.mockInternationalStringDto());
        MultidatasetCubeDto actual = statisticalResourcesServiceFacade.updateMultidatasetCube(getServiceContextAdministrador(), expected);
        assertEqualsInternationalStringDto(expected.getTitle(), actual.getTitle());
    }

    @Override
    @Test
    @MetamacMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateMultidatasetCubeLocation() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        String multidatasetCubeUrn = multidatasetVersion.getCubes().get(3).getNameableStatisticalResource().getUrn();
        MultidatasetCubeDto multidatasetCubeDto = statisticalResourcesServiceFacade.updateMultidatasetCubeLocation(getServiceContextAdministrador(), multidatasetCubeUrn, Long.valueOf(1));
        assertEquals(Long.valueOf(1), multidatasetCubeDto.getOrderInMultidataset());
    }

    @Test
    @MetamacMock({MULTIDATASET_CUBE_01_BASIC_NAME, DATASET_01_BASIC_NAME})
    public void testUpdateMultidatasetCubeDataset() throws Exception {
        String multidatasetCubeUrn = multidatasetCubeMockFactory.retrieveMock(MULTIDATASET_CUBE_01_BASIC_NAME).getNameableStatisticalResource().getUrn();
        MultidatasetCubeDto expected = statisticalResourcesServiceFacade.retrieveMultidatasetCube(getServiceContextAdministrador(), multidatasetCubeUrn);
        expected.setDatasetUrn(datasetMockFactory.retrieveMock(DATASET_01_BASIC_NAME).getIdentifiableStatisticalResource().getUrn());
        expected.setQueryUrn(null);
        MultidatasetCubeDto actual = statisticalResourcesServiceFacade.updateMultidatasetCube(getServiceContextAdministrador(), expected);
        assertEqualsInternationalStringDto(expected.getTitle(), actual.getTitle());
    }

    @Test
    @MetamacMock({MULTIDATASET_CUBE_01_BASIC_NAME, QUERY_01_SIMPLE_NAME})
    public void testUpdateMultidatasetCubeQuery() throws Exception {
        String multidatasetCubeUrn = multidatasetCubeMockFactory.retrieveMock(MULTIDATASET_CUBE_01_BASIC_NAME).getNameableStatisticalResource().getUrn();
        MultidatasetCubeDto expected = statisticalResourcesServiceFacade.retrieveMultidatasetCube(getServiceContextAdministrador(), multidatasetCubeUrn);
        expected.setQueryUrn(queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME).getIdentifiableStatisticalResource().getUrn());
        expected.setDatasetUrn(null);
        MultidatasetCubeDto actual = statisticalResourcesServiceFacade.updateMultidatasetCube(getServiceContextAdministrador(), expected);
        assertEqualsInternationalStringDto(expected.getTitle(), actual.getTitle());
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_CUBE_01_BASIC_NAME, MULTIDATASET_CUBE_02_BASIC_NAME})
    public void testRetrieveMultidatasetCube() throws Exception {
        MultidatasetCube expected = multidatasetCubeMockFactory.retrieveMock(MULTIDATASET_CUBE_01_BASIC_NAME);
        MultidatasetCubeDto actual = statisticalResourcesServiceFacade.retrieveMultidatasetCube(getServiceContextAdministrador(), expected.getNameableStatisticalResource().getUrn());
        assertEqualsMultidatasetCube(expected, actual);
    }

    @Override
    @Test
    @MetamacMock(MULTIDATASET_CUBE_01_BASIC_NAME)
    public void testDeleteMultidatasetCube() throws Exception {
        String urn = multidatasetCubeMockFactory.retrieveMock(MULTIDATASET_CUBE_01_BASIC_NAME).getNameableStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.MULTIDATASET_CUBE_NOT_FOUND, urn));

        statisticalResourcesServiceFacade.deleteMultidatasetCube(getServiceContextAdministrador(), urn);
        statisticalResourcesServiceFacade.retrieveMultidatasetCube(getServiceContextAdministrador(), urn);
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_33_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME)
    public void testSendMultidatasetVersionToProductionValidationWithNoFilteringDimension() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_33_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(), multidatasetVersionUrn);
        multidatasetVersionDto.setFilteringDimension(null);

        expectedMetamacException(
                new MetamacException(ServiceExceptionType.METADATA_REQUIRED, addParameter(ServiceExceptionParameters.MULTIDATASET_VERSION, ServiceExceptionSingleParameters.FILTERING_DIMENSION)));

        statisticalResourcesServiceFacade.sendMultidatasetVersionToProductionValidation(getServiceContextAdministrador(), multidatasetVersionDto);

        Assert.fail("Should have thrown Exception and should not send to production");
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_37_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME)
    public void testSendMultidatasetVersionToDiffusionValidationWithNoFilteringDimension() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_37_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME)
                .getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(), multidatasetVersionUrn);
        multidatasetVersionDto.setFilteringDimension(null);

        expectedMetamacException(
                new MetamacException(ServiceExceptionType.METADATA_REQUIRED, addParameter(ServiceExceptionParameters.MULTIDATASET_VERSION, ServiceExceptionSingleParameters.FILTERING_DIMENSION)));

        statisticalResourcesServiceFacade.sendMultidatasetVersionToDiffusionValidation(getServiceContextAdministrador(), multidatasetVersionDto);

        Assert.fail("Should have thrown Exception and should not send to diffusion");
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME)
    public void testPublishMultidatasetVersionWithNoFilteringDimension() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME)
                .getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(), multidatasetVersionUrn);
        multidatasetVersionDto.setFilteringDimension(null);

        expectedMetamacException(
                new MetamacException(ServiceExceptionType.METADATA_REQUIRED, addParameter(ServiceExceptionParameters.MULTIDATASET_VERSION, ServiceExceptionSingleParameters.FILTERING_DIMENSION)));

        statisticalResourcesServiceFacade.publishMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionDto);

        Assert.fail("Should have thrown Exception and should not be published");
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_16_PUBLISHED_NAME)
    public void testVersioningMultidatasetVersionWithNoFilteringDimension() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_16_PUBLISHED_NAME).getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(), multidatasetVersionUrn);
        multidatasetVersionDto.setFilteringDimension(null);

        expectedMetamacException(
                new MetamacException(ServiceExceptionType.METADATA_REQUIRED, addParameter(ServiceExceptionParameters.MULTIDATASET_VERSION, ServiceExceptionSingleParameters.FILTERING_DIMENSION)));

        statisticalResourcesServiceFacade.versioningMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersionDto, VersionTypeEnum.MINOR);

        Assert.fail("Should have thrown Exception and should not be versioned");
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_38_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME)
    public void testSendMultidatasetVersionToValidationRejectedWithNoFilteringDimension() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_38_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME)
                .getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetVersionDto multidatasetVersionDto = statisticalResourcesServiceFacade.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(), multidatasetVersionUrn);
        multidatasetVersionDto.setFilteringDimension(null);

        expectedMetamacException(
                new MetamacException(ServiceExceptionType.METADATA_REQUIRED, addParameter(ServiceExceptionParameters.MULTIDATASET_VERSION, ServiceExceptionSingleParameters.FILTERING_DIMENSION)));

        statisticalResourcesServiceFacade.sendMultidatasetVersionToValidationRejected(getServiceContextAdministrador(), multidatasetVersionDto);

        Assert.fail("Should have thrown Exception and should not be rejected");
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME, MULTIDATASET_04_STRUCTURED_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testFindMultidatasetsByCondition() throws Exception {
        MultidatasetVersion expected1 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME);
        MultidatasetVersion expected2 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_18_WITH_STRUCTURE_FOR_MULTIDATASET_VERSION_04_AND_LAST_VERSION_NAME);

        MetamacCriteria metamacCriteria = new MetamacCriteria();

        MetamacCriteriaResult<RelatedResourceDto> pagedResults = statisticalResourcesServiceFacade.findMultidatasetsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(2, pagedResults.getPaginatorResult().getTotalResults().intValue());
        List<RelatedResourceDto> results = pagedResults.getResults();
        assertEquals(2, results.size());

        assertEqualsMultidataset(expected1, results.get(0));
        assertEqualsMultidataset(expected2, results.get(1));
    }

    @Override
    @Test
    @MetamacMock({DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME})
    public void testCreateDatabaseDatasourceInDatasetVersion() throws Exception {
        String tableName = RandomStringUtils.randomAlphabetic(10);
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();

        DataMockUtils.mockDsdAndDataRepositorySimpleDimensionsNoAttributes(datasetRepositoriesServiceFacade, srmRestInternalService);
        statisticalResourcesServiceFacade.createDatabaseDatasourceInDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn, tableName);

        List<DatasourceDto> datasources = statisticalResourcesServiceFacade.retrieveDatasourcesByDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn);

        assertTrue(CollectionUtils.isNotEmpty(datasources));
        assertEquals(1, datasources.size());
        assertNotNull(datasources.get(0).getCode());
        assertTrue(datasources.get(0).getCode().startsWith(tableName));
    }

    private DatasetVersionBaseDto find(List<DatasetVersionBaseDto> datasetVersionDtos, String urn) {
        for (DatasetVersionBaseDto datasetVersionBaseDto : datasetVersionDtos) {
            if (urn.equals(datasetVersionBaseDto.getUrn())) {
                return datasetVersionBaseDto;
            }
        }
        return null;
    }

    @Override
    @Test
    @MetamacMock(STATISTIC_OFFICIALITY_01_BASIC_NAME)
    public void testCopyDatasetVersion() throws Exception {
        StatisticOfficiality officiality = statisticOfficialityMockFactory.retrieveMock(STATISTIC_OFFICIALITY_01_BASIC_NAME);
        DatasetVersionDto datasetVersionDto = StatisticalResourcesDtoMocks.mockDatasetVersionDto(officiality);
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto();

        mockDsdAndCreateDatasetRepository(datasetVersionDto, statisticalOperation);

        DatasetVersionDto newDatasetVersionDto = statisticalResourcesServiceFacade.copyDatasetVersion(getServiceContextAdministrador(), datasetVersionDto, statisticalOperation);
        assertNotNull(newDatasetVersionDto);
        assertNotNull(newDatasetVersionDto.getUrn());
        assertNotNull(newDatasetVersionDto.getViewCode());
        assertEquals(DatasetVersionUtils.generateViewCode(newDatasetVersionDto.getCode()), newDatasetVersionDto.getViewCode());

    }

    @Test
    @MetamacMock({DATASET_VERSION_06_FOR_QUERIES_NAME})
    public void testGenerateDifferentUrn() throws Exception {
        ExternalItemDto statisticalOperation = StatisticalResourcesDtoMocks.mockStatisticalOperationExternalItemDto();

        QueryVersionDto queryToPersist = mockQueryVersionDto(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME));
        queryToPersist.getSelection().clear();
        queryToPersist.getSelection().put("DIM_01", Arrays.asList(new CodeItemDto("CODE_01", "code 01")));
        queryToPersist.getSelection().put("DIM_02", Arrays.asList(new CodeItemDto("CODE_11", "code 11")));

        QueryVersionDto persistedQuery = statisticalResourcesServiceFacade.createQuery(getServiceContextAdministrador(), queryToPersist, statisticalOperation);
        QueryVersionDto persistedQueryCopy = statisticalResourcesServiceFacade.createQuery(getServiceContextAdministrador(), queryToPersist, statisticalOperation);

        assertNotSame(persistedQuery, persistedQueryCopy);
    }

    @Override
    public void testImportAttributesFromFile() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME).getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        URL url = new URL("file", null, "myfile.px");

        statisticalResourcesServiceFacade.importAttributesFromFile(getServiceContextAdministrador(), datasetVersionDto, Arrays.asList(url));
    }

    @Override
    public void testCheckAttributeInstance() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testUpdateDatasetVersionInGroup() throws Exception {
        // // Without test in facade

    }

    @Override
    public void testRetrieveExternalItemsByDatasetUrn() throws Exception {
        // TODO Auto-generated method stub
        
    }
}
