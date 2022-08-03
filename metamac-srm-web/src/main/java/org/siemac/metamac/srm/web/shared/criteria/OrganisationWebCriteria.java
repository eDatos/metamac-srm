package org.siemac.metamac.srm.web.shared.criteria;

import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.srm.web.shared.organisation.OrganisationWebCriteriaOrderEnum;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class OrganisationWebCriteria extends ItemWebCriteria {

    private static final long          serialVersionUID = 1L;

    private OrganisationSchemeTypeEnum organisationSchemeType;

    private OrganisationTypeEnum[] organisationTypes;
    
    private OrganisationWebCriteriaOrderEnum orderField;
    
    private OrderTypeEnum orderType;
    
    public OrganisationWebCriteria() {
    }

    public OrganisationWebCriteria(String criteria) {
        super(criteria);
    }

    public OrganisationSchemeTypeEnum getOrganisationSchemeType() {
        return organisationSchemeType;
    }

    public void setOrganisationSchemeType(OrganisationSchemeTypeEnum organisationSchemeType) {
        this.organisationSchemeType = organisationSchemeType;
    }
    
    public OrganisationTypeEnum[] getOrganisationTypes() {
        return organisationTypes;
    }
    
    public void setOrganisationTypes(OrganisationTypeEnum[] organisationTypes) {
        this.organisationTypes = organisationTypes;
    }

    public OrganisationWebCriteriaOrderEnum getOrderField() {
        return orderField;
    }

    public void setOrderField(OrganisationWebCriteriaOrderEnum orderField) {
        this.orderField = orderField;
    }

    public OrderTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderTypeEnum orderType) {
        this.orderType = orderType;
    }
    
    
}
