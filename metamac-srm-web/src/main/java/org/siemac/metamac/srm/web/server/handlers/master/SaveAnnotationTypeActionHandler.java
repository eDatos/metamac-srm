package org.siemac.metamac.srm.web.server.handlers.master;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.master.SaveAnnotationTypeAction;
import org.siemac.metamac.srm.web.shared.master.SaveAnnotationTypeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveAnnotationTypeActionHandler extends SecurityActionHandler<SaveAnnotationTypeAction, SaveAnnotationTypeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveAnnotationTypeActionHandler() {
        super(SaveAnnotationTypeAction.class);
    }

    @Override
    public SaveAnnotationTypeResult executeSecurityAction(SaveAnnotationTypeAction action) throws ActionException {
        try {
            AnnotationTypeDto output = null;

            if (action.getDto().getUuid() == null) {
                output = srmCoreServiceFacade.createAnnotationType(ServiceContextHolder.getCurrentServiceContext(), action.getDto());
            } else {
                output = srmCoreServiceFacade.updateAnnotationType(ServiceContextHolder.getCurrentServiceContext(), action.getDto());
            }
            return new SaveAnnotationTypeResult(output);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }

    }

}
