package org.siemac.metamac.srm.rest.external.v1_0.adapter;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.siemac.metamac.rest.structural_resources.v1_0.domain.export.entities.PlainTextTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;

@Produces({"text/tab-separated-values", "text/csv"})
public class PlainTextMessageBodyWritter implements MessageBodyWriter<Object> {

    private final Logger logger = LoggerFactory.getLogger(PlainTextMessageBodyWritter.class);

    public long getSize(Object myCollectionOfObjects, Class type, Type genericType, Annotation[] annotations, MediaType arg4) {
        return 0;
    }

    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType arg3) {
        return true;
    }

    public void writeTo(Object myCollectionOfObjects, Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, OutputStream entityHeaders)
            throws IOException, WebApplicationException {
        // Whatever makes it in here should be a list
        List<?> myList = new ArrayList<>();
        if (myCollectionOfObjects instanceof List && ((myList = (List<?>) myCollectionOfObjects).size() > 0)) {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema schema = null;

            // If it's not a flat POJO must implement CSVTransformer

            if (implementsPlainTextTransformer(myList.get(0).getClass())) {
                Class[] params = {};
                try {
                    Method meth = PlainTextTransformer.class.getDeclaredMethod("flatten", params);

                    List<Map<String, ?>> listOfMaps = new ArrayList<>();
                    Set<String> headers = null;
                    for (Object obj : myList) {
                        Map<String, ?> keyVals = (Map<String, ?>) meth.invoke(obj, params);

                        if (schema == null) {
                            schema = this.buildSchemaFromKeySet(keyVals.keySet());
                            headers = keyVals.keySet();
                        }

                        // Validate that latest headers are the same as the original ones
                        if (headers.equals(keyVals.keySet()))
                            listOfMaps.add(keyVals);
                        else
                            logger.warn("Headers should be the same for each objects in the list, excluding this object " + keyVals);
                    }

                    csvMapper.writer(schema.withColumnSeparator(getColumnSeparator(mediaType))).writeValue(entityHeaders, listOfMaps);
                } catch (Exception e) {
                    throw new IOException("Unable to retrieve flatten() " + e.getMessage());
                }
            } else {
                schema = csvMapper.schemaFor(myList.get(0).getClass()).withHeader().withColumnSeparator(getColumnSeparator(mediaType));
                csvMapper.writer(schema).writeValue(entityHeaders, myList);
            }
        } else if (myList.isEmpty()) {
            logger.warn("Nothing in list to convert to TSV....");
            entityHeaders.write(myList.toString().getBytes(StandardCharsets.UTF_8));
        } else {
            logger.error("Not in proper format must pass a java.util.List to use this MessageBodyWriter...");
            throw new IOException("Unable to obtain a valid conversion to TSV/CSV");
        }
    }

    private char getColumnSeparator(MediaType mediaType) {
        return "tab-separated-values".equals(mediaType.getSubtype()) ? '\t' : ',';
    }

    private CsvSchema buildSchemaFromKeySet(Set<String> keySet) {
        Builder build = CsvSchema.builder();
        for (String field : keySet) {
            build.addColumn(field);
        }
        return build.build().withHeader().withoutQuoteChar();
    }

    private Boolean implementsPlainTextTransformer(Class arg1) {
        Class[] interfaces = arg1.getInterfaces();
        for (Class aClass : interfaces) {
            if (aClass.getName().equals(PlainTextTransformer.class.getName()))
                return true;
        }

        return false;
    }
}
