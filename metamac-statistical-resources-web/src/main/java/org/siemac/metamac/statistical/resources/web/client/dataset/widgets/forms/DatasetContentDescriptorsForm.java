package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.TemporalCodeDto;
import org.siemac.metamac.statistical.resources.web.client.base.checks.DatasetMetadataShowChecks;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils.DatasetWidgetsUtil;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataContentDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.TemporalCodeListItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.ExternalItemListItem;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

public class DatasetContentDescriptorsForm extends SiemacMetadataContentDescriptorsForm {

    public DatasetContentDescriptorsForm() {
        super();
        init(false);
    }

    public DatasetContentDescriptorsForm(boolean isMultipleUpdate) {
        super();
        init(isMultipleUpdate);
    }

    private void init(boolean isMultipleUpdate) {

        ExternalItemListItem geographicCoverage = new ExternalItemListItem(DatasetDS.GEOGRAPHIC_COVERAGE, getConstants().datasetGeographicCoverage(), false);
        geographicCoverage.setShowIfCondition(getCanShowCoveragesFunction());

        TemporalCodeListItem temporalCoverage = new TemporalCodeListItem(DatasetDS.TEMPORAL_COVERAGE, getConstants().datasetTemporalCoverage(), false);
        temporalCoverage.setShowIfCondition(getCanShowCoveragesFunction());

        ExternalItemListItem measures = new ExternalItemListItem(DatasetDS.MEASURES, getConstants().datasetMeasures(), false);
        measures.setShowIfCondition(getCanShowCoveragesFunction());

        ExternalItemListItem geographicGranularities = new ExternalItemListItem(DatasetDS.GEOGRAPHIC_GRANULARITY, getConstants().datasetGeographicGranularities(), false);
        ExternalItemListItem temporalGranularities = new ExternalItemListItem(DatasetDS.TEMPORAL_GRANULARITY, getConstants().datasetTemporalGranularities(), false);

        ExternalItemListItem statisticalUnit = new ExternalItemListItem(DatasetDS.STATISTICAL_UNIT, getConstants().datasetStatisticalUnit(), false);

        ViewTextItem dateStart = new ViewTextItem(DatasetDS.DATE_START, getConstants().datasetDateStart());
        ViewTextItem dateEnd = new ViewTextItem(DatasetDS.DATE_END, getConstants().datasetDateEnd());

        dateStart.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));
        dateEnd.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        addFields(dateStart, dateEnd, statisticalUnit, geographicCoverage, temporalCoverage, measures, geographicGranularities, temporalGranularities);
    }

    public void setDatasetVersionDto(DatasetVersionDto datasetDto) {
        setSiemacMetadataStatisticalResourceDto(datasetDto);

        ((ExternalItemListItem) getItem(DatasetDS.GEOGRAPHIC_GRANULARITY)).setExternalItems(datasetDto.getGeographicGranularities());
        ((ExternalItemListItem) getItem(DatasetDS.TEMPORAL_GRANULARITY)).setExternalItems(datasetDto.getTemporalGranularities());

        setValue(DatasetDS.DATE_START, datasetDto.getDateStart());
        setValue(DatasetDS.DATE_END, datasetDto.getDateEnd());

        ((ExternalItemListItem) getItem(DatasetDS.STATISTICAL_UNIT)).setExternalItems(datasetDto.getStatisticalUnit());
    }

    public void setCoverages(List<ExternalItemDto> geoItems, List<TemporalCodeDto> temporalItems, List<ExternalItemDto> measureItems) {
        ((ExternalItemListItem) getItem(DatasetDS.GEOGRAPHIC_COVERAGE)).setExternalItems(geoItems);
        ((TemporalCodeListItem) getItem(DatasetDS.TEMPORAL_COVERAGE)).setTemporalCodes(temporalItems);
        ((ExternalItemListItem) getItem(DatasetDS.MEASURES)).setExternalItems(measureItems);
    }

    private FormItemIfFunction getCanShowCoveragesFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return DatasetMetadataShowChecks.canCoveragesBeShown(procStatus);
            }
        };
    }
}
