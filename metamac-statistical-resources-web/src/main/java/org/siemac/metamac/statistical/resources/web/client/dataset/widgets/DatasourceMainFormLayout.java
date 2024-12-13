package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DataSourceTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetDatasourcesTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.MainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.core.client.Scheduler;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;

public class DatasourceMainFormLayout extends MainFormLayout {

    private DatasetDatasourcesTabUiHandlers uiHandlers;

    private GroupDynamicForm                datasourceForm;
    private GroupDynamicForm                datasourceEditionForm;

    private DatasetVersionDto               datasetVersionDto;

    public DatasourceMainFormLayout() {
        super();
        setStyles();

        setViewMode();

        createViewForm();
        createEditionForm();

        bindEvents();
    }

    public void setDatasetVersionDto(DatasetVersionDto datasetVersionDto) {
        this.datasetVersionDto = datasetVersionDto;

        setViewMode();

        setDatasetVersionDtoViewMode(datasetVersionDto);
        setDatasetVersionDtoEditionMode(datasetVersionDto);

        setCanDelete(Boolean.FALSE);
        setCanEdit(DatasetClientSecurityUtils.canUpdateDatasetVersion(datasetVersionDto));
    }

    public DatasetDatasourcesTabUiHandlers getUiHandlers() {
        return uiHandlers;
    }
    public void setUiHandlers(DatasetDatasourcesTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    private void setStyles() {
        setStyleName("normal");
    }
    private void setDatasetVersionDtoViewMode(DatasetVersionDto datasetVersionDto) {
        datasourceForm.setValue(DatasetDS.KEEP_ALL_DATA, Boolean.TRUE.equals(datasetVersionDto.isKeepAllData()) ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no());
        datasourceForm.setValue(DatasetDS.DATA_SOURCE_TYPE, datasetVersionDto.getDataSourceType().getName());
        datasourceForm.setValue(DatasetDS.DATE_LAST_TIME_DATA_IMPORT, DateUtils.getFormattedDateTime(datasetVersionDto.getDateLastTimeDataImport()));
    }

    private void setDatasetVersionDtoEditionMode(DatasetVersionDto datasetVersionDto) {
        datasourceEditionForm.setValue(DatasetDS.KEEP_ALL_DATA, datasetVersionDto.isKeepAllData());
        datasourceEditionForm.setValue(DatasetDS.DATA_SOURCE_TYPE, datasetVersionDto.getDataSourceType().getName());
        datasourceEditionForm.setValue(DatasetDS.DATE_LAST_TIME_DATA_IMPORT, DateUtils.getFormattedDateTime(datasetVersionDto.getDateLastTimeDataImport()));
    }

    private void createViewForm() {
        ViewTextItem keepAllDataViewTextItem = new ViewTextItem(DatasetDS.KEEP_ALL_DATA, getConstants().keepAllData());

        ViewTextItem dataSourceTypeItem = new ViewTextItem(DatasetDS.DATA_SOURCE_TYPE, getConstants().datasetVersionDataSourceType());
        dataSourceTypeItem.setValueMap(CommonUtils.getDataSourceTypeHashMap());

        ViewTextItem dateLastTimeDataImportItem = new ViewTextItem(DatasetDS.DATE_LAST_TIME_DATA_IMPORT, getConstants().dateLastTimeDataImport());

        datasourceForm = new GroupDynamicForm(getConstants().datasetDatasources());
        datasourceForm.setFields(keepAllDataViewTextItem, dateLastTimeDataImportItem, dataSourceTypeItem);
        addViewCanvas(datasourceForm);
    }

    private void createEditionForm() {
        FormItemIcon infoIcon = new FormItemIcon();
        infoIcon.setSrc(GlobalResources.RESOURCE.info().getURL());
        infoIcon.setPrompt(StatisticalResourcesWeb.getMessages().datasetKeepAllDataInfo());

        CustomCheckboxItem keepAllDataCheckBoxItem = new CustomCheckboxItem(DatasetDS.KEEP_ALL_DATA, getConstants().keepAllData());
        keepAllDataCheckBoxItem.setCanEdit(Boolean.TRUE);
        keepAllDataCheckBoxItem.setIcons(infoIcon);

        RequiredSelectItem dataSourceTypeItem = new RequiredSelectItem(DatasetDS.DATA_SOURCE_TYPE, getConstants().datasetVersionDataSourceType());
        dataSourceTypeItem.setValueMap(CommonUtils.getDataSourceTypeHashMap());

        ViewTextItem dateLastTimeDataImportItem = new ViewTextItem(DatasetDS.DATE_LAST_TIME_DATA_IMPORT, getConstants().dateLastTimeDataImport());

        datasourceEditionForm = new GroupDynamicForm(getConstants().datasetDatasources());
        datasourceEditionForm.setFields(keepAllDataCheckBoxItem, dateLastTimeDataImportItem, dataSourceTypeItem);
        addEditionCanvas(datasourceEditionForm);
    }

    private void bindEvents() {
        getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (datasourceEditionForm.isVisible() && datasourceEditionForm.validate(false)) {
                    // See: METAMAC-2516
                    // Two invokes to getXXXDto() is needed for Chrome, please don't remove this two call fix.
                    getDatasetVersionDto();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            getUiHandlers().saveDataset(getDatasetVersionDto());
                        }
                    });
                }
            }

        });
    }
    private DatasetVersionDto getDatasetVersionDto() {
        datasetVersionDto.setKeepAllData(Boolean.valueOf(datasourceEditionForm.getValueAsString(DatasetDS.KEEP_ALL_DATA)));
        datasetVersionDto.setDataSourceType(DataSourceTypeEnum.valueOf(datasourceEditionForm.getValueAsString(DatasetDS.DATA_SOURCE_TYPE)));
        return datasetVersionDto;
    }
}
