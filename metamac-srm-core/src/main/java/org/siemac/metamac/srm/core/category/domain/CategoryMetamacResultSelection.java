package org.siemac.metamac.srm.core.category.domain;

import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;

public class CategoryMetamacResultSelection extends ItemMetamacResultSelection {

    public static final CategoryMetamacResultSelection EXPORT              = new CategoryMetamacResultSelection(true, true, true, true, true, false);

    private boolean                                    categoryElementFull = false;
    private boolean                                    categoryElementCode = false;

    public CategoryMetamacResultSelection(boolean descriptions, boolean comments, boolean annotations, boolean internationalStringsMetamac, boolean categoryElementCode, boolean categoryElementFull) {
        super(descriptions, comments, annotations, internationalStringsMetamac);
        this.categoryElementCode = categoryElementCode;
        this.categoryElementFull = categoryElementFull;
    }

    public boolean isCategoryElementCode() {
        return categoryElementCode;
    }

    public boolean isCategoryElementFull() {
        return categoryElementFull;
    }
}
