package org.siemac.metamac.statistical.resources.core.io.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.io.FileUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataType;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdAttribute;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.core.io.domain.TemporalAttributeValues;
import org.siemac.metamac.statistical.resources.core.io.mapper.MetamacCsv2StatRepoMapper;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators.ValidateDataVersusDsd;
import org.siemac.metamac.statistical.resources.core.io.utils.CsvAttributesParser;
import org.siemac.metamac.statistical.resources.core.utils.AttributesUtils;
import org.siemac.metamac.statistical.resources.core.utils.InternationalStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.parser.csv.CsvParser;
import com.arte.statistic.parser.csv.CsvReader;
import com.arte.statistic.parser.csv.constants.CsvConstants;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceBasicDto;
import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

@Component(ManipulateCsvDataService.BEAN_ID)
public class ManipulateCsvDataServiceImpl implements ManipulateCsvDataService {

    @Autowired
    private MetamacCsv2StatRepoMapper        metamacCsv2StatRepoMapper;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @Autowired
    StatisticalResourcesServiceFacade        statisticalResourcesServiceFacade;

    private static final int                 SPLIT_DATA_FACTOR            = 5000;

    private PrintWriter                      printWriter;
    private static final String              HEADER_ID_ATTRIBUTE          = "ID_ATRIBUTO";
    private static final String              HEADER_DIMENSIONES           = "DIMENSIONES";
    private static final String              HEADER_VALORES_DIMENSIONES   = "VALORES_DIMENSION";
    private static final String              HEADER_PREFIX_VALOR_ATRIBUTO = "VALOR_ATRIBUTO";
    private static final String              HEADER_PREFIX_LANGUAGE       = "#";
    private static final String              EMPTY                        = "";
    private static final String              DIMENSION_VALUES_SEPARATOR   = ", ";

    @Override
    public void importCsv(ServiceContext ctx, File csvFile, DataStructure dataStructure, String datasetID, String dataSourceID, ValidateDataVersusDsd validateDataVersusDsd) throws Exception {
        InputStream is = null;
        try {
            // Parse Csv
            String charsetName = FileUtils.guessCharset(csvFile);
            is = new FileInputStream(csvFile);

            CsvReader csvReader = CsvParser.parseCsv(is, charsetName, CsvConstants.SEPARATOR_TAB);

            List<ObservationExtendedDto> dataDtos = new LinkedList<>();
            ObservationExtendedDto observationExtendedDto = null;

            boolean processData = true;
            while (processData) {
                for (int i = 0; i < SPLIT_DATA_FACTOR; i++) {
                    observationExtendedDto = metamacCsv2StatRepoMapper.toObservation(csvReader.next(), dataSourceID);
                    if (observationExtendedDto == null) {
                        // Insert incomplete slice
                        insertDataAndAttributes(datasetID, dataDtos, validateDataVersusDsd);
                        processData = false;
                        break;
                    }
                    dataDtos.add(observationExtendedDto);
                }
                // Insert slice
                if (processData) {
                    insertDataAndAttributes(datasetID, dataDtos, validateDataVersusDsd);
                    dataDtos.clear();
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @Override
    public void importCsvAttributes(File csvFile, DataStructure dataStructure, Map<String, List<CodeDimension>> codeDimensions, Map<String, List<ExternalItemDto>> externalItemsAttributeId,
            ServiceContext ctx, String datasetVersionUrn, List<String> validLanguages) throws Exception {
        InputStream is = null;
        try {
            // Parse Csv
            String charsetName = FileUtils.guessCharset(csvFile);
            is = new FileInputStream(csvFile);

            CsvAttributesParser csvReader = new CsvAttributesParser(is, charsetName, CsvConstants.SEPARATOR_TAB, dataStructure, validLanguages);

            List<DsdAttributeInstanceDto> dsdAttributeInstanceDtos = new ArrayList<>();

            String idAttribute = "";
            for (int i = 0; i < SPLIT_DATA_FACTOR || idAttribute != null; i++) {
                idAttribute = csvReader.nextLine(dsdAttributeInstanceDtos, codeDimensions, externalItemsAttributeId);
            }
            checkAttributeInstancesIsNotEmpty(dsdAttributeInstanceDtos);
            checkAnyErrorInTSV(csvReader);
            checkAttributeInstances(csvReader, datasetVersionUrn, dsdAttributeInstanceDtos, ctx);
            insertAttributes(ctx, datasetVersionUrn, dsdAttributeInstanceDtos);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    private void checkAttributeInstancesIsNotEmpty(List<DsdAttributeInstanceDto> dsdAttributeInstancesDtos) throws MetamacException {
        if (dsdAttributeInstancesDtos.isEmpty()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORTATION_ATTRIBUTES_FILE_EMPTY).build();
        }
    }

    private void checkAttributeInstances(CsvAttributesParser csvReader, String datasetVersionUrn, List<DsdAttributeInstanceDto> dsdAttributeInstancesDto, ServiceContext ctx) throws MetamacException {
        for (DsdAttributeInstanceDto dsdAttributeInstanceDto : dsdAttributeInstancesDto) {
            statisticalResourcesServiceFacade.checkAttributeInstance(ctx, datasetVersionUrn, dsdAttributeInstanceDto, csvReader.getExceptions());
        }
    }

    private void checkAnyErrorInTSV(CsvAttributesParser csvReader) throws MetamacException {
        if (!csvReader.getExceptions().isEmpty()) {
            throw new MetamacException(csvReader.getExceptions());
        }
    }

    private void insertAttributes(ServiceContext ctx, String datasetVersionUrn, List<DsdAttributeInstanceDto> dsdAttributeInstanceDto) throws MetamacException {
        for (DsdAttributeInstanceDto entry : dsdAttributeInstanceDto) {
            List<DsdAttributeInstanceDto> attributeInstances = new ArrayList<>();
            // to define the attribute at the dataset level we need to know if it has already been created previously
            if (entry.getCodeDimensions() == null || entry.getCodeDimensions().isEmpty()) {
                attributeInstances = statisticalResourcesServiceFacade.retrieveAttributeInstances(ctx, datasetVersionUrn, entry.getAttributeId());
            }
            if (attributeInstances.isEmpty()) {
                statisticalResourcesServiceFacade.createAttributeInstance(ctx, datasetVersionUrn, entry);
            } else {
                DsdAttributeInstanceDto attributeInstanceDto = attributeInstances.get(0);
                attributeInstanceDto.setValue(entry.getValue());
                statisticalResourcesServiceFacade.updateAttributeInstance(ctx, datasetVersionUrn, attributeInstanceDto);
            }
        }
    }

    private void insertDataAndAttributes(String datasetID, List<ObservationExtendedDto> dataDtos, ValidateDataVersusDsd validateDataVersusDsd) throws Exception {
        // Persist Observations and attributes at level observation.
        if (!dataDtos.isEmpty()) {
            validateDataVersusDsd.checkObservation(dataDtos);
            datasetRepositoriesServiceFacade.createOrUpdateObservationsExtended(datasetID, dataDtos);
        }
    }

    @Override
    public String exportCsvAttributes(DataStructure dataStructure, DatasetVersion datasetVersion, List<String> validLanguages) throws Exception {
        String[] datasetUrn = UrnUtils.splitUrnStructure(datasetVersion.getDatasetRepositoryId());
        String prefix = "attributes" + "-" + datasetUrn[0] + "-" + datasetUrn[1] + "-" + datasetUrn[2] + "-";

        try {
            File tmpFileAttributes = File.createTempFile(prefix, ".tsv");
            String fileName = tmpFileAttributes.getName();

            try (FileOutputStream outputStreamAttributes = new FileOutputStream(tmpFileAttributes)) {
                exportCreateBodyForAttributes(dataStructure, datasetVersion, outputStreamAttributes, validLanguages);
            }

            return fileName;
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.ATTRIBUTES_EXPORT_ERROR, e.getMessage());
        }
    }

    public void exportCreateBodyForAttributes(DataStructure dataStructure, DatasetVersion datasetVersion, OutputStream os, List<String> validLanguages) throws MetamacException {
        printWriter = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        List<DsdAttribute> dsdAttributes = DsdProcessor.getAttributes(dataStructure);
        try {
            validateAttributesExist(dsdAttributes);

            String header = getExportHeader(validLanguages);
            write(header);

            for (DsdAttribute dsdAttribute : dsdAttributes) {
                if (!dsdAttribute.isAttributeAtObservationLevel()) {
                    processAttribute(dsdAttribute, datasetVersion, validLanguages);
                }
            }
        } finally {
            dispose();
        }
    }

    private void validateAttributesExist(List<DsdAttribute> dsdAttributes) throws MetamacException {
        if (dsdAttributes == null || dsdAttributes.isEmpty()) {
            throw new MetamacException(ServiceExceptionType.ATTRIBUTES_EXPORT_NO_DATA_PRESENT);
        }
    }

    private void processAttribute(DsdAttribute dsdAttribute, DatasetVersion datasetVersion, List<String> validLanguages) throws MetamacException {
        String attributeId = dsdAttribute.getComponentId();

        try {
            if (dsdAttribute.getAttributeRelationship().getNone() != null) {
                processDatasetLevelAttribute(dsdAttribute, attributeId, datasetVersion, validLanguages);
            } else if (!CollectionUtils.isEmpty(dsdAttribute.getAttributeRelationship().getDimensions())) {
                processDimensionLevelAttribute(dsdAttribute, attributeId, datasetVersion, validLanguages);
            } else if (dsdAttribute.getAttributeRelationship().getGroup() != null) {
                processGroupLevelAttribute(dsdAttribute, attributeId, datasetVersion, validLanguages);
            }
        } catch (Exception e) {
            throw new MetamacException(ServiceExceptionType.ATTRIBUTES_EXPORT_ERROR_PROCESSING, attributeId, datasetVersion.getDatasetRepositoryId());
        }
    }

    private void processDatasetLevelAttribute(DsdAttribute dsdAttribute, String attributeId, DatasetVersion datasetVersion, List<String> validLanguages) throws Exception {
        TemporalAttributeValues temporalAttributeValues = prepareTemporalValuesForDatasetLevel(dsdAttribute, attributeId, datasetVersion);
        if (hasNonEmptyValues(temporalAttributeValues, validLanguages)) {
            exportWriteAttributeDimensionLine(attributeId, EMPTY, EMPTY, temporalAttributeValues, validLanguages, false);
        }
    }

    private void processDimensionLevelAttribute(DsdAttribute dsdAttribute, String attributeId, DatasetVersion datasetVersion, List<String> validLanguages) throws Exception {
        List<AttributeInstanceDto> attributeInstances = datasetRepositoriesServiceFacade.findAttributesInstancesWithDimensionAttachmentLevel(datasetVersion.getDatasetRepositoryId(), attributeId,
                null);

        for (AttributeInstanceDto attributeInstance : attributeInstances) {
            TemporalAttributeValues temporalAttributeValues = prepareTemporalValues(dsdAttribute, attributeInstance);
            if (hasNonEmptyValues(temporalAttributeValues, validLanguages)) {
                writeAttributeInstanceForDimension(attributeId, attributeInstance, temporalAttributeValues, validLanguages);
            }
        }
    }

    private void processGroupLevelAttribute(DsdAttribute dsdAttribute, String attributeId, DatasetVersion datasetVersion, List<String> validLanguages) throws Exception {
        List<AttributeInstanceDto> attributeInstances = datasetRepositoriesServiceFacade.findAttributesInstancesWithDimensionAttachmentLevel(datasetVersion.getDatasetRepositoryId(), attributeId,
                null);

        for (AttributeInstanceDto attributeInstance : attributeInstances) {
            TemporalAttributeValues temporalAttributeValues = prepareTemporalValues(dsdAttribute, attributeInstance);
            if (hasNonEmptyValues(temporalAttributeValues, validLanguages)) {
                writeAttributeInstanceForGroup(attributeId, attributeInstance, temporalAttributeValues, validLanguages);
            }
        }
    }

    private TemporalAttributeValues prepareTemporalValuesForDatasetLevel(DsdAttribute dsdAttribute, String attributeId, DatasetVersion datasetVersion) throws Exception {
        TemporalAttributeValues temporalAttributeValues = new TemporalAttributeValues();

        if (isTextFormatAttributeMultilingual(dsdAttribute)) {
            temporalAttributeValues.setInternationalStringValues(datasetRepositoriesServiceFacade.findAttributeInstancesValues(datasetVersion.getDatasetRepositoryId(), attributeId));
            temporalAttributeValues.setMultilingualValue(true);
        } else {
            String value = toDataAttributeWithDatasetAttachmentLevel(datasetVersion.getDatasetRepositoryId(), attributeId);
            List<String> values = new ArrayList<>();
            values.add(value);
            temporalAttributeValues.setValues(values);
        }

        return temporalAttributeValues;
    }

    private TemporalAttributeValues prepareTemporalValues(DsdAttribute dsdAttribute, AttributeInstanceDto attributeInstance) {
        TemporalAttributeValues temporalAttributeValues = new TemporalAttributeValues();

        if (isTextFormatAttributeMultilingual(dsdAttribute)) {
            if (attributeInstance != null) {
                temporalAttributeValues.setInternationalStringValue(InternationalStringUtils.copy(attributeInstance.getValue(), true));
            }
            temporalAttributeValues.setMultilingualValue(true);
        } else {
            String value = toAttributeInstanceValueToData(attributeInstance);
            List<String> values = new ArrayList<>();
            values.add(value);
            temporalAttributeValues.setValues(values);
        }

        return temporalAttributeValues;
    }

    private void writeAttributeInstanceForDimension(String attributeId, AttributeInstanceDto attributeInstance, TemporalAttributeValues temporalAttributeValues, List<String> validLanguages) {
        if (attributeInstance == null) {
            return;
        }

        Map<String, List<String>> dimensionValueByDimension = attributeInstance.getCodesByDimension();
        if (dimensionValueByDimension.isEmpty()) {
            return;
        }

        Map.Entry<String, List<String>> firstEntry = dimensionValueByDimension.entrySet().iterator().next();
        String dimensionCode = firstEntry.getKey();
        List<String> dimensionCodeValues = firstEntry.getValue();

        exportWriteAttributeDimensionLine(attributeId, dimensionCode, getDimensionValues(dimensionCodeValues), temporalAttributeValues, validLanguages, false);
    }

    private void writeAttributeInstanceForGroup(String attributeId, AttributeInstanceDto attributeInstance, TemporalAttributeValues temporalAttributeValues, List<String> validLanguages) {
        if (attributeInstance == null) {
            return;
        }

        Map<String, List<String>> dimensionValueByDimension = attributeInstance.getCodesByDimension();
        if (dimensionValueByDimension.isEmpty()) {
            return;
        }

        boolean firstDimension = true;
        String tempAttributeId = attributeId;

        for (Map.Entry<String, List<String>> entry : dimensionValueByDimension.entrySet()) {
            String dimensionCode = entry.getKey();
            List<String> dimensionCodeValues = entry.getValue();

            exportWriteAttributeDimensionLine(tempAttributeId, dimensionCode, getDimensionValues(dimensionCodeValues), temporalAttributeValues, validLanguages, firstDimension);

            if (firstDimension) {
                firstDimension = false;
                tempAttributeId = null;
            }
        }
    }

    private String getDimensionValues(List<String> dimensionCodevalues) {
        StringBuilder dimensionValue = new StringBuilder();
        if (dimensionCodevalues != null) {
            for (String dimensionCodeValue : dimensionCodevalues) {
                if (dimensionValue.length() == 0) {
                    dimensionValue.append(dimensionCodeValue);
                } else {
                    dimensionValue.append(DIMENSION_VALUES_SEPARATOR).append(dimensionCodeValue);
                }
            }
        }
        return dimensionValue.toString();
    }

    private void dispose() {
        if (printWriter != null) {
            printWriter.flush();
        }
    }

    private void exportWriteAttributeDimensionLine(String attributeId, String dimension, String dimensionValue, TemporalAttributeValues temporalAttributeValues, List<String> validLanguages,
            boolean withoutValues) {
        StringBuilder line = new StringBuilder();
        appendBaseColumns(line, attributeId, dimension, dimensionValue);

        if (withoutValues) {
            appendEmptyValues(line, validLanguages.size());
        } else if (temporalAttributeValues.isMultilingualValue()) {
            appendMultilingualValues(line, temporalAttributeValues, validLanguages);
        } else {
            appendSingleLanguageValue(line, temporalAttributeValues, validLanguages.size());
        }

        write(line.toString());
    }

    private void appendBaseColumns(StringBuilder line, String attributeId, String dimension, String dimensionValue) {
        String escapedAttributeId = attributeId == null ? EMPTY : AttributesUtils.escapeValueForTsv(attributeId);
        String escapedDimension = dimension == null ? EMPTY : AttributesUtils.escapeValueForTsv(dimension);
        String escapedDimensionValue = dimensionValue == null ? EMPTY : AttributesUtils.escapeValueForTsv(dimensionValue);
        line.append(escapedAttributeId).append(CsvConstants.SEPARATOR_TAB).append(escapedDimension).append(CsvConstants.SEPARATOR_TAB).append(escapedDimensionValue);
    }

    private void appendEmptyValues(StringBuilder line, int languageCount) {
        for (int i = 0; i < languageCount; i++) {
            line.append(CsvConstants.SEPARATOR_TAB).append(EMPTY);
        }
    }

    private void appendMultilingualValues(StringBuilder line, TemporalAttributeValues temporalAttributeValues, List<String> validLanguages) {
        Map<String, String> attributeValuesByLocale = extractAttributeValuesByLocale(temporalAttributeValues);

        for (String locale : validLanguages) {
            String value = attributeValuesByLocale.get(locale);
            line.append(CsvConstants.SEPARATOR_TAB).append(value == null ? EMPTY : value);
        }
    }

    private Map<String, String> extractAttributeValuesByLocale(TemporalAttributeValues temporalAttributeValues) {
        Map<String, String> attributeValuesByLocale = new HashMap<>();

        if (temporalAttributeValues.getInternationalStringValues() != null && !temporalAttributeValues.getInternationalStringValues().isEmpty()) {
            for (es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto attributeValue : temporalAttributeValues.getInternationalStringValues().get(0).getTexts()) {
                String escapedLabel = AttributesUtils.escapeValueForTsv(attributeValue.getLabel());
                attributeValuesByLocale.put(attributeValue.getLocale(), escapedLabel);
            }
        }

        return attributeValuesByLocale;
    }

    private void appendSingleLanguageValue(StringBuilder line, TemporalAttributeValues temporalAttributeValues, int languageCount) {
        String firstValue = temporalAttributeValues.getValues().isEmpty() ? EMPTY : temporalAttributeValues.getValues().get(0);
        String escapedFirstValue = EMPTY.equals(firstValue) ? EMPTY : AttributesUtils.escapeValueForTsv(firstValue);

        for (int i = 0; i < languageCount; i++) {
            String value = (i == 0) ? escapedFirstValue : EMPTY;
            line.append(CsvConstants.SEPARATOR_TAB).append(value);
        }
    }

    private void write(String line) {
        printWriter.println(line);
    }

    private boolean isTextFormatAttributeMultilingual(DsdAttribute dsdAttribute) {
        return dsdAttribute.getTextFormatRepresentation() != null && DataType.INTERNATIONAL_STRING.equals(dsdAttribute.getTextFormatRepresentation().getTextType());
    }

    private String getExportHeader(List<String> validLanguages) {

        StringBuilder headerLine = new StringBuilder();
        headerLine.append(HEADER_ID_ATTRIBUTE).append(CsvConstants.SEPARATOR_TAB).append(HEADER_DIMENSIONES).append(CsvConstants.SEPARATOR_TAB).append(HEADER_VALORES_DIMENSIONES);

        for (String locale : validLanguages) {
            headerLine.append(CsvConstants.SEPARATOR_TAB).append(HEADER_PREFIX_VALOR_ATRIBUTO).append(HEADER_PREFIX_LANGUAGE).append(locale);
        }
        return headerLine.toString();
    }

    private String toDataAttributeWithDatasetAttachmentLevel(String datasetId, String attributeId) throws Exception {
        List<AttributeInstanceDto> attributeInstances = datasetRepositoriesServiceFacade.findAttributesInstancesWithDatasetAttachmentLevel(datasetId, attributeId);

        if (CollectionUtils.isEmpty(attributeInstances)) {
            return EMPTY;
        }

        return toAttributeInstanceValueToData(attributeInstances.get(0));
    }

    /**
     * Checks if TemporalAttributeValues has at least one non-empty value
     */
    private boolean hasNonEmptyValues(TemporalAttributeValues temporalAttributeValues, List<String> validLanguages) {
        if (temporalAttributeValues == null) {
            return false;
        }

        // Check multilingual values
        if (temporalAttributeValues.isMultilingualValue()) {
            Map<String, String> attributeValuesByLocale = extractAttributeValuesByLocale(temporalAttributeValues);
            for (String locale : validLanguages) {
                String value = attributeValuesByLocale.get(locale);
                if (value != null && !value.trim().isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        // Check single language values
        if (!temporalAttributeValues.getValues().isEmpty()) {
            for (String value : temporalAttributeValues.getValues()) {
                if (value != null && !value.trim().isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Retrieves attribute instance value in locale of dataset-repository. NOTE: This value will be escaped to not contain separator in DATA
     */
    private String toAttributeInstanceValueToData(AttributeInstanceBasicDto attributeDto) {
        String attributeValue = attributeDto.getValue().getLocalisedLabel(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE); // all attributes has only one locale
        return AttributesUtils.escapeValueToData(attributeValue);
    }
}
