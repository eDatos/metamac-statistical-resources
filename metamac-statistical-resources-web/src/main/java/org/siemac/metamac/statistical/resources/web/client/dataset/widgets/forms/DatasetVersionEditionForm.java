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
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.TextAndUrlItem;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.DateUtils;
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
    private TextAndUrlItem pruebas;
    
    public DatasetVersionEditionForm() {
        super();

        final CustomDateItem dateNextUpdate = createDateNextUpdateItem();

        updateFrequency = createUpdateFrequencyItem();
        updateFrequency.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                ExternalItemDto externalItem = DatasetVersionEditionForm.this.getValueAsExternalItemDto(DatasetDS.UPDATE_FRECUENCY);
                return CommonUtils.isResourceInProductionValidationOrGreaterProcStatus(procStatus) ? externalItem != null : true;
            }
        });

        pruebas = createFieldPruebas();
        
        //pruebas.setDefaultValue(new Date());
        //pruebas.setDefaultValue(DateUtils.getDate(01, 05, 1970));
        pruebas.setDefaultValue("2022-08-01");
        
        
        addFields(pruebas, updateFrequency, dateNextUpdate);
    }

    public void setDatasetVersionDto(DatasetVersionDto dto) {
        super.setLifeCycleStatisticalResourceDto(dto);

        this.procStatus = dto.getProcStatus();

        setValue(DatasetDS.DATE_NEXT_UPDATE, dto.getDateNextUpdate());
        setValue(DatasetDS.UPDATE_FRECUENCY, dto.getUpdateFrequency());
    }

    public DatasetVersionDto getDatasetVersionDto(DatasetVersionDto dto) {
        super.getLifeCycleStatisticalResourceDto(dto);

        dto.setDateNextUpdate(((CustomDateItem) getItem(DatasetDS.DATE_NEXT_UPDATE)).getValueAsDate());
        dto.setUpdateFrequency(getValueAsExternalItemDto(DatasetDS.UPDATE_FRECUENCY));

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


    // TODO EDATOS-3744 QUITAR
    private TextAndUrlItem createFieldPruebas() {
        return new TextAndUrlItem(DatasetDS.DATE_NEXT_UPDATE + "1", getConstants().datasetDateNextUpdate(), false);
       
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
