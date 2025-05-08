package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.getExternalItemsValue;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setExternalItemsValue;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.base.view.handlers.StatisticalResourceUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchMultiExternalItemSimpleItem;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

public class SiemacMetadataThematicContentClassifiersEditionForm extends StatisticalResourceThematicContentClassifiersEditionForm {

    private StatisticalResourceUiHandlers     uiHandlers;

    private SearchMultiExternalItemSimpleItem instancesItem;

    public SiemacMetadataThematicContentClassifiersEditionForm() {
        super();
        initSiemacMetadataThematicContentClassifiersEditionForm();
    }

    public SiemacMetadataThematicContentClassifiersEditionForm(boolean isMultipleUpdate) {
        super(isMultipleUpdate);
        initSiemacMetadataThematicContentClassifiersEditionForm();
    }

    private void initSiemacMetadataThematicContentClassifiersEditionForm() {
        instancesItem = createStatisticalOperationInstancesItem();
        addFields(instancesItem);
    }

    public void setSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto siemacMetadataStatisticalResourceDto) {
        setStatisticalResourceDto(siemacMetadataStatisticalResourceDto);
        setExternalItemsValue(getItem(SiemacMetadataDS.STATISTICAL_OPERATION_INSTANCE), siemacMetadataStatisticalResourceDto.getStatisticalOperationInstances());
    }

    public SiemacMetadataStatisticalResourceDto getSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        dto = (SiemacMetadataStatisticalResourceDto) getStatisticalResourceDto(dto);
        dto.getStatisticalOperationInstances().clear();
        dto.getStatisticalOperationInstances().addAll(getExternalItemsValue(getItem(SiemacMetadataDS.STATISTICAL_OPERATION_INSTANCE)));
        return dto;
    }

    // ************************************
    // STATISTICAL OPERATION INSTANCE
    // ************************************

    public void setStatisticalOperationInstances(List<ExternalItemDto> items, int firstResult, int totalResults) {
        instancesItem.setResources(items, firstResult, totalResults);
    }

    private SearchMultiExternalItemSimpleItem createStatisticalOperationInstancesItem() {
        return new SearchMultiExternalItemSimpleItem(SiemacMetadataDS.STATISTICAL_OPERATION_INSTANCE, getConstants().siemacMetadataStatisticalResourceStatisticalOperationInstance(),
                StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                uiHandlers.retrieveStatisticalOperationInstances(statisticalOperationCode, firstResult, maxResults, webCriteria);
            }
        };
    }

    public void setUiHandlers(StatisticalResourceUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
