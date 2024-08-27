package org.siemac.metamac.statistical.resources.core.utils.mocks;

import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

public class GeoCovVarElementCacheDatasetVersionMock extends GeoCacheResource {

    private static final long serialVersionUID = -289909094976654068L;

    private Integer           sequentialId;

    public GeoCovVarElementCacheDatasetVersionMock() {
    }

    public static GeoCovVarElementCacheDatasetVersionMock buildBasicSingleVersionWithSequence(int sequenceId) {
        GeoCovVarElementCacheDatasetVersionMock instance = new GeoCovVarElementCacheDatasetVersionMock();

        // TODO EDATOS-4587 VER SI AÑADIR TABLAS AUXILIARES CON ELEMENTO VARIABLE

        instance.setSequentialId(sequenceId);
        instance.setIsLastVersion(true);
        return instance;
    }

    public Integer getSequentialId() {
        return sequentialId;
    }

    public void setSequentialId(Integer sequentialId) {
        this.sequentialId = sequentialId;
    }

    public void setStatisticalOperationCode(String operationCode) {
        ExternalItem operation = StatisticalResourcesPersistedDoMocks.mockStatisticalOperationExternalItem(operationCode);
        this.setOperationCode(operation.getCode());
        this.setOperationUrn(operation.getUrn());
    }
}
