package org.siemac.metamac.srm.web.category.view.handlers;

import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;

public interface CategoryElementUiHandlers extends BaseCategoryElementUiHandlers {

    void saveCategoryElement(CategoryElementDto categoryElementDto);
    void deleteCategoryElement(String urn);

    // logo upload
    void resourceImportationFailed(String errorMessage);
    void resourceImportationSucceed(String successMessage, String categoryElementUrn);
    void showWaitPopup();

    void retrieveCategoryElementLogo(String categoryElementUrn);
    void deleteCategoryElementLogo(String categoryElementUrn);
}
