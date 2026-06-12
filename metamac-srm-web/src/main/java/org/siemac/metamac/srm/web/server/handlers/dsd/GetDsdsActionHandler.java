package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.criteria.DataStructureDefinitionVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.server.utils.MetamacWebCriteriaUtils;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDsdsActionHandler extends SecurityActionHandler<GetDsdsAction, GetDsdsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetDsdsActionHandler() {
        super(GetDsdsAction.class);
    }

    @Override
    public GetDsdsResult executeSecurityAction(GetDsdsAction action) throws ActionException {
        DataStructureDefinitionWebCriteria dsdWebCriteria = action.getCriteria();

        boolean hasDimensionConcept = dsdWebCriteria.getDimensionConceptUrn() != null && !dsdWebCriteria.getDimensionConceptUrn().isEmpty();
        boolean hasAttributeConcept = dsdWebCriteria.getAttributeConceptUrn() != null && !dsdWebCriteria.getAttributeConceptUrn().isEmpty();
        boolean hasCodelist = dsdWebCriteria.getCodelistUrn() != null && !dsdWebCriteria.getCodelistUrn().isEmpty();
        int componentCriteriaCount = (hasDimensionConcept ? 1 : 0) + (hasAttributeConcept ? 1 : 0) + (hasCodelist ? 1 : 0);

        if (componentCriteriaCount > 1) {
            return executeMultiComponentSearch(action, dsdWebCriteria, hasDimensionConcept, hasAttributeConcept, hasCodelist);
        }

        return executeSingleSearch(action, dsdWebCriteria);
    }

    private GetDsdsResult executeSingleSearch(GetDsdsAction action, DataStructureDefinitionWebCriteria dsdWebCriteria) throws ActionException {
        MetamacCriteria criteria = buildOrderedMetamacCriteria();
        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
        restriction.getRestrictions().add(MetamacWebCriteriaUtils.getDataStructureDefinitionCriteriaRestriction(dsdWebCriteria));
        criteria.setRestriction(restriction);
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);
        try {
            MetamacCriteriaResult<DataStructureDefinitionMetamacBasicDto> result = srmCoreServiceFacade.findDataStructureDefinitionsByCondition(ServiceContextHolder.getCurrentServiceContext(),
                    criteria);
            return new GetDsdsResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    // When multiple component criteria are active (e.g. dimension concept + attribute concept),
    // a single Sculptor/Hibernate query cannot satisfy them because both paths traverse
    // grouping.components and receive the same JOIN alias, requiring one row to simultaneously
    // match conflicting componentType values. We solve this by running each component criterion
    // in its own query and intersecting the resulting URN sets.
    private GetDsdsResult executeMultiComponentSearch(GetDsdsAction action, DataStructureDefinitionWebCriteria original, boolean hasDimensionConcept, boolean hasAttributeConcept, boolean hasCodelist)
            throws ActionException {
        try {
            // Save the three component criteria values
            List<String> savedDimensionConceptUrns = original.getDimensionConceptUrn();
            String savedAttributeConceptUrn = original.getAttributeConceptUrn();
            String savedCodelistUrn = original.getCodelistUrn();

            // Temporarily keep only the primary component criterion on original
            // (dimension > attribute > codelist), then restore after the primary query.
            // This avoids the Hibernate join alias collision when multiple criteria share grouping.components.
            if (hasDimensionConcept) {
                original.setAttributeConceptUrn(null);
                original.setCodelistUrn(null);
            } else if (hasAttributeConcept) {
                original.setDimensionConceptUrn(null);
                original.setCodelistUrn(null);
            } else {
                original.setDimensionConceptUrn(null);
                original.setAttributeConceptUrn(null);
            }
            List<DataStructureDefinitionMetamacBasicDto> orderedPrimaryResults = findAllDsdsOrdered(original);

            // Restore original immediately after the primary query
            original.setDimensionConceptUrn(savedDimensionConceptUrns);
            original.setAttributeConceptUrn(savedAttributeConceptUrn);
            original.setCodelistUrn(savedCodelistUrn);

            Set<String> intersectionUrns = extractUrns(orderedPrimaryResults);

            // Secondary queries: one per remaining component criterion. Only the component criterion
            // is set (no base criteria) because we are only computing the URN intersection.
            if (hasAttributeConcept && hasDimensionConcept) {
                DataStructureDefinitionWebCriteria attributeCriteria = new DataStructureDefinitionWebCriteria();
                attributeCriteria.setAttributeConceptUrn(savedAttributeConceptUrn);
                intersectionUrns.retainAll(extractUrns(findAllDsdsOrdered(attributeCriteria)));
            }
            if (hasCodelist && (hasDimensionConcept || hasAttributeConcept)) {
                DataStructureDefinitionWebCriteria codelistCriteria = new DataStructureDefinitionWebCriteria();
                codelistCriteria.setCodelistUrn(savedCodelistUrn);
                intersectionUrns.retainAll(extractUrns(findAllDsdsOrdered(codelistCriteria)));
            }

            // Filter the ordered primary result to keep only DSDs present in all component criteria sets
            List<DataStructureDefinitionMetamacBasicDto> filteredResults = new ArrayList<DataStructureDefinitionMetamacBasicDto>();
            for (DataStructureDefinitionMetamacBasicDto dto : orderedPrimaryResults) {
                if (intersectionUrns.contains(dto.getUrn())) {
                    filteredResults.add(dto);
                }
            }

            // Apply pagination in Java
            int totalResults = filteredResults.size();
            int firstResult = action.getFirstResult();
            int fromIndex = Math.min(firstResult, totalResults);
            int toIndex = Math.min(firstResult + action.getMaxResults(), totalResults);
            List<DataStructureDefinitionMetamacBasicDto> page = new ArrayList<DataStructureDefinitionMetamacBasicDto>(filteredResults.subList(fromIndex, toIndex));

            return new GetDsdsResult(page, firstResult, totalResults);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private List<DataStructureDefinitionMetamacBasicDto> findAllDsdsOrdered(DataStructureDefinitionWebCriteria webCriteria) throws MetamacException {
        MetamacCriteria criteria = buildOrderedMetamacCriteria();
        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
        restriction.getRestrictions().add(MetamacWebCriteriaUtils.getDataStructureDefinitionCriteriaRestriction(webCriteria));
        criteria.setRestriction(restriction);
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(0);
        criteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        criteria.getPaginator().setCountTotalResults(false);
        MetamacCriteriaResult<DataStructureDefinitionMetamacBasicDto> result = srmCoreServiceFacade.findDataStructureDefinitionsByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
        return result.getResults();
    }

    private MetamacCriteria buildOrderedMetamacCriteria() {
        MetamacCriteria criteria = new MetamacCriteria();
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(DataStructureDefinitionVersionMetamacCriteriaOrderEnum.RESOURCE_LAST_UPDATED.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);
        return criteria;
    }

    private Set<String> extractUrns(List<DataStructureDefinitionMetamacBasicDto> dtos) {
        Set<String> urns = new HashSet<String>();
        for (DataStructureDefinitionMetamacBasicDto dto : dtos) {
            if (dto.getUrn() != null) {
                urns.add(dto.getUrn());
            }
        }
        return urns;
    }
}
