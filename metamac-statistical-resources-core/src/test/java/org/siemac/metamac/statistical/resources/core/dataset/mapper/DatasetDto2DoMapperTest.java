package org.siemac.metamac.statistical.resources.core.dataset.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsCategorisation;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasetVersion;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasource;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_01_DATASET_VERSION_01_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_100_WITH_STATISTIC_OFFICIALITY_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_107_DRAFT_NOT_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_108_PUBLISHED_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_109_DRAFT_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_110_PRODUCTION_VALIDATION_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_111_DIFFUSION_VALIDATION_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_112_VALIDATION_REJECTED_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_57_DRAFT_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_58_PRODUCTION_VALIDATION_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_59_DIFFUSION_VALIDATION_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_60_VALIDATION_REJECTED_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_62_DRAFT_NOT_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_63_PRODUCTION_VALIDATION_NOT_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_64_DIFFUSION_VALIDATION_NOT_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_65_VALIDATION_REJECTED_NOT_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_66_PUBLISHED_NOT_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_77_NO_PUB_REPLACES_DATASET_78_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_78_PUB_IS_REPLACED_BY_DATASET_77_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticOfficialityMockFactory.STATISTIC_OFFICIALITY_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticOfficialityMockFactory.STATISTIC_OFFICIALITY_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDtoMocks.createStatisticOfficialityDtoFromDo;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasourceDto;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DataSourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDtoMocks;
import org.siemac.metamac.statistical.resources.core.utils.shared.StatisticalResourcesVersionSharedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class DatasetDto2DoMapperTest extends StatisticalResourcesBaseTest {

    @Autowired
    private DatasetDto2DoMapper               datasetDto2DoMapper;

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    @Test
    public void testDatasourceDtoToDo() throws MetamacException {
        DatasourceDto dto = StatisticalResourcesDtoMocks.mockDatasourceDto();
        Datasource entity = datasetDto2DoMapper.datasourceDtoToDo(dto);
        assertEqualsDatasource(dto, entity);
    }

    @Test
    @MetamacMock(STATISTIC_OFFICIALITY_01_BASIC_NAME)
    public void testDatasetDtoToDo() throws MetamacException {
        StatisticOfficiality officiality = statisticOfficialityMockFactory.retrieveMock(STATISTIC_OFFICIALITY_01_BASIC_NAME);
        DatasetVersionDto dto = StatisticalResourcesDtoMocks.mockDatasetVersionDto(officiality);
        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);
        assertEqualsDatasetVersion(dto, entity);
    }

    @Test
    @MetamacMock({DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME})
    public void testDatasetDtoToDoDetectDateNextUpdateChange() throws MetamacException {
        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME);
        // We need to retrieve the datasetVersion because it has special data specified
        DatasetVersionDto dto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), source.getSiemacMetadataStatisticalResource().getUrn());

        String newNextUpdate = "2022-17-09"; 
        dto.setDateNextUpdate(newNextUpdate);

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        Assert.assertTrue(entity.getUserModifiedDateNextUpdate());
        Assert.assertEquals(newNextUpdate, entity.getDateNextUpdate());
    }

    @Test
    @MetamacMock({DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME})
    public void testDatasetDtoToDoDetectDateNextUpdateChangeNewInSource() throws MetamacException {
        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME);
        // We need to retrieve the datasetVersion because it has special data specified
        DatasetVersionDto dto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), source.getSiemacMetadataStatisticalResource().getUrn());

        String newDateNextUpdate = "2022-17-09"; 
        dto.setDateNextUpdate(newDateNextUpdate);

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        Assert.assertTrue(entity.getUserModifiedDateNextUpdate());
        Assert.assertEquals(entity.getDateNextUpdate(), newDateNextUpdate);
    }

    @Test
    @MetamacMock({DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME})
    public void testDatasetDtoToDoNoDateNextUpdateChange() throws MetamacException {

        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME);
                
        // We need to retrieve the datasetVersion because it has special data specified
        DatasetVersionDto dto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), source.getSiemacMetadataStatisticalResource().getUrn());

        String originalDateNextUpdate = source.getDateNextUpdate();

        dto.setDateNextUpdate(originalDateNextUpdate);

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        Assert.assertFalse(entity.getUserModifiedDateNextUpdate());
        Assert.assertEquals(originalDateNextUpdate, entity.getDateNextUpdate());
    }

    @Test
    @MetamacMock({DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME})
    public void testDatasetDtoToDoDetectDateNextUpdateChangeButDontUpdatesBecauseNextVersionTypeAlsoChange() throws MetamacException {
        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME);
        // We need to retrieve the datasetVersion because it has special data specified
        DatasetVersionDto dto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), source.getSiemacMetadataStatisticalResource().getUrn());

        dto.setDateNextUpdate("2022-17-09");
        dto.setNextVersion(NextVersionTypeEnum.NO_UPDATES);

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertEquals(NextVersionTypeEnum.NO_UPDATES, entity.getSiemacMetadataStatisticalResource().getNextVersion());
        Assert.assertFalse(entity.getUserModifiedDateNextUpdate());
        assertNull(entity.getDateNextUpdate());
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, DATASET_VERSION_77_NO_PUB_REPLACES_DATASET_78_NAME})
    public void testDatasetDtoToDoCanNotReplaceADatasetVersionThatHasBeenReplacedAlready() throws MetamacException {
        DatasetVersion datasetVersionAlreadyReplaced = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_78_PUB_IS_REPLACED_BY_DATASET_77_NAME);
        DatasetVersion datasetVersionThatReplaces = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_77_NO_PUB_REPLACES_DATASET_78_NAME);

        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        System.out.println(datasetVersionThatReplaces.getId());
        System.out.println(source.getId());
        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(source);

        dto.setReplaces(StatisticalResourcesDtoMocks.mockNotPersistedRelatedResourceDatasetVersionDto(datasetVersionAlreadyReplaced));

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_ALREADY_BEEN_REPLACED_BY_OTHER_DATASET_VERSION, source.getSiemacMetadataStatisticalResource().getUrn(),
                datasetVersionAlreadyReplaced.getSiemacMetadataStatisticalResource().getUrn()));

        datasetDto2DoMapper.datasetVersionDtoToDo(dto);
    }

    @Test
    @MetamacMock({DATASET_VERSION_77_NO_PUB_REPLACES_DATASET_78_NAME})
    public void testDatasetDtoToDoReplaceADatasetVersionThatHasBeenReplacedAlreadyByThisDataset() throws MetamacException {
        DatasetVersion datasetVersionAlreadyReplaced = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_78_PUB_IS_REPLACED_BY_DATASET_77_NAME);

        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_77_NO_PUB_REPLACES_DATASET_78_NAME);
        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(source);

        dto.setReplaces(StatisticalResourcesDtoMocks.mockNotPersistedRelatedResourceDatasetVersionDto(datasetVersionAlreadyReplaced));

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);
        assertNotNull(entity.getSiemacMetadataStatisticalResource().getReplaces());
        DatasetVersion replacedDataset = entity.getSiemacMetadataStatisticalResource().getReplaces().getDatasetVersion();
        assertEquals(datasetVersionAlreadyReplaced.getSiemacMetadataStatisticalResource().getUrn(), replacedDataset.getSiemacMetadataStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, DATASET_VERSION_02_BASIC_NAME})
    public void testDatasetDtoToDoCanReplaceADatasetVersionThatHasNotBeenReplacedYet() throws MetamacException {
        DatasetVersion datasetVersionNotReplacedYet = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_02_BASIC_NAME);

        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(source);

        dto.setReplaces(StatisticalResourcesDtoMocks.mockNotPersistedRelatedResourceDatasetVersionDto(datasetVersionNotReplacedYet));

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertNotNull(entity.getSiemacMetadataStatisticalResource().getReplaces());
        DatasetVersion replacedDataset = entity.getSiemacMetadataStatisticalResource().getReplaces().getDatasetVersion();
        DatasetsAsserts.assertEqualsDatasetVersion(datasetVersionNotReplacedYet, replacedDataset);
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME})
    public void testDatasetDtoToDoReplacesItself() throws MetamacException {
        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(source);

        dto.setReplaces(StatisticalResourcesDtoMocks.mockNotPersistedRelatedResourceDatasetVersionDto(source));

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_CANT_REPLACE_ITSELF, source.getSiemacMetadataStatisticalResource().getUrn()));

        datasetDto2DoMapper.datasetVersionDtoToDo(dto);
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, STATISTIC_OFFICIALITY_02_BASIC_NAME})
    public void testDatasetDtoToDoUpdatesStatisticOfficiality() throws MetamacException {
        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(source);

        StatisticOfficiality statisticOfficiality = statisticOfficialityMockFactory.retrieveMock(STATISTIC_OFFICIALITY_02_BASIC_NAME);
        dto.setStatisticOfficiality(createStatisticOfficialityDtoFromDo(statisticOfficiality));

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);
        assertEquals(statisticOfficiality.getId(), entity.getStatisticOfficiality().getId());
        assertEquals(statisticOfficiality.getIdentifier(), entity.getStatisticOfficiality().getIdentifier());
    }

    @Test
    @MetamacMock({DATASET_VERSION_100_WITH_STATISTIC_OFFICIALITY_NAME, STATISTIC_OFFICIALITY_02_BASIC_NAME})
    public void testDatasetDtoToDoUpdatesStatisticOfficialityForDatasetThatAlreadyHaveStatisticOfficiality() throws MetamacException {
        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_100_WITH_STATISTIC_OFFICIALITY_NAME);
        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(source);

        StatisticOfficiality statisticOfficiality = statisticOfficialityMockFactory.retrieveMock(STATISTIC_OFFICIALITY_02_BASIC_NAME);
        dto.setStatisticOfficiality(createStatisticOfficialityDtoFromDo(statisticOfficiality));

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);
        assertEquals(statisticOfficiality.getId(), entity.getStatisticOfficiality().getId());
        assertEquals(statisticOfficiality.getIdentifier(), entity.getStatisticOfficiality().getIdentifier());
    }

    @Test
    @MetamacMock(DATASET_VERSION_62_DRAFT_NOT_INITIAL_VERSION_NAME)
    public void testDatasetDtoToDoErrorCantChangeKeepAllDataNotInitialVersionDataset() throws MetamacException {
        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_62_DRAFT_NOT_INITIAL_VERSION_NAME);
        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(source);

        dto.setKeepAllData(!source.isKeepAllData());

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.DATASET_VERSION_CANT_ALTER_KEEP_ALL_DATA).build());

        datasetDto2DoMapper.datasetVersionDtoToDo(dto);
    }

    @Test
    @MetamacMock(DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION_NAME)
    public void testDatasetDtoToDoErrorCantChangeKeepAllDataPublishedDataset() throws MetamacException {
        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION_NAME);
        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(source);

        dto.setKeepAllData(!source.isKeepAllData());

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.DATASET_VERSION_CANT_ALTER_KEEP_ALL_DATA).build());

        datasetDto2DoMapper.datasetVersionDtoToDo(dto);
    }

    @Test
    @MetamacMock(DATASET_VERSION_107_DRAFT_NOT_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME)
    public void testDatasetDtoToDoErrorCantChangeDataSourceTypeNotInitialVersion() throws MetamacException {
        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_107_DRAFT_NOT_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME);
        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(source);

        assertTrue(CollectionUtils.isEmpty(source.getDatasources()));
        assertFalse(StatisticalResourcesVersionSharedUtils.isInitialVersion(source.getSiemacMetadataStatisticalResource().getVersionLogic()));
        assertFalse(ProcStatusEnum.PUBLISHED.equals(source.getSiemacMetadataStatisticalResource().getProcStatus()));

        dto.setDataSourceType(DataSourceTypeEnum.FILE.equals(source.getDataSourceType()) ? DataSourceTypeEnum.DATABASE : DataSourceTypeEnum.FILE);

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.DATASET_VERSION_CANT_ALTER_DATA_SOURCE_TYPE).build());

        datasetDto2DoMapper.datasetVersionDtoToDo(dto);
    }

    @Test
    @MetamacMock(DATASET_VERSION_57_DRAFT_INITIAL_VERSION_NAME)
    public void testDatasetDtoToDoErrorCantChangeDataSourceTypeDatasourcesConfigured() throws MetamacException {
        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_57_DRAFT_INITIAL_VERSION_NAME);
        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(source);

        assertFalse(CollectionUtils.isEmpty(source.getDatasources()));
        assertTrue(StatisticalResourcesVersionSharedUtils.isInitialVersion(source.getSiemacMetadataStatisticalResource().getVersionLogic()));
        assertFalse(ProcStatusEnum.PUBLISHED.equals(source.getSiemacMetadataStatisticalResource().getProcStatus()));

        dto.setDataSourceType(DataSourceTypeEnum.FILE.equals(source.getDataSourceType()) ? DataSourceTypeEnum.DATABASE : DataSourceTypeEnum.FILE);

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.DATASET_VERSION_CANT_ALTER_DATA_SOURCE_TYPE).build());

        datasetDto2DoMapper.datasetVersionDtoToDo(dto);
    }

    @Test
    @MetamacMock(DATASET_VERSION_108_PUBLISHED_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME)
    public void testDatasetDtoToDoErrorCantChangeDataSourceTypePublishedDataset() throws MetamacException {
        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_108_PUBLISHED_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME);
        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(source);

        assertTrue(CollectionUtils.isEmpty(source.getDatasources()));
        assertTrue(StatisticalResourcesVersionSharedUtils.isInitialVersion(source.getSiemacMetadataStatisticalResource().getVersionLogic()));
        assertTrue(ProcStatusEnum.PUBLISHED.equals(source.getSiemacMetadataStatisticalResource().getProcStatus()));

        dto.setDataSourceType(DataSourceTypeEnum.FILE.equals(source.getDataSourceType()) ? DataSourceTypeEnum.DATABASE : DataSourceTypeEnum.FILE);

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.DATASET_VERSION_CANT_ALTER_DATA_SOURCE_TYPE).build());

        datasetDto2DoMapper.datasetVersionDtoToDo(dto);
    }

    @Test
    @MetamacMock({DATASET_VERSION_57_DRAFT_INITIAL_VERSION_NAME, DATASET_VERSION_58_PRODUCTION_VALIDATION_INITIAL_VERSION_NAME, DATASET_VERSION_59_DIFFUSION_VALIDATION_INITIAL_VERSION_NAME,
            DATASET_VERSION_60_VALIDATION_REJECTED_INITIAL_VERSION_NAME})
    public void testDatasetDtoToDoCanChangeKeepAllData() throws MetamacException {
        // Draft - Initial version
        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_57_DRAFT_INITIAL_VERSION_NAME);
        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(source);

        dto.setKeepAllData(!source.isKeepAllData());

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertEquals(!source.isKeepAllData(), entity.isKeepAllData());

        // Production validation - Initial version
        source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_58_PRODUCTION_VALIDATION_INITIAL_VERSION_NAME);
        dto = buildDatasetVersionDtoFromDo(source);

        dto.setKeepAllData(!source.isKeepAllData());

        entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertEquals(!source.isKeepAllData(), entity.isKeepAllData());

        // Diffusion validation - Initial version
        source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_59_DIFFUSION_VALIDATION_INITIAL_VERSION_NAME);
        dto = buildDatasetVersionDtoFromDo(source);

        dto.setKeepAllData(!source.isKeepAllData());

        entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertEquals(!source.isKeepAllData(), entity.isKeepAllData());

        // Validation rejected - Initial version
        source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_60_VALIDATION_REJECTED_INITIAL_VERSION_NAME);
        dto = buildDatasetVersionDtoFromDo(source);

        dto.setKeepAllData(!source.isKeepAllData());

        entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertEquals(!source.isKeepAllData(), entity.isKeepAllData());
    }

    @Test
    @MetamacMock({DATASET_VERSION_109_DRAFT_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME, DATASET_VERSION_110_PRODUCTION_VALIDATION_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME,
            DATASET_VERSION_111_DIFFUSION_VALIDATION_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME, DATASET_VERSION_112_VALIDATION_REJECTED_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME})
    public void testDatasetDtoToDoCanChangeDataSourceType() throws MetamacException {
        // Draft - Initial version
        DatasetVersion source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_109_DRAFT_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME);
        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(source);

        assertEquals(DataSourceTypeEnum.FILE, source.getDataSourceType());

        dto.setDataSourceType(DataSourceTypeEnum.DATABASE);

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertEquals(DataSourceTypeEnum.DATABASE, entity.getDataSourceType());

        // Production validation - Initial version
        source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_110_PRODUCTION_VALIDATION_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME);
        dto = buildDatasetVersionDtoFromDo(source);

        assertEquals(DataSourceTypeEnum.FILE, source.getDataSourceType());

        dto.setDataSourceType(DataSourceTypeEnum.DATABASE);

        entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertEquals(DataSourceTypeEnum.DATABASE, entity.getDataSourceType());

        // Diffusion validation - Initial version
        source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_111_DIFFUSION_VALIDATION_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME);
        dto = buildDatasetVersionDtoFromDo(source);

        assertEquals(DataSourceTypeEnum.FILE, source.getDataSourceType());

        dto.setDataSourceType(DataSourceTypeEnum.DATABASE);

        entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertEquals(DataSourceTypeEnum.DATABASE, entity.getDataSourceType());

        // Validation rejected - Initial version
        source = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_112_VALIDATION_REJECTED_INITIAL_VERSION_WITHOUT_DATASOURCES_NAME);
        dto = buildDatasetVersionDtoFromDo(source);

        assertEquals(DataSourceTypeEnum.FILE, source.getDataSourceType());

        dto.setDataSourceType(DataSourceTypeEnum.DATABASE);

        entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertEquals(DataSourceTypeEnum.DATABASE, entity.getDataSourceType());
    }

    @Test
    @MetamacMock({DATASET_VERSION_57_DRAFT_INITIAL_VERSION_NAME, DATASET_VERSION_58_PRODUCTION_VALIDATION_INITIAL_VERSION_NAME, DATASET_VERSION_59_DIFFUSION_VALIDATION_INITIAL_VERSION_NAME,
            DATASET_VERSION_60_VALIDATION_REJECTED_INITIAL_VERSION_NAME, DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION_NAME})
    public void testChangeDsdByOtherVersionInInitialVersion() throws MetamacException {

        checkCanChangeDsdByOtherVersion(DATASET_VERSION_57_DRAFT_INITIAL_VERSION_NAME);

        checkCanNotChangeDsdByOtherVersion(DATASET_VERSION_58_PRODUCTION_VALIDATION_INITIAL_VERSION_NAME);

        checkCanNotChangeDsdByOtherVersion(DATASET_VERSION_59_DIFFUSION_VALIDATION_INITIAL_VERSION_NAME);

        checkCanChangeDsdByOtherVersion(DATASET_VERSION_60_VALIDATION_REJECTED_INITIAL_VERSION_NAME);

        checkCanNotChangeDsdByOtherVersion(DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION_NAME);
    }

    @Test
    @MetamacMock({DATASET_VERSION_57_DRAFT_INITIAL_VERSION_NAME, DATASET_VERSION_58_PRODUCTION_VALIDATION_INITIAL_VERSION_NAME, DATASET_VERSION_59_DIFFUSION_VALIDATION_INITIAL_VERSION_NAME,
            DATASET_VERSION_60_VALIDATION_REJECTED_INITIAL_VERSION_NAME, DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION_NAME})
    public void testChangeDsdByOtherInInitialVersion() throws MetamacException {

        checkCanChangeDsdByOther(DATASET_VERSION_57_DRAFT_INITIAL_VERSION_NAME);

        checkCanNotChangeDsdByOther(DATASET_VERSION_58_PRODUCTION_VALIDATION_INITIAL_VERSION_NAME);

        checkCanNotChangeDsdByOther(DATASET_VERSION_59_DIFFUSION_VALIDATION_INITIAL_VERSION_NAME);

        checkCanChangeDsdByOther(DATASET_VERSION_60_VALIDATION_REJECTED_INITIAL_VERSION_NAME);

        checkCanNotChangeDsdByOther(DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION_NAME);
    }

    @Test
    @MetamacMock({DATASET_VERSION_62_DRAFT_NOT_INITIAL_VERSION_NAME, DATASET_VERSION_63_PRODUCTION_VALIDATION_NOT_INITIAL_VERSION_NAME,
            DATASET_VERSION_64_DIFFUSION_VALIDATION_NOT_INITIAL_VERSION_NAME, DATASET_VERSION_65_VALIDATION_REJECTED_NOT_INITIAL_VERSION_NAME, DATASET_VERSION_66_PUBLISHED_NOT_INITIAL_VERSION_NAME})
    public void testChangeDsdByOtherVersionNotInInitialVersion() throws MetamacException {

        checkCanChangeDsdByOtherVersion(DATASET_VERSION_62_DRAFT_NOT_INITIAL_VERSION_NAME);

        checkCanNotChangeDsdByOtherVersion(DATASET_VERSION_63_PRODUCTION_VALIDATION_NOT_INITIAL_VERSION_NAME);

        checkCanNotChangeDsdByOtherVersion(DATASET_VERSION_64_DIFFUSION_VALIDATION_NOT_INITIAL_VERSION_NAME);

        checkCanChangeDsdByOtherVersion(DATASET_VERSION_65_VALIDATION_REJECTED_NOT_INITIAL_VERSION_NAME);

        checkCanNotChangeDsdByOtherVersion(DATASET_VERSION_66_PUBLISHED_NOT_INITIAL_VERSION_NAME);
    }

    @Test
    @MetamacMock({DATASET_VERSION_62_DRAFT_NOT_INITIAL_VERSION_NAME, DATASET_VERSION_63_PRODUCTION_VALIDATION_NOT_INITIAL_VERSION_NAME,
            DATASET_VERSION_64_DIFFUSION_VALIDATION_NOT_INITIAL_VERSION_NAME, DATASET_VERSION_65_VALIDATION_REJECTED_NOT_INITIAL_VERSION_NAME, DATASET_VERSION_66_PUBLISHED_NOT_INITIAL_VERSION_NAME})
    public void testChangeDsdByOtherNotInInitialVersion() throws MetamacException {

        checkCanNotChangeDsdByOther(DATASET_VERSION_62_DRAFT_NOT_INITIAL_VERSION_NAME);

        checkCanNotChangeDsdByOther(DATASET_VERSION_63_PRODUCTION_VALIDATION_NOT_INITIAL_VERSION_NAME);

        checkCanNotChangeDsdByOther(DATASET_VERSION_64_DIFFUSION_VALIDATION_NOT_INITIAL_VERSION_NAME);

        checkCanNotChangeDsdByOther(DATASET_VERSION_65_VALIDATION_REJECTED_NOT_INITIAL_VERSION_NAME);

        checkCanNotChangeDsdByOther(DATASET_VERSION_66_PUBLISHED_NOT_INITIAL_VERSION_NAME);
    }

    @Test
    public void testCategorisationDtoToDo() throws MetamacException {
        CategorisationDto dto = StatisticalResourcesDtoMocks.mockCategorisationDto();
        Categorisation entity = datasetDto2DoMapper.categorisationDtoToDo(dto);
        assertEqualsCategorisation(dto, entity);
    }

    @Test
    @MetamacMock({CATEGORISATION_01_DATASET_VERSION_01_NAME})
    public void testCategorisationDtoToDoErrorUpdateUnsupported() throws MetamacException {
        Categorisation categorisation = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_01_NAME);
        CategorisationDto dto = new CategorisationDto();
        dto.setId(categorisation.getId());
        expectedMetamacException(new MetamacException(ServiceExceptionType.UNKNOWN, "Categorisation can not be updated"));
        datasetDto2DoMapper.categorisationDtoToDo(dto);
    }

    @Test
    @MetamacMock(STATISTIC_OFFICIALITY_01_BASIC_NAME)
    public void testDatasetDtoToDoCheckDataSourceTypeAndVersionableAttributes() throws MetamacException {
        StatisticOfficiality officiality = statisticOfficialityMockFactory.retrieveMock(STATISTIC_OFFICIALITY_01_BASIC_NAME);
        DatasetVersionDto dto = StatisticalResourcesDtoMocks.mockDatasetVersionDto(officiality);
        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);
        assertEquals(dto.getDataSourceType(), entity.getDataSourceType());

        dto.setDataSourceType(DataSourceTypeEnum.DATABASE);
        entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);
        assertEquals(DataSourceTypeEnum.DATABASE, entity.getDataSourceType());

        dto.setDataSourceType(DataSourceTypeEnum.FILE);
        entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);
        assertEquals(DataSourceTypeEnum.FILE, entity.getDataSourceType());
    }

    private void checkCanChangeDsdByOtherVersion(String datasetVersionMockName) throws MetamacException {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(datasetVersionMockName);

        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(datasetVersion);
        dto.setRelatedDsd(generateOtherDsdVersion(datasetVersion.getRelatedDsd()));

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertEquals(dto.getRelatedDsd().getUrn(), entity.getRelatedDsd().getUrn());
    }

    private void checkCanNotChangeDsdByOtherVersion(String datasetVersionMockName) throws MetamacException {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(datasetVersionMockName);

        String oldDsdUrn = datasetVersion.getRelatedDsd().getUrn();

        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(datasetVersion);
        dto.setRelatedDsd(generateOtherDsdVersion(datasetVersion.getRelatedDsd()));

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertEquals(oldDsdUrn, entity.getRelatedDsd().getUrn());
    }

    private void checkCanChangeDsdByOther(String datasetVersionMockName) throws MetamacException {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(datasetVersionMockName);

        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(datasetVersion);
        dto.setRelatedDsd(generateOtherDsd(datasetVersion.getRelatedDsd()));

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertEquals(dto.getRelatedDsd().getUrn(), entity.getRelatedDsd().getUrn());
    }

    private void checkCanNotChangeDsdByOther(String datasetVersionMockName) throws MetamacException {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(datasetVersionMockName);

        String oldDsdUrn = datasetVersion.getRelatedDsd().getUrn();

        DatasetVersionDto dto = buildDatasetVersionDtoFromDo(datasetVersion);
        dto.setRelatedDsd(generateOtherDsd(datasetVersion.getRelatedDsd()));

        DatasetVersion entity = datasetDto2DoMapper.datasetVersionDtoToDo(dto);

        assertEquals(oldDsdUrn, entity.getRelatedDsd().getUrn());
    }

    private DatasetVersionDto buildDatasetVersionDtoFromDo(DatasetVersion datasetVersion) {
        DatasetVersionDto dto = StatisticalResourcesDtoMocks.mockDatasetVersionDto(datasetVersion.getStatisticOfficiality());
        dto.setCode(datasetVersion.getSiemacMetadataStatisticalResource().getCode());
        dto.setUrn(datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        dto.setOptimisticLockingVersion(datasetVersion.getVersion());
        dto.setId(datasetVersion.getId());
        return dto;
    }

    private ExternalItemDto generateOtherDsdVersion(ExternalItem relatedDsd) {
        ExternalItemDto dsd = StatisticalResourcesDtoMocks.mockDsdExternalItemDto();
        dsd.setCode(relatedDsd.getCode());
        dsd.setUrn(StatisticalResourcesDtoMocks.mockDsdUrn(relatedDsd.getCode(), "02.035"));
        assertFalse(dsd.getUrn().equals(relatedDsd.getUrn()));
        return dsd;
    }

    private ExternalItemDto generateOtherDsd(ExternalItem relatedDsd) {
        ExternalItemDto dsd = StatisticalResourcesDtoMocks.mockDsdExternalItemDto();
        String newCode = relatedDsd.getCode() + "_new";
        dsd.setCode(newCode);
        dsd.setUrn(StatisticalResourcesDtoMocks.mockDsdUrn(newCode, VersionUtil.PATTERN_XX_YYY_INITIAL_VERSION));
        assertFalse(dsd.getUrn().equals(relatedDsd.getUrn()));
        return dsd;
    }
}
