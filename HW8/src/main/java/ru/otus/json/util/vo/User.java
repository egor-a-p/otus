package ru.otus.json.util.vo;

import lombok.Data;

/**
 * Created by egor on 02.08.17.
 */
@Data
public class User {
    private String name;
    private int age;
    private Boolean deleted;
}
