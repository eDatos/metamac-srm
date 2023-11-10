package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.domain.shared.VariableElementVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.srm.web.server.utils.StreamUtils;
import org.siemac.metamac.srm.web.shared.code.ReSendMultipleVariableElementsStreamMessageAction;
import org.siemac.metamac.srm.web.shared.code.ReSendMultipleVariableElementsStreamMessageResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ReSendMultipleVariableElementStreamMessageActionHandler
        extends
            SecurityActionHandler<ReSendMultipleVariableElementsStreamMessageAction, ReSendMultipleVariableElementsStreamMessageResult> {

    @Autowired
    private SrmCoreServiceFacade      srmCoreServiceFacade;

    @Autowired
    private NoticesRestInternalFacade noticesRestInternalFacade;

    public ReSendMultipleVariableElementStreamMessageActionHandler() {
        super(ReSendMultipleVariableElementsStreamMessageAction.class);
    }

    @Override
    public ReSendMultipleVariableElementsStreamMessageResult executeSecurityAction(ReSendMultipleVariableElementsStreamMessageAction action) throws ActionException {
        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();
        List<MetamacExceptionItem> exceptionItems = new ArrayList<>();
        List<VariableElementDto> variableElementsDto = new ArrayList<VariableElementDto>();
        List<String> variableElementsUrn = retrieveVariableElementsUrnToSend(serviceContext, action);

        for (String variableElementUrn : variableElementsUrn) {
            sendVariableElementToKafka(serviceContext, variableElementUrn, variableElementsDto, exceptionItems);
        }

        MetamacWebException webExceptions = StreamUtils.retrieveMultipleKafkaSendingErrors(serviceContext, noticesRestInternalFacade, exceptionItems, variableElementsDto);

        return new ReSendMultipleVariableElementsStreamMessageResult(webExceptions);

    }

    private void sendVariableElementToKafka(ServiceContext serviceContext, String variableElementUrn, List<VariableElementDto> variableElementsDto, List<MetamacExceptionItem> exceptionItems)
            throws MetamacWebException {
        try {
            VariableElementDto variableElementDto;
            SendStreamMessageResult result;
            result = srmCoreServiceFacade.resendUpdatedVariableElement(serviceContext, variableElementUrn);
            variableElementDto = srmCoreServiceFacade.retrieveVariableElementByUrn(serviceContext, variableElementUrn);
            StreamUtils.getResendKafkaVariableElementMessageErrors(variableElementsDto, variableElementDto, result, exceptionItems);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private List<String> retrieveVariableElementsUrnToSend(ServiceContext serviceContext, ReSendMultipleVariableElementsStreamMessageAction action) throws MetamacWebException {
        try {
            if (action.getUrns() != null && !action.getUrns().isEmpty()) {
                return action.getUrns();
            }
            List<String> variableElementsUrn = new ArrayList<String>();
            if (action.getVariable() != null && action.getLocale() != null) {

                List<VariableElementVisualisationResult> variableElementsByVariable = srmCoreServiceFacade.retrieveVariableElementsByVariable(serviceContext, action.getVariable(), action.getLocale());
                for (VariableElementVisualisationResult variableElement : variableElementsByVariable) {
                    variableElementsUrn.add(variableElement.getUrn());
                }
            }
            return variableElementsUrn;

        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}