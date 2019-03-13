package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;
    
    public AddressEntity createAddress (final AddressEntity addressEntity){
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    public StateEntity getStateByUuid (final String stateUuid){
        try{
            return entityManager.createNamedQuery("stateByUuid", StateEntity.class).setParameter("stateUuid",stateUuid)
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }

    }
}
