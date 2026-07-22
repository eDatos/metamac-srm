package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorySchemeOrdersAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorySchemeOrdersResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteCategorySchemeOrdersActionHandler extends SecurityActionHandler<DeleteCategorySchemeOrdersAction, DeleteCategorySchemeOrdersResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteCategorySchemeOrdersActionHandler() {
        super(DeleteCategorySchemeOrdersAction.class);
    }

    @Override
    public DeleteCategorySchemeOrdersResult executeSecurityAction(DeleteCategorySchemeOrdersAction action) throws ActionException {
        try {
            for (String orderUrn : action.getOrderUrns()) {
                srmCoreServiceFacade.deleteCategorySchemeOrderVisualisation(ServiceContextHolder.getCurrentServiceContext(), orderUrn);
            }
            return new DeleteCategorySchemeOrdersResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
