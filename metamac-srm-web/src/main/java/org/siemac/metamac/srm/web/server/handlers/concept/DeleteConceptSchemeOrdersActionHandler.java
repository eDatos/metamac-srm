package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemeOrdersAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemeOrdersResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteConceptSchemeOrdersActionHandler extends SecurityActionHandler<DeleteConceptSchemeOrdersAction, DeleteConceptSchemeOrdersResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteConceptSchemeOrdersActionHandler() {
        super(DeleteConceptSchemeOrdersAction.class);
    }

    @Override
    public DeleteConceptSchemeOrdersResult executeSecurityAction(DeleteConceptSchemeOrdersAction action) throws ActionException {
        try {
            for (String orderUrn : action.getOrderUrns()) {
                srmCoreServiceFacade.deleteConceptSchemeOrderVisualisation(ServiceContextHolder.getCurrentServiceContext(), orderUrn);
            }
            return new DeleteConceptSchemeOrdersResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
