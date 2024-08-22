package org.siemac.metamac.statistical.resources.core.geocache.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResourceProperties;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourceProperties;
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
    public void updateAllGeoCacheResourcesByUrn(ServiceContext ctx, String resourceUrn, String resourceVersionUrn) {
        List<GeoCacheResource> geoCacheResources = findResourcesLastVersionByUrn(resourceUrn);

        for (GeoCacheResource geoCacheResource : geoCacheResources) {
            if (!geoCacheResource.getUrn().equals(resourceVersionUrn)) {
                geoCacheResource.setIsLastVersion(Boolean.FALSE);
                this.getGeoCacheResourceRepository().save(geoCacheResource);
            }
        }
    }

    @Override
    public void disabledResourceByUrn(ServiceContext ctx, String resourceUrn) {
        this.getGeoCacheResourceRepository().disabledByResourceVersionUrn(resourceUrn);
    }

    @Override
    public void deleteDisabledCacheEntries(ServiceContext ctx) {
        this.getGeoCacheResourceRepository().deleteAll();
    }

    private List<GeoCacheResource> findResourcesLastVersionByUrn(String resourceUrn) {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheResource.class).withProperty(GeoCacheResourceProperties.urn()).like(resourceUrn + "%").and()
                .withProperty(GeoCacheResourceProperties.isLastVersion()).eq(Boolean.TRUE).distinctRoot().build();
        // @formatter:off

        List<GeoCacheResource> geoCacheResources = this.getGeoCacheResourceRepository().findByCondition(conditions);     
        return geoCacheResources;
    }
    
    @Override
    public GeoCacheResource retrieveGeoCacheResourceByUrn(ServiceContext ctx, String resourceUrn) {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheResource.class).withProperty(GeoCacheResourceProperties.urn()).eq(resourceUrn).and()
                .withProperty(GeoCacheResourceProperties.isActivated()).eq(Boolean.TRUE).distinctRoot().build();
        // @formatter:off

        List<GeoCacheResource> geoCacheResources = this.getGeoCacheResourceRepository().findByCondition(conditions);     
                
        if ( geoCacheResources != null && !geoCacheResources.isEmpty() && geoCacheResources.size() == 1) {
            return geoCacheResources.get(0);
        }
        return null;

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
    public void updateAllGeoCacheRelatedResourcesByUrn(ServiceContext ctx, String resourceUrn, String resourceVersionUrn) {
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
    
    @Override
    public void disabledRelatedResourceByUrn(ServiceContext ctx, String relatedResourceUrn) {
        this.getGeoCacheByRelatedResourceRepository().disabledByResourceVersionUrn(relatedResourceUrn);
        
    }

    @Override
    public void deleteDisabledRelatedResourceCacheEntries(ServiceContext ctx) {
        // TODO EDATOS-4587
        
    }
    
    
}
