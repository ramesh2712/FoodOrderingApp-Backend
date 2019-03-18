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
import java.math.RoundingMode;
import java.util.List;

@Service
public class RestaurantBusinessService {
    @Autowired
    private RestaurantDao resturantDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getRestaurantByName (final String restName)throws RestaurantNotFoundException {
        if(restName.length() == 0){
            throw new RestaurantNotFoundException("RNF-003","Restaurant name field should not be empty");
        }
        else {
            List<RestaurantEntity> resturants = resturantDao.getRestaurant(restName);
            return resturants;
        }
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
                throw new CategoryNotFoundException("CNF-002", "No category by this id");
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
                throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
            }
            return restaurant;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRating (final String restaurantuUid, final BigDecimal customer_rating) throws RestaurantNotFoundException, InvalidRatingException {

        if(restaurantuUid == null){
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        } else if((customer_rating == null) || (!(customer_rating.compareTo(BigDecimal.valueOf(1)) >= 0 && customer_rating.compareTo(BigDecimal.valueOf(5)) <= 0))){
            throw new InvalidRatingException("IRE-001","Restaurant should be in the range of 1 to 5");
        }else{
            RestaurantEntity restaurantEntity = resturantDao.getResaurantById(restaurantuUid);
            if(restaurantEntity == null){
                throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
            }
            BigDecimal rating = restaurantEntity.getCustomer_rating();
            BigDecimal totalRating = rating.add(customer_rating);
            BigDecimal divideByTwo = new BigDecimal("2");
            BigDecimal avgRating = totalRating.divide(divideByTwo,1,RoundingMode.CEILING);
            restaurantEntity.setCustomer_rating(avgRating);
            restaurantEntity.setNoCustomersRated(restaurantEntity.getNoCustomersRated()+1);
            return resturantDao.updateCuetomerRating(restaurantEntity);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getAllRestaurants (){
        return resturantDao.getAllRestaurant();
    }
}
