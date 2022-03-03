package org.siemac.metamac.statistical.resources.core.error;

import java.io.Serializable;

public class ServiceExceptionParameters extends ServiceExceptionBaseParameters {

    // Version
    public static final String       VERSION_TYPE                             = "parameter.resources.version_type";
    public static final String       PREVIOUS_VERSION                         = "parameter.resources.previous_version";

    // DSD
    public static final String       DSD_DIMENSION_ID                         = "parameter.resources.dsd_dimension_id";

    // Statistical operation
    public static final String       STATISTICAL_OPERATION_URN                = "parameter.resources.statistical_operation_urn";
    public static final String       STATISTICAL_OPERATION                    = "parameter.resources.statistical_operation";
    public static final String       STATISTICAL_OPERATION_CODE               = "parameter.resources.statistical_operation_code";

    // Dataset
    public static final String       DATASET_URN                              = "parameter.resources.dataset_urn";
    public static final String       DATASET_VERSION_URN                      = "parameter.resources.dataset_version_urn";
    public static final String       DATASET_VERSION_URN_TO_COPY              = "parameter.resources.dataset_version_urn_to_copy";
    public static final String       DATASET_ATTRIBUTE                        = "parameter.resources.dataset_attribute";
    public static final String       DATASET_ATTRIBUTE_VALUE                  = "parameter.resources.dataset_attribute_value";
    public static final String       DATASET_ATTRIBUTE_UUID                   = "parameter.resources.dataset_attribute_uuid";
    public static final String       DATASET_DIMENSION_REPRESENTATION_MAPPING = "parameter.resources.dataset_dimension_representation_mapping";
    public static final String       DATASET_REPOSITORY_ID                    = "parameter.resources.dataset_repository_id";

    // Publication
    public static final String       PUBLICATION_URN                          = "parameter.resources.publication_urn";
    public static final String       PUBLICATION_VERSION_URN                  = "parameter.resources.publication_version_urn";
    public static final String       PUBLICATION_VERSION_URN_TO_COPY          = "parameter.resources.publication_version_urn_to_copy";
    public static final String       CHAPTER_URN                              = "parameter.resources.chapter_urn";
    public static final String       PARENT_CHAPTER_URN                       = "parameter.resources.parent_chapter_urn";
    public static final String       ORDER_IN_LEVEL                           = "parameter.resources.order_in_level";
    public static final String       CUBE_URN                                 = "parameter.resources.cube_urn";
    public static final String       STRUCTURE_FILE_URL                       = "parameter.resources.structure_file_url";
    public static final String       STRUCTURE_FILE_LANGUAGE                  = "parameter.resources.structure_file_language";

    // Query
    public static final String       QUERY_URN                                = "parameter.resources.query_urn";
    public static final String       QUERY_VERSION_URN                        = "parameter.resources.query_version_urn";
    public static final String       QUERY_VERSION_SELECTION_TIME_PERIOD      = "parameter.resources.query_version.selection.time_period";

    // Multidataset
    public static final String       MULTIDATASET_URN                         = "parameter.resources.multidataset_urn";
    public static final String       MULTIDATASET_VERSION_URN                 = "parameter.resources.multidataset_version_urn";

    // Tasks
    public static final String       TASK_INFO_RESOURCE_ID                    = "parameter.resources.resource_id";
    public static final String       TASK_INFO_DATASET                        = "parameter.resources.task_info_dataset";
    public static final String       TASK_INFO_DATASET_DSD_URN                = "parameter.resources.data_structure_urn";
    public static final String       TASK_INFO_DATASET_DATASET_VERSION_ID     = "parameter.resources.dataset_version_id";
    public static final String       TASK_INFO_DATASET_NEW_DATASET_VERSION_ID = "parameter.resources.new_dataset_version_id";
    public static final String       TASK_INFO_DATASET_FILES                  = "parameter.resources.files";
    public static final String       TASK_DATASOURCE_ID                       = "parameter.resources.datasource_id";
    public static final String       TASK                                     = "parameter.resources.task";
    public static final String       TASK_DATASET_JOB_KEY                     = "parameter.resources.job_key";
    public static final String       NOTIFY_TO_USER                           = "parameter.resources.notify_to_user";

    // Files
    public static final String       FILE_URLS                                = "parameter.resources.file_urls";
    public static final String       FILE_DESCRIPTORS                         = "parameter.resources.file_descriptors";
    public static final String       FILE_DESCRIPTOR_INPUT_MESSAGE            = "parameter.resources.input_message";
    public static final String       FILE_DESCRIPTOR_FILE_NAME                = "parameter.resources.file_name";
    public static final String       FILE_DESCRIPTOR_FORMAT                   = "parameter.resources.dataset_format";

    // Rest parameters
    public static final Serializable CONCEPT_SCHEME_URN                       = "parameter.resources.rest.concept_scheme_urn";
    public static final Serializable CODELIST_URN                             = "parameter.resources.rest.codelist_urn";
    public static final Serializable CATEGORY_SCHEME_URN                      = "parameter.resources.rest.category_scheme_urn";
    public static final Serializable ORGANISATION_SCHEME_URN                  = "parameter.resources.rest.organisation_scheme_urn";

    // Misc
    public static final String       TIME                                     = "parameter.resources.time";
    public static final String       METAMAC_EXCEPTION                        = "parameter.resources.metamac_exception";
    public static final String       DELETE_ATTRIBUTES                        = "parameter.resources.delete_attributes";

    // Constraints
    public static final String       CONTENT_CONSTRAINT                       = "parameter.resources.content_constraint";
    public static final String       CONTENT_CONSTRAINT_ATTACHMENT            = "parameter.resources.content_constraint.attachment";

    // Db importation
    public static final String       TABLE_NAME                               = "parameter.resources.table_name";
    public static final String       COLUMNS_NAME                             = "parameter.resources.columns_name";
    public static final String       FILTER_COLUMN_NAME                       = "parameter.resources.filter_column_name";

}
