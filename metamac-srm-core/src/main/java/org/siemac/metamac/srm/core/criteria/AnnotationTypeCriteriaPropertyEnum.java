package org.siemac.metamac.srm.core.criteria;

import java.io.Serializable;

public enum AnnotationTypeCriteriaPropertyEnum implements Serializable {

    CODE, TITLE, UUID;

    private AnnotationTypeCriteriaPropertyEnum() {
    }

    public String value() {
        return name();
    }

    public static AnnotationTypeCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}