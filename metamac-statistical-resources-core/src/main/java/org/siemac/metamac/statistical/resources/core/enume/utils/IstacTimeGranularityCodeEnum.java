package org.siemac.metamac.statistical.resources.core.enume.utils;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;

public enum IstacTimeGranularityCodeEnum {

        /**
        * List of results in the order of bigger to smaller.
        */
        YEARLY("YEARLY", "A"),
        BIYEARLY("BIYEARLY", "S"),
        QUARTERLY("QUARTERLY", "Q"),
        FOUR_MONTHLY("FOUR_MONTHLY","T"),
        MONTHLY("MONTHLY","M"),
        WEEKLY("WEEKLY","W"),
        DAILY("DAILY","D"),
        HOURLY("HOURLY","H");

        private final String label;
        private final String code;
        /**
         */
        IstacTimeGranularityCodeEnum(String code, String label) {
            this.label = label;
            this.code = code;
        }

        public String getLabel() {
            return label;
        }

        public String getCode() {
            return code;
        }

        public IstacTimeGranularityCodeEnum getIstacTimeGranularityCodeEnumByCode(String code) throws MetamacException {
            switch (code) {
                case "Y":
                    return YEARLY;
                case "S":
                    return BIYEARLY;
                case "Q":
                    return QUARTERLY;
                case "T":
                    return FOUR_MONTHLY;
                case "M":
                    return MONTHLY;
                case "W":
                    return WEEKLY;
                case "D":
                    return DAILY;
                case "H":
                    return HOURLY;
                default:
                    throw new MetamacException(CommonServiceExceptionType.PARAMETER_INCORRECT, code);
                    
            }
        }
}
