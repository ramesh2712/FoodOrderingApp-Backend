package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

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

    public List<AddressEntity> getAddressList(final String accessToken){
       return entityManager.createNamedQuery("getAddress2",AddressEntity.class).setParameter("accessToken",accessToken)
                .getResultList();
    }

  /*  public void saveCustAdd(CustomerAddressEntity customerAddressEntity){
        entityManager.persist(customerAddressEntity);
    }*/

    public CustomerAddressEntity customerAddress(CustomerAddressEntity customerAddress){
       entityManager.persist(customerAddress);
       return customerAddress;
    }

    public CustomerAddressEntity getCustomerByAddressId(final AddressEntity addressEntity){
        try {
            return entityManager.createNamedQuery("getCustomerByAddressId",CustomerAddressEntity.class).
                    setParameter("address",addressEntity).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }
    public AddressEntity getAddressByuUid(final String uUid) {
       try {
           return entityManager.createNamedQuery("getAddressUuid", AddressEntity.class).setParameter("uuid", uUid)
                  .getSingleResult();
       } catch (NoResultException nre) {
          return null;
       }
    }

      public AddressEntity updateAddress(final AddressEntity addressEntity){
         return entityManager.merge(addressEntity);
      }

      public void deleteAddress(final AddressEntity addressEntity){
         entityManager.remove(addressEntity);
      }

      public List<StateEntity> getAllStates(){
        return entityManager.createNamedQuery("getStates",StateEntity.class).getResultList();
      }

}
