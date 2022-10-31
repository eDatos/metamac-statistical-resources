package org.siemac.metamac.statistical.resources.core.utils.asserts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.BooleanUtils;
import org.junit.Assert;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.StatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.dto.IdentifiableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.StatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionRationaleTypeDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionableStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.StatisticOfficialityDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;

public class BaseAsserts extends CommonAsserts {

    // -----------------------------------------------------------------
    // VERSIONING ASSERTS: DO & DO
    // -----------------------------------------------------------------

    public static void assertEqualsVersioningSiemacMetadata(SiemacMetadataStatisticalResource previous, SiemacMetadataStatisticalResource next) throws MetamacException {
        assertEqualsVersioningLifecycle(previous, next);

        // Not inherited
        assertNull(next.getNewnessUntilDate());
        assertNull(next.getReplaces());
        assertNull(next.getCopyrightedDate());

        // Inherited
        assertEqualsExternalItem(previous.getLanguage(), next.getLanguage());
        assertEqualsExternalItemCollection(previous.getLanguages(), next.getLanguages());

        assertEqualsExternalItemCollection(previous.getStatisticalOperationInstances(), next.getStatisticalOperationInstances());

        assertEqualsInternationalString(previous.getSubtitle(), next.getSubtitle());
        assertEqualsInternationalString(previous.getTitleAlternative(), next.getTitleAlternative());
        assertEqualsInternationalString(previous.getAbstractLogic(), next.getAbstractLogic());

        assertEqualsInternationalString(previous.getKeywords(), next.getKeywords());
        assertEquals(previous.getUserModifiedKeywords(), next.getUserModifiedKeywords());

        assertEquals(previous.getType(), next.getType());

        assertEqualsExternalItem(previous.getCommonMetadata(), next.getCommonMetadata());

        assertEqualsExternalItem(previous.getCreator(), next.getCreator());
        assertEqualsExternalItemCollection(previous.getContributor(), next.getContributor());
        assertEqualsDate(previous.getCreatedDate(), next.getCreatedDate());
        assertEqualsInternationalString(previous.getConformsTo(), next.getConformsTo());
        assertEqualsInternationalString(previous.getConformsToInternal(), next.getConformsToInternal());
        assertNotNull(next.getLastUpdate());
        assertTrue(next.getLastUpdate().isAfter(previous.getLastUpdate().getMillis()));

        assertEqualsExternalItemCollection(previous.getPublisher(), next.getPublisher());
        assertEqualsExternalItemCollection(previous.getPublisherContributor(), next.getPublisherContributor());
        assertEqualsExternalItemCollection(previous.getMediator(), next.getMediator());

        assertEqualsInternationalString(previous.getAccessRights(), next.getAccessRights());
    }

    public static void assertEqualsVersioningLifecycle(LifeCycleStatisticalResource previous, LifeCycleStatisticalResource next) {
        assertEqualsVersioningVersionable(previous, next);
        assertEquals(ProcStatusEnum.DRAFT, next.getProcStatus());

        assertNotNull(next.getCreationDate());
        assertFalse(previous.getCreationDate().equals(next.getCreationDate()));
        assertNotNull(next.getCreationUser());
        assertFalse(previous.getCreationUser().equals(next.getCreationUser()));

        assertNull(next.getProductionValidationDate());
        assertNull(next.getProductionValidationUser());
        assertNull(next.getDiffusionValidationDate());
        assertNull(next.getDiffusionValidationUser());
        assertNull(next.getRejectValidationDate());
        assertNull(next.getRejectValidationUser());
        assertNull(next.getPublicationDate());
        assertNull(next.getPublicationUser());

        assertNotNull(next.getReplacesVersion());

        assertEqualsExternalItem(previous.getMaintainer(), next.getMaintainer());
    }

    private static void assertEqualsVersioningVersionable(VersionableStatisticalResource previous, VersionableStatisticalResource next) {
        assertEqualsVersioningNameable(previous, next);

        assertNotNull(next.getVersionLogic());
        assertFalse(previous.getVersionLogic().equals(next.getVersionLogic()));

        assertEquals(0, next.getVersionRationaleTypes().size());
        assertNull(next.getVersionRationale());
        assertNull(next.getValidFrom());
        assertNull(next.getValidTo());
        assertEquals(previous.getNextVersion(), next.getNextVersion());
        assertNull(next.getNextVersionDate());
    }

    private static void assertEqualsVersioningNameable(NameableStatisticalResource previous, NameableStatisticalResource next) {
        assertEqualsVersioningIdentifiable(previous, next);

        assertEqualsInternationalString(previous.getTitle(), next.getTitle());
        assertEqualsInternationalString(previous.getDescription(), next.getDescription());
    }

    private static void assertEqualsVersioningIdentifiable(IdentifiableStatisticalResource previous, IdentifiableStatisticalResource next) {
        assertEqualsVersioningStatisticalResource(previous, next);

        assertEquals(previous.getCode(), next.getCode());
        assertNotNull(next.getUrn());
        assertFalse(next.getUrn().equals(previous.getUrn()));
    }

    private static void assertEqualsVersioningStatisticalResource(IdentifiableStatisticalResource previous, IdentifiableStatisticalResource next) {
        assertNotNull(next.getStatisticalOperation());
        assertEqualsExternalItem(previous.getStatisticalOperation(), next.getStatisticalOperation());
    }

    // -----------------------------------------------------------------
    // MAIN HERITANCE: DO & DO
    // -----------------------------------------------------------------

    protected static void assertEqualsSiemacMetadataStatisticalResource(SiemacMetadataStatisticalResource expected, SiemacMetadataStatisticalResource actual) throws MetamacException {
        assertEqualsExternalItem(expected.getLanguage(), actual.getLanguage());
        assertEqualsExternalItemList(expected.getLanguages(), actual.getLanguages());

        assertEqualsExternalItemCollection(expected.getStatisticalOperationInstances(), actual.getStatisticalOperationInstances());

        assertEqualsInternationalString(expected.getSubtitle(), actual.getSubtitle());
        assertEqualsInternationalString(expected.getTitleAlternative(), actual.getTitleAlternative());
        assertEqualsInternationalString(expected.getAbstractLogic(), actual.getAbstractLogic());
        assertEqualsInternationalString(expected.getKeywords(), actual.getKeywords());

        assertEquals(expected.getType(), actual.getType());

        assertEqualsExternalItem(expected.getCommonMetadata(), actual.getCommonMetadata());

        assertEqualsExternalItem(expected.getCreator(), actual.getCreator());
        assertEqualsExternalItemList(expected.getContributor(), actual.getContributor());
        assertEqualsDate(expected.getResourceCreatedDate(), actual.getResourceCreatedDate());
        assertEqualsInternationalString(expected.getConformsTo(), actual.getConformsTo());
        assertEqualsInternationalString(expected.getConformsToInternal(), actual.getConformsToInternal());

        assertEqualsExternalItemList(expected.getPublisher(), actual.getPublisher());
        assertEqualsExternalItemList(expected.getPublisherContributor(), actual.getPublisherContributor());
        assertEqualsExternalItemList(expected.getMediator(), actual.getMediator());
        assertEqualsDate(expected.getNewnessUntilDate(), actual.getNewnessUntilDate());

        assertEqualsRelatedResource(expected.getReplaces(), actual.getReplaces());
        assertEqualsRelatedResource(expected.getReplacesVersion(), actual.getReplacesVersion());

        assertEquals(expected.getCopyrightedDate(), actual.getCopyrightedDate());
        assertEqualsInternationalString(expected.getAccessRights(), actual.getAccessRights());

        assertEqualsLifeCycleStatisticalResource(expected, actual);
    }

    protected static void assertEqualsLifeCycleStatisticalResource(LifeCycleStatisticalResource expected, LifeCycleStatisticalResource actual) throws MetamacException {
        assertEquals(expected.getProcStatus(), actual.getProcStatus());

        assertEqualsDate(expected.getCreationDate(), actual.getCreationDate());
        assertEquals(expected.getCreationUser(), actual.getCreationUser());
        assertEqualsDate(expected.getProductionValidationDate(), actual.getProductionValidationDate());
        assertEquals(expected.getProductionValidationUser(), actual.getProductionValidationUser());
        assertEqualsDate(expected.getDiffusionValidationDate(), actual.getDiffusionValidationDate());
        assertEquals(expected.getDiffusionValidationUser(), actual.getDiffusionValidationUser());
        assertEqualsDate(expected.getRejectValidationDate(), actual.getRejectValidationDate());
        assertEquals(expected.getRejectValidationUser(), actual.getRejectValidationUser());
        assertEqualsDate(expected.getPublicationDate(), actual.getPublicationDate());
        assertEquals(expected.getPublicationUser(), actual.getPublicationUser());

        assertEqualsRelatedResource(expected.getReplacesVersion(), actual.getReplacesVersion());

        assertEqualsExternalItem(expected.getMaintainer(), actual.getMaintainer());

        assertEquals(expected.getPublicationStreamStatus(), actual.getPublicationStreamStatus());

        assertEqualsVersionableStatisticalResource(expected, actual);
    }

    protected static void assertEqualsVersionableStatisticalResource(VersionableStatisticalResource expected, VersionableStatisticalResource actual) {

        assertEquals(expected.getVersionLogic(), actual.getVersionLogic());
        assertEquals(expected.getNextVersionDate(), actual.getNextVersionDate());
        assertEquals(expected.getNextVersion(), actual.getNextVersion());
        assertEqualsVersionRationaleTypeCollection(expected.getVersionRationaleTypes(), actual.getVersionRationaleTypes());
        assertEqualsInternationalString(expected.getVersionRationale(), actual.getVersionRationale());
        assertEqualsDate(expected.getValidFrom(), actual.getValidFrom());
        assertEqualsDate(expected.getValidTo(), actual.getValidTo());

        assertEqualsNameableStatisticalResource(expected, actual);
    }

    protected static void assertEqualsNameableStatisticalResource(NameableStatisticalResource expected, NameableStatisticalResource actual) {
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        assertEqualsInternationalString(expected.getDescription(), actual.getDescription());

        assertEqualsIdentifiableStatisticalResource(expected, actual);
    }

    protected static void assertEqualsVersionedNameableStatisticalResourceForResourceThatChangesCode(NameableStatisticalResource expected, NameableStatisticalResource actual) {
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        assertEqualsInternationalString(expected.getDescription(), actual.getDescription());

        assertEqualsVersionedIdentifiableStatisticalResourceForResourceThatChangesCode(expected, actual);
    }

    protected static void assertEqualsVersionedNameableStatisticalResource(NameableStatisticalResource expected, NameableStatisticalResource actual) {
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        assertEqualsInternationalString(expected.getDescription(), actual.getDescription());

        assertEqualsVersionedIdentifiableStatisticalResource(expected, actual);
    }

    public static void assertEqualsIdentifiableStatisticalResource(IdentifiableStatisticalResource expected, IdentifiableStatisticalResource actual) {
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getUrn(), actual.getUrn());

        assertEqualsStatisticalResource(expected, actual);
    }

    public static void assertEqualsVersionedIdentifiableStatisticalResourceForResourceThatChangesCode(IdentifiableStatisticalResource expected, IdentifiableStatisticalResource actual) {
        assertEqualsStatisticalResource(expected, actual);
    }

    public static void assertEqualsVersionedIdentifiableStatisticalResource(IdentifiableStatisticalResource expected, IdentifiableStatisticalResource actual) {
        assertEquals(expected.getCode(), actual.getCode());

        assertEqualsStatisticalResource(expected, actual);
    }

    public static void assertEqualsStatisticalResource(StatisticalResource expected, StatisticalResource actual) {
        assertEqualsExternalItem(expected.getStatisticalOperation(), actual.getStatisticalOperation());
    }

    // -----------------------------------------------------------------
    // MAIN HERITANCE: DTO & DO
    // -----------------------------------------------------------------

    protected static void assertEqualsSiemacMetadataStatisticalResource(SiemacMetadataStatisticalResource entity, SiemacMetadataStatisticalResourceDto dto, MapperEnum mapperEnum)
            throws MetamacException {
        switch (mapperEnum) {
            case DO2DTO:
                assertEqualsExternalItem(entity.getLanguage(), dto.getLanguage(), mapperEnum);
                assertEqualsExternalItemCollectionMapper(entity.getLanguages(), dto.getLanguages());

                assertEqualsExternalItemCollectionMapper(entity.getStatisticalOperationInstances(), dto.getStatisticalOperationInstances());

                assertEqualsInternationalString(entity.getSubtitle(), dto.getSubtitle());
                assertEqualsInternationalString(entity.getTitleAlternative(), dto.getTitleAlternative());
                assertEqualsInternationalString(entity.getAbstractLogic(), dto.getAbstractLogic());
                assertEqualsInternationalString(entity.getKeywords(), dto.getKeywords());

                assertEquals(dto.getType(), entity.getType());

                assertEqualsExternalItem(entity.getCreator(), dto.getCreator(), mapperEnum);
                assertEqualsExternalItemCollectionMapper(entity.getContributor(), dto.getContributor());
                assertEqualsDate(entity.getResourceCreatedDate(), dto.getResourceCreatedDate());
                assertEqualsDate(entity.getLastUpdate(), dto.getLastUpdate());
                assertEqualsInternationalString(entity.getConformsTo(), dto.getConformsTo());
                assertEqualsInternationalString(entity.getConformsToInternal(), dto.getConformsToInternal());

                assertEqualsRelatedResource(entity.getReplaces(), dto.getReplaces());
                assertEqualsRelatedResource(entity.getReplacesVersion(), dto.getReplacesVersion());

                assertEqualsExternalItem(entity.getCommonMetadata(), dto.getCommonMetadata(), mapperEnum);
                assertEquals(entity.getCopyrightedDate(), dto.getCopyrightedDate());
                assertEqualsInternationalString(entity.getAccessRights(), dto.getAccessRights());
                break;

            case DTO2DO:
                assertEqualsExternalItem(entity.getLanguage(), dto.getLanguage(), mapperEnum);
                assertEqualsExternalItemCollectionMapper(entity.getLanguages(), dto.getLanguages());

                assertEqualsExternalItemCollectionMapper(entity.getStatisticalOperationInstances(), dto.getStatisticalOperationInstances());

                assertEqualsInternationalString(entity.getSubtitle(), dto.getSubtitle());
                assertEqualsInternationalString(entity.getTitleAlternative(), dto.getTitleAlternative());
                assertEqualsInternationalString(entity.getAbstractLogic(), dto.getAbstractLogic());
                assertEqualsInternationalString(entity.getKeywords(), dto.getKeywords());

                assertEqualsExternalItem(entity.getCreator(), dto.getCreator(), mapperEnum);
                assertEqualsExternalItemCollectionMapper(entity.getContributor(), dto.getContributor());
                assertEqualsInternationalString(entity.getConformsTo(), dto.getConformsTo());
                assertEqualsInternationalString(entity.getConformsToInternal(), dto.getConformsToInternal());

                assertEqualsRelatedResource(entity.getReplaces(), dto.getReplaces());

                assertEqualsExternalItem(entity.getCommonMetadata(), dto.getCommonMetadata(), mapperEnum);
                assertEqualsInternationalString(entity.getAccessRights(), dto.getAccessRights());

                break;
        }
        assertEqualsLifeCycleStatisticalResource(entity, dto, mapperEnum);
    }

    protected static void assertEqualsLifeCycleStatisticalResource(LifeCycleStatisticalResource entity, LifeCycleStatisticalResourceDto dto, MapperEnum mapperEnum) throws MetamacException {
        switch (mapperEnum) {
            case DO2DTO:
                assertEquals(entity.getEffectiveProcStatus(), dto.getProcStatus());
                boolean effectiveLastVersion = BooleanUtils.isTrue(entity.getLastVersion());
                assertEquals(effectiveLastVersion, dto.getLastVersion());

                assertEqualsDate(entity.getCreationDate(), dto.getCreationDate());
                assertEquals(entity.getCreationUser(), dto.getCreationUser());
                assertEqualsDate(entity.getProductionValidationDate(), dto.getProductionValidationDate());
                assertEquals(entity.getProductionValidationUser(), dto.getProductionValidationUser());
                assertEqualsDate(entity.getDiffusionValidationDate(), dto.getDiffusionValidationDate());
                assertEquals(entity.getDiffusionValidationUser(), dto.getDiffusionValidationUser());
                assertEqualsDate(entity.getRejectValidationDate(), dto.getRejectValidationDate());
                assertEquals(entity.getRejectValidationUser(), dto.getRejectValidationUser());
                assertEqualsDate(entity.getPublicationDate(), dto.getPublicationDate());
                assertEquals(entity.getPublicationUser(), dto.getPublicationUser());

                assertEqualsRelatedResource(entity.getReplacesVersion(), dto.getReplacesVersion());

                assertEqualsExternalItem(entity.getMaintainer(), dto.getMaintainer(), mapperEnum);
                break;
        }
        assertEqualsVersionableStatisticalResource(entity, dto, mapperEnum);
    }

    protected static void assertEqualsVersionableStatisticalResource(VersionableStatisticalResource entity, VersionableStatisticalResourceDto dto, MapperEnum mapperEnum) {
        switch (mapperEnum) {
            case DO2DTO:
                assertEquals(entity.getVersionLogic(), dto.getVersionLogic());
                assertEquals(entity.getNextVersionDate(), dto.getNextVersionDate());
                assertEqualsDate(entity.getValidFrom(), dto.getValidFrom());
                assertEqualsDate(entity.getValidTo(), dto.getValidTo());
                assertEqualsVersionRationaleTypeCollectionMapper(entity.getVersionRationaleTypes(), dto.getVersionRationaleTypes());
                assertEqualsInternationalString(entity.getVersionRationale(), dto.getVersionRationale());
                assertEquals(entity.getNextVersion(), dto.getNextVersion());
                break;
            case DTO2DO:
                assertEquals(entity.getNextVersion(), dto.getNextVersion());
                if (NextVersionTypeEnum.SCHEDULED_UPDATE.equals(entity.getNextVersionDate())) {
                    assertEquals(entity.getNextVersionDate(), dto.getNextVersionDate());
                }
                assertEqualsInternationalString(entity.getVersionRationale(), dto.getVersionRationale());
                assertEqualsVersionRationaleTypeCollectionMapper(entity.getVersionRationaleTypes(), dto.getVersionRationaleTypes());
                break;
        }
        assertEqualsNameableStatisticalResource(entity, dto, mapperEnum);
    }

    protected static void assertEqualsNameableStatisticalResource(NameableStatisticalResource entity, NameableStatisticalResourceDto dto, MapperEnum mapperEnum) {
        assertEqualsInternationalString(entity.getTitle(), dto.getTitle());
        assertEqualsInternationalString(entity.getDescription(), dto.getDescription());

        assertEqualsIdentifiableStatisticalResource(entity, dto, mapperEnum);
    }

    protected static void assertEqualsIdentifiableStatisticalResource(IdentifiableStatisticalResource entity, IdentifiableStatisticalResourceDto dto, MapperEnum mapperEnum) {
        assertEquals(entity.getCode(), dto.getCode());
        assertEquals(entity.getUrn(), dto.getUrn());

        assertEqualsStatisticalResouce(entity, dto, mapperEnum);
    }

    protected static void assertEqualsStatisticalResouce(StatisticalResource entity, StatisticalResourceDto dto, MapperEnum mapperEnum) {
        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            assertEquals(entity.getVersion(), dto.getOptimisticLockingVersion());
            assertEqualsExternalItem(entity.getStatisticalOperation(), dto.getStatisticalOperation(), mapperEnum);
        }
    }

    protected static void assertEqualsSiemacMetadataStatisticalResourceBase(SiemacMetadataStatisticalResource entity, SiemacMetadataStatisticalResourceBaseDto dto, MapperEnum mapperEnum)
            throws MetamacException {
        assertEqualsLifeCycleStatisticalResourceBase(entity, dto, mapperEnum);
    }

    protected static void assertEqualsLifeCycleStatisticalResourceBase(LifeCycleStatisticalResource entity, LifeCycleStatisticalResourceBaseDto dto, MapperEnum mapperEnum) throws MetamacException {
        switch (mapperEnum) {
            case DO2DTO:
                assertEquals(entity.getProcStatus(), dto.getProcStatus());

                assertEqualsDate(entity.getCreationDate(), dto.getCreationDate());
                assertEqualsDate(entity.getPublicationDate(), dto.getPublicationDate());

                assertEquals(entity.getCreationUser(), dto.getCreationUser());
                assertEquals(entity.getProductionValidationUser(), dto.getProductionValidationUser());
                assertEquals(entity.getDiffusionValidationUser(), dto.getDiffusionValidationUser());
                assertEquals(entity.getPublicationUser(), dto.getPublicationUser());

                assertEquals(entity.getMaintainer().getCodeNested(), dto.getMaintainerCodeNested());
                break;
        }
        assertEqualsVersionableStatisticalResourceBase(entity, dto, mapperEnum);
    }

    protected static void assertEqualsVersionableStatisticalResourceBase(VersionableStatisticalResource entity, VersionableStatisticalResourceBaseDto dto, MapperEnum mapperEnum) {
        switch (mapperEnum) {
            case DO2DTO:
                assertEquals(entity.getVersionLogic(), dto.getVersionLogic());
                break;
        }
        assertEqualsNameableStatisticalResourceBase(entity, dto, mapperEnum);
    }

    protected static void assertEqualsNameableStatisticalResourceBase(NameableStatisticalResource entity, NameableStatisticalResourceBaseDto dto, MapperEnum mapperEnum) {
        assertEqualsInternationalString(entity.getTitle(), dto.getTitle());

        assertEqualsIdentifiableStatisticalResource(entity, dto, mapperEnum);
    }

    // -----------------------------------------------------------------
    // STATISTIC OFFICIALITY: DO & DO
    // -----------------------------------------------------------------
    public static void assertEqualsStatisticOfficiality(StatisticOfficiality expected, StatisticOfficiality actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.getIdentifier(), actual.getIdentifier());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getKey(), actual.getKey());
        assertEqualsInternationalString(expected.getDescription(), actual.getDescription());
    }

    // -----------------------------------------------------------------
    // STATISTIC OFFICIALITY: DTO & DO
    // -----------------------------------------------------------------
    public static void assertEqualsStatisticOfficiality(StatisticOfficiality expected, StatisticOfficialityDto actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.getIdentifier(), actual.getIdentifier());
        assertEquals(expected.getId(), actual.getId());
        assertEqualsInternationalString(expected.getDescription(), actual.getDescription());
    }

    // -----------------------------------------------------------------
    // STATISTIC OFFICIALITY: DO & DTO
    // -----------------------------------------------------------------
    public static void assertEqualsStatisticOfficiality(StatisticOfficialityDto expected, StatisticOfficiality actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.getIdentifier(), actual.getIdentifier());
        assertEquals(expected.getId(), actual.getId());
        assertEqualsInternationalString(actual.getDescription(), expected.getDescription());
    }

    // -----------------------------------------------------------------
    // VERSION RATIONALE TYPE: DO & DO
    // -----------------------------------------------------------------
    public static void assertEqualsVersionRationaleType(VersionRationaleType expected, VersionRationaleType actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.getValue(), actual.getValue());
    }

    public static void assertEqualsVersionRationaleTypeCollection(Collection<VersionRationaleType> expected, Collection<VersionRationaleType> actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.size(), actual.size());

        for (VersionRationaleType expec : expected) {
            boolean found = false;
            for (VersionRationaleType actualRes : actual) {
                if (actualRes.getValue().equals(expec.getValue())) {
                    found = true;
                }
            }
            if (!found) {
                Assert.fail("Found element in expected collection which is not contained in actual collection");
            }
        }
    }

    // -----------------------------------------------------------------
    // VERSION RATIONALE TYPE: DTO & DO
    // -----------------------------------------------------------------
    public static void assertEqualsVersionRationaleType(VersionRationaleType expected, VersionRationaleTypeDto actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.getValue(), actual.getValue());
    }

    public static void assertEqualsVersionRationaleTypeCollectionMapper(Collection<VersionRationaleType> entities, Collection<VersionRationaleTypeDto> dtos) {

        assertEqualsNullability(entities, dtos);
        if (entities == null) {
            assertNull(dtos);
        }
        assertEquals(entities.size(), dtos.size());
        for (VersionRationaleType entity : entities) {
            boolean found = false;
            Iterator<VersionRationaleTypeDto> itDto = dtos.iterator();
            while (itDto.hasNext() && !found) {
                VersionRationaleTypeDto dto = itDto.next();
                found = dto.getValue().equals(entity.getValue());
            }
            if (!found) {
                Assert.fail("Not equal collections");
            }
        }
    }

    // -----------------------------------------------------------------
    // VERSION RATIONALE TYPE: DO & DTO
    // -----------------------------------------------------------------
    public static void assertEqualsVersionRationaleType(VersionRationaleTypeDto expected, VersionRationaleType actual) {
        assertEqualsNullability(expected, actual);
        if (expected == null) {
            return;
        }
        assertEquals(expected.getValue(), actual.getValue());
    }
}
