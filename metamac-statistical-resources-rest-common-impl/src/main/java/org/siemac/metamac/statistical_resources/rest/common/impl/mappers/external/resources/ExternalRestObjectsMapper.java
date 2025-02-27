package org.siemac.metamac.statistical_resources.rest.common.impl.mappers.external.resources;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;

public class ExternalRestObjectsMapper {

    Map<String, InternationalString> operationTitlesByCode          = new HashMap<>();
    Map<String, InternationalString> operationInstancesTitlesByCode = new HashMap<>();

    public Map<String, InternationalString> getOperationInstancesTitlesByCode() {
        return operationInstancesTitlesByCode;
    }

    public void setOperationInstancesTitlesByCode(Map<String, InternationalString> operationInstancesTitlesByCode) {
        this.operationInstancesTitlesByCode = operationInstancesTitlesByCode;
    }

    public Map<String, InternationalString> getOperationTitlesByCode() {
        return operationTitlesByCode;
    }

    public void setOperationTitlesByCode(Map<String, InternationalString> operationTitlesByCode) {
        this.operationTitlesByCode = operationTitlesByCode;
    }

}
