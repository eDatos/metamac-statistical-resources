package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.jsonstat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatData;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;

public interface JsonStatDo2RestMapperV10 {
    public JsonStatData toJsonStatDataset(DatasetVersion source, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages, Set<String> parsedFields) throws Exception;
    public JsonStatData toJsonStatQuery(QueryVersion source, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages, Set<String> parsedFields) throws Exception;
}
