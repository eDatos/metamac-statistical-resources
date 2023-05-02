package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.collection;

import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Collection;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Collections;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceWithStatisticalOperation;

public interface CollectionsDo2RestMapperV10 {

    public Collections toCollections(PagedResult<PublicationVersion> sources, String agencyID, String resourceID, String query, String orderBy, Integer limit, List<String> selectedLanguages,
            Set<String> parsedFields) throws MetamacException;
    public Collection toCollection(PublicationVersion source, List<String> selectedLanguages, Set<String> fields) throws Exception;
    public ResourceLink toCollectionSelfLink(LifeCycleStatisticalResourceDto source);
    public ResourceLink toCollectionSelfLink(LifeCycleStatisticalResourceBaseDto source);
    public ResourceWithStatisticalOperation toResource(PublicationVersion source, List<String> selectedLanguages, Set<String> parsedFields) throws MetamacException;
    public ResourceStatisticalResourceBase toResource(RelatedResourceResult source, List<String> selectedLanguages) throws MetamacException;
    public ResourceStatisticalResourceBase toResource(String url, List<String> selectedLanguages) throws MetamacException;
}
