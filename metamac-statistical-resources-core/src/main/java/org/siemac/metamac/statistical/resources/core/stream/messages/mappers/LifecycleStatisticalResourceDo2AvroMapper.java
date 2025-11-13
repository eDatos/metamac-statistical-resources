package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.stream.messages.LifecycleStatisticalResourceAvro;

public class LifecycleStatisticalResourceDo2AvroMapper {

    protected LifecycleStatisticalResourceDo2AvroMapper() {
    }

    public static LifecycleStatisticalResourceAvro do2Avro(LifeCycleStatisticalResource source) throws MetamacException {
        LifecycleStatisticalResourceAvro target = null;
        if (source != null) {
            target = LifecycleStatisticalResourceAvro.newBuilder().setVersionableStatisticalResource(VersionableStatisticalResourceDo2AvroMapper.do2Avro(source))
                    .setFeaturedUntilDate(DateTimeDo2AvroMapper.do2Avro(source.getFeaturedUntilDate()))
                    .setNewnessUntilDate(DateTimeDo2AvroMapper.do2Avro(source.getNewnessUntilDate()))
                    .setCreationDate(DateTimeDo2AvroMapper.do2Avro(source.getCreationDate())).setCreationUser(source.getCreationUser())
                    .setProductionValidationDate(DateTimeDo2AvroMapper.do2Avro(source.getProductionValidationDate())).setProductionValidationUser(source.getProductionValidationUser())
                    .setDiffusionValidationDate(DateTimeDo2AvroMapper.do2Avro(source.getDiffusionValidationDate())).setDiffusionValidationUser(source.getDiffusionValidationUser())
                    .setRejectValidationDate(DateTimeDo2AvroMapper.do2Avro(source.getRejectValidationDate())).setRejectValidationUser(source.getRejectValidationUser())
                    .setPublicationDate(DateTimeDo2AvroMapper.do2Avro(source.getPublicationDate())).setPublicationUser(source.getPublicationUser()).setLastVersion(source.getLastVersion())
                    .setProcStatus(ProcStatusEnumDo2AvroMapper.do2Avro(source.getProcStatus())).setReplacesVersion(RelatedResourceDo2AvroMapper.do2Avro(source.getReplacesVersion()))
                    .setIsReplacedByVersion(RelatedResourceDo2AvroMapper.do2Avro(source.getIsReplacedByVersion())).setMaintainer(ExternalItemDo2AvroMapper.do2Avro(source.getMaintainer())).build();
        }
        return target;
    }

}
