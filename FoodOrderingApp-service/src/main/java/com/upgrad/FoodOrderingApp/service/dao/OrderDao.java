package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.*;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager entityManager;
    public CouponEntity getCouponName (final String couponName){
        try{
            return entityManager.createNamedQuery("getCouponByname", CouponEntity.class).setParameter("couponName",couponName)
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    public CouponEntity getCouponById(final String uuid){
        try{
            return entityManager.createNamedQuery("getCouponById",CouponEntity.class).setParameter("uuid",uuid)
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    public AddressEntity getAddressById(final String uuid){
        try{
            return entityManager.createNamedQuery("getAddressUuid",AddressEntity.class).setParameter("uuid",uuid)
                    .getSingleResult();

        }catch(NoResultException nre){
            return null;

        }
    }

    public PaymentEntity getPaymentId (final String uuid){
        try{
            return entityManager.createNamedQuery("paymentById",PaymentEntity.class).setParameter("uuid",uuid)
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    public RestaurantEntity getRestaurantId (final String uuid){
        try{
          return   entityManager.createNamedQuery("getRestById",RestaurantEntity.class).setParameter("uuid",uuid)
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    public OrderEntity saveOrders (final OrderEntity orderEntity){
        entityManager.persist(orderEntity);
        return orderEntity;
    }

    public List<OrderEntity> getOrderByCustomer(final CustomerEntity customerEntity){
        try{
            return entityManager.createNamedQuery("getOrderByCustomer", OrderEntity.class).setParameter("customer",customerEntity).getResultList();
        } catch (NoResultException nre){
            return null;
        }
    }
    public ItemEntity getItem(final String uuid){
        try{
           return entityManager.createNamedQuery("getItemById",ItemEntity.class).setParameter("uuid",uuid)
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }
}
