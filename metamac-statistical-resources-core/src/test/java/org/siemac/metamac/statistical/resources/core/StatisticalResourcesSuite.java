package org.siemac.metamac.statistical.resources.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.siemac.metamac.statistical.resources.core.base.mapper.BaseDto2DoMapperTest;
import org.siemac.metamac.statistical.resources.core.base.repositoryimpl.IdentifiableStatisticalResourceRepositoryTest;
import org.siemac.metamac.statistical.resources.core.base.repositoryimpl.SiemacMetadataStatisticalResourceRepositoryTest;
import org.siemac.metamac.statistical.resources.core.base.utils.StatisticalResourcesUrnParserUtilsTest;
import org.siemac.metamac.statistical.resources.core.base.utils.VersionUtilsTest;
import org.siemac.metamac.statistical.resources.core.base.validators.BaseInvocationValidatorTest;
import org.siemac.metamac.statistical.resources.core.common.mapper.CommonDo2DtoMapperTest;
import org.siemac.metamac.statistical.resources.core.common.mapper.CommonDto2DoMapperTest;
import org.siemac.metamac.statistical.resources.core.common.repositoryimpl.InternationalStringRepositoryTest;
import org.siemac.metamac.statistical.resources.core.dataset.mapper.DatasetDo2DtoMapperTest;
import org.siemac.metamac.statistical.resources.core.dataset.mapper.DatasetDto2DoMapperTest;
import org.siemac.metamac.statistical.resources.core.dataset.mapper.StatRepoDto2StatisticalResourcesDtoMapperTest;
import org.siemac.metamac.statistical.resources.core.dataset.mapper.StatisticalResourcesDto2StatRepoDtoMapperTest;
import org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl.CodeDimensionRepositoryTest;
import org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl.DatasetRepositoryTest;
import org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl.DatasetVersionRepositoryTest;
import org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl.DatasourceRepositoryTest;
import org.siemac.metamac.statistical.resources.core.dataset.serviceapi.DatasetIntegrationServiceTest;
import org.siemac.metamac.statistical.resources.core.dataset.serviceapi.DatasetServiceDatasourceManagementTest;
import org.siemac.metamac.statistical.resources.core.dataset.serviceapi.DatasetServiceImportationTest;
import org.siemac.metamac.statistical.resources.core.dataset.serviceapi.DatasetServiceTest;
import org.siemac.metamac.statistical.resources.core.dataset.utils.DatasetVersionUtilsTest;
import org.siemac.metamac.statistical.resources.core.enume.utils.ProcStatusEnumUtilsTest;
import org.siemac.metamac.statistical.resources.core.enume.utils.QueryStatusEnumUtilsTest;
import org.siemac.metamac.statistical.resources.core.enume.utils.TypeRelatedResourceEnumUtilsTest;
import org.siemac.metamac.statistical.resources.core.error.StatisticalResourcesCheckTranslationsTest;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesOptimisticLockingTest;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacadeTest;
import org.siemac.metamac.statistical.resources.core.geocache.serviceapi.CacheServiceTest;
import org.siemac.metamac.statistical.resources.core.io.serviceapi.DataManipulateTest;
import org.siemac.metamac.statistical.resources.core.lifecycle.LifecycleCheckerTest;
import org.siemac.metamac.statistical.resources.core.lifecycle.LifecycleCommonMetadataCheckerTest;
import org.siemac.metamac.statistical.resources.core.lifecycle.LifecycleFillerTest;
import org.siemac.metamac.statistical.resources.core.lifecycle.SiemacLifecycleCheckerTest;
import org.siemac.metamac.statistical.resources.core.lifecycle.SiemacLifecycleFillerTest;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.checker.ExternalItemCheckerTest;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.dataset.DatasetLifecycleServiceTest;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.dataset.DatasetPublishingServiceTest;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.dataset.DatasetVersioningServiceTest;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.publication.PublicationLifecycleServiceTest;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.publication.PublicationPublishingServiceTest;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.query.QueryLifecycleServiceTest;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.query.QueryPublishingServiceTest;
import org.siemac.metamac.statistical.resources.core.publication.mapper.PublicationDo2DtoMapperTest;
import org.siemac.metamac.statistical.resources.core.publication.mapper.PublicationDto2DoMapperTest;
import org.siemac.metamac.statistical.resources.core.publication.repositoryimpl.ChapterRepositoryTest;
import org.siemac.metamac.statistical.resources.core.publication.repositoryimpl.CubeRepositoryTest;
import org.siemac.metamac.statistical.resources.core.publication.repositoryimpl.PublicationRepositoryTest;
import org.siemac.metamac.statistical.resources.core.publication.repositoryimpl.PublicationVersionRepositoryTest;
import org.siemac.metamac.statistical.resources.core.publication.serviceapi.PublicationIntegrationServiceTest;
import org.siemac.metamac.statistical.resources.core.publication.serviceapi.PublicationServiceTest;
import org.siemac.metamac.statistical.resources.core.publication.serviceapi.PublicationVersioningServiceTest;
import org.siemac.metamac.statistical.resources.core.query.mapper.QueryDo2DtoMapperTest;
import org.siemac.metamac.statistical.resources.core.query.mapper.QueryDto2DoMapperTest;
import org.siemac.metamac.statistical.resources.core.query.repositoryimpl.QueryRepositoryTest;
import org.siemac.metamac.statistical.resources.core.query.repositoryimpl.QueryVersionRepositoryTest;
import org.siemac.metamac.statistical.resources.core.query.serviceapi.QueryIntegrationServiceTest;
import org.siemac.metamac.statistical.resources.core.query.serviceapi.QueryServiceTest;
import org.siemac.metamac.statistical.resources.core.query.serviceapi.QueryVersioningServiceTest;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesCollectionUtilsTest;

// @formatter:off
@RunWith(Suite.class)
@Suite.SuiteClasses({CacheServiceTest.class,
                     StatisticalResourcesCheckTranslationsTest.class,
                     StatisticalResourcesServiceFacadeTest.class,  
                     StatisticalResourcesOptimisticLockingTest.class,
                     InternationalStringRepositoryTest.class,
                     SiemacMetadataStatisticalResourceRepositoryTest.class,
                     IdentifiableStatisticalResourceRepositoryTest.class,
                     BaseInvocationValidatorTest.class,
                     BaseDto2DoMapperTest.class,
                     QueryRepositoryTest.class,
                     QueryVersionRepositoryTest.class, 
                     QueryServiceTest.class,  
                     QueryDo2DtoMapperTest.class,
                     QueryDto2DoMapperTest.class,
                     DatasourceRepositoryTest.class,
                     DatasetRepositoryTest.class,
                     CodeDimensionRepositoryTest.class,
                     DatasetVersionRepositoryTest.class, 
                     DatasetDo2DtoMapperTest.class,
                     DatasetDto2DoMapperTest.class,
                     StatisticalResourcesDto2StatRepoDtoMapperTest.class, 
                     StatRepoDto2StatisticalResourcesDtoMapperTest.class,
                     DatasetServiceTest.class, 
                     DatasetServiceImportationTest.class, 
                     DatasetServiceDatasourceManagementTest.class, 
                     PublicationRepositoryTest.class,
                     PublicationVersionRepositoryTest.class,
                     CubeRepositoryTest.class,
                     ChapterRepositoryTest.class,
                     PublicationServiceTest.class,
                     PublicationDo2DtoMapperTest.class,
                     PublicationDto2DoMapperTest.class,
                     ProcStatusEnumUtilsTest.class, 
                     TypeRelatedResourceEnumUtilsTest.class,
                     QueryStatusEnumUtilsTest.class,
                     LifecycleCommonMetadataCheckerTest.class,
                     LifecycleCheckerTest.class,
                     SiemacLifecycleCheckerTest.class,
                     LifecycleFillerTest.class,
                     SiemacLifecycleFillerTest.class, 
                     DatasetLifecycleServiceTest.class,
                     PublicationLifecycleServiceTest.class,
                     QueryLifecycleServiceTest.class,
                     VersionUtilsTest.class,
                     DatasetVersionUtilsTest.class,
                     CommonDto2DoMapperTest.class,
                     CommonDo2DtoMapperTest.class,
                     ExternalItemCheckerTest.class,
                     DatasetIntegrationServiceTest.class,
                     PublicationIntegrationServiceTest.class,
                     QueryIntegrationServiceTest.class,
                     StatisticalResourcesCollectionUtilsTest.class,
                     StatisticalResourcesUrnParserUtilsTest.class,
                     DatasetPublishingServiceTest.class,
                     DatasetVersioningServiceTest.class,
                     PublicationPublishingServiceTest.class,
                     PublicationVersioningServiceTest.class,
                     QueryPublishingServiceTest.class,
                     QueryVersioningServiceTest.class,
                     DataManipulateTest.class
                     })
// @formatter:on
public class StatisticalResourcesSuite {
}
