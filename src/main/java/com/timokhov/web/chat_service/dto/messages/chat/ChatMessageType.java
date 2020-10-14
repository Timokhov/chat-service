package com.timokhov.web.chat_service.dto.messages.chat;

import com.timokhov.web.chat_service.config.logger.annotations.Loggable;

@Loggable
public enum ChatMessageType {
    CHAT,
    SYSTEM
}
