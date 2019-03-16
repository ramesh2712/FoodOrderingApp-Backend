package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.CategoryList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.MarshalException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantBusinessService restaurantBusinessService;

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{reastaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantDetailsResponse>> getRestaurantByName (@PathVariable("reastaurant_name") final String restaurantName){
        List<RestaurantEntity> resturants = restaurantBusinessService.getRestaurantByName(restaurantName);
        List<CategoryEntity> category = restaurantBusinessService.getCategories();

        List<RestaurantDetailsResponse> restaurantDetailsResponsesList = new ArrayList<>();
        for(RestaurantEntity r :resturants){
            RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();

            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
            restaurantDetailsResponseAddress.id(UUID.fromString(r.getRestAddress().getUuid()));
            restaurantDetailsResponseAddress.flatBuildingName(r.getRestAddress().getFlatBuilNumber());
            restaurantDetailsResponseAddress.locality(r.getRestAddress().getLocality());
            restaurantDetailsResponseAddress.city(r.getRestAddress().getCity());
            restaurantDetailsResponseAddress.pincode(r.getRestAddress().getPinCode());

            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
            restaurantDetailsResponseAddressState.stateName(r.getRestAddress().getState().getStateName());
            restaurantDetailsResponseAddressState.id(UUID.fromString(r.getRestAddress().getState().getUuid()));
            restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);
            List<CategoryList> catList = new ArrayList<>();
            for(CategoryEntity c : category){
                CategoryList li = new CategoryList();
               li.categoryName(c.getCategory_name());
                catList.add(li);
            }

            restaurantDetailsResponse.categories(catList);
            restaurantDetailsResponse.id(UUID.fromString(r.getUuid()));
            restaurantDetailsResponse.restaurantName(r.getRestaurantName());
            restaurantDetailsResponse.photoURL(r.getPhotoUrl());
            restaurantDetailsResponse.customerRating(r.getCustomer_rating());
            restaurantDetailsResponse.averagePrice(r.getAveragePriceForTwo());
            restaurantDetailsResponse.numberCustomersRated(r.getNoCustomersRated());
            restaurantDetailsResponse.address(restaurantDetailsResponseAddress);
            restaurantDetailsResponsesList.add(restaurantDetailsResponse);
        }
return new ResponseEntity<List<RestaurantDetailsResponse>>(restaurantDetailsResponsesList,HttpStatus.OK);


    }


}
