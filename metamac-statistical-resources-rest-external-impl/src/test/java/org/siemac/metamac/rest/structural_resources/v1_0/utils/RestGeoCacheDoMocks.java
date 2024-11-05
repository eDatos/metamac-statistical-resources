package org.siemac.metamac.rest.structural_resources.v1_0.utils;

import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.COLLECTION_1_CODE;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.DATASET_1_CODE;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.DATASET_2_CODE;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.QUERY_1_CODE;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.QUERY_2_CODE;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.VERSION_1;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.mocks.GeoCacheResourceMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

public class RestGeoCacheDoMocks {

    private static final String VARIABLE_ELEMENT_1 = "variableElement01";
    private static final String VARIABLE_ELEMENT_2 = "variableElement02";
    private static final String VARIABLE_ELEMENT_3 = "variableElement03";
    private static final String VARIABLE_ELEMENT_4 = "variableElement04";

    private RestDoMocks         restDoMocks;

    public RestGeoCacheDoMocks(RestDoMocks restDoMocks) {
        this.restDoMocks = restDoMocks;
    }
    public GeoCacheByRelatedResource mockCacheByRelatedResources(String agencyId, String resourceId, String versionId, StatisticalResourceTypeEnum statisticalResourceTypeEnum) {

        if (StatisticalResourceTypeEnum.COLLECTION.equals(statisticalResourceTypeEnum)) {
            return mockCacheByRelatedResourcesForCollections(agencyId, resourceId, versionId, statisticalResourceTypeEnum);
        }
        return null;
    }

    private GeoCacheByRelatedResource mockCacheByRelatedResourcesForCollections(String agencyId, String resourceId, String versionId, StatisticalResourceTypeEnum statisticalResourceTypeEnum) {
        PublicationVersion dv = restDoMocks.mockPublicationVersion(agencyId, resourceId, versionId);

        if (resourceId.equals(COLLECTION_1_CODE)) {

            dv.removeAllHasPart();
            dv.addHasPart(restDoMocks.mockDatasetRelatedResource(AGENCY_1, DATASET_1_CODE, VERSION_1));
            dv.addHasPart(restDoMocks.mockQueryRelatedResource(AGENCY_1, QUERY_1_CODE, VERSION_1));

            GeoCacheByRelatedResource geoCacheByRelatedResource = mockGeoCacheByRelatedResource(dv.getSiemacMetadataStatisticalResource(), statisticalResourceTypeEnum);

            geoCacheByRelatedResource
                    .addRelatedResource(mockResources(AGENCY_1, DATASET_1_CODE, VERSION_1, Arrays.asList(VARIABLE_ELEMENT_1, VARIABLE_ELEMENT_2), true, StatisticalResourceTypeEnum.DATASET));
            geoCacheByRelatedResource.addRelatedResource(mockResources(AGENCY_1, QUERY_1_CODE, VERSION_1, Arrays.asList(VARIABLE_ELEMENT_3), true, StatisticalResourceTypeEnum.QUERY));

            return geoCacheByRelatedResource;
        } else {
            dv.removeAllHasPart();
            dv.addHasPart(restDoMocks.mockDatasetRelatedResource(AGENCY_1, DATASET_2_CODE, VERSION_1));
            dv.addHasPart(restDoMocks.mockQueryRelatedResource(AGENCY_1, QUERY_2_CODE, VERSION_1));

            GeoCacheByRelatedResource geoCacheByRelatedResource = mockGeoCacheByRelatedResource(dv.getSiemacMetadataStatisticalResource(), statisticalResourceTypeEnum);

            geoCacheByRelatedResource
                    .addRelatedResource(mockResources(AGENCY_1, DATASET_2_CODE, VERSION_1, Arrays.asList(VARIABLE_ELEMENT_1, VARIABLE_ELEMENT_4), true, StatisticalResourceTypeEnum.DATASET));
            geoCacheByRelatedResource.addRelatedResource(mockResources(AGENCY_1, QUERY_2_CODE, VERSION_1, Arrays.asList(VARIABLE_ELEMENT_4), true, StatisticalResourceTypeEnum.QUERY));
            return geoCacheByRelatedResource;
        }

    }

    public GeoCacheResource mockResources(String agencyId, String resourceId, String versionId, List<String> variableElementsId, Boolean isLastVersion,
            StatisticalResourceTypeEnum statisticalResourceTypeEnum) {

        switch (statisticalResourceTypeEnum) {
            case DATASET:
                return mockGeoCacheResourceForDataset(agencyId, resourceId, versionId, variableElementsId, isLastVersion);
            case QUERY:
                return mockGeoCacheResourceForQuery(agencyId, resourceId, versionId, variableElementsId, isLastVersion);
            default:
                return null;
        }
    }

    private GeoCacheResource mockGeoCacheResourceForDataset(String agencyId, String resourceId, String versionId, List<String> variableElementsId, Boolean isLastVersion) {
        DatasetVersion dv = restDoMocks.mockDatasetVersion(agencyId, resourceId, versionId);

        return mockGeoCacheResource(dv.getSiemacMetadataStatisticalResource(), StatisticalResourceTypeEnum.DATASET, variableElementsId, isLastVersion);

    }

    private GeoCacheResource mockGeoCacheResourceForQuery(String agencyId, String resourceId, String versionId, List<String> variableElementsId, Boolean isLastVersion) {
        QueryVersion dv = restDoMocks.mockQueryVersion(agencyId, resourceId, versionId);

        return mockGeoCacheResource(dv.getLifeCycleStatisticalResource(), StatisticalResourceTypeEnum.QUERY, variableElementsId, isLastVersion);

    }

    public static GeoCacheByRelatedResource mockGeoCacheByRelatedResource(LifeCycleStatisticalResource lifeCycleStatisticalResource, StatisticalResourceTypeEnum statisticalResourceTypeEnum) {
        GeoCacheByRelatedResource geoCacheByRelatedResource = new GeoCacheByRelatedResource();

        geoCacheByRelatedResource.setType(statisticalResourceTypeEnum.getName());
        geoCacheByRelatedResource.setOperationCode(lifeCycleStatisticalResource.getCode());
        geoCacheByRelatedResource.setOperationUrn(lifeCycleStatisticalResource.getStatisticalOperation().getUrn());

        geoCacheByRelatedResource.setCode(lifeCycleStatisticalResource.getCode());
        geoCacheByRelatedResource.setIsExternalSource(false);
        geoCacheByRelatedResource.setUrn(lifeCycleStatisticalResource.getUrn());
        geoCacheByRelatedResource.setTitle(lifeCycleStatisticalResource.getTitle());
        geoCacheByRelatedResource.setHtmlLink(StatisticalResourcesPersistedDoMocks.getHtmlLink(lifeCycleStatisticalResource.getUrn(), statisticalResourceTypeEnum));

        geoCacheByRelatedResource.setIsActivated(true);
        geoCacheByRelatedResource.setIsLastVersion(true);

        geoCacheByRelatedResource.setCreatedBy(StatisticalResourcesDoMocks.mockString(10));
        geoCacheByRelatedResource.setCreatedDate(StatisticalResourcesDoMocks.mockDateTime());
        geoCacheByRelatedResource.setLastUpdatedBy(StatisticalResourcesDoMocks.mockString(10));
        geoCacheByRelatedResource.setLastUpdated(StatisticalResourcesDoMocks.mockDateTime());

        return geoCacheByRelatedResource;

    }
    public static GeoCacheResource mockGeoCacheResource(LifeCycleStatisticalResource lifeCycleStatisticalResource, StatisticalResourceTypeEnum statisticalResourceTypeEnum,
            List<String> variableElementsId, Boolean isLastVersion) {

        GeoCacheResource geoCacheResource = new GeoCacheResourceMock();
        geoCacheResource.setType(statisticalResourceTypeEnum.getName());
        geoCacheResource.setOperationCode(lifeCycleStatisticalResource.getStatisticalOperation().getCode());
        geoCacheResource.setOperationUrn(lifeCycleStatisticalResource.getStatisticalOperation().getUrn());

        geoCacheResource.setCode(lifeCycleStatisticalResource.getCode());
        geoCacheResource.setIsExternalSource(false);
        geoCacheResource.setUrn(lifeCycleStatisticalResource.getUrn());
        geoCacheResource.setTitle(lifeCycleStatisticalResource.getTitle());
        geoCacheResource.setHtmlLink(StatisticalResourcesPersistedDoMocks.getHtmlLink(lifeCycleStatisticalResource.getUrn(), statisticalResourceTypeEnum));

        geoCacheResource.setIsActivated(true);
        geoCacheResource.setIsLastVersion(isLastVersion);

        geoCacheResource.setCreatedBy(StatisticalResourcesDoMocks.mockString(10));
        geoCacheResource.setCreatedDate(StatisticalResourcesDoMocks.mockDateTime());
        geoCacheResource.setLastUpdatedBy(StatisticalResourcesDoMocks.mockString(10));
        geoCacheResource.setLastUpdated(StatisticalResourcesDoMocks.mockDateTime());

        geoCacheResource.getTerritories().clear();
        for (String variableElement : variableElementsId) {
            geoCacheResource.addTerritory(StatisticalResourcesPersistedDoMocks.mockVariableElementExternalItem("variableGeo", variableElement));
        }

        return geoCacheResource;

    }
}
