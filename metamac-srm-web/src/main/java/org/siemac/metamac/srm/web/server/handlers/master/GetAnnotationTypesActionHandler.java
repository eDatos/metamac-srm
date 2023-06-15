package org.siemac.metamac.srm.web.server.handlers.master;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.master.GetAnnotationTypesAction;
import org.siemac.metamac.srm.web.shared.master.GetAnnotationTypesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetAnnotationTypesActionHandler extends SecurityActionHandler<GetAnnotationTypesAction, GetAnnotationTypesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetAnnotationTypesActionHandler() {
        super(GetAnnotationTypesAction.class);
    }

    @Override
    public GetAnnotationTypesResult executeSecurityAction(GetAnnotationTypesAction action) throws ActionException {
        try {
            List<AnnotationTypeDto> annotationTypes = srmCoreServiceFacade.findAllAnnotationTypes(ServiceContextHolder.getCurrentServiceContext());
            return new GetAnnotationTypesResult(annotationTypes);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
