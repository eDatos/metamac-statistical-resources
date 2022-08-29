package org.siemac.metamac.statistical.resources.core.lifecycle;

import static org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils.checkMetadataEmpty;
import static org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils.checkMetadataRequired;
import static org.siemac.metamac.statistical.resources.core.error.utils.ServiceExceptionParametersUtils.addParameter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.HasLifecycle;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionSingleParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators.ValidateDataVersusDsd;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoDataset;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesValidationUtils;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesVersionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

@Component
public class LifecycleCommonMetadataChecker {

    private static final Logger              logger = LoggerFactory.getLogger(LifecycleCommonMetadataChecker.class);

    @Autowired
    private SrmRestInternalService           srmRestInternalService;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    public void checkLifecycleCommonMetadata(HasLifecycle resource, String metadataName, List<MetamacExceptionItem> exceptionItems) {
        LifeCycleStatisticalResource lifeCycleStatisticalResource = resource.getLifeCycleStatisticalResource();

        // StatisticalResource
        checkMetadataRequired(lifeCycleStatisticalResource.getStatisticalOperation(), addParameter(metadataName, ServiceExceptionSingleParameters.STATISTICAL_OPERATION), exceptionItems);

        // IdentifiableResource
        checkMetadataRequired(lifeCycleStatisticalResource.getCode(), addParameter(metadataName, ServiceExceptionSingleParameters.CODE), exceptionItems);
        checkMetadataRequired(lifeCycleStatisticalResource.getUrn(), addParameter(metadataName, ServiceExceptionSingleParameters.URN), exceptionItems);

        // NameableResource
        checkMetadataRequired(lifeCycleStatisticalResource.getTitle(), addParameter(metadataName, ServiceExceptionSingleParameters.TITLE), exceptionItems);
        checkMetadataRequired(lifeCycleStatisticalResource.getDescription(), addParameter(metadataName, ServiceExceptionSingleParameters.DESCRIPTION), exceptionItems);

        // VersionableResource
        checkMetadataRequired(lifeCycleStatisticalResource.getVersionLogic(), addParameter(metadataName, ServiceExceptionSingleParameters.VERSION_LOGIC), exceptionItems);
        checkMetadataRequired(lifeCycleStatisticalResource.getVersionRationaleTypes(), addParameter(metadataName, ServiceExceptionSingleParameters.VERSION_RATIONALE_TYPES), exceptionItems);
        for (VersionRationaleType versionRationaleType : lifeCycleStatisticalResource.getVersionRationaleTypes()) {
            if (VersionRationaleTypeEnum.MINOR_ERRATA.equals(versionRationaleType.getValue())) {
                checkMetadataRequired(lifeCycleStatisticalResource.getVersionRationale(), addParameter(metadataName, ServiceExceptionSingleParameters.VERSION_RATIONALE), exceptionItems);
            }
        }

        checkMetadataRequired(lifeCycleStatisticalResource.getNextVersion(), addParameter(metadataName, ServiceExceptionSingleParameters.NEXT_VERSION), exceptionItems);
        if (lifeCycleStatisticalResource.getNextVersion() != null && !NextVersionTypeEnum.SCHEDULED_UPDATE.equals(lifeCycleStatisticalResource.getNextVersion())) {
            checkMetadataEmpty(lifeCycleStatisticalResource.getNextVersionDate(), addParameter(metadataName, ServiceExceptionSingleParameters.NEXT_VERSION_DATE), exceptionItems);
        } else if (lifeCycleStatisticalResource.getNextVersion() != null && NextVersionTypeEnum.SCHEDULED_UPDATE.equals(lifeCycleStatisticalResource.getNextVersion())) {
            checkMetadataRequired(lifeCycleStatisticalResource.getNextVersionDate(), addParameter(metadataName, ServiceExceptionSingleParameters.NEXT_VERSION_DATE), exceptionItems);
        }

        // LifeCycleResource
        checkMetadataRequired(lifeCycleStatisticalResource.getProcStatus(), addParameter(metadataName, ServiceExceptionSingleParameters.PROC_STATUS), exceptionItems);
        if (StringUtils.isNotBlank(lifeCycleStatisticalResource.getVersionLogic()) && !StatisticalResourcesVersionUtils.isInitialVersion(lifeCycleStatisticalResource.getVersionLogic())) {
            checkMetadataRequired(lifeCycleStatisticalResource.getReplacesVersion(), addParameter(metadataName, ServiceExceptionSingleParameters.REPLACES_VERSION), exceptionItems);
        }
        checkMetadataRequired(lifeCycleStatisticalResource.getMaintainer(), addParameter(metadataName, ServiceExceptionSingleParameters.MAINTAINER), exceptionItems);
    }

    public void checkSiemacCommonMetadataGeneral(HasSiemacMetadata resource, String metadataName, List<MetamacExceptionItem> exceptionItems) {
        SiemacMetadataStatisticalResource siemacMetadataStatisticalResource = resource.getSiemacMetadataStatisticalResource();

        checkMetadataRequired(siemacMetadataStatisticalResource.getLanguage(), addParameter(metadataName, ServiceExceptionSingleParameters.LANGUAGE), exceptionItems);
        checkMetadataRequired(siemacMetadataStatisticalResource.getLanguages(), addParameter(metadataName, ServiceExceptionSingleParameters.LANGUAGES), exceptionItems);

        checkMetadataRequired(siemacMetadataStatisticalResource.getType(), addParameter(metadataName, ServiceExceptionSingleParameters.TYPE), exceptionItems);

        checkMetadataRequired(siemacMetadataStatisticalResource.getCreator(), addParameter(metadataName, ServiceExceptionSingleParameters.CREATOR), exceptionItems);

        checkMetadataRequired(siemacMetadataStatisticalResource.getLastUpdate(), addParameter(metadataName, ServiceExceptionSingleParameters.LAST_UPDATE), exceptionItems);

        checkMetadataRequired(siemacMetadataStatisticalResource.getPublisher(), addParameter(metadataName, ServiceExceptionSingleParameters.PUBLISHER), exceptionItems);

        checkMetadataRequired(siemacMetadataStatisticalResource.getCommonMetadata(), addParameter(metadataName, ServiceExceptionSingleParameters.COMMON_METADATA), exceptionItems);
    }

    public void checkSiemacCommonMetadata(HasSiemacMetadata resource, String metadataName, List<MetamacExceptionItem> exceptionItems) {
        checkSiemacCommonMetadataGeneral(resource, metadataName, exceptionItems);
        SiemacMetadataStatisticalResource siemacMetadataStatisticalResource = resource.getSiemacMetadataStatisticalResource();
        checkMetadataRequired(siemacMetadataStatisticalResource.getDataProvider(), addParameter(metadataName, ServiceExceptionSingleParameters.DATA_PROVIDER), exceptionItems);
    }
    
    // Because is required but the datasets without this value before this development must version o reject.
    public void checkSiemacCommonMetadataVersioningWithoutDataProvider(HasSiemacMetadata resource, String metadataName, List<MetamacExceptionItem> exceptionItems) {
        checkSiemacCommonMetadataGeneral(resource, metadataName, exceptionItems);
    }
        
    public void checkDatasetVersionCommonMetadata(ServiceContext ctx, DatasetVersion resource, String metadataName, List<MetamacExceptionItem> exceptionItems) {

        checkMetadataRequired(resource.getRelatedDsd(), addParameter(metadataName, ServiceExceptionSingleParameters.RELATED_DSD), exceptionItems);

        checkMetadataRequired(resource.getGeographicGranularities(), addParameter(metadataName, ServiceExceptionSingleParameters.GEOGRAPHIC_GRANULARITIES), exceptionItems);
        checkMetadataRequired(resource.getTemporalGranularities(), addParameter(metadataName, ServiceExceptionSingleParameters.TEMPORAL_GRANULARITIES), exceptionItems);

        checkMetadataRequired(resource.getUpdateFrequency(), addParameter(metadataName, ServiceExceptionSingleParameters.UPDATE_FREQUENCY), exceptionItems);
        checkMetadataRequired(resource.getStatisticOfficiality(), addParameter(metadataName, ServiceExceptionSingleParameters.STATISTIC_OFFICIALITY), exceptionItems);

        if (resource.getDatasources() == null || resource.getDatasources().isEmpty()) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.DATASET_EMPTY_DATASOURCES, resource.getSiemacMetadataStatisticalResource().getUrn()));
        } else {
            if (!hasAnyDatasourceDateNextUpdate(resource)) {
                if (resource.getSiemacMetadataStatisticalResource().getNextVersion() != null
                        && NextVersionTypeEnum.SCHEDULED_UPDATE.equals(resource.getSiemacMetadataStatisticalResource().getNextVersion())) {
                    checkMetadataRequired(resource.getDateNextUpdate(), addParameter(metadataName, ServiceExceptionSingleParameters.DATE_NEXT_UPDATE), exceptionItems);
                }
            }
        }

        if (resource.getRelatedDsd() != null) {
            try {
                TaskInfoDataset taskInfoDataset = new TaskInfoDataset();
                taskInfoDataset.setDatasetUrn(resource.getDataset().getIdentifiableStatisticalResource().getUrn());
                taskInfoDataset.setDatasetVersionId(resource.getSiemacMetadataStatisticalResource().getUrn());
                taskInfoDataset.setDataStructureUrn(resource.getRelatedDsd().getUrn());
                ValidateDataVersusDsd validator = new ValidateDataVersusDsd(ctx, srmRestInternalService, taskInfoDataset);

                List<AttributeInstanceDto> attributesInstances = datasetRepositoriesServiceFacade.findAttributesInstances(resource.getDatasetRepositoryId());

                validator.checkAttributesInstancesRepresentation(attributesInstances);

                checkAttributesInstancesMandatoryAtNonObservationLevel(resource, validator);

            } catch (MetamacException e) {
                exceptionItems.addAll(e.getExceptionItems());
            } catch (Exception e) {
                logger.error("An error has occurred during attributes validation process ", e);
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_VALIDATE_ATTRIBUTES_ERROR));
            }
        }
    }

    private void checkAttributesInstancesMandatoryAtNonObservationLevel(DatasetVersion datasetVersion, ValidateDataVersusDsd validateDataVersusDsd) throws ApplicationException, MetamacException {
        Map<String, List<String>> coverage = datasetRepositoriesServiceFacade.findCodeDimensions(datasetVersion.getDatasetRepositoryId());
        List<MetamacExceptionItem> exceptions = new LinkedList<MetamacExceptionItem>();
        try {
            for (String attributeId : validateDataVersusDsd.getMandatoryAttributeIdsAtNonObservationLevel()) {
                List<AttributeInstanceDto> attributeInstancesWithDimensionAttachmentLevel = datasetRepositoriesServiceFacade
                        .findAttributesInstancesWithDimensionAttachmentLevelDenormalized(datasetVersion.getDatasetRepositoryId(), attributeId, null);
                List<AttributeInstanceDto> attributeInstancesWithDatasetAttachmentLevel = datasetRepositoriesServiceFacade
                        .findAttributesInstancesWithDatasetAttachmentLevel(datasetVersion.getDatasetRepositoryId(), attributeId);
                List<AttributeInstanceDto> attributeInstances = new ArrayList<AttributeInstanceDto>();
                attributeInstances.addAll(attributeInstancesWithDimensionAttachmentLevel);
                attributeInstances.addAll(attributeInstancesWithDatasetAttachmentLevel);
                validateDataVersusDsd.checkAttributesInstancesAssignmentStatus(attributeId, attributeInstances, coverage, exceptions);
            }
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN,
                    "Error validating mandatory attributes at non observation level " + datasetVersion.getDatasetRepositoryId() + ". Details: " + e.getMessage());
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    private boolean hasAnyDatasourceDateNextUpdate(DatasetVersion resource) {
        for (Datasource datasource : resource.getDatasources()) {
            if (datasource.getDateNextUpdate() != null) {
                return true;
            }
        }
        return false;
    }

    public void checkPublicationVersionCommonMetadata(PublicationVersion resource, String metadataName, List<MetamacExceptionItem> exceptionItems) {
        // Metadata specific of publicationVersion are only required for published: formatExtentResources and at least one cube per level.
    }

    public void checkQueryVersionCommonMetadata(QueryVersion resource, String metadataName, List<MetamacExceptionItem> exceptionItems) {
        StatisticalResourcesValidationUtils.checkMetadataRequiredIncompatible(resource.getFixedDatasetVersion(), addParameter(metadataName, ServiceExceptionSingleParameters.FIXED_DATASET_VERSION),
                resource.getDataset(), addParameter(metadataName, ServiceExceptionSingleParameters.DATASET), exceptionItems);
        checkMetadataRequired(resource.getStatus(), addParameter(metadataName, ServiceExceptionSingleParameters.STATUS), exceptionItems);
        checkMetadataRequired(resource.getType(), addParameter(metadataName, ServiceExceptionSingleParameters.TYPE), exceptionItems);
        checkMetadataRequired(resource.getSelection(), addParameter(metadataName, ServiceExceptionSingleParameters.SELECTION), exceptionItems);

        if (QueryTypeEnum.LATEST_DATA.equals(resource.getType())) {
            checkMetadataRequired(resource.getLatestDataNumber(), addParameter(metadataName, ServiceExceptionSingleParameters.LATEST_DATA_NUMBER), exceptionItems);
        }
    }

    // Metadata specific of multidatasetVersion are only required for published: formatExtentResources and at least one cube
    public void checkMultidatasetVersionCommonMetadata(MultidatasetVersion resource, String publicationVersion, List<MetamacExceptionItem> exceptions) {
        checkMetadataRequired(resource.getFilteringDimension(), addParameter(publicationVersion, ServiceExceptionSingleParameters.FILTERING_DIMENSION), exceptions);
    }
}
