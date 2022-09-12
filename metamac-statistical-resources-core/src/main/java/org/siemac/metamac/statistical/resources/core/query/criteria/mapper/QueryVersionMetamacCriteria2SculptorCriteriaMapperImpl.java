package org.siemac.metamac.statistical.resources.core.query.criteria.mapper;

import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.SculptorCriteriaConjunction;
import org.siemac.metamac.core.common.criteria.SculptorCriteriaDisjunction;
import org.siemac.metamac.core.common.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.core.common.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.common.criteria.enums.StatisticalResourcesCriteriaOrderEnum;
import org.siemac.metamac.statistical.resources.core.common.criteria.enums.StatisticalResourcesCriteriaPropertyEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionProperties;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesCriteriaUtils;
import org.springframework.stereotype.Component;

@Component
public class QueryVersionMetamacCriteria2SculptorCriteriaMapperImpl implements QueryVersionMetamacCriteria2SculptorCriteriaMapper {

    private MetamacCriteria2SculptorCriteria<QueryVersion> queryCriteriaMapper = null;

    /**************************************************************************
     * Constructor
     **************************************************************************/

    public QueryVersionMetamacCriteria2SculptorCriteriaMapperImpl() throws MetamacException {
        queryCriteriaMapper = new MetamacCriteria2SculptorCriteria<QueryVersion>(QueryVersion.class, StatisticalResourcesCriteriaOrderEnum.class, StatisticalResourcesCriteriaPropertyEnum.class,
                new QueryCriteriaCallback());
    }

    /**************************************************************************
     * Mappings
     **************************************************************************/

    @Override
    public MetamacCriteria2SculptorCriteria<QueryVersion> getQueryCriteriaMapper() {
        return queryCriteriaMapper;
    }

    /**************************************************************************
     * CallBacks classes
     *************************************************************************/

    private class QueryCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            StatisticalResourcesCriteriaPropertyEnum propertyEnum = StatisticalResourcesCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case STATISTICAL_OPERATION_URN:
                    return new SculptorPropertyCriteria(QueryVersionProperties.lifeCycleStatisticalResource().statisticalOperation().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case CODE:
                    return new SculptorPropertyCriteria(QueryVersionProperties.lifeCycleStatisticalResource().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(QueryVersionProperties.lifeCycleStatisticalResource().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case TITLE:
                    return new SculptorPropertyCriteria(QueryVersionProperties.lifeCycleStatisticalResource().title().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(QueryVersionProperties.lifeCycleStatisticalResource().description().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case VERSION_RATIONALE_TYPE:
                    return new SculptorPropertyCriteria(QueryVersionProperties.lifeCycleStatisticalResource().versionRationaleTypes().value(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case NEXT_VERSION:
                    return new SculptorPropertyCriteria(QueryVersionProperties.lifeCycleStatisticalResource().nextVersion(), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                case NEXT_VERSION_DATE:
                    return new SculptorPropertyCriteria(CriteriaUtils.getDatetimeLeafPropertyEmbedded(QueryVersionProperties.lifeCycleStatisticalResource().nextVersionDate1(), QueryVersion.class),
                            propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(QueryVersionProperties.lifeCycleStatisticalResource().procStatus(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case QUERY_STATUS:
                    return new SculptorPropertyCriteria(QueryVersionProperties.status(), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                case QUERY_TYPE:
                    return new SculptorPropertyCriteria(QueryVersionProperties.type(), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                case QUERY_RELATED_DATASET_URN: {
                    Property<QueryVersion> fixedDatasetVersionUrn = QueryVersionProperties.fixedDatasetVersion().siemacMetadataStatisticalResource().urn();
                    Property<QueryVersion> urnOfAnyVersionInDataset = QueryVersionProperties.dataset().versions().siemacMetadataStatisticalResource().urn();
                    Property<QueryVersion> lastVersionInDataset = QueryVersionProperties.dataset().versions().siemacMetadataStatisticalResource().lastVersion();
                    Property<QueryVersion> datasetVersionProcStatus = QueryVersionProperties.dataset().versions().siemacMetadataStatisticalResource().procStatus();
                    Property<QueryVersion> datasetVersionValidFrom = QueryVersionProperties.dataset().versions().siemacMetadataStatisticalResource().validFrom();

                    SculptorPropertyCriteria datasetVersionUrnCriteria = new SculptorPropertyCriteria(fixedDatasetVersionUrn, propertyRestriction.getStringValue(), OperationType.EQ);
                    SculptorPropertyCriteria isVersionOfLinkedDatasetCriteria = new SculptorPropertyCriteria(urnOfAnyVersionInDataset, propertyRestriction.getStringValue(), OperationType.EQ);

                    SculptorPropertyCriteria isLastVersionOfDatasetCriteria = new SculptorPropertyCriteria(lastVersionInDataset, Boolean.TRUE, OperationType.EQ);
                    SculptorPropertyCriteriaBase isLastPublishedVersionCriteria = StatisticalResourcesCriteriaUtils.buildPublishedVisibleCondition(datasetVersionProcStatus, datasetVersionValidFrom,
                            QueryVersion.class);

                    SculptorCriteriaDisjunction datasetLastVersionOrLastPublishedInDatasetCriteria = new SculptorCriteriaDisjunction(isLastVersionOfDatasetCriteria, isLastPublishedVersionCriteria);

                    SculptorCriteriaConjunction correspondingVersionInDatasetLinkedCriteria = new SculptorCriteriaConjunction(isVersionOfLinkedDatasetCriteria,
                            datasetLastVersionOrLastPublishedInDatasetCriteria);

                    return new SculptorCriteriaDisjunction(datasetVersionUrnCriteria, correspondingVersionInDatasetLinkedCriteria);
                }
                case LAST_VERSION:
                    return new SculptorPropertyCriteria(QueryVersionProperties.lifeCycleStatisticalResource().lastVersion(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }
        @Override
        public Property<QueryVersion> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            StatisticalResourcesCriteriaOrderEnum propertyOrderEnum = StatisticalResourcesCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return QueryVersionProperties.lifeCycleStatisticalResource().code();
                case URN:
                    return QueryVersionProperties.lifeCycleStatisticalResource().urn();
                case TITLE:
                    return QueryVersionProperties.lifeCycleStatisticalResource().title().texts().label();
                case LAST_VERSION:
                    return QueryVersionProperties.lifeCycleStatisticalResource().lastVersion();
                case PROC_STATUS:
                    return QueryVersionProperties.lifeCycleStatisticalResource().procStatus();
                case QUERY_STATUS:
                    QueryVersionProperties.status();
                case STATISTICAL_OPERATION_URN:
                    QueryVersionProperties.lifeCycleStatisticalResource().statisticalOperation().urn();
                case LAST_UPDATED:
                    return new LeafProperty<QueryVersion>(QueryVersionProperties.lifeCycleStatisticalResource().lastUpdated().getName(), CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true,
                            QueryVersion.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<QueryVersion> retrievePropertyOrderDefault() throws MetamacException {
            return new LeafProperty<QueryVersion>(QueryVersionProperties.lifeCycleStatisticalResource().lastUpdated().getName(), CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true,
                    QueryVersion.class);
        }
    }
}
