package com.timokhov.web.chat_service.services.configs;

import org.springframework.stereotype.Component;

@Component
public class ServiceConfig extends AbstractConfig {

    public String[] getCorsAllowedOrigins() {
        return environment.getProperty("service.corsAllowedOrigins", String[].class, new String[] {});
    }

}
