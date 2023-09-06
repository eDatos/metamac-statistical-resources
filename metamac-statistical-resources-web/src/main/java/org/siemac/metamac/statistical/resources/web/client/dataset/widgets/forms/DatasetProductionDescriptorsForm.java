package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataProductionDescriptorsForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;

public class DatasetProductionDescriptorsForm extends SiemacMetadataProductionDescriptorsForm {

    public DatasetProductionDescriptorsForm() {
        ExternalItemLinkItem relatedDsd = new ExternalItemLinkItem(DatasetDS.RELATED_DSD, getConstants().datasetRelatedDSD());
        addFields(relatedDsd);
    }

    public void setDatasetVersionDto(DatasetVersionDto datasetDto) {
        setSiemacMetadataStatisticalResourceDto(datasetDto);
        setValue(DatasetDS.RELATED_DSD, CommonUtils.setCodeVersionExternalItem(datasetDto.getRelatedDsd()));
    }
}
