package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.ExportConceptsOrderAction;
import org.siemac.metamac.srm.web.shared.concept.ExportConceptsOrderResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportConceptsOrderActionHandler extends SecurityActionHandler<ExportConceptsOrderAction, ExportConceptsOrderResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportConceptsOrderActionHandler() {
        super(ExportConceptsOrderAction.class);
    }

    @Override
    public ExportConceptsOrderResult executeSecurityAction(ExportConceptsOrderAction action) throws ActionException {
        try {
            String fileName = srmCoreServiceFacade.exportConceptOrdersTsv(ServiceContextHolder.getCurrentServiceContext(), action.getConceptSchemeUrn());
            return new ExportConceptsOrderResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
