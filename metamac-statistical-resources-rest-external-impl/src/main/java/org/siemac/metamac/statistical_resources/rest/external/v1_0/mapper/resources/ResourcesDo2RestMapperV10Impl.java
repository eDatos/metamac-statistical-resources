package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.resources;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourcesByRelatedResource;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceWithRelatedResources;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceWithStatisticalOperation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Resources;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourcesStatisticalResourceBase;
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
    public Resources toResources(PagedResult<GeoCacheByRelatedResource> sources, String query, String orderBy, Integer limit, List<String> selectedLanguages) throws RestException {

        Resources targets = new Resources();
        targets.setKind(StatisticalResourcesRestExternalConstants.KIND_RESOURCES);

        // Pagination
        String baseLink = toResourcesLink(TypeExternalArtefactsEnum.DATASET.getName()); // EDATOS-4587 SI SE PONE UN TIPO NUEVO.
        SculptorCriteria2RestCriteria.toPagedResult(sources, targets, query, orderBy, limit, baseLink);

        Map<String, org.siemac.metamac.rest.common.v1_0.domain.InternationalString> operationTitles = statisticalOperationsRestExternalFacade.getOperationTitles(null);

        // Values
        for (GeoCacheByRelatedResource source : sources.getValues()) {
            ResourceWithRelatedResources resourceWithRelatedResources = new ResourceWithRelatedResources();
            ResourceWithStatisticalOperation mainResource = toResource(source, operationTitles, selectedLanguages);
            resourceWithRelatedResources.setMainResource(mainResource);

            ResourcesStatisticalResourceBase resources = new ResourcesStatisticalResourceBase();

            if (source.getRelatedResources() != null) {
                for (GeoCacheResourcesByRelatedResource geoRelatedResourceByResource : source.getRelatedResources()) {
                    ResourceWithStatisticalOperation relatedResource = toResource(geoRelatedResourceByResource.getGeoCacheResource(), operationTitles, selectedLanguages);
                    resources.getResources().add(relatedResource);

                }
                resources.setTotal(BigInteger.valueOf(resources.getResources().size()));

            }
            resourceWithRelatedResources.setRelatedResources(resources);

            targets.getResources().add(resourceWithRelatedResources);
        }
        return targets;
    }

    private ResourceWithStatisticalOperation toResource(GeoCacheByRelatedResource source, Map<String, org.siemac.metamac.rest.common.v1_0.domain.InternationalString> operationTitles,
            List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        ResourceWithStatisticalOperation target = new ResourceWithStatisticalOperation();
        target.setId(source.getCode());
        target.setUrn(getCollectionUrnWithoutVersion(source.getUrn()));
        target.setName(commonDo2RestMapper.toInternationalString(source.getTitle(), selectedLanguages));
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_RESOURCE);
        target.setSelfLink(toResourceSelfLink(source.getUrn(), TypeExternalArtefactsEnum.COLLECTION.getName()));
        target.setVisualizerHtmlLink(source.getHtmlLink());
        target.setStatisticalOperation(toStatisticalOperationResource(source.getOperationCode(), source.getOperationUrn(), operationTitles, selectedLanguages));

        return target;
    }

    private String getCollectionUrnWithoutVersion(String urn) {
        String[] params = UrnUtils.splitUrnItem(urn, false);
        return GeneratorUrnUtils.generateSiemacStatisticalResourceCollectionUrn(new String[]{params[0]}, params[1]);
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
        target.setSelfLink(toResourceSelfLink(source.getUrn(), TypeExternalArtefactsEnum.DATASET.getName()));
        target.setVisualizerHtmlLink(source.getHtmlLink());
        target.setStatisticalOperation(toStatisticalOperationResource(source.getOperationCode(), source.getOperationUrn(), operationTitles, selectedLanguages));

        return target;
    }

    private ResourceStatisticalResourceBase toStatisticalOperationResource(String codeOperation, String urnOperation,
            Map<String, org.siemac.metamac.rest.common.v1_0.domain.InternationalString> operationTitles, List<String> selectedLanguages) {
        ResourceStatisticalResourceBase target = new ResourceStatisticalResourceBase();
        target.setId(codeOperation);
        target.setUrn(urnOperation);

        org.siemac.metamac.rest.common.v1_0.domain.InternationalString operationTitle = operationTitles.get(codeOperation);
        target.setName(operationTitle);

        target.setKind(TypeExternalArtefactsEnum.STATISTICAL_OPERATION.getValue());
        return target;
    }

    private String toResourcesLink(String typeResource) {
        String resourceSubpath = StatisticalResourcesRestExternalConstants.LINK_SUBPATH_RESOURCES + StatisticalResourcesRestExternalConstants.KIND_SEPARATOR + typeResource.toLowerCase();
        return commonDo2RestMapper.toResourceLink(resourceSubpath, null, null, null);
    }

    private ResourceLink toResourceSelfLink(String urn, String typeResource) {
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