package ru.otus.web.socket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;

/**
 * @author e.petrov. Created 09 - 2017.
 */
@Slf4j
@ServerEndpoint(value = "/cache")
public class CacheEndpoint {

	@OnOpen
	public void onOpen(final Session session) {

	}

	@OnMessage
	public void onMessage(final Session session) {

	}

	@OnClose
	public void onClose(final Session session) {

	}

	@OnError
	public void onError(final Session session) {

	}
}
