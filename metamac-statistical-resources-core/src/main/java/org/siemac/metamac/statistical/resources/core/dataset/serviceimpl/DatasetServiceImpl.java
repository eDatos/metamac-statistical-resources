package org.siemac.metamac.statistical.resources.core.dataset.serviceimpl;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;
import static org.siemac.metamac.core.common.util.MetamacCollectionUtils.isInCollection;
import static org.siemac.metamac.statistical.resources.core.base.domain.utils.RelatedResourceResultUtils.getUrnsFromRelatedResourceResults;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.util.MetamacCollectionUtils;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;
import org.siemac.metamac.core.common.util.transformers.MetamacTransformer;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.statistical.resources.core.base.components.SiemacStatisticalResourceGeneratedCode;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResourceRepository;
import org.siemac.metamac.statistical.resources.core.base.utils.FillMetadataForCreateResourceUtils;
import org.siemac.metamac.statistical.resources.core.base.validators.ProcStatusValidator;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceRepository;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdAttribute;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdComponent;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdComponentType;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdDimension;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.constraint.api.ConstraintsService;
import org.siemac.metamac.statistical.resources.core.dataset.checks.DatasetMetadataEditionChecks;
import org.siemac.metamac.statistical.resources.core.dataset.domain.AttributeValue;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CategorisationProperties;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionProperties;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasourceProperties;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DimensionRepresentationMapping;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.dataset.serviceapi.validators.DatasetServiceInvocationValidator;
import org.siemac.metamac.statistical.resources.core.dataset.utils.DatasetVersionUpdateUtils;
import org.siemac.metamac.statistical.resources.core.dataset.utils.DatasetVersionUtils;
import org.siemac.metamac.statistical.resources.core.dataset.utils.DatasetVersioningCopyUtils;
import org.siemac.metamac.statistical.resources.core.dto.BasicVersionableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DataSourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.task.domain.DatasetFileFormatEnum;
import org.siemac.metamac.statistical.resources.core.enume.utils.NextVersionTypeEnumUtils;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.export.PlainTextExporter;
import org.siemac.metamac.statistical.resources.core.geocache.serviceapi.CacheService;
import org.siemac.metamac.statistical.resources.core.invocation.service.NoticesRestInternalService;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.invocation.service.StatisticalOperationsRestInternalService;
import org.siemac.metamac.statistical.resources.core.invocation.utils.RestMapper;
import org.siemac.metamac.statistical.resources.core.io.domain.TemporalAttributeValues;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.ImportDatasetFromDatabaseJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.ManipulateCsvDataService;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators.ValidateDataVersusDsd;
import org.siemac.metamac.statistical.resources.core.io.utils.ManipulateDataUtils;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.checker.ExternalItemChecker;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.serviceapi.QueryService;
import org.siemac.metamac.statistical.resources.core.security.DatasetsSecurityUtils;
import org.siemac.metamac.statistical.resources.core.task.domain.AlternativeEnumeratedRepresentation;
import org.siemac.metamac.statistical.resources.core.task.domain.FileDescriptor;
import org.siemac.metamac.statistical.resources.core.task.domain.FileDescriptorResult;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoDataset;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.siemac.metamac.statistical.resources.core.utils.DatabaseDatasetImportUtils;
import org.siemac.metamac.statistical.resources.core.utils.InternationalStringUtils;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesCollectionUtils;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesVersionUtils;
import org.siemac.metamac.statistical.resources.core.utils.predicates.CodeDimensionEqualsIdentifierPredicate;
import org.siemac.metamac.statistical.resources.core.utils.predicates.ExternalItemEqualsIdentifierPredicate;
import org.siemac.metamac.statistical.resources.core.utils.shared.DatasetAttibuteSharedUtils;
import org.siemac.metamac.statistical.resources.core.utils.transformers.CodeDimensionToCodeStringTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.DatasetRepositoryDto;
import es.gobcan.istac.edatos.dataset.repository.dto.DimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;
import es.ibestat.jaxi.stream.messages.DatasetAvro;
import es.ibestat.jaxi.stream.messages.PublicationAvro;

/**
 * Implementation of DatasetService.
 */
@Service("datasetService")
public class DatasetServiceImpl extends DatasetServiceImplBase {

    private static final Logger                       log = LoggerFactory.getLogger(DatasetServiceImpl.class);

    @Autowired
    private IdentifiableStatisticalResourceRepository identifiableStatisticalResourceRepository;

    @Autowired
    private DatasetServiceInvocationValidator         datasetServiceInvocationValidator;

    @Autowired
    private SiemacStatisticalResourceGeneratedCode    siemacStatisticalResourceGeneratedCode;

    @Autowired
    private SrmRestInternalService                    srmRestInternalService;

    @Autowired
    StatisticalOperationsRestInternalService          statisticalOperationsRestInternalService;

    @Autowired
    private QueryVersionRepository                    queryVersionRepository;

    @Autowired
    private QueryService                              queryService;

    @Autowired
    private DatasetRepositoriesServiceFacade          statisticsDatasetRepositoriesServiceFacade;

    @Autowired
    private RestMapper                                restMapper;

    @Autowired
    private ExternalItemChecker                       externalItemChecker;

    @Autowired
    private DatasetVersionRepository                  datasetVersionRepository;

    @Autowired
    private StatisticalResourcesConfiguration         configurationService;

    @Autowired
    private ConstraintsService                        constraintsService;

    @Autowired
    private RelatedResourceRepository                 relatedResourceRepository;

    @Autowired
    private NoticesRestInternalService                noticesRestInternalService;

    @Autowired
    private TaskService                               taskService;

    @Autowired
    @Qualifier("txManager")
    private PlatformTransactionManager                platformTransactionManager;

    @Autowired
    private DatasetRepositoriesServiceFacade          datasetRepositoriesServiceFacade;

    @Autowired
    CacheService                                      cacheService;

    @Autowired
    private ManipulateCsvDataService                  manipulateCsvDataService;

    // ------------------------------------------------------------------------
    // DATASOURCES
    // ------------------------------------------------------------------------

    @Override
    public Datasource createDatasource(ServiceContext ctx, String datasetVersionUrn, Datasource datasource) throws MetamacException {

        // Validations
        datasetServiceInvocationValidator.checkCreateDatasource(ctx, datasetVersionUrn, datasource);

        DatasetVersion datasetVersion = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        checkCanAlterDatasourcesInDatasetVersion(datasetVersion);

        // Fill metadata
        fillMetadataForCreateDatasource(datasource, datasetVersion);

        identifiableStatisticalResourceRepository.checkDuplicatedUrn(datasource.getIdentifiableStatisticalResource());

        // Save
        datasource = getDatasourceRepository().save(datasource);

        // Update dataset version (add datasource)
        datasetVersion = addDatasourceForDatasetVersion(datasource, datasetVersion);

        computeDataRelatedMetadata(datasetVersion);

        getDatasetVersionRepository().save(datasetVersion);

        return datasource;
    }

    protected void updateAutomaticDatasource(DatasetVersion datasetVersion) throws MetamacException {
        datasetVersion.getSiemacMetadataStatisticalResource().setLastUpdate(new DateTime());

        computeDataRelatedMetadata(datasetVersion);

        getDatasetVersionRepository().save(datasetVersion);
    }

    private void checkCanAlterDatasourcesInDatasetVersion(DatasetVersion datasetVersion) throws MetamacException {
        if (!DatasetMetadataEditionChecks.canAlterDatasources(datasetVersion.getSiemacMetadataStatisticalResource().getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.DATASET_VERSION_CANT_ALTER_DATASOURCES, datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        }
    }

    @Override
    public Datasource updateDatasource(ServiceContext ctx, Datasource datasource) throws MetamacException {

        // Validation of parameters
        datasetServiceInvocationValidator.checkUpdateDatasource(ctx, datasource);

        checkCanAlterDatasourcesInDatasetVersion(datasource.getDatasetVersion());

        checkNotTasksInProgress(ctx, datasource.getDatasetVersion().getDataset().getIdentifiableStatisticalResource().getUrn());

        // Update
        Datasource updatedDataSource = getDatasourceRepository().save(datasource);

        return updatedDataSource;
    }

    @Override
    public Datasource retrieveDatasourceByUrn(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        datasetServiceInvocationValidator.checkRetrieveDatasourceByUrn(ctx, urn);

        // Retrieve
        Datasource datasource = getDatasourceRepository().retrieveByUrn(urn);
        return datasource;
    }

    @Override
    public int deleteDatasource(ServiceContext ctx, String urn, boolean deleteAttributes) throws MetamacException {

        // Validation
        datasetServiceInvocationValidator.checkDeleteDatasource(ctx, urn, deleteAttributes);

        // Retrieve
        Datasource datasource = getDatasourceRepository().retrieveByUrn(urn);

        DatasetVersion datasetVersion = datasource.getDatasetVersion();

        checkCanAlterDatasourcesInDatasetVersion(datasource.getDatasetVersion());

        checkNotTasksInProgress(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());

        checkDatasetVersionForDatasourceHasNoQueries(datasource, datasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        datasetVersion = deleteDatasourceToDataset(datasource);

        deleteDatasourceDimensionRepresentationMappings(datasetVersion, datasource);

        int observationsDeleted = deleteDatasourceData(datasetVersion.getDatasetRepositoryId(), datasource);

        log.debug("Number of deleted observations: {} corresponding to the datasource: {}", observationsDeleted, urn);

        if (deleteAttributes) {
            deleteAttributeInstancesLowerThanDatasetLevel(datasetVersion);
        }

        computeDataRelatedMetadata(datasetVersion);

        getDatasetVersionRepository().save(datasetVersion);

        if (datasetVersion.getDatasources().isEmpty()) {
            // Revert to draft if there aren't datasources. This is possible because one will never be published without constraints datasources. And if you can delete datasources, then it is because
            // it has not been released datasetversion. Therefore, you can remove the constraint.
            constraintsService.revertContentConstraintsForArtefactToDraft(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        }

        return observationsDeleted;
    }

    @Override
    public List<String> deleteDatasourcesNotUsed(ServiceContext ctx, String datasetUrn, boolean deleteAttributes) throws MetamacException {
        datasetServiceInvocationValidator.checkDeleteDatasourcesNotUsed(ctx, datasetUrn, deleteAttributes);

        List<String> codesDataSourcesDeleted = new ArrayList<String>();
        List<String> urnsDataSourcesDeleted = new ArrayList<String>();

        List<String> datasourcesUsed = findDataSourcesUsedInDataset(datasetUrn);

        List<Datasource> allDataSources = retrieveDatasourcesByDatasetVersion(ctx, datasetUrn);

        for (Datasource dataSource : allDataSources) {
            if (datasourcesUsed.indexOf(dataSource.getIdentifiableStatisticalResource().getCode()) == -1) {
                codesDataSourcesDeleted.add(dataSource.getIdentifiableStatisticalResource().getCode());
                urnsDataSourcesDeleted.add(dataSource.getIdentifiableStatisticalResource().getUrn());
            }
        }

        for (String dataSourceUrn : urnsDataSourcesDeleted) {
            log.info("Deleting a not used datasource. Dataset = {}, Datasource = {}", new Object[]{datasetUrn, dataSourceUrn});
            deleteDatasource(ctx, dataSourceUrn, deleteAttributes);
        }

        return codesDataSourcesDeleted;
    }

    @Override
    public DimensionRepresentationMapping retrieveDimensionRepresentationMapping(ServiceContext ctx, String datasetUrn, String filename) throws MetamacException {

        // Validation
        datasetServiceInvocationValidator.checkRetrieveDimensionRepresentationMapping(ctx, datasetUrn, filename);

        // Retrieve
        return getDimensionRepresentationMappingRepository().findByDatasetAndDatasourceFilename(datasetUrn, filename);
    }

    private void deleteDatasourceDimensionRepresentationMappings(DatasetVersion datasetVersion, Datasource datasource) throws MetamacException {
        String filename = datasource.getSourceName();
        List<Datasource> datasources = retrieveDatasourcesByDatasetAndSourceName(datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), filename);
        // The dimension representation mapping is only deleted if there is no more datasources associated with the same file
        if (datasources.isEmpty()) {
            DimensionRepresentationMapping dimensionRepresentationMapping = getDimensionRepresentationMappingRepository()
                    .findByDatasetAndDatasourceFilename(datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn(), filename);
            if (dimensionRepresentationMapping != null) {
                getDimensionRepresentationMappingRepository().delete(dimensionRepresentationMapping);
            }
        }
    }

    private List<Datasource> retrieveDatasourcesByDatasetAndSourceName(String datasetVersionUrn, String sourceName) {
        List<ConditionalCriteria> condition = criteriaFor(Datasource.class).withProperty(DatasourceProperties.datasetVersion().siemacMetadataStatisticalResource().urn()).eq(datasetVersionUrn).and()
                .withProperty(DatasourceProperties.sourceName()).eq(sourceName).distinctRoot().build();
        return getDatasourceRepository().findByCondition(condition);
    }

    private void deleteAttributeInstancesLowerThanDatasetLevel(DatasetVersion datasetVersion) throws MetamacException {
        DataStructure dsd = srmRestInternalService.retrieveDsdByUrn(datasetVersion.getRelatedDsd().getUrn());

        List<DsdAttribute> attributes = DsdProcessor.getAttributes(dsd);
        for (DsdAttribute attribute : attributes) {
            if (attribute.isDimensionAttribute() || attribute.isObservationAttribute()) {
                deleteAllAttributeInstances(datasetVersion, attribute);
            }
        }
    }

    private void deleteAllAttributeInstances(DatasetVersion datasetVersion, DsdAttribute attribute) throws MetamacException {
        try {
            List<AttributeInstanceDto> attributeInstances = statisticsDatasetRepositoriesServiceFacade.findAttributesInstances(datasetVersion.getDatasetRepositoryId(), attribute.getComponentId());
            for (AttributeInstanceDto instance : attributeInstances) {
                statisticsDatasetRepositoriesServiceFacade.deleteAttributeInstance(instance.getUuid());
            }
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN,
                    "Error finding and deleting attribute instances in dataset repository: " + datasetVersion.getDatasetRepositoryId() + " for attribute " + attribute.getComponentId());
        }
    }

    private void checkDatasetVersionForDatasourceHasNoQueries(Datasource datasource, String datasetUrn) throws MetamacException {

        Boolean isDataSourceUsed = checkExistsAttributeInstanceValues(datasetUrn, datasource.getIdentifiableStatisticalResource().getCode());

        List<QueryVersion> queries = queryVersionRepository.findLinkedToFixedDatasetVersion(datasource.getDatasetVersion().getId());
        List<QueryVersion> queriesDataset = queryVersionRepository.findLinkedToDataset(datasource.getDatasetVersion().getDataset().getId());
        if ((!queries.isEmpty() || !queriesDataset.isEmpty()) && isDataSourceUsed) {
            throw new MetamacException(ServiceExceptionType.DATASOURCE_IN_DATASET_VERSION_WITH_QUERIES_DELETE_ERROR, datasource.getIdentifiableStatisticalResource().getUrn());
        }
    }

    private int deleteDatasourceData(String datasetId, Datasource datasource) throws MetamacException {
        try {
            InternationalStringDto internationalStringDto = new InternationalStringDto();
            LocalisedStringDto localisedStringDto = new LocalisedStringDto();
            localisedStringDto.setLabel(datasource.getIdentifiableStatisticalResource().getCode());
            localisedStringDto.setLocale(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE);
            internationalStringDto.addText(localisedStringDto);

            log.info("Deleting a datasource is trying to delete observations by attribute instance value. Dataset = {}, Datasource = {}",
                    new Object[]{datasetId, datasource.getIdentifiableStatisticalResource().getCode()});

            return statisticsDatasetRepositoriesServiceFacade.deleteObservationsByAttributeInstanceValue(datasetId, StatisticalResourcesConstants.ATTRIBUTE_DATA_SOURCE_ID, internationalStringDto);

        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATASOURCE_DATA_DELETE_ERROR, datasource.getIdentifiableStatisticalResource().getCode());
        }
    }

    private Boolean checkExistsAttributeInstanceValues(String datasetUrn, String dataSourceAtttributeUrn) throws MetamacException {
        try {

            return statisticsDatasetRepositoriesServiceFacade.checkExistsAttributeInstanceValues(datasetUrn, StatisticalResourcesConstants.ATTRIBUTE_DATA_SOURCE_ID,
                    ManipulateDataUtils.getLocaleDatasourceIdentificationAttribute(), dataSourceAtttributeUrn);

        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATASOURCE_IN_DATASET_VERSION_CHECK_DATASOURCE_IS_USED_ERROR, dataSourceAtttributeUrn, datasetUrn);
        }
    }

    private List<String> findDataSourcesUsedInDataset(String datasetUrn) throws MetamacException {
        try {

            return statisticsDatasetRepositoriesServiceFacade.findDataSourcesAttributesByDatasetId(datasetUrn, StatisticalResourcesConstants.ATTRIBUTE_DATA_SOURCE_ID,
                    ManipulateDataUtils.getLocaleDatasourceIdentificationAttribute());

        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATASOURCE_IN_DATASET_VERSION_FIND_DATASOURCES_USED_ERROR, datasetUrn);
        }
    }

    @Override
    public List<Datasource> retrieveDatasourcesByDatasetVersion(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {

        // Validation
        datasetServiceInvocationValidator.checkRetrieveDatasourcesByDatasetVersion(ctx, datasetVersionUrn);

        // Retrieve
        DatasetVersion datasetVersion = getDatasetVersionRepository().retrieveByUrn(datasetVersionUrn);
        List<Datasource> datasources = datasetVersion.getDatasources();

        return datasources;
    }

    // ------------------------------------------------------------------------
    // DATASETS VERSIONS
    // ------------------------------------------------------------------------

    @Override
    public DatasetVersion createDatasetVersion(ServiceContext ctx, DatasetVersion datasetVersion, ExternalItem statisticalOperation) throws MetamacException {
        // Validations
        datasetServiceInvocationValidator.checkCreateDatasetVersion(ctx, datasetVersion, statisticalOperation);

        // Create dataset
        Dataset dataset = new Dataset();
        fillMetadataForCreateDataset(ctx, dataset, statisticalOperation);

        // Fill metadata
        fillMetadataForCreateDatasetVersion(ctx, datasetVersion, statisticalOperation);

        // Save version
        datasetVersion.setDataset(dataset);

        assignCodeAndSaveDataset(dataset, datasetVersion);

        datasetVersion = getDatasetVersionRepository().retrieveByUrn(datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        DatasetRepositoryDto datasetRepositoryDto = createDatasetRepository(ctx, datasetVersion);
        datasetVersion.setDatasetRepositoryId(datasetRepositoryDto.getDatasetId());

        return getDatasetVersionRepository().save(datasetVersion);
    }

    @Override
    public DatasetVersion copyDatasetVersion(ServiceContext ctx, String urn) throws MetamacException {
        // Find entity
        DatasetVersion datasetVersion = getDatasetVersionRepository().retrieveByUrn(urn);
        DatasetVersion datasetVersionCopy = DatasetVersioningCopyUtils.copyDatasetVersion(datasetVersion);
        datasetVersionCopy.setVersion(null);
        datasetVersionCopy.getSiemacMetadataStatisticalResource().setStatisticalOperation(null);
        datasetVersionCopy.getSiemacMetadataStatisticalResource().setCreatedDate(null);
        datasetVersionCopy.setDataset(null);
        datasetVersionCopy.getDatasources().clear();
        copyCategorisations(ctx, datasetVersionCopy.getCategorisations());
        datasetVersionCopy.getDimensionsCoverage().clear();
        return createDatasetVersion(ctx, datasetVersionCopy, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation());
    }

    private void copyCategorisations(ServiceContext ctx, List<Categorisation> categorisations) throws MetamacException {
        for (Categorisation categorisation : categorisations) {
            initializeCategorisationMetadataForCreation(ctx, categorisation);
        }
    }

    private DatasetRepositoryDto createDatasetRepository(ServiceContext ctx, DatasetVersion datasetVersion) throws MetamacException {
        try {
            DatasetRepositoryDto datasetRepositoryDto = new DatasetRepositoryDto();
            datasetRepositoryDto.setDatasetId(datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
            datasetRepositoryDto.setTableName(DatasetVersionUtils.generateDatasetRepositoryTableName(datasetVersion.getSiemacMetadataStatisticalResource()));

            DataStructure dsd = srmRestInternalService.retrieveDsdByUrn(datasetVersion.getRelatedDsd().getUrn());

            List<DsdDimension> dimensions = DsdProcessor.getDimensions(dsd);
            for (DsdDimension dsdDimension : dimensions) {
                DimensionDto dimension = new DimensionDto();
                dimension.setDimensionId(dsdDimension.getComponentId());
                dimension.setSourceUrn(getDataSourceUrnForEnumeratedDimensions(dsdDimension));
                datasetRepositoryDto.getDimensions().add(dimension);
            }

            // Attributes
            List<DsdAttribute> attributes = DsdProcessor.getAttributes(dsd);
            datasetRepositoryDto.getAttributes().addAll(ManipulateDataUtils.extractDefinitionOfAttributes(attributes));

            datasetRepositoryDto.setLanguages(Arrays.asList(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE));

            DatasetRepositoryDto datasetRepositoryDto2 = statisticsDatasetRepositoriesServiceFacade.createDatasetRepository(datasetRepositoryDto);

            manageDatabaseView(ctx, datasetRepositoryDto2.getDatasetId(), datasetVersion);

            return datasetRepositoryDto2;

        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error in creation of datasetRepository for dataset version" + datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        }
    }

    private String getDataSourceUrnForEnumeratedDimensions(DsdDimension dsdDimension) {
        if (!DsdComponentType.TEMPORAL.equals(dsdDimension.getType()) && dsdDimension.getTextFormatRepresentation() == null) {
            if (dsdDimension.getCodelistRepresentationUrn() != null) {
                return dsdDimension.getCodelistRepresentationUrn();

            } else if (dsdDimension.getConceptSchemeRepresentationUrn() != null) {
                return dsdDimension.getConceptSchemeRepresentationUrn();
            }

        }

        return null;
    }

    @Override
    public void manageDatabaseView(ServiceContext ctx, String datasetRepositoryId, DatasetVersion datasetVersion) throws MetamacException {
        datasetServiceInvocationValidator.checkManageDatabaseView(ctx, datasetRepositoryId, datasetVersion);

        createOrReplaceLastVersionDatabaseView(datasetRepositoryId, datasetVersion);
        assignStatisticalResourcesDataRolePermissionsToView(datasetVersion.getDataset().getViewCode());
    }

    private void createOrReplaceLastVersionDatabaseView(String datasetRepositoryId, DatasetVersion datasetVersion) throws MetamacException {
        String viewCode = datasetVersion.getDataset().getViewCode();

        try {
            List<String> languages = configurationService.retrieveInternationalizationLanguages();
            statisticsDatasetRepositoriesServiceFacade.createOrReplaceDatasetRepositoryView(datasetRepositoryId, viewCode, languages,
                    Arrays.asList(StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID), new ArrayList<>());
        } catch (Exception e) {
            log.error("Error creating or replacing view " + viewCode + " for datasetRepositoryId " + datasetRepositoryId, e);
            noticesRestInternalService.createCreateReplaceDatasetErrorBackgroundNotification(datasetVersion, viewCode, datasetRepositoryId);
        }
    }

    private void assignStatisticalResourcesDataRolePermissionsToView(String viewCode) throws MetamacException {
        String dataViewRole = getDataViewsRole();

        try {
            statisticsDatasetRepositoriesServiceFacade.assignRolePermissionsToSelectDatasetView(dataViewRole, viewCode);
        } catch (Exception e) {
            log.error("Error assigning SELECT permission to " + dataViewRole + " over " + viewCode, e);
            noticesRestInternalService.createAssignRolePermissionsDatasetErrorBackgroundNotification(dataViewRole, viewCode);
        }
    }

    private String getDataViewsRole() throws MetamacException {
        return configurationService.retrieveDbDataViewsRole();
    }

    @Override
    public DatasetVersion updateDatasetVersion(ServiceContext ctx, DatasetVersion datasetVersion) throws MetamacException {
        // Validations
        datasetServiceInvocationValidator.checkUpdateDatasetVersion(ctx, datasetVersion);

        checkNotTasksInProgress(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());

        checkDsdChanges(datasetVersion);

        // Check status
        ProcStatusValidator.checkStatisticalResourceCanBeEdited(datasetVersion);

        identifiableStatisticalResourceRepository.checkDuplicatedUrn(datasetVersion.getSiemacMetadataStatisticalResource());

        if (datasetVersion.isRelatedDsdChanged()) {
            clearDataRelatedMetadata(ctx, datasetVersion);
        }

        datasetVersion = getDatasetVersionRepository().save(datasetVersion);
        return datasetVersion;
    }

    private void checkDsdChanges(DatasetVersion datasetVersion) throws MetamacException {
        if (datasetVersion.isRelatedDsdChanged()) {
            List<QueryVersion> queriesLinkedToDatasetVersion = queryVersionRepository.findLinkedToFixedDatasetVersion(datasetVersion.getId());
            List<QueryVersion> queriesLinkedToDataset = queryVersionRepository.findLinkedToDataset(datasetVersion.getDataset().getId());
            if (!queriesLinkedToDataset.isEmpty() || !queriesLinkedToDatasetVersion.isEmpty()) {
                throw new MetamacException(ServiceExceptionType.DATASET_VERSION_CANT_CHANGE_DSD_SOME_QUERIES_EXIST, datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
            }
        }
    }

    private void clearDataRelatedMetadata(ServiceContext ctx, DatasetVersion resource) throws MetamacException {
        // Clear datasources
        for (Datasource datasource : resource.getDatasources()) {
            getDatasourceRepository().delete(datasource);
        }
        resource.getDatasources().clear();
        resource.getSiemacMetadataStatisticalResource().setLastUpdate(new DateTime());
        resource.setDateLastTimeDataImport(null);

        // Clear coverages
        resource.getDimensionsCoverage().clear();
        resource.getGeographicCoverage().clear();
        resource.getTemporalCoverage().clear();
        resource.getMeasureCoverage().clear();

        // Date start and end
        resource.setDateStart(null);
        resource.setDateEnd(null);
        resource.setDateStartTimestamp(null);
        resource.setDateEndTimestamp(null);

        // Format extent
        resource.setFormatExtentDimensions(null);
        resource.setFormatExtentObservations(null);
        resource.setFormatExtentTableSize(null);

        // Date next update
        if (BooleanUtils.isNotTrue(resource.getUserModifiedDateNextUpdate())) {
            resource.setDateNextUpdate(null);
        }

        // Dataset repository
        try {
            log.info("The dataset table will be deleted from the repository when trying to delete data related to the dataset metadata. Dataset = {}", resource.getDatasetRepositoryId());
            statisticsDatasetRepositoriesServiceFacade.deleteDatasetRepository(resource.getDatasetRepositoryId());
            resource.setDatasetRepositoryId(null);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error removing datasetRepository " + resource.getDatasetRepositoryId());
        }

        DatasetRepositoryDto datasetRepository = createDatasetRepository(ctx, resource);
        resource.setDatasetRepositoryId(datasetRepository.getDatasetId());
        resource.getStubDimensions().clear();
        resource.getHeadingDimensions().clear();
    }

    @Override
    public DatasetVersion retrieveDatasetVersionByUrn(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        // Validations
        datasetServiceInvocationValidator.checkRetrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        // Retrieve
        DatasetVersion datasetVersion = getDatasetVersionRepository().retrieveByUrn(datasetVersionUrn);
        return datasetVersion;
    }

    @Override
    public DatasetVersion retrieveLatestDatasetVersionByDatasetUrn(ServiceContext ctx, String datasetUrn) throws MetamacException {
        // Validations
        datasetServiceInvocationValidator.checkRetrieveLatestDatasetVersionByDatasetUrn(ctx, datasetUrn);

        // Retrieve
        DatasetVersion datasetVersion = getDatasetVersionRepository().retrieveLastVersion(datasetUrn);
        return datasetVersion;
    }

    @Override
    public DatasetVersion retrieveLatestPublishedDatasetVersionByDatasetUrn(ServiceContext ctx, String datasetUrn) throws MetamacException {
        // Validations
        datasetServiceInvocationValidator.checkRetrieveLatestPublishedDatasetVersionByDatasetUrn(ctx, datasetUrn);

        // Retrieve
        DatasetVersion datasetVersion = getDatasetVersionRepository().retrieveLastPublishedVersion(datasetUrn);
        return datasetVersion;
    }

    @Override
    public List<DatasetVersion> retrieveDatasetVersions(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        // Validations
        datasetServiceInvocationValidator.checkRetrieveDatasetVersions(ctx, datasetVersionUrn);

        // Retrieve
        List<DatasetVersion> datasetVersions = getDatasetVersionRepository().retrieveByUrn(datasetVersionUrn).getDataset().getVersions();

        return datasetVersions;
    }

    @Override
    public PagedResult<DatasetVersion> findDatasetVersionsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validations
        datasetServiceInvocationValidator.checkFindDatasetVersionsByCondition(ctx, conditions, pagingParameter);

        // Find
        conditions = CriteriaUtils.initConditions(conditions, DatasetVersion.class);
        pagingParameter = CriteriaUtils.initPagingParameter(pagingParameter);

        PagedResult<DatasetVersion> datasetVersionPagedResult = getDatasetVersionRepository().findByCondition(conditions, pagingParameter);
        return datasetVersionPagedResult;
    }

    @Override
    public void deleteDatasetVersion(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        // Validations
        datasetServiceInvocationValidator.checkDeleteDatasetVersion(ctx, datasetVersionUrn);

        // Retrieve version to delete
        DatasetVersion datasetVersion = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        checkNotTasksInProgress(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());

        // Check can be deleted
        ProcStatusValidator.checkStatisticalResourceCanBeDeleted(datasetVersion);

        checkCanDatasetVersionBeDeleted(ctx, datasetVersion);

        updateReplacedResourceIsReplacedByResource(datasetVersion);

        String datasetRepositoryId = datasetVersion.getDatasetRepositoryId();
        String viewCode = datasetVersion.getDataset().getViewCode();

        // Remove dataset version
        if (StatisticalResourcesVersionUtils.isInitialVersion(datasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic())) {
            Dataset dataset = datasetVersion.getDataset();
            getDatasetRepository().delete(dataset);

            tryDeleteDatabaseView(viewCode);
        } else {
            // Previous version
            updateReplacedVersionIsReplacedByVersion(datasetVersion);

            manageDatabaseView(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getReplacesVersion().getDatasetVersion().getDatasetRepositoryId(), datasetVersion);

            // Delete version
            Dataset dataset = datasetVersion.getDataset();
            dataset.getVersions().remove(datasetVersion);
            getDatasetVersionRepository().delete(datasetVersion);
        }

        // Remove associated constraints
        constraintsService.deleteContentConstraintsForArtefactUrn(ctx, datasetVersionUrn);

        // Remove data dataset-repository
        log.info("The dataset table will be deleted from the repository when trying to delete the dataset version. Dataset = {}", datasetRepositoryId);
        tryToDeleteDatasetRepository(datasetRepositoryId);
    }

    private void updateReplacedVersionIsReplacedByVersion(DatasetVersion datasetVersion) {
        RelatedResource previousResource = datasetVersion.getSiemacMetadataStatisticalResource().getReplacesVersion();
        if (previousResource.getDatasetVersion() != null) {
            DatasetVersion previousVersion = previousResource.getDatasetVersion();
            previousVersion.getSiemacMetadataStatisticalResource().setLastVersion(true);
            RelatedResource isReplacedByVersion = previousVersion.getSiemacMetadataStatisticalResource().getIsReplacedByVersion();
            relatedResourceRepository.delete(isReplacedByVersion);
            previousVersion.getSiemacMetadataStatisticalResource().setIsReplacedByVersion(null);
            getDatasetVersionRepository().save(previousVersion);
        }
    }

    private void updateReplacedResourceIsReplacedByResource(DatasetVersion datasetVersion) {
        RelatedResource previousResource = datasetVersion.getSiemacMetadataStatisticalResource().getReplaces();
        if (previousResource != null && previousResource.getDatasetVersion() != null) {
            DatasetVersion previousVersion = previousResource.getDatasetVersion();
            RelatedResource isReplacedBy = previousVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy();
            relatedResourceRepository.delete(isReplacedBy);
            previousVersion.getSiemacMetadataStatisticalResource().setIsReplacedBy(null);
            getDatasetVersionRepository().save(previousVersion);
        }
    }

    private void checkCanDatasetVersionBeDeleted(ServiceContext ctx, DatasetVersion datasetVersion) throws MetamacException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();

        boolean isOnlyVersion = StatisticalResourcesVersionUtils.isInitialVersion(datasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic());

        if (isOnlyVersion) {
            checkDatasetVersionIsPartOfSomePublication(datasetVersion, exceptionItems);
        }

        checkDatasetVersionIsReplacedBySomeDataset(datasetVersion, exceptionItems);

        checkDatasetVersionIsRequiredBySomeQuery(ctx, datasetVersion, exceptionItems);

        if (exceptionItems.size() > 0) {
            MetamacExceptionItem item = new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_CANT_BE_DELETED, datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
            item.setExceptionItems(exceptionItems);
            throw new MetamacException(Arrays.asList(item));
        }
    }

    protected void checkDatasetVersionIsRequiredBySomeQuery(ServiceContext ctx, DatasetVersion datasetVersion, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        boolean isOnlyVersion = StatisticalResourcesVersionUtils.isInitialVersion(datasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic());

        List<RelatedResourceResult> resourcesIsRequiredBy = datasetVersionRepository.retrieveIsRequiredBy(datasetVersion);
        if (!resourcesIsRequiredBy.isEmpty()) {
            if (isOnlyVersion) {
                List<String> urns = getUrnsFromRelatedResourceResults(resourcesIsRequiredBy);
                Collections.sort(urns);
                String parameter = StringUtils.join(urns, ", ");
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_IS_REQUIRED_BY_OTHER_RESOURCES, parameter));
            } else {
                DatasetVersion lastPublishedVersion = datasetVersionRepository.retrieveLastPublishedVersion(datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());
                List<String> urns = getUrnsFromRelatedResourceResults(resourcesIsRequiredBy);
                List<String> incompatibleUrns = new ArrayList<String>();
                for (String queryUrn : urns) {
                    QueryVersion queryVersion = queryVersionRepository.retrieveByUrn(queryUrn);
                    if (!queryService.checkQueryCompatibility(ctx, queryVersion, lastPublishedVersion)) {
                        incompatibleUrns.add(queryUrn);
                    }
                }
                if (!incompatibleUrns.isEmpty()) {
                    Collections.sort(urns);
                    String parameter = StringUtils.join(urns, ", ");
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_IS_REQUIRED_BY_OTHER_RESOURCES_LAST_VERSION_INCOMPATIBLE, parameter));
                }
            }
        }

    }

    protected void checkDatasetVersionIsReplacedBySomeDataset(DatasetVersion datasetVersion, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        RelatedResource resourceIsReplacedBy = datasetVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy();
        if (resourceIsReplacedBy != null) {
            exceptionItems.add(
                    new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_IS_REPLACED_BY_OTHER_RESOURCE, resourceIsReplacedBy.getDatasetVersion().getLifeCycleStatisticalResource().getUrn()));
        }
    }

    protected void checkDatasetVersionIsPartOfSomePublication(DatasetVersion datasetVersion, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        List<RelatedResourceResult> resourcesIsPartOf = datasetVersionRepository.retrieveIsPartOf(datasetVersion);
        if (!resourcesIsPartOf.isEmpty()) {
            List<String> urns = getUrnsFromRelatedResourceResults(resourcesIsPartOf);
            Collections.sort(urns);
            String parameter = StringUtils.join(urns, ", ");
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_IS_PART_OF_OTHER_RESOURCES, parameter));
        }
    }

    @Override
    public void importDatasourcesInDatasetVersion(ServiceContext ctx, String datasetVersionUrn, List<URL> fileUrls, Map<String, String> dimensionRepresentationMapping,
            boolean storeDimensionRepresentationMapping, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto) throws MetamacException {
        importDatasourcesInDatasetVersion(ctx, datasetVersionUrn, fileUrls, dimensionRepresentationMapping, storeDimensionRepresentationMapping, DataSourceTypeEnum.FILE,
                basicVersionableStatisticalResourceDto);
    }

    @Override
    public void importAttributesFromFile(ServiceContext ctx, String datasetVersionUrn, List<URL> fileUrls) throws MetamacException {
        datasetServiceInvocationValidator.checkImportAttributesFromFile(ctx, datasetVersionUrn, fileUrls);
        DatasetVersion datasetVersion = getDatasetVersionRepository().retrieveByUrn(datasetVersionUrn);

        checkNotTasksInProgress(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());
        TaskInfoDataset taskInfo = buildImportationTaskInfo(datasetVersion, fileUrls, new HashMap<>(), null, null);
        getTaskService().planifyImportationAttributes(ctx, taskInfo);
    }

    @Override
    public void importDatabaseDatasourcesInDatasetVersion(ServiceContext ctx, String datasetVersionUrn, List<URL> fileUrls, Map<String, String> dimensionRepresentationMapping,
            boolean storeDimensionRepresentationMapping) throws MetamacException {
        importDatasourcesInDatasetVersion(ctx, datasetVersionUrn, fileUrls, dimensionRepresentationMapping, storeDimensionRepresentationMapping, DataSourceTypeEnum.DATABASE,
                new BasicVersionableStatisticalResourceDto());
    }

    private void importDatasourcesInDatasetVersion(ServiceContext ctx, String datasetVersionUrn, List<URL> fileUrls, Map<String, String> dimensionRepresentationMapping,
            boolean storeDimensionRepresentationMapping, DataSourceTypeEnum expectedDataSourceTypeEnum, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto)
            throws MetamacException {
        datasetServiceInvocationValidator.checkImportDatasourcesInDatasetVersion(ctx, datasetVersionUrn, fileUrls, dimensionRepresentationMapping, storeDimensionRepresentationMapping,
                basicVersionableStatisticalResourceDto);

        DatasetVersion datasetVersion = getDatasetVersionRepository().retrieveByUrn(datasetVersionUrn);

        checkNotTasksInProgress(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());

        checkValidDataSourceTypeForImportationTask(expectedDataSourceTypeEnum, datasetVersion);

        if (DataSourceTypeEnum.FILE.equals(datasetVersion.getDataSourceType())) {
            ProcStatusValidator.checkDatasetVersionCanImportDatasources(datasetVersion, basicVersionableStatisticalResourceDto);
        }

        String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();

        checkFilesCanBeAssociatedWithDataset(datasetUrn, datasetVersionUrn, fileUrls);

        TaskInfoDataset taskInfo = buildImportationTaskInfo(datasetVersion, fileUrls, dimensionRepresentationMapping, storeDimensionRepresentationMapping, basicVersionableStatisticalResourceDto);

        getTaskService().planifyImportationDataset(ctx, taskInfo);
    }

    private TaskInfoDataset buildImportationTaskInfo(DatasetVersion datasetVersion, List<URL> fileUrls, Map<String, String> dimensionRepresentationMapping, Boolean storeDimensionRepresentationMapping,
            BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto) {
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        TaskInfoDataset taskInfo = new TaskInfoDataset();
        taskInfo.setDatasetUrn(datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());
        taskInfo.setDatasetVersionId(datasetVersionUrn);
        taskInfo.setDataStructureUrn(datasetVersion.getRelatedDsd().getUrn());
        taskInfo.setStoreAlternativeRepresentations(storeDimensionRepresentationMapping);
        taskInfo.setStatisticalOperationUrn(datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getUrn());
        taskInfo.setDatasetVersionCode(datasetVersion.getSiemacMetadataStatisticalResource().getCode());
        if (basicVersionableStatisticalResourceDto != null) {
            taskInfo.setDatasetNextVersion(basicVersionableStatisticalResourceDto.getNextVersion());
            taskInfo.setDatasetNextVersionDate(basicVersionableStatisticalResourceDto.getNextVersionDate());
            taskInfo.setDatasetNextUpdateDate(basicVersionableStatisticalResourceDto.getNextUpdateDate());
            taskInfo.setDatasetUpdateFrequency(basicVersionableStatisticalResourceDto.getUpdateFrequency());
            taskInfo.setDatasetVersionDataProviderUrn(basicVersionableStatisticalResourceDto.getDataProvidersUrn());
            taskInfo.setDatasetVersionRationaleTypes(basicVersionableStatisticalResourceDto.getVersionRationaleTypes());
            taskInfo.setDatasetNextProcStatus(basicVersionableStatisticalResourceDto.getNextProcStatus());
            taskInfo.setDatasetAutomaticLifeCicle(basicVersionableStatisticalResourceDto.getAutomaticLifeCicle());
            taskInfo.setVersionRationale(basicVersionableStatisticalResourceDto.getVersionRationale());
        }
        for (String dimensionId : dimensionRepresentationMapping.keySet()) {
            AlternativeEnumeratedRepresentation representation = new AlternativeEnumeratedRepresentation();
            representation.setComponentId(dimensionId);
            representation.setUrn(dimensionRepresentationMapping.get(dimensionId));
            taskInfo.getAlternativeRepresentations().add(representation);
        }

        for (URL url : fileUrls) {
            String filename = getFilenameFromPath(url.getPath());
            DatasetFileFormatEnum format = calculateFileFormat(filename);
            FileDescriptor fileDescriptor = new FileDescriptor(new File(url.getPath()), filename, format);
            taskInfo.addFile(fileDescriptor);
        }

        return taskInfo;
    }

    private void checkFilesCanBeAssociatedWithDataset(String datasetUrn, String datasetVersionUrn, List<URL> fileUrls) throws MetamacException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        String encoding = StringUtils.isEmpty(System.getProperty("file.encoding")) ? "UTF-8" : System.getProperty("file.encoding");

        for (URL url : fileUrls) {
            try {
                String filename = URLDecoder.decode(getFilenameFromPath(url.getPath()), encoding);
                String linkedDatasetVersionUrn = getDatasetRepository().findDatasetUrnLinkedToDatasourceSourceName(filename);
                if (linkedDatasetVersionUrn != null && !StringUtils.equals(datasetUrn, linkedDatasetVersionUrn)) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.INVALID_FILE_FOR_DATASET_VERSION, filename, datasetVersionUrn));
                }
            } catch (UnsupportedEncodingException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.FILE_ENCODING_ERROR).withMessageParameters(getFilenameFromPath(url.getPath())).build();
            }
        }
        if (!exceptionItems.isEmpty()) {
            throw new MetamacException(exceptionItems);
        }
    }

    private DatasetFileFormatEnum calculateFileFormat(String filename) {
        if (filename.endsWith(StatisticalResourcesConstants.PX_EXTENSION)) {
            return DatasetFileFormatEnum.PX;
        } else if (filename.endsWith(StatisticalResourcesConstants.SDMX_EXTENSION)) {
            return DatasetFileFormatEnum.SDMX_2_1;
        } else {
            return DatasetFileFormatEnum.CSV;
        }
    }

    private String getFilenameFromPath(String path) {
        String base = FilenameUtils.getBaseName(path);
        String extension = FilenameUtils.getExtension(path);
        if (StringUtils.isEmpty(extension)) {
            return base;
        }
        return base + "." + extension;
    }

    @Override
    public MetamacException importDatasourcesInStatisticalOperation(ServiceContext ctx, String statisticalOperationCode, List<URL> fileUrls,
            BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto) throws MetamacException {
        datasetServiceInvocationValidator.checkImportDatasourcesInStatisticalOperation(ctx, statisticalOperationCode, fileUrls, basicVersionableStatisticalResourceDto);

        Map<String, List<URL>> datasetVersionsForFiles = organizeFilesByDatasetVersionCode(statisticalOperationCode, fileUrls);

        List<MetamacExceptionItem> items = new ArrayList<MetamacExceptionItem>();
        for (String datasetVersionUrn : datasetVersionsForFiles.keySet()) {
            try {
                List<URL> urls = datasetVersionsForFiles.get(datasetVersionUrn);
                HashMap<String, String> dimensionRepresentationMapping = new HashMap<String, String>();
                boolean storeDimensionRepresentationMapping = false;

                importDatasourcesInDatasetVersion(ctx, datasetVersionUrn, urls, dimensionRepresentationMapping, storeDimensionRepresentationMapping, basicVersionableStatisticalResourceDto);
            } catch (MetamacException e) {
                MetamacExceptionItem item = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_DATASET_VERSION_ERROR, datasetVersionUrn);
                item.setExceptionItems(e.getExceptionItems());
                items.add(item);
            }
        }
        if (!items.isEmpty()) {
            return new MetamacException(items);
        }
        return null;
    }

    protected Map<String, List<URL>> organizeFilesByDatasetVersionCode(String statisticalOperationCode, List<URL> fileUrls) throws MetamacException {
        Map<String, List<URL>> datasetVersionsForFiles = new HashMap<String, List<URL>>();
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        String encoding = StringUtils.isEmpty(System.getProperty("file.encoding")) ? "UTF-8" : System.getProperty("file.encoding");

        for (URL url : fileUrls) {
            try {
                String filename = URLDecoder.decode(getFilenameFromPath(url.getPath()), encoding);
                String datasetUrn = getDatasetRepository().findDatasetUrnLinkedToDatasourceSourceName(filename);
                DatasetVersion datasetVersion = null;
                if (datasetUrn != null) {
                    datasetVersion = getDatasetVersionRepository().retrieveLastVersion(datasetUrn);
                }

                if (datasetVersion != null && StringUtils.equals(statisticalOperationCode, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode())) {
                    StatisticalResourcesCollectionUtils.addValueToMapValueList(datasetVersionsForFiles, datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), url);
                } else {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.FILE_NOT_LINKED_TO_ANY_DATASET_IN_STATISTICAL_OPERATION, filename, statisticalOperationCode));
                }
            } catch (UnsupportedEncodingException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.FILE_ENCODING_ERROR).withMessageParameters(getFilenameFromPath(url.getPath())).build();
            }
        }
        if (exceptionItems.size() > 0) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(exceptionItems).build();
        }
        return datasetVersionsForFiles;
    }

    @Override
    public void proccessDatasetFileImportationResult(ServiceContext ctx, String datasetImportationId, List<FileDescriptorResult> fileDescriptors) throws MetamacException {
        String datasetVersionUrn = datasetImportationId;
        datasetServiceInvocationValidator.checkProccessDatasetFileImportationResult(ctx, datasetImportationId, fileDescriptors);

        DatasetVersion datasetVersion = getDatasetVersionRepository().retrieveByUrn(datasetVersionUrn);
        datasetVersion.setDatasetRepositoryId(datasetImportationId);
        datasetVersion.setDateLastTimeDataImport(getDateLastTimeDataImport(ctx));

        getDatasetVersionRepository().save(datasetVersion);

        if (!DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(ctx)) {
            for (FileDescriptorResult fileDescriptor : fileDescriptors) {
                Datasource datasource = new Datasource();
                datasource.setIdentifiableStatisticalResource(new IdentifiableStatisticalResource());
                datasource.getIdentifiableStatisticalResource().setCode(fileDescriptor.getDatasourceId());
                datasource.setSourceName(fileDescriptor.getFileName());
                if (DatasetFileFormatEnum.PX.equals(fileDescriptor.getDatasetFileFormatEnum())) {
                    datasource.setDateNextUpdate(new DateTime(fileDescriptor.getNextUpdate()));
                }
                createDatasource(ctx, datasetImportationId, datasource);
            }
        } else {
            updateAutomaticDatasource(datasetVersion);
        }
    }

    private DateTime getDateLastTimeDataImport(ServiceContext ctx) {
        return (DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(ctx) ? (DateTime) ctx.getProperty(ImportDatasetFromDatabaseJob.DATABASE_IMPORT_JOB_EXECUTION_DATE) : new DateTime());
    }

    @Override
    public List<String> retrieveDatasetVersionDimensionsIds(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        datasetServiceInvocationValidator.checkRetrieveDatasetVersionDimensionsIds(ctx, datasetVersionUrn);

        DatasetVersion datasetVersion = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        checkNotTasksInProgress(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());

        List<String> dimensionsIds = getDatasetVersionRepository().retrieveDimensionsIds(datasetVersion);
        if (!dimensionsIds.isEmpty()) {
            return dimensionsIds;
        } else {
            throw new MetamacException(ServiceExceptionType.DATASET_NO_DATA, datasetVersionUrn);
        }
    }

    @Override
    public List<CodeDimension> retrieveCoverageForDatasetVersionDimension(ServiceContext ctx, String datasetVersionUrn, String dimensionId) throws MetamacException {
        datasetServiceInvocationValidator.checkRetrieveCoverageForDatasetVersionDimension(ctx, datasetVersionUrn, dimensionId);

        DatasetVersion datasetVersion = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        checkNotTasksInProgress(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());

        return getCodeDimensionRepository().findCodesForDatasetVersionByDimensionId(datasetVersion.getId(), dimensionId, null);
    }

    @Override
    public List<CodeDimension> filterCoverageForDatasetVersionDimension(ServiceContext ctx, String datasetVersionUrn, String dimensionId, String filter) throws MetamacException {
        datasetServiceInvocationValidator.checkFilterCoverageForDatasetVersionDimension(ctx, datasetVersionUrn, dimensionId, filter);

        DatasetVersion datasetVersion = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        checkNotTasksInProgress(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());

        return getCodeDimensionRepository().findCodesForDatasetVersionByDimensionId(datasetVersion.getId(), dimensionId, filter);
    }

    @Override
    public DimensionRepresentationMapping saveDimensionRepresentationMapping(ServiceContext ctx, Dataset dataset, String datasourceFilename, Map<String, String> mapping) throws MetamacException {

        datasetServiceInvocationValidator.checkSaveDimensionRepresentationMapping(ctx, dataset, datasourceFilename, mapping);

        DimensionRepresentationMapping dimensionRepresentationMapping = getDimensionRepresentationMappingRepository()
                .findByDatasetAndDatasourceFilename(dataset.getIdentifiableStatisticalResource().getUrn(), datasourceFilename);
        if (dimensionRepresentationMapping == null) {
            dimensionRepresentationMapping = new DimensionRepresentationMapping();
            dimensionRepresentationMapping.setDataset(dataset);
            dimensionRepresentationMapping.setDatasourceFilename(datasourceFilename);
        }
        dimensionRepresentationMapping.setMapping(DatasetVersionUtils.dimensionRepresentationMapToString(mapping));

        if (mapping == null || mapping.isEmpty()) {
            if (dimensionRepresentationMapping.getId() != null) {
                getDimensionRepresentationMappingRepository().delete(dimensionRepresentationMapping);
            }
            return null;
        } else {
            return getDimensionRepresentationMappingRepository().save(dimensionRepresentationMapping);
        }
    }

    // ------------------------------------------------------------------------
    // DATASETS
    // ------------------------------------------------------------------------

    @Override
    public PagedResult<Dataset> findDatasetsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validations
        datasetServiceInvocationValidator.checkFindDatasetsByCondition(ctx, conditions, pagingParameter);

        // Find
        conditions = CriteriaUtils.initConditions(conditions, DatasetVersion.class);
        pagingParameter = CriteriaUtils.initPagingParameter(pagingParameter);

        PagedResult<Dataset> datasetsPagedResult = getDatasetRepository().findByCondition(conditions, pagingParameter);
        return datasetsPagedResult;
    }

    @Override
    public List<StatisticOfficiality> findStatisticOfficialities(ServiceContext ctx) throws MetamacException {
        datasetServiceInvocationValidator.checkFindStatisticOfficialities(ctx);
        return getStatisticOfficialityRepository().findAll();
    }

    // ------------------------------------------------------------------------
    // DATASET ATTRIBUTES
    // ------------------------------------------------------------------------

    @Override
    public AttributeInstanceDto createAttributeInstance(ServiceContext ctx, String datasetVersionUrn, AttributeInstanceDto attributeInstanceDto) throws MetamacException {

        // Validations
        datasetServiceInvocationValidator.checkCreateAttributeInstance(ctx, datasetVersionUrn, attributeInstanceDto);

        // Retrieve the datasetVersion to get the datasetRepositoryId
        DatasetVersion datasetVersion = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        DataStructure dsd = srmRestInternalService.retrieveDsdByUrn(datasetVersion.getRelatedDsd().getUrn());
        DsdAttribute attribute = DsdProcessor.getAttribute(dsd, attributeInstanceDto.getAttributeId());

        checkAttributeInstanceRepresentation(ctx, attributeInstanceDto, datasetVersion, dsd);

        // Create attribute
        AttributeInstanceDto attributeInstance = null;
        try {
            attributeInstance = statisticsDatasetRepositoriesServiceFacade.createAttributeInstance(datasetVersion.getDatasetRepositoryId(), attributeInstanceDto);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error creating attribute instance in datasetRepository " + datasetVersionUrn + ". Details: " + e.getMessage());
        }

        processNonObservationAttributeCoverage(datasetVersion, attribute);

        processSpecificCoveragesFromAttribute(datasetVersion, dsd, attribute);

        getDatasetVersionRepository().save(datasetVersion);

        return attributeInstance;
    }

    protected void checkAttributeInstanceRepresentation(ServiceContext ctx, AttributeInstanceDto attributeInstanceDto, DatasetVersion datasetVersion, DataStructure dsd) throws MetamacException {
        TaskInfoDataset taskInfoDataset = new TaskInfoDataset();
        taskInfoDataset.setDatasetUrn(datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());
        taskInfoDataset.setDatasetVersionId(datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        taskInfoDataset.setDataStructureUrn(datasetVersion.getRelatedDsd().getUrn());
        ValidateDataVersusDsd validator = new ValidateDataVersusDsd(ctx, dsd, srmRestInternalService, null, taskInfoDataset);

        List<AttributeInstanceDto> attributesInstances = Arrays.asList(attributeInstanceDto);

        validator.checkAttributesInstancesRepresentation(attributesInstances);
    }

    protected void processSpecificCoveragesFromAttribute(DatasetVersion datasetVersion, DataStructure dsd, DsdAttribute attribute) throws MetamacException {
        switch (attribute.getType()) {
            case SPATIAL:
                datasetVersion.getGeographicCoverage().clear();
                List<ExternalItem> spatialCodeItems = processExternalItemsCodeFromAttributeByType(datasetVersion, dsd, DsdComponentType.SPATIAL);
                datasetVersion.getGeographicCoverage().addAll(spatialCodeItems);
                break;
            case TEMPORAL:
                datasetVersion.getTemporalCoverage().clear();
                List<CodeDimension> temporalCodeItems = processCodeFromAttributeByType(datasetVersion, dsd, DsdComponentType.TEMPORAL);
                DatasetVersionUtils.sortTemporalCodeDimensions(temporalCodeItems);
                datasetVersion.getTemporalCoverage().addAll(buildTemporalCodeFromCodeDimensions(temporalCodeItems));
                processStartEndDates(datasetVersion);
                break;
            case MEASURE:
                datasetVersion.getMeasureCoverage().clear();
                List<ExternalItem> mesureCodeItems = processExternalItemsCodeFromAttributeByType(datasetVersion, dsd, DsdComponentType.MEASURE);
                datasetVersion.getMeasureCoverage().addAll(mesureCodeItems);
                break;
            default:
                break;
        }
    }

    @Override
    public AttributeInstanceDto updateAttributeInstance(ServiceContext ctx, String datasetVersionUrn, AttributeInstanceDto attributeInstanceDto) throws MetamacException {

        // Validations
        datasetServiceInvocationValidator.checkUpdateAttributeInstance(ctx, datasetVersionUrn, attributeInstanceDto);

        // Retrieve the datasetVersion to get the datasetRepositoryId
        DatasetVersion datasetVersion = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        DataStructure dsd = srmRestInternalService.retrieveDsdByUrn(datasetVersion.getRelatedDsd().getUrn());
        DsdAttribute attribute = DsdProcessor.getAttribute(dsd, attributeInstanceDto.getAttributeId());

        checkAttributeInstanceRepresentation(ctx, attributeInstanceDto, datasetVersion, dsd);

        // Update attribute
        AttributeInstanceDto attributeInstance = null;
        try {
            attributeInstance = statisticsDatasetRepositoriesServiceFacade.updateAttributeInstance(attributeInstanceDto);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error updating attribute instance in datasetRepository " + attributeInstanceDto.getUuid() + ".");
        }

        processNonObservationAttributeCoverage(datasetVersion, attribute);

        processSpecificCoveragesFromAttribute(datasetVersion, dsd, attribute);

        getDatasetVersionRepository().save(datasetVersion);

        return attributeInstance;
    }

    @Override
    public void deleteAttributeInstance(ServiceContext ctx, String datasetVersionUrn, String attributeInstanceUuid) throws MetamacException {
        // Validations
        datasetServiceInvocationValidator.checkDeleteAttributeInstance(ctx, datasetVersionUrn, attributeInstanceUuid);

        // Retrieve the datasetVersion to get the datasetRepositoryId
        DatasetVersion datasetVersion = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        // Delete attribute
        try {
            AttributeInstanceDto attributeInstanceDto = statisticsDatasetRepositoriesServiceFacade.retrieveAttributeInstanceByUuid(attributeInstanceUuid);

            DataStructure dsd = srmRestInternalService.retrieveDsdByUrn(datasetVersion.getRelatedDsd().getUrn());
            DsdAttribute attribute = DsdProcessor.getAttribute(dsd, attributeInstanceDto.getAttributeId());
            statisticsDatasetRepositoriesServiceFacade.deleteAttributeInstance(attributeInstanceUuid);

            processNonObservationAttributeCoverage(datasetVersion, attribute);

            processSpecificCoveragesFromAttribute(datasetVersion, dsd, attribute);

            getDatasetVersionRepository().save(datasetVersion);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error updating attribute instance in datasetRepository " + attributeInstanceUuid + ".");
        }
    }

    @Override
    public List<AttributeInstanceDto> retrieveAttributeInstances(ServiceContext ctx, String datasetVersionUrn, String attributeId) throws MetamacException {

        // Validations
        datasetServiceInvocationValidator.checkRetrieveAttributeInstances(ctx, datasetVersionUrn, attributeId);

        // Retrieve the datasetVersion to get the datasetRepositoryId
        DatasetVersion datasetVersion = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        // Retrieve the attribute instances
        try {
            return statisticsDatasetRepositoriesServiceFacade.findAttributesInstances(datasetVersion.getDatasetRepositoryId(), attributeId);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error retrieve attribute instances in datasetRepository " + datasetVersionUrn + ". Details: " + e.getMessage());
        }
    }

    @Override
    public List<AttributeValue> retrieveCoverageForDatasetVersionAttribute(ServiceContext ctx, String datasetVersionUrn, String dsdAttributeId) throws MetamacException {

        datasetServiceInvocationValidator.checkRetrieveCoverageForDatasetVersionAttribute(ctx, datasetVersionUrn, dsdAttributeId);

        // Retrieve the datasetVersion to get the datasetRepositoryId
        DatasetVersion datasetVersion = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        checkNotTasksInProgress(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());

        return getAttributeValueRepository().findValuesForDatasetVersionByAttributeId(datasetVersion.getId(), dsdAttributeId);
    }

    // ------------------------------------------------------------------------
    // CATEGORISATIONS
    // ------------------------------------------------------------------------

    @Override
    public Categorisation createCategorisation(ServiceContext ctx, String datasetVersionUrn, Categorisation categorisation) throws MetamacException {

        // Validation
        datasetServiceInvocationValidator.checkCreateCategorisation(ctx, datasetVersionUrn, categorisation);

        DatasetVersion datasetVersion = datasetVersionRepository.retrieveByUrn(datasetVersionUrn);

        checkNotTasksInProgress(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());

        if (ProcStatusEnum.PUBLISHED.equals(datasetVersion.getLifeCycleStatisticalResource().getProcStatus())) {
            // Check external items are externally published
            List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
            externalItemChecker.checkExternalItemsExternallyPublished(categorisation.getCategory(), ServiceExceptionParameters.DATASET_VERSION__CATEGORISATIONS, exceptionItems);
            externalItemChecker.checkExternalItemsExternallyPublished(categorisation.getMaintainer(), ServiceExceptionParameters.DATASET_VERSION__CATEGORISATIONS, exceptionItems);
            ExceptionUtils.throwIfException(exceptionItems);
        }

        // Fill metadata
        categorisation.setDatasetVersion(datasetVersion);
        fillMetadataForCreateCategorisation(ctx, categorisation);

        // Save categorisation
        return getCategorisationRepository().save(categorisation);

    }

    @Override
    public void initializeCategorisationMetadataForCreation(ServiceContext ctx, Categorisation categorisation) throws MetamacException {
        if (categorisation.getVersionableStatisticalResource().getCode() != null) {
            throw new MetamacException(ServiceExceptionType.METADATA_UNEXPECTED, ServiceExceptionParameters.CATEGORISATION__CODE);
        }
        String code = siemacStatisticalResourceGeneratedCode.fillGeneratedCodeForCreateCategorisation(categorisation);
        String[] maintainerCodes = new String[]{categorisation.getMaintainer().getCodeNested()};
        categorisation.getVersionableStatisticalResource().setVersionLogic(StatisticalResourcesVersionUtils.INITIAL_VERSION);
        categorisation.getVersionableStatisticalResource().setCode(code);
        categorisation.getVersionableStatisticalResource().setUrn(GeneratorUrnUtils.generateSdmxCategorisationUrn(maintainerCodes, categorisation.getVersionableStatisticalResource().getCode(),
                categorisation.getVersionableStatisticalResource().getVersionLogic()));
        identifiableStatisticalResourceRepository.checkDuplicatedUrn(categorisation.getVersionableStatisticalResource());

        // Title
        InternationalString title = new InternationalString();
        title.addText(new LocalisedString("es", "Categoría " + code));
        title.addText(new LocalisedString("en", "Category " + code));
        title.addText(new LocalisedString("pt", "Categoria " + code));
        categorisation.getVersionableStatisticalResource().setTitle(title);
    }

    @Override
    public Categorisation retrieveCategorisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        datasetServiceInvocationValidator.checkRetrieveCategorisationByUrn(ctx, urn);

        // Retrieve
        Categorisation categorisation = getCategorisationRepository().retrieveByUrn(urn);
        return categorisation;
    }

    @Override
    public List<Categorisation> retrieveCategorisationsByDatasetVersion(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        // Validation
        datasetServiceInvocationValidator.checkRetrieveCategorisationsByDatasetVersion(ctx, datasetVersionUrn);

        // Retrieve
        List<Categorisation> categorisations = getCategorisationRepository().retrieveCategorisationsByDatasetVersionUrn(datasetVersionUrn);
        return categorisations;
    }

    @Override
    public void deleteCategorisation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        datasetServiceInvocationValidator.checkDeleteCategorisation(ctx, urn);
        Categorisation categorisation = getCategorisationRepository().retrieveByUrn(urn);
        DatasetVersion datasetVersion = categorisation.getDatasetVersion();

        // Check is not final
        checkNotTasksInProgress(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());
        ProcStatusValidator.checkStatisticalResourceCanBeEdited(datasetVersion);

        // Delete
        getCategorisationRepository().delete(categorisation);
    }

    @Override
    public Categorisation endCategorisationValidity(ServiceContext ctx, String urn, DateTime validTo) throws MetamacException {
        // Validation
        datasetServiceInvocationValidator.checkEndCategorisationValidity(ctx, urn, validTo);
        Categorisation categorisation = getCategorisationRepository().retrieveByUrn(urn);
        if (categorisation.getValidFromEffective() == null || categorisation.getValidFromEffective().isAfterNow()) {
            throw new MetamacException(ServiceExceptionType.CATEGORISATION_CANT_END_VALIDITY_WITHOUT_VALIDITY_STARTED, urn);
        }
        if (validTo != null && validTo.isBefore(categorisation.getValidFromEffective())) {
            throw new MetamacException(ServiceExceptionType.CATEGORISATION_CANT_END_VALIDITY_BEFORE_VALIDITY_STARTED, urn);
        }
        checkNotTasksInProgress(ctx, categorisation.getDatasetVersion().getDataset().getIdentifiableStatisticalResource().getUrn());

        if (validTo == null) {
            validTo = new DateTime();
        }
        categorisation.getVersionableStatisticalResource().setValidTo(validTo);
        return getCategorisationRepository().save(categorisation);
    }

    @Override
    public PagedResult<Categorisation> findCategorisationsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        datasetServiceInvocationValidator.checkFindCategorisationsByCondition(ctx, conditions, pagingParameter);

        // Find
        conditions = CriteriaUtils.initConditions(conditions, Categorisation.class);
        pagingParameter = CriteriaUtils.initPagingParameter(pagingParameter);
        PagedResult<Categorisation> pagedResult = getCategorisationRepository().findByCondition(conditions, pagingParameter);
        return pagedResult;
    }

    @Override
    public void createDatabaseDatasourceInDatasetVersion(ServiceContext ctx, String datasetVersionUrn, String tableName) throws MetamacException {
        datasetServiceInvocationValidator.checkCreateDatabaseDatasourceInDatasetVersion(ctx, datasetVersionUrn, tableName);

        DatasetVersion datasetVersion = getDatasetVersionRepository().retrieveByUrn(datasetVersionUrn);

        checkTableNameLength(tableName, datasetVersionUrn);

        checkTableNameFormat(tableName, datasetVersionUrn);

        checkNotTasksInProgress(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());

        ProcStatusValidator.checkDatasetVersionCanImportDatasources(datasetVersion, new BasicVersionableStatisticalResourceDto());

        checkValidDataSourceTypeForImportationTask(DataSourceTypeEnum.DATABASE, datasetVersion);

        checkNotDatasourceForDataset(ctx, datasetVersionUrn);

        Datasource datasource = buildDatasource(tableName);

        createDatasource(ctx, datasetVersionUrn, datasource);
    }

    // ------------------------------------------------------------------------
    // CACHE
    // ------------------------------------------------------------------------

    private AttributeValue getSpatialAttributeValueFromDsdAttribute(DatasetVersion datasetVersion, DsdAttribute spatialAttribute) throws MetamacException {
        if (spatialAttribute != null) {
            for (AttributeValue attrValue : datasetVersion.getAttributesCoverage()) {
                if (attrValue.getDsdComponentId().equals(spatialAttribute.getComponentId())) {
                    return attrValue;
                }
            }
        }
        return null;
    }

    @Override
    public void updateGeographicCoverageFromSpatialAttribute(ServiceContext ctx, DatasetVersion datasetVersion) throws MetamacException {
        DataStructure dataStructure = srmRestInternalService.retrieveDsdByUrn(datasetVersion.getRelatedDsd().getUrn());
        DsdAttribute spatialAttribute = DsdProcessor.getSpatialAttributeFromDsd(dataStructure);

        if (spatialAttribute == null) {
            return;
        }

        AttributeValue spatialAttributeValue = getSpatialAttributeValueFromDsdAttribute(datasetVersion, spatialAttribute);

        if (spatialAttributeValue != null) {
            Codes codes = srmRestInternalService.retrieveCodesOfCodelistEfficiently(spatialAttribute.getCodelistRepresentationUrn());

            for (CodeResourceInternal code : codes.getCodes()) {
                if (spatialAttributeValue.getIdentifier().equals(code.getId())) {
                    ExternalItem item = restMapper.buildExternalItemFromCode(code);
                    if (!StatisticalResourcesCollectionUtils.isExternalItemInCollection(datasetVersion.getGeographicCoverage(), item)) {
                        datasetVersion.getGeographicCoverage().clear();
                        datasetVersion.addGeographicCoverage(item);
                    }
                }
            }
        } else {
            List<ExternalItem> codeItems = processExternalItemsCodeFromSpatialAttribute(datasetVersion, spatialAttribute);
            datasetVersion.getGeographicCoverage().clear();
            datasetVersion.getGeographicCoverage().addAll(codeItems);
        }
    }

    @Override
    public void updateGeographicCoverageVariableElementsCache(ServiceContext ctx, DatasetVersion datasetVersion) throws MetamacException {
        datasetServiceInvocationValidator.checkUpdateGeographicCoverageVariableElementsCache(ctx, datasetVersion);
        updateGeocoverageCache(ctx, datasetVersion, true);

    }

    @Override
    public void updateAllGeographicCoverageVariableElementsCache(ServiceContext ctx) throws MetamacException {
        datasetServiceInvocationValidator.checkUpdateAllGeographicCoverageVariableElementsCache(ctx);

        List<ConditionalCriteria> criteria = ConditionalCriteriaBuilder.criteriaFor(DatasetVersion.class).withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().procStatus())
                .eq(ProcStatusEnum.PUBLISHED).distinctRoot().build();
        List<DatasetVersion> datasetVersions = datasetVersionRepository.findByCondition(criteria);

        updateAllGeocoverageCache(ctx, datasetVersions);
    }

    private void updateGeographicCoverageExternalPublicationCacheByResource(ServiceContext ctx, SpecificRecordBase message) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canUpdateGeographicCoverageVariableElementsCache(ctx);

        if (message instanceof DatasetAvro) {
            cacheService.updateDatasetExternalPublicationCache(ctx, (DatasetAvro) message);
        } else if (message instanceof PublicationAvro) {
            cacheService.updateCollectionExternalPublicationCache(ctx, (PublicationAvro) message);
        }

    if (!hasValidStatisticalOperation(jaxiDatasetVersionAvro)) {
            return;
        }

        cacheService.disabledResourceByUrn(ctx, jaxiDatasetVersionAvro.getUrn());

        if (ProcStatusEnumAvro.PUBLISHED.equals(jaxiDatasetVersionAvro.getProcStatus())) {
            List<GeoCacheTerritoriesByGeoCacheResource> territories = restMapper.buildExternalItemFromJaxiExternalPublication(jaxiDatasetVersionAvro, srmRestInternalService,
                    noticesRestInternalService, exceptionItems);
            if (exceptionItems.isEmpty()) {
                InternationalString datasetTitle = restMapper.getInternationalStringFromInternationalStringAvro(jaxiDatasetVersionAvro.getTitle());
                GeoCacheResource geoCacheResource = cacheService.updateGeoCacheExternalResource(ctx, jaxiDatasetVersionAvro, datasetTitle);
                geoCacheResource.getTerritories().addAll(territories);

            } else {
                MetamacException metamacException = new MetamacException();
                metamacException.getExceptionItems().addAll(exceptionItems);
                throw metamacException;
            }

        }
    }

    private boolean hasValidStatisticalOperation(DatasetAvro jaxiDatasetVersionAvro) {
        if (jaxiDatasetVersionAvro.getStatisticalOperation() == null || jaxiDatasetVersionAvro.getStatisticalOperation().getCode() == null) {
            String datasetUrn = jaxiDatasetVersionAvro.getUrn();
            log.error("Dataset with null statistical operation received from Kafka. Dataset URN: {}", datasetUrn);
            noticesRestInternalService.createExternalPublicationNullOperationErrorBackgroundNotification(datasetUrn);
            return false;
        }
        return true;
    }

    @Override
    public void updateGeographicCoverageExternalPublicationCache(ServiceContext ctx, SpecificRecordBase message) throws MetamacException {

        getTransactionTemplate().execute(new MetamacExceptionTransactionCallback<Void>() {

            @Override
            protected Void doInMetamacTransaction(TransactionStatus status) throws MetamacException {
                updateGeographicCoverageExternalPublicationCacheByResource(ctx, message);
                return null;
            }
        });
    }

    private TransactionTemplate getTransactionTemplate() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate;
    }

    private void updateAllGeocoverageCache(ServiceContext ctx, List<DatasetVersion> datasetVersions) throws MetamacException {
        // Given that jobs take an ID from the dataset urn (not the dataset version), and we need to update all the versions
        // of a dataset, a conflict emerges when trying to schedule two jobs to update the cache of several versions of the same dataset.
        // We prevent this by planning only one dataset version at a time, and then waiting until that job finishes to planify the next one.
        // That's why we run a loop that checks whether a job with a dataset urn has been created before planning it. We can have
        // multiple jobs for different datasets, but not for multiple versions of the same dataset.
        while (!datasetVersions.isEmpty()) {
            ListIterator<DatasetVersion> datasetVersionsIt = datasetVersions.listIterator();

            while (datasetVersionsIt.hasNext()) {
                DatasetVersion datasetVersion = datasetVersionsIt.next();
                String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();

                if (!getTaskService().existsTaskForResource(ctx, datasetUrn)) {

                    // Thing is, Quartz run its own thread to execute jobs. That thread does not share the same transaction as the
                    // one planning the jobs (the one where this method runs). Given that this thread does not end until all jobs
                    // have been planned (because we cannot have two jobs for the same dataset, we have to planify them only one at
                    // a time) that means a job starts and ends executing while this thread it's still alive.
                    //
                    // That causes an exception: when a job finishes it looks in the DB to mark itself as finished. But since the transaction
                    // at the planify thread has not yet been completed, it has not flushed data to the DB, meaning an exception it's thrown:
                    // "couldn't find job on DB".
                    //
                    // This is fixed by creating a specific transaction to planify a job, that allows it to be saved to DB before the job even
                    // starts, preventing the exception.
                    getTransactionTemplate().execute(new MetamacExceptionTransactionCallback<Void>() {

                        @Override
                        protected Void doInMetamacTransaction(TransactionStatus status) throws MetamacException {
                            updateGeocoverageCache(ctx, datasetVersion, false);
                            return null;
                        }
                    });

                    datasetVersionsIt.remove();
                }
            }
        }
    }

    abstract static class MetamacExceptionTransactionCallback<T> implements TransactionCallback<T> {

        @Override
        public final T doInTransaction(TransactionStatus status) {
            try {
                return doInMetamacTransaction(status);
            } catch (MetamacException e) {
                throw new RuntimeException("Error in transactional method", e);
            }
        }

        protected abstract T doInMetamacTransaction(TransactionStatus status) throws MetamacException;
    }

    // ------------------------------------------------------------------------
    // PRIVATE METHODS
    // ------------------------------------------------------------------------

    private void updateGeocoverageCache(ServiceContext ctx, DatasetVersion datasetVersion, boolean sendNotification) throws MetamacException {
        String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        checkNotTasksInProgress(ctx, datasetVersionUrn);

        TaskInfoDataset taskInfo = new TaskInfoDataset();
        taskInfo.setDatasetVersionId(datasetVersionUrn);
        taskInfo.setDatasetUrn(datasetUrn);
        taskService.planifyUpdateGeocoverageCache(ctx, taskInfo, sendNotification);
    }

    private void checkNotTasksInProgress(ServiceContext ctx, String datasetUrn) throws MetamacException {
        if (getTaskService().existsTaskForResource(ctx, datasetUrn)) {
            throw new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, datasetUrn);
        }
    }

    protected void computeDataRelatedMetadata(DatasetVersion resource) throws MetamacException {
        ExternalItem externalDsd = resource.getRelatedDsd();
        DataStructure dataStructure = srmRestInternalService.retrieveDsdByUrn(externalDsd.getUrn());

        processDimensionCoverages(resource, dataStructure);

        processObservationAttributeCoverages(resource, dataStructure);

        processDataRelatedMetadata(resource);

        processStartEndDates(resource);

        processDateNextUpdate(resource);
    }

    private void processStartEndDates(DatasetVersion resource) {
        List<TemporalCode> temporalCoverage = resource.getTemporalCoverage();
        if (temporalCoverage.isEmpty()) {
            resource.setDateStart(null);
            resource.setDateEnd(null);
            resource.setDateStartTimestamp(null);
            resource.setDateEndTimestamp(null);
            return;
        }
        TemporalCode start = temporalCoverage.get(temporalCoverage.size() - 1);
        TemporalCode end = temporalCoverage.get(0);

        resource.setDateStart(start.getIdentifier());
        resource.setDateEnd(end.getIdentifier());
        resource.setDateStartTimestamp(SdmxTimeUtils.getDateTimeFromSDMXFormat(start.getIdentifier()));
        resource.setDateEndTimestamp(SdmxTimeUtils.getDateTimeFromSDMXFormat(end.getIdentifier()));
    }

    private void processDateNextUpdate(DatasetVersion resource) {
        if (NextVersionTypeEnumUtils.isInAnyNextVersionType(resource, NextVersionTypeEnum.SCHEDULED_UPDATE)
                && (resource.getDateNextUpdate() == null || BooleanUtils.isNotTrue(resource.getUserModifiedDateNextUpdate()))) {
            DateTime mostRecentDate = null;
            for (Datasource datasource : resource.getDatasources()) {
                if (datasource.getDateNextUpdate() != null && isNewDateBestOptionForDateNextUpdate(mostRecentDate, datasource.getDateNextUpdate())) {
                    mostRecentDate = datasource.getDateNextUpdate();
                }
            }

            resource.setDateNextUpdate(setDateInSdmx(mostRecentDate));
            resource.setUserModifiedDateNextUpdate(false);
        }
    }

    private String setDateInSdmx(DateTime date) {
        return date != null ? CoreCommonUtil.jodaDateTime2IsoDate(date.toDate()) : null;
    }

    private boolean isNewDateBestOptionForDateNextUpdate(DateTime current, DateTime newCandidate) {
        if (current == null) {
            return true;
        }
        if (newCandidate.isAfterNow() && newCandidate.isBefore(current)) {
            return true;
        }
        return false;
    }

    private void processDataRelatedMetadata(DatasetVersion resource) throws MetamacException {
        try {
            Long tableSize = calculateTableSize(null, resource);
            resource.setFormatExtentTableSize(tableSize);
            DatasetRepositoryDto datasetRepository = statisticsDatasetRepositoriesServiceFacade.retrieveDatasetRepository(resource.getDatasetRepositoryId());
            resource.setFormatExtentDimensions(datasetRepository.getDimensions().size());
            long num = statisticsDatasetRepositoriesServiceFacade.countObservations(resource.getDatasetRepositoryId());
            resource.setFormatExtentObservations(num);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error retrieving datasetRepository " + resource.getDatasetRepositoryId() + ". Details: " + e.getMessage());
        }
    }

    // COVERAGE UTILS
    private void processObservationAttributeCoverages(DatasetVersion resource, DataStructure dataStructure) throws MetamacException {
        try {
            Map<String, List<String>> attrCoverages = statisticsDatasetRepositoriesServiceFacade.findAttributeInstancesValuesWithObservationAttachmentLevel(resource.getDatasetRepositoryId(),
                    StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE);
            List<DsdAttribute> attributes = DsdProcessor.getAttributes(dataStructure);

            for (DsdAttribute dsdAttribute : attributes) {
                if (dsdAttribute.isAttributeAtObservationLevel()) {
                    processAttributeCoverage(resource, dsdAttribute, new TemporalAttributeValues(attrCoverages.get(dsdAttribute.getComponentId())));
                }
            }
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "An error has ocurred retrieving values for attributes for dataset repository " + resource.getDatasetRepositoryId());
        }
    }

    // Single attribute coverage
    private void processNonObservationAttributeCoverage(DatasetVersion resource, DsdAttribute dsdAttribute) throws MetamacException {

        TemporalAttributeValues temporalAttributeValues = getNonObservationalAttributes(resource, dsdAttribute);
        processAttributeCoverage(resource, dsdAttribute, temporalAttributeValues);

    }

    private TemporalAttributeValues getNonObservationalAttributes(DatasetVersion resource, DsdAttribute dsdAttribute) throws MetamacException {
        try {
            TemporalAttributeValues temporalAttributeValues = new TemporalAttributeValues();
            if (isTextFormatAttributeMultilingual(dsdAttribute)) {
                temporalAttributeValues
                        .setInternationalStringValues(statisticsDatasetRepositoriesServiceFacade.findAttributeInstancesValues(resource.getDatasetRepositoryId(), dsdAttribute.getComponentId()));
                temporalAttributeValues.setMultilingualValue(true);
            } else {
                temporalAttributeValues.setValues(statisticsDatasetRepositoriesServiceFacade.findAttributeInstancesValues(resource.getDatasetRepositoryId(), dsdAttribute.getComponentId(),
                        StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE));
            }
            return temporalAttributeValues;
        } catch (ApplicationException e) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Error retrieving values for attribute " + dsdAttribute.getComponentId());
        }
    }

    private boolean isTextFormatAttributeMultilingual(DsdAttribute dsdAttribute) {
        return dsdAttribute.getTextFormatRepresentation() != null && DataType.INTERNATIONAL_STRING.equals(dsdAttribute.getTextFormatRepresentation().getTextType());
    }

    private void processAttributeCoverage(DatasetVersion resource, DsdAttribute dsdAttribute, TemporalAttributeValues temporalAttributeValues) throws MetamacException {
        String attributeId = dsdAttribute.getComponentId();

        List<AttributeValue> attrValues = new ArrayList<AttributeValue>();
        if (temporalAttributeValues.hasValues()) {
            List<ExternalItem> items = buildExternalItemsBasedOnCodeIdentifiers(temporalAttributeValues.getValues(), dsdAttribute);
            String locale = configurationService.retrieveLanguageDefault();

            if (temporalAttributeValues.isMultilingualValue()) {
                attrValues = buildInternationalStringAttributeValues(attributeId, temporalAttributeValues, resource);
            } else {
                attrValues = buildAttributeValues(attributeId, temporalAttributeValues.getValues(), resource, items, locale);
            }

            if (CollectionUtils.isNotEmpty(items)) {
                addTranslationsToAttributeValuesFromExternalItems(attrValues, items, locale);
            }

        }

        clearAttributeValues(resource, attributeId);

        for (AttributeValue attrValue : attrValues) {
            resource.addAttributesCoverage(attrValue);
        }
    }

    private List<AttributeValue> buildInternationalStringAttributeValues(final String attributeId, TemporalAttributeValues temporalAttributeValues, final DatasetVersion datasetVersion) {

        List<AttributeValue> attrValues = new ArrayList<AttributeValue>();

        Set<InternationalStringDto> uniqueValues = new HashSet<InternationalStringDto>(temporalAttributeValues.getInternationalStringValues());

        StatisticalResourcesCollectionUtils.mapCollection(uniqueValues, attrValues, new MetamacTransformer<InternationalStringDto, AttributeValue>() {

            @Override
            public AttributeValue transformItem(InternationalStringDto item) {
                AttributeValue result = new AttributeValue();
                result.setIdentifier(getGenericIdentifier());
                result.setTitle(null);
                result.setInternationalStringValue(InternationalStringUtils.getCommonInternationalStringFromDatasetRepositoryInternationalStringDto(item));
                result.setDsdComponentId(attributeId);
                result.setDatasetVersion(datasetVersion);
                return result;
            }

        });
        return attrValues;

    }

    private List<AttributeValue> buildAttributeValues(final String attributeId, List<String> values, final DatasetVersion datasetVersion, List<ExternalItem> externalItems, String locale) {

        List<AttributeValue> attrValues = new ArrayList<AttributeValue>();

        Set<String> uniqueValues = new HashSet<String>(values);

        StatisticalResourcesCollectionUtils.mapCollection(uniqueValues, attrValues, new MetamacTransformer<String, AttributeValue>() {

            @Override
            public AttributeValue transformItem(String item) {
                AttributeValue result = new AttributeValue();
                result.setIdentifier(getAttributeIdentifier(item, externalItems));
                result.setTitle(item);
                result.setDsdComponentId(attributeId);
                result.setDatasetVersion(datasetVersion);
                return result;
            }

            private String getAttributeIdentifier(String item, List<ExternalItem> externalItems) {
                // If there is an external item with the same value for the code attribute as the one passed in the item parameter,
                // the identifier assigned will be the latter because the title of the attribute will be extracted from the title of the external item.
                if (CollectionUtils.isNotEmpty(externalItems)) {
                    ExternalItem externalItem = MetamacCollectionUtils.find(externalItems, new ExternalItemEqualsIdentifierPredicate(item));
                    if (externalItem != null && externalItem.getTitle().getLocalisedLabel(locale) != null) {
                        return item;
                    }
                }

                // If the length of the item parameter is less than the maximum value allowed in the database for the identifier (255),
                // the latter is returned as the identifier
                if (StringUtils.length(item) <= DatasetAttibuteSharedUtils.ATTRIBUTE_IDENTIFIER_MAXIMUM_SIZE) {
                    return item;
                }

                // In any other case, if the size of the item parameter is greater than 255 and there is no variable element associated with it,
                // the generated identifier will be a random uuid.
                String uuidIdenfier = getGenericIdentifier();
                log.info("Item can not be set as identifier because is too long: {} using uuid instead: {}", StringUtils.length(item), uuidIdenfier);

                return uuidIdenfier;
            }

        });
        return attrValues;
    }

    private String getGenericIdentifier() {
        return UUID.randomUUID().toString();
    }

    private void clearAttributeValues(DatasetVersion datasetVersion, String componentId) {
        Iterator<AttributeValue> iterator = datasetVersion.getAttributesCoverage().iterator();
        while (iterator.hasNext()) {
            AttributeValue attrValue = iterator.next();
            if (attrValue.getDsdComponentId().equals(componentId)) {
                iterator.remove();
            }
        }
    }

    private void processDimensionCoverages(DatasetVersion resource, DataStructure dataStructure) throws MetamacException {
        resource.getDimensionsCoverage().clear();
        resource.getGeographicCoverage().clear();
        resource.getTemporalCoverage().clear();
        resource.getMeasureCoverage().clear();

        List<DsdDimension> dimensions = DsdProcessor.getDimensions(dataStructure);
        for (DsdDimension dimension : dimensions) {
            List<CodeDimension> codes = getCodesFromDsdComponent(resource, dimension);
            List<String> codeIdentifiers = mapCodeDimensionsToCodeIdentifiers(codes);
            List<ExternalItem> items = buildExternalItemsBasedOnCodeIdentifiers(codeIdentifiers, dimension);
            if (items != null) {
                addTranslationsToCodesFromExternalItems(codes, items);
            }

            if (DsdComponentType.TEMPORAL.equals(dimension.getType())) {
                DatasetVersionUtils.sortTemporalCodeDimensions(codes);
            }

            resource.getDimensionsCoverage().addAll(codes);
            switch (dimension.getType()) {
                case SPATIAL:
                    for (ExternalItem item : items) {
                        if (!StatisticalResourcesCollectionUtils.isExternalItemInCollection(resource.getGeographicCoverage(), item)) {
                            resource.getGeographicCoverage().add(item);
                        }
                    }
                    break;
                case MEASURE:
                    resource.getMeasureCoverage().addAll(items);
                    break;
                case TEMPORAL:
                    List<TemporalCode> temporalCodes = buildTemporalCodeFromCodeDimensions(codes);
                    resource.getTemporalCoverage().addAll(temporalCodes);
                    break;
                case OTHER:
                    break;
                default:
                    break;
            }
        }

        // Try to fill specific coverages from attributes
        if (resource.getGeographicCoverage().isEmpty()) {
            List<ExternalItem> codeItems = processExternalItemsCodeFromAttributeByType(resource, dataStructure, DsdComponentType.SPATIAL);
            resource.getGeographicCoverage().addAll(codeItems);
        }
        if (resource.getTemporalCoverage().isEmpty()) {
            List<CodeDimension> codeItems = processCodeFromAttributeByType(resource, dataStructure, DsdComponentType.TEMPORAL);
            DatasetVersionUtils.sortTemporalCodeDimensions(codeItems);
            resource.getTemporalCoverage().addAll(buildTemporalCodeFromCodeDimensions(codeItems));
        }
        if (resource.getMeasureCoverage().isEmpty()) {
            List<ExternalItem> codeItems = processExternalItemsCodeFromAttributeByType(resource, dataStructure, DsdComponentType.MEASURE);
            resource.getMeasureCoverage().addAll(codeItems);
        }
    }

    private List<String> mapCodeDimensionsToCodeIdentifiers(List<CodeDimension> codes) {
        List<String> identifiers = new ArrayList<String>();
        StatisticalResourcesCollectionUtils.mapCollection(codes, identifiers, new CodeDimensionToCodeStringTransformer());
        return identifiers;
    }

    private void addTranslationsToCodesFromExternalItems(List<CodeDimension> codeDimensions, List<ExternalItem> externalItems) throws MetamacException {
        String locale = configurationService.retrieveLanguageDefault();
        for (ExternalItem externalItem : externalItems) {
            for (CodeDimension codeDimension : codeDimensions) {
                if (codeDimension.getIdentifier().equals(externalItem.getCode())) {
                    String title = externalItem.getTitle().getLocalisedLabel(locale);
                    if (title != null) {
                        codeDimension.setTitle(title);
                    } else {
                        codeDimension.setTitle(codeDimension.getIdentifier());
                    }
                }
            }
        }
    }

    private void addTranslationsToAttributeValuesFromExternalItems(List<AttributeValue> values, List<ExternalItem> externalItems, String locale) throws MetamacException {
        for (ExternalItem externalItem : externalItems) {
            for (AttributeValue attrValue : values) {
                if (attrValue.getIdentifier().equals(externalItem.getCode())) {
                    String title = externalItem.getTitle().getLocalisedLabel(locale);
                    if (title != null) {
                        attrValue.setTitle(title);
                    } else {
                        attrValue.setTitle(attrValue.getIdentifier());
                    }
                }
            }
        }
    }

    private List<ExternalItem> processExternalItemsCodeFromAttributeByType(DatasetVersion resource, DataStructure dataStructure, DsdComponentType type) throws MetamacException {
        List<DsdAttribute> attributes = getDsdAttributesByType(dataStructure, type);
        List<ExternalItem> items = new ArrayList<ExternalItem>();
        if (attributes != null) {
            for (DsdAttribute attribute : attributes) {
                List<CodeDimension> codes = filterCodesFromAttribute(resource, resource.getDatasetRepositoryId(), attribute.getComponentId());
                List<String> codesIdentifiers = mapCodeDimensionsToCodeIdentifiers(codes);
                List<ExternalItem> attributeItems = buildExternalItemsBasedOnCodeIdentifiers(codesIdentifiers, attribute);
                // Avoid repeat items
                for (ExternalItem item : attributeItems) {
                    if (!StatisticalResourcesCollectionUtils.isExternalItemInCollection(attributeItems, item)) {
                        items.add(item);
                    }
                }
            }
        }
        return items;
    }

    private List<ExternalItem> processExternalItemsCodeFromSpatialAttribute(DatasetVersion resource, DsdAttribute spatialAttribute) throws MetamacException {
        List<ExternalItem> items = new ArrayList<>();
        List<CodeDimension> codes = filterCodesFromAttribute(resource, resource.getDatasetRepositoryId(), spatialAttribute.getComponentId());
        List<String> codesIdentifiers = mapCodeDimensionsToCodeIdentifiers(codes);
        List<ExternalItem> attributeItems = buildExternalItemsBasedOnCodeIdentifiers(codesIdentifiers, spatialAttribute);
        // Avoid repeat items
        for (ExternalItem item : attributeItems) {
            if (!StatisticalResourcesCollectionUtils.isExternalItemInCollection(attributeItems, item)) {
                items.add(item);
            }
        }

        if (items.isEmpty()) {
            items.addAll(attributeItems);
        }
        return items;
    }

    private List<CodeDimension> processCodeFromAttributeByType(DatasetVersion resource, DataStructure dataStructure, DsdComponentType type) throws MetamacException {
        List<DsdAttribute> attributes = getDsdAttributesByType(dataStructure, type);
        List<CodeDimension> codeDimensions = new ArrayList<CodeDimension>();
        if (attributes != null) {
            for (DsdAttribute attribute : attributes) {
                List<CodeDimension> codes = filterCodesFromAttribute(resource, resource.getDatasetRepositoryId(), attribute.getComponentId());
                List<String> codesIdentifiers = mapCodeDimensionsToCodeIdentifiers(codes);
                List<ExternalItem> items = buildExternalItemsBasedOnCodeIdentifiers(codesIdentifiers, attribute);
                addTranslationsToCodesFromExternalItems(codes, items);
                // Avoid repeat items
                for (CodeDimension codeDim : codes) {
                    if (!isInCollection(codeDimensions, new CodeDimensionEqualsIdentifierPredicate(codeDim.getIdentifier()))) {
                        codeDimensions.add(codeDim);
                    }
                }
            }
        }
        return codeDimensions;
    }

    private List<DsdAttribute> getDsdAttributesByType(DataStructure dataStructure, DsdComponentType type) throws MetamacException {
        List<DsdAttribute> attributes = DsdProcessor.getAttributes(dataStructure);
        List<DsdAttribute> foundAttributes = filterDsdAttributeWithType(attributes, type);
        return foundAttributes;
    }

    private List<DsdAttribute> filterDsdAttributeWithType(List<DsdAttribute> attributes, DsdComponentType type) {
        List<DsdAttribute> attributesWithType = new ArrayList<DsdProcessor.DsdAttribute>();
        for (DsdAttribute attr : attributes) {
            if (type.equals(attr.getType())) {
                attributesWithType.add(attr);
            }
        }
        return attributesWithType;
    }

    private List<TemporalCode> buildTemporalCodeFromCodeDimensions(List<CodeDimension> codes) {
        List<TemporalCode> temporalCodes = new ArrayList<TemporalCode>();
        for (CodeDimension codeDim : codes) {
            TemporalCode tempCode = new TemporalCode();
            tempCode.setIdentifier(codeDim.getIdentifier());
            tempCode.setTitle(codeDim.getTitle());
            temporalCodes.add(tempCode);
        }
        return temporalCodes;
    }

    private List<ExternalItem> buildExternalItemsBasedOnCodeIdentifiers(List<String> codes, DsdComponent component) throws MetamacException {
        if (component.getCodelistRepresentationUrn() != null) {
            return buildExternalItemsBasedOnCodeDimensionsInCodelist(codes, component.getCodelistRepresentationUrn());
        } else if (component.getConceptSchemeRepresentationUrn() != null) {
            return buildExternalItemsBasedOnCodeDimensionsInConceptScheme(codes, component.getConceptSchemeRepresentationUrn());
        } else {
            return Collections.emptyList();
        }
    }

    private List<ExternalItem> buildExternalItemsBasedOnCodeDimensionsInCodelist(List<String> codeDimensions, String codelistRepresentationUrn) throws MetamacException {
        List<ExternalItem> externalItems = new ArrayList<ExternalItem>();

        Codes codes = srmRestInternalService.retrieveCodesOfCodelistEfficiently(codelistRepresentationUrn);

        for (CodeResourceInternal code : codes.getCodes()) {
            for (String codeIdentifier : codeDimensions) {
                if (codeIdentifier.equals(code.getId())) {
                    externalItems.add(restMapper.buildExternalItemFromCode(code));
                }
            }
        }

        if (externalItems.size() < codeDimensions.size()) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Some codes in dimension were not found in codelist " + codelistRepresentationUrn);
        }

        return externalItems;
    }

    private List<ExternalItem> buildExternalItemsBasedOnCodeDimensionsInConceptScheme(List<String> codeDimensions, String conceptSchemeRepresentationUrn) throws MetamacException {
        List<ExternalItem> externalItems = new ArrayList<ExternalItem>();

        Concepts concepts = srmRestInternalService.retrieveConceptsOfConceptSchemeEfficiently(conceptSchemeRepresentationUrn);

        for (ItemResourceInternal concept : concepts.getConcepts()) {
            for (String codeIdentifier : codeDimensions) {
                if (codeIdentifier.equals(concept.getId())) {
                    externalItems.add(restMapper.buildExternalItemFromSrmItemResourceInternal(concept));
                }
            }
        }

        if (externalItems.size() < codeDimensions.size()) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Some codes in dimension were not found in conceptScheme " + conceptSchemeRepresentationUrn);
        }

        return externalItems;
    }

    private List<CodeDimension> getCodesFromDsdComponent(DatasetVersion resource, DsdComponent dsdComponent) throws MetamacException {
        List<CodeDimension> codes = new ArrayList<CodeDimension>();
        if (dsdComponent != null) {
            if (dsdComponent instanceof DsdDimension) {
                codes = filterCodesFromDimension(resource, resource.getDatasetRepositoryId(), dsdComponent.getComponentId());
            } else if (dsdComponent instanceof DsdAttribute) {
                codes = filterCodesFromAttribute(resource, resource.getDatasetRepositoryId(), dsdComponent.getComponentId());
            }
        }
        return codes;
    }

    private List<CodeDimension> filterCodesFromDimension(DatasetVersion resource, String datasetRepositoryId, String dimensionId) throws MetamacException {
        try {

            Map<String, List<String>> codeDimensionsMap = statisticsDatasetRepositoriesServiceFacade.findCodeDimensions(datasetRepositoryId);
            List<String> dimCodes = codeDimensionsMap.get(dimensionId);

            List<CodeDimension> codes = new ArrayList<CodeDimension>();
            for (String code : dimCodes) {
                CodeDimension codeDimension = new CodeDimension();
                codeDimension.setIdentifier(code);
                codeDimension.setTitle(code);
                codeDimension.setDsdComponentId(dimensionId);
                codeDimension.setDatasetVersion(resource);
                codes.add(codeDimension);
            }
            return codes;
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "An error has ocurred retrieving codes from dataset repository " + datasetRepositoryId + " for dimension " + dimensionId);
        }
    }

    private List<CodeDimension> filterCodesFromAttribute(DatasetVersion resource, String datasetRepositoryId, String attributeId) throws MetamacException {
        try {
            List<AttributeInstanceDto> attributes = statisticsDatasetRepositoriesServiceFacade.findAttributesInstancesWithDatasetAttachmentLevel(datasetRepositoryId, attributeId);

            List<CodeDimension> codes = new ArrayList<CodeDimension>();
            if (attributes.size() > 0) {
                String value = attributes.get(0).getValue().getLocalisedLabel(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE);
                CodeDimension codeDimension = new CodeDimension();
                codeDimension.setIdentifier(value);
                codeDimension.setTitle(value);
                codeDimension.setDsdComponentId(attributeId);
                codeDimension.setDatasetVersion(resource);
                codes.add(codeDimension);
            }
            return codes;
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "An error has ocurred retrieving values from dataset repository " + datasetRepositoryId + " for attribute " + attributeId);
        }
    }

    private static void fillMetadataForCreateDatasource(Datasource datasource, DatasetVersion datasetVersion) {
        FillMetadataForCreateResourceUtils.fillMetadataForCreateIdentifiableResource(datasource.getIdentifiableStatisticalResource(),
                datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation());

        datasource.setDatasetVersion(datasetVersion);
        datasource.getIdentifiableStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceDatasourceUrn(datasource.getIdentifiableStatisticalResource().getCode()));
    }

    private DatasetVersion addDatasourceForDatasetVersion(Datasource datasource, DatasetVersion datasetVersion) {
        datasetVersion.addDatasource(datasource);
        datasetVersion.getSiemacMetadataStatisticalResource().setLastUpdate(new DateTime());
        return getDatasetVersionRepository().save(datasetVersion);
    }

    private DatasetVersion deleteDatasourceToDataset(Datasource datasource) {
        DatasetVersion parent = datasource.getDatasetVersion();
        parent.removeDatasource(datasource);
        parent.getSiemacMetadataStatisticalResource().setLastUpdate(new DateTime());
        parent.setDateLastTimeDataImport(null);
        return getDatasetVersionRepository().save(parent);
    }

    private void tryDeleteDatabaseView(String viewCode) {
        try {
            statisticsDatasetRepositoriesServiceFacade.dropDatasetRepositoryView(viewCode);
        } catch (ApplicationException e) {
            log.warn("Dataset view [" + viewCode + "] could not be deleted", e);
        }
    }
    private void tryToDeleteDatasetRepository(String datasetRepositoryId) {
        if (!StringUtils.isEmpty(datasetRepositoryId)) {
            try {
                statisticsDatasetRepositoriesServiceFacade.deleteDatasetRepository(datasetRepositoryId);
            } catch (ApplicationException e) {
                log.warn("Dataset repository [" + datasetRepositoryId + "] could not be deleted", e);
            }
        }
    }

    private void fillMetadataForCreateDataset(ServiceContext ctx, Dataset dataset, ExternalItem statisticalOperation) {
        dataset.setIdentifiableStatisticalResource(new IdentifiableStatisticalResource());
        FillMetadataForCreateResourceUtils.fillMetadataForCreateIdentifiableResource(dataset.getIdentifiableStatisticalResource(), statisticalOperation);
    }

    private void fillMetadataForCreateDatasetVersion(ServiceContext ctx, DatasetVersion datasetVersion, ExternalItem statisticalOperation) {
        FillMetadataForCreateResourceUtils.fillMetadataForCretateSiemacResource(datasetVersion.getSiemacMetadataStatisticalResource(), statisticalOperation, StatisticalResourceTypeEnum.DATASET, ctx);
    }

    private synchronized Dataset assignCodeAndSaveDataset(Dataset dataset, DatasetVersion datasetVersion) throws MetamacException {
        String code = siemacStatisticalResourceGeneratedCode.fillGeneratedCodeForCreateSiemacMetadataResource(datasetVersion.getSiemacMetadataStatisticalResource());
        String[] maintainerCodes = new String[]{datasetVersion.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested()};

        dataset.getIdentifiableStatisticalResource().setCode(code);
        dataset.getIdentifiableStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceDatasetUrn(maintainerCodes, dataset.getIdentifiableStatisticalResource().getCode()));
        dataset.setViewCode(DatasetVersionUtils.generateViewCode(code));

        datasetVersion.getSiemacMetadataStatisticalResource().setCode(code);
        datasetVersion.getSiemacMetadataStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceDatasetVersionUrn(maintainerCodes,
                datasetVersion.getSiemacMetadataStatisticalResource().getCode(), datasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic()));

        // Checks
        identifiableStatisticalResourceRepository.checkDuplicatedUrn(datasetVersion.getSiemacMetadataStatisticalResource());

        // Add version to dataset
        dataset.addVersion(datasetVersion);
        return getDatasetRepository().save(datasetVersion.getDataset());
    }

    private void fillMetadataForCreateCategorisation(ServiceContext ctx, Categorisation categorisation) throws MetamacException {
        // Fill code, urn...
        initializeCategorisationMetadataForCreation(ctx, categorisation);

        if (categorisation.getDatasetVersion().getLifeCycleStatisticalResource().isPublishedVisible()) {
            categorisation.getVersionableStatisticalResource().setValidFrom(new DateTime());
        } else {
            // NOTE: validFrom will be inherited from dataset when it was published
            categorisation.getVersionableStatisticalResource().setValidFrom(null);
        }
        categorisation.getVersionableStatisticalResource().setValidTo(null);

    }

    private Datasource buildDatasource(String tableName) {
        Datasource datasource = new Datasource();
        datasource.setIdentifiableStatisticalResource(new IdentifiableStatisticalResource());
        datasource.getIdentifiableStatisticalResource().setCode(Datasource.generateDataSourceId(tableName, new DateTime()));
        datasource.setSourceName(tableName);
        return datasource;
    }

    private void checkNotDatasourceForDataset(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        List<Datasource> datasources = retrieveDatasourcesByDatasetVersion(ctx, datasetVersionUrn);

        if (datasources != null && !datasources.isEmpty()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORTATION_MORE_THAN_ONE_DATASOURCE_FOR_DATABASE_IMPORTATION_ERROR).build();
        }
    }

    private void checkValidDataSourceTypeForImportationTask(DataSourceTypeEnum dataSourceTypeExpected, DatasetVersion datasetVersion) throws MetamacException {
        DataSourceTypeEnum dataSourceType = datasetVersion.getDataSourceType();

        if (!dataSourceTypeExpected.equals(dataSourceType)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.INVALID_DATA_SOURCE_TYPE_FOR_DATASET_DATA_IMPORTATION)
                    .withMessageParameters(dataSourceType, dataSourceTypeExpected).build();
        }
    }

    private void checkTableNameLength(String tableName, String datasetVersionUrn) throws MetamacException {
        if (!DatabaseDatasetImportUtils.checkTableNameLength(tableName)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.INVALID_TABLENAME_LENGTH).withMessageParameters(tableName.length(), tableName, datasetVersionUrn,
                    DatabaseDatasetImportUtils.TABLENAME_MIN_LENGTH_PERMITTED, DatabaseDatasetImportUtils.TABLENAME_MAX_LENGTH_PERMITTED).build();
        }
    }

    private void checkTableNameFormat(String tableName, String datasetVersionUrn) throws MetamacException {
        if (!DatabaseDatasetImportUtils.checkTableNameFormat(tableName)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.INVALID_TABLENAME_FORMAT).withMessageParameters(tableName, datasetVersionUrn).build();
        }
    }

    @Override
    public List<DatasetVersion> retrievePublishedLastVersionDatasets(ServiceContext ctx) throws MetamacException {

        List<ConditionalCriteria> criteria = ConditionalCriteriaBuilder.criteriaFor(DatasetVersion.class).withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().procStatus())
                .eq(ProcStatusEnum.PUBLISHED).and().withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().validTo()).isNull().distinctRoot().build();
        return datasetVersionRepository.findByCondition(criteria);

    }

    @Override
    public Long calculateTableSize(ServiceContext ctx, DatasetVersion resource) throws MetamacException {
        Long tableSize = Long.valueOf(1);
        DataStructure dataStructure = srmRestInternalService.retrieveDsdByUrn(resource.getRelatedDsd().getUrn());
        List<DsdDimension> dimensions = DsdProcessor.getDimensions(dataStructure);
        for (DsdDimension dimension : dimensions) {
            List<CodeDimension> codes = getCodesFromDsdComponent(resource, dimension);
            tableSize *= codes.size();
        }
        return tableSize;
    }

    @Override
    public DatasetVersion getDatasetLastVersionPublishedByDatasetUrn(ServiceContext ctx, String agencyId, String resourceId) throws MetamacException {
        PagingParameter paging = PagingParameter.rowAccess(0, 1, 1);
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DatasetVersion.class).withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().procStatus())
                .eq(ProcStatusEnum.PUBLISHED).and().withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().maintainer().code()).eq(agencyId).and()
                .withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().code()).eq(resourceId).and()
                .withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().validTo()).isNull().distinctRoot().build();

        // @formatter:off

        PagedResult<DatasetVersion> datasetResult =  datasetVersionRepository.findByCondition(conditions, paging);
        
        if ( datasetResult.getValues() != null && !datasetResult.getValues().isEmpty() && datasetResult.getValues().size() == 1) {
            return datasetResult.getValues().get(0);
        }
        return null;
    }

    @Override
    public void updateDatasetVersionInGroup(ServiceContext ctx, DatasetVersion datasetVersionMetadataToChange, String datasetUrnToChange) throws MetamacException {
        DatasetVersion datasetVersion = retrieveDatasetVersionByUrn(ctx, datasetUrnToChange);             
        updateDatasetVersionInGroupInline(ctx, datasetVersion, datasetVersionMetadataToChange); 
    }
    
    private void updateDatasetVersionInGroupInline(ServiceContext ctx, DatasetVersion datasetVersion, DatasetVersion datasetVersionMetadataToChange) throws MetamacException {
        datasetServiceInvocationValidator.checkUpdateDatasetVersion(ctx, datasetVersion);
        
        datasetServiceInvocationValidator.checkUpdateDatasetVersionInGroup(ctx, datasetVersion, datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        
        DatasetVersionUpdateUtils.updateDatasetVersion(datasetVersionMetadataToChange, datasetVersion);
        updateDatasetVersion(ctx, datasetVersion);   
        updateDatasetVersionCategorisations(ctx, datasetVersion, DatasetVersionUpdateUtils.copyCategorisations(datasetVersionMetadataToChange.getCategorisations()));
        
    }

    private void updateDatasetVersionCategorisations(ServiceContext ctx, DatasetVersion datasetVersion,List<Categorisation> categorisations)  throws MetamacException {
        if (!categorisations.isEmpty()) {

            for (Categorisation categorisation : categorisations ) {
                List<ConditionalCriteria> condition = criteriaFor(Categorisation.class).withProperty(CategorisationProperties.category().urn()).eq(categorisation.getCategory().getUrn()).and()
                        .withProperty(CategorisationProperties.datasetVersion().siemacMetadataStatisticalResource().urn()).eq(datasetVersion.getSiemacMetadataStatisticalResource().getUrn())
                        .distinctRoot().build();
                List<Categorisation> result = getCategorisationRepository().findByCondition(condition);
                if (result.isEmpty()) {
                    categorisation.setDatasetVersion(datasetVersion);
                    fillMetadataForCreateCategorisation(ctx, categorisation);

                    getCategorisationRepository().save(categorisation);
                }
            }
        }
    }

    @Override
    public String exportDatasourcesTsv(ServiceContext ctx, String datasetVersionUrn, boolean useCommaDecimalSeparator) throws MetamacException {
        datasetServiceInvocationValidator.checkExportDatasourcesTsv(ctx, datasetVersionUrn, useCommaDecimalSeparator);
        FileOutputStream outputStreamObservations = null;
        String fileName = "";
        try {
            DatasetVersion datasetVersion = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);
            Map<String, ObservationExtendedDto> observations = datasetRepositoriesServiceFacade.findObservationsExtendedByDimensions(datasetVersion.getDatasetRepositoryId(), null);

            String[] datasetUrn = UrnUtils.splitUrnStructure(datasetVersionUrn);
            String prefix = "datasource" + "-" + datasetUrn[0] + "-" + datasetUrn[1] + "-" + datasetUrn[2] + "-";

            File tmpFileObservations = File.createTempFile(prefix, ".tsv");
            fileName = tmpFileObservations.getName();

            outputStreamObservations = new FileOutputStream(tmpFileObservations);

            PlainTextExporter exporter = new PlainTextExporter(observations, null, null, null, useCommaDecimalSeparator);

            exporter.writeObservationsAndAttributesWithObservationAttachmentLevel(outputStreamObservations, ManipulateDataUtils.getLocaleDatasourceIdentificationAttribute());

            return fileName;

        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.DATASOURCE_EXPORT_ERROR, e.getMessage());
        } finally {
            IOUtils.closeQuietly(outputStreamObservations);
        }
    }
    
    @Override
    public String exportAttributesTsv(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        datasetServiceInvocationValidator.checkExportAttributesTsv(ctx,datasetVersionUrn);
        String fileName = "";
        try {
            DatasetVersion datasetVersion = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);
             datasetRepositoriesServiceFacade.findAttributesInstancesWithDatasetAttachmentLevel(datasetVersionUrn, fileName);
            
            DataStructure dataStructure = srmRestInternalService.retrieveDsdByUrn(datasetVersion.getRelatedDsd().getUrn());

            List<String> languages = configurationService.retrieveLanguages();
  
            fileName = manipulateCsvDataService.exportCsvAttributes(dataStructure, datasetVersion,  languages);
 
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.ATTRIBUTES_EXPORT_ERROR, e.getMessage());
        } 
        
        return fileName;
    }    
}
