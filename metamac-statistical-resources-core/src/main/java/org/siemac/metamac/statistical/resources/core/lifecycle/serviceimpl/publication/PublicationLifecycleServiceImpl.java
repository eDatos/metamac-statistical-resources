package org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.publication;

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
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionRepository;
import org.siemac.metamac.statistical.resources.core.publication.utils.PublicationVersioningCopyUtils;
import org.siemac.metamac.statistical.resources.core.publication.utils.PublicationsUtils;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("publicationLifecycleService")
public class PublicationLifecycleServiceImpl extends LifecycleTemplateService<PublicationVersion> {

    @Autowired
    private LifecycleCommonMetadataChecker lifecycleCommonMetadataChecker;

    @Autowired
    private PublicationVersionRepository   publicationVersionRepository;

    @Autowired
    private DatasetVersionRepository       datasetVersionRepository;

    @Autowired
    private QueryVersionRepository         queryVersionRepository;

    @Autowired
    private MultidatasetVersionRepository  multidatasetVersionRepository;

    @Override
    protected String getResourceMetadataName() throws MetamacException {
        return ServiceExceptionParameters.PUBLICATION_VERSION;
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PRODUCTION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToProductionValidationResource(PublicationVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected void applySendToProductionValidationResource(ServiceContext ctx, PublicationVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    // ------------------------------------------------------------------------------------------------------
    // >> DIFFUSION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToDiffusionValidationResource(PublicationVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected void applySendToDiffusionValidationResource(ServiceContext ctx, PublicationVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VALIDATION REJECTED
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToValidationRejectedResource(PublicationVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected void applySendToValidationRejectedResource(ServiceContext ctx, PublicationVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PUBLISHED
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void applySendToPublishedCurrentResource(ServiceContext ctx, PublicationVersion resource, PublicationVersion previousResource) throws MetamacException {
        resource.setFormatExtentResources(resource.getChildrenAllLevels().size());
        resource.getHasPart().clear();
        resource.getHasPart().addAll(PublicationsUtils.computeHasPart(resource));
    }

    @Override
    protected void applySendToPublishedPreviousResource(ServiceContext ctx, PublicationVersion resource) throws MetamacException {
        // NOTHING TO DO
    }

    @Override
    protected void checkSendToPublishedResource(ServiceContext ctx, PublicationVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        checkStructure(resource, exceptionItems);
    }

    private void checkStructure(PublicationVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        checkAtLeastOneCube(resource, exceptionItems);
        checkAllLeafChaptersMustHaveCubes(resource, exceptionItems);
        checkAllCubesLinkToDatasetQueryOrMultidataset(resource, exceptionItems);
        checkAllResourcesMustBePublishedAndVisibleBeforePublication(resource, exceptionItems);
    }

    private void checkAtLeastOneCube(PublicationVersion resource, List<MetamacExceptionItem> exceptionItems) {
        for (ElementLevel elementLevel : resource.getChildrenAllLevels()) {
            if (elementLevel.isCube()) {
                return;
            }
        }
        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_MUST_HAVE_AT_LEAST_ONE_CUBE));
    }

    private void checkAllLeafChaptersMustHaveCubes(PublicationVersion resource, List<MetamacExceptionItem> exceptionItems) {
        for (ElementLevel elementLevel : resource.getChildrenAllLevels()) {
            if (elementLevel.isChapter() && elementLevel.getChildren().isEmpty()) {
                exceptionItems.add(
                        new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_CHAPTER_MUST_HAVE_AT_LEAST_ONE_CUBE, elementLevel.getChapter().getNameableStatisticalResource().getUrn()));
            }
        }
    }

    private void checkAllCubesLinkToDatasetQueryOrMultidataset(PublicationVersion resource, List<MetamacExceptionItem> exceptionItems) {
        for (ElementLevel elementLevel : resource.getChildrenAllLevels()) {
            if (elementLevel.isCube() && elementLevel.getCube().getDataset() == null && elementLevel.getCube().getQuery() == null && elementLevel.getCube().getMultidataset() == null) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_CUBE_MUST_LINK_TO_DATASET_QUERY_OR_MULTIDATASET,
                        elementLevel.getCube().getNameableStatisticalResource().getUrn()));
            }
        }
    }

    private void checkAllResourcesMustBePublishedAndVisibleBeforePublication(PublicationVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        for (ElementLevel elementLevel : resource.getChildrenAllLevels()) {
            if (elementLevel.isCube()) {
                Cube cube = elementLevel.getCube();
                if (cube.getDataset() != null) {
                    checkDatasetMustHaveSomeVersionPublishedAndVisibleBeforePublication(resource, exceptionItems, cube.getDatasetUrn());
                } else if (cube.getQuery() != null) {
                    checkQueryMustBePublishedAndVisibleBeforePublication(resource, exceptionItems, cube.getQueryUrn());
                } else if (cube.getMultidataset() != null) {
                    checkMultidatasetMustBePublishedAndVisibleBeforePublication(exceptionItems, cube.getMultidatasetUrn());
                }
            }
        }
    }

    protected void checkDatasetMustHaveSomeVersionPublishedAndVisibleBeforePublication(PublicationVersion resource, List<MetamacExceptionItem> exceptionItems, String datasetUrn)
            throws MetamacException {
        DatasetVersion lastPublishedVersion = datasetVersionRepository.retrieveLastPublishedVersion(datasetUrn);
        if (lastPublishedVersion == null) {
            DatasetVersion lastVersion = datasetVersionRepository.retrieveLastVersion(datasetUrn);
            if (ProcStatusEnumUtils.isInAnyProcStatus(lastVersion, ProcStatusEnum.PUBLISHED)) {
                if (lastVersion.getSiemacMetadataStatisticalResource().getValidFrom().isAfter(resource.getSiemacMetadataStatisticalResource().getValidFrom())) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_LINKED_TO_NOT_PUBLISHED_DATASET, datasetUrn));
                }
            } else {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_LINKED_TO_NOT_PUBLISHED_DATASET, datasetUrn));
            }
        }
    }

    protected void checkQueryMustBePublishedAndVisibleBeforePublication(PublicationVersion resource, List<MetamacExceptionItem> exceptionItems, String queryUrn) throws MetamacException {
        QueryVersion lastPublishedVersion = queryVersionRepository.retrieveLastPublishedVersion(queryUrn);
        if (lastPublishedVersion == null) {
            QueryVersion lastVersion = queryVersionRepository.retrieveLastVersion(queryUrn);
            if (ProcStatusEnumUtils.isInAnyProcStatus(lastVersion, ProcStatusEnum.PUBLISHED)) {
                if (lastVersion.getLifeCycleStatisticalResource().getValidFrom().isAfter(resource.getSiemacMetadataStatisticalResource().getValidFrom())) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_LINKED_TO_NOT_PUBLISHED_QUERY, queryUrn));
                }
            } else {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_LINKED_TO_NOT_PUBLISHED_QUERY, queryUrn));
            }
        }
    }

    protected void checkMultidatasetMustBePublishedAndVisibleBeforePublication(List<MetamacExceptionItem> exceptionItems, String multidatasetUrn) throws MetamacException {
        MultidatasetVersion lastPublishedVersion = multidatasetVersionRepository.retrieveLastPublishedVersion(multidatasetUrn);
        if (lastPublishedVersion == null) {
            MultidatasetVersion lastVersion = multidatasetVersionRepository.retrieveLastVersion(multidatasetUrn);
            if (!ProcStatusEnumUtils.isInAnyProcStatus(lastVersion, ProcStatusEnum.PUBLISHED)) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_LINKED_TO_NOT_PUBLISHED_MULTIDATASET, multidatasetUrn));
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VERSIONING
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkVersioningResource(PublicationVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected PublicationVersion copyResourceForVersioning(ServiceContext ctx, PublicationVersion previousResource) throws MetamacException {
        return PublicationVersioningCopyUtils.copyPublicationVersion(previousResource);
    }

    @Override
    protected void applyVersioningNewResource(ServiceContext ctx, PublicationVersion resource, PublicationVersion previous) throws MetamacException {
        // nothing specific to apply
    }

    @Override
    protected void applyVersioningPreviousResource(ServiceContext ctx, PublicationVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    @Override
    protected PublicationVersion updateResourceUrn(PublicationVersion resource) throws MetamacException {
        String[] creator = new String[]{resource.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested()};
        resource.getSiemacMetadataStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceCollectionVersionUrn(creator,
                resource.getSiemacMetadataStatisticalResource().getCode(), resource.getSiemacMetadataStatisticalResource().getVersionLogic()));
        return resource;
    }

    // ------------------------------------------------------------------------------------------------------
    // GENERAL ABSTRACT METHODS
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected PublicationVersion saveResource(PublicationVersion resource) {
        return publicationVersionRepository.save(resource);
    }

    @Override
    protected PublicationVersion retrieveResourceByUrn(String urn) throws MetamacException {
        return publicationVersionRepository.retrieveByUrn(urn);
    }

    @Override
    protected PublicationVersion retrieveResourceByResource(PublicationVersion resource) throws MetamacException {
        return publicationVersionRepository.retrieveByUrn(resource.getSiemacMetadataStatisticalResource().getUrn());
    }

    @Override
    protected PublicationVersion retrievePreviousPublishedResourceByResource(PublicationVersion resource) throws MetamacException {
        return publicationVersionRepository.retrieveLastPublishedVersion(resource.getPublication().getIdentifiableStatisticalResource().getUrn());
    }

    @Override
    protected void checkResourceMetadataAllActions(ServiceContext ctx, PublicationVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        lifecycleCommonMetadataChecker.checkPublicationVersionCommonMetadata(resource, ServiceExceptionParameters.PUBLICATION_VERSION, exceptions);
    }

    @Override
    protected String getResourceUrn(PublicationVersion resource) {
        return resource.getSiemacMetadataStatisticalResource().getUrn();
    }

    @Override
    public void sendNewVersionPublishedStreamMessageByResource(ServiceContext ctx, PublicationVersion resource) {
        try {
            streamMessagingServiceFacade.sendNewVersionPublished(resource);
        } catch (MetamacException e) {
            createStreamMessageSentNotification(ctx, resource);
        }
    }

}