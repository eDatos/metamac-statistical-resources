package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockProvider;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersion;
import org.siemac.metamac.statistical.resources.core.utils.mocks.GeoCovVarElementCacheDatasetVersionMock;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

@MockProvider
@SuppressWarnings("unused")
public class GeoCovVarElementCacheDatasetVersionFactory extends StatisticalResourcesMockFactory<GeoCovVarElementCacheDatasetVersion> {

    public static final String                                GEO_COV_VAR_ELEMENT_CACHE_01 = "GEO_COV_VAR_ELEMENT_CACHE_DATASET_VERSION_01";
    public static final String                                GEO_COV_VAR_ELEMENT_CACHE_02 = "GEO_COV_VAR_ELEMENT_CACHE_DATASET_VERSION_02";
    public static final String                                VARIABLE_ELEMENT_01          = "variableElement01";

    private static GeoCovVarElementCacheDatasetVersionFactory instance = null;

    private GeoCovVarElementCacheDatasetVersionFactory() {
    }

    public static GeoCovVarElementCacheDatasetVersionFactory getInstance() {
        if (instance == null) {
            instance = new GeoCovVarElementCacheDatasetVersionFactory();
        }
        return instance;
    }
        
    public static GeoCovVarElementCacheDatasetVersion getGeoCovVarElementCacheDatasetVersion01() {
        DatasetVersion dv = StatisticalResourcesPersistedDoMocks.getInstance().mockDatasetVersion();
        GeoCovVarElementCacheDatasetVersionMock geoCovVarElementCacheDatasetVersionMock = getGeoCovVarElementCacheDatasetVersionBase();
        geoCovVarElementCacheDatasetVersionMock.setVariableElement(StatisticalResourcesPersistedDoMocks.mockVariableElementExternalItem("variable01", VARIABLE_ELEMENT_01));

        return geoCovVarElementCacheDatasetVersionMock;
 
    }

    public static GeoCovVarElementCacheDatasetVersion getGeoCovVarElementCacheDatasetVersion02() {
        DatasetVersion dv = StatisticalResourcesPersistedDoMocks.getInstance().mockDatasetVersion();
        GeoCovVarElementCacheDatasetVersionMock geoCovVarElementCacheDatasetVersionMock = getGeoCovVarElementCacheDatasetVersionBase();
        geoCovVarElementCacheDatasetVersionMock.setVariableElement(StatisticalResourcesPersistedDoMocks.mockVariableElementExternalItem("variable02", "variableElement02"));

        return geoCovVarElementCacheDatasetVersionMock;
 
    }
     
    public static GeoCovVarElementCacheDatasetVersionMock getDatasetVersionData(DatasetVersion dv, int seq) {
        GeoCovVarElementCacheDatasetVersionMock geoCovVarElementCacheDatasetVersionMock = new GeoCovVarElementCacheDatasetVersionMock();
        geoCovVarElementCacheDatasetVersionMock.setStatisticalOperationCode("statOper01");
        geoCovVarElementCacheDatasetVersionMock.setSequentialId(seq);
        geoCovVarElementCacheDatasetVersionMock.setCode(dv.getSiemacMetadataStatisticalResource().getCode());
        geoCovVarElementCacheDatasetVersionMock.setIsExternalSource(false);
        geoCovVarElementCacheDatasetVersionMock.setUrn(dv.getSiemacMetadataStatisticalResource().getUrn());
        geoCovVarElementCacheDatasetVersionMock.setHtmlLink(StatisticalResourcesPersistedDoMocks.getHtmlLink(dv.getSiemacMetadataStatisticalResource().getUrn()));
        return geoCovVarElementCacheDatasetVersionMock;
 
    }
    
    public static GeoCovVarElementCacheDatasetVersionMock getGeoCovVarElementCacheDatasetVersionBase() {
        DatasetVersion dv = StatisticalResourcesPersistedDoMocks.getInstance().mockDatasetVersion();
        GeoCovVarElementCacheDatasetVersionMock geoCovVarElementCacheDatasetVersionMock = new GeoCovVarElementCacheDatasetVersionMock();
        geoCovVarElementCacheDatasetVersionMock.setStatisticalOperationCode("statOper01");
        geoCovVarElementCacheDatasetVersionMock.setSequentialId(1);
        geoCovVarElementCacheDatasetVersionMock.setCode(dv.getSiemacMetadataStatisticalResource().getCode());
        geoCovVarElementCacheDatasetVersionMock.setIsExternalSource(false);
        geoCovVarElementCacheDatasetVersionMock.setUrn(dv.getSiemacMetadataStatisticalResource().getUrn());
        geoCovVarElementCacheDatasetVersionMock.setTitle(StatisticalResourcesPersistedDoMocks.mockInternationalStringMetadata(dv.getSiemacMetadataStatisticalResource().getCode(), dv.getSiemacMetadataStatisticalResource().getCode() + "-title"));
        geoCovVarElementCacheDatasetVersionMock.setOperationTitle(StatisticalResourcesPersistedDoMocks.mockInternationalStringMetadata("statOper01", "statOper01-title"));
        geoCovVarElementCacheDatasetVersionMock.setHtmlLink(StatisticalResourcesPersistedDoMocks.getHtmlLink(dv.getSiemacMetadataStatisticalResource().getUrn()));
        geoCovVarElementCacheDatasetVersionMock.setIsActivated(true);
        return geoCovVarElementCacheDatasetVersionMock;
 
    }
 
    

}
