package org.siemac.metamac.srm.core.criteria;

import java.io.Serializable;

public enum AnnotationTypeCriteriaOrderEnum implements Serializable {

    CODE, TITLE;

    private AnnotationTypeCriteriaOrderEnum() {
    }

    public String value() {
        return name();
    }

    public static AnnotationTypeCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}