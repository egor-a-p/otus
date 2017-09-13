package ru.otus.web.endpoint;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author e.petrov. Created 09 - 2017.
 */
public class ServerEndpointInstanceFactory<T> implements ServerEndpointConfig {
	private final Function<Class<T>, T> endpointFactory;
	private final Class<T> clazz;

	public ServerEndpointInstanceFactory(Class<T> clazz, Function<Class<T>, T> endpointFactory) {
		this.endpointFactory = endpointFactory;
		this.clazz = clazz;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Configurator getConfigurator() {
		return new Configurator() {
			@Override
			public <E> E getEndpointInstance(Class<E> endpointClass) throws InstantiationException {
				return (E) endpointFactory.apply((Class<T>) endpointClass);
			}
		};
	}

	@Override
	public Class<?> getEndpointClass() {
		return clazz;
	}

	@Override
	public String getPath() {
		return null;
	}

	@Override
	public List<String> getSubprotocols() {
		return null;
	}

	@Override
	public List<Extension> getExtensions() {
		return null;
	}

	@Override
	public List<Class<? extends Encoder>> getEncoders() {
		return null;
	}

	@Override
	public List<Class<? extends Decoder>> getDecoders() {
		return null;
	}

	@Override
	public Map<String, Object> getUserProperties() {
		return null;
	}
}
