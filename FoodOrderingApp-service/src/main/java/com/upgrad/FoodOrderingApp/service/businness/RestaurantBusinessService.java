package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantBusinessService {
    @Autowired
    private RestaurantDao resturantDao;

    public List<RestaurantEntity> getRestaurantByName (final String restName) {
       List<RestaurantEntity> resturants = resturantDao.getRestaurant(restName);
       return resturants;

    }

    public List<CategoryEntity> getCategories(){
        return resturantDao.getCategory();
    }

    public CategoryEntity getRestauantByCatId(final String categoryUuid) throws CategoryNotFoundException {

        if(categoryUuid == null){
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }else {
            CategoryEntity category = resturantDao.getRestByCatId(categoryUuid);
            if (category == null) {
                throw new CategoryNotFoundException("CNF-002)", "No category by this id");
            }
            return category;
        }

    }

    public RestaurantEntity getRestaurantsById(final String uuid) throws RestaurantNotFoundException {

        if(uuid == null){
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        }else {
            RestaurantEntity restaurant = resturantDao.getResaurantById(uuid);
            if(restaurant == null){
                throw new RestaurantNotFoundException("RNF-00","No restaurant by this id");
            }
            return restaurant;
        }

    }
}
