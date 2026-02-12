package org.siemac.metamac.statistical_resources.rest.external.interceptor.cache.request;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.siemac.metamac.statistical_resources.rest.external.interceptor.cache.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public abstract class AbstractResourceCacheRequestInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractResourceCacheRequestInterceptor.class);

    @Autowired
    private CacheHelper cacheHelper;

    public AbstractResourceCacheRequestInterceptor() {
        super(Phase.PRE_INVOKE);
    }

    abstract Date getResourceLastModified(HttpServletRequest request);

    @Override
    public void handleMessage(Message message) throws Fault {
        try {
            HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);

            String httpMethod = request.getMethod();
            if (!"GET".equals(httpMethod)) {
                return;
            }

            Date lastModified = getResourceLastModified(request);
            if (lastModified == null) {
                return;
            }

            message.getExchange().put("lastModified", lastModified);

            String ifModifiedSinceHeader = request.getHeader("If-Modified-Since");
            if (ifModifiedSinceHeader == null) {
                return;
            }

            Date clientDate = cacheHelper.parseHttpDate(ifModifiedSinceHeader);
            if (clientDate == null) {
                return;
            }

            if (!cacheHelper.isModifiedSince(lastModified, clientDate)) {
                HttpServletResponse response = (HttpServletResponse) message.get(AbstractHTTPDestination.HTTP_RESPONSE);
                if (response != null) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    response.setDateHeader("Last-Modified", lastModified.getTime());
                    response.setHeader("Vary", "Accept");
                    response.flushBuffer();

                    message.getInterceptorChain().abort();
                }
            }

        } catch (Exception e) {
            logger.warn("Error in dataset cache request interceptor", e);
        }
    }
}
