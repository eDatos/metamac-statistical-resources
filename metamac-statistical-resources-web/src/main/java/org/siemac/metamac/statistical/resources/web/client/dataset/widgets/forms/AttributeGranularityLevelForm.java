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
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
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
import org.siemac.metamac.web.common.client.widgets.form.fields.external.ExternalItemListItem;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.Layout;

public class AttributeGranularityLevelForm extends GroupDynamicForm {

    private DsdAttributeDto                    dsdAttributeDto;
    private DsdGranularityAttributeInstanceDto dto;
    private String                             temporalDimensionId;

    private ExternalItemListItem               temporalGranularitiesItem;
    private DimensionCoverageValuesSelectionItem dimensionCoverageItem;

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

        // Temporal granularity list (read-only, CODE+TITLE+URN via ExternalItemListItem)
        temporalGranularitiesItem = new ExternalItemListItem(DatasetDS.TEMPORAL_GRANULARITY, getConstants().datasetTemporalGranularities(), false);
        applyTemporalGranularitiesAlignment(temporalGranularitiesItem);
        temporalGranularitiesItem.setColSpan(4);
        fields.add(temporalGranularitiesItem);

        // Non-temporal dimensions + summary grid
        List<String> nonTemporalDimIds = CommonUtils.getNonTemporalDimensionIds(dsdAttributeDto);
        dimensionCoverageItem = new DimensionCoverageValuesSelectionItem(
                DsdGranularityAttributeInstanceDS.GRANULARITY_CODES, getConstants().datasetAttributeDimensionValuesSelection(), nonTemporalDimIds, false);
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

    private void applyTemporalGranularitiesAlignment(ExternalItemListItem item) {
        Canvas canvas = item.getCanvas();
        if (canvas instanceof Layout) {
            ((Layout) canvas).setLayoutMargin(10);
            ((Layout) canvas).setMembersMargin(0);
        } else if (canvas != null) {
            canvas.setPadding(10);
        }
    }

    public void setTemporalGranularities(List<ExternalItemDto> granularities) {
        if (temporalGranularitiesItem == null || granularities == null || dto == null || dto.getGranularityCodesByDimension() == null) {
            return;
        }
        List<String> selectedCodes = dto.getGranularityCodesByDimension().get(temporalDimensionId);
        if (selectedCodes == null) {
            selectedCodes = new ArrayList<String>();
        }

        List<ExternalItemDto> selected = new ArrayList<ExternalItemDto>();
        List<CodeItemDto> selectedCodeItems = new ArrayList<CodeItemDto>();
        for (ExternalItemDto granularity : granularities) {
            if (selectedCodes.contains(granularity.getCode())) {
                selected.add(granularity);
                String title = granularity.getTitle() != null ? InternationalStringUtils.getLocalisedString(granularity.getTitle()) : granularity.getCode();
                selectedCodeItems.add(new CodeItemDto(granularity.getCode(), title));
            }
        }
        temporalGranularitiesItem.setExternalItems(selected);
        if (dimensionCoverageItem != null) {
            dimensionCoverageItem.syncTemporalToSummary(temporalDimensionId, selectedCodeItems);
        }
    }

    public void setDimensionsCoverageValues(Map<String, List<CodeItemDto>> dimensionsCoverages) {
        if (dimensionCoverageItem == null) {
            return;
        }
        Map<String, List<CodeItemDto>> codeDimensions = dto.getCodesByDimension() != null ? dto.getCodesByDimension() : new HashMap<String, List<CodeItemDto>>();
        for (String dimensionId : dimensionsCoverages.keySet()) {
            List<CodeItemDto> selectedCodes = codeDimensions.get(dimensionId) != null ? codeDimensions.get(dimensionId) : new ArrayList<CodeItemDto>();
            dimensionCoverageItem.setDimensionCoverageValues(dimensionId, dimensionsCoverages.get(dimensionId));
            dimensionCoverageItem.selectDimensionCodes(dimensionId, selectedCodes);
        }
    }
}
