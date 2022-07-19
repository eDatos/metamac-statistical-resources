package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.LifecycleMainFormLayout;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetClientSecurityUtils;

public class DatasetMainFormLayout extends LifecycleMainFormLayout {

    private DatasetVersionDto datasetVersionDto;

    public DatasetMainFormLayout() {
        super();
    }

    public DatasetMainFormLayout(boolean canEdit) {
        super(canEdit);
    }

    public void setDatasetVersion(DatasetVersionDto datasetVersionDto) {
        this.datasetVersionDto = datasetVersionDto;
        setCanEdit(DatasetClientSecurityUtils.canUpdateDatasetVersion(datasetVersionDto));
        setCanDelete(DatasetClientSecurityUtils.canDeleteDatasetVersion(datasetVersionDto));
        updatePublishSection(datasetVersionDto.getLastVersion());
    }

    //
    // SECURITY
    //

    @Override
    protected boolean canSendToProductionValidation() {
        return DatasetClientSecurityUtils.canSendDatasetVersionToProductionValidation(datasetVersionDto);
    }

    @Override
    protected boolean canSendToDiffusionValidation() {
        return DatasetClientSecurityUtils.canSendDatasetVersionToDiffusionValidation(datasetVersionDto);
    }

    @Override
    protected boolean canRejectValidation() {
        return DatasetClientSecurityUtils.canSendDatasetVersionToValidationRejected(datasetVersionDto);
    }

    @Override
    protected boolean canPublish() {
        return DatasetClientSecurityUtils.canPublishDatasetVersion(datasetVersionDto);
    }

    @Override
    protected boolean canResendStreamMessage() {
        return DatasetClientSecurityUtils.canResendStreamMessageDatasetVersion(datasetVersionDto);
    }

    @Override
    protected boolean canUpdateGeocoverageCache() {
        return DatasetClientSecurityUtils.canUpdateGeographicCoverageVariableElementsCache(datasetVersionDto);
    }

    @Override
    protected boolean canVersion() {
        return DatasetClientSecurityUtils.canVersionDataset(datasetVersionDto);
    }

    @Override
    protected boolean canPreviewData() {
        return DatasetClientSecurityUtils.canPreviewDatasetData(datasetVersionDto);
    }

    @Override
    protected void showPreviewButton() {
        if (datasetVersionDto.isKeepAllData() || isLastVersion()) {
            super.showPreviewButton();
        }
    }
}
