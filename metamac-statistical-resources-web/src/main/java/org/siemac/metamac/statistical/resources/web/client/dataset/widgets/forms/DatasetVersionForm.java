package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceVersionForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDatePickerItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;

public class DatasetVersionForm extends LifeCycleResourceVersionForm {

    CustomDatePickerItem dateNextUpdate;

    public DatasetVersionForm() {
        super();
        initDatasetVersionForm();
    }

    private void initDatasetVersionForm() {

        ExternalItemLinkItem updateFrequency = new ExternalItemLinkItem(DatasetDS.UPDATE_FRECUENCY, getConstants().datasetUpdateFrequency());
        dateNextUpdate = createFieldDateNextUpdate();
        addFields(dateNextUpdate, updateFrequency);
    }

    private CustomDatePickerItem createFieldDateNextUpdate() {
        return new CustomDatePickerItem(DatasetDS.DATE_NEXT_UPDATE, getConstants().datasetDateNextUpdate(), true, false, CommonUtils.getDateFormatTypeHashMap());
    }

    public void setDatasetVersionDto(DatasetVersionDto datasetDto) {
        setLifeCycleStatisticalResourceDto(datasetDto);
        setValue(DatasetDS.UPDATE_FRECUENCY, datasetDto.getUpdateFrequency());
        dateNextUpdate.setValue(datasetDto.getDateNextUpdate());
    }
}
