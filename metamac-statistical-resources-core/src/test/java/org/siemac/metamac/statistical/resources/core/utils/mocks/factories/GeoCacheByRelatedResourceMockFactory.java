package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockProvider;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.utils.mocks.GeoCacheByRelatedResourceMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

@MockProvider
@SuppressWarnings("unused")
public class GeoCacheByRelatedResourceMockFactory extends StatisticalResourcesMockFactory<GeoCacheByRelatedResource> {

    public static final String                      GEO_CACHE_BY_RELATED_RESOURCE_01 = "GEO_CACHE_BY_RELATED_RESOURCE_01";
    public static final String                      GEO_CACHE_BY_RELATED_RESOURCE_02 = "GEO_CACHE_BY_RELATED_RESOURCE_02";

    private static GeoCacheByRelatedResourceMockFactory instance                         = null;

    private GeoCacheByRelatedResourceMockFactory() {
    }

    public static GeoCacheByRelatedResourceMockFactory getInstance() {
        if (instance == null) {
            instance = new GeoCacheByRelatedResourceMockFactory();
        }
        return instance;
    }

    public static GeoCacheByRelatedResource getGeoCacheByRelatedResource01() {
        GeoCacheByRelatedResourceMock geoCacheByRelatedResourceMock = getGeoCacheResourceMockForCollectionResourceWithSequence(1);
        GeoCacheByRelatedResource geoCacheByRelatedResource = mockGeoCacheByRelatedResource(geoCacheByRelatedResourceMock);

        return geoCacheByRelatedResource;
    }

    private static GeoCacheByRelatedResourceMock getGeoCacheResourceMockForCollectionResourceWithSequence(int sequenceId) {
        PublicationVersion dv = PublicationVersionMockFactory.getPublicationVersionMock(PublicationVersionMockFactory.PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME);
        return getGeoCacheByRelatedResourceMockBasicResourceWithSequence(sequenceId, dv.getSiemacMetadataStatisticalResource(), StatisticalResourceTypeEnum.COLLECTION);
    }

    private static GeoCacheByRelatedResourceMock getGeoCacheByRelatedResourceMockBasicResourceWithSequence(int sequenceId, LifeCycleStatisticalResource lifeCycleStatisticalResource,
            StatisticalResourceTypeEnum statisticalResourceTypeEnum) {
        GeoCacheByRelatedResourceMock geoCacheByRelatedResourceMock = new GeoCacheByRelatedResourceMock();
        geoCacheByRelatedResourceMock.setSequentialId(sequenceId);

        geoCacheByRelatedResourceMock.setType(statisticalResourceTypeEnum.getName());
        geoCacheByRelatedResourceMock.setStatisticalOperationCode(lifeCycleStatisticalResource.getStatisticalOperation().getCode());

        geoCacheByRelatedResourceMock.setCode(lifeCycleStatisticalResource.getCode());
        geoCacheByRelatedResourceMock.setIsExternalSource(false);
        geoCacheByRelatedResourceMock.setUrn(lifeCycleStatisticalResource.getUrn());
        // TODO EDATOS-4587 Ver si se puede grabar.
        // geoCacheResourceMock.setTitle(StatisticalResourcesDoMocks.mockInternationalStringMetadata(siemacMetadataStatisticalResource.getCode(), "title"));
        geoCacheByRelatedResourceMock.setHtmlLink(StatisticalResourcesPersistedDoMocks.getHtmlLink(lifeCycleStatisticalResource.getUrn(), statisticalResourceTypeEnum));

        return geoCacheByRelatedResourceMock;
    }

    public static GeoCacheByRelatedResource mockGeoCacheByRelatedResource(GeoCacheByRelatedResourceMock geoCacheByRelatedResourceMock) {

        geoCacheByRelatedResourceMock.setIsActivated(true);

        geoCacheByRelatedResourceMock.setCreatedBy(StatisticalResourcesDoMocks.mockString(10));
        geoCacheByRelatedResourceMock.setCreatedDate(StatisticalResourcesDoMocks.mockDateTime());
        geoCacheByRelatedResourceMock.setLastUpdatedBy(StatisticalResourcesDoMocks.mockString(10));
        geoCacheByRelatedResourceMock.setLastUpdated(StatisticalResourcesDoMocks.mockDateTime());
        return geoCacheByRelatedResourceMock;

    }

}
