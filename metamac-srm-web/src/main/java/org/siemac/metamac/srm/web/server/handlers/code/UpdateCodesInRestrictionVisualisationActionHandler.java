package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.UpdateCodesInRestrictionVisualisationAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodesInRestrictionVisualisationResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateCodesInRestrictionVisualisationActionHandler extends SecurityActionHandler<UpdateCodesInRestrictionVisualisationAction, UpdateCodesInRestrictionVisualisationResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateCodesInRestrictionVisualisationActionHandler() {
        super(UpdateCodesInRestrictionVisualisationAction.class);
    }

    @Override
    public UpdateCodesInRestrictionVisualisationResult executeSecurityAction(UpdateCodesInRestrictionVisualisationAction action) throws ActionException {
        try {
            srmCoreServiceFacade.updateCodesInRestrictionVisualisation(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistRestrictionVisualisationUrn(), action.getRestrictions());
            CodelistVisualisationDto codelistVisualisationDto = srmCoreServiceFacade.retrieveCodelistRestrictionVisualisationByUrn(ServiceContextHolder.getCurrentServiceContext(),
                    action.getCodelistRestrictionVisualisationUrn());
            return new UpdateCodesInRestrictionVisualisationResult(codelistVisualisationDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
