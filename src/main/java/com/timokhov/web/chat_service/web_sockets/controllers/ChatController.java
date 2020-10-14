package com.timokhov.web.chat_service.web_sockets.controllers;

import com.timokhov.web.chat_service.dto.messages.chat.ChatMessage;
import com.timokhov.web.chat_service.dto.user.User;
import com.timokhov.web.chat_service.utils.WebSocketsUtils;
import com.timokhov.web.chat_service.web_sockets.services.UsersTypingService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
@MessageMapping(ChatController.URL)
public class ChatController {

    static final String URL = "/topic/chat/publish";

    @Resource
    private UsersTypingService usersTypingService;

    @MessageMapping("/message")
    @SendTo(WebSocketsUtils.CHAT_MESSAGES_TOPIC)
    public ChatMessage publishMessage(ChatMessage message) {
        return message;
    }

    @MessageMapping("/start-typing")
    public void startTyping(User user, SimpMessageHeaderAccessor stompHeaderAccessor) {
        usersTypingService.userStartTyping(stompHeaderAccessor.getSessionId(), user);
    }

    @MessageMapping("/stop-typing")
    public void stopTyping(SimpMessageHeaderAccessor stompHeaderAccessor) {
        usersTypingService.userStopTyping(stompHeaderAccessor.getSessionId());
    }

}
