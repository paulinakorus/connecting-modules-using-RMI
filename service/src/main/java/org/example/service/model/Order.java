package org.example.service.model;

import org.example.service.model.enums.OrderStatus;
import org.example.shop.ICallback;
import org.example.shop.Item;

import java.util.List;
import java.util.UUID;

public class Order {
    private ICallback user;
    private UUID orderID = UUID.randomUUID();
    private List<Item> itemList;

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

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
}
