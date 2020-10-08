package com.timokhov.web.chat_service.services.configs;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public class AbstractConfig implements EnvironmentAware {

    protected Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
