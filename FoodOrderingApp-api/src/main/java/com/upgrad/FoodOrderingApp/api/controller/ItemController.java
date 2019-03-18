package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.ItemListResponse;
import com.upgrad.FoodOrderingApp.service.businness.ItemBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class ItemController {

    //Get Top 5 Items by Popularity - “/item/restaurant/{restaurant_id}

    @Autowired
    ItemBusinessService itemBusinessService;

    // Get Top 5 Items by Popularity ...

    @RequestMapping(method = RequestMethod.GET, path = "/item/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<ItemListResponse>> getPopularItemByResturantID(@PathVariable("restaurant_id") final String uuid) throws RestaurantNotFoundException {

        final List<RestaurantItemEntity> restaurantItemEntityList = itemBusinessService.getItemsByRestaurantID(uuid);

        List<ItemListResponse> items = new ArrayList<>();
        for(RestaurantItemEntity object : restaurantItemEntityList){

            ItemListResponse itemListResponse = new ItemListResponse();

            ItemList itemList = new ItemList();
            itemList.id(UUID.fromString(object.getItem().getUuid()));
            ItemList.ItemTypeEnum itemTypeEnum;
            if(object.getItem().getType().equals("0")){
                itemTypeEnum = ItemList.ItemTypeEnum.VEG;
            }else{
                itemTypeEnum = ItemList.ItemTypeEnum.NON_VEG;
            }
            itemList.itemType(itemTypeEnum);
            itemList.itemName(object.getItem().getItenName());
            itemList.price(object.getItem().getPrice());
            itemListResponse.add(itemList);
            items.add(itemListResponse);
        }
        return new ResponseEntity<List<ItemListResponse>>(items, HttpStatus.OK);
    }
}
