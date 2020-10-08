package com.timokhov.web.chat_service.utils;

import org.springframework.util.ReflectionUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.UnknownHostException;

public class ServerUtils {

    public static final String ROOT_APP_DIR_NAME = "ROOT";
    public static final String APP_DIR_NAME_SEPARATOR = "#";

    public enum ServerType {
        TOMCAT
    }

    public enum ServletContainerType {
        TOMCAT
    }

    public static ServerType getServerType() {
        if (System.getProperty("catalina.base") != null) {
            return ServerType.TOMCAT;
        } else {
            throw new IllegalStateException("Unknown server");
        }
    }

    public static ServletContainerType getServletContainerType() {
        if (System.getProperty("catalina.base") != null) {
            return ServletContainerType.TOMCAT;
        } else {
            throw new IllegalStateException("Unknown servlet container");
        }
    }

    /**
     * Returns absolute path to application server log directory, without trailing slash.
     * Examples:
     * "C:\tomcat\logs",
     * "/opt/tomcat/logs"
     * @return log directory path
     */
    public static String getLogDirPath() {
        switch (getServerType()) {
            case TOMCAT:
                try {
                    return new File(System.getProperty("catalina.base")).getCanonicalPath() + "/logs";
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            default:
                throw new IllegalStateException("Unknown server");
        }
    }

    /**
     * Returns absolute path to application server config directory, without trailing slash.
     * Examples:
     * "C:\tomcat\conf",
     * "/opt/tomcat/conf"
     * @return config directory path
     */
    public static String getConfDirPath() {
        switch (getServerType()) {
            case TOMCAT:
                try {
                    return new File(System.getProperty("catalina.base")).getCanonicalPath() + "/conf";
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            default:
                throw new IllegalStateException("Unknown server");
        }
    }

    /**
     * Returns servlet context path, with leading slash, without trailing slash.
     * If context path is empty or "/", returns empty string.
     * Examples:
     * "",
     * "/path",
     * "/context-path",
     * "/my/context/path"
     *
     * @param servletContext servlet context
     * @return servlet context path
     */
    public static String getServletContextPath(ServletContext servletContext) {
        String contextPath = "";

        Method getContextPathMethod = ReflectionUtils.findMethod(ServletContext.class, "getContextPath");
        if (getContextPathMethod == null) {
            if (ServletContainerType.TOMCAT.equals(getServletContainerType())) {
                //hacky way for servlet version <= 2.4, works for tomcat only
                String servletResourcePath;
                try {
                    servletResourcePath = servletContext.getResource("/").getPath();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                String[] servletResourcePathParts = servletResourcePath.split("/", 3);

                if (servletResourcePathParts.length > 2) {
                    contextPath = servletResourcePathParts[2];
                }
            } else {
                throw new UnsupportedOperationException("Can't get context path for servlet 2.4-");
            }
        } else {
            // Servlet 2.5 available
            contextPath = (String) ReflectionUtils.invokeMethod(getContextPathMethod, servletContext);
        }

        //normalize
        if (contextPath == null || contextPath.length() == 0 || contextPath.equals("/")) {
            contextPath = "";
        } else {
            if (!contextPath.startsWith("/")) {
                contextPath = "/" + contextPath;
            }

            if (contextPath.endsWith("/")) {
                contextPath = contextPath.substring(0, contextPath.length() - 1);
            }
        }

        return contextPath;
    }

    public static String getAppLogDirPath(ServletContext servletContext) {
        return getLogDirPath() + "/" + getAppDirName(servletContext);
    }

    public static String getAppConfDirPath(ServletContext servletContext) {
        return getConfDirPath() + "/" + getAppDirName(servletContext);
    }

    public static String getAppDirName(ServletContext servletContext) {
        String contextPath = getServletContextPath(servletContext);
        if (contextPath.length() == 0) {
            return ROOT_APP_DIR_NAME;
        } else {
            return contextPath.substring(1, contextPath.length()).replaceAll("/", APP_DIR_NAME_SEPARATOR);
        }
    }

    public static String getServerName() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return "<unknown>";
        }
        return address.getHostName() + " (" + address.getHostAddress() + ")";
    }

    public static String getServerHost() {
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}
