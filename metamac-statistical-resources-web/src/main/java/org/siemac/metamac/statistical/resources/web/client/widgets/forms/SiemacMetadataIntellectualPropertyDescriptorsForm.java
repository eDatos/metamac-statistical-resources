package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

public class SiemacMetadataIntellectualPropertyDescriptorsForm extends GroupDynamicForm {

    public SiemacMetadataIntellectualPropertyDescriptorsForm() {
        super(getConstants().formIntellectualPropertyDescriptors());
        init();
    }

    private void init() {
        ViewMultiLanguageTextItem accessRights = new ViewMultiLanguageTextItem(SiemacMetadataDS.ACCESS_RIGHTS, getConstants().siemacMetadataStatisticalResourceAccessRights());
        ViewTextItem copyrightDate = new ViewTextItem(SiemacMetadataDS.COPYRIGHT_DATE, getConstants().siemacMetadataStatisticalResourceCopyrightedDate());

        addFields(copyrightDate, accessRights);
    }

    public void setSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        setValue(SiemacMetadataDS.ACCESS_RIGHTS, dto.getAccessRights());
        setValue(SiemacMetadataDS.COPYRIGHT_DATE, dto.getCopyrightedDate() != null ? dto.getCopyrightedDate().toString() : null);
    }
}
