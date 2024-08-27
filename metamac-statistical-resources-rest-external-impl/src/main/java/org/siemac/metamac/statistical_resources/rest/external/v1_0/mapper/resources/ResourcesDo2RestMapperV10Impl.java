package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.resources;

import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceWithStatisticalOperation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Resources;
import org.siemac.metamac.statistical_resources.rest.external.StatisticalResourcesRestExternalConstants;
import org.siemac.metamac.statistical_resources.rest.external.invocation.StatisticalOperationsRestExternalFacade;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base.CommonDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourcesDo2RestMapperV10Impl implements ResourcesDo2RestMapperV10 {

    @Autowired
    private CommonDo2RestMapperV10                  commonDo2RestMapper;

    @Autowired
    private StatisticalOperationsRestExternalFacade statisticalOperationsRestExternalFacade;

    @Override
    public Resources toResources(PagedResult<GeoCacheResource> sources, String query, String orderBy, Integer limit, List<String> selectedLanguages) throws RestException {

        Resources targets = new Resources();
        targets.setKind(StatisticalResourcesRestExternalConstants.KIND_RESOURCES);

        // Pagination
        String baseLink = toResourcesLink(TypeExternalArtefactsEnum.DATASET.getName());
        SculptorCriteria2RestCriteria.toPagedResult(sources, targets, query, orderBy, limit, baseLink);

        Map<String, org.siemac.metamac.rest.common.v1_0.domain.InternationalString> operationTitles = statisticalOperationsRestExternalFacade.getOperationTitles(null);

        // Values
        for (GeoCacheResource source : sources.getValues()) {
            ResourceWithStatisticalOperation target = toResource(source, operationTitles, selectedLanguages);
            targets.getResources().add(target);
        }
        return targets;
    }

    private ResourceWithStatisticalOperation toResource(GeoCacheResource source, Map<String, org.siemac.metamac.rest.common.v1_0.domain.InternationalString> operationTitles,
            List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        ResourceWithStatisticalOperation target = new ResourceWithStatisticalOperation();
        target.setId(source.getCode());
        target.setUrn(source.getUrn());
        target.setName(commonDo2RestMapper.toInternationalString(source.getTitle(), selectedLanguages));
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_RESOURCE);
        target.setSelfLink(toDatasetSelfLink(source.getUrn(), TypeExternalArtefactsEnum.DATASET.getName()));
        target.setVisualizerHtmlLink(source.getHtmlLink());
        target.setStatisticalOperation(toStatisticalOperationResource(source, operationTitles, selectedLanguages));

        return target;
    }

    private ResourceStatisticalResourceBase toStatisticalOperationResource(GeoCacheResource source, Map<String, org.siemac.metamac.rest.common.v1_0.domain.InternationalString> operationTitles,
            List<String> selectedLanguages) {
        ResourceStatisticalResourceBase target = new ResourceStatisticalResourceBase();
        target.setId(source.getOperationCode());
        target.setUrn(source.getOperationUrn());

        org.siemac.metamac.rest.common.v1_0.domain.InternationalString operationTitle = operationTitles.get(source.getOperationCode());
        target.setName(operationTitle);

        target.setKind(TypeExternalArtefactsEnum.STATISTICAL_OPERATION.getValue());
        return target;
    }

    private String toResourcesLink(String typeResource) {
        String resourceSubpath = StatisticalResourcesRestExternalConstants.LINK_SUBPATH_RESOURCES + StatisticalResourcesRestExternalConstants.KIND_SEPARATOR + typeResource.toLowerCase();
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