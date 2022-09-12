package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetMetadataExternalField;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetMetadataTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.CustomDatePickerItem;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceVersionEditionForm;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.CustomRequiredValidator;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemSimpleItem;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.smartgwt.client.widgets.form.fields.FormItemIcon;

public class DatasetVersionEditionForm extends LifeCycleResourceVersionEditionForm {

    private DatasetMetadataTabUiHandlers uiHandlers;
    private SearchExternalItemSimpleItem updateFrequency;
    private ProcStatusEnum               procStatus;
    private CustomDatePickerItem dateNextUpdate;
    
    public DatasetVersionEditionForm() {
        super();

        final CustomDateItem dateNextUpdate1 = createDateNextUpdateItem();

        updateFrequency = createUpdateFrequencyItem();
        updateFrequency.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                ExternalItemDto externalItem = DatasetVersionEditionForm.this.getValueAsExternalItemDto(DatasetDS.UPDATE_FRECUENCY);
                return CommonUtils.isResourceInProductionValidationOrGreaterProcStatus(procStatus) ? externalItem != null : true;
            }
        });

        dateNextUpdate = createFieldDateNextUpdate();
        
        addFields(dateNextUpdate, updateFrequency, dateNextUpdate1);
    }

    public void setDatasetVersionDto(DatasetVersionDto dto) {
        super.setLifeCycleStatisticalResourceDto(dto);

        this.procStatus = dto.getProcStatus();

        setValue(DatasetDS.DATE_NEXT_UPDATE, dto.getDateNextUpdate());
        setValue(DatasetDS.UPDATE_FRECUENCY, dto.getUpdateFrequency());
        dateNextUpdate.setValue(dto.getDateNextUpdate1());

    }

    public DatasetVersionDto getDatasetVersionDto(DatasetVersionDto dto) {
        super.getLifeCycleStatisticalResourceDto(dto);

        dto.setDateNextUpdate(((CustomDateItem) getItem(DatasetDS.DATE_NEXT_UPDATE)).getValueAsDate());
        dto.setUpdateFrequency(getValueAsExternalItemDto(DatasetDS.UPDATE_FRECUENCY));
        dto.setDateNextUpdate1(dateNextUpdate.getValue());

        return dto;
    }
    private CustomDateItem createDateNextUpdateItem() {
            
        FormItemIcon infoIcon = new FormItemIcon();
        infoIcon.setSrc(GlobalResources.RESOURCE.info().getURL());
        infoIcon.setPrompt(StatisticalResourcesWeb.getMessages().dateNextUpdateInfo());
        CustomDateItem item = new CustomDateItem(DatasetDS.DATE_NEXT_UPDATE, getConstants().datasetDateNextUpdate());
        item.setIcons(infoIcon);
        return item;
    }

    private CustomDatePickerItem createFieldDateNextUpdate() {
        CustomDatePickerItem customDatePickerItem = new CustomDatePickerItem(DatasetDS.DATE_NEXT_UPDATE1, getConstants().datasetDateNextUpdate(), false, false);
        customDatePickerItem.setIconCreateDateItem(StatisticalResourcesWeb.getMessages().dateNextUpdateInfo());
        customDatePickerItem.setIconCustomSdmxTimePeriodItem(StatisticalResourcesWeb.getMessages().dateNextUpdateInfo());
        customDatePickerItem.defaultDateType();
        return customDatePickerItem;

    }

    private SearchExternalItemSimpleItem createUpdateFrequencyItem() {
        return new SearchExternalItemSimpleItem(DatasetDS.UPDATE_FRECUENCY, getConstants().datasetUpdateFrequency(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                uiHandlers.retrieveTemporalCodesForField(firstResult, maxResults, webCriteria, DatasetMetadataExternalField.UPDATE_FREQUENCY);
            }
        };
    }

    public void setCodesForUpdateFrequency(List<ExternalItemDto> items, int firstResult, int totalResults) {
        updateFrequency.setResources(items, firstResult, totalResults);
    }

    public void setUiHandlers(DatasetMetadataTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
