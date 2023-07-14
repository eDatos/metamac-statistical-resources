package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.base;

import javax.ws.rs.core.Response.Status;

import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaDisjunction;
import org.siemac.metamac.rest.search.criteria.utils.CriteriaUtils;
import org.siemac.metamac.rest.search.criteria.utils.CriteriaUtils.PropertyValueRestToPropertyValueEntityInterface;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItemProperties.ExternalItemProperty;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ProcStatusType;
import org.siemac.metamac.statistical_resources.rest.internal.exception.RestServiceExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseRest2DoMapperV10Impl {

    private final Logger                                    logger                                 = LoggerFactory.getLogger(BaseRest2DoMapperV10Impl.class);

    private PropertyValueRestToPropertyValueEntityInterface propertyValueRestToPropertyValueEntity = null;

    protected enum PropertyTypeEnum {
        STRING, DATE, BOOLEAN, QUERY_TYPE, QUERY_STATUS, PROC_STATUS
    }

    public BaseRest2DoMapperV10Impl() {
        propertyValueRestToPropertyValueEntity = new PropertyValueRestToPropertyValueEntity();
    }

    @SuppressWarnings("rawtypes")
    protected SculptorPropertyCriteria buildSculptorPropertyCriteria(Property propertyEntity, PropertyTypeEnum propertyEntityType, MetamacRestQueryPropertyRestriction restPropertyRestriction) {
        return CriteriaUtils.buildSculptorPropertyCriteria(propertyEntity, propertyEntityType.name(), restPropertyRestriction, propertyValueRestToPropertyValueEntity);
    }

    @SuppressWarnings({"rawtypes"})
    protected SculptorPropertyCriteriaDisjunction buildSculptorPropertyCriteriaDisjunctionForUrnProperty(MetamacRestQueryPropertyRestriction propertyRestriction,
            ExternalItemProperty externalItemProperty) {
        SculptorPropertyCriteria propertyCriteria1Urn = buildSculptorPropertyCriteria(externalItemProperty.urn(), PropertyTypeEnum.STRING, propertyRestriction);
        SculptorPropertyCriteria propertyCriteria2UrnProvider = buildSculptorPropertyCriteria(externalItemProperty.urnProvider(), PropertyTypeEnum.STRING, propertyRestriction);
        return new SculptorPropertyCriteriaDisjunction(propertyCriteria1Urn, propertyCriteria2UrnProvider);
    }

    private class PropertyValueRestToPropertyValueEntity implements PropertyValueRestToPropertyValueEntityInterface {

        @Override
        public Object restValueToEntityValue(String propertyName, String value, String propertyType) {
            if (value == null) {
                return null;
            }

            try {
                PropertyTypeEnum propertyTypeEnum = PropertyTypeEnum.valueOf(propertyType);
                switch (propertyTypeEnum) {
                    case STRING:
                        return value;
                    case DATE:
                        return CoreCommonUtil.transformISODateTimeLexicalRepresentationToDateTime(value).toDate();
                    case BOOLEAN:
                        return Boolean.valueOf(value);
                    case QUERY_TYPE:
                        return toQueryType(value);
                    case QUERY_STATUS:
                        return toQueryStatus(value);
                    case PROC_STATUS:
                        return propertyRestrictionValueToProcStatusEnum(propertyName, value);
                    default:
                        throw toRestExceptionParameterIncorrect(propertyName);
                }
            } catch (Exception e) {
                logger.error("Error parsing Rest query", e);
                if (e instanceof RestException) {
                    throw (RestException) e;
                } else {
                    throw toRestExceptionParameterIncorrect(propertyName);
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected SculptorPropertyCriteria buildSculptorPropertyCriteriaForDateProperty(MetamacRestQueryPropertyRestriction propertyRestriction, Property propertyEntity, Class entity, boolean embedded) {
        String propertyName = null;
        if (embedded) {
            propertyName = ((LeafProperty) propertyEntity).getEmbeddedName();
        } else {
            propertyName = propertyEntity.getName();
        }
        return buildSculptorPropertyCriteria(new LeafProperty(propertyName, CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true, entity), PropertyTypeEnum.DATE, propertyRestriction);
    }

    protected RestException toRestExceptionParameterIncorrect(String parameter) {
        org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.PARAMETER_INCORRECT, parameter);
        throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
    }

    private QueryTypeEnum toQueryType(String source) {
        QueryTypeEnum target = QueryTypeEnum.valueOf(source);
        return target;
    }

    private QueryStatusEnum toQueryStatus(String source) {
        return QueryStatusEnum.valueOf(source);
    }

    private ProcStatusEnum propertyRestrictionValueToProcStatusEnum(String propertyName, String value) {
        ProcStatusType procStatus = ProcStatusType.valueOf(value);
        switch (procStatus) {
            case DIFFUSION_VALIDATION:
                return ProcStatusEnum.DIFFUSION_VALIDATION;
            case DRAFT:
                return ProcStatusEnum.DRAFT;
            case PRODUCTION_VALIDATION:
                return ProcStatusEnum.PRODUCTION_VALIDATION;
            case PUBLISHED:
                return ProcStatusEnum.PUBLISHED;
            case VALIDATION_REJECTED:
                return ProcStatusEnum.VALIDATION_REJECTED;
            default:
                throw toRestExceptionParameterIncorrect(propertyName);
        }
    }

}