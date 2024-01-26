package org.example.service.model.enums;

import org.example.service.model.Order;
import org.example.service.model.Product;
import org.example.service.model.Receipt;
import org.example.service.model.User;

import java.util.UUID;

public enum Method {
    Test(Product.class),
    Register(User.class),
    Unregister(UUID.class),
    GetOrder(),
    GetOrders(),
    ReturnOrder(Order.class),
    GetInfo(Integer.class),
    GetInfoByUserRole(Role.class),
    GetOffer(),
    PutOrder(Order.class),
    ReturnReceipt(Receipt.class),
    AcceptOrder(Order.class),
    UpdateOrder(Order.class);
    private final Class<?> type;

    Method(Class<?> type) {
        this.type = type;
    }
    Method() {
        this.type = null;
    }

    public Class<?> getType() {
        return type;
    }
}
