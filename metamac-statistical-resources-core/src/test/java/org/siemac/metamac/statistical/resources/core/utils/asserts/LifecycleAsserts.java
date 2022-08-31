package org.siemac.metamac.statistical.resources.core.utils.asserts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.HasLifecycle;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.utils.RelatedResourceUtils;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesVersionUtils;

public class LifecycleAsserts extends CommonAsserts {

    // ------------------------------------------------------------------------------------------------------
    // >> PRODUCTION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    public static void assertNotNullAutomaticallyFilledMetadataSendToProductionValidation(HasLifecycle resource) {
        assertFilledMetadataLifecycleSendToProductionValidation(resource, true);
    }

    private static void assertFilledMetadataSiemacSendToProductionValidation(HasSiemacMetadata resource) {
        SiemacMetadataStatisticalResource siemac = resource.getSiemacMetadataStatisticalResource();

        assertNotNull(siemac.getLanguage());
        assertNotNull(siemac.getLanguages());
        assertFalse(siemac.getLanguages().isEmpty());

        assertNotNull(siemac.getKeywords());
        assertNotNull(siemac.getType());

        assertNotNull(siemac.getCreator());
        assertNotNull(siemac.getDataProvider());
        assertFalse(siemac.getDataProvider().isEmpty());
        assertNotNull(siemac.getLastUpdate());
        assertNotNull(siemac.getPublisher());
        assertFalse(siemac.getPublisher().isEmpty());

        assertNotNull(siemac.getCommonMetadata());
    }

    private static void assertFilledMetadataLifecycleSendToProductionValidation(HasLifecycle resource, boolean checkStatus) {
        assertNotNull(resource.getLifeCycleStatisticalResource().getProductionValidationDate());
        assertNotNull(resource.getLifeCycleStatisticalResource().getProductionValidationUser());
        if (checkStatus) {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, resource.getLifeCycleStatisticalResource().getProcStatus());
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // >> DIFFUSION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    public static void assertNotNullAutomaticallyFilledMetadataSendToDiffusionValidation(HasLifecycle resource) {
        assertFilledMetadataLifecycleSendToDiffusionValidation(resource, true);
    }

    private static void assertFilledMetadataLifecycleSendToDiffusionValidation(HasLifecycle resource, boolean checkStatus) {
        assertFilledMetadataLifecycleSendToProductionValidation(resource, false);

        assertNotNull(resource.getLifeCycleStatisticalResource().getDiffusionValidationDate());
        assertNotNull(resource.getLifeCycleStatisticalResource().getDiffusionValidationUser());

        if (checkStatus) {
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, resource.getLifeCycleStatisticalResource().getProcStatus());
        }
    }

    private static void assertFilledMetadataSiemacSendToDiffusionValidation(HasSiemacMetadata resource) {
        assertFilledMetadataSiemacSendToProductionValidation(resource);
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VALIDATION REJECTED
    // ------------------------------------------------------------------------------------------------------

    public static void assertNotNullAutomaticallyFilledMetadataSendToValidationRejected(HasLifecycle resource) {
        assertNotNullAutomaticallyFilledMetadataSendToValidationRejected(resource, true);
    }

    private static void assertNotNullAutomaticallyFilledMetadataSendToValidationRejected(HasLifecycle resource, boolean checkStatus) {
        assertFilledMetadataLifecycleSendToProductionValidation(resource, false);

        assertNotNull(resource.getLifeCycleStatisticalResource().getRejectValidationDate());
        assertNotNull(resource.getLifeCycleStatisticalResource().getRejectValidationUser());

        if (checkStatus) {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, resource.getLifeCycleStatisticalResource().getProcStatus());
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PUBLISHED
    // ------------------------------------------------------------------------------------------------------

    public static void assertNotNullAutomaticallyFilledMetadataSiemacSendToPublished(HasSiemacMetadata current, HasSiemacMetadata previous) {
        assertNotNullAutomaticallyFilledMetadataLifecycleSendToPublished(current, previous);

        assertFilledMetadataSiemacAfterPublishing(current, previous);
    }

    public static void assertNotNullAutomaticallyFilledMetadataLifecycleSendToPublished(HasLifecycle current, HasLifecycle previous) {
        assertFilledMetadataLifecycleAfterPublishing(current, previous);
    }

    private static void assertFilledMetadataLifecycleAfterPublishing(HasLifecycle current, HasLifecycle previous) {
        assertFilledMetadataLifecycleForAPublishedResource(current);
        if (!StatisticalResourcesVersionUtils.isInitialVersion(current)) {
            assertFilledMetadataLifecycleForAPreviousVersionOfPublishedResource(previous, current);
        }
    }

    private static void assertFilledMetadataLifecycleForAPublishedResource(HasLifecycle resource) {
        assertFilledMetadataLifecycleSendToDiffusionValidation(resource, false);

        LifeCycleStatisticalResource lifeCycleStatisticalResource = resource.getLifeCycleStatisticalResource();

        assertNotNull(lifeCycleStatisticalResource.getPublicationDate());
        assertNotNull(lifeCycleStatisticalResource.getPublicationUser());
        assertNotNull(lifeCycleStatisticalResource.getValidFrom());
        assertFalse(lifeCycleStatisticalResource.getPublicationDate().isAfterNow());
        assertFalse(lifeCycleStatisticalResource.getValidFrom().isBefore(lifeCycleStatisticalResource.getPublicationDate()));
        assertEquals(ProcStatusEnum.PUBLISHED, lifeCycleStatisticalResource.getProcStatus());

        if (StatisticalResourcesVersionUtils.isInitialVersion(resource)) {
            assertNull(lifeCycleStatisticalResource.getReplacesVersion());
        } else {
            assertNotNull(lifeCycleStatisticalResource.getReplacesVersion());
        }
    }

    private static void assertFilledMetadataLifecycleForAPreviousVersionOfPublishedResource(HasLifecycle previous, HasLifecycle current) {
        LifeCycleStatisticalResource currentLifecycle = current.getLifeCycleStatisticalResource();
        LifeCycleStatisticalResource previousLifecycle = previous.getLifeCycleStatisticalResource();

        assertFilledMetadataLifecycleForAPublishedResource(previous);

        // VALID_TO
        assertTrue(previousLifecycle.getValidFrom().isBefore(currentLifecycle.getValidFrom()));
        assertEqualsDate(currentLifecycle.getValidFrom(), previousLifecycle.getValidTo());

        // REPLACES_VERSION
        try {
            NameableStatisticalResource currentReplacesNameable = RelatedResourceUtils.retrieveNameableResourceLinkedToRelatedResource(currentLifecycle.getReplacesVersion());
            assertEquals(previousLifecycle.getUrn(), currentReplacesNameable.getUrn());
        } catch (MetamacException e) {
            Assert.fail("Error retrieving nameable from related resource:" + e);
        }
    }

    private static void assertFilledMetadataSiemacAfterPublishing(HasSiemacMetadata current, HasSiemacMetadata previous) {
        SiemacMetadataStatisticalResource currentSiemac = current.getSiemacMetadataStatisticalResource();

        assertFilledMetadataSiemacSendToDiffusionValidation(current);

        // COPYRIGHTED_DATE
        assertEquals(Integer.valueOf(currentSiemac.getValidFrom().getYear()), currentSiemac.getCopyrightedDate());
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VERSIONING
    // ------------------------------------------------------------------------------------------------------

    public static void assertNotNullAutomaticallyFilledMetadataVersioningNewResource(HasLifecycle resource, HasLifecycle previousVersion) throws MetamacException {
        assertNotNullAutomaticallyFilledMetadataForAVersionedResource(resource, previousVersion);
        assertNullAutomaticallyFilledMetadataForAVersionedResource(resource);
    }

    public static void assertNotNullAutomaticallyFilledMetadataVersioningPreviousResource(HasLifecycle resource, HasLifecycle previousVersion) throws MetamacException {
        assertNotNullAutomaticallyFilledForAPreviosVersionOfVersionedResource(resource, previousVersion);
    }

    private static void assertNotNullAutomaticallyFilledForAPreviosVersionOfVersionedResource(HasLifecycle resource, HasLifecycle previousVersion) throws MetamacException {
        assertFalse(previousVersion.getLifeCycleStatisticalResource().getLastVersion());
        assertFilledMetadataLifecycleForAPublishedResource(previousVersion);
    }

    private static void assertNotNullAutomaticallyFilledMetadataForAVersionedResource(HasLifecycle resource, HasLifecycle previousVersion) throws MetamacException {
        assertNotNull(resource.getLifeCycleStatisticalResource().getCreationDate());
        assertNotNull(resource.getLifeCycleStatisticalResource().getCreationUser());
        assertEquals(ProcStatusEnum.DRAFT, resource.getLifeCycleStatisticalResource().getProcStatus());
        assertTrue(resource.getLifeCycleStatisticalResource().getLastVersion());
        assertNotNull(resource.getLifeCycleStatisticalResource().getReplacesVersion());
        assertEqualsRelatedResource(resource.getLifeCycleStatisticalResource().getReplacesVersion(), RelatedResourceUtils.createRelatedResourceForHasLifecycleResource(previousVersion));
        assertTrue(Double.valueOf(resource.getLifeCycleStatisticalResource().getVersionLogic()) > Double.valueOf(previousVersion.getLifeCycleStatisticalResource().getVersionLogic()));
    }

    private static void assertNullAutomaticallyFilledMetadataForAVersionedResource(HasLifecycle resource) {
        // Production validation
        assertNull(resource.getLifeCycleStatisticalResource().getProductionValidationDate());
        assertNull(resource.getLifeCycleStatisticalResource().getProductionValidationUser());

        // Diffusion validation
        assertNull(resource.getLifeCycleStatisticalResource().getDiffusionValidationDate());
        assertNull(resource.getLifeCycleStatisticalResource().getDiffusionValidationUser());

        // Reject validation
        assertNull(resource.getLifeCycleStatisticalResource().getRejectValidationDate());
        assertNull(resource.getLifeCycleStatisticalResource().getRejectValidationUser());

        // Publication
        assertNull(resource.getLifeCycleStatisticalResource().getPublicationDate());
        assertNull(resource.getLifeCycleStatisticalResource().getPublicationUser());
        assertNull(resource.getLifeCycleStatisticalResource().getValidFrom());
        assertNull(resource.getLifeCycleStatisticalResource().getValidTo());
    }

}
