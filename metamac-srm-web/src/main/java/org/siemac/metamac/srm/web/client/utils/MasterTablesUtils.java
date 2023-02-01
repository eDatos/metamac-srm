package org.siemac.metamac.srm.web.client.utils;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

public class MasterTablesUtils {

    private MasterTablesUtils() {
        // without impl.
    }
    /**
     * Returns {@link LinkedHashMap} of {@link EniSchemaCatalogDto}
     * 
     * @return
     */
    public static LinkedHashMap<String, String> getEniSchemaCatalogHashMap(List<EniSchemaCatalogDto> list) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put("", "");
        for (EniSchemaCatalogDto type : list) {
            hashMap.put(type.getId().toString(), InternationalStringUtils.getLocalisedString(type.getDescription()));
        }
        return hashMap;
    }
    
    /**
     * Returns {EniSchemaCatalogDto} from id
     * 
     * @param EniSchemaCatalogDto
     * @param id
     * @return
     */
    public static EniSchemaCatalogDto geteniSchemaCatalogDto(String id, List<EniSchemaCatalogDto> list) {
        if (id != null && !id.isEmpty()) {
            Long idType = Long.valueOf(id);
            for (EniSchemaCatalogDto o : list) {
                if (o.getId().compareTo(idType) == 0) {
                    return o;
                }
            }
        }
        return null;
    }
    
}
