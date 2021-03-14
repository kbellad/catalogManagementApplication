package com.demo.catalog.management.domain;

import java.io.Serializable;


/**
 * POJO for Product
 */
public class Product implements Serializable {

    private static final long serialVersionUID = -1238790352312287124L;

    String name;
    String desc;
    Double price;
    String categoryName;

    public Product(){

    }

    public Product(String name, String desc, Double price, String categoryName) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", price=" + price +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
