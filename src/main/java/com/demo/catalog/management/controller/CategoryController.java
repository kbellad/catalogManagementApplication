package com.demo.catalog.management.controller;

import com.demo.catalog.management.common.CatalogUtils;
import com.demo.catalog.management.domain.Category;
import com.demo.catalog.management.exception.CategoryException;
import com.demo.catalog.management.service.CatalogService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * This class exposes the CRUD API's for Category feature
 */

@RestController
@RequestMapping("/category")
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public CatalogService<Category> catalogService;


    @PostMapping("/")
    public ResponseEntity addCategory(@RequestBody JsonNode body){
        Category category;
        try {
            category = objectMapper.readValue(body.traverse(), Category.class);
            LOGGER.info("CategoryController >> addCategory >> categoryName : {}" , category.getName());
            catalogService.add(category);
            LOGGER.info("CategoryController >> addCategory >> categoryName : {} saved" , category.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (IOException e) {
            LOGGER.error("CategoryController >> addCategory >> IOException : {}" , e.getMessage());
            return CatalogUtils.createResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(CategoryException categoryException){
            LOGGER.error("CategoryController >> addCategory >> CategoryException : {}" , categoryException.getMessage());
            return CatalogUtils.createResponse(categoryException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception exception) {
            LOGGER.error("CategoryController >> addCategory >> Exception : {}" , exception.getMessage());
            return CatalogUtils.createResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{name}")
    public ResponseEntity updateCategory(@PathVariable("name") String categoryName, @RequestBody JsonNode body){
        LOGGER.info("CategoryController >> updateCategory >> categoryName : {}" , categoryName);
        Category category;
        try {
            category = objectMapper.readValue(body.traverse(), Category.class);
            catalogService.update(categoryName, null, category);
            LOGGER.info("CategoryController >> updateCategory >> categoryName : {} updated " , categoryName);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (IOException e) {
            LOGGER.error("CategoryController >> updateCategory >> IOException : {}" , e.getMessage());
            return CatalogUtils.createResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(CategoryException categoryException){
            LOGGER.error("CategoryController >> updateCategory >> CategoryException : {}" , categoryException.getMessage());
            return CatalogUtils.createResponse(categoryException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception exception) {
            LOGGER.error("CategoryController >> updateCategory >> Exception : {}" , exception.getMessage());
            return CatalogUtils.createResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity searchCategory(@PathVariable("name") String categoryName){
        LOGGER.info("CategoryController >> searchCategory >> categoryName : {}" , categoryName);
        Category category = catalogService.get(categoryName, null);
        if(category != null){
            LOGGER.info("CategoryController >> searchCategory >> categoryName : {} completed" , categoryName);
            return CatalogUtils.createResponse(HttpStatus.OK, category);
        } else{
            LOGGER.error("CategoryController >> searchCategory >> No records found for the given category name  : {}" , categoryName);
            return CatalogUtils.createResponse("No records found for the given category name : " + categoryName, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity deleteCategory(@PathVariable("name") String categoryName){
        LOGGER.info("CategoryController >> deleteCategory >> categoryName : {}" , categoryName);
        try {
            catalogService.remove(categoryName, null);
            LOGGER.info("CategoryController >> deleteCategory >> categoryName : {} removed" , categoryName);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch(CategoryException categoryException){
            LOGGER.error("CategoryController >> deleteCategory >> CategoryException : {}" , categoryException.getMessage());
            return CatalogUtils.createResponse(categoryException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception exception) {
            LOGGER.error("CategoryController >> deleteCategory >> Exception : {}" , exception.getMessage());
            return CatalogUtils.createResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
