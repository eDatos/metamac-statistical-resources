package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.getExternalItemsValue;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setExternalItemsValue;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.TemporalCodeDto;
import org.siemac.metamac.statistical.resources.web.client.base.checks.DatasetMetadataShowChecks;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetMetadataExternalField;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetMetadataCommonTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils.DatasetWidgetsUtil;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataContentDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.TemporalCodeListItem;
import org.siemac.metamac.web.common.client.constants.CommonWebConstants;
import org.siemac.metamac.web.common.client.utils.CustomRequiredValidator;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.ExternalItemListItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchMultiExternalItemSimpleItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchSrmListItemWithSchemeFilterItem;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchMultipleSrmItemWithSchemeFilterPaginatedWindow;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

public class DatasetContentDescriptorsEditionForm extends SiemacMetadataContentDescriptorsEditionForm {

    private DatasetMetadataCommonTabUiHandlers                   uiHandlers;
    private SearchMultipleSrmItemWithSchemeFilterPaginatedWindow statisticalUnitWindow;

    private SearchMultiExternalItemSimpleItem                    geographicalGranularitiesItem;
    private SearchMultiExternalItemSimpleItem                    temporalGranularitiesItem;

    public DatasetContentDescriptorsEditionForm() {
        super();
        init(false);
    }

    public DatasetContentDescriptorsEditionForm(boolean isMultipleUpdate) {
        super();
        init(isMultipleUpdate);
    }

    private void init(boolean isMultipleDatasetUpdate) {

        ExternalItemListItem geographicCoverage = new ExternalItemListItem(DatasetDS.GEOGRAPHIC_COVERAGE, getConstants().datasetGeographicCoverage(), false);
        geographicCoverage.setShowIfCondition(getCanShowCoveragesFunction());

        TemporalCodeListItem temporalCoverage = new TemporalCodeListItem(DatasetDS.TEMPORAL_COVERAGE, getConstants().datasetTemporalCoverage(), false);
        temporalCoverage.setShowIfCondition(getCanShowCoveragesFunction());

        ExternalItemListItem measures = new ExternalItemListItem(DatasetDS.MEASURES, getConstants().datasetMeasures(), false);
        measures.setShowIfCondition(getCanShowCoveragesFunction());

        geographicalGranularitiesItem = createGeographicGranularitiesItem();
        geographicalGranularitiesItem.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                List<ExternalItemDto> values = getExternalItemsValue(getItem(DatasetDS.GEOGRAPHIC_GRANULARITY));
                return CommonUtils.isResourceInProductionValidationOrGreaterProcStatus(procStatus) ? (values != null && values.size() > 0) : true;
            }
        });

        temporalGranularitiesItem = createTemporalGranularitiesItem();
        temporalGranularitiesItem.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                List<ExternalItemDto> values = getExternalItemsValue(getItem(DatasetDS.TEMPORAL_GRANULARITY));
                return CommonUtils.isResourceInProductionValidationOrGreaterProcStatus(procStatus) ? (values != null && values.size() > 0) : true;
            }
        });

        ExternalItemListItem statisticalUnit = createStatisticalUnitItem();

        // only in one dataset update
        ViewTextItem dateStart = new ViewTextItem(DatasetDS.DATE_START, getConstants().datasetDateStart());
        ViewTextItem dateEnd = new ViewTextItem(DatasetDS.DATE_END, getConstants().datasetDateEnd());

        dateStart.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleDatasetUpdate));
        dateEnd.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleDatasetUpdate));
        statisticalUnit.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleDatasetUpdate));

        addFields(dateStart, dateEnd, statisticalUnit, geographicCoverage, temporalCoverage, measures, geographicalGranularitiesItem, temporalGranularitiesItem);
    }

    public void setDatasetVersionDto(DatasetVersionDto datasetDto) {
        setSiemacMetadataStatisticalResourceDto(datasetDto);

        setExternalItemsValue(getItem(DatasetDS.GEOGRAPHIC_GRANULARITY), datasetDto.getGeographicGranularities());

        setExternalItemsValue(getItem(DatasetDS.TEMPORAL_GRANULARITY), datasetDto.getTemporalGranularities());

        setValue(DatasetDS.DATE_START, datasetDto.getDateStart());
        setValue(DatasetDS.DATE_END, datasetDto.getDateEnd());

        setSelectedConceptsForStatisticalUnit(datasetDto.getStatisticalUnit());
    }

    public void setCoverages(List<ExternalItemDto> geoItems, List<TemporalCodeDto> temporalItems, List<ExternalItemDto> measureItems) {
        ((ExternalItemListItem) getItem(DatasetDS.GEOGRAPHIC_COVERAGE)).setExternalItems(geoItems);
        ((TemporalCodeListItem) getItem(DatasetDS.TEMPORAL_COVERAGE)).setTemporalCodes(temporalItems);
        ((ExternalItemListItem) getItem(DatasetDS.MEASURES)).setExternalItems(measureItems);
    }

    public DatasetVersionDto getDatasetVersionDto(DatasetVersionDto datasetDto) {
        datasetDto = (DatasetVersionDto) getSiemacMetadataStatisticalResourceDto(datasetDto);

        datasetDto.getGeographicGranularities().clear();
        datasetDto.getGeographicGranularities().addAll(getExternalItemsValue(getItem(DatasetDS.GEOGRAPHIC_GRANULARITY)));

        datasetDto.getTemporalGranularities().clear();
        datasetDto.getTemporalGranularities().addAll(getExternalItemsValue(getItem(DatasetDS.TEMPORAL_GRANULARITY)));

        datasetDto.getStatisticalUnit().clear();
        datasetDto.getStatisticalUnit().addAll(getExternalItemsValue(getItem(DatasetDS.STATISTICAL_UNIT)));

        return datasetDto;
    }

    // ***************************************************************************************
    // GEO GRANULARITIES
    // ***************************************************************************************

    public void setCodesForGeographicalGranularities(List<ExternalItemDto> items, int firstResult, int totalResults) {
        geographicalGranularitiesItem.setResources(items, firstResult, totalResults);
    }

    private SearchMultiExternalItemSimpleItem createGeographicGranularitiesItem() {
        return new SearchMultiExternalItemSimpleItem(DatasetDS.GEOGRAPHIC_GRANULARITY, getConstants().datasetGeographicGranularities(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                uiHandlers.retrieveCodesForGeographicalGranularities(firstResult, maxResults, webCriteria);
            }
        };
    }

    // ***************************************************************************************
    // Time GRANULARITIES
    // ***************************************************************************************

    public void setCodesForTemporalGranularities(List<ExternalItemDto> items, int firstResult, int totalResults) {
        temporalGranularitiesItem.setResources(items, firstResult, totalResults);
    }

    private SearchMultiExternalItemSimpleItem createTemporalGranularitiesItem() {
        return new SearchMultiExternalItemSimpleItem(DatasetDS.TEMPORAL_GRANULARITY, getConstants().datasetTemporalGranularities(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                uiHandlers.retrieveTemporalCodesForField(firstResult, maxResults, webCriteria, DatasetMetadataExternalField.TEMPORAL_GRANULARITY);
            }
        };
    }

    // ***************************************************************************************
    // STATISTICAL UNIT
    // ***************************************************************************************

    public void setConceptsForStatisticalUnit(List<ExternalItemDto> items, int firstResult, int totalResults) {
        if (statisticalUnitWindow != null) {
            statisticalUnitWindow.setResources(items);
            statisticalUnitWindow.refreshSourcePaginationInfo(firstResult, items.size(), totalResults);
        }
    }

    public void setConceptSchemesForStatisticalUnit(List<ExternalItemDto> items, int firstResult, int totalResults) {
        if (statisticalUnitWindow != null) {
            statisticalUnitWindow.setFilterResources(items);
            statisticalUnitWindow.refreshFilterSourcePaginationInfo(firstResult, items.size(), totalResults);
        }
    }

    public void setSelectedConceptsForStatisticalUnit(List<ExternalItemDto> items) {
        if (statisticalUnitWindow != null) {
            statisticalUnitWindow.setSelectedResources(items);
            setExternalItemsValue(getItem(DatasetDS.STATISTICAL_UNIT), items);
        }
    }

    private ExternalItemListItem createStatisticalUnitItem() {

        SearchSrmListItemWithSchemeFilterItem listItem = new SearchSrmListItemWithSchemeFilterItem(DatasetDS.STATISTICAL_UNIT, getConstants().datasetStatisticalUnit(),
                CommonWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveItems(int firstResult, int maxResults, SrmItemRestCriteria webCriteria) {
                retrieveConceptsForStatisticalUnit(firstResult, maxResults, webCriteria);
            }

            @Override
            protected void retrieveItemSchemes(int firstResult, int maxResults, SrmExternalResourceRestCriteria webCriteria) {
                retrieveConceptSchemesForStatisticalUnit(firstResult, maxResults, webCriteria);
            }
        };

        return listItem;
    }

    private void retrieveConceptSchemesForStatisticalUnit(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
        uiHandlers.retrieveConceptSchemesForStatisticalUnit(firstResult, maxResults, webCriteria);
    };

    private void retrieveConceptsForStatisticalUnit(int firstResult, int maxResults, SrmItemRestCriteria webCriteria) {
        uiHandlers.retrieveConceptsForStatisticalUnit(firstResult, maxResults, webCriteria);
    };

    public void setUiHandlers(DatasetMetadataCommonTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    private FormItemIfFunction getCanShowCoveragesFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DatasetMetadataShowChecks.canCoveragesBeShown(procStatus);
            }
        };
    }
}
