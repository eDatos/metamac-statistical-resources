package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.web.client.model.ds.DsdAttributeInstanceDS;
import org.siemac.metamac.statistical.resources.web.client.model.record.DsdAttributeInstanceRecord;
import org.siemac.metamac.statistical.resources.web.client.utils.StatisticalResourcesRecordUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;
import org.siemac.metamac.web.common.client.widgets.CustomListGridSectionStack;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class AttributeInstancesSectionStack extends CustomListGridSectionStack {

    private CustomToolStripButton    newInstanceButton;
    private CustomToolStripButton    deleteInstanceButton;

    private DeleteConfirmationWindow deleteConfirmationWindow;

    private boolean                  canCreate = Boolean.TRUE;
    private boolean                  canDelete = Boolean.FALSE;

    public AttributeInstancesSectionStack() {
        super(new CustomListGrid(), getConstants().datasetAttributeInstances(), "versionSectionStackStyle");

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        newInstanceButton = new CustomToolStripButton(MetamacWebCommon.getConstants().actionNew(), GlobalResources.RESOURCE.newListGrid().getURL());
        newInstanceButton.setVisible(canCreate);
        deleteInstanceButton = new CustomToolStripButton(MetamacWebCommon.getConstants().actionDelete(), GlobalResources.RESOURCE.deleteListGrid().getURL());
        deleteInstanceButton.setVisible(canDelete);
        toolStrip.addButton(newInstanceButton);
        toolStrip.addButton(deleteInstanceButton);

        deleteInstanceButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        // ListGrid

        CustomListGridField valueField = new CustomListGridField(DsdAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
        listGrid.setFields(valueField);

        listGrid.addSelectionUpdatedHandler(new SelectionUpdatedHandler() {

            @Override
            public void onSelectionUpdated(SelectionUpdatedEvent event) {
                if (listGrid.getSelectedRecords() != null && listGrid.getSelectedRecords().length > 0) {
                    deleteInstanceButton.setVisible(canDelete);
                } else {
                    deleteInstanceButton.setVisible(false);
                }
            }
        });

        // Add listGrid to sectionStack
        defaultSection.setItems(toolStrip, listGrid);

        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacWebCommon.getConstants().deleteConfirmationTitle(), MetamacWebCommon.getConstants().deleteConfirmationMessage());
        deleteConfirmationWindow.setVisible(false);
    }

    public ListGrid getListGrid() {
        return listGrid;
    }
    
    public void showInstances(DsdAttributeDto dsdAttributeDto, List<DsdAttributeInstanceDto> dsdAttributeInstanceDtos) {
        setSectionTitle(getMessages().datasetAttributeIntances(dsdAttributeDto.getIdentifier()));
        listGrid.setData(StatisticalResourcesRecordUtils.getDsdAttributeInstanceRecords(dsdAttributeInstanceDtos));
        deleteInstanceButton.setVisible(false);
        show();
    }

    public List<String> getSelectedAttributeInstancesUuids() {
        List<String> uuids = new ArrayList<String>();
        ListGridRecord[] records = listGrid.getSelectedRecords();
        for (ListGridRecord record : records) {
            if (record instanceof DsdAttributeInstanceRecord) {
                uuids.add(((DsdAttributeInstanceRecord) record).getUuid());
            }
        }
        return uuids;
    }

    public ListGridRecord getSelectedAttributeInstance(String uuid) {
        ListGridRecord[] records = listGrid.getRecords();
        for (ListGridRecord record : records) {
            if (record instanceof DsdAttributeInstanceRecord) {
                if (((DsdAttributeInstanceRecord) record).getUuid().equals(uuid)) {
                    return record;
                }
            }
        }
        return null;
    }
    
    public HasClickHandlers getNewInstanceButton() {
        return newInstanceButton;
    }

    public HasClickHandlers getConfirmDeleteButton() {
        return deleteConfirmationWindow.getYesButton();
    }

    private void setSectionTitle(String title) {
        defaultSection.setTitle(title);
        markForRedraw();
    }

    public void setCanCreate(boolean canCreateAttributeInstance) {
        this.canCreate = canCreateAttributeInstance;
        newInstanceButton.setVisible(canCreateAttributeInstance);
        newInstanceButton.markForRedraw();
    }

    public void setCanDelete(boolean canDeleteAttributeInstance) {
        this.canDelete = canDeleteAttributeInstance;
        deleteInstanceButton.setVisible(canDeleteAttributeInstance);
        deleteInstanceButton.markForRedraw();
    }
}
