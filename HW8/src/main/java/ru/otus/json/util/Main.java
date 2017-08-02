package ru.otus.json.util;

import ru.otus.json.util.vo.Group;
import ru.otus.json.util.vo.User;

import java.util.Arrays;

/**
 * Created by egor on 02.08.17.
 */
public class Main {
    public static void main(String[] args) {
        User user1 = new User();
        user1.setAge(12);
        user1.setName("test name 1");
        user1.setDeleted(false);

        User user2 = new User();
        user2.setAge(13);
        user2.setName("test name 2");
        user2.setDeleted(true);

        Group group = new Group();
        group.setId('c');
        group.setActive(true);
        group.setName("test group");
        group.setSomeNumbers(new Integer[]{1, 2, 3, 4});
        group.setUsers(Arrays.asList(user1, user2));

        String json = JsonUtil.transform(group);

        System.out.println(json);
    }
}
