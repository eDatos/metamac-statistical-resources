package org.siemac.metamac.statistical.resources.core.geocache.serviceimpl.validators;

import static org.siemac.edatos.core.common.exception.CommonServiceExceptionParameters.URN;
import static org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils.checkParameterRequired;

import java.util.List;

import org.apache.avro.specific.SpecificRecordBase;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesValidationUtils;

import es.ibestat.jaxi.stream.messages.DatasetAvro;
import es.ibestat.jaxi.stream.messages.PublicationAvro;

public class CacheServiceInvocationValidatorImpl {

    private CacheServiceInvocationValidatorImpl() {
        // without impl
    }

    public static void checkRetrieveQueryVersionByUrn(String urn, List<MetamacExceptionItem> exceptions) {
        checkParameterRequired(urn, URN, exceptions);
    }

    public static void checkFindQueryVersionByUrn(String urn, List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkUpdateGeoCacheResource(SiemacMetadataStatisticalResource metadataResource, LifeCycleStatisticalResource lifeCycleResource, StatisticalResourceTypeEnum type,
            boolean isLastVersionPublished, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // NOTHING
    }

    public static void checkUpdateGeographicCoverageExternalPublication(SpecificRecordBase message, List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkUpdateAllGeoCacheResourcesByUrn(String resourceUrn, String resourceVersionUrn, List<MetamacExceptionItem> exceptions) {
        checkParameterRequired(resourceUrn, URN, exceptions);
        checkParameterRequired(resourceVersionUrn, URN, exceptions);
    }

    public static void checkDisabledResourceByUrn(String resourceUrn, List<MetamacExceptionItem> exceptions) {
        checkParameterRequired(resourceUrn, URN, exceptions);
    }

    public static void checkDeleteDisabledCacheEntries(List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkUpdateGeoCacheExternalResource(DatasetAvro jaxiDatasetVersionAvro, InternationalString datasetTitle, List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkUpdateGeoCacheByRelatedResource(SiemacMetadataStatisticalResource metadataResource, LifeCycleStatisticalResource lifeCycleResource, StatisticalResourceTypeEnum type,
            boolean isLastVersionPublished, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkMetadataEmpty(lifeCycleResource.getCode(), ServiceExceptionParameters.CODE, exceptions);
        ValidationUtils.checkMetadataEmpty(lifeCycleResource.getVersionLogic(), ServiceExceptionParameters.DATASET_VERSION__VERSION_LOGIC, exceptions);
        ValidationUtils.checkMetadataEmpty(metadataResource.getCode(), ServiceExceptionParameters.CODE, exceptions);
        ValidationUtils.checkMetadataEmpty(metadataResource.getUrn(), ServiceExceptionParameters.URN, exceptions);
    }

    public static void checkUpdateAllGeoCacheRelatedResourcesByUrn(String resourceUrn, String resourceVersionUrn, List<MetamacExceptionItem> exceptions) {
        checkParameterRequired(resourceUrn, URN, exceptions);
        checkParameterRequired(resourceVersionUrn, URN, exceptions);
    }

    public static void checkDisabledRelatedResourceByUrn(String relatedResourceUrn, List<MetamacExceptionItem> exceptions) {
        checkParameterRequired(relatedResourceUrn, URN, exceptions);
    }

    public static void checkDeleteDisabledRelatedResourceCacheEntries(List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkRetrieveGeoCacheResourceByUrn(String resourceUrn, List<MetamacExceptionItem> exceptions) {
        checkParameterRequired(resourceUrn, URN, exceptions);
    }

    public static void checkUpdateRelatedResourceByCacheResource(GeoCacheResource geoCacheResource, List<GeoCacheResource> geoCacheResourcesDisabled,
            List<GeoCacheResource> geoCacheResourcesOldVersions, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(geoCacheResource, ServiceExceptionParameters.GEO_CACHE_RESOURCE, exceptions);
    }

    public static void checkCreateRelatedResourceByCacheResourceByUrn(GeoCacheByRelatedResource geoCacheByRelatedResource, String resourceUrn, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(geoCacheByRelatedResource, ServiceExceptionParameters.GEO_CACHE_RESOURCES_BY_RELATED_RESOURCE, exceptions);
        checkParameterRequired(resourceUrn, URN, exceptions);
    }

    public static void checkProcessGeoCacheRelatedCollection(PublicationVersion publicationVersion, boolean isLastVersionPublished, String urn, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(publicationVersion, ServiceExceptionParameters.PUBLICATION__VERSION, exceptions);
        checkParameterRequired(urn, URN, exceptions);

    }

    public static void checkProcessUpdateGeoCacheResource(LifeCycleStatisticalResource lifeCycleResource, String urnResource, StatisticalResourceTypeEnum statisticalResourceTypeEnum,
            List<ExternalItem> geographicCoverage, boolean isLastVersionPublished, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(lifeCycleResource, ServiceExceptionParameters.PUBLICATION__VERSION, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(geographicCoverage, ServiceExceptionParameters.CODE_DIMENSION__DATASET_VERSION__GEOGRAPHIC_COVERAGE, exceptions);
        checkParameterRequired(urnResource, URN, exceptions);

    }

    public static void checkFindResourcesByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkFindGeoRelatedResourcesByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkDeleteRelatedResourceOldVersions(String resourceUrn, List<MetamacExceptionItem> exceptions) {
        checkParameterRequired(resourceUrn, URN, exceptions);
    }

    public static void checkUpdateGeoCacheByRelatedResource(PublicationAvro jaxiPublicationVersionAvro, List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkUpdateCollectionExternalPublicationCache(PublicationAvro jaxiPublicationVersionAvro, List<MetamacExceptionItem> exceptions) {
        // NOTHING

    }

    public static void checkUpdateDatasetExternalPublicationCache(DatasetAvro jaxiDatasetVersionAvro, List<MetamacExceptionItem> exceptions) {
        // NOTHING

    }
}
