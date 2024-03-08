package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils.DatasetWidgetsUtil;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;

public class LifeCycleResourceProductionDescriptorsForm extends GroupDynamicForm {

    public LifeCycleResourceProductionDescriptorsForm() {
        super(getConstants().formProductionDescriptors());
        init(false);
    }

    public LifeCycleResourceProductionDescriptorsForm(boolean isMultipleUpdate) {
        super(getConstants().formProductionDescriptors());
        init(isMultipleUpdate);
    }

    public void init(boolean isMultipleUpdate) {
        ExternalItemLinkItem maintainer = new ExternalItemLinkItem(SiemacMetadataDS.MAINTAINER, getConstants().siemacMetadataStatisticalResourceMaintainer());
        maintainer.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));
        setFields(maintainer);
    }

    public void setLifeCycleResourceDto(LifeCycleStatisticalResourceDto dto) {
        setValue(SiemacMetadataDS.MAINTAINER, dto.getMaintainer());
    }
}
