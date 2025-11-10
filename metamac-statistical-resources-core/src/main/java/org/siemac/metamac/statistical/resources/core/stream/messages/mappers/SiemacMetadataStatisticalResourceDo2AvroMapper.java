package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.stream.messages.ExternalItemAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.SiemacMetadataStatisticalResourceAvro;

public class SiemacMetadataStatisticalResourceDo2AvroMapper {

    protected SiemacMetadataStatisticalResourceDo2AvroMapper() {
    }

    public static SiemacMetadataStatisticalResourceAvro do2Avro(SiemacMetadataStatisticalResource source) throws MetamacException {
        SiemacMetadataStatisticalResourceAvro target = null;
        if (source != null) {
            target = SiemacMetadataStatisticalResourceAvro.newBuilder().setLifecycleStatisticalResource(LifecycleStatisticalResourceDo2AvroMapper.do2Avro(source))
                    .setResourceCreatedDate(DateTimeDo2AvroMapper.do2Avro(source.getResourceCreatedDate())).setLastUpdate(DateTimeDo2AvroMapper.do2Avro(source.getLastUpdate()))
                    .setNewnessUntilDate(DateTimeDo2AvroMapper.do2Avro(source.getNewnessUntilDate())).setFeaturedUntilDate(DateTimeDo2AvroMapper.do2Avro(source.getFeaturedUntilDate()))
                    .setCopyrightedDate(source.getCopyrightedDate())
                    .setLanguage(ExternalItemDo2AvroMapper.do2Avro(source.getLanguage())).setSubtitle(InternationalStringDo2AvroMapper.do2Avro(source.getSubtitle()))
                    .setTitleAlternative(InternationalStringDo2AvroMapper.do2Avro(source.getTitleAlternative())).setAbstractLogic(InternationalStringDo2AvroMapper.do2Avro(source.getAbstractLogic()))
                    .setKeywords(InternationalStringDo2AvroMapper.do2Avro(source.getKeywords())).setCommonMetadata(ExternalItemDo2AvroMapper.do2Avro(source.getCommonMetadata()))
                    .setType(StatisticalResourceTypeEnumDo2AvroMapper.do2Avro(source.getType())).setCreator(ExternalItemDo2AvroMapper.do2Avro(source.getCreator()))
                    .setConformsTo(InternationalStringDo2AvroMapper.do2Avro(source.getConformsTo())).setConformsToInternal(InternationalStringDo2AvroMapper.do2Avro(source.getConformsToInternal()))
                    .setReplaces(RelatedResourceDo2AvroMapper.do2Avro((source.getReplaces()))).setIsReplacedBy(RelatedResourceDo2AvroMapper.do2Avro(source.getIsReplacedBy()))
                    .setAccessRights(InternationalStringDo2AvroMapper.do2Avro(source.getAccessRights())).setLanguages(generateListOfLanguages(source))
                    .setStatisticalOperationInstances(generateListOfStatisticalOperationInstances(source)).setContributors(generateListOfContributors(source))
                    .setPublishers(generateListOfPublishers(source)).setPublisherContributors(generateListOfPublishersContributors(source)).setMediators(generateListOfMediators(source)).build();
        }
        return target;

    }

    protected static List<ExternalItemAvro> generateListOfLanguages(SiemacMetadataStatisticalResource source) {
        List<ExternalItemAvro> list = generateListGeneric(source.getLanguages());
        return list;
    }

    protected static List<ExternalItemAvro> generateListOfStatisticalOperationInstances(SiemacMetadataStatisticalResource source) {
        List<ExternalItemAvro> list = generateListGeneric(source.getStatisticalOperationInstances());
        return list;
    }

    protected static List<ExternalItemAvro> generateListOfContributors(SiemacMetadataStatisticalResource source) {
        List<ExternalItemAvro> list = generateListGeneric(source.getContributor());
        return list;
    }

    protected static List<ExternalItemAvro> generateListOfPublishers(SiemacMetadataStatisticalResource source) {
        List<ExternalItemAvro> list = generateListGeneric(source.getPublisher());
        return list;
    }

    protected static List<ExternalItemAvro> generateListOfPublishersContributors(SiemacMetadataStatisticalResource source) {
        List<ExternalItemAvro> list = generateListGeneric(source.getPublisherContributor());
        return list;
    }

    protected static List<ExternalItemAvro> generateListOfMediators(SiemacMetadataStatisticalResource source) {
        List<ExternalItemAvro> list = generateListGeneric(source.getMediator());
        return list;
    }

    private static List<ExternalItemAvro> generateListGeneric(List<ExternalItem> source) {
        List<ExternalItemAvro> list = new ArrayList<ExternalItemAvro>();
        for (ExternalItem extItem : source) {
            list.add(ExternalItemDo2AvroMapper.do2Avro(extItem));
        }
        return list;
    }

}
