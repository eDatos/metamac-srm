package org.siemac.metamac.srm.web.category.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.web.shared.criteria.CategoryElementWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface CategoryElementsUiHandlers extends BaseUiHandlers {

    void goToCategoryElement(String categoryElementCode);
    void retrieveCategoryElements(int firstResult, int maxResults, CategoryElementWebCriteria criteria);
    void createCategoryElement(CategoryElementDto categoryElementDto);
    void deleteCategoryElements(List<String> urns);
}
