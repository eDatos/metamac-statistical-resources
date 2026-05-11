package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dto.datasets.AttributeValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetAttributesTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.DimensionCoverageValuesSelectionItem;
import org.siemac.metamac.statistical.resources.web.client.model.ds.DsdGranularityAttributeInstanceDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.DatasetAttibuteUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemSimpleItem;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.smartgwt.client.widgets.form.fields.FormItem;

public class AttributeGranularityLevelEditionForm extends GroupDynamicForm {

    private DatasetAttributesTabUiHandlers        uiHandlers;
    private DsdAttributeDto                       dsdAttributeDto;
    private DsdGranularityAttributeInstanceDto    dto;
    private String                                temporalDimensionId;

    public AttributeGranularityLevelEditionForm() {
        super("GranularityAttribute");
    }

    public void setAttribute(DsdAttributeDto dsdAttributeDto, DsdGranularityAttributeInstanceDto dto) {
        setFields(new FormItem[0]);
        clearValues();
        clearErrors(true);

        this.dsdAttributeDto = dsdAttributeDto;
        this.dto = dto;
        this.temporalDimensionId = StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID;

        setGroupTitle(dsdAttributeDto.getIdentifier());
        buildForm(dsdAttributeDto, dto);
    }

    private void buildForm(DsdAttributeDto dsdAttributeDto, DsdGranularityAttributeInstanceDto dto) {
        List<FormItem> fields = new ArrayList<FormItem>();

        List<String> dimensionIds = new ArrayList<String>();
        dimensionIds.add(temporalDimensionId);
        List<String> nonTemporalDimIds = CommonUtils.getNonTemporalDimensionIds(dsdAttributeDto);
        dimensionIds.addAll(nonTemporalDimIds);

        DimensionCoverageValuesSelectionItem dimensionCoverageItem = new DimensionCoverageValuesSelectionItem(
                DsdGranularityAttributeInstanceDS.GRANULARITY_CODES, getConstants().datasetAttributeDimensionValuesSelection(), dimensionIds, true);
        dimensionCoverageItem.setColSpan(4);
        dimensionCoverageItem.setRequired(true);
        fields.add(dimensionCoverageItem);

        if (!nonTemporalDimIds.isEmpty()) {
            getUiHandlers().retrieveDimensionsCoverage(nonTemporalDimIds, new MetamacWebCriteria());
        }

        // Value field
        if (CommonUtils.hasEnumeratedRepresentation(dsdAttributeDto)) {
            SearchExternalItemSimpleItem valueItem = createEnumeratedValueItem(DsdGranularityAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
            valueItem.setRequired(true);
            fields.add(valueItem);
            if (dto.getValue() != null && dto.getValue().getExternalItemValue() != null) {
                setValue(DsdGranularityAttributeInstanceDS.VALUE, RecordUtils.getExternalItemRecord(dto.getValue().getExternalItemValue()));
            }
        } else if (Boolean.TRUE.equals(dsdAttributeDto.getAttributeRepresentation().getMultilingualType())) {
            MultiLanguageTextItem valueItem = new MultiLanguageTextItem(DsdGranularityAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
            valueItem.setRequired(true);
            valueItem.setValidators(DatasetAttibuteUtils.getDimensionOrGroupLevelAttibuteValueLengthValidator());
            if (dto.getValue() != null && dto.getValue().getInternationalStringValue() != null) {
                setValue(DsdGranularityAttributeInstanceDS.VALUE, RecordUtils.getInternationalStringRecord(dto.getValue().getInternationalStringValue()));
            }
            fields.add(valueItem);
        } else {
            CustomTextItem valueItem = new CustomTextItem(DsdGranularityAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
            valueItem.setRequired(true);
            valueItem.setValidators(DatasetAttibuteUtils.getDimensionOrGroupLevelAttibuteValueLengthValidator());
            if (dto.getValue() != null && dto.getValue().getStringValue() != null) {
                setValue(DsdGranularityAttributeInstanceDS.VALUE, dto.getValue().getStringValue());
            }
            fields.add(valueItem);
        }

        setFields(fields.toArray(new FormItem[fields.size()]));
    }

    private SearchExternalItemSimpleItem createEnumeratedValueItem(String name, String title) {
        return new SearchExternalItemSimpleItem(name, title, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                getUiHandlers().retrieveItemsFromItemSchemeForDimensionOrGroupLevelAttribute(dsdAttributeDto.getAttributeRepresentation(), firstResult, maxResults, webCriteria);
            }
        };
    }

    public void setTemporalGranularities(List<ExternalItemDto> granularities) {
        FormItem item = getItem(DsdGranularityAttributeInstanceDS.GRANULARITY_CODES);
        if (!(item instanceof DimensionCoverageValuesSelectionItem) || granularities == null) {
            return;
        }
        DimensionCoverageValuesSelectionItem selectionItem = (DimensionCoverageValuesSelectionItem) item;
        List<CodeItemDto> temporalGranularities = new ArrayList<CodeItemDto>(granularities.size());
        for (ExternalItemDto granularity : granularities) {
            temporalGranularities.add(new CodeItemDto(granularity.getCode(), granularity.getTitle() != null ? InternationalStringUtils.getLocalisedString(granularity.getTitle())
                    : granularity.getCode()));
        }
        selectionItem.setDimensionCoverageValues(temporalDimensionId, temporalGranularities);

        if (dto != null && dto.getGranularityCodesByDimension() != null) {
            List<String> selectedCodes = dto.getGranularityCodesByDimension().get(temporalDimensionId);
            if (selectedCodes != null && !selectedCodes.isEmpty()) {
                List<CodeItemDto> selectedGranularities = new ArrayList<CodeItemDto>();
                for (CodeItemDto temporalGranularity : temporalGranularities) {
                    if (selectedCodes.contains(temporalGranularity.getCode())) {
                        selectedGranularities.add(temporalGranularity);
                    }
                }
                selectionItem.selectDimensionCodes(temporalDimensionId, selectedGranularities);
            }
        }
    }

    public void setDimensionsCoverageValues(Map<String, List<CodeItemDto>> dimensionsCoverages) {
        FormItem item = getItem(DsdGranularityAttributeInstanceDS.GRANULARITY_CODES);
        if (item instanceof DimensionCoverageValuesSelectionItem) {
            DimensionCoverageValuesSelectionItem selectionItem = (DimensionCoverageValuesSelectionItem) item;
            Map<String, List<CodeItemDto>> codeDimensions = dto.getCodesByDimension() != null ? dto.getCodesByDimension() : new HashMap<String, List<CodeItemDto>>();
            for (String dimensionId : dimensionsCoverages.keySet()) {
                List<CodeItemDto> selectedCodes = codeDimensions.get(dimensionId) != null ? codeDimensions.get(dimensionId) : new ArrayList<CodeItemDto>();
                selectionItem.setDimensionCoverageValues(dimensionId, dimensionsCoverages.get(dimensionId));
                selectionItem.selectDimensionCodes(dimensionId, selectedCodes);
            }
        }
    }

    public DsdGranularityAttributeInstanceDto getDsdGranularityAttributeInstanceDto() {
        Map<String, List<CodeItemDto>> allSelectedCodeDimensions = new HashMap<String, List<CodeItemDto>>();
        FormItem dimItem = getItem(DsdGranularityAttributeInstanceDS.GRANULARITY_CODES);
        if (dimItem instanceof DimensionCoverageValuesSelectionItem) {
            allSelectedCodeDimensions = ((DimensionCoverageValuesSelectionItem) dimItem).getSelectedCodeDimensions();
        }

        List<String> selectedCodes = new ArrayList<String>();
        List<CodeItemDto> temporalCodes = allSelectedCodeDimensions.get(temporalDimensionId);
        if (temporalCodes != null) {
            for (CodeItemDto temporalCode : temporalCodes) {
                selectedCodes.add(temporalCode.getCode());
            }
        }

        Map<String, List<String>> granularityCodesByDimension = new HashMap<String, List<String>>();
        granularityCodesByDimension.put(temporalDimensionId, selectedCodes);
        dto.setGranularityCodesByDimension(granularityCodesByDimension);

        Map<String, List<CodeItemDto>> codesByDimension = new HashMap<String, List<CodeItemDto>>(allSelectedCodeDimensions);
        codesByDimension.remove(temporalDimensionId);
        dto.setCodesByDimension(codesByDimension);

        // Value
        AttributeValueDto valueDto = new AttributeValueDto();
        FormItem valueItem = getItem(DsdGranularityAttributeInstanceDS.VALUE);
        if (valueItem instanceof SearchExternalItemSimpleItem) {
            valueDto.setExternalItemValue(((SearchExternalItemSimpleItem) valueItem).getExternalItemDto());
        } else if (valueItem instanceof MultiLanguageTextItem) {
            valueDto.setInternationalStringValue(getValueAsInternationalStringDto(DsdGranularityAttributeInstanceDS.VALUE));
        } else {
            valueDto.setStringValue(getValueAsString(DsdGranularityAttributeInstanceDS.VALUE));
        }
        dto.setValue(valueDto);

        return dto;
    }

    public DsdAttributeDto getDsdAttributeDto() {
        return dsdAttributeDto;
    }

    public DatasetAttributesTabUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setUiHandlers(DatasetAttributesTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public void setItemsForDimensionOrGroupLevelAttributeValueSelection(List<ExternalItemDto> externalItemDtos, int firstResult, int totalResults) {
        FormItem valueItem = getItem(DsdGranularityAttributeInstanceDS.VALUE);
        if (valueItem instanceof SearchExternalItemSimpleItem) {
            ((SearchExternalItemSimpleItem) valueItem).setResources(externalItemDtos, firstResult, totalResults);
        }
    }
}
