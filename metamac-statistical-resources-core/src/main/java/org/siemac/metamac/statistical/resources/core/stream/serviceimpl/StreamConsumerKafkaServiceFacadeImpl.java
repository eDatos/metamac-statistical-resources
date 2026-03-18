package org.siemac.metamac.statistical.resources.core.stream.serviceimpl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.stream.serviceapi.StreamConsumerServiceFacade;
import org.siemac.metamac.statistical.resources.web.server.stream.KafkaConsumerLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreamConsumerKafkaServiceFacadeImpl implements StreamConsumerServiceFacade {

    @Autowired
    protected StatisticalResourcesConfiguration statisticalResourcesConfig;

    @Autowired(required = false)
    protected KafkaConsumerLauncher             kafkaConsumerLauncher;

    @Override
    public void updateGeographicCoverageExternalPublicationVariableElementsCache(ServiceContext ctx) throws MetamacException {
        if (kafkaConsumerLauncher != null) {
            kafkaConsumerLauncher.createCustomConsumer(StatisticalResourceTypeEnum.DATASET);
        } else {
            throw new MetamacException(ServiceExceptionType.TASKS_JOB_UPDATE_EXTERNAL_GEOCOVERAGE_CACHE_GET_ALL_EXTERNAL_PUBLICATION_MESSAGES_ERROR);
        }
    }

    @Override
    public void updateGeographicalCacheExternalCollections(ServiceContext ctx) throws MetamacException {
        if (kafkaConsumerLauncher != null) {
            kafkaConsumerLauncher.createCustomConsumer(StatisticalResourceTypeEnum.COLLECTION);
        } else {
            throw new MetamacException(ServiceExceptionType.TASKS_JOB_UPDATE_EXTERNAL_GEOCOVERAGE_CACHE_GET_ALL_EXTERNAL_COLLECTIONS_PUBLICATION_MESSAGES_ERROR);
        }
    }
}
