package org.siemac.metamac.srm.core.code.domain.shared;

import java.io.Serializable;

public class EquivalenceCode implements Serializable {

    private static final long serialVersionUID = 1L;

    private String            originCodeUrn;
    private String            equivalentCodeUrn;
    private String            originCode;
    private String            equivalentCode;
    private String            variableElement;

    public EquivalenceCode() {
    }

    public String getEquivalentCode() {
        return equivalentCode;
    }

    public void setEquivalentCode(String equivalentCode) {
        this.equivalentCode = equivalentCode;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getEquivalentCodeUrn() {
        return equivalentCodeUrn;
    }

    public void setEquivalentCodeUrn(String equivalentCodeUrn) {
        this.equivalentCodeUrn = equivalentCodeUrn;
    }

    public String getOriginCodeUrn() {
        return originCodeUrn;
    }

    public void setOriginCodeUrn(String originCodeUrn) {
        this.originCodeUrn = originCodeUrn;
    }

    public String getVariableElement() {
        return variableElement;
    }

    public void setVariableElement(String variableElement) {
        this.variableElement = variableElement;
    }

    
}