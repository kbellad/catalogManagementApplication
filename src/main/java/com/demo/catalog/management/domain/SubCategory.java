package com.demo.catalog.management.domain;

import java.util.Map;


/**
 * POJO for SubCategory
 */

public class SubCategory extends Category{

    private static final long serialVersionUID = -1238790352312287121L;

    public SubCategory(String name, String desc, SubCategory subCategory, Map<String, Product> products) {
        this.name = name;
        this.desc = desc;
        this.subCategory = subCategory;
        this.products = products;
    }

    public SubCategory(){

    }
}
