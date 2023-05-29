package org.siemac.metamac.statistical.resources.core.base.components;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResourceRepository;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LifeCycleStatisticalResourceGeneratedCode {

    @Autowired
    private LifeCycleStatisticalResourceRepository siemacMetadataStatisticalResourceRepository;

    private static final Logger                    log = LoggerFactory.getLogger(LifeCycleStatisticalResourceGeneratedCode.class);

    public String fillGeneratedCodeForCreateSiemacMetadataResource(String statisticalOperatioUrn, String statisticalOperationCode)
            throws MetamacException {
        String seqCodeStr = siemacMetadataStatisticalResourceRepository.findLastUsedCodeForResourceType(statisticalOperatioUrn);
        int seqCode = 1;
        if (!StringUtils.isEmpty(seqCodeStr)) {
            try {
                seqCode = Integer.parseInt(seqCodeStr);
                seqCode++;
            } catch (NumberFormatException e) {
                log.error("Error parsing last sequential code in statistical operation " + statisticalOperationCode + " (" + seqCodeStr + ")");
                throw new MetamacException(e, CommonServiceExceptionType.UNKNOWN, "Error parsing code");
            }
        }
        if (seqCode >= 999999) {
            throwSpecificException(StatisticalResourceTypeEnum.QUERY, statisticalOperatioUrn);

        }
        return statisticalOperationCode + "_" + String.format("%06d", seqCode);
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
