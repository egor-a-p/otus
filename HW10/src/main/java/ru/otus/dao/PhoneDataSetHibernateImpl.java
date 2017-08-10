package ru.otus.dao;

import ru.otus.entity.PhoneEntity;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by egor on 25.07.17.
 */
public class PhoneDataSetHibernateImpl extends AbstractDataSetHibernateDAO<PhoneEntity> implements PhoneDataSetDAO {

    public PhoneDataSetHibernateImpl(EntityManager em) {
        super(em, PhoneEntity.class);
    }

    @Override
    public List<PhoneEntity> readByCode(int code) {
        return em.createNamedQuery("PhoneDataSet.readByCode", entityClass)
                 .setParameter("code", code)
                 .getResultList();
    }

    @Override
    public List<PhoneEntity> readAll() {
        return em.createNamedQuery("PhoneDataSet.readAll", entityClass)
                .getResultList();
    }
}
