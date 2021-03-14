package com.demo.catalog.management.domain;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * This class acts as a callback response for all the API's
 */
public class CatalogResponse {


    public CatalogResponse(){

    }

    public CatalogResponse(String status, String message, Category category, Product product) {
        this.status = status;
        this.message = message;
        this.category = category;
        this.product = product;
    }

    @JsonProperty(value = "status")
    String status;

    @JsonProperty("message")
    String message;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @JsonProperty("category")
    Category category;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @JsonProperty("product")
    Product product;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CatalogResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", category=" + category +
                ", product=" + product +
                '}';
    }
}
