package org.siemac.metamac.statistical_resources.rest.common.impl.export;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.enume.utils.IstacTimeGranularityCodeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.utils.DimensionsFilter;

import es.gobcan.istac.edatos.dataset.repository.dto.TabularDataDto;

public class PlainTextTransposedExporter {

    private static final String    SEPARATOR                  = "\t";
    private PrintWriter            printWriter;
    private final TabularDataDto   tabularDataDto;
    private final DimensionsFilter dimensionsFilter;
    private final List<String>     geographicCodes;
    int                            temporalDimensionIndex     = -1;
    int                            geographicalDimensionIndex = -1;

    public PlainTextTransposedExporter(TabularDataDto tabularDataDto, DimensionsFilter dimensionsFilter, List<String> geographicCodes) {
        this.tabularDataDto = tabularDataDto;
        this.dimensionsFilter = dimensionsFilter;
        this.geographicCodes = geographicCodes;
    }

    public void writeObservations(OutputStream os) throws MetamacException {
        printWriter = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        createBodyForPlainTextObservations();
    }

    private void createBodyForPlainTextObservations() throws MetamacException {
        try {
            if (tabularDataDto == null || tabularDataDto.getRows().isEmpty()) {
                throw new MetamacException(ServiceExceptionType.DATASOURCE_EXPORT_NO_DATA_PRESENT);
            }

            String header = getHeader(tabularDataDto.getHeaders());
            write(header);
            write(tabularDataDto.getRows());
        } finally {
            dispose();
        }
    }

    private void write(List<String[]> rows) throws MetamacException {
        for (String[] row : rows) {
            if (checkInTemporalGranularities(row, temporalDimensionIndex, dimensionsFilter) && checkInGeographicalGranularities(row, geographicalDimensionIndex, dimensionsFilter, geographicCodes)) {
                write(String.join(SEPARATOR, row));
            }
        }
    }

    private String getHeader(List<String> header) {
        StringBuilder headerLine = new StringBuilder();
        for (String key : header) {
            headerLine.append(headerLine.length() == 0 ? key : (SEPARATOR + key));
        }

        for (int i = 0; i < header.size(); i++) {
            if (header.get(i).equals(dimensionsFilter.getTemporalDimensionId())) {
                temporalDimensionIndex = i;
            }
            if (header.get(i).equals(dimensionsFilter.getGeographicDimensionId())) {
                geographicalDimensionIndex = i;
            }
        }
        return headerLine.toString();
    }

    private boolean checkCodeDimensionsInGeographicalGranularities(String source, List<String> geographicGranularities) {
        if (geographicGranularities == null || geographicGranularities.isEmpty()) {
            return true;
        }
        return geographicGranularities.contains(source);
    }

    private boolean checkInTemporalGranularities(Object[] row, int temporalDimensionIndex, DimensionsFilter dimensionsFilter) throws MetamacException {
        if (temporalDimensionIndex >= 0) {
            IstacTimeGranularityCodeEnum istacTimeGranularityCodeEnum = org.siemac.metamac.statistical.resources.core.enume.utils.IstacTimeUtils
                    .guessTimeGranularity((String) row[temporalDimensionIndex]);
            return dimensionsFilter.getTemporalDimensionValuesIds().contains(istacTimeGranularityCodeEnum.getLabel());
        }
        return true;
    }

    private boolean checkInGeographicalGranularities(Object[] row, int geographicalDimensionIndex, DimensionsFilter dimensionsFilter, List<String> geographicCodes) {
        if (geographicalDimensionIndex >= 0) {
            for (String code : geographicCodes) {
                if (row[geographicalDimensionIndex].equals(code)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private void dispose() {
        if (printWriter != null) {
            printWriter.flush();
        }
    }

    private void write(String line) {
        printWriter.println(line);
    }

    public DimensionsFilter getDimensionsFilter() {
        return dimensionsFilter;
    }

    public List<String> getCodes() {
        return geographicCodes;
    }

    public int getTemporalDimensionIndex() {
        return temporalDimensionIndex;
    }

    public void setTemporalDimensionIndex(int temporalDimensionIndex) {
        this.temporalDimensionIndex = temporalDimensionIndex;
    }

    public int getGeographicalDimensionIndex() {
        return geographicalDimensionIndex;
    }

    public void setGeographicalDimensionIndex(int geographicalDimensionIndex) {
        this.geographicalDimensionIndex = geographicalDimensionIndex;
    }

    public TabularDataDto getTabularDataDto() {
        return tabularDataDto;
    }

}
