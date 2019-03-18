package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;
    public List<CategoryEntity> getCategories(){
        return entityManager.createNamedQuery("getAllCategories",CategoryEntity.class).getResultList();
    }

    public  CategoryEntity categoryById(final String uuid){
        try{
            return entityManager.createNamedQuery("getCategoryById",CategoryEntity.class).setParameter("uuid",uuid)
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }

    }
}
