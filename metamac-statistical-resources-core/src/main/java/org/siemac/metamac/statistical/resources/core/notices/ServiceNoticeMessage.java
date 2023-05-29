package org.siemac.metamac.statistical.resources.core.notices;

public class ServiceNoticeMessage {

    // Clone dataset
    public static final String DUPLICATION_DATASET_JOB_OK             = "notice_message.resources.message.duplication_dataset_job.ok";
    public static final String IMPORT_DATASET_JOB_OK                  = "notice_message.resources.message.import_dataset_job.ok";
    public static final String IMPORT_ATTRIBUTES_JOB_OK               = "notice_message.resources.message.import_attributes_job.ok";
    public static final String DATABASE_IMPORT_DATASET_JOB_DETECTED   = "notice_message.resources.message.database_import_dataset_job";
    public static final String RESOURCE_SEND_PRODUCTION_VALIDATION_OK = "notice_message.resources.action.send_production_validation.ok";
    public static final String RESOURCE_SEND_DIFFUSION_VALIDATION_OK  = "notice_message.resources.action.send_diffusion_validation.ok";
    public static final String RESOURCE_CANCEL_VALIDATION_OK          = "notice_message.resources.action.cancel_validation.ok";
    public static final String RESOURCE_PUBLICATION_OK                = "notice_message.resources.action.publication.ok";
    public static final String RESOURCE_PUBLICATION_ERROR_OK          = "notice_message.resources.action.publication_error.ok";
    public static final String UPDATE_GEOCOVERAGE_CACHE_DATASET_JOB_ERROR = "notice_message.resources.action.update_geocoverage_cache.error";
    public static final String RESOURCE_RECEIVED_FROM_KAFKA_ERROR     = "notice_message.resources.message.received_from_kafka.error";
    public static final String UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_ERROR = "notice_message.resources.message.update_geocoverage_cache_get_messages_from_kafka.error";
    
    
    
    // Stream messaging
    public static final String STREAM_MESSAGE_SEND_ERROR              = "notice_message.resources.stream_messaging.action.send.error";

    public static final String CREATE_REPLACE_DATASET_ERROR           = "notice_message.resources.message.create_replace_dataset.error";
    public static final String ASSIGN_ROLE_PERMISSIONS_DATASET_ERROR  = "notice_message.resources.message.assign_role_permissions_dataset.error";
}
