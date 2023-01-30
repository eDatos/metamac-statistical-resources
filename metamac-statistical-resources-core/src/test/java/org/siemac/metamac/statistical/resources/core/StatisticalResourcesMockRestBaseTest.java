package org.siemac.metamac.statistical.resources.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.siemac.metamac.common_metadata.rest.external.v1_0.service.CommonMetadataV1_0;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.rest.api.constants.RestApiConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.service.SrmRestInternalFacadeV10;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.utils.RelatedResourceUtils;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.service.MetamacApisLocator;
import org.siemac.metamac.statistical.resources.core.utils.CommonMetadataRestInternalFacadeV10MockUtils;
import org.siemac.metamac.statistical.resources.core.utils.SrmRestInternalFacadeV10MockUtils;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalOperationsRestInternalFacadeV10MockUtils;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesCollectionUtils;
import org.siemac.metamac.statistical.resources.core.utils.transformers.ExternalItemToUrnTransformer;
import org.siemac.metamac.statistical_operations.rest.internal.v1_0.service.StatisticalOperationsRestInternalFacadeV10;
import org.springframework.beans.factory.annotation.Autowired;

public class StatisticalResourcesMockRestBaseTest extends StatisticalResourcesBaseTest {

    @Mock
    private SrmRestInternalFacadeV10                   srmRestInternalFacadeV10;

    @Mock
    private CommonMetadataV1_0                         commonMetadataV10;

    @Mock
    private StatisticalOperationsRestInternalFacadeV10 statisticalOperationsRestInternalFacadeV10;

    @Autowired
    protected MetamacApisLocator                       metamacApisLocator;

    @Before
    public void setUp() throws MetamacException {
        MockitoAnnotations.initMocks(this);
        mockApis();
    }

    private void mockApis() throws MetamacException {
        Mockito.when(metamacApisLocator.getStatisticalOperationsRestInternalFacadeV10()).thenReturn(statisticalOperationsRestInternalFacadeV10);
        Mockito.when(metamacApisLocator.getCommonMetadataRestExternalFacadeV10()).thenReturn(commonMetadataV10);
        Mockito.when(metamacApisLocator.getSrmRestInternalFacadeV10()).thenReturn(srmRestInternalFacadeV10);
    }

    protected void mockExternalItemPublished(ExternalItem item) {
        if (item != null) {
            mockExternalItemsPublished(Arrays.asList(item));
        }
    }

    protected void mockExternalItemNotPublished(ExternalItem item) {
        if (item != null) {
            mockExternalItemsNotPublished(Arrays.asList(item));
        }
    }

    protected void mockExternalItemsPublished(Collection<ExternalItem> items) {
        mockExternalItemsPublished(items, items);
    }

    protected void mockExternalItemsNotPublished(Collection<ExternalItem> items) {
        mockExternalItemsPublished(items, new ArrayList<ExternalItem>());
    }

    private void mockExternalItemsPublished(Collection<ExternalItem> allItems, Collection<ExternalItem> publishedItems) {
        if (allItems != null && allItems.size() > 0) {
            TypeExternalArtefactsEnum typeExternalItem = allItems.iterator().next().getType();
            switch (typeExternalItem) {
                case ORGANISATION_UNIT:
                case AGENCY:
                case DATA_PROVIDER:
                    mockFindPublishedOrganisations(allItems, publishedItems);
                    break;
                case CODE:
                    mockFindPublishedCodes(allItems, publishedItems);
                    break;
                case CONCEPT:
                    mockFindPublishedConcepts(allItems, publishedItems);
                    break;
                case CATEGORY:
                    mockFindPublishedCategories(allItems, publishedItems);
                    break;
                case DATASTRUCTURE:
                    mockFindPublishedDsd(allItems, publishedItems);
                    break;
                case CONFIGURATION:
                    mockFindPublishedConfiguration(allItems, publishedItems);
                    break;
                case STATISTICAL_OPERATION:
                    mockFindPublishedStatisticalOperation(allItems, publishedItems);
                    break;
                case STATISTICAL_OPERATION_INSTANCE:
                    mockFindPublishedStatisticalOperationInstances(allItems, publishedItems);
                    break;
                case CODELIST:
                    mockFindPublishedCodelist(allItems, publishedItems);
                    break;
                default:
                    throw new RuntimeException("Found unknown external item type " + typeExternalItem);
            }
        }
    }

    private void mockFindPublishedOrganisations(Collection<ExternalItem> allItems, Collection<ExternalItem> publishedItems) {
        List<String> urns = getUrnsFromExternalItems(allItems);
        List<String> publishedUrns = getUrnsFromExternalItems(publishedItems);
        Mockito.when(srmRestInternalFacadeV10.findOrganisations(Mockito.eq(RestApiConstants.WILDCARD_ALL), Mockito.eq(RestApiConstants.WILDCARD_ALL), Mockito.eq(RestApiConstants.WILDCARD_ALL),
                Mockito.eq(SrmRestInternalFacadeV10MockUtils.mockQueryFindPublishedOrganisationsUrnsAsList(urns)), Mockito.isNull(String.class), Mockito.anyString(), Mockito.anyString(), null))
                .thenReturn(SrmRestInternalFacadeV10MockUtils.mockOrganisationsWithOnlyUrns(publishedUrns));
    }

    protected void mockFindPublishedCodes(Collection<ExternalItem> allItems, Collection<ExternalItem> publishedItems) {
        List<String> urns = getUrnsFromExternalItems(allItems);
        List<String> urnsPublished = getUrnsFromExternalItems(publishedItems);
        Mockito.when(srmRestInternalFacadeV10.findCodes(Mockito.eq(RestApiConstants.WILDCARD_ALL), Mockito.eq(RestApiConstants.WILDCARD_ALL), Mockito.eq(RestApiConstants.WILDCARD_ALL),
                Mockito.eq(SrmRestInternalFacadeV10MockUtils.mockQueryFindPublishedCodesUrnsAsList(urns)), Mockito.isNull(String.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(SrmRestInternalFacadeV10MockUtils.mockCodesWithOnlyUrns(urnsPublished));
    }

    protected void mockFindPublishedConcepts(Collection<ExternalItem> allItems, Collection<ExternalItem> publishedItems) {
        List<String> urns = getUrnsFromExternalItems(allItems);
        List<String> publishedUrns = getUrnsFromExternalItems(publishedItems);
        Mockito.when(srmRestInternalFacadeV10.findConcepts(Mockito.eq(RestApiConstants.WILDCARD_ALL), Mockito.eq(RestApiConstants.WILDCARD_ALL), Mockito.eq(RestApiConstants.WILDCARD_ALL),
                Mockito.eq(SrmRestInternalFacadeV10MockUtils.mockQueryFindPublishedConceptsUrnsAsList(urns)), Mockito.isNull(String.class), Mockito.anyString(), Mockito.anyString(),
                Mockito.isNull(String.class))).thenReturn(SrmRestInternalFacadeV10MockUtils.mockConceptsWithOnlyUrns(publishedUrns));
    }

    protected void mockFindPublishedCategories(Collection<ExternalItem> allItems, Collection<ExternalItem> publishedItems) {
        List<String> urns = getUrnsFromExternalItems(allItems);
        List<String> publishedUrns = getUrnsFromExternalItems(publishedItems);
        Mockito.when(srmRestInternalFacadeV10.findCategories(Mockito.eq(RestApiConstants.WILDCARD_ALL), Mockito.eq(RestApiConstants.WILDCARD_ALL), Mockito.eq(RestApiConstants.WILDCARD_ALL),
                Mockito.eq(SrmRestInternalFacadeV10MockUtils.mockQueryFindPublishedCategoriesUrnsAsList(urns)), Mockito.isNull(String.class), Mockito.anyString(), Mockito.anyString(), null))
                .thenReturn(SrmRestInternalFacadeV10MockUtils.mockCategoriesWithOnlyUrns(publishedUrns));
    }

    protected void mockFindPublishedDsd(Collection<ExternalItem> allItems, Collection<ExternalItem> publishedItems) {
        List<String> urns = getUrnsFromExternalItems(allItems);
        List<String> publishedUrns = getUrnsFromExternalItems(publishedItems);
        Mockito.when(srmRestInternalFacadeV10.findDataStructures(Mockito.eq(SrmRestInternalFacadeV10MockUtils.mockQueryFindPublishedDsdsUrnsAsList(urns)), Mockito.isNull(String.class),
                Mockito.anyString(), Mockito.anyString())).thenReturn(SrmRestInternalFacadeV10MockUtils.mockDsdsWithOnlyUrns(publishedUrns));
    }

    protected void mockFindPublishedConfiguration(Collection<ExternalItem> allItems, Collection<ExternalItem> publishedItems) {
        List<String> urns = getUrnsFromExternalItems(allItems);
        List<String> publishedUrns = getUrnsFromExternalItems(publishedItems);
        Mockito.when(commonMetadataV10.findConfigurations(Mockito.eq(CommonMetadataRestInternalFacadeV10MockUtils.mockQueryFindPublishedConfigurationsUrnsAsList(urns)), Mockito.isNull(String.class)))
                .thenReturn(CommonMetadataRestInternalFacadeV10MockUtils.mockConfigurationsWithOnlyUrns(publishedUrns));
    }

    protected void mockFindPublishedStatisticalOperation(Collection<ExternalItem> allItems, Collection<ExternalItem> publishedItems) {
        List<String> urns = getUrnsFromExternalItems(allItems);
        List<String> publishedUrns = getUrnsFromExternalItems(publishedItems);
        Mockito.when(
                statisticalOperationsRestInternalFacadeV10.findOperations(Mockito.eq(StatisticalOperationsRestInternalFacadeV10MockUtils.mockQueryFindPublishedStatisticalOperationsUrnsAsList(urns)),
                        Mockito.isNull(String.class), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(StatisticalOperationsRestInternalFacadeV10MockUtils.mockStatisticalOperationsWithOnlyUrns(publishedUrns));
    }

    protected void mockFindPublishedStatisticalOperationInstances(Collection<ExternalItem> allItems, Collection<ExternalItem> publishedItems) {
        List<String> urns = getUrnsFromExternalItems(allItems);
        List<String> publishedUrns = getUrnsFromExternalItems(publishedItems);
        Mockito.when(statisticalOperationsRestInternalFacadeV10.findInstances(Mockito.eq(RestApiConstants.WILDCARD_ALL),
                Mockito.eq(StatisticalOperationsRestInternalFacadeV10MockUtils.mockQueryFindPublishedStatisticalOperationInstancesUrnsAsList(urns)), Mockito.isNull(String.class), Mockito.anyString(),
                Mockito.anyString())).thenReturn(StatisticalOperationsRestInternalFacadeV10MockUtils.mockStatisticalOperationInstancesWithOnlyUrns(publishedUrns));
    }

    protected void mockFindPublishedCodelist(Collection<ExternalItem> allItems, Collection<ExternalItem> publishedItems) {
        List<String> urns = getUrnsFromExternalItems(allItems);
        List<String> publishedUrns = getUrnsFromExternalItems(publishedItems);
        Mockito.when(srmRestInternalFacadeV10.findCodelists(Mockito.eq(SrmRestInternalFacadeV10MockUtils.mockQueryFindPublishedCodelistUrnsAsList(urns)), Mockito.isNull(String.class),
                Mockito.anyString(), Mockito.anyString())).thenReturn(SrmRestInternalFacadeV10MockUtils.mockCodelistWithOnlyUrns(publishedUrns));
    }

    protected List<String> getUrnsFromExternalItems(Collection<ExternalItem> items) {
        List<String> urns = new ArrayList<String>();
        StatisticalResourcesCollectionUtils.mapCollection(items, urns, new ExternalItemToUrnTransformer());
        return urns;
    }

    protected List<MetamacExceptionItem> buildExternalItemNotPublishedExceptionForAllItems(List<ExternalItem> items, String field) {
        return buildExpectedExceptionForAllItems(ServiceExceptionType.EXTERNAL_ITEM_NOT_PUBLISHED, items, field);
    }

    protected List<MetamacExceptionItem> buildExpectedExceptionForAllItems(CommonServiceExceptionType exceptionType, Collection<ExternalItem> items, String messageParameter) {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        for (ExternalItem item : items) {
            exceptionItems.add(new MetamacExceptionItem(exceptionType, messageParameter, item.getUrn()));
        }
        return exceptionItems;
    }

    protected List<MetamacExceptionItem> getExceptionItemsForExternalItemNotPublishedLifecycle(LifeCycleStatisticalResource lifecycleResource, String baseField, boolean fromSiemac) {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        exceptionItems.add(buildExternalItemNotPublishedException(lifecycleResource.getStatisticalOperation(), baseField, "statistical_operation"));
        exceptionItems.add(buildExternalItemNotPublishedException(lifecycleResource.getMaintainer(), baseField, "maintainer"));

        return exceptionItems;
    }

    protected List<MetamacExceptionItem> getExceptionItemsForExternalItemNotPublishedSiemac(SiemacMetadataStatisticalResource siemacResource, String baseField) {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();

        exceptionItems.addAll(getExceptionItemsForExternalItemNotPublishedLifecycle(siemacResource, baseField, true));
        exceptionItems.add(buildExternalItemNotPublishedException(siemacResource.getLanguage(), baseField, "language"));
        exceptionItems.addAll(buildExternalItemsNotPublishedExceptions(siemacResource.getLanguages(), baseField, "languages"));
        exceptionItems.addAll(buildExternalItemsNotPublishedExceptions(siemacResource.getStatisticalOperationInstances(), baseField, "statistical_operation_instances"));
        exceptionItems.add(buildExternalItemNotPublishedException(siemacResource.getCreator(), baseField, "creator"));
        exceptionItems.addAll(buildExternalItemsNotPublishedExceptions(siemacResource.getDataProvider(), baseField, "data_provider"));
        exceptionItems.addAll(buildExternalItemsNotPublishedExceptions(siemacResource.getContributor(), baseField, "contributor"));
        exceptionItems.addAll(buildExternalItemsNotPublishedExceptions(siemacResource.getPublisher(), baseField, "publisher"));
        exceptionItems.addAll(buildExternalItemsNotPublishedExceptions(siemacResource.getPublisherContributor(), baseField, "publisher_contributor"));
        exceptionItems.addAll(buildExternalItemsNotPublishedExceptions(siemacResource.getMediator(), baseField, "mediator"));
        exceptionItems.add(buildExternalItemNotPublishedException(siemacResource.getCommonMetadata(), baseField, "common_metadata"));

        return exceptionItems;
    }

    protected MetamacExceptionItem buildExternalItemNotPublishedException(ExternalItem item, String entity, String field) {
        return new MetamacExceptionItem(ServiceExceptionType.EXTERNAL_ITEM_NOT_PUBLISHED, buildField(entity, field), item.getUrn());
    }

    protected MetamacExceptionItem buildRelatedResourceNotPublishedException(RelatedResource resource, String entity, String field) {
        NameableStatisticalResource nameable = null;
        try {
            nameable = RelatedResourceUtils.retrieveNameableResourceLinkedToRelatedResource(resource);
        } catch (MetamacException e) {
            Assert.fail("Could not retrieve nameable form related resource " + resource.getId());
        }
        return new MetamacExceptionItem(ServiceExceptionType.RELATED_RESOURCE_NOT_PUBLISHED, buildField(entity, field), nameable.getUrn());
    }

    protected List<MetamacExceptionItem> buildExternalItemsNotPublishedExceptions(Collection<ExternalItem> items, String entity, String field) {
        return buildExpectedExceptionForAllItems(ServiceExceptionType.EXTERNAL_ITEM_NOT_PUBLISHED, items, buildField(entity, field));
    }

    private String buildField(String... fields) {
        return StringUtils.join(fields, ".");
    }

    protected void mockLifecycleExternalItemsNotPublished(LifeCycleStatisticalResource lifecycleResource) {
        mockExternalItemNotPublished(lifecycleResource.getStatisticalOperation());
        mockExternalItemNotPublished(lifecycleResource.getMaintainer());
    }

    protected void mockSiemacExternalItemsNotPublished(SiemacMetadataStatisticalResource siemacResource) {
        // Lifecycle
        mockLifecycleExternalItemsNotPublished(siemacResource);

        // Siemac
        mockExternalItemNotPublished(siemacResource.getLanguage());
        mockExternalItemsNotPublished(siemacResource.getLanguages());
        mockExternalItemsNotPublished(siemacResource.getStatisticalOperationInstances());
        mockExternalItemNotPublished(siemacResource.getCreator());
        mockExternalItemsNotPublished(siemacResource.getDataProvider());
        mockExternalItemsNotPublished(siemacResource.getContributor());
        mockExternalItemsNotPublished(siemacResource.getPublisher());
        mockExternalItemsNotPublished(siemacResource.getPublisherContributor());
        mockExternalItemsNotPublished(siemacResource.getMediator());
        mockExternalItemNotPublished(siemacResource.getCommonMetadata());
    }

    protected void mockLifecycleExternalItemsPublished(LifeCycleStatisticalResource lifecycleResource) {
        mockExternalItemPublished(lifecycleResource.getStatisticalOperation());
        mockExternalItemPublished(lifecycleResource.getMaintainer());
    }

    protected void mockConstraintsResponses() {
        Mockito.when(srmRestInternalFacadeV10.publishContentConstraintsForArtefact(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(new MyResponse());
    }

    protected void mockSiemacExternalItemsPublished(SiemacMetadataStatisticalResource siemacResource) {
        // Lifecycle
        mockLifecycleExternalItemsPublished(siemacResource);

        // Siemac
        mockExternalItemPublished(siemacResource.getLanguage());
        mockExternalItemsPublished(siemacResource.getLanguages());
        mockExternalItemsPublished(siemacResource.getStatisticalOperationInstances());
        mockExternalItemPublished(siemacResource.getCreator());
        mockExternalItemsPublished(siemacResource.getDataProvider());
        mockExternalItemsPublished(siemacResource.getContributor());
        mockExternalItemsPublished(siemacResource.getPublisher());
        mockExternalItemsPublished(siemacResource.getPublisherContributor());
        mockExternalItemsPublished(siemacResource.getMediator());
        mockExternalItemPublished(siemacResource.getCommonMetadata());
    }

    protected String getChapterMockUrn(String mockId) {
        return chapterMockFactory.retrieveMock(mockId).getNameableStatisticalResource().getUrn();
    }

    protected String getCubeMockUrn(String mockId) {
        return cubeMockFactory.retrieveMock(mockId).getNameableStatisticalResource().getUrn();
    }

    protected String getDatasetMockUrn(String mockId) {
        return datasetMockFactory.retrieveMock(mockId).getIdentifiableStatisticalResource().getUrn();
    }

    protected String getDatasetVersionMockUrn(String mockId) {
        return datasetVersionMockFactory.retrieveMock(mockId).getSiemacMetadataStatisticalResource().getUrn();
    }

    protected String getPublicationVersionMockUrn(String mockId) {
        return publicationVersionMockFactory.retrieveMock(mockId).getSiemacMetadataStatisticalResource().getUrn();
    }

    protected List<String> getDatasetsMocksUrns(String... mockIds) {
        List<String> urns = new ArrayList<String>();
        for (String mockId : mockIds) {
            urns.add(getDatasetMockUrn(mockId));
        }
        return urns;
    }

    protected String getQueryMockUrn(String mockId) {
        return queryMockFactory.retrieveMock(mockId).getIdentifiableStatisticalResource().getUrn();
    }

    protected String getQueryVersionMockUrn(String mockId) {
        return queryVersionMockFactory.retrieveMock(mockId).getLifeCycleStatisticalResource().getUrn();
    }

    protected List<String> getQueryMocksUrns(String... mockIds) {
        List<String> urns = new ArrayList<String>();
        for (String mockId : mockIds) {
            urns.add(getQueryMockUrn(mockId));
        }
        return urns;
    }

    private String getMultidatasetMockUrn(String mockId) {
        return multidatasetMockFactory.retrieveMock(mockId).getIdentifiableStatisticalResource().getUrn();
    }

    protected List<String> getMultidatasetMocksUrns(String... mockIds) {
        List<String> urns = new ArrayList<String>();
        for (String mockId : mockIds) {
            urns.add(getMultidatasetMockUrn(mockId));
        }
        return urns;
    }

    class MyResponse extends Response {

        private final int status = Response.Status.OK.getStatusCode();

        @Override
        public Object getEntity() {
            return null;
        }
        @Override
        public int getStatus() {
            return status;
        }
        @Override
        public MultivaluedMap<String, Object> getMetadata() {
            return null;
        }
    };
}
