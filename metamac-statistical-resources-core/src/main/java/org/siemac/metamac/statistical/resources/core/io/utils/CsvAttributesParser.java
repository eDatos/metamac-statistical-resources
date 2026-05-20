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
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeBase;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionBase;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.TimeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.enume.utils.AttributeInstanceTypeEnum;
import org.siemac.metamac.statistical.resources.core.dto.datasets.AttributeValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.utils.InternationalStringUtils;

import au.com.bytecode.opencsv.CSVReader;

public class CsvAttributesParser {

    private CSVReader                  csvReader                                 = null;
    private String[]                   headers                                   = null;
    List<String>                       validLanguages                            = new ArrayList<>();
    private static final int           COLUMN_ID_ATTRIBUTE                       = 0;
    private static final int           COLUMN_INSTANCE_TYPE                      = 1;
    private static final int           COLUMN_DIMENSION_NAME                     = 2;
    private static final int           COLUMN_DIMENSION_VALUES                   = 3;
    private static final int           COLUMN_ATTRIBUTE_VALUE                    = 4;
    private List<String>               idsDimensions                             = new ArrayList<>();
    private List<String>               idsAttributes                             = new ArrayList<>();
    private Map<String, Boolean>       isMultilingualByIdAttribute               = new HashMap<>();
    private List<MetamacExceptionItem> exceptions                                = new ArrayList<>();
    private String                     timeDimensionId                           = null;
    private String                     lastInstanceType                          = null;
    private List<String>               validGranularityCodes                     = new ArrayList<>();
    private String                     temporalGranularityCodelistUrn            = null;
    private boolean                    legacyFormat                              = false;

    public CsvAttributesParser(InputStream pxStream, String charsetName, char separator, DataStructure dataStructure, List<String> validLanguages, List<String> validGranularityCodes,
            String temporalGranularityCodelistUrn) throws Exception {
        BufferedReader bufferedReader = getBufferedReader(pxStream, charsetName);
        csvReader = new CSVReader(bufferedReader, separator);
        headers = readDefinition(csvReader);
        if (validLanguages != null) {
            this.validLanguages = validLanguages;
        }
        if (validGranularityCodes != null) {
            this.validGranularityCodes = validGranularityCodes;
        }
        this.temporalGranularityCodelistUrn = temporalGranularityCodelistUrn;
        initializeIdsDsdDimensions(dataStructure);
        initializeIdsDsdAttributes(dataStructure);
    }

    private void initializeIdsDsdDimensions(DataStructure dataStructure) {
        for (DimensionBase dimensionBase : dataStructure.getDataStructureComponents().getDimensions().getDimensions()) {
            idsDimensions.add(dimensionBase.getId());
            if (dimensionBase instanceof TimeDimension) {
                timeDimensionId = dimensionBase.getId();
            }
        }
    }

    private boolean isAttributeMultilingual(AttributeBase base) {
        return base.getLocalRepresentation() != null && base.getLocalRepresentation().getTextFormat() != null && base.getLocalRepresentation().getTextFormat().getTextType() != null
                && DataType.INTERNATIONAL_STRING.equals(base.getLocalRepresentation().getTextFormat().getTextType());
    }

    private void initializeIdsDsdAttributes(DataStructure dataStructure) {
        for (AttributeBase base : dataStructure.getDataStructureComponents().getAttributes().getAttributes()) {
            idsAttributes.add(base.getId());
            isMultilingualByIdAttribute.put(base.getId(), isAttributeMultilingual(base));
        }
    }

    private String[] readDefinition(CSVReader csvReader) throws Exception {
        headers = csvReader.readNext();
        if (isEmptyLine(headers)) {
            throw new Exception("[Incorrect header] Header not found");
        }
        if (isLegacyFormat(headers)) {
            legacyFormat = true;
            headers = normalizeToCurrentFormat(headers, ManipulateDataUtils.HEADER_INSTANCE_TYPE);
        }
        return headers;
    }

    private boolean isLegacyFormat(String[] headers) {
        return headers.length < 2 || !ManipulateDataUtils.HEADER_INSTANCE_TYPE.equals(headers[COLUMN_INSTANCE_TYPE]);
    }

    private String[] normalizeToCurrentFormat(String[] original, String instanceTypeValue) {
        String[] normalized = new String[original.length + 1];
        normalized[COLUMN_ID_ATTRIBUTE] = original[COLUMN_ID_ATTRIBUTE];
        normalized[COLUMN_INSTANCE_TYPE] = instanceTypeValue;
        System.arraycopy(original, 1, normalized, 2, original.length - 1);
        return normalized;
    }

    private static boolean isEmptyLine(String[] line) {
        return line == null || line.length == 0;
    }

    public String nextLine(List<DsdAttributeInstanceDto> dsdAttributeInstanceDtos, List<DsdGranularityAttributeInstanceDto> granularityInstanceDtos,
            Map<String, List<CodeDimension>> codeDimensions, Map<String, List<ExternalItemDto>> externalItemsAttributeId) throws Exception {
        String[] line = csvReader.readNext();
        if (line == null) {
            return null;
        }
        if (legacyFormat) {
            line = normalizeToCurrentFormat(line, "");
        }
        String idAttribute = line[COLUMN_ID_ATTRIBUTE];
        String instanceType = line[COLUMN_INSTANCE_TYPE];
        if (!StringUtils.isBlank(idAttribute)) {
            checkIdAttributeInDsd(idAttribute);
            checkIdDimensionInDsd(line[COLUMN_DIMENSION_NAME]);
            if (AttributeInstanceTypeEnum.isGranularity(instanceType)) {
                lastInstanceType = AttributeInstanceTypeEnum.GRANULARITY.getValue();
                DsdGranularityAttributeInstanceDto dto = csvToGranularityAttributeInstanceDto(line, codeDimensions, externalItemsAttributeId);
                granularityInstanceDtos.add(dto);
            } else {
                lastInstanceType = instanceType;
                DsdAttributeInstanceDto dsdAttributeInstanceDto = csvToDsdCreateAttributeInstanceDto(line, codeDimensions, externalItemsAttributeId);
                dsdAttributeInstanceDtos.add(dsdAttributeInstanceDto);
            }
        } else {
            checkIdDimensionInDsd(line[COLUMN_DIMENSION_NAME]);
            if (AttributeInstanceTypeEnum.isGranularity(lastInstanceType)) {
                csvAddDimensionToGranularityInstance(granularityInstanceDtos.get(granularityInstanceDtos.size() - 1), line, codeDimensions, externalItemsAttributeId);
            } else {
                csvToDsdAttributeInstanceDto(dsdAttributeInstanceDtos.get(dsdAttributeInstanceDtos.size() - 1), line, codeDimensions, externalItemsAttributeId);
            }
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
        dsdAttributeInstanceDto.setAttributeId(line[COLUMN_ID_ATTRIBUTE]);
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
            if (Boolean.TRUE.equals(this.isMultilingualByIdAttribute.get(dsdAttributeInstanceDto.getAttributeId()))) {
                attributeValueDto.setInternationalStringValue(retrieveInternationalStringAttributeValues(line, dsdAttributeInstanceDto.getAttributeId()));
            } else {
                attributeValueDto.setStringValue(line[COLUMN_ATTRIBUTE_VALUE]);
            }

        }
        attributeValueDto.setExternalItemValue(externalItem);
        dsdAttributeInstanceDto.setValue(attributeValueDto);
    }

    private InternationalStringDto retrieveInternationalStringAttributeValues(String[] line, String idAttribute) {

        InternationalStringDto attributeValue = new InternationalStringDto();
        List<String> locales = new ArrayList<>();
        if (headers.length != line.length) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_ATTRIBUTES_INVALID_HEADER_OR_ATTRIBUTE_INFO, idAttribute));
        }

        for (int i = COLUMN_ATTRIBUTE_VALUE; i < line.length; i++) {
            if (!StringUtils.isBlank(line[i])) {
                String[] columnSplited = StringUtils.splitPreserveAllTokens(this.headers[i], ManipulateDataUtils.HEADER_LANGUAGE_SEPARATOR);

                if (i == COLUMN_ATTRIBUTE_VALUE && !checkMultilingualHeader(columnSplited, idAttribute)) {
                    return attributeValue;
                }

                if (locales.contains(columnSplited[1])) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_ATTRIBUTES_INVALID_MULTILINGUAL_HEADER_DUPLICATE_LANGUAGE, idAttribute));
                    return attributeValue;
                }

                attributeValue.addText(InternationalStringUtils.createCommonLocalisedStringDto(columnSplited[1], line[i], false));
                locales.add(columnSplited[1]);
            }
        }

        return attributeValue;
    }

    private boolean checkMultilingualHeader(String[] headerMultilingualValue, String idAttribute) {
        if (headerMultilingualValue == null || headerMultilingualValue.length != 2 || StringUtils.isBlank(headerMultilingualValue[1])) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_ATTRIBUTES_INVALID_MULTILINGUAL_HEADER, idAttribute));
            return false;
        }

        if (!this.validLanguages.contains(headerMultilingualValue[1])) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_ATTRIBUTES_INVALID_MULTILINGUAL_HEADER_INVALID_LANGUAGE, idAttribute));
            return false;
        }
        return true;
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
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_ATTRIBUTES_DIMENSION_VALUE_INVALID, line[COLUMN_DIMENSION_NAME], line[COLUMN_ID_ATTRIBUTE]));
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

    private DsdGranularityAttributeInstanceDto csvToGranularityAttributeInstanceDto(String[] line, Map<String, List<CodeDimension>> codeDimensions,
            Map<String, List<ExternalItemDto>> externalItemsAttributeId) throws MetamacException {
        DsdGranularityAttributeInstanceDto dto = new DsdGranularityAttributeInstanceDto();
        dto.setAttributeId(line[COLUMN_ID_ATTRIBUTE]);
        AttributeValueDto attributeValueDto = buildAttributeValue(dto.getAttributeId(), line, externalItemsAttributeId);
        dto.setValue(attributeValueDto);
        addDimensionToGranularityInstance(dto, line, codeDimensions);
        return dto;
    }

    private void csvAddDimensionToGranularityInstance(DsdGranularityAttributeInstanceDto dto, String[] line, Map<String, List<CodeDimension>> codeDimensions,
            Map<String, List<ExternalItemDto>> externalItemsAttributeId) throws MetamacException {
        AttributeValueDto attributeValueDto = buildAttributeValue(dto.getAttributeId(), line, externalItemsAttributeId);
        dto.setValue(attributeValueDto);
        addDimensionToGranularityInstance(dto, line, codeDimensions);
    }

    private void addDimensionToGranularityInstance(DsdGranularityAttributeInstanceDto dto, String[] line, Map<String, List<CodeDimension>> codeDimensions) throws MetamacException {
        String dimensionName = line[COLUMN_DIMENSION_NAME];
        if (StringUtils.isBlank(dimensionName)) {
            return;
        }
        if (dimensionName.equals(timeDimensionId)) {
            String[] rawCodes = line[COLUMN_DIMENSION_VALUES].split(", ");
            List<String> codesList = new ArrayList<String>();
            for (String code : rawCodes) {
                String trimmedCode = code.trim();
                if (!validGranularityCodes.isEmpty() && !validGranularityCodes.contains(trimmedCode)) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_ATTRIBUTES_GRANULARITY_CODE_INVALID, trimmedCode, temporalGranularityCodelistUrn));
                } else {
                    codesList.add(trimmedCode);
                }
            }
            Map<String, List<String>> granularityCodesByDimension = dto.getGranularityCodesByDimension();
            if (granularityCodesByDimension == null) {
                granularityCodesByDimension = new HashMap<String, List<String>>();
                dto.setGranularityCodesByDimension(granularityCodesByDimension);
            }
            granularityCodesByDimension.put(dimensionName, codesList);
        } else {
            Map<String, List<CodeItemDto>> codesByDimension = dto.getCodesByDimension();
            if (codesByDimension == null) {
                codesByDimension = new HashMap<String, List<CodeItemDto>>();
                dto.setCodesByDimension(codesByDimension);
            }
            Map<String, List<CodeItemDto>> dimEntry = new HashMap<String, List<CodeItemDto>>();
            setCodeItems(line, dimensionName, dimEntry, codeDimensions.get(dimensionName));
            codesByDimension.putAll(dimEntry);
        }
    }

    private AttributeValueDto buildAttributeValue(String attributeId, String[] line, Map<String, List<ExternalItemDto>> externalItemsAttributeId) {
        AttributeValueDto attributeValueDto = new AttributeValueDto();
        ExternalItemDto externalItem = getExternalItemDto(line[COLUMN_ATTRIBUTE_VALUE], attributeId, externalItemsAttributeId);
        if (externalItem == null && checkAttributeExternalItemDefinition(attributeId, externalItemsAttributeId)) {
            if (Boolean.TRUE.equals(this.isMultilingualByIdAttribute.get(attributeId))) {
                attributeValueDto.setInternationalStringValue(retrieveInternationalStringAttributeValues(line, attributeId));
            } else {
                attributeValueDto.setStringValue(line[COLUMN_ATTRIBUTE_VALUE]);
            }
        }
        attributeValueDto.setExternalItemValue(externalItem);
        return attributeValueDto;
    }

}