package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.UpdateCodesCategoryElementsAction;
import org.siemac.metamac.srm.web.shared.category.UpdateCodesCategoryElementsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateCodesCategoryElementsActionHandler extends SecurityActionHandler<UpdateCodesCategoryElementsAction, UpdateCodesCategoryElementsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateCodesCategoryElementsActionHandler() {
        super(UpdateCodesCategoryElementsAction.class);
    }

    @Override
    public UpdateCodesCategoryElementsResult executeSecurityAction(UpdateCodesCategoryElementsAction action) throws ActionException {
        try {
            srmCoreServiceFacade.updateCodesCategoryElements(ServiceContextHolder.getCurrentServiceContext(), action.getCategorySchemetUrn(), action.getCategoryElementsIdByCodeId());
            return new UpdateCodesCategoryElementsResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
