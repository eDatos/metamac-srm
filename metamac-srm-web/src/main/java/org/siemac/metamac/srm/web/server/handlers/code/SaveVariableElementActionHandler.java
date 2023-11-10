package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.serviceimpl.result.ResourceVersionPublicationResult;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.srm.web.server.utils.StreamUtils;
import org.siemac.metamac.srm.web.shared.code.SaveVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableElementResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveVariableElementActionHandler extends SecurityActionHandler<SaveVariableElementAction, SaveVariableElementResult> {

    @Autowired
    private SrmCoreServiceFacade      srmCoreServiceFacade;

    @Autowired
    private NoticesRestInternalFacade noticesRestInternalFacade;

    public SaveVariableElementActionHandler() {
        super(SaveVariableElementAction.class);
    }

    @Override
    public SaveVariableElementResult executeSecurityAction(SaveVariableElementAction action) throws ActionException {
        try {
            VariableElementDto variableElementToSave = action.getVariableElementDto();
            ResourceVersionPublicationResult<VariableElementDto> variableElementResult = null;
            List<MetamacExceptionItem> exceptionItems = new ArrayList<>();
            List<VariableElementDto> variableElementsDto = new ArrayList<VariableElementDto>();

            if (variableElementToSave.getId() == null) {
                // Create
                variableElementResult = srmCoreServiceFacade.createVariableElement(ServiceContextHolder.getCurrentServiceContext(), variableElementToSave);
            } else {
                // Update
                variableElementResult = srmCoreServiceFacade.updateVariableElement(ServiceContextHolder.getCurrentServiceContext(), variableElementToSave);
            }

            StreamUtils.getResendKafkaVariableElementMessageErrors(variableElementsDto, variableElementResult.getResourceVersion(), variableElementResult, exceptionItems);

            MetamacWebException webExceptions = StreamUtils.retrieveMultipleKafkaSendingErrors(ServiceContextHolder.getCurrentServiceContext(), noticesRestInternalFacade, exceptionItems,
                    variableElementsDto);

            return new SaveVariableElementResult(variableElementResult.getResourceVersion(), webExceptions);

        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
