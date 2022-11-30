package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.UpdateAttributeInOrderAction;
import org.siemac.metamac.srm.web.shared.dsd.UpdateAttributeInOrderResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateAttributeInOrderActionHandler extends SecurityActionHandler<UpdateAttributeInOrderAction, UpdateAttributeInOrderResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateAttributeInOrderActionHandler() {
        super(UpdateAttributeInOrderAction.class);
    }

    @Override
    public UpdateAttributeInOrderResult executeSecurityAction(UpdateAttributeInOrderAction action) throws ActionException {
        try {
            srmCoreServiceFacade.updateAttributeInOrder(ServiceContextHolder.getCurrentServiceContext(), action.getAttributeUrn(), action.getAttributeSchemeUrn(), action.getNewAttributeIndex());
            return new UpdateAttributeInOrderResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
