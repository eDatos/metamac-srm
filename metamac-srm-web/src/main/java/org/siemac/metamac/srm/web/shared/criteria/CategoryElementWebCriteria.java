package org.siemac.metamac.srm.web.shared.criteria;

import java.util.Date;

public class CategoryElementWebCriteria extends NameableArtefactWebCriteria {

    private static final long serialVersionUID = 1L;

    private Date              validFromDate;
    private Date              validToDate;
    private Boolean           visible;

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

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
