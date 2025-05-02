package org.siemac.metamac.statistical.resources.core.io.domain;

import java.util.ArrayList;
import java.util.List;

import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;

public class TemporalAttributeValues {

    private List<String>                 values                    = new ArrayList<>();
    private List<InternationalStringDto> internationalStringValues = new ArrayList<>();
    private boolean                      isMultilingualValue       = false;

    public TemporalAttributeValues() {

    }

    public TemporalAttributeValues(List<String> values) {
        if (values != null && !values.isEmpty()) {
            this.values.addAll(values);
        }
    }

    public List<String> getValues() {
        return values;
    }
    public void setValues(List<String> values) {
        this.values = values;
    }

    public void setValues(String value) {
        if (value != null) {
            this.values.add(value);
        }
    }

    public void setInternationalStringValue(InternationalStringDto internationalStringValue) {

        if (internationalStringValue != null) {
            this.internationalStringValues.add(internationalStringValue);
        }
    }

    public List<InternationalStringDto> getInternationalStringValues() {
        return internationalStringValues;
    }

    public void setInternationalStringValues(List<InternationalStringDto> internationalStringValues) {
        this.internationalStringValues = internationalStringValues;
    }

    public boolean isMultilingualValue() {
        return isMultilingualValue;
    }

    public void setMultilingualValue(boolean isMultilingualValue) {
        this.isMultilingualValue = isMultilingualValue;
    }

    public boolean hasValues() {
        return !values.isEmpty() || !internationalStringValues.isEmpty();
    }

}
