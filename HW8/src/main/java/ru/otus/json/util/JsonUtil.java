package ru.otus.json.util;

import lombok.val;
import org.apache.commons.lang3.ClassUtils;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;

public class JsonUtil {

    private static final JsonWriterFactory FACTORY = Json.createWriterFactory(
            Collections.singletonMap(
                    JsonGenerator.PRETTY_PRINTING, true));

    public static <T> String transform(T instance) {
        StringWriter writer = new StringWriter();
        try(JsonWriter jsonWriter = FACTORY.createWriter(writer)) {
            jsonWriter.writeObject(map(instance, Json.createObjectBuilder()).build());
        }
        return writer.toString();
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
