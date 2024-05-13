package org.siemac.metamac.statistical.resources.web.client.query.view.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.model.ds.ExternalItemDS;
import org.siemac.metamac.web.common.client.model.ds.RelatedResourceBaseDS;
import org.siemac.metamac.web.common.client.model.record.ExternalItemRecord;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.NavigationUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.ExternalItemListItem;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchMultipleExternalItemPaginatedWindow;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public abstract class SearchMultiExternalItem extends ExternalItemListItem {

    private static final String DELETE_ICON = "deleteIcon";
    private SearchMultipleExternalItemPaginatedWindow window;

    public SearchMultiExternalItem(String name, String title) {
        super(name, title, true);
        setColumnsToShow(new HashSet<String>(Arrays.asList(ExternalItemDS.CODE, ExternalItemDS.TITLE, ExternalItemDS.URN)));
        ListGridField[] gridFields = listGrid.getAllFields();
        ListGridField deleteField = new ListGridField(DELETE_ICON, getConstants().delete());
        deleteField.setType(ListGridFieldType.ICON);
        deleteField.setCellFormatter(new com.smartgwt.client.widgets.grid.CellFormatter() {

            @Override
            public String format(Object arg0, ListGridRecord arg1, int arg2, int arg3) {
                String url = GlobalResources.RESOURCE.deleteListGrid().getURL();
                return "<img src='" + url +"' style='cursor:pointer;' />";
            }
        });
        setListGrid(true);
        gridFields[3] = deleteField;
        listGrid.setFields(gridFields);
    }

    private void setListGrid(boolean editionMode) {
        listGrid = null;
        listGrid = new BaseCustomListGrid();
        CustomListGridField codeField = new CustomListGridField(RelatedResourceBaseDS.CODE, MetamacWebCommon.getConstants().relatedResourceCode());
        CustomListGridField nameField = new CustomListGridField(RelatedResourceBaseDS.TITLE, MetamacWebCommon.getConstants().relatedResourceTitle());
        CustomListGridField urnField = new CustomListGridField(RelatedResourceBaseDS.URN, MetamacWebCommon.getConstants().relatedResourceURN());
        listGrid.setFields(codeField, nameField, urnField);
        setCellStyle("dragAndDropCellStyle");

        listGrid = new BaseCustomListGrid();
        listGrid.setAutoFitMaxRecords(6);
        listGrid.setAutoFitData(Autofit.VERTICAL);

        HLayout hLayout = new HLayout();
        hLayout.addMember(listGrid);
        hLayout.setStyleName("canvasCellStyle");

        // In edition mode, add a search icon to edit concept list
        if (editionMode) {
            searchViewTextItem = new SearchViewTextItem();
            searchViewTextItem.setShowTitle(false);

            DynamicForm form = new DynamicForm();
            form.setFields(searchViewTextItem);

            hLayout.addMember(form);
            form.setWidth("1%");
            listGrid.setWidth("99%");
        } else {
            setTitleStyle("staticFormItemTitle");
            listGrid.setWidth100();
        }

        setCanvas(hLayout);
    }

    public void setResources(List<ExternalItemDto> items, int firstResult, int totalResults) {
        if (window != null) {
            window.setResources(items);
            window.refreshSourcePaginationInfo(firstResult, items.size(), totalResults);
        }
    }

    public String getInformationLabelContents() {
        // By default, no information label is set. This method must be override to set an information label on the top of the search window.
        return null;
    }

    public void setColumnsToShow(Set<String> fieldNamesToShow) {
        showFields(getListGrid(), fieldNamesToShow);
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

    public abstract void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria);

    public List<String> getCodes() {
        List<ExternalItemDto> externalItems = getExternalItemDtos();
        if (externalItems == null || externalItems.isEmpty()) {
            return null;
        }
        List<String> codes = new ArrayList<String>();
        for (ExternalItemDto externalItem : externalItems) {
            codes.add(externalItem.getCode());
        }
        return codes;
    }

    public SearchMultipleExternalItemPaginatedWindow getWindow() {
        return window;
    }

    public void setWindow(SearchMultipleExternalItemPaginatedWindow window) {
        this.window = window;
    }

    public BaseCustomListGrid getListGrid() {
        return listGrid;
    }
}
