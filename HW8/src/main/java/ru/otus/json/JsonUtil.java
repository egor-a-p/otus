package ru.otus.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import javax.json.JsonException;

import org.apache.commons.lang3.ClassUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import lombok.val;

public class JsonUtil {

    public static String toJson(Object object) {
        if (object == null) {
            return JSONValue.toJSONString(null);
        }
        if (isSimple(object)) {
            return simpleToJSON(object);
        }
	    if (isArray(object)) {
		    return arrayToJSON(object);
	    }
	    if (isIterable(object)) {
		    return iterableToJSON(object);
	    }
	    if (isMap(object)) {
		    return mapToJSON(object);
	    }
        return objectToJson(object);
    }

	private static boolean isSimple(Object o) {
        return ClassUtils.isPrimitiveOrWrapper(o.getClass()) || o.getClass() == String.class;
    }

	private static boolean isArray(Object o) {
		return o.getClass().isArray();
	}

	private static boolean isIterable(Object o) {
		return o instanceof Iterable;
	}

	private static boolean isMap(Object o) {
		return o instanceof Map;
	}

    private static String simpleToJSON(Object object) {
        if (object.getClass() == char.class || object.getClass() == Character.class) {
            object = String.valueOf(object);
        }
        return JSONValue.toJSONString(object);
    }

    @SuppressWarnings("unchecked")
	private static String arrayToJSON(Object object) {
		val jsonArray = new JSONArray();
		for (int i = 0; i < Array.getLength(object); i++) {
			jsonArray.add(JSONValue.parse(toJson(Array.get(object, i))));
		}
		return jsonArray.toJSONString();
	}

	@SuppressWarnings("unchecked")
	private static String iterableToJSON(Object object) {
		val jsonArray = new JSONArray();
		for (Object o : (Iterable) object) {
			jsonArray.add(JSONValue.parse(toJson(o)));
		}
		return jsonArray.toJSONString();
	}

	@SuppressWarnings("unchecked")
	private static String mapToJSON(Object object) {
		val jsonObject = new JSONObject();
		((Map) object).forEach((k, v) -> {
			jsonObject.put(JSONValue.parse(toJson(k)), JSONValue.parse(toJson(v)));
		});
		return jsonObject.toJSONString();
	}


	@SuppressWarnings("unchecked")
    private static String objectToJson(Object object) {
        try {
	        val jsonObject = new JSONObject();
            for (Class c = object.getClass(); c != null; c = c.getSuperclass()) {
	            for (Field field : c.getDeclaredFields()) {
		            if (Modifier.isTransient(field.getModifiers())) {
			            continue;
		            }

		            field.setAccessible(true);
		            Object fieldValue = field.get(object);
		            jsonObject.put(field.getName(), JSONValue.parse(toJson(fieldValue)));
	            }
            }
	        return jsonObject.toJSONString();
        } catch (Exception e) {
            throw new JsonException("Can't create json object:", e);
        }
    }

    private JsonUtil() {
    }
}
