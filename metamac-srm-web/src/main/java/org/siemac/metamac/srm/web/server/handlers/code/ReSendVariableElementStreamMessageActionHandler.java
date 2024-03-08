package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.srm.web.server.utils.StreamUtils;
import org.siemac.metamac.srm.web.shared.code.ReSendVariableElementStreamMessageAction;
import org.siemac.metamac.srm.web.shared.code.ReSendVariableElementStreamMessageResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ReSendVariableElementStreamMessageActionHandler extends SecurityActionHandler<ReSendVariableElementStreamMessageAction, ReSendVariableElementStreamMessageResult> {

    @Autowired
    private SrmCoreServiceFacade      srmCoreServiceFacade;

    @Autowired
    private NoticesRestInternalFacade noticesRestInternalFacade;

    public ReSendVariableElementStreamMessageActionHandler() {
        super(ReSendVariableElementStreamMessageAction.class);
    }

    @Override
    public ReSendVariableElementStreamMessageResult executeSecurityAction(ReSendVariableElementStreamMessageAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();

        SendStreamMessageResult result;
        VariableElementDto variableElementDto;
        List<MetamacExceptionItem> exceptionItems = new ArrayList<>();
        List<VariableElementDto> variableElementsDto = new ArrayList<VariableElementDto>();

        try {
            result = srmCoreServiceFacade.resendUpdatedVariableElement(serviceContext, action.getVariableElementUrn());
            variableElementDto = srmCoreServiceFacade.retrieveVariableElementByUrn(serviceContext, action.getVariableElementUrn());
            StreamUtils.getResendKafkaVariableElementMessageErrors(variableElementsDto, variableElementDto, result, exceptionItems);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }

        MetamacWebException webExceptions = StreamUtils.retrieveMultipleKafkaSendingErrors(serviceContext, noticesRestInternalFacade, exceptionItems, variableElementsDto);

        return new ReSendVariableElementStreamMessageResult(variableElementDto, webExceptions);
    }
}