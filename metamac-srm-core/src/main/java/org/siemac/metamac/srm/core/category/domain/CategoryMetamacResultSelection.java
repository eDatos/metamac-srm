package org.siemac.metamac.srm.core.category.domain;

import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;

public class CategoryMetamacResultSelection extends ItemMetamacResultSelection {

    public static final CategoryMetamacResultSelection API                 = new CategoryMetamacResultSelection(false, false, false, false, false, true, false, true);
    public static final CategoryMetamacResultSelection EXPORT              = new CategoryMetamacResultSelection(true, true, true, true, true, false, true, false);
    public static final CategoryMetamacResultSelection RETRIEVE            = new CategoryMetamacResultSelection(true, false, true, true, false, false, false, false);

    private boolean                                    categoryElementFull = false;
    private boolean                                    categoryElementCode = false;

    private boolean                                    eniSchemaFull       = false;
    private boolean                                    eniSchemaCode       = false;

    public CategoryMetamacResultSelection(boolean descriptions, boolean comments, boolean annotations, boolean internationalStringsMetamac, boolean categoryElementCode, boolean categoryElementFull,
            boolean eniSchemaCode, boolean eniSchemaFull) {
        super(descriptions, comments, annotations, internationalStringsMetamac);
        this.categoryElementCode = categoryElementCode;
        this.categoryElementFull = categoryElementFull;
        this.eniSchemaCode = eniSchemaCode;
        this.eniSchemaFull = eniSchemaFull;
    }

    public boolean isCategoryElementCode() {
        return categoryElementCode;
    }

    public boolean isCategoryElementFull() {
        return categoryElementFull;
    }

    public void setCategoryElementFull(boolean categoryElementFull) {
        this.categoryElementFull = categoryElementFull;
    }

    public boolean isEniSchemaCode() {
        return eniSchemaCode;
    }

    public boolean isEniSchemaFull() {
        return eniSchemaFull;
    }

    public void setEniSchemaFull(boolean eniSchemaFull) {
        this.eniSchemaFull = eniSchemaFull;
    }
}
