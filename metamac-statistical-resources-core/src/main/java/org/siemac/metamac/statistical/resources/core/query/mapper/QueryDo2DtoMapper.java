package org.siemac.metamac.statistical.resources.core.query.mapper;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.PurposeDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.query.domain.Purpose;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;

public interface QueryDo2DtoMapper {

    // Query
    public RelatedResourceDto queryVersionDoToQueryRelatedResourceDto(QueryVersion source) throws MetamacException;

    // Query version
    public QueryVersionDto queryVersionDoToDto(ServiceContext ctx, QueryVersion source) throws MetamacException;
    public QueryVersionBaseDto queryVersionDoToBaseDto(ServiceContext ctx, QueryVersion item) throws MetamacException;
    public List<QueryVersionBaseDto> queryVersionDoListToDtoList(ServiceContext ctx, List<QueryVersion> sources) throws MetamacException;
    public List<PurposeDto> purposeDoListToDtoList(List<Purpose> sources) throws MetamacException;
    public PurposeDto purposeDo2Dto(Purpose source);

}
