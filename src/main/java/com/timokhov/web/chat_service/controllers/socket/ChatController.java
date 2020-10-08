package com.timokhov.web.chat_service.controllers.socket;

import com.timokhov.web.chat_service.dto.message.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
@MessageMapping(ChatController.URL)
public class ChatController {

    static final String URL = "/topic/chat";

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @SubscribeMapping("/messages")
    public void handleChatSubscription() {}

    @MessageMapping("/publish")
    @SendTo("/topic/chat/messages")
    public Message publishMessage(Message message) {
        return message;
    }

}
