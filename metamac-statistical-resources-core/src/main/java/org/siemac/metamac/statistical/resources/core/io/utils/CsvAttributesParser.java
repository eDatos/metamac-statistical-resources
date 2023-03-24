package org.siemac.metamac.statistical.resources.core.io.utils;

import static com.arte.statistic.parser.util.IoUtils.getBufferedReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeBase;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionBase;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dto.datasets.AttributeValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;

import au.com.bytecode.opencsv.CSVReader;

public class CsvAttributesParser {

    private CSVReader                  csvReader               = null;
    private String[]                   headers                 = null;
    private static final int           COLUMN_ID_ATRIBUTTE     = 0;
    private static final int           COLUMN_DIMENSION_NAME   = 1;
    private static final int           COLUMN_DIMENSION_VALUES = 2;
    private static final int           COLUMN_ATTRIBUTE_VALUE  = 3;
    private List<String>               idsDimensions           = new ArrayList<>();
    private List<String>               idsAttributes           = new ArrayList<>();
    private List<MetamacExceptionItem> exceptions              = new ArrayList<>();

    public CsvAttributesParser(InputStream pxStream, String charsetName, char separator, DataStructure dataStructure) throws Exception {
        BufferedReader bufferedReader = getBufferedReader(pxStream, charsetName);
        csvReader = new CSVReader(bufferedReader, separator);
        headers = readDefinition(csvReader);
        initializeIdsDsdDimensions(dataStructure);
        initializeIdsDsdAttributes(dataStructure);
    }

    private void initializeIdsDsdDimensions(DataStructure dataStructure) {
        for (DimensionBase dimensionBase : dataStructure.getDataStructureComponents().getDimensions().getDimensions()) {
            idsDimensions.add(dimensionBase.getId());
        }
    }

    private void initializeIdsDsdAttributes(DataStructure dataStructure) {
        for (AttributeBase base : dataStructure.getDataStructureComponents().getAttributes().getAttributes()) {
            idsAttributes.add(base.getId());
        }
    }

    private String[] readDefinition(CSVReader csvReader) throws Exception {
        headers = csvReader.readNext();
        if (isEmptyLine(headers)) {
            throw new Exception("[Incorrect header] Header not found");
        }
        return headers;
    }

    private static boolean isEmptyLine(String[] line) {
        return line == null || line.length == 0;
    }

    public String nextLine(List<DsdAttributeInstanceDto> dsdAttributeInstanceDtos, Map<String, List<CodeDimension>> codeDimensions, Map<String, List<ExternalItemDto>> externalItemsAttributeId)
            throws Exception {
        String[] line = csvReader.readNext();
        if (line == null) {
            return null;
        }
        String idAttribute = line[COLUMN_ID_ATRIBUTTE];
        if (!StringUtils.isBlank(idAttribute)) {
            checkIdAttributeInDsd(idAttribute);
            checkIdDimensionInDsd(line[COLUMN_DIMENSION_NAME]);
            DsdAttributeInstanceDto dsdAttributeInstanceDto = csvToDsdCreateAttributeInstanceDto(line, codeDimensions, externalItemsAttributeId);
            dsdAttributeInstanceDtos.add(dsdAttributeInstanceDto);
        } else {
            checkIdDimensionInDsd(line[COLUMN_DIMENSION_NAME]);
            csvToDsdAttributeInstanceDto(dsdAttributeInstanceDtos.get(dsdAttributeInstanceDtos.size() - 1), line, codeDimensions, externalItemsAttributeId);
        }
        return idAttribute;
    }

    private void checkIdAttributeInDsd(String idAttribute) {
        if (!idsAttributes.contains(idAttribute)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_ATTRIBUTES_ATTRIBUTE_ID_INVALID, idAttribute));
        }
    }

    private void checkIdDimensionInDsd(String idDimension) {
        if (!StringUtils.isBlank(idDimension) && !idsDimensions.contains(idDimension)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_ATTRIBUTES_DIMENSION_ID_INVALID, idDimension));
        }
    }

    private void csvToDsdAttributeInstanceDto(DsdAttributeInstanceDto dsdAttributeInstanceDto, String[] line, Map<String, List<CodeDimension>> codeDimensions,
            Map<String, List<ExternalItemDto>> externalItemsAttributeId) throws MetamacException {
        AttributeValueDto attributeValueDto = new AttributeValueDto();
        setAttribute(dsdAttributeInstanceDto, line, externalItemsAttributeId, attributeValueDto);
        setCodeDimensions(dsdAttributeInstanceDto, line, codeDimensions);
    }

    private DsdAttributeInstanceDto csvToDsdCreateAttributeInstanceDto(String[] line, Map<String, List<CodeDimension>> codeDimensions, Map<String, List<ExternalItemDto>> externalItemsAttributeId)
            throws MetamacException {
        AttributeValueDto attributeValueDto = new AttributeValueDto();
        DsdAttributeInstanceDto dsdAttributeInstanceDto = new DsdAttributeInstanceDto();
        dsdAttributeInstanceDto.setAttributeId(line[COLUMN_ID_ATRIBUTTE]);
        setAttribute(dsdAttributeInstanceDto, line, externalItemsAttributeId, attributeValueDto);
        setCodeDimensions(dsdAttributeInstanceDto, line, codeDimensions);
        return dsdAttributeInstanceDto;
    }

    private void setCodeDimensions(DsdAttributeInstanceDto dsdAttributeInstanceDto, String[] line, Map<String, List<CodeDimension>> codeDimensions) throws MetamacException {
        if (dsdAttributeInstanceDto.getCodeDimensions() == null) {
            dsdAttributeInstanceDto.setCodeDimensions(getDimensions(line, codeDimensions));
        } else {
            dsdAttributeInstanceDto.getCodeDimensions().putAll(getDimensions(line, codeDimensions));
        }
    }

    private void setAttribute(DsdAttributeInstanceDto dsdAttributeInstanceDto, String[] line, Map<String, List<ExternalItemDto>> externalItemsAttributeId, AttributeValueDto attributeValueDto) {
        ExternalItemDto externalItem = getExternalItemDto(line[COLUMN_ATTRIBUTE_VALUE], dsdAttributeInstanceDto.getAttributeId(), externalItemsAttributeId);
        if (externalItem == null && checkAttributeExternalItemDefinition(dsdAttributeInstanceDto.getAttributeId(), externalItemsAttributeId)) {
            attributeValueDto.setStringValue(line[COLUMN_ATTRIBUTE_VALUE]);
        }
        attributeValueDto.setExternalItemValue(externalItem);
        dsdAttributeInstanceDto.setValue(attributeValueDto);
    }

    private boolean checkAttributeExternalItemDefinition(String attributeId, Map<String, List<ExternalItemDto>> externalItemsAttributeId) {
        List<ExternalItemDto> externalItems = externalItemsAttributeId.get(attributeId);
        return externalItems != null && !externalItems.isEmpty();
    }

    private ExternalItemDto getExternalItemDto(String code, String attributeId, Map<String, List<ExternalItemDto>> externalItemsAttributeId) {
        List<ExternalItemDto> externalItems = externalItemsAttributeId.get(attributeId);
        if (externalItems == null) {
            return null;
        }
        for (ExternalItemDto externalItem : externalItems) {
            if (code.equals(externalItem.getCode())) {
                return externalItem;
            }
        }
        return null;
    }

    private Map<String, List<CodeItemDto>> getDimensions(String[] line, Map<String, List<CodeDimension>> codesDimensions) throws MetamacException {
        Map<String, List<CodeItemDto>> codeItemDtos = new HashMap<>();
        String dimension = line[COLUMN_DIMENSION_NAME];
        if (!StringUtils.isBlank(dimension)) {
            List<CodeDimension> codeDimensions = codesDimensions.get(dimension);
            setCodeItems(line, dimension, codeItemDtos, codeDimensions);
        }
        return codeItemDtos;
    }

    private void setCodeItems(String[] line, String dimension, Map<String, List<CodeItemDto>> codesDimensions, List<CodeDimension> codeDimensions) throws MetamacException {
        String[] dimensionValues = line[COLUMN_DIMENSION_VALUES].split(", ");
        List<CodeItemDto> codeItemDtos = new ArrayList<>();
        for (String dimensionValue : dimensionValues) {
            CodeItemDto codeItemDto = new CodeItemDto();
            CodeDimension codeDimension = getCodeDimension(dimensionValue, codeDimensions);
            if (codeDimension == null && (codeDimensions != null && !codeDimensions.isEmpty())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORTATION_ATTRIBUTES_DIMENSION_VALUE_INVALID)
                        .withMessageParameters(line[COLUMN_DIMENSION_NAME], line[COLUMN_ID_ATRIBUTTE]).build();
            }
            codeItemDto.setCode(codeDimension != null ? codeDimension.getIdentifier() : dimensionValue);
            codeItemDto.setTitle(codeDimension != null ? codeDimension.getTitle() : dimensionValue);
            codeItemDtos.add(codeItemDto);
        }
        codesDimensions.put(dimension, codeItemDtos);
    }

    private CodeDimension getCodeDimension(String tittle, List<CodeDimension> codeDimensions) {
        if (codeDimensions == null) {
            return null;
        }

        for (CodeDimension codeDimension : codeDimensions) {
            if (tittle.equals(codeDimension.getIdentifier())) {
                return codeDimension;
            }
        }
        return null;
    }

    public List<MetamacExceptionItem> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<MetamacExceptionItem> exceptions) {
        this.exceptions = exceptions;
    }

}