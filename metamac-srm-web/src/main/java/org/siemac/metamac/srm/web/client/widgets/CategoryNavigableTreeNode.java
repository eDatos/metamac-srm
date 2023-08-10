package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.srm.core.category.domain.shared.CategoryElementVisualisationResult;
import org.siemac.metamac.srm.web.category.model.ds.CategoryDS;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.NavigableTreeNode;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class CategoryNavigableTreeNode extends NavigableTreeNode {

    public void setCategoryElement(CategoryElementVisualisationResult categoryElement) {
        setAttribute(CategoryDS.CATEGORY_ELEMENT, categoryElement != null ? CommonWebUtils.getElementName(categoryElement.getCode(), categoryElement.getShortName()) : null);
        setAttribute(CategoryDS.CATEGORY_ELEMENT_ID_DATABASE, categoryElement != null ? categoryElement.getIdDatabase() : null);
        setAttribute(CategoryDS.CATEGORY_ELEMENT_URN, categoryElement != null ? categoryElement.getUrn() : null);
    }

    public void setCategoryElement(RelatedResourceDto categoryElement) {
        setAttribute(CategoryDS.CATEGORY_ELEMENT, RelatedResourceUtils.getRelatedResourceName(categoryElement));
        setAttribute(CategoryDS.CATEGORY_ELEMENT_ID_DATABASE, categoryElement != null ? categoryElement.getId() : null);
        setAttribute(CategoryDS.CATEGORY_ELEMENT_URN, categoryElement != null ? categoryElement.getUrn() : null);
    }

    public String getCategoryElementUrn() {
        return getAttribute(CategoryDS.CATEGORY_ELEMENT_URN);
    }
}
