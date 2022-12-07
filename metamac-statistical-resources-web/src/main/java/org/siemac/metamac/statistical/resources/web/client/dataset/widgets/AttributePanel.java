package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetAttributesTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.model.record.DsdAttributeInstanceRecord;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.StatisticalResourcesRecordUtils;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class AttributePanel extends VLayout {

    private AttributeInstancesSectionStack instancesSectionStack;
    private AttributeMainFormLayout        mainFormLayout;

    private DsdAttributeDto                dsdAttributeDto;

    private DatasetAttributesTabUiHandlers uiHandlers;

    public AttributePanel() {

        // Instances SectionStack

        instancesSectionStack = new AttributeInstancesSectionStack();
        instancesSectionStack.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() > 0 && event.getRecord() instanceof DsdAttributeInstanceRecord) {
                    DsdAttributeInstanceDto dsdAttributeInstanceDto = ((DsdAttributeInstanceRecord) event.getRecord()).getDsdAttributeInstanceDto();
                    mainFormLayout.showInstance(dsdAttributeDto, dsdAttributeInstanceDto);
                }
            }
        });

        instancesSectionStack.getNewInstanceButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                if (CommonUtils.hasDimensionRelationshipType(dsdAttributeDto)) {
                    DsdAttributeInstanceDto dsdAttributeInstanceDto = createNewAttributeInstance(dsdAttributeDto.getAttributeRelationship().getDimensions());
                    mainFormLayout.showInstance(dsdAttributeDto, dsdAttributeInstanceDto);
                    mainFormLayout.setEditionMode();
                } else if (CommonUtils.hasGroupRelationshipType(dsdAttributeDto)) {
                    DsdAttributeInstanceDto dsdAttributeInstanceDto = createNewAttributeInstance(dsdAttributeDto.getAttributeRelationship().getGroupDimensions());
                    mainFormLayout.showInstance(dsdAttributeDto, dsdAttributeInstanceDto);
                    mainFormLayout.setEditionMode();
                }
            }
        });

        instancesSectionStack.getConfirmDeleteButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<String> uuids = instancesSectionStack.getSelectedAttributeInstancesUuids();
                getUiHandlers().deleteAttributeInstances(dsdAttributeDto, uuids);
            }
        });

        addMember(instancesSectionStack);

        // Main form layout

        mainFormLayout = new AttributeMainFormLayout();

        mainFormLayout.getCancelToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (mainFormLayout.isCreateMode()) {
                    mainFormLayout.hide();
                } else {
                    getUiHandlers().retrieveAttributeInstancesForRefresh(dsdAttributeDto);
                }
                    
            }
        });

        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (mainFormLayout.getDsdAttributeInstanceDto() != null) {
                    getUiHandlers().deleteAttributeInstance(dsdAttributeDto, mainFormLayout.getDsdAttributeInstanceDto());
                }
            }
        });

        addMember(mainFormLayout);
    }
    public void showAttributeInstances(DsdAttributeDto dsdAttributeDto, List<DsdAttributeInstanceDto> dsdAttributeInstanceDtos) {

        this.dsdAttributeDto = dsdAttributeDto;

        hideInstances();
        if (CommonUtils.hasDatasetRelationshipType(dsdAttributeDto)) {
            showDatasetAttributeInstance(dsdAttributeDto, dsdAttributeInstanceDtos);
        } else if (CommonUtils.hasDimensionRelationshipType(dsdAttributeDto)) {
            showDimensionAttributeInstances(dsdAttributeDto, dsdAttributeInstanceDtos);
        } else if (CommonUtils.hasGroupRelationshipType(dsdAttributeDto)) {
            showGroupAttributeInstances(dsdAttributeDto, dsdAttributeInstanceDtos);
        }
        show();
    }

    public void resetDataAttributeInstance(DsdAttributeDto dsdAttributeDto, List<DsdAttributeInstanceDto> dsdAttributeInstanceDtos) {
        this.dsdAttributeDto = dsdAttributeDto;
        if (CommonUtils.hasDatasetRelationshipType(dsdAttributeDto)) {
            showAttributeInstances(dsdAttributeDto, dsdAttributeInstanceDtos);
        } else {
            // Updated attributeInstances from database because it could have been changed
            if (instancesSectionStack.getListGrid().getSelectedRecords() != null && instancesSectionStack.getListGrid().getSelectedRecords().length > 0) {
                ListGridRecord[] attributeInstances = instancesSectionStack.getListGrid().getSelectedRecords();
                if (attributeInstances != null && attributeInstances.length == 1
                        && (CommonUtils.hasDimensionRelationshipType(dsdAttributeDto) || CommonUtils.hasGroupRelationshipType(dsdAttributeDto))) {
                    instancesSectionStack.getListGrid().setData(StatisticalResourcesRecordUtils.getDsdAttributeInstanceRecords(dsdAttributeInstanceDtos));

                    DsdAttributeInstanceRecord dsdAttributeInstanceRecordUpdated = (DsdAttributeInstanceRecord) instancesSectionStack
                            .getSelectedAttributeInstance(((DsdAttributeInstanceRecord) attributeInstances[0]).getUuid());

                    mainFormLayout.showInstance(dsdAttributeDto, dsdAttributeInstanceRecordUpdated.getDsdAttributeInstanceDto());

                    instancesSectionStack.getListGrid().selectRecord(dsdAttributeInstanceRecordUpdated);

                }
            }
        }
    }
    
    public void setDimensionsCoverageValues(Map<String, List<CodeItemDto>> dimensionsCoverage) {
        mainFormLayout.setDimensionsCoverageValues(dimensionsCoverage);
    }

    private void showDatasetAttributeInstance(DsdAttributeDto dsdAttributeDto, List<DsdAttributeInstanceDto> dsdAttributeInstanceDtos) {
        // Attributes with dataset relationship only have one instance
        DsdAttributeInstanceDto dsdAttributeInstanceDto = dsdAttributeInstanceDtos != null && !dsdAttributeInstanceDtos.isEmpty()
                ? dsdAttributeInstanceDtos.get(0)
                : createNewDsdAttributeInstanceDtoWithDatasetAttachmentLevel();
        mainFormLayout.showInstance(dsdAttributeDto, dsdAttributeInstanceDto);
    }

    private DsdAttributeInstanceDto createNewDsdAttributeInstanceDtoWithDatasetAttachmentLevel() {
        DsdAttributeInstanceDto dsdAttributeInstanceDto = new DsdAttributeInstanceDto();
        dsdAttributeInstanceDto.setAttributeId(dsdAttributeDto.getIdentifier());
        return dsdAttributeInstanceDto;
    }

    private void showDimensionAttributeInstances(DsdAttributeDto dsdAttributeDto, List<DsdAttributeInstanceDto> dsdAttributeInstanceDtos) {
        instancesSectionStack.showInstances(dsdAttributeDto, dsdAttributeInstanceDtos);
    }

    private void showGroupAttributeInstances(DsdAttributeDto dsdAttributeDto, List<DsdAttributeInstanceDto> dsdAttributeInstanceDtos) {
        instancesSectionStack.showInstances(dsdAttributeDto, dsdAttributeInstanceDtos);
    }

    private DsdAttributeInstanceDto createNewAttributeInstance(List<String> dimensionIds) {
        DsdAttributeInstanceDto attributeInstance = new DsdAttributeInstanceDto();
        attributeInstance.setAttributeId(dsdAttributeDto.getIdentifier());
        attributeInstance.setCodeDimensions(new HashMap<String, List<CodeItemDto>>());
        for (String dimensionId : dimensionIds) {
            attributeInstance.getCodeDimensions().put(dimensionId, new ArrayList<CodeItemDto>());
        }
        return attributeInstance;
    }

    public void setUiHandlers(DatasetAttributesTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        mainFormLayout.setUiHandlers(uiHandlers);
    }

    public DatasetAttributesTabUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void hideInstances() {
        instancesSectionStack.hide();
        mainFormLayout.hide();
    }

    //
    // RELATED RESOURCES
    //

    public void setItemsForDatasetLevelAttributeValueSelection(List<ExternalItemDto> externalItemDtos, int firstResult, int totalResults) {
        mainFormLayout.setItemsForDatasetLevelAttributeValueSelection(externalItemDtos, firstResult, totalResults);
    }

    public void setItemsForDimensionOrGroupLevelAttributeValueSelection(List<ExternalItemDto> externalItemDtos, int firstResult, int totalResults) {
        mainFormLayout.setItemsForDimensionOrGroupLevelAttributeValueSelection(externalItemDtos, firstResult, totalResults);
    }

    public void updateButtonsVisibility(DatasetVersionDto datasetVersionDto) {
        boolean canCreateAttributeInstance = DatasetClientSecurityUtils.canCreateAttributeInstance(datasetVersionDto);
        boolean canUpdateAttributeInstance = DatasetClientSecurityUtils.canUpdateAttributeInstance(datasetVersionDto);
        boolean canDeleteAttributeInstance = DatasetClientSecurityUtils.canDeleteAttributeInstance(datasetVersionDto);

        instancesSectionStack.setCanCreate(canCreateAttributeInstance);
        instancesSectionStack.setCanDelete(canDeleteAttributeInstance);

        mainFormLayout.setCanEdit(canUpdateAttributeInstance);
        mainFormLayout.setCanDelete(canDeleteAttributeInstance);
    }
}
