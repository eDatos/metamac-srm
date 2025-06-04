package org.siemac.metamac.srm.web.external;

import javax.servlet.ServletContextEvent;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.listener.ApplicationStartupListener;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.core.common.util.WebUtils;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.constants.SrmConfigurationConstants;
import org.siemac.metamac.srm.rest.external.utils.SrmWebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationStartup extends ApplicationStartupListener {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStartup.class);
    private SrmConfiguration srmConfiguration;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);
        try {
            srmConfiguration = ApplicationContextProvider.getApplicationContext().getBean(SrmConfiguration.class);
            WebUtils.setAppsBaseUrl(configurationService.retrieveAppsExternalWebApplicationUrlBase());
            WebUtils.setApiBaseURL(configurationService.retrieveSrmExternalApiUrlBase());

            WebUtils.setApiStyleHeaderUrl(configurationService.retrieveApiStyleHeaderUrl());
            WebUtils.setApiStyleFooterUrl(configurationService.retrieveApiStyleFooterUrl());
            SrmWebUtils.setSrmVisualizerUrl(srmConfiguration.retrieveSrmVisualizerUrl());
        } catch (MetamacException e) {
            log.error("Error retrieving application configuration", e);
        }
    }

    @Override
    public String projectName() {
        return "structural-resources";
    }

    @Override
    public void checkApplicationProperties() throws MetamacException {
        // Datasource
        checkRequiredProperty(SrmConfigurationConstants.DB_DRIVER_NAME);
        checkRequiredProperty(SrmConfigurationConstants.DB_URL);
        checkRequiredProperty(SrmConfigurationConstants.DB_USERNAME);
        checkRequiredProperty(SrmConfigurationConstants.DB_PASSWORD);

        // Api
        checkRequiredProperty(SrmConfigurationConstants.ENDPOINT_SRM_EXTERNAL_API);
        checkRequiredProperty(SrmConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_EXTERNAL_API);
        checkRequiredProperty(SrmConfigurationConstants.METAMAC_ORGANISATION_URN);
    }
}
