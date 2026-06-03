package org.siemac.metamac.statistical.resources.core.stream.serviceapi;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.HasLifecycle;

public interface StreamMessagingService<K, V> {

    public static final String BEAN_ID = "StreamMessagingService";

    public void sendMessage(HasLifecycle message) throws MetamacException;
}
