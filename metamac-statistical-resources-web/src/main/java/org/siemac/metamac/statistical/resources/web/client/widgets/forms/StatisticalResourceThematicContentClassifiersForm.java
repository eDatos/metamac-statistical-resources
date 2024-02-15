package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.StatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils.DatasetWidgetsUtil;
import org.siemac.metamac.statistical.resources.web.client.model.ds.StatisticalResourceDS;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;

public class StatisticalResourceThematicContentClassifiersForm extends GroupDynamicForm {

    public StatisticalResourceThematicContentClassifiersForm() {
        super(getConstants().formThematicContentClassifiers());
        init(false);
    }

    public StatisticalResourceThematicContentClassifiersForm(boolean isMultipleUpdate) {

        super(getConstants().formThematicContentClassifiers());
        init(isMultipleUpdate);
    }

    private void init(boolean isMultipleUpdate) {
        ExternalItemLinkItem statisticalOperation = new ExternalItemLinkItem(StatisticalResourceDS.STATISTICAL_OPERATION, getConstants().siemacMetadataStatisticalResourceStatisticalOperation());

        statisticalOperation.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        setFields(statisticalOperation);
    }

    public void setStatisticalResourceDto(StatisticalResourceDto dto) {
        setValue(StatisticalResourceDS.STATISTICAL_OPERATION, dto.getStatisticalOperation());
    }
}
