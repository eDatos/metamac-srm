package org.siemac.metamac.srm.web.client.utils;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;

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

    /**
     * Returns {@link LinkedHashMap} of {@link AnnotationTypeDto}
     * 
     * @return
     */
    public static LinkedHashMap<String, String> getAnnotationTypeHashMap(List<AnnotationTypeDto> list) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put("", "");
        for (AnnotationTypeDto type : list) {
            hashMap.put(type.getId().toString(), InternationalStringUtils.getLocalisedString(type.getTitle()));
        }
        return hashMap;
    }

    /**
     * Returns {AnnotationTypeDto} from id
     * 
     * @param AnnotationTypeDto
     * @param id
     * @return
     */
    public static AnnotationTypeDto getAnnotationTypeDto(String id, List<AnnotationTypeDto> list) {
        if (id != null && !id.isEmpty()) {
            Long idType = Long.valueOf(id);
            for (AnnotationTypeDto o : list) {
                if (o.getId().compareTo(idType) == 0) {
                    return o;
                }
            }
        }
        return null;
    }
}
