package org.siemac.metamac.statistical.resources.core.geocache.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResourceProperties;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourceProperties;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourcesByRelatedResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourcesByRelatedResourceProperties;
import org.siemac.metamac.statistical.resources.core.utils.InternationalStringUtils;
import org.siemac.metamac.statistical.resources.core.utils.shared.MetamacPortalWebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.ibestat.jaxi.stream.messages.DatasetAvro;

/**
 * Implementation of CacheService.
 */
@Service("cacheService")
public class CacheServiceImpl extends CacheServiceImplBase {

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

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

    @Override
    public GeoCacheResource updateGeoCacheResource(ServiceContext ctx, SiemacMetadataStatisticalResource metadataResource, LifeCycleStatisticalResource lifeCycleResource,
            StatisticalResourceTypeEnum type, boolean isLastVersionPublished) throws MetamacException {
        GeoCacheResource geoCacheResource = new GeoCacheResource();
        InternationalString titleResource = InternationalStringUtils.copy(metadataResource.getTitle());

        geoCacheResource.setCode(metadataResource.getCode());
        geoCacheResource.setUrn(metadataResource.getUrn());
        geoCacheResource.setTitle(titleResource);
        geoCacheResource.setOperationCode(metadataResource.getStatisticalOperation().getCode());
        geoCacheResource.setOperationUrn(metadataResource.getStatisticalOperation().getUrn());
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
    
    @Override
    public void  updateAllGeoCacheRelatedResourcesByUrn(ServiceContext ctx, String resourceUrn, String resourceVersionUrn) {
        List<GeoCacheByRelatedResource> geoCacheRelatedResources = findRelatedResourcesByUrn(resourceUrn);
        for (GeoCacheByRelatedResource geoCacheRelatedResource : geoCacheRelatedResources) {
            if (!geoCacheRelatedResource.getUrn().equals(resourceVersionUrn)) {
                geoCacheRelatedResource.setIsLastVersion(Boolean.FALSE);
                this.getGeoCacheByRelatedResourceRepository().save(geoCacheRelatedResource);
            }
        }        
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
    public void disabledRelatedResourceByUrn(ServiceContext ctx, String relatedResourceUrn) {
        this.getGeoCacheByRelatedResourceRepository().disabledByResourceVersionUrn(relatedResourceUrn);
        
    }

    @Override
    public void deleteDisabledRelatedResourceCacheEntries(ServiceContext ctx) {
        // TODO EDATOS-4587
        
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
     //   datasetServiceInvocationValidator.checkFindResourcesByCondition(ctx, conditions, pagingParameter);
        // TODO EDATOS-4587 PONER VALIDATOR A CacheServiceInvocationValidator

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
    
    
}
