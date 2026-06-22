package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.stream.messages.ExternalItemAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.SiemacMetadataStatisticalResourceAvro;

public class SiemacMetadataStatisticalResourceDo2AvroMapper {
    protected static ConfigurationService          configurationService;
    protected SiemacMetadataStatisticalResourceDo2AvroMapper() {
    }

    public static ConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = ApplicationContextProvider.getApplicationContext().getBean(ConfigurationService.class);
        }
        return configurationService;
    }
    public static SiemacMetadataStatisticalResourceAvro do2Avro(SiemacMetadataStatisticalResource source) throws MetamacException {
        String srmApiExternalEndpoint = getConfigurationService().retrieveSrmExternalApiUrlBase();
        String commonMetadataApiInternalEndpoint = getConfigurationService().retrieveCommonMetadataExternalApiUrlBase();
        SiemacMetadataStatisticalResourceAvro target = null;
        if (source != null) {
            target = SiemacMetadataStatisticalResourceAvro.newBuilder().setLifecycleStatisticalResource(LifecycleStatisticalResourceDo2AvroMapper.do2Avro(source))
                    .setResourceCreatedDate(DateTimeDo2AvroMapper.do2Avro(source.getResourceCreatedDate()))
                    .setNewnessUntilDate(DateTimeDo2AvroMapper.do2Avro(source.getNewnessUntilDate())).setFeaturedUntilDate(DateTimeDo2AvroMapper.do2Avro(source.getFeaturedUntilDate()))
                    .setCopyrightedDate(source.getCopyrightedDate())
                    .setLanguage(ExternalItemDo2AvroMapper.do2Avro(source.getLanguage(), srmApiExternalEndpoint)).setSubtitle(InternationalStringDo2AvroMapper.do2Avro(source.getSubtitle()))
                    .setTitleAlternative(InternationalStringDo2AvroMapper.do2Avro(source.getTitleAlternative())).setAbstractLogic(InternationalStringDo2AvroMapper.do2Avro(source.getAbstractLogic()))
                    .setKeywords(InternationalStringDo2AvroMapper.do2Avro(source.getKeywords())).setCommonMetadata(ExternalItemDo2AvroMapper.do2Avro(source.getCommonMetadata(), commonMetadataApiInternalEndpoint))
                    .setCreator(ExternalItemDo2AvroMapper.do2Avro(source.getCreator(), srmApiExternalEndpoint))
                    .setConformsTo(InternationalStringDo2AvroMapper.do2Avro(source.getConformsTo())).setConformsToInternal(InternationalStringDo2AvroMapper.do2Avro(source.getConformsToInternal()))
                    .setReplaces(RelatedResourceDo2AvroMapper.do2Avro((source.getReplaces()))).setIsReplacedBy(RelatedResourceDo2AvroMapper.do2Avro(source.getIsReplacedBy()))
                    .setAccessRights(InternationalStringDo2AvroMapper.do2Avro(source.getAccessRights())).setLanguages(generateListOfLanguages(source))
                    .setStatisticalOperationInstances(generateListOfStatisticalOperationInstances(source)).setContributors(generateListOfContributors(source))
                    .setPublishers(generateListOfPublishers(source)).setPublisherContributors(generateListOfPublishersContributors(source)).setMediators(generateListOfMediators(source)).build();
        }
        return target;

    }

    protected static List<ExternalItemAvro> generateListOfLanguages(SiemacMetadataStatisticalResource source) throws MetamacException {
        String srmApiExternalEndpoint = getConfigurationService().retrieveSrmExternalApiUrlBase();
        List<ExternalItemAvro> list = generateListGeneric(source.getLanguages(), srmApiExternalEndpoint);
        return list;
    }

    protected static List<ExternalItemAvro> generateListOfStatisticalOperationInstances(SiemacMetadataStatisticalResource source) throws MetamacException {
        String operationApiExternalEndpoint = getConfigurationService().retrieveStatisticalOperationsExternalApiUrlBase();
        List<ExternalItemAvro> list = generateListGeneric(source.getStatisticalOperationInstances(), operationApiExternalEndpoint);
        return list;
    }

    protected static List<ExternalItemAvro> generateListOfContributors(SiemacMetadataStatisticalResource source) throws MetamacException {
        String srmApiExternalEndpoint = getConfigurationService().retrieveSrmExternalApiUrlBase();
        List<ExternalItemAvro> list = generateListGeneric(source.getContributor(), srmApiExternalEndpoint);
        return list;
    }

    protected static List<ExternalItemAvro> generateListOfPublishers(SiemacMetadataStatisticalResource source) throws MetamacException {
        String srmApiExternalEndpoint = getConfigurationService().retrieveSrmExternalApiUrlBase();
        List<ExternalItemAvro> list = generateListGeneric(source.getPublisher(), srmApiExternalEndpoint);
        return list;
    }

    protected static List<ExternalItemAvro> generateListOfPublishersContributors(SiemacMetadataStatisticalResource source) throws MetamacException {
        String srmApiExternalEndpoint = getConfigurationService().retrieveSrmExternalApiUrlBase();
        List<ExternalItemAvro> list = generateListGeneric(source.getPublisherContributor(), srmApiExternalEndpoint);
        return list;
    }

    protected static List<ExternalItemAvro> generateListOfMediators(SiemacMetadataStatisticalResource source) throws MetamacException {
        String srmApiExternalEndpoint = getConfigurationService().retrieveSrmExternalApiUrlBase();
        List<ExternalItemAvro> list = generateListGeneric(source.getMediator(), srmApiExternalEndpoint);
        return list;
    }

    private static List<ExternalItemAvro> generateListGeneric(List<ExternalItem> source, String apiExternalEndpoint) {
        List<ExternalItemAvro> list = new ArrayList<>();
        for (ExternalItem extItem : source) {
            list.add(ExternalItemDo2AvroMapper.do2Avro(extItem, apiExternalEndpoint));
        }
        return list;
    }

}
