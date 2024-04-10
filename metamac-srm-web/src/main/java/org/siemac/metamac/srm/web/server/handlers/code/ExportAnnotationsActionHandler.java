package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.ExportAnnotationsAction;
import org.siemac.metamac.srm.web.shared.code.ExportAnnotationsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportAnnotationsActionHandler extends SecurityActionHandler<ExportAnnotationsAction, ExportAnnotationsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportAnnotationsActionHandler(Class<ExportAnnotationsAction> actionType) {
        super(ExportAnnotationsAction.class);
    }


    @Override
    public ExportAnnotationsResult executeSecurityAction(ExportAnnotationsAction action) throws ActionException {
        try {
            String fileName = srmCoreServiceFacade.exportAnnotationsTsv(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn());
            return new ExportAnnotationsResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
