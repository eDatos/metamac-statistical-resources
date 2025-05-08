package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setRelatedResourceValue;

import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.RelatedResourceLinkItem;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public class SiemacMetadataResourceRelationDescriptorsForm extends NavigationEnabledDynamicForm {

    private BaseUiHandlers uiHandlers;

    public SiemacMetadataResourceRelationDescriptorsForm() {
        super(getConstants().formResourceRelationDescriptors());
        init();
    }

    private void init() {

        RelatedResourceLinkItem replaces = new RelatedResourceLinkItem(SiemacMetadataDS.REPLACES, getConstants().siemacMetadataStatisticalResourceReplaces(),
                getCustomLinkItemNavigationClickHandler());
        RelatedResourceLinkItem isReplacedBy = new RelatedResourceLinkItem(SiemacMetadataDS.IS_REPLACED_BY, getConstants().siemacMetadataStatisticalResourceIsReplacedBy(),
                getCustomLinkItemNavigationClickHandler());

        setFields(replaces, isReplacedBy);
    }

    public void setSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        setRelatedResourceValue(getItem(SiemacMetadataDS.REPLACES), dto.getReplaces());
        setRelatedResourceValue(getItem(SiemacMetadataDS.IS_REPLACED_BY), dto.getIsReplacedBy());
    }

    public void setUiHandlers(BaseUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    @Override
    public BaseUiHandlers getBaseUiHandlers() {
        return uiHandlers;
    }
}
