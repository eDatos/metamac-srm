package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.UpdateCategoryParentAction;
import org.siemac.metamac.srm.web.shared.category.UpdateCategoryParentResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateCategoryParentActionHandler extends SecurityActionHandler<UpdateCategoryParentAction, UpdateCategoryParentResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateCategoryParentActionHandler() {
        super(UpdateCategoryParentAction.class);
    }

    @Override
    public UpdateCategoryParentResult executeSecurityAction(UpdateCategoryParentAction action) throws ActionException {
        try {
            srmCoreServiceFacade.updateCategoryParent(ServiceContextHolder.getCurrentServiceContext(), action.getCategoryUrn(), action.getNewCategoryParentUrn());
            return new UpdateCategoryParentResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
