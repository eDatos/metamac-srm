package org.siemac.metamac.srm.web.shared.criteria;

import java.util.List;

public class DataStructureDefinitionWebCriteria extends VersionableResourceWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            statisticalOperationUrn;
    private List<String>      dimensionConceptUrn;
    private String            attributeConceptUrn;
    private String            codelistUrn;

    public DataStructureDefinitionWebCriteria() {
    }

    public DataStructureDefinitionWebCriteria(String criteria) {
        super(criteria);
    }

    public String getStatisticalOperationUrn() {
        return statisticalOperationUrn;
    }

    public void setStatisticalOperationUrn(String statisticalOperationUrn) {
        this.statisticalOperationUrn = statisticalOperationUrn;
    }

    public List<String> getDimensionConceptUrn() {
        return dimensionConceptUrn;
    }

    public String getAttributeConceptUrn() {
        return attributeConceptUrn;
    }

    public void setDimensionConceptUrn(List<String> dimensionConceptUrn) {
        this.dimensionConceptUrn = dimensionConceptUrn;
    }

    public void setAttributeConceptUrn(String attributeConceptUrn) {
        this.attributeConceptUrn = attributeConceptUrn;
    }

    public String getCodelistUrn() {
        return codelistUrn;
    }

    public void setCodelistUrn(String codelistUrn) {
        this.codelistUrn = codelistUrn;
    }
}
