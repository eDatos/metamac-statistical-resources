package org.siemac.metamac.statistical.resources.core.stream.serviceimpl;

import org.apache.avro.specific.SpecificRecord;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.stream.serviceapi.StreamConsumerServiceFacade;
import org.siemac.metamac.statistical.resources.core.stream.serviceapi.StreamMessagingService;
import org.siemac.metamac.statistical.resources.web.server.stream.KafkaConsumerLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreamConsumerKafkaServiceFacadeImpl implements StreamConsumerServiceFacade {

    @Autowired
    protected StreamMessagingService<String, SpecificRecord> messagingService;

    @Autowired
    protected StatisticalResourcesConfiguration              statisticalResourcesConfig;

    @Override
    public void updateGeographicCoverageExternalPublicationVariableElementsCache(ServiceContext ctx) throws MetamacException {
        KafkaConsumerLauncher kafkaConsumerLauncher = new KafkaConsumerLauncher();
        kafkaConsumerLauncher.createCustomConsumer(ctx);
        
    }
}
