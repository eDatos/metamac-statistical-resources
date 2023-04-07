package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.entities;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class PlainTextResourceAccess implements PlainTextTransformer {

    private Map<String, String> fields = new LinkedHashMap<>();

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    @Override
    public Map<?, ?> flatten() {
        Map<String, Object> myMap = new LinkedHashMap<>();

        myMap.putAll(fields);

        return myMap;
    }

    @Override
    public Set<String> retrieveExcludedFields(Set<String> fields) {
        Set<String> excludedFields = new HashSet<>();

        excludedFields.add("fields");

        return excludedFields;
    }

}
