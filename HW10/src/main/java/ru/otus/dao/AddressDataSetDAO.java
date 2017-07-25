package ru.otus.dao;

import ru.otus.entity.AddressDataSet;
import ru.otus.entity.UserDataSet;

import java.util.List;

/**
 * Created by egor on 25.07.17.
 */
public interface AddressDataSetDAO extends DataSetDAO<AddressDataSet> {

    List<AddressDataSet> readByUser(UserDataSet user);

}
