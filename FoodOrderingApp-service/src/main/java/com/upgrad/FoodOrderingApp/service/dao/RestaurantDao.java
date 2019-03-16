package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantEntity> getRestaurant (final String restaurantName){
        return entityManager.createNamedQuery("getRestByName",RestaurantEntity.class).setParameter("restName","%"+restaurantName+"%")
                .getResultList();
    }

    public List<CategoryEntity> getCategory (){
        return entityManager.createNamedQuery("getCategory",CategoryEntity.class).getResultList();
    }
}
