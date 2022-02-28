package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.NameableResourceIdentifiersEditionForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

public class DatasetIdentifiersEditionForm extends NameableResourceIdentifiersEditionForm {

    public DatasetIdentifiersEditionForm() {

        ViewTextItem datasetRepositoryId = new ViewTextItem(DatasetDS.DATASET_REPOSITORY_ID, getConstants().datasetRepositoryId());
        ViewTextItem datasetViewIdentifier = new ViewTextItem(DatasetDS.VIEW_CODE, getConstants().datasetViewIdentifier());

        addFields(datasetRepositoryId, datasetViewIdentifier);
    }

    public void setDatasetVersionDto(DatasetVersionDto datasetDto) {
        setNameableStatisticalResourceDto(datasetDto);
        setValue(DatasetDS.DATASET_REPOSITORY_ID, datasetDto.getDatasetRepositoryId());
        setValue(DatasetDS.VIEW_CODE, datasetDto.getViewCode());
    }

    public DatasetVersionDto getDatasetVersionDto(DatasetVersionDto datasetDto) {
        return (DatasetVersionDto) getNameableStatisticalResourceDto(datasetDto);
    }
}
