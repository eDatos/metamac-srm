package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.server.utils.MetamacWebCriteriaUtils;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCodelistsActionHandler extends SecurityActionHandler<GetCodelistsAction, GetCodelistsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCodelistsActionHandler() {
        super(GetCodelistsAction.class);
    }

    @Override
    public GetCodelistsResult executeSecurityAction(GetCodelistsAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(CodelistVersionMetamacCriteriaOrderEnum.RESOURCE_LAST_UPDATED.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        CodelistWebCriteria codelistWebCriteria = action.getCriteria();

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        if (codelistWebCriteria.getVariableElementUrn() != null) {
            //Due to slow performance the search must be divided if variable element information is provided. First the match with variable, variableFamily and variable element information is found
            //and, after this, the rest of conditions joined to the codelist Ids found in this search.
            codelistWebCriteria
                    .setCodeListIds(getCriteriaByVariableElement(codelistWebCriteria.getVariableUrn(), codelistWebCriteria.getVariableFamilyUrn(), codelistWebCriteria.getVariableElementUrn()));
            
            if (codelistWebCriteria.getCodeListIds() == null ||codelistWebCriteria.getCodeListIds().isEmpty()) {
                return new GetCodelistsResult(new ArrayList<CodelistMetamacBasicDto>(), 0, 0);
            }
            
            codelistWebCriteria.setVariableElementUrn(null);
            codelistWebCriteria.setVariableUrn(null);
            codelistWebCriteria.setVariableFamilyUrn(null);
        }
        
        // Codelist criteria
        restriction.getRestrictions().add(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<CodelistMetamacBasicDto> result = srmCoreServiceFacade.findCodelistsByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetCodelistsResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
    
    private List<Object> getCriteriaByVariableElement(String variableUrn, String variableFamilyUrn, String variableElementUrn) throws ActionException {
        try {
            return srmCoreServiceFacade.findCodelistsByVariableInformation(ServiceContextHolder.getCurrentServiceContext(), variableUrn, variableFamilyUrn, variableElementUrn);

        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
