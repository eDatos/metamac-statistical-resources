package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.shared.utils.StatisticalResourcesSharedTokens;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.UploadResourceWithPreviewWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.google.gwt.core.client.Scheduler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;

public abstract class ImportAttributesWithPreviewWindow extends UploadResourceWithPreviewWindow {

    public ImportAttributesWithPreviewWindow(String title, String datasetVersionUrn) {
        super(title);
        List<FormItem> items = new ArrayList<FormItem>();
        CustomButtonItem uploadButton = new CustomButtonItem("button-import", MetamacWebCommon.getConstants().accept());
        uploadButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                submitIfValid();
            }
        });

        HiddenItem updateParamDatasetVersionUrn = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_PARAM_DATASET_VERSION_URN);
        updateParamDatasetVersionUrn.setValue(datasetVersionUrn);
        mainForm.addFields(updateParamDatasetVersionUrn);
        items.add(uploadButton);
        extraForm.setFields(items.toArray(new FormItem[items.size()]));
    }

    @Override
    public String getRelativeURL(String url) {
        return StatisticalResourcesWeb.getRelativeURL(url);
    }

    @Override
    protected UploadForm buildMainUploadForm() {
        return new UploadAttributeForm();
    }

    @Override
    protected CustomDynamicForm buildExtraForm() {
        CustomDynamicForm form = new CustomDynamicForm();
        form.setWidth(420);
        form.setVisible(false);
        return form;
    }

    @Override
    protected void copyHiddenValuesToMainForm(UploadForm mainForm, DynamicForm extraForm) {
        // NOTHING
    }

    @Override
    protected void onPreviewComplete(String response) {
        extraForm.clearValues();
        extraForm.setVisible(true);
    }

    @Override
    protected void onPreviewFailed(String errorMessage) {
        uploadFailed(errorMessage);
    }

    @Override
    protected void onSubmitComplete(String response) {
        uploadSuccess(response);
    }

    @Override
    protected void onSubmitFailed(String errorMessage) {
        uploadFailed(errorMessage);
    }

    protected abstract void uploadFailed(String error);
    protected abstract void uploadSuccess(String message);

    
    private class UploadAttributeForm extends UploadForm {

        private UploadItem uploadItem;

        public UploadAttributeForm() {
            super();

            uploadItem = new UploadItem("file-name");
            uploadItem.setTitle(getConstants().datasetDatasource());
            uploadItem.setWidth(400);
            uploadItem.setRequired(true);
            uploadItem.setTitleStyle("requiredFormLabel");

            uploadItem.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

                @Override
                public void onChange(com.smartgwt.client.widgets.form.fields.events.ChangeEvent event) {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            submitPreviewIfValid();
                        }
                    });
                }
            });

            HiddenItem loadParamAttributes = new HiddenItem(StatisticalResourcesSharedTokens.LOAD_PARAM_ATTRIBUTES);
            loadParamAttributes.setValue(true);
            setFields(uploadItem, loadParamAttributes);
        }

        @Override
        public UploadItem getUploadItem() {
            return uploadItem;
        }
    }
}
