package org.siemac.metamac.statistical.resources.web.client.utils;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DateUtils {
       
    private static final String SDMX_FORMAT = "yyyy-MM-dd";
    private static final String DATE_SEPARATOR_SDMX_FORMAT = "-";
    
    private static final String OUTPUT_FORMAT = "dd/MM/yyyy";
    private static final String DATE_SEPARATOR_OUTPUT_FORMAT = "/";

    private DateUtils() {
        // without implement
    }
    
    
    public static Date getDate(String dateInSdmx, String format) {
        if (dateInSdmx == null || format == null)
            return null;

        Date retVal = null;
        try {
            retVal = DateTimeFormat.getFormat(format).parse(dateInSdmx);
        } catch (Exception e) {
            retVal = null;
        }
        return retVal;
    }
    
    public static Date getDate(String dateInSdmx) {
        if (dateInSdmx == null || "".equals(dateInSdmx) || !isValidDateInSdmx(dateInSdmx))
            return null;

        Date retVal = null;
        try {
            retVal = DateTimeFormat.getFormat(OUTPUT_FORMAT).parse(dateInSdmx.split(DATE_SEPARATOR_SDMX_FORMAT)[2] + DATE_SEPARATOR_OUTPUT_FORMAT + dateInSdmx.split(DATE_SEPARATOR_SDMX_FORMAT)[1]
                    + DATE_SEPARATOR_OUTPUT_FORMAT + dateInSdmx.split(DATE_SEPARATOR_SDMX_FORMAT)[0]);
        } catch (Exception e) {
            retVal = null;
        }
        return retVal;
    }
    
    public static String getDateInSdmxFormat(Date date) {
        
        return DateTimeFormat.getFormat(SDMX_FORMAT).format(date);
    }
    
    public static boolean isValidDateInSdmx(String sdmxTimePeriod) {
        boolean isvalidDate = true;

        try {
            String transformedInput = DateTimeFormat.getFormat(SDMX_FORMAT).format(getDate(sdmxTimePeriod, SDMX_FORMAT));

            isvalidDate = transformedInput.equals(sdmxTimePeriod);
        } catch (Exception e) {
            isvalidDate = false;
        }

        return isvalidDate;
    }
}
