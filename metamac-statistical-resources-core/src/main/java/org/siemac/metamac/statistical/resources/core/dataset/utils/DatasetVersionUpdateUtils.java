package org.siemac.metamac.statistical.resources.core.dataset.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.utils.CommonVersioningCopyUtils;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;

public class DatasetVersionUpdateUtils extends CommonVersioningCopyUtils {

    /**
     * Create a new {@link DatasetVersion} copying values from a source.
     */
    public static void updateDatasetVersion(DatasetVersion source, DatasetVersion target) {
        copyMetadata(source, target);
        copyCategorisations(source, target);
    }

    private static void copyCategorisations(DatasetVersion source, DatasetVersion target) {
        if (!source.getCategorisations().isEmpty()) {
            target.getCategorisations().clear();
            for (Categorisation categorisation : source.getCategorisations()) {
                Categorisation newCategorisation = new Categorisation();
                copyCategorisation(categorisation, newCategorisation);
                target.addCategorisation(newCategorisation);
            }
        }
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

    public static SiemacMetadataStatisticalResource copySiemacMetadataStatisticalResource(SiemacMetadataStatisticalResource source, SiemacMetadataStatisticalResource target) {

        copyLifeCycleStatisticalResource(source, target);

        // Languages
        if (!source.getLanguages().isEmpty()) {
            target.setLanguage(copyExternalItem(source.getLanguage()));
            target.getLanguages().clear();
            target.getLanguages().addAll(copyCollectionExternalItem(source.getLanguages()));
        }

        // Theme content classifiers
        if (!source.getStatisticalOperationInstances().isEmpty()) {
            target.getStatisticalOperationInstances().clear();
            target.getStatisticalOperationInstances().addAll(copyCollectionExternalItem(source.getStatisticalOperationInstances()));
        }

        // Content descriptors
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

        if (source.getCommonMetadata() != null && source.getCommonMetadata().getId() != null) {
            target.setCommonMetadata(copyExternalItem(source.getCommonMetadata()));
        }

        // Production descriptors

        if (source.getCreator() != null && source.getCreator().getId() != null) {
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

        // Publishing descriptors
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

        // Resources relation descriptors

        // Intellectual ownership descriptors
        if (source.getAccessRights() != null && !source.getAccessRights().getTexts().isEmpty()) {
            target.setAccessRights(copyInternationalString(source.getAccessRights()));
        }

        return target;
    }

    // --------------------------------------------------------------------------
    // LIFE CYCLE STATISTICAL RESOURCE
    // --------------------------------------------------------------------------

    public static LifeCycleStatisticalResource copyLifeCycleStatisticalResource(LifeCycleStatisticalResource source, LifeCycleStatisticalResource target) {

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

    private static Collection<VersionRationaleType> copyListVersionRationaleType(List<VersionRationaleType> source) {

        List<VersionRationaleType> target = new ArrayList<VersionRationaleType>();
        for (VersionRationaleType item : source) {
            target.add(copyVersionRationaleType(item));
        }
        return target;
    }

    public static VersionRationaleType copyVersionRationaleType(VersionRationaleType source) {
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

    public static TemporalCode copyTemporalCode(TemporalCode source) {
        if (source == null) {
            return null;
        }
        TemporalCode target = new TemporalCode();
        target.setIdentifier(source.getIdentifier());
        target.setTitle(source.getTitle());
        return target;
    }

    private static void copyCategorisation(Categorisation source, Categorisation target) {
        target.setCategory(copyExternalItem(source.getCategory()));
        target.setMaintainer(copyExternalItem(source.getMaintainer()));
        target.setVersionableStatisticalResource(new VersionableStatisticalResource()); // all metadata will be autogenerated (code, title...)
    }

}