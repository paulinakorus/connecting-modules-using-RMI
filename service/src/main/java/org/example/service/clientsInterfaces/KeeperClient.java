package org.example.service.clientsInterfaces;

import org.example.service.model.Order;
import org.example.service.model.Product;
import org.example.service.model.User;
import org.example.service.model.enums.Role;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface KeeperClient {
    User register(User user) throws IOException;
    User unregister(UUID id) throws IOException;
    List<Product> getOffer() throws IOException;
    Order putOrder(Order order) throws IOException;
    Order getOrder() throws IOException;
    List<Order> getOrders() throws IOException;
    User getInfo(int id2) throws IOException;
    User getInfoByUserRole(Role role) throws IOException;
    List<Product> returnOrder(Order order) throws IOException;
    Order updateOrder(Order order) throws IOException;
}
