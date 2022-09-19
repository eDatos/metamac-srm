package org.siemac.metamac.srm.web.server.handlers.organisation;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.criteria.OrganisationMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.web.server.utils.MetamacWebCriteriaUtils;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationWebCriteria;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationsActionHandler extends SecurityActionHandler<GetOrganisationsAction, GetOrganisationsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationsActionHandler() {
        super(GetOrganisationsAction.class);
    }

    @Override
    public GetOrganisationsResult executeSecurityAction(GetOrganisationsAction action) throws ActionException {

        try {
            MetamacCriteriaResult<OrganisationMetamacBasicDto> result = srmCoreServiceFacade.findOrganisationsByCondition(ServiceContextHolder.getCurrentServiceContext(), getCriterias(action));
            return new GetOrganisationsResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private MetamacCriteria getCriterias(GetOrganisationsAction action) {
        MetamacCriteria criteria = new MetamacCriteria();

        OrganisationWebCriteria organisationWebCriteria = action.getCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();

        if (organisationWebCriteria.getOrderType() != null) {
            order.setType(organisationWebCriteria.getOrderType());
        } else {
            order.setType(OrderTypeEnum.DESC);
        }

        if (organisationWebCriteria.getOrderField() != null) {
            order.setPropertyName(organisationWebCriteria.getOrderField().name());
        } else {
            order.setPropertyName(OrganisationMetamacCriteriaOrderEnum.LAST_UPDATED.name());
        }

        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        // Organisation criteria
        restriction.getRestrictions().add(MetamacWebCriteriaUtils.getOrganisationCriteriaRestriction(organisationWebCriteria));
        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        return criteria;
    }
}
