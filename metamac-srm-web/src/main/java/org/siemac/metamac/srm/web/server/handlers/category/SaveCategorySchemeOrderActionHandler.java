package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeOrderAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeOrderResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveCategorySchemeOrderActionHandler extends SecurityActionHandler<SaveCategorySchemeOrderAction, SaveCategorySchemeOrderResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveCategorySchemeOrderActionHandler() {
        super(SaveCategorySchemeOrderAction.class);
    }

    @Override
    public SaveCategorySchemeOrderResult executeSecurityAction(SaveCategorySchemeOrderAction action) throws ActionException {
        try {
            CategorySchemeVisualisationDto orderToSave = action.getCategorySchemeOrderVisualisationDto();
            CategorySchemeVisualisationDto orderSaved = null;

            if (orderToSave.getId() == null) {
                // Create
                RelatedResourceDto categoryScheme = RelatedResourceUtils.createRelatedResourceDto(RelatedResourceTypeEnum.CATEGORY_SCHEME, action.getCategorySchemeUrn());
                orderToSave.setCategoryScheme(categoryScheme);
                orderSaved = srmCoreServiceFacade.createCategorySchemeOrderVisualisation(ServiceContextHolder.getCurrentServiceContext(), orderToSave);
            } else {
                // Update
                orderSaved = srmCoreServiceFacade.updateCategorySchemeOrderVisualisation(ServiceContextHolder.getCurrentServiceContext(), orderToSave);
            }
            return new SaveCategorySchemeOrderResult(orderSaved);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
