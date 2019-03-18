package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;
    public CustomerEntity createCustomer (final CustomerEntity customerEntity){
        entityManager.persist(customerEntity);
        return customerEntity;

    }

    public CustomerEntity contactCheck (final CustomerEntity customerEntity){
        try{
            return entityManager.createNamedQuery("contactNumber", CustomerEntity.class).setParameter("number",customerEntity.getContactNumber())
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    public CustomerEntity getUserByContact(final String contactNumber){
        try{
            return entityManager.createNamedQuery("userByContact",CustomerEntity.class).setParameter("ContactNumber",contactNumber)
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    public CustomerAuthTokenEntity createAuthToken (final CustomerAuthTokenEntity customerAuthTokenEntity){
        entityManager.persist(customerAuthTokenEntity);
        return customerAuthTokenEntity;
    }

    public CustomerAuthTokenEntity getAuthToken (final String authorization){
       try{
           return entityManager.createNamedQuery("userByToken",CustomerAuthTokenEntity.class).setParameter("accessToken",authorization)
                   .getSingleResult();
       }catch(NoResultException nre){
           return null;
       }


    }

    public CustomerAuthTokenEntity updateAuthToken (final CustomerAuthTokenEntity customerAuthToken){
           entityManager.merge(customerAuthToken);
           return customerAuthToken;
    }

    public  CustomerEntity updateCustomerDetails (final CustomerEntity customerEntity){
         return entityManager.merge(customerEntity);

    }

    public CustomerEntity updateCustomerPassword (final CustomerEntity customer){
        return entityManager.merge(customer);
    }

}
