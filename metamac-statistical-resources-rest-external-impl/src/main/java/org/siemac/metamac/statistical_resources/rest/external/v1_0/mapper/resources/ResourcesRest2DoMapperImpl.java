package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.resources;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResourceProperties;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourceProperties;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourcesCriteriaPropertyOrder;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourcesCriteriaPropertyRestriction;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

@Component
public class ResourcesRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements ResourcesRest2DoMapper {

    private RestCriteria2SculptorCriteria<GeoCacheResource>          geoCacheResourceCriteriaMapper         = null;
    private RestCriteria2SculptorCriteria<GeoCacheByRelatedResource> geoCacheRelatedResourcesCriteriaMapper = null;

    public ResourcesRest2DoMapperImpl() {
        geoCacheResourceCriteriaMapper = new RestCriteria2SculptorCriteria<GeoCacheResource>(GeoCacheResource.class, ResourcesCriteriaPropertyOrder.class, ResourcesCriteriaPropertyRestriction.class,
                new ResourcesCriteriaCallback());

        geoCacheRelatedResourcesCriteriaMapper = new RestCriteria2SculptorCriteria<GeoCacheByRelatedResource>(GeoCacheByRelatedResource.class, ResourcesCriteriaPropertyOrder.class,
                ResourcesCriteriaPropertyRestriction.class, new GeoCacheRelatedResourcesCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<GeoCacheResource> getResourcesCriteriaMapper() {
        return geoCacheResourceCriteriaMapper;
    }

    @Override
    public RestCriteria2SculptorCriteria<GeoCacheByRelatedResource> getGeoCacheByRelatedResourceCriteriaMapper() {
        return geoCacheRelatedResourcesCriteriaMapper;
    }

    private class ResourcesCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            ResourcesCriteriaPropertyRestriction propertyNameCriteria = ResourcesCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case URN:
                    return buildSculptorPropertyCriteria(GeoCacheResourceProperties.urn(), PropertyTypeEnum.STRING, propertyRestriction);
                case STATISTICAL_OPERATION_URN:
                    return buildSculptorPropertyCriteria(GeoCacheResourceProperties.operationUrn(), PropertyTypeEnum.STRING, propertyRestriction);
                case GEOCOV_VARELEM_ID:
                    return buildSculptorPropertyCriteria(GeoCacheResourceProperties.territories().variableElement().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case IS_LAST_VERSION:
                    return buildSculptorPropertyCriteria(GeoCacheResourceProperties.isLastVersion(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
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
                    return GeoCacheResourceProperties.code();
                case STATISTICAL_OPERATION_ID:
                    return GeoCacheResourceProperties.operationCode();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return GeoCacheResourceProperties.code();
        }
    }

    private class GeoCacheRelatedResourcesCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            ResourcesCriteriaPropertyRestriction propertyNameCriteria = ResourcesCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case URN:
                    return buildSculptorPropertyCriteria(GeoCacheByRelatedResourceProperties.urn(), PropertyTypeEnum.STRING, propertyRestriction);
                case STATISTICAL_OPERATION_URN:
                    return buildSculptorPropertyCriteria(GeoCacheByRelatedResourceProperties.operationUrn(), PropertyTypeEnum.STRING, propertyRestriction);
                case GEOCOV_VARELEM_ID:
                    return buildSculptorPropertyCriteria(GeoCacheByRelatedResourceProperties.relatedResources().geoCacheResource().territories().variableElement().code(), PropertyTypeEnum.STRING,
                            propertyRestriction);
                case IS_LAST_VERSION:
                    return buildSculptorPropertyCriteria(GeoCacheByRelatedResourceProperties.isLastVersion(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
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
                    return GeoCacheByRelatedResourceProperties.code();
                case STATISTICAL_OPERATION_ID:
                    return GeoCacheByRelatedResourceProperties.operationCode();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return GeoCacheByRelatedResourceProperties.code();
        }
    }
}
