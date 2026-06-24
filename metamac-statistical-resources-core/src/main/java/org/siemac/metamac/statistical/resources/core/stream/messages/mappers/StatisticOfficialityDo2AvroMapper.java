package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.stream.messages.StatisticOfficialityAvro;

public class StatisticOfficialityDo2AvroMapper {

    protected StatisticOfficialityDo2AvroMapper() {
    }

    public static StatisticOfficialityAvro do2Avro(StatisticOfficiality source) {
        if (source == null) {
            return null;
        }
        StatisticOfficialityAvro target = StatisticOfficialityAvro.newBuilder().setDescription(InternationalStringDo2AvroMapper.do2Avro(source.getDescription())).setIdentifier(source.getIdentifier())
                .build();
        return target;
    }
}
