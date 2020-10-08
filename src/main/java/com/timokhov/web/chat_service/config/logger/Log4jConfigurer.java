package com.timokhov.web.chat_service.config.logger;

import com.timokhov.web.chat_service.config.properties.PropertiesHolder;
import com.timokhov.web.chat_service.utils.ServerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;

public class Log4jConfigurer implements ServletContextListener {

    public static final String SERVER_NAME_PROPERTY_NAME = "serverName";

    public static final String CONFIG_PATH_PARAM_NAME = "logger.configPath";

    public static final String SMTP_MIN_LEVEL_PARAM_NAME = "logging.smtp.minLevel";
    public static final String SMTP_HOST_PARAM_NAME = "logging.smtp.host";
    public static final String SMTP_MAIL_TO_PARAM_NAME = "logging.smtp.mailTo";
    public static final String SMTP_MAIL_FROM_PARAM_NAME = "logging.smtp.mailFrom";

    public static final String SENTRY_DSN_PARAM_NAME = "sentry.dsn";
    public static final String SENTRY_ENVIRONMENT_PARAM_NAME = "sentry.environment";
    public static final String SENTRY_MIN_LEVEL_PARAM_NAME = "sentry.minLevel";

    public static final String EXTERNAL_FILE_MIN_LEVEL_PARAM_NAME = "logging.external.minLevel";

    public static final String DEFAULT_CONFIG_PATH = "log4j2.xml";

    public void contextInitialized(final ServletContextEvent sce) {

        final URL configUrl = getConfigUrl(sce.getServletContext());

        final File file;
        try {
            file = new File(configUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        if (!file.exists()) {
            return;
        }

        configure(sce.getServletContext(), file.toURI());
    }

    protected void configure(ServletContext servletContext, URI configURI) {
        setProperties(servletContext);
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.setConfigLocation(configURI);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        LogManager.shutdown();
    }

    protected URL getConfigUrl(ServletContext sc) {
        String configPath = sc.getInitParameter(CONFIG_PATH_PARAM_NAME);
        if (configPath == null) {
            configPath = DEFAULT_CONFIG_PATH;
        }

        try {
            URL context = new File(ServerUtils.getAppConfDirPath(sc)).toURI().toURL();
            return new URL(context, configPath);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setProperties(ServletContext sc) {
        Properties properties = PropertiesHolder.getProperties();
        setStandardProperties(sc, properties);
    }

    protected void setStandardProperties(ServletContext sc, Properties properties) {
        setProperty(properties, SMTP_MIN_LEVEL_PARAM_NAME);
        setProperty(properties, SMTP_HOST_PARAM_NAME);
        setProperty(properties, SMTP_MAIL_TO_PARAM_NAME);
        setProperty(properties, SMTP_MAIL_FROM_PARAM_NAME);
        setProperty(properties, SENTRY_DSN_PARAM_NAME);
        setProperty(properties, SENTRY_ENVIRONMENT_PARAM_NAME);
        setProperty(properties, SENTRY_MIN_LEVEL_PARAM_NAME);
        setProperty(properties, EXTERNAL_FILE_MIN_LEVEL_PARAM_NAME);
        System.setProperty(SERVER_NAME_PROPERTY_NAME, ServerUtils.getServerName());
    }

    private void setProperty(Properties properties, String paramName) {
        System.setProperty(
                paramName,
                Optional.ofNullable(properties.getProperty(paramName)).orElse("")
        );
    }
}
