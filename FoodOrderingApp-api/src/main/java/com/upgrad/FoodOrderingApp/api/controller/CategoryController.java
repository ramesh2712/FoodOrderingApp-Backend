package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryList;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.CategoryBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
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
public class CategoryController {
    @Autowired
    private CategoryBusinessService categoryBusinessService;

    @RequestMapping(method = RequestMethod.GET, path = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<CategoryListResponse>> getAllCategories (){

        List<CategoryEntity> categoryEntityList = categoryBusinessService.getAllCategory();

        List<CategoryListResponse> categoryListResponsesList = new ArrayList<>();
        for(CategoryEntity c: categoryEntityList){
            CategoryListResponse categoryListResponse = new CategoryListResponse();
            categoryListResponse.id(UUID.fromString(c.getUuid()));
            categoryListResponse.categoryName(c.getCategoryName());
            categoryListResponsesList .add(categoryListResponse);

        }
        return new ResponseEntity<List<CategoryListResponse>>(categoryListResponsesList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryByid (@PathVariable("category_id") final String uuid) throws CategoryNotFoundException {

        CategoryEntity categoryEntity = categoryBusinessService.getCategoryById(uuid);
        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse();
        categoryDetailsResponse.id(UUID.fromString(categoryEntity.getUuid()));
        categoryDetailsResponse.categoryName(categoryEntity.getCategoryName());

        List<CategoryItemEntity> categoryItemList = categoryEntity.getCategoryItemList();
        List<ItemList> itemListList = new ArrayList<>();

        for(CategoryItemEntity i : categoryItemList){
            ItemList itemList = new ItemList();
            itemList.id(UUID.fromString(i.getItem().getUuid()));
            ItemList.ItemTypeEnum itemTypeEnum;
            if(i.getItem().getType().equals("1")){
             itemTypeEnum = ItemList.ItemTypeEnum.VEG;
            }else{
                itemTypeEnum = ItemList.ItemTypeEnum.NON_VEG;
            }
            itemList.itemType(itemTypeEnum);
            itemList.itemName(i.getItem().getItenName());
            itemList.price(i.getItem().getPrice());
            itemListList.add(itemList);
        }

        categoryDetailsResponse.itemList(itemListList);
        return new ResponseEntity<CategoryDetailsResponse> (categoryDetailsResponse,HttpStatus.OK);
    }
}
