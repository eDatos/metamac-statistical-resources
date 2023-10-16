package org.siemac.metamac.statistical_resources.rest.common.impl.export;

import static org.siemac.metamac.core.common.exception.CommonServiceExceptionType.UNKNOWN;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.enume.LabelVisualisationModeEnum;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.enume.ResourcesFormat;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.utils.ExportUtils;

public class PlainTextExporter {

    private final ResourceAccess             datasetAccess;
    private String                           format                                = "";
    private static final String              HEADER_OBSERVATION                    = "OBS_VALUE";
    private static final String              HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE   = "_CODE";
    private static final String              HEADER_INTERNATIONAL_STRING_SEPARATOR = "#";
    private String                           separator;
    private ExcelMapper                      excelMapper;
    PrintWriter                              printWriter;
    private boolean                          isExcelFormat                         = false;
    private static final Map<String, String> separatorsByFormat                    = initMapSeparators();
    Pattern                                  unsupportedCharaters                  = Pattern.compile("[\n\t\r\b\f]");

    private static Map<String, String> initMapSeparators() {
        Map<String, String> map = new HashMap<>();
        map.put("csv", ",");
        map.put("tsv", "\t");
        return Collections.unmodifiableMap(map);
    }

    public PlainTextExporter(ResourceAccess resourceAccess, String format) {
        datasetAccess = resourceAccess;
        this.format = format;
        separator = separatorsByFormat.get(format);

        if (ResourcesFormat.XLSX.name().equals(format.toUpperCase()) || ResourcesFormat.XLS.name().equals(format.toUpperCase())) {
            isExcelFormat = true;
        }
    }

    private String getHeaderName(String name) {
        return processUnsupportedCharaters(name);
    }

    private String getHeaderNameCode(LabelVisualisationModeEnum labelVisualisation, String name) {
        if (labelVisualisation.isLabelAndCode()) {
            return processUnsupportedCharaters(name + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE);
        }
        return "";
    }

    public void writeObservationsAndAttributesWithObservationAttachmentLevel(OutputStream os) throws MetamacException {
        try {

            if (isExcelFormat) {
                excelMapper = new ExcelMapper();
            } else {
                printWriter = new PrintWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));
            }
            getBodyForPlainTextObservations(os);
        } catch (Exception e) {
            throw new MetamacException(e, UNKNOWN, "Error exporting");
        }
    }

    private void dispose() throws MetamacException {
        if (isExcelFormat) {
            excelMapper.dispose();
            excelMapper = null;
        }
        if (printWriter != null) {
            printWriter.flush();
        }
    }

    private void getBodyForPlainTextObservations(OutputStream os) throws MetamacException {
        boolean isHeaderFill = false;
        try {

            for (int i = 0; i < datasetAccess.getDataSelection().getRows(); i++) {
                for (int j = 0; j < datasetAccess.getDataSelection().getColumns(); j++) {
                    Map<String, String> line = new LinkedHashMap<>();
                    Map<String, String> permutationAtCell = datasetAccess.getDataSelection().permutationAtCell(i, j);
                    // The observation is complete
                    // Dimension values
                    for (String dimensionId : datasetAccess.getDimensionsOrderedForData()) {

                        String dimensionValueId = permutationAtCell.get(dimensionId);
                        // LabelVisualisationModeEnum labelVisualisation = datasetAccess.getDimensionLabelVisualisationMode(dimensionId);
                        LabelVisualisationModeEnum labelVisualisation = datasetAccess.getDataSelection() != null
                                ? datasetAccess.getDataSelection().getDimensionLabelVisualisationModel(dimensionId)
                                : LabelVisualisationModeEnum.CODE_AND_LABEL;
                        String headerName = getHeaderName(dimensionId);
                        if (labelVisualisation.isLabel()) {
                            InternationalString dimensionValueLabel = datasetAccess.getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
                            line.putAll(internationalString2MapExport(headerName, dimensionValueLabel));
                        }

                        // if label and code, it needs another column name for code.
                        if (labelVisualisation.isLabelAndCode()) {
                            headerName = getHeaderNameCode(labelVisualisation, dimensionId);
                        }

                        if (labelVisualisation.isCode()) {
                            line.put(headerName, processUnsupportedCharaters(dimensionValueId));
                        }
                    }
                    // Observation
                    String observation = datasetAccess.observationAtPermutation(permutationAtCell);

                    line.put(HEADER_OBSERVATION, processUnsupportedCharaters(observation));

                    // Attributes
                    for (String attributeId : datasetAccess.getAttributeAttachmentLevelIds()) {

                        LabelVisualisationModeEnum labelVisualisation = datasetAccess.getAttributeLabelVisualisationMode(attributeId);
                        String headerName = getHeaderName(attributeId);

                        String attributeValue = datasetAccess.measureAttributeValueAtPermutation(attributeId, permutationAtCell);
                        if (attributeValue == null) {
                            line.putAll(internationalString2MapExport(headerName, new InternationalString()));
                        } else {

                            if (labelVisualisation.isLabel()) {
                                InternationalString attributeValueLabel = datasetAccess.getAttributeValueLabelCurrentLocale(attributeId, attributeValue);
                                if (attributeValueLabel != null) {
                                    line.putAll(internationalString2MapExport(headerName, attributeValueLabel));
                                } else {
                                    line.putAll(internationalString2MapExport(headerName, new InternationalString()));
                                }

                            }

                            // if label and code, it needs another column name for code.
                            if (labelVisualisation.isLabelAndCode()) {
                                headerName = getHeaderNameCode(labelVisualisation, attributeId);
                            }

                            if (labelVisualisation.isCode()) {
                                line.put(headerName, processUnsupportedCharaters(attributeValue));
                            }
                        }
                    }

                    // put line in file
                    if (!isHeaderFill) {
                        createHeader(line);
                        isHeaderFill = true;
                    }

                    createObservation(line);
                }
            }
            writeToOutputStream(os);
        } finally {
            dispose();
        }
    }

    private void writeToOutputStream(OutputStream os) throws MetamacException {
        if (isExcelFormat) {
            excelMapper.writeExcelWorkBookToOutputStream(os);
        }
    }

    private void createHeader(Map<String, String> line) {
        if (isExcelFormat) {
            excelMapper.createHeaderRow(line);
        } else {
            createHeaderPlainText(line);
        }

    }

    private void createHeaderPlainText(Map<String, String> line) {
        StringBuilder headerLine = new StringBuilder();
        for (Map.Entry<String, String> columnObservation : line.entrySet()) {
            String key = columnObservation.getKey();
            headerLine.append(headerLine.length() == 0 ? key : (separator + key));
        }
        printWriter.println(headerLine);
    }

    private void createObservation(Map<String, String> line) {
        if (isExcelFormat) {
            excelMapper.addObservationRow(line);
        } else {
            createObservationPlainText(line);
        }

    }

    private void createObservationPlainText(Map<String, String> line) {
        StringBuilder observationLine = new StringBuilder();
        for (Map.Entry<String, String> columnObservation : line.entrySet()) {
            String value = ExportUtils.escapeNulls(columnObservation.getValue());
            observationLine.append(observationLine.length() == 0 ? value : (separator + value));
        }
        printWriter.println(observationLine);
    }

    private Map<String, String> internationalString2MapExport(String nameField, InternationalString source) {
        Map<String, String> target = new LinkedHashMap<>();

        InternationalString copySource = new InternationalString();

        if (source != null) {
            copySource = source;
        }

        String header;
        for (String language : getSelectedLanguages()) {
            header = nameField + HEADER_INTERNATIONAL_STRING_SEPARATOR + language;
            target.put(header, getLocalisedStringByLang(copySource.getTexts(), language));
        }

        return target;
    }

    private String getLocalisedStringByLang(List<LocalisedString> source, String language) {
        // in the api, lamba functions is not allowed. the search is done with a loop.
        for (LocalisedString loc : source) {
            if (language.equals(loc.getLang())) {
                return processUnsupportedCharaters(loc.getValue());
            }
        }
        return null;
    }

    private String formattedText(String text) {
        if ("csv".equals(format)) {
            text = StringEscapeUtils.escapeCsv(text);
        }
        return text;
    }

    // remove unsupported characters and if there are excluded characters, to put the value between on quotation marks
    private String processUnsupportedCharaters(String string) {
        if (StringUtils.isNotBlank(string)) {
            String value = unsupportedCharaters.matcher(string).replaceAll(" ");
            return formattedText(value);
        } else {
            return null;
        }
    }

    public List<String> getSelectedLanguages() {
        return datasetAccess.getSelectedLanguages();
    }
}