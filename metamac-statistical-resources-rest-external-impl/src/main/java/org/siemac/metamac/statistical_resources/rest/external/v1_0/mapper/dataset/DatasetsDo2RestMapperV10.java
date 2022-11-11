package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.dataset;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Datasets;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatData;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;

public interface DatasetsDo2RestMapperV10 {

    public Datasets toDatasets(PagedResult<DatasetVersion> sources, String agencyID, String resourceID, String query, String orderBy, Integer limit, List<String> selectedLanguages);
    public Dataset toDataset(DatasetVersion source, Map<String, List<String>> dimensions, List<String> selectedLanguages, Set<String> fields) throws Exception;
    public Resource toResource(DatasetVersion source, List<String> selectedLanguages);
    public Resource toResourceAsLatest(DatasetVersion source, List<String> selectedLanguages);
    public Resource toResource(RelatedResourceResult source, List<String> selectedLanguages);
    public JsonStatData toJsonStatDataset(DatasetVersion source, Map<String, List<String>> selectedDimensions, String selectedLanguage, Set<String> parsedFields) throws Exception;
}
