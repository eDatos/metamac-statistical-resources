package org.siemac.metamac.statistical.resources.web.client.publication.widgets;

import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.LifecycleMainFormLayout;
import org.siemac.metamac.statistical.resources.web.client.publication.utils.PublicationClientSecurityUtils;

public class PublicationMainFormLayout extends LifecycleMainFormLayout {

    private PublicationVersionDto publicationVersionDto;

    public PublicationMainFormLayout() {
        super();
    }

    public PublicationMainFormLayout(boolean canEdit) {
        super(canEdit);
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
    protected boolean canVersion() {
        return PublicationClientSecurityUtils.canVersionPublication(publicationVersionDto);
    }

    @Override
    protected boolean canPreviewData() {
        return PublicationClientSecurityUtils.canPreviewDataPublicationVersion(publicationVersionDto);
    }
}
