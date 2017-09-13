package ru.otus.dao;

import ru.otus.entity.PhoneEntity;

import java.util.List;

/**
 * Created by egor on 25.07.17.
 */
public interface PhoneDataSetDAO extends DataSetDAO<PhoneEntity> {

    List<PhoneEntity> readByCode(int code);

}
