package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.ExportCodesRestrictionAction;
import org.siemac.metamac.srm.web.shared.code.ExportCodesRestrictionResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportCodesRestrictionActionHandler extends SecurityActionHandler<ExportCodesRestrictionAction, ExportCodesRestrictionResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportCodesRestrictionActionHandler() {
        super(ExportCodesRestrictionAction.class);
    }

    @Override
    public ExportCodesRestrictionResult executeSecurityAction(ExportCodesRestrictionAction action) throws ActionException {
        try {
            String fileName = srmCoreServiceFacade.exportCodeRestrictionsTsv(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn());
            return new ExportCodesRestrictionResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
