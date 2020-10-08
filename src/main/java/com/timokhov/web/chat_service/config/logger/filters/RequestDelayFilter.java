package com.timokhov.web.chat_service.config.logger.filters;

import com.timokhov.web.chat_service.config.logger.models.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestDelayFilter extends OncePerRequestFilter {

    private Logger log = new Logger(getClass());

    private String delayUrlPathMask;

    private Long delayMills;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String uri = request.getRequestURI();
        if (delayMills != null && delayMills > 0 && delayUrlPathMask != null && uri.contains(delayUrlPathMask)) {
            log.debug("doFilterInternal: request %s delayed on %s mills", uri, delayMills);
            try {
                Thread.sleep(delayMills);
            } catch (InterruptedException e) {
                log.warn(e, "doFilterInternal catch exception and continue");
            }
        }

        filterChain.doFilter(request, response);
    }

    public String getDelayUrlPathMask() {
        return delayUrlPathMask;
    }

    public void setDelayUrlPathMask(String delayUrlPathMask) {
        this.delayUrlPathMask = delayUrlPathMask;
    }

    public Long getDelayMills() {
        return delayMills;
    }

    public void setDelayMills(Long delayMills) {
        this.delayMills = delayMills;
    }
}
