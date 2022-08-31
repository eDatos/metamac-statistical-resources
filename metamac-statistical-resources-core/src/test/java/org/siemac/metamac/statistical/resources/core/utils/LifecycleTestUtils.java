package org.siemac.metamac.statistical.resources.core.utils;

import org.joda.time.DateTime;
import org.siemac.metamac.statistical.resources.core.base.domain.HasLifecycle;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticalResourcesMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

public class LifecycleTestUtils {

    // ------------------------------------------------------------------------------------------------------
    // >> PRODUCTION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    public static void prepareToProductionValidationSiemac(HasSiemacMetadata resource) {
        prepareToLifecycleCommonLifeCycleResource(resource);
        prepareToLifecycleCommonSiemacResource(resource);
        prepareToProductionValidation(resource);
    }

    public static void prepareToProductionValidationLifecycle(HasLifecycle resource) {
        prepareToLifecycleCommonLifeCycleResource(resource);
        prepareToProductionValidation(resource);
    }

    private static void prepareToProductionValidation(HasLifecycle resource) {
        resource.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
    }

    public static void fillAsProductionValidationSiemac(HasSiemacMetadata resource) {
        prepareToProductionValidationSiemac(resource);
        fillAsProductionValidation(resource);
    }

    public static void fillAsProductionValidationLifecycle(HasLifecycle resource) {
        prepareToProductionValidationLifecycle(resource);
        fillAsProductionValidation(resource);
    }

    private static void fillAsProductionValidation(HasLifecycle resource) {
        LifeCycleStatisticalResource lifecycleResource = resource.getLifeCycleStatisticalResource();

        lifecycleResource.setProcStatus(ProcStatusEnum.PRODUCTION_VALIDATION);
        if (lifecycleResource.getProductionValidationDate() == null) {
            lifecycleResource.setProductionValidationDate(new DateTime());
        }
        if (lifecycleResource.getProductionValidationUser() == null) {
            lifecycleResource.setProductionValidationUser("PRODUCTION_VALIDATION_USER");
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // >> DIFFUSION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    public static void prepareToDiffusionValidationSiemac(HasSiemacMetadata resource) {
        fillAsProductionValidationSiemac(resource);
    }

    public static void prepareToDiffusionValidationLifecycle(HasLifecycle resource) {
        fillAsProductionValidationLifecycle(resource);
    }

    public static void fillAsDiffusionValidationSiemac(HasSiemacMetadata resource) {
        prepareToDiffusionValidationSiemac(resource);
        fillAsDiffusionValidation(resource);
    }

    public static void fillAsDiffusionValidationLifecycle(HasLifecycle resource) {
        prepareToDiffusionValidationLifecycle(resource);
        fillAsDiffusionValidation(resource);
    }

    private static void fillAsDiffusionValidation(HasLifecycle resource) {
        LifeCycleStatisticalResource lifecycleResource = resource.getLifeCycleStatisticalResource();

        lifecycleResource.setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
        if (lifecycleResource.getDiffusionValidationDate() == null) {
            lifecycleResource.setDiffusionValidationDate(new DateTime());
        }
        if (lifecycleResource.getDiffusionValidationUser() == null) {
            lifecycleResource.setDiffusionValidationUser("DIFFUSION_VALIDATION_USER");
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VALIDATION REJECTED being in Production Validation
    // ------------------------------------------------------------------------------------------------------

    public static void prepareToValidationRejectedFromProductionValidationSiemac(HasSiemacMetadata resource) {
        fillAsProductionValidationSiemac(resource);
    }

    public static void prepareToValidationRejectedFromProductionValidationLifecycle(HasLifecycle resource) {
        fillAsProductionValidationLifecycle(resource);
    }

    public static void fillAsValidationRejectedFromProductionValidationSiemac(HasSiemacMetadata resource) {
        prepareToDiffusionValidationSiemac(resource);
        fillAsValidationRejectedFromProductionValidation(resource);
    }

    public static void fillAsValidationRejectedFromProductionValidationLifecycle(HasLifecycle resource) {
        prepareToDiffusionValidationLifecycle(resource);
        fillAsValidationRejectedFromProductionValidation(resource);
    }

    private static void fillAsValidationRejectedFromProductionValidation(HasLifecycle resource) {
        LifeCycleStatisticalResource lifecycleResource = resource.getLifeCycleStatisticalResource();

        lifecycleResource.setProcStatus(ProcStatusEnum.VALIDATION_REJECTED);
        if (lifecycleResource.getRejectValidationDate() == null) {
            lifecycleResource.setRejectValidationDate(new DateTime());
        }
        if (lifecycleResource.getRejectValidationUser() == null) {
            lifecycleResource.setRejectValidationUser("VALIDATION_REJECTED_USER");
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PUBLISHED
    // ------------------------------------------------------------------------------------------------------

    public static void prepareToPublishingSiemac(HasSiemacMetadata resource) {
        fillAsDiffusionValidationSiemac(resource);
        prepareToPublishing(resource);
    }

    public static void prepareToPublishingLifecycle(HasLifecycle resource) {
        fillAsDiffusionValidationLifecycle(resource);
        prepareToPublishing(resource);
    }

    private static void prepareToPublishing(HasLifecycle resource) {
        LifeCycleStatisticalResource lifeCycleStatisticalResource = resource.getLifeCycleStatisticalResource();
        if (lifeCycleStatisticalResource.getValidFrom() == null) {
            lifeCycleStatisticalResource.setValidFrom(new DateTime());
        }
    }

    public static void fillAsPublishedSiemac(HasSiemacMetadata resource) {
        prepareToPublishingSiemac(resource);
        fillAsPublished(resource);
    }

    public static void fillAsPublishedLifecycle(HasLifecycle resource) {
        prepareToPublishingLifecycle(resource);
        fillAsPublished(resource);
    }

    private static void fillAsPublished(HasLifecycle resource) {
        LifeCycleStatisticalResource lifeCycleStatisticalResource = resource.getLifeCycleStatisticalResource();

        lifeCycleStatisticalResource.setProcStatus(ProcStatusEnum.PUBLISHED);

        if (lifeCycleStatisticalResource.getValidFrom() == null) {
            lifeCycleStatisticalResource.setValidFrom(new DateTime());
        }

        if (lifeCycleStatisticalResource.getPublicationDate() == null) {
            if (!lifeCycleStatisticalResource.getValidFrom().isAfterNow()) {
                lifeCycleStatisticalResource.setPublicationDate(lifeCycleStatisticalResource.getValidFrom());
            } else {
                lifeCycleStatisticalResource.setPublicationDate(new DateTime());
            }
        }

        if (lifeCycleStatisticalResource.getPublicationUser() == null) {
            lifeCycleStatisticalResource.setPublicationUser("PUBLISHING_USER");
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VERSIONING
    // ------------------------------------------------------------------------------------------------------

    public static void prepareToVersioningSiemac(HasSiemacMetadata resource) {
        fillAsPublishedSiemac(resource);
    }

    public static void prepareToVersioningLifecycle(HasLifecycle resource) {
        fillAsPublishedLifecycle(resource);
    }

    public static void fillAsVersionedSiemac(HasSiemacMetadata resource) {
        prepareToVersioningSiemac(resource);
        fillAsVersioned(resource);
    }

    public static void fillAsVersionedLifecycle(HasLifecycle resource) {
        prepareToVersioningLifecycle(resource);
        fillAsVersioned(resource);
    }

    private static void fillAsVersioned(HasLifecycle resource) {
        LifeCycleStatisticalResource lifeCycleStatisticalResource = resource.getLifeCycleStatisticalResource();

        if (lifeCycleStatisticalResource.getStatisticalOperation() == null) {
            ExternalItem operation = StatisticalResourcesPersistedDoMocks.mockStatisticalOperationExternalItem();
            lifeCycleStatisticalResource.setStatisticalOperation(operation);
        }

        if (lifeCycleStatisticalResource.getVersionLogic() == null) {
            lifeCycleStatisticalResource.setVersionLogic(StatisticalResourcesMockFactory.SECOND_VERSION);
        }

        if (lifeCycleStatisticalResource.getVersionRationaleTypes().isEmpty()) {
            lifeCycleStatisticalResource.addVersionRationaleType(new VersionRationaleType(VersionRationaleTypeEnum.MAJOR_CATEGORIES));
        }

        if (lifeCycleStatisticalResource.getNextVersion() == null) {
            lifeCycleStatisticalResource.setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);
        }

        if (lifeCycleStatisticalResource.getTitle() == null) {
            lifeCycleStatisticalResource.setTitle(StatisticalResourcesPersistedDoMocks.mockInternationalString());
        }

        if (lifeCycleStatisticalResource.getDescription() == null) {
            lifeCycleStatisticalResource.setDescription(StatisticalResourcesPersistedDoMocks.mockInternationalString());
        }

        if (lifeCycleStatisticalResource.getMaintainer() == null) {
            lifeCycleStatisticalResource.setMaintainer(StatisticalResourcesPersistedDoMocks.mockAgencyExternalItem());
        }

        lifeCycleStatisticalResource.setProcStatus(ProcStatusEnum.DRAFT);

        if (lifeCycleStatisticalResource.getCreationDate() == null) {
            lifeCycleStatisticalResource.setCreationDate(new DateTime());
        }

        if (lifeCycleStatisticalResource.getCreationUser() == null) {
            lifeCycleStatisticalResource.setCreationUser("VERSIONED_USER");
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // >> COMMON UTILS
    // ------------------------------------------------------------------------------------------------------

    public static void fillOptionalExternalItemsSiemacResource(HasSiemacMetadata resource) {
        resource.getSiemacMetadataStatisticalResource().getStatisticalOperationInstances().clear();
        resource.getSiemacMetadataStatisticalResource().addStatisticalOperationInstance(StatisticalResourcesPersistedDoMocks.mockStatisticalOperationInstanceExternalItem());
        resource.getSiemacMetadataStatisticalResource().addStatisticalOperationInstance(StatisticalResourcesPersistedDoMocks.mockStatisticalOperationInstanceExternalItem());

        resource.getSiemacMetadataStatisticalResource().getDataProvider().clear();
        resource.getSiemacMetadataStatisticalResource().addDataProvider(StatisticalResourcesPersistedDoMocks.mockDataProviderExternalItem());
        resource.getSiemacMetadataStatisticalResource().addDataProvider(StatisticalResourcesPersistedDoMocks.mockDataProviderExternalItem());
        
        resource.getSiemacMetadataStatisticalResource().getContributor().clear();
        resource.getSiemacMetadataStatisticalResource().addContributor(StatisticalResourcesPersistedDoMocks.mockOrganizationUnitExternalItem());
        resource.getSiemacMetadataStatisticalResource().addContributor(StatisticalResourcesPersistedDoMocks.mockOrganizationUnitExternalItem());

        resource.getSiemacMetadataStatisticalResource().getPublisherContributor().clear();
        resource.getSiemacMetadataStatisticalResource().addPublisherContributor(StatisticalResourcesPersistedDoMocks.mockOrganizationUnitExternalItem());
        resource.getSiemacMetadataStatisticalResource().addPublisherContributor(StatisticalResourcesPersistedDoMocks.mockOrganizationUnitExternalItem());

        resource.getSiemacMetadataStatisticalResource().getMediator().clear();
        resource.getSiemacMetadataStatisticalResource().addMediator(StatisticalResourcesPersistedDoMocks.mockOrganizationUnitExternalItem());
        resource.getSiemacMetadataStatisticalResource().addMediator(StatisticalResourcesPersistedDoMocks.mockOrganizationUnitExternalItem());
    }

    private static void prepareToLifecycleCommonSiemacResource(HasSiemacMetadata resource) {
        SiemacMetadataStatisticalResource siemacResource = resource.getSiemacMetadataStatisticalResource();

        if (siemacResource.getLanguage() == null) {
            siemacResource.setLanguage(StatisticalResourcesPersistedDoMocks.mockCodeExternalItem());
        }

        if (siemacResource.getLanguages().isEmpty()) {
            siemacResource.addLanguage(StatisticalResourcesPersistedDoMocks.mockCodeExternalItem(siemacResource.getLanguage().getCode()));
            siemacResource.addLanguage(StatisticalResourcesPersistedDoMocks.mockCodeExternalItem());
        }

        if (siemacResource.getCommonMetadata() == null) {
            siemacResource.setCommonMetadata(StatisticalResourcesPersistedDoMocks.mockCommonConfigurationExternalItem());
        }

        if (siemacResource.getCreator() == null) {
            siemacResource.setCreator(StatisticalResourcesPersistedDoMocks.mockOrganizationUnitExternalItem());
        }

        if (siemacResource.getDataProvider().isEmpty()) {
            siemacResource.addDataProvider(StatisticalResourcesPersistedDoMocks.mockDataProviderExternalItem());
        }
        
        if (siemacResource.getLastUpdate() == null) {
            siemacResource.setLastUpdate(new DateTime().minusMinutes(10));
        }

        if (siemacResource.getPublisher().isEmpty()) {
            siemacResource.addPublisher(StatisticalResourcesPersistedDoMocks.mockOrganizationUnitExternalItem());
        }
    }

    private static void prepareToLifecycleCommonLifeCycleResource(HasLifecycle resource) {
        LifeCycleStatisticalResource lifeCycleStatisticalResource = resource.getLifeCycleStatisticalResource();

        if (lifeCycleStatisticalResource.getStatisticalOperation() == null) {
            ExternalItem operation = StatisticalResourcesPersistedDoMocks.mockStatisticalOperationExternalItem();
            lifeCycleStatisticalResource.setStatisticalOperation(operation);
        }

        if (lifeCycleStatisticalResource.getVersionLogic() == null) {
            lifeCycleStatisticalResource.setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);
        }

        if (lifeCycleStatisticalResource.getVersionRationaleTypes().isEmpty()) {
            lifeCycleStatisticalResource.addVersionRationaleType(new VersionRationaleType(VersionRationaleTypeEnum.MAJOR_NEW_RESOURCE));
        }

        if (lifeCycleStatisticalResource.getNextVersion() == null) {
            lifeCycleStatisticalResource.setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);
        }

        // ReplacesVersion can not be filled because depends of the type: DatasetVersion, PublicationVersion or QueryVersion

        if (lifeCycleStatisticalResource.getTitle() == null) {
            lifeCycleStatisticalResource.setTitle(StatisticalResourcesPersistedDoMocks.mockInternationalString());
        }

        if (lifeCycleStatisticalResource.getDescription() == null) {
            lifeCycleStatisticalResource.setDescription(StatisticalResourcesPersistedDoMocks.mockInternationalString());
        }

        if (lifeCycleStatisticalResource.getMaintainer() == null) {
            lifeCycleStatisticalResource.setMaintainer(StatisticalResourcesPersistedDoMocks.mockAgencyExternalItem());
        }
    }
}
