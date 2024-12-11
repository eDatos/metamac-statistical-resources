package org.siemac.metamac.statistical.resources.web.client.publication.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;
import static org.siemac.metamac.statistical.resources.web.shared.utils.StatisticalResourcesSharedTokens.UPLOAD_PARAM_LANGUAGE;
import static org.siemac.metamac.statistical.resources.web.shared.utils.StatisticalResourcesSharedTokens.UPLOAD_PARAM_PUBLICATION_VERSION_URN;
import static org.siemac.metamac.statistical.resources.web.shared.utils.StatisticalResourcesSharedTokens.UPLOAD_RESOURCE_TYPE;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.publication.view.handlers.PublicationStructureTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.shared.utils.ImportableResourceTypeEnum;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;
import org.siemac.metamac.web.common.client.widgets.WarningLabel;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportPublicationStructureWindow extends ImportResourceWindow {

    private PublicationStructureTabUiHandlers uiHandlers;
    protected WarningLabel                    warningLabel;

    public ImportPublicationStructureWindow() {
        super(getConstants().actionImport());

        buildWarningLabel();

        UploadStructureForm form = new UploadStructureForm();
        setForm(form);
    }

    private void buildWarningLabel() {
        warningLabel = new WarningLabel(getMessages().errorFileRequired());
        warningLabel.setWidth(getWidth());
        warningLabel.setMargin(5);
        warningLabel.hide();
        body.addMember(warningLabel, 0);
    }

    public void setPublicationVersionUrn(String urn) {
        ((HiddenItem) form.getItem(UPLOAD_PARAM_PUBLICATION_VERSION_URN)).setDefaultValue(urn);
    }

    private class UploadStructureForm extends UploadForm {

        public UploadStructureForm() {
            super(getConstants().publicationStructure());

            HiddenItem resourceTypeItem = new HiddenItem(UPLOAD_RESOURCE_TYPE);
            resourceTypeItem.setDefaultValue(ImportableResourceTypeEnum.PUBLICATION_VERSION_STRUCTURE.name());

            HiddenItem publicationVersionUrnItem = new HiddenItem(UPLOAD_PARAM_PUBLICATION_VERSION_URN);

            CustomSelectItem languageItem = new CustomSelectItem(UPLOAD_PARAM_LANGUAGE, getConstants().publicationStructureImportationLanguage());
            languageItem.setValueMap(CommonUtils.getEditionLanguagesHashMap());
            if (ApplicationEditionLanguages.getLocales() != null && !ApplicationEditionLanguages.getLocales().isEmpty()) {
                languageItem.setDefaultValue(ApplicationEditionLanguages.getLocales().get(0));
            }
            languageItem.setWidth(50);

            addFieldsInThePenultimePosition(resourceTypeItem, publicationVersionUrnItem, languageItem);

            prepareRequiredFileCheck();
        }

        private void prepareRequiredFileCheck() {
            getUploadItem().addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

                @Override
                public void onChange(com.smartgwt.client.widgets.form.fields.events.ChangeEvent event) {
                    warningLabel.hide();
                }
            });

            getUploadButton().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                @Override
                public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                    String displayValue = ImportPublicationStructureWindow.this.form.getUploadItem().getDisplayValue();
                    if (StringUtils.isBlank(displayValue)) {
                        warningLabel.show();
                    }
                }
            });
        }
    }

    @Override
    public String getRelativeURL(String url) {
        return StatisticalResourcesWeb.getRelativeURL(url);
    }

    @Override
    public void showWaitPopup() {
        getUiHandlers().showWaitPopup();
    }

    public void setUiHandlers(PublicationStructureTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public PublicationStructureTabUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
