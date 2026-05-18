package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.web.client.model.ds.CodeItemDS;
import org.siemac.metamac.statistical.resources.web.client.model.record.CodeItemRecord;
import org.siemac.metamac.statistical.resources.web.client.utils.StatisticalResourcesRecordUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class DimensionCoverageValuesSelectionItem extends CustomCanvasItem {

    private BaseCustomListGrid selectedDimensionValuesListGrid;

    private CustomDynamicForm  dimensionsForm;

    private boolean            editionMode;

    private List<String>       dimensionIds;

    public DimensionCoverageValuesSelectionItem(String name, String title, List<String> dimensionIds, boolean editionMode) {
        super(name, title);
        setShouldSaveValue(true);

        setCellStyle("dragAndDropCellStyle");

        this.editionMode = editionMode;
        this.dimensionIds = dimensionIds;

        // Dimensions layout (form)

        dimensionsForm = new CustomDynamicForm();
        dimensionsForm.setNumCols(3);
        dimensionsForm.setColWidths("33%", "33%", "33%");
        List<DimensionsListGridItem> dimensionItems = new ArrayList<DimensionsListGridItem>();
        for (String dimensionId : dimensionIds) {
            DimensionsListGridItem item = createDimensionListGridItem(dimensionId);
            dimensionItems.add(item);
        }
        dimensionsForm.setFields(dimensionItems.toArray(new DimensionsListGridItem[dimensionItems.size()]));

        // Selected dimensions list

        selectedDimensionValuesListGrid = new BaseCustomListGrid();
        selectedDimensionValuesListGrid.setAutoFitData(Autofit.VERTICAL);
        selectedDimensionValuesListGrid.setAutoFitMaxRecords(8);
        CustomListGridField dimensionField = new CustomListGridField(CodeItemDS.DIMENSION_ID, getConstants().codeItemDimension());
        CustomListGridField codeField = new CustomListGridField(CodeItemDS.CODE, getConstants().codeItemCode());
        CustomListGridField titleField = new CustomListGridField(CodeItemDS.TITLE, getConstants().codeItemTitle());
        selectedDimensionValuesListGrid.setFields(dimensionField, codeField, titleField);

        VLayout mainPanel = new VLayout(10);
        mainPanel.setAutoHeight();
        mainPanel.setOverflow(Overflow.VISIBLE);
        mainPanel.addMember(dimensionsForm);
        mainPanel.addMember(selectedDimensionValuesListGrid);
        mainPanel.setPadding(10);
        setCanvas(mainPanel);

    }
    @Override
    public void setRequired(Boolean required) {
        super.setRequired(required);
        for (FormItem item : dimensionsForm.getFields()) {
            item.setRequired(required);
        }
    }

    public Map<String, List<CodeItemDto>> getSelectedCodeDimensions() {
        Map<String, List<CodeItemDto>> values = new HashMap<String, List<CodeItemDto>>();
        for (String dimensionId : dimensionIds) {
            List<CodeItemDto> codeItemDtos = ((DimensionsListGridItem) dimensionsForm.getItem(dimensionId)).getSelectedCodeItemDtos();
            values.put(dimensionId, codeItemDtos);
        }
        return values;
    }

    private DimensionsListGridItem createDimensionListGridItem(final String dimensionId) {
        final DimensionsListGridItem dimensionsListGridItem = new DimensionsListGridItem(dimensionId);
        if (editionMode) {
            dimensionsListGridItem.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

                @Override
                public void onSelectionChanged(SelectionEvent event) {
                    if (event.getRecord() instanceof CodeItemRecord) {
                        CodeItemRecord selectedCodeItemRecord = (CodeItemRecord) event.getRecord();
                        if (event.getState()) {
                            // The record is being selected, so it is added to the selected values list
                            CodeItemRecord record = StatisticalResourcesRecordUtils.getCodeItemRecord(dimensionId, selectedCodeItemRecord.getCodeItemDto());
                            selectedDimensionValuesListGrid.addData(record);
                        } else {
                            // The record is being unselected, so it is removed from the selected values list
                            if (selectedDimensionValuesListGrid.getRecordList() != null) {
                                Map<String, String> criteriaMap = new HashMap<String, String>();
                                criteriaMap.put(CodeItemDS.DIMENSION_ID, dimensionId);
                                criteriaMap.put(CodeItemDS.CODE, selectedCodeItemRecord.getCode());
                                Record[] records = selectedDimensionValuesListGrid.getRecordList().findAll(criteriaMap);
                                if (records != null && records.length > 0) {
                                    selectedDimensionValuesListGrid.removeData(records[0]); // the result should be only one
                                }
                            }
                        }
                    }
                }
            });
            dimensionsListGridItem.getListGrid().addSelectionUpdatedHandler(new SelectionUpdatedHandler() {

                @Override
                public void onSelectionUpdated(SelectionUpdatedEvent event) {
                    storeValue(buildRecordForForm());
                }

            });
        }
        return dimensionsListGridItem;
    }

    // To use form validation we trick the form using appropiate record or null
    private Record buildRecordForForm() {
        for (String dimensionId : dimensionIds) {
            if (dimensionsForm.getItem(dimensionId) != null) {
                ListGridRecord[] selectedRecords = ((DimensionsListGridItem) dimensionsForm.getItem(dimensionId)).getListGrid().getSelectedRecords();
                if (selectedRecords.length == 0) {
                    return null;
                }
            } else {
                return null;
            }
        }
        return new Record();
    }

    public void setDimensionCoverageValues(String dimensionId, List<CodeItemDto> codeItemDtos) {
        if (dimensionsForm.getItem(dimensionId) != null) {
            CodeItemRecord[] records = StatisticalResourcesRecordUtils.getCodeItemRecords(codeItemDtos);
            DimensionsListGridItem listGridItem = ((DimensionsListGridItem) dimensionsForm.getItem(dimensionId));
            listGridItem.getListGrid().setData(records);
        }
    }

    public void selectDimensionCodes(String dimensionId, List<CodeItemDto> selected) {
        if (dimensionsForm.getItem(dimensionId) != null) {
            DimensionsListGridItem listGridItem = ((DimensionsListGridItem) dimensionsForm.getItem(dimensionId));
            ListGridRecord[] records = listGridItem.getListGrid().getRecords();
            for (CodeItemDto selectedItem : selected) {
                for (ListGridRecord record : records) {
                    if (((CodeItemRecord) record).getCode().equals(selectedItem.getCode())) {
                        listGridItem.getListGrid().selectRecord(record);
                        if (!editionMode) { // no selection handler, must add it manually
                            CodeItemRecord selectionRecord = StatisticalResourcesRecordUtils.getCodeItemRecord(dimensionId, selectedItem);
                            selectedDimensionValuesListGrid.addData(selectionRecord);
                        }
                    }
                }
            }

        }
    }

    public void syncTemporalToSummary(String dimensionId, List<CodeItemDto> items) {
        if (selectedDimensionValuesListGrid.getRecordList() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(CodeItemDS.DIMENSION_ID, dimensionId);
            Record[] existing = selectedDimensionValuesListGrid.getRecordList().findAll(criteria);
            if (existing != null) {
                for (Record record : existing) {
                    selectedDimensionValuesListGrid.removeData(record);
                }
            }
        }
        for (CodeItemDto item : items) {
            CodeItemRecord record = StatisticalResourcesRecordUtils.getCodeItemRecord(dimensionId, item);
            selectedDimensionValuesListGrid.addData(record);
        }
    }

    private class DimensionsListGridItem extends CustomCanvasItem {

        protected BaseCustomListGrid customListGrid;

        public DimensionsListGridItem(String name) {
            super(name, StringUtils.EMPTY);
            setCellStyle("dragAndDropCellStyle");
            setShowTitle(false);

            customListGrid = new BaseCustomListGrid();

            if (editionMode) {
                customListGrid.setSelectionType(SelectionStyle.SIMPLE);
                customListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
                customListGrid.setCanSelectAll(true);
            }

            customListGrid.setAutoFitMaxRecords(6);
            customListGrid.setAutoFitData(Autofit.VERTICAL);
            customListGrid.setHeaderHeight(40);
            CustomListGridField codeField = new CustomListGridField(CodeItemDS.CODE, getConstants().codeItemCode());
            CustomListGridField titleField = new CustomListGridField(CodeItemDS.TITLE, getConstants().codeItemTitle());
            customListGrid.setFields(codeField, titleField);
            customListGrid.setHeaderSpans(new HeaderSpan(name, new String[]{CodeItemDS.CODE, CodeItemDS.TITLE}));

            HLayout hLayout = new HLayout();
            hLayout.addMember(customListGrid);
            hLayout.setStyleName("canvasCellStyle");

            setCanvas(hLayout);
        }

        public BaseCustomListGrid getListGrid() {
            return customListGrid;
        }

        public List<CodeItemDto> getSelectedCodeItemDtos() {
            List<CodeItemDto> codeItemDtos = new ArrayList<CodeItemDto>();
            ListGridRecord[] selectedRecords = customListGrid.getSelectedRecords();
            for (ListGridRecord record : selectedRecords) {
                if (record instanceof CodeItemRecord) {
                    codeItemDtos.add(((CodeItemRecord) record).getCodeItemDto());
                }
            }
            return codeItemDtos;
        }
    }
}
