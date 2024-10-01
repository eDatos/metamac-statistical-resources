package org.siemac.metamac.statistical.resources.web.client.query.view.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.XStreamStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.LifecycleMainFormLayout;
import org.siemac.metamac.statistical.resources.web.client.query.utils.QueryClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;

import com.smartgwt.client.widgets.events.HasClickHandlers;

public class QueryMainFormLayout extends LifecycleMainFormLayout {

    private QueryVersionDto      queryVersionDto;
    private MainFormLayoutButton updateGeoCacheRelatedResource;

    public QueryMainFormLayout() {
        super();
        createButtonsForToolStrip();
    }

    public QueryMainFormLayout(boolean canEdit) {
        super(canEdit);
        createButtonsForToolStrip();
    }

    private void createButtonsForToolStrip() {
        updateGeoCacheRelatedResource = new MainFormLayoutButton(getConstants().updateGeographicCoverageVariableElementsCache(), GlobalResources.RESOURCE.reload().getURL());
        toolStrip.addButton(updateGeoCacheRelatedResource);
    }

    public void setQueryVersion(QueryVersionDto queryVersionDto) {
        this.queryVersionDto = queryVersionDto;
        setTitleLabelContents(InternationalStringUtils.getLocalisedString(queryVersionDto.getTitle()));
        setCanEdit(QueryClientSecurityUtils.canUpdateQueryVersion(queryVersionDto));
        setCanDelete(QueryClientSecurityUtils.canDeleteQueryVersion(queryVersionDto));
        updatePublishSection(queryVersionDto.getLastVersion());
    }

    @Override
    protected boolean canSendToProductionValidation() {
        return QueryClientSecurityUtils.canSendQueryVersionToProductionValidation(queryVersionDto);
    }

    @Override
    protected boolean canSendToDiffusionValidation() {
        return QueryClientSecurityUtils.canSendQueryVersionToDiffusionValidation(queryVersionDto);
    }

    @Override
    protected boolean canRejectValidation() {
        return QueryClientSecurityUtils.canSendQueryVersionToValidationRejected(queryVersionDto);
    }

    @Override
    protected boolean canPublish() {
        return QueryClientSecurityUtils.canPublishQueryVersion(queryVersionDto);
    }

    @Override
    protected boolean canResendStreamMessage() {
        return QueryClientSecurityUtils.canResendStreamMessageQueryVersion(queryVersionDto);
    }

    @Override
    protected boolean canVersion() {
        return QueryClientSecurityUtils.canVersionQueryVersion(queryVersionDto);
    }

    @Override
    protected boolean canPreviewData() {
        return QueryClientSecurityUtils.canPreviewQueryData(queryVersionDto);
    }

    @Override
    protected boolean canShowCopyButton() {
        return false;
    }

    private boolean canUpdateGeoCacheRelatedResource() {
        return QueryClientSecurityUtils.canUpdateGeoCacheRelatedResource(queryVersionDto);
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

    @Override
    protected boolean canResendXMessage() {
        if (Boolean.FALSE.equals(queryVersionDto.getLastVersion()) || (queryVersionDto.getXStreamStatus() == null || XStreamStatusEnum.SENT.equals(queryVersionDto.getXStreamStatus()))) {
            return false;
        }

        return canResendXMessageDatasetVersion();
    }

    private boolean canResendXMessageDatasetVersion() {
        return QueryClientSecurityUtils.canResendStreamMessageQueryVersion(queryVersionDto);
    }
}
