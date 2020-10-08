package com.timokhov.web.chat_service.dto.http;

import com.timokhov.web.chat_service.config.logger.annotations.Loggable;

@Loggable
public class DataResponse<T> extends SuccessResponse<T> {

    public DataResponse(T data) {
        super(data);
    }
}
