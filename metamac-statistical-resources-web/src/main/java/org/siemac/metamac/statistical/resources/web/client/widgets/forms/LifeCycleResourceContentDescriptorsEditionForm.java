package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.model.ds.LifeCycleResourceDS;
import org.siemac.metamac.web.common.client.model.record.InternationalStringRecord;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.CustomRequiredValidator;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.utils.FormUtils;

public class LifeCycleResourceContentDescriptorsEditionForm extends GroupDynamicForm {

    protected ProcStatusEnum procStatus;

    public LifeCycleResourceContentDescriptorsEditionForm() {
        super(getConstants().formContentDescriptors());
        final MultiLanguageRichTextEditorItem description = new MultiLanguageRichTextEditorItem(LifeCycleResourceDS.DESCRIPTION, getConstants().nameableStatisticalResourceDescription());
        setFields(description);
    }

    private boolean checkCurrentLanguage(InternationalStringDto internationalString) {
        if (internationalString != null) {
            for (LocalisedStringDto localisedString : internationalString.getTexts()) {
                if (ApplicationEditionLanguages.getCurrentLocale().equals(localisedString.getLocale())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setLifeCycleStatisticalResourceDto(LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto) {
        this.procStatus = lifeCycleStatisticalResourceDto.getProcStatus();
        final MultiLanguageRichTextEditorItem description = (MultiLanguageRichTextEditorItem) getItem(LifeCycleResourceDS.DESCRIPTION);
        description.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                InternationalStringDto internationalString = FormUtils.getJsObjectAttributeAsTypedObject(description.getValue(), InternationalStringRecord.INTERNATIONAL_STRING_DTO);
                return checkCurrentLanguage(internationalString);
            }
        });
        setValue(LifeCycleResourceDS.DESCRIPTION, lifeCycleStatisticalResourceDto.getDescription());
    }

    public LifeCycleStatisticalResourceDto getLifeCycleStatisticalResourceDto(LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto) {
        lifeCycleStatisticalResourceDto.setDescription(getValueAsInternationalStringDto(LifeCycleResourceDS.DESCRIPTION));
        return lifeCycleStatisticalResourceDto;
    }
}
