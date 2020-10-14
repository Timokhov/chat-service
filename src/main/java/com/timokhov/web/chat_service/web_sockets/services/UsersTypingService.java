package com.timokhov.web.chat_service.web_sockets.services;

import com.timokhov.web.chat_service.dto.user.User;

public interface UsersTypingService {

    void userStartTyping(String sessionId, User user);

    void userStopTyping(String sessionId);

    void generateAndSendTypingMessage(String sessionId);
}
