package org.siemac.metamac.statistical.resources.web.client.query.utils;

import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.security.shared.SharedQueriesSecurityUtils;
import org.siemac.metamac.statistical.resources.core.security.shared.SharedSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.base.utils.LifecycleClientSecurityUtils;

public class QueryClientSecurityUtils extends LifecycleClientSecurityUtils {

    // ------------------------------------------------------------------------
    // QUERY VERSIONS
    // ------------------------------------------------------------------------

    public static boolean canCreateQuery() {
        return SharedQueriesSecurityUtils.canCreateQuery(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    public static boolean canUpdateQueryVersion(QueryVersionDto queryVersionDto) {
        return SharedQueriesSecurityUtils.canUpdateQueryVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), queryVersionDto.getProcStatus());
    }

    public static boolean canDeleteQueryVersion(QueryVersionDto queryVersionDto) {
        return canDeleteQueryVersion(queryVersionDto.getProcStatus());
    }

    public static boolean canDeleteQueryVersion(QueryVersionBaseDto queryVersionDto) {
        return canDeleteQueryVersion(queryVersionDto.getProcStatus());
    }

    private static boolean canDeleteQueryVersion(ProcStatusEnum queryProcStatus) {
        if (isPublished(queryProcStatus)) {
            return false;
        }
        return SharedQueriesSecurityUtils.canDeleteQueryVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), queryProcStatus);
    }

    public static boolean canPreviewQueryData(QueryVersionDto dto) {
        return SharedQueriesSecurityUtils.canPreviewQueryData(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), dto.getProcStatus());
    }

    // ------------------------------------------------------------------------
    // QUERY VERSIONS LIFECYCLE
    // ------------------------------------------------------------------------

    public static boolean canSendQueryVersionToProductionValidation(QueryVersionDto dto) {
        return canSendQueryVersionToProductionValidation(dto.getProcStatus());
    }

    public static boolean canSendQueryVersionToProductionValidation(QueryVersionBaseDto dto) {
        return canSendQueryVersionToProductionValidation(dto.getProcStatus());
    }

    public static boolean canSendQueryVersionToDiffusionValidation(QueryVersionDto dto) {
        return canSendQueryVersionToDiffusionValidation(dto.getProcStatus());
    }

    public static boolean canSendQueryVersionToDiffusionValidation(QueryVersionBaseDto dto) {
        return canSendQueryVersionToDiffusionValidation(dto.getProcStatus());
    }

    public static boolean canSendQueryVersionToValidationRejected(QueryVersionDto dto) {
        return canSendQueryVersionToValidationRejected(dto.getProcStatus());
    }

    public static boolean canSendQueryVersionToValidationRejected(QueryVersionBaseDto dto) {
        return canSendQueryVersionToValidationRejected(dto.getProcStatus());
    }

    public static boolean canPublishQueryVersion(QueryVersionDto dto) {
        return canPublishQueryVersion(dto.getProcStatus());
    }

    public static boolean canPublishQueryVersion(QueryVersionBaseDto dto) {
        return canPublishQueryVersion(dto.getProcStatus());
    }

    public static boolean canResendStreamMessageQueryVersion(QueryVersionDto dto) {
        if (!dto.getLastVersion()) {
            return false;
        }
        return canResendStreamMessageQueryVersion(dto.getProcStatus());
    }

    public static boolean canVersionQueryVersion(QueryVersionDto dto) {
        return canVersionQueryVersion(dto.getProcStatus());
    }

    public static boolean canVersionQueryVersion(QueryVersionBaseDto dto) {
        return canVersionQueryVersion(dto.getProcStatus());
    }

    private static boolean canSendQueryVersionToProductionValidation(ProcStatusEnum procStatus) {
        if (!canSendToProductionValidation(procStatus)) {
            return false;
        }
        return SharedQueriesSecurityUtils.canSendQueryVersionToProductionValidation(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canSendQueryVersionToDiffusionValidation(ProcStatusEnum procStatus) {
        if (!canSendToDiffusionValidation(procStatus)) {
            return false;
        }
        return SharedQueriesSecurityUtils.canSendQueryVersionToDiffusionValidation(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canSendQueryVersionToValidationRejected(ProcStatusEnum procStatus) {
        if (!canRejectValidation(procStatus)) {
            return false;
        }
        return SharedQueriesSecurityUtils.canSendQueryVersionToValidationRejected(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canPublishQueryVersion(ProcStatusEnum procStatus) {
        if (!canPublish(procStatus)) {
            return false;
        }
        return SharedQueriesSecurityUtils.canPublishQueryVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canResendStreamMessageQueryVersion(ProcStatusEnum procStatus) {
        if (!canResendStreamMessage(procStatus)) {
            return false;
        }
        return SharedQueriesSecurityUtils.canPublishQueryVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canVersionQueryVersion(ProcStatusEnum procStatus) {
        if (!canVersion(procStatus)) {
            return false;
        }
        return SharedQueriesSecurityUtils.canVersionQueryVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    public static boolean canUpdateGeoCacheRelatedResource(QueryVersionDto dto) {
        if (!isPublished(dto.getProcStatus())) {
            return false;
        }

        // check is the last version published
        if (dto.getValidTo() != null) {
            return false;
        }

        return SharedSecurityUtils.canUpdateGeoCacheRelatedResource(getMetamacPrincipal());
    }
}
