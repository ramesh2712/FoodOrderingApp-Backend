package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
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

   @RequestMapping(method = RequestMethod.GET, path ="/restaurant/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantListResponse>> getRestaurantsById(@PathVariable("category_id") final String categoryUuid) throws CategoryNotFoundException {
       CategoryEntity category = restaurantBusinessService.getRestauantByCatId(categoryUuid);
       List<RestaurantCategoryEntity> resturantByCategory = category.getCategoryRestauant();

       List<RestaurantListResponse> listRestaurantsResponse =  new ArrayList<>();
       Integer aa =resturantByCategory.size();
       Integer bb =aa;

       String categoryName ;
       for(RestaurantCategoryEntity r : resturantByCategory){
           RestaurantList restList = new RestaurantList();
           RestaurantEntity restaurant = r.getRestaurant();
           restList.restaurantName(restaurant.getRestaurantName());
           String name = restaurant.getRestaurantName();
           String name1= name;
           restList.id(UUID.fromString(restaurant.getUuid()));
           restList.photoURL(restaurant.getPhotoUrl());
           restList.customerRating(restaurant.getCustomer_rating());
           restList.averagePrice(restaurant.getAveragePriceForTwo());
           restList.numberCustomersRated(restaurant.getNoCustomersRated());

           AddressEntity address = restaurant.getRestAddress();
           RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
           restaurantDetailsResponseAddress.id(UUID.fromString(address.getUuid()));
           restaurantDetailsResponseAddress.flatBuildingName(address.getFlatBuilNumber());
           restaurantDetailsResponseAddress.locality(address.getLocality());
           restaurantDetailsResponseAddress.city(address.getCity());
           restaurantDetailsResponseAddress.pincode(address.getPinCode());

           RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
           StateEntity state = address.getState();
           restaurantDetailsResponseAddressState.id(UUID.fromString(state.getUuid()));
           restaurantDetailsResponseAddressState.stateName(state.getStateName());
            restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);

           List<RestaurantCategoryEntity> getCategoriesByRestaurant =  r.getRestaurant().getRestaurantCategory();

          List<String>  stringList = new ArrayList<>();
           for(RestaurantCategoryEntity r1:getCategoriesByRestaurant){
                stringList.add(r1.getCategory().getCategory_name());
           }

           categoryName = String.join(", ",stringList);
            restList.address(restaurantDetailsResponseAddress);
           restList.categories(categoryName);
            List<RestaurantList> restaurantLists = new ArrayList<>();
            restaurantLists.add(restList);
           RestaurantListResponse restaurantListResponse = new RestaurantListResponse();
            restaurantListResponse.restaurants(restaurantLists);
            listRestaurantsResponse.add(restaurantListResponse);
       }
    return new ResponseEntity<List<RestaurantListResponse>>(listRestaurantsResponse,HttpStatus.OK);

    }



}
