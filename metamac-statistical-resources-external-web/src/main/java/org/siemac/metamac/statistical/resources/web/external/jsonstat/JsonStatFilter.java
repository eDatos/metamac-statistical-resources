package org.siemac.metamac.statistical.resources.web.external.jsonstat;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This filter allows to request a JSON-stat resource with a query parameter _type=jsonstat, fixing a
 * deficiency from Apache CXF, which doesn't allow modifications to the shortcuts object it uses to
 * map a name from the query parameter to a MIME type (i.e. json: application/json).
 */
public class JsonStatFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonStatFilter.class);
    public static final String JSONSTAT_MIME_TYPE = "application/jsonstat+json";
    public static final String REQUEST_PARAMETER_TYPE = "_type";
    public static final String JSONSTAT_PARAMETER_VALUE = "jsonstat";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String acceptType = request.getParameter(REQUEST_PARAMETER_TYPE);
        if (acceptType != null && acceptType.equals(JSONSTAT_PARAMETER_VALUE)) {
            LOGGER.debug("JSON-stat MIME type detected on query parameter");
            request = getRequestWrapperForParameter(request);
        }
        chain.doFilter(request, response);
    }


    private ServletRequest getRequestWrapperForParameter(ServletRequest request) {
        MutableHttpServletRequestWrapper wrapper = new MutableHttpServletRequestWrapper((HttpServletRequest) request);
        wrapper.putHeader("Accept", JSONSTAT_MIME_TYPE);
        wrapper.removeParameter(REQUEST_PARAMETER_TYPE);
        return wrapper;
    }

    @Override
    public void destroy() {
    }

}
