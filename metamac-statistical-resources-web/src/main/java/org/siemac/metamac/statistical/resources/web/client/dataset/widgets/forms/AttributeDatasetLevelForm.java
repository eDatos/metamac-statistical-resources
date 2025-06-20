package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.web.client.model.ds.DsdAttributeInstanceDS;
import org.siemac.metamac.statistical.resources.web.client.utils.DatasetAttibuteUtils;
import org.siemac.metamac.web.common.client.model.record.ExternalItemRecord;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

public class AttributeDatasetLevelForm extends AttributeBaseForm {

    @Override
    protected void buildEnumeratedRepresentationForm(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        ExternalItemLinkItem value = new ExternalItemLinkItem(DsdAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
        setFields(value);

        if (dsdAttributeInstanceDto.getValue() != null) {
            ExternalItemRecord record = RecordUtils.getExternalItemRecord(dsdAttributeInstanceDto.getValue().getExternalItemValue());
            setValue(DsdAttributeInstanceDS.VALUE, record);
        }
    }

    @Override
    protected void buildNonEnumeratedRepresentationForm(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {

        if (Boolean.TRUE.equals(dsdAttributeDto.getAttributeRepresentation().getMultilingualType())) {
            buildMultilanguageTextItemNonEnumeratedRepresentationForm(dsdAttributeInstanceDto);
        } else {
            buildTextItemNonEnumeratedRepresentationForm(dsdAttributeInstanceDto);
        }
    }

    private void buildMultilanguageTextItemNonEnumeratedRepresentationForm(DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        ViewMultiLanguageTextItem value = new ViewMultiLanguageTextItem(DsdAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
        value.setValidators(DatasetAttibuteUtils.getDatasetLevelAttibuteValueLengthValidator());

        if (dsdAttributeInstanceDto.getValue() != null) {
            setValue(DsdAttributeInstanceDS.VALUE, RecordUtils.getInternationalStringRecord(dsdAttributeInstanceDto.getValue().getInternationalStringValue()));
        }

        setFields(value);

    }

    private void buildTextItemNonEnumeratedRepresentationForm(DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        ViewTextItem value = new ViewTextItem(DsdAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());

        if (dsdAttributeInstanceDto.getValue() != null) {
            value.setValue(dsdAttributeInstanceDto.getValue().getStringValue());
        }

        setFields(value);
    }
}
