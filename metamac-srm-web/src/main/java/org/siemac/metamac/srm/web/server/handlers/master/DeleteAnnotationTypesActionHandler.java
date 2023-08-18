package org.siemac.metamac.srm.web.server.handlers.master;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.master.DeleteAnnotationTypesAction;
import org.siemac.metamac.srm.web.shared.master.DeleteAnnotationTypesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteAnnotationTypesActionHandler extends SecurityActionHandler<DeleteAnnotationTypesAction, DeleteAnnotationTypesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteAnnotationTypesActionHandler() {
        super(DeleteAnnotationTypesAction.class);
    }

    @Override
    public DeleteAnnotationTypesResult executeSecurityAction(DeleteAnnotationTypesAction action) throws ActionException {
        try {

            for (String uuid : action.getUuids()) {
                srmCoreServiceFacade.deleteAnnotationType(ServiceContextHolder.getCurrentServiceContext(), uuid);
            }
            return new DeleteAnnotationTypesResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }

    }

}
