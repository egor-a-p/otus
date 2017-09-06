package ru.otus.web.socket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;
import ru.otus.persistence.PersistenceUnit;
import ru.otus.repository.UserRepositoryImpl;
import ru.otus.service.CachedUserService;
import ru.otus.service.CachedUserServiceImpl;
import ru.otus.web.protocol.Message;
import ru.otus.web.protocol.MessageDecoder;
import ru.otus.web.protocol.MessageEncoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author e.petrov. Created 09 - 2017.
 */
@Slf4j
@ServerEndpoint(value = "/cache", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class CacheEndpoint {

	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";

	private final List<Session> sessions;
	private final CachedUserService service;

	public CacheEndpoint() {
		this(new CopyOnWriteArrayList<>(),
			 new CachedUserServiceImpl(new UserRepositoryImpl(PersistenceUnit.createEntityManager())));
	}

	public CacheEndpoint(List<Session> sessions, CachedUserService service) {
		this.sessions = sessions;
		this.service = service;
	}

	@OnOpen
	public void onOpen(final Session session) {
		sessions.add(session);
	}

	@OnMessage
	public void onMessage(final Session session, final Message message) throws IOException, EncodeException {
		switch (message.getType()) {
			case Message.LOGIN_MSG_TYPE:
				Map<String, Object> data = new HashMap<>();
				if (verify(message)) {
					data.put(Message.CONNECTED_KEY, true);
					data.put(Message.MISS_COUNT_KEY, 100);
					data.put(Message.HIT_COUNT_KEY, 100);
					data.put(Message.LOAD_COUNT_KEY, 100);
					data.put(Message.EVICTION_COUNT_KEY, 100);
				} else {
					data.put(Message.CONNECTED_KEY, false);
					sessions.remove(session);
				}
				Message response = new Message(Message.LOGIN_MSG_TYPE, data);
				session.getBasicRemote().sendObject(response);
				break;
			default:
				sessions.remove(session);
				session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "PROTOCOL_ERROR"));
		}
	}

	private boolean verify(Message message) {
		return USERNAME.equals(message.getData().get(Message.USERNAME_KEY)) &&
            PASSWORD.equals(message.getData().get(Message.PASSWORD_KEY));
	}

	@OnClose
	public void onClose(final Session session) throws IOException {
		log.debug("NORMAL_CLOSURE");
		sessions.remove(session);
		session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "NORMAL_CLOSURE"));
	}
}
