package org.siemac.metamac.statistical_resources.rest.external.v1_0.adapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.entities.PlainTextTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;

public abstract class MessageBodyWritter {

    private final Logger logger = LoggerFactory.getLogger(MessageBodyWritter.class);

    private CsvSchema schema;
    private Set<String> headers;

    protected List<Map<String, ?>> getFlattenInformation(List<?> myList) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class[] params = {};
        Method meth = PlainTextTransformer.class.getDeclaredMethod("flatten", params);

        List<Map<String, ?>> listOfMaps = new ArrayList<>();
        headers = null;
        schema = null;
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
        return listOfMaps;
    }

    private CsvSchema buildSchemaFromKeySet(Set<String> keySet) {
        Builder build = CsvSchema.builder();
        for (String field : keySet) {
            build.addColumn(field);
        }
        return build.build().withHeader().withoutQuoteChar();
    }

    protected Boolean implementsPlainTextTransformer(Class arg1) {
        Class[] interfaces = arg1.getInterfaces();
        for (Class aClass : interfaces) {
            if (aClass.getName().equals(PlainTextTransformer.class.getName()))
                return true;
        }

        return false;
    }

    protected CsvSchema getSchema() {
        return schema;
    }

    protected Set<String> getHeaders() {
        return headers;
    }
}
