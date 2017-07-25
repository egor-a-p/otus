package ru.otus.dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.otus.persistence.PersistenceUnit;

@RunWith(Suite.class)
@Suite.SuiteClasses({UserDataSetDAOTest.class})
public class DAOTestSuite {
    @BeforeClass
    public static void initialize() {
        PersistenceUnit.initialize();
    }

    @AfterClass
    public static void destroy() {
        PersistenceUnit.destroy();
    }
}
