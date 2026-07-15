package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.stream.messages.PublicationVersionAvro;

public class PublicationVersionDo2AvroMapper {

    protected PublicationVersionDo2AvroMapper() {
    }

    public static PublicationVersionAvro do2Avro(PublicationVersion source) throws MetamacException {
        return do2Avro(source, false);
    }

    public static PublicationVersionAvro do2Avro(PublicationVersion source, boolean reload) throws MetamacException {
        PublicationVersionAvro target = PublicationVersionAvro.newBuilder()
                .setSiemacMetadataStatisticalResource(SiemacMetadataStatisticalResourceDo2AvroMapper.do2Avro(source.getSiemacMetadataStatisticalResource(), reload))
                .setHasPart(RelatedResourceDo2AvroMapper.do2Avro(source.getHasPart())).setPublication(PublicationDo2AvroMapper.do2Avro(source.getPublication())).build();
        return target;
    }

    protected static List<PublicationVersionAvro> PublicationVersionListDo2Avro(List<PublicationVersion> children) throws MetamacException {
        List<PublicationVersionAvro> list = new ArrayList<PublicationVersionAvro>();
        for (PublicationVersion el : children) {
            list.add(do2Avro(el));
        }
        return list;
    }

}
