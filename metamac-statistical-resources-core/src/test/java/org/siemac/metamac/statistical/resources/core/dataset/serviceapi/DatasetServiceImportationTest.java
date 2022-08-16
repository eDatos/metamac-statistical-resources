package org.siemac.metamac.statistical.resources.core.dataset.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_34_FOR_IMPORT_IN_OPERATION_0001_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_35_FOR_IMPORT_IN_OPERATION_0001_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_36_FOR_IMPORT_IN_OPERATION_0002_NAME;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.MetamacCollectionUtils;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dataset.serviceapi.validators.DatasetServiceInvocationValidator;
import org.siemac.metamac.statistical.resources.core.dataset.serviceimpl.DatasetServiceImpl;
import org.siemac.metamac.statistical.resources.core.dto.BasicVersionableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DataSourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.task.domain.DatasetFileFormatEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.task.domain.AlternativeEnumeratedRepresentation;
import org.siemac.metamac.statistical.resources.core.task.domain.FileDescriptor;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoDataset;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.siemac.metamac.statistical.resources.core.utils.TaskInfoPredicateByDatasetId;

public class DatasetServiceImportationTest extends StatisticalResourcesBaseTest {

    private static final String               DATASET_MOCK_URN = "MOCK_URN";

    @InjectMocks
    private DatasetService                    datasetService   = new DatasetServiceImpl();

    @Mock
    private DatasetServiceInvocationValidator datasetServiceInvocationValidator;

    @Mock
    private DatasetVersionRepository          datasetVersionRepository;

    @Mock
    private DatasetRepository                 datasetRepository;

    @Mock
    private TaskService                       taskService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testImportDatasourcesInDatasetVersion() throws Exception {
        String filename = "prueba.px";
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME);
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        Mockito.when(datasetVersionRepository.retrieveByUrn(Mockito.eq(datasetVersionUrn))).thenReturn(datasetVersion);
        Mockito.when(datasetRepository.findDatasetUrnLinkedToDatasourceSourceName(filename)).thenReturn(null);

        List<URL> urls = Arrays.asList(buildFileUrl(filename));
        Map<String, String> mappings = new HashMap<String, String>();
        mappings.put("dimension01", "urn01");
        mappings.put("dimension02", "urn02");
        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), datasetVersionUrn, urls, mappings, false, new BasicVersionableStatisticalResourceDto());

        ArgumentCaptor<TaskInfoDataset> argument = ArgumentCaptor.forClass(TaskInfoDataset.class);
        verify(taskService).planifyImportationDataset(any(ServiceContext.class), argument.capture());

        assertTaskInfoFile(datasetVersion, 1, argument.getValue(), mappings);
        assertTaskFile(filename, DatasetFileFormatEnum.PX, argument.getValue().getFiles().get(0));
    }

    @Test
    public void testImportDatasourcesInDatasetVersionWithAutomaticLifeCicle() throws Exception {
        String filename = "prueba.zip";
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME);
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        Mockito.when(datasetVersionRepository.retrieveByUrn(Mockito.eq(datasetVersionUrn))).thenReturn(datasetVersion);
        Mockito.when(datasetRepository.findDatasetUrnLinkedToDatasourceSourceName(filename)).thenReturn(null);

        List<URL> urls = Arrays.asList(buildFileUrl(filename));
        Map<String, String> mappings = new HashMap<String, String>();
        
        BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto = new BasicVersionableStatisticalResourceDto();
        Date date = new Date();
        basicVersionableStatisticalResourceDto.setAutomaticLifeCicle(true);
        List<String> versionRationaleTypes = new ArrayList<>();
        versionRationaleTypes.add(VersionRationaleTypeEnum.MINOR_DATA_UPDATE.getName());
        basicVersionableStatisticalResourceDto.setVersionRationaleTypes(versionRationaleTypes);
        basicVersionableStatisticalResourceDto.setNextVersion(NextVersionTypeEnum.SCHEDULED_UPDATE.getName());
        basicVersionableStatisticalResourceDto.setNextVersionDate(convertDateToString("12/02/2022"));
        basicVersionableStatisticalResourceDto.setNextUpdateDate(convertDateToString("12/02/2022"));
        
        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), datasetVersionUrn, urls, mappings, false, basicVersionableStatisticalResourceDto);

        ArgumentCaptor<TaskInfoDataset> argument = ArgumentCaptor.forClass(TaskInfoDataset.class);
        verify(taskService).planifyImportationDataset(any(ServiceContext.class), argument.capture());
        
        assertTaskInfoZipFileWithAutomaticLifeCicle(datasetVersion, argument.getValue(), basicVersionableStatisticalResourceDto);
        assertTaskFile(filename, DatasetFileFormatEnum.CSV, argument.getValue().getFiles().get(0));
    }
    
    @Test
    public void testImportDatasourcesInDatasetVersionInvalidFile() throws Exception {
        String filename = "prueba.px";
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME);
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        Mockito.when(datasetVersionRepository.retrieveByUrn(Mockito.eq(datasetVersionUrn))).thenReturn(datasetVersion);
        Mockito.when(datasetRepository.findDatasetUrnLinkedToDatasourceSourceName(filename)).thenReturn(DATASET_MOCK_URN);

        expectedMetamacException(new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.INVALID_FILE_FOR_DATASET_VERSION, "prueba.px", datasetVersionUrn))));

        List<URL> urls = Arrays.asList(buildFileUrl(filename));
        HashMap<String, String> mappings = new HashMap<String, String>();
        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), datasetVersionUrn, urls, mappings, false, new BasicVersionableStatisticalResourceDto());
    }

    @Test
    public void testImportDatasourcesInDatasetVersionInvalidSourceType() throws Exception {
        String filename = "prueba.px";

        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME);
        datasetVersion.setDataSourceType(DataSourceTypeEnum.DATABASE);

        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        Mockito.when(datasetVersionRepository.retrieveByUrn(Mockito.eq(datasetVersionUrn))).thenReturn(datasetVersion);
        Mockito.when(datasetRepository.findDatasetUrnLinkedToDatasourceSourceName(filename)).thenReturn(DATASET_MOCK_URN);

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.INVALID_DATA_SOURCE_TYPE_FOR_DATASET_DATA_IMPORTATION)
                .withMessageParameters(DataSourceTypeEnum.DATABASE, DataSourceTypeEnum.FILE).build());

        List<URL> urls = Arrays.asList(buildFileUrl(filename));
        HashMap<String, String> mappings = new HashMap<String, String>();
        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), datasetVersionUrn, urls, mappings, false, new BasicVersionableStatisticalResourceDto());
    }

    @Test
    public void testImportDatasourcesInStatisticalOperation() throws Exception {
        String filename = "prueba.px";
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME);
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();
        String statisticalOperationCode = datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        Mockito.when(datasetVersionRepository.retrieveByUrn(Mockito.eq(datasetVersionUrn))).thenReturn(datasetVersion);
        Mockito.when(datasetRepository.findDatasetUrnLinkedToDatasourceSourceName(filename)).thenReturn(datasetUrn);
        Mockito.when(datasetVersionRepository.retrieveLastVersion(Mockito.eq(datasetUrn))).thenReturn(datasetVersion);

        List<URL> urls = Arrays.asList(buildFileUrl(filename));
        datasetService.importDatasourcesInStatisticalOperation(getServiceContextWithoutPrincipal(), statisticalOperationCode, urls, new BasicVersionableStatisticalResourceDto());

        ArgumentCaptor<TaskInfoDataset> argument = ArgumentCaptor.forClass(TaskInfoDataset.class);
        verify(taskService).planifyImportationDataset(any(ServiceContext.class), argument.capture());

        HashMap<String, String> mappings = new HashMap<String, String>();
        assertTaskInfoFile(datasetVersion, 1, argument.getValue(), mappings);
        assertTaskFile(filename, DatasetFileFormatEnum.PX, argument.getValue().getFiles().get(0));
    }

    @Test
    public void testImportDatasourcesInStatisticalOperationMultiple() throws Exception {
        String filename01 = "prueba01.px";
        String filename02 = "prueba02.px";
        String statisticalOperationCode = null;
        DatasetVersion datasetVersion01 = null;
        DatasetVersion datasetVersion02 = null;
        {
            DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_34_FOR_IMPORT_IN_OPERATION_0001_NAME);
            String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
            String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();
            statisticalOperationCode = datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
            datasetVersion01 = datasetVersion;

            Mockito.when(datasetVersionRepository.retrieveByUrn(Mockito.eq(datasetVersionUrn))).thenReturn(datasetVersion);
            Mockito.when(datasetRepository.findDatasetUrnLinkedToDatasourceSourceName(filename01)).thenReturn(datasetUrn);
            Mockito.when(datasetVersionRepository.retrieveLastVersion(Mockito.eq(datasetUrn))).thenReturn(datasetVersion);
        }
        {
            DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_35_FOR_IMPORT_IN_OPERATION_0001_NAME);
            String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
            String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();
            datasetVersion02 = datasetVersion;

            Mockito.when(datasetVersionRepository.retrieveByUrn(Mockito.eq(datasetVersionUrn))).thenReturn(datasetVersion);
            Mockito.when(datasetRepository.findDatasetUrnLinkedToDatasourceSourceName(filename02)).thenReturn(datasetUrn);
            Mockito.when(datasetVersionRepository.retrieveLastVersion(Mockito.eq(datasetUrn))).thenReturn(datasetVersion);
        }

        List<URL> urls = Arrays.asList(buildFileUrl(filename01), buildFileUrl(filename02));
        datasetService.importDatasourcesInStatisticalOperation(getServiceContextWithoutPrincipal(), statisticalOperationCode, urls, new BasicVersionableStatisticalResourceDto());

        ArgumentCaptor<TaskInfoDataset> argument = ArgumentCaptor.forClass(TaskInfoDataset.class);
        verify(taskService, Mockito.times(2)).planifyImportationDataset(any(ServiceContext.class), argument.capture());

        HashMap<String, String> mappings = new HashMap<String, String>();

        TaskInfoDataset taskDataset01 = MetamacCollectionUtils.find(argument.getAllValues(), new TaskInfoPredicateByDatasetId(datasetVersion01.getSiemacMetadataStatisticalResource().getUrn()));
        assertTaskInfoFile(datasetVersion01, 1, taskDataset01, mappings);
        assertTaskFile(filename01, DatasetFileFormatEnum.PX, taskDataset01.getFiles().get(0));

        TaskInfoDataset taskDataset02 = MetamacCollectionUtils.find(argument.getAllValues(), new TaskInfoPredicateByDatasetId(datasetVersion02.getSiemacMetadataStatisticalResource().getUrn()));
        assertTaskInfoFile(datasetVersion02, 1, taskDataset02, mappings);
        assertTaskFile(filename02, DatasetFileFormatEnum.PX, taskDataset02.getFiles().get(0));
    }

    @Test
    public void testImportDatasourcesInStatisticalOperationFilesNotEverMappedToAnyDataset() throws Exception {
        String filename01 = "prueba01.px";
        String filename02 = "prueba02.px";
        String statisticalOperationCode = null;
        {
            DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_34_FOR_IMPORT_IN_OPERATION_0001_NAME);
            String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
            String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();
            statisticalOperationCode = datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

            Mockito.when(datasetVersionRepository.retrieveByUrn(Mockito.eq(datasetVersionUrn))).thenReturn(datasetVersion);
            Mockito.when(datasetRepository.findDatasetUrnLinkedToDatasourceSourceName(filename01)).thenReturn(null);
            Mockito.when(datasetVersionRepository.retrieveLastVersion(Mockito.eq(datasetUrn))).thenReturn(datasetVersion);
        }
        {
            DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_35_FOR_IMPORT_IN_OPERATION_0001_NAME);
            String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
            String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();

            Mockito.when(datasetVersionRepository.retrieveByUrn(Mockito.eq(datasetVersionUrn))).thenReturn(datasetVersion);
            Mockito.when(datasetRepository.findDatasetUrnLinkedToDatasourceSourceName(filename02)).thenReturn(datasetUrn);
            Mockito.when(datasetVersionRepository.retrieveLastVersion(Mockito.eq(datasetUrn))).thenReturn(datasetVersion);
        }

        expectedMetamacException(new MetamacException(ServiceExceptionType.FILE_NOT_LINKED_TO_ANY_DATASET_IN_STATISTICAL_OPERATION, filename01, statisticalOperationCode));

        List<URL> urls = Arrays.asList(buildFileUrl(filename01), buildFileUrl(filename02));
        datasetService.importDatasourcesInStatisticalOperation(getServiceContextWithoutPrincipal(), statisticalOperationCode, urls, new BasicVersionableStatisticalResourceDto());

    }

    @Test
    public void testImportDatasourcesInStatisticalOperationFileAlreadyLinkedInOtherStatisticalOperation() throws Exception {
        String filename01 = "prueba01.px";
        String filename02 = "prueba02.px";
        String statisticalOperationCode = null;
        {
            DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_34_FOR_IMPORT_IN_OPERATION_0001_NAME);
            String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
            String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();
            statisticalOperationCode = datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

            Mockito.when(datasetVersionRepository.retrieveByUrn(Mockito.eq(datasetVersionUrn))).thenReturn(datasetVersion);
            Mockito.when(datasetRepository.findDatasetUrnLinkedToDatasourceSourceName(filename01)).thenReturn(datasetUrn);
            Mockito.when(datasetVersionRepository.retrieveLastVersion(Mockito.eq(datasetUrn))).thenReturn(datasetVersion);
        }
        {
            DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_36_FOR_IMPORT_IN_OPERATION_0002_NAME);
            String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
            String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();

            Mockito.when(datasetVersionRepository.retrieveByUrn(Mockito.eq(datasetVersionUrn))).thenReturn(datasetVersion);
            Mockito.when(datasetRepository.findDatasetUrnLinkedToDatasourceSourceName(filename02)).thenReturn(datasetUrn);
            Mockito.when(datasetVersionRepository.retrieveLastVersion(Mockito.eq(datasetUrn))).thenReturn(datasetVersion);
        }

        expectedMetamacException(new MetamacException(ServiceExceptionType.FILE_NOT_LINKED_TO_ANY_DATASET_IN_STATISTICAL_OPERATION, filename02, statisticalOperationCode));

        List<URL> urls = Arrays.asList(buildFileUrl(filename01), buildFileUrl(filename02));
        datasetService.importDatasourcesInStatisticalOperation(getServiceContextWithoutPrincipal(), statisticalOperationCode, urls, new BasicVersionableStatisticalResourceDto());

    }

    private void assertTaskInfoFile(DatasetVersion datasetVersion, int numFiles, TaskInfoDataset taskInfo, Map<String, String> mappings) {
        assertEquals(datasetVersion.getRelatedDsd().getUrn(), taskInfo.getDataStructureUrn());
        assertEquals(datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), taskInfo.getDatasetVersionId());
        assertEquals(numFiles, taskInfo.getFiles().size());
        assertEquals(mappings.size(), taskInfo.getAlternativeRepresentations().size());
        Collections.sort(taskInfo.getAlternativeRepresentations(), new Comparator<AlternativeEnumeratedRepresentation>() {

            @Override
            public int compare(AlternativeEnumeratedRepresentation o1, AlternativeEnumeratedRepresentation o2) {
                return o1.getComponentId().compareTo(o2.getComponentId());
            }
        });

        List<String> dimensions = new ArrayList<String>(mappings.keySet());
        Collections.sort(dimensions);

        for (int i = 0; i < dimensions.size(); i++) {
            String dimensionId = dimensions.get(i);
            assertEquals(dimensionId, taskInfo.getAlternativeRepresentations().get(i).getComponentId());
            assertEquals(mappings.get(dimensionId), taskInfo.getAlternativeRepresentations().get(i).getUrn());
        }

    }
    
       
    private void assertTaskFile(String filename, DatasetFileFormatEnum type, FileDescriptor fileDescriptor) {
        assertEquals(filename, fileDescriptor.getFileName());
        assertEquals(type, fileDescriptor.getDatasetFileFormatEnum());
        assertNotNull(fileDescriptor.getDatasetFileFormatEnum());
    }

    private URL buildFileUrl(String filename) throws Exception {
        return new URL("file", null, filename);
    }
    
    private void assertTaskInfoZipFileWithAutomaticLifeCicle(DatasetVersion datasetVersion, TaskInfoDataset taskInfo, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto) {
        assertEquals(datasetVersion.getRelatedDsd().getUrn(), taskInfo.getDataStructureUrn());
        assertEquals(datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), taskInfo.getDatasetVersionId());
        assertEquals(1, taskInfo.getFiles().size());

        assertEquals(basicVersionableStatisticalResourceDto.getVersionRationaleTypes(), taskInfo.getDatasetVersionRationaleTypes());
        assertEquals(basicVersionableStatisticalResourceDto.getNextVersion(), taskInfo.getDatasetNextVersion());
        assertEquals(basicVersionableStatisticalResourceDto.getNextVersionDate(), taskInfo.getDatasetNextVersionDate());
        assertEquals(basicVersionableStatisticalResourceDto.getNextUpdateDate(), taskInfo.getDatasetNextUpdateDate());

    }

    private String convertDateToString(String date) {
        DateFormat df = new SimpleDateFormat(date);

        Date today = Calendar.getInstance().getTime();
        String dateToString = df.format(today);

        return (dateToString);
    }

}
