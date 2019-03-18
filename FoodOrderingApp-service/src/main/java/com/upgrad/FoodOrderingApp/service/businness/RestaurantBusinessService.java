package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RestaurantBusinessService {
    @Autowired
    private RestaurantDao resturantDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getRestaurantByName (final String restName) {
       List<RestaurantEntity> resturants = resturantDao.getRestaurant(restName);
       return resturants;

    }

    @Transactional(propagation = Propagation.REQUIRED)
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

    @Transactional(propagation = Propagation.REQUIRED)
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

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRating (final String restaurantuUid, final BigDecimal customer_rating) throws RestaurantNotFoundException, InvalidRatingException {

        if(restaurantuUid == null){
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        } else if((customer_rating == null) || (!(customer_rating.compareTo(BigDecimal.valueOf(0)) > 0 && customer_rating.compareTo(BigDecimal.valueOf(5)) < 0))){
            throw new InvalidRatingException("IRE-001","(Restaurant should be in the range of 1 to 5");
        }else{
            RestaurantEntity restaurantEntity = resturantDao.getResaurantById(restaurantuUid);
            if(restaurantEntity == null){
                throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
            }
            restaurantEntity.setCustomer_rating(customer_rating);
            restaurantEntity.setNoCustomersRated(restaurantEntity.getNoCustomersRated()+1);
            return resturantDao.updateCuetomerRating(restaurantEntity);
        }
    }

    public List<RestaurantEntity> getAllRestaurants (){
        return resturantDao.getALlRest();

    }
}
