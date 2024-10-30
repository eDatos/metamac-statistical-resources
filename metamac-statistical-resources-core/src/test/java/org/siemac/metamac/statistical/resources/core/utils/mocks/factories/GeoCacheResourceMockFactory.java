package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockProvider;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.mocks.GeoCacheResourceMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

@MockProvider
@SuppressWarnings("unused")
public class GeoCacheResourceMockFactory extends StatisticalResourcesMockFactory<GeoCacheResource> {

    public static final String                 GEO_COV_VAR_ELEMENT_CACHE_01                = "GEO_COV_VAR_ELEMENT_CACHE_DATASET_VERSION_01";
    public static final String                 GEO_COV_VAR_ELEMENT_CACHE_02                = "GEO_COV_VAR_ELEMENT_CACHE_DATASET_VERSION_02";
    public static final String                 GEO_COV_VAR_ELEMENT_CACHE_NOT_ACTIVATED_03  = "GEO_COV_VAR_ELEMENT_CACHE_DATASET_VERSION_NOT_ACTIVATED_03";
    public static final String                 GEO_COV_VAR_ELEMENT_CACHE_QUERY_RESOURCE_01 = "GEO_COV_VAR_ELEMENT_CACHE_QUERY_VERSION_01";

    public static final String                 VARIABLE_ELEMENT_01                         = "variableElement01";

    private static GeoCacheResourceMockFactory instance                                    = null;

    private GeoCacheResourceMockFactory() {
    }

    public static GeoCacheResourceMockFactory getInstance() {
        if (instance == null) {
            instance = new GeoCacheResourceMockFactory();
        }
        return instance;
    }

    public static GeoCacheResource getGeoCovVarElementCacheDatasetVersion01() {
        GeoCacheResourceMock geoCacheResourceMock = getGeoCacheResourceMockForDatasetResourceWithSequence(1);
        GeoCacheResource geoCacheResource = mockGeoCacheResource(geoCacheResourceMock);
        geoCacheResource.getTerritories().clear();
        geoCacheResource.addTerritory(StatisticalResourcesPersistedDoMocks.mockVariableElementExternalItem("variable01", VARIABLE_ELEMENT_01));
        return geoCacheResource;
    }

    public static GeoCacheResource getGeoCovVarElementCacheDatasetVersion02() {
        GeoCacheResourceMock geoCacheResourceMock = getGeoCacheResourceMockForDatasetResourceWithSequence(2);
        GeoCacheResource geoCacheResource = mockGeoCacheResource(geoCacheResourceMock);
        geoCacheResourceMock.getTerritories().clear();
        geoCacheResourceMock.addTerritory(StatisticalResourcesPersistedDoMocks.mockVariableElementExternalItem("variable02", "variableElement02"));
        return geoCacheResourceMock;

    }

    public static GeoCacheResource getGeoCovVarElementCacheDatasetVersionNotActivated03() {
        GeoCacheResourceMock geoCacheResourceMock = getGeoCacheResourceMockForDatasetResourceWithSequence(3);
        GeoCacheResource geoCacheResource = mockGeoCacheResource(geoCacheResourceMock);
        geoCacheResourceMock.getTerritories().clear();
        geoCacheResourceMock.addTerritory(StatisticalResourcesPersistedDoMocks.mockVariableElementExternalItem("variable03", "variableElement03"));
        geoCacheResourceMock.setIsActivated(false);
        return geoCacheResourceMock;

    }

    private static GeoCacheResourceMock getGeoCacheResourceMockForDatasetResourceWithSequence(int sequenceId) {
        DatasetVersion dv = DatasetVersionMockFactory.createDatasetVersionInStatusWithGeneratedDatasource(sequenceId, ProcStatusEnum.PUBLISHED);
        return getGeoCacheResourceMockBasicResourceWithSequence(sequenceId, dv.getSiemacMetadataStatisticalResource(), StatisticalResourceTypeEnum.DATASET);
    }

    private static GeoCacheResourceMock getGeoCacheResourceMockForQueryResourceWithSequence(int sequenceId) {
        QueryVersion dv = QueryVersionMockFactory.getQueryVersionForCacheResource();
        return getGeoCacheResourceMockBasicResourceWithSequence(sequenceId, dv.getLifeCycleStatisticalResource(), StatisticalResourceTypeEnum.QUERY);
    }

    public static GeoCacheResource getGeoCovVarElementCacheQueryVersion01() {
        GeoCacheResourceMock geoCacheResourceMock = getGeoCacheResourceMockForQueryResourceWithSequence(1);
        GeoCacheResource geoCacheResource = mockGeoCacheResource(geoCacheResourceMock);
        geoCacheResource.getTerritories().clear();
        geoCacheResource.addTerritory(StatisticalResourcesPersistedDoMocks.mockVariableElementExternalItem("variable01", VARIABLE_ELEMENT_01));
        return geoCacheResource;
    }

    private static GeoCacheResourceMock getGeoCacheResourceMockBasicResourceWithSequence(int sequenceId, LifeCycleStatisticalResource lifeCycleStatisticalResource,
            StatisticalResourceTypeEnum statisticalResourceTypeEnum) {
        GeoCacheResourceMock geoCacheResourceMock = new GeoCacheResourceMock();
        geoCacheResourceMock.setSequentialId(sequenceId);

        geoCacheResourceMock.setType(statisticalResourceTypeEnum.getName());
        geoCacheResourceMock.setStatisticalOperationCode(lifeCycleStatisticalResource.getStatisticalOperation().getCode());

        geoCacheResourceMock.setCode(lifeCycleStatisticalResource.getCode());
        geoCacheResourceMock.setIsExternalSource(false);
        geoCacheResourceMock.setUrn(lifeCycleStatisticalResource.getUrn());
        // TODO EDATOS-4587 Ver si se puede grabar.
        // geoCacheResourceMock.setTitle(StatisticalResourcesDoMocks.mockInternationalStringMetadata(siemacMetadataStatisticalResource.getCode(), "title"));
        geoCacheResourceMock.setHtmlLink(StatisticalResourcesPersistedDoMocks.getHtmlLink(lifeCycleStatisticalResource.getUrn(), statisticalResourceTypeEnum));

        return geoCacheResourceMock;
    }

    public static GeoCacheResourceMock getDatasetVersionData(DatasetVersion dv, int seq) {
        GeoCacheResourceMock GeoCacheResourceMock = new GeoCacheResourceMock();
        GeoCacheResourceMock.setStatisticalOperationCode("statOper01");
        GeoCacheResourceMock.setSequentialId(seq);
        GeoCacheResourceMock.setCode(dv.getSiemacMetadataStatisticalResource().getCode());
        GeoCacheResourceMock.setIsExternalSource(false);
        GeoCacheResourceMock.setUrn(dv.getSiemacMetadataStatisticalResource().getUrn());
        GeoCacheResourceMock.setHtmlLink(StatisticalResourcesPersistedDoMocks.getHtmlLink(dv.getSiemacMetadataStatisticalResource().getUrn(), StatisticalResourceTypeEnum.DATASET));
        return GeoCacheResourceMock;

    }

    public static GeoCacheResource mockGeoCacheResource(GeoCacheResourceMock geoCacheResourceMock) {

        geoCacheResourceMock.setIsActivated(true);

        geoCacheResourceMock.setCreatedBy(StatisticalResourcesDoMocks.mockString(10));
        geoCacheResourceMock.setCreatedDate(StatisticalResourcesDoMocks.mockDateTime());
        geoCacheResourceMock.setLastUpdatedBy(StatisticalResourcesDoMocks.mockString(10));
        geoCacheResourceMock.setLastUpdated(StatisticalResourcesDoMocks.mockDateTime());
        return geoCacheResourceMock;

    }

}
