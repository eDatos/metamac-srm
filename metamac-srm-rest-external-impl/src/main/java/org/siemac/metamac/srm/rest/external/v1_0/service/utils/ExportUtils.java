package org.siemac.metamac.srm.rest.external.v1_0.service.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Annotation;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.export.entities.ExportFieldAnnotation;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.export.entities.PlainTextAnnotation;
import org.siemac.metamac.rest.utils.RestCommonUtil;
import org.siemac.metamac.srm.core.common.service.utils.TsvExportationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExportUtils.class);
    private static final String HEADER_INTERNATIONAL_STRING_SEPARATOR = "#";
    private static final String HEADER_PREFIX_ANNOTATION = "annotation_";
    private static final int NUM_MAX_ANNOTATIONS = 5;

    private ExportUtils() {
    }

    public static Map<String, String> toMap(Object obj, List<Field> fields, List<String> languages, String prefixName, Set<String> excludedFields) {
        Map<String, String> map = new LinkedHashMap<>();
        try {
            for (Field field : fields) {
                if (!containsField(excludedFields, field.getName())) {
                    field.setAccessible(true);
                    map.putAll(setFieldInMap(obj, field, languages, prefixName));
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("Illegal access to field", e);
        }
        return map;
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        return fields;
    }

    public static boolean containsField(Set<String> fields, String field) {
        return fields != null && fields.contains(field);
    }

    private static Map<String, String> setFieldInMap(Object obj, Field field, List<String> languages, String prefixName) throws IllegalAccessException {
        Map<String, String> target = new LinkedHashMap<>();
        ExportFieldAnnotation exportFieldAnnotation = field.getAnnotation(ExportFieldAnnotation.class);
        String fieldName = getFieldName(prefixName, field.getName(), exportFieldAnnotation);
        if (field.getType().isAssignableFrom(InternationalString.class)) {
            Object e = field.get(obj);
            target.putAll(internationalString2MapExport(fieldName, (InternationalString) e, languages));
        } else {
            target.put(fieldName, getValueFieldPojo(obj, field));
        }

        return target;
    }

    private static String getFieldName(String prefix, String sourceFieldName, ExportFieldAnnotation exportFieldAnnotation) {
        String fieldName = exportFieldAnnotation != null && !StringUtils.isBlank(exportFieldAnnotation.name()) ? exportFieldAnnotation.name() : sourceFieldName;
        if (!StringUtils.isBlank(prefix)) {
            fieldName = prefix + fieldName;
        }
        return fieldName;
    }

    private static Map<String, String> internationalString2MapExport(String nameField, InternationalString source, List<String> languages) {
        Map<String, String> target = new LinkedHashMap<>();

        InternationalString copySource = new InternationalString();

        if (source != null) {
            copySource = source;
        }

        String header;
        for (String language : languages) {
            header = nameField + HEADER_INTERNATIONAL_STRING_SEPARATOR + language;
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

    public static Map<String, String> toMapAnnotations(List<Annotation> annotations, List<String> languages) {
        Map<String, String> mapAnnotations = new LinkedHashMap<>();
        List<Annotation> copyAnnotations;
        int iterator = 0;
        if (annotations == null || annotations.isEmpty()) {
            copyAnnotations = new ArrayList<>();
            copyAnnotations.addAll(Collections.nCopies(NUM_MAX_ANNOTATIONS, new Annotation()));
        } else {
            copyAnnotations = annotations;
            if (copyAnnotations.size() < NUM_MAX_ANNOTATIONS) {
                copyAnnotations.addAll(Collections.nCopies(NUM_MAX_ANNOTATIONS - copyAnnotations.size(), new Annotation()));
            }
        }

        for (Annotation annotation : copyAnnotations) {
            iterator++;
            if (iterator > NUM_MAX_ANNOTATIONS) {
                break;
            }

            mapAnnotations.putAll(toMapAnnotation(annotation, languages, iterator));
        }

        return mapAnnotations;
    }

    private static Map<String, String> toMapAnnotation(Annotation annotation, List<String> languages, int iterator) {
        PlainTextAnnotation plainTextAnnotation = new PlainTextAnnotation();
        plainTextAnnotation.setId(annotation.getId());
        plainTextAnnotation.setUrl(annotation.getUrl());
        plainTextAnnotation.setText(annotation.getText());

        return toMap(plainTextAnnotation, Arrays.asList(plainTextAnnotation.getClass().getDeclaredFields()), languages, getPrefixAnnotation(iterator), new HashSet<>());
    }

    private static String getPrefixAnnotation(int iterator) {
        return HEADER_PREFIX_ANNOTATION + Integer.toString(iterator) + "_";
    }
}
