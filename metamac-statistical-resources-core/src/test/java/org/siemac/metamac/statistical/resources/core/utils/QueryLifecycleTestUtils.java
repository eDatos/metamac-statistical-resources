package org.siemac.metamac.statistical.resources.core.utils;

import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryLifecycleTestUtils {

    private static final Logger LOG = LoggerFactory.getLogger(QueryLifecycleTestUtils.class);

    // *****************************************************
    // PRODUCTION VALIDATION
    // *****************************************************

    public static void prepareToProductionValidation(QueryVersion queryVersion) {
        LifecycleTestUtils.prepareToProductionValidationLifecycle(queryVersion);
        prepareToLifecycleCommonQueryVersion(queryVersion);
    }

    public static void fillAsProductionValidation(QueryVersion queryVersion) {
        prepareToProductionValidation(queryVersion);
        LifecycleTestUtils.fillAsProductionValidationLifecycle(queryVersion);
    }

    // *****************************************************
    // DIFFUSION VALIDATION
    // *****************************************************

    public static void prepareToDiffusionValidation(QueryVersion queryVersion) {
        fillAsProductionValidation(queryVersion);
        LifecycleTestUtils.prepareToDiffusionValidationLifecycle(queryVersion);
    }

    public static void fillAsDiffusionValidation(QueryVersion queryVersion) {
        prepareToDiffusionValidation(queryVersion);
        LifecycleTestUtils.fillAsDiffusionValidationLifecycle(queryVersion);
    }

    // *****************************************************
    // VALIDATION REJECTED
    // *****************************************************

    public static void prepareToValidationRejected(QueryVersion queryVersion) {
        fillAsProductionValidation(queryVersion);
        LifecycleTestUtils.prepareToValidationRejectedFromProductionValidationLifecycle(queryVersion);
    }

    public static void fillAsValidationRejected(QueryVersion queryVersion) {
        prepareToProductionValidation(queryVersion);
        LifecycleTestUtils.fillAsValidationRejectedFromProductionValidationLifecycle(queryVersion);
    }

    // *****************************************************
    // PUBLISHING
    // *****************************************************

    public static void prepareToPublished(QueryVersion queryVersion) {
        fillAsDiffusionValidation(queryVersion);
        LifecycleTestUtils.prepareToPublishingLifecycle(queryVersion);
        prepareQueryToPublished(queryVersion);

    }

    private static void prepareQueryToPublished(QueryVersion queryVersion) {
        boolean datasetPublished = false;
        if (queryVersion.getDataset() != null) {
            for (DatasetVersion datasetVersion : queryVersion.getDataset().getVersions()) {
                if (isDatasetVersionPublishedVisibleBeforeQuery(datasetVersion, queryVersion)) {
                    datasetPublished = true;
                }
            }
        } else {
            datasetPublished = isDatasetVersionPublishedVisibleBeforeQuery(queryVersion.getFixedDatasetVersion(), queryVersion);
        }
        if (!datasetPublished) {
            LOG.warn("The dataset/version linked to query won't be published and visible before query");
        }
    }

    private static boolean isDatasetVersionPublishedVisibleBeforeQuery(DatasetVersion datasetVersion, QueryVersion queryVersion) {
        if (ProcStatusEnum.PUBLISHED.equals(datasetVersion.getSiemacMetadataStatisticalResource().getProcStatus())) {
            if (!datasetVersion.getSiemacMetadataStatisticalResource().getValidFrom().isAfter(queryVersion.getLifeCycleStatisticalResource().getValidFrom())) {
                return true;
            }
        }
        return false;
    }

    public static void fillAsPublished(QueryVersion queryVersion) {
        prepareToPublished(queryVersion);
        LifecycleTestUtils.fillAsPublishedLifecycle(queryVersion);
    }

    // *****************************************************
    // VERSIONING
    // *****************************************************

    public static void prepareToVersioning(QueryVersion queryVersion) {
        fillAsPublished(queryVersion);
        LifecycleTestUtils.prepareToVersioningLifecycle(queryVersion);
    }

    public static void fillAsVersioned(QueryVersion queryVersion) {
        prepareToVersioning(queryVersion);
        LifecycleTestUtils.fillAsVersionedLifecycle(queryVersion);
    }

    private static void prepareToLifecycleCommonQueryVersion(QueryVersion queryVersion) {
        if (queryVersion.getQueryType() == null) {
            queryVersion.setQueryType(QueryTypeEnum.FIXED);
        }
        if (queryVersion.getStatus() == null) {
            queryVersion.setStatus(QueryStatusEnum.ACTIVE);
        }
    }

}
