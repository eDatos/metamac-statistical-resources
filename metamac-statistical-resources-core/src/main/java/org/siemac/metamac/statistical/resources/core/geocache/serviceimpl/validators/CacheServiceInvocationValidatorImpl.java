package org.siemac.metamac.statistical.resources.core.geocache.serviceimpl.validators;

import java.util.List;

import org.apache.avro.specific.SpecificRecordBase;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;

import es.ibestat.jaxi.stream.messages.DatasetAvro;

public class CacheServiceInvocationValidatorImpl {

    // TODO EDATOS-4587 HACER

    public static void checkRetrieveQueryVersionByUrn(String urn, List<MetamacExceptionItem> exceptions) {
    }

    public static void checkFindQueryVersionByUrn(String urn, List<MetamacExceptionItem> exceptions) {
    }

    public static void checkUpdateGeoCacheResource(SiemacMetadataStatisticalResource metadataResource, LifeCycleStatisticalResource lifeCycleResource, StatisticalResourceTypeEnum type,
            boolean isLastVersionPublished, List<MetamacExceptionItem> exceptions) throws MetamacException {

    }

    public static void checkUpdateGeographicCoverageExternalPublication(SpecificRecordBase message, List<MetamacExceptionItem> exceptions) {

    }

    public static void checkUpdateAllGeoCacheResourcesByUrn(String resourceUrn, String resourceVersionUrn, List<MetamacExceptionItem> exceptions) {

    }

    public static void checkDisabledResourceByUrn(String resourceUrn, List<MetamacExceptionItem> exceptions) {

    }

    public static void checkDeleteDisabledCacheEntries(List<MetamacExceptionItem> exceptions) {

    }

    public static void checkUpdateGeoCacheExternalResource(DatasetAvro jaxiDatasetVersionAvro, InternationalString datasetTitle, List<MetamacExceptionItem> exceptions) {

    }

    public static void checkUpdateGeoCacheByRelatedResource(SiemacMetadataStatisticalResource metadataResource, LifeCycleStatisticalResource lifeCycleResource, StatisticalResourceTypeEnum type,
            boolean isLastVersionPublished, List<MetamacExceptionItem> exceptions) {

    }

    public static void checkUpdateAllGeoCacheRelatedResourcesByUrn(String resourceUrn, String resourceVersionUrn, List<MetamacExceptionItem> exceptions) {

    }

    public static void checkDisabledRelatedResourceByUrn(String relatedResourceUrn, List<MetamacExceptionItem> exceptions) {

    }

    public static void checkDeleteDisabledRelatedResourceCacheEntries(List<MetamacExceptionItem> exceptions) {

    }

    public static void checkRetrieveGeoCacheResourceByUrn(String resourceUrn, List<MetamacExceptionItem> exceptions) {

    }

    public static void checkUpdateRelatedResourceByCacheResource(GeoCacheResource geoCacheResource, List<GeoCacheResource> geoCacheResourcesDisabled,
            List<GeoCacheResource> geoCacheResourcesOldVersions, List<MetamacExceptionItem> exceptions) {

    }

    public static void checkCreateRelatedResourceByCacheResourceByUrn(GeoCacheByRelatedResource geoCacheByRelatedResource, String resourceUrn, List<MetamacExceptionItem> exceptions) {

    }

}
