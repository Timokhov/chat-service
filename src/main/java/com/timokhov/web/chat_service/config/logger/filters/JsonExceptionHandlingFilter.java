package com.timokhov.web.chat_service.config.logger.filters;

import com.timokhov.web.chat_service.dto.http.ErrorResponse;
import com.timokhov.web.chat_service.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonExceptionHandlingFilter extends OncePerRequestFilter {

    protected HttpMessageConverter<Object> jsonMessageConverter;
    protected HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    protected boolean logException = true;
    protected boolean rethrowException = false;
    protected boolean logRequest = false;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public JsonExceptionHandlingFilter(HttpMessageConverter<Object> jsonMessageConverter) {
        this.jsonMessageConverter = jsonMessageConverter;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Throwable throwable) {
            handleException(request, response, throwable);
        }
    }

    protected void handleException(HttpServletRequest request, HttpServletResponse response, Throwable throwable) throws IOException, ServletException {
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);

        response.setStatus(getStatus(request, response, throwable).value());
        jsonMessageConverter.write(getResponse(request, response, throwable), null, outputMessage);

        StringBuilder errorMessage = new StringBuilder("Exception during request processing");
        if (logRequest) {
            errorMessage.append("\n")
                    .append("Request: ")
                    .append(WebUtils.getRequestInfo(request));
        }

        if (logException) {
            if (WebUtils.isClientConnectionFailure(throwable)) {
                logger.warn(errorMessage.toString(), throwable);
            } else {
                logger.error(errorMessage.toString(), throwable);
            }
        }

        if (rethrowException) {
            if (throwable instanceof IOException) {
                throw (IOException) throwable;
            } else if (throwable instanceof ServletException) {
                throw (ServletException) throwable;
            } else {
                throw new RuntimeException(throwable);
            }
        }
    }

    protected Object getResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            Throwable throwable
    ) {
        return ErrorResponse.getInstance();
    }

    protected HttpStatus getStatus(
            HttpServletRequest request,
            HttpServletResponse response,
            Throwable throwable
    ) {
        return httpStatus;
    }

    // accessors


    public HttpMessageConverter<Object> getJsonMessageConverter() {
        return jsonMessageConverter;
    }

    public void setJsonMessageConverter(HttpMessageConverter<Object> jsonMessageConverter) {
        this.jsonMessageConverter = jsonMessageConverter;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public boolean isLogException() {
        return logException;
    }

    public void setLogException(boolean logException) {
        this.logException = logException;
    }

    public boolean isRethrowException() {
        return rethrowException;
    }

    public void setRethrowException(boolean rethrowException) {
        this.rethrowException = rethrowException;
    }

    public boolean isLogRequest() {
        return logRequest;
    }

    public void setLogRequest(boolean logRequest) {
        this.logRequest = logRequest;
    }
}
