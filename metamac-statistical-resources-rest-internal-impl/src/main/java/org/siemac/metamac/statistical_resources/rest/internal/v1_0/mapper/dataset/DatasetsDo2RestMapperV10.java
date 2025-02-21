package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.dataset;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatData;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical_resources.rest.common.impl.mappers.external.resources.ExternalRestObjectsMapper;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Datasets;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceWithStatisticalOperation;

public interface DatasetsDo2RestMapperV10 {

    public Datasets toDatasets(PagedResult<DatasetVersion> sources, String agencyID, String resourceID, String query, String orderBy, Integer limit, List<String> selectedLanguages,
            Set<String> parsedFields) throws MetamacException;
    public Dataset toDataset(DatasetVersion source, Map<String, List<String>> dimensions, List<String> selectedLanguages, Set<String> fields, String granularity) throws Exception;
    public JsonStatData toJsonStatDataset(DatasetVersion source, Map<String, List<String>> selectedDimension, String selectedLanguages, Set<String> parsedFields, String granularity) throws Exception;
    public ResourceLink toDatasetSelfLink(LifeCycleStatisticalResourceDto source);
    public ResourceLink toDatasetSelfLink(LifeCycleStatisticalResourceBaseDto source);
    public ResourceWithStatisticalOperation toResource(DatasetVersion source, List<String> selectedLanguages, Set<String> parsedFields, ExternalRestObjectsMapper externalRestObjectsMapper)
            throws MetamacException;
    public ResourceStatisticalResourceBase toResourceAsLatest(DatasetVersion source, List<String> selectedLanguages) throws MetamacException;
    public ResourceStatisticalResourceBase toResource(RelatedResourceResult source, List<String> selectedLanguages) throws MetamacException;
}
