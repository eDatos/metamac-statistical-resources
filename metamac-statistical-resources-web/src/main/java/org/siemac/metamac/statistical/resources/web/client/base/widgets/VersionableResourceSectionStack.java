package org.siemac.metamac.statistical.resources.web.client.base.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.statistical.resources.web.client.model.ds.LifeCycleResourceDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.CustomListGridSectionStack;
import org.siemac.metamac.web.common.client.widgets.utils.VersionFieldSortNormalizer;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.grid.ListGridField;

public class VersionableResourceSectionStack extends CustomListGridSectionStack {

    public VersionableResourceSectionStack(String title) {
        super(new BaseCustomListGrid(), title, "versionSectionStackStyle");
    }

    public VersionableResourceSectionStack(String title, boolean canRemoveElements) {
        super(new BaseCustomListGrid(), title, "versionSectionStackStyle", canRemoveElements);
    }

    protected void setListGridFields() {
        setListGridFields(null);
    }

    protected void setListGridFields(ListGridField... extraFields) {
        List<ListGridField> gridFields = new ArrayList<ListGridField>();
        // Add fields to listGrid
        addCodeFieldToListGrid(gridFields);
        addNameFieldToListGrid(gridFields);
        addProcStatusFieldToGrid(gridFields);
        addVersionFieldToListGrid(gridFields);
        addExtraFieldsToGrid(gridFields, extraFields);
        listGrid.setFields(gridFields.toArray(new ListGridField[gridFields.size()]));
        // To avoid multisort problems related to version column
        listGrid.setCanMultiSort(Boolean.FALSE);
        // Add listGrid to sectionStack
        defaultSection.setItems(listGrid);
    }

    protected void addExtraFieldsToGrid(List<ListGridField> gridFields, ListGridField... extraFields) {
        if (extraFields != null && extraFields.length > 0) {
            gridFields.addAll(Arrays.asList(extraFields));
        }
    }

    protected void addProcStatusFieldToGrid(List<ListGridField> gridFields) {
        ListGridField procStatusField = new ListGridField(LifeCycleResourceDS.PROC_STATUS, getConstants().lifeCycleStatisticalResourceProcStatus());
        gridFields.add(procStatusField);
    }

    protected void addNameFieldToListGrid(List<ListGridField> gridFields) {
        ListGridField nameField = new ListGridField(VersionableResourceDS.TITLE, getConstants().nameableStatisticalResourceTitle());
        gridFields.add(nameField);
    }

    protected void addCodeFieldToListGrid(List<ListGridField> gridFields) {
        ListGridField codeField = new ListGridField(VersionableResourceDS.CODE, getConstants().identifiableStatisticalResourceCode());
        codeField.setWidth("30%");
        gridFields.add(codeField);
    }

    protected void addVersionFieldToListGrid(List<ListGridField> gridFields) {
        ListGridField versionField = new ListGridField(VersionableResourceDS.VERSION, getConstants().versionableStatisticalResourceVersionLogic());
        versionField.setWidth("15%");
        versionField.setSortNormalizer(new VersionFieldSortNormalizer());
        gridFields.add(versionField);
    }

    public void selectRecord(String fieldName, String value) {
        RecordList recordList = listGrid.getRecordList();
        final int index = recordList.findIndex(fieldName, value);
        if (index >= 0) {
            listGrid.selectRecord(index);
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                @Override
                public void execute() {
                    listGrid.scrollToRow(index);
                }
            });
        }
    }
}
