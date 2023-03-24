package org.siemac.metamac.srm.rest.external.v1_0.service.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
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
import org.siemac.metamac.rest.utils.RestCommonUtil;
import org.siemac.metamac.srm.core.common.service.utils.TsvExportationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExportUtils.class);
    private static final String HEADER_INTERNATIONAL_STRING_SEPARATOR = "#";
    private static final String ANNOTATION_METADATA_SEPARATOR = ";";

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

    /*
     * All annotation metadata will be added in the same column of the tsv/csv. The format will be:
     * [{fields annotation 1},{fields annotation 2}, ...]
     * Ex: [{id:id1,url:url1,text:[name#es:text_es_1,name#ca:text_ca_1,...]},{id:id2,url:url2,text:[name#es:text_es_2,name#ca:text_ca_2,...]}]
     */
    public static Map<String, String> toMapAnnotations(List<Annotation> annotations, List<String> languages) {
        Map<String, String> target = new LinkedHashMap<>();
        StringBuilder annotationsResult = new StringBuilder();

        for (Annotation annotation : annotations) {
            annotationsResult.append(StringUtils.isEmpty(annotationsResult) ? "" : ANNOTATION_METADATA_SEPARATOR + " ");
            String plainAnnotation = toMapAnnotation(annotation, languages);
            annotationsResult.append("{" + plainAnnotation + "}");
        }

        if (annotationsResult.length() > 0) {
            target.put("annotations", "[" + annotationsResult + "]");
        } else {
            target.put("annotations", null);
        }

        return target;
    }

    private static String toMapAnnotation(Annotation annotation, List<String> languages) {
        String annotationResult = null;

        annotationResult = "id:" + (StringUtils.isEmpty(annotation.getId()) ? "" : annotation.getId());
        annotationResult += ANNOTATION_METADATA_SEPARATOR + " url:" + (StringUtils.isEmpty(annotation.getUrl()) ? "" : annotation.getUrl());
        Map<String, String> texts = internationalString2MapExport("text", annotation.getText(), languages);
        Iterator<Map.Entry<String, String>> iterator = texts.entrySet().iterator();

        StringBuilder annotationTexts = new StringBuilder();
        while (iterator.hasNext()) {

            annotationTexts.append(StringUtils.isEmpty(annotationTexts) ? "" : ANNOTATION_METADATA_SEPARATOR + " ");

            Map.Entry<String, String> text = iterator.next();
            annotationTexts.append(text.getKey() + ":" + (StringUtils.isEmpty(text.getValue()) ? "" : text.getValue()));
        }
        annotationResult += ANNOTATION_METADATA_SEPARATOR + " text:[" + annotationTexts.toString() + "]";

        return annotationResult;
    }
}
