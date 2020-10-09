package com.timokhov.web.chat_service.web_sockets.services.impl;


import com.timokhov.web.chat_service.web_sockets.services.WebSocketsSubscriptionsContainerService;
import com.timokhov.web.chat_service.web_sockets.services.models.WebSocketSubscription;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WebSocketsSubscriptionsContainerServiceImpl implements WebSocketsSubscriptionsContainerService {

    private List<WebSocketSubscription> subscriptionList = new ArrayList<>();

    @Override
    public void addSubscription(WebSocketSubscription subscription) {
        if (subscription.isValid()) {
            subscriptionList.add(subscription);
        }
    }

    @Override
    public Optional<WebSocketSubscription> getSubscription(String id) {
        return subscriptionList.stream().filter(subscription -> subscription.getId().equals(id)).findFirst();
    }

    @Override
    public void removeSubscription(String id) {
        subscriptionList.removeIf(subscription -> subscription.getId().equals(id));
    }

    @Override
    public List<WebSocketSubscription> getSessionSubscriptions(String sessionId) {
        return subscriptionList.stream()
                .filter(subscription -> subscription.getSessionId().equals(sessionId))
                .collect(Collectors.toList());
    }

    @Override
    public void removeSessionSubscriptions(String sessionId) {
        subscriptionList.removeIf(subscription -> subscription.getSessionId().equals(sessionId));
    }

    @Override
    public List<WebSocketSubscription> getSubscriptionsByTopic(String topic) {
        return subscriptionList.stream()
                .filter(subscription -> subscription.getTopic().equals(topic))
                .collect(Collectors.toList());
    }
}
