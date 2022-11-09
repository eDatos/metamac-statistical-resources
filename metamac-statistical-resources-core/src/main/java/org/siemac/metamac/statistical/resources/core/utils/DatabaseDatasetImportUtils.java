package org.siemac.metamac.statistical.resources.core.utils;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
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

        // TODO METAMAC-2866 - METAMAC-2956 How to define these metadata has not been defined yet
        DatabaseDatasetImportUtils.setDatasetVersionNextVersion(datasetVersion);
        DatabaseDatasetImportUtils.setDatasetVersionNextVersionDate(datasetVersion);
        DatabaseDatasetImportUtils.setDatasetVersionDateNextUpdate(datasetVersion);
    }

    private static void setDatasetVersionVersionRationaleType(DatasetVersion datasetVersion) {
        if (CollectionUtils.isEmpty(datasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes())) {
            datasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().add(new VersionRationaleType(VersionRationaleTypeEnum.MINOR_DATA_UPDATE));
        }
    }

    private static void setDatasetVersionNextVersion(DatasetVersion datasetVersion) {
        if (datasetVersion.getSiemacMetadataStatisticalResource().getNextVersion() == null) {
            // TODO METAMAC-2866 - METAMAC-2956 The value of this metadata should be "SCHEDULED_UPDATE" but it is necessary to establish a translation between the value of the update_frecuency
            // metadata and next_version_date and date_next_update to calculate their values ​​in the proper way.
            // The task METAMAC-2956 was created in order to define and resolve this TODO
            datasetVersion.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);
            datasetVersion.setUpdateFrequency(null);
        }
    }

    private static void setDatasetVersionNextVersionDate(DatasetVersion datasetVersion) {
        // TODO METAMAC-2866 - METAMAC-2956 it's necessary to resolve TODO in method setDatasetVersionNextVersion to assign proper value in this metadata
    }

    private static void setDatasetVersionDateNextUpdate(DatasetVersion datasetVersion) {
        // TODO METAMAC-2866 - METAMAC-2956 it's necessary to resolve TODO in method setDatasetVersionNextVersion to assign proper value in this metadata
    }
}
