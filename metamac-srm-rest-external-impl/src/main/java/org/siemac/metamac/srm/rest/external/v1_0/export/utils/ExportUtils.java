package org.siemac.metamac.srm.rest.external.v1_0.export.utils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Variable;

public class ExportUtils {

    public static final String TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR = "#";

    public Map<String, String> setFieldInMap(Object obj, Field field, List<String> languages) throws IllegalArgumentException, IllegalAccessException {
        Map<String, String> target = new LinkedHashMap<>();
        if (field.getType().isAssignableFrom(InternationalString.class)) {
            Object e = field.get(obj);
            target.putAll(internationalString2MapExport(field.getName(), (InternationalString) e, languages));
        } else if (field.getType().isAssignableFrom(Variable.class)) {
            target.put(field.getName(), null);
        } else {
            target.put(field.getName(), getValueFieldPojo(obj, field));
        }

        return target;
    }

    public Map<String, String> toMap(Object obj, Field[] fields, List<String> languages) throws IllegalArgumentException, IllegalAccessException {
        Map<String, String> map = new LinkedHashMap<>();
        for (Field field : fields) {
            if (!"fields".equals(field.getName())) {
                field.setAccessible(true);
                map.putAll(setFieldInMap(obj, field, languages));
            }
        }
        return map;
    }

    private Map<String, String> internationalString2MapExport(String nameField, InternationalString source, List<String> languages) {
        Map<String, String> target = new LinkedHashMap<>();

        if (source == null) {
            return null;
        }

        String header;
        String value;
        for (String language : languages) {
            header = nameField + TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language;
            value = null;
            Optional<LocalisedString> valueLanguage = source.getTexts().stream().filter(p -> language.equals(p.getLang())).findFirst();
            if (valueLanguage.isPresent()) {
                value = valueLanguage.get().getValue();
            }
            target.put(header, value);
        }

        return target;
    }

    private String getValueFieldPojo(Object obj, Field field) throws IllegalArgumentException, IllegalAccessException {
        Object value = field.get(obj) != null ? String.valueOf(field.get(obj)) : null;
        return String.valueOf(value);
    }

}
