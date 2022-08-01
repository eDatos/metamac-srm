package org.siemac.metamac.srm.web.shared.organisation;

public enum OrganisationSchemeWebCriteriaOrderEnum {

    CODE, URN, PROC_STATUS, RESOURCE_CREATED_DATE, RESOURCE_LAST_UPDATED, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static OrganisationSchemeWebCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
