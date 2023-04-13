package org.siemac.metamac.statistical_resources.rest.external.v1_0.adapter;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
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

@Produces({"application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"})
public class XlsMessageBodyWritter extends MessageBodyWritter implements MessageBodyWriter<Object> {

    private final Logger logger = LoggerFactory.getLogger(PlainTextMessageBodyWritter.class);

    public long getSize(Object myCollectionOfObjects, Class type, Type genericType, Annotation[] annotations, MediaType arg4) {
        return 0;
    }

    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType arg3) {
        return true;
    }

    public void writeTo(Object myCollectionOfObjects, Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, OutputStream entityHeaders)
            throws IOException, WebApplicationException {
        List<?> myList = new ArrayList<>();
        if (myCollectionOfObjects instanceof List && ((myList = (List<?>) myCollectionOfObjects).size() > 0)) {
            XlsMapper xlsMapper = new XlsMapper();
            // If it's not a flat POJO must implement CSVTransformer
            if (implementsPlainTextTransformer(myList.get(0).getClass())) {

                try {
                    List<Map<String, ?>> listOfMaps = getFlattenInformation(myList);
                    xlsMapper.writeValue(listOfMaps, new ArrayList<String>(getHeaders()), entityHeaders);
                } catch (Exception e) {
                    throw new IOException("Unable to retrieve flatten() " + e.getMessage());
                }
            } else {
                logger.warn("Unable to convert to XLS/XLSX because of source format list ....");
                entityHeaders.write(myList.toString().getBytes(StandardCharsets.UTF_8));
            }
        } else if (myList.isEmpty()) {
            logger.warn("Nothing in list to convert to XLS/XLSX ....");
            entityHeaders.write(myList.toString().getBytes(StandardCharsets.UTF_8));
        } else {
            logger.error("Not in proper format must pass a list to use this xls/xlsx mapper...");
            throw new IOException("Unable to obtain a valid conversion to XLS/XLSX");
        }
    }

}
