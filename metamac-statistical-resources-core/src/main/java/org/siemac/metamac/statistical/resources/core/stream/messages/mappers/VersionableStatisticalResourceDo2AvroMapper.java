package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.stream.messages.VersionableStatisticalResourceAvro;
import org.joda.time.DateTime;

public class VersionableStatisticalResourceDo2AvroMapper {

    protected VersionableStatisticalResourceDo2AvroMapper() {
    }

    public static VersionableStatisticalResourceAvro do2Avro(VersionableStatisticalResource source) {
        VersionableStatisticalResourceAvro target = null;
        if (source != null) {
            target = VersionableStatisticalResourceAvro.newBuilder().setNameableStatisticalResource(NameableStatisticalResourceDo2AvroMapper.do2Avro(source))
                    // TODO EDATOS-3744 PENDIENTE
                   // .setNextVersion(NextVersionTypeEnumDo2AvroMapper.do2Avro(source.getNextVersion())).setNextVersionDate(DateTimeDo2AvroMapper.do2Avro(source.getNextVersionDate()))
                    .setNextVersion(NextVersionTypeEnumDo2AvroMapper.do2Avro(source.getNextVersion())).setNextVersionDate(DateTimeDo2AvroMapper.do2Avro(new DateTime()))
                    .setValidFrom(DateTimeDo2AvroMapper.do2Avro(source.getValidFrom())).setVersionRationale(InternationalStringDo2AvroMapper.do2Avro(source.getVersionRationale()))
                    .setValidTo(DateTimeDo2AvroMapper.do2Avro(source.getValidTo())).setVersionLogic(source.getVersionLogic())
                    .setVersionRationaleTypes(VersionRationaleTypeEnumDo2AvroMapper.do2Avro(source.getVersionRationaleTypes())).build();
        }
        return target;
    }

}
