package org.siemac.metamac.srm.core.conf;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.constants.SrmConfigurationConstants;

import com.arte.statistic.sdmx.srm.core.conf.SdmxSrmConfigurationImpl;

public class SrmConfigurationImpl extends SdmxSrmConfigurationImpl implements SrmConfiguration {

    @Override
    public String retrieveHelpUrl() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.HELP_URL);
    }

    @Override
    public String retrievePrimaryMeasureConceptIdUrnDefault() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DSD_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN);
    }

    @Override
    public String retrieveSrmVisualizerUrl() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.SRM_VISUALIZER_URL);
    }

    @Override
    public String retrieveCodelistUrnForVariableElementGeographicalGranularity() throws MetamacException {
        return retrieveDefaultCodelistGeographicalGranularityUrn();
    }

    @Override
    public String retrieveVariableWorldUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.VARIABLE_WORLD);
    }

    @Override
    public String retrieveVariableElementWorldUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.VARIABLE_ELEMENT_WORLD);
    }

    @Override
    public String findJobDeleteDeprecatedEntitiesCronExpression() throws MetamacException {
        return findProperty(SrmConfigurationConstants.JOB_DELETE_DEPRECATED_ENTITIES_CRON_EXPRESSION);
    }

    @Override
    public String retrieveDsdPrimaryMeasureDefaultConceptIdUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DSD_PRIMARY_MEASURE_DEFAULT_CONCEPT_ID_URN);
    }

    @Override
    public String retrieveDsdMeasureDimensionOrAttributeDefaultConceptIdUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DSD_MEASURE_DIMENSION_OR_ATTRIBUTE_DEFAULT_CONCEPT_ID_URN);
    }

    @Override
    public String retrieveDsdTimeDimensionOrAttributeDefaultConceptIdUrn() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.DSD_TIME_DIMENSION_OR_ATTRIBUTE_DEFAULT_CONCEPT_ID_URN);
    }

    @Override
    public String retrieveCronExpressionForResendPublishedResourcesKafkaMessage() throws MetamacException {
        return retrieveProperty(SrmConfigurationConstants.CRON_EXPRESSION__RESEND_RESOURCES_KAFKA_MESSAGE);
    }
}
