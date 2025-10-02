package org.siemac.metamac.statistical.resources.core.export;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.common.domain.DimensionsFilter;
import org.siemac.metamac.statistical.resources.core.enume.utils.IstacTimeGranularityCodeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.edatos.dataset.repository.dto.TabularDataDto;

public class PlainTextExporter {

    private static final String                       HEADER_OBSERVATION         = "OBS_VALUE";
    private static final String                       EXCLUDE_HEADER             = "DATA_SOURCE_ID";
    private static final String                       SEPARATOR                  = "\t";
    private final Map<String, ObservationExtendedDto> observations;
    private final TabularDataDto                      tabularDataDto;
    private final DimensionsFilter                    dimensionsFilter;
    private final List<String>                        geographicCodes;
    int                                               temporalDimensionIndex     = -1;
    int                                               geographicalDimensionIndex = -1;
    private PrintWriter                               printWriter;
    private String                                    lang;

    public PlainTextExporter(Map<String, ObservationExtendedDto> observations, TabularDataDto tabularDataDto, DimensionsFilter dimensionsFilter, List<String> geographicCodes) {
        this.observations = observations;
        this.tabularDataDto = tabularDataDto;
        this.dimensionsFilter = dimensionsFilter;
        this.geographicCodes = geographicCodes;
    }

    public void writeObservations(OutputStream os) throws MetamacException {
        printWriter = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        createBodyForPlainTextTransposedObservations();
    }

    private void createBodyForPlainTextTransposedObservations() throws MetamacException {
        try {
            if (tabularDataDto == null || tabularDataDto.getRows().isEmpty()) {
                throw new MetamacException(ServiceExceptionType.DATASOURCE_EXPORT_NO_DATA_PRESENT);
            }

            String header = getHeader(tabularDataDto.getHeaders());
            write(header);
            writeTransposedRows(tabularDataDto.getRows());
        } finally {
            dispose();
        }
    }

    private void writeTransposedRows(List<List> rows) throws MetamacException {
        for (List row : rows) {
            if (checkInTemporalGranularities(row, temporalDimensionIndex, dimensionsFilter) && checkInGeographicalGranularities(row, geographicalDimensionIndex, geographicCodes)) {
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

    private boolean checkInTemporalGranularities(List row, int temporalDimensionIndex, DimensionsFilter dimensionsFilter) throws MetamacException {
        if (temporalDimensionIndex >= 0) {
            IstacTimeGranularityCodeEnum istacTimeGranularityCodeEnum = org.siemac.metamac.statistical.resources.core.enume.utils.IstacTimeUtils
                    .guessTimeGranularity((String) row.get(temporalDimensionIndex));
            return dimensionsFilter.getTemporalDimensionValuesIds().contains(istacTimeGranularityCodeEnum.getLabel());
        }
        return true;
    }

    private boolean checkInGeographicalGranularities(List row, int geographicalDimensionIndex, List<String> geographicCodes) {
        if (geographicalDimensionIndex >= 0) {
            for (String code : geographicCodes) {
                if (row.get(geographicalDimensionIndex).equals(code)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public void writeObservationsAndAttributesWithObservationAttachmentLevel(OutputStream os, String lang) throws MetamacException {
        this.lang = lang;
        printWriter = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        createBodyForPlainTextObservations();
    }

    private void createBodyForPlainTextObservations() throws MetamacException {
        Map<String, List<String>> datasourceColumns = new LinkedHashMap<>();

        try {
            if (observations == null || observations.keySet().isEmpty()) {
                throw new MetamacException(ServiceExceptionType.DATASOURCE_EXPORT_NO_DATA_PRESENT);
            }

            for (Map.Entry<String, ObservationExtendedDto> entry : observations.entrySet()) {
                ObservationExtendedDto observation = entry.getValue();
                createDimensionColumns(datasourceColumns, observation);
            }
            datasourceColumns.remove(EXCLUDE_HEADER);
            int obsHeaderIndex = new ArrayList<>(datasourceColumns.keySet()).indexOf(HEADER_OBSERVATION);

            if (obsHeaderIndex + 1 < datasourceColumns.keySet().size()) {
                List<String> attributeIds = new ArrayList<>(datasourceColumns.keySet()).subList(obsHeaderIndex + 1, datasourceColumns.keySet().size());
                for (Map.Entry<String, ObservationExtendedDto> entry : observations.entrySet()) {
                    ObservationExtendedDto observation = entry.getValue();
                    createAttributesColumns(datasourceColumns, observation, attributeIds);
                }
            }

            String header = getHeader(datasourceColumns);
            write(header);

            List<String> observationLines = getObservations(datasourceColumns);
            write(observationLines);
        } finally {
            dispose();
        }
    }

    private List<String> getObservations(Map<String, List<String>> datasourceColumns) {
        List<String> observationLines = new ArrayList<>();
        int maxLines = datasourceColumns.get(HEADER_OBSERVATION).size();

        for (int i = 0; i < maxLines; i++) {
            StringBuilder line = new StringBuilder();
            for (List<String> column : datasourceColumns.values()) {
                String value = column.get(i) != null ? column.get(i) : "";
                line.append(line.length() == 0 ? value : (SEPARATOR + value));
            }
            observationLines.add(line.toString());
        }
        return observationLines;
    }

    private void createDimensionColumns(Map<String, List<String>> datasourceLines, ObservationExtendedDto observation) {
        // dimensions
        for (CodeDimensionDto codeDimensionDto : observation.getCodesDimension()) {
            String value = codeDimensionDto.getCodeDimensionId();
            datasourceLines.putIfAbsent(codeDimensionDto.getDimensionId(), new ArrayList<>());
            datasourceLines.get(codeDimensionDto.getDimensionId()).add(value);
        }

        // observation primary measure value
        datasourceLines.putIfAbsent(HEADER_OBSERVATION, new ArrayList<>());
        datasourceLines.get(HEADER_OBSERVATION).add(observation.getPrimaryMeasure());

        // attributes first pass: only add the attribute id
        for (AttributeInstanceObservationDto attribute : observation.getAttributes()) {
            datasourceLines.putIfAbsent(attribute.getAttributeId(), new ArrayList<>());
        }
    }

    private void createAttributesColumns(Map<String, List<String>> datasourceColumns, ObservationExtendedDto observation, List<String> attributeIds) {
        // attributes second pass: add values
        for (String attributeId : attributeIds) {
            if (observation.getAttributesAsMap().containsKey(attributeId)) {
                String value = observation.getAttributesAsMap().get(attributeId).getValue().getLocalisedLabel(lang);
                datasourceColumns.get(attributeId).add(value);
            } else {
                datasourceColumns.get(attributeId).add(null);
            }
        }
    }

    private void write(String line) {
        printWriter.println(line);
    }

    private void write(List<String> lines) {
        for (String line : lines) {
            write(line);
        }
    }

    private void dispose() {
        if (printWriter != null) {
            printWriter.flush();
        }
    }

    private String getHeader(Map<String, List<String>> observations) {
        StringBuilder headerLine = new StringBuilder();
        for (String key : observations.keySet()) {
            headerLine.append(headerLine.length() == 0 ? key : (SEPARATOR + key));
        }
        return headerLine.toString();
    }

}