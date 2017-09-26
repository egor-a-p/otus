package ru.otus.listener;

import java.util.Map;

/**
 * @author e.petrov. Created 09 - 2017.
 */
public interface CacheStateListener {
	String HIT_COUNT_KEY = "HIT_COUNT_KEY";
	String MISS_COUNT_KEY = "MISS_COUNT_KEY";
	String LOAD_COUNT_KEY = "LOAD_COUNT_KEY";
	String EVICTION_COUNT_KEY = "EVICTION_COUNT_KEY";
	String SIZE_KEY = "SIZE_KEY";

	void notifyStateChange(Map<String, Object> cacheState);
}
