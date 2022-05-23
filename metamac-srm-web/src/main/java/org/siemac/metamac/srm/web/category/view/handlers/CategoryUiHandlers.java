package org.siemac.metamac.srm.web.category.view.handlers;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;

public interface CategoryUiHandlers extends BaseCategoryUiHandlers {

    void retrieveEniSchemaCatalogs();
    void retrieveCategory(String categoryUrn);
    void retrieveCategoryListByScheme(String categorySchemeUrn);
    void deleteCategory(CategoryMetamacDto categoryMetamacDto);
}
