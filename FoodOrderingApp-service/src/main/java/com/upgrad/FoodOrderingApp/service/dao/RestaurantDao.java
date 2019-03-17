package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public CategoryEntity getRestByCatId(final String uuid){
        try{
            return entityManager.createNamedQuery("getCategoryById",CategoryEntity.class).setParameter("uuid",uuid)
                  .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    public RestaurantEntity getResaurantById(final String uuid){
        try{
            return entityManager.createNamedQuery("getRestById",RestaurantEntity.class).setParameter("uuid",uuid)
                    .getSingleResult();
       }catch (NoResultException nre){
            return null;
        }
    }

    public RestaurantEntity updateCuetomerRating(final RestaurantEntity restaurantEntity){
        entityManager.merge(restaurantEntity);
        return restaurantEntity;
    }

    public List<RestaurantEntity> getALlRest(){
        return entityManager.createNamedQuery("getAllRest",RestaurantEntity.class).getResultList();
    }
}
