package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;

import com.smartgwt.client.widgets.form.fields.FormItemIcon;

public class SiemacMetadataContentDescriptorsEditionForm extends LifeCycleResourceContentDescriptorsEditionForm {

    public SiemacMetadataContentDescriptorsEditionForm() {
        addMultilanguagesFields();
    }

    public SiemacMetadataContentDescriptorsEditionForm(boolean isMultilanguageRichTextEditorRequired) {
        super(isMultilanguageRichTextEditorRequired);
        addMultilanguagesFields();
    }

    private void addMultilanguagesFields() {
        MultiLanguageTextItem subtitle = new MultiLanguageTextItem(SiemacMetadataDS.SUBTITLE, getConstants().siemacMetadataStatisticalResourceSubtitle());
        MultiLanguageTextItem titleAlternative = new MultiLanguageTextItem(SiemacMetadataDS.TITLE_ALTERNATIVE, getConstants().siemacMetadataStatisticalResourceTitleAlternative());
        MultiLanguageRichTextEditorItem abstractLogic = new MultiLanguageRichTextEditorItem(SiemacMetadataDS.ABSTRACT, getConstants().siemacMetadataStatisticalResourceAbstractLogic());
        MultiLanguageTextAreaItem keywords = createKeywordsItem();

        addFields(subtitle, titleAlternative, abstractLogic, keywords);
    }

    public void setSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto siemacMetadataStatisticalResourceDto) {
        setLifeCycleStatisticalResourceDto(siemacMetadataStatisticalResourceDto);
        setValue(SiemacMetadataDS.SUBTITLE, siemacMetadataStatisticalResourceDto.getSubtitle());
        setValue(SiemacMetadataDS.TITLE_ALTERNATIVE, siemacMetadataStatisticalResourceDto.getTitleAlternative());
        setValue(SiemacMetadataDS.ABSTRACT, siemacMetadataStatisticalResourceDto.getAbstractLogic());
        setValue(SiemacMetadataDS.KEYWORDS, siemacMetadataStatisticalResourceDto.getKeywords());
    }

    public SiemacMetadataStatisticalResourceDto getSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto siemacMetadataStatisticalResourceDto) {
        siemacMetadataStatisticalResourceDto = (SiemacMetadataStatisticalResourceDto) getLifeCycleStatisticalResourceDto(siemacMetadataStatisticalResourceDto);
        siemacMetadataStatisticalResourceDto.setSubtitle(getValueAsInternationalStringDto(SiemacMetadataDS.SUBTITLE));
        siemacMetadataStatisticalResourceDto.setTitleAlternative(getValueAsInternationalStringDto(SiemacMetadataDS.TITLE_ALTERNATIVE));
        siemacMetadataStatisticalResourceDto.setAbstractLogic(getValueAsInternationalStringDto(SiemacMetadataDS.ABSTRACT));
        siemacMetadataStatisticalResourceDto.setKeywords(getValueAsInternationalStringDto(SiemacMetadataDS.KEYWORDS));
        return siemacMetadataStatisticalResourceDto;
    }

    private MultiLanguageTextAreaItem createKeywordsItem() {
        FormItemIcon infoIcon = new FormItemIcon();
        infoIcon.setSrc(GlobalResources.RESOURCE.info().getURL());
        infoIcon.setPrompt(StatisticalResourcesWeb.getMessages().siemacMetadataStatisticalResourceKeywordsInfo());
        MultiLanguageTextAreaItem keywords = new MultiLanguageTextAreaItem(SiemacMetadataDS.KEYWORDS, getConstants().siemacMetadataStatisticalResourceKeywords());
        keywords.setIcons(infoIcon);
        return keywords;
    }

}
