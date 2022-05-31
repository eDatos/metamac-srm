package org.siemac.metamac.srm.core.category.domain;

import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;

public class CategoryMetamacResultExtensionPoint {

    private String eniScheme;
    private EniSchemaCatalog eniSchemeCatalog;
    
    public String getEniScheme() {
        return eniScheme;
    }

    public void setEniScheme(String eniScheme) {
        this.eniScheme = eniScheme;
    }

    public EniSchemaCatalog getEniSchemeCatalog() {
        return eniSchemeCatalog;
    }

    public void setEniSchemeCatalog(EniSchemaCatalog eniSchemeCatalog) {
        this.eniSchemeCatalog = eniSchemeCatalog;
    }
}
