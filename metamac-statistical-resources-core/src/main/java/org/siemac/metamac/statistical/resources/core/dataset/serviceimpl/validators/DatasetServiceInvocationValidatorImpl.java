package org.siemac.metamac.statistical.resources.core.dataset.serviceimpl.validators;

import static org.siemac.metamac.statistical.resources.core.error.utils.ServiceExceptionParametersUtils.addParameter;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.base.validators.BaseInvocationValidator;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dto.BasicVersionableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionSingleBaseParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionSingleParameters;
import org.siemac.metamac.statistical.resources.core.task.domain.FileDescriptorResult;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesValidationUtils;
import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;

public class DatasetServiceInvocationValidatorImpl extends BaseInvocationValidator {

    // ------------------------------------------------------------------------
    // PUBLIC METHODS FOR SERVICE METHODS
    // ------------------------------------------------------------------------

    // DATASOURCE
    // -------------

    public static void checkCreateDatasource(String datasetVersionUrn, Datasource datasource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersionUrn, ServiceExceptionParameters.DATASET_VERSION_URN, exceptions);
        checkNewDatasource(datasource, exceptions);
    }

    public static void checkUpdateDatasource(Datasource datasource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkExistingDatasource(datasource, ServiceExceptionParameters.DATASOURCE, exceptions);
    }

    public static void checkRetrieveDatasourceByUrn(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);
    }

    public static void checkDeleteDatasource(String urn, boolean deleteAttributes, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(deleteAttributes, ServiceExceptionParameters.DELETE_ATTRIBUTES, exceptions);
    }

    public static void checkRetrieveDatasourcesByDatasetVersion(String datasetVersionUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersionUrn, ServiceExceptionParameters.DATASET_VERSION_URN, exceptions);
    }

    public static void checkRetrieveDimensionRepresentationMapping(String datasetUrn, String filename, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetUrn, ServiceExceptionParameters.DATASET_URN, exceptions);
    }

    // DATASETS
    // --------------------

    public static void checkFindDatasetsByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    // DATASETS VERSIONS
    // --------------------

    public static void checkCreateDatasetVersion(DatasetVersion datasetVersion, ExternalItem statisticalOperation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkNewDatasetVersion(datasetVersion, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(statisticalOperation, ServiceExceptionParameters.STATISTICAL_OPERATION, exceptions);
    }

    public static void checkUpdateDatasetVersion(DatasetVersion datasetVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkExistingDatasetVersion(datasetVersion, ServiceExceptionParameters.DATASET_VERSION, exceptions);
    }

    public static void checkRetrieveDatasetVersionByUrn(String datasetVersionUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersionUrn, ServiceExceptionParameters.DATASET_VERSION_URN, exceptions);
    }

    public static void checkRetrieveLatestDatasetVersionByDatasetUrn(String datasetUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetUrn, ServiceExceptionParameters.DATASET_URN, exceptions);
    }

    public static void checkRetrieveLatestPublishedDatasetVersionByDatasetUrn(String datasetUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetUrn, ServiceExceptionParameters.DATASET_URN, exceptions);
    }

    public static void checkRetrieveDatasetVersions(String datasetVersionUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersionUrn, ServiceExceptionParameters.DATASET_VERSION_URN, exceptions);
    }

    public static void checkFindDatasetVersionsByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // NOTHING
    }

    public static void checkDeleteDatasetVersion(String datasetVersionUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersionUrn, ServiceExceptionParameters.DATASET_VERSION_URN, exceptions);
    }

    public static void checkVersioningDatasetVersion(String datasetVersionUrnToCopy, VersionTypeEnum versionType, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersionUrnToCopy, ServiceExceptionParameters.DATASET_VERSION_URN_TO_COPY, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(versionType, ServiceExceptionParameters.VERSION_TYPE, exceptions);
    }

    public static void checkRetrieveDatasetVersionDimensionsIds(String datasetVersionUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersionUrn, ServiceExceptionParameters.URN, exceptions);
    }

    public static void checkRetrieveCoverageForDatasetVersionDimension(String datasetVersionUrn, String dsdDimensionId, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(datasetVersionUrn, ServiceExceptionParameters.URN, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(dsdDimensionId, ServiceExceptionParameters.DSD_DIMENSION_ID, exceptions);
    }

    public static void checkFilterCoverageForDatasetVersionDimension(String datasetVersionUrn, String dsdDimensionId, String filter, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(datasetVersionUrn, ServiceExceptionParameters.URN, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(dsdDimensionId, ServiceExceptionParameters.DSD_DIMENSION_ID, exceptions);
    }

    public static void checkSaveDimensionRepresentationMapping(Dataset dataset, String datasourceUrn, Map<String, String> mapping, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(dataset, ServiceExceptionParameters.DATASET, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(datasourceUrn, ServiceExceptionParameters.DATASOURCE__URN, exceptions);
    }

    // ATTRIBUTES
    // --------------

    public static void checkCreateAttributeInstance(String datasetVersionUrn, AttributeInstanceDto attributeInstanceDto, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(attributeInstanceDto, ServiceExceptionParameters.DATASET_ATTRIBUTE, exceptions);
        if (attributeInstanceDto != null) {
            StatisticalResourcesValidationUtils.checkParameterRequired(attributeInstanceDto.getValue(), ServiceExceptionParameters.DATASET_ATTRIBUTE_VALUE, exceptions);
        }
    }

    public static void checkUpdateAttributeInstance(String datasetVersionUrn, AttributeInstanceDto attributeInstanceDto, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(attributeInstanceDto, ServiceExceptionParameters.DATASET_ATTRIBUTE, exceptions);
        if (attributeInstanceDto != null) {
            StatisticalResourcesValidationUtils.checkParameterRequired(attributeInstanceDto.getValue(), ServiceExceptionParameters.DATASET_ATTRIBUTE_VALUE, exceptions);
        }
    }

    public static void checkDeleteAttributeInstance(String datasetVersionUrn, String attributeInstanceUuid, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(attributeInstanceUuid, ServiceExceptionParameters.DATASET_ATTRIBUTE_UUID, exceptions);
    }

    public static void checkRetrieveAttributeInstances(String datasetVersionUrn, String attributeId, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(datasetVersionUrn, ServiceExceptionParameters.URN, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(attributeId, ServiceExceptionParameters.ATTRIBUTE_VALUE__ID, exceptions);
    }

    public static void checkRetrieveCoverageForDatasetVersionAttribute(String datasetVersionUrn, String dsdAttributeId, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(datasetVersionUrn, ServiceExceptionParameters.URN, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(dsdAttributeId, ServiceExceptionParameters.ATTRIBUTE_VALUE__ID, exceptions);
    }

    // ------------------------------------------------------------------------
    // PRIVATE METHODS
    // ------------------------------------------------------------------------

    // DATASOURCES
    // --------------

    private static void checkNewDatasource(Datasource datasource, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasource, ServiceExceptionParameters.DATASOURCE, exceptions);

        if (datasource == null) {
            return;
        }

        checkNewIdentifiableStatisticalResource(datasource.getIdentifiableStatisticalResource(), ServiceExceptionParameters.DATASOURCE, exceptions);

        // CODE
        if (datasource.getIdentifiableStatisticalResource() != null) {
            StatisticalResourcesValidationUtils.checkMetadataRequired(datasource.getIdentifiableStatisticalResource().getCode(), ServiceExceptionParameters.DATASOURCE__CODE, exceptions);
        }

        checkDatasource(datasource, ServiceExceptionParameters.DATASOURCE, exceptions);

        // Metadata that must be empty for new entities
        StatisticalResourcesValidationUtils.checkMetadataEmpty(datasource.getDatasetVersion(), ServiceExceptionParameters.DATASOURCE__DATASET_VERSION, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(datasource.getId(), ServiceExceptionParameters.DATASOURCE__ID, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(datasource.getVersion(), ServiceExceptionParameters.DATASOURCE__VERSION, exceptions);
    }

    private static void checkExistingDatasource(Datasource datasource, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasource, metadataName, exceptions);

        if (datasource == null) {
            return;
        }

        checkExistingIdentifiableStatisticalResource(datasource.getIdentifiableStatisticalResource(), TypeRelatedResourceEnum.DATASOURCE, metadataName, exceptions);
        checkDatasource(datasource, metadataName, exceptions);

        // Metadata that must be filled for existing entities
        StatisticalResourcesValidationUtils.checkMetadataRequired(datasource.getDatasetVersion(), addParameter(metadataName, ServiceExceptionSingleParameters.DATASET_VERSION), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(datasource.getId(), addParameter(metadataName, ServiceExceptionSingleParameters.ID), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(datasource.getVersion(), addParameter(metadataName, ServiceExceptionSingleParameters.VERSION), exceptions);
    }

    private static void checkDatasource(Datasource datasource, String metadataName, List<MetamacExceptionItem> exceptions) {
    }

    // DATASET VERSION
    // ----------------

    private static void checkNewDatasetVersion(DatasetVersion datasetVersion, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersion, ServiceExceptionParameters.DATASET_VERSION, exceptions);

        if (datasetVersion == null) {
            return;
        }

        checkNewSiemacMetadataStatisticalResource(datasetVersion.getSiemacMetadataStatisticalResource(), ServiceExceptionParameters.DATASET_VERSION, exceptions);
        checkDatasetVersion(datasetVersion, ServiceExceptionParameters.DATASET_VERSION, exceptions);

        // Metadata that must be empty for new entities
        StatisticalResourcesValidationUtils.checkMetadataEmpty(datasetVersion.getDataset(), ServiceExceptionParameters.DATASET_VERSION__DATASET, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(datasetVersion.getId(), ServiceExceptionParameters.DATASET_VERSION__ID, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(datasetVersion.getVersion(), ServiceExceptionParameters.DATASET_VERSION__VERSION, exceptions);
    }

    public static void checkExistingDatasetVersion(DatasetVersion datasetVersion, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersion, metadataName, exceptions);

        if (datasetVersion == null) {
            return;
        }

        checkExistingSiemacMetadataStatisticalResource(datasetVersion.getSiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.DATASET_VERSION, metadataName, exceptions);
        checkDatasetVersion(datasetVersion, metadataName, exceptions);

        // Metadata that must be filled for existing entities
        StatisticalResourcesValidationUtils.checkMetadataRequired(datasetVersion.getDataset(), addParameter(metadataName, ServiceExceptionSingleParameters.DATASET), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(datasetVersion.getId(), addParameter(metadataName, ServiceExceptionSingleParameters.ID), exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(datasetVersion.getVersion(), addParameter(metadataName, ServiceExceptionSingleParameters.VERSION), exceptions);
    }

    private static void checkDatasetVersion(DatasetVersion datasetVersion, String metadataName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersion.getDataSourceType(), addParameter(metadataName, ServiceExceptionSingleBaseParameters.DATA_SOURCE_TYPE), exceptions);
    }

    public static void checkFindStatisticOfficialities(List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkProccessDatasetFileImportationResult(String datasetImportationId, List<FileDescriptorResult> fileDescriptors, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetImportationId, ServiceExceptionParameters.DATASET_VERSION_URN, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(fileDescriptors, ServiceExceptionParameters.FILE_DESCRIPTORS, exceptions);
    }

    public static void checkImportDatasourcesInDatasetVersion(String datasetVersionUrn, List<URL> fileUrls, Map<String, String> dimensionRepresentationMapping,
            boolean storeDimensionRepresentationMapping, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersionUrn, ServiceExceptionParameters.DATASET_VERSION_URN, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(fileUrls, ServiceExceptionParameters.FILE_URLS, exceptions);
        if (dimensionRepresentationMapping == null) {
            StatisticalResourcesValidationUtils.checkParameterRequired(dimensionRepresentationMapping, ServiceExceptionParameters.DATASET_DIMENSION_REPRESENTATION_MAPPING, exceptions);
        }
        
        checkImportDataSourcesInStatisticalOperationWithAutomaticLifeCicle(basicVersionableStatisticalResourceDto, exceptions);
    }

    public static void checkImportDatabaseDatasourcesInDatasetVersion(String datasetVersionUrn, List<URL> fileUrls, Map<String, String> dimensionRepresentationMapping,
            boolean storeDimensionRepresentationMapping, List<MetamacExceptionItem> exceptions) {
        // NOTHING TO DO HERE
    }

    public static void checkImportDatasourcesInStatisticalOperation(String statisticalOperationCode, List<URL> fileUrls, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto,
            List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(statisticalOperationCode, ServiceExceptionParameters.STATISTICAL_OPERATION_CODE, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(fileUrls, ServiceExceptionParameters.FILE_URLS, exceptions);
        checkImportDataSourcesInStatisticalOperationWithAutomaticLifeCicle(basicVersionableStatisticalResourceDto, exceptions);

    }

    private static void checkImportDataSourcesInStatisticalOperationWithAutomaticLifeCicle(BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto,
            List<MetamacExceptionItem> exceptions) {
        if (basicVersionableStatisticalResourceDto != null && Boolean.TRUE.equals(basicVersionableStatisticalResourceDto.getAutomaticLifeCicle())) {
            StatisticalResourcesValidationUtils.checkParameterRequired(basicVersionableStatisticalResourceDto.getVersionRationaleTypes(), ServiceExceptionParameters.DATASET_VERSION_RATIONALE_TYPES,
                    exceptions);
            StatisticalResourcesValidationUtils.checkParameterRequired(basicVersionableStatisticalResourceDto.getNextVersion(), ServiceExceptionParameters.DATASET_NEXT_VERSION, exceptions);
            if (basicVersionableStatisticalResourceDto.getNextVersion() != null
                    && NextVersionTypeEnum.SCHEDULED_UPDATE.equals(NextVersionTypeEnum.valueOf(basicVersionableStatisticalResourceDto.getNextVersion()))) {
                StatisticalResourcesValidationUtils.checkParameterRequired(basicVersionableStatisticalResourceDto.getNextVersionDate(), ServiceExceptionParameters.DATASET_DATE_NEXT_VERSION,
                        exceptions);
                StatisticalResourcesValidationUtils.checkParameterRequired(basicVersionableStatisticalResourceDto.getNextUpdateDate(), ServiceExceptionParameters.DATASET_DATE_NEXT_UPDATE, exceptions);

            }
            StatisticalResourcesValidationUtils.checkParameterRequired(basicVersionableStatisticalResourceDto.getNextProcStatus(), ServiceExceptionParameters.DATASET_NEXT_PROC_STATUS, exceptions);

        }
    }
    
    public static void checkCheckAttributesInstancesWithDatasetAndDimensionAttachment(DatasetVersion datasetVersion, List<MetamacExceptionItem> exceptions) {
        checkExistingDatasetVersion(datasetVersion, ServiceExceptionParameters.DATASET_VERSION, exceptions);
    }

    // CATEGORISATION
    // ----------------

    public static void checkCreateCategorisation(String datasetVersionUrn, Categorisation categorisation, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersionUrn, ServiceExceptionParameters.DATASET_VERSION_URN, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(categorisation, ServiceExceptionParameters.CATEGORISATION, exceptions);
        if (categorisation == null) {
            return;
        }
        StatisticalResourcesValidationUtils.checkMetadataRequired(categorisation.getCategory(), ServiceExceptionParameters.CATEGORISATION__CATEGORY, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(categorisation.getMaintainer(), ServiceExceptionParameters.CATEGORISATION__MAINTAINER, exceptions);
    }

    public static void checkRetrieveCategorisationByUrn(String urn, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);
    }

    public static void checkRetrieveCategorisationsByDatasetVersion(String datasetVersionUrn, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersionUrn, ServiceExceptionParameters.URN, exceptions);
    }

    public static void checkFindCategorisationsByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkDeleteCategorisation(String urn, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);
    }

    public static void checkEndCategorisationValidity(String urn, DateTime validTo, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);
    }

    public static void checkInitializeCategorisationMetadataForCreation(Categorisation categorisation, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(categorisation, ServiceExceptionParameters.CATEGORISATION, exceptions);
    }

    public static void checkCreateDatabaseDatasourceInDatasetVersion(String datasetVersionUrn, String tableName, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersionUrn, ServiceExceptionParameters.DATASET_VERSION_URN, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(tableName, ServiceExceptionParameters.TABLE_NAME, exceptions);
    }

    public static void checkManageDatabaseView(String datasetRepositoryId, DatasetVersion datasetVersion, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetRepositoryId, ServiceExceptionParameters.DATASET_REPOSITORY_ID, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersion, ServiceExceptionParameters.DATASET_VERSION, exceptions);

    }
}
