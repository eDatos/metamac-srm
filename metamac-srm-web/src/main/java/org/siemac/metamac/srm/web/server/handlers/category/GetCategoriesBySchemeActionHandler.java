package org.siemac.metamac.srm.web.server.handlers.category;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.shared.CategoryMetamacVisualisationResult;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesBySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesBySchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCategoriesBySchemeActionHandler extends SecurityActionHandler<GetCategoriesBySchemeAction, GetCategoriesBySchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCategoriesBySchemeActionHandler() {
        super(GetCategoriesBySchemeAction.class);
    }

    @Override
    public GetCategoriesBySchemeResult executeSecurityAction(GetCategoriesBySchemeAction action) throws ActionException {
        try {
            List<CategoryMetamacVisualisationResult> categories = srmCoreServiceFacade.retrieveCategoriesByCategorySchemeUrn(ServiceContextHolder.getCurrentServiceContext(), action.getSchemeUrn(),
                    action.getLocale(), action.getCategorySchemeOrderUrn());

            CategorySchemeVisualisationDto categorySchemeOrderVisualisationDto = null;
            if (action.getCategorySchemeOrderUrn() != null) {
                categorySchemeOrderVisualisationDto = srmCoreServiceFacade.retrieveCategorySchemeOrderVisualisationByUrn(ServiceContextHolder.getCurrentServiceContext(),
                        action.getCategorySchemeOrderUrn());
            }

            return new GetCategoriesBySchemeResult(categories, categorySchemeOrderVisualisationDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
