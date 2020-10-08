package com.timokhov.web.chat_service.config.properties;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class PropertySourceInitializer implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {

    @Override
    public void initialize(ConfigurableWebApplicationContext applicationContext) {
        applicationContext.getEnvironment().getPropertySources().addLast(
                new PropertiesHolderPropertySource()
        );
    }
}
