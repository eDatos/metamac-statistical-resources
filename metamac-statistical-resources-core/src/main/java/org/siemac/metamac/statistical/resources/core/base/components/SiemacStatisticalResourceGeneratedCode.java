package org.siemac.metamac.statistical.resources.core.base.components;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResourceRepository;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CategorisationSequenceRepository;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SiemacStatisticalResourceGeneratedCode {

    @Autowired
    private SiemacMetadataStatisticalResourceRepository siemacMetadataStatisticalResourceRepository;

    @Autowired
    private CategorisationSequenceRepository            categorisationSequenceRepository;

    private static final Logger                         log = LoggerFactory.getLogger(SiemacStatisticalResourceGeneratedCode.class);

    public String fillGeneratedCodeForCreateSiemacMetadataResource(SiemacMetadataStatisticalResource resource) throws MetamacException {
        ExternalItem statisticalOperation = resource.getStatisticalOperation();
        String seqCodeStr = siemacMetadataStatisticalResourceRepository.findLastUsedCodeForResourceType(statisticalOperation.getUrn(), resource.getType());
        int seqCode = 1;
        if (!StringUtils.isEmpty(seqCodeStr)) {
            try {
                seqCode = Integer.parseInt(seqCodeStr);
                seqCode++;
            } catch (NumberFormatException e) {
                log.error("Error parsing last sequential code in statistical operation " + statisticalOperation.getCode() + " (" + seqCodeStr + ")");
                throw new MetamacException(e, CommonServiceExceptionType.UNKNOWN, "Error parsing code");
            }
        }
        if (seqCode >= 999999) {
            throwSpecificException(resource.getType(), statisticalOperation.getUrn());

        }
        String code = statisticalOperation.getCode() + "_" + String.format("%06d", seqCode);
        return code;
    }

    /**
     * Generates categorisation code. This must be unique in all categorisations of maintainer.
     * The autoincremental is relative to maintainer
     */
    public String fillGeneratedCodeForCreateCategorisation(Categorisation categorisation) throws MetamacException {
        Long nextUniqueSequenceByMaintainer = categorisationSequenceRepository.generateNextSequence(categorisation.getMaintainer().getUrn());
        return StatisticalResourcesConstants.CATEGORISATION_CODE_PREFIX + nextUniqueSequenceByMaintainer;
    }

    private void throwSpecificException(StatisticalResourceTypeEnum type, String urn) throws MetamacException {
        CommonServiceExceptionType exceptionType;

        switch (type) {
            case DATASET:
                exceptionType = ServiceExceptionType.DATASET_MAX_REACHED_IN_OPERATION;
                break;
            case COLLECTION:
                exceptionType = ServiceExceptionType.PUBLICATION_MAX_REACHED_IN_OPERATION;
                break;
            case MULTIDATASET:
                exceptionType = ServiceExceptionType.MULTIDATASET_MAX_REACHED_IN_OPERATION;
                break;
            default:
                exceptionType = ServiceExceptionType.UNKNOWN;
                break;
        }
        throw new MetamacException(exceptionType, urn);
    }

}
