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
import org.siemac.metamac.srm.core.criteria.OrganisationSchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.server.utils.MetamacWebCriteriaUtils;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemesAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationSchemesActionHandler extends SecurityActionHandler<GetOrganisationSchemesAction, GetOrganisationSchemesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationSchemesActionHandler() {
        super(GetOrganisationSchemesAction.class);
    }

    @Override
    public GetOrganisationSchemesResult executeSecurityAction(GetOrganisationSchemesAction action) throws ActionException {
      

        try {
            MetamacCriteriaResult<OrganisationSchemeMetamacBasicDto> result = srmCoreServiceFacade.findOrganisationSchemesByCondition(ServiceContextHolder.getCurrentServiceContext(), getCriterias(action));
            return new GetOrganisationSchemesResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private MetamacCriteria getCriterias(GetOrganisationSchemesAction action) {
        MetamacCriteria criteria = new MetamacCriteria();

        OrganisationSchemeWebCriteria organisationSchemeWebCriteria = action.getCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        if (organisationSchemeWebCriteria.getOrderType() != null) {
            order.setType(organisationSchemeWebCriteria.getOrderType());
        } else {
            order.setType(OrderTypeEnum.DESC);
        }

        if (organisationSchemeWebCriteria.getOrderField() != null) {
            order.setPropertyName(organisationSchemeWebCriteria.getOrderField().name());
        } else {
            order.setPropertyName(OrganisationSchemeVersionMetamacCriteriaOrderEnum.RESOURCE_LAST_UPDATED.name());
        }
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        // OrganisationScheme criteria
        restriction.getRestrictions().add(MetamacWebCriteriaUtils.getOrganisationSchemeCriteriaRestriction(organisationSchemeWebCriteria));
        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        return criteria;
    }
}
