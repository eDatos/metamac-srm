package org.siemac.metamac.srm.rest.external.v1_0.adapter;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.sett4.dataformat.xlsx.XlsxMapper;

@Produces({"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel"})
public class XlsMessageBodyWritter extends MessageBodyWritter implements MessageBodyWriter<Object> {

    private final Logger logger = LoggerFactory.getLogger(XlsMessageBodyWritter.class);

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
        XlsxMapper mapper = new XlsxMapper();
        if (myCollectionOfObjects instanceof List && ((myList = (List<?>) myCollectionOfObjects).size() > 0)) {
            // If it's not a flat POJO must implement CSVTransformer

            if (implementsPlainTextTransformer(myList.get(0).getClass())) {
                try {

                    List<Map<String, ?>> listOfMaps = getFlattenInformation(myList);

                    mapper.writer(getSchema()).writeValue(entityHeaders, listOfMaps);

                } catch (Exception e) {
                    throw new IOException("Unable to retrieve flatten() " + e.getMessage());
                }
            } else {
                CsvSchema schema = mapper.schemaFor(myList.get(0).getClass()).withHeader();
                mapper.writer(schema).writeValue(entityHeaders, myList);

            }
        } else if (myList.isEmpty()) {
            retrieveEmptyXls(mapper, entityHeaders);
        } else {
            logger.error("Not in proper format must pass a list to use this xls mapper...");
            throw new IOException("Unable to obtain a valid conversion to xlsx");
        }
    }

    private void retrieveEmptyXls(XlsxMapper mapper, OutputStream entityHeaders) throws DatabindException, StreamWriteException, IOException {
        logger.warn("Nothing in list to convert to XLSX.... empty xlsx is returned");
        List<String> list = new ArrayList<>();
        list.add("");
        CsvSchema schema = mapper.schemaFor(list.get(0).getClass());
        mapper.writer(schema).writeValue(entityHeaders, list);
    }
}
