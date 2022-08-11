package org.siemac.metamac.statistical.resources.core.io.serviceimpl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ExceptionHelper;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.statistical.resources.core.enume.task.domain.DatasetFileFormatEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.service.NoticesRestInternalService;
import org.siemac.metamac.statistical.resources.core.task.domain.AlternativeEnumeratedRepresentation;
import org.siemac.metamac.statistical.resources.core.task.domain.FileDescriptor;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoDataset;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskServiceFacade;
import org.siemac.metamac.statistical.resources.core.task.utils.JobUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractImportDatasetJob implements Job {

    protected final Logger             logger                            = LoggerFactory.getLogger(getClass());

    public static final String         USER                              = "user";
    public static final String         FILE_PATHS                        = "filePaths";
    public static final String         FILE_NAMES                        = "fileNames";
    public static final String         FILE_FORMATS                      = "fileFormats";
    public static final String         DATA_STRUCTURE_URN                = "dataStructureUrn";
    public static final String         DATASET_URN                       = "datasetUrn";
    public static final String         DATASET_VERSION_ID                = "datasetVersionId";
    public static final String         ALTERNATIVE_REPRESENTATIONS       = "alternativeRepresentations";
    public static final String         STORE_ALTERNATIVE_REPRESENTATIONS = "storeAlternativeRepresentations";
    public static final String         DATASET_VERSION_RATIONALE_TYPES   = "datasetVersionRationaleTypes";
    public static final String         DATASET_NEXT_VERSION              = "datasetNextVersion";
    public static final String         DATASET_NEXT_VERSION_DATE         = "datasetNextVersionDate";
    public static final String         DATASET_AUTOMATIC_LIFE_CICLE      = "datasetAutomaticLifeCicle";
    public static final String         TASK_NAME                         = "taskName";

    private TaskServiceFacade          taskServiceFacade                 = null;
    private NoticesRestInternalService noticesRestInternalService        = null;
    private JobDataMap                 data                              = null;
    protected ServiceContext           serviceContext                    = null;

    protected abstract ServiceContext setAdditionalProperties(ServiceContext serviceContext, JobDataMap jobDataMap);
    protected abstract void sendSuccessNotification(String fileNames, String user);
    protected abstract void sendErrorNotification(MetamacException metamacException);
    protected abstract void executeImportTask(ServiceContext serviceContext, String jobName, TaskInfoDataset taskInfoDataset) throws MetamacException;
    protected abstract void processImportJobError(String taskName, String fileNames, MetamacException metamacException);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDetail jobDetail = context.getJobDetail();

        JobKey jobKey = jobDetail.getKey();

        // Parameters
        data = jobDetail.getJobDataMap();
        String dataStructureUrn = data.getString(DATA_STRUCTURE_URN);
        String filePaths = data.getString(FILE_PATHS);
        String fileNames = data.getString(FILE_NAMES);
        String fileFormats = data.getString(FILE_FORMATS);
        String datasetUrn = data.getString(DATASET_URN);
        String datasetVersionId = data.getString(DATASET_VERSION_ID);
        String alternativeRepresentations = data.getString(ALTERNATIVE_REPRESENTATIONS);
        Boolean storeAlternativeRepresentations = data.getBoolean(STORE_ALTERNATIVE_REPRESENTATIONS);
        String taskName = data.getString(TASK_NAME);
        String user = data.getString(USER);
        String datasetVersionRationaleTypes = data.getString(DATASET_VERSION_RATIONALE_TYPES); 
        String datasetNextVersion = data.getString(DATASET_NEXT_VERSION);
        String datasetNextVersionDate = data.getString(DATASET_NEXT_VERSION_DATE);
        Boolean datasetAutomaticLifeCicle = data.getBoolean(DATASET_AUTOMATIC_LIFE_CICLE);

        try {
            logger.info("Importation job: {} starting at {}", jobKey, new Date());

            serviceContext = new ServiceContext(user, context.getFireInstanceId(), "statistical-resources-core");
            serviceContext = setAdditionalProperties(serviceContext, data);

            TaskInfoDataset taskInfoDataset = new TaskInfoDataset();
            taskInfoDataset.setDataStructureUrn(dataStructureUrn);
            taskInfoDataset.getFiles().addAll(inflateFileDescriptors(filePaths, fileNames, fileFormats));
            taskInfoDataset.setDatasetUrn(datasetUrn);
            taskInfoDataset.setDatasetVersionId(datasetVersionId);
            taskInfoDataset.getAlternativeRepresentations().addAll(inflateAlternativeRepresentations(alternativeRepresentations));
            taskInfoDataset.setStoreAlternativeRepresentations(storeAlternativeRepresentations);
            taskInfoDataset.setDatasetVersionRationaleTypes(inflateDatasetVersionRationaleTypes(datasetVersionRationaleTypes));
            taskInfoDataset.setDatasetNextVersion(datasetNextVersion);
            taskInfoDataset.setDatasetNextVersionDate(datasetNextVersionDate);
            taskInfoDataset.setDatasetAutomaticLifeCicle(datasetAutomaticLifeCicle);
            

            executeImportTask(serviceContext, taskName, taskInfoDataset);

            logger.info("Importation job: {} finished at {}", jobKey, new Date());

            sendSuccessNotification(fileNames, user);

        } catch (UnsupportedEncodingException e) {
            logger.error("The importation with key {} has failed due to an unsupported encoding", jobKey.getName(), e);
            MetamacException metamacException = MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(ExceptionHelper.excMessage(e))
                    .build();

            processImportJobError(taskName, fileNames, metamacException);
        } catch (MetamacException e) {
            logger.error("The importation with key {} has failed", jobKey.getName(), e);
            processImportJobError(taskName, fileNames, e);
        } catch (Exception e) {
            logger.error("Unexpected error in the importation with key {}", jobKey.getName(), e);
            MetamacException metamacException = MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(ExceptionHelper.excMessage(e))
                    .build();
            processImportJobError(taskName, fileNames, metamacException);
        }
    }

    protected TaskServiceFacade getTaskServiceFacade() {
        if (taskServiceFacade == null) {
            taskServiceFacade = (TaskServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TaskServiceFacade.BEAN_ID);
        }

        return taskServiceFacade;
    }

    protected NoticesRestInternalService getNoticesRestInternalService() {
        if (noticesRestInternalService == null) {
            noticesRestInternalService = (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
        }
        return noticesRestInternalService;
    }

    protected JobDataMap getData() {
        return data;
    }

    private List<FileDescriptor> inflateFileDescriptors(String filePaths, String fileNames, String fileFormats) throws UnsupportedEncodingException {
        List<FileDescriptor> fileDescriptorDtos = new LinkedList<>();
        String[] files = filePaths.split("\\" + JobUtil.SERIALIZATION_SEPARATOR);
        String[] names = fileNames.split("\\" + JobUtil.SERIALIZATION_SEPARATOR);
        String[] formats = fileFormats.split("\\" + JobUtil.SERIALIZATION_SEPARATOR);

        for (int i = 0; i < files.length; i++) {
            FileDescriptor fileDescriptorDto = new FileDescriptor();
            fileDescriptorDto.setDatasetFileFormatEnum(DatasetFileFormatEnum.valueOf(formats[i]));
            String encoding = StringUtils.isEmpty(System.getProperty("file.encoding")) ? "UTF-8" : System.getProperty("file.encoding");
            fileDescriptorDto.setFileName(URLDecoder.decode(names[i], encoding));
            fileDescriptorDto.setFile(new File(URLDecoder.decode(files[i], encoding)));
            fileDescriptorDtos.add(fileDescriptorDto);
        }

        return fileDescriptorDtos;
    }

    private List<AlternativeEnumeratedRepresentation> inflateAlternativeRepresentations(String alternativeRepresentations) {
        List<AlternativeEnumeratedRepresentation> alternativeRepresentationList = new ArrayList<AlternativeEnumeratedRepresentation>();
        if (!StringUtils.isEmpty(alternativeRepresentations)) {
            String[] pairs = alternativeRepresentations.split("\\" + JobUtil.SERIALIZATION_SEPARATOR);

            for (int i = 0; i < pairs.length; i++) {
                String[] items = pairs[i].split(JobUtil.SERIALIZATION_PAIR_SEPARATOR);
                AlternativeEnumeratedRepresentation alternativeEnumeratedRepresentation = new AlternativeEnumeratedRepresentation();
                alternativeEnumeratedRepresentation.setComponentId(items[0]);
                alternativeEnumeratedRepresentation.setUrn(items[1]);
                alternativeRepresentationList.add(alternativeEnumeratedRepresentation);
            }
        }

        return alternativeRepresentationList;
    }
    
    private List<String> inflateDatasetVersionRationaleTypes(String datasetVersionRationaleTypes) {
        List<String> datasetVersionRationaleTypesList = new ArrayList<String>();
        if (!StringUtils.isEmpty(datasetVersionRationaleTypes)) {
            String[] pairs = datasetVersionRationaleTypes.split("\\" + JobUtil.SERIALIZATION_SEPARATOR);

            for (int i = 0; i < pairs.length; i++) {
                datasetVersionRationaleTypesList.add(pairs[i]);
            }
        }

        return datasetVersionRationaleTypesList;
    }
}
