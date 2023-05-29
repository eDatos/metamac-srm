package org.siemac.metamac.srm.web.server.handlers.master;

import java.util.List;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.master.GetAnnotationTypesPaginatedListAction;
import org.siemac.metamac.srm.web.shared.master.GetAnnotationTypesPaginatedListResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetAnnotationTypesPaginatedListActionHandler extends SecurityActionHandler<GetAnnotationTypesPaginatedListAction, GetAnnotationTypesPaginatedListResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetAnnotationTypesPaginatedListActionHandler() {
        super(GetAnnotationTypesPaginatedListAction.class);
    }

    @Override
    public GetAnnotationTypesPaginatedListResult executeSecurityAction(GetAnnotationTypesPaginatedListAction action) throws ActionException {
        try {
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setFirstResult(action.getFirstResult());
            criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
            criteria.getPaginator().setCountTotalResults(true);
            List<AnnotationTypeDto> annotationTypes = srmCoreServiceFacade.findAllAnnotationTypes(ServiceContextHolder.getCurrentServiceContext());
            // return new GetAnnotationTypesResult(annotationTypes);
            // TODO EDATOS-4082 COMPLETAR
            return null;
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
