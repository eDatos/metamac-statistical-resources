package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils.DatasetWidgetsUtil;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataPublicationDescriptorsEditionForm;
import org.siemac.metamac.web.common.client.utils.CustomRequiredValidator;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;

public class DatasetPublicationDescriptorsEditionForm extends SiemacMetadataPublicationDescriptorsEditionForm {

    public DatasetPublicationDescriptorsEditionForm() {
        super();
        init(false);
    }

    public DatasetPublicationDescriptorsEditionForm(boolean isMultipleUpdate) {
        super();
        init(isMultipleUpdate);
    }

    private void init(boolean isMultipleUpdate) {

        final CustomSelectItem statisticOfficiality = new CustomSelectItem(DatasetDS.STATISTIC_OFFICIALITY, getConstants().datasetStatisticOfficiality());
        statisticOfficiality.setValueMap(CommonUtils.getStatisticOfficialityHashMap());
        statisticOfficiality.addChangedHandler(FormItemUtils.getMarkForRedrawChangedHandler(this));
        statisticOfficiality.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                return CommonUtils.isResourceInProductionValidationOrGreaterProcStatus(procStatus) ? !StringUtils.isBlank(statisticOfficiality.getValueAsString()) : true;
            }
        });

        ViewMultiLanguageTextItem bibliographicCitation = new ViewMultiLanguageTextItem(DatasetDS.BIBLIOGRAPHIC_CITATION, getConstants().datasetBibliographicCitation());
        bibliographicCitation.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        addFields(statisticOfficiality, bibliographicCitation);
    }

    public void setDatasetVersionDto(DatasetVersionDto datasetDto) {
        setSiemacMetadataStatisticalResourceDto(datasetDto);
        setValue(DatasetDS.BIBLIOGRAPHIC_CITATION, datasetDto.getBibliographicCitation());

        if (datasetDto.getStatisticOfficiality() != null) {
            setValue(DatasetDS.STATISTIC_OFFICIALITY, datasetDto.getStatisticOfficiality().getIdentifier());
        } else {
            setValue(DatasetDS.STATISTIC_OFFICIALITY, (String) null);
        }
    }

    public DatasetVersionDto getDatasetVersionDto(DatasetVersionDto datasetDto) {
        datasetDto = (DatasetVersionDto) getSiemacMetadataStatisticalResourceDto(datasetDto);

        String statisticOfficialityIdentifier = getValueAsString(DatasetDS.STATISTIC_OFFICIALITY);
        if (!StringUtils.isEmpty(statisticOfficialityIdentifier)) {
            datasetDto.setStatisticOfficiality(CommonUtils.getStatisticOfficialityByIdentifier(statisticOfficialityIdentifier));
        } else {
            datasetDto.setStatisticOfficiality(null);
        }
        return datasetDto;
    }

}
