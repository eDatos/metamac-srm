package org.siemac.metamac.srm.core.criteria;

public enum CategoryElementCriteriaPropertyEnum {

    // @formatter:off
    CODE, 
    URN, 
    SHORT_NAME, 
    VALID_FROM_DATE,
    VALID_TO_DATE,
    VISIBLE;
    // @formatter:on

    public String value() {
        return name();
    }
    public static CategoryElementCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}
