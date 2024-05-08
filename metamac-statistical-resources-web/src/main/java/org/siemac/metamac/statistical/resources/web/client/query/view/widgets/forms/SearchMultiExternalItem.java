package org.siemac.metamac.statistical.resources.web.client.query.view.widgets.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.web.common.client.model.ds.ExternalItemDS;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.ExternalItemListItem;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchMultipleExternalItemPaginatedWindow;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

public abstract class SearchMultiExternalItem extends ExternalItemListItem {

    private SearchMultipleExternalItemPaginatedWindow window;

    public SearchMultiExternalItem(String name, String title, int maxResults) {
        super(name, title, true);
        setColumnsToShow(new HashSet<String>(Arrays.asList(ExternalItemDS.CODE, ExternalItemDS.TITLE, ExternalItemDS.URN)));
        //appendWindow(maxResults);
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
}
