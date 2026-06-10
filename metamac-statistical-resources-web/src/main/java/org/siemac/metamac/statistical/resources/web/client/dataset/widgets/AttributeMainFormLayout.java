package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetAttributesTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.AttributeDatasetLevelEditionForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.AttributeDatasetLevelForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.AttributeDimensionOrGroupLevelEditionForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.AttributeDimensionOrGroupLevelForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.AttributeGranularityLevelEditionForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.AttributeGranularityLevelForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

public class AttributeMainFormLayout extends InternationalMainFormLayout {

    private DatasetAttributesTabUiHandlers            uiHandlers;

    private AttributeDatasetLevelForm                 attributeDatasetLevelForm;
    private AttributeDatasetLevelEditionForm          attributeDatasetLevelEditionForm;

    private AttributeDimensionOrGroupLevelForm        attributeDimensionOrGroupLevelForm;
    private AttributeDimensionOrGroupLevelEditionForm attributeDimensionOrGroupLevelEditionForm;

    private AttributeGranularityLevelForm             attributeGranularityLevelForm;
    private AttributeGranularityLevelEditionForm      attributeGranularityLevelEditionForm;

    private boolean                                   createMode;
    private DsdAttributeInstanceDto                   dsdAttributeInstanceDto;
    private DsdGranularityAttributeInstanceDto        dsdGranularityAttributeInstanceDto;

    public AttributeMainFormLayout() {
        setCanEdit(true);

        bindMainFormLayoutEvents();

        attributeDatasetLevelForm = new AttributeDatasetLevelForm();
        addViewCanvas(attributeDatasetLevelForm);

        attributeDatasetLevelEditionForm = new AttributeDatasetLevelEditionForm();
        addEditionCanvas(attributeDatasetLevelEditionForm);

        attributeDimensionOrGroupLevelForm = new AttributeDimensionOrGroupLevelForm();
        addViewCanvas(attributeDimensionOrGroupLevelForm);

        attributeDimensionOrGroupLevelEditionForm = new AttributeDimensionOrGroupLevelEditionForm();
        addEditionCanvas(attributeDimensionOrGroupLevelEditionForm);

        attributeGranularityLevelForm = new AttributeGranularityLevelForm();
        addViewCanvas(attributeGranularityLevelForm);

        attributeGranularityLevelEditionForm = new AttributeGranularityLevelEditionForm();
        addEditionCanvas(attributeGranularityLevelEditionForm);

        getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (attributeDatasetLevelEditionForm.isVisible()) {
                    if (attributeDatasetLevelEditionForm.validate(false)) {
                        getUiHandlers().saveAttributeInstance(attributeDatasetLevelEditionForm.getDsdAttributeDto(), attributeDatasetLevelEditionForm.getDsdAttributeInstanceDto());
                    }
                } else if (attributeDimensionOrGroupLevelEditionForm.isVisible()) {
                    if (attributeDimensionOrGroupLevelEditionForm.validate(false)) {
                        getUiHandlers().saveAttributeInstance(attributeDimensionOrGroupLevelEditionForm.getDsdAttributeDto(),
                                attributeDimensionOrGroupLevelEditionForm.getDsdAttributeInstanceDto());
                    }
                } else if (attributeGranularityLevelEditionForm.isVisible()) {
                    if (attributeGranularityLevelEditionForm.validate(false)) {
                        getUiHandlers().saveGranularityAttributeInstance(attributeGranularityLevelEditionForm.getDsdAttributeDto(),
                                attributeGranularityLevelEditionForm.getDsdGranularityAttributeInstanceDto());
                    }
                }
            }
        });
    }

    private void bindMainFormLayoutEvents() {
        getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                setTranslation();
            }
        });
    }

    private void setTranslation() {
        if (getTranslateToolStripButton().isVisible()) {
            boolean translationsShowed = Boolean.TRUE.equals(getTranslateToolStripButton().isSelected());
            attributeDatasetLevelForm.setTranslationsShowed(translationsShowed);
            attributeDatasetLevelEditionForm.setTranslationsShowed(translationsShowed);
            attributeDimensionOrGroupLevelForm.setTranslationsShowed(translationsShowed);
            attributeDimensionOrGroupLevelEditionForm.setTranslationsShowed(translationsShowed);
            attributeGranularityLevelForm.setTranslationsShowed(translationsShowed);
            attributeGranularityLevelEditionForm.setTranslationsShowed(translationsShowed);
        }
    }

    public boolean isCreateMode() {
        return createMode;
    }

    public void showInstance(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        hideAllForms();
        this.dsdAttributeInstanceDto = dsdAttributeInstanceDto;
        this.dsdGranularityAttributeInstanceDto = null;

        boolean canDeleteBck = canDelete;

        createMode = dsdAttributeInstanceDto.getUuid() == null;
        setCanDelete(canDelete && !createMode);

        switch (dsdAttributeDto.getAttributeRelationship().getRelationshipType()) {
            case NO_SPECIFIED_RELATIONSHIP:
                showDatasetLevelForm(dsdAttributeDto, dsdAttributeInstanceDto);
                break;
            case DIMENSION_RELATIONSHIP:
                showDimensionOrGroupLevelForm(dsdAttributeDto, dsdAttributeInstanceDto);
                break;
            case GROUP_RELATIONSHIP:
                showDimensionOrGroupLevelForm(dsdAttributeDto, dsdAttributeInstanceDto);
                break;
            default:
                break;
        }
        if (!createMode) {
            this.setViewMode();
        }

        canDelete = canDeleteBck;

        if (Boolean.TRUE.equals(dsdAttributeDto.getAttributeRepresentation().getMultilingualType())) {
            getTranslateToolStripButton().show();
            setTranslation();
        } else {
            getTranslateToolStripButton().hide();
        }

    }

    private void showDatasetLevelForm(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        attributeDatasetLevelForm.setAttribute(dsdAttributeDto, dsdAttributeInstanceDto);
        attributeDatasetLevelForm.show();

        attributeDatasetLevelEditionForm.setAttribute(dsdAttributeDto, dsdAttributeInstanceDto);
        attributeDatasetLevelEditionForm.show();

        show();
    }

    private void showDimensionOrGroupLevelForm(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        attributeDimensionOrGroupLevelForm.setAttribute(dsdAttributeDto, dsdAttributeInstanceDto);
        attributeDimensionOrGroupLevelForm.show();

        attributeDimensionOrGroupLevelEditionForm.setAttribute(dsdAttributeDto, dsdAttributeInstanceDto);
        attributeDimensionOrGroupLevelEditionForm.show();

        show();
    }

    public void showGranularityInstance(DsdAttributeDto dsdAttributeDto, DsdGranularityAttributeInstanceDto dto) {
        hideAllForms();
        this.dsdGranularityAttributeInstanceDto = dto;
        this.dsdAttributeInstanceDto = null;

        createMode = dto.getUuid() == null;
        boolean canDeleteBck = canDelete;
        setCanDelete(canDelete && !createMode);

        attributeGranularityLevelForm.setAttribute(dsdAttributeDto, dto);
        attributeGranularityLevelForm.show();

        attributeGranularityLevelEditionForm.setAttribute(dsdAttributeDto, dto);
        attributeGranularityLevelEditionForm.show();

        if (createMode) {
            setEditionMode();
        } else {
            setViewMode();
        }

        canDelete = canDeleteBck;

        show();

        if (Boolean.TRUE.equals(dsdAttributeDto.getAttributeRepresentation().getMultilingualType())) {
            getTranslateToolStripButton().show();
            setTranslation();
        } else {
            getTranslateToolStripButton().hide();
        }
    }

    private void hideAllForms() {
        attributeDatasetLevelForm.hide();
        attributeDatasetLevelEditionForm.hide();
        attributeDimensionOrGroupLevelForm.hide();
        attributeDimensionOrGroupLevelEditionForm.hide();
        attributeGranularityLevelForm.hide();
        attributeGranularityLevelEditionForm.hide();
        hide();
    }

    public DsdAttributeInstanceDto getDsdAttributeInstanceDto() {
        return dsdAttributeInstanceDto;
    }

    public DsdGranularityAttributeInstanceDto getDsdGranularityAttributeInstanceDto() {
        return dsdGranularityAttributeInstanceDto;
    }

    public void setUiHandlers(DatasetAttributesTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        attributeDatasetLevelForm.setUiHandlers(uiHandlers);
        attributeDatasetLevelEditionForm.setUiHandlers(uiHandlers);
        attributeDimensionOrGroupLevelForm.setUiHandlers(uiHandlers);
        attributeDimensionOrGroupLevelEditionForm.setUiHandlers(uiHandlers);
        attributeGranularityLevelEditionForm.setUiHandlers(uiHandlers);
    }

    @Override
    public void setEditionMode() {
        super.setEditionMode();
        // Force redraw of edition forms to recover from SmartGWT 3.0 issue where CanvasItems
        // set via setFields() while the parent layout was hidden are not rendered when re-shown.
        editionFormLayout.markForRedraw();
    }

    public DatasetAttributesTabUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setDimensionsCoverageValues(Map<String, List<CodeItemDto>> dimensionsCoverages) {
        attributeDimensionOrGroupLevelEditionForm.setDimensionsCoverageValues(dimensionsCoverages);
        attributeGranularityLevelForm.setDimensionsCoverageValues(dimensionsCoverages);
        attributeGranularityLevelEditionForm.setDimensionsCoverageValues(dimensionsCoverages);
    }

    public void setTemporalGranularities(List<ExternalItemDto> granularities) {
        attributeGranularityLevelForm.setTemporalGranularities(granularities);
        attributeGranularityLevelEditionForm.setTemporalGranularities(granularities);
    }

    public void setItemsForDatasetLevelAttributeValueSelection(List<ExternalItemDto> externalItemDtos, int firstResult, int totalResults) {
        attributeDatasetLevelEditionForm.setItemsForDatasetLevelAttributeValueSelection(externalItemDtos, firstResult, totalResults);
    }

    public void setItemsForDimensionOrGroupLevelAttributeValueSelection(List<ExternalItemDto> externalItemDtos, int firstResult, int totalResults) {
        attributeDimensionOrGroupLevelEditionForm.setItemsForDimensionOrGroupLevelAttributeValueSelection(externalItemDtos, firstResult, totalResults);
        attributeGranularityLevelEditionForm.setItemsForDimensionOrGroupLevelAttributeValueSelection(externalItemDtos, firstResult, totalResults);
    }
}
