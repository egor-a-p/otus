package ru.otus.dao;

import ru.otus.entity.AddressDataSet;
import ru.otus.entity.UserDataSet;
import ru.otus.persistence.Manageable;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by egor on 25.07.17.
 */
public class AddressDataSetDAOHibernateImpl extends Manageable<AddressDataSet> implements AddressDataSetDAO {

    public AddressDataSetDAOHibernateImpl(EntityManager em) {
        super(em, AddressDataSet.class);
    }

    @Override
    public List<AddressDataSet> readAll() {
        return em.createNamedQuery("AddressDataSet.readAll", entityClass)
                 .getResultList();
    }

    @Override
    public List<AddressDataSet> readByUser(UserDataSet user) {
        return em.createNamedQuery("AddressDataSet.readByUser", entityClass)
                 .setParameter("user", user)
                 .getResultList();
    }
}
