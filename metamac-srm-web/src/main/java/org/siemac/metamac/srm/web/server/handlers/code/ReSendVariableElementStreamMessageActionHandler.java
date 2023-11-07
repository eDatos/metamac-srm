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
        MetamacWebException variableElementStreamMessageException = null;
        try {
            result = srmCoreServiceFacade.resendUpdatedVariableElement(serviceContext, action.getVariableElementUrn());
            variableElementDto = srmCoreServiceFacade.retrieveVariableElementByUrn(serviceContext, action.getVariableElementUrn());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }

        if (!result.isOk()) {
            MetamacException e = result.getMainException();
            List<MetamacExceptionItem> list = new ArrayList<>();
            for (MetamacException exception : result.getSecondaryExceptions()) {
                list.addAll(exception.getExceptionItems());
            }
            e.getExceptionItems().addAll(list);
            variableElementStreamMessageException = WebExceptionUtils.createMetamacWebException(e);
            try {
                noticesRestInternalFacade.createStreamMessageErrorNotification(serviceContext, variableElementDto, TypeExternalArtefactsEnum.VARIABLE_ELEMENT);
            } catch (MetamacWebException noticeException) {
                variableElementStreamMessageException.getWebExceptionItems().addAll(noticeException.getWebExceptionItems());
            }
        }

        return new ReSendVariableElementStreamMessageResult(variableElementDto, variableElementStreamMessageException);
    }
}