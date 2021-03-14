package com.demo.catalog.management.service;

import com.demo.catalog.management.domain.Category;
import com.demo.catalog.management.domain.Product;
import com.demo.catalog.management.exception.ProductException;
import com.demo.catalog.management.validation.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This class maintains the business logic for Product feature
 */

@Service
public class ProductService extends CatalogDataResource implements CatalogService<Product> {

    @Autowired
    ValidatorService validatorService;


    @Override
    public void add(Product product) {
        Category existingCategory = map.get(product.getCategoryName());
        if(existingCategory == null){
            throw new ProductException("Category : " +product.getCategoryName() + "not present, hence cannot add the product");
        }
        if(existingCategory.getProducts() != null && !existingCategory.getProducts().isEmpty()) {
            validatorService.validateProductName(product.getName(), existingCategory.getProducts().values());
            existingCategory.getProducts().put(product.getName(), product);
        } else{
            Map<String, Product> map = new HashMap<>();
            map.put(product.getName(), product);
            existingCategory.setProducts(map);
        }
    }

    @Override
    public void update(String categoryName, String productName, Product updateProduct) {
        Category existingCategory = map.get(categoryName);
        if(existingCategory == null){
            throw new ProductException("Category : " + categoryName + "not present, hence cannot update the product");
        }
        if(existingCategory.getProducts() == null || existingCategory.getProducts().isEmpty()){
            throw new ProductException("Product not exist : " + productName + " ,hence cannot update the product");
        }
        if(existingCategory.getProducts().get(productName) != null) {
            if (!updateProduct.getName().equals(productName)) {
                validatorService.validateUpdatedProductName(productName, updateProduct.getName(), existingCategory.getProducts().values());
                existingCategory.getProducts().remove(productName);
                existingCategory.getProducts().put(updateProduct.getName(), updateProduct);
            } else {
                existingCategory.getProducts().put(productName, updateProduct);
            }
        } else{
            throw new ProductException("Product not exist : " + productName + " ,hence cannot update the product");
        }
        map.put(existingCategory.getName(), existingCategory);
    }

    @Override
    public void remove(String categoryName, String productName) {
        Category category = map.get(categoryName);
        if(category == null){
            throw new ProductException("Category : " + categoryName + "not present, hence cannot remove the product");
        }
        category.getProducts().remove(productName);
        map.put(categoryName, category);

    }

    @Override
    public Product get(String categoryName, String productName) {
        Category existedCategory = map.get(categoryName);
        if(existedCategory == null){
            throw new ProductException("Category : " + categoryName + "not present, hence cannot show the product");
        }
        Map<String, Product> map = existedCategory.getProducts();
        if(map != null && !map.isEmpty()){
            return map.get(productName);
        } else {
            throw new ProductException(productName + " product not found");
        }
    }

    public void moveProduct(String orgCatName, String destCatName, Map<String,Product> products){
        Optional<String> name;
        if(products == null || products.isEmpty()){
            throw new ProductException("Empty products, cannot be moved");
        }
        Category orgCategory = map.get(orgCatName);
        Category destCategory = map.get(destCatName);
        if(orgCategory == null || destCategory == null){
            throw new ProductException("Source or Destination category cannot be empty");
        }
        if(destCategory.getProducts() == null || destCategory.getProducts().isEmpty()) {
            destCategory.setProducts(products);
        } else{
            destCategory.getProducts().putAll(products);
        }
        map.put(destCategory.getName(), destCategory);
        products.keySet().stream().iterator().forEachRemaining(key -> orgCategory.getProducts().remove(key));
        map.put(orgCategory.getName(), orgCategory);
    }
}
