package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.dataset;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.common.query.domain.OperationTypeEnum;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionProperties;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DatasetCriteriaPropertyOrder;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DatasetCriteriaPropertyRestriction;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

@Component
public class DatasetsRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements DatasetsRest2DoMapper {

    private RestCriteria2SculptorCriteria<DatasetVersion> datasetCriteriaMapper = null;

    public DatasetsRest2DoMapperImpl() {
        datasetCriteriaMapper = new RestCriteria2SculptorCriteria<DatasetVersion>(DatasetVersion.class, DatasetCriteriaPropertyOrder.class, DatasetCriteriaPropertyRestriction.class,
                new DatasetCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<DatasetVersion> getDatasetCriteriaMapper() {
        return datasetCriteriaMapper;
    }

    private class DatasetCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            DatasetCriteriaPropertyRestriction propertyNameCriteria = DatasetCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(DatasetVersionProperties.siemacMetadataStatisticalResource().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case NAME:
                    return buildSculptorPropertyCriteria(DatasetVersionProperties.siemacMetadataStatisticalResource().title().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(DatasetVersionProperties.siemacMetadataStatisticalResource().description().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case GEOGRAPHIC_COVERAGE_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, DatasetVersionProperties.geographicCoverage());
                case GEOGRAPHIC_COVERAGE_TITLE:
                    return buildSculptorPropertyCriteria(DatasetVersionProperties.geographicCoverage().title().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case GEOGRAPHIC_COVERAGE_CODE:
                    return buildSculptorPropertyCriteria(DatasetVersionProperties.geographicCoverage().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case TEMPORAL_COVERAGE:
                    return buildSculptorPropertyCriteria(DatasetVersionProperties.temporalCoverage().identifier(), PropertyTypeEnum.STRING, propertyRestriction);
                case GEOGRAPHIC_GRANULARITY_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, DatasetVersionProperties.geographicGranularities());
                case TEMPORAL_GRANULARITY_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, DatasetVersionProperties.temporalGranularities());
                case DATE_START:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, DatasetVersionProperties.siemacMetadataStatisticalResource().validFrom(), DatasetVersion.class, false);
                case DATE_END:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, DatasetVersionProperties.dateStart(), DatasetVersion.class, false);
                case STATISTICAL_UNIT_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, DatasetVersionProperties.statisticalUnit());
                case MEASURE_COVERAGE_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, DatasetVersionProperties.measureCoverage());
                case RELATED_DSD_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, DatasetVersionProperties.relatedDsd());
                case DATE_NEXT_UPDATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, DatasetVersionProperties.dateNextUpdate(), DatasetVersion.class, false);
                case STATISTIC_OFFICIALITY:
                    return buildSculptorPropertyCriteria(DatasetVersionProperties.statisticOfficiality().identifier(), PropertyTypeEnum.STRING, propertyRestriction);
                case SUBTITLE:
                    return buildSculptorPropertyCriteria(DatasetVersionProperties.siemacMetadataStatisticalResource().subtitle().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case TITLE_ALTERNATIVE:
                    return buildSculptorPropertyCriteria(DatasetVersionProperties.siemacMetadataStatisticalResource().titleAlternative().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case KEYWORD:
                    propertyRestriction.setOperationType(OperationTypeEnum.LIKE); // override, because keywords are a string separated by space
                    return buildSculptorPropertyCriteria(DatasetVersionProperties.siemacMetadataStatisticalResource().keywords().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case NEWNESS_UNTIL_DATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, DatasetVersionProperties.siemacMetadataStatisticalResource().newnessUntilDate(), DatasetVersion.class,
                            false);
                case VALID_FROM:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, DatasetVersionProperties.siemacMetadataStatisticalResource().validTo(), DatasetVersion.class, false);
                case VALID_TO:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, DatasetVersionProperties.siemacMetadataStatisticalResource().validTo(), DatasetVersion.class, false);
                case STATISTICAL_OPERATION_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, DatasetVersionProperties.siemacMetadataStatisticalResource().statisticalOperation());
                case IS_LAST_VERSION:
                    return buildSculptorPropertyCriteriaForDateProperty(mapperSculptorPropertyCriteriaForValidFromIsNull(propertyRestriction), DatasetVersionProperties.siemacMetadataStatisticalResource().validTo(), DatasetVersion.class, false);
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        private MetamacRestQueryPropertyRestriction mapperSculptorPropertyCriteriaForValidFromIsNull(MetamacRestQueryPropertyRestriction propertyRestriction) {
            MetamacRestQueryPropertyRestriction propertyRestrictionForValidFromIsNull = new MetamacRestQueryPropertyRestriction();
            propertyRestrictionForValidFromIsNull.setPropertyName(DatasetVersionProperties.siemacMetadataStatisticalResource().validTo().getName());
            if (Boolean.TRUE.equals(Boolean.valueOf(propertyRestriction.getValue()))) {
                propertyRestrictionForValidFromIsNull.setOperationType(OperationTypeEnum.IS_NULL);
                return propertyRestrictionForValidFromIsNull;
            }
            propertyRestrictionForValidFromIsNull.setOperationType(OperationTypeEnum.IS_NOT_NULL);
            return propertyRestrictionForValidFromIsNull;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            DatasetCriteriaPropertyOrder propertyNameCriteria = DatasetCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return DatasetVersionProperties.siemacMetadataStatisticalResource().code();
                case NAME:
                	return DatasetVersionProperties.siemacMetadataStatisticalResource().title().texts().label();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return DatasetVersionProperties.siemacMetadataStatisticalResource().code();
        }
    }
}
