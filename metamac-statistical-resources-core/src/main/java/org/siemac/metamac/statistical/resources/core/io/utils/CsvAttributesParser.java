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
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dto.datasets.AttributeValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;

public class CsvAttributesParser {

    private au.com.bytecode.opencsv.CSVReader csvReader        = null;
    private String[]                          headers          = null;
    private int                               lineSizeExpected = 0;
    private int                               lineNumber       = 0;

    public CsvAttributesParser(InputStream pxStream, String charsetName, char separator) throws Exception {
        BufferedReader bufferedReader = getBufferedReader(pxStream, charsetName);
        csvReader = new au.com.bytecode.opencsv.CSVReader(bufferedReader, separator);
        headers = readDefinition(csvReader);
    }

    private String[] readDefinition(au.com.bytecode.opencsv.CSVReader csvReader) throws Exception {
        String[] header = csvReader.readNext();
        if (isEmptyLine(header)) {
            throw new Exception("[Incorrect header] Header not found");
        }
        lineNumber++;
        lineSizeExpected = header.length;
        return headers;
    }

    private static boolean isEmptyLine(String[] line) {
        return line == null || line.length == 0;
    }

    public boolean setNextLine(Map<String, DsdAttributeInstanceDto> dsdAttributeInstanceDtos, Map<String, List<CodeDimension>> codeDimensions, Map<String, List<ExternalItemDto>> externalItemsAttributeId) throws Exception {
        DsdAttributeInstanceDto dsdAttributeInstanceDto = null;
        String[] line = csvReader.readNext();
        if (line == null) {
            return false;
        }
        dsdAttributeInstanceDto = dsdAttributeInstanceDtos.get(line[0]);
        DsdAttributeInstanceDto dsdAttributeCreatedInstanceDto = csvToDsdAttributeInstanceDto(dsdAttributeInstanceDto, line, codeDimensions, externalItemsAttributeId);
        dsdAttributeInstanceDtos.put(dsdAttributeCreatedInstanceDto.getAttributeId(), dsdAttributeCreatedInstanceDto);
        return true;
    }

    private DsdAttributeInstanceDto csvToDsdAttributeInstanceDto(DsdAttributeInstanceDto dsdAttributeInstanceDto, String[] line, Map<String, List<CodeDimension>> codeDimensions, Map<String, List<ExternalItemDto>> externalItemsAttributeId) {
        String attributeId = line[0];
        if (dsdAttributeInstanceDto == null) {
            dsdAttributeInstanceDto = new DsdAttributeInstanceDto();
            AttributeValueDto attributeValueDto = new AttributeValueDto();
            dsdAttributeInstanceDto.setAttributeId(attributeId);
            ExternalItemDto externalItem = getExternalItemDto(line[5], attributeId, externalItemsAttributeId);
            if (externalItem == null) {
                attributeValueDto.setStringValue(line[5]);
            }
            attributeValueDto.setExternalItemValue(externalItem);
            dsdAttributeInstanceDto.setValue(attributeValueDto);
        }
        dsdAttributeInstanceDto.setCodeDimensions(getDimensions(line, codeDimensions));
        return dsdAttributeInstanceDto;
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

    private Map<String, List<CodeItemDto>> getDimensions(String[] line, Map<String, List<CodeDimension>> codesDimensions) {
        Map<String, List<CodeItemDto>> codeItemDtos = new HashMap<>();
        String dimension = line[3];
        if (!StringUtils.isBlank(dimension)) {
            List<CodeDimension> codeDimensions = codesDimensions.get(dimension);
            setCodeItems(line, dimension, codeItemDtos, codeDimensions);
        }
        return codeItemDtos;
    }

    private void setCodeItems(String[] line, String dimension, Map<String, List<CodeItemDto>> codesDimensions, List<CodeDimension> codeDimensions) {
        String[] dimensionValues = line[4].split(", ");
        List<CodeItemDto> codeItemDtos = new ArrayList<>();
        for (String dimensionValue : dimensionValues) {
            CodeItemDto codeItemDto = new CodeItemDto();
            CodeDimension codeDimension = getCodeDimension(dimensionValue, codeDimensions);
            codeItemDto.setCode(codeDimension != null ? codeDimension.getIdentifier() : dimensionValue);
            codeItemDto.setTitle(codeDimension != null ? codeDimension.getTitle() : dimensionValue);
            codeItemDtos.add(codeItemDto);
        }
        codesDimensions.put(dimension, codeItemDtos);
    }

    private CodeDimension getCodeDimension(String tittle, List<CodeDimension> codeDimensions) {
        // return codeDimensions != null ? codeDimensions.stream().filter(codeDimension -> title.equals(codeDimension.getTitle())).findAny().orElse(null) : null;
        for (CodeDimension codeDimension : codeDimensions) {
            if (tittle.equals(codeDimension.getTitle())) {
                return codeDimension;
            }
        }
        return null;
    }
}