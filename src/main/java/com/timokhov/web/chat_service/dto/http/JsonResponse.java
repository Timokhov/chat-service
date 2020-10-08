package com.timokhov.web.chat_service.dto.http;

import com.timokhov.web.chat_service.config.logger.annotations.IncludeCollectionLog;
import com.timokhov.web.chat_service.config.logger.annotations.Loggable;
import com.timokhov.web.chat_service.dto.AbstractDto;

@Loggable
public class JsonResponse<T> extends AbstractDto {

    private boolean isSuccessful;

    @IncludeCollectionLog
    private T data;

    protected JsonResponse(boolean isSuccessful) {
        this(isSuccessful, null);
    }

    protected JsonResponse(boolean isSuccessful, T data) {
        this.isSuccessful = isSuccessful;
        this.data = data;
    }

    @SuppressWarnings("UnusedDeclaration")
    public boolean getIsSuccessful() {
        return isSuccessful;
    }

    @SuppressWarnings("UnusedDeclaration")
    public T getData() {
        return data;
    }
}
