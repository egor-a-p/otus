package ru.otus.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ru.otus.entity.UserEntity;
import ru.otus.repository.UserRepository;
import ru.otus.repository.UserRepositoryTest;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * author: egor, created: 30.08.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl userService;

    @Rule
    public TestName testName = new TestName();

    @After
    public void after() {
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void loadTest() {
        //given
        UserEntity user = create();
        user.setId(10L);
        when(repository.findOne(user.getId())).thenReturn(user);

        //when
        UserEntity loaded = userService.load(user.getId());

        //then
        assertEquals(user, loaded);

        //when
        UserEntity cached = userService.load(user.getId());

        //then
        assertEquals(user, cached);
        verify(repository, only()).findOne(user.getId());
    }

    @Test
    public void saveTest() {
        //given
        UserEntity user = create();
        user.setId(11L);
        when(repository.save(user)).thenReturn(user);
        when(repository.findOne(user.getId())).thenReturn(user);

        //when
        UserEntity saved = userService.save(user);

        //then
        assertEquals(user, saved);
        verify(repository, only()).save(user);

        //when
        UserEntity loaded = userService.load(user.getId());
        UserEntity cached = userService.load(user.getId());

        //then
        verify(repository, never()).findOne(user.getId());
        assertEquals(user, loaded);
        assertEquals(cached, loaded);
    }

    @Test
    public void loadAllTest() {
        //given
        List<UserEntity> users = IntStream.range(0, 10).boxed().map(i -> {
            UserEntity userEntity = create();
            userEntity.setId(i.longValue());
            return userEntity;
        }).collect(Collectors.toList());

        when(repository.findAll(anyList())).thenReturn(users);

        //when
        Iterable<UserEntity> loaded = userService.load(users.stream().map(UserEntity::getId).collect(Collectors.toList()));

        //then
        assertThat(users, containsInAnyOrder(StreamSupport.stream(loaded.spliterator(), false).toArray(UserEntity[]::new)));

        //when
        UserEntity cached = userService.load(users.get(0).getId());

        //then
        assertEquals(users.get(0), cached);
        verify(repository, only()).findAll(anyList());
        verify(repository, never()).findOne(anyLong());
    }


    private UserEntity create() {
        return UserRepositoryTest.create(testName);
    }
}
