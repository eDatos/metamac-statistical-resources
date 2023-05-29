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
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceVersionEditionForm;
import org.siemac.metamac.web.common.client.utils.CustomRequiredValidator;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDatePickerItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemSimpleItem;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

public class DatasetVersionEditionForm extends LifeCycleResourceVersionEditionForm {

    private DatasetMetadataTabUiHandlers uiHandlers;
    private SearchExternalItemSimpleItem updateFrequency;
    private ProcStatusEnum               procStatus;
    private CustomDatePickerItem dateNextUpdate;
    
    public DatasetVersionEditionForm() {
        super();

        updateFrequency = createUpdateFrequencyItem();
        updateFrequency.setShowIfCondition(getUpdateFrecuencyFormItemIfFunction());
        updateFrequency.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                ExternalItemDto externalItem = DatasetVersionEditionForm.this.getValueAsExternalItemDto(DatasetDS.UPDATE_FRECUENCY);
                return CommonUtils.isResourceInProductionValidationOrGreaterProcStatus(procStatus) ? externalItem != null : true;
            }
        });

        dateNextUpdate = createFieldDateNextUpdate();
        
        addFields(dateNextUpdate, updateFrequency);
    }
    
    public void setDatasetVersionDto(DatasetVersionDto dto) {
        super.setLifeCycleStatisticalResourceDto(dto);

        this.procStatus = dto.getProcStatus();

        setValue(DatasetDS.UPDATE_FRECUENCY, dto.getUpdateFrequency());
        dateNextUpdate.setValue(dto.getDateNextUpdate());

    }

    public DatasetVersionDto getDatasetVersionDto(DatasetVersionDto dto) {
        super.getLifeCycleStatisticalResourceDto(dto);

        dto.setUpdateFrequency(this.isNextVersionScheduledNecessary() ? getValueAsExternalItemDto(DatasetDS.UPDATE_FRECUENCY) : null);
        dto.setDateNextUpdate(dateNextUpdate.getValue());

        return dto;
    }

    private CustomDatePickerItem createFieldDateNextUpdate() {
        CustomDatePickerItem customDatePickerItem = new CustomDatePickerItem(DatasetDS.DATE_NEXT_UPDATE, getConstants().datasetDateNextUpdate(), false, false, CommonUtils.getDateFormatTypeHashMap());
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
