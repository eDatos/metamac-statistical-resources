package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dataset.checks.DatasetMetadataEditionChecks;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetMetadataTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils.DatasetWidgetsUtil;
import org.siemac.metamac.statistical.resources.web.client.resources.GlobalResources;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataProductionDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.search.SearchSingleDsdPaginatedWindow;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DsdWebCriteria;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemLinkItem;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class DatasetProductionDescriptorsEditionForm extends SiemacMetadataProductionDescriptorsEditionForm {

    private DatasetMetadataTabUiHandlers   uiHandlers;
    // related Dsd
    private SearchSingleDsdPaginatedWindow searchDsdWindow;
    private String                         fixedDsdCode;
    private ExternalItemDto                statisticalOperation;

    // components
    private SearchExternalItemLinkItem     relatedDsd;

    public DatasetProductionDescriptorsEditionForm() {
        initDatasetProductionDescriptorsEditionForm(false);
    }

    public DatasetProductionDescriptorsEditionForm(boolean isMultipleUpdate) {
        super(isMultipleUpdate);
        initDatasetProductionDescriptorsEditionForm(isMultipleUpdate);
    }

    private void initDatasetProductionDescriptorsEditionForm(boolean isMultipleUpdate) {
        relatedDsd = createDsdsItem(DatasetDS.RELATED_DSD, getConstants().datasetRelatedDSD());
        relatedDsd.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));
        relatedDsd.setRequired(true);
        addFields(relatedDsd);
    }

    public void setUiHandlers(DatasetMetadataTabUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        this.uiHandlers = uiHandlers;
    }

    public void setDatasetVersionDto(DatasetVersionDto datasetDto) {
        setDatasetVersionDto(datasetDto, true);
    }

    public void setDatasetVersionDto(DatasetVersionDto datasetDto, boolean checkDsd) {
        setSiemacMetadataStatisticalResourceDto(datasetDto);

        if (checkDsd) {
            setRelatedDsd(datasetDto.getRelatedDsd());
            relatedDsd.setShowIfCondition(getRelatedDsdFormItemIfFunction(datasetDto));

            if (!DatasetMetadataEditionChecks.canDsdBeReplacedByAnyOtherDsd(datasetDto.getId(), datasetDto.getVersionLogic(), datasetDto.getProcStatus())) {
                fixedDsdCode = datasetDto.getRelatedDsd().getCode();
                statisticalOperation = datasetDto.getStatisticalOperation();
            } else {
                fixedDsdCode = null;
            }
        }
    }

    public DatasetVersionDto getDatasetVersionDto(DatasetVersionDto datasetDto) {
        datasetDto = (DatasetVersionDto) getSiemacMetadataStatisticalResourceDto(datasetDto);
        datasetDto.setRelatedDsd(getValueAsExternalItemDto(DatasetDS.RELATED_DSD));
        return datasetDto;
    }

    private void setRelatedDsd(ExternalItemDto relatedDsdDto) {
        setValue(DatasetDS.RELATED_DSD, relatedDsdDto);
    }

    public void setExternalItemsForRelatedDsd(List<ExternalItemDto> externalItemsDtos, int firstResult, int totalResults) {
        if (searchDsdWindow != null) {
            searchDsdWindow.setResources(externalItemsDtos);
            searchDsdWindow.refreshSourcePaginationInfo(firstResult, externalItemsDtos.size(), totalResults);
        }
    }

    public void setStatisticalOperationsForRelatedDsd(List<ExternalItemDto> externalItemsDtos, ExternalItemDto defaultSelected, String dsdCode) {
        if (searchDsdWindow != null) {
            searchDsdWindow.setStatisticalOperations(externalItemsDtos);
            searchDsdWindow.setSelectedStatisticalOperation(defaultSelected);
            searchDsdWindow.setFixedDsdCode(dsdCode);
            if (StringUtils.isEmpty(dsdCode)) {
                searchDsdWindow.setOnlyLastVersion(true);
            } else {
                searchDsdWindow.setOnlyLastVersion(false);
            }

            retrieveDsds(0, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, searchDsdWindow.getDsdWebCriteria());
        }
    }

    public void retrieveDsds(int firstResult, int maxResults, DsdWebCriteria criteria) {
        uiHandlers.retrieveDsdsForRelatedDsd(firstResult, maxResults, criteria);
    }

    public void retrieveStatisticalOperationsForDsdSelection() {
        uiHandlers.retrieveStatisticalOperationsForDsdSelection();
    }

    // FORM ITEM IF FUNCTIONS

    private FormItemIfFunction getRelatedDsdFormItemIfFunction(final DatasetVersionDto resource) {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DatasetMetadataEditionChecks.canDsdBeEdited(resource.getId(), resource.getProcStatus());
            }
        };
    }
    private FormItemIfFunction getStaticRelatedDsdFormItemIfFunction(final DatasetVersionDto resource) {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !DatasetMetadataEditionChecks.canDsdBeEdited(resource.getId(), resource.getProcStatus());
            }
        };
    }

    // WINDOWS

    private SearchExternalItemLinkItem createDsdsItem(String name, String title) {

        final SearchExternalItemLinkItem dsdItem = new SearchExternalItemLinkItem(name, title, true) {

            @Override
            public void onSearch() {

                searchDsdWindow = new SearchSingleDsdPaginatedWindow(getConstants().resourceSelection(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS,
                        new SearchPaginatedAction<DsdWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, DsdWebCriteria criteria) {
                                retrieveDsds(firstResult, maxResults, criteria);
                            }
                        });

                // Load resources (to populate the selection window)
                if (!StringUtils.isEmpty(fixedDsdCode)) {
                    setStatisticalOperationsForRelatedDsd(Arrays.asList(statisticalOperation), statisticalOperation, fixedDsdCode);
                } else {
                    retrieveStatisticalOperationsForDsdSelection();
                }

                searchDsdWindow.setSaveAction(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        ExternalItemDto selectedResource = searchDsdWindow.getSelectedResource();
                        searchDsdWindow.markForDestroy();
                        // Set selected resource in form
                        setRelatedDsd(selectedResource);
                        DatasetProductionDescriptorsEditionForm.this.validate(false);
                    }
                });

                searchDsdWindow.show();
            }

            @Override
            protected List<FormItemIcon> getAdditionalIcons() {
                FormItemIcon infoIcon = new FormItemIcon();
                infoIcon.setSrc(GlobalResources.RESOURCE.info().getURL());
                infoIcon.setPrompt(getMessages().datasetRelatedDsdInfo());
                return Arrays.asList(infoIcon);
            }
        };

        return dsdItem;
    }
}
