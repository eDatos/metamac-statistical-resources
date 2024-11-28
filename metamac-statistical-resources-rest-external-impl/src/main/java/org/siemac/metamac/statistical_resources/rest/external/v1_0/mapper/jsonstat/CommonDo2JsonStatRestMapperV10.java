package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.jsonstat;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatDimension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatExtension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentations;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimensions;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.DsdProcessorResult;

public interface CommonDo2JsonStatRestMapperV10 {

    String METRIC_ROLE       = "metric";
    String GEO_ROLE          = "geo";
    String TIME_ROLE         = "time";
    String JSON_STAT_VERSION = "2.0";
    String JSON_STAT_CLASS   = "dataset";

    Map<String, JsonStatDimension> toJsonStatDatasetDimensions(Dimensions dimensions, DimensionRepresentations dimensionRepresentations, DsdProcessorResult dsdProcessorResult, Attributes attributes, DataAttributes dataAttributes, String selectedLanguage) throws Exception;
    String getSelectedLanguage(DatasetVersion source, List<String> selectedLanguages);
    List<String> toJsonStatNote(DatasetVersion source, Data data, Dimensions dimensions, Attributes attributes, DsdProcessorResult dsdProcessorResult, String selectedLanguage);
    String getValueFromPosition(List<DimensionRepresentation> dimensions, List<String> values, int... position);
    List<String> getJsonStatId(Data data, DsdProcessorResult attributes);
    List<Long> toJsonStatSize(Data data, DsdProcessorResult dsdProcessorResult, Attributes attributes);
    JsonStatExtension toJsonStatExtension(DatasetVersion source, Dimensions dimensions, String selectedLanguage);
    Map<String, List<String>> toJsonStatRoles(DsdProcessorResult dsdProcessorResult) throws MetamacException;
    List<String> toJsonStatDatasetValues(Data data) throws Exception;
    String toI18nValue(InternationalString source, String selectedLanguage);
    String toI18nValue(org.siemac.metamac.statistical.resources.core.common.domain.InternationalString source, String selectedLanguage);
}
