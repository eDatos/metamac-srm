package org.siemac.metamac.srm.web.server.handlers.category;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.shared.CodeCategoryElementNormalisationResult;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.NormaliseCategoryElementsToCategoriesAction;
import org.siemac.metamac.srm.web.shared.category.NormaliseCategoryElementsToCategoriesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class NormaliseCategoryElementsToCategoriesActionHandler extends SecurityActionHandler<NormaliseCategoryElementsToCategoriesAction, NormaliseCategoryElementsToCategoriesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public NormaliseCategoryElementsToCategoriesActionHandler() {
        super(NormaliseCategoryElementsToCategoriesAction.class);
    }

    @Override
    public NormaliseCategoryElementsToCategoriesResult executeSecurityAction(NormaliseCategoryElementsToCategoriesAction action) throws ActionException {
        try {
            List<CodeCategoryElementNormalisationResult> result = srmCoreServiceFacade.normaliseCategoryElementsToCategories(ServiceContextHolder.getCurrentServiceContext(),
                    action.getCategorySchemeUrn(), action.getLocale(), action.getOnlyNormaliseCategoriesWithoutCategoryElement());
            return new NormaliseCategoryElementsToCategoriesResult(result);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
