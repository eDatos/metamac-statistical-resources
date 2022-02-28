package org.siemac.metamac.statistical.resources.core.utils.asserts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.AttributeValue;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DimensionRepresentationMapping;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.dataset.utils.DatasetVersionUtils;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.AttributeValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionMainCoveragesDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DimensionRepresentationMappingDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import junit.framework.AssertionFailedError;

public class DatasetsAsserts extends BaseAsserts {

    // -----------------------------------------------------------------
    // DATASET: DO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsDataset(Dataset expected, Dataset actual) {
        assertEqualsNullability(expected, actual);

        if (expected != null) {
            assertEqualsIdentifiableStatisticalResource(expected.getIdentifiableStatisticalResource(), actual.getIdentifiableStatisticalResource());

            if (expected.getVersions() != null) {
                assertNotNull(actual.getVersions());
                assertEquals(expected.getVersions().size(), actual.getVersions().size());
                assertEqualsDatasetVersionCollection(expected.getVersions(), actual.getVersions());
            } else {
                assertEquals(null, actual);
            }
        }
    }

    public static void assertEqualsCoverageForDsdComponent(DatasetVersion datasetVersion, String dsdComponentId, List<CodeDimension> codes) {
        assertNotNull(datasetVersion.getDimensionsCoverage());
        List<CodeDimension> codesForDimensions = new ArrayList<CodeDimension>();
        for (CodeDimension codeDim : datasetVersion.getDimensionsCoverage()) {
            if (codeDim.getDsdComponentId().equals(dsdComponentId)) {
                codesForDimensions.add(codeDim);
            }
        }

        assertEquals(codes.size(), codesForDimensions.size());

        for (int i = 0; i < codes.size(); i++) {
            assertEquals(codes.get(i).getIdentifier(), codesForDimensions.get(i).getIdentifier());
            assertEquals(codes.get(i).getTitle(), codesForDimensions.get(i).getTitle());
        }
    }

    public static void assertEqualsCodeDimensionsCollection(List<CodeDimension> expected, List<CodeDimension> actual) {
        assertEqualsNullability(expected, actual);

        if (expected != null) {
            assertEquals(expected.size(), actual.size());

            for (int i = 0; i < expected.size(); i++) {
                CodeDimension expectedCode = expected.get(i);
                CodeDimension actualCode = actual.get(i);
                assertEqualsCodeDimension(expectedCode, actualCode);
            }
        }
    }

    public static void assertEqualsCodeDimension(CodeDimension expected, CodeDimension actual) {
        assertEqualsNullability(expected, actual);
        assertEquals(expected.getDsdComponentId(), actual.getDsdComponentId());
        assertEquals(expected.getIdentifier(), actual.getIdentifier());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEqualsNullability(expected.getDatasetVersion(), actual.getDatasetVersion());
        if (expected.getDatasetVersion() != null) {
            assertEquals(expected.getDatasetVersion().getId(), actual.getDatasetVersion().getId());
        }
    }

    public static void assertEqualsAttributeValuesCollection(List<AttributeValue> expected, List<AttributeValue> actual) {
        assertEqualsNullability(expected, actual);

        if (expected != null) {
            assertEquals(expected.size(), actual.size());

            for (int i = 0; i < expected.size(); i++) {
                AttributeValue expectedValue = expected.get(i);
                boolean found = false;
                for (int j = 0; j < actual.size(); j++) {
                    AttributeValue actualValue = actual.get(j);
                    try {
                        assertEqualsAttributeValue(expectedValue, actualValue);
                        found = true;
                    } catch (AssertionError e) {
                    }
                }
                if (!found) {
                    throw new AssertionFailedError("Expected attribute not found " + expectedValue.getIdentifier());
                }
            }
        }
    }

    public static void assertEqualsAttributeValue(AttributeValue expected, AttributeValue actual) {
        assertEqualsNullability(expected, actual);
        assertEquals(expected.getDsdComponentId(), actual.getDsdComponentId());
        assertEquals(expected.getIdentifier(), actual.getIdentifier());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEqualsNullability(expected.getDatasetVersion(), actual.getDatasetVersion());
        if (expected.getDatasetVersion() != null) {
            assertEquals(expected.getDatasetVersion().getId(), actual.getDatasetVersion().getId());
        }
    }

    public static void assertEqualsCodeItemDtosCollection(List<CodeItemDto> expected, List<CodeItemDto> actual) {
        assertEqualsNullability(expected, actual);

        if (expected != null) {
            assertEquals(expected.size(), actual.size());

            for (int i = 0; i < expected.size(); i++) {
                CodeItemDto expectedCode = expected.get(i);
                CodeItemDto actualCode = actual.get(i);
                assertEqualsCodeItemDto(expectedCode, actualCode);
            }
        }
    }

    public static void assertEqualsCodeItemDto(CodeItemDto expected, CodeItemDto actual) {
        assertEqualsNullability(expected, actual);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    // -----------------------------------------------------------------
    // DATASET VERSION: DO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsDatasetVersion(DatasetVersion expected, DatasetVersion actual) throws MetamacException {
        if ((expected != null && actual == null) || (expected == null && actual != null)) {
            fail("The expected datasetVersion and the actual are not equals");
        } else if (expected != null && actual != null) {
            assertEqualsDatasetVersion(expected, actual, false);
        }
    }

    public static void assertEqualsDatasetVersionNotChecksDataset(DatasetVersion expected, DatasetVersion actual) throws MetamacException {
        if ((expected != null && actual == null) || (expected == null && actual != null)) {
            fail("The expected datasetVersion and the actual are not equals");
        } else if (expected != null && actual != null) {
            assertEqualsDatasetVersion(expected, actual, true);
        }
    }

    private static void assertEqualsDatasetVersion(DatasetVersion expected, DatasetVersion actual, boolean datasetChecked) throws MetamacException {
        assertEqualsSiemacMetadataStatisticalResource(expected.getSiemacMetadataStatisticalResource(), actual.getSiemacMetadataStatisticalResource());

        assertEqualsDatasetVersionMetadata(expected, actual);

        if (!datasetChecked) {
            assertEqualsDataset(expected.getDataset(), actual.getDataset());
        }
    }

    public static void assertEqualsDatasetVersionCollection(Collection<DatasetVersion> expected, Collection<DatasetVersion> actual) {
        if (expected != null) {
            assertNotNull(actual);
            assertEquals(expected.size(), actual.size());
            for (DatasetVersion expectedItem : expected) {
                if (!actual.contains(expectedItem)) {
                    fail("Found elements in expected collection, which are not contained in actual collection");
                }
            }
        } else {
            assertNull(actual);
        }
    }

    private static void assertEqualsDatasetVersionMetadata(DatasetVersion expected, DatasetVersion actual) {
        assertEqualsExternalItem(expected.getRelatedDsd(), actual.getRelatedDsd());
        assertEqualsExternalItemCollection(expected.getGeographicCoverage(), actual.getGeographicCoverage());
        assertEqualsTemporalCodeCollection(expected.getTemporalCoverage(), actual.getTemporalCoverage());
        assertEqualsExternalItemCollection(expected.getMeasureCoverage(), actual.getMeasureCoverage());
        assertEqualsExternalItemCollection(expected.getGeographicGranularities(), actual.getGeographicGranularities());
        assertEqualsExternalItemCollection(expected.getTemporalGranularities(), actual.getTemporalGranularities());
        assertEqualsExternalItemList(expected.getStatisticalUnit(), actual.getStatisticalUnit());
        assertEqualsExternalItem(expected.getUpdateFrequency(), actual.getUpdateFrequency());

        assertEqualsDate(expected.getDateNextUpdate(), actual.getDateNextUpdate());

        assertEqualsDate(expected.getDateStart(), actual.getDateStart());
        assertEqualsDate(expected.getDateEnd(), actual.getDateEnd());

        assertEquals(expected.getFormatExtentDimensions(), actual.getFormatExtentDimensions());
        assertEquals(expected.getFormatExtentObservations(), actual.getFormatExtentObservations());

        assertEqualsCodeDimensionsCollection(expected.getDimensionsCoverage(), actual.getDimensionsCoverage());

        assertEqualsAttributeValuesCollection(expected.getAttributesCoverage(), actual.getAttributesCoverage());

        assertEqualsStatisticOfficiality(expected.getStatisticOfficiality(), actual.getStatisticOfficiality());

        assertEqualsInternationalString(expected.getBibliographicCitation(), actual.getBibliographicCitation());

        assertEquals(expected.isKeepAllData(), actual.isKeepAllData());

    }

    // -----------------------------------------------------------------
    // DATASET: DTO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsDataset(DatasetVersion entity, RelatedResourceDto dto) throws MetamacException {
        assertNotNull(entity.getDataset().getId());
        assertEquals(entity.getDataset().getId(), dto.getId());

        assertNotNull(entity.getDataset().getVersion());
        assertEquals(entity.getDataset().getVersion(), dto.getVersion());

        assertEquals(TypeRelatedResourceEnum.DATASET, dto.getType());
        assertEquals(entity.getDataset().getIdentifiableStatisticalResource().getCode(), dto.getCode());
        assertNull(dto.getCodeNested());
        assertEquals(entity.getDataset().getIdentifiableStatisticalResource().getUrn(), dto.getUrn());
        assertEqualsInternationalString(entity.getSiemacMetadataStatisticalResource().getTitle(), dto.getTitle());
    }

    // -----------------------------------------------------------------
    // DATASET VERSION: DTO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsDatasetVersion(DatasetVersion entity, DatasetVersionDto dto) throws MetamacException {
        assertEqualsDatasetVersion(dto, entity, MapperEnum.DO2DTO);
    }

    public static void assertEqualsDatasetVersion(DatasetVersionDto dto, DatasetVersion entity) throws MetamacException {
        assertEqualsDatasetVersion(dto, entity, MapperEnum.DTO2DO);
    }

    private static void assertEqualsDatasetVersion(DatasetVersionDto dto, DatasetVersion entity, MapperEnum mapperEnum) throws MetamacException {
        assertEqualsSiemacMetadataStatisticalResource(entity.getSiemacMetadataStatisticalResource(), dto, mapperEnum);

        // Dataset attributes

        switch (mapperEnum) {
            case DO2DTO:
                assertEquals(entity.getId(), dto.getId());
                assertEquals(entity.getVersion(), dto.getVersion());

                assertEquals(entity.getDatasetRepositoryId(), dto.getDatasetRepositoryId());

                assertEqualsExternalItemCollectionMapper(entity.getTemporalGranularities(), dto.getTemporalGranularities());
                assertEqualsExternalItemCollectionMapper(entity.getGeographicGranularities(), dto.getGeographicGranularities());
                assertEqualsExternalItemCollectionMapper(entity.getStatisticalUnit(), dto.getStatisticalUnit());

                assertEqualsDate(entity.getDateStart(), dto.getDateStart());
                assertEqualsDate(entity.getDateEnd(), dto.getDateEnd());
                assertEqualsDate(entity.getDateNextUpdate(), dto.getDateNextUpdate());

                assertEqualsStatisticOfficiality(entity.getStatisticOfficiality(), dto.getStatisticOfficiality());
                assertEqualsExternalItem(entity.getRelatedDsd(), dto.getRelatedDsd(), mapperEnum);
                assertEqualsExternalItem(entity.getUpdateFrequency(), dto.getUpdateFrequency(), mapperEnum);
                assertEquals(entity.getFormatExtentDimensions(), dto.getFormatExtentDimensions());
                assertEquals(entity.getFormatExtentObservations(), dto.getFormatExtentObservations());

                assertEqualsInternationalString(entity.getBibliographicCitation(), dto.getBibliographicCitation());
                assertEquals(entity.isKeepAllData(), dto.isKeepAllData());
                assertEquals(entity.getDataset().getViewCode(), dto.getViewCode());
                break;
            case DTO2DO:
                assertEqualsExternalItemCollectionMapper(entity.getStatisticalUnit(), dto.getStatisticalUnit());
                assertEqualsExternalItem(entity.getRelatedDsd(), dto.getRelatedDsd(), mapperEnum);
                assertEqualsExternalItem(entity.getUpdateFrequency(), dto.getUpdateFrequency(), mapperEnum);
                assertEqualsStatisticOfficiality(entity.getStatisticOfficiality(), dto.getStatisticOfficiality());
                assertEquals(entity.isKeepAllData(), dto.isKeepAllData());
                break;
        }
    }

    public static void assertEqualsDatasetVersionBase(DatasetVersion entity, DatasetVersionBaseDto dto) throws MetamacException {
        assertEqualsDatasetVersionBase(dto, entity, MapperEnum.DO2DTO);
    }

    public static void assertEqualsDatasetVersionBase(DatasetVersionBaseDto dto, DatasetVersion entity) throws MetamacException {
        assertEqualsDatasetVersionBase(dto, entity, MapperEnum.DTO2DO);
    }

    private static void assertEqualsDatasetVersionBase(DatasetVersionBaseDto dto, DatasetVersion entity, MapperEnum mapperEnum) throws MetamacException {
        assertEqualsSiemacMetadataStatisticalResourceBase(entity.getSiemacMetadataStatisticalResource(), dto, mapperEnum);

        // Dataset attributes
        assertEqualsStatisticOfficiality(entity.getStatisticOfficiality(), dto.getStatisticOfficiality());
        assertEqualsExternalItem(entity.getRelatedDsd(), dto.getRelatedDsd(), mapperEnum);

        switch (mapperEnum) {
            case DO2DTO:
                assertEquals(entity.getId(), dto.getId());
                assertEquals(entity.getVersion(), dto.getVersion());
                break;
            case DTO2DO:
                break;
            default:
                break;
        }
    }

    // -----------------------------------------------------------------
    // DatasetVersionMainCoverages
    // -----------------------------------------------------------------
    public static void assertEqualsDatasetVersionMainCoverages(DatasetVersionMainCoveragesDto mainCoverages, List<ExternalItem> geographicCoverage, List<TemporalCode> temporalCoverage,
            List<ExternalItem> measureCoverage) {
        assertEqualsExternalItemCollectionMapper(geographicCoverage, mainCoverages.getGeographicCoverage());
        assertEqualsTemporalCodeCollectionMapper(temporalCoverage, mainCoverages.getTemporalCoverage());
        assertEqualsExternalItemCollectionMapper(measureCoverage, mainCoverages.getMeasureCoverage());
    }

    // -----------------------------------------------------------------
    // DATASOURCE: DO & DO
    // -----------------------------------------------------------------
    public static void assertEqualsDatasource(Datasource expected, Datasource actual) throws MetamacException {
        assertEqualsIdentifiableStatisticalResource(expected.getIdentifiableStatisticalResource(), actual.getIdentifiableStatisticalResource());
        assertEqualsDatasetVersion(expected.getDatasetVersion(), actual.getDatasetVersion());
    }

    public static void assertEqualsDatasourceCollection(Collection<Datasource> expected, Collection<Datasource> actual) {
        if (expected != null) {
            assertNotNull(actual);
            assertEquals(expected.size(), actual.size());
            for (Datasource expectedItem : expected) {
                if (!actual.contains(expectedItem)) {
                    fail("Found elements in expected collection, which are not contained in actual collection");
                }
            }
        } else {
            assertNull(actual);
        }
    }

    // -----------------------------------------------------------------
    // DATASOURCE: DTO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsDatasource(Datasource entity, DatasourceDto dto) {
        assertEqualsDatasource(dto, entity, MapperEnum.DO2DTO);
    }

    public static void assertEqualsDatasource(DatasourceDto dto, Datasource entity) {
        assertEqualsDatasource(dto, entity, MapperEnum.DTO2DO);
    }

    public static void assertEqualsDatasourceDoAndDtoCollection(List<Datasource> expected, List<DatasourceDto> actual) {
        assertEqualsDatasourceCollection(actual, expected, MapperEnum.DO2DTO);
    }

    public static void assertEqualsDatasourceDtoAndDoCollection(List<DatasourceDto> expected, List<Datasource> actual) {
        assertEqualsDatasourceCollection(expected, actual, MapperEnum.DTO2DO);
    }

    private static void assertEqualsDatasource(DatasourceDto dto, Datasource entity, MapperEnum mapperEnum) {
        assertEqualsIdentifiableStatisticalResource(entity.getIdentifiableStatisticalResource(), dto, mapperEnum);

        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            assertEquals(entity.getId(), dto.getId());

            assertNotNull(entity.getVersion());
            assertEquals(entity.getVersion(), dto.getVersion());

            assertEquals(entity.getDatasetVersion().getSiemacMetadataStatisticalResource().getUrn(), dto.getDatasetVersionUrn());
        }
    }

    private static void assertEqualsDatasourceCollection(List<DatasourceDto> dtos, List<Datasource> entities, MapperEnum mapperEnum) {
        if (dtos != null) {
            assertNotNull(entities);
            assertEquals(dtos.size(), entities.size());
            for (DatasourceDto expectedItem : dtos) {
                boolean match = false;
                for (Datasource actualItem : entities) {
                    try {
                        assertEqualsDatasource(expectedItem, actualItem, mapperEnum);
                        match = true;
                    } catch (AssertionError e) {
                        continue;
                    }
                }

                if (!match) {
                    fail("Found elements in expected collection, which are not contained in actual collection");
                }
            }
        } else {
            assertNull(entities);
        }
    }

    // -----------------------------------------------------------------
    // ATTRIBUTES
    // -----------------------------------------------------------------

    public static void assertEqualsAttributeDtoAndDsdAttributeInstaceDto(AttributeInstanceDto attributeInstanceDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        assertEquals(attributeInstanceDto.getAttributeId(), attributeInstanceDto.getAttributeId());
        assertEqualsAttributeValues(attributeInstanceDto.getValue(), dsdAttributeInstanceDto.getValue());
        assertEqualsCodesByDimensionMap(attributeInstanceDto.getCodesByDimension(), dsdAttributeInstanceDto.getCodeDimensions());
    }

    private static void assertEqualsAttributeValues(InternationalStringDto internationalStringDto, AttributeValueDto attributeValueDto) {
        if (attributeValueDto != null) {
            assertNotNull(internationalStringDto);
            String localisedAttributeValue = internationalStringDto.getLocalisedLabel(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE);
            String attributeInstaceValue = null;
            if (StringUtils.isNotBlank(attributeValueDto.getStringValue())) {
                attributeInstaceValue = attributeValueDto.getStringValue();
            } else if (attributeValueDto.getExternalItemValue() != null) {
                attributeInstaceValue = attributeValueDto.getExternalItemValue().getCode();
            }
            assertEquals(localisedAttributeValue, attributeInstaceValue);
        } else {
            assertNull(internationalStringDto);
        }
    }

    private static void assertEqualsCodesByDimensionMap(Map<String, List<String>> stringMap, Map<String, List<CodeItemDto>> codeItemMap) {
        assertEquals(stringMap.size(), codeItemMap.size());
        for (String stringMapKey : stringMap.keySet()) {
            assertTrue(codeItemMap.containsKey(stringMapKey));
            assertEqualsCodeItemListAndStringList(codeItemMap.get(stringMapKey), stringMap.get(stringMapKey));
        }
    }

    private static void assertEqualsCodeItemListAndStringList(List<CodeItemDto> codeItemList, List<String> stringList) {
        assertEquals(codeItemList.size(), stringList.size());
        HashSet<String> stringSet = new HashSet<String>(stringList);
        for (CodeItemDto codeItemDto : codeItemList) {
            assertTrue(stringSet.contains(codeItemDto.getCode()));
        }
    }

    // -----------------------------------------------------------------
    // CATEGORISATION: DO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsCategorisation(Categorisation expected, Categorisation actual) throws MetamacException {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEqualsVersionableStatisticalResource(expected.getVersionableStatisticalResource(), actual.getVersionableStatisticalResource());
        assertEqualsDatasetVersion(expected.getDatasetVersion(), actual.getDatasetVersion());
        assertEqualsExternalItem(expected.getCategory(), actual.getCategory());
        assertEqualsExternalItem(expected.getMaintainer(), actual.getMaintainer());
    }

    public static void assertEqualsCategorisation(Categorisation entity, CategorisationDto dto) {
        assertEqualsCategorisation(dto, entity, MapperEnum.DO2DTO);
    }

    public static void assertEqualsCategorisation(CategorisationDto dto, Categorisation entity) {
        assertEqualsCategorisation(dto, entity, MapperEnum.DTO2DO);
    }

    public static void assertEqualsCategorisationDoAndDtoCollection(List<Categorisation> expected, List<CategorisationDto> actual) {
        assertEqualsCategorisationCollection(actual, expected, MapperEnum.DO2DTO);
    }

    public static void assertEqualsCategorisationDtoAndDoCollection(List<CategorisationDto> expected, List<Categorisation> actual) {
        assertEqualsCategorisationCollection(expected, actual, MapperEnum.DTO2DO);
    }

    private static void assertEqualsCategorisation(CategorisationDto dto, Categorisation entity, MapperEnum mapperEnum) {
        assertEqualsVersionableStatisticalResource(entity.getVersionableStatisticalResource(), dto, mapperEnum);

        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            assertEquals(entity.getId(), dto.getId());

            assertNotNull(entity.getVersion());
            assertEquals(entity.getVersion(), dto.getVersion());
            assertEquals(entity.getDatasetVersion().getSiemacMetadataStatisticalResource().getUrn(), dto.getDatasetVersion().getUrn());
        } else {
            assertNull(entity.getDatasetVersion());
        }
        assertEqualsExternalItem(entity.getCategory(), dto.getCategory(), mapperEnum);
        assertEqualsExternalItem(entity.getMaintainer(), dto.getMaintainer(), mapperEnum);
    }

    private static void assertEqualsCategorisationCollection(List<CategorisationDto> dtos, List<Categorisation> entities, MapperEnum mapperEnum) {
        if (dtos != null) {
            assertNotNull(entities);
            assertEquals(dtos.size(), entities.size());
            for (CategorisationDto expectedItem : dtos) {
                boolean match = false;
                for (Categorisation actualItem : entities) {
                    try {
                        assertEqualsCategorisation(expectedItem, actualItem, mapperEnum);
                        match = true;
                    } catch (AssertionError e) {
                        continue;
                    }
                }

                if (!match) {
                    fail("Found elements in expected collection, which are not contained in actual collection");
                }
            }
        } else {
            assertNull(entities);
        }
    }

    // -----------------------------------------------------------------
    // DIMENSION REPRESENTATION MAPPING
    // -----------------------------------------------------------------

    public static void assertEqualsDimensionRepresentationMapping(DimensionRepresentationMapping expected, DimensionRepresentationMapping actual) throws MetamacException {
        assertEquals(expected.getDataset().getIdentifiableStatisticalResource().getUrn(), actual.getDataset().getIdentifiableStatisticalResource().getUrn());
        assertEquals(expected.getDatasourceFilename(), actual.getDatasourceFilename());
        assertEquals(expected.getMapping(), actual.getMapping());
    }

    public static void assertEqualsDimensionRepresentationMapping(DimensionRepresentationMapping expected, DimensionRepresentationMappingDto actual) throws MetamacException {
        assertEquals(expected.getDatasourceFilename(), actual.getDatasourceFilename());
        assertEquals(DatasetVersionUtils.dimensionRepresentationMapFromString(expected.getMapping()), actual.getMapping());
    }
}
