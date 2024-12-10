package org.siemac.metamac.statistical.resources.core.multidataset.serviceimpl;

import static org.siemac.metamac.statistical.resources.core.base.domain.utils.RelatedResourceResultUtils.getUrnsFromRelatedResourceResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.statistical.resources.core.base.components.SiemacStatisticalResourceGeneratedCode;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResourceRepository;
import org.siemac.metamac.statistical.resources.core.base.utils.FillMetadataForCreateResourceUtils;
import org.siemac.metamac.statistical.resources.core.base.validators.ProcStatusValidator;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceRepository;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetCube;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionProperties;
import org.siemac.metamac.statistical.resources.core.multidataset.serviceapi.validators.MultidatasetServiceInvocationValidator;
import org.siemac.metamac.statistical.resources.core.multidataset.utils.MultidatasetCubeComparator;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoResources;
import org.siemac.metamac.statistical.resources.core.task.serviceimpl.TaskServiceImpl;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesVersionUtils;
import org.siemac.metamac.statistical.resources.core.utils.shared.StatisticalResourcesUrnParserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of MultidatasetService.
 */
@Service("multidatasetService")
public class MultidatasetServiceImpl extends MultidatasetServiceImplBase {

    @Autowired
    private IdentifiableStatisticalResourceRepository identifiableStatisticalResourceRepository;

    @Autowired
    private SiemacStatisticalResourceGeneratedCode    siemacStatisticalResourceGeneratedCode;

    @Autowired
    private MultidatasetServiceInvocationValidator    multidatasetServiceInvocationValidator;

    @Autowired
    private RelatedResourceRepository                 relatedResourceRepository;

    public MultidatasetServiceImpl() {
    }

    @Override
    public MultidatasetVersion createMultidatasetVersion(ServiceContext ctx, MultidatasetVersion multidatasetVersion, ExternalItem statisticalOperation) throws MetamacException {
        // Validations
        multidatasetServiceInvocationValidator.checkCreateMultidatasetVersion(ctx, multidatasetVersion, statisticalOperation);

        // Create multidataset
        Multidataset multidataset = new Multidataset();
        fillMetadataForCreateMultidataset(multidataset, statisticalOperation, ctx);

        // Fill metadata
        fillMetadataForCreateMultidatasetVersion(multidatasetVersion, statisticalOperation, ctx);

        // Add version to multidataset and Save version
        multidatasetVersion.setMultidataset(multidataset);
        assignCodeAndSaveMultidatasetVersion(multidataset, multidatasetVersion);

        // Retrieve multidatasetVersion
        multidatasetVersion = getMultidatasetVersionRepository().retrieveByUrn(multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        return multidatasetVersion;
    }

    @Override
    public MultidatasetVersion updateMultidatasetVersion(ServiceContext ctx, MultidatasetVersion multidatasetVersion) throws MetamacException {
        // Validations
        multidatasetServiceInvocationValidator.checkUpdateMultidatasetVersion(ctx, multidatasetVersion);

        // Check status
        ProcStatusValidator.checkStatisticalResourceCanBeEdited(multidatasetVersion);

        // It's not necessary recreate URN because the code is assigned in the create service and never change.
        identifiableStatisticalResourceRepository.checkDuplicatedUrn(multidatasetVersion.getSiemacMetadataStatisticalResource());

        multidatasetVersion = getMultidatasetVersionRepository().save(multidatasetVersion);
        return multidatasetVersion;
    }

    @Override
    public MultidatasetVersion retrieveMultidatasetVersionByUrn(ServiceContext ctx, String multidatasetVersionUrn) throws MetamacException {
        // Validations
        multidatasetServiceInvocationValidator.checkRetrieveMultidatasetVersionByUrn(ctx, multidatasetVersionUrn);

        // Retrieve
        MultidatasetVersion multidatasetVersion = getMultidatasetVersionRepository().retrieveByUrn(multidatasetVersionUrn);
        return multidatasetVersion;
    }

    @Override
    public MultidatasetVersion retrieveLatestMultidatasetVersionByMultidatasetUrn(ServiceContext ctx, String multidatasetUrn) throws MetamacException {
        // Validations
        multidatasetServiceInvocationValidator.checkRetrieveLatestMultidatasetVersionByMultidatasetUrn(ctx, multidatasetUrn);

        // Retrieve
        MultidatasetVersion multidatasetVersion = getMultidatasetVersionRepository().retrieveLastVersion(multidatasetUrn);
        return multidatasetVersion;
    }

    @Override
    public MultidatasetVersion retrieveLatestPublishedMultidatasetVersionByMultidatasetUrn(ServiceContext ctx, String multidatasetUrn) throws MetamacException {

        // Validations
        multidatasetServiceInvocationValidator.checkRetrieveLatestPublishedMultidatasetVersionByMultidatasetUrn(ctx, multidatasetUrn);

        // Retrieve
        MultidatasetVersion multidatasetVersion = getMultidatasetVersionRepository().retrieveLastPublishedVersion(multidatasetUrn);
        return multidatasetVersion;

    }

    @Override
    public List<MultidatasetVersion> retrieveMultidatasetVersions(ServiceContext ctx, String multidatasetVersionUrn) throws MetamacException {

        // Validations
        multidatasetServiceInvocationValidator.checkRetrieveMultidatasetVersions(ctx, multidatasetVersionUrn);

        // Retrieve
        List<MultidatasetVersion> multidatasetVersions = getMultidatasetVersionRepository().retrieveByUrn(multidatasetVersionUrn).getMultidataset().getVersions();

        return multidatasetVersions;
    }

    @Override
    public PagedResult<MultidatasetVersion> findMultidatasetVersionsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Validations
        multidatasetServiceInvocationValidator.checkFindMultidatasetVersionsByCondition(ctx, conditions, pagingParameter);

        // Find
        conditions = CriteriaUtils.initConditions(conditions, MultidatasetVersion.class);
        pagingParameter = CriteriaUtils.initPagingParameter(pagingParameter);

        PagedResult<MultidatasetVersion> multidatasetVersionPagedResult = getMultidatasetVersionRepository().findByCondition(conditions, pagingParameter);
        return multidatasetVersionPagedResult;

    }

    @Override
    public void deleteMultidatasetVersion(ServiceContext ctx, String multidatasetVersionUrn) throws MetamacException {
        // Validations
        multidatasetServiceInvocationValidator.checkDeleteMultidatasetVersion(ctx, multidatasetVersionUrn);

        // Retrieve version to delete
        MultidatasetVersion multidatasetVersion = retrieveMultidatasetVersionByUrn(ctx, multidatasetVersionUrn);

        // Check can be deleted
        ProcStatusValidator.checkStatisticalResourceCanBeDeleted(multidatasetVersion);

        checkCanMultidatasetVersionBeDeleted(multidatasetVersion);

        updateReplacedResourceIsReplacedByResource(multidatasetVersion);

        if (StatisticalResourcesVersionUtils.isInitialVersion(multidatasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic())) {
            Multidataset multidataset = multidatasetVersion.getMultidataset();
            getMultidatasetRepository().delete(multidataset);
        } else {
            // Previous version
            updateReplacedVersionIsReplacedByVersion(multidatasetVersion);

            // Delete version
            Multidataset multidataset = multidatasetVersion.getMultidataset();
            multidataset.getVersions().remove(multidatasetVersion);
            getMultidatasetVersionRepository().delete(multidatasetVersion);
        }

    }

    @Override
    public void updateGeographicalCache(ServiceContext ctx, MultidatasetVersion multidatasetVersion) throws MetamacException {
        updateGeographicalCacheInJob(ctx, multidatasetVersion, true, false);
    }

    private void updateGeographicalCacheInJob(ServiceContext ctx, MultidatasetVersion multidatasetVersion, boolean sendNotification, boolean mustWaitForRelatedResourcesUpdate)
            throws MetamacException {
        ProcStatusValidator.checkStatisticalResourceStructureCanBeCached(multidatasetVersion);

        String multidatasetUrn = multidatasetVersion.getMultidataset().getIdentifiableStatisticalResource().getUrn();
        String multidatasetVersionUrn = multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        checkNotTasksInProgress(ctx, multidatasetVersionUrn);

        TaskInfoResources taskInfo = new TaskInfoResources();
        taskInfo.setVersionId(multidatasetVersionUrn);
        taskInfo.setUrn(multidatasetUrn);
        taskInfo.setResourceType(StatisticalResourceTypeEnum.MULTIDATASET.name());
        taskInfo.setMustWaitForRelatedResourcesUpdate(mustWaitForRelatedResourcesUpdate);
        getTaskService().planifyUpdateGeographicalCacheRelatedResource(ctx, taskInfo, sendNotification);

    }

    private void checkNotTasksInProgress(ServiceContext ctx, String multidatasetUrn) throws MetamacException {
        if (getTaskService().existsTaskForResource(ctx, multidatasetUrn)) {
            throw new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, multidatasetUrn);
        }
    }

    @Override
    public void updateAllGeographicalCache(ServiceContext ctx, boolean mustWaitForRelatedResourcesUpdate) throws MetamacException {
        multidatasetServiceInvocationValidator.checkUpdateAllGeographicalCache(ctx, mustWaitForRelatedResourcesUpdate);

        if (!mustWaitForRelatedResourcesUpdate && checkCanUpdateAllGeographicalCache(ctx)) {
            mustWaitForRelatedResourcesUpdate = true;
        }

        List<MultidatasetVersion> lastVersionMultidataset = retrievePublishedLastVersionMultidatasets();
        for (MultidatasetVersion multidatasetVersion : lastVersionMultidataset) {
            updateGeographicalCacheInJob(ctx, multidatasetVersion, false, mustWaitForRelatedResourcesUpdate);
        }

    }

    private boolean checkCanUpdateAllGeographicalCache(ServiceContext ctx) throws MetamacException {
        List<String> tasksName = new ArrayList<>();
        tasksName.add(TaskServiceImpl.PREFIX_JOB_UPDATE_GEOCOVERAGE_CACHE);
        tasksName.add(TaskServiceImpl.PREFIX_JOB_UPDATE_GEO_CACHE_RELATED_RESOURCES + StatisticalResourcesUrnParserUtils.getPrefixQueryUrn()); // TODO EDATOS-4587
        return getTaskService().existsGeoCacheTasksByTaskName(ctx, tasksName);
    }

    private List<MultidatasetVersion> retrievePublishedLastVersionMultidatasets() throws MetamacException {

        List<ConditionalCriteria> criteria = ConditionalCriteriaBuilder.criteriaFor(MultidatasetVersion.class)
                .withProperty(MultidatasetVersionProperties.siemacMetadataStatisticalResource().procStatus()).eq(ProcStatusEnum.PUBLISHED).and()
                .withProperty(MultidatasetVersionProperties.siemacMetadataStatisticalResource().validTo()).isNull().distinctRoot().build();
        return getMultidatasetVersionRepository().findByCondition(criteria);

    }

    private void updateReplacedVersionIsReplacedByVersion(MultidatasetVersion multidatasetVersion) {
        RelatedResource previousResource = multidatasetVersion.getSiemacMetadataStatisticalResource().getReplacesVersion();
        if (previousResource.getMultidatasetVersion() != null) {
            MultidatasetVersion previousVersion = previousResource.getMultidatasetVersion();
            previousVersion.getSiemacMetadataStatisticalResource().setLastVersion(true);
            RelatedResource isReplacedByVersion = previousVersion.getSiemacMetadataStatisticalResource().getIsReplacedByVersion();
            relatedResourceRepository.delete(isReplacedByVersion);
            previousVersion.getSiemacMetadataStatisticalResource().setIsReplacedByVersion(null);
            getMultidatasetVersionRepository().save(previousVersion);
        }
    }

    private void updateReplacedResourceIsReplacedByResource(MultidatasetVersion multidatasetVersion) {
        RelatedResource previousResource = multidatasetVersion.getSiemacMetadataStatisticalResource().getReplaces();
        if (previousResource != null && previousResource.getMultidatasetVersion() != null) {
            MultidatasetVersion previousVersion = previousResource.getMultidatasetVersion();
            RelatedResource isReplacedBy = previousVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy();
            relatedResourceRepository.delete(isReplacedBy);
            previousVersion.getSiemacMetadataStatisticalResource().setIsReplacedBy(null);
            getMultidatasetVersionRepository().save(previousVersion);
        }
    }

    private void checkCanMultidatasetVersionBeDeleted(MultidatasetVersion multidatasetVersion) throws MetamacException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();

        boolean isOnlyVersion = StatisticalResourcesVersionUtils.isInitialVersion(multidatasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic());
        if (isOnlyVersion) {
            checkMultiDatasetVersionIsPartOfSomePublication(multidatasetVersion, exceptionItems);
        }

        RelatedResource resourcesIsReplacedBy = multidatasetVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy();
        if (resourcesIsReplacedBy != null) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.MULTIDATASET_VERSION_IS_REPLACED_BY_OTHER_RESOURCE,
                    resourcesIsReplacedBy.getMultidatasetVersion().getLifeCycleStatisticalResource().getUrn()));
        }

        if (exceptionItems.size() > 0) {
            MetamacExceptionItem item = new MetamacExceptionItem(ServiceExceptionType.MULTIDATASET_VERSION_CANT_BE_DELETED, multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());
            item.setExceptionItems(exceptionItems);
            throw new MetamacException(Arrays.asList(item));
        }
    }

    private void checkMultiDatasetVersionIsPartOfSomePublication(MultidatasetVersion multidatasetVersion, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        List<RelatedResourceResult> resourcesIsPartOf = getMultidatasetVersionRepository().retrieveIsPartOf(multidatasetVersion);
        if (!resourcesIsPartOf.isEmpty()) {
            List<String> urns = getUrnsFromRelatedResourceResults(resourcesIsPartOf);
            Collections.sort(urns);
            String parameter = StringUtils.join(urns, ", ");
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.MULTIDATASET_VERSION_IS_PART_OF_OTHER_RESOURCES, parameter));
        }
    }

    // ------------------------------------------------------------------------
    // PRIVATE METHODS
    // ------------------------------------------------------------------------
    private static void fillMetadataForCreateMultidataset(Multidataset multidataset, ExternalItem statisticalOperation, ServiceContext ctx) {
        multidataset.setIdentifiableStatisticalResource(new IdentifiableStatisticalResource());
        FillMetadataForCreateResourceUtils.fillMetadataForCreateIdentifiableResource(multidataset.getIdentifiableStatisticalResource(), statisticalOperation);
    }

    private static void fillMetadataForCreateMultidatasetVersion(MultidatasetVersion multidatasetVersion, ExternalItem statisticalOperation, ServiceContext ctx) {
        FillMetadataForCreateResourceUtils.fillMetadataForCretateSiemacResource(multidatasetVersion.getSiemacMetadataStatisticalResource(), statisticalOperation,
                StatisticalResourceTypeEnum.MULTIDATASET, ctx);
    }

    private synchronized Multidataset assignCodeAndSaveMultidatasetVersion(Multidataset multidataset, MultidatasetVersion multidatasetVersion) throws MetamacException {
        String code = siemacStatisticalResourceGeneratedCode.fillGeneratedCodeForCreateSiemacMetadataResource(multidatasetVersion.getSiemacMetadataStatisticalResource());
        String[] maintainer = new String[]{multidatasetVersion.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested()};

        // Fill code and urn for root and version
        multidataset.getIdentifiableStatisticalResource().setCode(code);
        multidataset.getIdentifiableStatisticalResource()
                .setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceMultidatasetUrn(maintainer, multidataset.getIdentifiableStatisticalResource().getCode()));

        multidatasetVersion.getSiemacMetadataStatisticalResource().setCode(code);
        multidatasetVersion.getSiemacMetadataStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceMultidatasetVersionUrn(maintainer,
                multidatasetVersion.getSiemacMetadataStatisticalResource().getCode(), multidatasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic()));

        // Checks
        identifiableStatisticalResourceRepository.checkDuplicatedUrn(multidatasetVersion.getSiemacMetadataStatisticalResource());

        // Add version to multidataset
        multidataset.addVersion(multidatasetVersion);
        return getMultidatasetRepository().save(multidatasetVersion.getMultidataset());
    }

    @Override
    public MultidatasetCube createMultidatasetCube(ServiceContext ctx, String multidatasetVersionUrn, MultidatasetCube cube) throws MetamacException {
        // Validations
        multidatasetServiceInvocationValidator.checkCreateMultidatasetCube(ctx, multidatasetVersionUrn, cube);
        MultidatasetVersion multidatasetVersion = retrieveMultidatasetVersionByUrn(ctx, multidatasetVersionUrn);
        ProcStatusValidator.checkStatisticalResourceStructureCanBeEdited(multidatasetVersion);

        checkMultidatasetCubeIdentifierDuplicated(multidatasetVersion, cube);

        // Fill metadata for create cube
        fillMetadataForCreateMultidatasetCube(ctx, cube, multidatasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation());

        // Create cube
        cube = addToMultidatasetVersion(ctx, multidatasetVersion, cube);

        updateLastUpdateMetadata(multidatasetVersion);

        return cube;
    }

    private void checkMultidatasetCubeIdentifierDuplicated(MultidatasetVersion multidatasetVersion, MultidatasetCube cube) throws MetamacException {
        if (existsMultidataseCubeWithSameIdentifier(cube, multidatasetVersion)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MULTIDATASET_CUBE_DUPLICATE_IDENTIFIER)
                    .withMessageParameters(cube.getIdentifier(), multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn()).build();
        }
    }

    private boolean existsMultidataseCubeWithSameIdentifier(MultidatasetCube cube, MultidatasetVersion multidatasetVersion) {
        return CollectionUtils.exists(multidatasetVersion.getCubes(), new Predicate() {

            @Override
            public boolean evaluate(Object object) {
                MultidatasetCube multidatasetCube = (MultidatasetCube) object;

                return ObjectUtils.notEqual(cube.getId(), multidatasetCube.getId()) && StringUtils.equalsIgnoreCase(cube.getIdentifier(), multidatasetCube.getIdentifier());
            }
        });
    }

    private MultidatasetCube addToMultidatasetVersion(ServiceContext ctx, MultidatasetVersion multidatasetVersion, MultidatasetCube cube) throws MetamacException {
        cube.setMultidatasetVersion(multidatasetVersion);
        cube = getMultidatasetCubeRepository().save(cube);

        // Update multidatasetVersion adding element
        multidatasetVersion.addCube(cube);
        multidatasetVersion = getMultidatasetVersionRepository().save(multidatasetVersion);

        // Check order and update order of other elements
        updateMultidatasetVersionCubesOrdersInLevelAddingCube(ctx, multidatasetVersion.getCubes(), cube);
        return cube;
    }

    private void updateMultidatasetVersionCubesOrdersInLevelAddingCube(ServiceContext ctx, List<MultidatasetCube> cubes, MultidatasetCube cubeToAdd) throws MetamacException {
        // Create a set with all possibles orders. At the end of this method, this set must be empty
        Set<Long> orders = new HashSet<Long>();
        for (int i = 1; i <= cubes.size(); i++) {
            orders.add(Long.valueOf(i));
        }

        cubes.sort(new MultidatasetCubeComparator());

        // Update orders
        for (MultidatasetCube existingCube : cubes) {
            // it is possible that element is already added to parent and order is already set
            if (existingCube.getId().equals(cubeToAdd.getId())) {
                // nothing
            } else {
                // Update order - Make space on the existing list to include the new cube
                if (existingCube.getOrderInMultidataset() >= cubeToAdd.getOrderInMultidataset()) {
                    existingCube.setOrderInMultidataset(existingCube.getOrderInMultidataset() + 1);
                    getMultidatasetCubeRepository().save(existingCube);
                }
            }

            boolean removed = orders.remove(existingCube.getOrderInMultidataset());
            if (!removed) {
                break; // order incorrect
            }
        }

        // Checks orders
        if (!orders.isEmpty()) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.MULTIDATASET_CUBE__ORDER_IN_MULTIDATASET);
        }

    }

    private MultidatasetCube fillMetadataForCreateMultidatasetCube(ServiceContext ctx, MultidatasetCube multidatasetcube, ExternalItem statisticalOperation) {
        FillMetadataForCreateResourceUtils.fillMetadataForCreateNameableResource(multidatasetcube.getNameableStatisticalResource(), statisticalOperation);
        multidatasetcube.fillCodeAndUrn();
        return multidatasetcube;
    }

    @Override
    public MultidatasetCube updateMultidatasetCube(ServiceContext ctx, MultidatasetCube cube) throws MetamacException {
        // Validations
        multidatasetServiceInvocationValidator.checkUpdateMultidatasetCube(ctx, cube);
        MultidatasetVersion multidatasetVersion = retrieveMultidatasetVersionByUrn(ctx, cube.getMultidatasetVersion().getSiemacMetadataStatisticalResource().getUrn());
        ProcStatusValidator.checkStatisticalResourceStructureCanBeEdited(multidatasetVersion);

        checkMultidatasetCubeIdentifierDuplicated(multidatasetVersion, cube);

        updateLastUpdateMetadata(multidatasetVersion);

        // Save
        return getMultidatasetCubeRepository().save(cube);
    }

    @Override
    public MultidatasetCube retrieveMultidatasetCube(ServiceContext ctx, String cubeUrn) throws MetamacException {
        // Validations
        multidatasetServiceInvocationValidator.checkRetrieveMultidatasetCube(ctx, cubeUrn);

        // Retrieve
        MultidatasetCube cube = getMultidatasetCubeRepository().retrieveCubeByUrn(cubeUrn);
        return cube;
    }

    @Override
    public void deleteMultidatasetCube(ServiceContext ctx, String cubeUrn) throws MetamacException {
        // Validations
        multidatasetServiceInvocationValidator.checkDeleteMultidatasetCube(ctx, cubeUrn);

        // Retrieve
        MultidatasetCube multidatasetCube = retrieveMultidatasetCube(ctx, cubeUrn);

        // Check indicators system proc status
        MultidatasetVersion multidatasetVersion = retrieveMultidatasetVersionByUrn(ctx, multidatasetCube.getMultidatasetVersion().getSiemacMetadataStatisticalResource().getUrn());
        ProcStatusValidator.checkStatisticalResourceStructureCanBeEdited(multidatasetVersion);

        // Delete
        getMultidatasetCubeRepository().delete(multidatasetCube);

        // Retrieve
        multidatasetVersion = retrieveMultidatasetVersionByUrn(ctx, multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        updateMultidatasetVersionCubesOrdersInLevelRemovingCube(ctx, multidatasetVersion.getCubes(), multidatasetCube, multidatasetCube.getOrderInMultidataset());

        updateLastUpdateMetadata(multidatasetVersion);
    }

    private void updateMultidatasetVersionCubesOrdersInLevelRemovingCube(ServiceContext ctx, List<MultidatasetCube> cubes, MultidatasetCube multidatasetCubeToRemove, Long orderInLevelOfDeletedElement)
            throws MetamacException {
        for (MultidatasetCube existingCube : cubes) {
            if (existingCube.getId().equals(multidatasetCubeToRemove.getId())) {
                // nothing
            } else if (existingCube.getOrderInMultidataset() > orderInLevelOfDeletedElement) {
                existingCube.setOrderInMultidataset(existingCube.getOrderInMultidataset() - 1);
                updateMultidatasetCube(ctx, existingCube);
            }
        }
    }

    private void updateLastUpdateMetadata(MultidatasetVersion multidatasetVersion) {
        multidatasetVersion.getSiemacMetadataStatisticalResource().setLastUpdate(new DateTime());
        getMultidatasetVersionRepository().save(multidatasetVersion);
    }

    @Override
    public MultidatasetCube updateMultidatasetCubeLocation(ServiceContext ctx, String multidatasetCubeUrn, Long orderInMultidataset) throws MetamacException {
        // Validations
        multidatasetServiceInvocationValidator.checkUpdateMultidatasetCubeLocation(ctx, multidatasetCubeUrn, orderInMultidataset);

        // Update location
        MultidatasetCube multidatasetCube = retrieveMultidatasetCube(ctx, multidatasetCubeUrn);

        // Check indicators system proc status
        MultidatasetVersion multidatasetVersion = retrieveMultidatasetVersionByUrn(ctx, multidatasetCube.getMultidatasetVersion().getSiemacMetadataStatisticalResource().getUrn());
        ProcStatusValidator.checkStatisticalResourceStructureCanBeEdited(multidatasetVersion);

        // Change order
        Long orderInLevelBefore = multidatasetCube.getOrderInMultidataset();
        multidatasetCube.setOrderInMultidataset(orderInMultidataset);

        // Same parent, only changes order. Check order is correct and update orders
        List<MultidatasetCube> cubes = multidatasetCube.getMultidatasetVersion().getCubes();
        updateMultidatasetVersionCubesOrdersInLevelChangingOrder(ctx, cubes, multidatasetCube, orderInLevelBefore, multidatasetCube.getOrderInMultidataset());
        multidatasetCube = getMultidatasetCubeRepository().save(multidatasetCube);

        return multidatasetCube;
    }

    private void updateMultidatasetVersionCubesOrdersInLevelChangingOrder(ServiceContext ctx, List<MultidatasetCube> cubes, MultidatasetCube multidatasetCube, Long orderBeforeUpdate,
            Long orderAfterUpdate) throws MetamacException {

        // Checks orders
        if (orderAfterUpdate > cubes.size()) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.MULTIDATASET_CUBE__ORDER_IN_MULTIDATASET);
        }

        // Update orders
        for (MultidatasetCube existingCube : cubes) {
            if (existingCube.getId().equals(multidatasetCube.getId())) {
                continue;
            }
            if (orderAfterUpdate < orderBeforeUpdate) {
                if (existingCube.getOrderInMultidataset() >= orderAfterUpdate && existingCube.getOrderInMultidataset() < orderBeforeUpdate) {
                    existingCube.setOrderInMultidataset(existingCube.getOrderInMultidataset() + 1);
                    getMultidatasetCubeRepository().save(existingCube);
                }
            } else if (orderAfterUpdate > orderBeforeUpdate) {
                if (existingCube.getOrderInMultidataset() > orderBeforeUpdate && existingCube.getOrderInMultidataset() <= orderAfterUpdate) {
                    existingCube.setOrderInMultidataset(existingCube.getOrderInMultidataset() - 1);
                    getMultidatasetCubeRepository().save(existingCube);
                }
            }
        }
    }

    @Override
    public PagedResult<Multidataset> findMultidatasetsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validations
        multidatasetServiceInvocationValidator.checkFindMultidatasetsByCondition(ctx, conditions, pagingParameter);

        conditions = CriteriaUtils.initConditions(conditions, MultidatasetVersion.class);
        pagingParameter = CriteriaUtils.initPagingParameter(pagingParameter);

        return getMultidatasetRepository().findByCondition(conditions, pagingParameter);
    }
}
