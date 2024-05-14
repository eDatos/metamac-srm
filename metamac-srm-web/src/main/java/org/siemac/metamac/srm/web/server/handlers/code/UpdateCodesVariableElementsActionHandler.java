package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.srm.web.shared.code.UpdateCodesVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodesVariableElementsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateCodesVariableElementsActionHandler extends SecurityActionHandler<UpdateCodesVariableElementsAction, UpdateCodesVariableElementsResult> {

    @Autowired
    private SrmCoreServiceFacade      srmCoreServiceFacade;

    @Autowired
    private NoticesRestInternalFacade noticesRestInternalFacade;

    public UpdateCodesVariableElementsActionHandler() {
        super(UpdateCodesVariableElementsAction.class);
    }

    @Override
    public UpdateCodesVariableElementsResult executeSecurityAction(UpdateCodesVariableElementsAction action) throws ActionException {
        try {

            SendStreamMessageResult codelistVersionResult = null;
            CodelistMetamacDto codelistMetamacDto;
            MetamacWebException codelistStreamMessageException = null;

            codelistVersionResult = srmCoreServiceFacade.updateCodesVariableElements(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn(), action.getVariableElementsIdByCodeId());
            codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn());
            if (codelistVersionResult != null && !codelistVersionResult.isOk()) {
                MetamacException e = codelistVersionResult.getMainException();
                List<MetamacExceptionItem> list = new ArrayList<>();
                for (MetamacException exception : codelistVersionResult.getSecondaryExceptions()) {
                    list.addAll(exception.getExceptionItems());
                }
                e.getExceptionItems().addAll(list);
                codelistStreamMessageException = WebExceptionUtils.createMetamacWebException(e);
                try {
                    noticesRestInternalFacade.createStreamMessageErrorNotification(ServiceContextHolder.getCurrentServiceContext(), codelistMetamacDto, TypeExternalArtefactsEnum.CODELIST);
                } catch (MetamacWebException noticeException) {
                    codelistStreamMessageException.getWebExceptionItems().addAll(noticeException.getWebExceptionItems());
                }
            }

            return new UpdateCodesVariableElementsResult(codelistStreamMessageException);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
