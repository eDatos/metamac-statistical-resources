package org.siemac.metamac.statistical.resources.core.utils.mocks;

import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

public class GeoCacheResourceMock extends GeoCacheResource {

    private static final long serialVersionUID = -289909094976654068L;

    private Integer           sequentialId;

    public GeoCacheResourceMock() {

    }

    public static GeoCacheResourceMock buildBasicSingleVersionWithSequence(int sequenceId) {
        GeoCacheResourceMock instance = new GeoCacheResourceMock();
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
