package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.GetCategoryElementLogoAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoryElementLogoResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCategoryElementLogoActionHandler extends SecurityActionHandler<GetCategoryElementLogoAction, GetCategoryElementLogoResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCategoryElementLogoActionHandler() {
        super(GetCategoryElementLogoAction.class);
    }

    @Override
    public GetCategoryElementLogoResult executeSecurityAction(GetCategoryElementLogoAction action) throws ActionException {
        try {
            String fileName = srmCoreServiceFacade.retrieveCategoryElementLogo(ServiceContextHolder.getCurrentServiceContext(), action.getCategoryElementUrn());
            return new GetCategoryElementLogoResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
