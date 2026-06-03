package org.siemac.metamac.statistical.resources.web.client.model.record;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.web.client.model.ds.DsdAttributeInstanceDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.DsdGranularityAttributeInstanceDS;
import org.siemac.metamac.web.common.client.widgets.NavigableListGridRecord;

public class DsdGranularityAttributeInstanceRecord extends NavigableListGridRecord {

    public DsdGranularityAttributeInstanceRecord() {
    }

    public void setUuid(String value) {
        setAttribute(DsdGranularityAttributeInstanceDS.UUID, value);
    }

    public String getUuid() {
        return getAttributeAsString(DsdGranularityAttributeInstanceDS.UUID);
    }

    public void setGranularityCodesSummary(String value) {
        setAttribute(DsdGranularityAttributeInstanceDS.GRANULARITY_CODES_SUMMARY, value);
    }

    public void setStringValue(String value) {
        setAttribute(DsdAttributeInstanceDS.VALUE, value);
        setAttribute(DsdGranularityAttributeInstanceDS.VALUE, value);
    }

    public void setExternalItemValue(ExternalItemDto externalItemDto) {
        setExternalItem(DsdAttributeInstanceDS.VALUE, externalItemDto);
        setExternalItem(DsdGranularityAttributeInstanceDS.VALUE, externalItemDto);
    }

    public void setDto(DsdGranularityAttributeInstanceDto dto) {
        setAttribute(DsdGranularityAttributeInstanceDS.DTO, dto);
    }

    public DsdGranularityAttributeInstanceDto getDto() {
        return (DsdGranularityAttributeInstanceDto) getAttributeAsObject(DsdGranularityAttributeInstanceDS.DTO);
    }
}
