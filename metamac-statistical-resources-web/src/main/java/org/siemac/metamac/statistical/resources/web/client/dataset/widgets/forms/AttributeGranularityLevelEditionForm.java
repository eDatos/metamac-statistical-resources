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
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetAttributesTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.DimensionCoverageValuesSelectionItem;
import org.siemac.metamac.statistical.resources.web.client.model.ds.DsdGranularityAttributeInstanceDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.DatasetAttibuteUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemSimpleItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchMultiExternalItemSimpleItem;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.Layout;

public class AttributeGranularityLevelEditionForm extends GroupDynamicForm {

    private DatasetAttributesTabUiHandlers          uiHandlers;
    private DsdAttributeDto                         dsdAttributeDto;
    private DsdGranularityAttributeInstanceDto      dto;
    private String                                  temporalDimensionId;

    private SearchMultiExternalItemSimpleItem        temporalGranularitiesItem;
    private DimensionCoverageValuesSelectionItem     dimensionCoverageItem;
    private List<ExternalItemDto>                    availableTemporalGranularities;

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

        // Temporal granularity selector (SearchMultiExternalItemSimpleItem with CODE+TITLE+URN)
        temporalGranularitiesItem = new SearchMultiExternalItemSimpleItem(DatasetDS.TEMPORAL_GRANULARITY, getConstants().datasetTemporalGranularities(),
                StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS) {
            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                if (availableTemporalGranularities != null) {
                    setResources(availableTemporalGranularities, 0, availableTemporalGranularities.size());
                }
            }

            @Override
            public void setExternalItems(List<ExternalItemDto> items) {
                super.setExternalItems(items);
                syncTemporalGranularitiesToSummary(items);
            }
        };
        applyTemporalGranularitiesAlignment(temporalGranularitiesItem);
        temporalGranularitiesItem.setColSpan(4);
        temporalGranularitiesItem.setRequired(true);
        temporalGranularitiesItem.getListGrid().setAnimateRemoveRecord(false);
        hookRemoveRecordClick(temporalGranularitiesItem.getListGrid().getOrCreateJsObj());
        fields.add(temporalGranularitiesItem);

        // Non-temporal dimensions + summary grid
        List<String> nonTemporalDimIds = CommonUtils.getNonTemporalDimensionIds(dsdAttributeDto);
        dimensionCoverageItem = new DimensionCoverageValuesSelectionItem(
                DsdGranularityAttributeInstanceDS.GRANULARITY_CODES, getConstants().datasetAttributeDimensionValuesSelection(), nonTemporalDimIds, true);
        dimensionCoverageItem.setColSpan(4);
        if (!nonTemporalDimIds.isEmpty()) {
            dimensionCoverageItem.setRequired(true);
        }
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

    private void applyTemporalGranularitiesAlignment(SearchMultiExternalItemSimpleItem item) {
        Canvas canvas = item.getCanvas();
        if (canvas instanceof Layout) {
            ((Layout) canvas).setLayoutMargin(10);
            ((Layout) canvas).setMembersMargin(0);
        } else if (canvas != null) {
            canvas.setPadding(10);
        }
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
        if (temporalGranularitiesItem == null || granularities == null) {
            return;
        }
        availableTemporalGranularities = granularities;

        List<ExternalItemDto> selected = new ArrayList<ExternalItemDto>();
        if (dto != null && dto.getGranularityCodesByDimension() != null) {
            List<String> selectedCodes = dto.getGranularityCodesByDimension().get(temporalDimensionId);
            if (selectedCodes != null) {
                for (ExternalItemDto granularity : granularities) {
                    if (selectedCodes.contains(granularity.getCode())) {
                        selected.add(granularity);
                    }
                }
            }
        }
        temporalGranularitiesItem.setExternalItems(selected);
        temporalGranularitiesItem.storeValue(selected.isEmpty() ? null : new Record());
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

    @Override
    public Boolean validate(boolean validateHiddenFields) {
        if (temporalGranularitiesItem != null) {
            List<ExternalItemDto> items = temporalGranularitiesItem.getExternalItemDtos();
            temporalGranularitiesItem.storeValue(items != null && !items.isEmpty() ? new Record() : null);
        }
        if (dimensionCoverageItem != null) {
            dimensionCoverageItem.storeCurrentValue();
        }
        return super.validate(validateHiddenFields);
    }

    public DsdGranularityAttributeInstanceDto getDsdGranularityAttributeInstanceDto() {
        // Temporal granularity codes from the search item
        List<String> selectedTemporalCodes = new ArrayList<String>();
        if (temporalGranularitiesItem != null) {
            List<ExternalItemDto> selectedItems = temporalGranularitiesItem.getExternalItemDtos();
            if (selectedItems != null) {
                for (ExternalItemDto item : selectedItems) {
                    selectedTemporalCodes.add(item.getCode());
                }
            }
        }
        Map<String, List<String>> granularityCodesByDimension = new HashMap<String, List<String>>();
        granularityCodesByDimension.put(temporalDimensionId, selectedTemporalCodes);
        dto.setGranularityCodesByDimension(granularityCodesByDimension);

        // Non-temporal dimension codes from the coverage item
        Map<String, List<CodeItemDto>> codesByDimension = dimensionCoverageItem != null
                ? dimensionCoverageItem.getSelectedCodeDimensions()
                : new HashMap<String, List<CodeItemDto>>();
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

    private native void hookRemoveRecordClick(JavaScriptObject listGridJsObj) /*-{
        var form = this;
        var original = listGridJsObj.removeRecordClick;
        listGridJsObj.removeRecordClick = $entry(function(rowNum, record) {
            if (original) {
                original.apply(this, arguments);
            } else {
                this.removeData(record);
            }
            form.@org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.AttributeGranularityLevelEditionForm::onGranularityRemoved()();
        });
    }-*/;

    private void onGranularityRemoved() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                if (temporalGranularitiesItem == null) {
                    return;
                }
                List<ExternalItemDto> current = temporalGranularitiesItem.getExternalItemDtos();
                temporalGranularitiesItem.storeValue(current != null && !current.isEmpty() ? new Record() : null);
                syncTemporalGranularitiesToSummary(current);
            }
        });
    }

    private void syncTemporalGranularitiesToSummary(List<ExternalItemDto> selected) {
        if (dimensionCoverageItem == null) {
            return;
        }
        List<CodeItemDto> codeItems = new ArrayList<CodeItemDto>();
        if (selected != null) {
            for (ExternalItemDto item : selected) {
                String title = item.getTitle() != null ? InternationalStringUtils.getLocalisedString(item.getTitle()) : item.getCode();
                codeItems.add(new CodeItemDto(item.getCode(), title));
            }
        }
        dimensionCoverageItem.syncTemporalToSummary(temporalDimensionId, codeItems);
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
