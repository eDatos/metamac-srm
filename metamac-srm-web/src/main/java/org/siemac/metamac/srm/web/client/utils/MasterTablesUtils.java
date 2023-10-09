package org.siemac.metamac.srm.web.client.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;

public class MasterTablesUtils {

    private static final String DELIMITER_ANNOTATION_TYPE_ORDERED = "#";

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
     * Returns {String} from {EniSchemaCatalogDto}
     * 
     * @param EniSchemaCatalogDto
     * @return
     */
    public static String getEniSchemaDescription(EniSchemaCatalogDto eniSchema) {
        return InternationalStringUtils.getLocalisedString(eniSchema.getDescription());
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
    public static LinkedHashMap<String, String> getAnnotationTypeHashMap(List<AnnotationTypeDto> list, Map<String, String> annotationTypeOrderedRelatedById) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        for (AnnotationTypeDto type : list) {
            String description = InternationalStringUtils.getLocalisedString(type.getTitle());
            if (description == null) {
                description = type.getCode();
            }
            hashMap.put(type.getId().toString(), description);
        }

        return annotationTypeOrderByValue(hashMap, true, annotationTypeOrderedRelatedById);
    }

    /**
     * Returns {AnnotationTypeDto} from id
     * 
     * @param AnnotationTypeDto
     * @param id
     * @return
     */
    public static AnnotationTypeDto getAnnotationTypeDto(String idOrdered, List<AnnotationTypeDto> list) {
        String id = getIdFromAnnotationTypeOrdered(idOrdered);
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

    private static LinkedHashMap<String, String> annotationTypeOrderByValue(LinkedHashMap<String, String> linkMap, boolean addEmpty, Map<String, String> annotationTypeOrderedRelatedById) {
        LinkedHashMap<String, String> sortedMap = new LinkedHashMap<String, String>();

        // 1. get entrySet from LinkedHashMap object
        Set<Map.Entry<String, String>> sourceMapSet = linkMap.entrySet();

        // 2. convert LinkedHashMap to List of Map.Entry
        List<Map.Entry<String, String>> targetMapListEntry = new ArrayList<Map.Entry<String, String>>(sourceMapSet);

        // 3. sort list of entries using Collections class'
        // utility method sort(ls, cmptr)
        Collections.sort(targetMapListEntry, new Comparator<Map.Entry<String, String>>() {

            @Override
            public int compare(Entry<String, String> value1, Entry<String, String> value2) {
                return value1.getValue().compareToIgnoreCase(value2.getValue());
            }
        });

        if (addEmpty) {
            sortedMap.put("", ""); // ad empty at start position.
        }

        Integer sortNumber = 1;
        // 4. iterating list and storing in LinkedHahsMap
        for (Map.Entry<String, String> map : targetMapListEntry) {
            String orderedKey = sortNumber.toString() + DELIMITER_ANNOTATION_TYPE_ORDERED + map.getKey();
            annotationTypeOrderedRelatedById.put(map.getKey(), orderedKey);
            sortedMap.put(orderedKey, map.getValue());
            sortNumber++;
        }

        return sortedMap;
    }

    private static String getIdFromAnnotationTypeOrdered(String annotationTypeId) {
        String[] annotationTypeOrdered = annotationTypeId.split(DELIMITER_ANNOTATION_TYPE_ORDERED);
        if (annotationTypeOrdered.length == 2) {
            return annotationTypeOrdered[1];
        }
        return null;
    }

}
