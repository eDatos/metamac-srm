package org.siemac.metamac.srm.web.shared;

import java.io.Serializable;

public enum ImportableResourceTypeEnum implements Serializable {
    SDMX_STRUCTURE, CODES, VARIABLE_ELEMENTS, CODES_ORDER, CODES_RESTRICTION, CODES_OPENNESS_LEVEL, VARIABLE_ELEMENT_SHAPE, CONCEPTS, ORGANISATIONS, CATEGORIES, CATEGORY_ELEMENTS, ANNOTATIONS;

    private ImportableResourceTypeEnum() {
    }

    public String getName() {
        return name();
    }
}
