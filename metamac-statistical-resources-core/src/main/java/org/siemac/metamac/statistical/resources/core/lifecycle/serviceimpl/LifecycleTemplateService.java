package org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.HasLifecycle;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.base.validators.ProcStatusValidator;
import org.siemac.metamac.statistical.resources.core.common.utils.RelatedResourceUtils;
import org.siemac.metamac.statistical.resources.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.service.NoticesRestInternalService;
import org.siemac.metamac.statistical.resources.core.lifecycle.LifecycleChecker;
import org.siemac.metamac.statistical.resources.core.lifecycle.LifecycleFiller;
import org.siemac.metamac.statistical.resources.core.lifecycle.SiemacLifecycleChecker;
import org.siemac.metamac.statistical.resources.core.lifecycle.SiemacLifecycleFiller;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceapi.LifecycleInvocationValidatorBase;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceapi.LifecycleService;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeAction;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeMessage;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.stream.serviceapi.StreamMessagingServiceFacade;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class LifecycleTemplateService<E extends Object> implements LifecycleService<E> {

    private static final String            ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_SENDING_TO_PUBLISHED             = "Found an unknown resource type sending to published";

    private static final String            ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_SENDING_TO_VALIDATION_REJECTED   = "Found an unknown resource type sending to validation rejected";

    private static final String            ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_SENDING_TO_DIFFUSION_VALIDATION  = "Found an unknown resource type sending to diffusion validation";

    private static final String            ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_SENDING_TO_PRODUCTION_VALIDATION = "Found an unknown resource type sending to production validation";

    private static final String            ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_FOR_VERSIONING                   = "Found an unknown resource type for versioning";

    @Autowired
    LifecycleInvocationValidatorBase       lifecycleInvocationValidatorBase;

    @Autowired
    private SiemacLifecycleChecker         siemacLifecycleChecker;

    @Autowired
    private SiemacLifecycleFiller          siemacLifecycleFiller;

    @Autowired
    private LifecycleChecker               lifecycleChecker;

    @Autowired
    private LifecycleFiller                lifecycleFiller;

    @Autowired
    private TaskService                    taskService;

    @Autowired
    protected StreamMessagingServiceFacade streamMessagingServiceFacade;

    @Autowired
    private NoticesRestInternalService     noticesRestInternalService;

    // ------------------------------------------------------------------------------------------------------
    // >> PRODUCTION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Override
    public E sendToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        getInvocationValidator().checkSendToProductionValidation(ctx, urn);

        E resource = retrieveResourceByUrn(urn);

        checkSendToProductionValidation(ctx, resource);

        applySendToProductionValidation(ctx, resource);

        return saveResource(resource);
    }

    protected void checkSendToProductionValidation(ServiceContext ctx, E resource) throws MetamacException {
        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        checkNotTasksInProgress(ctx, resource);
        ProcStatusValidator.checkStatisticalResourceCanSendToProductionValidation((HasLifecycle) resource);
        checkSendToProductionValidationLinkedStatisticalResource(resource, exceptions);

        checkResourceMetadataAllActions(ctx, resource, exceptions);
        checkSendToProductionValidationResource(resource, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    protected void applySendToProductionValidation(ServiceContext ctx, E resource) throws MetamacException {
        applySendToProductionValidationLinkedStatisticalResource(ctx, resource);

        applySendToProductionValidationResource(ctx, resource);
    }

    protected void checkSendToProductionValidationLinkedStatisticalResource(E resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        if (resource instanceof HasSiemacMetadata) {
            siemacLifecycleChecker.checkSendToProductionValidation((HasSiemacMetadata) resource, getResourceMetadataName(), exceptionItems);
        } else if (resource instanceof HasLifecycle) {
            lifecycleChecker.checkSendToProductionValidation((HasLifecycle) resource, getResourceMetadataName(), exceptionItems);
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_SENDING_TO_PRODUCTION_VALIDATION);
        }
    }

    protected void applySendToProductionValidationLinkedStatisticalResource(ServiceContext ctx, E resource) throws MetamacException {
        if (resource instanceof HasSiemacMetadata) {
            siemacLifecycleFiller.applySendToProductionValidationActions(ctx, (HasSiemacMetadata) resource);
        } else if (resource instanceof HasLifecycle) {
            lifecycleFiller.applySendToProductionValidationActions(ctx, (HasLifecycle) resource);
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_SENDING_TO_PRODUCTION_VALIDATION);
        }
    }

    protected abstract void checkSendToProductionValidationResource(E resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException;

    protected abstract void applySendToProductionValidationResource(ServiceContext ctx, E resource) throws MetamacException;

    // ------------------------------------------------------------------------------------------------------
    // >> DIFFUSION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Override
    public E sendToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        getInvocationValidator().checkSendToDiffusionValidation(ctx, urn);

        E resource = retrieveResourceByUrn(urn);

        checkSendToDiffusionValidation(ctx, resource);

        applySendToDiffusionValidation(ctx, resource);

        return saveResource(resource);
    }

    protected void checkSendToDiffusionValidation(ServiceContext ctx, E resource) throws MetamacException {
        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        checkNotTasksInProgress(ctx, resource);
        ProcStatusValidator.checkStatisticalResourceCanSendToDiffusionValidation((HasLifecycle) resource);
        checkSendToDiffusionValidationLinkedStatisticalResource(resource, exceptions);

        checkResourceMetadataAllActions(ctx, resource, exceptions);
        checkSendToDiffusionValidationResource(resource, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    protected void applySendToDiffusionValidation(ServiceContext ctx, E resource) throws MetamacException {
        applySendToDiffusionValidationLinkedStatisticalResource(ctx, resource);

        applySendToDiffusionValidationResource(ctx, resource);
    }

    protected void checkSendToDiffusionValidationLinkedStatisticalResource(E resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        if (resource instanceof HasSiemacMetadata) {
            siemacLifecycleChecker.checkSendToDiffusionValidation((HasSiemacMetadata) resource, getResourceMetadataName(), exceptionItems);
        } else if (resource instanceof HasLifecycle) {
            lifecycleChecker.checkSendToDiffusionValidation((HasLifecycle) resource, getResourceMetadataName(), exceptionItems);
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_SENDING_TO_DIFFUSION_VALIDATION);
        }
    }

    protected void applySendToDiffusionValidationLinkedStatisticalResource(ServiceContext ctx, E resource) throws MetamacException {
        if (resource instanceof HasSiemacMetadata) {
            siemacLifecycleFiller.applySendToDiffusionValidationActions(ctx, (HasSiemacMetadata) resource);
        } else if (resource instanceof HasLifecycle) {
            lifecycleFiller.applySendToDiffusionValidationActions(ctx, (HasLifecycle) resource);
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_SENDING_TO_DIFFUSION_VALIDATION);
        }
    }

    protected abstract void checkSendToDiffusionValidationResource(E resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException;

    protected abstract void applySendToDiffusionValidationResource(ServiceContext ctx, E resource) throws MetamacException;

    // ------------------------------------------------------------------------------------------------------
    // >> VALIDATION REJECTED
    // ------------------------------------------------------------------------------------------------------

    @Override
    public E sendToValidationRejected(ServiceContext ctx, String urn) throws MetamacException {
        getInvocationValidator().checkSendToValidationRejected(ctx, urn);

        E resource = retrieveResourceByUrn(urn);

        checkSendToValidationRejected(ctx, resource);

        applySendToValidationRejected(ctx, resource);

        return saveResource(resource);
    }

    protected void checkSendToValidationRejected(ServiceContext ctx, E resource) throws MetamacException {
        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        checkNotTasksInProgress(ctx, resource);
        ProcStatusValidator.checkStatisticalResourceCanSendToValidationRejected((HasLifecycle) resource);
        checkSendToValidationRejectedLinkedStatisticalResource(resource, exceptions);

        checkResourceMetadataAllActions(ctx, resource, exceptions);
        checkSendToValidationRejectedResource(resource, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    protected void applySendToValidationRejected(ServiceContext ctx, E resource) throws MetamacException {
        applySendToValidationRejectedLinkedStatisticalResource(ctx, resource);

        applySendToValidationRejectedResource(ctx, resource);
    }

    protected void checkSendToValidationRejectedLinkedStatisticalResource(E resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        if (resource instanceof HasSiemacMetadata) {
            siemacLifecycleChecker.checkSendToValidationRejected((HasSiemacMetadata) resource, getResourceMetadataName(), exceptionItems);
        } else if (resource instanceof HasLifecycle) {
            lifecycleChecker.checkSendToValidationRejected((HasLifecycle) resource, getResourceMetadataName(), exceptionItems);
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_SENDING_TO_VALIDATION_REJECTED);
        }
    }

    protected void applySendToValidationRejectedLinkedStatisticalResource(ServiceContext ctx, E resource) throws MetamacException {
        if (resource instanceof HasSiemacMetadata) {
            siemacLifecycleFiller.applySendToValidationRejectedActions(ctx, (HasSiemacMetadata) resource);
        } else if (resource instanceof HasLifecycle) {
            lifecycleFiller.applySendToValidationRejectedActions(ctx, (HasLifecycle) resource);
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_SENDING_TO_VALIDATION_REJECTED);
        }
    }

    protected abstract void checkSendToValidationRejectedResource(E resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException;

    protected abstract void applySendToValidationRejectedResource(ServiceContext ctx, E resource) throws MetamacException;

    // ------------------------------------------------------------------------------------------------------
    // >> PUBLISHED
    // ------------------------------------------------------------------------------------------------------

    @Override
    public E sendToPublished(ServiceContext ctx, String urn) throws MetamacException {
        getInvocationValidator().checkSendToPublished(ctx, urn);

        E resource = retrieveResourceByUrn(urn);
        E previousResource = retrievePreviousPublishedResourceByResource(resource);

        checkSendToPublished(ctx, resource, previousResource);

        applySendToPublishedCurrentVersion(ctx, resource, previousResource);
        resource = saveResource(resource);

        if (previousResource != null) {
            applySendToPublishedPreviousVersion(ctx, resource, previousResource);
            saveResource(previousResource);
        }

        sendNewVersionPublishedStreamMessageByResource(ctx, resource);

        return retrieveResourceByResource(resource);
    }

    protected void checkSendToPublished(ServiceContext ctx, E resource, E previousResource) throws MetamacException {
        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        checkNotTasksInProgress(ctx, resource);
        ProcStatusValidator.checkStatisticalResourceCanSendToPublish((HasLifecycle) resource);
        checkSendToPublishedLinkedStatisticalResource(resource, previousResource, exceptions);

        checkResourceMetadataAllActions(ctx, resource, exceptions);
        checkSendToPublishedResource(ctx, resource, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    protected void applySendToPublishedCurrentVersion(ServiceContext ctx, E resource, E previousResource) throws MetamacException {
        applySendToPublishedCurrentLinkedStatisticalResource(ctx, resource, previousResource);
        applySendToPublishedCurrentResource(ctx, resource, previousResource);
    }

    protected void applySendToPublishedPreviousVersion(ServiceContext ctx, E resource, E previousResource) throws MetamacException {
        applySendToPublishedPreviousLinkedStatisticalResource(ctx, resource, previousResource);
        applySendToPublishedPreviousResource(ctx, previousResource);
    }

    protected void checkSendToPublishedLinkedStatisticalResource(E resource, E previousResource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        if (resource instanceof HasSiemacMetadata) {
            siemacLifecycleChecker.checkSendToPublished((HasSiemacMetadata) resource, (HasSiemacMetadata) previousResource, getResourceMetadataName(), exceptionItems);
        } else if (resource instanceof HasLifecycle) {
            lifecycleChecker.checkSendToPublished((HasLifecycle) resource, (HasLifecycle) previousResource, getResourceMetadataName(), exceptionItems);
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_SENDING_TO_PUBLISHED);
        }
    }

    protected void applySendToPublishedCurrentLinkedStatisticalResource(ServiceContext ctx, E resource, E previousResource) throws MetamacException {
        if (resource instanceof HasSiemacMetadata) {
            siemacLifecycleFiller.applySendToPublishedCurrentResourceActions(ctx, (HasSiemacMetadata) resource, (HasSiemacMetadata) previousResource);
        } else if (resource instanceof HasLifecycle) {
            lifecycleFiller.applySendToPublishedCurrentResourceActions(ctx, (HasLifecycle) resource, (HasLifecycle) previousResource);
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_SENDING_TO_PUBLISHED);
        }
    }

    protected void applySendToPublishedPreviousLinkedStatisticalResource(ServiceContext ctx, E resource, E previousResource) throws MetamacException {
        if (resource instanceof HasSiemacMetadata) {
            siemacLifecycleFiller.applySendToPublishedPreviousResourceActions(ctx, (HasSiemacMetadata) resource, (HasSiemacMetadata) previousResource,
                    RelatedResourceUtils.createRelatedResourceForHasLifecycleResource((HasSiemacMetadata) resource));
        } else if (resource instanceof HasLifecycle) {
            lifecycleFiller.applySendToPublishedPreviousResourceActions(ctx, (HasLifecycle) resource, (HasLifecycle) previousResource,
                    RelatedResourceUtils.createRelatedResourceForHasLifecycleResource((HasLifecycle) resource));
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_SENDING_TO_PUBLISHED);
        }
    }

    protected abstract void checkSendToPublishedResource(ServiceContext ctx, E resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException;

    protected abstract void applySendToPublishedCurrentResource(ServiceContext ctx, E resource, E previousResource) throws MetamacException;

    protected abstract void applySendToPublishedPreviousResource(ServiceContext ctx, E previousResource) throws MetamacException;

    // ------------------------------------------------------------------------------------------------------
    // >> VERSIONING
    // ------------------------------------------------------------------------------------------------------

    @Override
    public E versioning(ServiceContext ctx, String urn, VersionTypeEnum versionType) throws MetamacException {
        getInvocationValidator().checkVersioning(ctx, urn);

        E previousResource = retrieveResourceByUrn(urn);

        checkVersioning(ctx, previousResource);
        E resource = copyResourceForVersioning(ctx, previousResource);

        applyVersioningNewResource(ctx, resource, previousResource, versionType);
        resource = saveResource(resource);

        applyVersioningPreviousResource(ctx, resource, previousResource, versionType);
        previousResource = saveResource(previousResource);

        return retrieveResourceByResource(resource);
    }

    protected void checkVersioning(ServiceContext ctx, E resource) throws MetamacException {
        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        checkNotTasksInProgress(ctx, resource);
        ProcStatusValidator.checkStatisticalResourceCanSendToVersion((HasLifecycle) resource);
        checkVersioningLinkedStatisticalResource(resource, exceptions);

        checkResourceMetadataAllActions(ctx, resource, exceptions);
        checkVersioningResource(resource, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    protected void applyVersioningNewResource(ServiceContext ctx, E resource, E previousResource, VersionTypeEnum versionType) throws MetamacException {
        applyVersioningLinkedStatisticalNewResource(ctx, resource, previousResource, versionType);

        resource = updateResourceUrn(resource);

        applyVersioningNewResource(ctx, resource, previousResource);
    }

    protected void applyVersioningPreviousResource(ServiceContext ctx, E resource, E previousResource, VersionTypeEnum versionType) throws MetamacException {
        applyVersioningLinkedStatisticalPreviousResource(ctx, resource, previousResource, versionType);

        applyVersioningPreviousResource(ctx, previousResource);
    }

    protected void checkVersioningLinkedStatisticalResource(E resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        if (resource instanceof HasSiemacMetadata) {
            siemacLifecycleChecker.checkVersioning((HasSiemacMetadata) resource, getResourceMetadataName(), exceptionItems);
        } else if (resource instanceof HasLifecycle) {
            lifecycleChecker.checkVersioning((HasLifecycle) resource, getResourceMetadataName(), exceptionItems);
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_FOR_VERSIONING);
        }
    }

    protected void applyVersioningLinkedStatisticalNewResource(ServiceContext ctx, E resource, E previousResource, VersionTypeEnum versionType) throws MetamacException {
        if (resource instanceof HasSiemacMetadata) {
            siemacLifecycleFiller.applyVersioningNewResourceActions(ctx, (HasSiemacMetadata) resource, (HasSiemacMetadata) previousResource, versionType);
        } else if (resource instanceof HasLifecycle) {
            lifecycleFiller.applyVersioningNewResourceActions(ctx, (HasLifecycle) resource, (HasLifecycle) previousResource, versionType);
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_FOR_VERSIONING);
        }
    }

    protected void applyVersioningLinkedStatisticalPreviousResource(ServiceContext ctx, E resource, E previousResource, VersionTypeEnum versionType) throws MetamacException {
        if (resource instanceof HasSiemacMetadata) {
            siemacLifecycleFiller.applyVersioningPreviousResourceActions(ctx, (HasSiemacMetadata) resource, (HasSiemacMetadata) previousResource, versionType);
        } else if (resource instanceof HasLifecycle) {
            lifecycleFiller.applyVersioningPreviousResourceActions(ctx, (HasLifecycle) resource, (HasLifecycle) previousResource, versionType);
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, ERROR_FOUND_AN_UNKNOWN_RESOURCE_TYPE_FOR_VERSIONING);
        }
    }

    protected abstract void checkVersioningResource(E resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException;

    protected abstract E copyResourceForVersioning(ServiceContext ctx, E previousResource) throws MetamacException;

    protected abstract void applyVersioningNewResource(ServiceContext ctx, E resource, E previousResource) throws MetamacException;

    protected abstract void applyVersioningPreviousResource(ServiceContext ctx, E resource) throws MetamacException;

    protected abstract E updateResourceUrn(E resource) throws MetamacException;

    // ------------------------------------------------------------------------------------------------------
    // GLOBAL METHODS
    // ------------------------------------------------------------------------------------------------------

    private void checkNotTasksInProgress(ServiceContext ctx, E resource) throws MetamacException {
        if (taskService.existsTaskForResource(ctx, getResourceUrn(resource))) {
            throw new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, getResourceUrn(resource));
        }
    }

    // This method provides a valid implementation of InvocationValidator based on resource Type
    protected LifecycleInvocationValidatorBase getInvocationValidator() {
        return lifecycleInvocationValidatorBase;
    }

    // This method knows how to retrieve a resource given the resource URN
    protected abstract E retrieveResourceByUrn(String urn) throws MetamacException;

    // This method knows how to retrieve a resource given the resource
    protected abstract E retrieveResourceByResource(E resource) throws MetamacException;

    // This method knows how to retrieve a previous published resource given the actual resource
    protected abstract E retrievePreviousPublishedResourceByResource(E resource) throws MetamacException;

    protected abstract void checkResourceMetadataAllActions(ServiceContext ctx, E resource, List<MetamacExceptionItem> exceptions) throws MetamacException;

    protected abstract E saveResource(E resource);

    protected abstract String getResourceMetadataName() throws MetamacException;

    protected abstract String getResourceUrn(E resource);

    protected void createStreamMessageSentNotification(ServiceContext ctx, HasSiemacMetadata version) {
        if (version.getLifeCycleStatisticalResource().getPublicationStreamStatus() != StreamMessageStatusEnum.SENT) {
            String userId = ctx.getUserId();
            String messageCode = ServiceNoticeAction.STREAM_MESSAGE_SEND;
            String messageText = ServiceNoticeMessage.STREAM_MESSAGE_SEND_ERROR;
            noticesRestInternalService.createErrorOnStreamMessagingService(userId, messageCode, version, messageText, (Serializable[]) null);
        }
    }

    protected void createStreamMessageSentNotification(ServiceContext ctx, QueryVersion version) {
        if (version.getLifeCycleStatisticalResource().getPublicationStreamStatus() != StreamMessageStatusEnum.SENT) {
            String userId = ctx.getUserId();
            String messageCode = ServiceNoticeAction.STREAM_MESSAGE_SEND;
            String messageText = ServiceNoticeMessage.STREAM_MESSAGE_SEND_ERROR;
            noticesRestInternalService.createErrorOnStreamMessagingService(userId, messageCode, version, messageText, (Serializable[]) null);
        }
    }
}
