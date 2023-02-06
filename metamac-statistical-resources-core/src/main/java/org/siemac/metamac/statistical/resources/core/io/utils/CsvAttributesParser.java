package org.siemac.metamac.statistical.resources.core.io.utils;

import static com.arte.statistic.parser.util.IoUtils.getBufferedReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void setNextLine(Map<String, DsdAttributeInstanceDto> dsdAttributeInstanceDtos) throws Exception {
        DsdAttributeInstanceDto dsdAttributeInstanceDto = null;
        String[] line = csvReader.readNext();
        dsdAttributeInstanceDto = dsdAttributeInstanceDtos.get(line[0]);
        csvToDsdAttributeInstanceDto(dsdAttributeInstanceDto, line);
        dsdAttributeInstanceDtos.put(dsdAttributeInstanceDto.getAttributeId(), dsdAttributeInstanceDto);
    }

    private DsdAttributeInstanceDto csvToDsdAttributeInstanceDto(DsdAttributeInstanceDto dsdAttributeInstanceDto, String[] line) {
        if (dsdAttributeInstanceDto == null) {
            dsdAttributeInstanceDto = new DsdAttributeInstanceDto();
            dsdAttributeInstanceDto.setAttributeId(line[0]);
            String dimension = line[3];
            if (dimension != null) {
                Map<String, List<CodeItemDto>> codeDimensions = new HashMap<>();
                setCodeItems(line, dimension, codeDimensions);
            }
        } else {
            String dimension = line[3];
            if (dimension != null) {
                Map<String, List<CodeItemDto>> codeDimensions = dsdAttributeInstanceDto.getCodeDimensions();
                setCodeItems(line, dimension, codeDimensions);
            }
        }
        return dsdAttributeInstanceDto;
    }

    private void setCodeItems(String[] line, String dimension, Map<String, List<CodeItemDto>> codeDimensions) {
        String[] dimensionValues = line[4].split(", ");
        List<CodeItemDto> codeItemDtos = new ArrayList<>();
        for (String dimensionValue : dimensionValues) {
            CodeItemDto codeItemDto = new CodeItemDto();
            codeItemDto.setCode(dimensionValue);
            codeItemDto.setTitle(dimensionValue);
            codeItemDtos.add(codeItemDto);
        }
        codeDimensions.put(dimension, codeItemDtos);
    }
}