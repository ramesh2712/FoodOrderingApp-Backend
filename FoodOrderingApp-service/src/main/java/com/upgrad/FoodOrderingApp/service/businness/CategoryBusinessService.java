package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryBusinessService {
    @Autowired
    private CategoryDao categoryDao;

    public List<CategoryEntity> getAllCategory(){
        return categoryDao.getCategories();

    }

    public CategoryEntity getCategoryById (final String uuid) throws CategoryNotFoundException {
        if(uuid == null){
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }else{
            CategoryEntity categoryEntity = categoryDao.categoryById(uuid);
            if(categoryEntity == null){
                throw new CategoryNotFoundException("CNF-002","No category by this id");
            }
            return categoryEntity;
        }

    }
}
