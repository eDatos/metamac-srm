package org.siemac.metamac.srm.core.category.domain;

import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;

public class CategoryMetamacResultExtensionPoint {

    private String eniSchemeIdentifier;
    private EniSchemaCatalog eniSchemeCatalog;
    
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
}
