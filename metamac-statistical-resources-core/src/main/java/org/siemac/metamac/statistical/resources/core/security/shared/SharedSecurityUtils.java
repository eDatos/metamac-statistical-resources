package org.siemac.metamac.statistical.resources.core.security.shared;

import static org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourcesRoleEnum.ADMINISTRADOR;
import static org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourcesRoleEnum.ANY_ROLE_ALLOWED;
import static org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourcesRoleEnum.JEFE_PRODUCCION;
import static org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourcesRoleEnum.TECNICO_APOYO_DIFUSION;
import static org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourcesRoleEnum.TECNICO_APOYO_PRODUCCION;
import static org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourcesRoleEnum.TECNICO_DIFUSION;
import static org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourcesRoleEnum.TECNICO_PRODUCCION;
import static org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourcesRoleEnum.LECTOR;

import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourcesRoleEnum;

public class SharedSecurityUtils {

    protected static final StatisticalResourcesRoleEnum[] PRODUCTION_ROLES             = {StatisticalResourcesRoleEnum.JEFE_PRODUCCION, StatisticalResourcesRoleEnum.TECNICO_PRODUCCION,
            StatisticalResourcesRoleEnum.TECNICO_APOYO_PRODUCCION                      };

    protected static final StatisticalResourcesRoleEnum[] PRODUCTION_ROLES_WITH_READER = {StatisticalResourcesRoleEnum.JEFE_PRODUCCION, StatisticalResourcesRoleEnum.TECNICO_PRODUCCION,
            StatisticalResourcesRoleEnum.TECNICO_APOYO_PRODUCCION, StatisticalResourcesRoleEnum.LECTOR};

    protected static final StatisticalResourcesRoleEnum[] DIFFUSION_ROLES              = {StatisticalResourcesRoleEnum.TECNICO_APOYO_DIFUSION, StatisticalResourcesRoleEnum.TECNICO_DIFUSION};

    /**
     * Checks if logged user has one of the allowed roles
     * 
     * @param roles
     * @return
     */
    protected static boolean isStatisticalResourcesRoleAllowed(MetamacPrincipal metamacPrincipal, StatisticalResourcesRoleEnum... roles) {
        // Administration has total control
        if (SharedSecurityUtils.isAdministrador(metamacPrincipal)) {
            return true;
        }
        // Checks user has any role of requested
        if (roles != null) {
            for (int i = 0; i < roles.length; i++) {
                StatisticalResourcesRoleEnum role = roles[i];
                if (SharedSecurityUtils.isUserInStatisticalResourcesRol(metamacPrincipal, role)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if logged user has access to a statistical operation with one of the selected roles
     * 
     * @param operationCode
     * @param roles
     * @return
     */
    protected static boolean isOperationAllowed(MetamacPrincipal metamacPrincipal, String operationCode, StatisticalResourcesRoleEnum... roles) {
        // Administrator has total control in all statistical operations
        if (isAdministrador(metamacPrincipal)) {
            return true;
        }
        // Checks if the statistical operation is in any role
        if (roles != null) {
            for (int i = 0; i < roles.length; i++) {
                StatisticalResourcesRoleEnum role = roles[i];
                if (haveAccessToOperationInRol(metamacPrincipal, role, operationCode)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected static boolean isOperationAllowedForAnyStatisticalResoueceRole(MetamacPrincipal metamacPrincipal, String operationCode) {
        // Administrator has total control in all statistical operations
        if (isAdministrador(metamacPrincipal)) {
            return true;
        }
        // Checks if the statistical operation is in any role
        for (StatisticalResourcesRoleEnum role : StatisticalResourcesRoleEnum.values()) {
            if (haveAccessToOperationInRol(metamacPrincipal, role, operationCode)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks user has any role
     */
    protected static boolean isUserInStatisticalResourcesRol(MetamacPrincipal metamacPrincipal, StatisticalResourcesRoleEnum role) {
        if (ANY_ROLE_ALLOWED.equals(role)) {
            return isAnyStatisticalResourceRole(metamacPrincipal);
        } else {
            return isRoleInAccesses(metamacPrincipal, role);
        }
    }

    /**
     * Checks if user has access to an operation. To have access, any access must exists to specified role and operation, or has any access with
     * role and operation with 'null' value
     */
    protected static boolean haveAccessToOperationInRol(MetamacPrincipal metamacPrincipal, StatisticalResourcesRoleEnum role, String operation) {
        for (MetamacPrincipalAccess metamacPrincipalAccess : metamacPrincipal.getAccesses()) {
            if (StatisticalResourcesConstants.APPLICATION_ID.equals(metamacPrincipalAccess.getApplication()) && metamacPrincipalAccess.getRole().equals(role.name())) {
                if (metamacPrincipalAccess.getOperation() == null || metamacPrincipalAccess.getOperation().equals(operation)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected static boolean isAdministrador(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, ADMINISTRADOR);
    }

    /**
     * Checks if user has access with role
     */
    protected static boolean isRoleInAccesses(MetamacPrincipal metamacPrincipal, StatisticalResourcesRoleEnum role) {
        for (MetamacPrincipalAccess metamacPrincipalAccess : metamacPrincipal.getAccesses()) {
            if (StatisticalResourcesConstants.APPLICATION_ID.equals(metamacPrincipalAccess.getApplication()) && metamacPrincipalAccess.getRole().equals(role.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if metamacPrincipal has any of the roles allowed in SRM (except DSD module)
     * 
     * @param metamacPrincipal
     * @return
     */
    protected static boolean isAnyStatisticalResourceRole(MetamacPrincipal metamacPrincipal) {
        return isAdministrador(metamacPrincipal) || isTecnicoApoyoDifusion(metamacPrincipal) || isTecnicoDifusion(metamacPrincipal) || isTecnicoApoyoProduccion(metamacPrincipal)
                || isTecnicoProduccion(metamacPrincipal) || isJefeProduccion(metamacPrincipal) || isLector(metamacPrincipal);
    }

    protected static boolean isTecnicoApoyoProduccion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, TECNICO_APOYO_PRODUCCION);
    }

    protected static boolean isTecnicoProduccion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, TECNICO_PRODUCCION);
    }

    protected static boolean isTecnicoApoyoDifusion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, TECNICO_APOYO_DIFUSION);
    }

    protected static boolean isTecnicoDifusion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, TECNICO_DIFUSION);
    }

    protected static boolean isJefeProduccion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, JEFE_PRODUCCION);
    }

    protected static boolean isLector(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, LECTOR);
    }

    // -----------------------------------------------------------------------
    // STATISTICAL RESOURCES ACTIONS
    // -----------------------------------------------------------------------

    protected static boolean canModifyStatisticalResource(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.DRAFT.equals(procStatus) || ProcStatusEnum.VALIDATION_REJECTED.equals(procStatus)) {
            return isOperationAllowed(metamacPrincipal, operationCode, PRODUCTION_ROLES);
        } else {
            return isOperationAllowed(metamacPrincipal, operationCode, StatisticalResourcesRoleEnum.JEFE_PRODUCCION, StatisticalResourcesRoleEnum.TECNICO_PRODUCCION);
        }
    }

    protected static boolean canRetrieveStatisticalResource(MetamacPrincipal metamacPrincipal, String operationCode, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.PUBLISHED.equals(procStatus)) {
            return true;
        } else {
            return isOperationAllowedForAnyStatisticalResoueceRole(metamacPrincipal, operationCode);
        }
    }

    public static boolean canUpdateGeographicCoverageVariableElementsCache(MetamacPrincipal metamacPrincipal) {
        return isAdministrador(metamacPrincipal);
    }

}
