package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.AttributeValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.model.ds.DsdAttributeInstanceDS;
import org.siemac.metamac.statistical.resources.web.client.utils.DatasetAttibuteUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemSimpleItem;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

public class AttributeDatasetLevelEditionForm extends AttributeBaseForm {

    private DsdAttributeInstanceDto dsdAttributeInstanceDto;
    private DsdAttributeDto         dsdAttributeDto;

    @Override
    protected void buildEnumeratedRepresentationForm(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        this.dsdAttributeInstanceDto = dsdAttributeInstanceDto;
        this.dsdAttributeDto = dsdAttributeDto;
        SearchExternalItemSimpleItem value = createEnumeratedValueItem(DsdAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
        value.setRequired(true);
        setFields(value);

        if (dsdAttributeInstanceDto.getValue() != null) {
            setValue(DsdAttributeInstanceDS.VALUE, RecordUtils.getExternalItemRecord(dsdAttributeInstanceDto.getValue().getExternalItemValue()));
        }
    }

    @Override
    protected void buildNonEnumeratedRepresentationForm(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        this.dsdAttributeInstanceDto = dsdAttributeInstanceDto;
        this.dsdAttributeDto = dsdAttributeDto;

        CustomTextItem value = new CustomTextItem(DsdAttributeInstanceDS.VALUE, getConstants().datasetAttributeValue());
        value.setValidators(DatasetAttibuteUtils.getDatasetLevelAttibuteValueLengthValidator());

        if (dsdAttributeInstanceDto.getValue() != null) {
            value.setValue(dsdAttributeInstanceDto.getValue().getStringValue());
        }
        setFields(value);
    }

    public DsdAttributeInstanceDto getDsdAttributeInstanceDto() {

        AttributeValueDto attributeValueDto = new AttributeValueDto();
        if (getItem(DsdAttributeInstanceDS.VALUE) instanceof CustomTextItem) {
            attributeValueDto.setStringValue(getValueAsString(DsdAttributeInstanceDS.VALUE));
        } else if (getItem(DsdAttributeInstanceDS.VALUE) instanceof SearchExternalItemSimpleItem) {
            ExternalItemDto selectedExternalItemDto = ((SearchExternalItemSimpleItem) getItem(DsdAttributeInstanceDS.VALUE)).getExternalItemDto();
            attributeValueDto.setExternalItemValue(selectedExternalItemDto);
        }
        dsdAttributeInstanceDto.setValue(attributeValueDto);

        return dsdAttributeInstanceDto;
    }

    public DsdAttributeDto getDsdAttributeDto() {
        return dsdAttributeDto;
    }
    //
    // RELATED RESOURCES
    //

    public void setItemsForDatasetLevelAttributeValueSelection(List<ExternalItemDto> externalItemDtos, int firstResult, int totalResults) {
        if (getItem(DsdAttributeInstanceDS.VALUE) != null && getItem(DsdAttributeInstanceDS.VALUE) instanceof SearchExternalItemSimpleItem) {
            ((SearchExternalItemSimpleItem) getItem(DsdAttributeInstanceDS.VALUE)).setResources(externalItemDtos, firstResult, totalResults);
        }
    }

    private SearchExternalItemSimpleItem createEnumeratedValueItem(String name, String title) {
        return new SearchExternalItemSimpleItem(name, title, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                getUiHandlers().retrieveItemsFromItemSchemeForDatasetLevelAttribute(dsdAttributeDto.getAttributeRepresentation(), firstResult, maxResults, webCriteria);
            }
        };
    }
}
