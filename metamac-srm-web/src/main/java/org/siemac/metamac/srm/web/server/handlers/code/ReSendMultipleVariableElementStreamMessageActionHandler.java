package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
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

        MetamacWebException variableElementStreamMessageException = null;
        try {

            for (String variableElementUrn : action.getUrns()) {
                VariableElementDto variableElementDto;
                SendStreamMessageResult result;
                result = srmCoreServiceFacade.resendUpdatedVariableElement(serviceContext, variableElementUrn);
                variableElementDto = srmCoreServiceFacade.retrieveVariableElementByUrn(serviceContext, variableElementUrn);
                getResendKafkaVariableElementMessageErrors(serviceContext, variableElementDto, result, variableElementStreamMessageException);
            }

        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }

        return new ReSendMultipleVariableElementsStreamMessageResult(variableElementStreamMessageException);
    }

    private void getResendKafkaVariableElementMessageErrors(ServiceContext serviceContext, VariableElementDto variableElementDto, SendStreamMessageResult result,
            MetamacWebException variableElementStreamMessageException) {
        if (!result.isOk()) {
            MetamacException e = result.getMainException();
            List<MetamacExceptionItem> list = new ArrayList<>();
            for (MetamacException exception : result.getSecondaryExceptions()) {
                list.addAll(exception.getExceptionItems());
            }
            e.getExceptionItems().addAll(list);

            if (variableElementStreamMessageException == null) {
                variableElementStreamMessageException = WebExceptionUtils.createMetamacWebException(e);
            }

            try {
                noticesRestInternalFacade.createStreamMessageErrorNotification(serviceContext, variableElementDto, TypeExternalArtefactsEnum.VARIABLE_ELEMENT);
            } catch (MetamacWebException noticeException) {
                variableElementStreamMessageException.getWebExceptionItems().addAll(noticeException.getWebExceptionItems());
            }
        }
    }
}