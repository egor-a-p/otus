package ru.otus.dao;

import java.util.List;

import javax.persistence.EntityManager;

import ru.otus.entity.AddressEntity;

/**
 * Created by egor on 25.07.17.
 */
public class AddressDataSetDAOHibernateImpl extends AbstractDataSetHibernateDAO<AddressEntity> implements AddressDataSetDAO {

    public AddressDataSetDAOHibernateImpl(EntityManager em) {
        super(em, AddressEntity.class);
    }

    @Override
    public List<AddressEntity> readAll() {
        return em.createNamedQuery("AddressDataSet.readAll", entityClass)
                 .getResultList();
    }

}
