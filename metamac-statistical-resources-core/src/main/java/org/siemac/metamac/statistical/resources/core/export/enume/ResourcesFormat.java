package org.siemac.metamac.statistical.resources.core.export.enume;

import javax.ws.rs.core.MediaType;

import org.siemac.metamac.rest.common.export.mappers.PlainTextMessageBodyWritter;
import org.siemac.metamac.statistical.resources.core.export.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ResourcesFormat {
    JSON, XML, JSONSTAT, TSV, CSV, XLSX, XLS;

    private static final Logger logger = LoggerFactory.getLogger(PlainTextMessageBodyWritter.class);

    public static String getMimeType(String format) {
        if (format.equals(ResourcesFormat.JSON.name())) {
            return MediaType.APPLICATION_JSON;
        } else if (format.equals(ResourcesFormat.JSONSTAT.name())) {
            return Constants.MIME_TYPE_JSONSTAT;
        } else if (format.equals(ResourcesFormat.XML.name())) {
            return MediaType.APPLICATION_XML;
        } else if (format.equals(ResourcesFormat.TSV.name())) {
            return Constants.MIME_TYPE_TSV;
        } else if (format.equals(ResourcesFormat.CSV.name())) {
            return Constants.MIME_TYPE_CSV;
        } else if (format.equals(ResourcesFormat.XLSX.name())) {
            return Constants.MIME_TYPE_XLSX;
        } else {
            logger.warn("mimeType not recognized for {}", format);
            return null;
        }
    }
}
