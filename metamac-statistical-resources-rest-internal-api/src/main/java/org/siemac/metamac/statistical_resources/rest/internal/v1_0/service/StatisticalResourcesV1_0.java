package org.siemac.metamac.statistical_resources.rest.internal.v1_0.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Collection;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Collections;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Datasets;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Multidataset;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Multidatasets;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Queries;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Query;

@Path("v1.0")
// IMPORTANT: If a new version of API is added, remember change latest url y urlrewrite.xml in war
public interface StatisticalResourcesV1_0 {

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("datasets")
    Datasets findDatasets(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset,
            @QueryParam("lang") List<String> lang);

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("datasets/{agencyID}")
    Datasets findDatasets(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset, @QueryParam("lang") List<String> lang);

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("datasets/{agencyID}/{resourceID}")
    Datasets findDatasets(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
            @QueryParam("limit") String limit, @QueryParam("offset") String offset, @QueryParam("lang") List<String> lang);

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("datasets/{agencyID}/{resourceID}/{version}")
    Dataset retrieveDataset(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @QueryParam("lang") List<String> lang,
            @QueryParam("fields") String fields, @QueryParam("dim") String dim);

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("collections")
    Collections findCollections(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset,
            @QueryParam("lang") List<String> lang);

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("collections/{agencyID}")
    Collections findCollections(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset, @QueryParam("lang") List<String> lang);

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("collections/{agencyID}/{resourceID}")
    Collection retrieveCollection(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("lang") List<String> lang, @QueryParam("fields") String fields);

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("queries")
    Queries findQueries(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset,
            @QueryParam("lang") List<String> lang);

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("queries/{agencyID}")
    Queries findQueries(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset, @QueryParam("lang") List<String> lang);

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("queries/{agencyID}/{resourceID}")
    Query retrieveQuery(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("lang") List<String> lang, @QueryParam("fields") String fields,
            @QueryParam("dim") String dim);

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("multidatasets")
    Multidatasets findMultidatasets(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset,
            @QueryParam("lang") List<String> lang);

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("multidatasets/{agencyID}")
    Multidatasets findMultidatasets(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset, @QueryParam("lang") List<String> lang);

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("multidatasets/{agencyID}/{resourceID}")
    Multidataset retrieveMultidataset(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("lang") List<String> lang, @QueryParam("fields") String fields);

}
