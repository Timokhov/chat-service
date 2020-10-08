package com.timokhov.web.chat_service.config.logger.utils;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;
import com.timokhov.web.chat_service.config.logger.annotations.ExcludeLog;
import com.timokhov.web.chat_service.config.logger.annotations.IncludeCollectionLog;
import com.timokhov.web.chat_service.config.logger.annotations.LogHash;
import com.timokhov.web.chat_service.config.logger.annotations.Loggable;
import org.springframework.http.HttpEntity;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class LogToStringBuilder {

    public static Object[] toStringArgs(Object... args) {
        if (args == null) {
            return null;
        }

        Class<?> componentType = args.getClass().getComponentType();
        //if we get array with type not object - then return args without changes
        if (!Object.class.equals(componentType)) {
            return args;
        }

        for (int i = 0; i < args.length; i++) {
            final Object arg = args[i];

            if (arg == null) {
                continue;
            }

            Class<?> type = arg.getClass();
            if (isLoggableType(type)) {
                args[i] = new Object() {
                    //lazy exec for Logable
                    @Override
                    public String toString() {
                        return toStringLoggableObject(arg);
                    }
                };
            } else if (HttpEntity.class.isAssignableFrom(type)) {
                Object body = ((HttpEntity<?>) arg).getBody();
                if (body != null && isLoggableType(body.getClass())) {
                    args[i] = new Object() {
                        //lazy exec for Logable
                        @Override
                        public String toString() {
                            return toStringLoggableObject(body);
                        }
                    };
                }
            }

        }
        return args;
    }

    public static String hash(Object obj) {
        return obj == null ? "null" : Integer.toHexString(obj.hashCode());
    }

    protected static String toStringLoggableObject(final Object object) {
        if (object == null) {
            return null;
        }

        final Class<?> type = object.getClass();

        if (Collection.class.isAssignableFrom(type)) {
            return toStringCollection(object);
        }

        if (Map.class.isAssignableFrom(type)) {
            return toStringMap(object);
        }

        final List<String> result = new ArrayList<String>();

        ReflectionUtils.doWithFields(type, new ReflectionUtils.FieldCallback() {

            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {

                ReflectionUtils.makeAccessible(field);

                Object fieldObject = field.get(object);
                String fieldName = field.getName();

                if (field.getAnnotation(ExcludeLog.class) != null) {
                    //do not write excluded fields to log
                    return;
                }

                if (fieldObject == null) {
                    appendField(result, fieldName, "null");
                    return;
                }

                boolean useHash = field.getAnnotation(LogHash.class) != null;

                Class<?> fieldObjectClass = fieldObject.getClass();

                if (isSimpleField(fieldObjectClass) && !useHash) {
                    appendField(result, fieldName, fieldObject);
                    return;
                }

                if (isLoggableType(fieldObjectClass) && !useHash) {
                    appendField(result, fieldName, toStringLoggableObject(fieldObject));
                    return;
                }

                if (field.getAnnotation(IncludeCollectionLog.class) != null) {

                    if (fieldObjectClass.isArray()) {
                        result.add(toStringArrayField(fieldName, fieldObject));
                        return;
                    }

                    appendField(result, fieldName, toStringLoggableObject(fieldObject));
                    return;
                }

                //write collection size and hash
                if (isCollection(fieldObjectClass)) {
                    appendField(result, fieldName, "@Collection|size:" + getCollectionSize(fieldObject));
                    return;
                }

                //object class and hash, by default
                appendField(result, fieldName, "@Object|" + fieldObjectClass.getSimpleName() + "|" + hash(fieldObject));
            }
        }, new ReflectionUtils.FieldFilter() {
            public boolean matches(Field field) {
                //all non-static and non-synthetic fields
                return !field.isSynthetic() && !Modifier.isStatic(field.getModifiers());
            }
        });

        return "{" + StringUtils.join(result, ",") + "}";
    }

    protected static String toStringElement(Object elem) {
        if (elem == null) {
            return null;
        }

        if (isLoggableType(elem.getClass()) || Collection.class.isAssignableFrom(elem.getClass()) ||
                Map.class.isAssignableFrom(elem.getClass())) {
            return toStringLoggableObject(elem);
        } else {
            return String.valueOf(elem);
        }
    }

    protected static String toStringCollection(Object collection) {
        if (collection == null) {
            return null;
        }

        List<String> result = new ArrayList<String>();
        for (Object elem : ((Collection) collection)) {
            result.add(wrap(toStringElement(elem)));
        }
        return "[" + StringUtils.join(result, ", ") + "]";
    }

    protected static String toStringMap(Object map) {
        if (map == null) {
            return null;
        }

        List<String> result = new ArrayList<String>();
        for (Object o : ((Map) map).entrySet()) {
            Map.Entry entry = ((Map.Entry) o);
            result.add(wrap(entry.getKey()) + ":" + wrap(toStringElement(entry.getValue())));
        }
        return "{" + StringUtils.join(result, ", ") + "}";
    }

    protected static String toStringArrayField(String name, Object array) {

        List<String> result = new ArrayList<String>();

        if (name == null || array == null) {
            return null;
        }

        Class<?> type = array.getClass();
        if (long[].class.isAssignableFrom(type)) {
            appendField(result, name, Arrays.toString((long[]) array));
            return StringUtils.join(result, ";");
        }

        if (int[].class.isAssignableFrom(type)) {
            appendField(result, name, Arrays.toString((int[]) array));
            return StringUtils.join(result, ";");
        }

        if (short[].class.isAssignableFrom(type)) {
            appendField(result, name, Arrays.toString((short[]) array));
            return StringUtils.join(result, ";");
        }

        if (char[].class.isAssignableFrom(type)) {
            appendField(result, name, Arrays.toString((char[]) array));
            return StringUtils.join(result, ";");
        }

        if (byte[].class.isAssignableFrom(type)) {
            appendField(result, name, Arrays.toString((byte[]) array));
            return StringUtils.join(result, ";");
        }

        if (boolean[].class.isAssignableFrom(type)) {
            appendField(result, name, Arrays.toString((boolean[]) array));
            return StringUtils.join(result, ";");
        }

        if (float[].class.isAssignableFrom(type)) {
            appendField(result, name, Arrays.toString((float[]) array));
            return StringUtils.join(result, ";");
        }

        if (double[].class.isAssignableFrom(type)) {
            appendField(result, name, Arrays.toString((double[]) array));
            return StringUtils.join(result, ";");
        }

        if (Object[].class.isAssignableFrom(type)) {
            List<String> res = new ArrayList<String>();
            for (Object elem : (Object[]) array) {
                res.add(toStringElement(elem));
            }
            appendField(result, name, "[" + StringUtils.join(res, ", ") + "]");
            return StringUtils.join(result, ";");
        }

        return null;
    }

    protected static boolean isLoggableType(Class clazz) {
        return clazz.getAnnotation(Loggable.class) != null;
    }

    protected static boolean isSimpleField(Class<?> fieldType) {
        return fieldType.isPrimitive()
                || fieldType.isEnum()
                || Number.class.isAssignableFrom(fieldType)
                || Boolean.class.isAssignableFrom(fieldType)
                || CharSequence.class.isAssignableFrom(fieldType)
                || Date.class.isAssignableFrom(fieldType)
                || UUID.class.isAssignableFrom(fieldType);
    }

    protected static void appendField(List<String> fields, String name, @Nullable Object value) {
        fields.add(wrap(name) + ":" + wrap(value));
    }

    protected static String wrap(Object value) {
        String symbol = "\"";
        if (value == null || String.valueOf(value).startsWith("{") ||  String.valueOf(value).startsWith("[")) {
            return String.valueOf(value);
        }
        return symbol + String.valueOf(value) + symbol;
    }

    protected static boolean isCollection(Class<?> type) {
        return type.isArray() || Map.class.isAssignableFrom(type) || Collection.class.isAssignableFrom(type);
    }

    protected static Integer getCollectionSize(Object obj) {
        if (obj == null) {
            return null;
        }

        Class type = obj.getClass();


        if (type.isArray())  {
            return Array.getLength(obj);
        }

        if (Map.class.isAssignableFrom(type)) {
            return ((Map) obj).size();
        }

        if (Collection.class.isAssignableFrom(type)) {
            return ((Collection) obj).size();
        }

        return null;
    }
}
