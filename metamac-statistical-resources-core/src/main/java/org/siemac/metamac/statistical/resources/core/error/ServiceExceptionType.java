package org.siemac.metamac.statistical.resources.core.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends CommonServiceExceptionType {

    // Dataset
    public static final CommonServiceExceptionType DATASET_VERSION_NOT_FOUND                                                    = create("exception.resources.dataset.version_not_found");
    public static final CommonServiceExceptionType DATASET_LAST_VERSION_NOT_FOUND                                               = create("exception.resources.dataset.last_version_not_found");
    public static final CommonServiceExceptionType DATASET_MAX_REACHED_IN_OPERATION                                             = create("exception.resources.dataset.max_reached_in_operation");
    public static final CommonServiceExceptionType DATASET_EMPTY_DATASOURCES                                                    = create("exception.resources.dataset.empty_datasources");
    public static final CommonServiceExceptionType DATASET_NO_DATA                                                              = create("exception.resources.dataset.empty_data");

    // Dataset Version
    public static final CommonServiceExceptionType DATASET_VERSION_CANT_ALTER_DATASOURCES                                       = create("exception.resources.dataset_version.cant_alter_datasources");

    public static final CommonServiceExceptionType DATASET_VERSION_CANT_ALTER_KEEP_ALL_DATA                                     = create(
            "exception.resources.dataset_version.cant_alter_keep_all_data");
    public static final CommonServiceExceptionType DATASET_VERSION_CANT_ALTER_DATA_SOURCE_TYPE                                  = create(
            "exception.resources.dataset_version.cant_alter_data_source_type");
    public static final CommonServiceExceptionType DATASET_VERSION_CANT_CHANGE_DSD_SOME_QUERIES_EXIST                           = create(
            "exception.resources.dataset_version.cant_change_dsd_some_queries_exist");
    public static final CommonServiceExceptionType DATASET_VERSION_ALREADY_BEEN_REPLACED_BY_OTHER_DATASET_VERSION               = create(
            "exception.resources.dataset_version.already_been_replaced_by_other_dataset_version");
    public static final CommonServiceExceptionType DATASET_VERSION_CANT_REPLACE_ITSELF                                          = create("exception.resources.dataset_version.cant_replace_itself");
    public static final CommonServiceExceptionType DATASET_VERSION_CANT_BE_DELETED                                              = create("exception.resources.dataset_version.cant_be_deleted");
    public static final CommonServiceExceptionType DATASET_VERSION_VALIDATE_ATTRIBUTES_ERROR                                    = create(
            "exception.resources.dataset_version.validate_attributes_error");
    public static final CommonServiceExceptionType DATASET_VERSION_IS_PART_OF_OTHER_RESOURCES                                   = create("exception.resources.dataset_version.is_part_of_publications");
    public static final CommonServiceExceptionType DATASET_VERSION_IS_PART_OF_NOT_VISIBLE_PUBLICATION                           = create(
            "exception.resources.dataset_version.is_part_of_not_visible_publication");
    public static final CommonServiceExceptionType DATASET_VERSION_IS_REPLACED_BY_OTHER_RESOURCE                                = create(
            "exception.resources.dataset_version.is_replaced_by_other_datasets");
    public static final CommonServiceExceptionType DATASET_VERSION_IS_REPLACED_BY_NOT_VISIBLE                                   = create(
            "exception.resources.dataset_version.is_replaced_by_not_visible_dataset");
    public static final CommonServiceExceptionType DATASET_VERSION_IS_REQUIRED_BY_OTHER_RESOURCES                               = create("exception.resources.dataset_version.is_required_by_queries");
    public static final CommonServiceExceptionType DATASET_VERSION_IS_REQUIRED_BY_OTHER_RESOURCES_LAST_VERSION_INCOMPATIBLE     = create(
            "exception.resources.dataset_version.is_required_by_queries_last_version_incompatible");
    public static final CommonServiceExceptionType DATASET_VERSION_IS_REQUIRED_BY_NOT_VISIBLE_QUERY                             = create(
            "exception.resources.dataset_version.is_required_by_not_visible_query");
    public static final CommonServiceExceptionType DATASET_OBSERVATION_NONENUMERATED_TEMPORAL_PATTERN                           = create(
            "exception.resources.dataset_version.observational_time_period_pattern");

    // Datasource
    public static final CommonServiceExceptionType DATASOURCE_NOT_FOUND                                                         = create("exception.resources.datasource.not_found");
    public static final CommonServiceExceptionType DATASOURCE_DATA_DELETE_ERROR                                                 = create("exception.resources.datasource.data.delete_error");
    public static final CommonServiceExceptionType DATASOURCE_IN_DATASET_VERSION_WITH_QUERIES_DELETE_ERROR                      = create(
            "exception.resources.datasource.data.datasetversion_with_queries_delete_error");

    // Categorisation
    public static final CommonServiceExceptionType CATEGORISATION_NOT_FOUND                                                     = create("exception.resources.categorisation.not_found");
    public static final CommonServiceExceptionType CATEGORISATION_CANT_END_VALIDITY_WITHOUT_VALIDITY_STARTED                    = create(
            "exception.resources.categorisation.cant_end_validity_without_validity_started");
    public static final CommonServiceExceptionType CATEGORISATION_CANT_END_VALIDITY_BEFORE_VALIDITY_STARTED                     = create(
            "exception.resources.categorisation.cant_end_validity_before_validity_started");

    // Publication
    public static final CommonServiceExceptionType PUBLICATION_LAST_VERSION_NOT_FOUND                                           = create("exception.resources.publication.last_version_not_found");
    public static final CommonServiceExceptionType PUBLICATION_MAX_REACHED_IN_OPERATION                                         = create("exception.resources.publication.max_reached_in_operation");

    // Publication Version
    public static final CommonServiceExceptionType PUBLICATION_VERSION_NOT_FOUND                                                = create("exception.resources.publication.version_not_found");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_ALREADY_BEEN_REPLACED_BY_OTHER_PUBLICATION_VERSION       = create(
            "exception.resources.publication_version.already_been_replaced_by_other_publication_version");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_CANT_REPLACE_ITSELF                                      = create("exception.resources.publication_version.cant_replace_itself");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_MUST_HAVE_AT_LEAST_ONE_CUBE                              = create(
            "exception.resources.publication_version.must_have_at_least_one_cube");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_CHAPTER_MUST_HAVE_AT_LEAST_ONE_CUBE                      = create(
            "exception.resources.publication_version.chapter_must_have_at_least_one_cube");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_CUBE_MUST_LINK_TO_DATASET_QUERY_OR_MULTIDATASET          = create(
            "exception.resources.publication_version.cube_must_link_to_dataset_query_or_multidataset");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_LINKED_TO_NOT_PUBLISHED_DATASET                          = create(
            "exception.resources.publication_version.linked_to_dataset_with_no_published_version");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_LINKED_TO_NOT_PUBLISHED_QUERY                            = create(
            "exception.resources.publication_version.linked_to_query_not_published");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_LINKED_TO_NOT_PUBLISHED_MULTIDATASET                     = create(
            "exception.resources.publication_version.linked_to_multidataset_not_published");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_CANT_BE_DELETED                                          = create("exception.resources.publication_version.cant_be_deleted");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_IS_REPLACED_BY_OTHER_RESOURCE                            = create(
            "exception.resources.publication_version.is_replaced_by_other_publication");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_IS_REPLACED_BY_NOT_VISIBLE                               = create(
            "exception.resources.publication_version.is_replaced_by_not_visible_publication");

    // Publication structure importation
    public static final CommonServiceExceptionType PUBLICATION_VERSION_STRUCTURE_IMPORTATION_ERROR                              = create(
            "exception.resources.publication_version.structure.importation.error");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_STRUCTURE_IMPORTATION_FORMAT_NOT_VALID                   = create(
            "exception.resources.publication_version.structure.importation.format_not_valid");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_STRUCTURE_IMPORTATION_HEADER_NOT_VALID                   = create(
            "exception.resources.publication_version.structure.importation.header_not_valid");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CHAPTER_WITH_RELATED_RESOURCE      = create(
            "exception.resources.publication_version.structure.importation.chapter_with_related_resource");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITHOUT_RELATED_RESOURCE      = create(
            "exception.resources.publication_version.structure.importation.cube_without_related_resource");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_WRONG_RELATED_RESOURCE   = create(
            "exception.resources.publication_version.structure.importation.cube_with_wrong_related_resource");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_STRUCTURE_IMPORTATION_ELEMENT_WITH_EMTPY_NAME            = create(
            "exception.resources.publication_version.structure.importation.element_with_empty_name");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_SUBELEMENTS              = create(
            "exception.resources.publication_version.structure.importation.cube_with_subelements");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_NONEXISTENT_QUERY        = create(
            "exception.resources.publication_version.structure.importation.cube_with_nonexistent_query");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_NONEXISTENT_DATASET      = create(
            "exception.resources.publication_version.structure.importation.cube_with_nonexistent_dataset");
    public static final CommonServiceExceptionType PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_NONEXISTENT_MULTIDATASET = create(
            "exception.resources.publication_version.structure.importation.cube_with_nonexistent_multidataset");

    // Chapter
    public static final CommonServiceExceptionType CHAPTER_NOT_FOUND                                                            = create("exception.resources.chapter.not_found");
    public static final CommonServiceExceptionType CHAPTER_NOT_FOUND_IN_PUBLICATION_VERSION                                     = create(
            "exception.resources.chapter.not_found_in_publication_version");

    // Cube
    public static final CommonServiceExceptionType CUBE_NOT_FOUND                                                               = create("exception.resources.cube.not_found");

    // Multidataset Cube
    public static final CommonServiceExceptionType MULTIDATASET_CUBE_NOT_FOUND                                                  = create("exception.resources.multidatasetcube.not_found");

    public static final CommonServiceExceptionType MULTIDATASET_CUBE_DUPLICATE_IDENTIFIER                                       = create("exception.resources.multidatasetcube.duplicate_identifier");

    // Multidataset
    public static final CommonServiceExceptionType MULTIDATASET_VERSION_NOT_FOUND                                               = create("exception.resources.multidataset.version_not_found");
    public static final CommonServiceExceptionType MULTIDATASET_LAST_VERSION_NOT_FOUND                                          = create("exception.resources.multidataset.last_version_not_found");
    public static final CommonServiceExceptionType MULTIDATASET_MAX_REACHED_IN_OPERATION                                        = create("exception.resources.multidataset.max_reached_in_operation");

    // Multidataset version
    public static final CommonServiceExceptionType MULTIDATASET_VERSION_CANT_REPLACE_ITSELF                                     = create(
            "exception.resources.multidataset_version.cant_replace_itself");
    public static final CommonServiceExceptionType MULTIDATASET_VERSION_ALREADY_BEEN_REPLACED_BY_OTHER_MULTIDATASET_VERSION     = create(
            "exception.resources.multidataset_version.already_been_replaced_by_other_multidataset_version");
    public static final CommonServiceExceptionType MULTIDATASET_VERSION_IS_REPLACED_BY_OTHER_RESOURCE                           = create(
            "exception.resources.multidataset_version.is_replaced_by_other_multidataset");
    public static final CommonServiceExceptionType MULTIDATASET_VERSION_CANT_BE_DELETED                                         = create("exception.resources.multidataset_version.cant_be_deleted");
    public static final CommonServiceExceptionType MULTIDATASET_VERSION_MUST_HAVE_AT_LEAST_ONE_CUBE                             = create(
            "exception.resources.multidataset_version.must_have_at_least_one_cube");
    public static final CommonServiceExceptionType MULTIDATASET_VERSION_CUBE_MUST_LINK_TO_DATASET_OR_QUERY                      = create(
            "exception.resources.multidataset_version.cube_must_link_to_dataset_or_query");
    public static final CommonServiceExceptionType MULTIDATASET_VERSION_LINKED_TO_NOT_PUBLISHED_DATASET                         = create(
            "exception.resources.multidataset_version.linked_to_dataset_with_no_published_version");
    public static final CommonServiceExceptionType MULTIDATASET_VERSION_LINKED_TO_NOT_PUBLISHED_QUERY                           = create(
            "exception.resources.multidataset_version.linked_to_query_not_published");

    // Query
    public static final CommonServiceExceptionType QUERY_NOT_FOUND                                                              = create("exception.resources.query.not_found");
    public static final CommonServiceExceptionType QUERY_LAST_VERSION_NOT_FOUND                                                 = create("exception.resources.query.last_version_not_found");

    // Query Version
    public static final CommonServiceExceptionType QUERY_VERSION_NOT_COMPATIBLE_WITH_DATASET                                    = create("exception.resources.query_version.not_compatible");
    public static final CommonServiceExceptionType QUERY_VERSION_NOT_COMPATIBLE_WITH_LAST_PUBLISHED_DATASET_VERSION             = create(
            "exception.resources.query_version.not_compatible_last_published_dataset_version");
    public static final CommonServiceExceptionType QUERY_VERSION_DATASET_VERSION_MUST_BE_PUBLISHED                              = create(
            "exception.resources.query_version.dataset_version_must_be_published");
    public static final CommonServiceExceptionType QUERY_VERSION_DATASET_WITH_NO_PUBLISHED_VERSION                              = create(
            "exception.resources.query_version.dataset_with_no_published_version");
    public static final CommonServiceExceptionType QUERY_VERSION_PUBLISH_INVALID_STATUS                                         = create("exception.resources.query_version.publish_invalid_status");
    public static final CommonServiceExceptionType QUERY_VERSION_PUBLISH_MUST_LINK_TO_DATASET                                   = create(
            "exception.resources.query_version.publish_must_link_to_dataset");
    public static final CommonServiceExceptionType QUERY_VERSION_CANT_BE_DELETED                                                = create("exception.resources.query_version.cant_be_deleted");
    public static final CommonServiceExceptionType QUERY_VERSION_IS_PART_OF_OTHER_RESOURCES                                     = create("exception.resources.query_version.is_part_of_publications");
    public static final CommonServiceExceptionType QUERY_VERSION_IS_PART_OF_NOT_VISIBLE_PUBLICATION                             = create(
            "exception.resources.query_version.is_part_of_not_visible_publication");

    // Constraints
    public static final CommonServiceExceptionType CONSTRAINTS_CREATE_DATASET_WITH_DATASOURCES                                  = create(
            "exception.resources.constraints.create.dataset_with_datasources");
    public static final CommonServiceExceptionType CONSTRAINTS_UPDATE_FINAL                                                     = create("exception.resources.constraints.update.final");
    public static final CommonServiceExceptionType CONSTRAINTS_UPDATE_DATASOURCES_NO_EMPTY                                      = create("exception.resources.constraints.update.datasources_no_empty");

    // Identifiable Statistical Resource
    public static final CommonServiceExceptionType IDENTIFIABLE_STATISTICAL_RESOURCE_NOT_FOUND                                  = create(
            "exception.resources.identifiable_statistical_resource.not_found");
    public static final CommonServiceExceptionType IDENTIFIABLE_STATISTICAL_RESOURCE_URN_DUPLICATED                             = create(
            "exception.resources.identifiable_statistical_resource.urn_duplicated");

    // Life Cycle Statistical Resource
    public static final CommonServiceExceptionType LIFE_CYCLE_WRONG_PROC_STATUS                                                 = create("exception.resources.life_cycle.wrong_proc_status");
    public static final CommonServiceExceptionType RESOURCE_MAXIMUM_VERSION_REACHED                                             = create("exception.resources.version.max_version_reached");

    // Related resources
    public static final CommonServiceExceptionType RELATED_RESOURCE_NOT_PUBLISHED                                               = create("exception.resources.related_resource.not_published");
    public static final CommonServiceExceptionType RELATED_RESOURCE_WITHOUT_PUBLISHED_VERSION                                   = create(
            "exception.resources.related_resource.without_published_version");

    // External items
    public static final CommonServiceExceptionType EXTERNAL_ITEM_NOT_PUBLISHED                                                  = create("exception.resources.external_item.urn.not_published");

    // Lists
    public static final CommonServiceExceptionType STATISTIC_OFFICIALITY_NOT_FOUND                                              = create("exception.resources.statistic_officiality.not_found");

    // Tasks
    public static final CommonServiceExceptionType TASKS_ERROR_MAX_CURRENT_JOBS                                                 = create("exception.resources.task.error.max_current_jobs");
    public static final CommonServiceExceptionType TASKS_JOB_NOT_FOUND                                                          = create("exception.resources.task.error.not_found");
    public static final CommonServiceExceptionType TASKS_ERROR_SERVER_DOWN                                                      = create("exception.resources.task.error.server_down");
    public static final CommonServiceExceptionType TASKS_ERROR                                                                  = create("exception.resources.task.error");
    public static final CommonServiceExceptionType TASKS_SCHEDULER_ERROR                                                        = create("exception.resources.task.scheduler.error");
    public static final CommonServiceExceptionType TASKS_JOB_RECOVERY_IN_PROCESS                                                = create("exception.resources.task.error.recovery_in_process");
    public static final CommonServiceExceptionType TASKS_JOB_DUPLICATION_IN_PROCESS                                             = create("exception.resources.task.error.duplication_in_process");
    public static final CommonServiceExceptionType TASKS_JOB_IMPORTATION_IN_PROCESS                                             = create("exception.resources.task.error.imporation_in_process");
    public static final CommonServiceExceptionType TASKS_JOB_DATABASE_IMPORTATION_IN_PROCESS                                    = create(
            "exception.resources.task.error.database_importation_in_process");
    public static final CommonServiceExceptionType TASKS_JOB_UPDATE_GEOCOVERAGE_CACHE_IN_PROCESS                                = create(
            "exception.resources.task.error.update_geocoverage_cache_in_process");
    // TODO EDATOS-3770 PONER CONSTANTE
    public static final CommonServiceExceptionType TASKS_JOB_UPDATE_EXTERNAL_GEOCOVERAGE_CACHE_IN_PROCESS                                = create(
            "exception.resources.task.error.update_external_geocoverage_cache_in_process");
    public static final CommonServiceExceptionType TASKS_IN_PROGRESS                                                            = create("exception.resources.task.in_progress");

    // Dataset Importation
    public static final CommonServiceExceptionType FILE_NOT_LINKED_TO_ANY_DATASET_IN_STATISTICAL_OPERATION                      = create(
            "exception.resources.dataset.importation.file_not_linked_to_any_dataset_in_statistical_operation");
    public static final CommonServiceExceptionType INVALID_DATA_SOURCE_TYPE_FOR_DATASET_DATA_IMPORTATION                        = create(
            "exception.resources.dataset.importation.invalid_data_source_type_for_data_importation");
    public static final CommonServiceExceptionType INVALID_FILE_FOR_DATASET_VERSION                                             = create(
            "exception.resources.dataset.importation.invalid_file_for_dataset");
    public static final CommonServiceExceptionType TABLE_NOT_EXIST                                                              = create("exception.resources.dataset.importation.table_not_exist");
    public static final CommonServiceExceptionType DIMENSION_COLUMN_NOT_EXIST                                                   = create(
            "exception.resources.dataset.importation.dimension_column_not_exist");
    public static final CommonServiceExceptionType ATTRIBUTE_COLUMN_NOT_EXIST                                                   = create(
            "exception.resources.dataset.importation.attribute_column_not_exist");
    public static final CommonServiceExceptionType OBS_COLUMN_NOT_EXIST                                                         = create(
            "exception.resources.dataset.importation.obs_column_not_exist");
    public static final CommonServiceExceptionType FILTER_COLUMN_NOT_EXIST                                                      = create(
            "exception.resources.dataset.importation.filter_column_not_exist");
    public static final CommonServiceExceptionType INVALID_TABLENAME_LENGTH                                                     = create(
            "exception.resources.dataset.importation.invalid_tablename_length");
    public static final CommonServiceExceptionType INVALID_TABLENAME_FORMAT                                                     = create(
            "exception.resources.dataset.importation.invalid_tablename_format");
    public static final CommonServiceExceptionType IMPORTATION_DATASET_VERSION_ERROR                                            = create(
            "exception.resources.dataset.importation.dataset_version_error");
    public static final CommonServiceExceptionType IMPORTATION_CSV_FILE_ERROR                                                   = create("exception.resources.dataset.importation.csv_file_error");
    public static final CommonServiceExceptionType IMPORTATION_ATTR_CODE_ENUM_NOT_VALID                                         = create(
            "exception.resources.dataset.importation.attribute_code_enumeration_not_valid");
    public static final CommonServiceExceptionType IMPORTATION_ATTR_NOT_MATCH                                                   = create("exception.resources.dataset.importation.attribute_not_match");
    public static final CommonServiceExceptionType IMPORTATION_MORE_THAN_ONE_DATASOURCE_FOR_DATABASE_IMPORTATION_ERROR          = create(
            "exception.resources.dataset.importation.more_than_one_datasource_for_database_importation_error");

    public static final CommonServiceExceptionType IMPORTATION_DIM_CODE_ENUM_NOT_VALID                                          = create(
            "exception.resources.dataset.importation.dimension_code_enumeration_not_valid");
    public static final CommonServiceExceptionType IMPORTATION_DIM_CODE_ENUM_NOT_VALID_TRANSLATION                              = create(
            "exception.resources.dataset.importation.dimension_code_enumeration_not_valid_translation");
    public static final CommonServiceExceptionType IMPORTATION_DIM_KEY_CARDINALITY_NOT_MATCH                                    = create(
            "exception.resources.dataset.importation.dimension_keys_cardinality_not_match");
    public static final CommonServiceExceptionType IMPORTATION_DIM_NOT_MATCH                                                    = create("exception.resources.dataset.importation.dimension_not_match");

    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_ATTR_NOT_MATCH                                       = create(
            "exception.resources.dataset.importation.observation_attribute_not_match");
    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_ATTR_CARDINALITY_EXCEEDED                            = create(
            "exception.resources.dataset.importation.observation_attributes_cardinality_exceeded");

    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_ATTR_VALUE_SIZE_EXCEEDED                             = create(
            "exception.resources.dataset.importation.observation_attributes_value_size_exceeded");
    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_MANDATORY_ATTR_NOT_FOUND                             = create(
            "exception.resources.dataset.importation.observation_mandatory_attribute_not_found");

    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_MANDATORY_CONTENT_CONSTRAINT_FAIL                    = create(
            "exception.resources.dataset.importation.observation_content_constraint_fail");

    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_NONENUMERATED_MINLENGTH                              = create(
            "exception.resources.dataset.importation.observation.nonenumerated.minlength");
    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_NONENUMERATED_MAXLENGTH                              = create(
            "exception.resources.dataset.importation.observation.nonenumerated.maxlength");
    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_NONENUMERATED_MINVALUE                               = create(
            "exception.resources.dataset.importation.observation.nonenumerated.minvalue");
    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_NONENUMERATED_MAXVALUE                               = create(
            "exception.resources.dataset.importation.observation.nonenumerated.maxvalue");
    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_NONENUMERATED_PATTERN                                = create(
            "exception.resources.dataset.importation.observation.nonenumerated.pattern");
    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_NONENUMERATED_TEMPORAL_PATTERN                       = create(
            "exception.resources.dataset.importation.observation.nonenumerated.temporal_pattern");
    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_NONENUMERATED_DECIMAL                                = create(
            "exception.resources.dataset.importation.observation.nonenumerated.decimal");
    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_NONENUMERATED_STARTTIMES                             = create(
            "exception.resources.dataset.importation.observation.nonenumerated.starttimes");
    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_NONENUMERATED_ENDTIMES                               = create(
            "exception.resources.dataset.importation.observation.nonenumerated.endtimes");

    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_CODE_ENUM_NOT_VALID                                  = create(
            "exception.resources.dataset.importation.observation.code_enumeration_not_valid");
    public static final CommonServiceExceptionType IMPORTATION_OBSERVATION_NOT_NUMERIC                                          = create(
            "exception.resources.dataset.importation.observation.not_numeric");

    public static final CommonServiceExceptionType VALIDATION_NONOBSLEVEL_MANDATORY_ATTR_NOT_FOUND                              = create(
            "exception.resources.dataset.validation.nonobslevel_mandatory_attribute_not_found");

    // Notices
    public static final CommonServiceExceptionType DUPLICATION_DATASET_JOB_ERROR                                                = create(
            "notice_message.resources.exception.duplication_dataset_job.fails");
    public static final CommonServiceExceptionType DUPLICATION_DATASET_JOB_ERROR_AND_CANT_MARK_AS_ERROR                         = create(
            "notice_message.resources.exception.duplication_dataset_job.fails_and_cant_mark_as_error");
    public static final CommonServiceExceptionType IMPORT_DATASET_JOB_ERROR                                                     = create("notice_message.resources.exception.import_dataset_job.fails");
    public static final CommonServiceExceptionType IMPORT_DATASET_JOB_ERROR_AND_CANT_MARK_AS_ERROR                              = create(
            "notice_message.resources.exception.import_dataset_job.fails_and_cant_mark_as_error");

    public static final CommonServiceExceptionType DB_IMPORT_DATASET_JOB_ERROR                                                  = create(
            "notice_message.resources.exception.db_import_dataset_job.fails");
    public static final CommonServiceExceptionType DB_IMPORT_DATASET_JOB_ERROR_AND_CANT_MARK_AS_FINISHED                        = create(
            "notice_message.resources.exception.db_import_dataset_job.fails_and_cant_mark_as_finished");

    // Stream messaging
    public static final CommonServiceExceptionType UNABLE_TO_SEND_STREAM_MESSAGING_TO_STREAM_MESSAGING_SERVER                   = create("stream_message.resources.exception.send_message.fails");
    public static final CommonServiceExceptionType STREAM_MESSAGING_TOPIC_IS_INVALID                                            = create("stream_message.resources.exception.topic.invalid");
    public static final CommonServiceExceptionType STREAM_MESSAGING_MISSING_MANDATORY_SETTINGS                                  = create("stream_message.resources.exception.config.missing_settings");

    public static final CommonServiceExceptionType FILE_ENCODING_ERROR                                                          = create("exception.resources.dataset.importation.file_encoding_error");

    // Geographic Coverage
    public static final CommonServiceExceptionType GEOGRAPHICAL_COVERAGE_CODE_NOT_FOUND                                         = create(
            "exception.resources.dataset_version.geocoverage.geographical_coverage_code_not_found");
    public static final CommonServiceExceptionType UPDATE_GEOCOVERAGE_CACHE_DATASET_JOB_ERROR                                   = create("exception.resources.dataset_version.geocoverage.job_error");
    public static final CommonServiceExceptionType UPDATE_GEOCOVERAGE_CACHE_DATASET_JOB_ERROR_AND_CANT_MARK_AS_ERROR            = create(
        "exception.resources.dataset_version.geocoverage.job_error_and_cant_mark_as_error");
    //TODO EDATOS-3770 CREATE THIS INTERNATIONAL MESSAGE
    public static final CommonServiceExceptionType UPDATE_GEOCOVERAGE_CACHE_DATASET_FROM_EXTERNAL_PUBLICATION_ERROR_STREAM_NO_VALID = create(
            "exception.resources.dataset_version.geocoverage.cache_dataset_from_external_publication_error_stream_no_valid");
    public static final CommonServiceExceptionType UPDATE_GEOCOVERAGE_CACHE_DATASET_FROM_EXTERNAL_PUBLICATION_ERROR = create(
            "exception.resources.dataset_version.geocoverage.cache_dataset_from_external_publication_error");
    public static final CommonServiceExceptionType UPDATE_GEOCOVERAGE_CACHE_DATASET_FROM_EXTERNAL_PUBLICATION_JOB_ERROR_AND_CANT_MARK_AS_ERROR            = create(
            "exception.resources.dataset_version.geocoverage.cache_external_publication_job_error_and_cant_mark_as_error");
    public static final CommonServiceExceptionType UPDATE_GEOCOVERAGE_CACHE_DATASET_JOB_FROM_EXTERNAL_PUBLICATION_ERROR                                   = create("exception.resources.dataset_version.geocoverage.job_from_external_publication_error");
    
    }
