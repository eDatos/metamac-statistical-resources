package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.serviceapi.TranslationService;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.VersionableStatisticalResourceAvro;
import org.springframework.beans.factory.annotation.Autowired;

public class VersionableStatisticalResourceDo2AvroMapper {

    public static final ServiceContext SERVICE_CONTEXT          = new ServiceContext("restInternal", "restInternal", "restInternal");
    
    private static TranslationService translationService;
    
    @Autowired
    protected VersionableStatisticalResourceDo2AvroMapper(TranslationService translationService) {
        VersionableStatisticalResourceDo2AvroMapper.translationService = translationService;
    }

    public static VersionableStatisticalResourceAvro do2Avro(VersionableStatisticalResource source) throws MetamacException {
        VersionableStatisticalResourceAvro target = null;
        if (source != null) {
            target = VersionableStatisticalResourceAvro.newBuilder().setNameableStatisticalResource(NameableStatisticalResourceDo2AvroMapper.do2Avro(source))
                    .setNextVersion(NextVersionTypeEnumDo2AvroMapper.do2Avro(source.getNextVersion())).setNextVersionDate(toSdmxObservationalTimePeriod(source.getNextVersionDate()))
                    .setValidFrom(DateTimeDo2AvroMapper.do2Avro(source.getValidFrom())).setVersionRationale(InternationalStringDo2AvroMapper.do2Avro(source.getVersionRationale()))
                    .setValidTo(DateTimeDo2AvroMapper.do2Avro(source.getValidTo())).setVersionLogic(source.getVersionLogic())
                    .setVersionRationaleTypes(VersionRationaleTypeEnumDo2AvroMapper.do2Avro(source.getVersionRationaleTypes())).build();
        }
        return target;
    }
    
    public static InternationalStringAvro toSdmxObservationalTimePeriod(String sdmxValue) throws MetamacException {
        if (StringUtils.isNotBlank(sdmxValue)) {
            Map<String, String> internationalStringValue = translationService.retrieveTimeTranslation(SERVICE_CONTEXT, sdmxValue);
            return InternationalStringDo2AvroMapper.mapDo2Avro(internationalStringValue);
        }
        return null;
    }

}
