package com.timokhov.web.chat_service.dto.message;

import com.timokhov.web.chat_service.config.logger.annotations.Loggable;
import com.timokhov.web.chat_service.dto.AbstractDto;
import com.timokhov.web.chat_service.dto.user.User;

@Loggable
public class Message extends AbstractDto {

    private String id;

    private MessageType type;

    private User user;

    private String text;

    private String date;

    public Message() {
    }

    public Message(String id, MessageType type, User user, String text, String date) {
        this.id = id;
        this.type = type;
        this.user = user;
        this.text = text;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
