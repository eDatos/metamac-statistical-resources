package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;
import static org.siemac.metamac.statistical.resources.web.shared.utils.StatisticalResourcesSharedTokens.UPLOAD_RESOURCE_TYPE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.VersionRationaleTypeDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.SearchVersionRationaleTypeItem;
import org.siemac.metamac.statistical.resources.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.shared.utils.ImportableResourceTypeEnum;
import org.siemac.metamac.statistical.resources.web.shared.utils.StatisticalResourcesSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.WarningLabel;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportDatasourcesWindow extends ImportResourceWindow {

    protected WarningLabel warningLabel;
    protected WarningLabel warningLabelVersionRationaleType;
    
    public ImportDatasourcesWindow(Boolean hasExtraFields) {
        super(getConstants().actionLoadDatasources());
        buildExtraFieldsConfiguration(hasExtraFields);
        buildInformationLabel();
        buildWarningLabel();
        UploadDatasourceForm form = new UploadDatasourceForm(hasExtraFields);
        setForm(form);
       
    }

    private void buildExtraFieldsConfiguration(Boolean hasExtraFields) {
        if (Boolean.TRUE.equals(hasExtraFields)) {
            this.setHeight(250);
        }
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
        private Boolean hasExtraFields;
        
        public UploadDatasourceForm(Boolean hasExtraFields) {
            super(getConstants().datasources());
            this.hasExtraFields = hasExtraFields;
            
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
           
            if (Boolean.TRUE.equals(this.hasExtraFields)) {
                itemsToAdd.addAll(addExtraFields());
            }
            
            addFieldsInThePenultimePosition(itemsToAdd.toArray(new FormItem[itemsToAdd.size()]));

            prepareRequiredFileCheck();
        }

        private List<FormItem> addExtraFields() {
         
            HiddenItem extraFields = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_HAS_EXTRA_FIELDS);
            extraFields.setDefaultValue(true);
            
            List<FormItem> extraItemsToAdd = new ArrayList<FormItem>();
            HiddenItem versionRationaleTypeItemInternal = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_VERSION_RATIONALE_TYPES);
            final SearchVersionRationaleTypeItem searchVersionRationaleTypeItem = new SearchVersionRationaleTypeItem(VersionableResourceDS.VERSION_RATIONALE_TYPES, getConstants()
                    .versionableStatisticalResourceVersionRationaleTypes(), true);
            searchVersionRationaleTypeItem.setVersionRationaleTypes(getDefaultVersionRationaleType());
            //searchVersionRationaleTypeItem.setRequired(true);
            searchVersionRationaleTypeItem.setWidth(350);
            
            HiddenItem nextVersionItemInternal = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_NEXT_VERSION);
            final CustomSelectItem nextVersion = new CustomSelectItem(VersionableResourceDS.NEXT_VERSION, getConstants().versionableStatisticalResourceNextVersion());
            nextVersion.setValueMap(CommonUtils.getStatisticalResourceNextVersionHashMap());
            //nextVersion.setRequired(true);
            nextVersion.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

                @Override
                public void onChange(com.smartgwt.client.widgets.form.fields.events.ChangeEvent event) {
                    String nextVersionValue = event.getValue().toString();
                    CustomDateItem dateNextVersion = ((CustomDateItem) getItem(VersionableResourceDS.DATE_NEXT_VERSION));
                    if (nextVersionValue != null && NextVersionTypeEnum.SCHEDULED_UPDATE.equals(NextVersionTypeEnum.valueOf(nextVersionValue))) {
                        dateNextVersion.show();
                    } else {
                        if (Boolean.TRUE.equals(dateNextVersion.isVisible())) {
                            dateNextVersion.hide();
                        }
                    }
                }
            });
            nextVersion.setWidth(350);
            
            HiddenItem nextVersionDateInternal = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_DATE_NEXT_VERSION);
            CustomDateItem nextVersionDate = new CustomDateItem(VersionableResourceDS.DATE_NEXT_VERSION, getConstants().versionableStatisticalResourceNextVersionDate());
            
            extraItemsToAdd.add(extraFields);
            extraItemsToAdd.add(versionRationaleTypeItemInternal);
            extraItemsToAdd.add(searchVersionRationaleTypeItem);
            extraItemsToAdd.add(nextVersionItemInternal);
            extraItemsToAdd.add(nextVersion);
            extraItemsToAdd.add(nextVersionDateInternal);
            extraItemsToAdd.add(nextVersionDate);
            return extraItemsToAdd;
        }
     
        private List<VersionRationaleTypeDto> getDefaultVersionRationaleType() {
            List<VersionRationaleTypeDto> versionRationaleTypesDto = new ArrayList<VersionRationaleTypeDto>();
            versionRationaleTypesDto.add(new VersionRationaleTypeDto(VersionRationaleTypeEnum.MINOR_DATA_UPDATE));
            return versionRationaleTypesDto;
        }
        
        private void prepareRequiredFileCheck() {
            getUploadItem().addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

                @Override
                public void onChange(com.smartgwt.client.widgets.form.fields.events.ChangeEvent event) {
                    String displayValue = ImportDatasourcesWindow.this.form.getUploadItem().getDisplayValue();
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
    
                    setExtraFields();
                }
            });
        }

        private void setExtraFields() {
            if (Boolean.TRUE.equals(this.hasExtraFields)) {
                setFormFieldVersionRationaleTypeItem();
                setFormFieldNextVersion();
                setFormFieldDateNextVersion();
            }
        }

        private void setFormFieldNextVersion() {
            form.setValue(StatisticalResourcesSharedTokens.UPLOAD_NEXT_VERSION, getFieldNextVersion());
        }

        private String getFieldNextVersion() {
            return !StringUtils.isBlank(getValueAsString(VersionableResourceDS.NEXT_VERSION)) ? getValueAsString(VersionableResourceDS.NEXT_VERSION) : null;
        }
        
        private void setFormFieldDateNextVersion() {
            String nextVersionValue = getFieldNextVersion();
            Date dateNextVersionValue = null;
            if (nextVersionValue != null && NextVersionTypeEnum.SCHEDULED_UPDATE.equals(NextVersionTypeEnum.valueOf(nextVersionValue))) {
                dateNextVersionValue = ((CustomDateItem) getItem(VersionableResourceDS.DATE_NEXT_VERSION)).getValueAsDate();
            }
            form.setValue(StatisticalResourcesSharedTokens.UPLOAD_DATE_NEXT_VERSION, dateNextVersionValue);
        }
        
        private void setFormFieldVersionRationaleTypeItem() {
            List<VersionRationaleTypeDto> versionRationaleTypeDto = new ArrayList<VersionRationaleTypeDto>();
            versionRationaleTypeDto.addAll(((SearchVersionRationaleTypeItem) getItem(VersionableResourceDS.VERSION_RATIONALE_TYPES)).getSelectedVersionRationaleTypeDtos());
            
            StringBuilder versionRationaleTypes = new StringBuilder();
            for (VersionRationaleTypeDto item : versionRationaleTypeDto) {
                if (versionRationaleTypes.length() != 0) {
                    versionRationaleTypes.append(",");
                }
                versionRationaleTypes.append(item.getValue().getName());
            }
            
            form.setValue(StatisticalResourcesSharedTokens.UPLOAD_VERSION_RATIONALE_TYPES, versionRationaleTypes.toString());
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
