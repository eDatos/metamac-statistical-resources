package org.siemac.metamac.statistical.resources.core.security.shared;

import java.util.Date;

import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourcesRoleEnum;

public class SharedDatasetsSecurityUtils extends SharedSecurityUtils {

    // ------------------------------------------------------------------------
    // DATASOURCES
    // ------------------------------------------------------------------------

    public static boolean canCreateDatasource(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, PRODUCTION_ROLES);
    }

    public static boolean canUpdateDatasource(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, PRODUCTION_ROLES);
    }

    public static boolean canRetrieveDatasourceByUrn(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, PRODUCTION_ROLES_WITH_READER);
    }

    public static boolean canDeleteDatasource(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, PRODUCTION_ROLES);
    }

    public static boolean canRetrieveDatasourcesByDatasetVersion(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, PRODUCTION_ROLES_WITH_READER);
    }

    public static boolean canRetrieveDatasourceDimensionRepresentationMappings(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, PRODUCTION_ROLES_WITH_READER);
    }

    // ------------------------------------------------------------------------
    // DATASETS
    // ------------------------------------------------------------------------

    public static boolean canFindDatasetsByCondition(MetamacPrincipal metamacPrincipal) {
        return isAnyStatisticalResourceRole(metamacPrincipal);
    }

    // ------------------------------------------------------------------------
    // DATASETS VERSIONS
    // ------------------------------------------------------------------------

    public static boolean canCreateDataset(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, PRODUCTION_ROLES);
    }

    public static boolean canUpdateDatasetVersion(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        return canModifyStatisticalResource(metamacPrincipal, operationCode, procStatus);
    }

    public static boolean canDeleteDatasetVersion(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        return canModifyStatisticalResource(metamacPrincipal, operationCode, procStatus);
    }

    public static boolean canFindDatasetsVersionsByCondition(MetamacPrincipal metamacPrincipal) {
        return isAnyStatisticalResourceRole(metamacPrincipal);
    }

    public static boolean canRetrieveDatasetVersionByUrn(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        return canRetrieveStatisticalResource(metamacPrincipal, operationCode, procStatus);
    }

    public static boolean canRetrieveDatasetVersions(MetamacPrincipal metamacPrincipal) {
        return isAnyStatisticalResourceRole(metamacPrincipal);
    }

    public static boolean canVersionDataset(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, PRODUCTION_ROLES);
    }

    public static boolean canSendDatasetVersionToProductionValidation(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, PRODUCTION_ROLES);
    }

    public static boolean canSendDatasetVersionToDiffusionValidation(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, StatisticalResourcesRoleEnum.TECNICO_PRODUCCION, StatisticalResourcesRoleEnum.TECNICO_APOYO_PRODUCCION);
    }

    public static boolean canSendDatasetVersionToValidationRejected(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, StatisticalResourcesRoleEnum.TECNICO_PRODUCCION, StatisticalResourcesRoleEnum.TECNICO_APOYO_PRODUCCION,
                StatisticalResourcesRoleEnum.TECNICO_DIFUSION, StatisticalResourcesRoleEnum.TECNICO_APOYO_DIFUSION);
    }

    public static boolean canPublishDataset(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, DIFFUSION_ROLES);
    }

    public static boolean canRetrieveLatestDatasetVersion(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        return canRetrieveStatisticalResource(metamacPrincipal, operationCode, procStatus);
    }

    public static boolean canRetrieveLatestPublishedDatasetVersion(MetamacPrincipal metamacPrincipal) {
        return isAnyStatisticalResourceRole(metamacPrincipal);
    }

    public static boolean canRetrieveDatasetVersionDimensionsIds(MetamacPrincipal metamacPrincipal) {
        return isAnyStatisticalResourceRole(metamacPrincipal);
    }

    public static boolean canRetrieveCoverageForDatasetVersionDimension(MetamacPrincipal metamacPrincipal) {
        return isAnyStatisticalResourceRole(metamacPrincipal);
    }

    public static boolean canFilterCoverageForDatasetVersionDimension(MetamacPrincipal metamacPrincipal) {
        return isAnyStatisticalResourceRole(metamacPrincipal);
    }

    public static boolean canFindStatisticOfficialities(MetamacPrincipal metamacPrincipal) {
        return isAnyStatisticalResourceRole(metamacPrincipal);
    }

    public static boolean canImportDatasourcesInDatasetVersion(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, PRODUCTION_ROLES);
    }

    public static boolean canImportDatasources(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, PRODUCTION_ROLES);
    }

    public static boolean canImportDatasourcesInStatisticalOperation(MetamacPrincipal metamacPrincipal, String operationCode) {
        return isOperationAllowed(metamacPrincipal, operationCode, PRODUCTION_ROLES);
    }

    public static boolean canRetrieveDatasetVersionMainCoverages(MetamacPrincipal metamacPrincipal) {
        return isAnyStatisticalResourceRole(metamacPrincipal);
    }

    public static boolean canCreateAttributeInstance(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        return canModifyStatisticalResource(metamacPrincipal, operationCode, procStatus);
    }

    public static boolean canUpdateAttributeInstance(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        return canModifyStatisticalResource(metamacPrincipal, operationCode, procStatus);
    }

    public static boolean canDeleteAttributeInstance(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        return canModifyStatisticalResource(metamacPrincipal, operationCode, procStatus);
    }

    public static boolean canRetrieveAttributeInstances(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        return canRetrieveStatisticalResource(metamacPrincipal, operationCode, procStatus);
    }

    public static boolean canPreviewDatasetData(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        return canRetrieveStatisticalResource(metamacPrincipal, operationCode, procStatus);
    }

    // ------------------------------------------------------------------------
    // CATEGORISATIONS
    // ------------------------------------------------------------------------

    public static boolean canCreateCategorisation(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        return canModifyStatisticalResource(metamacPrincipal, operationCode, procStatus);
    }

    public static boolean canRetrieveCategorisationByUrn(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        return canRetrieveStatisticalResource(metamacPrincipal, operationCode, procStatus);
    }

    public static boolean canDeleteDatasetCategorisation(MetamacPrincipal metamacPrincipal, Date validFromEffective, String operationCode, ProcStatusEnum procStatus) {
        if (canModifyStatisticalResource(metamacPrincipal, operationCode, procStatus)) {
            return validFromEffective == null;
        }
        return false;
    }

    public static boolean canRetrieveCategorisationsByDatasetVersion(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        return canRetrieveStatisticalResource(metamacPrincipal, operationCode, procStatus);
    }

    public static boolean canEndCategorisationValidity(MetamacPrincipal metamacPrincipal, Date validFromEffective, Date validToEffective, String operationCode, ProcStatusEnum procStatus) {
        if (canModifyStatisticalResource(metamacPrincipal, operationCode, procStatus)) {
            return validFromEffective != null && validFromEffective.before(new Date()) && validToEffective == null;
        }
        return false;
    }

}
