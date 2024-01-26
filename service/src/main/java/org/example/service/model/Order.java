package org.example.service.model;

import org.example.service.model.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public class Order {
    private UUID orderID = UUID.randomUUID();
    private UUID userID;
    private List<Product> productList;
    private OrderStatus orderStatus;

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
