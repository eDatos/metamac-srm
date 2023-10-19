package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.UpdateAttributesInOrderAction;
import org.siemac.metamac.srm.web.shared.dsd.UpdateAttributesInOrderResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateAttributesInOrderActionHandler extends SecurityActionHandler<UpdateAttributesInOrderAction, UpdateAttributesInOrderResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateAttributesInOrderActionHandler() {
        super(UpdateAttributesInOrderAction.class);
    }

    @Override
    public UpdateAttributesInOrderResult executeSecurityAction(UpdateAttributesInOrderAction action) throws ActionException {
        try {
            srmCoreServiceFacade.updateAttributesInOrder(ServiceContextHolder.getCurrentServiceContext(), action.getAttributesId(), action.getDsdUrn());
            return new UpdateAttributesInOrderResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
