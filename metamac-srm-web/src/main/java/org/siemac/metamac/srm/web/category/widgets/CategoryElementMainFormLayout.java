package org.siemac.metamac.srm.web.category.widgets;

import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

public class CategoryElementMainFormLayout extends InternationalMainFormLayout {

    public CategoryElementMainFormLayout() {
        super();
    }

    public CategoryElementMainFormLayout(boolean canEdit, boolean canDelete) {
        super(canEdit, canDelete);
    }

    public void setCategoryElement() {
        setCanEdit();
        setCanDelete();
    }

    public void setCanEdit() {
        super.setCanEdit(CategoriesClientSecurityUtils.canUpdateCategoryElement());
    }

    public void setCanDelete() {
        super.setCanDelete(CategoriesClientSecurityUtils.canDeleteCategoryElement());
    }
}
