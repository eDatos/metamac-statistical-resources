package org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.multidataset;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.utils.ProcStatusEnumUtils;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.lifecycle.LifecycleCommonMetadataChecker;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.LifecycleTemplateService;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetCube;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.multidataset.utils.MultidatasetVersioningCopyUtils;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("multidatasetLifecycleService")
public class MultidatasetLifecycleServiceImpl extends LifecycleTemplateService<MultidatasetVersion> {

    @Autowired
    private LifecycleCommonMetadataChecker lifecycleCommonMetadataChecker;

    @Autowired
    private MultidatasetVersionRepository  multidatasetVersionRepository;

    @Autowired
    private DatasetVersionRepository       datasetVersionRepository;

    @Autowired
    private QueryVersionRepository         queryVersionRepository;

    @Override
    protected String getResourceMetadataName() throws MetamacException {
        return ServiceExceptionParameters.MULTIDATASET_VERSION;
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PRODUCTION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToProductionValidationResource(MultidatasetVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected void applySendToProductionValidationResource(ServiceContext ctx, MultidatasetVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    // ------------------------------------------------------------------------------------------------------
    // >> DIFFUSION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToDiffusionValidationResource(MultidatasetVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected void applySendToDiffusionValidationResource(ServiceContext ctx, MultidatasetVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VALIDATION REJECTED
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToValidationRejectedResource(MultidatasetVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected void applySendToValidationRejectedResource(ServiceContext ctx, MultidatasetVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PUBLISHED
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void applySendToPublishedCurrentResource(ServiceContext ctx, MultidatasetVersion resource, MultidatasetVersion previousResource) throws MetamacException {
        resource.setFormatExtentResources(resource.getCubes().size());
    }

    @Override
    protected void applySendToPublishedPreviousResource(ServiceContext ctx, MultidatasetVersion resource) throws MetamacException {
        // NOTHING TO DO
    }

    @Override
    protected void checkSendToPublishedResource(ServiceContext ctx, MultidatasetVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        checkStructure(resource, exceptionItems);
    }

    private void checkStructure(MultidatasetVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        checkAtLeastOneCube(resource, exceptionItems);
        checkAllCubesLinkToDatasetOrQuery(resource, exceptionItems);
        checkAllResourcesMustBePublishedAndVisibleBeforeMultidataset(resource, exceptionItems);
    }

    private void checkAtLeastOneCube(MultidatasetVersion resource, List<MetamacExceptionItem> exceptionItems) {
        if (resource.getCubes().size() > 0) {
            return;
        }
        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.MULTIDATASET_VERSION_MUST_HAVE_AT_LEAST_ONE_CUBE));
    }

    private void checkAllCubesLinkToDatasetOrQuery(MultidatasetVersion resource, List<MetamacExceptionItem> exceptionItems) {
        for (MultidatasetCube cubes : resource.getCubes()) {
            if (cubes.getDataset() == null && cubes.getQuery() == null) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.MULTIDATASET_VERSION_CUBE_MUST_LINK_TO_DATASET_OR_QUERY, cubes.getNameableStatisticalResource().getUrn()));
            }
        }
    }

    private void checkAllResourcesMustBePublishedAndVisibleBeforeMultidataset(MultidatasetVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        for (MultidatasetCube cube : resource.getCubes()) {
            if (cube.getDataset() != null) {
                checkDatasetMustHaveSomeVersionPublishedAndVisibleBeforeMultidataset(resource, exceptionItems, cube.getDatasetUrn());
            } else if (cube.getQuery() != null) {
                checkQueryMustBePublishedAndVisibleBeforeMultidataset(resource, exceptionItems, cube.getQueryUrn());
            }
        }
    }

    protected void checkDatasetMustHaveSomeVersionPublishedAndVisibleBeforeMultidataset(MultidatasetVersion resource, List<MetamacExceptionItem> exceptionItems, String datasetUrn)
            throws MetamacException {
        DatasetVersion lastPublishedVersion = datasetVersionRepository.retrieveLastPublishedVersion(datasetUrn);
        if (lastPublishedVersion == null) {
            DatasetVersion lastVersion = datasetVersionRepository.retrieveLastVersion(datasetUrn);
            if (ProcStatusEnumUtils.isInAnyProcStatus(lastVersion, ProcStatusEnum.PUBLISHED)) {
                if (lastVersion.getSiemacMetadataStatisticalResource().getValidFrom().isAfter(resource.getSiemacMetadataStatisticalResource().getValidFrom())) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.MULTIDATASET_VERSION_LINKED_TO_NOT_PUBLISHED_DATASET, datasetUrn));
                }
            } else {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.MULTIDATASET_VERSION_LINKED_TO_NOT_PUBLISHED_DATASET, datasetUrn));
            }
        }
    }

    protected void checkQueryMustBePublishedAndVisibleBeforeMultidataset(MultidatasetVersion resource, List<MetamacExceptionItem> exceptionItems, String queryUrn) throws MetamacException {
        QueryVersion lastPublishedVersion = queryVersionRepository.retrieveLastPublishedVersion(queryUrn);
        if (lastPublishedVersion == null) {
            QueryVersion lastVersion = queryVersionRepository.retrieveLastVersion(queryUrn);
            if (ProcStatusEnumUtils.isInAnyProcStatus(lastVersion, ProcStatusEnum.PUBLISHED)) {
                if (lastVersion.getLifeCycleStatisticalResource().getValidFrom().isAfter(resource.getSiemacMetadataStatisticalResource().getValidFrom())) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.MULTIDATASET_VERSION_LINKED_TO_NOT_PUBLISHED_QUERY, queryUrn));
                }
            } else {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.MULTIDATASET_VERSION_LINKED_TO_NOT_PUBLISHED_QUERY, queryUrn));
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VERSIONING
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkVersioningResource(MultidatasetVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected MultidatasetVersion copyResourceForVersioning(ServiceContext ctx, MultidatasetVersion previousResource) throws MetamacException {
        return MultidatasetVersioningCopyUtils.copyMultidatasetVersion(previousResource);
    }

    @Override
    protected void applyVersioningNewResource(ServiceContext ctx, MultidatasetVersion resource, MultidatasetVersion previous) throws MetamacException {
        // nothing specific to apply
    }

    @Override
    protected void applyVersioningPreviousResource(ServiceContext ctx, MultidatasetVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    @Override
    protected MultidatasetVersion updateResourceUrn(MultidatasetVersion resource) throws MetamacException {
        String[] creator = new String[]{resource.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested()};
        resource.getSiemacMetadataStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceMultidatasetVersionUrn(creator,
                resource.getSiemacMetadataStatisticalResource().getCode(), resource.getSiemacMetadataStatisticalResource().getVersionLogic()));
        return resource;
    }

    // ------------------------------------------------------------------------------------------------------
    // GENERAL ABSTRACT METHODS
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected MultidatasetVersion saveResource(MultidatasetVersion resource) {
        return multidatasetVersionRepository.save(resource);
    }

    @Override
    protected MultidatasetVersion retrieveResourceByUrn(String urn) throws MetamacException {
        return multidatasetVersionRepository.retrieveByUrn(urn);
    }

    @Override
    protected MultidatasetVersion retrieveResourceByResource(MultidatasetVersion resource) throws MetamacException {
        return multidatasetVersionRepository.retrieveByUrn(resource.getSiemacMetadataStatisticalResource().getUrn());
    }

    @Override
    protected MultidatasetVersion retrievePreviousPublishedResourceByResource(MultidatasetVersion resource) throws MetamacException {
        return multidatasetVersionRepository.retrieveLastPublishedVersion(resource.getMultidataset().getIdentifiableStatisticalResource().getUrn());
    }

    @Override
    protected void checkResourceMetadataAllActions(ServiceContext ctx, MultidatasetVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        lifecycleCommonMetadataChecker.checkMultidatasetVersionCommonMetadata(resource, ServiceExceptionParameters.MULTIDATASET_VERSION, exceptions);
    }

    @Override
    protected String getResourceUrn(MultidatasetVersion resource) {
        return resource.getSiemacMetadataStatisticalResource().getUrn();
    }

    @Override
    public void sendNewVersionPublishedStreamMessageByResource(ServiceContext ctx, MultidatasetVersion resource) {
        // TODO METAMAC-2715 - Realizar la notificación a Kafka de los recursos Multidataset
    }

}