package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.stream.messages.VersionableStatisticalResourceAvro;

public class VersionableStatisticalResourceDo2AvroMapper {
    
    protected VersionableStatisticalResourceDo2AvroMapper() {
    }

    public static VersionableStatisticalResourceAvro do2Avro(VersionableStatisticalResource source) throws MetamacException {
        return do2Avro(source, false);
    }

    public static VersionableStatisticalResourceAvro do2Avro(VersionableStatisticalResource source, boolean reload) throws MetamacException {
        VersionableStatisticalResourceAvro target = null;
        if (source != null) {
            target = VersionableStatisticalResourceAvro.newBuilder().setNameableStatisticalResource(NameableStatisticalResourceDo2AvroMapper.do2Avro(source))
                    .setNextVersion(NextVersionTypeEnumDo2AvroMapper.do2Avro(source.getNextVersion())).setNextVersionDate(AvroMapperUtils.toSdmxObservationalTimePeriod(source.getNextVersionDate()))
                    .setValidFrom(DateTimeDo2AvroMapper.do2Avro(source.getValidFrom())).setVersionRationale(InternationalStringDo2AvroMapper.do2Avro(source.getVersionRationale()))
                    .setValidTo(DateTimeDo2AvroMapper.do2Avro(source.getValidTo())).setVersionLogic(source.getVersionLogic())
                    .setVersionRationaleTypes(VersionRationaleTypeEnumDo2AvroMapper.do2Avro(source.getVersionRationaleTypes())).setPatch(source.getPatch()).setReload(reload).build();
        }
        return target;
    }


}
