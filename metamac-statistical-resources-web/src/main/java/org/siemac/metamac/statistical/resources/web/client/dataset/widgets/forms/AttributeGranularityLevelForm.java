package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.DimensionCoverageValuesSelectionItem;
import org.siemac.metamac.statistical.resources.web.client.model.ds.DsdAttributeInstanceDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.DsdGranularityAttributeInstanceDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.model.record.ExternalItemRecord;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.form.fields.FormItem;

public class AttributeGranularityLevelForm extends GroupDynamicForm {

    private DsdAttributeDto                    dsdAttributeDto;
    private DsdGranularityAttributeInstanceDto dto;
    private String                             temporalDimensionId;

    public AttributeGranularityLevelForm() {
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
                DsdGranularityAttributeInstanceDS.GRANULARITY_CODES, getConstants().datasetAttributeDimensionValuesSelection(), dimensionIds, false);
        dimensionCoverageItem.setColSpan(4);
        fields.add(dimensionCoverageItem);

        if (CommonUtils.hasEnumeratedRepresentation(dsdAttributeDto)) {
            ExternalItemLinkItem valueItem = new ExternalItemLinkItem(DsdAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
            fields.add(valueItem);
            if (dto.getValue() != null && dto.getValue().getExternalItemValue() != null) {
                ExternalItemRecord record = RecordUtils.getExternalItemRecord(dto.getValue().getExternalItemValue());
                setValue(DsdAttributeInstanceDS.VALUE, record);
            }
        } else if (Boolean.TRUE.equals(dsdAttributeDto.getAttributeRepresentation().getMultilingualType())) {
            ViewMultiLanguageTextItem valueItem = new ViewMultiLanguageTextItem(DsdAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
            fields.add(valueItem);
            if (dto.getValue() != null && dto.getValue().getInternationalStringValue() != null) {
                setValue(DsdAttributeInstanceDS.VALUE, RecordUtils.getInternationalStringRecord(dto.getValue().getInternationalStringValue()));
            }
        } else {
            ViewTextItem valueItem = new ViewTextItem(DsdAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
            fields.add(valueItem);
            if (dto.getValue() != null && dto.getValue().getStringValue() != null) {
                setValue(DsdAttributeInstanceDS.VALUE, dto.getValue().getStringValue());
            }
        }

        setFields(fields.toArray(new FormItem[fields.size()]));
    }

    public void setTemporalGranularities(List<ExternalItemDto> granularities) {
        FormItem item = getItem(DsdGranularityAttributeInstanceDS.GRANULARITY_CODES);
        if (!(item instanceof DimensionCoverageValuesSelectionItem) || granularities == null || dto == null || dto.getGranularityCodesByDimension() == null) {
            return;
        }
        DimensionCoverageValuesSelectionItem selectionItem = (DimensionCoverageValuesSelectionItem) item;
        List<String> selectedCodes = dto.getGranularityCodesByDimension().get(temporalDimensionId);
        if (selectedCodes == null) {
            selectedCodes = new ArrayList<String>();
        }

        List<CodeItemDto> temporalGranularities = new ArrayList<CodeItemDto>();
        List<CodeItemDto> selectedGranularities = new ArrayList<CodeItemDto>();
        for (ExternalItemDto granularity : granularities) {
            CodeItemDto temporalGranularity = new CodeItemDto(granularity.getCode(),
                    granularity.getTitle() != null ? InternationalStringUtils.getLocalisedString(granularity.getTitle()) : granularity.getCode());
            temporalGranularities.add(temporalGranularity);
            if (selectedCodes.contains(granularity.getCode())) {
                selectedGranularities.add(temporalGranularity);
            }
        }
        selectionItem.setDimensionCoverageValues(temporalDimensionId, temporalGranularities);
        selectionItem.selectDimensionCodes(temporalDimensionId, selectedGranularities);
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
}
