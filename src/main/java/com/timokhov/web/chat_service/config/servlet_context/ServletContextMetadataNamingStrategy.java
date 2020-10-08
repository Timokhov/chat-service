package com.timokhov.web.chat_service.config.servlet_context;

import com.timokhov.web.chat_service.utils.ServerUtils;
import org.springframework.jmx.export.metadata.JmxAttributeSource;
import org.springframework.jmx.export.naming.MetadataNamingStrategy;
import org.springframework.jmx.support.ObjectNameManager;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.servlet.ServletContext;

public class ServletContextMetadataNamingStrategy extends MetadataNamingStrategy {

    private ServletContext servletContext;

    public ServletContextMetadataNamingStrategy(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContextMetadataNamingStrategy(ServletContext servletContext, JmxAttributeSource attributeSource) {
        super(attributeSource);
        this.servletContext = servletContext;
    }

    @Override
    public ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException {
        ObjectName objectName = super.getObjectName(managedBean, beanKey);
        String contextPath = ServerUtils.getServletContextPath(servletContext);

        if (contextPath.startsWith("/")) {
            contextPath = contextPath.substring(1);
        }

        if (!contextPath.equals("")) {
            contextPath = contextPath.replaceAll("/", "_") + "_";
        }

        return ObjectNameManager.getInstance(contextPath + objectName.getCanonicalName());
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
