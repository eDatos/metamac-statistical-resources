package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setExternalItemsValue;

import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils.DatasetWidgetsUtil;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.ExternalItemListItem;

public class SiemacMetadataProductionDescriptorsForm extends LifeCycleResourceProductionDescriptorsForm {

    public SiemacMetadataProductionDescriptorsForm() {
        super();
        init(false);
    }

    public SiemacMetadataProductionDescriptorsForm(boolean isMultipleUpdate) {
        super(isMultipleUpdate);
        init(isMultipleUpdate);
    }

    public void init(boolean isMultipleUpdate) {

        ExternalItemLinkItem creator = new ExternalItemLinkItem(SiemacMetadataDS.CREATOR, getConstants().siemacMetadataStatisticalResourceCreator());
        ExternalItemListItem dataProvider = new ExternalItemListItem(SiemacMetadataDS.DATA_PROVIDER, getConstants().siemacMetadataStatisticalResourceDataProvider(), false);
        ViewMultiLanguageTextItem dataProviderAnnotations = new ViewMultiLanguageTextItem(SiemacMetadataDS.DATA_PROVIDER_ANNOTATIONS,
                getConstants().siemacMetadataStatisticalResourceDataProviderAnnotations());
        dataProviderAnnotations.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        ExternalItemListItem contributor = new ExternalItemListItem(SiemacMetadataDS.CONTRIBUTOR, getConstants().siemacMetadataStatisticalResourceContributor(), false);
        ViewTextItem dateCreated = new ViewTextItem(SiemacMetadataDS.DATE_CREATED, getConstants().siemacMetadataStatisticalResourceDateCreated());
        dataProviderAnnotations.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        ViewTextItem lastUpdate = new ViewTextItem(SiemacMetadataDS.LAST_UPDATE, getConstants().siemacMetadataStatisticalResourceLastUpdate());
        dataProviderAnnotations.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        ViewMultiLanguageTextItem conformsTo = new ViewMultiLanguageTextItem(SiemacMetadataDS.CONFORMS_TO, getConstants().siemacMetadataStatisticalResourceConformsTo());
        dataProviderAnnotations.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        ViewMultiLanguageTextItem conformsToInternal = new ViewMultiLanguageTextItem(SiemacMetadataDS.CONFORMS_TO_INTERNAL, getConstants().siemacMetadataStatisticalResourceConformsToInternal());
        dataProviderAnnotations.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        addFields(dateCreated, lastUpdate, creator, dataProvider, dataProviderAnnotations, contributor, conformsTo, conformsToInternal);
    }

    public void setSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        setLifeCycleResourceDto(dto);

        setValue(SiemacMetadataDS.CREATOR, dto.getCreator());
        setExternalItemsValue(getItem(SiemacMetadataDS.DATA_PROVIDER), dto.getDataProvider());
        setValue(SiemacMetadataDS.DATA_PROVIDER_ANNOTATIONS, dto.getDataProviderAnnotations());
        setExternalItemsValue(getItem(SiemacMetadataDS.CONTRIBUTOR), dto.getContributor());
        setValue(SiemacMetadataDS.DATE_CREATED, dto.getResourceCreatedDate());
        setValue(SiemacMetadataDS.LAST_UPDATE, dto.getLastUpdate());
        setValue(SiemacMetadataDS.CONFORMS_TO, dto.getConformsTo());
        setValue(SiemacMetadataDS.CONFORMS_TO_INTERNAL, dto.getConformsToInternal());
    }
}
