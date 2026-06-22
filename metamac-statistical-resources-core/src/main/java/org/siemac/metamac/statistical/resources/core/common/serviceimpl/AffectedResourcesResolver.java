package org.siemac.metamac.statistical.resources.core.common.serviceimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AffectedResourcesResolver {

    @Autowired
    private QueryVersionRepository        queryVersionRepository;

    @Autowired
    private PublicationVersionRepository   publicationVersionRepository;

    @Autowired
    private MultidatasetVersionRepository  multidatasetVersionRepository;

    public Set<AffectedResource> findAffectedResources(String resourceRootUrn, StatisticalResourceTypeEnum resourceType) {
        Set<String> visitedRootUrns = new HashSet<String>();
        visitedRootUrns.add(resourceRootUrn);

        Set<AffectedResource> affectedResources = new LinkedHashSet<AffectedResource>();

        Queue<ResourceRef> queue = new LinkedList<ResourceRef>();
        queue.add(new ResourceRef(null, resourceRootUrn, resourceType));

        while (!queue.isEmpty()) {
            ResourceRef current = queue.poll();
            List<ResourceRef> newRefs = findDirectlyAffectedResources(current.rootUrn, current.type);

            for (ResourceRef ref : newRefs) {
                affectedResources.add(new AffectedResource(ref.versionUrn, ref.rootUrn, ref.type));
                if (visitedRootUrns.add(ref.rootUrn)) {
                    queue.add(ref);
                }
            }
        }

        return affectedResources;
    }

    private List<ResourceRef> findDirectlyAffectedResources(String rootUrn, StatisticalResourceTypeEnum type) {
        List<ResourceRef> refs = new ArrayList<ResourceRef>();

        switch (type) {
            case DATASET:
                addQueryRefs(rootUrn, refs);
                addPublicationRefs(rootUrn, StatisticalResourceTypeEnum.DATASET, refs);
                addMultidatasetRefs(rootUrn, StatisticalResourceTypeEnum.DATASET, refs);
                break;
            case QUERY:
                addPublicationRefs(rootUrn, StatisticalResourceTypeEnum.QUERY, refs);
                addMultidatasetRefs(rootUrn, StatisticalResourceTypeEnum.QUERY, refs);
                break;
            case MULTIDATASET:
                addPublicationRefs(rootUrn, StatisticalResourceTypeEnum.MULTIDATASET, refs);
                break;
            case COLLECTION:
                addPublicationRefs(rootUrn, StatisticalResourceTypeEnum.COLLECTION, refs);
                break;
            default:
                break;
        }

        return refs;
    }

    private void addQueryRefs(String datasetRootUrn, List<ResourceRef> refs) {
        List<QueryVersion> queryVersions = queryVersionRepository.findQueriesPublishedLinkedToDataset(datasetRootUrn);
        for (QueryVersion queryVersion : queryVersions) {
            refs.add(new ResourceRef(
                    queryVersion.getLifeCycleStatisticalResource().getUrn(),
                    queryVersion.getQuery().getIdentifiableStatisticalResource().getUrn(),
                    StatisticalResourceTypeEnum.QUERY));
        }
    }

    private void addPublicationRefs(String rootUrn, StatisticalResourceTypeEnum containedType, List<ResourceRef> refs) {
        List<PublicationVersion> publicationVersions = publicationVersionRepository.findPublishedContainingResource(rootUrn, containedType);
        for (PublicationVersion publicationVersion : publicationVersions) {
            refs.add(new ResourceRef(
                    publicationVersion.getSiemacMetadataStatisticalResource().getUrn(),
                    publicationVersion.getPublication().getIdentifiableStatisticalResource().getUrn(),
                    StatisticalResourceTypeEnum.COLLECTION));
        }
    }

    private void addMultidatasetRefs(String rootUrn, StatisticalResourceTypeEnum containedType, List<ResourceRef> refs) {
        List<MultidatasetVersion> multidatasetVersions = multidatasetVersionRepository.findPublishedContainingResource(rootUrn, containedType);
        for (MultidatasetVersion multidatasetVersion : multidatasetVersions) {
            refs.add(new ResourceRef(
                    multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn(),
                    multidatasetVersion.getMultidataset().getIdentifiableStatisticalResource().getUrn(),
                    StatisticalResourceTypeEnum.MULTIDATASET));
        }
    }

    public static class AffectedResource {

        private final String                       versionUrn;
        private final String                       rootUrn;
        private final StatisticalResourceTypeEnum  type;

        AffectedResource(String versionUrn, String rootUrn, StatisticalResourceTypeEnum type) {
            this.versionUrn = versionUrn;
            this.rootUrn = rootUrn;
            this.type = type;
        }

        public String getVersionUrn() {
            return versionUrn;
        }

        public String getRootUrn() {
            return rootUrn;
        }

        public StatisticalResourceTypeEnum getType() {
            return type;
        }

        @Override
        public int hashCode() {
            return versionUrn.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof AffectedResource)) return false;
            return versionUrn.equals(((AffectedResource) obj).versionUrn);
        }
    }

    private static class ResourceRef {

        final String                       versionUrn;
        final String                       rootUrn;
        final StatisticalResourceTypeEnum  type;

        ResourceRef(String versionUrn, String rootUrn, StatisticalResourceTypeEnum type) {
            this.versionUrn = versionUrn;
            this.rootUrn = rootUrn;
            this.type = type;
        }
    }
}
