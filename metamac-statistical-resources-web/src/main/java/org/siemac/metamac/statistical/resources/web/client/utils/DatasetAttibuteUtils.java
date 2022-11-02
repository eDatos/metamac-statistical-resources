package org.siemac.metamac.statistical.resources.web.client.utils;

import org.siemac.metamac.statistical.resources.core.utils.shared.DatasetAttibuteSharedUtils;

import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;

public class DatasetAttibuteUtils {

    private DatasetAttibuteUtils() {
    }

    public static LengthRangeValidator getDatasetLevelAttibuteValueLengthValidator() {
        return getAttibuteValueLengthValidator(DatasetAttibuteSharedUtils.DATASET_LEVEL_ATTRIBUTE_VALUE_MAXIMUM_SIZE);
    }

    public static LengthRangeValidator getDimensionOrGroupLevelAttibuteValueLengthValidator() {
        return getAttibuteValueLengthValidator(DatasetAttibuteSharedUtils.DIMENSION_OR_GROUP_LEVEL_ATTRIBUTE_VALUE_MAXIMUM_SIZE);
    }

    private static LengthRangeValidator getAttibuteValueLengthValidator(int maxSize) {
        LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();
        lengthRangeValidator.setMin(0);
        lengthRangeValidator.setMax(maxSize);

        return lengthRangeValidator;
    }

}
