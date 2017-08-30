package ru.otus.json.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by egor on 02.08.17.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Group extends GroupParent {
    private String name;
    private Integer[] someNumbers;
    private List<User> users;
    private transient boolean active;
}
