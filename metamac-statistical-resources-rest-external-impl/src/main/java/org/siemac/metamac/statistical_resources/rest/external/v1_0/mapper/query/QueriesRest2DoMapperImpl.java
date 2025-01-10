package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.query;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.joda.time.DateTime;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.common.query.domain.OperationTypeEnum;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaConjunction;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaDisjunction;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionProperties;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.QueryCriteriaPropertyOrder;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.QueryCriteriaPropertyRestriction;
import org.siemac.metamac.statistical_resources.rest.external.StatisticalResourcesRestExternalConstants;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

@Component
public class QueriesRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements QueriesRest2DoMapper {

    private RestCriteria2SculptorCriteria<QueryVersion> datasetCriteriaMapper = null;

    public QueriesRest2DoMapperImpl() {
        datasetCriteriaMapper = new RestCriteria2SculptorCriteria<QueryVersion>(QueryVersion.class, QueryCriteriaPropertyOrder.class, QueryCriteriaPropertyRestriction.class,
                new QueryCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<QueryVersion> getQueryCriteriaMapper() {
        return datasetCriteriaMapper;
    }

    private class QueryCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            QueryCriteriaPropertyRestriction propertyNameCriteria = QueryCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(QueryVersionProperties.lifeCycleStatisticalResource().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case NAME:
                    return buildSculptorPropertyCriteria(QueryVersionProperties.lifeCycleStatisticalResource().title().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(QueryVersionProperties.lifeCycleStatisticalResource().description().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case RELATED_DATASET_URN:
                    if (StatisticalResourcesRestExternalConstants.IS_INTERNAL_API) {
                        return buildRelatedDatasetInternalProperty(propertyRestriction);
                    } else {
                        return buildRelatedDatasetExternalProperty(propertyRestriction);
                    }
                case TYPE:
                    return buildSculptorPropertyCriteria(QueryVersionProperties.type(), PropertyTypeEnum.QUERY_TYPE, propertyRestriction);
                case STATUS:
                    return buildSculptorPropertyCriteria(QueryVersionProperties.status(), PropertyTypeEnum.QUERY_STATUS, propertyRestriction);
                case VALID_FROM:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, QueryVersionProperties.lifeCycleStatisticalResource().validTo(), QueryVersion.class, false);
                case VALID_TO:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, QueryVersionProperties.lifeCycleStatisticalResource().validTo(), QueryVersion.class, false);
                case STATISTICAL_OPERATION_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, QueryVersionProperties.lifeCycleStatisticalResource().statisticalOperation());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        protected SculptorPropertyCriteriaBase buildRelatedDatasetInternalProperty(MetamacRestQueryPropertyRestriction propertyRestriction) {
            // Last version internal
            MetamacRestQueryPropertyRestriction propertyLastVersionCriteria = new MetamacRestQueryPropertyRestriction();
            propertyLastVersionCriteria.setOperationType(OperationTypeEnum.EQ);
            propertyLastVersionCriteria.setPropertyName(propertyRestriction.getPropertyName());
            propertyLastVersionCriteria.setValue("TRUE");
            SculptorPropertyCriteria propertyCriteriaDatasetLast = buildSculptorPropertyCriteria(QueryVersionProperties.dataset().versions().siemacMetadataStatisticalResource().lastVersion(),
                    PropertyTypeEnum.BOOLEAN, propertyLastVersionCriteria);

            // URN
            SculptorPropertyCriteria propertyUrnCriteria = buildSculptorPropertyCriteria(QueryVersionProperties.dataset().versions().siemacMetadataStatisticalResource().urn(), PropertyTypeEnum.STRING,
                    propertyRestriction);

            SculptorPropertyCriteriaConjunction sculptorPropertyCriteriaConjunction = new SculptorPropertyCriteriaConjunction(propertyCriteriaDatasetLast, propertyUrnCriteria);

            // Equals to Fixed URN
            SculptorPropertyCriteria propertyCriteriaFixed = buildSculptorPropertyCriteria(QueryVersionProperties.fixedDatasetVersion().siemacMetadataStatisticalResource().urn(),
                    PropertyTypeEnum.STRING, propertyRestriction);

            return new SculptorPropertyCriteriaDisjunction(sculptorPropertyCriteriaConjunction, propertyCriteriaFixed);
        }

        protected SculptorPropertyCriteriaBase buildRelatedDatasetExternalProperty(MetamacRestQueryPropertyRestriction propertyRestriction) {
            DateTime now = new DateTime();

            // Is Published
            SculptorPropertyCriteria propertyPublishedCriteria;
            {
                MetamacRestQueryPropertyRestriction propertyPublishedRestriction = new MetamacRestQueryPropertyRestriction();
                propertyPublishedRestriction.setOperationType(OperationTypeEnum.EQ);
                propertyPublishedRestriction.setPropertyName(propertyRestriction.getPropertyName());
                propertyPublishedRestriction.setValue(ProcStatusEnum.PUBLISHED.getName());

                propertyPublishedCriteria = buildSculptorPropertyCriteria(QueryVersionProperties.dataset().versions().siemacMetadataStatisticalResource().procStatus(), PropertyTypeEnum.STRING,
                        propertyPublishedRestriction);
            }

            // ValidFromLE
            SculptorPropertyCriteria validFromLECriteria;
            {
                MetamacRestQueryPropertyRestriction propertyPublishedRestriction = new MetamacRestQueryPropertyRestriction();
                propertyPublishedRestriction.setOperationType(OperationTypeEnum.LE);
                propertyPublishedRestriction.setPropertyName(propertyRestriction.getPropertyName());
                propertyPublishedRestriction.setValue(now.toString());

                validFromLECriteria = buildSculptorPropertyCriteriaForDateProperty(propertyPublishedRestriction, QueryVersionProperties.lifeCycleStatisticalResource().validFrom(), QueryVersion.class,
                        false);
            }

            // ValidToG
            SculptorPropertyCriteria validToGCriteria;
            {
                MetamacRestQueryPropertyRestriction propertyPublishedRestriction = new MetamacRestQueryPropertyRestriction();
                propertyPublishedRestriction.setOperationType(OperationTypeEnum.GT);
                propertyPublishedRestriction.setPropertyName(propertyRestriction.getPropertyName());
                propertyPublishedRestriction.setValue(now.toString());

                validToGCriteria = buildSculptorPropertyCriteriaForDateProperty(propertyPublishedRestriction, QueryVersionProperties.lifeCycleStatisticalResource().validTo(), QueryVersion.class,
                        false);
            }

            // ValidToIsNull
            SculptorPropertyCriteria validToIsNullCriteria;
            {
                MetamacRestQueryPropertyRestriction propertyPublishedRestriction = new MetamacRestQueryPropertyRestriction();
                propertyPublishedRestriction.setOperationType(OperationTypeEnum.IS_NULL);
                propertyPublishedRestriction.setPropertyName(propertyRestriction.getPropertyName());
                propertyPublishedRestriction.setValue(now.toString());

                validToIsNullCriteria = buildSculptorPropertyCriteriaForDateProperty(propertyPublishedRestriction, QueryVersionProperties.lifeCycleStatisticalResource().validTo(), QueryVersion.class,
                        false);
            }

            // DatasetVersion URN
            SculptorPropertyCriteria propertyUrnCriteria;
            {
                propertyUrnCriteria = buildSculptorPropertyCriteria(QueryVersionProperties.dataset().versions().siemacMetadataStatisticalResource().urn(), PropertyTypeEnum.STRING,
                        propertyRestriction);
            }

            // Fixed DatasetVersion URN
            SculptorPropertyCriteria propertyCriteriaFixed;
            {
                propertyCriteriaFixed = buildSculptorPropertyCriteria(QueryVersionProperties.fixedDatasetVersion().siemacMetadataStatisticalResource().urn(), PropertyTypeEnum.STRING,
                        propertyRestriction);
            }

            // -------> IMPORTANT: The following code must build the conditions tree in the same order of the conditions tree in test:
            // QueryVersionRepositoryTest.testRetrieveRelatedUrnQueryForExternalAPI (core). Another untested order can produce incorrect results.
            SculptorPropertyCriteriaDisjunction disjunctionUrnToCriteria = new SculptorPropertyCriteriaDisjunction(propertyUrnCriteria, propertyCriteriaFixed);
            SculptorPropertyCriteriaDisjunction disjunctionValidToCriteria = new SculptorPropertyCriteriaDisjunction(validToGCriteria, validToIsNullCriteria);
            SculptorPropertyCriteriaConjunction conjunctionValidCriteria = new SculptorPropertyCriteriaConjunction(validFromLECriteria, propertyPublishedCriteria);
            SculptorPropertyCriteriaConjunction conjuctionPublishedCriteria = new SculptorPropertyCriteriaConjunction(disjunctionValidToCriteria, conjunctionValidCriteria);
            // <--------

            return new SculptorPropertyCriteriaConjunction(disjunctionUrnToCriteria, conjuctionPublishedCriteria);
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            QueryCriteriaPropertyOrder propertyNameCriteria = QueryCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return QueryVersionProperties.lifeCycleStatisticalResource().code();
                case NAME:
                	return QueryVersionProperties.lifeCycleStatisticalResource().title().texts().label();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return QueryVersionProperties.lifeCycleStatisticalResource().code();
        }
    }
}