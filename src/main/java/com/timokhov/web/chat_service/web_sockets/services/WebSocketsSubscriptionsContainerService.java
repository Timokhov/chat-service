package com.timokhov.web.chat_service.web_sockets.services;

import com.timokhov.web.chat_service.web_sockets.services.models.WebSocketSubscription;

import java.util.List;
import java.util.Optional;

public interface WebSocketsSubscriptionsContainerService {

    void addSubscription(WebSocketSubscription subscription);

    Optional<WebSocketSubscription> getSubscription(String id);

    void removeSubscription(String id);

    List<WebSocketSubscription> getSessionSubscriptions(String sessionId);

    void removeSessionSubscriptions(String sessionId);

    List<WebSocketSubscription> getSubscriptionsByTopic(String topic);

}
