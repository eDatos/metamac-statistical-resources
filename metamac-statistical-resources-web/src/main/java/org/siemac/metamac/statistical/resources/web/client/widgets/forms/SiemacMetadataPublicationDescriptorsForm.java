package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setExternalItemsValue;

import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.ExternalItemListItem;

public class SiemacMetadataPublicationDescriptorsForm extends GroupDynamicForm {

    public SiemacMetadataPublicationDescriptorsForm() {
        super(getConstants().formPublicationDescriptors());

        ExternalItemListItem publisher = new ExternalItemListItem(SiemacMetadataDS.PUBLISHER, getConstants().siemacMetadataStatisticalResourcePublisher(), false);

        ExternalItemListItem publisherContributor = new ExternalItemListItem(SiemacMetadataDS.PUBLISHER_CONTRIBUTOR, getConstants().siemacMetadataStatisticalResourcePublisherContributor(), false);

        ExternalItemListItem mediator = new ExternalItemListItem(SiemacMetadataDS.MEDIATOR, getConstants().siemacMetadataStatisticalResourceMediator(), false);

        ViewTextItem dateNewnessUntil = new ViewTextItem(SiemacMetadataDS.NEWNESS_UNTIL_DATE, getConstants().siemacMetadataStatisticalResourceNewnessUntilDate());
        ViewTextItem dateFeaturedUntil = new ViewTextItem(SiemacMetadataDS.FEATURED_UNTIL_DATE, getConstants().siemacMetadataStatisticalResourceFeaturedUntilDate());

        setFields(publisher, publisherContributor, mediator, dateNewnessUntil, dateFeaturedUntil);
    }

    public void setSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        setExternalItemsValue(getItem(SiemacMetadataDS.PUBLISHER), dto.getPublisher());
        setExternalItemsValue(getItem(SiemacMetadataDS.PUBLISHER_CONTRIBUTOR), dto.getPublisherContributor());
        setExternalItemsValue(getItem(SiemacMetadataDS.MEDIATOR), dto.getMediator());
        setValue(SiemacMetadataDS.NEWNESS_UNTIL_DATE, dto.getNewnessUntilDate());
        setValue(SiemacMetadataDS.FEATURED_UNTIL_DATE, dto.getFeaturedUntilDate());
    }
}
