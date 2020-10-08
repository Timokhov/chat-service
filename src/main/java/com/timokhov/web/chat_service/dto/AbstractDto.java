package com.timokhov.web.chat_service.dto;

import com.timokhov.web.chat_service.utils.JsonUtils;

public abstract class AbstractDto {

    @Override
    public String toString() {
        return JsonUtils.toStringJson(this);
    }
}
