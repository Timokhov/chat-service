package com.timokhov.web.chat_service.config.logger.filters;

import com.timokhov.web.chat_service.utils.WebUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionLoggingFilter implements Filter {

    private final Log log = LogFactory.getLog(getClass());

    private boolean rethrowException = false;
    private boolean setHttpErrorStatus = true;
    private boolean logRequest = false;

    public void init(FilterConfig filterConfig) throws ServletException {
        String rethrowExceptionString = filterConfig.getInitParameter("rethrowException");
        if (rethrowExceptionString != null) {
            rethrowException = Boolean.valueOf(rethrowExceptionString);
        }

        String setHttpErrorStatusString = filterConfig.getInitParameter("setHttpErrorStatus");
        if (setHttpErrorStatusString != null) {
            setHttpErrorStatus = Boolean.valueOf(setHttpErrorStatusString);
        }

        String logRequestString = filterConfig.getInitParameter("logRequest");
        if (logRequestString != null) {
            logRequest = Boolean.valueOf(logRequestString);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Throwable e) {
            handleException(request, response, e);
        }
    }

    public void destroy() {
    }

    protected void handleException(ServletRequest request, ServletResponse response, Throwable e) throws IOException, ServletException {
        StringBuilder errorMessage = new StringBuilder("Exception during request processing");

        if (logRequest && request instanceof HttpServletRequest) {
            errorMessage.append("\n")
                    .append("Request: ")
                    .append(WebUtils.getRequestInfo((HttpServletRequest) request));
        }

        if (WebUtils.isClientConnectionFailure(e)) {
            log.warn(errorMessage.toString(), e);
        } else {
            log.error(errorMessage.toString(), e);
        }

        if (rethrowException) {
            if (e instanceof IOException) {
                throw (IOException) e;
            } else if (e instanceof ServletException) {
                throw (ServletException) e;
            } else if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }

        if (setHttpErrorStatus) {
            if (response instanceof HttpServletResponse && !response.isCommitted()) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
}
