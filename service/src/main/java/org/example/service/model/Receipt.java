package org.example.service.model;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private double price;
    private List<Product> productList = new ArrayList<>();

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
