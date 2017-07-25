package ru.otus.dao;

import ru.otus.entity.PhoneDataSet;

import java.util.List;

/**
 * Created by egor on 25.07.17.
 */
public interface PhoneDataSetDAO extends DataSetDAO<PhoneDataSet> {

    List<PhoneDataSet> readByCode(int code);

}
