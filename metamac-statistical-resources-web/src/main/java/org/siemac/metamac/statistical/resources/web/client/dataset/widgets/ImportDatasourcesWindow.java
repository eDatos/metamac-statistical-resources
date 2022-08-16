package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;
import static org.siemac.metamac.statistical.resources.web.shared.utils.StatisticalResourcesSharedTokens.UPLOAD_RESOURCE_TYPE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.shared.utils.ImportableResourceTypeEnum;
import org.siemac.metamac.statistical.resources.web.shared.utils.StatisticalResourcesSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.WarningLabel;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportDatasourcesWindow extends ImportResourceWindow {

    protected WarningLabel warningLabel;

    public ImportDatasourcesWindow() {
        super(getConstants().actionLoadDatasources());

        buildInformationLabel();
        buildWarningLabel();

        UploadDatasourceForm form = new UploadDatasourceForm();
        setForm(form);
    }

    private void buildInformationLabel() {
        InformationLabel informationLabel = new InformationLabel(getMessages().datasourceImportationInfoMessage());
        informationLabel.setWidth(getWidth());
        informationLabel.setMargin(5);
        body.addMember(informationLabel);
    }

    private void buildWarningLabel() {
        warningLabel = new WarningLabel(getMessages().errorFileRequired());
        warningLabel.setWidth(getWidth());
        warningLabel.setMargin(5);
        warningLabel.hide();
        body.addMember(warningLabel, 0);
    }

    public void setDatasetVersion(String datasetVersionUrn) {
        ((HiddenItem) form.getItem(StatisticalResourcesSharedTokens.UPLOAD_PARAM_DATASET_VERSION_URN)).setDefaultValue(datasetVersionUrn);
        setStatisticalOperation(StatisticalResourcesDefaults.getSelectedStatisticalOperation().getCode());
    }

    public void setStatisticalOperation(String statisticalOperationUrn) {
        ((HiddenItem) form.getItem(StatisticalResourcesSharedTokens.UPLOAD_PARAM_OPERATION_CODE)).setDefaultValue(statisticalOperationUrn);
    }

    private class UploadDatasourceForm extends UploadForm {

        public UploadDatasourceForm() {
            super(getConstants().datasources());

            HiddenItem datasetVersionUrnItem = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_PARAM_DATASET_VERSION_URN);
            HiddenItem operationUrnItem = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_PARAM_OPERATION_CODE);
            HiddenItem mustBeZip = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_MUST_BE_ZIP_FILE);
            mustBeZip.setDefaultValue(true);

            HiddenItem resourceTypeItem = new HiddenItem(UPLOAD_RESOURCE_TYPE);
            resourceTypeItem.setDefaultValue(ImportableResourceTypeEnum.DATASOURCE.name());

            List<FormItem> itemsToAdd = new ArrayList<FormItem>();
            itemsToAdd.add(datasetVersionUrnItem);
            itemsToAdd.add(operationUrnItem);
            itemsToAdd.add(mustBeZip);

            addFieldsInThePenultimePosition(itemsToAdd.toArray(new FormItem[itemsToAdd.size()]));

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
                    String displayValue = ImportDatasourcesWindow.this.form.getUploadItem().getDisplayValue();
                    if (StringUtils.isBlank(displayValue)) {
                        warningLabel.show();
                    }
                }
            });
        }

    }

    private String getDimensionHiddenFieldName(String dimensionId) {
        return StatisticalResourcesSharedTokens.UPLOAD_PARAM_DIM_PREFIX + dimensionId;
    }

    @Override
    public String getRelativeURL(String url) {
        return StatisticalResourcesWeb.getRelativeURL(url);
    }

    @Override
    public void showWaitPopup() {
        // no need to show the waitPopup here
    }

    @Override
    protected void initNativeFunctions() {
        initZipComplete(this);
        initZipUploadFailed(this);
    }

    public void uploadZipComplete(String fileName) {
        super.uploadComplete(fileName);
    }

    public void uploadZipFailed(String errorMessage) {
        super.uploadFailed(errorMessage);
    }

    protected native void initZipComplete(ImportDatasourcesWindow upload) /*-{
                                                                          $wnd.uploadZipComplete = function(fileName) {
                                                                          upload.@org.siemac.metamac.statistical.resources.web.client.dataset.widgets.ImportDatasourcesWindow::uploadZipComplete(Ljava/lang/String;)(fileName);
                                                                          };
                                                                          }-*/;

    protected native void initZipUploadFailed(ImportDatasourcesWindow upload) /*-{
                                                                              $wnd.uploadZipFailed = function(errorMessage) {
                                                                              upload.@org.siemac.metamac.statistical.resources.web.client.dataset.widgets.ImportDatasourcesWindow::uploadZipFailed(Ljava/lang/String;)(errorMessage);
                                                                              }
                                                                              }-*/;
}
