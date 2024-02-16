package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataPublicationDescriptorsForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

public class DatasetPublicationDescriptorsForm extends SiemacMetadataPublicationDescriptorsForm {

    public DatasetPublicationDescriptorsForm() {
        super();
        init();
    }

    private void init() {

        ViewTextItem statisticOfficiality = new ViewTextItem(DatasetDS.STATISTIC_OFFICIALITY, getConstants().datasetStatisticOfficiality());
        ViewMultiLanguageTextItem bibliographicCitation = new ViewMultiLanguageTextItem(DatasetDS.BIBLIOGRAPHIC_CITATION, getConstants().datasetBibliographicCitation());

        addFields(statisticOfficiality, bibliographicCitation);
    }

    public void setDatasetVersionDto(DatasetVersionDto datasetDto) {
        setSiemacMetadataStatisticalResourceDto(datasetDto);
        setValue(DatasetDS.STATISTIC_OFFICIALITY, CommonUtils.getStatisticOfficialityName(datasetDto.getStatisticOfficiality()));
        setValue(DatasetDS.BIBLIOGRAPHIC_CITATION, datasetDto.getBibliographicCitation());
    }
}
