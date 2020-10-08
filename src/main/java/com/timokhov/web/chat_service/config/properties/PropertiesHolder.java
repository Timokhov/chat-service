package com.timokhov.web.chat_service.config.properties;

import com.timokhov.web.chat_service.config.servlet_context.ServletContextHolder;
import com.timokhov.web.chat_service.utils.ServerUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.DefaultPropertiesPersister;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PropertiesHolder {

    public static final String PROPERTIES_FILE_PATH_PARAM_NAME = "properties.propertiesFilePath";
    public static final String COMMON_PROPERTIES_FILE_PATH_PARAM_NAME = "properties.commonPropertiesFilePath";
    public static final String LOCAL_PROPERTIES_FILE_PATH_PARAM_NAME = "properties.localPropertiesFilePath";
    public static final String REFRESH_SECONDS_PARAM_NAME = "properties.refreshSeconds";

    public static final String DEFAULT_PROPERTIES_FILE_PATH = "app.properties";
    public static final String DEFAULT_COMMON_PROPERTIES_FILE_PATH = "common.app.properties";
    public static final String DEFAULT_LOCAL_PROPERTIES_FILE_PATH = "local.app.properties";
    public static final long DEFAULT_REFRESH_SECONDS = 5L;

    private static final Map<Object, Properties> holder = new ConcurrentHashMap<>();
    private static final Object key = new Object();

    private static ScheduledExecutorService scheduledLogConfigReloadExecutor;

    public static Properties getProperties() {
        return holder.computeIfAbsent(key, key -> {
            Properties properties = new Properties();
            refreshProperties(properties);
            return properties;
        });
    }

    public static void invalidateProperties() {
        holder.remove(key);
    }

    private static void init() {
        long refreshSeconds = NumberUtils.toLong(
                ServletContextHolder.getServletContext().getInitParameter(REFRESH_SECONDS_PARAM_NAME), DEFAULT_REFRESH_SECONDS
        );

        if (refreshSeconds > 0) {
            scheduledLogConfigReloadExecutor = Executors.newScheduledThreadPool(1);

            scheduledLogConfigReloadExecutor.scheduleAtFixedRate(new Runnable() {

                private long propLastModified = 0L;
                private long commonPropLastModified = 0L;
                private long localPropLastModified = 0L;

                @Override
                public void run() {
                    File commonPropertiesFile = getCommonPropertiesFile();
                    File propertiesFile = getPropertiesFile();
                    File localPropertiesFile = getLocalPropertiesFile();
                    if (
                            propertiesFile.lastModified() != propLastModified ||
                                    commonPropertiesFile.lastModified() != commonPropLastModified ||
                                    localPropertiesFile.lastModified() != localPropLastModified
                    ) {
                        commonPropLastModified = commonPropertiesFile.lastModified();
                        propLastModified = propertiesFile.lastModified();
                        localPropLastModified = localPropertiesFile.lastModified();
                        refreshProperties();
                    }
                }
            }, refreshSeconds, refreshSeconds, TimeUnit.SECONDS);
        }
    }

    private static void destroy() {
        if (scheduledLogConfigReloadExecutor != null) {
            scheduledLogConfigReloadExecutor.shutdown();
        }
    }

    private static void refreshProperties() {
        holder.compute(key, (key, properties) -> {
            if (properties == null) {
                properties = new Properties();
            }
            refreshProperties(properties);
            return properties;
        });
    }

    private static void refreshProperties(Properties properties) {
        properties.clear();
        fillProperties(properties, getCommonPropertiesFile());
        fillProperties(properties, getPropertiesFile());
        fillProperties(properties, getLocalPropertiesFile());
    }

    private static void fillProperties(Properties properties, File propertiesFile) {
        if (!propertiesFile.exists()) {
            return;
        }

        try (Reader reader = new EncodedResource(new UrlResource(propertiesFile.toURI().toURL()), "UTF-8").getReader()) {
            new DefaultPropertiesPersister().load(properties, reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File getPropertiesFile() {
        String propertiesFilePath = ServletContextHolder.getServletContext().getInitParameter(PROPERTIES_FILE_PATH_PARAM_NAME);
        if (propertiesFilePath == null) {
            propertiesFilePath = DEFAULT_PROPERTIES_FILE_PATH;
        }
        return new File(ServerUtils.getAppConfDirPath(ServletContextHolder.getServletContext()), propertiesFilePath);
    }

    private static File getCommonPropertiesFile() {
        String propertiesFilePath = ServletContextHolder.getServletContext().getInitParameter(COMMON_PROPERTIES_FILE_PATH_PARAM_NAME);
        if (propertiesFilePath == null) {
            propertiesFilePath = DEFAULT_COMMON_PROPERTIES_FILE_PATH;
        }
        return new File(ServerUtils.getAppConfDirPath(ServletContextHolder.getServletContext()), propertiesFilePath);
    }

    private static File getLocalPropertiesFile() {
        String propertiesFilePath = ServletContextHolder.getServletContext().getInitParameter(LOCAL_PROPERTIES_FILE_PATH_PARAM_NAME);
        if (propertiesFilePath == null) {
            propertiesFilePath = DEFAULT_LOCAL_PROPERTIES_FILE_PATH;
        }
        return new File(ServerUtils.getAppConfDirPath(ServletContextHolder.getServletContext()), propertiesFilePath);
    }

    /**
     * Should be registered in web.xml after ServletContextHolder$Listener,
     * before spring application context listener
     */
    public static class Listener implements ServletContextListener {

        public void contextInitialized(ServletContextEvent sce) {
            init();
        }

        public void contextDestroyed(ServletContextEvent sce) {
            destroy();
        }
    }
}
