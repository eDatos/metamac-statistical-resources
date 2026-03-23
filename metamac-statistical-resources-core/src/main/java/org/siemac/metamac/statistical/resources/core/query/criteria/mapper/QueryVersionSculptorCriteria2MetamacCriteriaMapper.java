package org.siemac.metamac.statistical.resources.core.query.criteria.mapper;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;

public interface QueryVersionSculptorCriteria2MetamacCriteriaMapper {

    public MetamacCriteriaResult<QueryVersionBaseDto> pageResultToMetamacCriteriaResultQuery(ServiceContext ctx, PagedResult<QueryVersion> source, Integer pageSize) throws MetamacException;

}
