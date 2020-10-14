package com.timokhov.web.chat_service.web_sockets.services.impl;

import com.timokhov.web.chat_service.dto.messages.typing.TypingMessage;
import com.timokhov.web.chat_service.dto.user.User;
import com.timokhov.web.chat_service.utils.UUIDUtils;
import com.timokhov.web.chat_service.utils.WebSocketsUtils;
import com.timokhov.web.chat_service.web_sockets.services.UsersTypingService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class UsersTypingServiceImpl implements UsersTypingService {

    private final Map<String, User> typingUsers = new HashMap<>();

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void userStartTyping(String sessionId, User user) {
        typingUsers.put(sessionId, user);
        generateAndSendTypingMessage(null);
    }

    @Override
    public void userStopTyping(String sessionId) {
        typingUsers.remove(sessionId);
        generateAndSendTypingMessage(null);
    }

    @Override
    public void generateAndSendTypingMessage(String sessionId) {
        TypingMessage message = new TypingMessage(
                UUIDUtils.generateUUID(),
                new DateTime().toString(),
                new ArrayList<>(typingUsers.values())
        );

        if (StringUtils.isBlank(sessionId)) {
            simpMessagingTemplate.convertAndSend(WebSocketsUtils.TYPING_TOPIC, message);
        } else {
            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                    .create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(sessionId);
            headerAccessor.setLeaveMutable(true);
            simpMessagingTemplate.convertAndSendToUser(sessionId, WebSocketsUtils.TYPING_TOPIC, message, headerAccessor.getMessageHeaders());
        }
    }
}
