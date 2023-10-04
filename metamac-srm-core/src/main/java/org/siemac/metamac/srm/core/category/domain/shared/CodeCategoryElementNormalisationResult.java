package org.siemac.metamac.srm.core.category.domain.shared;

import java.io.Serializable;

public class CodeCategoryElementNormalisationResult implements Serializable {

    private static final long                  serialVersionUID = 1L;

    private CategoryMetamacVisualisationResult        code;
    private CategoryElementVisualisationResult categoryElementProposed;

    public CategoryMetamacVisualisationResult getCode() {
        return code;
    }

    public void setCode(CategoryMetamacVisualisationResult code) {
        this.code = code;
    }

    public CategoryElementVisualisationResult getCategoryElementProposed() {
        return categoryElementProposed;
    }

    public void setCategoryElementProposed(CategoryElementVisualisationResult categoryElementProposed) {
        this.categoryElementProposed = categoryElementProposed;
    }

}