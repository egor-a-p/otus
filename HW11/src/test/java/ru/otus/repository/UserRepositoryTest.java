package ru.otus.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import ru.otus.entity.AddressEntity;
import ru.otus.entity.PhoneEntity;
import ru.otus.entity.UserEntity;
import ru.otus.persistence.PersistenceUnit;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


/**
 * author: egor, created: 15.08.17.
 */
public class UserRepositoryTest {

    private static UserRepository repository;

    @BeforeClass
    public static void initialize() {
        PersistenceUnit.initialize();
        repository = new UserRepositoryImpl(PersistenceUnit.createEntityManager());
    }

    @AfterClass
    public static void destroy() {
        PersistenceUnit.destroy();
    }

    @Rule
    public TestName testName = new TestName();

    @Test
    public void shouldSaveAndDeleteUser() {
        //given
        UserEntity user = create();
        long size = repository.size();

        //when
        user = repository.save(user);

        //then
        assertEquals(size + 1, repository.size());
        assertNotNull(user.getId());

        //when
        repository.delete(user);

        //then
        assertEquals(size, repository.size());
    }

    @Test
    public void shouldSaveAndFindUser() {
        //given
        UserEntity saved = repository.save(create());

        //when
        UserEntity loadedById = repository.findOne(saved.getId());
        UserEntity loadedByName = repository.findByName(saved.getName());

        //then
        assertEquals(saved, loadedById);
        assertEquals(loadedByName, saved);

        repository.delete(saved);
    }

    @Test
    public void shouldSaveAllAndDeleteAllUser() {
        //given
        List<UserEntity> users = IntStream.range(0, 10).boxed().map(i -> create()).collect(Collectors.toList());
        long size = repository.size();

        //when
        Iterable<UserEntity> savedUsers = repository.save(users);

        //then
        assertEquals(size + 10, repository.size());
        savedUsers.forEach(u -> assertNotNull(u.getId()));

        //when
        repository.delete(savedUsers);

        //then
        assertEquals(size, repository.size());
    }

    @Test
    public void shouldSaveAllAndFindAllUser() {
        //given
        Iterable<UserEntity> savedUsers = repository.save(IntStream.range(0, 10).boxed().map(i -> create()).collect(Collectors.toList()));

        //when
        Iterable<UserEntity> allUsers = repository.findAll();
        Iterable<UserEntity> loadedUsers = repository.findAll(StreamSupport.stream(savedUsers.spliterator(), false)
                .map(UserEntity::getId)
                .collect(Collectors.toList()));

        //then
        assertThat(loadedUsers, is(savedUsers));
        assertThat(allUsers, hasItems(StreamSupport.stream(savedUsers.spliterator(), false).toArray(UserEntity[]::new)));
        savedUsers.forEach(u -> assertTrue(repository.contains(u.getId())));

        repository.delete(allUsers);
    }

    private UserEntity create() {
        return create(testName);
    }


    public static UserEntity create(TestName testName) {
        UserEntity user = new UserEntity();
        AddressEntity address = new AddressEntity();
        address.setStreet("street for test: " + testName.getMethodName() + 1000 * Math.random());
        address.setIndex(testName.getMethodName().length());
        user.setUserAddress(address);

        List<PhoneEntity> phones = new ArrayList<>();

        IntStream.range(0, testName.hashCode() % 5).forEach(i -> {
            PhoneEntity phone = new PhoneEntity();
            phone.setCode(901 + i);
            phone.setNumber("" + testName.hashCode() % 10000000 + i);
            phone.setUser(user);
            phones.add(phone);
        });

        user.setName("user for test: " + testName.getMethodName() + 1000 * Math.random());
        user.setUserAddress(address);
        user.setPhones(phones);

        return user;
    }
}
