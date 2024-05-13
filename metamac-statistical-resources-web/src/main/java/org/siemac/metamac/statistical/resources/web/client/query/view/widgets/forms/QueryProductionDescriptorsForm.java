package org.siemac.metamac.statistical.resources.web.client.query.view.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.query.model.ds.QueryDS;
import org.siemac.metamac.statistical.resources.web.client.query.utils.QueryRelatedDatasetUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.NavigationEnabledDynamicForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.CodeItemListItem;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.RelatedResourceLinkItem;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.ExternalItemListItem;

import com.smartgwt.client.widgets.form.fields.FormItem;

public class QueryProductionDescriptorsForm extends NavigationEnabledDynamicForm {

    private BaseUiHandlers                    uiHandlers;

    public QueryProductionDescriptorsForm() {
        super(getConstants().formProductionDescriptors());

        List<FormItem> fields = createElements();

        setFields(fields.toArray(new FormItem[fields.size()]));
    }

    private List<FormItem> createElements() {
        List<FormItem> fields = new ArrayList<FormItem>();

        ExternalItemLinkItem maintainer = new ExternalItemLinkItem(QueryDS.MAINTAINER, getConstants().siemacMetadataStatisticalResourceMaintainer());
        fields.add(maintainer);

        RelatedResourceLinkItem datasetVersion = new RelatedResourceLinkItem(QueryDS.RELATED_DATASET_VERSION, getConstants().queryDataset(), getCustomLinkItemNavigationClickHandler());
        fields.add(datasetVersion);

        ViewTextItem status = new ViewTextItem(QueryDS.STATUS, getConstants().queryStatus());
        fields.add(status);

        ViewTextItem type = new ViewTextItem(QueryDS.TYPE, getConstants().queryType());
        fields.add(type);

        return fields;
    }

    public void setQueryDto(QueryVersionDto queryDto) {
        List<FormItem> fields = createElements();

        boolean isLatestData = QueryTypeEnum.LATEST_DATA.equals(queryDto.getType());

        if (queryDto.getSelection() != null) {

            List<String> dimensionIds = new ArrayList<String>(queryDto.getSelection().keySet());

            ensureTimeDimensionIsLast(dimensionIds);

            for (String dimensionId : dimensionIds) {
                if (!(isLatestData && isTemporalDimension(dimensionId))) {
                    if (isTemporalDimension(dimensionId)) {
                        ExternalItemListItem temporalGranularities = new ExternalItemListItem(DatasetDS.TEMPORAL_GRANULARITY, getConstants().datasetTemporalGranularities(), false);
                        fields.add(temporalGranularities);
                    }
                    fields.add(createCodeListItemForDimension(dimensionId));
                }
            }
            if (isLatestData) {
                ViewTextItem latestData = new ViewTextItem(QueryDS.LATEST_N_DATA, getConstants().queryLatestNData());
                fields.add(latestData);
            }
        }

        setFields(fields.toArray(new FormItem[fields.size()]));

        if (queryDto.getSelection() != null) {
            for (String dimensionId : queryDto.getSelection().keySet()) {
                if (!(isLatestData && isTemporalDimension(dimensionId))) {
                    CodeItemListItem item = (CodeItemListItem) getItem(QueryDS.SELECTION + "_" + dimensionId);
                    if (isTemporalDimension(dimensionId) && queryDto.getTemporalGranularities() != null && !queryDto.getTemporalGranularities().isEmpty()) {
                        ((ExternalItemListItem) getItem(DatasetDS.TEMPORAL_GRANULARITY)).setExternalItems(queryDto.getTemporalGranularities());
                    }
                    item.setCodeItems(queryDto.getSelection().get(dimensionId));
                }
            }
        }

        QueryRelatedDatasetUtils.setRelatedDataset(queryDto, (RelatedResourceLinkItem) getItem(QueryDS.RELATED_DATASET_VERSION));

        setValue(QueryDS.MAINTAINER, queryDto.getMaintainer());
        // Status
        setValue(QueryDS.STATUS, CommonUtils.getQueryStatusName(queryDto));
        setValue(QueryDS.TYPE, CommonUtils.getQueryTypeName(queryDto));
        if (isLatestData) {
            setValue(QueryDS.LATEST_N_DATA, queryDto.getLatestDataNumber());
        }
    }

    private void ensureTimeDimensionIsLast(List<String> dimensionIds) {
        if (dimensionIds.contains(StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID)) {
            dimensionIds.remove(StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID);
            dimensionIds.add(StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID);
        }
    }

    private CodeItemListItem createCodeListItemForDimension(final String dimensionId) {
        CodeItemListItem item = new CodeItemListItem(QueryDS.SELECTION + "_" + dimensionId, dimensionId, false);
        return item;
    }

    public void setUiHandlers(BaseUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    @Override
    public BaseUiHandlers getBaseUiHandlers() {
        return uiHandlers;
    }

    private boolean isTemporalDimension(String dimensionId) {
        return StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID.equals(dimensionId);
    }
}
