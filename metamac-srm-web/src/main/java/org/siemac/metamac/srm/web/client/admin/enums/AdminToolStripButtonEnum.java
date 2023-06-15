package org.siemac.metamac.srm.web.client.admin.enums;

import com.smartgwt.client.types.ValueEnum;

public enum AdminToolStripButtonEnum implements ValueEnum {

    ANNOTATION_TYPES("annotation_type_button");

    private String value;

    AdminToolStripButtonEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
