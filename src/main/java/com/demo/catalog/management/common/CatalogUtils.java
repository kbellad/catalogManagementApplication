package com.demo.catalog.management.common;

import com.demo.catalog.management.domain.CatalogResponse;
import com.demo.catalog.management.domain.Category;
import com.demo.catalog.management.domain.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This class is used as utility for this project
 */
public class CatalogUtils {

    public static ResponseEntity<CatalogResponse> createResponse(String message, HttpStatus status){
        CatalogResponse catalogResponse = new CatalogResponse(status.name(), message, null, null);
        return ResponseEntity.status(status).body(catalogResponse);
    }

    public static ResponseEntity<CatalogResponse> createResponse(HttpStatus status, Category category){
        CatalogResponse catalogResponse = new CatalogResponse(status.name(), null, category, null);
        return ResponseEntity.status(status).body(catalogResponse);
    }

    public static ResponseEntity<CatalogResponse> createResponse(HttpStatus status, Category category, Product product){
        CatalogResponse catalogResponse = new CatalogResponse(status.name(), null, category, product);
        return ResponseEntity.status(status).body(catalogResponse);
    }
}
