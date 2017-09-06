package ru.otus.web;

import java.util.concurrent.TimeUnit;

import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import ru.otus.emulator.WorkEmulator;
import ru.otus.entity.UserEntity;
import ru.otus.persistence.PersistenceUnit;
import ru.otus.repository.UserRepositoryImpl;
import ru.otus.service.UserService;
import ru.otus.service.UserServiceImpl;
import ru.otus.web.registry.SessionRegistry;
import ru.otus.web.endpoint.CacheEndpoint;
import ru.otus.web.endpoint.ServerEndpointInstanceFactory;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class WebApp {
	private final static int PORT = 8090;
	private final static String ADMIN_PASSWORD = "admin";
	private final static String ADMIN_NAME = "admin";
	private final static String PUBLIC_HTML = "HW12/src/main/webapp";
	//private final static String PUBLIC_HTML = "webapp"; для сборки jar
	private final static int WORK_EMULATORS_COUNT = 10;

	public static void main(String[] args) throws Exception {
		PersistenceUnit.initialize();
		UserServiceImpl userService = new UserServiceImpl(new UserRepositoryImpl(PersistenceUnit.createEntityManager()));
		prepareDB(userService);
		try {
			SessionRegistry registry = new SessionRegistry(userService);
			userService.registerListener(registry);

			ServerEndpointConfig config = new ServerEndpointInstanceFactory<>(CacheEndpoint.class, c -> new CacheEndpoint(registry));

			ResourceHandler resourceHandler = new ResourceHandler();
			resourceHandler.setResourceBase(PUBLIC_HTML);

			ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
			context.addServlet(DefaultServlet.class, "/");

			Server server = new Server(PORT);
			server.setHandler(new HandlerList(resourceHandler, context));

			ServerContainer container = WebSocketServerContainerInitializer.configureContext(context);
			container.addEndpoint(config);

			server.start();
			emulateWork(userService);
			server.join();
		} finally {
			PersistenceUnit.destroy();
		}
	}

	private static void prepareDB(UserService userService) {
		userService.delete(userService.loadAll());
		UserEntity admin = new UserEntity();
		admin.setName(ADMIN_NAME);
		admin.setPassword(ADMIN_PASSWORD);
		userService.save(admin);
	}

	private static void emulateWork(UserService userService) throws InterruptedException {
		for (int i = 0; i < WORK_EMULATORS_COUNT; i++) {
			new WorkEmulator(userService).start();
			Thread.sleep(TimeUnit.SECONDS.toMillis(5));
		}
	}
}
