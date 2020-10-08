package com.timokhov.web.chat_service.config.logger;

import com.timokhov.web.chat_service.config.logger.annotations.HideParamLog;
import com.timokhov.web.chat_service.config.logger.annotations.HideResultLog;
import com.timokhov.web.chat_service.config.logger.annotations.InjectLogger;
import com.timokhov.web.chat_service.config.logger.models.AnnotatedDtoLogger;
import com.timokhov.web.chat_service.config.logger.models.Logger;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.IntStream;

public class LoggerAspect {

    private static final String LOGGER_ASPECT = "LOGGER_ASPECT";

    private static final String START_MESSAGE = "start with params: ";
    private static final String SUCCESS_MESSAGE = "result return: ";
    private static final String ERROR_MESSAGE = "throw: ";

    @InjectLogger
    private Logger exceptionLogger;

    public void doLoggingStart(JoinPoint joinPoint) {
        composeLogMessage(joinPoint, START_MESSAGE, preProcessArguments(joinPoint));
    }

    public void doLoggingForSuccess(JoinPoint joinPoint, Object result) {
        composeLogMessage(joinPoint, SUCCESS_MESSAGE, preProcessResult(joinPoint, result));
    }

    public void doLoggingForError(JoinPoint joinPoint, Throwable exception) {
        composeLogMessage(joinPoint, ERROR_MESSAGE, exception.toString());
    }

    /**
     * Avoid to write collection map and array data to log
     * @param params - params
     */
    protected void overrideCollectionParams(Object[] params) {
        if (params == null) {
            return;
        }

        Class<?> componentType = params.getClass().getComponentType();
        if (!Object.class.equals(componentType)) {
            return;
        }

        for (int i = 0; i < params.length; i++) {
            Object param = params[i];

            if (param == null) {
                continue;
            }

            Class<?> type = param.getClass();

            //do not write collections in log by default
            if (Collection.class.isAssignableFrom(type)
                    || Map.class.isAssignableFrom(type)
                    || type.isArray()) {

                params[i] = type.getSimpleName();
            }
        }
    }

    private void composeLogMessage(JoinPoint joinPoint, String message, Object... params) {
        try {
            StringBuilder builder = new StringBuilder();
            Signature signature = joinPoint.getSignature();

            AnnotatedDtoLogger logger = new AnnotatedDtoLogger(LOGGER_ASPECT + "." + signature.getDeclaringType().getName());
            String methodName = signature.getName() + ": ";
            builder.append(methodName);
            builder.append(message);

            overrideCollectionParams(params);

            String paramString = StringUtils.repeat("%s, ", params.length);
            if (params.length > 0) {
                paramString = paramString.substring(0, paramString.length() - 2);
            }
            builder.append(paramString);
            logger.info(builder.toString(), params);
        }
        catch (Throwable e) {
            exceptionLogger.debug(e);
        }
    }

    protected Method getMethodForPoint(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            return methodSignature.getMethod();
        }
        return null;
    }

    private Object[] preProcessArguments(JoinPoint joinPoint) {
        Method method = getMethodForPoint(joinPoint);

        Annotation[][] annotations = method.getParameterAnnotations();
        Object[] arguments = joinPoint.getArgs();

        if (annotations.length > 0) {
            IntStream.range(0, arguments.length).forEach(i -> {
                boolean isHide = Arrays.stream(method.getParameterAnnotations()[i])
                        .anyMatch(annotation -> HideParamLog.class.equals(annotation.annotationType()));

                if (isHide) {
                    arguments[i] = "[PROTECTED]";
                }
            });
        }

        return arguments;
    }

    private Object preProcessResult(JoinPoint joinPoint, Object result) {
        boolean logResult = true;
        Method method = getMethodForPoint(joinPoint);
        if (method != null) {
            logResult = method.getAnnotation(HideResultLog.class) == null;
        }
        if (!logResult) {
            return "[PROTECTED]";
        } else {
            return result;
        }
    }
}
