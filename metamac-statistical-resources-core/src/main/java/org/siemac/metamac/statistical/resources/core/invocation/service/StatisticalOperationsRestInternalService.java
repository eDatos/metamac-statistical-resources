package org.siemac.metamac.statistical.resources.core.invocation.service;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Instance;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Instances;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;

public interface StatisticalOperationsRestInternalService {

    public static final String BEAN_ID = "statisticalOperationsRestInternalService";

    public Operation retrieveOperationById(String operationCode) throws MetamacException;
    public Instance retrieveInstanceById(String operationId, String id) throws MetamacException;

    public List<ResourceInternal> findOperations(String query) throws MetamacException;
    public Operations findOperations(int firstResult, int maxResult, String query) throws MetamacException;
    public List<String> findOperationsAsUrnsList(String query) throws MetamacException;
    public List<ExternalItem> findOperationsAsExternalItems(int firstResult, int maxResult, String query) throws MetamacException;

    public List<ResourceInternal> findInstances(String query) throws MetamacException;
    public Instances findInstances(int firstResult, int maxResult, String query) throws MetamacException;
    public Instances findInstances(String operationId, int firstResult, int maxResult, String query) throws MetamacException;
    public List<String> findInstancesAsUrnsList(String query) throws MetamacException;
    public Map<String, InternationalString> getOperationTitles(String query) throws MetamacException;

}
