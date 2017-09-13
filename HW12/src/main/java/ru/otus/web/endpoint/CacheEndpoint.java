package ru.otus.web.endpoint;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;
import ru.otus.web.protocol.Message;
import ru.otus.web.protocol.MessageDecoder;
import ru.otus.web.protocol.MessageEncoder;
import ru.otus.web.registry.SessionRegistry;

/**
 * @author e.petrov. Created 09 - 2017.
 */
@Slf4j
@ServerEndpoint(value = "/cache", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class CacheEndpoint {

	private final SessionRegistry registry;

	public CacheEndpoint(SessionRegistry registry) {
		this.registry = registry;
	}

	@OnOpen
	public void onOpen(final Session session) {
		log.debug("Open new session {}", session.getId());
	}

	@OnMessage
	public void onMessage(final Session session, final Message message) throws IOException, EncodeException {
		log.debug("Receive message {} from session {}", message, session.getId());
		registry.register(session, message);
	}

	@OnClose
	public void onClose(final Session session) throws IOException {
		registry.deregister(session);
	}

	@OnError
	public void onError(final Session session, Throwable t) throws IOException {
		log.error("Error in session " + session.getId(), t);
		registry.deregister(session);
	}
}
