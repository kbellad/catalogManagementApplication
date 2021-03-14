package com.demo.catalog.management.controller;


import com.demo.catalog.management.common.CatalogUtils;
import com.demo.catalog.management.domain.Category;
import com.demo.catalog.management.domain.Product;
import com.demo.catalog.management.exception.ProductException;
import com.demo.catalog.management.service.CatalogService;
import com.demo.catalog.management.service.CategoryService;
import com.demo.catalog.management.service.ProductService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;



/**
 * This class exposes the CRUD API's for Product feature
 */

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CatalogService<Product> catalogService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;


    @PostMapping("/")
    public ResponseEntity addProduct(@RequestBody JsonNode body){
        Product product;
        try {
            product = objectMapper.readValue(body.traverse(), Product.class);
            LOGGER.info("ProductController >> addProduct >> productName : {}" , product.getName());
            catalogService.add(product);
            LOGGER.info("ProductController >> addProduct >> productName : {} added" , product.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (IOException e) {
            LOGGER.error("ProductController >> addProduct >> IOException : {}" , e.getMessage());
            return CatalogUtils.createResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(ProductException productException){
            LOGGER.error("ProductController >> addProduct >> ProductException : {}" , productException.getMessage());
            return CatalogUtils.createResponse(productException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception exception) {
            LOGGER.error("ProductController >> addProduct >> Exception : {}" , exception.getMessage());
            return CatalogUtils.createResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity updateProduct(@RequestParam(value = "categoryName") String categoryName, @RequestParam(value = "productName") String productName, @RequestBody JsonNode body){
        LOGGER.info("ProductController >> updateProduct >> categoryName : {} productName : {}" , categoryName, productName);
        Product product;
        try {
            product = objectMapper.readValue(body.traverse(), Product.class);
            catalogService.update(categoryName, productName, product);
            LOGGER.info("ProductController >> updateProduct >> categoryName : {} productName : {} updated" , categoryName, productName);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (IOException e) {
            LOGGER.error("ProductController >> updateProduct >> IOException : {}" , e.getMessage());
            return CatalogUtils.createResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(ProductException productException){
            LOGGER.error("ProductController >> updateProduct >> ProductException : {}" , productException.getMessage());
            return CatalogUtils.createResponse(productException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception exception) {
            LOGGER.error("ProductController >> updateProduct >> Exception : {}" , exception.getMessage());
            return CatalogUtils.createResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping()
    public ResponseEntity deleteProduct(@RequestParam(value = "categoryName") String categoryName, @RequestParam(value = "productName") String productName){
        try {
            LOGGER.info("ProductController >> deleteProduct >> categoryName : {} productName : {}" , categoryName, productName);
            catalogService.remove(categoryName, productName);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch(ProductException productException){
            LOGGER.error("ProductController >> deleteProduct >> ProductException : {}" , productException.getMessage());
            return CatalogUtils.createResponse(productException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception exception) {
            LOGGER.error("ProductController >> deleteProduct >> Exception : {}" , exception.getMessage());
            return CatalogUtils.createResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/moveProducts")
    public ResponseEntity moveProducts(@RequestParam("sourceCategorytName") String sourceCategorytName, @RequestParam("destinationCategoryName") String destinationCategoryName, @RequestBody JsonNode body){
        LOGGER.info("ProductController >> moveProducts >>  sourceCategorytName : {} destinationCategoryName : {}" , sourceCategorytName, destinationCategoryName);
        Map<String,Product> products;
        try {
            products = objectMapper.readValue(body.traverse(), Map.class);
            productService.moveProduct(sourceCategorytName, destinationCategoryName, products);
            LOGGER.info("ProductController >> moveProducts >>  sourceCategorytName : {} destinationCategoryName : {} moved" , sourceCategorytName, destinationCategoryName);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (IOException e) {
            LOGGER.error("ProductController >> moveProducts >>  IOException : {}" , e.getMessage());
            return CatalogUtils.createResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(ProductException productException){
            LOGGER.error("ProductController >> moveProducts >>  ProductException : {}" , productException.getMessage());
            return CatalogUtils.createResponse(productException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception exception) {
            LOGGER.error("ProductController >> moveProducts >>  Exception : {}" , exception.getMessage());
            return CatalogUtils.createResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity searchProduct(@RequestParam(value = "categoryName") String categoryName, @RequestParam(value = "productName") String productName){
        try {
            LOGGER.info("ProductController >> searchProduct >>  categoryName : {} productName : {}" , categoryName, productName);
            Product product = catalogService.get(categoryName, productName);
            Category category = categoryService.get(categoryName, null);
            LOGGER.info("ProductController >> searchProduct >>  categoryName : {} productName : {} completed" , categoryName, productName);
            return CatalogUtils.createResponse(HttpStatus.OK, category, product);
        } catch (ProductException productException){
            LOGGER.error("ProductController >> searchProduct >>  ProductException : {}" , productException.getMessage());
            return CatalogUtils.createResponse(productException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception exception) {
            LOGGER.error("ProductController >> searchProduct >>  Exception : {}" , exception.getMessage());
            return CatalogUtils.createResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
