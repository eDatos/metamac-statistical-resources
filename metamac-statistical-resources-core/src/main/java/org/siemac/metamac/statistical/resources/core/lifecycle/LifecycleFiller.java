package org.siemac.metamac.statistical.resources.core.lifecycle;

import java.util.Locale;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.lang.LocaleUtil;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.core.common.util.shared.VersionResult;
import org.siemac.metamac.statistical.resources.core.base.domain.HasLifecycle;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.utils.RelatedResourceUtils;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesVersionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LifecycleFiller {

    public static final String                  MINOR_CHANGE_EXPECTED_MAJOR_VERSION_OCCURRED_MESSAGE = "lifecycle_message.resources.version_rationale.minor_version_expected_major_version_occurred";

    @Autowired
    protected StatisticalResourcesConfiguration configurationService;

    // ------------------------------------------------------------------------------------------------------
    // >> PRODUCTION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    public void applySendToProductionValidationActions(ServiceContext ctx, HasLifecycle resource) {
        resource.getLifeCycleStatisticalResource().setProductionValidationDate(new DateTime());
        resource.getLifeCycleStatisticalResource().setProductionValidationUser(ctx.getUserId());
        resource.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.PRODUCTION_VALIDATION);
    }

    // ------------------------------------------------------------------------------------------------------
    // >> DIFFUSION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    public void applySendToDiffusionValidationActions(ServiceContext ctx, HasLifecycle resource) {
        resource.getLifeCycleStatisticalResource().setDiffusionValidationDate(new DateTime());
        resource.getLifeCycleStatisticalResource().setDiffusionValidationUser(ctx.getUserId());
        resource.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
    }

    // ------------------------------------------------------------------------------------------------------
    // VALIDATION REJECTED
    // ------------------------------------------------------------------------------------------------------

    public void applySendToValidationRejectedActions(ServiceContext ctx, HasLifecycle resource) {
        resource.getLifeCycleStatisticalResource().setRejectValidationDate(new DateTime());
        resource.getLifeCycleStatisticalResource().setRejectValidationUser(ctx.getUserId());
        resource.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.VALIDATION_REJECTED);
    }

    // ------------------------------------------------------------------------------------------------------
    // PUBLISHED
    // ------------------------------------------------------------------------------------------------------

    public void applySendToPublishedCurrentResourceActions(ServiceContext ctx, HasLifecycle resource, HasLifecycle previousVersion) throws MetamacException {
        if (!StatisticalResourcesVersionUtils.isInitialVersion(resource) && ValidationUtils.isEmpty(previousVersion)) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.PREVIOUS_VERSION);
        }
        DateTime now = new DateTime();
        DateTime pubDate = now;
        resource.getLifeCycleStatisticalResource().setPublicationDate(pubDate);
        resource.getLifeCycleStatisticalResource().setValidFrom(now);
        resource.getLifeCycleStatisticalResource().setPublicationUser(ctx.getUserId());
        resource.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.PUBLISHED);
        resource.getLifeCycleStatisticalResource().setLastUpdate(now);
    }

    public void applySendToPublishedPreviousResourceActions(ServiceContext ctx, HasLifecycle resource, HasLifecycle previousVersion, RelatedResource currentAsRelatedResource) throws MetamacException {
        DateTime publicationDate = resource.getLifeCycleStatisticalResource().getValidFrom();
        previousVersion.getLifeCycleStatisticalResource().setValidTo(publicationDate);
    }

    // ------------------------------------------------------------------------------------------------------
    // VERSIONING
    // ------------------------------------------------------------------------------------------------------

    public void applyVersioningNewResourceActions(ServiceContext ctx, HasLifecycle resource, HasLifecycle previousVersion, VersionTypeEnum versionType) throws MetamacException {
        if (ValidationUtils.isEmpty(previousVersion)) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.PREVIOUS_VERSION);
        }
        RelatedResource previousVersionRelatedResource = RelatedResourceUtils.createRelatedResourceForHasLifecycleResource(previousVersion);

        LifeCycleStatisticalResource lifeCycleResource = resource.getLifeCycleStatisticalResource();

        // Set metadata for actual version
        lifeCycleResource.setReplacesVersion(previousVersionRelatedResource);
        lifeCycleResource.setProcStatus(ProcStatusEnum.DRAFT);
        lifeCycleResource.setCreationDate(new DateTime());
        lifeCycleResource.setCreationUser(ctx.getUserId());
        lifeCycleResource.setLastVersion(true);

        setNextVersion(previousVersion, versionType, lifeCycleResource);

        // Empty metadata
        lifeCycleResource.setProductionValidationDate(null);
        lifeCycleResource.setProductionValidationUser(null);
        lifeCycleResource.setDiffusionValidationDate(null);
        lifeCycleResource.setDiffusionValidationUser(null);
        lifeCycleResource.setRejectValidationDate(null);
        lifeCycleResource.setRejectValidationUser(null);
        lifeCycleResource.setPublicationDate(null);
        lifeCycleResource.setPublicationUser(null);
        lifeCycleResource.setValidFrom(null);
        lifeCycleResource.setLastUpdate(lifeCycleResource.getCreationDate());
    }

    public void applyVersioningPreviousResourceActions(ServiceContext ctx, HasLifecycle resource, HasLifecycle previousVersion, VersionTypeEnum versionType) throws MetamacException {
        if (ValidationUtils.isEmpty(previousVersion)) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.PREVIOUS_VERSION);
        }
        previousVersion.getLifeCycleStatisticalResource().setLastVersion(false);

        RelatedResource isReplacedBy = RelatedResourceUtils.createRelatedResourceForHasLifecycleResource(resource);
        previousVersion.getLifeCycleStatisticalResource().setIsReplacedByVersion(isReplacedBy);
    }

    private void setNextVersion(HasLifecycle previousVersion, VersionTypeEnum versionType, LifeCycleStatisticalResource lifeCycleResource) throws MetamacException {
        VersionResult versionResult = StatisticalResourcesVersionUtils.createNextVersion(previousVersion.getLifeCycleStatisticalResource().getVersionLogic(), versionType);

        lifeCycleResource.setVersionLogic(versionResult.getValue());

        if (VersionTypeEnum.MINOR.equals(versionType) && VersionTypeEnum.MAJOR.equals(versionResult.getType())) {
            lifeCycleResource.getVersionRationaleTypes().clear();
            lifeCycleResource.addVersionRationaleType(new VersionRationaleType(VersionRationaleTypeEnum.MAJOR_OTHER));

            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            String localisedMessage = LocaleUtil.getMessageForCode(MINOR_CHANGE_EXPECTED_MAJOR_VERSION_OCCURRED_MESSAGE, locale);

            lifeCycleResource.setVersionRationale(new InternationalString(locale.getLanguage(), localisedMessage));
        }
    }
}
