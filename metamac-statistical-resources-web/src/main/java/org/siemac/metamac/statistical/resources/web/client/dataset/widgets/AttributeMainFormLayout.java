package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetAttributesTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.AttributeDatasetLevelEditionForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.AttributeDatasetLevelForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.AttributeDimensionOrGroupLevelEditionForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.AttributeDimensionOrGroupLevelForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

public class AttributeMainFormLayout extends InternationalMainFormLayout {

    private DatasetAttributesTabUiHandlers            uiHandlers;

    private AttributeDatasetLevelForm                 attributeDatasetLevelForm;
    private AttributeDatasetLevelEditionForm          attributeDatasetLevelEditionForm;

    private AttributeDimensionOrGroupLevelForm        attributeDimensionOrGroupLevelForm;
    private AttributeDimensionOrGroupLevelEditionForm attributeDimensionOrGroupLevelEditionForm;

    private boolean                                   createMode;
    private DsdAttributeInstanceDto                   dsdAttributeInstanceDto;

    public AttributeMainFormLayout() {
        setCanEdit(true);

        bindMainFormLayoutEvents();

        // DATASET LEVEL FORMS

        attributeDatasetLevelForm = new AttributeDatasetLevelForm();
        addViewCanvas(attributeDatasetLevelForm);

        attributeDatasetLevelEditionForm = new AttributeDatasetLevelEditionForm();
        addEditionCanvas(attributeDatasetLevelEditionForm);

        // DIMENSION LEVEL FORMS

        attributeDimensionOrGroupLevelForm = new AttributeDimensionOrGroupLevelForm();
        addViewCanvas(attributeDimensionOrGroupLevelForm);

        attributeDimensionOrGroupLevelEditionForm = new AttributeDimensionOrGroupLevelEditionForm();
        addEditionCanvas(attributeDimensionOrGroupLevelEditionForm);

        // Bind events

        getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (attributeDatasetLevelEditionForm.isVisible()) {
                    if (attributeDatasetLevelEditionForm.validate(false)) {
                        getUiHandlers().saveAttributeInstance(attributeDatasetLevelEditionForm.getDsdAttributeDto(), attributeDatasetLevelEditionForm.getDsdAttributeInstanceDto());
                    }
                } else if (attributeDimensionOrGroupLevelEditionForm.isVisible()) {
                    if (attributeDimensionOrGroupLevelEditionForm.validate(false)) {
                        getUiHandlers().saveAttributeInstance(attributeDimensionOrGroupLevelEditionForm.getDsdAttributeDto(), attributeDimensionOrGroupLevelEditionForm.getDsdAttributeInstanceDto());
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
        boolean translationsShowed = getTranslateToolStripButton().isSelected();
        attributeDatasetLevelForm.setTranslationsShowed(translationsShowed);
        attributeDatasetLevelEditionForm.setTranslationsShowed(translationsShowed);
        attributeDimensionOrGroupLevelForm.setTranslationsShowed(translationsShowed);
        attributeDimensionOrGroupLevelEditionForm.setTranslationsShowed(translationsShowed);
    }

    public boolean isCreateMode() {
        return createMode;
    }

    public void showInstance(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        hideAllForms();
        this.dsdAttributeInstanceDto = dsdAttributeInstanceDto;

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

    private void hideAllForms() {
        attributeDatasetLevelForm.hide();
        attributeDatasetLevelEditionForm.hide();
        attributeDimensionOrGroupLevelForm.hide();
        attributeDimensionOrGroupLevelEditionForm.hide();
        hide();
    }

    public DsdAttributeInstanceDto getDsdAttributeInstanceDto() {
        return dsdAttributeInstanceDto;
    }

    public void setUiHandlers(DatasetAttributesTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        attributeDatasetLevelForm.setUiHandlers(uiHandlers);
        attributeDatasetLevelEditionForm.setUiHandlers(uiHandlers);
        attributeDimensionOrGroupLevelForm.setUiHandlers(uiHandlers);
        attributeDimensionOrGroupLevelEditionForm.setUiHandlers(uiHandlers);
    }

    public DatasetAttributesTabUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setDimensionsCoverageValues(Map<String, List<CodeItemDto>> dimensionsCoverages) {
        attributeDimensionOrGroupLevelEditionForm.setDimensionsCoverageValues(dimensionsCoverages);
    }

    //
    // RELATED RESOURCES
    //

    public void setItemsForDatasetLevelAttributeValueSelection(List<ExternalItemDto> externalItemDtos, int firstResult, int totalResults) {
        attributeDatasetLevelEditionForm.setItemsForDatasetLevelAttributeValueSelection(externalItemDtos, firstResult, totalResults);
    }

    public void setItemsForDimensionOrGroupLevelAttributeValueSelection(List<ExternalItemDto> externalItemDtos, int firstResult, int totalResults) {
        attributeDimensionOrGroupLevelEditionForm.setItemsForDimensionOrGroupLevelAttributeValueSelection(externalItemDtos, firstResult, totalResults);
    }
}
