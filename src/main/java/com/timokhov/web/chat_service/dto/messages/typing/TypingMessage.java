package com.timokhov.web.chat_service.dto.messages.typing;

import com.timokhov.web.chat_service.config.logger.annotations.Loggable;
import com.timokhov.web.chat_service.dto.messages.Message;
import com.timokhov.web.chat_service.dto.user.User;

import java.util.List;

@Loggable
public class TypingMessage extends Message {

    private List<User> typingUsers;

    public TypingMessage() {
    }

    public TypingMessage(String id, String date, List<User> typingUsers) {
        super(id, date);
        this.typingUsers = typingUsers;
    }

    public List<User> getTypingUsers() {
        return typingUsers;
    }

    public void setTypingUsers(List<User> typingUsers) {
        this.typingUsers = typingUsers;
    }
}
