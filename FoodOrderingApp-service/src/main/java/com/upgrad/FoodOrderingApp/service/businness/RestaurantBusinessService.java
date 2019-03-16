package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
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
}
