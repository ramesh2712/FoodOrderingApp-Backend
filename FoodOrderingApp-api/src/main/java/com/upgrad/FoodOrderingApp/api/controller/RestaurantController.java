package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.AuthenticationService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.rmi.MarshalException;
import java.util.*;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantBusinessService restaurantBusinessService;

    @Autowired
    private AuthenticationService authenticationService;


    //Get All Restaurants Endpoint ....

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantListResponse>> getAllRestaurants(){

        List<RestaurantEntity> restaurants = restaurantBusinessService.getAllRestaurants();
        List<RestaurantListResponse> restaurantListResponsesList = new ArrayList<>();

        String categoryName = "";

        for(RestaurantEntity r : restaurants){

            RestaurantList restaurantList = new RestaurantList();
            restaurantList.id(UUID.fromString(r.getUuid()));
            restaurantList.restaurantName(r.getRestaurantName());
            restaurantList.photoURL(r.getPhotoUrl());
            restaurantList.customerRating(r.getCustomer_rating());
            restaurantList.averagePrice(r.getAveragePriceForTwo());
            restaurantList.numberCustomersRated(r.getNoCustomersRated());

            AddressEntity address = r.getRestAddress();
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
            restaurantDetailsResponseAddress.id(UUID.fromString(address.getUuid()));
            restaurantDetailsResponseAddress.flatBuildingName(address.getFlatBuilNumber());
            restaurantDetailsResponseAddress.locality(address.getLocality());
            restaurantDetailsResponseAddress.city(address.getCity());
            restaurantDetailsResponseAddress.pincode(address.getPinCode());

            StateEntity state = address.getState();
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
            restaurantDetailsResponseAddressState.id(UUID.fromString(state.getUuid()));
            restaurantDetailsResponseAddressState.stateName(state.getStateName());

            restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);
            restaurantList.address(restaurantDetailsResponseAddress);

            List<RestaurantCategoryEntity> categoryList = r.getRestaurantCategory();
            Collections.sort(categoryList, new Comparator<RestaurantCategoryEntity>() {
                @Override
                public int compare(RestaurantCategoryEntity u1, RestaurantCategoryEntity  u2) {
                    return u1.getCategory().getCategory_name().compareTo(u2.getCategory().getCategory_name());
                }
            });

            List<String>  stringList = new ArrayList<>();
            for(RestaurantCategoryEntity r1:categoryList){
                stringList.add(r1.getCategory().getCategory_name());
            }
            categoryName = String.join(", ",stringList);

            restaurantList.categories(categoryName);
            List<RestaurantList> restaurantLists = new ArrayList<>();
            restaurantLists.add(restaurantList);
            RestaurantListResponse restaurantListResponse = new RestaurantListResponse();
            restaurantListResponse.restaurants(restaurantLists);
            restaurantListResponsesList.add(restaurantListResponse);

        }
        return new ResponseEntity<List<RestaurantListResponse>>(restaurantListResponsesList,HttpStatus.OK);
    }

    //  Get Restaurant by Name Endpoint ....

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{reastaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantListResponse>> getRestaurantByName (@PathVariable("reastaurant_name") final String restaurantName) throws RestaurantNotFoundException{

        List<RestaurantEntity> resturants = restaurantBusinessService.getRestaurantByName(restaurantName);
        List<RestaurantListResponse> restaurantListResponsesList = new ArrayList<>();

        String categoryName = "";

        for(RestaurantEntity r :resturants){

            RestaurantList restaurantList = new RestaurantList();
            restaurantList.id(UUID.fromString(r.getUuid()));
            restaurantList.restaurantName(r.getRestaurantName());
            restaurantList.photoURL(r.getPhotoUrl());
            restaurantList.customerRating(r.getCustomer_rating());
            restaurantList.averagePrice(r.getAveragePriceForTwo());
            restaurantList.numberCustomersRated(r.getNoCustomersRated());

            AddressEntity address = r.getRestAddress();
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
            restaurantDetailsResponseAddress.id(UUID.fromString(address.getUuid()));
            restaurantDetailsResponseAddress.flatBuildingName(address.getFlatBuilNumber());
            restaurantDetailsResponseAddress.locality(address.getLocality());
            restaurantDetailsResponseAddress.city(address.getCity());
            restaurantDetailsResponseAddress.pincode(address.getPinCode());

            StateEntity state = address.getState();
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
            restaurantDetailsResponseAddressState.id(UUID.fromString(state.getUuid()));
            restaurantDetailsResponseAddressState.stateName(state.getStateName());

            restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);
            restaurantList.address(restaurantDetailsResponseAddress);

            List<RestaurantCategoryEntity> categoryList = r.getRestaurantCategory();
            Collections.sort(categoryList, new Comparator<RestaurantCategoryEntity>() {
                @Override
                public int compare(RestaurantCategoryEntity u1, RestaurantCategoryEntity  u2) {
                    return u1.getCategory().getCategory_name().compareTo(u2.getCategory().getCategory_name());
                }
            });

            List<String>  stringList = new ArrayList<>();
            for(RestaurantCategoryEntity r1:categoryList){
                stringList.add(r1.getCategory().getCategory_name());
            }
            categoryName = String.join(", ",stringList);

            restaurantList.categories(categoryName);
            List<RestaurantList> restaurantLists = new ArrayList<>();
            restaurantLists.add(restaurantList);
            RestaurantListResponse restaurantListResponse = new RestaurantListResponse();
            restaurantListResponse.restaurants(restaurantLists);
            restaurantListResponsesList.add(restaurantListResponse);
        }
        return new ResponseEntity<List<RestaurantListResponse>>(restaurantListResponsesList,HttpStatus.OK);
    }

    // Get Restaurants by Category Id endpoint .....
   @RequestMapping(method = RequestMethod.GET, path ="/restaurant/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantListResponse>> getRestaurantsByCategoryId(@PathVariable("category_id") final String categoryUuid) throws CategoryNotFoundException {
       CategoryEntity category = restaurantBusinessService.getRestauantByCatId(categoryUuid);
       List<RestaurantCategoryEntity> resturantByCategory = category.getCategoryRestauant();

       List<RestaurantListResponse> listRestaurantsResponse =  new ArrayList<>();
       Integer aa =resturantByCategory.size();
       Integer bb =aa;

       String categoryName = "";
       for(RestaurantCategoryEntity r : resturantByCategory){
           RestaurantList restList = new RestaurantList();
           RestaurantEntity restaurant = r.getRestaurant();
           restList.restaurantName(restaurant.getRestaurantName());

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

           Collections.sort(getCategoriesByRestaurant, new Comparator<RestaurantCategoryEntity>() {
               @Override
               public int compare(RestaurantCategoryEntity u1, RestaurantCategoryEntity  u2) {
                   return u1.getCategory().getCategory_name().compareTo(u2.getCategory().getCategory_name());
               }
           });

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

    // Get Restaurant by Restaurant ID endpoint ....
    @RequestMapping(method = RequestMethod.GET, path = "/api/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantsById (@PathVariable("restaurant_id") final String uuid) throws RestaurantNotFoundException {
        RestaurantEntity restaurant = restaurantBusinessService.getRestaurantsById(uuid);
        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();

        restaurantDetailsResponse.id(UUID.fromString(restaurant.getUuid()));
        restaurantDetailsResponse.restaurantName(restaurant.getRestaurantName());
        restaurantDetailsResponse.photoURL(restaurant.getPhotoUrl());
        restaurantDetailsResponse.customerRating(restaurant.getCustomer_rating());
        restaurantDetailsResponse.averagePrice(restaurant.getAveragePriceForTwo());
        restaurantDetailsResponse.numberCustomersRated(restaurant.getNoCustomersRated());

        AddressEntity address =restaurant.getRestAddress();
        RestaurantDetailsResponseAddress restaurantDetailsResponseAddress =new RestaurantDetailsResponseAddress();
        restaurantDetailsResponseAddress.id(UUID.fromString(address.getUuid()));
        restaurantDetailsResponseAddress.flatBuildingName(address.getFlatBuilNumber());
        restaurantDetailsResponseAddress.locality(address.getLocality());
        restaurantDetailsResponseAddress.city(address.getCity());
        restaurantDetailsResponseAddress.pincode(address.getPinCode());

        StateEntity state = address.getState();
        RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
        restaurantDetailsResponseAddressState.id(UUID.fromString(state.getUuid()));
        restaurantDetailsResponseAddressState.stateName(state.getStateName());
        restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);
        restaurantDetailsResponse.address(restaurantDetailsResponseAddress);

       List<RestaurantCategoryEntity> categoryList = restaurant.getRestaurantCategory();
        Collections.sort(categoryList, new Comparator<RestaurantCategoryEntity>() {
            @Override
            public int compare(RestaurantCategoryEntity u1, RestaurantCategoryEntity  u2) {
                return u1.getCategory().getCategory_name().compareTo(u2.getCategory().getCategory_name());
            }
        });

        List<CategoryList> categoryListList = new ArrayList<>();
        for(RestaurantCategoryEntity ra : categoryList){
            CategoryEntity category =  ra.getCategory();
            CategoryList categoryList1 = new CategoryList();
            categoryList1.id(UUID.fromString(category.getUuid()));
            categoryList1.categoryName(category.getCategory_name());

            List<ItemList> itemListList = new ArrayList<>();
            List<CategoryItemEntity> categoryItem =  category.getCategoryItemList();
            for(CategoryItemEntity ci : categoryItem){
                ItemList itemList = new ItemList();
                ItemEntity item = ci.getItem();
                itemList.id(UUID.fromString(item.getUuid()));
                itemList.itemName(item.getItenName());
                itemList.price(item.getPrice());
                ItemList.ItemTypeEnum itemEnum;
                if(item.getType().equals("0")){
                   itemEnum = ItemList.ItemTypeEnum.VEG;
                }
                else{
                    itemEnum = ItemList.ItemTypeEnum.NON_VEG;
                }
                itemList.itemType(itemEnum);
                itemListList.add(itemList);
            }
            categoryList1.itemList(itemListList);

           categoryListList.add(categoryList1);
        }
     restaurantDetailsResponse.categories(categoryListList);
        return new ResponseEntity<RestaurantDetailsResponse>(restaurantDetailsResponse,HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.PUT, path = "/api/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantDetails (@PathVariable("restaurant_id") final String uuid,
                                                                              @RequestParam("CustomerRating") final BigDecimal customerRating,
                                                                              @RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, RestaurantNotFoundException, InvalidRatingException {
        CustomerAuthTokenEntity customerAuthToken = authenticationService.authCustomerToken(accessToken);
        RestaurantEntity restaurantEntity = restaurantBusinessService.updateRating(uuid,customerRating);

        RestaurantUpdatedResponse restaurantUpdatedResponse =  new RestaurantUpdatedResponse();
        restaurantUpdatedResponse.   id(UUID.fromString (restaurantEntity.getUuid())).status("RESTAURANT RATING UPDATED SUCCESSFULLY");
        return new ResponseEntity<RestaurantUpdatedResponse>(restaurantUpdatedResponse, HttpStatus.OK);
    }

}
