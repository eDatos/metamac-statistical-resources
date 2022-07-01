package org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.query;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.utils.ProcStatusEnumUtils;
import org.siemac.metamac.statistical.resources.core.enume.utils.QueryStatusEnumUtils;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.lifecycle.LifecycleCommonMetadataChecker;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceapi.query.QueryLifecycleService;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.LifecycleTemplateService;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.serviceapi.QueryService;
import org.siemac.metamac.statistical.resources.core.query.utils.QueryVersioningCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("queryLifecycleService")
public class QueryLifecycleServiceImpl extends LifecycleTemplateService<QueryVersion> implements QueryLifecycleService {

    @Autowired
    private LifecycleCommonMetadataChecker lifecycleCommonMetadataChecker;

    @Autowired
    private QueryVersionRepository         queryVersionRepository;

    @Autowired
    private PublicationVersionRepository   publicationVersionRepository;

    @Autowired
    private QueryService                   queryService;

    @Autowired
    private DatasetVersionRepository       datasetVersionRepository;

    @Override
    protected String getResourceMetadataName() throws MetamacException {
        return ServiceExceptionParameters.QUERY_VERSION;
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PRODUCTION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToProductionValidationResource(QueryVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected void applySendToProductionValidationResource(ServiceContext ctx, QueryVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    // ------------------------------------------------------------------------------------------------------
    // >> DIFFUSION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToDiffusionValidationResource(QueryVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected void applySendToDiffusionValidationResource(ServiceContext ctx, QueryVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VALIDATION REJECTED
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToValidationRejectedResource(QueryVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected void applySendToValidationRejectedResource(ServiceContext ctx, QueryVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PUBLISHED
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToPublishedResource(ServiceContext ctx, QueryVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        QueryStatusEnumUtils.checkPossibleQueryStatus(resource, QueryStatusEnum.ACTIVE, QueryStatusEnum.DISCONTINUED);
        this.checkLinkedDatasetOrDatasetVersionPublishedForQuery(ctx, resource, exceptionItems);
    }

    @Override
    public void checkLinkedDatasetOrDatasetVersionPublishedBeforeQuery(ServiceContext ctx, QueryVersion resource) throws MetamacException {
        if (ProcStatusEnumUtils.isInAnyProcStatus(resource, ProcStatusEnum.PUBLISHED)) {
            List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
            checkLinkedDatasetOrDatasetVersionPublishedForQuery(ctx, resource, exceptionItems);
            ExceptionUtils.throwIfException(exceptionItems);
        }
    }

    @Override
    protected void applySendToPublishedCurrentResource(ServiceContext ctx, QueryVersion resource, QueryVersion previousResource) throws MetamacException {
        // nothing to do
    }

    @Override
    protected void applySendToPublishedPreviousResource(ServiceContext ctx, QueryVersion resource) throws MetamacException {
        // nothing to do
    }

    private void checkLinkedDatasetOrDatasetVersionPublishedForQuery(ServiceContext ctx, QueryVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        if (resource.getDataset() != null) {
            this.checkLinkedDatasetPublishedOrVisibleBeforeQuery(ctx, resource, exceptionItems);
        } else if (resource.getFixedDatasetVersion() != null) {
            this.checkLinkedDatasetVersionPublishedOrVisibleForQuery(resource, exceptionItems);
        } else {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.QUERY_VERSION_PUBLISH_MUST_LINK_TO_DATASET, resource.getLifeCycleStatisticalResource().getUrn()));
        }
    }

    private void checkLinkedDatasetPublishedOrVisibleBeforeQuery(ServiceContext ctx, QueryVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        String datasetUrn = resource.getDataset().getIdentifiableStatisticalResource().getUrn();
        DatasetVersion lastVersion = this.datasetVersionRepository.retrieveLastVersion(datasetUrn);

        if (!this.isDatasetVersionPublishedAndVisibleBeforeOrEqualDate(lastVersion, resource.getLifeCycleStatisticalResource().getValidFrom())) {
            DatasetVersion lastPublishedVersion = this.datasetVersionRepository.retrieveLastPublishedVersion(datasetUrn);
            if (lastPublishedVersion != null) {
                if (!this.queryService.checkQueryCompatibility(ctx, resource, lastPublishedVersion)) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.QUERY_VERSION_NOT_COMPATIBLE_WITH_LAST_PUBLISHED_DATASET_VERSION, datasetUrn));
                }
            } else {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.QUERY_VERSION_DATASET_WITH_NO_PUBLISHED_VERSION, datasetUrn));
            }
        }
    }

    private void checkLinkedDatasetVersionPublishedOrVisibleForQuery(QueryVersion resource, List<MetamacExceptionItem> exceptionItems) {
        String datasetVersionUrn = resource.getFixedDatasetVersion().getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersion datasetVersion = resource.getFixedDatasetVersion();

        if (!this.isDatasetVersionPublishedAndVisibleBeforeOrEqualDate(datasetVersion, resource.getLifeCycleStatisticalResource().getValidFrom())) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.QUERY_VERSION_DATASET_VERSION_MUST_BE_PUBLISHED, datasetVersionUrn));
        }
    }

    private boolean isDatasetVersionPublishedAndVisibleBeforeOrEqualDate(DatasetVersion datasetVersion, DateTime date) {
        if (ProcStatusEnumUtils.isInAnyProcStatus(datasetVersion, ProcStatusEnum.PUBLISHED)) {
            return true;
        }
        return false;
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VERSIONING
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkVersioningResource(QueryVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected QueryVersion copyResourceForVersioning(ServiceContext ctx, QueryVersion previousResource) throws MetamacException {
        return QueryVersioningCopyUtils.copyQueryVersion(previousResource);
    }

    @Override
    protected void applyVersioningNewResource(ServiceContext ctx, QueryVersion resource, QueryVersion previous) throws MetamacException {
        // nothing specific to apply
    }

    @Override
    protected void applyVersioningPreviousResource(ServiceContext ctx, QueryVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    @Override
    protected QueryVersion updateResourceUrn(QueryVersion resource) throws MetamacException {
        String[] creator = new String[]{resource.getLifeCycleStatisticalResource().getMaintainer().getCodeNested()};
        resource.getLifeCycleStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceQueryVersionUrn(creator, resource.getLifeCycleStatisticalResource().getCode(),
                resource.getLifeCycleStatisticalResource().getVersionLogic()));
        return resource;
    }

    // ------------------------------------------------------------------------------------------------------
    // GENERAL ABSTRACT METHODS
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected QueryVersion saveResource(QueryVersion resource) {
        return this.queryVersionRepository.save(resource);
    }

    @Override
    protected QueryVersion retrieveResourceByUrn(String urn) throws MetamacException {
        return this.queryVersionRepository.retrieveByUrn(urn);
    }

    @Override
    protected QueryVersion retrieveResourceByResource(QueryVersion resource) throws MetamacException {
        return this.queryVersionRepository.retrieveByUrn(resource.getLifeCycleStatisticalResource().getUrn());
    }

    @Override
    protected QueryVersion retrievePreviousPublishedResourceByResource(QueryVersion resource) throws MetamacException {
        return this.queryVersionRepository.retrieveLastPublishedVersion(resource.getQuery().getIdentifiableStatisticalResource().getUrn());
    }

    @Override
    protected void checkResourceMetadataAllActions(ServiceContext ctx, QueryVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        this.lifecycleCommonMetadataChecker.checkQueryVersionCommonMetadata(resource, ServiceExceptionParameters.QUERY_VERSION, exceptions);
    }

    @Override
    protected String getResourceUrn(QueryVersion resource) {
        return resource.getLifeCycleStatisticalResource().getUrn();
    }

    @Override
    public void sendNewVersionPublishedStreamMessageByResource(ServiceContext ctx, QueryVersion resource) {
        try {
            streamMessagingServiceFacade.sendNewVersionPublished(resource);
        } catch (MetamacException e) {
            createStreamMessageSentNotification(ctx, resource);
        }
    }

}