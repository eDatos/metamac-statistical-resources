package org.siemac.metamac.statistical.resources.web.client.query.view.widgets;

import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.LifecycleMainFormLayout;
import org.siemac.metamac.statistical.resources.web.client.query.utils.QueryClientSecurityUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

public class QueryMainFormLayout extends LifecycleMainFormLayout {

    private QueryVersionDto queryVersionDto;

    public QueryMainFormLayout() {
        super();
    }

    public QueryMainFormLayout(boolean canEdit) {
        super(canEdit);
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
}
