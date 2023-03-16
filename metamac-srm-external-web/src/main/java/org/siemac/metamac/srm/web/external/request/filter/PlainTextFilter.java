package org.siemac.metamac.srm.web.external.request.filter;

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
 * This filter allows to request a tsv resource with a query parameter _type=tsv, fixing a
 * deficiency from Apache CXF, which doesn't allow modifications to the shortcuts object it uses to
 * map a name from the query parameter to a MIME type (i.e. json: application/json).
 */
public class PlainTextFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlainTextFilter.class);
    public static final String CSV_MIME_TYPE = "text/csv";
    public static final String TSV_MIME_TYPE = "text/tab-separated-values";
    public static final String REQUEST_PARAMETER_TYPE = "_type";
    public static final String TSV_PARAMETER_VALUE = "tsv";
    public static final String CSV_PARAMETER_VALUE = "csv";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String acceptType = request.getParameter(REQUEST_PARAMETER_TYPE);
        if (acceptType != null && (acceptType.equals(TSV_PARAMETER_VALUE) || acceptType.equals(CSV_PARAMETER_VALUE))) {
            String mimeType = acceptType.equals(TSV_PARAMETER_VALUE) ? TSV_MIME_TYPE : CSV_MIME_TYPE;
            LOGGER.debug(mimeType + " MIME type detected on query parameter");
            request = getRequestWrapperForParameter(request, mimeType);
        }
        chain.doFilter(request, response);
    }

    private ServletRequest getRequestWrapperForParameter(ServletRequest request, String mimeType) {
        MutableHttpServletRequestWrapper wrapper = new MutableHttpServletRequestWrapper((HttpServletRequest) request);
        wrapper.putHeader("Accept", TSV_MIME_TYPE);
        wrapper.removeParameter(REQUEST_PARAMETER_TYPE);
        return wrapper;
    }

    @Override
    public void destroy() {
    }

}
