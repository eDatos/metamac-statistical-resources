package org.siemac.metamac.statistical.resources.core.geocache.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.MetamacCollectionUtils;
import org.siemac.metamac.core.common.util.predicates.MetamacPredicate;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableElement;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.mapper.CommonDto2DoMapper;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResourceProperties;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourceProperties;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourcesByRelatedResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourcesByRelatedResourceProperties;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheTerritoriesByGeoCacheResource;
import org.siemac.metamac.statistical.resources.core.geocache.serviceapi.validators.CacheServiceInvocationValidator;
import org.siemac.metamac.statistical.resources.core.invocation.service.NoticesRestInternalService;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.invocation.utils.JaxiMapper;
import org.siemac.metamac.statistical.resources.core.invocation.utils.RestMapper;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeAction;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoResources;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.siemac.metamac.statistical.resources.core.task.utils.JobUtil;
import org.siemac.metamac.statistical.resources.core.utils.InternationalStringUtils;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesExternalItemUtils;
import org.siemac.metamac.statistical.resources.core.utils.shared.MetamacPortalWebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import es.ibestat.jaxi.stream.messages.DatasetAvro;
import es.ibestat.jaxi.stream.messages.ExternalItemAvro;
import es.ibestat.jaxi.stream.messages.ProcStatusEnumAvro;
import es.ibestat.jaxi.stream.messages.PublicationAvro;

/**
 * Implementation of CacheService.
 */
@Service("cacheService")
public class CacheServiceImpl extends CacheServiceImplBase {

    private static Logger                     logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    @Autowired
    @Qualifier("commonDto2DoMapper")
    private CommonDto2DoMapper                dto2DoMapper;

    @Autowired
    private RestMapper                        restMapper;

    @Autowired
    private NoticesRestInternalService        noticesRestInternalService;

    @Autowired
    private SrmRestInternalService            srmRestInternalService;

    @Autowired
    private CacheServiceInvocationValidator   cacheServiceInvocationValidator;

    @Autowired
    private TaskService                       taskService;

    public CacheServiceImpl() {
    }

    public String retrieveQueryVersionByUrn(ServiceContext ctx, String urn) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("retrieveQueryVersionByUrn not implemented");

    }

    public String findQueryVersionByUrn(ServiceContext ctx, String urn) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("findQueryVersionByUrn not implemented");

    }

    // CACHE RESOURCES

    /*
     * lifeCycleStatisticalResource: Metadata associated to the resource.
     * urnResource: resource base urn. For example for a datasetVersion it will be the dataset urn and not de datasetVersion urn. The same for Queries. It is the query urn
     * geographicCoverage: Set of geographical code associated to the resource.
     * isLastVersionPublished
     */
    @Override
    public void processUpdateGeoCacheResource(ServiceContext ctx, LifeCycleStatisticalResource lifeCycleStatisticalResource, String urnResource,
            StatisticalResourceTypeEnum statisticalResourceTypeEnum, List<ExternalItem> geographicCoverage, boolean isLastVersionPublished) throws MetamacException {

        String resourceVersionUrn = lifeCycleStatisticalResource.getUrn();

        // discard all variable elements present in the array to avoid duplicated or outdated data
        List<GeoCacheResource> geoCacheResourcesDisabled = disabledResourceByUrn(ctx, resourceVersionUrn);

        List<GeoCacheResource> geoCacheResourcesOldVersions = new ArrayList<>();
        if (isLastVersionPublished) {
            // disable old last version published version.
            geoCacheResourcesOldVersions = updateAllGeoCacheResourcesByUrn(ctx, urnResource, resourceVersionUrn);
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Processing geographic coverage to create the cache for Urn: %s ", resourceVersionUrn));
        }

        String geographicCoverageCodelistUrn = StatisticalResourcesExternalItemUtils.getCodelistFromCodeUrn(geographicCoverage.get(0).getUrn());
        List<CodeResourceInternal> codes = srmRestInternalService.retrieveCodesOfCodelistEfficiently(geographicCoverageCodelistUrn).getCodes();

        GeoCacheResource geoCacheResource = updateGeoCacheResource(ctx, lifeCycleStatisticalResource, statisticalResourceTypeEnum, isLastVersionPublished);

        for (ExternalItem geoCoverage : geographicCoverage) {
            CodeResourceInternal code = MetamacCollectionUtils.find(codes, new MetamacPredicate<CodeResourceInternal>() {

                @Override
                protected boolean eval(CodeResourceInternal code) {
                    return StringUtils.equals(code.getUrn(), geoCoverage.getUrn());
                }
            });

            if (code == null || code.getVariableElement() == null) {
                logger.error("Could not find variable element for {}", geoCoverage.getUrn());
                throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_COVERAGE_CODE_NOT_FOUND, geoCoverage.getUrn());
            }

            ExternalItem territoryVariableElement = restMapper.buildExternalItemFromResourceInternal(code.getVariableElement());

            GeoCacheTerritoriesByGeoCacheResource territories = new GeoCacheTerritoriesByGeoCacheResource();
            territories.setVariableElement(territoryVariableElement);
            geoCacheResource.addTerritory(territories);

        }

        // only when geoCacheResourcesDisabled is not empty must update related resource because geoCacheResource is the latest version published
        updateRelatedResourceByCacheResource(ctx, geoCacheResource, geoCacheResourcesDisabled, geoCacheResourcesOldVersions);

    }

    private GeoCacheResource updateGeoCacheResource(ServiceContext ctx, LifeCycleStatisticalResource lifeCycleResource, StatisticalResourceTypeEnum type, boolean isLastVersionPublished)
            throws MetamacException {
        GeoCacheResource geoCacheResource = new GeoCacheResource();
        InternationalString titleResource = InternationalStringUtils.copy(lifeCycleResource.getTitle());

        geoCacheResource.setCode(lifeCycleResource.getCode());
        geoCacheResource.setUrn(lifeCycleResource.getUrn());
        geoCacheResource.setTitle(titleResource);
        geoCacheResource.setOperationCode(lifeCycleResource.getStatisticalOperation().getCode());
        geoCacheResource.setOperationUrn(lifeCycleResource.getStatisticalOperation().getUrn());
        geoCacheResource.setIsExternalSource(Boolean.FALSE);
        geoCacheResource.setType(type.getName());
        String maintainer = lifeCycleResource.getMaintainer() != null ? lifeCycleResource.getMaintainer().getCode() : null;
        geoCacheResource.setHtmlLink(MetamacPortalWebUtils.buildResourceUrlByType(type, maintainer, lifeCycleResource.getCode(), lifeCycleResource.getVersionLogic(),
                configurationService.retrievePortalExternalWebApplicationUrlVisualizer()));
        geoCacheResource.setIsLastVersion(isLastVersionPublished);
        geoCacheResource.setIsActivated(true);

        return this.getGeoCacheResourceRepository().save(geoCacheResource);
    }

    @Override
    public List<GeoCacheResource> updateAllGeoCacheResourcesByUrn(ServiceContext ctx, String resourceUrn, String resourceVersionUrn) {
        List<GeoCacheResource> geoCacheResources = findResourcesLastVersionByUrn(resourceUrn);
        List<GeoCacheResource> geoCacheRelatedResourcesDisabled = new ArrayList<>();

        for (GeoCacheResource geoCacheResource : geoCacheResources) {
            if (!geoCacheResource.getUrn().equals(resourceVersionUrn)) {
                geoCacheResource.setIsLastVersion(Boolean.FALSE);
                geoCacheRelatedResourcesDisabled.add(geoCacheResource);
                this.getGeoCacheResourceRepository().save(geoCacheResource);
            }
        }
        return geoCacheRelatedResourcesDisabled;
    }

    @Override
    public List<GeoCacheResource> disabledResourceByUrn(ServiceContext ctx, String resourceUrn) {
        List<GeoCacheResource> geoCacheResources = retrieveGeoCacheResourceByUrnAndVersion(ctx, resourceUrn, false);

        if (geoCacheResources != null) {
            for (GeoCacheResource geoCacheResource : geoCacheResources) {
                geoCacheResource.setIsActivated(false);
                this.getGeoCacheResourceRepository().save(geoCacheResource);
            }
        }
        return geoCacheResources;
    }

    @Override
    public void deleteDisabledCacheEntries(ServiceContext ctx) {
        this.getGeoCacheResourceRepository().deleteAll();
    }

    private List<GeoCacheResource> findResourcesLastVersionByUrn(String resourceUrn) {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheResource.class).withProperty(GeoCacheResourceProperties.urn()).like(resourceUrn + '%').and()
                .withProperty(GeoCacheResourceProperties.isLastVersion()).eq(Boolean.TRUE).withProperty(GeoCacheResourceProperties.isActivated()).eq(Boolean.TRUE).distinctRoot().build();
        // @formatter:off

        List<GeoCacheResource> geoCacheResources = this.getGeoCacheResourceRepository().findByCondition(conditions);     
        return geoCacheResources;
    }
    
    private GeoCacheResource retrieveResourcesLastVersionByUrnAndVersion(String resourceUrn) {
       
        List<GeoCacheResource> geoCacheResources = findResourcesLastVersionByUrn(resourceUrn);
        
        if ( geoCacheResources != null && !geoCacheResources.isEmpty() && geoCacheResources.size() == 1) {
            return geoCacheResources.get(0);
        }
        
        return null;
    }
    
    @Override
    public GeoCacheResource retrieveGeoCacheResourceByUrn(ServiceContext ctx, String resourceUrn) {
       List<GeoCacheResource> geoCacheResources =  retrieveGeoCacheResourceByUrnAndVersion(ctx, resourceUrn, false);
       if ( geoCacheResources != null && !geoCacheResources.isEmpty() && geoCacheResources.size() == 1) {
           return geoCacheResources.get(0);
       }
       return null;

    }
    
    private List<GeoCacheResource> retrieveGeoCacheResourceByUrnAndVersion(ServiceContext ctx, String resourceUrn, boolean latestVersion) {
        
        
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheResource.class).withProperty(GeoCacheResourceProperties.urn()).eq(resourceUrn).and()
                .withProperty(GeoCacheResourceProperties.isActivated()).eq(Boolean.TRUE).distinctRoot().build();
        
        if (latestVersion) {
            conditions.add(ConditionalCriteriaBuilder.criteriaFor(GeoCacheResource.class).withProperty(GeoCacheResourceProperties.isLastVersion()).eq(Boolean.TRUE).buildSingle());
        }

        return this.getGeoCacheResourceRepository().findByCondition(conditions);     
    }
       
    //RELATED CACHE RESOURCES
    
    @Override
    public GeoCacheByRelatedResource updateGeoCacheByRelatedResource(ServiceContext ctx, SiemacMetadataStatisticalResource metadataResource, LifeCycleStatisticalResource lifeCycleResource,
            StatisticalResourceTypeEnum type, boolean isLastVersionPublished) throws MetamacException {
        GeoCacheByRelatedResource geoCacheByRelatedResource = new GeoCacheByRelatedResource();
        InternationalString titleResource = InternationalStringUtils.copy(metadataResource.getTitle());

        geoCacheByRelatedResource.setCode(metadataResource.getCode());
        geoCacheByRelatedResource.setUrn(metadataResource.getUrn());
        geoCacheByRelatedResource.setTitle(titleResource);
        geoCacheByRelatedResource.setOperationCode(metadataResource.getStatisticalOperation().getCode());
        geoCacheByRelatedResource.setOperationUrn(metadataResource.getStatisticalOperation().getUrn());
        geoCacheByRelatedResource.setIsExternalSource(Boolean.FALSE);
        geoCacheByRelatedResource.setType(type.getName());
        String maintainer = lifeCycleResource.getMaintainer() != null ? lifeCycleResource.getMaintainer().getCode() : null;
        geoCacheByRelatedResource.setHtmlLink(MetamacPortalWebUtils.buildResourceUrlByType(type, maintainer, lifeCycleResource.getCode(), lifeCycleResource.getVersionLogic(),
                configurationService.retrievePortalExternalWebApplicationUrlVisualizer()));
        geoCacheByRelatedResource.setIsLastVersion(isLastVersionPublished);
        geoCacheByRelatedResource.setIsActivated(true);

        return this.getGeoCacheByRelatedResourceRepository().save(geoCacheByRelatedResource);
    }
    
    private List<GeoCacheByRelatedResource> findRelatedResourcesByUrn(String resourceUrn) {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheByRelatedResource.class).withProperty(GeoCacheByRelatedResourceProperties.urn()).like(resourceUrn + "%").and()
                .withProperty(GeoCacheByRelatedResourceProperties.isLastVersion()).eq(Boolean.TRUE).distinctRoot().build();

        return this.getGeoCacheByRelatedResourceRepository().findByCondition(conditions);     
    }
    

    private List<GeoCacheResourcesByRelatedResource> findRelatedResourceByCacheResource(Long geoCacheResourceId) {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheResourcesByRelatedResource.class).withProperty(GeoCacheResourcesByRelatedResourceProperties.geoCacheResource().id()).eq(geoCacheResourceId).distinctRoot().build();

        return this.getGeoCacheResourcesByRelatedResourceRepository().findByCondition(conditions);     
    }
    
    @Override
    public void deleteRelatedResourceOldVersions(ServiceContext ctx, String resourceUrn) {
        
        List<GeoCacheByRelatedResource> geoCacheRelatedResources = findRelatedResourcesByUrn(resourceUrn);
        for (GeoCacheByRelatedResource geoCacheRelatedResource : geoCacheRelatedResources) {
                this.getGeoCacheByRelatedResourceRepository().delete(geoCacheRelatedResource); 
        }                
    }

    /*
     * update related resource associated with newGeoCacheResource urn to this version of the resource because is the last published.
     * newGeoCacheResource new cache resource that is being inserted.
     * geoCacheResourcesDisabled cache entries that have been disabled because users have been updated this resource manually from the app.
     * geoCacheResourcesOldVersions Cache entry that was the last version until now but has been replaced for the entry represented by the parameter newGeoCacheResource as the new last version resource.  
     */
    @Override
    public void updateRelatedResourceByCacheResource(ServiceContext ctx, GeoCacheResource newGeoCacheResource, List<GeoCacheResource> geoCacheResourcesDisabled, List<GeoCacheResource> geoCacheResourcesOldVersions) {

        for (GeoCacheResource geoCacheResource : geoCacheResourcesDisabled) {
            updateRelatedResourceByCacheResource(geoCacheResource, newGeoCacheResource);
        }
        
        for (GeoCacheResource geoCacheResource : geoCacheResourcesOldVersions) {
            updateRelatedResourceByCacheResource(geoCacheResource, newGeoCacheResource);
        }
        
    }

    private void updateRelatedResourceByCacheResource(GeoCacheResource geoCacheResourceToChange, GeoCacheResource newGeoCacheResource) {
        List<GeoCacheResourcesByRelatedResource> geoCacheResourcesByRelatedResourceToUpdate = findRelatedResourceByCacheResource(geoCacheResourceToChange.getId());           
        for (GeoCacheResourcesByRelatedResource geoCacheResourcesByRelatedResource: geoCacheResourcesByRelatedResourceToUpdate) {
            geoCacheResourcesByRelatedResource.setGeoCacheResource(newGeoCacheResource);
            this.getGeoCacheResourcesByRelatedResourceRepository().save(geoCacheResourcesByRelatedResource);
        }
    }
    
    @Override
    public boolean createRelatedResourceByCacheResourceByUrn(ServiceContext ctx, GeoCacheByRelatedResource geoCacheByRelatedResource, String resourceUrn) throws MetamacException {
        GeoCacheResourcesByRelatedResource geoCacheResourcesByRelatedResource = new GeoCacheResourcesByRelatedResource();
        GeoCacheResource geoCacheResource = retrieveResourcesLastVersionByUrnAndVersion(resourceUrn);
        if (geoCacheResource == null) {
            return false;
        }
        geoCacheResourcesByRelatedResource.setGeoCacheResource(geoCacheResource);
        geoCacheResourcesByRelatedResource.setGeoCacheResourcesByRelated(geoCacheByRelatedResource);
        geoCacheByRelatedResource.addRelatedResource(geoCacheResourcesByRelatedResource);
        return true;
        
    }
    
    @Override
    public PagedResult<GeoCacheResource> findResourcesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Validations
        cacheServiceInvocationValidator.checkFindResourcesByCondition(ctx, conditions, pagingParameter);

        // Find
        conditions = CriteriaUtils.initConditions(conditions, DatasetVersion.class);
        pagingParameter = CriteriaUtils.initPagingParameter(pagingParameter);

        return this.getGeoCacheResourceRepository().findByCondition(conditions, pagingParameter);

    }

    @Override
    public PagedResult<GeoCacheByRelatedResource> findGeoRelatedResourcesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        conditions = CriteriaUtils.initConditions(conditions, DatasetVersion.class);
        pagingParameter = CriteriaUtils.initPagingParameter(pagingParameter);

        return this.getGeoCacheByRelatedResourceRepository().findByCondition(conditions, pagingParameter);
    }  
  
    @Override
    public  void processGeoCacheRelatedCollection(ServiceContext ctx, PublicationVersion publicationVersion, boolean isLastVersionPublished, String urn) throws MetamacException {

        cacheServiceInvocationValidator.checkProcessGeoCacheRelatedCollection(ctx, publicationVersion, isLastVersionPublished, urn);

            deleteRelatedResourceOldVersions(ctx, urn);

            GeoCacheByRelatedResource geoCacheRelatedResource = updateGeoCacheByRelatedResource(ctx, publicationVersion.getSiemacMetadataStatisticalResource(),
                    publicationVersion.getLifeCycleStatisticalResource(), StatisticalResourceTypeEnum.COLLECTION, isLastVersionPublished);


            List<MetamacExceptionItem> exceptionItems = new ArrayList<>();
            
            for (RelatedResource relatedResource : publicationVersion.getHasPart()) {
                createRelatedResourceByCacheResourceByTypeAndUrn(ctx, geoCacheRelatedResource, urn, relatedResource, exceptionItems);
            }

            sendMessageException(ServiceNoticeAction.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_COLLECTION_PUBLICATION_DATASET, exceptionItems);
    }
    
    private void createRelatedResourceByCacheResourceByTypeAndUrn(ServiceContext ctx, GeoCacheByRelatedResource geoCacheRelatedResource, String urn, RelatedResource relatedResource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        
        String relatedResourceUrn = null;
        
        if (TypeRelatedResourceEnum.DATASET.equals(relatedResource.getType())) {
            relatedResourceUrn =  relatedResource.getDataset().getIdentifiableStatisticalResource().getUrn();
        } else     if (TypeRelatedResourceEnum.QUERY.equals(relatedResource.getType())) {
            relatedResourceUrn =  relatedResource.getQuery().getIdentifiableStatisticalResource().getUrn();
        } else {
            return;
        }
        
        boolean inserted = createRelatedResourceByCacheResourceByUrn(ctx, geoCacheRelatedResource, relatedResourceUrn);
        if (!inserted) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_COLLECTION_PUBLICATION_RESOURCE_NOT_FOUND_ERROR, relatedResourceUrn, urn));
        }
    }
        
    private void sendMessageException(String noticeAction, List<MetamacExceptionItem> exceptionItems) {
        if (!exceptionItems.isEmpty()) {
        MetamacException metamacException = new MetamacException();
        metamacException.getExceptionItems().addAll(exceptionItems);
        noticesRestInternalService.createErrorBackgroundNotification(noticeAction, metamacException);
        }
    }
    
    // JAXI PUBLICATION
    
    // JAXI - DATASET
    
    @Override
    public void updateDatasetExternalPublicationCache(ServiceContext ctx, DatasetAvro jaxiDatasetVersionAvro) throws MetamacException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<>();
        List<GeoCacheResource> geoCacheResourcesDisabled = disabledResourceByUrn(ctx, jaxiDatasetVersionAvro.getUrn());

        if (ProcStatusEnumAvro.PUBLISHED.equals(jaxiDatasetVersionAvro.getProcStatus())) {
            InternationalString datasetTitle = JaxiMapper.getInternationalStringFromInternationalStringAvro(jaxiDatasetVersionAvro.getTitle());
            GeoCacheResource geoCacheResource = updateGeoCacheExternalResource(ctx, jaxiDatasetVersionAvro, datasetTitle);

            buildExternalItemFromJaxiExternalPublication(jaxiDatasetVersionAvro, geoCacheResource, exceptionItems);
            if (exceptionItems.isEmpty()) {
                updateRelatedResourceByCacheResource(ctx, geoCacheResource, geoCacheResourcesDisabled, new ArrayList<>());
            } else {
                MetamacException metamacException = new MetamacException();
                metamacException.getExceptionItems().addAll(exceptionItems);
                throw metamacException;
            }

        }
    }
    
    private void buildExternalItemFromJaxiExternalPublication(DatasetAvro jaxiDatasetVersionAvro, GeoCacheResource geoCacheResource, 
            List<MetamacExceptionItem> exceptionItems) throws MetamacException {

        for (ExternalItemAvro externalAvro : jaxiDatasetVersionAvro.getGeographicCoverage()) {
            TypeExternalArtefactsEnum externalItemType = TypeExternalArtefactsEnum.valueOf(externalAvro.getType().name());

            if (TypeExternalArtefactsEnum.VARIABLE_ELEMENT.equals(externalItemType)) {
                ExternalItem externalItem = new ExternalItem();
                externalItem.setType(externalItemType);

                externalItem.setCode(externalAvro.getCode());
                externalItem.setCodeNested(externalAvro.getCodeNested());

                try {

                    if (externalAvro.getUrn() == null) {
                        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_VARIABLE_ELEMENT_ERROR, externalAvro.getUrn(),
                                jaxiDatasetVersionAvro.getUrn()));
                        continue;
                    }

                    VariableElement variableElement = srmRestInternalService.retrieveVariableElement(externalAvro.getUrn());
                    externalItem.setUri(dto2DoMapper.externalItemApiUrlDtoToDo(externalItemType, variableElement.getSelfLink().getHref()));
                    externalItem.setManagementAppUrl(dto2DoMapper.externalItemWebAppUrlDtoToDo(externalItemType, variableElement.getManagementAppLink()));
                } catch (Exception e) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_VARIABLE_ELEMENT_ERROR, externalAvro.getUrn(),
                            jaxiDatasetVersionAvro.getUrn()));
                }

                externalItem.setUrn(externalAvro.getUrn());
                externalItem.setTitle(JaxiMapper.getInternationalStringFromInternationalStringAvro(externalAvro.getTitle()));
                GeoCacheTerritoriesByGeoCacheResource territory = new GeoCacheTerritoriesByGeoCacheResource();
                territory.setVariableElement(externalItem);
                geoCacheResource.addTerritory(territory);
            }
        }
    }
    
    // JAXI - COLLECTIONS
    
    @Override
    public void updateCollectionExternalPublicationCache(ServiceContext ctx, PublicationAvro jaxiPublicationVersionAvro) throws MetamacException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<>();

        deleteRelatedResourceOldVersions(ctx, jaxiPublicationVersionAvro.getUrn());

        if (ProcStatusEnumAvro.PUBLISHED.equals(jaxiPublicationVersionAvro.getProcStatus())) {

            GeoCacheByRelatedResource geoCacheRelatedResource = updateGeoCacheByRelatedResource(ctx, jaxiPublicationVersionAvro);

            buildExternalItemFromJaxiExternalCollectionPublication(ctx, jaxiPublicationVersionAvro, geoCacheRelatedResource, exceptionItems);

           sendMessageException(ServiceNoticeAction.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_COLLECTION_PUBLICATION_DATASET, exceptionItems);

        }
    }
    
    private void buildExternalItemFromJaxiExternalCollectionPublication(ServiceContext ctx, PublicationAvro jaxiCollectionVersionAvro, GeoCacheByRelatedResource geoCacheRelatedResource,
            List<MetamacExceptionItem> exceptionItems) throws MetamacException {

        if (jaxiCollectionVersionAvro.getResources() == null || jaxiCollectionVersionAvro.getResources().isEmpty()) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_COLLECTION_PUBLICATION_NO_RESOURCES_ERROR, jaxiCollectionVersionAvro.getUrn()));
            return;
        }

        for (ExternalItemAvro externalAvro : jaxiCollectionVersionAvro.getResources()) {
            TypeExternalArtefactsEnum externalItemType = TypeExternalArtefactsEnum.valueOf(externalAvro.getType().name());

            if (externalAvro.getUrn() == null) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_COLLECTION_PUBLICATION_DATASET_NULL_ERROR, jaxiCollectionVersionAvro.getUrn()));
                continue;
            }

            if (TypeExternalArtefactsEnum.DATASET.equals(externalItemType)) {
                try {

                    boolean created = createRelatedResourceByCacheResourceByUrn(ctx, geoCacheRelatedResource, externalAvro.getUrn());

                    if (!created) {
                        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_COLLECTION_PUBLICATION_RESOURCE_NOT_FOUND_ERROR, externalAvro.getUrn(),
                                jaxiCollectionVersionAvro.getUrn()));
                    }

                } catch (Exception e) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_COLLECTION_PUBLICATION_DATASET_ERROR, externalAvro.getUrn(),
                            jaxiCollectionVersionAvro.getUrn()));
                }
            }
        }
    }
    

    @Override
    public GeoCacheResource updateGeoCacheExternalResource(ServiceContext ctx, DatasetAvro jaxiDatasetVersionAvro, InternationalString datasetTitle) {
        GeoCacheResource geoCacheResource = new GeoCacheResource();

        geoCacheResource.setCode(jaxiDatasetVersionAvro.getCode());
        geoCacheResource.setUrn(jaxiDatasetVersionAvro.getUrn());
        geoCacheResource.setTitle(datasetTitle);
        geoCacheResource.setOperationCode(jaxiDatasetVersionAvro.getStatisticalOperation().getCode());
        geoCacheResource.setOperationUrn(jaxiDatasetVersionAvro.getStatisticalOperation().getUrn());
        geoCacheResource.setIsExternalSource(Boolean.TRUE);
        geoCacheResource.setType(StatisticalResourceTypeEnum.DATASET.getName());
        geoCacheResource.setHtmlLink(jaxiDatasetVersionAvro.getHtmlLink());
        geoCacheResource.setIsLastVersion(true);
        geoCacheResource.setIsActivated(true);
        return getGeoCacheResourceRepository().save(geoCacheResource);
    }

    @Override
    public GeoCacheByRelatedResource updateGeoCacheByRelatedResource(ServiceContext ctx, es.ibestat.jaxi.stream.messages.PublicationAvro jaxiCollectionVersionAvro) throws MetamacException {
        GeoCacheByRelatedResource geoCacheByRelatedResource = new GeoCacheByRelatedResource();
        InternationalString titleResource = JaxiMapper.getInternationalStringFromInternationalStringAvro(jaxiCollectionVersionAvro.getTitle());

        geoCacheByRelatedResource.setCode(jaxiCollectionVersionAvro.getCode());
        geoCacheByRelatedResource.setUrn(jaxiCollectionVersionAvro.getUrn());
        geoCacheByRelatedResource.setTitle(titleResource);
        geoCacheByRelatedResource.setOperationCode(jaxiCollectionVersionAvro.getStatisticalOperation().getCode());
        geoCacheByRelatedResource.setOperationUrn(jaxiCollectionVersionAvro.getStatisticalOperation().getUrn());
        geoCacheByRelatedResource.setIsExternalSource(Boolean.TRUE);
        geoCacheByRelatedResource.setType(StatisticalResourceTypeEnum.COLLECTION.getName());
        geoCacheByRelatedResource.setHtmlLink(jaxiCollectionVersionAvro.getHtmlLink());
        geoCacheByRelatedResource.setIsLastVersion(true);
        geoCacheByRelatedResource.setIsActivated(true);

        return this.getGeoCacheByRelatedResourceRepository().save(geoCacheByRelatedResource);
    }

    @Override
    public GeoCacheResource updateGeoCacheResource(ServiceContext ctx, SiemacMetadataStatisticalResource metadataResource, LifeCycleStatisticalResource lifeCycleResource,
            StatisticalResourceTypeEnum type, boolean isLastVersionPublished) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void updateAllGeographicExternalCoverageVariableElementsCache(ServiceContext ctx, List<StatisticalResourceTypeEnum> externalResourcesToUpdate) throws MetamacException {
       
        logger.info("Execution start - updateAllGeographicExternalCoverageVariableElementsCache - jaxi dataset : {} ", new DateTime());

        for (StatisticalResourceTypeEnum resourceType : externalResourcesToUpdate) {
            updateAllExternalGeocoverageCache(ctx, resourceType);
            }

        logger.info("Execution end - updateAllGeographicExternalCoverageVariableElementsCache - jaxi dataset : {} ", new DateTime());

    }
    
    private void updateAllExternalGeocoverageCache(ServiceContext ctx, StatisticalResourceTypeEnum externalResourcesToUpdate) throws MetamacException {

        String resource = JobUtil.createJobNameForUpdateExternalGeocoverageCache();

        if (taskService.existUpdateExternalGeocoverageCacheTaskInResource(ctx)) {
            throw new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, resource);
        }

        TaskInfoResources taskInfo = new TaskInfoResources();
        taskInfo.setResourceType(externalResourcesToUpdate.getName());
        taskService.planifyUpdateExternalGeocoverageCache(ctx, taskInfo);
    }


}
