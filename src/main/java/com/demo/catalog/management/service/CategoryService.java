package com.demo.catalog.management.service;

import com.demo.catalog.management.domain.Category;
import com.demo.catalog.management.exception.CategoryException;
import com.demo.catalog.management.validation.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * This class maintains the business logic for Category feature
 */


@Service
public class CategoryService extends CatalogDataResource implements CatalogService<Category> {

    @Autowired
    public ValidatorService validatorService;



    @Override
    public void add(Category category) {
        validatorService.validateCategoryName(category.getName(), map);
        map.put(category.getName(), category);
    }

    @Override
    public void update(String categoryName, String optionalSubjectName, Category category) {
        Category existingCategory = map.get(categoryName);
        if(existingCategory == null){
            throw new CategoryException("Category : " + categoryName + "not present, hence cannot update");
        }
        update(category, existingCategory, map);
        map.put(existingCategory.getName(), existingCategory);
    }

    @Override
    public void remove(String categoryName, String name2) {
        Category existingCategory = map.get(categoryName);
        if(existingCategory == null){
            throw new CategoryException("Category : " + categoryName + "not present, hence cannot remove");
        }
        validatorService.validateCategoryRemove(existingCategory);
        map.remove(categoryName);
    }

    @Override
    public Category get(String categoryName, String name2) {
        return map.get(categoryName);
    }


    private void update(Category category, Category existingCategory, Map<String, Category> map) {
        if(!existingCategory.getName().equals(category.getName())) {
            validatorService.validateUpdatedCategoryName(existingCategory.getName(), category.getName(), map);
        }
        existingCategory.setDesc(category.getDesc());
        existingCategory.setName(category.getName());
        if(existingCategory.getProducts() == null || existingCategory.getProducts().isEmpty()){
            existingCategory.setProducts(category.getProducts());
        }else{
            existingCategory.getProducts().putAll(category.getProducts());
        }
        existingCategory.setSubCategory(category.getSubCategory());
    }
}
