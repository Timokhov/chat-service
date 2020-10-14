package com.timokhov.web.chat_service.dto.messages.chat;

import com.timokhov.web.chat_service.config.logger.annotations.Loggable;
import com.timokhov.web.chat_service.dto.messages.Message;
import com.timokhov.web.chat_service.dto.user.User;

@Loggable
public class ChatMessage extends Message {

    private ChatMessageType type;

    private User user;

    private String text;

    public ChatMessage() {
    }

    public ChatMessage(String id, String date, ChatMessageType type, User user, String text) {
        super(id, date);
        this.type = type;
        this.user = user;
        this.text = text;
    }

    public ChatMessageType getType() {
        return type;
    }

    public void setType(ChatMessageType type) {
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
}
