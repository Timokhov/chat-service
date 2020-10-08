package com.timokhov.web.chat_service.config.logger.models;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.IllegalFormatException;

public class Logger {

    private final Log log = LogFactory.getLog(getClass());

    private Log delegate;

    public Logger(Class<?> clazz) {
        this.delegate = LogFactory.getLog(clazz);
    }

    public Logger(String name) {
        this.delegate = LogFactory.getLog(name);
    }



    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }

    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }

    public boolean isFatalEnabled() {
        return delegate.isFatalEnabled();
    }



    public void trace(String message, Object... args) {
        delegate.trace(format(message, args));
    }

    public void trace(Throwable t) {
        delegate.trace("", t);
    }

    public void trace(Throwable t, String message, Object... args) {
        delegate.trace(format(message, args), t);
    }



    public void debug(String message, Object... args) {
        delegate.debug(format(message, args));
    }

    public void debug(Throwable t) {
        delegate.debug("", t);
    }

    public void debug(Throwable t, String message, Object... args) {
        delegate.debug(format(message, args), t);
    }



    public void info(String message, Object... args) {
        delegate.info(format(message, args));
    }

    public void info(Throwable t) {
        delegate.info("", t);
    }

    public void info(Throwable t, String message, Object... args) {
        delegate.info(format(message, args), t);
    }



    public void warn(String message, Object... args) {
        delegate.warn(format(message, args));
    }

    public void warn(Throwable t) {
        delegate.warn("", t);
    }

    public void warn(Throwable t, String message, Object... args) {
        delegate.warn(format(message, args), t);
    }



    public void error(String message, Object... args) {
        delegate.error(format(message, args));
    }

    public void error(Throwable t) {
        delegate.error("", t);
    }

    public void error(Throwable t, String message, Object... args) {
        delegate.error(format(message, args), t);
    }



    public void fatal(String message, Object... args) {
        delegate.fatal(format(message, args));
    }

    public void fatal(Throwable t) {
        delegate.fatal("", t);
    }

    public void fatal(Throwable t, String message, Object... args) {
        delegate.fatal(format(message, args), t);
    }

    protected Object[] processArgs(Object... args) {
        return args;
    }

    protected Object format(final String message, final Object... args) {
        //lazy formatting
        return new Object() {
            @Override
            public String toString() {
                if (message == null) {
                    return null;
                }
                if (args == null || args.length == 0) {
                    return message;
                }
                try {
                    return String.format(message, processArgs(args));
                } catch (IllegalFormatException e) {
                    log.error("Exception on message formatting: ", e);
                    return message;
                }
            }
        };
    }
}
