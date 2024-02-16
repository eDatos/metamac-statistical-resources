package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setExternalItemsValue;

import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.ExternalItemListItem;

public class SiemacMetadataLanguageForm extends GroupDynamicForm {

    public SiemacMetadataLanguageForm() {
        super(getConstants().formLanguages());

        ExternalItemLinkItem language = new ExternalItemLinkItem(SiemacMetadataDS.LANGUAGE, getConstants().siemacMetadataStatisticalResourceLanguage());
        ExternalItemListItem languages = new ExternalItemListItem(SiemacMetadataDS.LANGUAGES, getConstants().siemacMetadataStatisticalResourceLanguages(), false);

        setFields(language, languages);
    }

    public void setSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        setValue(SiemacMetadataDS.LANGUAGE, dto.getLanguage());
        setExternalItemsValue(getItem(SiemacMetadataDS.LANGUAGES), dto.getLanguages());
    }
}
