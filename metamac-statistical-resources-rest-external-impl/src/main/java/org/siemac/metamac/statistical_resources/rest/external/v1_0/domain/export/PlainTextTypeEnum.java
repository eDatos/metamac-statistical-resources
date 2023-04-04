package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import java.io.Serializable;

public enum PlainTextTypeEnum implements Serializable {

    //@formatter:off
    TSV("tsv", "\t", "text/tab-separated-values"),
    CSV_COMMA("csv", ",", "text/csv"),
    CSV_SEMICOLON("csv", ";", "text/csv");
    //@formatter:on

    private String extension;
    private String separator;
    private String mimeType;

    /**
     */
    private PlainTextTypeEnum(String extension, String separator, String mimeType) {
        this.extension = extension;
        this.separator = separator;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getSeparator() {
        return separator;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getName() {
        return name();
    }
}
