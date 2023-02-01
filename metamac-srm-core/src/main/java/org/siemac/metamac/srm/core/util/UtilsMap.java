package org.siemac.metamac.srm.core.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;

public class UtilsMap {

    private UtilsMap() {
        // without impl
    }

    public static Map<String, EniSchemaCatalog> getMapFromEniScheme(List<EniSchemaCatalog> eniSchemeCatalogs) {
        LinkedHashMap<String, EniSchemaCatalog> eniSchemeMap = new LinkedHashMap<String, EniSchemaCatalog>();
        for (EniSchemaCatalog eniItem : eniSchemeCatalogs) {
            eniSchemeMap.put(eniItem.getIdentifier(), eniItem);
        }
        return eniSchemeMap;
    }
}
