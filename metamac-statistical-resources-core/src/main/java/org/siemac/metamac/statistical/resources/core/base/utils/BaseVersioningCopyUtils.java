package org.siemac.metamac.statistical.resources.core.base.utils;

import org.apache.commons.lang.BooleanUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.StatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.utils.CommonVersioningCopyUtils;

public class BaseVersioningCopyUtils extends CommonVersioningCopyUtils {

    // --------------------------------------------------------------------------
    // SIEMAC METADATA STATISTICAL RESOURCE
    // --------------------------------------------------------------------------

    public static SiemacMetadataStatisticalResource copySiemacMetadataStatisticalResource(SiemacMetadataStatisticalResource source, SiemacMetadataStatisticalResource target) {
        if (target == null && source != null) {
            target = new SiemacMetadataStatisticalResource();
        }
        
        copyLifeCycleStatisticalResource(source, target);

        // Languages
        target.setLanguage(copyExternalItem(source.getLanguage()));
        target.getLanguages().clear();
        target.getLanguages().addAll(copyCollectionExternalItem(source.getLanguages()));

        // Theme content classifiers
        target.getStatisticalOperationInstances().clear();
        target.getStatisticalOperationInstances().addAll(copyCollectionExternalItem(source.getStatisticalOperationInstances()));

        // Content descriptors
        target.setSubtitle(copyInternationalString(source.getSubtitle()));
        target.setTitleAlternative(copyInternationalString(source.getTitleAlternative()));
        target.setAbstractLogic(copyInternationalString(source.getAbstractLogic()));

        target.setUserModifiedKeywords(BooleanUtils.isTrue(source.getUserModifiedKeywords()));
        target.setKeywords(copyInternationalString(source.getKeywords()));

        // Class descriptor
        target.setCommonMetadata(copyExternalItem(source.getCommonMetadata()));
        
        // Production descriptors
        target.setCreator(copyExternalItem(source.getCreator()));
        target.getContributor().clear();
        target.getContributor().addAll(copyCollectionExternalItem(source.getContributor()));
        target.getDataProvider().clear();
        target.getDataProvider().addAll(copyCollectionExternalItem(source.getDataProvider()));
        target.setDataProviderAnnotations(copyInternationalString(source.getDataProviderAnnotations()));
        target.setCreatedDate(source.getCreatedDate());
        target.setConformsTo(copyInternationalString(source.getConformsTo()));
        target.setConformsToInternal(copyInternationalString(source.getConformsToInternal()));

        // Publishing descriptors
        target.getPublisher().clear();
        target.getPublisher().addAll(copyCollectionExternalItem(source.getPublisher()));
        target.getPublisherContributor().clear();
        target.getPublisherContributor().addAll(copyCollectionExternalItem(source.getPublisherContributor()));
        target.getMediator().clear();
        target.getMediator().addAll(copyCollectionExternalItem(source.getMediator()));

        // Resources relation descriptors

        // Intellectual ownership descriptors
        target.setAccessRights(copyInternationalString(source.getAccessRights()));
        
        return target;
    }

    // --------------------------------------------------------------------------
    // LIFE CYCLE STATISTICAL RESOURCE
    // --------------------------------------------------------------------------

    public static LifeCycleStatisticalResource copyLifeCycleStatisticalResource(LifeCycleStatisticalResource source, LifeCycleStatisticalResource target) {
        if (target == null && source != null) {
            target = new LifeCycleStatisticalResource();
        }
        
        copyVersionableStatisticalResource(source, target);
        target.setMaintainer(copyExternalItem(source.getMaintainer()));
        // Other lifecycle metadata are filled automatically
        
        return target;
    }

    // --------------------------------------------------------------------------
    // VERSIONABLE STATISTICAL RESOURCE
    // --------------------------------------------------------------------------

    public static VersionableStatisticalResource copyVersionableStatisticalResource(VersionableStatisticalResource source, VersionableStatisticalResource target) {
        if (target == null && source != null) {
            target = new VersionableStatisticalResource();
        }
        
        copyNameableStatisticalResource(source, target);
        target.setNextVersion(source.getNextVersion());
        target.setPatch(source.getPatch());
        
        return target;
    }

    // --------------------------------------------------------------------------
    // NAMEABLE STATISTICAL RESOURCE
    // --------------------------------------------------------------------------

    public static NameableStatisticalResource copyNameableStatisticalResource(NameableStatisticalResource source, NameableStatisticalResource target) {
        if (target == null && source != null) {
            target = new NameableStatisticalResource();
        }
        
        target.setTitle(copyInternationalString(source.getTitle()));
        target.setDescription(copyInternationalString(source.getDescription()));

        copyIdentifiableStatisticalResource(source, target);
        
        return target;
    }

    // --------------------------------------------------------------------------
    // IDENTIFIABLE METADATA STATISTICAL RESOURCE
    // --------------------------------------------------------------------------

    public static IdentifiableStatisticalResource copyIdentifiableStatisticalResource(IdentifiableStatisticalResource source, IdentifiableStatisticalResource target) {
        if (target == null && source != null) {
            target = new IdentifiableStatisticalResource();
        }
        
        target.setCode(source.getCode());
        target.setType(source.getType());

        copyStatisticalResource(source, target);
        
        return target;
    }

    // --------------------------------------------------------------------------
    // STATISTICAL RESOURCE
    // --------------------------------------------------------------------------

    public static StatisticalResource copyStatisticalResource(StatisticalResource source, StatisticalResource target) {
        if (target == null && source != null) {
            target = new IdentifiableStatisticalResource();
        }
        
        target.setStatisticalOperation(copyExternalItem(source.getStatisticalOperation()));
        
        return target;
        
    }
}
