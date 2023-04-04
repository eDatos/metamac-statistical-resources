package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.ExportConstants.COMMA;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.ExportConstants.EQUALS;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.ExportConstants.LEFT_BRACE;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.ExportConstants.LEFT_PARENTHESES;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.ExportConstants.NEW_LINE;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.ExportConstants.QUOTE;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.ExportConstants.RIGHT_BRACE;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.ExportConstants.RIGHT_PARENTHESES;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.ExportConstants.SEMICOLON;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.ExportConstants.SPACE;
import static org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.PortalUtils.buildInternationalStringFromSingleCode;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.common.v1_0.domain.Resources;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Attribute;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.AttributeAttachmentLevelType;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.AttributeValues;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dimension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.EnumeratedAttributeValue;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.EnumeratedAttributeValues;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.EnumeratedDimensionValue;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.NextVersionType;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical_resources.rest.external.invocation.SrmRestExternalFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PxExporter {

    private static final int MAX_LINE_LENGTH = 252;

    private final Logger logger = LoggerFactory.getLogger(PxExporter.class);

    private final ResourceAccess datasetAccess;
    private final String ATTRIBUTE_LINE_SEPARATOR = "#";
    private Set<String> languages = new HashSet<String>();
    private Map<String, Integer> languageOrder = new HashMap<String, Integer>();
    private final DatasetSelection datasetSelection;
    private Integer showDecimals = null;
    private final static int MAX_PX_MATRIX_LENGTH = 8;

    public PxExporter(SrmRestExternalFacade srmRestExternalFacade, Dataset dataset, DatasetSelection datasetSelection, String lang, String langDefault) throws MetamacException {
        datasetAccess = new ResourceAccess(srmRestExternalFacade, dataset, datasetSelection, lang, langDefault);
        this.datasetSelection = datasetSelection;
    }

    public PxExporter(SrmRestExternalFacade srmRestExternalFacade, Query query, Dataset relatedDataset, DatasetSelection datasetSelection, String lang, String langDefault) throws MetamacException {
        datasetAccess = new ResourceAccess(srmRestExternalFacade, query, relatedDataset, datasetSelection, lang, langDefault);
        this.datasetSelection = datasetSelection;
    }

    public void write(OutputStream os) throws MetamacException {
        PrintWriter printWriter = null;
        try {
            /**
             * PX-Axis documentation says:
             * "If the keyword CHARSET is missing it means that all texts are in DOS text format, so that the same files can be used both in the DOS
             * and the Windows version of PC-AXIS. In the Windows version the texts are translated into Windows format when read. When a file is
             * saved in PC-AXIS file format it is always saved in DOS text format in versions prior to 2000.
             * Starting with version 2000 the files can be either in DOS or Windows texts. If they are in Windows texts this information is added: CHARSET="ANSI";".
             */
            printWriter = new PrintWriter(new OutputStreamWriter(os, Charset.forName("ISO-8859-1"))); // charset for ANSI
            writePx(printWriter);
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error exporting to px");
        } finally {
            if (printWriter != null) {
                printWriter.flush();
            }
        }
    }

    private void writePx(PrintWriter printWriter) throws MetamacException {
        // Recommended order of the keywords
        writeCharset(printWriter);

        writeAxisVersion(printWriter);

        // CODEPAGE: NOT SUPPORT

        writeLanguage(printWriter);

        writeLanguages(printWriter);

        writeCreationDate(printWriter);

        writeNextUpdate(printWriter);

        // PX-SERVER: NOT SUPPORT
        // DIRECTORY-PATH: NOT SUPPORT

        writeUpdateFrequency(printWriter);

        writeTableId(printWriter);

        // SYNONYMS: NOT SUPPORT
        // DEFAULT-GRAPH: NOT SUPPORT

        writeDecimals(printWriter);

        writeShowDecimals(printWriter);

        // ROUNDING: NOT SUPPORT

        writeMatrix(printWriter);

        writeAggregallowed(printWriter);

        writeAutopen(printWriter);

        writeSubjectAreaAndSubjectCode(printWriter);

        // CONFIDENTIAL: NOT SUPPORT

        writeCopyRight(printWriter);

        writeDescription(printWriter);

        writeTitle(printWriter);

        writeDescriptionDefault(printWriter);

        writeContents(printWriter);

        writeStub(printWriter);

        writeHeading(printWriter);

        writeContVariable(printWriter);

        writeValues(printWriter);

        // TIMEVAL: NOT SUPPORT

        writeCodes(printWriter);

        // DOUBLECOLUMN: NOT SUPPORT
        // PRESTEXT: NOT SUPPORT
        // DOMAIN: NOT SUPPORT
        // VARIABLE-TYPE: NOT SUPPORT
        // HIERARCHIES: NOT SUPPORT
        // HIERARCHYLEVELS: NOT SUPPORT
        // HIERARCHYLEVELSOPEN: NOT SUPPORT
        // HIERARCHYNAMES: NOT SUPPORT
        // MAP: NOT SUPPORT
        // PARTITIONED: NOT SUPPORT
        // ELIMINATION: NOT SUPPORT

        writeLastUpdated(printWriter);

        // STOCKFA: NOT SUPPORT
        // CFPRICES: NOT SUPPORT
        // DAYADJ: NOT SUPPORT
        // SEASADJ: NOT SUPPORT

        writeUnits(printWriter);

        writeContact(printWriter);

        // REFPERIOD: NOT SUPPORT
        // BASEPERIOD: NOT SUPPORT
        // DATABASE: NOT SUPPORT

        writeSource(printWriter);

        writeSurvey(printWriter);

        writeLink(printWriter);

        // NOTEX, NOTE, VALUENOTEX, VALUENOTE, CELLNOTEX, CELLNOTE
        writeAttributes(printWriter);

        // INFOFILE: NOT SUPPORT
        // FIRST-PUBLISHED: NOT SUPPORT
        // META-ID: NOT SUPPORT
        // OFFICIAL-STATISTICS: NOT SUPPORT
        // INFO: NOT SUPPORT
        // DATASYMBOL1: NOT SUPPORT
        // DATASYMBOL2: NOT SUPPORT
        // DATASYMBOL3: NOT SUPPORT
        // DATASYMBOL4: NOT SUPPORT
        // DATASYMBOL5: NOT SUPPORT
        // DATASYMBOL6: NOT SUPPORT
        // DATASYMBOLSUM: NOT SUPPORT
        // DATASYMBOLNIL: NOT SUPPORT
        // DATANOTECELL: NOT SUPPORT
        // DATANOTESUM: NOT SUPPORT
        // DATANOTE: NOT SUPPORT
        // KEYS: NOT SUPPORT
        // ATTRIBUTE-ID: NOT SUPPORT
        // ATTRIBUTE-TEXT: NOT SUPPORT
        // ATTRIBUTES: NOT SUPPORT

        writePrecision(printWriter);

        writeData(printWriter);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeCharset(PrintWriter printWriter) throws MetamacException {
        // Always export in Windows charset compliant, then always set to ANSI
        PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.CHARSET).withValue("ANSI").build();
        writeLine(printWriter, line);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeAxisVersion(PrintWriter printWriter) throws MetamacException {
        PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.AXIS_VERSION).withValue("2000").build();
        writeLine(printWriter, line);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeLanguage(PrintWriter printWriter) throws MetamacException {
        PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.LANGUAGE).withValue(datasetAccess.getLangDefault().toLowerCase()).build();
        writeLine(printWriter, line);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeLanguages(PrintWriter printWriter) throws MetamacException {
        Resources languages = datasetAccess.getMetadata().getLanguages();

        List<String> resourcesToResourcesId = resourcesToResourcesId(languages.getResources());
        this.languages = new HashSet<String>(resourcesToResourcesId);
        languageOrder = new HashMap<String, Integer>();
        int i = 1;
        String defaultLang = datasetAccess.getLangDefault();
        for (String lang : resourcesToResourcesId) {
            if (defaultLang.equals(lang)) {
                languageOrder.put(lang, 0);
            } else {
                languageOrder.put(lang, i++);
            }
        }

        if (languages.getResources().size() <= 1) {
            return; // Only default language, don't put de LANGUAGES KEY
        }

        PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.LANGUAGES).withValue(resourcesToResourcesId).build();
        writeLine(printWriter, line);
    }

    private List<String> resourcesToResourcesId(List<Resource> sources) {
        List<String> targets = new ArrayList<String>();
        if (CollectionUtils.isEmpty(sources)) {
            return targets;
        }
        for (Resource source : sources) {
            targets.add(source.getId().toLowerCase());
        }
        return targets;
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeCreationDate(PrintWriter printWriter) throws MetamacException {
        PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.CREATION_DATE).withValue(datasetAccess.getMetadata().getCreatedDate()).build();
        writeLine(printWriter, line);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeNextUpdate(PrintWriter printWriter) throws MetamacException {
        String label = PortalUtils.getLabel(datasetAccess.getMetadata().getDateNextUpdate(), datasetAccess.getLang(), datasetAccess.getLangDefault());
        PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.NEXT_UPDATE).withValue(label).build();
        writeLine(printWriter, line);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeUpdateFrequency(PrintWriter printWriter) throws MetamacException {
        StringBuilder value = new StringBuilder();
        NextVersionType nextVersion = datasetAccess.getMetadata().getNextVersion();

        if (nextVersion != null) {
            if (NextVersionType.SCHEDULED_UPDATE.equals(nextVersion)) {
                Resource updateFrequency = datasetAccess.getMetadata().getUpdateFrequency();
                String label = PortalUtils.getLabel(updateFrequency.getName(), datasetAccess.getLang(), datasetAccess.getLangDefault());
                value.append(label).append(SPACE).append(LEFT_PARENTHESES).append(updateFrequency.getId()).append(RIGHT_PARENTHESES);
            } else {
                value.append(nextVersion.name());
            }
        }

        PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.UPDATE_FREQUENCY).withValue(value.toString()).build();
        writeLine(printWriter, line);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeTableId(PrintWriter printWriter) throws MetamacException {
        PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.TABLEID).withValue(datasetAccess.getUrn()).build();
        writeLine(printWriter, line);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeDecimals(PrintWriter printWriter) throws MetamacException {
        // Filled with the same value of showdecimals
        PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.DECIMALS).withValue(datasetAccess.getRelatedDsd().getShowDecimals()).build();
        writeLine(printWriter, line);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeShowDecimals(PrintWriter printWriter) throws MetamacException {
        showDecimals = datasetAccess.getRelatedDsd().getShowDecimals();

        if (showDecimals != null) {
            // Check if correction for Precisions is needed: If precision is more less than showdecimals, then showdecimals is changed to precission value
            if (datasetAccess.existsContVariable()) {
                // Measure dimension Has an enumerated representation
                for (EnumeratedDimensionValue dimensionValue : datasetAccess.getSelectedValuesForMeasureDimension()) {
                    if (dimensionValue.getShowDecimalsPrecision() != null && showDecimals > dimensionValue.getShowDecimalsPrecision()) {
                        showDecimals = dimensionValue.getShowDecimalsPrecision();
                    }
                }
            }

            PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.SHOWDECIMALS).withValue(showDecimals).build();
            writeLine(printWriter, line);
        }
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeMatrix(PrintWriter printWriter) throws MetamacException {
        PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.MATRIX).withValue(datasetAccess.getUniqueId()).build();
        writeLine(printWriter, line);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeAggregallowed(PrintWriter printWriter) throws MetamacException {
        // Always fixed to NO
        PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.AGGREGALLOWED).withValue("NO").build();
        writeLine(printWriter, line);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeAutopen(PrintWriter printWriter) throws MetamacException {
        PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.AUTOPEN).withValue(datasetAccess.getRelatedDsd().getAutoOpen()).build();
        writeLine(printWriter, line);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeSubjectAreaAndSubjectCode(PrintWriter printWriter) throws MetamacException {
        Resources subjectAreas = datasetAccess.getMetadata().getSubjectAreas();

        if (subjectAreas == null) {
            writeLocalisedLine(printWriter, PxKeysEnum.SUBJECT_AREA, Collections.emptyList(), datasetAccess.getDataset().getName());
            PxLineContainer subjectCodeLine = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.SUBJECT_CODE).withValue(datasetAccess.getDataset().getId()).isMultiline(false).build();
            writeLine(printWriter, subjectCodeLine);
            return;
        }

        InternationalString valueNameInternationalString = null;
        StringBuilder valueCode = new StringBuilder();
        for (Iterator<Resource> iterator = subjectAreas.getResources().iterator(); iterator.hasNext();) {
            Resource subjectArea = iterator.next();
            valueNameInternationalString = PortalUtils.concatenateInternationalString(valueNameInternationalString, subjectArea.getName());
            valueCode.append(subjectArea.getId());
            if (iterator.hasNext()) {
                valueCode.append(". ");
            }
        }

        writeLocalisedLine(printWriter, PxKeysEnum.SUBJECT_AREA, Collections.emptyList(), valueNameInternationalString);

        PxLineContainer subjectCodeLine = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.SUBJECT_CODE).withValue(valueCode.toString()).isMultiline(false).build();
        writeLine(printWriter, subjectCodeLine);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeCopyRight(PrintWriter printWriter) throws MetamacException {
        PxLineContainer pxLineContainer = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.COPYRIGHT).withValue(datasetAccess.getMetadata().getCopyrightDate() != null).build();
        writeLine(printWriter, pxLineContainer);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeDescription(PrintWriter printWriter) throws MetamacException {
        writeLocalisedLine(printWriter, PxKeysEnum.DESCRIPTION, Collections.emptyList(), datasetAccess.getDescription() != null ? datasetAccess.getDescription() : datasetAccess.getName());
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeTitle(PrintWriter printWriter) throws MetamacException {
        writeLocalisedLine(printWriter, PxKeysEnum.TITLE, Collections.emptyList(), datasetAccess.getName());
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeDescriptionDefault(PrintWriter printWriter) throws MetamacException {
        PxLineContainer descriptionDefaultPxLineContainer = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.DESCRIPTIONDEFAULT).withValue(Boolean.TRUE).build();
        writeLine(printWriter, descriptionDefaultPxLineContainer);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeContents(PrintWriter printWriter) throws MetamacException {
        InternationalString value = null;
        if (datasetAccess.existsContVariable()) {
            // Exists the CONTVARIABLE in the PX
            Dimension measureDimension = datasetAccess.getMeasureDimension();
            value = measureDimension.getName(); // Concept associated to measure
        } else {
            Attribute measureAttribute = datasetAccess.getMeasureAttribute();
            if (measureAttribute != null) {
                // By Metamac Constraints, the measure attribute has dataset attachment and enumerated representation.
                String[] attributeValues = datasetAccess.getAttributeValues(measureAttribute.getId()); // Enumerated representation
                if (attributeValues == null || attributeValues.length != 1) {
                    throw new RuntimeException("No instances of measure attribute type in the dataset. This is a Metamac error.");
                }
                value = datasetAccess.getAttributeValue(measureAttribute.getId(), attributeValues[0]); // Translated enumerated representation, One value = Dataset Attachment
            } else {
                throw new RuntimeException("No attribute of type Measure or Measure Dimension found in the dataset. This is a Metamac error.");
            }
        }
        writeLocalisedLine(printWriter, PxKeysEnum.CONTENTS, Collections.emptyList(), value);
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeUnits(PrintWriter printWriter) throws MetamacException {
        if (datasetAccess.existsContVariable()) {
            // The general UNITS (not indexed) is mandatory or will launch a "missing units" on pc axis 2008
            writeLocalisedLineAllowEmptyValues(printWriter, PxKeysEnum.UNITS, Collections.emptyList(), buildInternationalStringFromSingleCode(StringUtils.EMPTY, languages));

            // Indexed to ContVariable Values
            for (EnumeratedDimensionValue dimensionValue : datasetAccess.getSelectedValuesForMeasureDimension()) {
                final InternationalString unit = datasetAccess.extractUnitCode(dimensionValue);
                if (unit != null) {
                    writeLocalisedLine(printWriter, PxKeysEnum.UNITS, dimensionValue.getName(), unit);
                }
            }
        } else {
            Attribute measureAttribute = datasetAccess.getMeasureAttribute();
            if (measureAttribute != null) {
                // By Metamac Constraints, the measure attribute has dataset attachment and enumerated representation.
                // Quantity
                String[] attributeValuesFromData = datasetAccess.getAttributeValues(measureAttribute.getId()); // Enumerated representation
                if (attributeValuesFromData == null || attributeValuesFromData.length != 1) {
                    throw new RuntimeException("No instances of measure attribute type in the dataset. This is a Metamac error.");
                }
                AttributeValues attributeValuesFromMetadata = measureAttribute.getAttributeValues();
                boolean writeEmptyUnit = true;
                if (attributeValuesFromMetadata instanceof EnumeratedAttributeValues) {
                    // En este caso sólo debería de tener un único Valor la representación ENUMERADA
                    List<EnumeratedAttributeValue> metadataAttributeValues = ((EnumeratedAttributeValues) attributeValuesFromMetadata).getValues();
                    for (EnumeratedAttributeValue attributeValue : metadataAttributeValues) {
                        if (attributeValue.getId().equals(attributeValuesFromData[0])) {
                            final InternationalString unit = datasetAccess.extractUnitCode(attributeValue);
                            if (unit != null) {
                                writeLocalisedLine(printWriter, PxKeysEnum.UNITS, Collections.emptyList(), unit);
                                writeEmptyUnit = false;
                            }
                        }
                    }
                }
                if (writeEmptyUnit) {
                    writeLocalisedLineAllowEmptyValues(printWriter, PxKeysEnum.UNITS, Collections.emptyList(), buildInternationalStringFromSingleCode(StringUtils.EMPTY, languages));
                }
            } else {
                throw new RuntimeException("No attribute of type Measure or Measure Dimension found in the dataset. This is a Metamac error.");
            }
        }
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeStub(PrintWriter printWriter) throws MetamacException {
        List<InternationalString> stubInternationalString = new LinkedList<>();
        for (DatasetSelectionDimension dimension : datasetSelection.getLeftDimensions()) {
            stubInternationalString.add(datasetAccess.getDimensionsMetadataMap().get(dimension.getId()).getName());
        }

        if (stubInternationalString.isEmpty()) {
            logger.debug("STUB is NOT present. At least one of the keywords STUB or HEADING must be included");
        } else {
            logger.debug("STUB is present");
            writeLocalisedLineAllowMixedTranslation(printWriter, PxKeysEnum.STUB, Collections.emptyList(), stubInternationalString);
        }
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeHeading(PrintWriter printWriter) throws MetamacException {
        List<InternationalString> headingInternationalString = new LinkedList<>();
        for (DatasetSelectionDimension dimension : datasetSelection.getTopDimensions()) {
            headingInternationalString.add(datasetAccess.getDimensionsMetadataMap().get(dimension.getId()).getName());
        }

        if (headingInternationalString.isEmpty()) {
            logger.debug("HEADING is NOT present. At least one of the keywords STUB or HEADING must be included");
        } else {
            logger.debug("HEADING is present");
            writeLocalisedLineAllowMixedTranslation(printWriter, PxKeysEnum.HEADING, Collections.emptyList(), headingInternationalString);
        }
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeContVariable(PrintWriter printWriter) throws MetamacException {
        if (datasetAccess.existsContVariable()) {
            writeLocalisedLine(printWriter, PxKeysEnum.CONTVARIABLE, Collections.emptyList(), datasetAccess.getMeasureDimension().getName());
        }
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeValues(PrintWriter printWriter) throws MetamacException {
        // For dimensions with enumerated representation, the values are the labels of each code
        // For dimensions with non enumerated representation, the values are the text
        for (String dimensionId : getPxDimensionsDatasetOrdered()) {
            List<InternationalString> dimensionValuesLabels = new ArrayList<>();

            for (String dimensionValueId : datasetSelection.getDimension(dimensionId).getSelectedDimensionValues()) {
                dimensionValuesLabels.add(datasetAccess.getDimensionValueLabel(dimensionId, dimensionValueId));
            }

            writeLocalisedLineAllowMixedTranslation(printWriter, PxKeysEnum.VALUES, Arrays.asList(datasetAccess.getDimensionsMetadataMap().get(dimensionId).getName()), dimensionValuesLabels);
        }
    }

    private List<String> computeLabelValuesForLang(String lang, List<InternationalString> indexValues, boolean fillValuesWithDefaultLanguage) {
        if (indexValues == null || indexValues.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> result = new LinkedList<>();

        for (InternationalString indexValue : indexValues) {
            String label = null;
            if (fillValuesWithDefaultLanguage) {
                label = PortalUtils.getLabel(indexValue, lang, datasetAccess.getLangDefault());
            } else {
                label = PortalUtils.getLabel(indexValue, lang);
            }
            if (StringUtils.isEmpty(label)) {
                return Collections.emptyList();
            }
            result.add(label);
        }

        return result;
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeCodes(PrintWriter printWriter) throws MetamacException {
        for (String dimensionId : getPxDimensionsDatasetOrdered()) {
            // If is a dimension with non enumerated representation, skip the codes
            if (!PortalUtils.isDimensionWithEnumeratedRepresentation(datasetAccess.getDimensionsMetadataMap().get(dimensionId))) {
                continue;
            }
            // Metamac doesn't support languages in codes
            // @formatter:off
            PxLineContainer pxLineContainer = PxLineContainerBuilder.pxLineContainer()
                    .withPxKey(PxKeysEnum.CODES)
                    .withIndexedValue(Arrays.asList(datasetAccess.getDimensionLabelDefaultLocale(dimensionId)))
                    .withValue(datasetSelection.getDimension(dimensionId).getSelectedDimensionValues())
                    .build();
            // @formatter:on
            writeLine(printWriter, pxLineContainer);
        }
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeLastUpdated(PrintWriter printWriter) throws MetamacException {
        if (datasetAccess.existsContVariable()) {
            // Indexed to ContVariable Values
            for (EnumeratedDimensionValue dimensionValue : datasetAccess.getSelectedValuesForMeasureDimension()) {
                // Metamac doesn't support languages in Last Updated
                InternationalString value = createInternationalStringWithDefaultValue(dateToString(datasetAccess.getMetadata().getLastUpdate()));
                writeLocalisedLine(printWriter, PxKeysEnum.LAST_UPDATED, dimensionValue.getName(), value);
            }
        } else {
            PxLineContainer pxLineContainer = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.LAST_UPDATED).withValue(datasetAccess.getMetadata().getLastUpdate()).build();
            writeLine(printWriter, pxLineContainer);
        }
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeContact(PrintWriter printWriter) throws MetamacException {
        // Calculate International String Contact
        Resources publishers = datasetAccess.getMetadata().getPublishers();
        InternationalString value = null;
        for (Resource publisher : publishers.getResources()) {
            value = PortalUtils.concatenateInternationalString(value, publisher.getName());
        }

        if (datasetAccess.existsContVariable()) {
            // Indexed to ContVariable Values
            for (EnumeratedDimensionValue dimensionValue : datasetAccess.getSelectedValuesForMeasureDimension()) {
                writeLocalisedLine(printWriter, PxKeysEnum.CONTACT, dimensionValue.getName(), value);
            }
        } else {
            writeLocalisedLine(printWriter, PxKeysEnum.CONTACT, Collections.emptyList(), value);
        }
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeSource(PrintWriter printWriter) throws MetamacException {
        writeFieldResourceName(printWriter, PxKeysEnum.SOURCE, datasetAccess.getMetadata().getMaintainer());
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeSurvey(PrintWriter printWriter) throws MetamacException {
        if (datasetAccess.getMetadata().getStatisticalOperation() == null) {
            return;
        }

        // Value = TITLE statistical operation (CODE statistical operation)
        InternationalString statisticalOperationName = datasetAccess.getMetadata().getStatisticalOperation().getName();
        String statisticalOperationCode = extractStatisticalOperationCodeFromLink(datasetAccess.getMetadata().getStatisticalOperation().getSelfLink());

        String defaultLang = datasetAccess.getLangDefault();
        Map<Integer, PxLineContainer> linesMap = new TreeMap<Integer, PxLineContainer>();
        for (LocalisedString localisedString : statisticalOperationName.getTexts()) {
            if (filterLanguagesToApply(localisedString.getLang())) {
                String lang = localisedString.getLang();
                StringBuilder valueBuilder = new StringBuilder(localisedString.getValue());
                if (!StringUtils.isEmpty(statisticalOperationCode)) {
                    valueBuilder.append(SPACE).append(LEFT_PARENTHESES).append(statisticalOperationCode).append(RIGHT_PARENTHESES);
                }
                if (defaultLang.equals(lang)) {
                    PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.SURVEY).withValue(valueBuilder.toString()).build();
                    // First, default lang
                    linesMap.put(0, line);
                } else {
                    PxLineContainer line = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.SURVEY).withValue(valueBuilder.toString()).withLang(lang).build();
                    linesMap.put(languageOrder.get(lang), line);
                }
            }
        }
    }

    /**
     * @param printWriter
     * @throws MetamacException
     */
    private void writeLink(PrintWriter printWriter) throws MetamacException {
        // Always empty
        PxLineContainer pxLineContainer = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.LINK).withValue(StringUtils.EMPTY).build();
        writeLine(printWriter, pxLineContainer);
    }

    /**
     * If exist more than one attribute for a same key (example: pair dimensionId-dimensionValueId for VALUENOTE) concat values with <br/>
     * (# in PX)
     *
     * @throws MetamacException
     */
    private void writeAttributes(PrintWriter printWriter) throws MetamacException {
        List<Attribute> attributes = datasetAccess.getAttributesMetadata();
        if (CollectionUtils.isEmpty(attributes)) {
            return;
        }
        writeAttributesNote(printWriter, attributes);
        writeAttributesValueNote(printWriter, attributes);
        writeAttributesCellNote(printWriter, attributes);
    }

    private void writePrecision(PrintWriter printWriter) throws MetamacException {
        if (datasetAccess.existsContVariable()) {
            // Measure dimension Has an enumerated representation
            for (EnumeratedDimensionValue dimensionValue : datasetAccess.getSelectedValuesForMeasureDimension()) {
                String variableLabel = PortalUtils.getLabel(datasetAccess.getMeasureDimension().getName(), datasetAccess.getLangDefault());
                String valueLabel = PortalUtils.getLabel(dimensionValue.getName(), datasetAccess.getLangDefault());
                // @formatter:off
                PxLineContainer pxLineContainer = PxLineContainerBuilder.pxLineContainer()
                        .withPxKey(PxKeysEnum.PRECISION)
                        .withIndexedValue(Arrays.asList(variableLabel, valueLabel))
                        .withValue(dimensionValue.getShowDecimalsPrecision())
                        .build();
                writeLine(printWriter, pxLineContainer);
                // @formatter:on
            }
        } else {
            return; // Nothing
        }
    }

    /**
     * Builds NOTE and NOTEX attributes: attributes with dataset attachment level
     *
     * @throws MetamacException
     */
    private void writeAttributesNote(PrintWriter printWriter, List<Attribute> attributes) throws MetamacException {
        StringBuilder notex = new StringBuilder();
        StringBuilder note = new StringBuilder();
        for (Attribute attribute : attributes) {
            if (!AttributeAttachmentLevelType.DATASET.equals(attribute.getAttachmentLevel())) {
                continue;
            }
            String attributeId = attribute.getId();
            String[] attributeValues = datasetAccess.getAttributeValues(attributeId);
            if (attributeValues == null) {
                continue;
            }
            StringBuilder value = PxKeysEnum.NOTEX.getKeyword().equals(attributeId) ? notex : note;
            addAttributeValue(value, attributeValues[0]);
        }
        if (notex.length() != 0) {
            PxLineContainer pxLineContainer = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.NOTEX).withValue(notex.toString()).build();
            writeLine(printWriter, pxLineContainer);
        }
        if (note.length() != 0) {
            PxLineContainer pxLineContainer = PxLineContainerBuilder.pxLineContainer().withPxKey(PxKeysEnum.NOTE).withValue(note.toString()).build();
            writeLine(printWriter, pxLineContainer);
        }
    }

    /**
     * Builds VALUENOTE and VALUENOTEX attributes: attributes with dimension attachment level but only one dimension
     *
     * @throws MetamacException
     */
    private void writeAttributesValueNote(PrintWriter printWriter, List<Attribute> attributes) throws MetamacException {
        // Attribute values indexed by dimensionId and dimensionValueId
        Map<String, Map<String, StringBuilder>> valueNotex = new HashMap<String, Map<String, StringBuilder>>();
        Map<String, Map<String, StringBuilder>> valueNote = new HashMap<String, Map<String, StringBuilder>>();

        // Builds attribute values
        for (Attribute attribute : attributes) {
            if (!AttributeAttachmentLevelType.DIMENSION.equals(attribute.getAttachmentLevel()) || attribute.getDimensions().getDimensions().size() != 1) {
                continue;
            }
            String attributeId = attribute.getId();
            String[] attributeValues = datasetAccess.getAttributeValues(attributeId);
            if (attributeValues == null) {
                continue;
            }
            String dimensionId = attribute.getDimensions().getDimensions().get(0).getDimensionId();
            Map<String, Map<String, StringBuilder>> attributeValuesByDimensionId = PxKeysEnum.VALUENOTEX.getKeyword().equals(attributeId) ? valueNotex : valueNote;
            if (!attributeValuesByDimensionId.containsKey(dimensionId)) {
                attributeValuesByDimensionId.put(dimensionId, new HashMap<String, StringBuilder>());
            }
            List<String> dimensionValuesId = datasetAccess.getDimensionValuesOrderedForData(dimensionId);
            List<String> selectedDimensionValuesId = datasetSelection.getDimension(dimensionId).getSelectedDimensionValues();
            Map<String, StringBuilder> attributeValuesByDimensionValueId = attributeValuesByDimensionId.get(dimensionId);
            for (int i = 0; i < dimensionValuesId.size(); i++) {
                String dimensionValueId = dimensionValuesId.get(i);
                if (!selectedDimensionValuesId.contains(dimensionValueId)) {
                    continue;
                }
                String attributeValue = attributeValues[i];
                if (StringUtils.isEmpty(attributeValue)) {
                    continue;
                }
                if (!attributeValuesByDimensionValueId.containsKey(dimensionValueId)) {
                    attributeValuesByDimensionValueId.put(dimensionValueId, new StringBuilder());
                }
                StringBuilder value = attributeValuesByDimensionValueId.get(dimensionValueId);
                attributeValue = datasetAccess.applyLabelVisualizationModeForAttributeValue(attributeId, attributeValue);
                addAttributeValue(value, attributeValue);
            }
        }

        // Print in stream
        writeAttributeValueNoteField(printWriter, PxKeysEnum.VALUENOTEX, valueNotex);
        writeAttributeValueNoteField(printWriter, PxKeysEnum.VALUENOTE, valueNote);
    }

    private void writeAttributeValueNoteField(PrintWriter printWriter, PxKeysEnum pxKey, Map<String, Map<String, StringBuilder>> valueNote) throws MetamacException {
        for (String dimensionId : datasetAccess.getDimensionsOrderedForData()) {
            if (valueNote.containsKey(dimensionId)) {
                List<String> dimensionValuesId = datasetAccess.getDimensionValuesOrderedForData(dimensionId);
                for (String dimensionValueId : dimensionValuesId) {
                    if (valueNote.get(dimensionId).containsKey(dimensionValueId)) {
                        String dimensionValueLabel = PortalUtils.getLabel(datasetAccess.getDimensionValueLabel(dimensionId, dimensionValueId), datasetAccess.getLangDefault());
                        // Localised Value Note is not supported
                        // @formatter:off
                        PxLineContainer pxLineContainer = PxLineContainerBuilder.pxLineContainer()
                                .withPxKey(pxKey)
                                .withValue(valueNote.get(dimensionId).get(dimensionValueId).toString())
                                .withIndexedValue(Arrays.asList(datasetAccess.getDimensionLabelDefaultLocale(dimensionId), dimensionValueLabel)).build();
                        // @formatter:on
                        writeLine(printWriter, pxLineContainer);
                    }
                }
            }
        }
    }

    /**
     * Builds CELLNOTE and CELLNOTEX attributes: attributes with observation attachment level or dimension attachment level but more than one dimension
     *
     * @throws MetamacException
     */
    private void writeAttributesCellNote(PrintWriter printWriter, List<Attribute> attributes) throws MetamacException {

        // Attribute values indexed by all dimension values of attribute. Dimensions ordered: First stub and then heading
        Map<String, StringBuilder> cellNotex = new HashMap<String, StringBuilder>();
        Map<String, StringBuilder> cellNote = new HashMap<String, StringBuilder>();
        List<String> allDimensionsDatasetOrderedForPx = getPxDimensionsDatasetOrdered();

        // Builds attribute values
        for (Attribute attribute : attributes) {
            if (!AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attribute.getAttachmentLevel())
                    && !(AttributeAttachmentLevelType.DIMENSION.equals(attribute.getAttachmentLevel()) && attribute.getDimensions().getDimensions().size() > 1)) {
                continue;
            }
            String attributeId = attribute.getId();
            String[] attributeValues = datasetAccess.getAttributeValues(attributeId);
            if (attributeValues == null) {
                continue;
            }

            Map<String, StringBuilder> cellNoteToAttribute = PxKeysEnum.CELLNOTEX.getKeyword().equals(attributeId) ? cellNotex : cellNote;
            List<String> dimensionsAttributeOrderedForData = datasetAccess.getDimensionsAttributeOrderedForData(attribute);
            writeAttributeCellNote(attributeId, attributeValues, dimensionsAttributeOrderedForData, allDimensionsDatasetOrderedForPx, cellNoteToAttribute);
        }

        // Print in stream
        writeAttributeCellNoteField(printWriter, PxKeysEnum.CELLNOTEX, cellNotex);
        writeAttributeCellNoteField(printWriter, PxKeysEnum.CELLNOTE, cellNote);
    }

    private void writeAttributeCellNote(String attributeId, String[] attributeValues, List<String> dimensionsAttributeOrderedForData, List<String> dimensionsDatasetOrderedForPx,
            Map<String, StringBuilder> cellNote) {

        Stack<DataOrderingStackElement> stack = new Stack<DataOrderingStackElement>();
        stack.push(new DataOrderingStackElement(null, -1, null));
        Map<String, String> dimensionValuesForAttributeValue = new HashMap<String, String>(dimensionsAttributeOrderedForData.size());

        int dimensionLastPosition = dimensionsAttributeOrderedForData.size() - 1;
        int attributeValueIndex = 0;
        while (stack.size() > 0) {
            DataOrderingStackElement elem = stack.pop();
            int dimensionPosition = elem.getDimensionPosition();
            String dimensionCodeId = elem.getDimensionCodeId();

            if (dimensionPosition != -1) {
                String dimensionId = elem.getDimensionId();
                dimensionValuesForAttributeValue.put(dimensionId, dimensionCodeId);
            }

            if (dimensionPosition == dimensionLastPosition) {
                // We have all dimensions here
                String attributeValue = attributeValues[attributeValueIndex++];
                if (!StringUtils.isEmpty(attributeValue) && allDimensionValuesAreSelected(dimensionsDatasetOrderedForPx, dimensionValuesForAttributeValue)) {
                    StringBuilder key = new StringBuilder();
                    key.append(QUOTE);
                    Iterator<String> iterator = dimensionsDatasetOrderedForPx.iterator();
                    while (iterator.hasNext()) {
                        String dimensionId = iterator.next();
                        if (dimensionValuesForAttributeValue.containsKey(dimensionId)) {
                            String dimensionValueId = dimensionValuesForAttributeValue.get(dimensionId);
                            key.append(PortalUtils.getLabel(datasetAccess.getDimensionValueLabel(dimensionId, dimensionValueId), datasetAccess.getLangDefault()));
                        } else {
                            key.append("*");
                        }
                        if (iterator.hasNext()) {
                            key.append(QUOTE + COMMA + QUOTE);
                        }
                    }
                    key.append(QUOTE);
                    String keyString = key.toString();
                    StringBuilder value = cellNote.get(keyString);
                    if (value == null) {
                        value = new StringBuilder();
                        cellNote.put(keyString, value);
                    }
                    attributeValue = datasetAccess.applyLabelVisualizationModeForAttributeValue(attributeId, attributeValue);
                    addAttributeValue(value, attributeValue);
                }
            } else {
                String dimensionId = dimensionsAttributeOrderedForData.get(dimensionPosition + 1);
                List<String> dimensionValues = datasetAccess.getDimensionValuesOrderedForData(dimensionId);
                for (int i = dimensionValues.size() - 1; i >= 0; i--) {
                    DataOrderingStackElement temp = new DataOrderingStackElement(dimensionId, dimensionPosition + 1, dimensionValues.get(i));
                    stack.push(temp);
                }
            }
        }
    }

    private boolean allDimensionValuesAreSelected(List<String> dimensionsDatasetOrderedForPx, Map<String, String> dimensionValuesForAttributeValue) {
        for (String dimensionId : dimensionsDatasetOrderedForPx) {
            if (dimensionValuesForAttributeValue.containsKey(dimensionId)) {
                String dimensionValueId = dimensionValuesForAttributeValue.get(dimensionId);
                if (!datasetSelection.getDimension(dimensionId).getSelectedDimensionValues().contains(dimensionValueId)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void writeAttributeCellNoteField(PrintWriter printWriter, PxKeysEnum pxKey, Map<String, StringBuilder> cellNote) throws MetamacException {
        if (cellNote.size() == 0) {
            return;
        }
        // Get keys ordered to obtain same result to same dataset
        List<String> keys = new ArrayList<String>(cellNote.keySet());
        Collections.sort(keys);

        for (String key : keys) {
            // Localised is not supported for Attribute Cell
            // @formatter:off
            PxLineContainer pxLineContainer = PxLineContainerBuilder.pxLineContainer()
                    .withPxKey(pxKey)
                    .withValue(cellNote.get(key).toString())
                    .withIndexedValue(Arrays.asList(StringUtils.removeStart(StringUtils.removeEnd(key, "\""), "\"").split("\",\"")))
                    .build();
            // @formatter:on
            writeLine(printWriter, pxLineContainer);
        }
    }

    private void writeData(PrintWriter printWriter) {
        printWriter.println(PxKeysEnum.DATA.getKeyword() + EQUALS);
        for (int i = 0; i < datasetSelection.getRows(); i++) {
            for (int j = 0; j < datasetSelection.getColumns(); j++) {
                Map<String, String> permutationAtCell = datasetSelection.permutationAtCell(i, j);
                String observation = datasetAccess.observationAtPermutation(permutationAtCell);
                if (i == 0 && j == 0) {
                    writeObservation(printWriter, StringUtils.EMPTY, observation);
                } else {
                    writeObservation(printWriter, SPACE, observation);
                }
            }
        }
        printWriter.print(SEMICOLON + SPACE);
    }

    private void writeObservation(PrintWriter printWriter, String character, String observation) {
        if (!StringUtils.isBlank(observation)) {
            printWriter.print(character + observation);
        } else {
            printWriter.print(character + QUOTE + "." + QUOTE);
        }
    }

    /**
     * Write one line, language dependent or not
     *
     * @param printWriter
     * @param pxLineContainer
     * @throws MetamacException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void writeLine(PrintWriter printWriter, PxLineContainer pxLineContainer) throws MetamacException {
        // General constraints
        checkIfKeywordIsMandatory(pxLineContainer);

        if (pxLineContainer.getValue() == null) {
            return;
        }
        StringBuilder line = new StringBuilder();

        // Left side
        line.append(pxLineContainer.getPxKey().getKeyword());

        if (pxLineContainer.getPxKey().isLanguageDependent() && pxLineContainer.getLang() != null) {
            line.append(LEFT_BRACE).append(pxLineContainer.getLang()).append(RIGHT_BRACE);
        }

        if (pxLineContainer.getIndexedValue() != null && pxLineContainer.getIndexedValue().size() > 0) {
            line.append(LEFT_PARENTHESES).append(listToValue(pxLineContainer.getIndexedValue())).append(RIGHT_PARENTHESES);
        }

        line.append(EQUALS);

        // Right side
        if (pxLineContainer.getValue() instanceof String) {
            if (pxLineContainer.isMultiline()) {
                line.append(QUOTE).append(formatMultilineValue((String) pxLineContainer.getValue())).append(QUOTE);
            } else {
                line.append(QUOTE).append(formatValue((String) pxLineContainer.getValue())).append(QUOTE);
            }
        } else if (pxLineContainer.getValue() instanceof Integer) {
            line.append(String.valueOf(pxLineContainer.getValue()));
        } else if (pxLineContainer.getValue() instanceof Boolean) {
            line.append((Boolean) pxLineContainer.getValue() ? "YES" : "NO");
        } else if (pxLineContainer.getValue() instanceof Date) {
            line.append(QUOTE).append(dateToString((Date) pxLineContainer.getValue())).append(QUOTE);
        } else if (pxLineContainer.getValue() instanceof List) { // List<String>
            line.append(listToValueRight((List) pxLineContainer.getValue(), line.length(), 1));
        } else {
            throw new IllegalArgumentException("Type unsupported: " + pxLineContainer.getValue().getClass().getCanonicalName());
        }

        line.append(SEMICOLON);

        printWriter.println(line);
    }

    private void writeLocalisedLine(PrintWriter printWriter, PxKeysEnum pxKey, InternationalString indexValue, InternationalString value) throws MetamacException {
        writeLocalisedLine(printWriter, pxKey, Arrays.asList(indexValue), Arrays.asList(value));
    }

    private void writeLocalisedLine(PrintWriter printWriter, PxKeysEnum pxKey, List<InternationalString> indexValues, InternationalString value) throws MetamacException {
        writeLocalisedLine(printWriter, pxKey, indexValues, Arrays.asList(value));
    }

    private void writeLocalisedLine(PrintWriter printWriter, PxKeysEnum pxKey, InternationalString indexValue, List<InternationalString> values) throws MetamacException {
        writeLocalisedLine(printWriter, pxKey, Arrays.asList(indexValue), values);
    }

    private void writeLocalisedLine(PrintWriter printWriter, PxKeysEnum pxKey, List<InternationalString> indexValues, List<InternationalString> values) throws MetamacException {
        final boolean allowEmptyValues = false;
        final boolean fillValuesWithDefaultLanguage = false;
        writeLocalisedLine(printWriter, pxKey, indexValues, values, allowEmptyValues, fillValuesWithDefaultLanguage);
    }

    private void writeLocalisedLineAllowEmptyValues(PrintWriter printWriter, PxKeysEnum pxKey, List<InternationalString> indexValues, InternationalString value) throws MetamacException {
        final boolean allowEmptyValues = true;
        final boolean fillValuesWithDefaultLanguage = false;
        writeLocalisedLine(printWriter, pxKey, indexValues, Arrays.asList(value), allowEmptyValues, fillValuesWithDefaultLanguage);
    }

    /* This is needed for STUB and HEADING or we won't be able to mix translated with non translated dimensions, generating a non valid PX */
    private void writeLocalisedLineAllowMixedTranslation(PrintWriter printWriter, PxKeysEnum pxKey, List<InternationalString> indexValues, List<InternationalString> values) throws MetamacException {
        final boolean allowEmptyValues = false;
        final boolean fillValuesWithDefaultLanguage = true;
        writeLocalisedLine(printWriter, pxKey, indexValues, values, allowEmptyValues, fillValuesWithDefaultLanguage);
    }

    private void writeLocalisedLine(PrintWriter printWriter, PxKeysEnum pxKey, List<InternationalString> indexValues, List<InternationalString> values, boolean allowEmptyValues,
            boolean fillValuesWithDefaultLanguage) throws MetamacException {
        if (values == null) {
            return;
        }

        String defaultLang = datasetAccess.getLangDefault();
        List<String> computedIndexValuesForDefaultLang = computeLabelValuesForLang(defaultLang, indexValues, false);

        Map<Integer, PxLineContainer> linesMap = new TreeMap<Integer, PxLineContainer>();

        for (String lang : languages) {
            if (filterLanguagesToApply(lang)) {
                if (defaultLang.equals(lang)) {

                    // @formatter:off
                    PxLineContainer line = PxLineContainerBuilder.pxLineContainer()
                            .withPxKey(pxKey)
                            .withIndexedValue(computedIndexValuesForDefaultLang)
                            .withValue(computeLabelValuesForLang(lang, values, false))
                            .build();
                    // @formatter:on
                    // First, default lang
                    linesMap.put(0, line);
                } else {
                    if (values == null || values.isEmpty()) {
                        continue;
                    }

                    // If there is some missing translation in de index array, the default locale index array is used
                    List<String> computedIndexValues = computeLabelValuesForLang(lang, indexValues, false);
                    if (computedIndexValues.isEmpty()) {
                        computedIndexValues = computedIndexValuesForDefaultLang;
                    }

                    // If there is some missing translation in de values array, the current lang for the metadata is skipped
                    List<String> computedValues = computeLabelValuesForLang(lang, values, fillValuesWithDefaultLanguage);
                    if (!allowEmptyValues && computedValues.isEmpty()) {
                        continue;
                    }

                    // @formatter:off
                    PxLineContainer line = PxLineContainerBuilder.pxLineContainer()
                            .withPxKey(pxKey)
                            .withIndexedValue(computedIndexValues)
                            .withValue(computedValues)
                            .withLang(lang).build();
                    // @formatter:on
                    linesMap.put(languageOrder.get(lang), line);
                }
            }
        }

        for (Map.Entry<Integer, PxLineContainer> line : linesMap.entrySet()) {
            writeLine(printWriter, line.getValue());
        }

    }

    private String formatMultilineValue(String value) {
        if (value == null) {
            return null;
        }
        final String cleanValue = Jsoup.parse(value.replace("\"", "'")).text();
        final String[] splittedValue = splitIntoPcAxisMaxLengthSubstrings(cleanValue);
        if (cleanValue.length() >= MAX_LINE_LENGTH * 2) {
            logger.warn("This attribute is longer than " + MAX_LINE_LENGTH * 2 + " characters. This has been problematic on PcAxis-2008.", value);
        }
        return StringUtils.join(splittedValue, QUOTE + NEW_LINE + QUOTE);
    }

    private String formatValue(String value) {
        if (value == null) {
            return null;
        }
        return splitIntoPcAxisMaxLengthSubstrings(value.replace("\"", "'"))[0];
    }

    // Split into 256 character strings is mostly simple: https://stackoverflow.com/a/3761521
    // But the limits of PCAxis 2008 are not 256 characters, testing shows that it´s not exactly 256 characters but looks like it takes into account quotes, semicolon and line breaks.
    // We split into 252 characters for taking this into account
    private String[] splitIntoPcAxisMaxLengthSubstrings(String value) {
        return value.split("(?<=\\G.{252})");
    }

    private void checkIfKeywordIsMandatory(PxLineContainer pxLineContainer) throws MetamacException {
        if (pxLineContainer.getPxKey().isMandatory() && pxLineContainer.getValue() == null) {
            throw new RuntimeException("No value for mandatory PC-Axis keyword");
        }
    }

    private boolean filterLanguagesToApply(String lang) {
        // Checks if the default locale or is language in the alternatives languages of PX
        return (datasetAccess.getLangDefault().equals(lang) || languages.contains(lang));
    }

    private void writeFieldResourceName(PrintWriter printWriter, PxKeysEnum pxKey, Resource value) throws MetamacException {
        if (value == null) {
            return;
        }
        writeLocalisedLine(printWriter, pxKey, Collections.emptyList(), value.getName());
    }

    private String listToValue(List<String> sources) {
        return listToValueRight(sources, -1, -1);
    }

    /**
     * Lines of MAX 256 characters
     *
     * @param sources
     * @return
     */
    private String listToValueRight(List<String> sources, int initialOffset, int lastOffset) {
        StringBuilder target = new StringBuilder(1024);
        if (sources.isEmpty()) {
            return "\"\"";
        }

        int currentLineCharactersLength = initialOffset;
        boolean multilineAvalaible = initialOffset != -1;
        for (Iterator<String> iterator = sources.iterator(); iterator.hasNext();) {
            String source = iterator.next();
            StringBuilder valueBuilder = new StringBuilder(MAX_LINE_LENGTH);
            valueBuilder.append(QUOTE);
            valueBuilder.append(formatMultilineValue(source));
            valueBuilder.append(QUOTE);
            if (iterator.hasNext()) {
                valueBuilder.append(COMMA);
            }

            if (multilineAvalaible && (currentLineCharactersLength + valueBuilder.length() + lastOffset > MAX_LINE_LENGTH)) {
                target.append(NEW_LINE);
                currentLineCharactersLength = valueBuilder.length();
            } else {
                currentLineCharactersLength += valueBuilder.length();
            }
            target.append(valueBuilder);
        }
        return target.toString();
    }

    private List<String> getPxDimensionsDatasetOrdered() {
        List<String> dimensionsOrderedToAttributes = new ArrayList<String>();
        for (DatasetSelectionDimension dimension : datasetSelection.getLeftDimensions()) {
            dimensionsOrderedToAttributes.add(dimension.getId());
        }
        for (DatasetSelectionDimension dimension : datasetSelection.getTopDimensions()) {
            dimensionsOrderedToAttributes.add(dimension.getId());
        }
        return dimensionsOrderedToAttributes;
    }

    private void addAttributeValue(StringBuilder attributeGlobalValue, String attributeValue) {
        if (attributeGlobalValue.length() != 0) {
            attributeGlobalValue.append(ATTRIBUTE_LINE_SEPARATOR);
        }
        attributeValue = formatMultilineValue(attributeValue);
        attributeGlobalValue.append(attributeValue);
    }

    private String extractStatisticalOperationCodeFromLink(ResourceLink selfLink) {
        if (selfLink != null && !StringUtils.isEmpty(selfLink.getHref())) {
            String[] splitted = selfLink.getHref().split("/");
            if (splitted.length > 0) {
                return splitted[splitted.length - 1];
            }
        }

        return null;
    }

    private InternationalString createInternationalStringWithDefaultValue(String value) {
        InternationalString internationalString = new InternationalString();
        LocalisedString localisedString = new LocalisedString();
        localisedString.setLang(datasetAccess.getLangDefault());
        localisedString.setValue(value);
        internationalString.getTexts().add(localisedString);
        return internationalString;
    }

    private String dateToString(Date value) {
        return new DateTime(value).toString("yyyyMMdd HH:mm");
    }

    public static String generateMatrixFromString(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes()).substring(0, MAX_PX_MATRIX_LENGTH);
    }

}