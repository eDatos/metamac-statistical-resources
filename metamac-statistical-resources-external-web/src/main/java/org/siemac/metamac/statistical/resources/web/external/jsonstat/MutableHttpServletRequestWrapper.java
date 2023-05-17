package org.siemac.metamac.statistical.resources.web.external.jsonstat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public final class MutableHttpServletRequestWrapper extends HttpServletRequestWrapper {

    // holds custom header and value mapping
    private final Map<String, String> customHeaders = new HashMap<>();
    private final List<String> removedParameters = new ArrayList<>();

    public MutableHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        // check the custom headers first
        String headerValue = customHeaders.get(name);

        if (headerValue != null) {
            return headerValue;
        }
        // else return from into the original wrapped object
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    public Enumeration<String> getHeaderNames() {
        // create a set of the custom header names
        Set<String> set = new HashSet<>(customHeaders.keySet());

        // now add the headers from the wrapped request object
        @SuppressWarnings("unchecked")
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            // add the names of the request headers into the list
            String n = e.nextElement();
            set.add(n);
        }

        // create an enumeration from the set and return
        return Collections.enumeration(set);
    }

    @Override
    public String getQueryString() {
        String qs = super.getQueryString();
        for (String removedParameter : removedParameters) {
            qs = qs.replaceAll(removedParameter + "=[^&]*+", "");
        }
        return qs;
    }

    @Override
    public String getParameter(String paramName) {
        String value = super.getParameter(paramName);
        if (removedParameters.contains(paramName)) {
            value = null;
        }
        return value;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> originalParams = super.getParameterMap();
         for (String removedParameter : removedParameters) {
            originalParams.remove(removedParameter);
         }
        return originalParams;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(getParameterMap().keySet());
    }

    @Override
    public String[] getParameterValues(String paramName) {
        String[] values = super.getParameterValues(paramName);
        if (removedParameters.contains(paramName)) {
            values = null;
        }
        return values;
    }

    public void removeParameter(String parameterName) {
        removedParameters.add(parameterName);
    }
}
