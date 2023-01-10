package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.ExportCodesOpennessAction;
import org.siemac.metamac.srm.web.shared.code.ExportCodesOpennessResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportCodesOpennessActionHandler extends SecurityActionHandler<ExportCodesOpennessAction, ExportCodesOpennessResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportCodesOpennessActionHandler() {
        super(ExportCodesOpennessAction.class);
    }

    @Override
    public ExportCodesOpennessResult executeSecurityAction(ExportCodesOpennessAction action) throws ActionException {
        try {
            String fileName = srmCoreServiceFacade.exportCodeOpennessLevelsTsv(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn());
            return new ExportCodesOpennessResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
