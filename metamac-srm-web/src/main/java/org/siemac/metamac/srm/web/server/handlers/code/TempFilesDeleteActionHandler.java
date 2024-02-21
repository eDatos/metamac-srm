package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.TempFilesDeleteAction;
import org.siemac.metamac.srm.web.shared.code.TempFilesDeleteResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class TempFilesDeleteActionHandler extends SecurityActionHandler<TempFilesDeleteAction, TempFilesDeleteResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public TempFilesDeleteActionHandler() {
        super(TempFilesDeleteAction.class);
    }
    @Override
    public TempFilesDeleteResult executeSecurityAction(TempFilesDeleteAction action) throws ActionException {
        try {
            srmCoreServiceFacade.deleteTemporalFile(ServiceContextHolder.getCurrentServiceContext(), action.getFileName());
            return new TempFilesDeleteResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
