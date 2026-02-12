package org.siemac.metamac.statistical_resources.rest.external.v1_0.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    ResourceType value();

    enum ResourceType {
        DATASET,
        QUERY,
        COLLECTION,
        MULTIDATASET
    }
}

