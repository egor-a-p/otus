package ru.otus.web.protocol;

import java.util.Map;

import lombok.Value;

/**
 * author: egor, created: 04.09.17.
 */
@Value
public class Message {
    public static final String LOGIN_MSG_TYPE = "LOGIN_MSG";
    public static final String CACHE_MSG_TYPE = "CACHE_MSG";

    private final String type;
    private final Map<String, Object> data;
}
