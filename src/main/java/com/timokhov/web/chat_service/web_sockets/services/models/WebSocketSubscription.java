package com.timokhov.web.chat_service.web_sockets.services.models;

import com.timokhov.web.chat_service.utils.WebSocketsUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.stream.Stream;

public class WebSocketSubscription {

    private String id;

    private String sessionId;

    private String topic;

    private String userName;

    public WebSocketSubscription() {
    }

    public WebSocketSubscription(String id, String sessionId, String topic, String userName) {
        this.id = id;
        this.sessionId = sessionId;
        this.topic = topic;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isValid() {
        return Stream.of(this.id, this.sessionId, this.topic, this.userName)
                .allMatch(StringUtils::isNotBlank);
    }

    public static WebSocketSubscription fromStompHeaderAccessor(StompHeaderAccessor stompHeaderAccessor) {
        String subscriptionId = stompHeaderAccessor.getSubscriptionId();
        String sessionId = stompHeaderAccessor.getSessionId();
        String topic = stompHeaderAccessor.getDestination();
        String userName = stompHeaderAccessor.getFirstNativeHeader(WebSocketsUtils.USER_NAME_HEADER);

        return new WebSocketSubscription(subscriptionId, sessionId, topic, userName);
    }
}
