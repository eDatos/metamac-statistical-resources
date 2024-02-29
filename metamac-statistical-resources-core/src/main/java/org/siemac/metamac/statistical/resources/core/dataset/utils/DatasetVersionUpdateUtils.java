package org.siemac.metamac.statistical.resources.core.dataset.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.utils.CommonVersioningCopyUtils;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesCollectionUtils;

public class DatasetVersionUpdateUtils extends CommonVersioningCopyUtils {

    /**
     * Create a new {@link DatasetVersion} copying values from a source.
     */

    public static void updateDatasetVersion(DatasetVersion source, DatasetVersion target) {
        copyMetadata(source, target);
    }

    private static void copyMetadata(DatasetVersion source, DatasetVersion target) {
        target.setSiemacMetadataStatisticalResource(copySiemacMetadataStatisticalResource(source.getSiemacMetadataStatisticalResource(), target.getSiemacMetadataStatisticalResource()));

        if (!source.getGeographicCoverage().isEmpty()) {
            target.getGeographicCoverage().clear();
            target.getGeographicCoverage().addAll(copyCollectionExternalItem(source.getGeographicCoverage()));
        }

        if (!source.getTemporalCoverage().isEmpty()) {
            target.getTemporalCoverage().clear();
            target.getTemporalCoverage().addAll(copyListTemporalCode(source.getTemporalCoverage()));
        }

        if (!source.getMeasureCoverage().isEmpty()) {
            target.getMeasureCoverage().clear();
            target.getMeasureCoverage().addAll(copyCollectionExternalItem(source.getMeasureCoverage()));
        }

        if (!source.getGeographicGranularities().isEmpty()) {
            target.getGeographicGranularities().clear();
            target.getGeographicGranularities().addAll(copyCollectionExternalItem(source.getGeographicGranularities()));
        }

        if (!source.getTemporalGranularities().isEmpty()) {
            target.getTemporalGranularities().clear();
            target.getTemporalGranularities().addAll(copyCollectionExternalItem(source.getTemporalGranularities()));
        }

        if (source.getUpdateFrequency() != null && source.getUpdateFrequency().getCode() != null) {
            target.setUpdateFrequency(copyExternalItem(source.getUpdateFrequency()));
        }

        if (source.getDateNextUpdate() != null) {
            target.setDateNextUpdate(source.getDateNextUpdate());
        }

        if (source.getStatisticOfficiality() != null && source.getStatisticOfficiality().getId() != null) {
            target.setStatisticOfficiality(source.getStatisticOfficiality());
        }

    }

    // --------------------------------------------------------------------------
    // SIEMAC METADATA STATISTICAL RESOURCE
    // --------------------------------------------------------------------------

    private static SiemacMetadataStatisticalResource copySiemacMetadataStatisticalResource(SiemacMetadataStatisticalResource source, SiemacMetadataStatisticalResource target) {

        copyLifeCycleStatisticalResource(source, target);

        copyLanguageMetadata(source, target);
        copyContentClassifiersMetadata(source, target);
        copyContentDescriptorsMetadata(source, target);
        copyProductionDescriptorsMetadata(source, target);
        copyPublishingDescriptorsMetadata(source, target);
        copyResourcesRelationDescriptorsMetadata(source, target);

        return target;
    }

    private static void copyLanguageMetadata(SiemacMetadataStatisticalResource source, SiemacMetadataStatisticalResource target) {
        if (!source.getLanguages().isEmpty()) {
            Collection<ExternalItem> newTargets = copyLanguages(source.getLanguages(), target.getLanguages(), target.getLanguage());
            target.getLanguages().clear();
            target.getLanguages().addAll(newTargets);
        }
    }

    public static Collection<ExternalItem> copyLanguages(Collection<ExternalItem> source, Collection<ExternalItem> oldTarget, ExternalItem defaultLanguage) {

        List<ExternalItem> target = new ArrayList<ExternalItem>();

        // default language should always be
        if (defaultLanguage != null && !StatisticalResourcesCollectionUtils.isExternalItemInCollection(source, defaultLanguage)) {
            source.add(CommonVersioningCopyUtils.copyExternalItem(defaultLanguage));
        }

        // only add language that did not exist before
        for (ExternalItem itemSource : source) {
            boolean exists = false;
            for (ExternalItem itemOldTarget : oldTarget) {
                if (itemSource.getUrn().equals(itemOldTarget.getUrn())) {
                    itemSource.setUrn(null);
                    target.add(itemOldTarget);
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                target.add(copyExternalItem(itemSource));
            }
        }

        return target;
    }

    private static void copyContentClassifiersMetadata(SiemacMetadataStatisticalResource source, SiemacMetadataStatisticalResource target) {
        if (!source.getStatisticalOperationInstances().isEmpty()) {
            target.getStatisticalOperationInstances().clear();
            target.getStatisticalOperationInstances().addAll(copyCollectionExternalItem(source.getStatisticalOperationInstances()));
        }
    }

    private static void copyContentDescriptorsMetadata(SiemacMetadataStatisticalResource source, SiemacMetadataStatisticalResource target) {
        if (source.getSubtitle() != null && !source.getSubtitle().getTexts().isEmpty()) {
            target.setSubtitle(copyInternationalString(source.getSubtitle()));
        }

        if (source.getTitleAlternative() != null && !source.getTitleAlternative().getTexts().isEmpty()) {
            target.setTitleAlternative(copyInternationalString(source.getTitleAlternative()));
        }

        if (source.getAbstractLogic() != null && !source.getAbstractLogic().getTexts().isEmpty()) {
            target.setAbstractLogic(copyInternationalString(source.getAbstractLogic()));
        }

        if (source.getKeywords() != null && !source.getKeywords().getTexts().isEmpty()) {
            target.setKeywords(copyInternationalString(source.getKeywords()));
        }

        if (source.getCommonMetadata() != null && source.getCommonMetadata().getUrn() != null) {
            target.setCommonMetadata(copyExternalItem(source.getCommonMetadata()));
        }
    }

    private static void copyProductionDescriptorsMetadata(SiemacMetadataStatisticalResource source, SiemacMetadataStatisticalResource target) {
        if (source.getCreator() != null && source.getCreator().getUrn() != null) {
            target.setCreator(copyExternalItem(source.getCreator()));
        }

        if (!source.getContributor().isEmpty()) {
            target.getContributor().clear();
            target.getContributor().addAll(copyCollectionExternalItem(source.getContributor()));
        }
        if (!source.getDataProvider().isEmpty()) {
            target.getDataProvider().clear();
            target.getDataProvider().addAll(copyCollectionExternalItem(source.getDataProvider()));
        }

        if (source.getDataProviderAnnotations() != null && !source.getDataProviderAnnotations().getTexts().isEmpty()) {
            target.setDataProviderAnnotations(copyInternationalString(source.getDataProviderAnnotations()));
        }

        if (source.getConformsTo() != null && !source.getConformsTo().getTexts().isEmpty()) {
            target.setConformsTo(copyInternationalString(source.getConformsTo()));
        }

        if (source.getConformsToInternal() != null && !source.getConformsToInternal().getTexts().isEmpty()) {
            target.setConformsToInternal(copyInternationalString(source.getConformsToInternal()));
        }
    }

    private static void copyPublishingDescriptorsMetadata(SiemacMetadataStatisticalResource source, SiemacMetadataStatisticalResource target) {
        if (!source.getPublisher().isEmpty()) {
            target.getPublisher().clear();
            target.getPublisher().addAll(copyCollectionExternalItem(source.getPublisher()));
        }
        if (!source.getPublisherContributor().isEmpty()) {
            target.getPublisherContributor().clear();
            target.getPublisherContributor().addAll(copyCollectionExternalItem(source.getPublisherContributor()));
        }
        if (!source.getMediator().isEmpty()) {
            target.getMediator().clear();
            target.getMediator().addAll(copyCollectionExternalItem(source.getMediator()));
        }

    }

    private static void copyResourcesRelationDescriptorsMetadata(SiemacMetadataStatisticalResource source, SiemacMetadataStatisticalResource target) {
        // Intellectual ownership descriptors
        if (source.getAccessRights() != null && !source.getAccessRights().getTexts().isEmpty()) {
            target.setAccessRights(copyInternationalString(source.getAccessRights()));
        }
    }

    // --------------------------------------------------------------------------
    // LIFE CYCLE STATISTICAL RESOURCE
    // --------------------------------------------------------------------------

    private static LifeCycleStatisticalResource copyLifeCycleStatisticalResource(LifeCycleStatisticalResource source, LifeCycleStatisticalResource target) {

        copyVersionableStatisticalResource(source, target);

        if (!source.getVersionRationaleTypes().isEmpty()) {
            target.getVersionRationaleTypes().clear();
            target.getVersionRationaleTypes().addAll(copyListVersionRationaleType(source.getVersionRationaleTypes()));
        }

        if (source.getVersionRationale() != null && !source.getVersionRationale().getTexts().isEmpty()) {
            target.setVersionRationale(copyInternationalString(source.getVersionRationale()));
        }

        if (source.getNextVersion() != null) {
            target.setNextVersion(source.getNextVersion());
        }

        if (source.getNextVersionDate() != null) {
            target.setNextVersionDate(source.getNextVersionDate());
        }

        return target;
    }

    // --------------------------------------------------------------------------
    // VERSIONABLE STATISTICAL RESOURCE
    // --------------------------------------------------------------------------

    private static void copyVersionableStatisticalResource(VersionableStatisticalResource source, VersionableStatisticalResource target) {
        copyNameableStatisticalResource(source, target);
    }

    // --------------------------------------------------------------------------
    // NAMEABLE STATISTICAL RESOURCE
    // --------------------------------------------------------------------------

    private static void copyNameableStatisticalResource(NameableStatisticalResource source, NameableStatisticalResource target) {
        if (source.getDescription() != null && !source.getDescription().getTexts().isEmpty()) {
            target.setDescription(copyInternationalString(source.getDescription()));
        }
    }

    private static Collection<VersionRationaleType> copyListVersionRationaleType(List<VersionRationaleType> source) {

        List<VersionRationaleType> target = new ArrayList<VersionRationaleType>();
        for (VersionRationaleType item : source) {
            target.add(copyVersionRationaleType(item));
        }
        return target;
    }

    private static VersionRationaleType copyVersionRationaleType(VersionRationaleType source) {
        if (source == null) {
            return null;
        }
        VersionRationaleType target = new VersionRationaleType(source.getValue());
        target.setVersion(source.getVersion());
        return target;
    }

    private static Collection<TemporalCode> copyListTemporalCode(List<TemporalCode> source) {
        if (source.isEmpty()) {
            return new ArrayList<TemporalCode>();
        }

        List<TemporalCode> target = new ArrayList<TemporalCode>();
        for (TemporalCode item : source) {
            target.add(copyTemporalCode(item));
        }
        return target;
    }

    private static TemporalCode copyTemporalCode(TemporalCode source) {
        if (source == null) {
            return null;
        }
        TemporalCode target = new TemporalCode();
        target.setIdentifier(source.getIdentifier());
        target.setTitle(source.getTitle());
        return target;
    }

    public static List<Categorisation> copyCategorisations(List<Categorisation> source) {
        List<Categorisation> target = new ArrayList<Categorisation>();
        for (Categorisation categorisation : source) {
            Categorisation newCategorisation = new Categorisation();
            copyCategorisation(categorisation, newCategorisation);
            target.add(newCategorisation);
        }
        return target;
    }

    private static void copyCategorisation(Categorisation source, Categorisation target) {
        target.setCategory(copyExternalItem(source.getCategory()));
        target.setMaintainer(copyExternalItem(source.getMaintainer()));
        target.setVersionableStatisticalResource(new VersionableStatisticalResource()); // all metadata will be autogenerated (code, title...)
    }
}