package org.example.service.model;

import org.example.service.model.enums.OrderStatus;
import org.example.shop.ICallback;
import org.example.shop.Item;

import java.util.List;

public class Order {
    private ICallback user;
    private List<Item> itemList;
    private OrderStatus orderStatus;

    public ICallback getUser() {
        return user;
    }

    public void setUser(ICallback user) {
        this.user = user;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
