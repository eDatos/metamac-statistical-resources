package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical_resources.rest.external.invocation.SrmRestExternalFacade;

public class PlainTextExporter {

    private final ResourceAccess datasetAccess;
    private final DatasetSelection datasetSelection;
    private static final String ESCAPE_DOUBLE_QUOTES = "\"";
    private static final String HEADER_OBSERVATION = "OBS_VALUE";
    private static final String HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE = "_CODE";
    private static final boolean ESCAPE_IF_NECESSARY = true;
    private PlainTextTypeEnum plainTextTypeEnum = null;

    public PlainTextExporter(PlainTextTypeEnum plainTextTypeEnum, ResourceAccess resourceAccess) throws MetamacException {
        datasetAccess = resourceAccess;
        datasetSelection = resourceAccess.getDataSelection();
        this.plainTextTypeEnum = plainTextTypeEnum;
        if (this.plainTextTypeEnum == null) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Plain Text format is required ");
        }
    }
    public PlainTextExporter(SrmRestExternalFacade srmRestExternalFacade, PlainTextTypeEnum plainTextTypeEnum, Dataset dataset, DatasetSelection datasetSelection, String lang, String langAlternative)
            throws MetamacException {
        datasetAccess = new ResourceAccess(srmRestExternalFacade, dataset, datasetSelection, lang, langAlternative);
        this.datasetSelection = datasetSelection;
        this.plainTextTypeEnum = plainTextTypeEnum;
        if (this.plainTextTypeEnum == null) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Plain Text format is required ");
        }
    }
    public PlainTextExporter(SrmRestExternalFacade srmRestExternalFacade, PlainTextTypeEnum plainTextTypeEnum, Query query, DatasetSelection datasetSelection, String lang, String langAlternative)
            throws MetamacException {
        datasetAccess = new ResourceAccess(srmRestExternalFacade, query, null, datasetSelection, lang, langAlternative);
        this.datasetSelection = datasetSelection;
        this.plainTextTypeEnum = plainTextTypeEnum;
        if (this.plainTextTypeEnum == null) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Plain Text format is required ");
        }
    }
    public void writeObservationsAndAttributesWithObservationAttachmentLevel(OutputStream os) throws MetamacException {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));
            writeHeaderForPlainTextObservations(printWriter);
            writeBodyForPlainTextObservations(printWriter);
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error exporting to " + plainTextTypeEnum.getName());
        } finally {
            if (printWriter != null) {
                printWriter.flush();
            }
        }
    }

    private void writeHeaderForPlainTextObservations(PrintWriter printWriter) {
        StringBuilder header = new StringBuilder();
        for (String dimensionId : datasetAccess.getDimensionsOrderedForData()) {
            LabelVisualisationModeEnum labelVisualisation = datasetAccess.getDimensionLabelVisualisationMode(dimensionId);
            if (labelVisualisation.isLabel() || labelVisualisation.isCode()) {
                header.append(escapeString(dimensionId, ESCAPE_IF_NECESSARY) + plainTextTypeEnum.getSeparator());
            }
            if (labelVisualisation.isCode() && labelVisualisation.isLabel()) {
                header.append(escapeString(dimensionId + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE, ESCAPE_IF_NECESSARY) + plainTextTypeEnum.getSeparator());
            }
        }
        header.append(HEADER_OBSERVATION);

        printWriter.println(header);
    }
    private void writeBodyForPlainTextObservations(PrintWriter printWriter) {
        for (int i = 0; i < datasetSelection.getRows(); i++) {
            for (int j = 0; j < datasetSelection.getColumns(); j++) {
                Map<String, String> permutationAtCell = datasetSelection.permutationAtCell(i, j);
                // The observation is complete
                StringBuilder line = new StringBuilder();
                // Dimension values
                for (String dimensionId : datasetAccess.getDimensionsOrderedForData()) {
                    String dimensionValueId = permutationAtCell.get(dimensionId);
                    LabelVisualisationModeEnum labelVisualisation = datasetAccess.getDimensionLabelVisualisationMode(dimensionId);
                    if (labelVisualisation.isLabel()) {
                        String dimensionValueLabel = datasetAccess.getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
                        line.append(escapeString(dimensionValueLabel, ESCAPE_IF_NECESSARY) + plainTextTypeEnum.getSeparator());
                    }
                    if (labelVisualisation.isCode()) {
                        line.append(escapeString(dimensionValueId, ESCAPE_IF_NECESSARY) + plainTextTypeEnum.getSeparator());
                    }
                }
                // Observation
                String observation = datasetAccess.observationAtPermutation(permutationAtCell);
                if (observation == null) {
                    observation = StringUtils.EMPTY;
                }
                line.append(escapeString(observation, ESCAPE_IF_NECESSARY));

                printWriter.println(line);
            }
        }
    }

    private String escapeString(String source, boolean escapeOnlyIfNecessary) {
        if (StringUtils.isEmpty(source)) {
            return source;
        }
        if (escapeOnlyIfNecessary) {
            if (!source.contains(plainTextTypeEnum.getSeparator())) {
                return source;
            }
            if (source.startsWith(ESCAPE_DOUBLE_QUOTES) && source.endsWith(ESCAPE_DOUBLE_QUOTES)) {
                return source; // Already escaped
            }
        }
        // Escape always
        return ESCAPE_DOUBLE_QUOTES + source + ESCAPE_DOUBLE_QUOTES;
    }

}