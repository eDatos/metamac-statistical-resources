package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.stream.messages.ExternalItemAvro;

public class ExternalItemDo2AvroMapper {

    protected ExternalItemDo2AvroMapper() {
    }

    public static ExternalItemAvro do2Avro(ExternalItem source) {
        ExternalItemAvro target = null;
        if (source != null) {
            try {
                target = ExternalItemAvro.newBuilder().setCode(source.getCode()).setCodeNested(source.getCodeNested()).setManagementAppUrl(source.getManagementAppUrl())
                        .setTitle(InternationalStringDo2AvroMapper.do2Avro(source.getTitle())).setType(TypeExternalArtefactsEnumDo2AvroMapper.do2Avro(source.getType())).setUrn(source.getUrn())
                        .setUrnProvider(source.getUrnProvider()).setSelfLink(AvroMapperUtils.getSelfLinkStructuralResources(source)).build();
            } catch (MetamacException e) {
            }
        }
        return target;

    }

    public static ExternalItemAvro do2AvroStructuralResources(ExternalItem source) {
        ExternalItemAvro target = null;
        if (source != null) {
            try {
                target = ExternalItemAvro.newBuilder().setCode(source.getCode()).setCodeNested(source.getCodeNested()).setManagementAppUrl(source.getManagementAppUrl())
                        .setTitle(InternationalStringDo2AvroMapper.do2Avro(source.getTitle())).setType(TypeExternalArtefactsEnumDo2AvroMapper.do2Avro(source.getType())).setUrn(source.getUrn())
                        .setUrnProvider(source.getUrnProvider()).setSelfLink(AvroMapperUtils.getSelfLinkStructuralResources(source)).build();
            } catch (MetamacException e) {
            }
        }
        return target;

    }

}
