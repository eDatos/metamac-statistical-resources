package org.siemac.metamac.statistical.resources.web.external.request.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.siemac.metamac.rest.request.filter.MutableHttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This filter allows to request a tsv, csv, xls or xlsx resource with a query parameter _type=tsv / _type=csv / _type=xls / _type=xlsx, fixing a
 * deficiency from Apache CXF, which doesn't allow modifications to the shortcuts object it uses to
 * map a name from the query parameter to a MIME type (i.e. json: application/json).
 */
public class PlainTextFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlainTextFilter.class);
    public static final String CSV_MIME_TYPE = "text/csv";
    public static final String TSV_MIME_TYPE = "text/tab-separated-values";
    public static final String XLS_MIME_TYPE = "application/vnd.ms-excel";
    public static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String REQUEST_PARAMETER_TYPE = "_type";
    public static final String TSV_PARAMETER_VALUE = "tsv";
    public static final String CSV_PARAMETER_VALUE = "csv";
    public static final String XLSX_PARAMETER_VALUE = "xlsx";
    public static final String XLS_PARAMETER_VALUE = "xls";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    private String getMimeTypeFromFormat(String type) {
        if (type != null) {
            switch (type) {
                case TSV_PARAMETER_VALUE:
                    return TSV_MIME_TYPE;
                case CSV_PARAMETER_VALUE:
                    return CSV_MIME_TYPE;
                case XLSX_PARAMETER_VALUE:
                    return XLSX_MIME_TYPE;
                case XLS_PARAMETER_VALUE:
                    return XLS_MIME_TYPE;
                default:
                    LOGGER.debug(type + " type detected on query parameter but its MIME TYPE is unknown");
                    return null;
            }
        }
        return null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String mimeType = getMimeTypeFromFormat(request.getParameter(REQUEST_PARAMETER_TYPE));
        if (mimeType != null) {
            request = getRequestWrapperForParameter(request, mimeType);
        }
        chain.doFilter(request, response);
    }

    private ServletRequest getRequestWrapperForParameter(ServletRequest request, String mimeType) {
        MutableHttpServletRequestWrapper wrapper = new MutableHttpServletRequestWrapper((HttpServletRequest) request);
        wrapper.putHeader("Accept", mimeType);
        wrapper.removeParameter(REQUEST_PARAMETER_TYPE);
        return wrapper;
    }

    @Override
    public void destroy() {
    }

}
