package org.siemac.metamac.statistical.resources.web.client.base.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;

public abstract class LifecycleMainFormLayout extends InternationalMainFormLayout {

    private MainFormLayoutButton productionValidation;
    private MainFormLayoutButton diffusionValidation;
    private MainFormLayoutButton rejectValidation;
    private MainFormLayoutButton publish;
    private MainFormLayoutButton resendStreamMessage;
    private MainFormLayoutButton updateGeocoverageCache;
    private MainFormLayoutButton versioning;
    private MainFormLayoutButton preview;

    private boolean              lastVersion;

    public LifecycleMainFormLayout() {
        super();
        common();
    }

    public LifecycleMainFormLayout(boolean canEdit) {
        super(canEdit);
        common();
    }

    private void common() {
        productionValidation = new MainFormLayoutButton(getConstants().lifeCycleSendToProductionValidation(), GlobalResources.RESOURCE.validateProduction().getURL());
        diffusionValidation = new MainFormLayoutButton(getConstants().lifeCycleSendToDiffusionValidation(), GlobalResources.RESOURCE.validateDiffusion().getURL());
        rejectValidation = new MainFormLayoutButton(getConstants().lifeCycleRejectValidation(), GlobalResources.RESOURCE.reject().getURL());
        publish = new MainFormLayoutButton(getConstants().lifeCyclePublish(), GlobalResources.RESOURCE.publish().getURL());
        resendStreamMessage = new MainFormLayoutButton(getConstants().lifeCycleReSendStreamMessage(), GlobalResources.RESOURCE.reload().getURL());
        updateGeocoverageCache = new MainFormLayoutButton(getConstants().updateGeographicCoverageVariableElementsCache(), GlobalResources.RESOURCE.reload().getURL());
        versioning = new MainFormLayoutButton(getConstants().lifeCycleVersioning(), GlobalResources.RESOURCE.version().getURL());
        preview = new MainFormLayoutButton(getConstants().actionPreviewData(), GlobalResources.RESOURCE.preview().getURL());

        toolStrip.addButton(productionValidation);
        toolStrip.addButton(diffusionValidation);
        toolStrip.addButton(rejectValidation);
        toolStrip.addButton(publish);
        toolStrip.addButton(resendStreamMessage);
        toolStrip.addButton(updateGeocoverageCache);
        toolStrip.addButton(versioning);
        toolStrip.addButton(preview);
    }

    @Override
    public void setViewMode() {
        super.setViewMode();
        updateVisibility();
    }

    @Override
    public void setEditionMode() {
        super.setEditionMode();
        hideAllLifeCycleButtons();
    }

    public void updatePublishSection(boolean lastVersion) {
        this.lastVersion = lastVersion;
    }

    protected void updateVisibility() {
        // Hide all buttons
        hideAllLifeCycleButtons();

        if (canSendToProductionValidation()) {
            showProductionValidationButton();
        }
        if (canSendToDiffusionValidation()) {
            showDiffusionValidationButton();
        }
        if (canRejectValidation()) {
            showRejectValidationButton();
        }
        if (canPublish()) {
            showPublishButton();
        }
        if (canResendStreamMessage()) {
            showResendStreamMessageButton();
        }
        if (canUpdateGeocoverageCache()) {
            showUpdateGeocoverageCacheButton();
        }
        if (canVersion() && lastVersion) {
            showVersioningButton();
        }

        showPreviewButton();
    }

    protected void hideAllLifeCycleButtons() {
        productionValidation.hide();
        diffusionValidation.hide();
        rejectValidation.hide();
        publish.hide();
        resendStreamMessage.hide();
        updateGeocoverageCache.hide();
        versioning.hide();
        preview.hide();
    }

    private void showProductionValidationButton() {
        if (canSendToProductionValidation()) {
            productionValidation.show();
        }
    }

    private void showDiffusionValidationButton() {
        if (canSendToDiffusionValidation()) {
            diffusionValidation.show();
        }
    }

    private void showRejectValidationButton() {
        if (canRejectValidation()) {
            rejectValidation.show();
        }
    }

    private void showPublishButton() {
        if (canPublish()) {
            publish.show();
        }
    }

    private void showResendStreamMessageButton() {
        if (canResendStreamMessage()) {
            resendStreamMessage.show();
        }
    }

    private void showUpdateGeocoverageCacheButton() {
        if (canUpdateGeocoverageCache()) {
            updateGeocoverageCache.show();
        }
    }

    private void showVersioningButton() {
        if (canVersion()) {
            versioning.show();
        }
    }

    protected void showPreviewButton() {
        if (canPreviewData()) {
            preview.show();
        }
    }

    public HasClickHandlers getProductionValidationButton() {
        return productionValidation;
    }

    public HasClickHandlers getDiffusionValidationButton() {
        return diffusionValidation;
    }

    public HasClickHandlers getRejectValidationButton() {
        return rejectValidation;
    }

    public HasClickHandlers getPublishButton() {
        return publish;
    }

    public HasClickHandlers getResendStreamMessageButton() {
        return resendStreamMessage;
    }

    public HasClickHandlers getUpdateGeocoverageCache() {
        return updateGeocoverageCache;
    }

    public HasClickHandlers getVersioningButton() {
        return versioning;
    }

    public HasClickHandlers getPreviewButton() {
        return preview;
    }

    public boolean isLastVersion() {
        return lastVersion;
    }

    //
    // ABSTRACT METHODS
    //

    protected abstract boolean canSendToProductionValidation();
    protected abstract boolean canSendToDiffusionValidation();
    protected abstract boolean canRejectValidation();
    protected abstract boolean canPublish();
    protected abstract boolean canResendStreamMessage();
    protected abstract boolean canUpdateGeocoverageCache();
    protected abstract boolean canVersion();
    protected abstract boolean canPreviewData();
}
