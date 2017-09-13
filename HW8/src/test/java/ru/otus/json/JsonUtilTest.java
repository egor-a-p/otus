package ru.otus.json;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import ru.otus.json.vo.Group;
import ru.otus.json.vo.User;

public class JsonUtilTest {

    private Gson gson = new Gson();

    @Test
    public void shouldTransformNull() {
        //given
        Object o = null;

        //when
        String json = JsonUtil.toJson(o);

        //then
        Assert.assertEquals(gson.toJson(o), json);
    }

    @Test
    public void shouldTransformPrimitive() {
        //given
        int i = 10;

        //when
        String json = JsonUtil.toJson(i);

        //then
        Assert.assertEquals(gson.toJson(i), json);
    }

    @Test
    public void shouldTransformPrimitiveChar() {
        //given
        char c = 'c';

        //when
        String json = JsonUtil.toJson(c);

        //then
        Assert.assertEquals(gson.toJson(c), json);
    }

    @Test
    public void shouldTransformWrapperChar() {
        //given
        Character c = 'c';

        //when
        String json = JsonUtil.toJson(c);

        //then
        Assert.assertEquals(gson.toJson(c), json);
    }

    @Test
    public void shouldTransformPrimitiveDouble() {
        //given
        double d = 1.05;

        //when
        String json = JsonUtil.toJson(d);

        //then
        Assert.assertEquals(gson.toJson(d), json);
    }

    @Test
    public void shouldTransformWrapperDouble() {
        //given
        Double d = 1.05;

        //when
        String json = JsonUtil.toJson(d);

        //then
        Assert.assertEquals(gson.toJson(d), json);
    }

    @Test
    public void shouldTransformPrimitiveBoolean() {
        //given
        boolean b = true;

        //when
        String json = JsonUtil.toJson(b);

        //then
        Assert.assertEquals(gson.toJson(b), json);
    }

    @Test
    public void shouldTransformWrapper() {
        //given
        String f = "false";

        //when
        String json = JsonUtil.toJson(Boolean.valueOf(f));

        //then
        Assert.assertEquals(gson.toJson(Boolean.valueOf(f)), json);
    }

    @Test
    public void shouldTransformString() {
        //given
        String s = "abc";

        //when
        String json = JsonUtil.toJson(s);

        //then
        Assert.assertEquals(gson.toJson(s), json);
    }

    @Test
    public void shouldTransformArrayOfPrimitives() {
        //given
        char[] chars = new char[]{'a', 'b', 'c'};

        //when
        String json = JsonUtil.toJson(chars);

        //then
        Assert.assertEquals(gson.toJson(chars), json);
    }

    @Test
    public void shouldTransformArrayOfWrappers() {
        //given
        int[] integers = new int[]{1, 2, 3};

        //when
        String json = JsonUtil.toJson(integers);

        //then
        Assert.assertEquals(gson.toJson(integers), json);
    }

    @Test
    public void shouldTransformCollectionOfStrings() {
        //given
        List<String> strings = Arrays.asList("abc", "bcd", "cde");

        //when
        String json = JsonUtil.toJson(strings);

        //then
        Assert.assertEquals(gson.toJson(strings), json);
    }

	@Test
	public void shouldTransformCollectionOfIntegers() {
		//given
		List<Integer> integers = Arrays.asList(1, 2, 3);

		//when
		String json = JsonUtil.toJson(integers);

		//then
		Assert.assertEquals(gson.toJson(integers), json);
	}

	@Test
	public void shouldTransformCollectionOfArraysOfChars() {
		//given
		List<char[]> characters = Arrays.asList(new char[]{'a', 'b', 'c'}, new char[]{'c', 'd' , 'e'});

		//when
		String json = JsonUtil.toJson(characters);

		//then
		Assert.assertEquals(gson.toJson(characters), json);
	}

	@Test
	public void shouldTransformCollectionOfCharacters() {
		//given
		List<Character> characters = Arrays.asList('a', 'b', 'c');

		//when
		String json = JsonUtil.toJson(characters);

		//then
		Assert.assertEquals(gson.toJson(characters), json);
	}

    @Test
    public void shouldTransformMap() {
        //given
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "abc");
        map.put(2, "abcd");
        map.put(3, "abcde");

        //when
        String json = JsonUtil.toJson(map);

        //then
        Assert.assertEquals(gson.toJson(map), json);
    }

    @Test
    public void shouldTransformVO() {

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

        Assert.assertEquals(JSONValue.parse(new Gson().toJson(group)), JSONValue.parse(JsonUtil.toJson(group)));

    }
}
