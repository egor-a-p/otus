package ru.otus.persistence;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Велосипед
 * <p>
 * Created by egor on 25.07.17.
 */
@Slf4j
public class PersistenceUnit {

    public static final String PERSISTENT_UNIT_NAME = "users_pu";
    public static final String ILLEGAL_STATE_MESSAGE = "Illegal state of PersistenceUnit: it's not initialized or already destroyed!";

    private static final AtomicReference<EntityManagerFactory> FACTORY_REFERENCE = new AtomicReference<>();

    public static void initialize() {
        FACTORY_REFERENCE.compareAndSet(null, Persistence.createEntityManagerFactory(PERSISTENT_UNIT_NAME));
    }

    public static EntityManager createEntityManager() {
        return Optional.ofNullable(FACTORY_REFERENCE.get())
                .orElseThrow(() -> {
                    RuntimeException e = new IllegalStateException(ILLEGAL_STATE_MESSAGE);
                    log.error("Can't create EntityManager:", e);
                    return e;
                })
                .createEntityManager();
    }

    public static void destroy() {
        Optional.ofNullable(FACTORY_REFERENCE.getAndSet(null))
                .orElseThrow(() -> {
                    RuntimeException e = new IllegalStateException(ILLEGAL_STATE_MESSAGE);
                    log.error("Can't destroy PersistenceUnit:", e);
                    return e;
                })
                .close();
    }
}
