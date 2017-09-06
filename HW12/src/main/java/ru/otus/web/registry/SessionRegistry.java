package ru.otus.web.registry;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.Session;

import lombok.extern.slf4j.Slf4j;
import ru.otus.entity.UserEntity;
import ru.otus.service.UserService;
import ru.otus.listener.CacheStateListener;
import ru.otus.web.protocol.Message;

/**
 * @author e.petrov. Created 09 - 2017.
 */
@Slf4j
public class SessionRegistry implements CacheStateListener {

	public static final String CONNECTED_KEY = "CONNECTED_KEY";
	public static final String USERNAME_KEY = "username";
	public static final String PASSWORD_KEY = "password";

	private final UserService userService;
	private final List<Session> sessions;
	private final Map<String, Object> cacheCurrentState;

	public SessionRegistry(UserService userService) {
		this.userService = userService;
		this.sessions = new CopyOnWriteArrayList<>();
		this.cacheCurrentState = new ConcurrentHashMap<>();
	}

	public void register(Session session, Message message) throws IOException {
		UserEntity user = userService.loadByName(String.valueOf(message.getData().get(USERNAME_KEY)));
		Map<String, Object> responseData = new HashMap<>();
		if (user == null || !user.getPassword().equals(String.valueOf(message.getData().get(PASSWORD_KEY)))) {
			log.warn("Can't register session for user {}!", String.valueOf(message.getData().get(USERNAME_KEY)));
			responseData.put(CONNECTED_KEY, false);
			if(!sendMessage(session, Message.LOGIN_MSG_TYPE, responseData)) {
				session.close();
			}
		} else {
			responseData.put(CONNECTED_KEY, true);
			responseData.putAll(cacheCurrentState);
			if (sendMessage(session, Message.LOGIN_MSG_TYPE, responseData)) {
				sessions.add(session);
				log.debug("Register new session: {} for user {}", session.getId(), message.getData().get(USERNAME_KEY));
			}
		}
	}

	public void deregister(Session session) {
		log.debug("Deregister session {}", session.getId());
		sessions.remove(session);
	}

	@Override
	public void notifyStateChange(Map<String, Object> cacheState) {
		log.debug("Cache state changes: {}", cacheState);
		cacheCurrentState.putAll(cacheState);
		broadcastState(cacheState);
	}

	private void broadcastState(Map<String, Object> state) {
		sessions.removeIf(s -> !sendMessage(s, Message.CACHE_MSG_TYPE, state));
	}

	private boolean sendMessage(Session session, String type, Map<String, Object> data) {
		try {
			session.getBasicRemote().sendObject(new Message(type, data));
			log.debug("Message successfully send into session {}", session.getId());
			return true;
		} catch (Exception e) {
			log.error("Can't send message to session: " + session.getId(), e);
			return false;
		}
	}
}
