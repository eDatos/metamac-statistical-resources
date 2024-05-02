package org.siemac.metamac.statistical.resources.core.export;

import static org.siemac.metamac.core.common.exception.CommonServiceExceptionType.UNKNOWN;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;

public class PlainTextExporter {

    private static final String HEADER_OBSERVATION = "OBS_VALUE";
    private static final String EXCLUDE_HEADER = "DATA_SOURCE_ID";
    private static final String SEPARATOR = "\t";
    private final Map<String, ObservationExtendedDto> observations;
    PrintWriter printWriter;

    public PlainTextExporter(Map<String, ObservationExtendedDto> observations) {
        this.observations = observations;
    }

    public void writeObservationsAndAttributesWithObservationAttachmentLevel(OutputStream os) throws MetamacException {
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            createBodyForPlainTextObservations();
        } catch (Exception e) {
            throw new MetamacException(e, UNKNOWN, "Error exporting");
        }
    }

    private void createBodyForPlainTextObservations() throws MetamacException {
        boolean isHeaderFill = false;

        Map<String, List<String>> datasourceColumns = new LinkedHashMap<>();

        try {
            if (observations == null || observations.keySet().isEmpty()) {
                throw new MetamacException(UNKNOWN, "No datasources to export");
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
                String value = observation.getAttributesAsMap().get(attributeId).getValue().getLocalisedLabel("es");
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