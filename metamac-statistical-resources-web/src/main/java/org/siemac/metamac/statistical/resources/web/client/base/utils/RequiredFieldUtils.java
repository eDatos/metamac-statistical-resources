package org.siemac.metamac.statistical.resources.web.client.base.utils;

import static org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS.GEOGRAPHIC_COVERAGE;
import static org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS.GEOGRAPHIC_GRANULARITY;
import static org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS.STATISTIC_OFFICIALITY;
import static org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS.TEMPORAL_COVERAGE;
import static org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS.TEMPORAL_GRANULARITY;
import static org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS.UPDATE_FRECUENCY;
import static org.siemac.metamac.statistical.resources.web.client.model.ds.NameableResourceDS.DESCRIPTION;
import static org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS.COMMON_METADATA;
import static org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS.CREATOR;
import static org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS.LANGUAGE;
import static org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS.LANGUAGES;
import static org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS.PUBLISHER;
import static org.siemac.metamac.statistical.resources.web.client.model.ds.VersionableResourceDS.NEXT_VERSION;
import static org.siemac.metamac.statistical.resources.web.client.model.ds.VersionableResourceDS.VERSION_RATIONALE_TYPES;
import static org.siemac.metamac.statistical.resources.web.client.multidataset.model.ds.MultidatasetDS.FILTERING_DIMENSION;
import static org.siemac.metamac.statistical.resources.web.client.query.model.ds.QueryDS.LATEST_N_DATA;
import static org.siemac.metamac.statistical.resources.web.client.query.model.ds.QueryDS.SELECTION;
import static org.siemac.metamac.statistical.resources.web.client.query.model.ds.QueryDS.TYPE;

import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;

/**
 * This class contain methods that, given a procStatus of a resource, return the fields that are required to go to the next procStatus.
 */
public class RequiredFieldUtils {

    // PUBLICATION VERSION

    private static final String[] publicationFieldsToProductionValidation = new String[]{DESCRIPTION, NEXT_VERSION, VERSION_RATIONALE_TYPES, LANGUAGE, LANGUAGES, CREATOR, PUBLISHER, COMMON_METADATA};
    private static final String[] publicationFieldsToDiffusionValidation  = publicationFieldsToProductionValidation;
    private static final String[] publicationFieldsToPublication          = publicationFieldsToDiffusionValidation;

    public static String[] getPublicationRequiredFieldsToNextProcStatus(ProcStatusEnum currentProcStatus) {
        switch (currentProcStatus) {
            case DRAFT:
                return publicationFieldsToProductionValidation;
            case VALIDATION_REJECTED:
                return publicationFieldsToProductionValidation;
            case PRODUCTION_VALIDATION:
                return publicationFieldsToDiffusionValidation;
            case DIFFUSION_VALIDATION:
                return publicationFieldsToPublication;
            default:
                return new String[]{};
        }
    }

    // DATASET VERSION

    private static final String[] datasetFieldsToProductionValidation = new String[]{DESCRIPTION, NEXT_VERSION, VERSION_RATIONALE_TYPES, LANGUAGE, LANGUAGES, CREATOR, PUBLISHER, COMMON_METADATA,
            GEOGRAPHIC_COVERAGE, GEOGRAPHIC_GRANULARITY, TEMPORAL_COVERAGE, TEMPORAL_GRANULARITY, UPDATE_FRECUENCY, STATISTIC_OFFICIALITY};
    private static final String[] datasetFieldsToDiffusionValidation  = datasetFieldsToProductionValidation;
    private static final String[] datasetFieldsToPublication          = datasetFieldsToDiffusionValidation;

    public static String[] getDatasetRequiredFieldsToNextProcStatus(ProcStatusEnum currentProcStatus) {
        switch (currentProcStatus) {
            case DRAFT:
                return datasetFieldsToProductionValidation;
            case VALIDATION_REJECTED:
                return datasetFieldsToProductionValidation;
            case PRODUCTION_VALIDATION:
                return datasetFieldsToDiffusionValidation;
            case DIFFUSION_VALIDATION:
                return datasetFieldsToPublication;
            default:
                return new String[]{};
        }
    }

    // QUERY VERSION

    private static final String[] queryFieldsToProductionValidation = new String[]{DESCRIPTION, NEXT_VERSION, VERSION_RATIONALE_TYPES, TYPE, SELECTION, LATEST_N_DATA};
    private static final String[] queryFieldsToDiffusionValidation  = queryFieldsToProductionValidation;
    private static final String[] queryFieldsToPublication          = queryFieldsToDiffusionValidation;

    public static String[] getQueryRequiredFieldsToNextProcStatus(ProcStatusEnum currentProcStatus) {
        if (currentProcStatus == null) {
            return queryFieldsToProductionValidation;
        }
        switch (currentProcStatus) {
            case DRAFT:
                return queryFieldsToProductionValidation;
            case VALIDATION_REJECTED:
                return queryFieldsToProductionValidation;
            case PRODUCTION_VALIDATION:
                return queryFieldsToDiffusionValidation;
            case DIFFUSION_VALIDATION:
                return queryFieldsToPublication;
            default:
                return new String[]{};
        }
    }

    // MULTIDATASET VERSION

    private static final String[] multidatasetFieldsToProductionValidation = new String[]{DESCRIPTION, FILTERING_DIMENSION, NEXT_VERSION, VERSION_RATIONALE_TYPES, LANGUAGE, LANGUAGES, CREATOR,
            PUBLISHER, COMMON_METADATA};
    private static final String[] multidatasetFieldsToDiffusionValidation  = multidatasetFieldsToProductionValidation;
    private static final String[] multidatasetFieldsToMultidataset         = multidatasetFieldsToDiffusionValidation;

    public static String[] getMultidatasetRequiredFieldsToNextProcStatus(ProcStatusEnum currentProcStatus) {
        switch (currentProcStatus) {
            case DRAFT:
                return multidatasetFieldsToProductionValidation;
            case VALIDATION_REJECTED:
                return multidatasetFieldsToProductionValidation;
            case PRODUCTION_VALIDATION:
                return multidatasetFieldsToDiffusionValidation;
            case DIFFUSION_VALIDATION:
                return multidatasetFieldsToMultidataset;
            default:
                return new String[]{};
        }
    }

}
