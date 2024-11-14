package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.multidataset;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.common.query.domain.OperationTypeEnum;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionProperties;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.MultidatasetCriteriaPropertyOrder;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.MultidatasetCriteriaPropertyRestriction;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

@Component
public class MultidatasetsRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements MultidatasetsRest2DoMapper {

    private RestCriteria2SculptorCriteria<MultidatasetVersion> multidatasetCriteriaMapper = null;

    public MultidatasetsRest2DoMapperImpl() {
        multidatasetCriteriaMapper = new RestCriteria2SculptorCriteria<MultidatasetVersion>(MultidatasetVersion.class, MultidatasetCriteriaPropertyOrder.class,
                MultidatasetCriteriaPropertyRestriction.class, new MultidatasetCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<MultidatasetVersion> getMultidatasetCriteriaMapper() {
        return multidatasetCriteriaMapper;
    }

    private class MultidatasetCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            MultidatasetCriteriaPropertyRestriction propertyNameCriteria = MultidatasetCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case NAME:
                    return buildSculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().title().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().description().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case TITLE_ALTERNATIVE:
                    return buildSculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().titleAlternative().texts().label(), PropertyTypeEnum.STRING,
                            propertyRestriction);
                case KEYWORD:
                    propertyRestriction.setOperationType(OperationTypeEnum.LIKE); // override, because keywords are a string separated by space
                    return buildSculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().keywords().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case NEWNESS_UNTIL_DATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, MultidatasetVersionProperties.siemacMetadataStatisticalResource().newnessUntilDate(),
                            MultidatasetVersion.class, false);
                case VALID_FROM:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, MultidatasetVersionProperties.siemacMetadataStatisticalResource().validTo(), MultidatasetVersion.class,
                            false);
                case VALID_TO:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, MultidatasetVersionProperties.siemacMetadataStatisticalResource().validTo(), MultidatasetVersion.class,
                            false);
                case STATISTICAL_OPERATION_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, MultidatasetVersionProperties.siemacMetadataStatisticalResource().statisticalOperation());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            MultidatasetCriteriaPropertyOrder propertyNameCriteria = MultidatasetCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return MultidatasetVersionProperties.siemacMetadataStatisticalResource().code();
                case NAME:
                	return  MultidatasetVersionProperties.siemacMetadataStatisticalResource().title().texts().label();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return MultidatasetVersionProperties.siemacMetadataStatisticalResource().code();
        }
    }

}