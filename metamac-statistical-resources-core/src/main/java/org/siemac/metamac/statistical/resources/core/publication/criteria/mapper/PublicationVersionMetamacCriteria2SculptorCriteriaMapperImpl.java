package org.siemac.metamac.statistical.resources.core.publication.criteria.mapper;

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
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionProperties;
import org.springframework.stereotype.Component;

@Component
public class PublicationVersionMetamacCriteria2SculptorCriteriaMapperImpl implements PublicationVersionMetamacCriteria2SculptorCriteriaMapper {

    private MetamacCriteria2SculptorCriteria<PublicationVersion> publicationVersionCriteriaMapper = null;

    /**************************************************************************
     * Constructor
     **************************************************************************/

    public PublicationVersionMetamacCriteria2SculptorCriteriaMapperImpl() throws MetamacException {
        publicationVersionCriteriaMapper = new MetamacCriteria2SculptorCriteria<PublicationVersion>(PublicationVersion.class, StatisticalResourcesCriteriaOrderEnum.class,
                StatisticalResourcesCriteriaPropertyEnum.class, new PublicationVersionCriteriaCallback());
    }

    /**************************************************************************
     * Mappings
     **************************************************************************/

    @Override
    public MetamacCriteria2SculptorCriteria<PublicationVersion> getPublicationVersionCriteriaMapper() {
        return publicationVersionCriteriaMapper;
    }

    /**************************************************************************
     * CallBacks classes
     *************************************************************************/

    private class PublicationVersionCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            StatisticalResourcesCriteriaPropertyEnum propertyEnum = StatisticalResourcesCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case STATISTICAL_OPERATION_URN:
                    return new SculptorPropertyCriteria(PublicationVersionProperties.siemacMetadataStatisticalResource().statisticalOperation().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case CODE:
                    return new SculptorPropertyCriteria(PublicationVersionProperties.siemacMetadataStatisticalResource().code(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(PublicationVersionProperties.siemacMetadataStatisticalResource().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case TITLE:
                    return new SculptorPropertyCriteria(PublicationVersionProperties.siemacMetadataStatisticalResource().title().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(PublicationVersionProperties.siemacMetadataStatisticalResource().description().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case VERSION_RATIONALE_TYPE:
                    return new SculptorPropertyCriteria(PublicationVersionProperties.siemacMetadataStatisticalResource().versionRationaleTypes().value(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case NEXT_VERSION:
                    return new SculptorPropertyCriteria(PublicationVersionProperties.siemacMetadataStatisticalResource().nextVersion(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case NEXT_VERSION_DATE:
                    return new SculptorPropertyCriteria(PublicationVersionProperties.siemacMetadataStatisticalResource().nextVersionDate(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(PublicationVersionProperties.siemacMetadataStatisticalResource().procStatus(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case PUBLICATION_STREAM_STATUS:
                    return new SculptorPropertyCriteria(PublicationVersionProperties.siemacMetadataStatisticalResource().publicationStreamStatus(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case TITLE_ALTERNATIVE:
                    return new SculptorPropertyCriteria(PublicationVersionProperties.siemacMetadataStatisticalResource().titleAlternative().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case KEYWORDS:
                    return new SculptorPropertyCriteria(PublicationVersionProperties.siemacMetadataStatisticalResource().keywords().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case NEWNESS_UNTIL_DATE:
                    return new SculptorPropertyCriteria(CriteriaUtils.getDatetimeLeafPropertyEmbedded(PublicationVersionProperties.siemacMetadataStatisticalResource().newnessUntilDate(),
                            PublicationVersion.class), propertyRestriction.getDateValue(), propertyRestriction.getOperationType());
                case LAST_VERSION:
                    return new SculptorPropertyCriteria(PublicationVersionProperties.siemacMetadataStatisticalResource().lastVersion(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<PublicationVersion> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            StatisticalResourcesCriteriaOrderEnum propertyOrderEnum = StatisticalResourcesCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return PublicationVersionProperties.siemacMetadataStatisticalResource().code();
                case URN:
                    return PublicationVersionProperties.siemacMetadataStatisticalResource().urn();
                case TITLE:
                    return PublicationVersionProperties.siemacMetadataStatisticalResource().title().texts().label();
                case PROC_STATUS:
                    return PublicationVersionProperties.siemacMetadataStatisticalResource().procStatus();
                case STATISTICAL_OPERATION_URN:
                    return PublicationVersionProperties.siemacMetadataStatisticalResource().statisticalOperation().urn();
                case LAST_VERSION:
                    return PublicationVersionProperties.siemacMetadataStatisticalResource().lastVersion();
                case LAST_UPDATED:
                    return new LeafProperty<PublicationVersion>(PublicationVersionProperties.siemacMetadataStatisticalResource().lastUpdated().getName(),
                            CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true, PublicationVersion.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<PublicationVersion> retrievePropertyOrderDefault() throws MetamacException {
            return new LeafProperty<PublicationVersion>(PublicationVersionProperties.siemacMetadataStatisticalResource().lastUpdated().getName(),
                    CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true, PublicationVersion.class);
        }
    }
}
