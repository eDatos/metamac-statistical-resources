package org.siemac.metamac.statistical.resources.web.client.utils;

import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;

public class DatasetAttibuteUtils {

    private DatasetAttibuteUtils() {
    }

    public static LengthRangeValidator getAttibuteValueLengthValidator() {
        LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();
        lengthRangeValidator.setMin(0);
        lengthRangeValidator.setMax(4000);

        return lengthRangeValidator;
    }

}
