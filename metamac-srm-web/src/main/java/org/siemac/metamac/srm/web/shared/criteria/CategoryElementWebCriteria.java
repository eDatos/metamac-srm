package org.siemac.metamac.srm.web.shared.criteria;

import java.util.Date;

public class CategoryElementWebCriteria extends NameableArtefactWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            categorySchemeUrn;
    private Date              validFromDate;
    private Date              validToDate;

    public CategoryElementWebCriteria() {
    }

    public CategoryElementWebCriteria(String criteria) {
        super(criteria);
    }

    public Date getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(Date validFromDate) {
        this.validFromDate = validFromDate;
    }

    public Date getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(Date validToDate) {
        this.validToDate = validToDate;
    }

    public String getCategorySchemeUrn() {
        return categorySchemeUrn;
    }

    public void setCategorySchemeUrn(String categorySchemeUrn) {
        this.categorySchemeUrn = categorySchemeUrn;
    }
}
