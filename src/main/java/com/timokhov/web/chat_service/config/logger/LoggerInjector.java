package com.timokhov.web.chat_service.config.logger;

import com.timokhov.web.chat_service.config.logger.annotations.InjectLogger;
import com.timokhov.web.chat_service.config.logger.models.AnnotatedDtoLogger;
import com.timokhov.web.chat_service.config.logger.models.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;

public class LoggerInjector implements BeanPostProcessor {

    private final Log log = LogFactory.getLog(getClass());

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {

        final String loggerClassName = AnnotatedDtoLogger.class.getName();

        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            if (
                    field.getAnnotation(InjectLogger.class) != null &&
                            field.getType().equals(Logger.class)
            ) {
                log.debug("postProcessBeforeInitialization: injecting logger to " + bean.getClass());
                ReflectionUtils.makeAccessible(field);


                //try to inject custom logger
                if (StringUtils.isNotBlank(loggerClassName)) {
                    try {
                        Constructor<?> constructor = Class.forName(loggerClassName).getDeclaredConstructor(Class.class);
                        field.set(bean, constructor.newInstance(bean.getClass()));
                        return;
                    } catch (Throwable th) {
                        log.warn("postProcessBeforeInitialization: injecting" + loggerClassName + "logger failed, simple logger wil be injected", th);
                        //do nothing - use simple logger
                    }
                }

                field.set(bean, new Logger(bean.getClass()));

            }
        });
        return bean;
    }
}
