package org.siemac.metamac.statistical.resources.web.client.multidataset.utils;

import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.security.shared.SharedMultidatasetsSecurityUtils;
import org.siemac.metamac.statistical.resources.core.security.shared.SharedSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.base.utils.LifecycleClientSecurityUtils;

public class MultidatasetClientSecurityUtils extends LifecycleClientSecurityUtils {

    // ------------------------------------------------------------------------
    // PUBLICATION VERSIONS
    // ------------------------------------------------------------------------

    public static boolean canCreateMultidataset() {
        return SharedMultidatasetsSecurityUtils.canCreateMultidataset(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    public static boolean canUpdateMultidatasetVersion(MultidatasetVersionDto multidatasetVersionDto) {
        if (isPublished(multidatasetVersionDto.getProcStatus())) {
            return false;
        }
        return SharedMultidatasetsSecurityUtils.canUpdateMultidatasetVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), multidatasetVersionDto.getProcStatus());
    }

    public static boolean canDeleteMultidatasetVersion(MultidatasetVersionBaseDto dto) {
        return canDeleteMultidatasetVersion(dto.getProcStatus());
    }

    public static boolean canDeleteMultidatasetVersion(MultidatasetVersionDto dto) {
        return canDeleteMultidatasetVersion(dto.getProcStatus());
    }

    private static boolean canDeleteMultidatasetVersion(ProcStatusEnum multidatasetProcStatus) {
        if (isPublished(multidatasetProcStatus)) {
            return false;
        }
        return SharedMultidatasetsSecurityUtils.canDeleteMultidatasetVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), multidatasetProcStatus);
    }

    public static boolean canPreviewDataMultidatasetVersion(MultidatasetVersionDto multidatasetVersionDto) {
        return SharedMultidatasetsSecurityUtils.canPreviewDataMultidatasetVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), multidatasetVersionDto.getProcStatus());
    }

    // ------------------------------------------------------------------------
    // PUBLICATION VERSIONS LIFECYCLE
    // ------------------------------------------------------------------------

    public static boolean canSendMultidatasetVersionToProductionValidation(MultidatasetVersionDto dto) {
        return canSendMultidatasetVersionToProductionValidation(dto.getProcStatus());
    }

    public static boolean canSendMultidatasetVersionToProductionValidation(MultidatasetVersionBaseDto dto) {
        return canSendMultidatasetVersionToProductionValidation(dto.getProcStatus());
    }

    public static boolean canSendMultidatasetVersionToDiffusionValidation(MultidatasetVersionDto dto) {
        return canSendMultidatasetVersionToDiffusionValidation(dto.getProcStatus());
    }

    public static boolean canSendMultidatasetVersionToDiffusionValidation(MultidatasetVersionBaseDto dto) {
        return canSendMultidatasetVersionToDiffusionValidation(dto.getProcStatus());
    }

    public static boolean canSendMultidatasetVersionToValidationRejected(MultidatasetVersionDto dto) {
        return canSendMultidatasetVersionToValidationRejected(dto.getProcStatus());
    }

    public static boolean canSendMultidatasetVersionToValidationRejected(MultidatasetVersionBaseDto dto) {
        return canSendMultidatasetVersionToValidationRejected(dto.getProcStatus());
    }

    public static boolean canPublishMultidatasetVersion(MultidatasetVersionDto dto) {
        return canPublishMultidatasetVersion(dto.getProcStatus());
    }

    public static boolean canPublishMultidatasetVersion(MultidatasetVersionBaseDto dto) {
        return canPublishMultidatasetVersion(dto.getProcStatus());
    }

    public static boolean canResendStreamMessageDatasetVersion(MultidatasetVersionDto dto) {
        if (!dto.getLastVersion()) {
            return false;
        }

        return canResendStreamMessageDatasetVersion(dto.getProcStatus());
    }

    public static boolean canVersionMultidataset(MultidatasetVersionDto dto) {
        return canVersionMultidataset(dto.getProcStatus());
    }

    public static boolean canVersionMultidataset(MultidatasetVersionBaseDto dto) {
        return canVersionMultidataset(dto.getProcStatus());
    }

    private static boolean canSendMultidatasetVersionToProductionValidation(ProcStatusEnum procStatus) {
        if (!canSendToProductionValidation(procStatus)) {
            return false;
        }
        return SharedMultidatasetsSecurityUtils.canSendMultidatasetVersionToProductionValidation(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canSendMultidatasetVersionToDiffusionValidation(ProcStatusEnum procStatus) {
        if (!canSendToDiffusionValidation(procStatus)) {
            return false;
        }
        return SharedMultidatasetsSecurityUtils.canSendMultidatasetVersionToDiffusionValidation(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canSendMultidatasetVersionToValidationRejected(ProcStatusEnum procStatus) {
        if (!canRejectValidation(procStatus)) {
            return false;
        }
        return SharedMultidatasetsSecurityUtils.canSendMultidatasetVersionToValidationRejected(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canPublishMultidatasetVersion(ProcStatusEnum procStatus) {
        if (!canPublish(procStatus)) {
            return false;
        }
        return SharedMultidatasetsSecurityUtils.canPublishMultidatasetVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canResendStreamMessageDatasetVersion(ProcStatusEnum procStatus) {
        if (!canResendStreamMessage(procStatus)) {
            return false;
        }

        return SharedMultidatasetsSecurityUtils.canPublishMultidatasetVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canVersionMultidataset(ProcStatusEnum procStatus) {
        if (!canVersion(procStatus)) {
            return false;
        }
        return SharedMultidatasetsSecurityUtils.canVersionMultidataset(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    // ------------------------------------------------------------------------
    // CUBES
    // ------------------------------------------------------------------------

    public static boolean canCreateCube(ProcStatusEnum multidatasetProcStatus) {
        if (isPublished(multidatasetProcStatus)) {
            return false;
        }
        return SharedMultidatasetsSecurityUtils.canCreateMultidatasetCube(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), multidatasetProcStatus);
    }

    public static boolean canUpdateCube(ProcStatusEnum multidatasetProcStatus) {
        if (isPublished(multidatasetProcStatus)) {
            return false;
        }
        return SharedMultidatasetsSecurityUtils.canUpdateMultidatasetCube(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), multidatasetProcStatus);
    }

    public static boolean canUpdateCubeLocation(ProcStatusEnum multidatasetProcStatus) {
        if (isPublished(multidatasetProcStatus)) {
            return false;
        }
        return SharedMultidatasetsSecurityUtils.canUpdateMultidatasetCubeLocation(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), multidatasetProcStatus);
    }

    public static boolean canDeleteCube(ProcStatusEnum multidatasetProcStatus) {
        if (isPublished(multidatasetProcStatus)) {
            return false;
        }
        return SharedMultidatasetsSecurityUtils.canDeleteMultidatasetCube(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), multidatasetProcStatus);
    }

    public static boolean canUpdateGeoCacheRelatedResource(MultidatasetVersionDto dto) {
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
