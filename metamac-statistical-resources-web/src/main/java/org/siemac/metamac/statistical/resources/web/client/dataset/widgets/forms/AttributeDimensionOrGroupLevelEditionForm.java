package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.AttributeValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.DimensionCoverageValuesSelectionItem;
import org.siemac.metamac.statistical.resources.web.client.model.ds.DsdAttributeInstanceDS;
import org.siemac.metamac.statistical.resources.web.client.utils.DatasetAttibuteUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemSimpleItem;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

public class AttributeDimensionOrGroupLevelEditionForm extends AttributeDimensionOrGroupLevelBaseForm {

    private DsdAttributeInstanceDto dsdAttributeInstanceDto;
    private DsdAttributeDto         dsdAttributeDto;

    @Override
    protected void buildEnumeratedRepresentationForm(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        this.dsdAttributeInstanceDto = dsdAttributeInstanceDto;
        this.dsdAttributeDto = dsdAttributeDto;
        super.buildEnumeratedRepresentationForm(dsdAttributeDto, dsdAttributeInstanceDto);
    }

    @Override
    protected void buildNonEnumeratedRepresentationForm(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        this.dsdAttributeInstanceDto = dsdAttributeInstanceDto;
        this.dsdAttributeDto = dsdAttributeDto;
        DimensionCoverageValuesSelectionItem dimensionCoverageValuesSelectionItem = createDimensionValuesSelectionItem(DsdAttributeInstanceDS.DIMENSION_SELECTION_VALUES,
                getConstants().datasetAttributeDimensionValuesSelection(), dsdAttributeInstanceDto);

        CustomTextItem value = new CustomTextItem(DsdAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue() + " jajaja");
        value.setValidators(DatasetAttibuteUtils.getAttibuteValueLengthValidator());
        value.setRequired(true);

        setFields(dimensionCoverageValuesSelectionItem, value);

        setDimensionValues(dsdAttributeDto, dsdAttributeInstanceDto);

        if (dsdAttributeInstanceDto.getValue() != null) {
            setValue(DsdAttributeInstanceDS.VALUE, dsdAttributeInstanceDto.getValue().getStringValue());
        }
    }

    @Override
    protected DimensionCoverageValuesSelectionItem createDimensionValuesSelectionItem(String name, String title, DsdAttributeInstanceDto dsdAttributeInstanceDto) {

        List<String> dimensionIds = new ArrayList<String>(dsdAttributeInstanceDto.getCodeDimensions().keySet());

        DimensionCoverageValuesSelectionItem selectionItem = new DimensionCoverageValuesSelectionItem(name, title, dimensionIds, true);
        selectionItem.setColSpan(4);
        selectionItem.setRequired(true);

        // Load dimension values
        getUiHandlers().retrieveDimensionsCoverage(dimensionIds, new MetamacWebCriteria());

        return selectionItem;
    }

    public void setDimensionsCoverageValues(Map<String, List<CodeItemDto>> dimensionsCoverages) {
        Map<String, List<CodeItemDto>> dimensionCodes = dsdAttributeInstanceDto.getCodeDimensions();

        List<CodeItemDto> selectedCodes = new ArrayList<CodeItemDto>();
        for (String dimensionId : dimensionsCoverages.keySet()) {
            if (dimensionCodes != null && dimensionCodes.get(dimensionId) != null) {
                selectedCodes = dimensionCodes.get(dimensionId);
            }
            DimensionCoverageValuesSelectionItem dimensionValues = (DimensionCoverageValuesSelectionItem) getItem(DsdAttributeInstanceDS.DIMENSION_SELECTION_VALUES);
            dimensionValues.setDimensionCoverageValues(dimensionId, dimensionsCoverages.get(dimensionId));
            dimensionValues.selectDimensionCodes(dimensionId, selectedCodes);
        }
    }

    public DsdAttributeInstanceDto getDsdAttributeInstanceDto() {
        // Code dimensions
        Map<String, List<CodeItemDto>> codeItems = ((DimensionCoverageValuesSelectionItem) getItem(DsdAttributeInstanceDS.DIMENSION_SELECTION_VALUES)).getSelectedCodeDimensions();
        dsdAttributeInstanceDto.setCodeDimensions(codeItems);

        // Value
        AttributeValueDto attributeValueDto = new AttributeValueDto();
        if (getItem(DsdAttributeInstanceDS.VALUE) instanceof CustomTextItem) {
            attributeValueDto.setStringValue(getValueAsString(DsdAttributeInstanceDS.VALUE));
        } else if (getItem(DsdAttributeInstanceDS.VALUE) instanceof SearchExternalItemSimpleItem) {
            ExternalItemDto selectedExternalItemDto = ((SearchExternalItemSimpleItem) getItem(DsdAttributeInstanceDS.VALUE)).getExternalItemDto();
            attributeValueDto.setExternalItemValue(selectedExternalItemDto);
        }
        dsdAttributeInstanceDto.setValue(attributeValueDto);

        return dsdAttributeInstanceDto;
    }

    public DsdAttributeDto getDsdAttributeDto() {
        return dsdAttributeDto;
    }

    //
    // RELATED RESOURCES
    //

    public void setItemsForDimensionOrGroupLevelAttributeValueSelection(List<ExternalItemDto> externalItemDtos, int firstResult, int totalResults) {
        if (getItem(DsdAttributeInstanceDS.VALUE) != null && getItem(DsdAttributeInstanceDS.VALUE) instanceof SearchExternalItemSimpleItem) {
            ((SearchExternalItemSimpleItem) getItem(DsdAttributeInstanceDS.VALUE)).setResources(externalItemDtos, firstResult, totalResults);
        }
    }

    @Override
    protected SearchExternalItemSimpleItem createEnumeratedValueItem(String name, String title) {
        SearchExternalItemSimpleItem searchExternalItemSimpleItem = new SearchExternalItemSimpleItem(name, title, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                getUiHandlers().retrieveItemsFromItemSchemeForDimensionOrGroupLevelAttribute(dsdAttributeDto.getAttributeRepresentation(), firstResult, maxResults, webCriteria);
            }
        };
        searchExternalItemSimpleItem.setRequired(true);
        return searchExternalItemSimpleItem;
    }

    @Override
    protected void setDimensionValues(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        // FILLED After values are set from async action
    }

    @Override
    public Boolean validate(boolean validateHiddenFields) {
        return super.validate(validateHiddenFields);
    }

    @Override
    public boolean validate() {
        return super.validate();
    }
}
