package org.siemac.metamac.statistical.resources.core.invocation.service;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.utils.TranslateExceptions;
import org.siemac.metamac.core.common.lang.LocaleUtil;
import org.siemac.metamac.rest.notices.v1_0.domain.Message;
import org.siemac.metamac.rest.notices.v1_0.domain.Notice;
import org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacApplicationsEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacRolesEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.MessageBuilder;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.NoticeBuilder;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
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

    @Override
    public ExternalItem buildExternalItemFromResourceInternal(org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal resource) throws MetamacException {
        return this.restMapper.buildExternalItemFromResourceInternal(resource);
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

    private void createBackgroundNotification(String actionCode, String message, String user) throws MetamacException {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            String subject = LocaleUtil.getMessageForCode(actionCode, locale);
            String sendingApp = MetamacApplicationsEnum.GESTOR_RECURSOS_ESTADISTICOS.getName();

            // @formatter:off
            sendNotice(NoticeBuilder.notification()
                    .withMessagesWithoutResources(message)
                    .withSendingApplication(sendingApp)
                    .withReceivers(user)
                    .withSendingUser(user)
                    .withSubject(subject)
                    .build());
            // @formatter:on
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
}
