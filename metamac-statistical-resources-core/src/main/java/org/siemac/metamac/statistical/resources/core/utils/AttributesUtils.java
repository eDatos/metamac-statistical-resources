package org.siemac.metamac.statistical.resources.core.utils;

import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataType;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdAttribute;

public class AttributesUtils {

    public static Boolean isMultilingualAttributeTextFormatType(DsdAttribute dsdAttribute) {
        return dsdAttribute.getTextFormatRepresentation() != null && dsdAttribute.getTextFormatRepresentation().getTextType() != null
                && DataType.INTERNATIONAL_STRING.equals(dsdAttribute.getTextFormatRepresentation().getTextType());
    }
}
