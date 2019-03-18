package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemBusinessService {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private RestaurantDao resturantDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantItemEntity> getItemsByRestaurantID(final String resturantUUID)throws RestaurantNotFoundException {

        RestaurantEntity restaurantEntity = resturantDao.getResaurantById(resturantUUID);
        if(restaurantEntity == null){
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }
        else {
            List<RestaurantItemEntity> restaurantItemEntityList = itemDao.getPopularItemByResturantID(restaurantEntity);
            return restaurantItemEntityList;
        }
    }
}
