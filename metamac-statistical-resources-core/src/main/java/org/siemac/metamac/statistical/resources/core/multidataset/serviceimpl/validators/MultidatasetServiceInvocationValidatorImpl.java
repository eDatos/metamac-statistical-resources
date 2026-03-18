package org.siemac.metamac.statistical.resources.core.multidataset.serviceimpl.validators;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.base.validators.BaseInvocationValidator;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetCube;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesValidationUtils;

public class MultidatasetServiceInvocationValidatorImpl extends BaseInvocationValidator {

    public static void checkCreateMultidatasetVersion(MultidatasetVersion multidatasetVersion, ExternalItem statisticalOperation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkNewMultidatasetVersion(multidatasetVersion, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(statisticalOperation, ServiceExceptionParameters.STATISTICAL_OPERATION, exceptions);
    }

    public static void checkUpdateMultidatasetVersion(MultidatasetVersion multidatasetVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkExistingMultidatasetVersion(multidatasetVersion, exceptions);
    }

    public static void checkRetrieveMultidatasetVersionByUrn(String multidatasetVersionUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(multidatasetVersionUrn, ServiceExceptionParameters.MULTIDATASET_VERSION_URN, exceptions);
    }

    public static void checkRetrieveLatestMultidatasetVersionByMultidatasetUrn(String multidatasetUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(multidatasetUrn, ServiceExceptionParameters.MULTIDATASET_URN, exceptions);
    }

    public static void checkRetrieveLatestPublishedMultidatasetVersionByMultidatasetUrn(String multidatasetUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(multidatasetUrn, ServiceExceptionParameters.MULTIDATASET_URN, exceptions);
    }

    public static void checkRetrieveMultidatasetVersions(String multidatasetVersionUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(multidatasetVersionUrn, ServiceExceptionParameters.MULTIDATASET_VERSION_URN, exceptions);
    }

    public static void checkFindMultidatasetVersionsByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // NOTHING
    }

    public static void checkDeleteMultidatasetVersion(String multidatasetVersionUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(multidatasetVersionUrn, ServiceExceptionParameters.MULTIDATASET_VERSION_URN, exceptions);
    }

    public static void checkCreateMultidatasetCube(String multidatasetVersionUrn, MultidatasetCube cube, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(multidatasetVersionUrn, ServiceExceptionParameters.MULTIDATASET_VERSION_URN, exceptions);

        // checkNewCube
        StatisticalResourcesValidationUtils.checkParameterRequired(cube, ServiceExceptionParameters.MULTIDATASET_CUBE, exceptions);

        if (cube == null) {
            return;
        }

        checkCube(cube, exceptions);
        checkNewNameableStatisticalResource(cube.getNameableStatisticalResource(), ServiceExceptionParameters.MULTIDATASET_CUBE, exceptions);

        StatisticalResourcesValidationUtils.checkMetadataRequired(cube.getOrderInMultidataset(), ServiceExceptionParameters.MULTIDATASET_CUBE__ORDER_IN_MULTIDATASET, exceptions);
    }

    public static void checkUpdateMultidatasetCube(MultidatasetCube cube, List<MetamacExceptionItem> exceptions) {
        // checkExistingCube
        StatisticalResourcesValidationUtils.checkParameterRequired(cube, ServiceExceptionParameters.MULTIDATASET_CUBE, exceptions);

        if (cube == null) {
            return;
        }

        checkCube(cube, exceptions);
        checkExistingNameableStatisticalResource(cube.getNameableStatisticalResource(), TypeRelatedResourceEnum.MULTIDATASET_CUBE, ServiceExceptionParameters.MULTIDATASET_CUBE, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(cube.getMultidatasetVersion(), ServiceExceptionParameters.MULTIDATASET_CUBE, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(cube.getOrderInMultidataset(), ServiceExceptionParameters.MULTIDATASET_CUBE__ORDER_IN_MULTIDATASET, exceptions);
    }

    private static void checkCube(MultidatasetCube cube, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkMetadataRequired(cube.getIdentifier(), ServiceExceptionParameters.MULTIDATASET_CUBE__IDENTIFIER, exceptions);

        if (cube.getDataset() != null && cube.getQuery() != null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED,
                    ServiceExceptionParameters.MULTIDATASET_CUBE__DATASET + " / " + ServiceExceptionParameters.MULTIDATASET_CUBE__QUERY));
        } else if (cube.getDataset() == null && cube.getQuery() == null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED,
                    ServiceExceptionParameters.MULTIDATASET_CUBE__DATASET + " / " + ServiceExceptionParameters.MULTIDATASET_CUBE__QUERY));
        }

        if (cube.getDataset() != null) {
            StatisticalResourcesValidationUtils.checkMetadataRequired(cube.getDatasetUrn(), ServiceExceptionParameters.MULTIDATASET_CUBE__DATASET__URN, exceptions);
        }

        if (cube.getQuery() != null) {
            StatisticalResourcesValidationUtils.checkMetadataRequired(cube.getQueryUrn(), ServiceExceptionParameters.MULTIDATASET_CUBE__QUERY__URN, exceptions);
        }
    }

    public static void checkUpdateMultidatasetCubeLocation(String cubeUrn, Long orderInMultidataset, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(cubeUrn, ServiceExceptionParameters.MULTIDATASET_CUBE__URN, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(orderInMultidataset, ServiceExceptionParameters.MULTIDATASET_CUBE__ORDER_IN_MULTIDATASET, exceptions);
    }

    public static void checkRetrieveMultidatasetCube(String cubeUrn, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(cubeUrn, ServiceExceptionParameters.MULTIDATASET_CUBE__URN, exceptions);

    }

    public static void checkDeleteMultidatasetCube(String cubeUrn, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(cubeUrn, ServiceExceptionParameters.MULTIDATASET_CUBE__URN, exceptions);
    }

    private static void checkNewMultidatasetVersion(MultidatasetVersion multidatasetVersion, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(multidatasetVersion, ServiceExceptionParameters.MULTIDATASET_VERSION, exceptions);

        if (multidatasetVersion == null) {
            return;
        }

        checkNewSiemacMetadataStatisticalResource(multidatasetVersion.getSiemacMetadataStatisticalResource(), ServiceExceptionParameters.MULTIDATASET_VERSION, exceptions);
        checkMultidatasetVersion(multidatasetVersion, exceptions);

        // Metadata that must be empty for new entities
        StatisticalResourcesValidationUtils.checkMetadataEmpty(multidatasetVersion.getMultidataset(), ServiceExceptionParameters.MULTIDATASET_VERSION__MULTIDATASET, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(multidatasetVersion.getId(), ServiceExceptionParameters.MULTIDATASET_VERSION__ID, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(multidatasetVersion.getVersion(), ServiceExceptionParameters.MULTIDATASET_VERSION__VERSION, exceptions);
    }

    private static void checkExistingMultidatasetVersion(MultidatasetVersion multidatasetVersion, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(multidatasetVersion, ServiceExceptionParameters.MULTIDATASET_VERSION, exceptions);

        if (multidatasetVersion == null) {
            return;
        }

        checkExistingSiemacMetadataStatisticalResource(multidatasetVersion.getSiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.MULTIDATASET_VERSION,
                ServiceExceptionParameters.MULTIDATASET_VERSION, exceptions);
        checkMultidatasetVersion(multidatasetVersion, exceptions);

        // Metadata that must be filled for existing entities
        StatisticalResourcesValidationUtils.checkMetadataRequired(multidatasetVersion.getMultidataset(), ServiceExceptionParameters.MULTIDATASET_VERSION__MULTIDATASET, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(multidatasetVersion.getId(), ServiceExceptionParameters.MULTIDATASET_VERSION__ID, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(multidatasetVersion.getVersion(), ServiceExceptionParameters.MULTIDATASET_VERSION__VERSION, exceptions);
    }

    public static void checkUpdateGeographicalCache(MultidatasetVersion multidatasetVersion, List<MetamacExceptionItem> exceptions) {
        checkExistingMultidatasetVersion(multidatasetVersion, exceptions);
    }

    private static void checkMultidatasetVersion(MultidatasetVersion multidatasetVersion, List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkFindMultidatasetsByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkUpdateAllGeographicalCache(boolean mustWaitForRelatedResourcesUpdate, List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkUpdateAllExternalGeographicalCache(List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

}
