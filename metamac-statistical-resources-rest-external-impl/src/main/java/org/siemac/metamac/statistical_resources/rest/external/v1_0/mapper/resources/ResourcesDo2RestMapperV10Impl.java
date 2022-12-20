package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.resources;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Resources;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersion;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ExtendedResource;
import org.siemac.metamac.statistical_resources.rest.external.StatisticalResourcesRestExternalConstants;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base.CommonDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourcesDo2RestMapperV10Impl implements ResourcesDo2RestMapperV10 {
    
    @Autowired
    private CommonDo2RestMapperV10   commonDo2RestMapper;
    
    @Override
    public Resources toResources(PagedResult<GeoCovVarElementCacheDatasetVersion> sources, String query, String orderBy, Integer limit, List<String> selectedLanguages) {

        Resources targets = new Resources();
        targets.setKind(StatisticalResourcesRestExternalConstants.KIND_RESOURCES);

        // Pagination
        String baseLink =  toResourcesLink(TypeExternalArtefactsEnum.DATASET.getName());
        SculptorCriteria2RestCriteria.toPagedResult(sources, targets, query, orderBy, limit, baseLink);

        // Values
        for (GeoCovVarElementCacheDatasetVersion source : sources.getValues()) {
            ExtendedResource target = toResource(source, selectedLanguages);
            targets.getResources().add(target);
        }
        return targets;
    } 
     
    private ExtendedResource toResource(GeoCovVarElementCacheDatasetVersion source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        ExtendedResource target = new ExtendedResource();
        target.setId(source.getCode());
        target.setUrn(source.getUrn());
        target.setName(commonDo2RestMapper.toInternationalString(source.getTitle(), selectedLanguages));
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_RESOURCE);
        target.setSelfLink(toDatasetSelfLink(source.getUrn(), TypeExternalArtefactsEnum.DATASET.getName()));
        target.setVisualizerHtmlLink(source.getHtmlLink());
        target.setStatisticalOperation(toStatisticalOperationResource(source, selectedLanguages));

        return target;
    }
    
    private Resource toStatisticalOperationResource(GeoCovVarElementCacheDatasetVersion source, List<String> selectedLanguages) {
        Resource target = new Resource();
        target.setId(source.getOperationCode());
        target.setUrn(source.getOperationUrn());
        target.setName(commonDo2RestMapper.toInternationalString(source.getOperationTitle(), selectedLanguages));
        target.setKind(TypeExternalArtefactsEnum.STATISTICAL_OPERATION.getValue());
        return target;
    }
    
    private String toResourcesLink(String typeResource) {
        String resourceSubpath = StatisticalResourcesRestExternalConstants.LINK_SUBPATH_RESOURCES +  StatisticalResourcesRestExternalConstants.KIND_SEPARATOR + typeResource.toLowerCase();
        return commonDo2RestMapper.toResourceLink(resourceSubpath, null, null, null);
    }
    
    private ResourceLink toDatasetSelfLink(String urn, String typeResource) {
        String[] params = UrnUtils.splitUrnItem(urn, false);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = params[2]; 
        return toResourceSelfLink(agencyId, resourceId, version, typeResource);
    }
    
    private ResourceLink toResourceSelfLink(String agencyID, String resourceID, String version, String typeResource) {
        String link = toDatasetLink(agencyID, resourceID, version, typeResource);
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestExternalConstants.KIND_RESOURCE, link);
    }
    
    private String toDatasetLink(String agencyID, String resourceID, String version, String typeResource) {
        String resourceSubpath = StatisticalResourcesRestExternalConstants.LINK_SUBPATH_RESOURCES + StatisticalResourcesRestExternalConstants.KIND_SEPARATOR + typeResource.toLowerCase();
        return commonDo2RestMapper.toResourceLink(resourceSubpath, agencyID, resourceID, version);
    }
}