package org.siemac.metamac.statistical.resources.web.shared.external;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDataProviderPaginatedList {

    @In(1)
    int                   firstResult;

    @In(2)
    int                   maxResults;

    @In(3)
    SrmItemRestCriteria criteria;

    @Out(1)
    List<ExternalItemDto> dataProviders;

    @Out(2)
    Integer               firstResultOut;

    @Out(3)
    Integer               totalResults;
}
