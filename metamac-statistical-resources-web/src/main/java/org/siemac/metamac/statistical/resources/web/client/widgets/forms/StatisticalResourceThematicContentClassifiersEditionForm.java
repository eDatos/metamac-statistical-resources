package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.StatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils.DatasetWidgetsUtil;
import org.siemac.metamac.statistical.resources.web.client.model.ds.StatisticalResourceDS;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;

public class StatisticalResourceThematicContentClassifiersEditionForm extends GroupDynamicForm {

    protected String statisticalOperationCode;

    public StatisticalResourceThematicContentClassifiersEditionForm() {
        super(getConstants().formThematicContentClassifiers());
        init(false);
    }

    public StatisticalResourceThematicContentClassifiersEditionForm(boolean isMultipleUpdate) {
        super(getConstants().formThematicContentClassifiers());
        init(isMultipleUpdate);
    }

    private void init(boolean isMultipleUpdate) {

        ExternalItemLinkItem statisticalOperation = new ExternalItemLinkItem(StatisticalResourceDS.STATISTICAL_OPERATION, getConstants().siemacMetadataStatisticalResourceStatisticalOperation());
        statisticalOperation.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        setFields(statisticalOperation);
    }

    public void setStatisticalResourceDto(StatisticalResourceDto dto) {
        statisticalOperationCode = dto.getStatisticalOperation() != null ? dto.getStatisticalOperation().getCode() : null;
        setValue(StatisticalResourceDS.STATISTICAL_OPERATION, dto.getStatisticalOperation());
    }

    public StatisticalResourceDto getStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        return dto;
    }
}
