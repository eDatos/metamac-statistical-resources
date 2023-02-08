package org.siemac.metamac.statistical.resources.core.io.utils;

import static com.arte.statistic.parser.util.IoUtils.getBufferedReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dto.datasets.AttributeValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;

public class CsvAttributesParser {

    private au.com.bytecode.opencsv.CSVReader csvReader        = null;
    private String[]                          headers          = null;
    private int                               lineSizeExpected = 0;
    private int                               lineNumber       = 0;
    private static final String               DIMENSIONS       = "Dimensiones";
    private static final String               DIMENSIONS_GROUP = "Grupo de dimensiones";
    private static final String               DATASET          = "Dataset";

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

    public void setNextLine(Map<String, DsdAttributeInstanceDto> dsdAttributeInstanceDtos, Map<String, List<CodeDimension>> codeDimensions) throws Exception {
        DsdAttributeInstanceDto dsdAttributeInstanceDto = null;
        String[] line = csvReader.readNext();
        dsdAttributeInstanceDto = dsdAttributeInstanceDtos.get(line[0]);
        DsdAttributeInstanceDto dsdAttributeCreatedInstanceDto = csvToDsdAttributeInstanceDto(dsdAttributeInstanceDto, line, codeDimensions);
        dsdAttributeInstanceDtos.put(dsdAttributeCreatedInstanceDto.getAttributeId(), dsdAttributeCreatedInstanceDto);
    }

    private DsdAttributeInstanceDto csvToDsdAttributeInstanceDto(DsdAttributeInstanceDto dsdAttributeInstanceDto, String[] line, Map<String, List<CodeDimension>> codeDimensions) {
        if (dsdAttributeInstanceDto == null) {
            dsdAttributeInstanceDto = new DsdAttributeInstanceDto();
            AttributeValueDto attributeValueDto = new AttributeValueDto();
            dsdAttributeInstanceDto.setAttributeId(line[0]);
            attributeValueDto.setStringValue(line[5]);
            dsdAttributeInstanceDto.setValue(attributeValueDto);
        }
        setDimensions(line, codeDimensions);
        return dsdAttributeInstanceDto;
    }

    private void setDimensions(String[] line, Map<String, List<CodeDimension>> codesDimensions) {
        String dimension = line[3];
        if (!StringUtils.isBlank(dimension)) {
            List<CodeDimension> codeDimensions = codesDimensions.get(dimension);
            Map<String, List<CodeItemDto>> codeItemDtos = new HashMap<>();
            setCodeItems(line, dimension, codeItemDtos, codeDimensions);
        }
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

    private CodeDimension getCodeDimension(String title, List<CodeDimension> codeDimensions) {
        return codeDimensions.stream().filter(codeDimension -> title.equals(codeDimension.getTitle())).findAny().orElse(null);
    }
}