package org.siemac.metamac.statistical.resources.core.utils.mocks;

import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

public class GeoCacheByRelatedResourceMock extends GeoCacheByRelatedResource {

    private static final long serialVersionUID = -289909094976654068L;

    private Integer           sequentialId;

    public GeoCacheByRelatedResourceMock() {

    }

    public static GeoCacheByRelatedResourceMock buildBasicSingleVersionWithSequence(int sequenceId) {
        GeoCacheByRelatedResourceMock instance = new GeoCacheByRelatedResourceMock();
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
