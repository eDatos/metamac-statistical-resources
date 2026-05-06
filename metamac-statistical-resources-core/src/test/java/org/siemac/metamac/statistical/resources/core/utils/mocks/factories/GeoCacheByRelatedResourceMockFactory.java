package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import java.util.List;

import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockProvider;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.utils.mocks.GeoCacheByRelatedResourceMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

@MockProvider
@SuppressWarnings("unused")
public class GeoCacheByRelatedResourceMockFactory extends StatisticalResourcesMockFactory<GeoCacheByRelatedResource> {

    // for PUBLICATION_07_WITH_TWO_VERSIONS_LAST_ONE_READY_TO_PUBLISHED_NAME
    public static final String                          GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_07                            = "GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_07";
    public static final String                          GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_01_FOR_CACHE                  = "PUBLICATION_VERSION_FOR_GEOGRAPHICAL_CACHE_01";
    public static final String                          GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_02_FOR_CACHE                  = "PUBLICATION_VERSION_FOR_GEOGRAPHICAL_CACHE_02";
    public static final String                          GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_02_FOR_CACHE_PREVIOUS_VERSION = "PUBLICATION_VERSION_FOR_GEOGRAPHICAL_CACHE_02_PREVIOUS_VERSION";

    private static GeoCacheByRelatedResourceMockFactory instance                                                                  = null;

    private GeoCacheByRelatedResourceMockFactory() {
    }

    public static GeoCacheByRelatedResourceMockFactory getInstance() {
        if (instance == null) {
            instance = new GeoCacheByRelatedResourceMockFactory();
        }
        return instance;
    }

    public static GeoCacheByRelatedResourceMock getGeoCacheResourceMockForCollectionResource(PublicationVersion publicationVersion, List<GeoCacheResource> relatedResources) {
        GeoCacheByRelatedResourceMock geoCacheByRelatedResourceMock = getGeoCacheByRelatedResourceMockBasicResourceWithSequence(0, publicationVersion.getSiemacMetadataStatisticalResource(),
                StatisticalResourceTypeEnum.COLLECTION);

        geoCacheByRelatedResourceMock.getRelatedResources().addAll(relatedResources);

        return geoCacheByRelatedResourceMock;
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

        geoCacheByRelatedResourceMock.setHtmlLink(StatisticalResourcesPersistedDoMocks.getHtmlLink(lifeCycleStatisticalResource.getUrn(), statisticalResourceTypeEnum));

        geoCacheByRelatedResourceMock.setIsLastVersion(true);
        geoCacheByRelatedResourceMock.setIsActivated(true);

        geoCacheByRelatedResourceMock.setCreatedBy(StatisticalResourcesDoMocks.mockString(10));
        geoCacheByRelatedResourceMock.setCreatedDate(StatisticalResourcesDoMocks.mockDateTime());
        geoCacheByRelatedResourceMock.setLastUpdatedBy(StatisticalResourcesDoMocks.mockString(10));
        geoCacheByRelatedResourceMock.setLastUpdated(StatisticalResourcesDoMocks.mockDateTime());

        return geoCacheByRelatedResourceMock;
    }
}
