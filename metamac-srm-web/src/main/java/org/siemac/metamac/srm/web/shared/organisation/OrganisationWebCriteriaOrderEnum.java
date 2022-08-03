package org.siemac.metamac.srm.web.shared.organisation;

public enum OrganisationWebCriteriaOrderEnum {

    CODE, URN, ORGANISATION_SCHEME_URN, ORGANISATION_SCHEME_CODE, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static OrganisationWebCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
