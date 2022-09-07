package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.getExternalItemsValue;
import static org.siemac.metamac.statistical.resources.web.shared.utils.StatisticalResourcesSharedTokens.UPLOAD_RESOURCE_TYPE;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.ArrayUtils;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.VersionRationaleTypeDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.base.utils.SiemacMetadataExternalField;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.SearchVersionRationaleTypeItem;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetListUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.model.ds.LifeCycleResourceDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.shared.utils.ImportableResourceTypeEnum;
import org.siemac.metamac.statistical.resources.web.shared.utils.StatisticalResourcesSharedTokens;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.constants.CommonWebConstants;
import org.siemac.metamac.web.common.client.utils.CustomRequiredValidator;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.UploadResourceWithPreviewWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchSrmListItemWithSchemeFilterItem;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

import com.google.gwt.core.client.Scheduler;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;

public abstract class ImportZipDatasourceWithMappingWindow extends UploadResourceWithPreviewWindow {
    private DatasetListUiHandlers             uiHandlers;

    private static int formWidth = 600;
    private static int formWidthFields = 400;
    InformationLabel informationLabel;
    
    private static final String[] REQUIRED_FIELDS = new String[]{VersionableResourceDS.VERSION_RATIONALE_TYPES, VersionableResourceDS.NEXT_VERSION, VersionableResourceDS.DATE_NEXT_VERSION,
            LifeCycleResourceDS.PROC_STATUS};
    
    private static final String[] REQUIRED_FIELDS_SCHEDULED_UPDATE = ArrayUtils.addStringElementsToStringArray(REQUIRED_FIELDS, DatasetDS.DATE_NEXT_UPDATE);

    protected ImportZipDatasourceWithMappingWindow() {
        super(getConstants().actionLoadDatasource());
        addFieldsInMainForm();
        addFieldsInExtraForm();
        addRequiredFieldsInExtraForm(false);
    }

    @Override
    protected UploadForm buildMainUploadForm() {
        return new UploadDatasourceForm();
    }
        
    @Override
    protected CustomDynamicForm buildExtraForm() {
        CustomDynamicForm form = new CustomDynamicForm();
        form.setWidth(formWidth);
        form.setMargin(1);
        form.setVisible(false);
        return form;
    }

    public void setStatisticalOperation(String statisticalOperationUrn) {
        ((HiddenItem) mainForm.getItem(StatisticalResourcesSharedTokens.UPLOAD_PARAM_OPERATION_CODE)).setDefaultValue(statisticalOperationUrn);
    }
    
    private void addFieldsInMainForm() {

        List<FormItem> itemsToAdd = new ArrayList<FormItem>();

        HiddenItem operationUrnItem = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_PARAM_OPERATION_CODE);

        HiddenItem mustBeZip = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_MUST_BE_ZIP_FILE);
        mustBeZip.setDefaultValue(true);
                   
        HiddenItem resourceTypeItem = new HiddenItem(UPLOAD_RESOURCE_TYPE);
        resourceTypeItem.setDefaultValue(ImportableResourceTypeEnum.DATASOURCE.name());

        itemsToAdd.add(operationUrnItem);
        itemsToAdd.add(mustBeZip);
        
        itemsToAdd.addAll(addExtraFieldsToMainForm());
        
        mainForm.addFields(itemsToAdd.toArray(new HiddenItem[itemsToAdd.size()]));
    }
   
    private List<FormItem> addExtraFieldsToMainForm() {
        List<FormItem> extraItemsToAdd = new ArrayList<FormItem>();
        
        HiddenItem extraFields = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_HAS_EXTRA_FIELDS);
        extraFields.setDefaultValue(true);
        HiddenItem dataProviderItem = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_DATA_PROVIDER);
        HiddenItem versionRationaleTypeItem = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_VERSION_RATIONALE_TYPES);
        HiddenItem nextVersionItem = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_NEXT_VERSION);
        HiddenItem nextVersionDate = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_DATE_NEXT_VERSION);
        HiddenItem nextUpdateDate = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_DATE_NEXT_UPDATE);
        HiddenItem procStatus = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_PROC_STATUS);
        extraItemsToAdd.add(extraFields);
        extraItemsToAdd.add(dataProviderItem);
        extraItemsToAdd.add(versionRationaleTypeItem);
        extraItemsToAdd.add(nextVersionItem);
        extraItemsToAdd.add(nextVersionDate);
        extraItemsToAdd.add(nextUpdateDate);
        extraItemsToAdd.add(procStatus);
        return extraItemsToAdd;
    }
    
    private void addFieldsInExtraForm() {

        List<FormItem> items = new ArrayList<FormItem>();
      
        items.addAll(addExtraFields());
                      
        CustomButtonItem uploadButton = new CustomButtonItem("button-import", MetamacWebCommon.getConstants().accept());
        uploadButton.setAlign(Alignment.CENTER);
        uploadButton.setColSpan(3);
        uploadButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                submitIfValid();
            }
        });

        items.add(uploadButton);

        extraForm.setFields(items.toArray(new FormItem[items.size()]));
        
    }

    private void addRequiredFieldsInExtraForm(boolean isScheduledUpdate) {
        extraForm.resetRequiredTitleSuffix();
        if (isScheduledUpdate) {
            extraForm.setRequiredTitleSuffix(REQUIRED_FIELDS_SCHEDULED_UPDATE);
        } else {
            extraForm.setRequiredTitleSuffix(REQUIRED_FIELDS);
        }
    }

    private List<FormItem> addExtraFields() {

        List<FormItem> extraItemsToAdd = new ArrayList<FormItem>();
        extraItemsToAdd.add(addFieldDataProviderItem());
        extraItemsToAdd.add(addFieldSearchVersionRationaleTypeItem());
        extraItemsToAdd.add(addFieldNextVersion());
        extraItemsToAdd.add(addFieldNextVersionDate());
        extraItemsToAdd.add(addFieldNextUpdateDate());
        extraItemsToAdd.add(addFieldProcStatus());
        return extraItemsToAdd;
    }

    private CustomDateItem addFieldNextVersionDate() {
        CustomDateItem nextVersionDate = new CustomDateItem(VersionableResourceDS.DATE_NEXT_VERSION, getConstants().versionableStatisticalResourceNextVersionDate());
        nextVersionDate.setTitleColSpan(2);
        return nextVersionDate;
   
    }

    private CustomDateItem addFieldNextUpdateDate() {
        CustomDateItem nextUpdateDate = new CustomDateItem(DatasetDS.DATE_NEXT_UPDATE, getConstants().datasetDateNextUpdate());
        nextUpdateDate.setTitleColSpan(2);
        return nextUpdateDate;
    }
    
    
    private CustomSelectItem addFieldNextVersion() {
        final CustomSelectItem nextVersion = new CustomSelectItem(VersionableResourceDS.NEXT_VERSION, getConstants().versionableStatisticalResourceNextVersion());
        nextVersion.setValueMap(CommonUtils.getStatisticalResourceNextVersionHashMap());
        nextVersion.setRequired(true);
        nextVersion.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

            @Override
            public void onChange(com.smartgwt.client.widgets.form.fields.events.ChangeEvent event) {
                String nextVersionValue = event.getValue().toString();
                CustomDateItem dateNextVersion = ((CustomDateItem) extraForm.getItem(VersionableResourceDS.DATE_NEXT_VERSION));
                CustomDateItem dateNextUpdate = ((CustomDateItem) extraForm.getItem(DatasetDS.DATE_NEXT_UPDATE));
                if (nextVersionValue != null && NextVersionTypeEnum.SCHEDULED_UPDATE.equals(NextVersionTypeEnum.valueOf(nextVersionValue))) {
                    setRequiredCustomDateItem(dateNextVersion, true);
                    setRequiredCustomDateItem(dateNextUpdate, true);                   
                    addRequiredFieldsInExtraForm(true);
                     dateNextVersion.show();
                    
                } else {
                    if (Boolean.TRUE.equals(dateNextVersion.isVisible())) {
                        setRequiredCustomDateItem(dateNextVersion, false);
                        setRequiredCustomDateItem(dateNextUpdate, false);
                        addRequiredFieldsInExtraForm(false);
                        dateNextVersion.clearValue();
                        dateNextVersion.clearValue();
                        dateNextVersion.hide();
                    }
                }
            }
        });
        nextVersion.setTitleColSpan(2);
        nextVersion.setWidth(formWidthFields);

        return nextVersion;
    }

    private CustomSelectItem addFieldProcStatus() {
        final CustomSelectItem procStatus = new CustomSelectItem(LifeCycleResourceDS.PROC_STATUS, getConstants().lifeCycleStatisticalResourceProcStatus());
        LinkedHashMap<String, String>  mapaProcStatus = CommonUtils.getProcStatusHashMap();
        mapaProcStatus.remove(ProcStatusEnum.VALIDATION_REJECTED.getName());
        procStatus.setValueMap(mapaProcStatus);
        procStatus.setRequired(true);   
        procStatus.setTitleColSpan(2);
        procStatus.setWidth(formWidthFields);

        return procStatus;
    }
    
    private void setRequiredCustomDateItem(CustomDateItem date, boolean isRequired) {
        date.setRequired(isRequired);
    }
    
    private SearchVersionRationaleTypeItem addFieldSearchVersionRationaleTypeItem() {
        final SearchVersionRationaleTypeItem searchVersionRationaleTypeItem = new SearchVersionRationaleTypeItem(VersionableResourceDS.VERSION_RATIONALE_TYPES,
                getConstants().versionableStatisticalResourceVersionRationaleTypes(), true);
          searchVersionRationaleTypeItem.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                return !searchVersionRationaleTypeItem.getSelectedVersionRationaleTypeDtos().isEmpty();
            }
        });
        searchVersionRationaleTypeItem.setWidth(formWidthFields);
        searchVersionRationaleTypeItem.setTitleColSpan(2);
        return searchVersionRationaleTypeItem;
    }
    
    private List<VersionRationaleTypeDto> getDefaultVersionRationaleType() {
        List<VersionRationaleTypeDto> versionRationaleTypesDto = new ArrayList<VersionRationaleTypeDto>();
        versionRationaleTypesDto.add(new VersionRationaleTypeDto(VersionRationaleTypeEnum.MINOR_DATA_UPDATE));
        return versionRationaleTypesDto;
    }

    // ***************************************************************************************
    // DATA PROVIDER
    // ***************************************************************************************

    public void setDataProvider(List<ExternalItemDto> items, int firstResult, int totalResults) {
        SearchSrmListItemWithSchemeFilterItem dataProviderItem = (SearchSrmListItemWithSchemeFilterItem) extraForm.getItem(SiemacMetadataDS.DATA_PROVIDER);
        dataProviderItem.setResources(items, firstResult, totalResults);
    }

    public void setDataProviderSchemes(List<ExternalItemDto> items, int firstResult, int totalResults) {
        SearchSrmListItemWithSchemeFilterItem dataProviderItem = (SearchSrmListItemWithSchemeFilterItem) extraForm.getItem(SiemacMetadataDS.DATA_PROVIDER);
        dataProviderItem.setFilterResources(items, firstResult, totalResults);
    }

    private SearchSrmListItemWithSchemeFilterItem addFieldDataProviderItem() {
        SearchSrmListItemWithSchemeFilterItem searchSrmListItemWithSchemeFilterItem = new SearchSrmListItemWithSchemeFilterItem(SiemacMetadataDS.DATA_PROVIDER,
                getConstants().siemacMetadataStatisticalResourceDataProvider(), CommonWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveItemSchemes(int firstResult, int maxResults, SrmExternalResourceRestCriteria webCriteria) {
                getUiHandlers().retrieveDataProviderSchemes(firstResult, maxResults, webCriteria, SiemacMetadataExternalField.DATA_PROVIDER);
            }

            @Override
            protected void retrieveItems(int firstResult, int maxResults, SrmItemRestCriteria webCriteria) {
                getUiHandlers().retrieveDataProviderUnits(firstResult, maxResults, webCriteria, SiemacMetadataExternalField.DATA_PROVIDER);
            }
        };

        searchSrmListItemWithSchemeFilterItem.setWidth(formWidthFields);
        searchSrmListItemWithSchemeFilterItem.setTitleColSpan(2);
        return searchSrmListItemWithSchemeFilterItem;
    }
     
    public void setUiHandlers(DatasetListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    private DatasetListUiHandlers getUiHandlers() {
        return uiHandlers;
    }
    
    @Override
    protected void copyHiddenValuesToMainForm(UploadForm mainForm, DynamicForm extraForm) {
        setFormFieldDataProviderItem();
        setFormFieldVersionRationaleTypeItem();
        setFormFieldNextVersion();
        setFormFieldDateNextVersion();
        setFormFieldDateNextUpdate();
        setFormFieldProcStatus();

    }

    private void setFormFieldProcStatus() {
        mainForm.setValue(StatisticalResourcesSharedTokens.UPLOAD_PROC_STATUS, getFieldProcStatus());
    }
    
    private String getFieldProcStatus() {
        return !StringUtils.isBlank(extraForm.getValueAsString(LifeCycleResourceDS.PROC_STATUS)) ? extraForm.getValueAsString(LifeCycleResourceDS.PROC_STATUS) : null;
    }
    
    private void setFormFieldNextVersion() {
        mainForm.setValue(StatisticalResourcesSharedTokens.UPLOAD_NEXT_VERSION, getFieldNextVersion());
    }

    private String getFieldNextVersion() {
        return !StringUtils.isBlank(extraForm.getValueAsString(VersionableResourceDS.NEXT_VERSION)) ? extraForm.getValueAsString(VersionableResourceDS.NEXT_VERSION) : null;
    }

    private void setFormFieldDateNextVersion() {
        String nextVersionValue = getFieldNextVersion();
        Date dateNextVersionValue = null;
        if (nextVersionValue != null && NextVersionTypeEnum.SCHEDULED_UPDATE.equals(NextVersionTypeEnum.valueOf(nextVersionValue))) {
            dateNextVersionValue = ((CustomDateItem) extraForm.getItem(VersionableResourceDS.DATE_NEXT_VERSION)).getValueAsDate();
        }
        mainForm.setValue(StatisticalResourcesSharedTokens.UPLOAD_DATE_NEXT_VERSION, dateNextVersionValue);
    }

    private void setFormFieldDateNextUpdate() {
        Date dateNextUpdateValue = ((CustomDateItem) extraForm.getItem(DatasetDS.DATE_NEXT_UPDATE)).getValueAsDate();
        mainForm.setValue(StatisticalResourcesSharedTokens.UPLOAD_DATE_NEXT_UPDATE, dateNextUpdateValue);
    }
    
    private void setFormFieldVersionRationaleTypeItem() {
        List<VersionRationaleTypeDto> versionRationaleTypeDto = new ArrayList<VersionRationaleTypeDto>();
        versionRationaleTypeDto.addAll(((SearchVersionRationaleTypeItem) extraForm.getItem(VersionableResourceDS.VERSION_RATIONALE_TYPES)).getSelectedVersionRationaleTypeDtos());

        StringBuilder versionRationaleTypes = new StringBuilder();
        for (VersionRationaleTypeDto item : versionRationaleTypeDto) {
            if (versionRationaleTypes.length() != 0) {
                versionRationaleTypes.append(",");
            }
            versionRationaleTypes.append(item.getValue().getName());
        }

        mainForm.setValue(StatisticalResourcesSharedTokens.UPLOAD_VERSION_RATIONALE_TYPES, versionRationaleTypes.toString());
    }
    
    private void setFormFieldDataProviderItem() {
        List<ExternalItemDto> dataProvidersDto = new ArrayList<ExternalItemDto>();
        dataProvidersDto.addAll(getExternalItemsValue(extraForm.getItem(SiemacMetadataDS.DATA_PROVIDER)));
        StringBuilder dataProviders = new StringBuilder();
        for (ExternalItemDto item : dataProvidersDto) {
            if (dataProviders.length() != 0) {
                dataProviders.append(",");
            }
            dataProviders.append(item.getUrn());
        }
     
        mainForm.setValue(StatisticalResourcesSharedTokens.UPLOAD_DATA_PROVIDER, dataProviders.toString());
    }
    
    @Override
    protected void onPreviewComplete(String response) {
        extraForm.clearValues();
        clearExtraFormValues();
        extraForm.setVisible(true);
        informationLabel.setVisible(true);
       
    }

    @Override
    public String getRelativeURL(String url) {
        return StatisticalResourcesWeb.getRelativeURL(url);
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

    @Override
    public void show() {
        clearExtraFormValues();
        extraForm.hide();
        informationLabel.hide();
        super.show();
    }

    
    private void clearExtraFormValues() {
        SearchSrmListItemWithSchemeFilterItem dataProviderItem = (SearchSrmListItemWithSchemeFilterItem) extraForm.getItem(SiemacMetadataDS.DATA_PROVIDER);
        dataProviderItem.clearRelatedResourceList();
        
        SearchVersionRationaleTypeItem searchVersionRationaleTypeItem = ((SearchVersionRationaleTypeItem) extraForm.getItem(VersionableResourceDS.VERSION_RATIONALE_TYPES));
        searchVersionRationaleTypeItem.setVersionRationaleTypes(getDefaultVersionRationaleType());

        CustomSelectItem nextVersion = ((CustomSelectItem) extraForm.getItem(VersionableResourceDS.NEXT_VERSION));
        nextVersion.clearValue();

        CustomDateItem nextVersionDate = ((CustomDateItem) extraForm.getItem(VersionableResourceDS.DATE_NEXT_VERSION));
        nextVersionDate.setRequired(false);
        nextVersionDate.clearValue();
        nextVersionDate.hide();
        
        CustomDateItem nextUpdateDate = ((CustomDateItem) extraForm.getItem(DatasetDS.DATE_NEXT_UPDATE));
        nextUpdateDate.setTitle(getConstants().datasetDateNextUpdate());
        nextUpdateDate.setRequired(false);
        nextUpdateDate.clearValue();
        
        addRequiredFieldsInExtraForm(false);
    }

    private class UploadDatasourceForm extends UploadForm {

        private UploadItem uploadItem;

        private void buildInformationLabel() {
            informationLabel = new InformationLabel(getMessages().datasourceImportationInfoLifeCicleRestrictionsMessage());
            informationLabel.setWidth(formWidth);
            informationLabel.setMargin(5);
            informationLabel.hide();
            body.addMember(informationLabel);
        }
        
        public UploadDatasourceForm() {
            super();
            this.setWidth(610);
            uploadItem = new UploadItem("file-name");
            uploadItem.setWidth(formWidth);
            uploadItem.setTitle(getConstants().datasetDatasource());
            uploadItem.setTitleColSpan(2);
            uploadItem.setWidth(formWidthFields);
            uploadItem.setRequired(true);
            buildInformationLabel();

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

            HiddenItem datasetVersionUrnItem = new HiddenItem(StatisticalResourcesSharedTokens.UPLOAD_PARAM_DATASET_VERSION_URN);

            setFields(uploadItem, datasetVersionUrnItem);
        }
        
        @Override
        public UploadItem getUploadItem() {
            return uploadItem;
        }
        
    }
}
