package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.entities;

import java.util.Map;
import java.util.Set;

public interface PlainTextTransformer {

    Map<?, ?> flatten();
    Set<String> retrieveExcludedFields(Set<String> fields);
}
