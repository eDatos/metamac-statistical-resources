package org.siemac.metamac.statistical_resources.rest.internal.invocation;

import java.util.Map;

import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Instance;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;

public interface StatisticalOperationsRestInternalFacade {

    public static final String BEAN_ID = "statisticalOperationsInternalFacade";

    public Operation retrieveOperation(String operationCode);
    public Instance retrieveInstanceById(String operationId, String instanceId);
    public Map<String, org.siemac.metamac.rest.common.v1_0.domain.InternationalString> getOperationTitles(String query) throws RestException;
}
