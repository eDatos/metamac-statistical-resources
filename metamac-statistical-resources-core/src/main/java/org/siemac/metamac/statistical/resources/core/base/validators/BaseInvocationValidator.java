package org.siemac.metamac.statistical.resources.core.base.validators;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.StatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.enume.utils.TypeRelatedResourceEnumUtils;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionSingleParameters;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesValidationUtils;

import static org.siemac.metamac.statistical.resources.core.error.utils.ServiceExceptionParametersUtils.addParameter;

public abstract class BaseInvocationValidator {

    // ------------------------------------------------------------------------------------
    // STATISTICAL RESOURCE
    // ------------------------------------------------------------------------------------

    protected static void checkNewStatisticalResource(StatisticalResource resource, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource, metadataName, exceptions);
        if (resource == null) {
            return;
        }

        checkStatisticalResource(resource, metadataName, exceptions);

        // Metadata that must be empty for new entities
        StatisticalResourcesValidationUtils.checkMetadataEmpty(resource.getId(), addParameter(metadataName, ServiceExceptionSingleParameters.ID), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(resource.getVersion(), addParameter(metadataName, ServiceExceptionSingleParameters.VERSION), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(resource.getCreatedDate(), addParameter(metadataName, ServiceExceptionSingleParameters.CREATED_DATE), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(resource.getCreatedBy(), addParameter(metadataName, ServiceExceptionSingleParameters.CREATED_BY), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(resource.getLastUpdated(), addParameter(metadataName, ServiceExceptionSingleParameters.LAST_UPDATED), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(resource.getLastUpdatedBy(), addParameter(metadataName, ServiceExceptionSingleParameters.LAST_UPDATED_BY), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(resource.getStatisticalOperation(), addParameter(metadataName, ServiceExceptionSingleParameters.STATISTICAL_OPERATION), exceptions);
    }

    protected static void checkExistingStatisticalResource(StatisticalResource resource, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource, metadataName, exceptions);
        if (resource == null) {
            return;
        }

        checkStatisticalResource(resource, metadataName, exceptions);

        // Metadata that must be filled for existing entities
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getId(), addParameter(metadataName, ServiceExceptionSingleParameters.ID), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getVersion(), addParameter(metadataName, ServiceExceptionSingleParameters.VERSION), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getCreatedDate(), addParameter(metadataName, ServiceExceptionSingleParameters.CREATED_DATE), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getCreatedBy(), addParameter(metadataName, ServiceExceptionSingleParameters.CREATED_BY), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getLastUpdated(), addParameter(metadataName, ServiceExceptionSingleParameters.LAST_UPDATED), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getLastUpdatedBy(), addParameter(metadataName, ServiceExceptionSingleParameters.LAST_UPDATED_BY), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getStatisticalOperation(), addParameter(metadataName, ServiceExceptionSingleParameters.STATISTICAL_OPERATION), exceptions);
    }

    private static void checkStatisticalResource(StatisticalResource resource, String metadataName, List<MetamacExceptionItem> exceptions) {
        // StatisticalResourcesValidationUtils.checkMetadataRequired(statisticalResource.getUuid(), addParameter(metadataName, ServiceExceptionSingleParameters.UUID, exceptions);
    }

    // ------------------------------------------------------------------------------------
    // IDENTIFIABLE STATISTICAL RESOURCE
    // ------------------------------------------------------------------------------------

    protected static void checkNewIdentifiableStatisticalResource(IdentifiableStatisticalResource resource, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource, metadataName, exceptions);
        if (resource == null) {
            return;
        }

        checkNewStatisticalResource(resource, metadataName, exceptions);
        checkIdentifiableStatisticalResource(resource, metadataName, exceptions);

        // Metadata that must be empty for new entities
        StatisticalResourcesValidationUtils.checkMetadataEmpty(resource.getUrn(), addParameter(metadataName, ServiceExceptionSingleParameters.URN), exceptions);
    }

    protected static void checkExistingIdentifiableStatisticalResource(IdentifiableStatisticalResource resource, TypeRelatedResourceEnum resourceType, String metadataName,
            List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource, metadataName, exceptions);
        if (resource == null) {
            return;
        }

        checkExistingStatisticalResource(resource, metadataName, exceptions);
        checkIdentifiableStatisticalResource(resource, metadataName, exceptions);

        // Metadata that must be filled for existing entities
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getUrn(), addParameter(metadataName, ServiceExceptionSingleParameters.URN), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getCode(), addParameter(metadataName, ServiceExceptionSingleParameters.CODE), exceptions);

        checkForSemanticIdentifier(resource.getCode(), resourceType, metadataName, exceptions);
    }

    protected static void checkForSemanticIdentifier(String code, TypeRelatedResourceEnum resourceType, String metadataName, List<MetamacExceptionItem> exceptions) {
        if (!(TypeRelatedResourceEnumUtils.checkIfTypeRelatedResourceIsAny(resourceType, TypeRelatedResourceEnum.DATASOURCE, TypeRelatedResourceEnum.CHAPTER, TypeRelatedResourceEnum.CUBE))) {
            StatisticalResourcesValidationUtils.checkSemanticIdentifierAsMetamacID(code, addParameter(metadataName, ServiceExceptionSingleParameters.CODE), exceptions);
        }
    }

    private static void checkIdentifiableStatisticalResource(IdentifiableStatisticalResource resource, String metadataName, List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    // ------------------------------------------------------------------------------------
    // NAMEABLE STATISTICAL RESOURCE
    // ------------------------------------------------------------------------------------

    protected static void checkNewNameableStatisticalResource(NameableStatisticalResource resource, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource, metadataName, exceptions);
        if (resource == null) {
            return;
        }

        checkNewIdentifiableStatisticalResource(resource, metadataName, exceptions);
        checkNameableStatisticalResource(resource, metadataName, exceptions);
    }

    protected static void checkExistingNameableStatisticalResource(NameableStatisticalResource resource, TypeRelatedResourceEnum typeRelatedResource, String metadataName,
            List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource, metadataName, exceptions);
        if (resource == null) {
            return;
        }

        checkExistingIdentifiableStatisticalResource(resource, typeRelatedResource, metadataName, exceptions);
        checkNameableStatisticalResource(resource, metadataName, exceptions);
    }

    private static void checkNameableStatisticalResource(NameableStatisticalResource resource, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getTitle(), addParameter(metadataName, ServiceExceptionSingleParameters.TITLE), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataOptionalIsValid(resource.getDescription(), addParameter(metadataName, ServiceExceptionSingleParameters.DESCRIPTION), exceptions);
    }

    // ------------------------------------------------------------------------------------
    // VERSIONABLE STATISTICAL RESOURCE
    // ------------------------------------------------------------------------------------

    protected static void checkNewVersionableStatisticalResource(VersionableStatisticalResource resource, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource, metadataName, exceptions);
        if (resource == null) {
            return;
        }

        checkNewNameableStatisticalResource(resource, metadataName, exceptions);
        checkVersionableStatisticalResource(resource, metadataName, exceptions);

        // Metadata that must be empty when the entity is created
        StatisticalResourcesValidationUtils.checkMetadataEmpty(resource.getVersionLogic(), addParameter(metadataName, ServiceExceptionSingleParameters.VERSION_LOGIC), exceptions);
    }

    protected static void checkExistingVersionableStatisticalResource(VersionableStatisticalResource resource, TypeRelatedResourceEnum typeRelatedResource, String metadataName,
            List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource, metadataName, exceptions);
        if (resource == null) {
            return;
        }

        checkExistingNameableStatisticalResource(resource, typeRelatedResource, metadataName, exceptions);
        checkVersionableStatisticalResource(resource, metadataName, exceptions);

        // Metadata that must be filled for existing entities
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getVersionLogic(), addParameter(metadataName, ServiceExceptionSingleParameters.VERSION_LOGIC), exceptions);
    }

    private static void checkVersionableStatisticalResource(VersionableStatisticalResource resource, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataOptionalIsValid(resource.getVersionRationale(), addParameter(metadataName, ServiceExceptionSingleParameters.VERSION_RATIONALE), exceptions);
    }

    // ------------------------------------------------------------------------------------
    // LIFE CYCLE STATISTICAL RESOURCE
    // ------------------------------------------------------------------------------------

    protected static void checkNewLifeCycleStatisticalResource(LifeCycleStatisticalResource resource, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource, metadataName, exceptions);
        if (resource == null) {
            return;
        }

        checkNewVersionableStatisticalResource(resource, metadataName, exceptions);
        checkLifeCycleStatisticalResource(resource, metadataName, exceptions);

        // Metadata that must be empty for new entities
        StatisticalResourcesValidationUtils.checkMetadataEmpty(resource.getProcStatus(), addParameter(metadataName, ServiceExceptionSingleParameters.PROC_STATUS), exceptions);
    }

    protected static void checkExistingLifeCycleStatisticalResource(LifeCycleStatisticalResource resource, TypeRelatedResourceEnum typeRelatedResource, String metadataName,
            List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource, metadataName, exceptions);
        if (resource == null) {
            return;
        }

        checkExistingVersionableStatisticalResource(resource, typeRelatedResource, metadataName, exceptions);
        checkLifeCycleStatisticalResource(resource, metadataName, exceptions);

        // Metadata that must be filled for existing entities
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getProcStatus(), addParameter(metadataName, ServiceExceptionSingleParameters.PROC_STATUS), exceptions);
    }

    private static void checkLifeCycleStatisticalResource(LifeCycleStatisticalResource resource, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getMaintainer(), addParameter(metadataName, ServiceExceptionSingleParameters.MAINTAINER), exceptions);
    }

    // ------------------------------------------------------------------------------------
    // SIEMAC METADATA STATISTICAL RESOURCE
    // ------------------------------------------------------------------------------------

    protected static void checkNewSiemacMetadataStatisticalResource(SiemacMetadataStatisticalResource resource, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource, metadataName, exceptions);
        if (resource == null) {
            return;
        }

        checkSiemacMetadataStatisticalResource(resource, metadataName, exceptions);
        checkNewLifeCycleStatisticalResource(resource, metadataName, exceptions);
    }

    protected static void checkExistingSiemacMetadataStatisticalResource(SiemacMetadataStatisticalResource resource, TypeRelatedResourceEnum typeRelatedResource, String metadataName,
            List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource, metadataName, exceptions);
        if (resource == null) {
            return;
        }

        checkSiemacMetadataStatisticalResource(resource, metadataName, exceptions);
        checkExistingLifeCycleStatisticalResource(resource, typeRelatedResource, metadataName, exceptions);
    }

    private static void checkSiemacMetadataStatisticalResource(SiemacMetadataStatisticalResource resource, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(resource.getLanguage(), addParameter(metadataName, ServiceExceptionSingleParameters.LANGUAGE), exceptions);
        StatisticalResourcesValidationUtils.checkListMetadataOptionalIsValid(resource.getLanguages(), addParameter(metadataName, ServiceExceptionSingleParameters.LANGUAGES), exceptions);
        StatisticalResourcesValidationUtils.checkListMetadataOptionalIsValid(resource.getStatisticalOperationInstances(),
                addParameter(metadataName, ServiceExceptionSingleParameters.STATISTICAL_OPERATION_INSTANCES), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataOptionalIsValid(resource.getCreator(), addParameter(metadataName, ServiceExceptionSingleParameters.CREATOR), exceptions);
        StatisticalResourcesValidationUtils.checkListMetadataOptionalIsValid(resource.getContributor(), addParameter(metadataName, ServiceExceptionSingleParameters.CONTRIBUTOR), exceptions);
        StatisticalResourcesValidationUtils.checkListMetadataOptionalIsValid(resource.getDataProvider(), addParameter(metadataName, ServiceExceptionSingleParameters.DATA_PROVIDER), exceptions);
        StatisticalResourcesValidationUtils.checkListMetadataOptionalIsValid(resource.getPublisher(), addParameter(metadataName, ServiceExceptionSingleParameters.PUBLISHER), exceptions);
        StatisticalResourcesValidationUtils.checkListMetadataOptionalIsValid(resource.getMediator(), addParameter(metadataName, ServiceExceptionSingleParameters.MEDIATOR), exceptions);
    }

}
