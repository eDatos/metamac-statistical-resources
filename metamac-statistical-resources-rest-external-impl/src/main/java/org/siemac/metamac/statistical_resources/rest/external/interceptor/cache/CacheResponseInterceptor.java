package org.siemac.metamac.statistical_resources.rest.external.interceptor.cache;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class CacheResponseInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final Logger logger = LoggerFactory.getLogger(CacheResponseInterceptor.class);

    private static final int DEFAULT_MAX_AGE_SECONDS = 0;

    public CacheResponseInterceptor() {
        super(Phase.PRE_STREAM);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        try {
            Message inMessage = message.getExchange().getInMessage();
            HttpServletResponse response = (HttpServletResponse) inMessage.get(AbstractHTTPDestination.HTTP_RESPONSE);

            Date lastModified = (Date) message.getExchange().get("lastModified");
            if (lastModified == null) {
                return;
            }

            response.setDateHeader("Last-Modified", lastModified.getTime());

            String cacheControl = "max-age=" + DEFAULT_MAX_AGE_SECONDS + ", public";
            response.setHeader("Cache-Control", cacheControl);
            response.setHeader("Vary", "Accept");

        } catch (Exception e) {
            logger.warn("Error in dataset cache response interceptor", e);
        }
    }
}
