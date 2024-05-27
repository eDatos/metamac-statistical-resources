package org.siemac.metamac.statistical.resources.web.shared.external;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetTemporalGranularitiesByDataset {
    @In(1)
    int                   firstResult;

    @In(2)
    int                   maxResults;

    @In(3)
    String                datasetUrn;

    @In(4)
    MetamacWebCriteria    criteria;

    @Out(1)
    List<ExternalItemDto> temporalGranularities;

    @Out(2)
    Integer               firstResultOut;

    @Out(3)
    Integer               totalResults;
}
