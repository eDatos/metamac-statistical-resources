package org.siemac.metamac.statistical.resources.core.invocation.service;

import static org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum.DIFFUSION_VALIDATION;
import static org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum.PRODUCTION_VALIDATION;
import static org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum.PUBLISHED;
import static org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum.VALIDATION_REJECTED;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.utils.TranslateExceptions;
import org.siemac.metamac.core.common.lang.LocaleUtil;
import org.siemac.metamac.core.common.util.ServiceContextUtils;
import org.siemac.metamac.rest.notices.v1_0.domain.Message;
import org.siemac.metamac.rest.notices.v1_0.domain.Notice;
import org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacApplicationsEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacRolesEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.MessageBuilder;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.NoticeBuilder;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionUtils;
import org.siemac.metamac.statistical.resources.core.invocation.utils.RestMapper;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeAction;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeMessage;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.ibestat.jaxi.stream.messages.DatasetAvro;

@Component(NoticesRestInternalService.BEAN_ID)
public class NoticesRestInternalServiceImpl implements NoticesRestInternalService {

    private static final String               ERROR  = "ERROR";

    private static Logger                     logger = LoggerFactory.getLogger(NoticesRestInternalServiceImpl.class);

    @Autowired
    private MetamacApisLocator                restApiLocator;

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    @Autowired
    private TranslateExceptions               translateExceptions;

    @Autowired
    private RestMapper                        restMapper;

    
    private static Map<ProcStatusEnum, MetamacRolesEnum[]> roles;
    private static Map<ProcStatusEnum, String>             actionCodes;
    private static Map<ProcStatusEnum, String>             messageCodes;

    static {
        roles = new HashMap<ProcStatusEnum, MetamacRolesEnum[]>();
        roles.put(PRODUCTION_VALIDATION, new MetamacRolesEnum[]{MetamacRolesEnum.TECNICO_PRODUCCION});
        roles.put(DIFFUSION_VALIDATION, new MetamacRolesEnum[]{MetamacRolesEnum.TECNICO_DIFUSION, MetamacRolesEnum.TECNICO_APOYO_DIFUSION});
        roles.put(PUBLISHED, new MetamacRolesEnum[]{MetamacRolesEnum.JEFE_PRODUCCION, MetamacRolesEnum.TECNICO_PRODUCCION, MetamacRolesEnum.TECNICO_APOYO_PRODUCCION});

        actionCodes = new HashMap<ProcStatusEnum, String>();
        actionCodes.put(PRODUCTION_VALIDATION, ServiceNoticeAction.RESOURCE_SEND_PRODUCTION_VALIDATION);
        actionCodes.put(DIFFUSION_VALIDATION, ServiceNoticeAction.RESOURCE_SEND_DIFFUSION_VALIDATION);
        actionCodes.put(VALIDATION_REJECTED, ServiceNoticeAction.RESOURCE_CANCEL_VALIDATION);
        actionCodes.put(PUBLISHED, ServiceNoticeAction.RESOURCE_PUBLICATION);

        messageCodes = new HashMap<ProcStatusEnum, String>();
        messageCodes.put(PRODUCTION_VALIDATION, ServiceNoticeMessage.RESOURCE_SEND_PRODUCTION_VALIDATION_OK);
        messageCodes.put(DIFFUSION_VALIDATION, ServiceNoticeMessage.RESOURCE_SEND_DIFFUSION_VALIDATION_OK);
        messageCodes.put(VALIDATION_REJECTED, ServiceNoticeMessage.RESOURCE_CANCEL_VALIDATION_OK);
        messageCodes.put(PUBLISHED, ServiceNoticeMessage.RESOURCE_PUBLICATION_OK);
    }
    
    @Override
    public void createErrorBackgroundNotification(String user, String actionCode, MetamacException exception) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();

            Throwable localisedException = translateExceptions.translateException(locale, exception);
            String localisedMessage = localisedException.getMessage();
            localisedMessage = ERROR + " - " + localisedMessage;

            createBackgroundNotification(actionCode, localisedMessage, user);
            logger.info("Sending errorBackgroundNotification for user " + user);
        } catch (MetamacException e) {
            logger.error("Error creating createErrorBackgroundNotification:", e);
        }
    } 
    
    @Override
    public void createErrorBackgroundNotification(String actionCode, MetamacException exception) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();

            Throwable localisedException = translateExceptions.translateException(locale, exception);
            String localisedMessage = localisedException.getMessage();
            localisedMessage = ERROR + " - " + localisedMessage;

            createBackgroundNotification(actionCode, localisedMessage, null);
        } catch (MetamacException e) {
            logger.error("Error creating createErrorBackgroundNotification:", e);
        }
    } 
    
    @Override
    public void createDatabaseImportErrorBackgroundNotification(DatasetVersion datasetVersion, String actionCode, MetamacException exception) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();

            Throwable localisedException = translateExceptions.translateException(locale, exception);
            String localisedMessage = localisedException.getMessage();
            localisedMessage = ERROR + " - " + localisedMessage;

            ResourceInternal resourceInternal = restMapper.generateResourceInternal(datasetVersion);
            Message message = MessageBuilder.message().withText(localisedMessage).withResources(resourceInternal).build();

            createDatabaseImportBackgroundNotification(locale, datasetVersion, actionCode, message);
        } catch (MetamacException e) {
            logger.error("Error creating createDatabaseImportErrorBackgroundNotification:", e);
        }

    }

    @Override
    public void createSuccessBackgroundNotification(String user, String actionCode, String successMessageCode, Serializable... successMessageParameters) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            String localisedMessage = LocaleUtil.getMessageForCode(successMessageCode, locale);
            localisedMessage = MessageFormat.format(localisedMessage, successMessageParameters);
            createBackgroundNotification(actionCode, localisedMessage, user);
            logger.info("Sending successBackgroundNotification for user " + user);
        } catch (MetamacException e) {
            logger.error("Error creating createSuccessBackgroundNotification:", e);
        }
    }

    @Override
    public void createDatabaseImportSuccessBackgroundNotification(DatasetVersion datasetVersion, String actionCode, String successMessageCode, Serializable... successMessageParameters) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            ResourceInternal resourceInternal = restMapper.generateResourceInternal(datasetVersion);
            Message message = createMessage(locale, Arrays.asList(resourceInternal), successMessageCode, successMessageParameters);

            createDatabaseImportBackgroundNotification(locale, datasetVersion, actionCode, message);
        } catch (MetamacException e) {
            logger.error("Error creating createDatabaseImportSuccessBackgroundNotification:", e);
        }
    }

    @Override
    public void createErrorUpdateGeocoverageCacheBackgroundNotification(DatasetAvro jaxiDatasetVersionAvro, String actionCode, String messageCode, Serializable... messageParameters) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            Message message = createMessage(locale, messageCode, messageParameters);

            createUpdateGeocoverageCacheBackgroundNotification(locale, jaxiDatasetVersionAvro.getStatisticalOperation().getUrn(), actionCode, message);
        } catch (MetamacException e) {
            logger.error("Error creating createErrorUpdateGeocoverageCacheBackgroundNotification:", e);
        }
    }
        
    @Override
    public void createUpdateGeocoverageCacheNotification(DatasetVersion datasetVersion, String actionCode, String messageCode, Serializable... messageParameters) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            ResourceInternal resourceInternal = restMapper.generateResourceInternal(datasetVersion);
            Message message = createMessage(locale, Collections.singletonList(resourceInternal), messageCode, messageParameters);

            createUpdateGeocoverageCacheBackgroundNotification(locale, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getUrn(), actionCode, message);
        } catch (MetamacException e) {
            logger.error("Error creating createUpdateGeocoverageCacheNotification:", e);
        }
    }

    private void createUpdateGeocoverageCacheBackgroundNotification(Locale locale, String urnStatisticalOperation, String actionCode, Message message) throws MetamacException {
        try {
            String subject = LocaleUtil.getMessageForCode(actionCode, locale);
            String sendingApp = MetamacApplicationsEnum.GESTOR_RECURSOS_ESTADISTICOS.getName();

            // @formatter:off
            sendNotice(NoticeBuilder.notification()
                    .withMessages(message)
                    .withSendingApplication(sendingApp)
                    .withRoles(MetamacRolesEnum.ADMINISTRADOR)
                    .withSubject(subject)
                    .withApplications(sendingApp)
                    .withStatisticalOperations(urnStatisticalOperation)
                    .build());
            // @formatter:on
        } catch (Exception e) {
            throw manageNoticesInternalRestException(e);
        }
    }
    
    private void createBackgroundNotification(String actionCode, String message, String user) throws MetamacException {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            String subject = LocaleUtil.getMessageForCode(actionCode, locale);
            String sendingApp = MetamacApplicationsEnum.GESTOR_RECURSOS_ESTADISTICOS.getName();

            if (user != null) {
            // @formatter:off
            sendNotice(NoticeBuilder.notification()
                    .withMessagesWithoutResources(message)
                    .withSendingApplication(sendingApp)
                    .withReceivers(user)
                    .withSendingUser(user)
                    .withSubject(subject)
                    .build());
            // @formatter:on
            } else {
             // @formatter:off
                sendNotice(NoticeBuilder.notification()
                        .withMessagesWithoutResources(message)
                        .withSendingApplication(sendingApp)
                        .withSubject(subject)
                        .withRoles(MetamacRolesEnum.ADMINISTRADOR)
                        .build());
                // @formatter:on
            }

        } catch (Exception e) {
            throw manageNoticesInternalRestException(e);
        }
    }

    private void createDatabaseImportBackgroundNotification(Locale locale, DatasetVersion datasetVersion, String actionCode, Message message) throws MetamacException {
        try {
            String subject = LocaleUtil.getMessageForCode(actionCode, locale);
            String sendingApp = MetamacApplicationsEnum.GESTOR_RECURSOS_ESTADISTICOS.getName();

            // @formatter:off
            sendNotice(NoticeBuilder.notification()
                    .withMessages(message)
                    .withSendingApplication(sendingApp)
                    .withRoles(MetamacRolesEnum.ADMINISTRADOR, MetamacRolesEnum.TECNICO_PRODUCCION)
                    .withSubject(subject)
                    .withApplications(sendingApp)
                    .withStatisticalOperations(datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getUrn())
                    .build());
            // @formatter:on
        } catch (Exception e) {
            throw manageNoticesInternalRestException(e);
        }
    }

    @Override
    public void createExternalPublicationUpdateErrorBackgroundNotification(String keyMessage) {
        if (ServiceNoticeAction.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_ERROR.equals(keyMessage)) {
            createBackgroundNotification(ServiceNoticeAction.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_ERROR, ServiceNoticeMessage.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_ERROR,
                    new ArrayList<DatasetVersion>(), keyMessage);
        }
    }
    
    @Override
    public void createConsumerFromKafkaErrorBackgroundNotification(String keyMessage) {
        createBackgroundNotification(ServiceNoticeAction.RESOURCE_RECEIVED_FROM_KAFKA_ERROR, ServiceNoticeMessage.RESOURCE_RECEIVED_FROM_KAFKA_ERROR, new ArrayList<DatasetVersion>(), keyMessage);
    }
    
    private void createBackgroundNotification(String actionCode, String messageCode, List<DatasetVersion> failedDatasets, Object... messageParams) {
        createAndSendViewNotification(actionCode, messageCode, failedDatasets, messageParams);
    }
    
    @Override
    public void createErrorOnStreamMessagingService(String user, String actionCode, HasSiemacMetadata affectedResource, String errorMessageCode, Serializable... extraParameters) {
        ResourceInternal resourceInternal = restMapper.generateResourceInternal(affectedResource);
        createNotificationErrorOnStreamMessagingService(user, actionCode, resourceInternal, errorMessageCode, extraParameters);
    }

    @Override
    public void createErrorOnStreamMessagingService(String user, String actionCode, QueryVersion affectedResource, String errorMessageCode, Serializable... extraParameters) {
        ResourceInternal resourceInternal = restMapper.generateResourceInternal(affectedResource);
        createNotificationErrorOnStreamMessagingService(user, actionCode, resourceInternal, errorMessageCode, extraParameters);
    }

    private void createNotificationErrorOnStreamMessagingService(String user, String actionCode, ResourceInternal resourceInternal, String errorMessageCode, Serializable... extraParameters) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            String subject = LocaleUtil.getMessageForCode(actionCode, locale);
            String sendingApp = MetamacApplicationsEnum.GESTOR_RECURSOS_ESTADISTICOS.getName();

            List<ResourceInternal> resourceInternalList = new ArrayList<ResourceInternal>();
            resourceInternalList.add(resourceInternal);

            Message message = createMessage(locale, resourceInternalList, errorMessageCode);

            // @formatter:off
            sendNotice(NoticeBuilder.notification()
                    .withMessages(message)
                    .withSendingApplication(sendingApp)
                    .withSubject(subject)
                    .withSendingUser(user)
                    .withRoles(MetamacRolesEnum.ADMINISTRADOR)
                    .build());
            // @formatter:on
        } catch (MetamacException e) {
            logger.error("Error creating notification for error on stream messaging service");
        }
    }

    @Override
    public void createAssignRolePermissionsDatasetErrorBackgroundNotification(String dataViewsRole, String viewCode) {
        createAndSendViewNotification(ServiceNoticeAction.ASSIGN_ROLE_PERMISSIONS_DATASET_ERROR, ServiceNoticeMessage.ASSIGN_ROLE_PERMISSIONS_DATASET_ERROR, new ArrayList<DatasetVersion>(),
                dataViewsRole, viewCode);
    }

    @Override
    public void createCreateReplaceDatasetErrorBackgroundNotification(DatasetVersion datasetVersion, String viewCode, String datasetRepositoryId) {
        createAndSendViewNotification(ServiceNoticeAction.CREATE_REPLACE_DATASET_ERROR, ServiceNoticeMessage.CREATE_REPLACE_DATASET_ERROR, Arrays.asList(datasetVersion), viewCode,
                datasetRepositoryId);
    }

    private void createAndSendViewNotification(String actionCode, String messageCode, List<DatasetVersion> failedDatasetVersions, Object... messageParams) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            String subject = LocaleUtil.getMessageForCode(actionCode, locale);
            String sendingApp = MetamacApplicationsEnum.GESTOR_RECURSOS_ESTADISTICOS.getName();

            List<ResourceInternal> resources = datasetsVersionsToResourcesInternal(failedDatasetVersions);

            Message message = createMessage(locale, resources, messageCode, messageParams);

            // @formatter:off
            sendNotice(NoticeBuilder.notification()
                    .withMessages(message)
                    .withSendingApplication(sendingApp)
                    .withSubject(subject)
                    .withRoles(MetamacRolesEnum.ADMINISTRADOR)
                    .build());
            // @formatter:on
        } catch (MetamacException e) {
            logger.error("Error creating notification for error on view creation:", e);
        }
    }

    // -------------------------------------------------------------------------------------------------
    // PRIVATE UTILS
    // -------------------------------------------------------------------------------------------------

    private MetamacException manageNoticesInternalRestException(Exception e) throws MetamacException {
        return ServiceExceptionUtils.manageMetamacRestException(e, ServiceExceptionParameters.API_NOTICES_INTERNAL, restApiLocator.getNoticesRestInternalFacadeV10());
    }

    private Message createMessage(Locale locale, List<ResourceInternal> resources, String messageCode, Object... messageParams) {
        String localisedMessage = (messageParams == null) ? LocaleUtil.getMessageForCode(messageCode, locale) : getMessageForCodeWithParams(messageCode, locale, messageParams);

        // @formatter:off
        return  MessageBuilder.message()
                .withText(localisedMessage)
                .withResources(resources)
                .build();
        // @formatter:off
    }

    private Message createMessage(Locale locale, String messageCode, Object... messageParams) {
        String localisedMessage = (messageParams == null) ? LocaleUtil.getMessageForCode(messageCode, locale) : getMessageForCodeWithParams(messageCode, locale, messageParams);

        // @formatter:off
        return  MessageBuilder.message()
                .withText(localisedMessage)
                .build();
        // @formatter:off
    }
    
    private void sendNotice(Notice notice) throws MetamacException {
        restApiLocator.getNoticesRestInternalFacadeV10().createNotice(notice);
    }

    private String getMessageForCodeWithParams(String actionCode, Locale locale, Object... messageParams) {
        String localisedMessage = LocaleUtil.getMessageForCode(actionCode, locale);
        return MessageFormat.format(localisedMessage, messageParams);
    }

    private List<ResourceInternal> datasetsVersionsToResourcesInternal(List<DatasetVersion> datasetsVersions) {
        List<ResourceInternal> resources = new ArrayList<>();

        for (DatasetVersion datasetVersion : datasetsVersions) {
            resources.add(restMapper.generateResourceInternal(datasetVersion));
        }

        return resources;
    }
    
    // LifeCicle Notifications functions
    
    @Override
    public void createLifeCycleNotification(ServiceContext serviceContext, ProcStatusEnum procStatus, DatasetVersion datasetVersion) throws MetamacException {
        
        
        MetamacRolesEnum[] notificationRoles = roles.containsKey(procStatus) ? roles.get(procStatus) : null;
        createNotification(serviceContext, procStatus, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getUrn(), notificationRoles, getResourceInternalFromDataset(datasetVersion));
 
    }

    private List<ResourceInternal> getResourceInternalFromDataset(DatasetVersion datasetVersion) {
        ResourceInternal resourceInternal = restMapper.generateResourceInternal(datasetVersion);
        List<ResourceInternal> resourceInternalList = new ArrayList<ResourceInternal>();
        resourceInternalList.add(resourceInternal);
        return resourceInternalList;
    }
    
    private void createNotification(ServiceContext ctx, ProcStatusEnum procStatus, String urnStatisticalOperation, MetamacRolesEnum[] notificationRoles, List<ResourceInternal> resourcesInternal)
            throws MetamacException {

        String actionCode = getActionCode(procStatus);
        String messageCode = getMessageCode(procStatus);

        String subject = buildSubject(ctx, actionCode);
        Message message = buildMessage(ctx, messageCode, resourcesInternal);

        MetamacApplicationsEnum[] applications = {MetamacApplicationsEnum.GESTOR_RECURSOS_ESTRUCTURALES};
        NoticeBuilder noticeBuilder = NoticeBuilder.notification().withMessages(message).withSendingApplication(getSendingApp()).withSendingUser(ctx.getUserId()).withSubject(subject)
        		.withApplications(applications);
        if (notificationRoles != null) {
            noticeBuilder = noticeBuilder.withRoles(notificationRoles);
        }
        if (StringUtils.isNotBlank(urnStatisticalOperation)) {
            noticeBuilder = noticeBuilder.withStatisticalOperations(urnStatisticalOperation);
        }

        try {
            Response response = restApiLocator.getNoticesRestInternalFacadeV10().createNotice(noticeBuilder.build());
        } catch (MetamacException e) {
            logger.error("Error creating notification for error on view creation:", e);
        }
    }

    private Message buildMessage(ServiceContext ctx, String messageCode, List<ResourceInternal> resources) {
        Locale locale = ServiceContextUtils.getLocale(ctx);
        String localisedMessage = LocaleUtil.getMessageForCode(messageCode, locale);
        return MessageBuilder.message().withText(localisedMessage).withResources(resources).build();
    }

    private String buildSubject(ServiceContext ctx, String actionCode) {
        Locale locale = ServiceContextUtils.getLocale(ctx);
        return LocaleUtil.getMessageForCode(actionCode, locale);
    }

    private String getActionCode(ProcStatusEnum lifeCycleAction) {
        return actionCodes.containsKey(lifeCycleAction) ? actionCodes.get(lifeCycleAction) : StringUtils.EMPTY;
    }

    private String getMessageCode(ProcStatusEnum lifeCycleAction) {
        return messageCodes.containsKey(lifeCycleAction) ? messageCodes.get(lifeCycleAction) : StringUtils.EMPTY;
    }

    private String getSendingApp() {
        return MetamacApplicationsEnum.GESTOR_RECURSOS_ESTADISTICOS.getName();
    }
    
}
