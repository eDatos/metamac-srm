package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.web.shared.GetHelpUrlAction;
import org.siemac.metamac.srm.web.shared.GetHelpUrlResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetHelpUrlActionHandler extends SecurityActionHandler<GetHelpUrlAction, GetHelpUrlResult> {

	private static final Logger log = LoggerFactory.getLogger(GetHelpUrlActionHandler.class);

	@Autowired
	private SrmConfiguration configurationService = null;

	public GetHelpUrlActionHandler() {
		super(GetHelpUrlAction.class);
	}

	@Override
	public GetHelpUrlResult executeSecurityAction(GetHelpUrlAction action) throws ActionException {
		try {
			String userGuideFileName = configurationService.retrieveHelpUrl();
			return new GetHelpUrlResult(userGuideFileName);
		} catch (MetamacException e) {
			log.debug("Error retrieving application notices OPTIONAL help url", e);
			return new GetHelpUrlResult("");
		}
	}
}
