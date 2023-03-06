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
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dto.datasets.AttributeValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

public class CsvAttributesParser {

    private static final Logger log                      = LoggerFactory.getLogger(CsvAttributesParser.class);  

    private CSVReader           csvReader                = null;
    private String[]            headers                  = null;
    private int                 attributesInstancesCount = 0;
    private static final int    COLUMN_ID_ATRIBUTTE      = 0;
    private static final int    COLUMN_DIMENSION_NAME    = 1;
    private static final int    COLUMN_DIMENSION_VALUES  = 2;
    private static final int    COLUMN_ATTRIBUTE_VALUE   = 3;

    public CsvAttributesParser(InputStream pxStream, String charsetName, char separator) throws Exception {
        BufferedReader bufferedReader = getBufferedReader(pxStream, charsetName);
        csvReader = new CSVReader(bufferedReader, separator);
        headers = readDefinition(csvReader);
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

    public String nextLine(Map<String, DsdAttributeInstanceDto> dsdAttributeInstanceDtos, Map<String, List<CodeDimension>> codeDimensions, Map<String, List<ExternalItemDto>> externalItemsAttributeId,
            String idLastReadAttribute) throws Exception {
        DsdAttributeInstanceDto dsdAttributeInstanceDto = null;
        String[] line = csvReader.readNext();
        if (line == null) {
            return null;
        }
        String idAttribute = getAttributeName(idLastReadAttribute, line[COLUMN_ID_ATRIBUTTE]);
        dsdAttributeInstanceDto = dsdAttributeInstanceDtos.get(idAttribute + attributesInstancesCount);
        DsdAttributeInstanceDto dsdAttributeCreatedInstanceDto = csvToDsdAttributeInstanceDto(dsdAttributeInstanceDto, line, codeDimensions, externalItemsAttributeId, idAttribute);
        dsdAttributeInstanceDtos.put(dsdAttributeCreatedInstanceDto.getAttributeId() + attributesInstancesCount, dsdAttributeCreatedInstanceDto);
        return idAttribute;
    }

    private String getAttributeName(String idLastReadAttribute, String idAttribute) {
        if (StringUtils.isBlank(idAttribute)) {
            return idLastReadAttribute;
        }
        attributesInstancesCount++;
        return idAttribute;
    }

    private DsdAttributeInstanceDto csvToDsdAttributeInstanceDto(DsdAttributeInstanceDto dsdAttributeInstanceDto, String[] line, Map<String, List<CodeDimension>> codeDimensions,
            Map<String, List<ExternalItemDto>> externalItemsAttributeId, String attributeId) throws MetamacException {
        AttributeValueDto attributeValueDto = new AttributeValueDto();
        if (dsdAttributeInstanceDto == null) {
            dsdAttributeInstanceDto = new DsdAttributeInstanceDto();
            dsdAttributeInstanceDto.setAttributeId(attributeId);
        }
        setAttribute(dsdAttributeInstanceDto, line, externalItemsAttributeId, attributeId, attributeValueDto);
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

    private void setAttribute(DsdAttributeInstanceDto dsdAttributeInstanceDto, String[] line, Map<String, List<ExternalItemDto>> externalItemsAttributeId, String attributeId,
            AttributeValueDto attributeValueDto) {
        ExternalItemDto externalItem = getExternalItemDto(line[COLUMN_ATTRIBUTE_VALUE], attributeId, externalItemsAttributeId);
        if (externalItem == null && checkAttributeExternalItemDefinition(attributeId, externalItemsAttributeId)) {
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
                log.error(new StringBuilder("Error: Dimension ").append(line[COLUMN_DIMENSION_NAME]).append(" value invalid").toString());
                throw new MetamacException(CommonServiceExceptionType.UNKNOWN, "Error: Dimension value invalid");
            }
            codeItemDto.setCode(codeDimension != null ? codeDimension.getIdentifier() : dimensionValue);
            codeItemDto.setTitle(codeDimension != null ? codeDimension.getTitle() : dimensionValue);
            codeItemDtos.add(codeItemDto);
        }
        codesDimensions.put(dimension, codeItemDtos);
    }

    private CodeDimension getCodeDimension(String tittle, List<CodeDimension> codeDimensions) {
        if(codeDimensions == null) {
            return null;
        }

        for (CodeDimension codeDimension : codeDimensions) {
            if (tittle.equals(codeDimension.getTitle())) {
                return codeDimension;
            }
        }
        return null;
    }
}