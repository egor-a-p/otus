package ru.otus.dao;

import java.util.List;

import javax.persistence.EntityManager;

import ru.otus.entity.AddressDataSet;
import ru.otus.entity.UserDataSet;

/**
 * Created by egor on 25.07.17.
 */
public class AddressDataSetDAOHibernateImpl extends AbstractDataSetHibernateDAO<AddressDataSet> implements AddressDataSetDAO {

    public AddressDataSetDAOHibernateImpl(EntityManager em) {
        super(em, AddressDataSet.class);
    }

    @Override
    public List<AddressDataSet> readAll() {
        return em.createNamedQuery("AddressDataSet.readAll", entityClass)
                 .getResultList();
    }

}
