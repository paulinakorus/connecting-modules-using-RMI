package org.example.service.clientsInterfaces;

import org.example.service.model.OrderOld;
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
    OrderOld putOrder(OrderOld order) throws IOException;
    OrderOld getOrder() throws IOException;
    List<OrderOld> getOrders() throws IOException;
    User getInfo(int id2) throws IOException;
    User getInfoByUserRole(Role role) throws IOException;
    List<Product> returnOrder(OrderOld order) throws IOException;
    OrderOld updateOrder(OrderOld order) throws IOException;
}
