package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistRestrictionAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistRestrictionResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteCodelistRestrictionActionHandler extends SecurityActionHandler<DeleteCodelistRestrictionAction, DeleteCodelistRestrictionResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteCodelistRestrictionActionHandler() {
        super(DeleteCodelistRestrictionAction.class);
    }

    @Override
    public DeleteCodelistRestrictionResult executeSecurityAction(DeleteCodelistRestrictionAction action) throws ActionException {
        try {
            for (String restrictionUrn : action.getRestrictionUrns()) {
                srmCoreServiceFacade.deleteCodelistRestrictionVisualisation(ServiceContextHolder.getCurrentServiceContext(), restrictionUrn);
            }
            return new DeleteCodelistRestrictionResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
