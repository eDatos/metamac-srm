package org.siemac.metamac.srm.core.task.domain;

public class ImportationCategoriesTsvHeader extends ImportationItemsTsvHeader {

    private int eniSchemePosition;
    private int categoryElementPosition;

    public int getEniSchemePosition() {
        return eniSchemePosition;
    }

    public void setEniSchemePosition(int eniSchemePosition) {
        this.eniSchemePosition = eniSchemePosition;
    }

    public int getCategoryElementPosition() {
        return categoryElementPosition;
    }

    public void setCategoryElementPosition(int categoryElementPosition) {
        this.categoryElementPosition = categoryElementPosition;
    }
}
