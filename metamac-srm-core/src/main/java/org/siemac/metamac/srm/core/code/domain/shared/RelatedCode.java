package org.siemac.metamac.srm.core.code.domain.shared;

import java.io.Serializable;

public class RelatedCode implements Serializable {

    private static final long serialVersionUID = 1L;

    private String            originCodeUrn;
    private String            recodedCodeUrn;
    private String            originCode;
    private String            recodedCode;
    private String            variableElement;

    public RelatedCode() {
        // without impl.
    }

    public String getRecodedCode() {
        return recodedCode;
    }

    public void setRecodedCode(String recodedCode) {
        this.recodedCode = recodedCode;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getRecodedCodeUrn() {
        return recodedCodeUrn;
    }

    public void setRecodedCodeUrn(String recodedCodeUrn) {
        this.recodedCodeUrn = recodedCodeUrn;
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