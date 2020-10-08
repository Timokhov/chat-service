package com.timokhov.web.chat_service.dto.http;

import com.timokhov.web.chat_service.config.logger.annotations.Loggable;

@Loggable
public class ErrorResponse<T> extends JsonResponse<T> {

    private static final ErrorResponse<?> INSTANCE = new ErrorResponse<>();

    public static ErrorResponse<?> getInstance() {
        return INSTANCE;
    }

    public ErrorResponse() {
        this(null);
    }

    public ErrorResponse(T data) {
        super(false, data);
    }
}
