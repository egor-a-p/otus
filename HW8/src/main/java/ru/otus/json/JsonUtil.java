package ru.otus.json;

import lombok.val;
import org.apache.commons.lang3.ClassUtils;

import javax.json.*;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Objects;

public class JsonUtil {

    public static String SIMPLE_FORMAT = "\"%s\"";

    public static String toJson(Object object) {
        if (object == null) {
            return JsonObject.NULL.toString();
        }
        switch (Type.getType(object)) {

            case PRIMITIVE:
                return String.valueOf(object);
            case STRING:
                return String.format(SIMPLE_FORMAT, String.valueOf(object));
            case ARRAY_OF_PRIMITIVES:
                val arrayBuilder = Json.createArrayBuilder();
                for (int i = 0; i < Array.getLength(object); i++) {
                    arrayBuilder.add(String.valueOf(Array.get(object, i)));
                }
                val writer = new StringWriter();
                Json.createWriter(writer).writeArray(arrayBuilder.build());
                return writer.toString();
            case ITERABLE:
                return "";
            case MAP:
                return "";
            default:
                return "";

        }
    }

    public enum Type {
        STRING,
        PRIMITIVE,
        ARRAY_OF_PRIMITIVES,
        ITERABLE,
        MAP,
        OBJECT;

        public static final Type getType(Object o) {
            Objects.requireNonNull(o);
            if (ClassUtils.isPrimitiveOrWrapper(o.getClass())) {
                return PRIMITIVE;
            }
            if (String.class == o.getClass()) {
                return STRING;
            }
            if (o.getClass().isArray() && ClassUtils.isPrimitiveOrWrapper(o.getClass().getComponentType())) {
                return ARRAY_OF_PRIMITIVES;
            }
            if (o instanceof Iterable) {
                return ITERABLE;
            }
            if (o instanceof Map) {
                return MAP;
            }
            return OBJECT;
        }
    }

    private static JsonObjectBuilder map(Object object, JsonObjectBuilder builder) {
        try {
            for (Class c = object.getClass(); c != null; c = c.getSuperclass()) {
                for (Field field : c.getDeclaredFields()) {
                    if (Modifier.isTransient(field.getModifiers())) {
                        continue;
                    }

                    field.setAccessible(true);
                    Object fieldValue = field.get(object);
                    if (ClassUtils.isPrimitiveOrWrapper(field.getType()) || fieldValue instanceof String) {
                        builder.add(field.getName(), String.valueOf(fieldValue));
                        continue;
                    }
                    if (field.getType().isArray()) {
                        val arrayBuilder = Json.createArrayBuilder();
                        for (int i = 0; i < Array.getLength(fieldValue); i++) {
                            Object iValue = Array.get(fieldValue, i);
                            if (ClassUtils.isPrimitiveOrWrapper(iValue.getClass()) || iValue instanceof String) {
                                arrayBuilder.add(String.valueOf(iValue));
                            } else {
                                arrayBuilder.add(map(iValue, Json.createObjectBuilder()).build());
                            }
                        }
                        builder.add(field.getName(), arrayBuilder);
                        continue;
                    }
                    if (fieldValue instanceof Iterable) {
                        val arrayBuilder = Json.createArrayBuilder();
                        for (Object o : (Iterable) fieldValue) {
                            if (ClassUtils.isPrimitiveOrWrapper(o.getClass()) || o instanceof String) {
                                arrayBuilder.add(String.valueOf(o));
                            } else {
                                arrayBuilder.add(map(o, Json.createObjectBuilder()).build());
                            }
                        }
                        builder.add(field.getName(), arrayBuilder);
                        continue;
                    }
                    builder.add(field.getName(), map(fieldValue, Json.createObjectBuilder()));
                }
            }
            return builder;
        } catch (Exception e) {
            throw new JsonException("Can't create json object:", e);
        }
    }

    private JsonUtil() {
    }
}
