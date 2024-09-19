package org.siemac.metamac.statistical.resources.web.client.multidataset.widgets;

import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.LifecycleMainFormLayout;
import org.siemac.metamac.statistical.resources.web.client.multidataset.utils.MultidatasetClientSecurityUtils;

public class MultidatasetMainFormLayout extends LifecycleMainFormLayout {

    private MultidatasetVersionDto multidatasetVersionDto;

    public MultidatasetMainFormLayout() {
        super();
    }

    public MultidatasetMainFormLayout(boolean canEdit) {
        super(canEdit);
    }

    public void setMultidatasetVersion(MultidatasetVersionDto multidatasetVersionDto) {
        this.multidatasetVersionDto = multidatasetVersionDto;
        setCanEdit(MultidatasetClientSecurityUtils.canUpdateMultidatasetVersion(multidatasetVersionDto));
        setCanDelete(MultidatasetClientSecurityUtils.canDeleteMultidatasetVersion(multidatasetVersionDto));
        updatePublishSection(multidatasetVersionDto.getLastVersion());
    }

    @Override
    protected boolean canSendToProductionValidation() {
        return MultidatasetClientSecurityUtils.canSendMultidatasetVersionToProductionValidation(multidatasetVersionDto);
    }

    @Override
    protected boolean canSendToDiffusionValidation() {
        return MultidatasetClientSecurityUtils.canSendMultidatasetVersionToDiffusionValidation(multidatasetVersionDto);
    }

    @Override
    protected boolean canRejectValidation() {
        return MultidatasetClientSecurityUtils.canSendMultidatasetVersionToValidationRejected(multidatasetVersionDto);
    }

    @Override
    protected boolean canPublish() {
        return MultidatasetClientSecurityUtils.canPublishMultidatasetVersion(multidatasetVersionDto);
    }

    @Override
    protected boolean canResendStreamMessage() {
        // TODO METAMAC-2715 - Realizar la notificación a Kafka de los recursos Multidataset
        return MultidatasetClientSecurityUtils.canResendStreamMessageDatasetVersion(multidatasetVersionDto);
    }

    @Override
    protected boolean canVersion() {
        return MultidatasetClientSecurityUtils.canVersionMultidataset(multidatasetVersionDto);
    }

    @Override
    protected boolean canPreviewData() {
        return MultidatasetClientSecurityUtils.canPreviewDataMultidatasetVersion(multidatasetVersionDto);
    }
}
