package org.siemac.metamac.statistical.resources.core.multidataset.criteria.mapper;

import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.core.common.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.common.criteria.enums.StatisticalResourcesCriteriaOrderEnum;
import org.siemac.metamac.statistical.resources.core.common.criteria.enums.StatisticalResourcesCriteriaPropertyEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionProperties;
import org.springframework.stereotype.Component;

@Component
public class MultidatasetVersionMetamacCriteria2SculptorCriteriaMapperImpl implements MultidatasetVersionMetamacCriteria2SculptorCriteriaMapper {

    private MetamacCriteria2SculptorCriteria<MultidatasetVersion> multidatasetVersionCriteriaMapper = null;

    /**************************************************************************
     * Constructor
     **************************************************************************/

    public MultidatasetVersionMetamacCriteria2SculptorCriteriaMapperImpl() throws MetamacException {
        multidatasetVersionCriteriaMapper = new MetamacCriteria2SculptorCriteria<MultidatasetVersion>(MultidatasetVersion.class, StatisticalResourcesCriteriaOrderEnum.class,
                StatisticalResourcesCriteriaPropertyEnum.class, new MultidatasetVersionCriteriaCallback());
    }

    /**************************************************************************
     * Mappings
     **************************************************************************/

    @Override
    public MetamacCriteria2SculptorCriteria<MultidatasetVersion> getMultidatasetVersionCriteriaMapper() {
        return multidatasetVersionCriteriaMapper;
    }

    /**************************************************************************
     * CallBacks classes
     *************************************************************************/

    private class MultidatasetVersionCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            StatisticalResourcesCriteriaPropertyEnum propertyEnum = StatisticalResourcesCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case STATISTICAL_OPERATION_URN:
                    return new SculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().statisticalOperation().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case CODE:
                    return new SculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().code(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case TITLE:
                    return new SculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().title().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().description().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case VERSION_RATIONALE_TYPE:
                    return new SculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().versionRationaleTypes().value(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case NEXT_VERSION:
                    return new SculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().nextVersion(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case NEXT_VERSION_DATE:
                    return new SculptorPropertyCriteria(
                            CriteriaUtils.getDatetimeLeafPropertyEmbedded(MultidatasetVersionProperties.siemacMetadataStatisticalResource().nextVersionDate(), MultidatasetVersion.class),
                            propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().procStatus(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case PUBLICATION_STREAM_STATUS:
                    return new SculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().publicationStreamStatus(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case TITLE_ALTERNATIVE:
                    return new SculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().titleAlternative().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case KEYWORDS:
                    return new SculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().keywords().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case NEWNESS_UNTIL_DATE:
                    return new SculptorPropertyCriteria(
                            CriteriaUtils.getDatetimeLeafPropertyEmbedded(MultidatasetVersionProperties.siemacMetadataStatisticalResource().newnessUntilDate(), MultidatasetVersion.class),
                            propertyRestriction.getDateValue(), propertyRestriction.getOperationType());
                case FEATURED_UNTIL_DATE:
                    return new SculptorPropertyCriteria(
                            CriteriaUtils.getDatetimeLeafPropertyEmbedded(MultidatasetVersionProperties.siemacMetadataStatisticalResource().featuredUntilDate(), MultidatasetVersion.class),
                            propertyRestriction.getDateValue(), propertyRestriction.getOperationType());
                case LAST_VERSION:
                    return new SculptorPropertyCriteria(MultidatasetVersionProperties.siemacMetadataStatisticalResource().lastVersion(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<MultidatasetVersion> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            StatisticalResourcesCriteriaOrderEnum propertyOrderEnum = StatisticalResourcesCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return MultidatasetVersionProperties.siemacMetadataStatisticalResource().code();
                case URN:
                    return MultidatasetVersionProperties.siemacMetadataStatisticalResource().urn();
                case TITLE:
                    return MultidatasetVersionProperties.siemacMetadataStatisticalResource().title().texts().label();
                case PROC_STATUS:
                    return MultidatasetVersionProperties.siemacMetadataStatisticalResource().procStatus();
                case STATISTICAL_OPERATION_URN:
                    return MultidatasetVersionProperties.siemacMetadataStatisticalResource().statisticalOperation().urn();
                case LAST_VERSION:
                    return MultidatasetVersionProperties.siemacMetadataStatisticalResource().lastVersion();
                case LAST_UPDATED:
                    return new LeafProperty<MultidatasetVersion>(MultidatasetVersionProperties.siemacMetadataStatisticalResource().lastUpdated().getName(),
                            CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true, MultidatasetVersion.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<MultidatasetVersion> retrievePropertyOrderDefault() throws MetamacException {
            return new LeafProperty<MultidatasetVersion>(MultidatasetVersionProperties.siemacMetadataStatisticalResource().lastUpdated().getName(),
                    CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true, MultidatasetVersion.class);
        }
    }
}
