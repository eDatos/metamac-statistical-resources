package org.siemac.metamac.statistical.resources.core.notices;

public class ServiceNoticeAction {

    public static final String DUPLICATION_DATASET_JOB                                         = "notice_message.resources.action.duplication_dataset_job";
    public static final String IMPORT_DATASET_JOB                                              = "notice_message.resources.action.import_dataset_job";
    public static final String IMPORT_ATTRIBUTE_JOB                                            = "notice_message.resources.action.import_attributes_job";
    public static final String DATABASE_IMPORT_DATASET_JOB                                     = "notice_message.resources.action.database_import_dataset_job";
    public static final String DATABASE_IMPORT_DATASET_RECOVERY_JOB_ROLLBACK_PUBLISHED_DATASET = "notice_message.resources.action.database_import_dataset_recovery_job_rollback_published_dataset";
    public static final String UPDATE_GEOCOVERAGE_CACHE_DATASET_JOB                            = "notice_message.resources.action.update_geocoverage_cache_dataset_job";
    public static final String CANCEL_IN_PROGRESS_TASKS_WHILE_SERVER_SHUTDOWN                  = "notice_message.resources.action.cancel_in_progess_tasks_while_server_shutdown";
    public static final String RESOURCE_SEND_PRODUCTION_VALIDATION                             = "notice_message.resources.action.send_production_validation";
    public static final String RESOURCE_SEND_DIFFUSION_VALIDATION                              = "notice_message.resources.action.send_diffusion_validation";
    public static final String RESOURCE_CANCEL_VALIDATION                                      = "notice_message.resources.action.cancel_validation";
    public static final String RESOURCE_PUBLICATION                                            = "notice_message.resources.action.publication";
    public static final String RESOURCE_PUBLICATION_ERROR                                      = "notice_message.resources.action.publication_error";
    public static final String STREAM_MESSAGE_SEND                                             = "notice_message.resources.stream_messaging.action.send";
    public static final String X_MESSAGE_SEND                                                  = "notice_message.resources.x_messaging.action.send";
    public static final String STREAM_MESSAGE_RESEND_KAFKA_DATASETS_MESSGES                    = "notice_message.resources.stream_messaging.action.resend_datasets";

    public static final String RESOURCE_RECEIVED_FROM_KAFKA_ERROR                             = "notice_message.resources.action.received_from_kafka.error";
    public static final String UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_DATASET          = "notice_message.resources.action.update_geocoverage_cache_external_publication_variable_element_error";
    public static final String UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_ERROR            = "notice_message.resources.action.update_geocoverage_cache_get_messages_from_kafka.error";
    public static final String UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_COLLECTION_PUBLICATION_ERROR = "notice_message.resources.action.update_geocoverage_cache_get_collection_messages_from_kafka.error";

    public static final String UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_COLLECTION_PUBLICATION       = "notice_message.resources.action.update_geocoverage_cache_external_collection_publication_error";
    public static final String UPDATE_GEOCOVERAGE_CACHE_COLLECTION_PUBLICATION                = "notice_message.resources.action.update_geocoverage_cache_collection_publication_error";
    public static final String UPDATE_GEOCOVERAGE_CACHE_MULTIDATASET_PUBLICATION              = "notice_message.resources.action.update_geocoverage_cache_multidataset_publication_error";

    public static final String CREATE_REPLACE_DATASET_ERROR                                   = "notice_message.resources.action.create_replace_dataset.error";
    public static final String ASSIGN_ROLE_PERMISSIONS_DATASET_ERROR                          = "notice_message.resources.action.assign_role_permissions_dataset.error";

    // GEOGRAPHICAL CACHE
    public static final String UPDATE_GEOGRAPHICAL_RELATED_CACHE_JOB                          = "notice_message.resources.action.update_geographical_related_cache_job";

    public static final String UPDATE_OF_RESOURCE_LAST_UPDATE_CACHE_FAILED                     = "notice_message.resources.cache.update_last_update_resource.error";
    public static final String UPDATE_OF_RESOURCE_LAST_UPDATE_CACHE_NO_MORE_RETRIES           = "notice_message.resources.cache.update_last_update_resource.no_more_retries";
}
