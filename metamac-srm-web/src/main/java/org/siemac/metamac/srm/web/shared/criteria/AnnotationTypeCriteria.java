package org.siemac.metamac.srm.web.shared.criteria;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.web.common.shared.criteria.PaginationWebCriteria;

public class AnnotationTypeCriteria extends PaginationWebCriteria {

    private static final long          serialVersionUID = 5888787900805195271L;

    private List<MetamacCriteriaOrder> orders           = new ArrayList<MetamacCriteriaOrder>();

    public AnnotationTypeCriteria() {
        setFirstResult(SrmWebConstants.ITEM_LIST_FIRST_RESULT);
        setMaxResults(SrmWebConstants.ITEM_LIST_MAX_RESULTS);
    }

    public List<MetamacCriteriaOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<MetamacCriteriaOrder> orders) {
        this.orders = orders;
    }
}
