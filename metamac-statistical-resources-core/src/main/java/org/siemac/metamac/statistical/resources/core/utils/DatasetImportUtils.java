package org.siemac.metamac.statistical.resources.core.utils;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.ImportDatasetJob;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoDataset;

public class DatasetImportUtils {

    private DatasetImportUtils() {
        super();
    }

    public static Boolean isDatasetImportJob(ServiceContext ctx) {
        return Boolean.TRUE.equals(ctx.getProperty(ImportDatasetJob.DATASET_IMPORT_JOB_FLAG));
    }

    public static void setRequiredMetadataForDatasetImportation(DatasetVersion datasetVersion, TaskInfoDataset taskInfoDataset) {
        setDatasetVersionVersionRationaleType(datasetVersion, taskInfoDataset);
        setDatasetVersionNextVersion(datasetVersion, taskInfoDataset);
        setDatasetVersionNextVersionDate(datasetVersion, taskInfoDataset);
    }

    private static void setDatasetVersionVersionRationaleType(DatasetVersion datasetVersion, TaskInfoDataset taskInfoDataset) {
        if (taskInfoDataset.getDatasetVersionRationaleTypes() != null) {
            for (String DatasetVersionRationaleTypes : taskInfoDataset.getDatasetVersionRationaleTypes()) {
                datasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().add(new VersionRationaleType(VersionRationaleTypeEnum.valueOf(DatasetVersionRationaleTypes)));
            }
        }
    }

    private static void setDatasetVersionNextVersion(DatasetVersion datasetVersion, TaskInfoDataset taskInfoDataset) {
        if (taskInfoDataset.getDatasetNextVersion() != null) {
            datasetVersion.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.valueOf(taskInfoDataset.getDatasetNextVersion()));
        }
    }

    private static void setDatasetVersionNextVersionDate(DatasetVersion datasetVersion, TaskInfoDataset taskInfoDataset) {
        if (taskInfoDataset.getDatasetNextVersionDate() != null) {
            datasetVersion.getSiemacMetadataStatisticalResource().setNextVersionDate(getFormattedDateTime(taskInfoDataset.getDatasetNextVersionDate()));
        }
    }

    public static DateTime getFormattedDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        return formatter.parseDateTime(date);
    }
    
}
