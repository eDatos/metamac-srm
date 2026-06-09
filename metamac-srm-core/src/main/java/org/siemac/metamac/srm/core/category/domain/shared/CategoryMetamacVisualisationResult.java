package org.siemac.metamac.srm.core.category.domain.shared;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;

public class CategoryMetamacVisualisationResult extends ItemVisualisationResult {

    private static final long                  serialVersionUID = 1L;

    private CategoryElementVisualisationResult categoryElement;
    private Integer                            order;

    public CategoryMetamacVisualisationResult() {
    }

    public CategoryElementVisualisationResult getCategoryElement() {
        return categoryElement;
    }

    public void setCategoryElement(CategoryElementVisualisationResult categoryElement) {
        this.categoryElement = categoryElement;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}