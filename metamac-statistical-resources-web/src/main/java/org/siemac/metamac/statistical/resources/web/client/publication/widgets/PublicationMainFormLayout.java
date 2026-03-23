package org.siemac.metamac.statistical.resources.web.client.publication.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.LifecycleMainFormLayout;
import org.siemac.metamac.statistical.resources.web.client.publication.utils.PublicationClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;

import com.smartgwt.client.widgets.events.HasClickHandlers;

public class PublicationMainFormLayout extends LifecycleMainFormLayout {

    private PublicationVersionDto publicationVersionDto;
    private MainFormLayoutButton  updateGeoCacheRelatedResource;

    public PublicationMainFormLayout() {
        super();
        createButtonsForToolStrip();
    }

    public PublicationMainFormLayout(boolean canEdit) {
        super(canEdit);
        createButtonsForToolStrip();
    }

    private void createButtonsForToolStrip() {
        updateGeoCacheRelatedResource = new MainFormLayoutButton(getConstants().updateGeographicCoverageVariableElementsCache(), GlobalResources.RESOURCE.reload().getURL());
        toolStrip.addButton(updateGeoCacheRelatedResource);
    }

    public void setPublicationVersion(PublicationVersionDto publicationVersionDto) {
        this.publicationVersionDto = publicationVersionDto;
        setCanEdit(PublicationClientSecurityUtils.canUpdatePublicationVersion(publicationVersionDto));
        setCanDelete(PublicationClientSecurityUtils.canDeletePublicationVersion(publicationVersionDto));
        updatePublishSection(publicationVersionDto.getLastVersion());
    }

    @Override
    protected boolean canSendToProductionValidation() {
        return PublicationClientSecurityUtils.canSendPublicationVersionToProductionValidation(publicationVersionDto);
    }

    @Override
    protected boolean canSendToDiffusionValidation() {
        return PublicationClientSecurityUtils.canSendPublicationVersionToDiffusionValidation(publicationVersionDto);
    }

    @Override
    protected boolean canRejectValidation() {
        return PublicationClientSecurityUtils.canSendPublicationVersionToValidationRejected(publicationVersionDto);
    }

    @Override
    protected boolean canPublish() {
        return PublicationClientSecurityUtils.canPublishPublicationVersion(publicationVersionDto);
    }

    @Override
    protected boolean canResendStreamMessage() {
        return PublicationClientSecurityUtils.canResendStreamMessageDatasetVersion(publicationVersionDto);
    }

    @Override
    protected boolean canResendXMessage() {
        return false;
    }

    @Override
    protected boolean canVersion() {
        return PublicationClientSecurityUtils.canVersionPublication(publicationVersionDto);
    }

    @Override
    protected boolean canPreviewData() {
        return PublicationClientSecurityUtils.canPreviewDataPublicationVersion(publicationVersionDto);
    }

    private boolean canUpdateGeoCacheRelatedResource() {
        return PublicationClientSecurityUtils.canUpdateGeoCacheRelatedResource(publicationVersionDto);
    }

    @Override
    protected void updateVisibility() {
        super.updateVisibility();
        if (canUpdateGeoCacheRelatedResource()) {
            showUpdateGeoCacheRelatedResourceButton();
        }
    }

    @Override
    protected void hideAllLifeCycleButtons() {
        super.hideAllLifeCycleButtons();
        updateGeoCacheRelatedResource.hide();
    }

    private void showUpdateGeoCacheRelatedResourceButton() {
        if (canUpdateGeoCacheRelatedResource()) {
            updateGeoCacheRelatedResource.show();
        }
    }

    public HasClickHandlers getUpdateGeoCacheRelatedResource() {
        return updateGeoCacheRelatedResource;
    }
}
