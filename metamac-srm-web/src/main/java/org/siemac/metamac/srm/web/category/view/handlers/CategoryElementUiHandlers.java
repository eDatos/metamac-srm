package org.siemac.metamac.srm.web.category.view.handlers;

import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;

public interface CategoryElementUiHandlers extends BaseCategoryElementUiHandlers {

    void saveCategoryElement(CategoryElementDto categoryElementDto);
    void deleteCategoryElement(String urn);
}
