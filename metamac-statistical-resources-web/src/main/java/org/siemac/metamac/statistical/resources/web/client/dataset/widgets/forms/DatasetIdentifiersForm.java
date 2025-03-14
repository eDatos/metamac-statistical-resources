package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.LifeCycleResourceDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.NameableResourceIdentifiersForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

public class DatasetIdentifiersForm extends NameableResourceIdentifiersForm {

    public DatasetIdentifiersForm() {

        ViewTextItem datasetRepositoryId = new ViewTextItem(DatasetDS.DATASET_REPOSITORY_ID, getConstants().datasetRepositoryId());
        ViewTextItem publicationStreamStatus = new ViewTextItem(LifeCycleResourceDS.PUBLICATION_STREAM_STATUS, getConstants().lifeCycleStatisticalResourceStreamMsgStatus());
        publicationStreamStatus.setWidth(20);
        
        ViewTextItem publicationXStatus = new ViewTextItem(LifeCycleResourceDS.PUBLICATION_X_STATUS, getConstants().lifeCycleStatisticalResourceXMsgStatus());
        publicationXStatus.setWidth(20);
        ViewTextItem datasetViewIdentifier = new ViewTextItem(DatasetDS.VIEW_CODE, getConstants().datasetViewIdentifier());

        addFields(datasetRepositoryId, publicationStreamStatus, publicationXStatus, datasetViewIdentifier);
    }

    public void setDatasetVersionDto(DatasetVersionDto datasetDto) {
        setNameableStatisticalResourceDto(datasetDto);
        setValue(DatasetDS.DATASET_REPOSITORY_ID, datasetDto.getDatasetRepositoryId());
        getItem(LifeCycleResourceDS.PUBLICATION_STREAM_STATUS).setIcons(CommonUtils.getPublicationStreamStatusIcon(datasetDto.getPublicationStreamStatus()));
        getItem(LifeCycleResourceDS.PUBLICATION_X_STATUS).setIcons(CommonUtils.getXStatusIcon(datasetDto.getXStreamStatus()));
        setValue(DatasetDS.VIEW_CODE, datasetDto.getViewCode());
    }

}
