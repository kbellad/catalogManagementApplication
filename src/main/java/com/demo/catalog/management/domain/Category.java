package com.demo.catalog.management.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * POJO for Category
 */
public class Category implements Serializable {

    private static final long serialVersionUID = -1238790352312287133L;

    String name;
    String desc;
    SubCategory subCategory;
    Map<String, Product> products;

    public Category(){

    }

    public Category(String name, String desc, SubCategory subCategory, Map<String,Product> products) {
        this.name = name;
        this.desc = desc;
        this.subCategory = subCategory;
        this.products = products;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Map<String,Product> getProducts() {
        return products;
    }

    public void setProducts(Map<String,Product> products) {
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", subCategory=" + subCategory +
                ", products=" + products +
                '}';
    }
}
