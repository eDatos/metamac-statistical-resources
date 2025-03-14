package org.siemac.metamac.statistical.resources.web.client.dataset.model.record;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.XStreamStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.LifeCycleResourceDS;
import org.siemac.metamac.statistical.resources.web.client.model.record.SiemacMetadataRecord;

import com.smartgwt.client.widgets.form.fields.FormItemIcon;

public class DatasetRecord extends SiemacMetadataRecord {

    public DatasetRecord() {
    }

    public void setRelatedDSD(ExternalItemDto value) {
        setExternalItem(DatasetDS.RELATED_DSD, value);
    }

    public void setStatisticOfficiality(String value) {
        setAttribute(DatasetDS.STATISTIC_OFFICIALITY, value);
    }

    public void setDatasetVersionBaseDto(DatasetVersionBaseDto datasetVersionBaseDto) {
        setAttribute(DatasetDS.DTO, datasetVersionBaseDto);
    }

    public DatasetVersionBaseDto getDatasetVersionBaseDto() {
        return (DatasetVersionBaseDto) getAttributeAsObject(DatasetDS.DTO);
    }

    @Override
    public ProcStatusEnum getProcStatusEnum() {
        return getDatasetVersionBaseDto().getProcStatus();
    }

    public XStreamStatusEnum getProcXEnum() {
        return getDatasetVersionBaseDto().getXSTreamStatus();
    }

    public void setXPublicationStatus(FormItemIcon formItemIcon) {
        FormItemIcon xPublicationStatus = (formItemIcon != null && formItemIcon.getSrc() == null) ? null : formItemIcon;

        if (xPublicationStatus != null) {
            xPublicationStatus.setShowOver(false);
            setAttribute(LifeCycleResourceDS.PUBLICATION_X_STATUS, xPublicationStatus);
        }
    }
}
