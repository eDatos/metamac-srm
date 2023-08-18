package org.siemac.metamac.srm.core.criteria;

public enum CategoryElementCriteriaOrderEnum {

    CODE, URN, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static CategoryElementCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}
