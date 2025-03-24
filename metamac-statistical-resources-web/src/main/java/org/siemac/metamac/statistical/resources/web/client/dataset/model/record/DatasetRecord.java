package org.siemac.metamac.statistical.resources.web.client.dataset.model.record;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.model.record.SiemacMetadataRecord;

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
}
