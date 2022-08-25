package org.siemac.metamac.statistical.resources.web.client.dataset.utils;

import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.security.shared.SharedDatasetsSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.base.utils.LifecycleClientSecurityUtils;

public class DatasetClientSecurityUtils extends LifecycleClientSecurityUtils {

    // ------------------------------------------------------------------------
    // DATASETS VERSIONS
    // ------------------------------------------------------------------------

    public static boolean canCreateDataset() {
        return SharedDatasetsSecurityUtils.canCreateDataset(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    public static boolean canUpdateDatasetVersion(DatasetVersionDto datasetVersionDto) {
        if (BooleanUtils.isTrue(datasetVersionDto.getIsTaskInBackground())) {
            return false;
        }
        if (isPublished(datasetVersionDto.getProcStatus())) {
            return false;
        }
        return SharedDatasetsSecurityUtils.canUpdateDatasetVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), datasetVersionDto.getProcStatus());
    }

    public static boolean canDeleteDatasetVersion(DatasetVersionDto datasetVersionDto) {
        return canDeleteDatasetVersion(datasetVersionDto.getIsTaskInBackground(), datasetVersionDto.getProcStatus());
    }

    public static boolean canDeleteDatasetVersion(DatasetVersionBaseDto datasetVersionBaseDto) {
        return canDeleteDatasetVersion(datasetVersionBaseDto.getIsTaskInBackground(), datasetVersionBaseDto.getProcStatus());
    }

    public static boolean canDeleteDatasetVersion(boolean isTaskInBackground, ProcStatusEnum procStatus) {
        if (BooleanUtils.isTrue(isTaskInBackground)) {
            return false;
        }
        if (isPublished(procStatus)) {
            return false;
        }
        return SharedDatasetsSecurityUtils.canDeleteDatasetVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), procStatus);
    }

    public static boolean canPreviewDatasetData(DatasetVersionDto datasetVersionDto) {
        return SharedDatasetsSecurityUtils.canPreviewDatasetData(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), datasetVersionDto.getProcStatus());
    }

    // ------------------------------------------------------------------------
    // DATASETS VERSIONS LIFECYCLE
    // ------------------------------------------------------------------------

    public static boolean canSendDatasetVersionToProductionValidation(DatasetVersionDto dto) {
        return canSendDatasetVersionToProductionValidation(dto.getProcStatus(), dto.getIsTaskInBackground());
    }

    public static boolean canSendDatasetVersionToProductionValidation(DatasetVersionBaseDto dto) {
        return canSendDatasetVersionToProductionValidation(dto.getProcStatus(), dto.getIsTaskInBackground());
    }

    public static boolean canSendDatasetVersionToDiffusionValidation(DatasetVersionDto dto) {
        return canSendDatasetVersionToDiffusionValidation(dto.getProcStatus(), dto.getIsTaskInBackground());
    }

    public static boolean canSendDatasetVersionToDiffusionValidation(DatasetVersionBaseDto dto) {
        return canSendDatasetVersionToDiffusionValidation(dto.getProcStatus(), dto.getIsTaskInBackground());
    }

    public static boolean canSendDatasetVersionToValidationRejected(DatasetVersionDto dto) {
        return canSendDatasetVersionToValidationRejected(dto.getProcStatus(), dto.getIsTaskInBackground());
    }

    public static boolean canSendDatasetVersionToValidationRejected(DatasetVersionBaseDto dto) {
        return canSendDatasetVersionToValidationRejected(dto.getProcStatus(), dto.getIsTaskInBackground());
    }

    public static boolean canPublishDatasetVersion(DatasetVersionDto dto) {
        return canPublishDatasetVersion(dto.getProcStatus(), dto.getIsTaskInBackground());
    }

    public static boolean canPublishDatasetVersion(DatasetVersionBaseDto dto) {
        return canPublishDatasetVersion(dto.getProcStatus(), dto.getIsTaskInBackground());
    }

    public static boolean canResendStreamMessageDatasetVersion(DatasetVersionDto dto) {
        if (!dto.getLastVersion()) {
            return false;
        }

        return canResendStreamMessageDatasetVersion(dto.getProcStatus(), dto.getIsTaskInBackground());
    }

    public static boolean canVersionDataset(DatasetVersionDto dto) {
        return canVersionDataset(dto.getProcStatus(), dto.getIsTaskInBackground());
    }

    public static boolean canVersionDataset(DatasetVersionBaseDto dto) {
        return canVersionDataset(dto.getProcStatus(), dto.getIsTaskInBackground());
    }

    // LIFECYCLE COMMON

    private static boolean canSendDatasetVersionToProductionValidation(ProcStatusEnum procStatus, boolean isTaskInBackground) {
        if (BooleanUtils.isTrue(isTaskInBackground)) {
            return false;
        }
        if (!canSendToProductionValidation(procStatus)) {
            return false;
        }
        return SharedDatasetsSecurityUtils.canSendDatasetVersionToProductionValidation(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canSendDatasetVersionToDiffusionValidation(ProcStatusEnum procStatus, boolean isTaskInBackground) {
        if (BooleanUtils.isTrue(isTaskInBackground)) {
            return false;
        }
        if (!canSendToDiffusionValidation(procStatus)) {
            return false;
        }
        return SharedDatasetsSecurityUtils.canSendDatasetVersionToDiffusionValidation(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canSendDatasetVersionToValidationRejected(ProcStatusEnum procStatus, boolean isTaskInBackground) {
        if (BooleanUtils.isTrue(isTaskInBackground)) {
            return false;
        }
        if (!canRejectValidation(procStatus)) {
            return false;
        }
        return SharedDatasetsSecurityUtils.canSendDatasetVersionToValidationRejected(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canPublishDatasetVersion(ProcStatusEnum procStatus, Boolean isTaskInBackground) {
        if (BooleanUtils.isTrue(isTaskInBackground)) {
            return false;
        }
        if (!canPublish(procStatus)) {
            return false;
        }
        return SharedDatasetsSecurityUtils.canPublishDataset(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canResendStreamMessageDatasetVersion(ProcStatusEnum procStatus, Boolean isTaskInBackground) {
        if (BooleanUtils.isTrue(isTaskInBackground)) {
            return false;
        }
        if (!canResendStreamMessage(procStatus)) {
            return false;
        }

        return SharedDatasetsSecurityUtils.canPublishDataset(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canVersionDataset(ProcStatusEnum procStatus, boolean isTaskInBackground) {
        if (BooleanUtils.isTrue(isTaskInBackground)) {
            return false;
        }
        if (!canVersion(procStatus)) {
            return false;
        }
        return SharedDatasetsSecurityUtils.canVersionDataset(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    // ------------------------------------------------------------------------
    // DATASOURCES
    // ------------------------------------------------------------------------

    public static boolean canDeleteDatasources(DatasetVersionDto datasetVersionDto) {
        if (BooleanUtils.isTrue(datasetVersionDto.getIsTaskInBackground())) {
            return false;
        }
        if (isPublished(datasetVersionDto.getProcStatus())) {
            return false;
        }
        return SharedDatasetsSecurityUtils.canDeleteDatasource(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    public static boolean canImportDatasourcesInDatasetVersion(DatasetVersionDto datasetVersionDto) {
        if (BooleanUtils.isTrue(datasetVersionDto.getIsTaskInBackground())) {
            return false;
        }

        // Datasources can only be imported in datasets in DRAFT or in VALIDATION_REJECTED
        if (!isDraftOrValidationRejected(datasetVersionDto.getProcStatus())) {
            return false;
        }
        return SharedDatasetsSecurityUtils.canImportDatasourcesInDatasetVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    public static boolean canImportDatasourcesInStatisticalOperation() {
        return SharedDatasetsSecurityUtils.canImportDatasourcesInStatisticalOperation(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    public static boolean canCreateCategorisation(DatasetVersionDto dto) {
        return SharedDatasetsSecurityUtils.canCreateCategorisation(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), dto.getProcStatus());
    }

    public static boolean canDeleteDatasetCategorisation(DatasetVersionDto dto, CategorisationDto categorisationDto) {
        return SharedDatasetsSecurityUtils.canDeleteDatasetCategorisation(getMetamacPrincipal(), categorisationDto.getValidFrom(), getCurrentStatisticalOperationCode(), dto.getProcStatus());
    }

    public static boolean canEndCategorisationValidity(DatasetVersionDto dto, CategorisationDto categorisationDto) {
        return SharedDatasetsSecurityUtils.canEndCategorisationValidity(getMetamacPrincipal(), categorisationDto.getValidFrom(), categorisationDto.getValidTo(), getCurrentStatisticalOperationCode(),
                dto.getProcStatus());
    }

    // ------------------------------------------------------------------------
    // ATTRIBUTES
    // ------------------------------------------------------------------------

    public static boolean canCreateAttributeInstance(DatasetVersionDto dto) {
        if (BooleanUtils.isTrue(dto.getIsTaskInBackground())) {
            return false;
        }
        if (isPublished(dto.getProcStatus())) {
            return false;
        }
        return SharedDatasetsSecurityUtils.canCreateAttributeInstance(getMetamacPrincipal(), dto.getStatisticalOperation().getCode(), dto.getProcStatus());
    }

    public static boolean canUpdateAttributeInstance(DatasetVersionDto dto) {
        if (BooleanUtils.isTrue(dto.getIsTaskInBackground())) {
            return false;
        }
        if (isPublished(dto.getProcStatus())) {
            return false;
        }
        return SharedDatasetsSecurityUtils.canUpdateAttributeInstance(getMetamacPrincipal(), dto.getStatisticalOperation().getCode(), dto.getProcStatus());
    }

    public static boolean canDeleteAttributeInstance(DatasetVersionDto dto) {
        if (BooleanUtils.isTrue(dto.getIsTaskInBackground())) {
            return false;
        }
        if (isPublished(dto.getProcStatus())) {
            return false;
        }
        return SharedDatasetsSecurityUtils.canDeleteAttributeInstance(getMetamacPrincipal(), dto.getStatisticalOperation().getCode(), dto.getProcStatus());
    }

    // ------------------------------------------------------------------------
    // ADMIN MANAGEMENT
    // ------------------------------------------------------------------------

    public static boolean canUpdateAllGeographicCoverageVariableElementsCache() {
        return SharedDatasetsSecurityUtils.canUpdateGeographicCoverageVariableElementsCache(getMetamacPrincipal());
    }    
    
    public static boolean canUpdateGeographicCoverageVariableElementsCache(DatasetVersionDto dto) {
        if (Boolean.FALSE.equals(dto.getLastVersion())) {
            return false;
        }
        return SharedDatasetsSecurityUtils.canUpdateGeographicCoverageVariableElementsCache(getMetamacPrincipal());
    }
}
