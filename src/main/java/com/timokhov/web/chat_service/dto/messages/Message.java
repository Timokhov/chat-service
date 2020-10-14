package com.timokhov.web.chat_service.dto.messages;

import com.timokhov.web.chat_service.config.logger.annotations.Loggable;
import com.timokhov.web.chat_service.dto.AbstractDto;
import com.timokhov.web.chat_service.dto.user.User;

@Loggable
public class Message extends AbstractDto {

    private String id;

    private String date;

    public Message() {
    }

    public Message(String id, String date) {
        this.id = id;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
