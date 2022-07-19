package org.siemac.metamac.srm.web.shared.criteria;

import java.util.List;

import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.srm.web.shared.organisation.OrganisationSchemeWebCriteriaOrderEnum;
import org.siemac.metamac.web.common.shared.criteria.base.HasOnlyLastVersionCriteria;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationSchemeWebCriteria extends VersionableResourceWebCriteria implements HasOnlyLastVersionCriteria {

    private static final long          serialVersionUID = 1L;

    private OrganisationSchemeTypeEnum type;
    
    private OrganisationSchemeTypeEnum[] organisationSchemeTypes;
    
    private OrganisationSchemeWebCriteriaOrderEnum orderField;
    
    private OrderTypeEnum orderType;
    
   
    private boolean           onlyPublished = false;
    private boolean           onlyLastVersion         = false;
    List<String>              urns;
    
    public OrganisationSchemeWebCriteria() {
    }

    public OrganisationSchemeWebCriteria(String criteria) {
        super(criteria);
    }

    public OrganisationSchemeTypeEnum getType() {
        return type;
    }
    
    public void setType(OrganisationSchemeTypeEnum type) {
        this.type = type;
    }
    
    public OrganisationSchemeTypeEnum[] getOrganisationSchemeTypes() {
        return organisationSchemeTypes;
    }
    
    public void setOrganisationSchemeTypes(OrganisationSchemeTypeEnum[] organisationSchemeTypes) {
        this.organisationSchemeTypes = organisationSchemeTypes;
    }
    
    @Override
    public boolean isOnlyLastVersion() {
        return onlyLastVersion;
    }

    @Override
    public void setOnlyLastVersion(boolean onlyLastVersion) {
        this.onlyLastVersion = onlyLastVersion;
    }
    
    public List<String> getUrns() {
        return urns;
    }

    public void setUrns(List<String> urns) {
        this.urns = urns;
    }

    public OrganisationSchemeWebCriteriaOrderEnum getOrderField() {
        return orderField;
    }

    public void setOrderField(OrganisationSchemeWebCriteriaOrderEnum orderField) {
        this.orderField = orderField;
    }

    public OrderTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderTypeEnum orderType) {
        this.orderType = orderType;
    }

    public boolean isOnlyPublished() {
        return onlyPublished;
    }

    public void setOnlyPublished(boolean onlyPublished) {
        this.onlyPublished = onlyPublished;
    }
}
