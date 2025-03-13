package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.LifecycleMainFormLayout;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;

import com.smartgwt.client.widgets.events.HasClickHandlers;

public class DatasetMainFormLayout extends LifecycleMainFormLayout {

    private DatasetVersionDto    datasetVersionDto;
    private MainFormLayoutButton updateGeocoverageCache;

    private MainFormLayoutButton copyDataset;

    public DatasetMainFormLayout() {
        super();
        createButtonsForToolStrip();
    }

    private void createButtonsForToolStrip() {
        updateGeocoverageCache = new MainFormLayoutButton(getConstants().updateGeographicCoverageVariableElementsCache(), GlobalResources.RESOURCE.reload().getURL());
        copyDataset = new MainFormLayoutButton(getConstants().actionCopy(), GlobalResources.RESOURCE.copy().getURL());

        toolStrip.addButton(updateGeocoverageCache);
        toolStrip.addButton(copyDataset);
    }

    public DatasetMainFormLayout(boolean canEdit) {
        super(canEdit);
        createButtonsForToolStrip();
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
    protected boolean canResendXMessage() {
        return DatasetClientSecurityUtils.canResendXMessageDatasetVersion(datasetVersionDto);
    }

    private boolean canUpdateGeocoverageCache() {
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

    protected boolean canCopyDataset() {
        return DatasetClientSecurityUtils.canCopyDataset(datasetVersionDto);
    }

    @Override
    protected void showPreviewButton() {
        if (datasetVersionDto.isKeepAllData() || isLastVersion()) {
            super.showPreviewButton();
        }
    }

    private void showUpdateGeocoverageCacheButton() {
        if (canUpdateGeocoverageCache()) {
            updateGeocoverageCache.show();
        }
    }

    private void showCopyDatasetButton() {
        if (canCopyDataset()) {
            copyDataset.show();
        }
    }

    @Override
    protected void updateVisibility() {
        super.updateVisibility();
        if (canUpdateGeocoverageCache()) {
            showUpdateGeocoverageCacheButton();
        }
        showCopyDatasetButton();
    }

    @Override
    protected void hideAllLifeCycleButtons() {
        super.hideAllLifeCycleButtons();
        updateGeocoverageCache.hide();
        copyDataset.hide();
    }

    public HasClickHandlers getUpdateGeocoverageCache() {
        return updateGeocoverageCache;
    }

    public HasClickHandlers getCopyDatasetButton() {
        return copyDataset;
    }
}
