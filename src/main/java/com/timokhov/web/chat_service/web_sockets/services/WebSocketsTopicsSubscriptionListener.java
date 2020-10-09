package com.timokhov.web.chat_service.web_sockets.services;

import com.timokhov.web.chat_service.dto.message.Message;
import com.timokhov.web.chat_service.dto.message.MessageType;
import com.timokhov.web.chat_service.web_sockets.services.models.WebSocketSubscription;
import com.timokhov.web.chat_service.utils.UUIDUtils;
import com.timokhov.web.chat_service.utils.WebSocketsUtils;
import org.joda.time.DateTime;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import javax.annotation.Resource;
import java.util.List;

@Component
public class WebSocketsTopicsSubscriptionListener {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    private WebSocketsSubscriptionsContainerService webSocketsSubscriptionsContainerService;

    @EventListener
    public void onSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        WebSocketSubscription subscription = WebSocketSubscription.fromStompHeaderAccessor(stompHeaderAccessor);
        if (subscription.isValid()) {
            webSocketsSubscriptionsContainerService.addSubscription(subscription);

            if (WebSocketsUtils.CHAT_MESSAGES_TOPIC.equals(subscription.getTopic())) {
                sendUserEnterMessage(subscription);
            }
        }
    }

    @EventListener
    public void onSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String subscriptionId = stompHeaderAccessor.getSubscriptionId();
        WebSocketSubscription subscription = webSocketsSubscriptionsContainerService.getSubscription(subscriptionId).orElse(null);
        if (subscription != null) {
            if (WebSocketsUtils.CHAT_MESSAGES_TOPIC.equals(subscription.getTopic())) {
                sendUserLeaveMessage(subscription);
            }

            webSocketsSubscriptionsContainerService.removeSubscription(subscription.getId());
        }
    }

    @EventListener
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = stompHeaderAccessor.getSessionId();
        List<WebSocketSubscription> sessionSubscriptions = webSocketsSubscriptionsContainerService.getSessionSubscriptions(sessionId);
        sessionSubscriptions.forEach(subscription -> {
            if (WebSocketsUtils.CHAT_MESSAGES_TOPIC.equals(subscription.getTopic())) {
                sendUserLeaveMessage(subscription);
            }
        });
        webSocketsSubscriptionsContainerService.removeSessionSubscriptions(sessionId);
    }

    private void sendUserEnterMessage(WebSocketSubscription subscription) {
        Message userEnterMessage = new Message(
                UUIDUtils.generateUUID(),
                MessageType.SYSTEM,
                null,
                String.format("%s has entered the chat", subscription.getUserName()),
                new DateTime().toString()
        );
        simpMessagingTemplate.convertAndSend("/topic/chat/messages", userEnterMessage);
    }

    private void sendUserLeaveMessage(WebSocketSubscription subscription) {
        Message userLeaveMessage = new Message(
                UUIDUtils.generateUUID(),
                MessageType.SYSTEM,
                null,
                String.format("%s has left the chat", subscription.getUserName()),
                new DateTime().toString()
        );
        simpMessagingTemplate.convertAndSend("/topic/chat/messages", userLeaveMessage);
    }

}
