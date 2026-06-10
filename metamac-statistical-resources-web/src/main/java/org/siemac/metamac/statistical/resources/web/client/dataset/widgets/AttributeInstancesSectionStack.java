package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.web.client.model.ds.DsdAttributeInstanceDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.DsdGranularityAttributeInstanceDS;
import org.siemac.metamac.statistical.resources.web.client.model.record.DsdAttributeInstanceRecord;
import org.siemac.metamac.statistical.resources.web.client.model.record.DsdGranularityAttributeInstanceRecord;
import org.siemac.metamac.statistical.resources.web.client.utils.StatisticalResourcesRecordUtils;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
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

    private static final String      FIELD_INSTANCE_TYPE = "instance-type";

    private CustomToolStripButton    newInstanceButton;
    private CustomToolStripButton    newGranularityInstanceButton;
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
        newGranularityInstanceButton = new CustomToolStripButton(getConstants().datasetAttributeNewGranularityInstance(), GlobalResources.RESOURCE.newListGrid().getURL());
        newGranularityInstanceButton.setVisible(false);
        deleteInstanceButton = new CustomToolStripButton(MetamacWebCommon.getConstants().actionDelete(), GlobalResources.RESOURCE.deleteListGrid().getURL());
        deleteInstanceButton.setVisible(canDelete);
        toolStrip.addButton(newInstanceButton);
        toolStrip.addButton(newGranularityInstanceButton);
        toolStrip.addButton(deleteInstanceButton);

        deleteInstanceButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        // ListGrid

        CustomListGridField typeField = new CustomListGridField(FIELD_INSTANCE_TYPE, getConstants().datasetAttributeInstanceType());
        typeField.setWidth(100);
        CustomListGridField valueField = new CustomListGridField(DsdAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
        listGrid.setFields(typeField, valueField);

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
        listGrid.setData(getValueRecords(dsdAttributeDto, dsdAttributeInstanceDtos));
        deleteInstanceButton.setVisible(false);
        show();
    }

    public DsdAttributeInstanceRecord[] getValueRecords(DsdAttributeDto dsdAttributeDto, List<DsdAttributeInstanceDto> dsdAttributeInstanceDtos) {
        DsdAttributeInstanceRecord[] valueRecords = StatisticalResourcesRecordUtils.getDsdAttributeInstanceRecords(dsdAttributeInstanceDtos, dsdAttributeDto);
        for (DsdAttributeInstanceRecord record : valueRecords) {
            record.setAttribute(FIELD_INSTANCE_TYPE, getConstants().datasetAttributeInstanceTypeValue());
        }
        return valueRecords;
    }

    public void addGranularityInstances(DsdAttributeDto dsdAttributeDto, List<DsdGranularityAttributeInstanceDto> granularityInstances) {
        if (granularityInstances == null) {
            return;
        }
        for (DsdGranularityAttributeInstanceDto instance : granularityInstances) {
            DsdGranularityAttributeInstanceRecord record = new DsdGranularityAttributeInstanceRecord();
            record.setUuid(instance.getUuid());
            record.setAttribute(FIELD_INSTANCE_TYPE, getConstants().datasetAttributeInstanceTypeGranularity());
            String summary = buildGranularityCodesSummary(instance);
            record.setGranularityCodesSummary(summary);
            setGranularityInstanceValue(record, instance, dsdAttributeDto);
            record.setDto(instance);
            listGrid.addData(record);
        }
    }

    private void setGranularityInstanceValue(DsdGranularityAttributeInstanceRecord record, DsdGranularityAttributeInstanceDto instance, DsdAttributeDto dsdAttributeDto) {
        if (instance.getValue() == null) {
            return;
        }
        if (instance.getValue().getExternalItemValue() != null) {
            record.setExternalItemValue(instance.getValue().getExternalItemValue());
        } else if (Boolean.TRUE.equals(dsdAttributeDto.getAttributeRepresentation().getMultilingualType())) {
            record.setStringValue(instance.getValue().getInternationalStringValue() != null
                    ? InternationalStringUtils.getLocalisedString(instance.getValue().getInternationalStringValue(), ApplicationEditionLanguages.getCurrentLocale())
                    : null);
        } else {
            record.setStringValue(instance.getValue().getStringValue());
        }
    }

    private String buildGranularityCodesSummary(DsdGranularityAttributeInstanceDto instance) {
        if (instance.getGranularityCodesByDimension() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : instance.getGranularityCodesByDimension().entrySet()) {
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append(entry.getKey()).append(": ");
            if (entry.getValue() != null) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(entry.getValue().get(i));
                }
            }
        }
        return sb.toString();
    }

    public List<String> getSelectedAttributeInstancesUuids() {
        return getSelectedValueInstancesUuids();
    }

    public List<String> getSelectedValueInstancesUuids() {
        List<String> uuids = new ArrayList<String>();
        ListGridRecord[] records = listGrid.getSelectedRecords();
        for (ListGridRecord record : records) {
            if (record instanceof DsdAttributeInstanceRecord) {
                uuids.add(((DsdAttributeInstanceRecord) record).getUuid());
            }
        }
        return uuids;
    }

    public List<String> getSelectedGranularityInstancesUuids() {
        List<String> uuids = new ArrayList<String>();
        ListGridRecord[] records = listGrid.getSelectedRecords();
        for (ListGridRecord record : records) {
            if (record instanceof DsdGranularityAttributeInstanceRecord) {
                uuids.add(((DsdGranularityAttributeInstanceRecord) record).getUuid());
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

    public ListGridRecord getSelectedGranularityInstance(String uuid) {
        ListGridRecord[] records = listGrid.getRecords();
        for (ListGridRecord record : records) {
            if (record instanceof DsdGranularityAttributeInstanceRecord) {
                if (((DsdGranularityAttributeInstanceRecord) record).getUuid().equals(uuid)) {
                    return record;
                }
            }
        }
        return null;
    }

    public HasClickHandlers getNewInstanceButton() {
        return newInstanceButton;
    }

    public HasClickHandlers getNewGranularityInstanceButton() {
        return newGranularityInstanceButton;
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

    public void setCanCreateGranularity(boolean canCreateGranularityInstance) {
        newGranularityInstanceButton.setVisible(canCreateGranularityInstance);
        newGranularityInstanceButton.markForRedraw();
    }

    public void setCanDelete(boolean canDeleteAttributeInstance) {
        this.canDelete = canDeleteAttributeInstance;
        deleteInstanceButton.setVisible(canDeleteAttributeInstance);
        deleteInstanceButton.markForRedraw();
    }
}
