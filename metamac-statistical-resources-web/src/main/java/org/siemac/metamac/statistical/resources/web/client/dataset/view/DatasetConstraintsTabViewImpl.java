package org.siemac.metamac.statistical.resources.web.client.dataset.view;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.constraint.ContentConstraintDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.RegionValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdDimensionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.ItemDto;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DimensionConstraintsDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.record.DimensionConstraintsRecord;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetConstraintsTabPresenter.DatasetConstraintsTabView;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.ConstraintsClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetConstraintsTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.DimensionConstraintMainFormLayout;
import org.siemac.metamac.statistical.resources.web.client.utils.StatisticalResourcesRecordUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;

import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class DatasetConstraintsTabViewImpl extends ViewWithUiHandlers<DatasetConstraintsTabUiHandlers> implements DatasetConstraintsTabView {

    private VLayout               panel;
    private ConstraintsPanel      constraintsPanel;

    private List<DsdDimensionDto> dsdDimensionDtos;
    private ContentConstraintDto  contentConstraintDto;
    private RegionValueDto        regionValueDto;

    public DatasetConstraintsTabViewImpl() {
        panel = new VLayout();
        panel.setMargin(5);
        panel.setHeight100();

        constraintsPanel = new ConstraintsPanel();
        panel.addMember(constraintsPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setConstraint(DatasetVersionDto datasetVersionDto, ContentConstraintDto contentConstraintDto, RegionValueDto regionValueDto) {
        this.contentConstraintDto = contentConstraintDto;
        this.regionValueDto = regionValueDto;
        this.constraintsPanel.setConstraint(datasetVersionDto, contentConstraintDto, regionValueDto);
    }

    @Override
    public void setRelatedDsdDimensions(List<DsdDimensionDto> dimensions) {
        this.dsdDimensionDtos = dimensions;
        constraintsPanel.setDimensions(dimensions, regionValueDto);
    }

    @Override
    public void setSrmResourceRestrictions(List<ExternalItemDto> restrictions, TypeExternalArtefactsEnum type) {
        constraintsPanel.setRestrictions(restrictions);
    }

    @Override
    public void setCodes(DsdDimensionDto dsdDimensionDto, ExternalItemDto itemScheme, List<ItemDto> itemDtos, String codeSrmRestriction) {
        if (StringUtils.isEmpty(codeSrmRestriction)) {
            constraintsPanel.setCodes(dsdDimensionDto, itemScheme, itemDtos);
        } else {
            constraintsPanel.setSrmRestrictionCodes(itemDtos);
        }
    }

    @Override
    public void setCodesWithSrmRestrictions(List<ItemDto> srmRestrictionCodes) {
        constraintsPanel.setCodesWithSrmRestriction(srmRestrictionCodes);
    }

    @Override
    public void setConcepts(DsdDimensionDto dsdDimensionDto, ExternalItemDto itemScheme, List<ItemDto> itemDtos) {
        constraintsPanel.setConcepts(dsdDimensionDto, itemScheme, itemDtos);
    }

    @Override
    public void showDimensionConstraints(DsdDimensionDto dsdDimensionDto) {
        constraintsPanel.showDimensionConstraints(dsdDimensionDto);
    }

    @Override
    public void updateDimensionsList(RegionValueDto regionValueDto) {
        constraintsPanel.setDimensions(dsdDimensionDtos, regionValueDto);
    }

    @Override
    public void setUiHandlers(DatasetConstraintsTabUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        constraintsPanel.setUiHandlers(uiHandlers);
    }

    private class ConstraintsPanel extends VLayout {

        private ToolStrip                         toolStrip;
        private CustomToolStripButton             enableConstraintsButton;
        private CustomToolStripButton             disableConstraintsButton;
        private InformationLabel                  informationLabel;
        private BaseCustomListGrid                constraintsList;
        private DimensionConstraintMainFormLayout mainFormLayout;

        public ConstraintsPanel() {
            createToolStrip();
            createInformationLabel();
            createConstraintsList();
            createDimensionConstraintMainFormLayout();
        }

        public void showDimensionConstraints(DsdDimensionDto dsdDimensionDto) {
            selectRowInConstraintList(dsdDimensionDto);
            mainFormLayout.showDimensionConstraints(dsdDimensionDto);
        }

        private void selectRowInConstraintList(DsdDimensionDto dsdDimensionDto) {
            if (constraintsList.getRecordList() != null) {
                Record dimensionRecord = constraintsList.getRecordList().find(DimensionConstraintsDS.DIMENSION_ID, dsdDimensionDto.getDimensionId());
                constraintsList.selectRecord(dimensionRecord);
            }
        }

        private void createToolStrip() {
            toolStrip = new ToolStrip();
            toolStrip.setWidth100();

            enableConstraintsButton = createEnableConstraintsButton();
            toolStrip.addButton(enableConstraintsButton);

            disableConstraintsButton = createDisableConstraintsButton();
            toolStrip.addButton(disableConstraintsButton);
            addMember(toolStrip);
        }

        private void createInformationLabel() {
            informationLabel = new InformationLabel(StatisticalResourcesWeb.getMessages().datasetConstraintNotExists());
            informationLabel.setMargin(10);
            informationLabel.setVisible(false);
            addMember(informationLabel);
        }

        private void createConstraintsList() {
            constraintsList = new BaseCustomListGrid();
            constraintsList.setVisible(true);
            constraintsList.setAutoFitMaxRecords(StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS);
            constraintsList.setAutoFitData(Autofit.VERTICAL);
            constraintsList.setDataSource(new DimensionConstraintsDS());
            constraintsList.setUseAllDataSourceFields(false);
            constraintsList.setWrapCells(true);
            constraintsList.setFixedRecordHeights(false);
            constraintsList.setAutoFitMaxHeight(250);
            constraintsList.setAutoFitData(Autofit.VERTICAL);

            ListGridField dimensionIdField = new ListGridField(DimensionConstraintsDS.DIMENSION_ID, getConstants().datasetConstraintDimensionId());
            dimensionIdField.setAlign(Alignment.LEFT);
            dimensionIdField.setWidth("20%");
            ListGridField inclusionTypeField = new ListGridField(DimensionConstraintsDS.INCLUSION_TYPE, getConstants().datasetConstraintInclusionType());
            inclusionTypeField.setWidth(90);
            ListGridField valuesField = new ListGridField(DimensionConstraintsDS.VALUES, getConstants().datasetConstraintValues());

            constraintsList.setFields(dimensionIdField, inclusionTypeField, valuesField);

            constraintsList.addSelectionChangedHandler(new SelectionChangedHandler() {

                @Override
                public void onSelectionChanged(SelectionEvent event) {
                    if (event.getSelectedRecord() instanceof DimensionConstraintsRecord) {
                        DsdDimensionDto dsdDimensionDto = ((DimensionConstraintsRecord) event.getSelectedRecord()).getDimensionDto();
                        showDimensionConstraints(dsdDimensionDto);
                    }
                }
            });

            addMember(constraintsList);
        }

        private void createDimensionConstraintMainFormLayout() {
            mainFormLayout = new DimensionConstraintMainFormLayout();
            mainFormLayout.setVisible(false);
            addMember(mainFormLayout);
        }

        public void setDimensions(List<DsdDimensionDto> dimensions, RegionValueDto regionValueDto) {
            DimensionConstraintsRecord[] records = StatisticalResourcesRecordUtils.getDimensionConstraintsRecords(dimensions, regionValueDto);
            constraintsList.setData(records);
            mainFormLayout.hide();
        }

        public void setCodes(DsdDimensionDto dsdDimensionDto, ExternalItemDto itemScheme, List<ItemDto> itemDtos) {
            mainFormLayout.setCodes(dsdDimensionDto, itemScheme, itemDtos);
        }

        public void setSrmRestrictionCodes(List<ItemDto> itemDtos) {
            mainFormLayout.setSrmRestrictionCodes(itemDtos);
        }

        public void setCodesWithSrmRestriction(List<ItemDto> itemDtos) {
            mainFormLayout.setEnumeratedValuesWithSrmRestrictions(itemDtos);
        }

        public void setRestrictions(List<ExternalItemDto> restrictions) {
            mainFormLayout.setRestrictions(restrictions);
        }

        public void setConcepts(DsdDimensionDto dsdDimensionDto, ExternalItemDto itemScheme, List<ItemDto> itemDtos) {
            mainFormLayout.setConcepts(dsdDimensionDto, itemScheme, itemDtos);
        }

        private CustomToolStripButton createEnableConstraintsButton() {
            CustomToolStripButton enableConstraintButton = new CustomToolStripButton(getConstants().actionEnableDatasetConstraints());
            enableConstraintButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    getUiHandlers().createConstraint();
                }
            });
            return enableConstraintButton;
        }

        private CustomToolStripButton createDisableConstraintsButton() {
            CustomToolStripButton disableConstraintButton = new CustomToolStripButton(getConstants().actionDisableDatasetConstraints());
            disableConstraintButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    DeleteConfirmationWindow deleteConfirmationWindow = new DeleteConfirmationWindow(getMessages().datasetConstraintDisableConfirmationTitle(),
                            getMessages().datasetConstraintDisableConfirmation());
                    deleteConfirmationWindow.show();
                    deleteConfirmationWindow.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            getUiHandlers().deleteConstraint(contentConstraintDto, regionValueDto);
                        }
                    });
                }
            });
            return disableConstraintButton;
        }

        private void setConstraint(DatasetVersionDto datasetVersionDto, ContentConstraintDto contentConstraintDto, RegionValueDto regionValueDto) {
            updateVisibility(datasetVersionDto, contentConstraintDto);
            mainFormLayout.setConstraint(datasetVersionDto, contentConstraintDto, regionValueDto);
        }

        private void updateVisibility(DatasetVersionDto datasetVersionDto, ContentConstraintDto contentConstraintDto) {
            if (contentConstraintDto == null) {
                enableConstraintsButton.setVisible(ConstraintsClientSecurityUtils.canCreateContentConstraint(datasetVersionDto));
                disableConstraintsButton.setVisible(false);
                informationLabel.show();
                constraintsList.setVisible(false);
                mainFormLayout.setVisible(false);
            } else {
                enableConstraintsButton.setVisible(false);
                disableConstraintsButton.setVisible(ConstraintsClientSecurityUtils.canDeleteContentConstraint(datasetVersionDto));
                constraintsList.setVisible(true);
                informationLabel.hide();
            }
        }

        public void setUiHandlers(DatasetConstraintsTabUiHandlers uiHandlers) {
            mainFormLayout.setUiHandlers(uiHandlers);
        }
    }
}
