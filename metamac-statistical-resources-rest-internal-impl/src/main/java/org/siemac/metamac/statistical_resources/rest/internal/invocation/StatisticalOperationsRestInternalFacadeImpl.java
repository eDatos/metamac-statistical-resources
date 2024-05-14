package org.siemac.metamac.statistical_resources.rest.internal.invocation;

import static org.siemac.metamac.rest.api.constants.RestApiConstants.DEFAULT_OFFSET;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.MAXIMUM_LIMIT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Instance;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ResourceInternal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(StatisticalOperationsRestInternalFacade.BEAN_ID)
public class StatisticalOperationsRestInternalFacadeImpl implements StatisticalOperationsRestInternalFacade {

    private final Logger       logger = LoggerFactory.getLogger(StatisticalOperationsRestInternalFacadeImpl.class);

    @Autowired
    private MetamacApisLocator restApiLocator;

    @Override
    public Operation retrieveOperation(String operationCode) {
        return restApiLocator.getStatisticalOperationsRestInternalFacadeV10().retrieveOperationById(operationCode);
    }

    @Override
    public Instance retrieveInstanceById(String operationId, String instanceId) {
        return restApiLocator.getStatisticalOperationsRestInternalFacadeV10().retrieveInstanceById(operationId, instanceId);
    }

    private Operations findOperations(int firstResult, int maxResult, String query) throws RestException {
        try {
            String limit = String.valueOf(maxResult);
            String offset = String.valueOf(firstResult);
            String orderBy = null;
            return restApiLocator.getStatisticalOperationsRestInternalFacadeV10().findOperations(query, orderBy, limit, offset);
        } catch (Exception e) {
            throw toRestException(e);
        }
    }

    private List<ResourceInternal> findOperations(String query) throws RestException {
        try {
            Integer offset = DEFAULT_OFFSET;
            List<ResourceInternal> results = new ArrayList<>();
            Operations operations = null;
            do {
                operations = findOperations(offset, MAXIMUM_LIMIT, query);
                results.addAll(operations.getOperations());
                offset += operations.getOperations().size(); // next page
            } while (operations.getTotal().intValue() != results.size());
            return results;
        } catch (Exception e) {
            throw toRestException(e);
        }
    }

    @Override
    public Map<String, org.siemac.metamac.rest.common.v1_0.domain.InternationalString> getOperationTitles(String query) throws RestException {
        List<ResourceInternal> operations = findOperations(query);
        Map<String, org.siemac.metamac.rest.common.v1_0.domain.InternationalString> operationTitles = new HashMap<>();
        for (Resource resource : operations) {
            operationTitles.put(resource.getId(), resource.getName());
        }
        return operationTitles;
    }

    private RestException toRestException(Exception e) {
        logger.error("Error in access internal api of statistical-operations in statistical-resources internal api", e);
        return RestExceptionUtils.toRestException(e, WebClient.client(restApiLocator.getStatisticalOperationsRestInternalFacadeV10()));
    }
}
