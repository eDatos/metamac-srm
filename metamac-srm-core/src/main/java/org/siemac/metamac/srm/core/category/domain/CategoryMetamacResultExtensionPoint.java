package org.siemac.metamac.srm.core.category.domain;

import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;

public class CategoryMetamacResultExtensionPoint {

    private String                eniSchemeIdentifier;
    private CategoryElementResult categoryElement;
    private String                categoryElementCode;
    private EniSchemaCatalog      eniSchemeCatalog;

    public String getEniSchemeIdentifier() {
        return eniSchemeIdentifier;
    }

    public void setEniSchemeIdentifier(String eniSchemeIdentifier) {
        this.eniSchemeIdentifier = eniSchemeIdentifier;
    }

    public EniSchemaCatalog getEniSchemeCatalog() {
        return eniSchemeCatalog;
    }

    public void setEniSchemeCatalog(EniSchemaCatalog eniSchemeCatalog) {
        this.eniSchemeCatalog = eniSchemeCatalog;
    }

    public CategoryElementResult getCategoryElement() {
        return categoryElement;
    }

    public void setCategoryElement(CategoryElementResult categoryElement) {
        this.categoryElement = categoryElement;
    }

    public String getCategoryElementCode() {
        return categoryElementCode;
    }

    public void setCategoryElementCode(String categoryElementCode) {
        this.categoryElementCode = categoryElementCode;
    }
}
