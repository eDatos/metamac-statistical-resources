package org.siemac.metamac.statistical.resources.core.utils;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;

public class ExceptionsUtils {

    public org.siemac.metamac.rest.common.v1_0.domain.Exception getException(MetamacException e) {
        if (e.getExceptionItems() != null && !e.getExceptionItems().isEmpty()
                && ServiceExceptionType.DATASET_NO_DATA.getCode().equals(e.getExceptionItems().get(0).getCode())) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = new org.siemac.metamac.rest.common.v1_0.domain.Exception();
            exception.setCode(e.getExceptionItems().get(0).getCode());
            exception.setMessage(e.getExceptionItems().get(0).getMessage());
            return exception;
        }
        return null;
    }
}