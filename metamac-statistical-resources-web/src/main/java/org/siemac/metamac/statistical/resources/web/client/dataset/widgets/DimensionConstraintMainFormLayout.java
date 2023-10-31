package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.constraint.ContentConstraintDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyValueDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.RegionValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdDimensionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.ItemDto;
import org.siemac.metamac.statistical.resources.core.enume.constraint.domain.RegionValueTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DimensionTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.ConstraintsClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetConstraintsTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.ConstraintEnumeratedValuesSelectionEditionForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.ConstraintEnumeratedValuesSelectionForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.ConstraintNonEnumeratedValuesSelectionEditionForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.ConstraintNonEnumeratedValuesSelectionForm;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.widgets.form.MainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.utils.FormUtils;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

public class DimensionConstraintMainFormLayout extends MainFormLayout {

    private DatasetConstraintsTabUiHandlers                   uiHandlers;

    private ConstraintEnumeratedValuesSelectionForm           enumeratedValuesSelectionForm;
    private ConstraintEnumeratedValuesSelectionEditionForm    enumeratedValuesSelectionEditionForm;

    private ConstraintNonEnumeratedValuesSelectionForm        nonEnumeratedValuesSelectionForm;
    private ConstraintNonEnumeratedValuesSelectionEditionForm nonEnumeratedValuesSelectionEditionForm;

    private DatasetVersionDto                                 datasetVersionDto;
    private ContentConstraintDto                              contentConstraintDto;
    private RegionValueDto                                    regionValueDto;

    private static final String                               GROUP_FORM_DEFAULT_NAME = "DIMENSION";

    public DimensionConstraintMainFormLayout() {

        // ENUMERATED VALUES SELECTION FORMS

        enumeratedValuesSelectionForm = new ConstraintEnumeratedValuesSelectionForm(GROUP_FORM_DEFAULT_NAME);
        addViewCanvas(enumeratedValuesSelectionForm);

        enumeratedValuesSelectionEditionForm = new ConstraintEnumeratedValuesSelectionEditionForm(GROUP_FORM_DEFAULT_NAME);
        addEditionCanvas(enumeratedValuesSelectionEditionForm);

        // NON ENUMERATED VALUES SELECTION FORMS

        nonEnumeratedValuesSelectionForm = new ConstraintNonEnumeratedValuesSelectionForm(GROUP_FORM_DEFAULT_NAME);
        addViewCanvas(nonEnumeratedValuesSelectionForm);

        nonEnumeratedValuesSelectionEditionForm = new ConstraintNonEnumeratedValuesSelectionEditionForm(GROUP_FORM_DEFAULT_NAME);
        addEditionCanvas(nonEnumeratedValuesSelectionEditionForm);

        // Bind events

        getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (enumeratedValuesSelectionEditionForm.isVisible()) {
                    if (enumeratedValuesSelectionEditionForm.validate(false)) {
                        if (regionValueDto == null) {
                            regionValueDto = createRegion();
                        }
                        regionValueDto = enumeratedValuesSelectionEditionForm.updateRegionDto(regionValueDto);
                        getUiHandlers().saveRegion(contentConstraintDto.getUrn(), regionValueDto, enumeratedValuesSelectionEditionForm.getSelectedDimension());
                    }
                } else if (nonEnumeratedValuesSelectionEditionForm.isVisible()) {
                    if (nonEnumeratedValuesSelectionEditionForm.validate(false)) {
                        if (regionValueDto == null) {
                            regionValueDto = createRegion();
                        }
                        regionValueDto = nonEnumeratedValuesSelectionEditionForm.updateRegionDto(regionValueDto);
                        getUiHandlers().saveRegion(contentConstraintDto.getUrn(), regionValueDto, nonEnumeratedValuesSelectionEditionForm.getSelectedDimension());
                    }
                }
            }
        });

        getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (enumeratedValuesSelectionForm.isVisible()) {
                    regionValueDto = CommonUtils.removeKeyValueOfDimension(regionValueDto, enumeratedValuesSelectionForm.getSelectedDimension());
                    getUiHandlers().saveRegion(contentConstraintDto.getUrn(), regionValueDto, enumeratedValuesSelectionForm.getSelectedDimension());
                } else if (nonEnumeratedValuesSelectionForm.isVisible()) {
                    regionValueDto = CommonUtils.removeKeyValueOfDimension(regionValueDto, nonEnumeratedValuesSelectionForm.getSelectedDimension());
                    getUiHandlers().saveRegion(contentConstraintDto.getUrn(), regionValueDto, nonEnumeratedValuesSelectionForm.getSelectedDimension());
                }
            }
        });
    }

    public void setConstraint(DatasetVersionDto datasetVersionDto, ContentConstraintDto contentConstraintDto, RegionValueDto regionValueDto) {
        this.datasetVersionDto = datasetVersionDto;
        this.contentConstraintDto = contentConstraintDto;
        this.regionValueDto = regionValueDto;
    }

    public void showDimensionConstraints(DsdDimensionDto dimension) {
        hideAllForms();
        if (!StringUtils.isBlank(dimension.getCodelistRepresentationUrn())) {
            showRestrictions(true);
            getUiHandlers().retrieveCodes(dimension, null);
            getUiHandlers().retrieveRestrictions(dimension, TypeExternalArtefactsEnum.CODELIST_RESTRICTION);
        } else if (!StringUtils.isBlank(dimension.getConceptSchemeRepresentationUrn())) {
            showRestrictions(false);
            getUiHandlers().retrieveConcepts(dimension);
        } else if (DimensionTypeEnum.TEMPORAL.equals(dimension.getType())) {
            showRestrictions(false);
            setNonEnumeratedValues(dimension);
        }
        setViewMode();
        updateButtonsVisibility(dimension);
    }

    public void showRestrictions(boolean isIncluded) {
        enumeratedValuesSelectionEditionForm.showRestrictions(isIncluded);
    }

    public void setCodes(DsdDimensionDto dsdDimensionDto, ExternalItemDto itemScheme, List<ItemDto> itemDtos) {
        setEnumeratedValues(dsdDimensionDto, itemScheme, itemDtos);
    }

    public void setSrmRestrictionCodes(List<ItemDto> itemDtos) {
        enumeratedValuesSelectionEditionForm.setSrmRestrictionCodes(itemDtos);
    }

    public void setEnumeratedValuesWithSrmRestrictions(List<ItemDto> itemDtos) {
        enumeratedValuesSelectionEditionForm.setSrmRestrictionCodes(itemDtos);
    }

    public void setRestrictions(List<ExternalItemDto> restrictions) {
        enumeratedValuesSelectionEditionForm.setRestrictions(restrictions);
    }

    public void setConcepts(DsdDimensionDto dsdDimensionDto, ExternalItemDto itemScheme, List<ItemDto> itemDtos) {
        setEnumeratedValues(dsdDimensionDto, itemScheme, itemDtos);
    }

    public void setEnumeratedValues(DsdDimensionDto dsdDimensionDto, ExternalItemDto itemScheme, List<ItemDto> itemDtos) {
        FormUtils.setGroupTitle(dsdDimensionDto.getDimensionId(), enumeratedValuesSelectionForm, enumeratedValuesSelectionEditionForm);
        enumeratedValuesSelectionForm.setRegionValues(regionValueDto, dsdDimensionDto, itemScheme, itemDtos);
        enumeratedValuesSelectionEditionForm.setRegionValues(regionValueDto, dsdDimensionDto, itemScheme, itemDtos);
        showEnumeratedValuesSelectionForms();
    }

    public void setNonEnumeratedValues(DsdDimensionDto dsdDimensionDto) {
        FormUtils.setGroupTitle(dsdDimensionDto.getDimensionId(), nonEnumeratedValuesSelectionForm, nonEnumeratedValuesSelectionEditionForm);
        nonEnumeratedValuesSelectionForm.setRegionValues(regionValueDto, dsdDimensionDto);
        nonEnumeratedValuesSelectionEditionForm.setRegionValues(regionValueDto, dsdDimensionDto);
        showNonEnumeratedValuesSelectionForms();
    }

    private void showEnumeratedValuesSelectionForms() {
        enumeratedValuesSelectionForm.show();
        enumeratedValuesSelectionEditionForm.show();
        show();
    }

    private void showNonEnumeratedValuesSelectionForms() {
        nonEnumeratedValuesSelectionForm.show();
        nonEnumeratedValuesSelectionEditionForm.show();
        show();
    }

    private void hideAllForms() {
        enumeratedValuesSelectionForm.hide();
        enumeratedValuesSelectionEditionForm.hide();
        nonEnumeratedValuesSelectionForm.hide();
        nonEnumeratedValuesSelectionEditionForm.hide();
        hide();
    }

    private RegionValueDto createRegion() {
        RegionValueDto regionValueDto = new RegionValueDto();
        regionValueDto.setContentConstraintUrn(contentConstraintDto.getUrn());
        regionValueDto.setRegionValueTypeEnum(RegionValueTypeEnum.CUBE);
        return regionValueDto;
    }

    private void updateButtonsVisibility(DsdDimensionDto selectedDimension) {
        boolean canModifyConstraint = ConstraintsClientSecurityUtils.canSaveForContentConstraint(datasetVersionDto);
        KeyValueDto keyValueDto = CommonUtils.getKeyValueOfDimension(selectedDimension, regionValueDto);
        boolean existsKeyValueOfDimension = keyValueDto != null;
        setCanEdit(canModifyConstraint);
        setCanDelete(canModifyConstraint && existsKeyValueOfDimension);
    }

    public void setUiHandlers(DatasetConstraintsTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        this.enumeratedValuesSelectionEditionForm.setUiHandlers(uiHandlers);
    }

    public DatasetConstraintsTabUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
