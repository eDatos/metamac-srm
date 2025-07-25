package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeOrderAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeOrderResult;
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
public class SaveConceptSchemeOrderActionHandler extends SecurityActionHandler<SaveConceptSchemeOrderAction, SaveConceptSchemeOrderResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveConceptSchemeOrderActionHandler() {
        super(SaveConceptSchemeOrderAction.class);
    }

    @Override
    public SaveConceptSchemeOrderResult executeSecurityAction(SaveConceptSchemeOrderAction action) throws ActionException {
        try {
            ConceptSchemeVisualisationDto orderToSave = action.getConceptSchemeOrderVisualisationDto();
            ConceptSchemeVisualisationDto orderSaved = null;

            if (orderToSave.getId() == null) {
                // Create
                RelatedResourceDto conceptScheme = RelatedResourceUtils.createRelatedResourceDto(RelatedResourceTypeEnum.CONCEPT_SCHEME, action.getConceptSchemeUrn());
                orderToSave.setConceptScheme(conceptScheme);
                orderSaved = srmCoreServiceFacade.createConceptSchemeOrderVisualisation(ServiceContextHolder.getCurrentServiceContext(), orderToSave);
            } else {
                // Update
                orderSaved = srmCoreServiceFacade.updateConceptSchemeOrderVisualisation(ServiceContextHolder.getCurrentServiceContext(), orderToSave);
            }
            return new SaveConceptSchemeOrderResult(orderSaved);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
