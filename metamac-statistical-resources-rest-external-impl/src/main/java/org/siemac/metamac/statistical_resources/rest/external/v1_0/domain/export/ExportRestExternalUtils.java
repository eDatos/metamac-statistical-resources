package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Response.Status;

import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportRestExternalUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExportRestExternalUtils.class);

    public static final String FIELD_EXCLUDE_ATTRIBUTES = "-attributes";
    private static final String SPACE = " ";
    private static final String PLUS = "+";

    /**
     * Throws response error, logging exception
     */
    public static RestException manageException(Exception e) {
        logger.error("Error", e);
        if (e instanceof RestException) {
            return (RestException) e;
        } else {
            // do not show information details about exception to user
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestCommonServiceExceptionType.UNKNOWN);
            return new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Set<String> parseFieldsParameter(String fieldsParam) {
        Set<String> showFields = new HashSet<>();
        if (fieldsParam != null) {
            Set<String> validFields = new HashSet<>();
            validFields.add(FIELD_EXCLUDE_ATTRIBUTES);
            List<String> fieldList = Arrays.asList(fieldsParam.split(","));
            List<String> parsedFieldList = new ArrayList<>();
            for (String field : fieldList) {
                if (field.startsWith(SPACE)) {
                    parsedFieldList.add(field.replaceFirst(SPACE, PLUS));
                } else {
                    parsedFieldList.add(field);
                }
            }
            for (String value : validFields) {
                if (parsedFieldList.contains(value)) {
                    showFields.add(value);
                }
            }
        }
        return showFields;
    }

    public static boolean containsField(Set<String> fields, String field) {
        return fields != null && fields.contains(field);
    }

}
