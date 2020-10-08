package com.timokhov.web.chat_service.dto.user;

import com.timokhov.web.chat_service.config.logger.annotations.Loggable;
import com.timokhov.web.chat_service.dto.AbstractDto;

@Loggable
public class User extends AbstractDto {

    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
