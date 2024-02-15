package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setExternalItemsValue;

import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.ExternalItemListItem;

public class SiemacMetadataThematicContentClassifiersForm extends StatisticalResourceThematicContentClassifiersForm {

    public SiemacMetadataThematicContentClassifiersForm() {
        super();
        initSiemacMetadataThematicContentClassifiersForm();
    }

    public SiemacMetadataThematicContentClassifiersForm(boolean isMultipleUpdate) {
        super(isMultipleUpdate);
        initSiemacMetadataThematicContentClassifiersForm();
    }

    private void initSiemacMetadataThematicContentClassifiersForm() {
        ExternalItemListItem instances = new ExternalItemListItem(SiemacMetadataDS.STATISTICAL_OPERATION_INSTANCE, getConstants().siemacMetadataStatisticalResourceStatisticalOperationInstance(),
                false);

        addFields(instances);
    }

    public void setSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        setStatisticalResourceDto(dto);
        setExternalItemsValue(getItem(SiemacMetadataDS.STATISTICAL_OPERATION_INSTANCE), dto.getStatisticalOperationInstances());
    }
}
