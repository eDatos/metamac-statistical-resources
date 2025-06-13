package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.DimensionCoverageValuesSelectionItem;
import org.siemac.metamac.statistical.resources.web.client.model.ds.DsdAttributeInstanceDS;
import org.siemac.metamac.statistical.resources.web.client.utils.DatasetAttibuteUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

public class AttributeDimensionOrGroupLevelForm extends AttributeDimensionOrGroupLevelBaseForm {

    @Override
    protected void buildNonEnumeratedRepresentationForm(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        DimensionCoverageValuesSelectionItem dimensionCoverageValuesSelectionItem = createDimensionValuesSelectionItem(DsdAttributeInstanceDS.DIMENSION_SELECTION_VALUES,
                getConstants().datasetAttributeDimensionValuesSelection(), dsdAttributeInstanceDto);

        if (Boolean.TRUE.equals(dsdAttributeDto.getAttributeRepresentation().getMultilingualType())) {
            ViewMultiLanguageTextItem value = buildMultilanguageTextItemNonEnumeratedRepresentationForm(dsdAttributeInstanceDto);
            setFields(dimensionCoverageValuesSelectionItem, value);

        } else {
            ViewTextItem value = buildTextItemNonEnumeratedRepresentationForm(dsdAttributeInstanceDto);
            setFields(dimensionCoverageValuesSelectionItem, value);
        }

        setDimensionValues(dsdAttributeDto, dsdAttributeInstanceDto);
    }

    private ViewTextItem buildTextItemNonEnumeratedRepresentationForm(DsdAttributeInstanceDto dsdAttributeInstanceDto) {

        ViewTextItem value = new ViewTextItem(DsdAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
        value.setValidators(DatasetAttibuteUtils.getDimensionOrGroupLevelAttibuteValueLengthValidator());
        value.setRequired(true);

        if (dsdAttributeInstanceDto.getValue() != null) {
            setValue(DsdAttributeInstanceDS.VALUE, dsdAttributeInstanceDto.getValue().getStringValue());
        }

        return value;
    }

    private ViewMultiLanguageTextItem buildMultilanguageTextItemNonEnumeratedRepresentationForm(DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        ViewMultiLanguageTextItem value = new ViewMultiLanguageTextItem(DsdAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
        value.setValidators(DatasetAttibuteUtils.getDimensionOrGroupLevelAttibuteValueLengthValidator());

        if (dsdAttributeInstanceDto.getValue() != null) {
            setValue(DsdAttributeInstanceDS.VALUE, RecordUtils.getInternationalStringRecord(dsdAttributeInstanceDto.getValue().getInternationalStringValue()));
        }

        return value;

    }

    @Override
    protected void setDimensionValues(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        Map<String, List<CodeItemDto>> dimensionCodes = dsdAttributeInstanceDto.getCodeDimensions();
        DimensionCoverageValuesSelectionItem dimensionValues = (DimensionCoverageValuesSelectionItem) getItem(DsdAttributeInstanceDS.DIMENSION_SELECTION_VALUES);
        for (String dimensionId : dimensionCodes.keySet()) {
            dimensionValues.setDimensionCoverageValues(dimensionId, dimensionCodes.get(dimensionId));
            dimensionValues.selectDimensionCodes(dimensionId, dimensionCodes.get(dimensionId));
        }
    }

    @Override
    protected DimensionCoverageValuesSelectionItem createDimensionValuesSelectionItem(String name, String title, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        List<String> dimensionIds = new ArrayList<String>(dsdAttributeInstanceDto.getCodeDimensions().keySet());

        DimensionCoverageValuesSelectionItem selectionItem = new DimensionCoverageValuesSelectionItem(name, title, dimensionIds, false);
        selectionItem.setColSpan(4);

        return selectionItem;
    }

    @Override
    protected ExternalItemLinkItem createEnumeratedValueItem(String name, String title) {
        ExternalItemLinkItem externalItemLinkItem = new ExternalItemLinkItem(name, title);
        return externalItemLinkItem;
    }
}
