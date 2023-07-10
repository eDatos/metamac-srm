package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.ExportCategoryElementsAction;
import org.siemac.metamac.srm.web.shared.category.ExportCategoryElementsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportCategoryElementsActionHandler extends SecurityActionHandler<ExportCategoryElementsAction, ExportCategoryElementsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportCategoryElementsActionHandler() {
        super(ExportCategoryElementsAction.class);
    }

    @Override
    public ExportCategoryElementsResult executeSecurityAction(ExportCategoryElementsAction action) throws ActionException {
        try {
            String fileName = srmCoreServiceFacade.exportCategoryElementsTsv(ServiceContextHolder.getCurrentServiceContext(), action.getUrns());
            return new ExportCategoryElementsResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
