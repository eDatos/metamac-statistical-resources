package org.siemac.metamac.statistical.resources.core.utils.asserts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;

public class QueryAsserts extends BaseAsserts {

    // -----------------------------------------------------------------
    // QUERY VERSION: DO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsQuery(Query expected, Query actual) {
        assertEqualsIdentifiableStatisticalResource(expected.getIdentifiableStatisticalResource(), actual.getIdentifiableStatisticalResource());
        assertEquals(expected.getLatestDataNumber(), actual.getLatestDataNumber());

        if (expected.getVersions() != null) {
            assertNotNull(actual.getVersions());
            assertEquals(expected.getVersions().size(), actual.getVersions().size());
            assertEqualsQueryVersionCollection(expected.getVersions(), actual.getVersions());
        } else {
            assertEquals(null, actual);
        }
    }

    public static void assertEqualsQueryVersion(QueryVersion expected, QueryVersion actual) throws MetamacException {
        if ((expected != null && actual == null) || (expected == null && actual != null)) {
            fail("The expected queryVersion and the actual are not equals");
        } else if (expected != null && actual != null) {
            assertEqualsQueryVersion(expected, actual, false);
        }
    }

    private static void assertEqualsQueryVersion(QueryVersion expected, QueryVersion actual, boolean queryChecked) throws MetamacException {
        assertEqualsLifeCycleStatisticalResource(expected.getLifeCycleStatisticalResource(), actual.getLifeCycleStatisticalResource());
        DatasetsAsserts.assertEqualsDatasetVersion(expected.getFixedDatasetVersion(), actual.getFixedDatasetVersion());
        DatasetsAsserts.assertEqualsDataset(expected.getDataset(), actual.getDataset());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getLatestDataNumber(), actual.getLatestDataNumber());
        assertEqualsSelection(expected.getSelection(), actual.getSelection());

        if (!queryChecked) {
            assertEqualsQuery(expected.getQuery(), actual.getQuery());
        }
    }

    public static void assertEqualsSelection(List<QuerySelectionItem> expected, List<QuerySelectionItem> actual) {
        assertEqualsNullability(expected, actual);

        if (expected != null) {
            assertEquals(expected.size(), actual.size());
            for (QuerySelectionItem expectedSelectionItem : expected) {
                boolean found = false;
                for (QuerySelectionItem actualSelectionItem : actual) {
                    try {
                        assertEqualsQueryVersionSelectionItem(expectedSelectionItem, actualSelectionItem);
                        found = true;
                        break;
                    } catch (AssertionError e) {
                        found = false;
                    }
                }
                if (!found) {
                    fail("Dimensions found in entity.getSelection, thar are not in actual dto.getSelection");
                }
            }
        }
    }

    private static void assertEqualsQueryVersionSelectionItem(QuerySelectionItem expectedSelectionItem, QuerySelectionItem actualSelectionItem) {
        assertEqualsNullability(expectedSelectionItem, actualSelectionItem);

        if (expectedSelectionItem != null) {
            assertEquals(expectedSelectionItem.getDimension(), actualSelectionItem.getDimension());
            for (CodeItem expectedCodeItem : expectedSelectionItem.getCodes()) {
                boolean found = false;
                for (CodeItem actualCodeItem : actualSelectionItem.getCodes()) {
                    if (expectedCodeItem.getCode().equals(actualCodeItem.getCode())) {
                        found = true;
                    }
                }

                if (!found) {
                    fail("Codes found in a dimension of entity.getSelection that are not in the respective dimension of dto.getSelection");
                }
            }

        }

    }

    public static void assertEqualsQueryVersionCollection(Collection<QueryVersion> expected, Collection<QueryVersion> actual) {
        if (expected != null) {
            assertNotNull(actual);
            assertEquals(expected.size(), actual.size());
            for (QueryVersion q : expected) {
                if (!actual.contains(q)) {
                    fail("Found elements in expected collection, which are not contained in actual collection");
                }
            }
        } else {
            assertNull(actual);
        }
    }

    // -----------------------------------------------------------------
    // QUERY: DTO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsQuery(QueryVersion entity, RelatedResourceDto dto) throws MetamacException {
        assertNotNull(entity.getQuery().getId());
        assertEquals(entity.getQuery().getId(), dto.getId());

        assertNotNull(entity.getQuery().getVersion());
        assertEquals(entity.getQuery().getVersion(), dto.getVersion());

        assertEquals(TypeRelatedResourceEnum.QUERY, dto.getType());
        assertEquals(entity.getQuery().getIdentifiableStatisticalResource().getCode(), dto.getCode());
        assertNull(dto.getCodeNested());
        assertEquals(entity.getQuery().getIdentifiableStatisticalResource().getUrn(), dto.getUrn());
        assertEqualsInternationalString(entity.getLifeCycleStatisticalResource().getTitle(), dto.getTitle());
    }

    // -----------------------------------------------------------------
    // QUERY VERSION: DTO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsQueryVersion(QueryVersion entity, QueryVersionDto dto) {
        assertEqualsQueryVersion(entity, dto, MapperEnum.DO2DTO);
    }

    public static void assertEqualsQueryVersion(QueryVersionDto dto, QueryVersion entity) {
        assertEqualsQueryVersion(entity, dto, MapperEnum.DTO2DO);
    }

    public static void assertEqualsQuerySelection(Map<String, List<CodeItemDto>> dtos, List<QuerySelectionItem> entities) {
        assertEqualsSelection(entities, dtos, MapperEnum.DTO2DO);
    }

    public static void assertEqualsQuerySelection(List<QuerySelectionItem> entities, Map<String, List<CodeItemDto>> dtos) {
        assertEqualsSelection(entities, dtos, MapperEnum.DO2DTO);
    }

    public static void assertEqualsQueryVersionDoAndDtoCollection(Collection<QueryVersion> expected, Collection<QueryVersionBaseDto> actual) {
        assertEqualsQueryVersionCollection(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsQueryVersionDtoAndDoCollection(Collection<QueryVersionBaseDto> expected, Collection<QueryVersion> actual) {
        assertEqualsQueryVersionCollection(actual, expected, MapperEnum.DTO2DO);
    }

    private static void assertEqualsQueryVersionCollection(Collection<QueryVersion> entities, Collection<QueryVersionBaseDto> dtos, MapperEnum mapperEnum) {
        if (entities != null) {
            assertNotNull(dtos);
            assertEquals(entities.size(), dtos.size());
            for (QueryVersion expectedItem : entities) {
                boolean match = false;
                for (QueryVersionBaseDto actualItem : dtos) {
                    try {
                        assertEqualsQueryVersionBase(expectedItem, actualItem, mapperEnum);
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
            assertNull(dtos);
        }
    }

    private static void assertEqualsQueryVersion(QueryVersion entity, QueryVersionDto dto, MapperEnum mapperEnum) {
        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            assertEquals(entity.getId(), dto.getId());

            assertNotNull(entity.getVersion());
            assertEquals(entity.getVersion(), dto.getVersion());

            assertNotNull(entity.getStatus());
            assertEquals(entity.getStatus(), dto.getStatus());
        }
        assertEqualsNameableStatisticalResource(entity.getLifeCycleStatisticalResource(), dto, mapperEnum);
        assertEqualsRelatedDatasetVersionInQueryVersion(entity, dto.getRelatedDatasetVersion());

        assertNotNull(entity.getType());
        assertEquals(entity.getType(), dto.getType());

        assertEquals(entity.getLatestDataNumber(), dto.getLatestDataNumber());

        assertEqualsSelection(entity.getSelection(), dto.getSelection(), mapperEnum);
    }

    private static void assertEqualsQueryVersionBase(QueryVersion entity, QueryVersionBaseDto dto, MapperEnum mapperEnum) {
        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            assertEquals(entity.getId(), dto.getId());

            assertNotNull(entity.getVersion());
            assertEquals(entity.getVersion(), dto.getVersion());

            assertNotNull(entity.getStatus());
            assertEquals(entity.getStatus(), dto.getStatus());
        }
        assertEqualsNameableStatisticalResourceBase(entity.getLifeCycleStatisticalResource(), dto, mapperEnum);
        assertEqualsRelatedDatasetVersionInQueryVersion(entity, dto.getRelatedDatasetVersion());

        assertNotNull(entity.getType());
        assertEquals(entity.getType(), dto.getType());
    }

    private static void assertEqualsSelection(List<QuerySelectionItem> entitySelection, Map<String, List<CodeItemDto>> dtoSelection, MapperEnum mapperEnum) {
        assertEqualsNullability(entitySelection, dtoSelection);

        if (entitySelection != null) {
            assertEquals(entitySelection.size(), dtoSelection.size());
            for (QuerySelectionItem entitySelectionItem : entitySelection) {
                List<CodeItemDto> dtoSelectionItem = dtoSelection.get(entitySelectionItem.getDimension());
                if (dtoSelectionItem == null) {
                    fail("Dimensions found in entity.getSelection, thar are not in actual dto.getSelection");
                } else {
                    assertEquals(entitySelectionItem.getCodes().size(), dtoSelectionItem.size());

                    for (CodeItem entityCodeItem : entitySelectionItem.getCodes()) {
                        boolean found = false;
                        for (CodeItemDto dtoCodeItem : dtoSelectionItem) {
                            if (entityCodeItem.getCode().equals(dtoCodeItem.getCode())) {
                                assertEquals(entityCodeItem.getTitle(), dtoCodeItem.getTitle());
                                found = true;
                            }
                        }
                        if (!found) {
                            fail("Codes found in a dimension of entity.getSelection that are not in the respective dimension of dto.getSelection");
                        }
                    }
                }
            }
        }
    }

    // -----------------------------------------------------------------
    // QUERY VERSION: DTO & DTO
    // -----------------------------------------------------------------

    public static void assertEqualsQuerySelection(Map<String, List<CodeItemDto>> expected, Map<String, List<CodeItemDto>> actual) {
        assertEqualsNullability(expected, actual);

        if (expected != null) {
            assertEquals(expected.size(), actual.size());
            for (String dimId : expected.keySet()) {
                List<CodeItemDto> expectedCodes = expected.get(dimId);
                List<CodeItemDto> actualCodes = actual.get(dimId);
                assertEqualsCodeItemDtoCollection(expectedCodes, actualCodes);
            }
        }
    }

    public static void assertEqualsCodeItemDtoCollection(List<CodeItemDto> expected, List<CodeItemDto> actual) {
        if (expected != null) {
            assertNotNull(actual);
            assertEquals(expected.size(), actual.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEqualsCodeItemDto(expected.get(i), actual.get(i));
            }
        } else {
            assertNull(actual);
        }
    }

    private static void assertEqualsCodeItemDto(CodeItemDto expected, CodeItemDto actual) {
        assertEqualsNullability(expected, actual);
        if (expected != null) {
            assertEquals(expected.getCode(), actual.getCode());
            assertEquals(expected.getTitle(), actual.getTitle());
        }
    }

    
    // -----------------------------------------------------------------
    // DATASET VERSION: QUERY AND DATASETVERSION URN
    // -----------------------------------------------------------------
    private static void assertEqualsRelatedDatasetVersionInQueryVersion(QueryVersion entity, RelatedResourceDto relatedDataset) {
        DatasetVersion datasetLinkedToQuery = null;
        if (entity.getDataset() != null) {
            for (DatasetVersion dv : entity.getDataset().getVersions()) {
                if (dv.getSiemacMetadataStatisticalResource().getLastVersion()) {
                    datasetLinkedToQuery = dv;
                    break;
                }
            }
        } else if (entity.getFixedDatasetVersion() != null) {
            datasetLinkedToQuery = entity.getFixedDatasetVersion();
        }

        assertEquals(datasetLinkedToQuery.getSiemacMetadataStatisticalResource().getUrn(), relatedDataset.getUrn());
        assertEquals(TypeRelatedResourceEnum.DATASET_VERSION, relatedDataset.getType());
        assertEquals(datasetLinkedToQuery.getSiemacMetadataStatisticalResource().getCode(), relatedDataset.getCode());
        assertEqualsInternationalString(datasetLinkedToQuery.getSiemacMetadataStatisticalResource().getTitle(), relatedDataset.getTitle());

        assertEquals(datasetLinkedToQuery.getLifeCycleStatisticalResource().getLastVersion(), relatedDataset.getLastVersion());
        assertEquals(datasetLinkedToQuery.getLifeCycleStatisticalResource().getVersionLogic(), relatedDataset.getVersionLogic());

    }
}
