package org.siemac.metamac.statistical.resources.core.query.serviceimpl.validators;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.base.validators.BaseInvocationValidator;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesValidationUtils;

public class QueryServiceInvocationValidatorImpl extends BaseInvocationValidator {

    // ------------------------------------------------------------------------
    // PUBLIC METHODS FOR SERVICE METHODS
    // ------------------------------------------------------------------------

    public static void checkRetrieveQueryVersionByUrn(String urn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);
    }

    public static void checkRetrieveLatestQueryVersionByQueryUrn(String queryUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(queryUrn, ServiceExceptionParameters.QUERY_URN, exceptions);
    }

    public static void checkRetrieveLatestPublishedQueryVersionByQueryUrn(String queryUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(queryUrn, ServiceExceptionParameters.QUERY_URN, exceptions);
    }

    public static void checkRetrieveQueryVersions(String queryVersionUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(queryVersionUrn, ServiceExceptionParameters.QUERY_VERSION_URN, exceptions);
    }

    public static void checkRetrieveQueryVersions(List<MetamacExceptionItem> exceptions) throws MetamacException {
        // NOTHING
    }

    public static void checkFindQueryVersionsByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // NOTHING
    }

    public static void checkFindQueriesByCondition(List<ConditionalCriteria> conditions, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // NOTHING
    }

    public static void checkUpdateGeographicalCache(QueryVersion queryVersion, List<MetamacExceptionItem> exceptions) {
        checkExistingQueryVersion(queryVersion, exceptions);
    }

    public static void checkUpdateAllGeographicalCache(List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkCreateQueryVersion(QueryVersion queryVersion, ExternalItem statisticalOperation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkNewQueryVersion(queryVersion, exceptions);
    }

    public static void checkStatisticalOperation(ExternalItem statisticalOperation, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(statisticalOperation, ServiceExceptionParameters.STATISTICAL_OPERATION, exceptions);
    }

    public static void checkUpdateQueryVersion(QueryVersion queryVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkExistingQueryVersion(queryVersion, exceptions);
    }

    public static void checkMarkQueryVersionAsDiscontinued(QueryVersion queryVersion, List<MetamacExceptionItem> exceptions) {
        checkExistingQueryVersion(queryVersion, exceptions);
    }

    public static void checkDeleteQueryVersion(String urn, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);
    }

    public static void checkNewQueryVersion(QueryVersion queryVersion, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(queryVersion, ServiceExceptionParameters.QUERY_VERSION, exceptions);
        if (queryVersion == null) {
            return;
        }

        checkNewLifeCycleStatisticalResource(queryVersion.getLifeCycleStatisticalResource(), ServiceExceptionParameters.QUERY_VERSION, exceptions);

        if (queryVersion.getLifeCycleStatisticalResource() != null) {
            // Check code
            StatisticalResourcesValidationUtils.checkMetadataRequired(queryVersion.getLifeCycleStatisticalResource().getCode(), ServiceExceptionParameters.QUERY_VERSION__CODE, exceptions);
            StatisticalResourcesValidationUtils.checkSemanticIdentifierAsMetamacID(queryVersion.getLifeCycleStatisticalResource().getCode(), ServiceExceptionParameters.QUERY_VERSION__CODE,
                    exceptions);
        }
        checkQueryVersion(queryVersion, exceptions);

        // Metadata that must be empty for new entities
        StatisticalResourcesValidationUtils.checkMetadataEmpty(queryVersion.getId(), ServiceExceptionParameters.QUERY_VERSION__ID, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(queryVersion.getVersion(), ServiceExceptionParameters.QUERY_VERSION__VERSION, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(queryVersion.getStatus(), ServiceExceptionParameters.QUERY_VERSION__STATUS, exceptions);
    }

    public static void checkExistingQueryVersion(QueryVersion queryVersion, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(queryVersion, ServiceExceptionParameters.QUERY_VERSION, exceptions);
        if (queryVersion == null) {
            return;
        }

        checkExistingLifeCycleStatisticalResource(queryVersion.getLifeCycleStatisticalResource(), TypeRelatedResourceEnum.QUERY_VERSION, ServiceExceptionParameters.QUERY_VERSION, exceptions);
        checkQueryVersion(queryVersion, exceptions);

        // Metadata that must be filled for existing entities
        StatisticalResourcesValidationUtils.checkMetadataRequired(queryVersion.getId(), ServiceExceptionParameters.QUERY_VERSION__ID, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(queryVersion.getVersion(), ServiceExceptionParameters.QUERY_VERSION__VERSION, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(queryVersion.getStatus(), ServiceExceptionParameters.QUERY_VERSION__STATUS, exceptions);
    }

    private static void checkQueryVersion(QueryVersion queryVersion, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequiredIncompatible(queryVersion.getDataset(), ServiceExceptionParameters.QUERY_VERSION__DATASET, queryVersion.getFixedDatasetVersion(),
                ServiceExceptionParameters.QUERY_VERSION__FIXED_DATASET_VERSION, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(queryVersion.getType(), ServiceExceptionParameters.QUERY_VERSION__TYPE, exceptions);

        if (QueryTypeEnum.LATEST_DATA.equals(queryVersion.getType())) {
            StatisticalResourcesValidationUtils.checkMetadataRequired(queryVersion.getLatestDataNumber(), ServiceExceptionParameters.QUERY_VERSION__LATEST_DATA_NUMBER, exceptions);
            if (queryVersion.getLatestDataNumber() != null && queryVersion.getLatestDataNumber() <= Integer.valueOf(0)) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.QUERY_VERSION__LATEST_DATA_NUMBER));
            }
        } else {
            StatisticalResourcesValidationUtils.checkMetadataEmpty(queryVersion.getLatestDataNumber(), ServiceExceptionParameters.QUERY_VERSION__LATEST_DATA_NUMBER, exceptions);
        }

        checkQueryVersionSelection(queryVersion, exceptions);
    }

    private static void checkQueryVersionSelection(QueryVersion queryVersion, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(queryVersion.getSelection(), ServiceExceptionParameters.QUERY_VERSION__SELECTION, exceptions);

        if (queryVersion.getSelection() != null) {
            boolean mustHaveEmptyTimePeriod = QueryTypeEnum.LATEST_DATA.equals(queryVersion.getType());
            for (QuerySelectionItem selectionItem : queryVersion.getSelection()) {
                if (mustHaveEmptyTimePeriod && selectionItem != null && StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID.equals(selectionItem.getDimension())) {
                    StatisticalResourcesValidationUtils.checkMetadataEmpty(selectionItem.getCodes(), ServiceExceptionParameters.QUERY_VERSION_SELECTION_TIME_PERIOD, exceptions);
                } else {
                    StatisticalResourcesValidationUtils.checkMetadataOptionalIsValid(selectionItem, ServiceExceptionParameters.QUERY_VERSION__SELECTION, exceptions);
                }
            }
        }
    }

    public static void checkCheckQueryCompatibility(QueryVersion queryVersion, DatasetVersion dataset, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(queryVersion, ServiceExceptionParameters.QUERY_VERSION, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(dataset, ServiceExceptionParameters.DATASET_VERSION, exceptions);
    }

    public static void checkFindPurposes(List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }
}
