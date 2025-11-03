package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.common.serviceapi.TranslationService;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetRepository;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationRepository;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.RelatedResourceAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvroMapperUtils {

    private static final Logger                    log = LoggerFactory.getLogger(AvroMapperUtils.class);

    protected static ConfigurationService          configurationService;

    protected static DatasetRepository             datasetRepository;
    protected static DatasetVersionRepository      datasetVersionRepository;

    protected static PublicationRepository         publicationRepository;
    protected static PublicationVersionRepository  publicationVersionRepository;

    protected static QueryRepository               queryRepository;
    protected static QueryVersionRepository        queryVersionRepository;

    protected static MultidatasetRepository        multidatasetRepository;
    protected static MultidatasetVersionRepository multidatasetVersionRepository;

    protected static TranslationService            translationService;

    public static TranslationService getTranslationService() {
        if (translationService == null) {
            translationService = ApplicationContextProvider.getApplicationContext().getBean(TranslationService.class);
        }
        return translationService;
    }

    public static DatasetRepository getDatasetRepository() {
        if (datasetRepository == null) {
            datasetRepository = ApplicationContextProvider.getApplicationContext().getBean(DatasetRepository.class);
        }
        return datasetRepository;
    }

    public static DatasetVersionRepository getDatasetVersionRepository() {
        if (datasetVersionRepository == null) {
            datasetVersionRepository = ApplicationContextProvider.getApplicationContext().getBean(DatasetVersionRepository.class);
        }
        return datasetVersionRepository;
    }

    public static PublicationRepository getPublicationRepository() {
        if (publicationRepository == null) {
            publicationRepository = ApplicationContextProvider.getApplicationContext().getBean(PublicationRepository.class);
        }
        return publicationRepository;
    }

    public static PublicationVersionRepository getPublicationVersionRepository() {
        if (publicationVersionRepository == null) {
            publicationVersionRepository = ApplicationContextProvider.getApplicationContext().getBean(PublicationVersionRepository.class);
        }
        return publicationVersionRepository;
    }

    // METAMAC-2715 - Realizar la notificación a Kafka de los recursos Multidataset
    // public static MultidatasetRepository getMultidatasetRepository() {
    // if (multidatasetRepository == null) {
    // multidatasetRepository = ApplicationContextProvider.getApplicationContext().getBean(MultidatasetRepository.class);
    // }
    // return multidatasetRepository;
    // }
    //
    public static MultidatasetVersionRepository getMultidatasetVersionRepository() {
        if (multidatasetVersionRepository == null) {
            multidatasetVersionRepository = ApplicationContextProvider.getApplicationContext().getBean(MultidatasetVersionRepository.class);
        }
        return multidatasetVersionRepository;
    }

    public static QueryRepository getQueryRepository() {
        if (queryRepository == null) {
            queryRepository = ApplicationContextProvider.getApplicationContext().getBean(QueryRepository.class);
        }
        return queryRepository;
    }

    public static QueryVersionRepository getQueryVersionRepository() {
        if (queryVersionRepository == null) {
            queryVersionRepository = ApplicationContextProvider.getApplicationContext().getBean(QueryVersionRepository.class);
        }
        return queryVersionRepository;
    }

    public static ConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = ApplicationContextProvider.getApplicationContext().getBean(ConfigurationService.class);
        }
        return configurationService;
    }

    public static Dataset retrieveDataset(String datasetUrn) throws MetamacException {
        Dataset target = null;
        try {
            target = getDatasetRepository().retrieveByUrn(datasetUrn);
        } catch (MetamacException e) {
            throw new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, "Dataset of Datasource not found");
        }
        return target;
    }

    public static DatasetVersion retrieveDatasetVersion(String datasetVersionUrn) throws MetamacException {
        DatasetVersion target = null;
        try {
            target = getDatasetVersionRepository().retrieveByUrn(datasetVersionUrn);
        } catch (MetamacException e) {
            throw new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, "DatasetVersion of Datasource not found");
        }
        return target;
    }

    public static PublicationVersion retrievePublicationVersion(String urn) throws MetamacException {
        PublicationVersion publicationVersion = null;
        try {
            publicationVersion = getPublicationVersionRepository().retrieveByUrn(urn);
        } catch (MetamacException e) {
            throw new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND);
        }
        return publicationVersion;
    }

    public static String getSelfLink(ExternalItem source) throws MetamacException {
        String statisticalResourcesApiInternalEndpointV10 = getConfigurationService().retrieveStatisticalResourcesInternalApiUrlBase();
        return RestUtils.createLink(statisticalResourcesApiInternalEndpointV10, source.getUri());
    }

    public static String getSelfLinkExternalItems(ExternalItem source, String apiExternalEndpoint) throws MetamacException {
        return RestUtils.createLink(apiExternalEndpoint, source.getUri());
    }


    public static RelatedResource createRelatedResourceFromRelatedResourceResult(RelatedResourceResult source) throws MetamacException {
        RelatedResource target = new RelatedResource();
        switch (source.getType()) {
            case DATASET:
                target.setDataset(getDatasetRepository().retrieveByUrn(source.getUrn()));
                target.setType(TypeRelatedResourceEnum.DATASET);
                break;
            case DATASET_VERSION:
                target.setDatasetVersion(getDatasetVersionRepository().retrieveByUrn(source.getUrn()));
                target.setType(TypeRelatedResourceEnum.DATASET_VERSION);
                break;
            case PUBLICATION:
                target.setPublication(getPublicationRepository().retrieveByUrn(source.getUrn()));
                target.setType(TypeRelatedResourceEnum.PUBLICATION);
                break;
            case PUBLICATION_VERSION:
                target.setPublicationVersion(getPublicationVersionRepository().retrieveByUrn(source.getUrn()));
                target.setType(TypeRelatedResourceEnum.PUBLICATION_VERSION);
                break;
            case QUERY:
                target.setQuery(getQueryRepository().retrieveByUrn(source.getUrn()));
                target.setType(TypeRelatedResourceEnum.QUERY);
                break;
            case QUERY_VERSION:
                target.setQueryVersion(getQueryVersionRepository().retrieveByUrn(source.getUrn()));
                target.setType(TypeRelatedResourceEnum.QUERY_VERSION);
                break;
            // METAMAC-2715 - Realizar la notificación a Kafka de los recursos Multidataset
            // case MULTIDATASET:
            // target.setMultidataset(getMultidatasetRepository().retrieveByUrn(source.getUrn()));
            // target.setType(TypeRelatedResourceEnum.MULTIDATASET);
            // break;
            // case MULTIDATASET_VERSION:
            // target.setMultidatasetVersion(getMultidatasetVersionRepository().retrieveByUrn(source.getUrn()));
            // target.setType(TypeRelatedResourceEnum.MULTIDATASET_VERSION);
            // break;
            default:
                log.debug("Dataset/Query related resource type not supported in avro publication {} urn resource: {}. Anyway, the resource will be sent to kafka.",
                        source.getType() != null ? source.getType() : "-", source.getUrn());
                return null;
        }
        return target;
    }

    public static InternationalStringAvro toSdmxObservationalTimePeriod(String sdmxValue) throws MetamacException {
        if (StringUtils.isNotBlank(sdmxValue)) {
            ServiceContext serviceContext = new ServiceContext("restInternal", "restInternal", "restInternal");
            Map<String, String> internationalStringValue = getTranslationService().retrieveTimeTranslation(serviceContext, sdmxValue);
            return InternationalStringDo2AvroMapper.mapDo2Avro(internationalStringValue);
        }
        return null;
    }

    public static List<RelatedResourceAvro> relatedResourceList2Avro(List<RelatedResourceResult> sourceList) throws MetamacException {
        List<RelatedResourceAvro> targetList = new ArrayList<RelatedResourceAvro>();
        for (RelatedResourceResult item : sourceList) {
            RelatedResource relatedResource = AvroMapperUtils.createRelatedResourceFromRelatedResourceResult(item);
            if (relatedResource != null) {
                targetList.add(RelatedResourceDo2AvroMapper.do2Avro(relatedResource));
            }
        }
        return targetList;
    }
}
