package org.siemac.metamac.statistical.resources.core.publication.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.Publication;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;

public final class PublicationsUtils {

    private PublicationsUtils() {
    }

    public static List<RelatedResource> computeHasPart(PublicationVersion resource) {
        Map<String, Dataset> datasetsByUrn = new HashMap<>();
        Map<String, Query> queriesByUrn = new HashMap<>();
        Map<String, Multidataset> multidatasetByUrn = new HashMap<>();
        Map<String, Publication> publicationByUrn = new HashMap<>();

        for (ElementLevel elementLevel : resource.getChildrenAllLevels()) {
            if (elementLevel.isCube()) {
                Cube cube = elementLevel.getCube();
                if (cube.getDataset() != null) {
                    datasetsByUrn.put(cube.getDatasetUrn(), cube.getDataset());
                } else if (cube.getQuery() != null) {
                    queriesByUrn.put(cube.getQueryUrn(), cube.getQuery());
                } else if (cube.getMultidataset() != null) {
                    multidatasetByUrn.put(cube.getMultidatasetUrn(), cube.getMultidataset());
                } else if (cube.getPublication() != null) {
                    publicationByUrn.put(cube.getPublicationUrn(), cube.getPublication());
                } 
            }
        }

        List<RelatedResource> resources = new ArrayList<>();
        resources.addAll(buildRelatedResourcesForDatasets(datasetsByUrn.values()));
        resources.addAll(buildRelatedResourcesForQueries(queriesByUrn.values()));
        resources.addAll(buildRelatedResourcesForMultidatasets(multidatasetByUrn.values()));
        resources.addAll(buildRelatedResourcesForPublications(publicationByUrn.values()));
        
        return resources;
    }

    public static List<String> computeUrlsHasPart(PublicationVersion resource) {
        List<String> urls = new ArrayList<String>();

        for (ElementLevel elementLevel : resource.getChildrenAllLevels()) {
            if (elementLevel.isCube()) {
                Cube cube = elementLevel.getCube();
                if (cube.getUrl() != null) {
                    urls.add(cube.getUrl());
                } 
            }
        }
  
        return urls;
    }
    
    private static Collection<RelatedResource> buildRelatedResourcesForMultidatasets(Collection<Multidataset> multidatasets) {
        Set<RelatedResource> resources = new HashSet<>();
        for (Multidataset multidataset : multidatasets) {
            resources.add(buildRelatedResourceForMultidataset(multidataset));
        }
        return resources;
    }

    private static Collection<RelatedResource> buildRelatedResourcesForPublications(Collection<Publication> publications) {
        Set<RelatedResource> resources = new HashSet<>();
        for (Publication publication : publications) {
            resources.add(buildRelatedResourceForPublication(publication));
        }
        return resources;
    }

    private static RelatedResource buildRelatedResourceForMultidataset(Multidataset multidataset) {
        RelatedResource resource = new RelatedResource(TypeRelatedResourceEnum.MULTIDATASET);
        resource.setMultidataset(multidataset);
        return resource;
    }

    private static RelatedResource buildRelatedResourceForPublication(Publication publication) {
        RelatedResource resource = new RelatedResource(TypeRelatedResourceEnum.PUBLICATION);
        resource.setPublication(publication);
        return resource;
    }

    public static Collection<RelatedResource> buildRelatedResourcesForQueries(Collection<Query> queries) {
        Set<RelatedResource> resources = new HashSet<>();
        for (Query query : queries) {
            resources.add(buildRelatedResourceForQuery(query));
        }
        return resources;
    }

    public static RelatedResource buildRelatedResourceForQuery(Query query) {
        RelatedResource resource = new RelatedResource(TypeRelatedResourceEnum.QUERY);
        resource.setQuery(query);
        return resource;
    }

    public static Collection<RelatedResource> buildRelatedResourcesForDatasets(Collection<Dataset> datasets) {
        Set<RelatedResource> resources = new HashSet<>();
        for (Dataset dataset : datasets) {
            resources.add(buildRelatedResourceForDataset(dataset));
        }
        return resources;
    }

    public static RelatedResource buildRelatedResourceForDataset(Dataset dataset) {
        RelatedResource resource = new RelatedResource(TypeRelatedResourceEnum.DATASET);
        resource.setDataset(dataset);
        return resource;
    }

}
