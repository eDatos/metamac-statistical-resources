package org.siemac.metamac.statistical.resources.core.utils.asserts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.core.common.constants.CoreCommonConstants.API_LATEST;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.siemac.metamac.common.test.constants.ConfigurationMockConstants;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.constants.shared.RegularExpressionConstants;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.enume.utils.TypeExternalArtefactsEnumUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.MetamacReflectionUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.HasLifecycle;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.common.utils.RelatedResourceUtils;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.TemporalCodeDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesCollectionUtils;

import javassist.ClassPool;
import javassist.CtMethod;
import javassist.NotFoundException;

public class CommonAsserts extends MetamacAsserts {

    // -----------------------------------------------------------------
    // METHOD IS EMPTY
    // -----------------------------------------------------------------

    public static void assertEmptyMethod(String className, String methodName) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        CtMethod method = pool.getMethod(className, methodName);
        boolean result = method.isEmpty();
        assertTrue(result);
    }

    // -----------------------------------------------------------------
    // INTERNATIONAL STRING: DO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsInternationalString(InternationalString expected, InternationalString actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }

        assertEquals(expected.getTexts().size(), actual.getTexts().size());
        for (LocalisedString localisedStringExpected : expected.getTexts()) {
            assertEquals(localisedStringExpected.getLabel(), actual.getLocalisedLabel(localisedStringExpected.getLocale()));
        }
    }

    public static void assertEqualsInternationalString(InternationalString internationalString, String locale1, String label1, String locale2, String label2) {
        int count = 0;
        if (locale1 != null) {
            assertEquals(label1, internationalString.getLocalisedLabel(locale1));
            count++;
        }
        if (locale2 != null) {
            assertEquals(label2, internationalString.getLocalisedLabel(locale2));
            count++;
        }
        assertEquals(count, internationalString.getTexts().size());
    }

    // -----------------------------------------------------------------
    // INTERNATIONAL STRING: DTO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsInternationalString(InternationalString entity, InternationalStringDto dto) {
        assertEqualsNullability(entity, dto);
        if (entity == null) {
            return;
        }

        assertEquals(entity.getTexts().size(), dto.getTexts().size());
        for (LocalisedString localisedStringExpected : entity.getTexts()) {
            assertEquals(localisedStringExpected.getLabel(), dto.getLocalisedLabel(localisedStringExpected.getLocale()));
        }
    }

    // -----------------------------------------------------------------
    // RELATED RESOURCE RESULT: DO & DATASTE VERSION
    // -----------------------------------------------------------------
    public static void assertEqualsRelatedResourceResultLifecycleResource(LifeCycleStatisticalResource expected, RelatedResourceResult actual, TypeRelatedResourceEnum type) throws MetamacException {

        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }

        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getUrn(), actual.getUrn());
        assertEquals(expected.getStatisticalOperation().getCode(), actual.getStatisticalOperationCode());
        assertEquals(expected.getStatisticalOperation().getUrn(), actual.getStatisticalOperationUrn());
        assertEquals(expected.getMaintainer().getCodeNested(), actual.getMaintainerNestedCode());
        assertEquals(type, actual.getType());

        assertEquals(expected.getTitle().getLocales().size(), actual.getTitle().size());

        for (String locale : expected.getTitle().getLocales()) {
            assertTrue(actual.getTitle().containsKey(locale));
            assertEquals(expected.getTitle().getLocalisedLabel(locale), actual.getTitle().get(locale));
        }
    }

    public static void assertEqualsRelatedResourceCollectionResultLifecycleResource(Collection<? extends HasLifecycle> expected, Collection<RelatedResourceResult> actual, TypeRelatedResourceEnum type)
            throws MetamacException {

        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }

        assertEquals(expected.size(), actual.size());

        for (HasLifecycle expect : expected) {
            boolean found = false;
            for (RelatedResourceResult current : actual) {
                try {
                    assertEqualsRelatedResourceResultLifecycleResource(expect.getLifeCycleStatisticalResource(), current, type);
                    found = true;
                } catch (AssertionError e) {
                }
            }
            if (!found) {
                Assert.fail("Found element in expected collection which is not contained in actual collection " + expect.getLifeCycleStatisticalResource().getUrn());
            }
        }

        for (RelatedResourceResult current : actual) {
            boolean found = false;
            for (HasLifecycle expect : expected) {
                try {
                    assertEqualsRelatedResourceResultLifecycleResource(expect.getLifeCycleStatisticalResource(), current, type);
                    found = true;
                } catch (AssertionError e) {
                }
            }
            if (!found) {
                Assert.fail("Found element in actual collection which is not contained in expected collection " + current.getUrn());
            }
        }
    }

    public static void assertEqualsRelatedResourceResultDatasetVersion(DatasetVersion expected, RelatedResourceResult actual) throws MetamacException {
        assertEqualsRelatedResourceResultLifecycleResource(expected.getSiemacMetadataStatisticalResource(), actual, TypeRelatedResourceEnum.DATASET_VERSION);
    }

    public static void assertEqualsRelatedResourceResultCollectionToDatasetVersionCollection(Collection<DatasetVersion> expected, Collection<RelatedResourceResult> actual) throws MetamacException {
        assertEqualsRelatedResourceCollectionResultLifecycleResource(expected, actual, TypeRelatedResourceEnum.DATASET_VERSION);
    }

    public static void assertEqualsRelatedResourceResultQueryVersion(QueryVersion expected, RelatedResourceResult actual) throws MetamacException {
        assertEqualsRelatedResourceResultLifecycleResource(expected.getLifeCycleStatisticalResource(), actual, TypeRelatedResourceEnum.QUERY_VERSION);
    }

    public static void assertEqualsRelatedResourceResultCollectionToQueryVersionCollection(Collection<QueryVersion> expected, Collection<RelatedResourceResult> actual) throws MetamacException {
        assertEqualsRelatedResourceCollectionResultLifecycleResource(expected, actual, TypeRelatedResourceEnum.QUERY_VERSION);
    }

    public static void assertEqualsRelatedResourceResultPublicationVersion(PublicationVersion expected, RelatedResourceResult actual) throws MetamacException {
        assertEqualsRelatedResourceResultLifecycleResource(expected.getSiemacMetadataStatisticalResource(), actual, TypeRelatedResourceEnum.PUBLICATION_VERSION);
    }

    public static void assertEqualsRelatedResourceResultCollectionToPublicationVersionCollection(Collection<PublicationVersion> expected, Collection<RelatedResourceResult> actual)
            throws MetamacException {
        assertEqualsRelatedResourceCollectionResultLifecycleResource(expected, actual, TypeRelatedResourceEnum.PUBLICATION_VERSION);
    }

    // RELATED RESOURCE: DO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsRelatedResource(RelatedResource expected, RelatedResource actual) throws MetamacException {

        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }

        NameableStatisticalResource nameableResourceActual = RelatedResourceUtils.retrieveNameableResourceLinkedToRelatedResource(actual);
        NameableStatisticalResource nameableResourceExpec = RelatedResourceUtils.retrieveNameableResourceLinkedToRelatedResource(expected);

        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getId(), actual.getId());
        BaseAsserts.assertEqualsNameableStatisticalResource(nameableResourceExpec, nameableResourceActual);
    }

    public static void assertEqualsRelatedResourceCollection(Collection<RelatedResource> expected, Collection<RelatedResource> actual) throws MetamacException {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.size(), actual.size());

        for (RelatedResource expec : expected) {
            boolean found = false;
            for (RelatedResource actualRes : actual) {
                if (actualRes.getType().equals(expec.getType())) {
                    IdentifiableStatisticalResource identifiableResourceActual = RelatedResourceUtils.retrieveIdentifiableResourceLinkedToRelatedResource(actualRes);
                    IdentifiableStatisticalResource identifiableResourceActualResourceExpec = RelatedResourceUtils.retrieveIdentifiableResourceLinkedToRelatedResource(expec);
                    if (identifiableResourceActual.getUrn().equals(identifiableResourceActualResourceExpec.getUrn())) {
                        found = true;
                    }
                }
            }
            if (!found) {
                Assert.fail("Found element in expected collection which is not contained in actual collection");
            }
        }
    }

    public static void assertEqualsRelatedResourceList(List<RelatedResource> expected, List<RelatedResource> actual) throws MetamacException {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertEqualsRelatedResource(expected.get(i), actual.get(i));
        }
    }

    // -----------------------------------------------------------------
    // RELATED RESOURCES : DTO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsRelatedResource(RelatedResource entity, RelatedResourceDto dto) throws MetamacException {

        assertEqualsNullability(entity, dto);
        if (entity == null) {
            return;
        }

        NameableStatisticalResource nameableResource = RelatedResourceUtils.retrieveNameableResourceLinkedToRelatedResource(entity);
        assertEquals(entity.getType(), dto.getType());
        assertEquals(nameableResource.getCode(), dto.getCode());
        assertEqualsInternationalString(nameableResource.getTitle(), dto.getTitle());
        assertEquals(nameableResource.getUrn(), dto.getUrn());
    }

    public static void assertEqualsRelatedResourceCollectionMapper(Collection<RelatedResource> entities, Collection<RelatedResourceDto> dtos) throws MetamacException {
        if (entities == null) {
            entities = new ArrayList<RelatedResource>();
        }
        if (dtos == null) {
            dtos = new ArrayList<RelatedResourceDto>();
        }

        assertEquals(entities.size(), dtos.size());
        for (RelatedResource entity : entities) {
            boolean found = false;
            Iterator<RelatedResourceDto> itDto = dtos.iterator();
            while (itDto.hasNext() && !found) {
                RelatedResourceDto dto = itDto.next();
                found = true;
                try {
                    assertEqualsRelatedResource(entity, dto);
                } catch (AssertionError e) {
                    found = false;
                }
            }
            if (!found) {
                Assert.fail("Not equal collections");
            }
        }
    }

    // -----------------------------------------------------------------
    // TEMPORAL CODE: DO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsTemporalCodeCollection(Collection<TemporalCode> expected, Collection<TemporalCode> actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.size(), actual.size());

        for (TemporalCode expec : expected) {
            boolean found = false;
            for (TemporalCode actualItem : actual) {
                if (actualItem.getIdentifier().equals(expec.getIdentifier())) {
                    assertEquals(expec.getTitle(), actualItem.getTitle());
                    found = true;
                }
            }
            if (!found) {
                Assert.fail("Found element in expected collection which is not contained in actual collection");
            }
        }
    }

    // -----------------------------------------------------------------
    // TEMPORAL CODE: DTO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsTemporalCode(TemporalCode entity, TemporalCodeDto dto) {
        assertEqualsNullability(entity, dto);
        if (entity == null) {
            return;
        }

        assertEquals(entity.getIdentifier(), dto.getIdentifier());
        assertEquals(entity.getTitle(), dto.getTitle());
    }

    public static void assertEqualsTemporalCodeCollectionMapper(Collection<TemporalCode> entities, Collection<TemporalCodeDto> dtos) {
        if (entities == null) {
            entities = new ArrayList<TemporalCode>();
        }

        if (dtos == null) {
            dtos = new ArrayList<TemporalCodeDto>();
        }

        assertEquals(entities.size(), dtos.size());
        for (TemporalCode entity : entities) {
            boolean found = false;
            Iterator<TemporalCodeDto> itDto = dtos.iterator();
            while (itDto.hasNext() && !found) {
                TemporalCodeDto dto = itDto.next();
                found = true;
                try {
                    assertEqualsTemporalCode(entity, dto);
                } catch (AssertionError e) {
                    found = false;
                }
            }
            if (!found) {
                Assert.fail("Not equal collections");
            }
        }
    }

    // -----------------------------------------------------------------
    // EXTERNAL ITEMS: DO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsExternalItem(ExternalItem expected, ExternalItem actual) {

        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }

        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getCodeNested(), actual.getCodeNested());
        assertEquals(expected.getUri(), actual.getUri());
        assertEquals(expected.getUrn(), actual.getUrn());
        assertEquals(expected.getUrnProvider(), actual.getUrnProvider());
        assertEquals(expected.getType(), actual.getType());
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getManagementAppUrl(), actual.getManagementAppUrl());
    }

    // CAUTION: objects must implement equals!
    public static void assertEqualsExternalItemCollection(Collection<ExternalItem> expected, Collection<ExternalItem> actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.size(), actual.size());

        for (ExternalItem expec : expected) {
            boolean found = false;
            for (ExternalItem actualItem : actual) {
                if (StringUtils.equals(actualItem.getUrn(), expec.getUrn()) || StringUtils.equals(actualItem.getUrnProvider(), expec.getUrnProvider())) {
                    found = true;
                }
            }
            if (!found) {
                Assert.fail("Found element in expected collection which is not contained in actual collection");
            }
        }
    }

    public static void assertEqualsExternalItemList(List<ExternalItem> expected, List<ExternalItem> actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertEqualsExternalItem(expected.get(i), actual.get(i));
        }
    }

    // -----------------------------------------------------------------
    // EXTERNAL ITEMS: DTO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsExternalItem(ExternalItem entity, ExternalItemDto dto, MapperEnum mapperEnum) {
        assertEqualsNullability(entity, dto);
        if (entity == null) {
            return;
        }

        String baseApi = null;
        String baseWebApplication = null;

        if (TypeExternalArtefactsEnumUtils.isExternalItemOfCommonMetadataApp(dto.getType())) {
            baseWebApplication = ConfigurationMockConstants.COMMON_METADATA_INTERNAL_WEB_APP_URL_BASE;
            baseApi = ConfigurationMockConstants.COMMON_METADATA_EXTERNAL_API_URL_BASE;
        } else if (TypeExternalArtefactsEnumUtils.isExternalItemOfStatisticalOperationsApp(dto.getType())) {
            baseWebApplication = ConfigurationMockConstants.STATISTICAL_OPERATIONS_INTERNAL_WEB_APP_URL_BASE;
            baseApi = ConfigurationMockConstants.STATISTICAL_OPERATIONS_INTERNAL_API_URL_BASE;
        } else if (TypeExternalArtefactsEnumUtils.isExternalItemOfSrmApp(dto.getType())) {
            baseWebApplication = ConfigurationMockConstants.SRM_INTERNAL_WEB_APP_URL_BASE;
            baseApi = ConfigurationMockConstants.SRM_INTERNAL_API_URL_BASE;
        } else {
            fail("unexpected type of external item");
        }

        assertEqualsExternalItem(entity, dto, baseApi, baseWebApplication, mapperEnum);
    }

    private static void assertEqualsExternalItem(ExternalItem entity, ExternalItemDto dto, String baseApi, String baseWebApplication, MapperEnum mapperEnum) {
        assertEqualsExternalItem(entity, dto);

        assertEqualsNullability(entity.getUri(), dto.getUri());
        if (entity.getUri() != null) {
            if (MapperEnum.DO2DTO.equals(mapperEnum)) {
                assertEquals(baseApi + entity.getUri(), dto.getUri());
            } else if (MapperEnum.DTO2DO.equals(mapperEnum)) {
                String expectedDoUri = dto.getUri().replaceFirst(baseApi, StringUtils.EMPTY);
                expectedDoUri = expectedDoUri.replaceFirst(RegularExpressionConstants.API_VERSION_REG_EXP, API_LATEST);
                assertEquals(expectedDoUri, entity.getUri());
            } else {
                fail("Mapper unexpected: " + mapperEnum);
            }
        }

        assertEqualsNullability(entity.getManagementAppUrl(), dto.getManagementAppUrl());
        if (entity.getManagementAppUrl() != null) {
            if (MapperEnum.DO2DTO.equals(mapperEnum)) {
                assertEquals(baseWebApplication + entity.getManagementAppUrl(), dto.getManagementAppUrl());
            } else if (MapperEnum.DTO2DO.equals(mapperEnum)) {
                assertEquals(dto.getManagementAppUrl().replaceFirst(baseWebApplication, StringUtils.EMPTY), entity.getManagementAppUrl());
            } else {
                fail("Mapper unexpected: " + mapperEnum);
            }
        }
    }

    private static void assertEqualsExternalItem(ExternalItem entity, ExternalItemDto dto) {
        assertEquals(entity.getCode(), dto.getCode());
        assertEquals(entity.getCodeNested(), dto.getCodeNested());
        assertEquals(entity.getUrn(), dto.getUrn());
        assertEquals(entity.getUrnProvider(), dto.getUrnProvider());
        assertEquals(entity.getType(), dto.getType());
        assertEqualsInternationalString(entity.getTitle(), dto.getTitle());
    }

    public static void assertEqualsExternalItemCollectionMapper(Collection<ExternalItem> entities, Collection<ExternalItemDto> dtos) {
        if (entities == null) {
            entities = new ArrayList<ExternalItem>();
        }

        if (dtos == null) {
            dtos = new ArrayList<ExternalItemDto>();
        }

        assertEquals(entities.size(), dtos.size());
        for (ExternalItem entity : entities) {
            boolean found = false;
            Iterator<ExternalItemDto> itDto = dtos.iterator();
            while (itDto.hasNext() && !found) {
                ExternalItemDto dto = itDto.next();
                found = true;
                try {
                    assertEqualsExternalItem(entity, dto);
                } catch (AssertionError e) {
                    found = false;
                }
            }
            if (!found) {
                Assert.fail("Not equal collections");
            }
        }
    }

    // -----------------------------------------------------------------
    // OTHER
    // -----------------------------------------------------------------

    public static <T, R> void assertEqualsCollectionByField(Collection<T> expected, Collection<R> actual, String expectedFieldName, String actualFieldName) throws Exception {
        StatisticalResourcesCollectionUtils.equalsCollectionByField(expected, actual, expectedFieldName, actualFieldName);
    }

    protected static void assertRelaxedEqualsObject(Object expected, Object actual) {
        if ((expected != null && actual == null) || (expected == null && actual != null)) {
            fail("The expected object and the actual are not equals");
        } else if (expected != null && actual != null) {
            Long expectedId = MetamacReflectionUtils.retrieveObjectId(expected);
            Long actualId = MetamacReflectionUtils.retrieveObjectId(actual);
            assertEquals(expectedId, actualId);
        }
    }
}
