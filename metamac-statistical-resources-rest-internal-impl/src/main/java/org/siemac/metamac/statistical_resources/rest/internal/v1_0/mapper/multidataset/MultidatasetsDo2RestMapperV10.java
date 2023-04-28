package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.multidataset;

import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Multidatasets;

public interface MultidatasetsDo2RestMapperV10 {

    public Multidatasets toMultidatasets(PagedResult<MultidatasetVersion> sources, String agencyID, String resourceID, String query, String orderBy, Integer limit, List<String> selectedLanguages,
            Set<String> parsedFields) throws MetamacException;
    public Multidataset toMultidataset(MultidatasetVersion source, List<String> selectedLanguages, Set<String> fields) throws Exception;
    public ResourceLink toMultidatasetSelfLink(LifeCycleStatisticalResourceDto source);
    public ResourceLink toMultidatasetSelfLink(LifeCycleStatisticalResourceBaseDto source);
    public Resource toResource(MultidatasetVersion source, List<String> selectedLanguages, Set<String> parsedFields) throws MetamacException;
    public Resource toResource(RelatedResourceResult source, List<String> selectedLanguages) throws MetamacException;
}
