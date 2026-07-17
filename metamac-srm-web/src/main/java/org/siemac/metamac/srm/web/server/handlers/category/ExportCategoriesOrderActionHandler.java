package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.ExportCategoriesOrderAction;
import org.siemac.metamac.srm.web.shared.category.ExportCategoriesOrderResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportCategoriesOrderActionHandler extends SecurityActionHandler<ExportCategoriesOrderAction, ExportCategoriesOrderResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportCategoriesOrderActionHandler() {
        super(ExportCategoriesOrderAction.class);
    }

    @Override
    public ExportCategoriesOrderResult executeSecurityAction(ExportCategoriesOrderAction action) throws ActionException {
        try {
            String fileName = srmCoreServiceFacade.exportCategoryOrdersTsv(ServiceContextHolder.getCurrentServiceContext(), action.getCategorySchemeUrn());
            return new ExportCategoriesOrderResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
