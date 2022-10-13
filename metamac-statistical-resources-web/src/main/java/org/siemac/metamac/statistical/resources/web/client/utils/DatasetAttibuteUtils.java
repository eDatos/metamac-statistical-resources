package org.siemac.metamac.statistical.resources.web.client.utils;

import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;

public class DatasetAttibuteUtils {

    private static final int DATASET_LEVEL_ATTRIBUTE_VALUE_MAXIMUM_SIZE            = 4000;
    private static final int DIMENSION_OR_GROUP_LEVEL_ATTRIBUTE_VALUE_MAXIMUM_SIZE = 500;

    private DatasetAttibuteUtils() {
    }

    public static LengthRangeValidator getDatasetLevelAttibuteValueLengthValidator() {
        return getAttibuteValueLengthValidator(DATASET_LEVEL_ATTRIBUTE_VALUE_MAXIMUM_SIZE);
    }

    public static LengthRangeValidator getDimensionOrGroupLevelAttibuteValueLengthValidator() {
        return getAttibuteValueLengthValidator(DIMENSION_OR_GROUP_LEVEL_ATTRIBUTE_VALUE_MAXIMUM_SIZE);
    }

    private static LengthRangeValidator getAttibuteValueLengthValidator(int maxSize) {
        LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();
        lengthRangeValidator.setMin(0);
        lengthRangeValidator.setMax(maxSize);

        return lengthRangeValidator;
    }

}
