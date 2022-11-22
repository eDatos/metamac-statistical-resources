package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.resources;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.ResourcesCriteriaPropertyOrder;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.ResourcesCriteriaPropertyRestriction;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersionProperties;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

@Component
public class ResourcesRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements ResourcesRest2DoMapper {

    private RestCriteria2SculptorCriteria<GeoCovVarElementCacheDatasetVersion> resourcesCriteriaMapper = null;

    public ResourcesRest2DoMapperImpl() {
        resourcesCriteriaMapper = new RestCriteria2SculptorCriteria<GeoCovVarElementCacheDatasetVersion>(GeoCovVarElementCacheDatasetVersion.class, ResourcesCriteriaPropertyOrder.class,
                ResourcesCriteriaPropertyRestriction.class,
                new ResourcesCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<GeoCovVarElementCacheDatasetVersion> getResourcesCriteriaMapper() {
        return resourcesCriteriaMapper;
    }

    private class ResourcesCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            ResourcesCriteriaPropertyRestriction propertyNameCriteria = ResourcesCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case URN:
                    return buildSculptorPropertyCriteria(GeoCovVarElementCacheDatasetVersionProperties.urn(), PropertyTypeEnum.STRING, propertyRestriction);
                case STATISTICAL_OPERATION_URN:
                    return buildSculptorPropertyCriteria(GeoCovVarElementCacheDatasetVersionProperties.operationUrn(), PropertyTypeEnum.STRING, propertyRestriction);
                case GEOCOV_VARELEM_ID:
                    return buildSculptorPropertyCriteria(GeoCovVarElementCacheDatasetVersionProperties.variableElement().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case IS_LAST_VERSION:
                    return buildSculptorPropertyCriteria(GeoCovVarElementCacheDatasetVersionProperties.isLastVersion(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            ResourcesCriteriaPropertyOrder propertyNameCriteria = ResourcesCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return GeoCovVarElementCacheDatasetVersionProperties.code();
                case STATISTICAL_OPERATION_ID:
                    return GeoCovVarElementCacheDatasetVersionProperties.operationCode();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return GeoCovVarElementCacheDatasetVersionProperties.code();
        }
    }
}
