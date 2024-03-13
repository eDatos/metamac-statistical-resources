package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.SiemacMetadataResourceSectionStack;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.record.DatasetRecord;
import org.siemac.metamac.statistical.resources.web.client.model.ds.LifeCycleResourceDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.StatisticalResourcesRecordUtils;

import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DatasetVersionsSectionStack extends SiemacMetadataResourceSectionStack {

    public DatasetVersionsSectionStack(String title) {
        super(title);
        initDatasetVersionsSectionStack();
    }

    public DatasetVersionsSectionStack(String title, String statusTitle, boolean canRemoveElements) {
        super(title, statusTitle, canRemoveElements);
        initDatasetVersionsSectionStack();
    }

    private void initDatasetVersionsSectionStack() {
        setListGridFields();
    }

    private boolean existDatasetVersion(String urn) {
        if (listGrid.getRecords() != null) {
            for (ListGridRecord rawRecord : listGrid.getRecords()) {
                DatasetRecord datasetRecord = (DatasetRecord) rawRecord;
                if (urn.equals(datasetRecord.getUrn())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setDatasetVersions(List<DatasetVersionBaseDto> datasetVersionBaseDtos, String sort) {
        listGrid.selectAllRecords();
        listGrid.removeSelectedData();
        for (DatasetVersionBaseDto datasetDto : datasetVersionBaseDtos) {
            listGrid.addData(StatisticalResourcesRecordUtils.getDatasetRecord(datasetDto));
        }
        listGrid.sort(sort, SortDirection.DESCENDING);
    }

    public void addDatasetVersions(List<DatasetVersionBaseDto> datasetVersionBaseDtos) {
        for (DatasetVersionBaseDto datasetDto : datasetVersionBaseDtos) {
            if (!existDatasetVersion(datasetDto.getUrn())) {
                listGrid.addData(StatisticalResourcesRecordUtils.getDatasetRecord(datasetDto));
            }
        }
        listGrid.sort(DatasetDS.CODE, SortDirection.DESCENDING);
    }

    public void selectDatasetVersion(String currentDatasetVersionUrn) {
        selectRecord(DatasetDS.URN, currentDatasetVersionUrn);
    }

    public List<String> getAllDatasetUrns() {
        return StatisticalResourcesRecordUtils.getDatasetVersionUrnsFromListGridRecords(listGrid.getRecords());
    }

    public void refreshStatusDatasetVersion(String urn, StreamMessageStatusEnum status) {
        if (listGrid.getRecords() != null) {
            for (ListGridRecord rawRecord : listGrid.getRecords()) {
                DatasetRecord datasetRecord = (DatasetRecord) rawRecord;
                if (urn.equals(datasetRecord.getUrn())) {
                    datasetRecord.setPublicationStreamStatus(CommonUtils.getPublicationStreamStatusIcon(status));
                    listGrid.redraw();
                    break;
                }
            }
        }
    }

    public void resetAllStatusDatasetVersion() {
        if (listGrid.getRecords() != null) {
            for (ListGridRecord rawRecord : listGrid.getRecords()) {
                DatasetRecord datasetRecord = (DatasetRecord) rawRecord;
                datasetRecord.resetPublicationStreamStatus();
            }
        }
    }

    public void showUpdateStatus(boolean show) {
        ListGridField fieldStatus = listGrid.getField(LifeCycleResourceDS.PUBLICATION_STREAM_STATUS);
        if (fieldStatus != null) {
            fieldStatus.setHidden(!show);
        }
    }

    public Integer getNumberSelectedDatasets() {
        return listGrid.getRecords() != null ? listGrid.getRecords().length : 0;
    }
}
