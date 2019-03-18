package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantItemEntity> getPopularItemByResturantID(final RestaurantEntity restaurantEntity){

        //return null;
        return entityManager.createNamedQuery("getItemByRestaurantId",RestaurantItemEntity.class).setParameter("restaurant", restaurantEntity)
                .getResultList();
    }
}
