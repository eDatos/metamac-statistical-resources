package org.siemac.metamac.statistical.resources.core.dataset.utils;

import static org.siemac.metamac.statistical.resources.core.base.utils.BaseVersioningCopyUtils.copyIdentifiableStatisticalResource;
import static org.siemac.metamac.statistical.resources.core.base.utils.BaseVersioningCopyUtils.copySiemacMetadataStatisticalResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.utils.CommonVersioningCopyUtils;
import org.siemac.metamac.statistical.resources.core.dataset.domain.AttributeValue;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;

import es.gobcan.istac.edatos.dataset.repository.dto.Mapping;

public class DatasetVersioningCopyUtils extends CommonVersioningCopyUtils {

    /**
     * Create a new {@link DatasetVersion} copying values from a source.
     */
    public static DatasetVersion copyDatasetVersion(DatasetVersion source) {
        DatasetVersion target = new DatasetVersion();
        copyDatasetVersion(source, target);
        return target;
    }

    /**
     * Copy values from a {@link DatasetVersion}
     */
    public static void copyDatasetVersion(DatasetVersion source, DatasetVersion target) {
        // Metadata
        copyMetadata(source, target);
        copyDimensionsCoverage(source, target);
        copyAttributesCoverage(source, target);

        // Relations
        target.setDataset(source.getDataset());
        copyDatasources(source, target);
        changeDatasourcesCodesAndUrns(target.getDatasources());
        copyCategorisations(source, target);
    }

    private static void copyAttributesCoverage(DatasetVersion source, DatasetVersion target) {
        target.getAttributesCoverage().clear();
        for (AttributeValue attributeValue : source.getAttributesCoverage()) {
            AttributeValue newAttributeValue = new AttributeValue();
            copyAttributeValue(attributeValue, newAttributeValue);
            target.addAttributesCoverage(newAttributeValue);
        }
    }

    private static void copyAttributeValue(AttributeValue source, AttributeValue target) {
        target.setDsdComponentId(source.getDsdComponentId());
        target.setIdentifier(source.getIdentifier());
        target.setTitle(source.getTitle());
    }

    private static void changeDatasourcesCodesAndUrns(List<Datasource> datasources) {
        DateTime now = new DateTime();
        int millisecondsOffset = 0;
        for (Datasource datasource : datasources) {
            DateTime fakeDatasourceUpdateTime = new DateTime(now.getMillis() + millisecondsOffset++);
            String sourceName = datasource.getSourceName();
            String code = Datasource.generateDataSourceId(sourceName, fakeDatasourceUpdateTime);
            datasource.getIdentifiableStatisticalResource().setCode(code);
            datasource.getIdentifiableStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceDatasourceUrn(code));
        }
    }

    private static void copyDatasources(DatasetVersion source, DatasetVersion target) {
        target.getDatasources().clear();
        for (Datasource datasource : source.getDatasources()) {
            Datasource newDatasource = new Datasource();
            copyDatasource(datasource, newDatasource);
            target.addDatasource(newDatasource);
        }
    }

    private static void copyCategorisations(DatasetVersion source, DatasetVersion target) {
        target.getCategorisations().clear();
        for (Categorisation categorisation : source.getCategorisations()) {
            Categorisation newCategorisation = new Categorisation();
            copyCategorisation(categorisation, newCategorisation);
            target.addCategorisation(newCategorisation);
        }
    }

    private static void copyDimensionsCoverage(DatasetVersion source, DatasetVersion target) {
        target.getDimensionsCoverage().clear();
        for (CodeDimension codeDimension : source.getDimensionsCoverage()) {
            CodeDimension newCodeDimension = new CodeDimension();
            copyCodeDimension(codeDimension, newCodeDimension);
            target.addDimensionsCoverage(newCodeDimension);
        }
    }

    private static void copyMetadata(DatasetVersion source, DatasetVersion target) {
        target.setSiemacMetadataStatisticalResource(copySiemacMetadataStatisticalResource(source.getSiemacMetadataStatisticalResource(), target.getSiemacMetadataStatisticalResource()));

        target.getGeographicCoverage().clear();
        target.getGeographicCoverage().addAll(copyCollectionExternalItem(source.getGeographicCoverage()));

        target.getTemporalCoverage().clear();
        target.getTemporalCoverage().addAll(copyListTemporalCode(source.getTemporalCoverage()));

        target.getMeasureCoverage().clear();
        target.getMeasureCoverage().addAll(copyCollectionExternalItem(source.getMeasureCoverage()));

        target.getGeographicGranularities().clear();
        target.getGeographicGranularities().addAll(copyCollectionExternalItem(source.getGeographicGranularities()));

        target.getTemporalGranularities().clear();
        target.getTemporalGranularities().addAll(copyCollectionExternalItem(source.getTemporalGranularities()));

        target.setDateStart(source.getDateStart());

        target.setDateEnd(source.getDateEnd());

        target.getStatisticalUnit().clear();
        target.getStatisticalUnit().addAll(copyCollectionExternalItem(source.getStatisticalUnit()));

        target.setRelatedDsd(copyExternalItem(source.getRelatedDsd()));

        target.setFormatExtentObservations(source.getFormatExtentObservations());

        target.setFormatExtentDimensions(source.getFormatExtentDimensions());

        target.setUpdateFrequency(copyExternalItem(source.getUpdateFrequency()));

        target.setStatisticOfficiality(source.getStatisticOfficiality());

        target.setKeepAllData(source.isKeepAllData());
        target.setDataSourceType(source.getDataSourceType());
        target.setDateLastTimeDataImport(source.getDateLastTimeDataImport());
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

    private static void copyDatasource(Datasource source, Datasource target) {
        target.setIdentifiableStatisticalResource(copyIdentifiableStatisticalResource(source.getIdentifiableStatisticalResource(), target.getIdentifiableStatisticalResource()));
        target.setSourceName(source.getSourceName());
        target.setDateNextUpdate(source.getDateNextUpdate());
    }

    public static CodeDimension copyCodeDimension(CodeDimension source, CodeDimension target) {
        target.setIdentifier(source.getIdentifier());
        target.setTitle(source.getTitle());
        target.setDsdComponentId(source.getDsdComponentId());
        return target;
    }

    private static void copyCategorisation(Categorisation source, Categorisation target) {
        target.setCategory(copyExternalItem(source.getCategory()));
        target.setMaintainer(copyExternalItem(source.getMaintainer()));
        target.setVersionableStatisticalResource(new VersionableStatisticalResource()); // all metadata will be autogenerated (code, title...)
    }

    public static List<Mapping> createDatasourceMapping(DatasetVersion datasetVersionOld, DatasetVersion datasetVersionNew) {
        List<Mapping> datasourcesMapping = new ArrayList<>();
        for (int i = 0; i < datasetVersionNew.getDatasources().size(); i++) {
            datasourcesMapping.add(new Mapping(datasetVersionOld.getDatasources().get(i).getIdentifiableStatisticalResource().getCode(),
                    datasetVersionNew.getDatasources().get(i).getIdentifiableStatisticalResource().getCode()));
        }
        return datasourcesMapping;
    }

}