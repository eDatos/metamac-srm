package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeOrdersAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeOrdersResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptSchemeOrdersActionHandler extends SecurityActionHandler<GetConceptSchemeOrdersAction, GetConceptSchemeOrdersResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetConceptSchemeOrdersActionHandler() {
        super(GetConceptSchemeOrdersAction.class);
    }

    @Override
    public GetConceptSchemeOrdersResult executeSecurityAction(GetConceptSchemeOrdersAction action) throws ActionException {
        try {
            List<ConceptSchemeVisualisationDto> orders = srmCoreServiceFacade.retrieveConceptSchemeOrderVisualisationsByConceptScheme(ServiceContextHolder.getCurrentServiceContext(),
                    action.getConceptSchemeUrn());
            return new GetConceptSchemeOrdersResult(orders);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
