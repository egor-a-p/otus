package ru.otus.web.protocol;

import lombok.Value;

import java.util.Map;

/**
 * author: egor, created: 04.09.17.
 */
@Value
public class Message {
    public static final String LOGIN_MSG_TYPE = "LOGIN_MSG";
    public static final String CACHE_MSG_TYPE = "CACHE_MSG";
    public static final String CONNECTED_KEY = "CONNECTED_KEY";
    public static final String HIT_COUNT_KEY = "HIT_COUNT_KEY";
    public static final String MISS_COUNT_KEY = "MISS_COUNT_KEY";
    public static final String LOAD_COUNT_KEY = "LOAD_COUNT_KEY";
    public static final String EVICTION_COUNT_KEY = "EVICTION_COUNT_KEY";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";

    private final String type;
    private final Map<String, Object> data;
}
