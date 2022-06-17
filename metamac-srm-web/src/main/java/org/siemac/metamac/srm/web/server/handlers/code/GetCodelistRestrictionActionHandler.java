package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetCodelistRestrictionAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistRestrictionResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCodelistRestrictionActionHandler extends SecurityActionHandler<GetCodelistRestrictionAction, GetCodelistRestrictionResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCodelistRestrictionActionHandler() {
        super(GetCodelistRestrictionAction.class);
    }

    @Override
    public GetCodelistRestrictionResult executeSecurityAction(GetCodelistRestrictionAction action) throws ActionException {
        try {
            List<CodelistVisualisationDto> restrictions = srmCoreServiceFacade.retrieveCodelistOpennessVisualisationsByCodelist(ServiceContextHolder.getCurrentServiceContext(),
                    action.getCodelistUrn());
            return new GetCodelistRestrictionResult(restrictions);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
