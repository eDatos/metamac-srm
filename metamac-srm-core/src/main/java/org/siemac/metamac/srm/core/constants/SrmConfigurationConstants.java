package org.siemac.metamac.srm.core.constants;

import org.siemac.metamac.core.common.constants.shared.ConfigurationConstants;

public class SrmConfigurationConstants extends ConfigurationConstants {

    // PROPERTIES SPECIFIED IN THE DATA DIRECTORY

    // Configuration

    public static final String JOB_DELETE_DEPRECATED_ENTITIES_CRON_EXPRESSION  = "metamac.srm.jobs.delete_deprecated_entities.cron_expression";

    public static final String HELP_URL                                        = "metamac.srm.help.url";

    // DataSource
    public static final String DB_URL                                          = "metamac.srm.db.url";
    public static final String DB_USERNAME                                     = "metamac.srm.db.username";
    public static final String DB_PASSWORD                                     = "metamac.srm.db.password";
    public static final String DB_DRIVER_NAME                                  = "metamac.srm.db.driver_name";
    public static final String DB_DRIVER_NAME_POSTGRESQL                       = "org.postgresql.Driver";

    // Misc
    public static final String VARIABLE_WORLD                                  = "metamac.srm.variables.variable_world.urn";
    public static final String VARIABLE_ELEMENT_WORLD                          = "metamac.srm.variables.variable_element_world.urn";
    public static final String CRON_EXPRESSION__RESEND_RESOURCES_KAFKA_MESSAGE = "metamac.srm.resend_resources_kafka_message.cron_expression";

    // srm-visualizer
    public static final String SRM_VISUALIZER_URL                              = "metamac.srm.srm-visualizer.url";
}
