package com.timokhov.web.chat_service.config.logger.models;

import com.timokhov.web.chat_service.config.logger.utils.LogToStringBuilder;

public class AnnotatedDtoLogger extends Logger {

    @SuppressWarnings("UnusedDeclaration")
    public AnnotatedDtoLogger(Class<?> clazz) {
        super(clazz);
    }

    public AnnotatedDtoLogger(String name) {
        super(name);
    }

    @Override
    protected Object[] processArgs(Object... args) {
        return LogToStringBuilder.toStringArgs(args);
    }

}
