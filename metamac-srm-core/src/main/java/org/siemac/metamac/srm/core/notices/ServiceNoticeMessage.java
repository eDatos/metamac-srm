package org.siemac.metamac.srm.core.notices;

public class ServiceNoticeMessage {

    // Import
    public static final String IMPORT_TSV_JOB_OK                             = "notice_message.srm.message.import_tsv_job.ok";
    public static final String IMPORT_SHAPE_JOB_OK                           = "notice_message.srm.message.import_shape_job.ok";
    public static final String IMPORT_SHAPE_JOB_WARNING                      = "notice_message.srm.message.import_shape_job.warning";

    // Publish / Merge
    public static final String PUBLISH_INTERNALLY_CODELIST_JOB_OK            = "notice_message.srm.message.publish_internally_codelist_job.ok";
    public static final String PUBLISH_KAFKA_VARIABLE_ELEMENT_MESSAGE_JOB_OK = "notice_message.srm.message.publish_kafka_variable_element_message_job.ok";
    public static final String RESOURCE_INTERNAL_PUBLICATION_OK              = "notice_message.srm.action.internal_publication.ok";
    public static final String RESOURCE_EXTERNAL_PUBLICATION_OK              = "notice_message.srm.action.external_publication.ok";

    // Errors
    public static final String STREAM_MESSAGE_SEND_ERROR                     = "notice_message.srm.action.stream_messaging.send.error";
    public static final String STREAM_MESSAGE_SEND_VARIABLE_ELEMENT_ERROR    = "notice_message.srm.action.stream_messaging.send_variable_elements.error";
    public static final String STREAM_MESSAGE_SEND_DSD_ERROR                 = "notice_message.srm.action.stream_messaging.send_dsds.error";

}