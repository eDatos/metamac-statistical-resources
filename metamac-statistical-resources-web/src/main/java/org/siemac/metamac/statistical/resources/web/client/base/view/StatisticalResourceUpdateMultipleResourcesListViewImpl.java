package org.siemac.metamac.statistical.resources.web.client.base.view;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.web.client.base.view.handlers.NewStatisticalResourceUiHandlers;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;

import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public abstract class StatisticalResourceUpdateMultipleResourcesListViewImpl<H extends NewStatisticalResourceUiHandlers> extends StatisticalResourceBaseListViewImpl<H> {

    protected CustomToolStripButton updateDatasetsGroupButton;

    protected StatisticalResourceUpdateMultipleResourcesListViewImpl() {
        super();

        updateDatasetsGroupButton = this.createUpdateDatasetsInGroupButton();
        toolStrip.addButton(updateDatasetsGroupButton);
    }

    private CustomToolStripButton createUpdateDatasetsInGroupButton() {
        CustomToolStripButton button = new CustomToolStripButton(getConstants().actionLoadUpdateDatasetInGroup(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.editListGrid().getURL());
        button.setVisible(false);
        button.addClickHandler(getUpdateDatasetsInGroupValidationClickHandler());
        return button;
    }

    @Override
    protected void showSelectionDependentButtons(ListGridRecord[] records) {
        super.showSelectionDependentButtons(records);
        showUpdateDatasetsInGroupValidationButton(records);
    }

    @Override
    protected void hideSelectionDependentButtons() {
        super.hideSelectionDependentButtons();
        updateDatasetsGroupButton.hide();
    }

    private void showUpdateDatasetsInGroupValidationButton(ListGridRecord[] records) {
        boolean isAllDatasetsInSameLifeCycle = true;
        boolean canVersion = false;
        boolean canSendToProduction = false;
        boolean canSendToDifussion = false;
        boolean canPublish = false;
        for (ListGridRecord datasetRecord : records) {
            if (canSendToProductionValidation(datasetRecord)) {
                canSendToProduction = true;
            }
            if (canVersion(datasetRecord)) {
                canVersion = true;
            }
            if (canSendToDiffusionValidation(datasetRecord)) {
                canSendToDifussion = true;
            }
            if (canPublish(datasetRecord)) {
                canPublish = true;
            }

            if (!checkDatasetsCanUpdateInGroup(canSendToProduction, canSendToDifussion, canPublish, canVersion)) {
                isAllDatasetsInSameLifeCycle = false;
                break;
            }
        }

        if (isAllDatasetsInSameLifeCycle) {
            updateDatasetsGroupButton.show();
        }
    }

    private boolean checkDatasetsCanUpdateInGroup(boolean canSendToProductionValidation, boolean canSendToDifussionValidation, boolean canPublish, boolean canVersion) {

        if (canVersion) {
            return false;
        }

        int i = 0;
        if (canSendToProductionValidation) {
            i++;
        }
        if (canSendToDifussionValidation) {
            i++;
        }
        if (canPublish) {
            i++;
        }

        return i <= 1;

    }

    protected abstract ClickHandler getUpdateDatasetsInGroupValidationClickHandler();
}
