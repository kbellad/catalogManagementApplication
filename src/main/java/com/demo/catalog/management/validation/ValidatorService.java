package com.demo.catalog.management.validation;

import com.demo.catalog.management.domain.Category;
import com.demo.catalog.management.domain.Product;
import com.demo.catalog.management.exception.CategoryException;
import com.demo.catalog.management.exception.ProductException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;


/**
 * This class is responsible for the validation of the application
 */

@Service
public class ValidatorService {

    public void validateCategoryName(String name, Map<String, Category> map){
        if(!map.isEmpty() && map.values().stream().anyMatch(cat -> cat.getName().equalsIgnoreCase(name))){
            throw new CategoryException("Category name already exist!");
        }
    }

    public void validateUpdatedCategoryName(String existedName, String updatedName, Map<String, Category> map){
        if (!map.isEmpty() && map.keySet().stream().filter(c -> !c.equals(existedName)).anyMatch(cat -> cat.equals(updatedName))) {
            throw new CategoryException("Category name already exist!");
        }
    }

    public void validateCategoryRemove(Category category) {
        if((category.getProducts() != null && !category.getProducts().isEmpty())){
            throw new CategoryException("Category cannot be removed as it contains products");
        } else if(category.getSubCategory() != null){
            if(category.getSubCategory().getProducts() != null && !category.getSubCategory().getProducts().isEmpty()){
                throw new CategoryException("Category cannot be removed as it contains products in the subcategory");
            }
        }
    }

    public void validateProductName(String productName, Collection<Product> products){
        if(products != null && !products.isEmpty() && products.stream().anyMatch(product -> product.getName().equals(productName))){
            throw new ProductException("Product name already exist!");
        }
    }

    public void validateUpdatedProductName(String productName, String updatedProductName, Collection<Product> products){
        if(products != null && !products.isEmpty() && products.stream().filter(p -> !p.getName().equals(productName)).anyMatch(product -> product.getName().equals(updatedProductName))){
            throw new ProductException("Product name already exist!");
        }
    }

}
