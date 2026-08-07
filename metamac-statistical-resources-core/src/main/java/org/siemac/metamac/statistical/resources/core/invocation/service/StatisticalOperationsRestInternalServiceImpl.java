package org.siemac.metamac.statistical.resources.core.invocation.service;

import static org.siemac.metamac.rest.api.constants.RestApiConstants.DEFAULT_OFFSET;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.MAXIMUM_LIMIT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.api.constants.RestApiConstants;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Instance;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Instances;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionUtils;
import org.siemac.metamac.statistical.resources.core.utils.InternationalStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(StatisticalOperationsRestInternalService.BEAN_ID)
public class StatisticalOperationsRestInternalServiceImpl implements StatisticalOperationsRestInternalService {

    @Autowired
    private MetamacApisLocator restApiLocator;

    // ---------------------------------------------------------------------------------
    // OPERATIONS
    // ---------------------------------------------------------------------------------

    @Override
    public Operation retrieveOperationById(String operationCode) throws MetamacException {
        try {
            return restApiLocator.getStatisticalOperationsRestInternalFacadeV10().retrieveOperationById(operationCode, null);
        } catch (Exception e) {
            throw manageStatisticalOperationsInternalRestException(e);
        }
    }

    @Override
    public Instance retrieveInstanceById(String operationId, String id) throws MetamacException {
        try {
            return restApiLocator.getStatisticalOperationsRestInternalFacadeV10().retrieveInstanceById(operationId, id);
        } catch (Exception e) {
            throw manageStatisticalOperationsInternalRestException(e);
        }
    }

    @Override
    public Operations findOperations(int firstResult, int maxResult, String query) throws MetamacException {
        try {
            String limit = String.valueOf(maxResult);
            String offset = String.valueOf(firstResult);
            String orderBy = null;
            return restApiLocator.getStatisticalOperationsRestInternalFacadeV10().findOperations(query, orderBy, limit, offset, null);
        } catch (Exception e) {
            throw manageStatisticalOperationsInternalRestException(e);
        }
    }

    @Override
    public List<ResourceInternal> findOperations(String query) throws MetamacException {
        try {
            Integer offset = DEFAULT_OFFSET;
            List<ResourceInternal> results = new ArrayList<ResourceInternal>();
            Operations operations = null;
            do {
                operations = findOperations(offset, MAXIMUM_LIMIT, query);
                results.addAll(operations.getOperations());
                offset += operations.getOperations().size(); // next page
            } while (operations.getTotal().intValue() != results.size());
            return results;
        } catch (Exception e) {
            throw manageStatisticalOperationsInternalRestException(e);
        }
    }

    @Override
    public Map<String, InternationalString> getOperationTitles(String query) throws MetamacException {
        List<ResourceInternal> operations = findOperations(query);
        Map<String, InternationalString> operationTitles = new HashMap<String, InternationalString>();
        for (ResourceInternal resource : operations) {
            operationTitles.put(resource.getId(), InternationalStringUtils.getCommonInternationalStringFromRestInternationalString(resource.getName()));
        }
        return operationTitles;
    }

    @Override
    public List<String> findOperationsAsUrnsList(String query) throws MetamacException {
        try {
            List<ResourceInternal> operations = findOperations(query);
            List<String> urns = new ArrayList<String>();
            for (ResourceInternal resource : operations) {
                urns.add(resource.getUrn());
            }
            return urns;
        } catch (Exception e) {
            throw manageStatisticalOperationsInternalRestException(e);
        }
    }

    @Override
    public List<ExternalItem> findOperationsAsExternalItems(int firstResult, int maxResult, String query) throws MetamacException {
        try {
            Operations operations = findOperations(firstResult, maxResult, query);
            List<ExternalItem> externalItems = new ArrayList<ExternalItem>();
            for (ResourceInternal resource : operations.getOperations()) {
                ExternalItem item = new ExternalItem();
                item.setCode(resource.getId());
                item.setManagementAppUrl(resource.getManagementAppLink());
                item.setTitle(apiInternationalStringToappInternationalString(resource.getName()));
                item.setType(TypeExternalArtefactsEnum.valueOf(resource.getKind()));
                item.setUri(resource.getSelfLink().toString());
                item.setUrn(resource.getUrn());
                item.setCodeNested(resource.getNestedId());
            }
            return externalItems;
        } catch (Exception e) {
            throw manageStatisticalOperationsInternalRestException(e);
        }
    }

    private InternationalString apiInternationalStringToappInternationalString(org.siemac.metamac.rest.common.v1_0.domain.InternationalString apiInternationalString) {
        InternationalString internationalString = new InternationalString();
        internationalString.getTexts().addAll(apiLocalisedStringsToAppLocalisedStrings(apiInternationalString.getTexts()));
        return internationalString;
    }

    private Collection<LocalisedString> apiLocalisedStringsToAppLocalisedStrings(List<org.siemac.metamac.rest.common.v1_0.domain.LocalisedString> texts) {
        Set<LocalisedString> appLocalisedStrings = new HashSet<LocalisedString>();
        for (org.siemac.metamac.rest.common.v1_0.domain.LocalisedString apiLocalisedString : texts) {
            LocalisedString item = new LocalisedString();
            item.setLabel(apiLocalisedString.getValue());
            item.setLocale(apiLocalisedString.getValue());
            appLocalisedStrings.add(item);
        }
        return appLocalisedStrings;
    }

    // ---------------------------------------------------------------------------------
    // INSTANCES
    // ---------------------------------------------------------------------------------

    @Override
    public Instances findInstances(int firstResult, int maxResult, String query) throws MetamacException {
        return findInstances(null, firstResult, maxResult, query);
    }

    @Override
    public Instances findInstances(String operationId, int firstResult, int maxResult, String query) throws MetamacException {
        String limit = String.valueOf(maxResult);
        String offset = String.valueOf(firstResult);
        String orderBy = null;

        if (StringUtils.isBlank(operationId)) {
            operationId = RestApiConstants.WILDCARD_ALL;
        }
        return restApiLocator.getStatisticalOperationsRestInternalFacadeV10().findInstances(operationId, query, orderBy, limit, offset, null);
    }

    @Override
    public List<ResourceInternal> findInstances(String query) throws MetamacException {
        try {
            Integer offset = DEFAULT_OFFSET;
            List<ResourceInternal> results = new ArrayList<ResourceInternal>();
            Instances instances = null;
            do {
                instances = findInstances(offset, MAXIMUM_LIMIT, query);
                results.addAll(instances.getInstances());
                offset += instances.getInstances().size(); // next page
            } while (instances.getTotal().intValue() != results.size());
            return results;
        } catch (Exception e) {
            throw manageStatisticalOperationsInternalRestException(e);
        }
    }

    @Override
    public List<String> findInstancesAsUrnsList(String query) throws MetamacException {
        List<ResourceInternal> instances = findInstances(query);
        List<String> ids = new ArrayList<String>();
        for (ResourceInternal resource : instances) {
            ids.add(resource.getUrn());
        }
        return ids;
    }

    private MetamacException manageStatisticalOperationsInternalRestException(Exception e) throws MetamacException {
        return ServiceExceptionUtils.manageMetamacRestException(e, ServiceExceptionParameters.API_STATISTICAL_OPERATIONS_INTERNAL, restApiLocator.getStatisticalOperationsRestInternalFacadeV10());
    }
}
