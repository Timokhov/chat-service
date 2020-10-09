package com.timokhov.web.chat_service.web_sockets.controllers;

import com.timokhov.web.chat_service.dto.message.Message;
import com.timokhov.web.chat_service.utils.WebSocketsUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping(ChatController.URL)
public class ChatController {

    static final String URL = "/topic/chat";

    @MessageMapping("/publish")
    @SendTo(WebSocketsUtils.CHAT_MESSAGES_TOPIC)
    public Message publishMessage(Message message) {
        return message;
    }

}
