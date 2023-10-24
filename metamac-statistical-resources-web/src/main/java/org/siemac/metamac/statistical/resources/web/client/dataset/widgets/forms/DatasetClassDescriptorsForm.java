package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataClassDescriptorsForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

public class DatasetClassDescriptorsForm extends SiemacMetadataClassDescriptorsForm {

    public DatasetClassDescriptorsForm() {

        ViewTextItem formatExtentObservations = new ViewTextItem(DatasetDS.FORMAT_EXTENT_OBSERVATIONS, getConstants().datasetFormatExtentObservations());
        ViewTextItem formatExtentDimensions = new ViewTextItem(DatasetDS.FORMAT_EXTENT_DIMENSIONS, getConstants().datasetFormatExtentDimensions());
        ViewTextItem formatExtentTableSize = new ViewTextItem(DatasetDS.FORMAT_EXTENT_TABLE_SIZE, getConstants().datasetFormatExtentTabSize());

        addFields(formatExtentObservations, formatExtentDimensions, formatExtentTableSize);
    }

    public void setDatasetVersionDto(DatasetVersionDto datasetDto) {
        setSiemacMetadataStatisticalResourceDto(datasetDto);
        setValue(DatasetDS.FORMAT_EXTENT_OBSERVATIONS, datasetDto.getFormatExtentObservations() != null ? datasetDto.getFormatExtentObservations().toString() : StringUtils.EMPTY);
        setValue(DatasetDS.FORMAT_EXTENT_DIMENSIONS, datasetDto.getFormatExtentDimensions() != null ? datasetDto.getFormatExtentDimensions().toString() : StringUtils.EMPTY);
        setValue(DatasetDS.FORMAT_EXTENT_TABLE_SIZE, datasetDto.getFormatExtentTableSize() != null ? datasetDto.getFormatExtentTableSize().toString() : StringUtils.EMPTY);
    }
}
