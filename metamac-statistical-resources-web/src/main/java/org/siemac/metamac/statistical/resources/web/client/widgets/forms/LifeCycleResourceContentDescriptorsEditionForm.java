package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.model.ds.LifeCycleResourceDS;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageRichTextEditorItem;

public class LifeCycleResourceContentDescriptorsEditionForm extends GroupDynamicForm {

    protected ProcStatusEnum procStatus;

    public LifeCycleResourceContentDescriptorsEditionForm(Boolean isMultilanguageRichTextEditorRequired) {
        super(getConstants().formContentDescriptors());
        initMultilanguageRichTextEditorItem(isMultilanguageRichTextEditorRequired);
    }

    public LifeCycleResourceContentDescriptorsEditionForm() {
        super(getConstants().formContentDescriptors());
        initMultilanguageRichTextEditorItem(true);
    }

    private void initMultilanguageRichTextEditorItem(Boolean isRequired) {
        final MultiLanguageRichTextEditorItem description = new MultiLanguageRichTextEditorItem(LifeCycleResourceDS.DESCRIPTION, getConstants().nameableStatisticalResourceDescription(), isRequired);
        setFields(description);
    }

    public void setLifeCycleStatisticalResourceDto(LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto) {
        this.procStatus = lifeCycleStatisticalResourceDto.getProcStatus();
        setValue(LifeCycleResourceDS.DESCRIPTION, lifeCycleStatisticalResourceDto.getDescription());
    }

    public LifeCycleStatisticalResourceDto getLifeCycleStatisticalResourceDto(LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto) {
        lifeCycleStatisticalResourceDto.setDescription(getValueAsInternationalStringDto(LifeCycleResourceDS.DESCRIPTION));
        return lifeCycleStatisticalResourceDto;
    }
}
