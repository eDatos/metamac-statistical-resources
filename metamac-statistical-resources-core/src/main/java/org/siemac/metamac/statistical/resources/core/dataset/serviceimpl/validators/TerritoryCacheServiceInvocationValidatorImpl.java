package org.siemac.metamac.statistical.resources.core.dataset.serviceimpl.validators;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;

import org.siemac.metamac.core.common.exception.MetamacExceptionItem;

import java.util.List;

public class TerritoryCacheServiceInvocationValidatorImpl {
    public static void checkFindTerritoriesByCondition(
        List<ConditionalCriteria> conditions, PagingParameter pagingParameter,
        List<MetamacExceptionItem> exceptions)
        throws org.siemac.metamac.core.common.exception.MetamacException {
    }
}
