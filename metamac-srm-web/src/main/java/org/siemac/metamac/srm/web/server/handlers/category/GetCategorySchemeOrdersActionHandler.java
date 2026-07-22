package org.siemac.metamac.srm.web.server.handlers.category;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeOrdersAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeOrdersResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCategorySchemeOrdersActionHandler extends SecurityActionHandler<GetCategorySchemeOrdersAction, GetCategorySchemeOrdersResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCategorySchemeOrdersActionHandler() {
        super(GetCategorySchemeOrdersAction.class);
    }

    @Override
    public GetCategorySchemeOrdersResult executeSecurityAction(GetCategorySchemeOrdersAction action) throws ActionException {
        try {
            List<CategorySchemeVisualisationDto> orders = srmCoreServiceFacade.retrieveCategorySchemeOrderVisualisationsByCategoryScheme(ServiceContextHolder.getCurrentServiceContext(),
                    action.getCategorySchemeUrn());
            return new GetCategorySchemeOrdersResult(orders);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
