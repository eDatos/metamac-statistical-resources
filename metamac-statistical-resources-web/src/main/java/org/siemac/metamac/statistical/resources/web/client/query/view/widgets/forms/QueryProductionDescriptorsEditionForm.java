package org.siemac.metamac.statistical.resources.web.client.query.view.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.getExternalItemsValue;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.getRelatedResourceValue;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setExternalItemsValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.base.checks.MetadataEditionChecks;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.LifeCycleResourceDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.statistical.resources.web.client.query.model.ds.QueryDS;
import org.siemac.metamac.statistical.resources.web.client.query.utils.QueryRelatedDatasetUtils;
import org.siemac.metamac.statistical.resources.web.client.query.view.handlers.QueryUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.NavigationEnabledDynamicForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.CodeItemListItem;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.SearchRelatedResourceLinkItem;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.search.SearchMultipleCodeItemWindow;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.search.SearchSingleDatasetVersionRelatedResourcePaginatedWindow;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DatasetVersionWebCriteria;
import org.siemac.metamac.web.common.client.model.ds.ExternalItemDS;
import org.siemac.metamac.web.common.client.model.record.ExternalItemRecord;
import org.siemac.metamac.web.common.client.utils.NavigationUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchAction;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomIntegerItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchSrmItemLinkItemWithSchemeFilterItem;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchMultipleExternalItemPaginatedWindow;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class QueryProductionDescriptorsEditionForm extends NavigationEnabledDynamicForm {

    private QueryUiHandlers                                          uiHandlers;

    private SearchSingleDatasetVersionRelatedResourcePaginatedWindow searchDatasetWindow;

    private Map<String, SearchMultipleCodeItemWindow>                dimensionCodeSelectionWindow;
    private Map<String, CodeItemListItem>                            selectionFields;

    protected SearchSrmItemLinkItemWithSchemeFilterItem              maintainerItem;

    private Map<String, List<CodeItemDto>>                           dtoSelection;

    private QueryVersionDto                                          queryDto;
    private SearchMultiExternalItem                                  temporalGranularitiesItem;

    public QueryProductionDescriptorsEditionForm() {
        super(getConstants().formProductionDescriptors());

        List<FormItem> fields = createComponents();
        setFields(fields.toArray(new FormItem[fields.size()]));
    }

    private List<FormItem> createComponents() {
        List<FormItem> fields = new ArrayList<FormItem>();

        maintainerItem = createMaintainerItem();
        maintainerItem.setShowIfCondition(getFormItemIfFunctionEditionMode());
        fields.add(maintainerItem);

        ExternalItemLinkItem maintainerViewItem = new ExternalItemLinkItem(SiemacMetadataDS.MAINTAINER_VIEW, getConstants().siemacMetadataStatisticalResourceMaintainer());
        maintainerViewItem.setShowIfCondition(getFormItemIfFunctionViewMode());
        fields.add(maintainerViewItem);

        SearchRelatedResourceLinkItem searchDatasetItem = createQueryDatasetItem();
        fields.add(searchDatasetItem);

        ViewTextItem status = new ViewTextItem(QueryDS.STATUS, getConstants().queryStatus());
        fields.add(status);

        CustomSelectItem typeSelectorItem = new CustomSelectItem(QueryDS.TYPE, getConstants().queryType());
        typeSelectorItem.setValueMap(CommonUtils.getQueryTypeHashMap());
        typeSelectorItem.setRequired(true);
        typeSelectorItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                QueryProductionDescriptorsEditionForm.this.markForRedraw();
            }
        });
        fields.add(typeSelectorItem);
        return fields;
    }

    private SearchMultiExternalItem createTemporalGranularitiesItem(final String datasetUrn) {
        SearchMultiExternalItem item = new SearchMultiExternalItem(DatasetDS.TEMPORAL_GRANULARITY, getConstants().datasetTemporalGranularities()) {

            @Override
            public void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                uiHandlers.retrieveTemporalCodesForField(datasetUrn);
            }
        }; 
        appendWindow(item, datasetUrn);
        setRecordHandlerToListGrid(item);
        return item; 
    }

    private void appendWindow(final SearchMultiExternalItem item, final String datasetUrn) {
        item.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                item.setWindow(new SearchMultipleExternalItemPaginatedWindow("pepito", StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, new SearchPaginatedAction<MetamacWebCriteria>() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                        uiHandlers.retrieveTemporalCodesForField(datasetUrn);
                    }

                }));
                Set<String> fieldNamesToShow = new HashSet<String>(Arrays.asList(ExternalItemDS.CODE, ExternalItemDS.TITLE, ExternalItemDS.URN));
                showFields(item.getWindow().getPaginatedCheckListGrid(), fieldNamesToShow);
                showFields(item.getWindow().getSelectionListGrid(), fieldNamesToShow);

                item.getWindow().retrieveItems();

                item.getWindow().setSelectedResources(item.getSelectedRelatedResources());

                item.getWindow().setSaveAction(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        item.setExternalItems(item.getWindow().getSelectedResources());
                        item.getWindow().markForDestroy();
                        resetTimePeriods();
                    }
                });
            }
        });
    }

    private void setRecordHandlerToListGrid(final SearchMultiExternalItem item) {
        item.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() == 3) {
                    ListGridRecord[] records = item.getListGrid().getRecords();
                    int i = event.getRecordNum();
                    item.getListGrid().setRecords(removeListGridRecord(records, i));
                    resetTimePeriods();
                } else {
                    Record record = event.getRecord();
                    if (record != null && record instanceof ExternalItemRecord) {
                        String url = ((ExternalItemRecord) record).getManagementAppUrl();
                        if (!StringUtils.isBlank(url)) {
                            NavigationUtils.goTo(url);
                        }
                    }
                }
            }
        });       
    }

    private ListGridRecord[] removeListGridRecord(ListGridRecord[] listGridRecord, int i) {
        ListGridRecord[] removeItemListGridRecord = new ListGridRecord[listGridRecord.length - 1];
        for (int j = 0; j < listGridRecord.length; j++) {
            if (j < i) {
                removeItemListGridRecord[j] = listGridRecord[j];
            } else if (j > i) {
                removeItemListGridRecord[j - 1] = listGridRecord[j];
            }
        }
        return removeItemListGridRecord;
    }

    private void resetTimePeriods() {
        CodeItemListItem item = (CodeItemListItem) getItem(QueryDS.SELECTION + "_" + StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID);
        if (item != null) {
            item.setCodeItems(new ArrayList<CodeItemDto>());
            this.markForRedraw();
        }
    }

    private void showFields(ListGrid listGrid, Set<String> fieldNamesToShow) {
        List<ListGridField> fieldsToInclude = new ArrayList<ListGridField>(fieldNamesToShow.size());
        for (ListGridField field : listGrid.getFields()) {
            if (fieldNamesToShow.contains(field.getName())) {
                fieldsToInclude.add(field);
            }
        }
        listGrid.setFields(fieldsToInclude.toArray(new ListGridField[fieldsToInclude.size()]));
    }

    
    public void setCodesForTemporalGranularities(List<ExternalItemDto> items, int firstResult, int totalResults) {
        temporalGranularitiesItem.setResources(items, firstResult, totalResults);
    }

    public void setQueryDto(QueryVersionDto queryDto) {

        QueryRelatedDatasetUtils.setRelatedDataset(queryDto, (SearchRelatedResourceLinkItem) getItem(QueryDS.RELATED_DATASET_VERSION));
        setValue(LifeCycleResourceDS.MAINTAINER, queryDto.getMaintainer());
        setValue(LifeCycleResourceDS.MAINTAINER_VIEW, queryDto.getMaintainer());

        String typeStr = queryDto.getType() != null ? queryDto.getType().name() : null;
        setValue(QueryDS.TYPE, typeStr);

        dtoSelection = queryDto.getSelection();
        if (queryDto.getRelatedDatasetVersion() != null) {
            retrieveDimensionsForDataset(queryDto.getRelatedDatasetVersion().getUrn());
        }

        // Status
        setValue(QueryDS.STATUS, CommonUtils.getQueryStatusName(queryDto));
        this.queryDto = queryDto;
    }

    public QueryVersionDto getQueryDto(QueryVersionDto queryDto) {
        QueryTypeEnum queryType = QueryTypeEnum.valueOf(getValueAsString(QueryDS.TYPE));
        queryDto.setMaintainer(getValueAsExternalItemDto(SiemacMetadataDS.MAINTAINER));
        queryDto.setRelatedDatasetVersion(getRelatedResourceValue(getItem(QueryDS.RELATED_DATASET_VERSION)));

        queryDto.setType(queryType);

        boolean isLatestData = QueryTypeEnum.LATEST_DATA.equals(queryType);

        Map<String, List<CodeItemDto>> selection = new HashMap<String, List<CodeItemDto>>();
        for (String dimensionId : selectionFields.keySet()) {
            if (isLatestData && StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID.equals(dimensionId)) {
                selection.put(dimensionId, new ArrayList<CodeItemDto>());
            } else {
                CodeItemListItem item = selectionFields.get(dimensionId);
                selection.put(dimensionId, item.getCodeItemsDtos());
            }
        }

        queryDto.setSelection(selection);
        Integer latestDataNumber = null;
        if (isLatestData) {
            latestDataNumber = ((CustomIntegerItem) getItem(QueryDS.LATEST_N_DATA)).getValueAsInteger();
        }
        queryDto.setLatestDataNumber(latestDataNumber);
        setTemporalGranularities(queryDto);
        return queryDto;
    }

    private void setTemporalGranularities(QueryVersionDto queryDto) {
        queryDto.getTemporalGranularities().clear();
        queryDto.getTemporalGranularities().addAll(getExternalItemsValue(getItem(DatasetDS.TEMPORAL_GRANULARITY)));
    }

    @Override
    public BaseUiHandlers getBaseUiHandlers() {
        return uiHandlers;
    }

    public void setUiHandlers(QueryUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    // ********************************************************
    // RELATED DATASET
    // *******************************************************

    private void setSelectedDataset(RelatedResourceDto datasetResource) {
        QueryRelatedDatasetUtils.setRelatedDataset(datasetResource, (SearchRelatedResourceLinkItem) getItem(QueryDS.RELATED_DATASET_VERSION));

        // Get dimensions
        if (datasetResource != null) {
            retrieveDimensionsForDataset(datasetResource.getUrn());
        }
    }

    public void setDatasetsForQuery(List<RelatedResourceDto> resourcesDtos, int firstResult, int elementsInPage, int totalResults) {
        if (searchDatasetWindow != null) {
            searchDatasetWindow.setResources(resourcesDtos);
            searchDatasetWindow.refreshSourcePaginationInfo(firstResult, elementsInPage, totalResults);
        }
    }

    public void setStatisticalOperationsForDatasetSelection(List<ExternalItemDto> externalItemsDtos) {
        if (searchDatasetWindow != null) {
            searchDatasetWindow.setStatisticalOperations(externalItemsDtos);
            retrieveDatasetsAsResources(0, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, searchDatasetWindow.getSearchCriteria());
        }
    }

    private SearchRelatedResourceLinkItem createQueryDatasetItem() {

        final SearchRelatedResourceLinkItem datasetItem = new SearchRelatedResourceLinkItem(QueryDS.RELATED_DATASET_VERSION, getConstants().queryDataset(), getCustomLinkItemNavigationClickHandler());
        datasetItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchDatasetWindow = new SearchSingleDatasetVersionRelatedResourcePaginatedWindow(getConstants().resourceSelection(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS,
                        new SearchPaginatedAction<DatasetVersionWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, DatasetVersionWebCriteria criteria) {
                                retrieveDatasetsAsResources(firstResult, maxResults, criteria);
                            }
                        });

                retrieveStatisticalOperationsForDatasetSelection();

                searchDatasetWindow.setSaveAction(new ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedResource = searchDatasetWindow.getSelectedResource();
                        searchDatasetWindow.markForDestroy();
                        // Set selected resource in form
                        setSelectedDataset(selectedResource);
                        dtoSelection = null;
                        validate(false);
                    }

                });
            }

        });

        datasetItem.getClearIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                datasetItem.setTitle(getConstants().queryDataset());
            }
        });

        datasetItem.setRequired(true);
        return datasetItem;
    }

    private void retrieveDatasetsAsResources(int firstResult, int maxResults, DatasetVersionWebCriteria criteria) {
        uiHandlers.retrieveDatasetsForQuery(firstResult, maxResults, criteria);
    }

    private void retrieveStatisticalOperationsForDatasetSelection() {
        uiHandlers.retrieveStatisticalOperationsForDatasetSelection();
    }

    // ********************************************************
    // DATASET DIMENSIONS
    // *******************************************************

    public void setDatasetDimensionsIds(List<String> datasetDimensions) {

        ExternalItemDto maintainer = getValueAsExternalItemDto(QueryDS.MAINTAINER);
        RelatedResourceDto datasetVersion = getRelatedResourceValue(getItem(QueryDS.RELATED_DATASET_VERSION));

        List<FormItem> fields = createComponents();

        selectionFields = new HashMap<String, CodeItemListItem>();
        dimensionCodeSelectionWindow = new HashMap<String, SearchMultipleCodeItemWindow>();

        boolean hasTemporalDimension = false;

        // force TIME_PERIOD last dimension
        if (datasetDimensions.contains(StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID)) {
            datasetDimensions.remove(StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID);
            datasetDimensions.add(StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID);
            hasTemporalDimension = true;
        }

        for (String dimensionId : datasetDimensions) {
            CodeItemListItem item = createCodeListItemForDimension(datasetVersion.getUrn(), dimensionId, true);
            
            if (!dimensionId.equals("TIME_PERIOD")) {
                fields.add(item);
            } else {
                temporalGranularitiesItem = createTemporalGranularitiesItem(queryDto.getRelatedDatasetVersion().getUrn());

                fields.add(temporalGranularitiesItem);
                fields.add(item);
            }
            selectionFields.put(dimensionId, item);
        }

        if (hasTemporalDimension) {
            CustomIntegerItem latestData = new CustomIntegerItem(QueryDS.LATEST_N_DATA, getConstants().queryLatestNData());
            latestData.setShowIfCondition(getFormItemIfFunctionShowLatestDataItem());
            latestData.setRequired(true);
            fields.add(latestData);
        }
        setFields(fields.toArray(new FormItem[fields.size()]));
        setExternalItemsValue(getItem(DatasetDS.TEMPORAL_GRANULARITY), queryDto.getTemporalGranularities());

        getItem(QueryDS.TYPE).setValidators(new QueryTypeValidator(hasTemporalDimension));

        if (dtoSelection != null && dtoSelection.size() > 0) {
            for (String dimensionId : datasetDimensions) {
                CodeItemListItem item = (CodeItemListItem) getItem(buildSelectionItemId(dimensionId));
                item.setCodeItems(dtoSelection.get(dimensionId));
            }
        }
        setValue(QueryDS.MAINTAINER, maintainer);
        QueryRelatedDatasetUtils.setRelatedDataset(datasetVersion, (SearchRelatedResourceLinkItem) getItem(QueryDS.RELATED_DATASET_VERSION));
        if (hasTemporalDimension && QueryTypeEnum.LATEST_DATA.equals(queryDto.getType())) {
            setValue(QueryDS.LATEST_N_DATA, queryDto.getLatestDataNumber());
        }
        this.markForRedraw();
    }

    public void setDatasetDimensionCodes(String dimensionId, List<CodeItemDto> codesDimension) {
        SearchMultipleCodeItemWindow window = dimensionCodeSelectionWindow.get(dimensionId);
        if (window != null) {
            window.setResources(codesDimension);
        }
    }

    private void setSelectedCodesForDimension(String dimensionId, List<CodeItemDto> selectedResources) {
        CodeItemListItem item = (CodeItemListItem) getItem(buildSelectionItemId(dimensionId));
        item.setCodeItems(selectedResources);
    }

    private CodeItemListItem createCodeListItemForDimension(final String datasetUrn, final String dimensionId, boolean editable) {
        CodeItemListItem item = new CodeItemListItem(buildSelectionItemId(dimensionId), dimensionId, editable);
        if (StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID.equals(dimensionId)) {
            item.setShowIfCondition(getFormItemIfFunctionShowTemporalDimension());
        } else {
            item.setShowIfCondition(getFormItemIfFunctionShowSelections());
        }
        item.setRequired(true);
        item.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                final SearchMultipleCodeItemWindow window = new SearchMultipleCodeItemWindow(StatisticalResourcesWeb.getConstants().codeSelection(), new SearchAction<MetamacWebCriteria>() {

                    @Override
                    public void retrieveResultSet(MetamacWebCriteria webCriteria) {
                        uiHandlers.retrieveDimensionCodesForDataset(datasetUrn, dimensionId, webCriteria);
                    }
                });
                dimensionCodeSelectionWindow.put(dimensionId, window);

                uiHandlers.retrieveDimensionCodesForDataset(datasetUrn, dimensionId, new MetamacWebCriteria());

                CodeItemListItem item = (CodeItemListItem) getItem(buildSelectionItemId(dimensionId));
                window.setSelectedResources(item.getCodeItemsDtos());

                window.setSaveAction(new ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        List<CodeItemDto> selectedResources = window.getSelectedResources();
                        window.markForDestroy();
                        // Set selected resource in form
                        setSelectedCodesForDimension(dimensionId, selectedResources);
                        validate(false);
                    }

                });
            }
        });

        return item;
    }

    private void retrieveDimensionsForDataset(String urn) {
        uiHandlers.retrieveDimensionsForDataset(urn);
    }

    // *******************************************************************
    // MAINTAINER
    // *******************************************************************

    public void setDefaultMaintainer(ExternalItemDto defaultMaintainer) {
        if (defaultMaintainer != null) {
            setValue(LifeCycleResourceDS.MAINTAINER, defaultMaintainer);
        }
    }

    public void setAgencySchemesForMaintainer(List<ExternalItemDto> externalItemsDtos, int firstResult, int elementsInPage, int totalResults) {
        if (maintainerItem != null) {
            maintainerItem.setFilterResources(externalItemsDtos, firstResult, elementsInPage, totalResults);
        }
    }

    public void setExternalItemsForMaintainer(List<ExternalItemDto> externalItemsDtos, int firstResult, int elementsInPage, int totalResults) {
        if (maintainerItem != null) {
            maintainerItem.setResources(externalItemsDtos, firstResult, elementsInPage, totalResults);
        }
    }

    private SearchSrmItemLinkItemWithSchemeFilterItem createMaintainerItem() {
        return new SearchSrmItemLinkItemWithSchemeFilterItem(LifeCycleResourceDS.MAINTAINER, getConstants().siemacMetadataStatisticalResourceMaintainer(),
                StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveItems(int firstResult, int maxResults, SrmItemRestCriteria webCriteria) {
                uiHandlers.retrieveAgencies(firstResult, maxResults, webCriteria);
            }

            @Override
            protected void retrieveItemSchemes(int firstResult, int maxResults, SrmExternalResourceRestCriteria webCriteria) {
                uiHandlers.retrieveAgencySchemes(firstResult, maxResults, webCriteria);
            }
        };
    }

    private FormItemIfFunction getFormItemIfFunctionShowTemporalDimension() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                CustomSelectItem selectType = ((CustomSelectItem) form.getItem(QueryDS.TYPE));
                String typeStr = selectType.getValueAsString();
                if (typeStr == null || QueryTypeEnum.LATEST_DATA.name().equals(typeStr)) {
                    return false;
                }
                return true;
            }
        };
    }

    private FormItemIfFunction getFormItemIfFunctionShowSelections() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                CustomSelectItem selectType = ((CustomSelectItem) form.getItem(QueryDS.TYPE));
                String typeStr = selectType.getValueAsString();
                if (typeStr != null) {
                    return true;
                }
                return false;
            }
        };
    }

    private FormItemIfFunction getFormItemIfFunctionShowLatestDataItem() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                CustomSelectItem selectType = ((CustomSelectItem) form.getItem(QueryDS.TYPE));
                String typeStr = selectType.getValueAsString();
                if (QueryTypeEnum.LATEST_DATA.name().equals(typeStr)) {
                    return true;
                }
                return false;
            }
        };
    }

    private FormItemIfFunction getFormItemIfFunctionEditionMode() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                ExternalItemDto maintainer = getValueAsExternalItemDto(SiemacMetadataDS.MAINTAINER_VIEW);
                return MetadataEditionChecks.canMaintainerBeEdited(maintainer != null ? maintainer.getId() : null);
            }
        };
    }

    private FormItemIfFunction getFormItemIfFunctionViewMode() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                ExternalItemDto maintainer = getValueAsExternalItemDto(SiemacMetadataDS.MAINTAINER_VIEW);
                return !MetadataEditionChecks.canMaintainerBeEdited(maintainer != null ? maintainer.getId() : null);
            }
        };
    }

    @Override
    public boolean validate() {
        boolean val = super.validate();
        for (String dimensionId : selectionFields.keySet()) {
            val = val && selectionFields.get(dimensionId).validate();
        }
        return val;
    }

    private class QueryTypeValidator extends CustomValidator {

        private final boolean hasTemporalDimension;

        public QueryTypeValidator(boolean hasTemporalDimension) {
            this.hasTemporalDimension = hasTemporalDimension;
        }

        @Override
        protected boolean condition(Object value) {
            if (hasTemporalDimension) {
                return true;
            } else {
                return QueryTypeEnum.FIXED.getName().equals(value);
            }
        }
    }

    private String buildSelectionItemId(String dimensionId) {
        return QueryDS.SELECTION + "_" + dimensionId;
    }
}
