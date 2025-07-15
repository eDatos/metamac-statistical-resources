package org.siemac.metamac.statistical.resources.web.client.query.view.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.query.model.ds.QueryDS;
import org.siemac.metamac.statistical.resources.web.client.query.view.handlers.QueryListUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.query.view.widgets.forms.SearchMultiExternalItem;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.CodeItemListItem;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.search.SearchMultipleCodeItemWindow;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.search.SearchSingleDatasetVersionRelatedResourcePaginatedWindow;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DatasetVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.model.ds.ExternalItemDS;
import org.siemac.metamac.web.common.client.model.record.ExternalItemRecord;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.NavigationUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchAction;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomIntegerItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchMultipleExternalItemPaginatedWindow;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class NewQueryWindow extends CustomWindow {

    private static final int                                         FORM_ITEM_CUSTOM_WIDTH = 300;
    private static final int                                         MAX_HEIGHT             = 800;
    private static final int                                         MAX_NUM_OF_ELEMENTS    = 7;
    private static final String                                      FIELD_SAVE             = "save-query";

    protected CustomDynamicForm                                      form;
    private QueryListUiHandlers                                      uiHandlers;
    private SearchExternalItemLinkItem                               relatedDatasetItem;
    private SearchSingleDatasetVersionRelatedResourcePaginatedWindow searchDatasetWindow;
    private Map<String, CodeItemListItem>                            selectionFields;
    private Map<String, SearchMultipleCodeItemWindow>                dimensionCodeSelectionWindow;
    private SearchMultiExternalItem                                  searchTemporalGranularitiesWindow;

    public NewQueryWindow(String title) {
        super(title);
        setAutoSize(true);

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setColWidths("30%", "70%");
        form.setWidth100();
        form.setHeight100();
        List<FormItem> fields = createComponents();
        form.setFields(fields.toArray(new FormItem[fields.size()]));
        addItem(form);
        show();
    }

    private List<FormItem> createComponents() {
        List<FormItem> items = new ArrayList<FormItem>();
        RequiredTextItem nameItem = new RequiredTextItem(QueryDS.TITLE, getConstants().nameableStatisticalResourceTitle());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        items.add(nameItem);
        relatedDatasetItem = createQueryDatasetItem();
        relatedDatasetItem.setRequired(true);
        items.add(relatedDatasetItem);
        CustomSelectItem typeSelectorItem = new CustomSelectItem(QueryDS.TYPE, getConstants().queryType());
        typeSelectorItem.setValueMap(CommonUtils.getQueryTypeHashMap());
        typeSelectorItem.setRequired(true);
        typeSelectorItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                form.markForRedraw();
            }
        });
        CustomSelectItem purposeTypeSelectorItem = new CustomSelectItem(QueryDS.PURPOSE_TYPE, getConstants().purpose());
        purposeTypeSelectorItem.setValueMap(CommonUtils.getPurposesHashMap());
        items.add(typeSelectorItem);
        items.add(purposeTypeSelectorItem);

        return items;
    }
    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }
    public void setUiHandlers(QueryListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
    public QueryVersionDto getNewQueryDto() {
        QueryVersionDto queryDto = new QueryVersionDto();
        QueryTypeEnum queryType = QueryTypeEnum.valueOf(form.getValueAsString(QueryDS.TYPE));
        queryDto.setCode(form.getValueAsString(QueryDS.CODE));
        queryDto.setTitle(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(QueryDS.TITLE)));
        queryDto.setSelection(new HashMap<String, List<CodeItemDto>>());
        queryDto.setRelatedDatasetVersion(RelatedResourceUtils.getRelatedResourceFromExternalItemDto(form.getValueAsExternalItemDto(QueryDS.RELATED_DATASET_VERSION)));
        queryDto.setMaintainer(StatisticalResourcesDefaults.defaultAgency);
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
            CustomIntegerItem customIntegerItem = (CustomIntegerItem) form.getItem(QueryDS.LATEST_N_DATA);  
            latestDataNumber = customIntegerItem != null ? customIntegerItem.getValueAsInteger() : null;
        }
        queryDto.setLatestDataNumber(latestDataNumber);
        setTemporalGranularitie(queryDto);
        queryDto.setPurpose(CommonUtils.getPurpose(((CustomSelectItem) form.getItem(QueryDS.PURPOSE_TYPE)).getValueAsString()));
        return queryDto;
    }

    private void setTemporalGranularitie(QueryVersionDto queryVersionDto) {
        List<ExternalItemDto> externalItemsDto = new ArrayList<ExternalItemDto>();
        externalItemsDto.addAll(searchTemporalGranularitiesWindow.getSelectedRelatedResources());
        queryVersionDto.getTemporalGranularities().addAll(externalItemsDto);
    }

    public boolean validateForm() {
        return form.validate();
    }

    private SearchExternalItemLinkItem createQueryDatasetItem() {

        final SearchExternalItemLinkItem item = new SearchExternalItemLinkItem(QueryDS.RELATED_DATASET_VERSION, getConstants().queryDataset()) {

            @Override
            public void onSearch() {

                searchDatasetWindow = new SearchSingleDatasetVersionRelatedResourcePaginatedWindow(getConstants().resourceSelection(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS,
                        new SearchPaginatedAction<DatasetVersionWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, DatasetVersionWebCriteria criteria) {
                                retrieveDatasetsAsResources(firstResult, maxResults, criteria);
                            }
                        });

                // Load resources (to populate the selection window)
                retrieveStatisticalOperationsForDatasetSelection();

                searchDatasetWindow.setSaveAction(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        ExternalItemDto selectedResource = ExternalItemUtils.getExternalItemDtoFromRelatedResourceDto(searchDatasetWindow.getSelectedResource(), TypeExternalArtefactsEnum.DATASET);
                        searchDatasetWindow.markForDestroy();
                        // Set selected resource in form
                        setRelatedDataset(selectedResource);
                        form.validate(false);
                    }
                });
            }
        };
        return item;
    }

    public void setDatasetDimensionsIds(List<String> datasetDimensions) {
        RelatedResourceDto datasetVersion = RelatedResourceUtils.getRelatedResourceFromExternalItemDto(form.getValueAsExternalItemDto(QueryDS.RELATED_DATASET_VERSION));
        dimensionCodeSelectionWindow = new HashMap<String, SearchMultipleCodeItemWindow>();

        List<FormItem> fields = createComponents();

        boolean hasTemporalDimension = false;

        selectionFields = new HashMap<String, CodeItemListItem>();
        // force TIME_PERIOD last dimension
        if (datasetDimensions.contains(StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID)) {
            datasetDimensions.remove(StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID);
            datasetDimensions.add(StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID);
            hasTemporalDimension = true;
        }

        setDimensions(datasetDimensions, datasetVersion, fields);

        CustomIntegerItem latestData = new CustomIntegerItem(QueryDS.LATEST_N_DATA, getConstants().queryLatestNData());
        latestData.setShowIfCondition(getFormItemIfFunctionShowLatestDataItem());
        latestData.setRequired(true);
        fields.add(latestData);
        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().queryCreate());
        saveItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                if (validateForm()) {
                    QueryVersionDto queryVersionDto = getNewQueryDto();
                    uiHandlers.createQuery(queryVersionDto);
                    destroy();
                }
            }
        });
        fields.add(saveItem);
        form.setFields(fields.toArray(new FormItem[fields.size()]));
        if (fields.size() > MAX_NUM_OF_ELEMENTS) {
            form.setHeight(MAX_HEIGHT);
            form.setWidth(MAX_HEIGHT);
            form.setOverflow(Overflow.AUTO);
        }
        form.redraw();
    }

    public void setCodesForTemporalGranularities(List<ExternalItemDto> items, int firstResult, int totalResults) {
        searchTemporalGranularitiesWindow.setResources(items, firstResult, totalResults);
    }

    private void setDimensions(List<String> datasetDimensions, RelatedResourceDto datasetVersion, List<FormItem> fields) {
        for (String dimensionId : datasetDimensions) {
            CodeItemListItem item = createCodeListItemForDimension(datasetVersion.getUrn(), dimensionId, true);
            selectionFields.put(dimensionId, item);
            createTemporalGranularitiesItem(datasetVersion.getUrn());
            if (!dimensionId.equals("TIME_PERIOD")) {
                fields.add(item);
            } else {
                searchTemporalGranularitiesWindow.setShowIfCondition(getFormItemIfFunctionShowSelections());
                fields.add(searchTemporalGranularitiesWindow);
                fields.add(item);
            }
        }
    }

    private void createTemporalGranularitiesItem(final String datasetUrn) {
        searchTemporalGranularitiesWindow = new SearchMultiExternalItem(QueryDS.TYPE_GRANULARITIES, getConstants().datasetTemporalGranularitiesCapitalLetter()) {

            @Override
            public void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                uiHandlers.retrieveTemporalCodesForField(firstResult, maxResults, datasetUrn, webCriteria);
            }
        };
        appendWindow(searchTemporalGranularitiesWindow, datasetUrn);
        setRecordHandlerToListGrid(searchTemporalGranularitiesWindow);
    }

    private void appendWindow(final SearchMultiExternalItem item, final String datasetUrn) {
        item.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                item.setWindow(new SearchMultipleExternalItemPaginatedWindow(getConstants().datasetTemporalGranularitiesCapitalLetter(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, new SearchPaginatedAction<MetamacWebCriteria>() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                        uiHandlers.retrieveTemporalCodesForField(firstResult, maxResults, datasetUrn, webCriteria);
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
        CodeItemListItem item = (CodeItemListItem) form.getItem(QueryDS.SELECTION + "_" + StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID);
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
    private void retrieveDimensionsForDataset(String urn) {
        uiHandlers.retrieveDimensionsForDataset(urn);
    }

    private CodeItemListItem createCodeListItemForDimension(final String datasetUrn, final String dimensionId, final boolean editable) {
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
                final SearchMultipleCodeItemWindow window = new SearchMultipleCodeItemWindow(dimensionId, new SearchAction<MetamacWebCriteria>() {

                    @Override
                    public void retrieveResultSet(MetamacWebCriteria webCriteria) {
                        uiHandlers.retrieveDimensionCodesForDataset(datasetUrn, dimensionId, webCriteria, searchTemporalGranularitiesWindow.getCodes());
                    }
                });
                dimensionCodeSelectionWindow.put(dimensionId, window);

                uiHandlers.retrieveDimensionCodesForDataset(datasetUrn, dimensionId, new MetamacWebCriteria(), searchTemporalGranularitiesWindow.getCodes());

                final CodeItemListItem item = new CodeItemListItem(buildSelectionItemId(dimensionId), dimensionId, editable);
                window.setSelectedResources(item.getCodeItemsDtos());
                //
                window.setSaveAction(new ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        List<CodeItemDto> selectedResources = window.getSelectedResources();
                        window.markForDestroy();
                        // Set selected resource in form
                        setSelectedCodesForDimension(dimensionId, selectedResources, editable);
                        form.validate(false);
                    }

                });
            }
        });

        return item;
    }

    private FormItemIfFunction getFormItemIfFunctionShowLatestDataItem() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                CustomSelectItem selectType = ((CustomSelectItem) form.getItem(QueryDS.TYPE));
                String typeStr = selectType.getValueAsString();
                return (QueryTypeEnum.LATEST_DATA.name().equals(typeStr));
            }
        };
    }

    private void setSelectedCodesForDimension(String dimensionId, List<CodeItemDto> selectedResources, boolean editable) {
        CodeItemListItem item = (CodeItemListItem) form.getItem(buildSelectionItemId(dimensionId));
        item.setCodeItems(selectedResources);
    }

    private FormItemIfFunction getFormItemIfFunctionShowTemporalDimension() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                CustomSelectItem selectType = ((CustomSelectItem) form.getItem(QueryDS.TYPE));
                String typeStr = selectType.getValueAsString();
                return !(typeStr == null || QueryTypeEnum.LATEST_DATA.name().equals(typeStr));
            }
        };
    }

    private FormItemIfFunction getFormItemIfFunctionShowSelections() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                CustomSelectItem selectType = ((CustomSelectItem) form.getItem(QueryDS.TYPE));
                String typeStr = selectType.getValueAsString();
                return (typeStr != null);
            }
        };
    }

    private String buildSelectionItemId(String dimensionId) {
        return QueryDS.SELECTION + "_" + dimensionId;
    }

    private void setRelatedDataset(ExternalItemDto relatedDsdDto) {
        form.setValue(QueryDS.RELATED_DATASET_VERSION, relatedDsdDto);
        // Get dimensions
        if (relatedDsdDto != null) {
            retrieveDimensionsForDataset(relatedDsdDto.getUrn());
        }
    }

    public void retrieveResourcesForRelatedDataset(int firstResult, int maxResults, DatasetVersionWebCriteria criteria) {
        uiHandlers.retrieveDatasetForRelatedDataset(firstResult, maxResults, criteria);
    }

    private void retrieveStatisticalOperationsForDatasetSelection() {
        uiHandlers.retrieveStatisticalOperationsForDatasetSelection();
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

    private void retrieveDatasetsAsResources(int firstResult, int maxResults, DatasetVersionWebCriteria criteria) {
        uiHandlers.retrieveDatasetsForQuery(firstResult, maxResults, criteria);
    }

    public void setDatasetDimensionCodes(String dimensionId, List<CodeItemDto> codesDimension) {
        SearchMultipleCodeItemWindow window = dimensionCodeSelectionWindow.get(dimensionId);
        if (window != null) {
            window.setResources(codesDimension);
        }
    }
}
