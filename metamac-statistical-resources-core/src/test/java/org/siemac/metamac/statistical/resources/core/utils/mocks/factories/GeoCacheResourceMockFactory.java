package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockProvider;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheTerritoriesByGeoCacheResource;
import org.siemac.metamac.statistical.resources.core.utils.mocks.GeoCacheResourceMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

@MockProvider
@SuppressWarnings("unused")
public class GeoCacheResourceMockFactory extends StatisticalResourcesMockFactory<GeoCacheResource> {

    public static final String                 GEO_COV_VAR_ELEMENT_CACHE_01 = "GEO_COV_VAR_ELEMENT_CACHE_DATASET_VERSION_01";
    public static final String                 GEO_COV_VAR_ELEMENT_CACHE_02 = "GEO_COV_VAR_ELEMENT_CACHE_DATASET_VERSION_02";
    public static final String                 VARIABLE_ELEMENT_01          = "variableElement01";

    private static GeoCacheResourceMockFactory instance                     = null;

    private GeoCacheResourceMockFactory() {
    }

    public static GeoCacheResourceMockFactory getInstance() {
        if (instance == null) {
            instance = new GeoCacheResourceMockFactory();
        }
        return instance;
    }

    public static GeoCacheResource getGeoCovVarElementCacheDatasetVersion01() {
        GeoCacheResourceMock geoCacheResourceMock = getGeoCovVarElementCacheDatasetVersionBase(StatisticalResourceTypeEnum.DATASET);
        geoCacheResourceMock.getTerritories().clear();
        geoCacheResourceMock.addTerritory(getGeoCacheTerritoriesByGeoCacheResourceMock01());
        return geoCacheResourceMock;
    }

    private static GeoCacheTerritoriesByGeoCacheResource getGeoCacheTerritoriesByGeoCacheResourceMock01() {
        GeoCacheTerritoriesByGeoCacheResource gGeoCacheTerritoriesByGeoCacheResource = new GeoCacheTerritoriesByGeoCacheResource();
        gGeoCacheTerritoriesByGeoCacheResource.setVariableElement(StatisticalResourcesPersistedDoMocks.mockVariableElementExternalItem("variable01", VARIABLE_ELEMENT_01));
        return gGeoCacheTerritoriesByGeoCacheResource;
    }

    private static GeoCacheTerritoriesByGeoCacheResource getGeoCacheTerritoriesByGeoCacheResourceMock02() {
        GeoCacheTerritoriesByGeoCacheResource gGeoCacheTerritoriesByGeoCacheResource = new GeoCacheTerritoriesByGeoCacheResource();
        gGeoCacheTerritoriesByGeoCacheResource.setVariableElement(StatisticalResourcesPersistedDoMocks.mockVariableElementExternalItem("variable02", "variableElement02"));
        return gGeoCacheTerritoriesByGeoCacheResource;
    }

    public static GeoCacheResource getGeoCovVarElementCacheDatasetVersion02() {
        GeoCacheResource geoCacheResource = getGeoCovVarElementCacheDatasetVersionBase(StatisticalResourceTypeEnum.DATASET);
        geoCacheResource.addTerritory(getGeoCacheTerritoriesByGeoCacheResourceMock02());
        registerGeoCacheResourceMock(GEO_COV_VAR_ELEMENT_CACHE_02, geoCacheResource);
        return geoCacheResource;

    }

    public static GeoCacheResourceMock getDatasetVersionData(DatasetVersion dv, int seq) {
        GeoCacheResourceMock GeoCacheResourceMock = new GeoCacheResourceMock();
        GeoCacheResourceMock.setStatisticalOperationCode("statOper01");
        GeoCacheResourceMock.setSequentialId(seq);
        GeoCacheResourceMock.setCode(dv.getSiemacMetadataStatisticalResource().getCode());
        GeoCacheResourceMock.setIsExternalSource(false);
        GeoCacheResourceMock.setUrn(dv.getSiemacMetadataStatisticalResource().getUrn());
        GeoCacheResourceMock.setHtmlLink(StatisticalResourcesPersistedDoMocks.getHtmlLink(dv.getSiemacMetadataStatisticalResource().getUrn()));
        return GeoCacheResourceMock;

    }

    public static GeoCacheResourceMock getGeoCovVarElementCacheDatasetVersionBase(StatisticalResourceTypeEnum statisticalResourceTypeEnum) {
        DatasetVersion dv = StatisticalResourcesPersistedDoMocks.getInstance().mockDatasetVersion();
        GeoCacheResourceMock geoCacheResourceMock = new GeoCacheResourceMock();

        geoCacheResourceMock.setStatisticalOperationCode("statOper01");
        geoCacheResourceMock.setSequentialId(1);
        geoCacheResourceMock.setCode(dv.getSiemacMetadataStatisticalResource().getCode());
        geoCacheResourceMock.setIsExternalSource(false);
        geoCacheResourceMock.setUrn(dv.getSiemacMetadataStatisticalResource().getUrn());
        geoCacheResourceMock.setTitle(StatisticalResourcesPersistedDoMocks.mockInternationalStringMetadata(dv.getSiemacMetadataStatisticalResource().getCode(),
                dv.getSiemacMetadataStatisticalResource().getCode() + "-title"));
        geoCacheResourceMock.setType(statisticalResourceTypeEnum.getName());
        geoCacheResourceMock.setHtmlLink(StatisticalResourcesPersistedDoMocks.getHtmlLink(dv.getSiemacMetadataStatisticalResource().getUrn()));
        geoCacheResourceMock.setIsActivated(true);

        geoCacheResourceMock.setCreatedBy(StatisticalResourcesDoMocks.mockString(10));
        geoCacheResourceMock.setCreatedDate(StatisticalResourcesDoMocks.mockDateTime());
        geoCacheResourceMock.setLastUpdatedBy(StatisticalResourcesDoMocks.mockString(10));
        geoCacheResourceMock.setLastUpdated(StatisticalResourcesDoMocks.mockDateTime());
        return geoCacheResourceMock;

    }

}
