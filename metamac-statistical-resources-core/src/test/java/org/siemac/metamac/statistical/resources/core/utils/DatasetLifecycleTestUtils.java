package org.siemac.metamac.statistical.resources.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasourceMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

public class DatasetLifecycleTestUtils {

    private static StatisticalResourcesPersistedDoMocks persistedMocks = StatisticalResourcesPersistedDoMocks.getInstance();

    // *****************************************************
    // PRODUCTION VALIDATION
    // *****************************************************

    public static void prepareToProductionValidation(DatasetVersion datasetVersion) {
        LifecycleTestUtils.prepareToProductionValidationSiemac(datasetVersion);
        prepareToLifecycleCommonDatasetVersion(datasetVersion);
    }

    public static void fillAsProductionValidation(DatasetVersion datasetVersion) {
        prepareToProductionValidation(datasetVersion);
        LifecycleTestUtils.fillAsProductionValidationSiemac(datasetVersion);
    }

    // *****************************************************
    // DIFFUSION VALIDATION
    // *****************************************************

    public static void prepareToDiffusionValidation(DatasetVersion datasetVersion) {
        fillAsProductionValidation(datasetVersion);
        LifecycleTestUtils.prepareToDiffusionValidationSiemac(datasetVersion);
    }

    public static void fillAsDiffusionValidation(DatasetVersion datasetVersion) {
        prepareToDiffusionValidation(datasetVersion);
        LifecycleTestUtils.fillAsDiffusionValidationSiemac(datasetVersion);
    }

    // *****************************************************
    // VALIDATON REJECTED
    // *****************************************************

    public static void prepareToValidationRejected(DatasetVersion datasetVersion) {
        fillAsProductionValidation(datasetVersion);
        LifecycleTestUtils.prepareToValidationRejectedFromProductionValidationSiemac(datasetVersion);
    }

    public static void fillAsValidationRejected(DatasetVersion datasetVersion) {
        prepareToValidationRejected(datasetVersion);
        LifecycleTestUtils.fillAsValidationRejectedFromProductionValidationSiemac(datasetVersion);
    }

    // *****************************************************
    // PUBLISHING
    // *****************************************************

    public static void prepareToPublished(DatasetVersion datasetVersion) {
        fillAsDiffusionValidation(datasetVersion);
        LifecycleTestUtils.prepareToPublishingSiemac(datasetVersion);
    }

    public static void fillAsPublished(DatasetVersion datasetVersion) {
        prepareToPublished(datasetVersion);
        LifecycleTestUtils.fillAsPublishedSiemac(datasetVersion);
    }

    // *****************************************************
    // VERSIONING
    // *****************************************************

    public static void prepareToVersioning(DatasetVersion datasetVersion) {
        fillAsPublished(datasetVersion);
        LifecycleTestUtils.prepareToVersioningSiemac(datasetVersion);
    }

    public static void fillAsVersioned(DatasetVersion datasetVersion) {
        prepareToVersioning(datasetVersion);
        LifecycleTestUtils.fillAsVersionedSiemac(datasetVersion);
    }

    // *****************************************************
    // UTILS
    // *****************************************************

    private static void prepareToLifecycleCommonDatasetVersion(DatasetVersion datasetVersion) {
        ExternalItem geoGranularity = StatisticalResourcesPersistedDoMocks.mockCodeExternalItem();
        datasetVersion.addGeographicGranularity(geoGranularity);

        ExternalItem timeGranularity = StatisticalResourcesPersistedDoMocks.mockCodeExternalItem();
        datasetVersion.addTemporalGranularity(timeGranularity);

        ExternalItem dsd = StatisticalResourcesPersistedDoMocks.mockDsdExternalItem();
        datasetVersion.setRelatedDsd(dsd);

        datasetVersion.setDateNextUpdate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(10).toDate()));

        ExternalItem codeUpdateFreq = StatisticalResourcesPersistedDoMocks.mockCodeExternalItem();
        datasetVersion.setUpdateFrequency(codeUpdateFreq);

        StatisticOfficiality officiality = persistedMocks.mockStatisticOfficiality("officiality");
        datasetVersion.setStatisticOfficiality(officiality);

        if (datasetVersion.getDatasources().isEmpty()) {
            Datasource datasource = DatasourceMockFactory.generateSimpleDatasource();
            datasource.setDatasetVersion(datasetVersion);
            datasetVersion.addDatasource(datasource);
        }

        if (StringUtils.isEmpty(datasetVersion.getDatasetRepositoryId())) {
            datasetVersion.setDatasetRepositoryId(StatisticalResourcesDoMocks.mockString(10));
        }

        // Coverages
        fillDimensionCoverages(datasetVersion);
    }

    private static void fillDimensionCoverages(DatasetVersion datasetVersion) {
        if (datasetVersion.getDimensionsCoverage().isEmpty()) {
            datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2012", "2012"));
            datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2011", "2011"));
            datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2010", "2010"));
            datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES", "España"));
            datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES61", "Andalucia"));
            datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES70", "Canarias"));
            datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "ES45", "Cataluña"));
            datasetVersion.addDimensionsCoverage(new CodeDimension("MEAS_DIM", "C01", "Concept 01"));
            datasetVersion.addDimensionsCoverage(new CodeDimension("MEAS_DIM", "C02", "Concept 02"));
            datasetVersion.addDimensionsCoverage(new CodeDimension("MEAS_DIM", "C03", "Concept 03"));
        }

        StatisticalResourcesPersistedDoMocks.computeCoverageRelatedMetadata(datasetVersion);
    }
}
