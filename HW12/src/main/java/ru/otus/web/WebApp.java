package ru.otus.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import ru.otus.persistence.PersistenceUnit;
import ru.otus.web.socket.CacheEndpoint;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class WebApp {
	private final static int PORT = 8090;
	private final static String PUBLIC_HTML = "HW12/src/main/webapp";

	public static void main(String[] args) throws Exception {
		PersistenceUnit.initialize();
		try {
            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setResourceBase(PUBLIC_HTML);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.addServlet(DefaultServlet.class, "/cache");

            ServerContainer container = WebSocketServerContainerInitializer.configureContext(context);
            container.addEndpoint(CacheEndpoint.class);

            Server server = new Server(PORT);
            server.setHandler(new HandlerList(resourceHandler, context));

            server.start();
            server.join();
        } finally {
            PersistenceUnit.destroy();
        }
	}
}
