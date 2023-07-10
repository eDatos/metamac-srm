package org.siemac.metamac.srm.web.category.view.handlers;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;

public interface CategoryUiHandlers extends BaseCategoryUiHandlers {

    void retrieveCategory(String categoryUrn);
    void retrieveCategoryListByScheme(String categorySchemeUrn);
    void deleteCategory(CategoryMetamacDto categoryMetamacDto);

    void updateCategoryElement(String categoryUrn, String categoryElementUrn);
    void retrieveCategoryElements(int firstResult, int maxResults, String criteria);
}
