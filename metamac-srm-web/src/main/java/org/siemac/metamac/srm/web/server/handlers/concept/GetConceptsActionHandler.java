package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.server.utils.MetamacWebCriteriaUtils;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsResult;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptsActionHandler extends SecurityActionHandler<GetConceptsAction, GetConceptsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetConceptsActionHandler() {
        super(GetConceptsAction.class);
    }

    @Override
    public GetConceptsResult executeSecurityAction(GetConceptsAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.LAST_UPDATED.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        ConceptWebCriteria conceptWebCriteria = action.getCriteria();

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
        restriction.getRestrictions().add(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
        criteria.setRestriction(restriction);

        // When deduplicateIgnoringVersion is set, multiple concept scheme versions may satisfy the
        // isLastVersion OR filter simultaneously. Fetching all results and deduplicating by
        // URN-without-version on the server guarantees a correct totalResults count.
        if (conceptWebCriteria.isDeduplicateIgnoringVersion()) {
            return executeWithDeduplication(action, criteria);
        }

        // Normal paginated flow
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<ConceptMetamacBasicDto> result = srmCoreServiceFacade.findConceptsByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetConceptsResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private GetConceptsResult executeWithDeduplication(GetConceptsAction action, MetamacCriteria criteria) throws ActionException {
        try {
            // Fetch all without pagination (capped to 1000 by MetamacCriteria2SculptorCriteria)
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setFirstResult(0);
            criteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            criteria.getPaginator().setCountTotalResults(false);
            MetamacCriteriaResult<ConceptMetamacBasicDto> result = srmCoreServiceFacade.findConceptsByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);

            // Deduplicate: first occurrence of each URN-without-version wins; LinkedHashMap preserves query order
            Map<String, ConceptMetamacBasicDto> deduplicatedMap = new LinkedHashMap<String, ConceptMetamacBasicDto>();
            for (ConceptMetamacBasicDto concept : result.getResults()) {
                String versionlessUrn = UrnUtils.removeVersion(concept.getUrn());
                if (!deduplicatedMap.containsKey(versionlessUrn)) {
                    deduplicatedMap.put(versionlessUrn, concept);
                }
            }
            List<ConceptMetamacBasicDto> deduplicatedList = new ArrayList<ConceptMetamacBasicDto>(deduplicatedMap.values());

            // Apply pagination in Java
            int totalResults = deduplicatedList.size();
            int firstResult = action.getFirstResult();
            int fromIndex = Math.min(firstResult, totalResults);
            int toIndex = Math.min(firstResult + action.getMaxResults(), totalResults);
            List<ConceptMetamacBasicDto> page = new ArrayList<ConceptMetamacBasicDto>(deduplicatedList.subList(fromIndex, toIndex));

            return new GetConceptsResult(page, firstResult, totalResults);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
