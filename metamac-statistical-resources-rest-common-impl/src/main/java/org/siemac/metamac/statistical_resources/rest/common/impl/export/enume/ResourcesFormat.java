package org.siemac.metamac.statistical_resources.rest.common.impl.export.enume;

import javax.ws.rs.core.MediaType;

import org.siemac.metamac.statistical_resources.rest.common.impl.export.utils.Constants;

public enum ResourcesFormat {

    //@formatter:off
    JSON(MediaType.APPLICATION_JSON, "json", null),
    XML(MediaType.APPLICATION_XML, "xml", null),
    JSONSTAT(Constants.MIME_TYPE_JSONSTAT, "jsonstat", null),
    TSV(Constants.MIME_TYPE_TSV, "tsv", "\t"),
    CSV(Constants.MIME_TYPE_CSV, "csv", ","),
    XLSX(Constants.MIME_TYPE_XLSX, "xlsx", null);
    //@formatter:on

    private final String mimeType;
    private final String extension;
    private final String separator;

    ResourcesFormat(String mimeType, String extension, String separator) {
        this.mimeType = mimeType;
        this.extension = extension;
        this.separator = separator;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getSeparator() {
        return separator;
    }

}
