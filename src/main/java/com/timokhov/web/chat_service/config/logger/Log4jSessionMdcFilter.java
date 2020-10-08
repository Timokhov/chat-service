package com.timokhov.web.chat_service.config.logger;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Log4jSessionMdcFilter extends AbstractRequestLoggingFilter {

    protected String getNestedDiagnosticContextMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? StringUtils.right(session.getId(), 5) : "";
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        ThreadContext.put("id", getNestedDiagnosticContextMessage(request));
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        ThreadContext.clearAll();
    }
}
