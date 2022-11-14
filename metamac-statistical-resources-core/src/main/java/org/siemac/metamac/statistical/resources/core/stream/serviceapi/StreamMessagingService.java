package org.siemac.metamac.statistical.resources.core.stream.serviceapi;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;

import es.ibestat.jaxi.stream.messages.DatasetAvro;

public interface StreamMessagingService<K, V> {

    public static final String BEAN_ID = "StreamMessagingService";

    public void sendMessage(HasSiemacMetadata message) throws MetamacException;
    public void sendMessage(QueryVersion message) throws MetamacException;
    public void sendMessage(DatasetAvro message) throws MetamacException; // TODO EDATOS-3770 QUITAR
}
