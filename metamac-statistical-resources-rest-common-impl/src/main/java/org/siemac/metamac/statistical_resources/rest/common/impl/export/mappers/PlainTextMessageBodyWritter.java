package org.siemac.metamac.statistical_resources.rest.common.impl.export.mappers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

@Produces({"text/tab-separated-values", "text/csv", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"})
public class PlainTextMessageBodyWritter implements MessageBodyWriter<Object> {

    public long getSize(Object myCollectionOfObjects, Class type, Type genericType, Annotation[] annotations, MediaType arg4) {
        return 0;
    }

    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType arg3) {
        return false;
    }

    public void writeTo(Object dataObject, Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, OutputStream entityHeaders)
            throws IOException, WebApplicationException {

    }
}