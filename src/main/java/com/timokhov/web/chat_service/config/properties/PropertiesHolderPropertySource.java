package com.timokhov.web.chat_service.config.properties;

import org.springframework.core.env.PropertySource;

public class PropertiesHolderPropertySource extends PropertySource<Class<PropertiesHolder>> {

    public PropertiesHolderPropertySource() {
        super(PropertiesHolder.class.getName(), PropertiesHolder.class);
    }

    @Override
    public Object getProperty(String name) {
        return PropertiesHolder.getProperties().get(name);
    }
}
