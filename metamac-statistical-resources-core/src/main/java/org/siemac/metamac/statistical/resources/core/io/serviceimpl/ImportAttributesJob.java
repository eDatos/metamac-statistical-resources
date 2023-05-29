package org.siemac.metamac.statistical.resources.core.io.serviceimpl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourcesRoleEnum;
import org.siemac.metamac.statistical.resources.core.enume.task.domain.DatasetFileFormatEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.service.NoticesRestInternalService;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeAction;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeMessage;
import org.siemac.metamac.statistical.resources.core.task.domain.FileDescriptor;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoDataset;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskServiceFacade;
import org.siemac.metamac.statistical.resources.core.task.utils.JobUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImportAttributesJob implements Job {

    private final Logger               logger                     = LoggerFactory.getLogger(getClass());

    public static final String         FILE_PATHS                 = "filePaths";
    public static final String         FILE_NAMES                 = "fileNames";
    public static final String         FILE_FORMATS               = "fileFormats";
    public static final String         DATASET_URN                = "datasetUrn";
    public static final String         DATASET_VERSION_ID         = "datasetVersionId";
    public static final String         DATA_STRUCTURE_URN         = "dataStructureUrn";
    public static final String         TASK_NAME                  = "taskName";
    public static final String         USER                       = "user";

    private TaskServiceFacade          taskServiceFacade          = null;
    private NoticesRestInternalService noticesRestInternalService = null;
    private JobDataMap                 data                       = null;
    private ServiceContext             serviceContext             = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();

        JobKey jobKey = jobDetail.getKey();
        data = jobDetail.getJobDataMap();
        String filePaths = data.getString(FILE_PATHS);
        String fileFormats = data.getString(FILE_FORMATS);
        String fileNames = data.getString(FILE_NAMES);
        String datasetUrn = data.getString(DATASET_URN);
        String datasetVersionId = data.getString(DATASET_VERSION_ID);
        String user = data.getString(USER);
        String taskName = data.getString(TASK_NAME);
        String dataStructureUrn = data.getString(DATA_STRUCTURE_URN);
        serviceContext = new ServiceContext(user, context.getFireInstanceId(), "statistical-resources-core");
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(serviceContext.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(StatisticalResourcesRoleEnum.ADMINISTRADOR.getName(), StatisticalResourcesConstants.APPLICATION_ID, null));
        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        try {
            TaskInfoDataset taskInfoDataset = new TaskInfoDataset();
            taskInfoDataset.getFiles().addAll(inflateFileDescriptors(filePaths, fileNames, fileFormats));
            taskInfoDataset.setDatasetUrn(datasetUrn);
            taskInfoDataset.setDatasetVersionId(datasetVersionId);
            taskInfoDataset.setDataStructureUrn(dataStructureUrn);
            executeImportTask(serviceContext, datasetVersionId, taskInfoDataset, taskName);
            sendSuccessNotification(fileNames, user);
        } catch (UnsupportedEncodingException e) {
            logger.error("The importation with key {} has failed due to an unsupported encoding", jobKey.getName(), e);
            MetamacException metamacException = MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(ExceptionHelper.excMessage(e))
                    .build();

            processImportJobError(taskName, fileNames, metamacException);
        } catch (MetamacException e) {
            logger.error("The importation with key {} has failed", jobKey.getName(), e);
            processImportJobError(taskName, fileNames, e);
        }
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

    private void processImportJobError(String taskName, String fileNames, MetamacException metamacException) {
        try {
            getTaskServiceFacade().markTaskAsFailed(serviceContext, getData().getString(TASK_NAME), getData().getString(DATASET_VERSION_ID), getData().getString(DATASET_URN), metamacException);
            logger.info("{} marked as error at {}", taskName, new Date());
            metamacException.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORT_DATASET_ATTRIBUTE_JOB_ERROR, fileNames));
            sendErrorNotification(metamacException);
        } catch (MetamacException e1) {
            logger.error("The importation with key {} has failed and it can't marked as error", taskName, e1);
            metamacException.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORT_DATASET_JOB_ERROR_AND_CANT_MARK_AS_ERROR, fileNames));
            sendErrorNotification(metamacException);
        }
    }

    private TaskServiceFacade getTaskServiceFacade() {
        if (taskServiceFacade == null) {
            taskServiceFacade = (TaskServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TaskServiceFacade.BEAN_ID);
        }

        return taskServiceFacade;
    }

    private void sendErrorNotification(MetamacException metamacException) {
        String user = getData().getString(USER);
        getNoticesRestInternalService().createErrorBackgroundNotification(user, ServiceNoticeAction.IMPORT_ATTRIBUTE_JOB, metamacException);
    }

    private JobDataMap getData() {
        return data;
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        if (noticesRestInternalService == null) {
            noticesRestInternalService = (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
        }
        return noticesRestInternalService;
    }

    private void executeImportTask(ServiceContext serviceContext, String datasetVersionUrn, TaskInfoDataset taskInfoDataset, String jobName) throws MetamacException {
        getTaskServiceFacade().importAttributesInDatasetVersion(serviceContext, datasetVersionUrn, taskInfoDataset);
        getTaskServiceFacade().markTaskAsFinished(serviceContext, jobName);
    }

    private void sendSuccessNotification(String fileNames, String user) {
        getNoticesRestInternalService().createSuccessBackgroundNotification(user, ServiceNoticeAction.IMPORT_ATTRIBUTE_JOB, ServiceNoticeMessage.IMPORT_ATTRIBUTES_JOB_OK, fileNames);
    }
}
