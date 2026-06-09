package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.UpdateCategoryInOrderAction;
import org.siemac.metamac.srm.web.shared.category.UpdateCategoryInOrderResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateCategoryInOrderActionHandler extends SecurityActionHandler<UpdateCategoryInOrderAction, UpdateCategoryInOrderResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateCategoryInOrderActionHandler() {
        super(UpdateCategoryInOrderAction.class);
    }

    @Override
    public UpdateCategoryInOrderResult executeSecurityAction(UpdateCategoryInOrderAction action) throws ActionException {
        try {
            srmCoreServiceFacade.updateCategoryInOrderVisualisation(ServiceContextHolder.getCurrentServiceContext(), action.getCategoryUrn(), action.getCategorySchemeUrn(),
                    action.getNewCategoryIndex());
            return new UpdateCategoryInOrderResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
