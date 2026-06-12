package org.siemac.metamac.srm.web.server.handlers.code;

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
            // Due to slow performance the search must be divided if variable element information is provided. First the match with variable, variableFamily and variable element information is found
            // and, after this, the rest of conditions joined to the codelist Ids found in this search.
            codelistWebCriteria
                    .setCodeListIds(getCriteriaByVariableElement(codelistWebCriteria.getVariableUrn(), codelistWebCriteria.getVariableFamilyUrn(), codelistWebCriteria.getVariableElementUrn()));

            if (codelistWebCriteria.getCodeListIds() == null || codelistWebCriteria.getCodeListIds().isEmpty()) {
                return new GetCodelistsResult(new ArrayList<CodelistMetamacBasicDto>(), 0, 0);
            }

            codelistWebCriteria.setVariableElementUrn(null);
            codelistWebCriteria.setVariableUrn(null);
            codelistWebCriteria.setVariableFamilyUrn(null);
        }

        // Codelist criteria
        restriction.getRestrictions().add(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
        criteria.setRestriction(restriction);

        // When deduplicateIgnoringVersion is set, multiple codelist versions may satisfy the
        // isLastVersion OR filter simultaneously. Fetching all results and deduplicating by
        // URN-without-version on the server guarantees a correct totalResults count.
        if (codelistWebCriteria.isDeduplicateIgnoringVersion()) {
            return executeWithDeduplication(action, criteria);
        }

        // Normal paginated flow
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

    private GetCodelistsResult executeWithDeduplication(GetCodelistsAction action, MetamacCriteria criteria) throws ActionException {
        try {
            // Fetch all without pagination (capped to 1000 by MetamacCriteria2SculptorCriteria)
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setFirstResult(0);
            criteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            criteria.getPaginator().setCountTotalResults(false);
            MetamacCriteriaResult<CodelistMetamacBasicDto> result = srmCoreServiceFacade.findCodelistsByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);

            // Deduplicate: first occurrence of each URN-without-version wins; LinkedHashMap preserves query order
            Map<String, CodelistMetamacBasicDto> deduplicatedMap = new LinkedHashMap<String, CodelistMetamacBasicDto>();
            for (CodelistMetamacBasicDto codelist : result.getResults()) {
                String versionlessUrn = UrnUtils.removeVersion(codelist.getUrn());
                if (!deduplicatedMap.containsKey(versionlessUrn)) {
                    deduplicatedMap.put(versionlessUrn, codelist);
                }
            }
            List<CodelistMetamacBasicDto> deduplicatedList = new ArrayList<CodelistMetamacBasicDto>(deduplicatedMap.values());

            // Apply pagination in Java
            int totalResults = deduplicatedList.size();
            int firstResult = action.getFirstResult();
            int fromIndex = Math.min(firstResult, totalResults);
            int toIndex = Math.min(firstResult + action.getMaxResults(), totalResults);
            List<CodelistMetamacBasicDto> page = new ArrayList<CodelistMetamacBasicDto>(deduplicatedList.subList(fromIndex, toIndex));

            return new GetCodelistsResult(page, firstResult, totalResults);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private List<Long> getCriteriaByVariableElement(String variableUrn, String variableFamilyUrn, String variableElementUrn) throws ActionException {
        try {
            return srmCoreServiceFacade.findCodelistsByVariableInformation(ServiceContextHolder.getCurrentServiceContext(), variableUrn, variableFamilyUrn, variableElementUrn);

        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
