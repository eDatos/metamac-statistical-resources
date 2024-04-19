package org.siemac.metamac.statistical.resources.core.utils;

import java.time.LocalDate;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.time.TimeGranularityUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.ImportDatasetFromDatabaseJob;
import org.siemac.metamac.statistical.resources.core.utils.shared.DatabaseDatasetImportSharedUtils;

public class DatabaseDatasetImportUtils extends DatabaseDatasetImportSharedUtils {

    private DatabaseDatasetImportUtils() {
        super();
    }

    public static boolean isDatabaseDatasetImportJob(ServiceContext ctx) {
        return Boolean.TRUE.equals(ctx.getProperty(ImportDatasetFromDatabaseJob.DATABASE_IMPORT_JOB_FLAG));
    }

    public static void setRequiredMetadataForDatabaseDatasetImportation(DatasetVersion datasetVersion) {
        DatabaseDatasetImportUtils.setDatasetVersionVersionRationaleType(datasetVersion);
        DatabaseDatasetImportUtils.setDatasetVersionNextVersion(datasetVersion);
        DatabaseDatasetImportUtils.setDatasetVersionNextVersionAndNextUpdateDate(datasetVersion);
    }

    private static void setDatasetVersionVersionRationaleType(DatasetVersion datasetVersion) {
        if (CollectionUtils.isEmpty(datasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes())) {
            datasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().add(new VersionRationaleType(VersionRationaleTypeEnum.MINOR_DATA_UPDATE));
        }
    }

    private static void setDatasetVersionNextVersion(DatasetVersion datasetVersion) {
        if (datasetVersion.getSiemacMetadataStatisticalResource().getNextVersion() == null) {
            datasetVersion.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);
            datasetVersion.setUpdateFrequency(null);
        }
    }

    private static void setDatasetVersionNextVersionAndNextUpdateDate(DatasetVersion datasetVersion) {
        if (NextVersionTypeEnum.SCHEDULED_UPDATE.equals(datasetVersion.getSiemacMetadataStatisticalResource().getNextVersion()) && datasetVersion.getUpdateFrequency() != null) {
            LocalDate nextVersionUpdate = TimeGranularityUtils.addTimeGranularityToDate(LocalDate.now(), datasetVersion.getUpdateFrequency().getCode());
            if (nextVersionUpdate != null) {
                datasetVersion.setDateNextUpdate(TimeGranularityUtils.getDateInSdmxFormat(nextVersionUpdate));
                datasetVersion.getSiemacMetadataStatisticalResource().setNextVersionDate(datasetVersion.getDateNextUpdate());
            }
        }
    }

}
