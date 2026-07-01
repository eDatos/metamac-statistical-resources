package org.siemac.metamac.statistical.resources.core.base.mapper;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang.BooleanUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.StatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.mapper.CommonDo2DtoMapperImpl;
import org.siemac.metamac.statistical.resources.core.dto.IdentifiableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.StatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionRationaleTypeDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionableStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;

@org.springframework.stereotype.Component("baseDo2DtoMapper")
public class BaseDo2DtoMapperImpl extends CommonDo2DtoMapperImpl implements BaseDo2DtoMapper {

    // ------------------------------------------------------------
    // BASE HIERARCHY
    // ------------------------------------------------------------

    @Override
    public void siemacMetadataStatisticalResourceDoToDto(SiemacMetadataStatisticalResource source, SiemacMetadataStatisticalResourceDto target) throws MetamacException {
        if (source == null) {
            return;
        }
        lifeCycleStatisticalResourceDoToDto(source, target);

        target.setLanguage(externalItemDoToDto(source.getLanguage()));
        target.getLanguages().clear();
        target.getLanguages().addAll(externalItemDoCollectionToDtoCollection(source.getLanguages()));

        target.getStatisticalOperationInstances().clear();
        target.getStatisticalOperationInstances().addAll(externalItemDoCollectionToDtoCollection(source.getStatisticalOperationInstances()));

        target.setSubtitle(internationalStringDoToDto(source.getSubtitle()));
        target.setTitleAlternative(internationalStringDoToDto(source.getTitleAlternative()));
        target.setDescription(internationalStringDoToDto(source.getDescription()));
        target.setAbstractLogic(internationalStringDoToDto(source.getAbstractLogic()));
        target.setKeywords(internationalStringDoToDto(source.getKeywords()));

        target.setCreator(externalItemDoToDto(source.getCreator()));
        target.getContributor().clear();
        target.getContributor().addAll(externalItemDoCollectionToDtoCollection(source.getContributor()));
        target.getDataProvider().clear();
        target.getDataProvider().addAll(externalItemDoCollectionToDtoCollection(source.getDataProvider()));
        target.setDataProviderAnnotations(internationalStringDoToDto(source.getDataProviderAnnotations()));
        target.setResourceCreatedDate(dateDoToDto(source.getResourceCreatedDate()));
        target.setLastUpdate(dateDoToDto(source.getLastUpdate()));
        target.setConformsTo(internationalStringDoToDto(source.getConformsTo()));
        target.setConformsToInternal(internationalStringDoToDto(source.getConformsToInternal()));

        target.getPublisher().clear();
        target.getPublisher().addAll(externalItemDoCollectionToDtoCollection(source.getPublisher()));
        target.getPublisherContributor().clear();
        target.getPublisherContributor().addAll(externalItemDoCollectionToDtoCollection(source.getPublisherContributor()));
        target.getMediator().clear();
        target.getMediator().addAll(externalItemDoCollectionToDtoCollection(source.getMediator()));
        target.setNewnessUntilDate(dateDoToDto(source.getNewnessUntilDate()));
        target.setFeaturedUntilDate(dateDoToDto(source.getFeaturedUntilDate()));

        target.setReplaces(relatedResourceDoToDto(source.getReplaces()));
        // Is replaced_by is filled in higher level resource mapper

        target.setCommonMetadata(externalItemDoToDto(source.getCommonMetadata()));
        target.setCopyrightedDate(source.getCopyrightedDate());
        target.setAccessRights(internationalStringDoToDto(source.getAccessRights()));
    }

    @Override
    public void siemacMetadataStatisticalResourceDoToBaseDto(SiemacMetadataStatisticalResource source, SiemacMetadataStatisticalResourceBaseDto target) throws MetamacException {
        if (source == null) {
            return;
        }
        lifeCycleStatisticalResourceDoToBaseDto(source, target);
    }

    @Override
    public void lifeCycleStatisticalResourceDoToDto(LifeCycleStatisticalResource source, LifeCycleStatisticalResourceDto target) throws MetamacException {
        if (source == null) {
            return;
        }
        versionableStatisticalResourceDoToDto(source, target);

        target.setProcStatus(source.getEffectiveProcStatus());
        target.setLastVersion(BooleanUtils.isTrue(source.getLastVersion()));
        target.setCreationDate(dateDoToDto(source.getCreationDate()));
        target.setCreationUser(source.getCreationUser());
        target.setProductionValidationDate(dateDoToDto(source.getProductionValidationDate()));
        target.setProductionValidationUser(source.getProductionValidationUser());
        target.setDiffusionValidationDate(dateDoToDto(source.getDiffusionValidationDate()));
        target.setDiffusionValidationUser(source.getDiffusionValidationUser());
        target.setRejectValidationDate(dateDoToDto(source.getRejectValidationDate()));
        target.setRejectValidationUser(source.getRejectValidationUser());
        target.setPublicationDate(dateDoToDto(source.getPublicationDate()));
        target.setPublicationUser(source.getPublicationUser());

        target.setReplacesVersion(relatedResourceDoToDto(source.getReplacesVersion()));
        target.setIsReplacedByVersion(relatedResourceDoToDto(source.getIsReplacedByVersion()));

        target.setMaintainer(externalItemDoToDto(source.getMaintainer()));

        target.setPublicationStreamStatus(source.getPublicationStreamStatus());
        target.setXStreamStatus(source.getXStreamStatus());
        target.setNewnessUntilDate(dateDoToDto(source.getNewnessUntilDate()));
        target.setFeaturedUntilDate(dateDoToDto(source.getFeaturedUntilDate()));
    }

    @Override
    public void lifeCycleStatisticalResourceDoToBaseDto(LifeCycleStatisticalResource source, LifeCycleStatisticalResourceBaseDto target) throws MetamacException {
        if (source == null) {
            return;
        }
        versionableStatisticalResourceDoToBaseDto(source, target);

        target.setProcStatus(source.getEffectiveProcStatus());
        target.setLastVersion(BooleanUtils.isTrue(source.getLastVersion()));
        target.setCreationDate(dateDoToDto(source.getCreationDate()));
        target.setPublicationDate(dateDoToDto(source.getPublicationDate()));
        target.setCreationUser(source.getCreationUser());
        target.setProductionValidationUser(source.getProductionValidationUser());
        target.setDiffusionValidationUser(source.getDiffusionValidationUser());
        target.setPublicationUser(source.getPublicationUser());
        target.setMaintainerCodeNested(source.getMaintainer().getCodeNested());
        target.setPublicationStreamStatus(source.getPublicationStreamStatus());

    }

    @Override
    public void versionableStatisticalResourceDoToDto(VersionableStatisticalResource source, VersionableStatisticalResourceDto target) throws MetamacException {
        if (source == null) {
            return;
        }
        nameableStatisticalResourceDoToDto(source, target);

        target.setVersionLogic(source.getVersionLogic());
        target.setNextVersion(source.getNextVersion());
        target.setNextVersionDate(source.getNextVersionDate());
        target.setVersionRationale(internationalStringDoToDto(source.getVersionRationale()));
        target.getVersionRationaleTypes().clear();
        target.getVersionRationaleTypes().addAll(versionRationaleTypeDoCollectionToDtoCollection(source.getVersionRationaleTypes()));
        target.setValidFrom(dateDoToDto(source.getValidFrom()));
        target.setValidTo(dateDoToDto(source.getValidTo()));
    }

    @Override
    public void versionableStatisticalResourceDoToBaseDto(VersionableStatisticalResource source, VersionableStatisticalResourceBaseDto target) throws MetamacException {
        if (source == null) {
            return;
        }
        nameableStatisticalResourceDoToBaseDto(source, target);

        target.setVersionLogic(source.getVersionLogic());
    }

    @Override
    public void nameableStatisticalResourceDoToDto(NameableStatisticalResource source, NameableStatisticalResourceDto target) throws MetamacException {
        if (source == null) {
            return;
        }
        identifiableStatisticalResourceDoToDto(source, target);

        target.setTitle(internationalStringDoToDto(source.getTitle()));
        target.setDescription(internationalStringDoToDto(source.getDescription()));
    }

    @Override
    public void nameableStatisticalResourceDoToBaseDto(NameableStatisticalResource source, NameableStatisticalResourceBaseDto target) throws MetamacException {
        if (source == null) {
            return;
        }
        identifiableStatisticalResourceDoToDto(source, target);

        target.setTitle(internationalStringDoToDto(source.getTitle()));
    }

    @Override
    public void identifiableStatisticalResourceDoToDto(IdentifiableStatisticalResource source, IdentifiableStatisticalResourceDto target) throws MetamacException {
        if (source == null) {
            return;
        }
        statisticalResourceDoToDto(source, target);

        target.setCode(source.getCode());
        target.setUrn(source.getUrn());
        target.setType(source.getType());
    }

    @Override
    public void statisticalResourceDoToDto(StatisticalResource source, StatisticalResourceDto target) throws MetamacException {
        if (source == null) {
            return;
        }

        // Statistical Operation
        target.setStatisticalOperation(externalItemDoToDto(source.getStatisticalOperation()));

        // Optimistic locking
        target.setOptimisticLockingVersion(source.getVersion());

        // Identity
        // Don't set the identity attributes because we choose the ones of the lastest element of the hierarchy

        // Auditable
        target.setCreatedBy(source.getCreatedBy());
        target.setCreatedDate(dateDoToDto(source.getCreatedDate()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setLastUpdated(dateDoToDto(source.getLastUpdated()));

    }

    // ------------------------------------------------------------
    // RELATED RESOURCE
    // ------------------------------------------------------------
    @Override
    public RelatedResourceDto lifecycleStatisticalResourceDoToRelatedResourceDto(LifeCycleStatisticalResource source, TypeRelatedResourceEnum type) {
        RelatedResourceDto target = new RelatedResourceDto();
        target.setCode(source.getCode());
        target.setUrn(source.getUrn());
        target.setTitle(internationalStringDoToDto(source.getTitle()));
        target.setType(type);
        target.setStatisticalOperationUrn(source.getStatisticalOperation() != null ? source.getStatisticalOperation().getUrn() : null);

        target.setVersionLogic(source.getVersionLogic());
        target.setLastVersion(source.getLastVersion());

        return target;
    }

    // ------------------------------------------------------------
    // VERSION RATIONALE TYPE
    // ------------------------------------------------------------

    @Override
    public Collection<VersionRationaleTypeDto> versionRationaleTypeDoCollectionToDtoCollection(Collection<VersionRationaleType> source) {
        HashSet<VersionRationaleTypeDto> result = new HashSet<VersionRationaleTypeDto>();
        for (VersionRationaleType resource : source) {
            result.add(versionRationaleTypeDoToDto(resource));
        }
        return result;
    }

    @Override
    public VersionRationaleTypeDto versionRationaleTypeDoToDto(VersionRationaleType source) {
        if (source == null) {
            return null;
        }
        return new VersionRationaleTypeDto(source.getValue());
    }
}
