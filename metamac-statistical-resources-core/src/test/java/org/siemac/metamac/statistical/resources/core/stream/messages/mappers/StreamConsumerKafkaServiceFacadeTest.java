package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import org.junit.Test;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.stream.serviceapi.StreamConsumerServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;

public class StreamConsumerKafkaServiceFacadeTest {
    
    @Autowired
    private static StreamConsumerServiceFacade streamConsumerServiceFacade;


    @Test
    public void testJaxiCache() throws MetamacException{
        streamConsumerServiceFacade.updateGeographicCoverageExternalPublicationVariableElementsCache(null);
    }
}
