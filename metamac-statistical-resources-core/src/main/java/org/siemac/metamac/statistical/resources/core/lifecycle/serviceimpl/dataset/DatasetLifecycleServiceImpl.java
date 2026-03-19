package org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.dataset;

import static org.siemac.metamac.statistical.resources.core.error.utils.ServiceExceptionParametersUtils.addParameter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.common.serviceapi.TranslationService;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.constraint.api.ConstraintsService;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.dataset.serviceapi.DatasetService;
import org.siemac.metamac.statistical.resources.core.dataset.utils.DatasetVersioningCopyUtils;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.XStreamStatusEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionSingleParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.lifecycle.LifecycleCommonMetadataChecker;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.LifecycleTemplateService;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.checker.ExternalItemChecker;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeMessage;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoDataset;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.siemac.metamac.statistical.resources.core.utils.DatabaseDatasetImportUtils;
import org.siemac.metamac.statistical.resources.core.utils.DatasetImportUtils;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesExternalItemUtils;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.utils.TwitterPostUtils;
import org.siemac.metamac.statistical.resources.core.utils.TemporalDimensionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.dataset.repository.dto.Mapping;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

@Service("datasetLifecycleService")
public class DatasetLifecycleServiceImpl extends LifecycleTemplateService<DatasetVersion> {

    private static Logger                     logger = LoggerFactory.getLogger(DatasetLifecycleServiceImpl.class);

    @Autowired
    private LifecycleCommonMetadataChecker    lifecycleCommonMetadataChecker;

    @Autowired
    private ExternalItemChecker               externalItemChecker;

    @Autowired
    private DatasetService                    datasetService;

    @Autowired
    private TranslationService                      translationService;

    @Autowired
    private DatasetVersionRepository          datasetVersionRepository;

    @Autowired
    private TaskService                       taskService;

    @Autowired
    private ConstraintsService                constraintsService;

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    @Autowired
    private DatasetRepositoriesServiceFacade  datasetRepositoriesServiceFacade;

    @Autowired
    private QueryVersionRepository            queryVersionRepository;

    @Autowired
    private TwitterPostUtils                  twitterPostUtils;
    @Override
    protected String getResourceMetadataName() throws MetamacException {
        return ServiceExceptionParameters.DATASET_VERSION;
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PRODUCTION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToProductionValidationResource(DatasetVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // NOTHING
    }

    @Override
    protected void applySendToProductionValidationResource(ServiceContext ctx, DatasetVersion resource) throws MetamacException {
        // NOTHING
    }

    // ------------------------------------------------------------------------------------------------------
    // >> DIFFUSION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToDiffusionValidationResource(DatasetVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // NOTHING
    }

    @Override
    protected void applySendToDiffusionValidationResource(ServiceContext ctx, DatasetVersion resource) throws MetamacException {
        // NOTHING
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VALIDATION REJECTED
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToValidationRejectedResource(DatasetVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // NOTHING
    }

    @Override
    protected void applySendToValidationRejectedResource(ServiceContext ctx, DatasetVersion resource) throws MetamacException {
        // NOTHING
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PUBLISHED
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToPublishedResource(ServiceContext ctx, DatasetVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        checkExternalItemsPreviouslyPublished(ctx, resource, ServiceExceptionParameters.DATASET_VERSION, exceptionItems);

        // No related resources to check
    }

    private void checkExternalItemsPreviouslyPublished(ServiceContext ctx, DatasetVersion resource, String metadataName, List<MetamacExceptionItem> exceptionItems) throws MetamacException {

        externalItemChecker.checkExternalItemsExternallyPublished(resource.getRelatedDsd(), addParameter(metadataName, ServiceExceptionSingleParameters.RELATED_DSD), exceptionItems);

        // Note: For GeographicCoverage the validation of DSD is sufficient because all his values has been validated in the dimension.
        // Note: For MeasureCoverage the validation of DSD is sufficient because all his values has been validated in the dimension.

        Set<ExternalItem> extractCodelistsUsedInGeographicGranularities = StatisticalResourcesExternalItemUtils.extractCodelistsUsedFromExternalItemCodes(resource.getGeographicGranularities());
        externalItemChecker.checkExternalItemsExternallyPublished(extractCodelistsUsedInGeographicGranularities, addParameter(metadataName, ServiceExceptionSingleParameters.GEOGRAPHIC_GRANULARITIES),
                exceptionItems);

        Set<ExternalItem> extractCodelistsUsedInTemporalGranularities = StatisticalResourcesExternalItemUtils.extractCodelistsUsedFromExternalItemCodes(resource.getTemporalGranularities());
        externalItemChecker.checkExternalItemsExternallyPublished(extractCodelistsUsedInTemporalGranularities, addParameter(metadataName, ServiceExceptionSingleParameters.TEMPORAL_GRANULARITIES),
                exceptionItems);

        if (!resource.getStatisticalUnit().isEmpty()) {
            externalItemChecker.checkExternalItemsExternallyPublished(resource.getStatisticalUnit(), addParameter(metadataName, ServiceExceptionSingleParameters.STATISTICAL_UNIT), exceptionItems);
        }

        externalItemChecker.checkExternalItemsExternallyPublished(resource.getUpdateFrequency(), addParameter(metadataName, ServiceExceptionSingleParameters.UPDATE_FREQUENCY), exceptionItems);

        for (Categorisation categorisation : resource.getCategorisations()) {
            externalItemChecker.checkExternalItemsExternallyPublished(categorisation.getCategory(), addParameter(metadataName, ServiceExceptionSingleParameters.CATEGORISATIONS), exceptionItems);
            externalItemChecker.checkExternalItemsExternallyPublished(categorisation.getMaintainer(), addParameter(metadataName, ServiceExceptionSingleParameters.CATEGORISATIONS), exceptionItems);
        }

        // Publish associated constraints
        constraintsService.publishContentConstraint(ctx, resource.getLifeCycleStatisticalResource().getUrn(), Boolean.TRUE);
    }

    @Override
    protected void applySendToPublishedCurrentResource(ServiceContext ctx, DatasetVersion resource, DatasetVersion previousResource) throws MetamacException {
        resource.setPatch(0);
        resource.setBibliographicCitation(buildBibliographicCitation(resource));
        updateGeographicCoverage(ctx, resource);
        updateGeographicCoverageVariableElementsCache(ctx, resource);
    }

    @Override
    protected void applySendToPublishedPreviousResource(ServiceContext ctx, DatasetVersion resource) throws MetamacException {
        try {
            if (!resource.isKeepAllData()) {
                logger.info(
                        "The dataset table will be deleted from the repository when trying to publish a new version of the dataset since it does not save the observations in previous versions. Dataset = {}",
                        resource.getDatasetRepositoryId());
                datasetRepositoriesServiceFacade.deleteDatasetRepository(resource.getDatasetRepositoryId());
                resource.setDatasetRepositoryId(null);
            }
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error removing datasetRepository " + resource.getDatasetRepositoryId());
        }

    }

    private void updateGeographicCoverage(ServiceContext ctx, DatasetVersion resource) throws MetamacException {
        datasetService.updateGeographicCoverageFromSpatialAttribute(ctx, resource);
    }

    private void updateGeographicCoverageVariableElementsCache(ServiceContext ctx, DatasetVersion resource) throws MetamacException {
        // In automatic life cicle this process is done later when the principal task finished
        if (!DatasetImportUtils.isDatasetImportJob(ctx) && !DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(ctx)) {
            datasetService.updateGeographicCoverageVariableElementsCache(ctx, resource);
        }
    }

    private InternationalString buildBibliographicCitation(DatasetVersion resource) throws MetamacException {
        // Format: Creator.code (date) Title (vXXX.YYY) [dataset]. Publisher.name (api url)
        // Example: ISTAC (2017) Índice censal de ocupación (v002.001) [dataset]. Instituto Canario de Estadística (url)
        // More information in: http://www.dcc.ac.uk/resources/how-guides/cite-datasets#sec:elements

        ExternalItem creator = resource.getSiemacMetadataStatisticalResource().getCreator();
        String version = resource.getSiemacMetadataStatisticalResource().getVersionLogic();
        InternationalString publisherName = resource.getSiemacMetadataStatisticalResource().getPublisher().get(0).getTitle();

        // This will be replaced by the API
        String publicUrl = StatisticalResourcesConstants.BIBLIOGRAPHIC_CITATION_URI_TOKEN;
        InternationalString bibliographicInternational = new InternationalString();
        String pubYear = String.valueOf(resource.getSiemacMetadataStatisticalResource().getValidFrom().getYear());
        for (LocalisedString localisedTitle : resource.getSiemacMetadataStatisticalResource().getTitle().getTexts()) {
            String locale = localisedTitle.getLocale();
            StringBuilder bibliographicCitation = new StringBuilder();
            bibliographicCitation.append(creator.getCode()).append(" (").append(pubYear).append(") ");
            bibliographicCitation.append(localisedTitle.getLabel()).append(" (v").append(version).append(") [dataset].");
            bibliographicCitation.append(" ").append(getLocalisedTextInLocaleOrAppDefault(publisherName, locale));
            bibliographicCitation.append(" (").append(publicUrl).append(")");

            LocalisedString localised = new LocalisedString(locale, bibliographicCitation.toString());
            bibliographicInternational.addText(localised);
        }
        return bibliographicInternational;
    }

    private String getLocalisedTextInLocaleOrAppDefault(InternationalString internationaString, String locale) throws MetamacException {
        if (StringUtils.isNotEmpty(internationaString.getLocalisedLabel(locale))) {
            return internationaString.getLocalisedLabel(locale);
        } else {
            return internationaString.getLocalisedLabel(configurationService.retrieveLanguageDefault());
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VERSIONING
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkVersioningResource(DatasetVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected DatasetVersion copyResourceForVersioning(ServiceContext ctx, DatasetVersion previousResource) throws MetamacException {
        DatasetVersion newVersion = DatasetVersioningCopyUtils.copyDatasetVersion(previousResource);
        for (Categorisation categorisation : newVersion.getCategorisations()) {
            datasetService.initializeCategorisationMetadataForCreation(ctx, categorisation);
        }

        if (newVersion.getFormatExtentTableSize() == null) {
            newVersion.setFormatExtentTableSize(datasetService.calculateTableSize(ctx, previousResource));
        }
        return newVersion;
    }

    @Override
    protected void applyVersioningNewResource(ServiceContext ctx, DatasetVersion resource, DatasetVersion previous) throws MetamacException {

        // Versioning the content constraints associated
        constraintsService.versioningContentConstraintsForArtefact(ctx, previous.getLifeCycleStatisticalResource().getUrn(), resource.getLifeCycleStatisticalResource().getUrn(),
                guessVersionTypeEnum(resource, previous));

        resource.setUserModifiedDateNextUpdate(Boolean.FALSE);
        String oldDatasetRepositoryId = previous.getDatasetRepositoryId();
        resource.setDatasetRepositoryId(resource.getSiemacMetadataStatisticalResource().getUrn());

        List<Mapping> datasourcesMapping = DatasetVersioningCopyUtils.createDatasourceMapping(previous, resource);

        TaskInfoDataset taskInfo = new TaskInfoDataset();
        taskInfo.setDatasetVersionId(oldDatasetRepositoryId);
        taskInfo.setDatasetUrn(resource.getDataset().getIdentifiableStatisticalResource().getUrn());
        taskService.planifyDuplicationDataset(ctx, taskInfo, resource.getDatasetRepositoryId(), datasourcesMapping);
    }

    private VersionTypeEnum guessVersionTypeEnum(DatasetVersion resource, DatasetVersion previous) {
        String[] versionPreviousParts = previous.getLifeCycleStatisticalResource().getVersionLogic().split("\\.");
        String[] versionParts = resource.getLifeCycleStatisticalResource().getVersionLogic().split("\\.");
        if (versionParts[0] != null && versionPreviousParts[0] != null && Integer.valueOf(versionParts[0]) > Integer.valueOf(versionPreviousParts[0])) {
            return VersionTypeEnum.MAJOR;
        }

        return VersionTypeEnum.MINOR;
    }

    @Override
    protected void applyVersioningPreviousResource(ServiceContext ctx, DatasetVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    @Override
    protected DatasetVersion updateResourceUrn(DatasetVersion resource) throws MetamacException {
        String[] creator = new String[]{resource.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested()};

        String urn = GeneratorUrnUtils.generateSiemacStatisticalResourceDatasetVersionUrn(creator, resource.getSiemacMetadataStatisticalResource().getCode(),
                resource.getSiemacMetadataStatisticalResource().getVersionLogic());

        resource.getSiemacMetadataStatisticalResource().setUrn(urn);
        return resource;
    }

    // ------------------------------------------------------------------------------------------------------
    // GENERAL ABSTRACT METHODS
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected DatasetVersion saveResource(DatasetVersion resource) {
        return datasetVersionRepository.save(resource);
    }

    @Override
    protected DatasetVersion retrieveResourceByUrn(String urn) throws MetamacException {
        return datasetVersionRepository.retrieveByUrn(urn);
    }

    @Override
    protected DatasetVersion retrieveResourceByResource(DatasetVersion resource) throws MetamacException {
        return datasetVersionRepository.retrieveByUrn(resource.getSiemacMetadataStatisticalResource().getUrn());
    }

    @Override
    protected DatasetVersion retrievePreviousPublishedResourceByResource(DatasetVersion resource) throws MetamacException {
        return datasetVersionRepository.retrieveLastPublishedVersion(resource.getDataset().getIdentifiableStatisticalResource().getUrn());
    }

    @Override
    protected void checkResourceMetadataAllActions(ServiceContext ctx, DatasetVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        lifecycleCommonMetadataChecker.checkDatasetVersionCommonMetadata(ctx, resource, ServiceExceptionParameters.DATASET_VERSION, exceptions);
    }

    @Override
    protected String getResourceUrn(DatasetVersion resource) {
        return resource.getDataset().getIdentifiableStatisticalResource().getUrn();
    }

    @Override
    public void sendNewVersionPublishedStreamMessageByResource(ServiceContext ctx, DatasetVersion resource) {
        try {
            streamMessagingServiceFacade.sendNewVersionPublished(resource);

            // Also, A new version of DatasetVersion and there are queries with a related dataset associated with this query version, then we send queries messages
            List<QueryVersion> queriesDataset = queryVersionRepository.findQueriesPublishedLinkedToDataset(resource.getDataset().getId());

            for (QueryVersion queryVersion : queriesDataset) {
                sendNewVersionPublishedStreamMessage(ctx, queryVersion);
            }
        } catch (MetamacException e) {
            createStreamMessageSentNotification(ctx, resource);
        }
    }

    @Override
    public void checkTwitterPostActivatedAndPostTwit(ServiceContext ctx, DatasetVersion resource) {
        TwitterPostUtils.TwitterPostResult result = twitterPostUtils.postTwitterForDatasetQueries(ctx, resource);

        // Create notifications for any errors that occurred
        if (result.hasErrors()) {
            for (TwitterPostUtils.TwitterPostError error : result.getErrors()) {
                if (error.getQueryVersion() != null) {
                    createXMessageSentNotification(ctx, error.getQueryVersion(), getNotificationMessageKey(error.getErrorType()));
                } else {
                    createXMessageSentNotification(ctx, resource);
                }
            }
        }
    }

    private String getNotificationMessageKey(TwitterPostUtils.TwitterPostErrorType errorType) {
        if (errorType == TwitterPostUtils.TwitterPostErrorType.NO_X_TEMPLATE) {
            return ServiceNoticeMessage.QUERY_NOT_HAVE_X_TEMPLATE;
        } else if (errorType == TwitterPostUtils.TwitterPostErrorType.NO_DATA) {
            return ServiceNoticeMessage.QUERY_NOT_HAVE_DATA;
        } else {
            return ServiceNoticeMessage.X_MESSAGE_SEND_ERROR;
        }
    }

    protected void sendNewVersionPublishedStreamMessage(ServiceContext ctx, QueryVersion version) {
        try {
            streamMessagingServiceFacade.sendNewVersionPublished(version);
        } catch (MetamacException e) {
            createStreamMessageSentNotification(ctx, version);
        }
    }

    @Override
    public void resendDatasetStreamMessage(ServiceContext ctx) throws MetamacException {

        List<DatasetVersion> datasets = datasetService.retrievePublishedLastVersionDatasets(ctx);
        List<MetamacExceptionItem> exceptionsItems = new ArrayList<MetamacExceptionItem>();
        int totalCount = 0;
        int partialCount = 0;
        logger.info("resend all published last version dataset kafka messages start at {} affected datasets: {} ", new Date(), datasets != null ? datasets.size() : 0);

        for (DatasetVersion datasetVersion : datasets) {
            try {
                streamMessagingServiceFacade.sendNewVersionPublished(datasetVersion);
                totalCount++;
                partialCount++;
                if (partialCount >= 100) {
                    partialCount = 0;
                    logger.info("processed datasets {} at {}", totalCount, new Date());
                }

            } catch (MetamacException e) {
                totalCount++;
                partialCount++;
                logger.info("error sending dataset {} ", datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
                exceptionsItems.add(new MetamacExceptionItem(ServiceExceptionType.UNABLE_TO_SEND_STREAM_MESSAGING_DATASET, datasetVersion.getSiemacMetadataStatisticalResource().getUrn()));
            }
        }
        if (!exceptionsItems.isEmpty()) {
            MetamacException exception = MetamacExceptionBuilder.builder().withExceptionItems(exceptionsItems).build();
            createStreamMessageResendSentSomeNotifications(exception);
        }

        logger.info("resend all published last version dataset kafka messages end at {}", new Date());
    }

}
