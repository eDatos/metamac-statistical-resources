package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.stream.messages.CategorisationAvro;

public class CategorisationDo2AvroMapper {

    protected CategorisationDo2AvroMapper() {
    }

    public static CategorisationAvro do2Avro(Categorisation source) throws MetamacException {
        CategorisationAvro target = CategorisationAvro.newBuilder().setCategory(ExternalItemDo2AvroMapper.do2Avro(source.getCategory())).setCreatedBy(source.getCreatedBy())
                .setCreatedDate(DateTimeDo2AvroMapper.do2Avro(source.getCreatedDate())).setDatasetVersionUrn(source.getDatasetVersion().getSiemacMetadataStatisticalResource().getUrn())
                .setLastUpdated(DateTimeDo2AvroMapper.do2Avro(source.getLastUpdated())).setLastUpdatedBy(source.getLastUpdatedBy())
                .setMaintainer(ExternalItemDo2AvroMapper.do2Avro(source.getMaintainer())).setValidFromEffective(DateTimeDo2AvroMapper.do2Avro(source.getValidFromEffective()))
                .setValidToEffective(DateTimeDo2AvroMapper.do2Avro(source.getValidToEffective())).setVersion(source.getVersion())
                .setVersionableStatisticalResource(VersionableStatisticalResourceDo2AvroMapper.do2Avro(source.getVersionableStatisticalResource())).build();
        return target;
    }

}
