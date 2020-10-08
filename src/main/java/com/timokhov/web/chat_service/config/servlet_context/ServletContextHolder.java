package com.timokhov.web.chat_service.config.servlet_context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServletContextHolder {

    private static ServletContext servletContext = null;

    public static ServletContext getServletContext() {
        if (servletContext == null) {
            throw new IllegalStateException("servletContextPath is not initialized, check listener in web.xml");
        }
        return servletContext;
    }

    /**
     * Should be registered in web.xml before spring application context listener
     */
    public static class Listener implements ServletContextListener {

        public void contextInitialized(ServletContextEvent sce) {
            servletContext = sce.getServletContext();
        }

        public void contextDestroyed(ServletContextEvent sce) {
        }
    }
}
