package org.siemac.metamac.statistical.resources.core.stream.serviceimpl;

import org.apache.avro.specific.SpecificRecord;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.stream.serviceapi.StreamMessagingService;
import org.siemac.metamac.statistical.resources.core.stream.serviceapi.StreamMessagingServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreamMessagingKafkaServiceFacadeImpl implements StreamMessagingServiceFacade {

    @Autowired
    protected StreamMessagingService<String, SpecificRecord> messagingService;

    @Autowired
    protected StatisticalResourcesConfiguration              statisticalResourcesConfig;

    @Override
    public void sendNewVersionPublished(HasSiemacMetadata version) throws MetamacException {
        try {
            updateMessageStatus(version, StreamMessageStatusEnum.PENDING);
            messagingService.sendMessage(version);
            updateMessageStatus(version, StreamMessageStatusEnum.SENT);
        } catch (MetamacException e) {
            updateMessageStatus(version, StreamMessageStatusEnum.FAILED);
            throw new MetamacException(e, ServiceExceptionType.UNABLE_TO_SEND_STREAM_MESSAGING_TO_STREAM_MESSAGING_SERVER);
        }
    }

    public void sendNewVersionPublished(QueryVersion queryVersion) throws MetamacException {
        try {
            updateMessageStatus(queryVersion, StreamMessageStatusEnum.PENDING);
            messagingService.sendMessage(queryVersion);
            updateMessageStatus(queryVersion, StreamMessageStatusEnum.SENT);
        } catch (MetamacException e) {
            updateMessageStatus(queryVersion, StreamMessageStatusEnum.FAILED);
            throw new MetamacException(e, ServiceExceptionType.UNABLE_TO_SEND_STREAM_MESSAGING_TO_STREAM_MESSAGING_SERVER);
        }
    }

    private void updateMessageStatus(HasSiemacMetadata version, StreamMessageStatusEnum status) {
        if (version != null) {
            version.getLifeCycleStatisticalResource().setPublicationStreamStatus(status);
        }
    }

    private void updateMessageStatus(QueryVersion queryVersion, StreamMessageStatusEnum status) {
        if (queryVersion != null) {
            queryVersion.getLifeCycleStatisticalResource().setPublicationStreamStatus(status);
        }
    }
}
