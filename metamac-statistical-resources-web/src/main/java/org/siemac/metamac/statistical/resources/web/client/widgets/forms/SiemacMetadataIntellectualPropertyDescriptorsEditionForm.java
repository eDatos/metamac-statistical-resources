package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils.DatasetWidgetsUtil;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

public class SiemacMetadataIntellectualPropertyDescriptorsEditionForm extends GroupDynamicForm {

    public SiemacMetadataIntellectualPropertyDescriptorsEditionForm() {
        super(getConstants().formIntellectualPropertyDescriptors());
        init(false);
    }

    public SiemacMetadataIntellectualPropertyDescriptorsEditionForm(boolean isMultipleUpdate) {
        super(getConstants().formIntellectualPropertyDescriptors());
        init(isMultipleUpdate);
    }

    private void init(boolean isMultipleUpdate) {

        MultiLanguageTextItem accessRights = new MultiLanguageTextItem(SiemacMetadataDS.ACCESS_RIGHTS, getConstants().siemacMetadataStatisticalResourceAccessRights());
        ViewTextItem copyrightDate = new ViewTextItem(SiemacMetadataDS.COPYRIGHT_DATE, getConstants().siemacMetadataStatisticalResourceCopyrightedDate());
        copyrightDate.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        addFields(copyrightDate, accessRights);
    }

    public void setSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        setValue(SiemacMetadataDS.ACCESS_RIGHTS, dto.getAccessRights());
        setValue(SiemacMetadataDS.COPYRIGHT_DATE, dto.getCopyrightedDate() != null ? dto.getCopyrightedDate().toString() : null);

    }

    public SiemacMetadataStatisticalResourceDto getSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        dto.setAccessRights(getValueAsInternationalStringDto(SiemacMetadataDS.ACCESS_RIGHTS));
        return dto;
    }
}
