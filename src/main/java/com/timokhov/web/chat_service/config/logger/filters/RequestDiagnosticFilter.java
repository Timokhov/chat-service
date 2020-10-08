package com.timokhov.web.chat_service.config.logger.filters;

import com.timokhov.web.chat_service.config.logger.models.Logger;
import com.timokhov.web.chat_service.utils.WebUtils;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

public class RequestDiagnosticFilter extends AbstractRequestLoggingFilter {

    protected final Logger log = new Logger(getClass());

    private boolean logBeforeMessage;

    private boolean logAfterMessage;


    @Override
    protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
        return prefix + WebUtils.getRequestInfo(request) + suffix;
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        if (log.isDebugEnabled() && logBeforeMessage) {
            log.debug(message);
        }
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        if (log.isDebugEnabled() && logAfterMessage) {
            log.debug(message);
        }
    }

    public boolean isLogBeforeMessage() {
        return logBeforeMessage;
    }

    public void setLogBeforeMessage(boolean logBeforeMessage) {
        this.logBeforeMessage = logBeforeMessage;
    }

    public boolean isLogAfterMessage() {
        return logAfterMessage;
    }

    public void setLogAfterMessage(boolean logAfterMessage) {
        this.logAfterMessage = logAfterMessage;
    }

}
