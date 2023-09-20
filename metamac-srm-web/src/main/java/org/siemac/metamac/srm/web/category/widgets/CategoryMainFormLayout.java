package org.siemac.metamac.srm.web.category.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;

public class CategoryMainFormLayout extends InternationalMainFormLayout {

    protected MainFormLayoutButton updateCategoryElementButton;

    public CategoryMainFormLayout() {
        common();
    }

    public void setCategoryScheme(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        setCanEdit(CategoriesClientSecurityUtils.canUpdateCategory(categorySchemeMetamacDto.getLifeCycle().getProcStatus()));
        setCanDelete(CategoriesClientSecurityUtils.canDeleteCategory(categorySchemeMetamacDto));
        updateButtonsVisibility(categorySchemeMetamacDto);
    }

    private void common() {
        // Add a button to update the category element of the category
        updateCategoryElementButton = new MainFormLayoutButton(getConstants().categoryUpdateCategoryElement(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.editListGrid().getURL());
        toolStrip.addButton(updateCategoryElementButton, 1);
    }

    public HasClickHandlers getUpdateCategoryElement() {
        return updateCategoryElementButton;
    }

    public void updateButtonsVisibility(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        if (CommonUtils.isMaintainableArtefactPublished(categorySchemeMetamacDto.getLifeCycle().getProcStatus())) {
            showUpdateCategoryElementButton(categorySchemeMetamacDto);
        } else {
            updateCategoryElementButton.hide();
        }
    }

    protected void showUpdateCategoryElementButton(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        if (CategoriesClientSecurityUtils.canUpdateCategoryCategoryElement(categorySchemeMetamacDto)) {
            updateCategoryElementButton.show();
        }
    }
}
