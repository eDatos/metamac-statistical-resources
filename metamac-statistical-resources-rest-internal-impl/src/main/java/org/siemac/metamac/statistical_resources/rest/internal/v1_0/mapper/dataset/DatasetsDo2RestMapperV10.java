package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.dataset;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Data;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Datasets;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.domain.DsdProcessorResult;

public interface DatasetsDo2RestMapperV10 {

    public Datasets toDatasets(PagedResult<DatasetVersion> sources, String agencyID, String resourceID, String query, String orderBy, Integer limit, List<String> selectedLanguages);
    public Dataset toDataset(DatasetVersion source, Map<String, List<String>> dimensions, List<String> selectedLanguages, Set<String> fields) throws Exception;
    public ResourceLink toDatasetSelfLink(LifeCycleStatisticalResourceDto source);
    public ResourceLink toDatasetSelfLink(LifeCycleStatisticalResourceBaseDto source);
    public ResourceInternal toResource(DatasetVersion source, List<String> selectedLanguages);
    public ResourceInternal toResourceAsLatest(DatasetVersion source, List<String> selectedLanguages);
    public ResourceInternal toResource(RelatedResourceResult source, List<String> selectedLanguages);

    Data toDatasetData(DatasetVersion source, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages) throws Exception;
}
