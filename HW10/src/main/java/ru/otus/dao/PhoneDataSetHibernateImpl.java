package ru.otus.dao;

import ru.otus.entity.PhoneDataSet;
import ru.otus.persistence.Manageable;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by egor on 25.07.17.
 */
public class PhoneDataSetHibernateImpl extends Manageable<PhoneDataSet> implements PhoneDataSetDAO {

    public PhoneDataSetHibernateImpl(EntityManager em) {
        super(em, PhoneDataSet.class);
    }

    @Override
    public List<PhoneDataSet> readByCode(int code) {
        return em.createNamedQuery("PhoneDataSet.readByCode", entityClass)
                 .setParameter("code", code)
                 .getResultList();
    }

    @Override
    public List<PhoneDataSet> readAll() {
        return em.createNamedQuery("PhoneDataSet.readAll", entityClass)
                .getResultList();
    }
}
