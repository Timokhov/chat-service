package com.timokhov.web.chat_service.dto.http;

import com.timokhov.web.chat_service.config.logger.annotations.Loggable;

@Loggable
public class SuccessResponse<T> extends JsonResponse<T> {

    private static final SuccessResponse<?> INSTANCE = new SuccessResponse<>();

    public static SuccessResponse<?> getInstance() {
        return INSTANCE;
    }

    public SuccessResponse() {
        this(null);
    }

    public SuccessResponse(T data) {
        super(true, data);
    }
}
