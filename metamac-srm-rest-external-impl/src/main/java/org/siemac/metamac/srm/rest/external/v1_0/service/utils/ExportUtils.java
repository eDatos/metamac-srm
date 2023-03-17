package org.siemac.metamac.srm.rest.external.v1_0.service.utils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.utils.RestCommonUtil;
import org.siemac.metamac.srm.core.common.service.utils.TsvExportationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExportUtils.class);
    private static final String TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR = "#";

    private ExportUtils() {
    }

    public static Map<String, String> toMap(Object obj, Field[] fields, List<String> languages, Set<String> excludedFields) {
        Map<String, String> map = new LinkedHashMap<>();
        try {
            for (Field field : fields) {
                if (!containsField(excludedFields, field.getName())) {
                    field.setAccessible(true);
                    map.putAll(setFieldInMap(obj, field, languages));
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("Illegal access to field", e);
        }
        return map;
    }

    public static boolean containsField(Set<String> fields, String field) {
        return fields != null && fields.contains(field);
    }

    private static Map<String, String> setFieldInMap(Object obj, Field field, List<String> languages) throws IllegalAccessException {
        Map<String, String> target = new LinkedHashMap<>();
        if (field.getType().isAssignableFrom(InternationalString.class)) {
            Object e = field.get(obj);
            target.putAll(internationalString2MapExport(field.getName(), (InternationalString) e, languages));
        } else {
            target.put(field.getName(), getValueFieldPojo(obj, field));
        }

        return target;
    }

    private static Map<String, String> internationalString2MapExport(String nameField, InternationalString source, List<String> languages) {
        Map<String, String> target = new LinkedHashMap<>();

        InternationalString copySource = new InternationalString();

        if (source != null) {
            copySource = source;
        }

        String header;
        for (String language : languages) {
            header = nameField + TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language;
            target.put(header, getLocalisedStringByLang(copySource.getTexts(), language));
        }

        return target;
    }

    private static String getLocalisedStringByLang(List<LocalisedString> source, String language) {
        // in the api, lamba functions is not allowed. the search is done with a loop.
        for (LocalisedString loc : source) {
            if (language.equals(loc.getLang())) {
                return removeUnsupportedCharaters(loc.getValue());
            }
        }
        return null;
    }

    private static String getValueFieldPojo(Object obj, Field field) throws IllegalAccessException {
        Object value = field.get(obj) != null ? String.valueOf(field.get(obj)) : null;
        if (value != null) {
            return removeUnsupportedCharaters(String.valueOf(value));
        }
        return null;
    }

    public static String getDateAsString(Date date) {
        DateTime validDate = RestCommonUtil.transformDateToDateTime(date);
        return RestCommonUtil.jodaDateTime2xsDateTime(validDate);
    }

    private static String removeUnsupportedCharaters(String string) {
        return TsvExportationUtils.removeUnsupportedCharaters(string);
    }
}
