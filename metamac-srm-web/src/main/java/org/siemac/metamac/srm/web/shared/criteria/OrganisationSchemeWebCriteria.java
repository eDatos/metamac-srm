package org.siemac.metamac.srm.web.shared.criteria;

import java.util.List;

import org.siemac.metamac.web.common.shared.criteria.base.HasOnlyExternallyPublishedCriteria;
import org.siemac.metamac.web.common.shared.criteria.base.HasOnlyLastVersionCriteria;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationSchemeWebCriteria extends VersionableResourceWebCriteria implements HasOnlyLastVersionCriteria, HasOnlyExternallyPublishedCriteria {

    private static final long          serialVersionUID = 1L;

    private OrganisationSchemeTypeEnum type;
    
    private OrganisationSchemeTypeEnum[] organisationSchemeTypes;
   
    private boolean           onlyExternallyPublished = false;
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
    public boolean isOnlyExternallyPublished() {
        return onlyExternallyPublished;
    }

    @Override
    public void setOnlyExternallyPublished(boolean externallyPublished) {
        onlyExternallyPublished = externallyPublished;
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
}
