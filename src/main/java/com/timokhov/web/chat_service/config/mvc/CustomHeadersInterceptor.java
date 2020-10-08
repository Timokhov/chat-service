package com.timokhov.web.chat_service.config.mvc;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomHeadersInterceptor extends HandlerInterceptorAdapter {

    protected Map<String, List<String>> headers = new HashMap<>();

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        for (Map.Entry<String, List<String>> header : headers.entrySet()) {
            List<String> values = header.getValue();
            if (values != null) {
                for (String value : values) {
                    response.addHeader(header.getKey(), value);
                }
            }
        }

        return super.preHandle(request, response, handler);
    }
}
