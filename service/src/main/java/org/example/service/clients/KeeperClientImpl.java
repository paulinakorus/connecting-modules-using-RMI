package org.example.service.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.Client;
import org.example.service.model.*;
import org.example.service.model.enums.Method;
import org.example.service.model.enums.Role;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class KeeperClientImpl extends Client implements org.example.service.clientsInterfaces.KeeperClient {
    private ObjectMapper objectMapper = new ObjectMapper();
    private Payload payload = new Payload();
    private String data;
    private String payloadString;
    public KeeperClientImpl(String host, int port) {
        super(host, port);
    }

    public User register(User user) throws IOException {
        data = objectMapper.writeValueAsString(user);
        payload.setArgument(data);
        payload.setMethod(Method.Register);

        payloadString = objectMapper.writeValueAsString(payload);
        var result = this.sendAndRead(payloadString);
        var user_result = objectMapper.readValue(result, User.class);
        System.out.println("Added: " + result);
        return user_result;
    }

    public User unregister(UUID id) throws IOException {
        data = objectMapper.writeValueAsString(id);
        payload.setArgument(data);
        payload.setMethod(Method.Unregister);

        payloadString = objectMapper.writeValueAsString(payload);
        var result = this.sendAndRead(payloadString);
        var user_result = objectMapper.readValue(result, User.class);
        System.out.println("Delated: " + result);
        return user_result;
    }

    public List<Product> getOffer() throws IOException {
        payload.setMethod(Method.GetOffer);
        payload.setArgument(null);

        payloadString = objectMapper.writeValueAsString(payload);
        var result = this.sendAndRead(payloadString);
        var productList_result = objectMapper.readValue(result, Product[].class);
        System.out.println("Getting offer");
        return List.of(productList_result);
    }

    @Override
    public Order updateOrder(Order order) throws IOException {
        data = objectMapper.writeValueAsString(order);
        payload.setArgument(data);
        payload.setMethod(Method.UpdateOrder);

        payloadString = objectMapper.writeValueAsString(payload);
        var result = this.sendAndRead(payloadString);
        var order_result = objectMapper.readValue(result, Order.class);
        System.out.println("Updating order");
        return order_result;
    }

    public Order putOrder(Order order) throws IOException {
        data = objectMapper.writeValueAsString(order);
        payload.setArgument(data);
        payload.setMethod(Method.PutOrder);

        payloadString = objectMapper.writeValueAsString(payload);
        var result = this.sendAndRead(payloadString);
        var order_result = objectMapper.readValue(result, Order.class);
        System.out.println("Putting order");
        return order_result;
    }

    public Order getOrder() throws IOException {
        payload.setArgument(null);
        payload.setMethod(Method.GetOrder);

        payloadString = objectMapper.writeValueAsString(payload);
        var result = this.sendAndRead(payloadString);
        var order_result = objectMapper.readValue(result, Order.class);
        System.out.println("Getting order");
        return order_result;
    }

    public List<Order> getOrders() throws IOException {
        payload.setArgument(null);
        payload.setMethod(Method.GetOrders);

        payloadString = objectMapper.writeValueAsString(payload);
        var result = this.sendAndRead(payloadString);
        var orders_result = objectMapper.readValue(result, Order[].class);
        System.out.println("Getting orders");
        return List.of(orders_result);
    }

    public User getInfo(int id2) throws IOException {
        data = objectMapper.writeValueAsString(id2);
        payload.setArgument(data);
        payload.setMethod(Method.GetInfo);

        payloadString = objectMapper.writeValueAsString(payload);
        var result = this.sendAndRead(payloadString);
        var user_result = objectMapper.readValue(result, User.class);
        System.out.println("Info about user: " + result);
        return user_result;
    }

    public User getInfoByUserRole(Role role) throws IOException {
        data = objectMapper.writeValueAsString(role);
        payload.setArgument(data);
        payload.setMethod(Method.GetInfoByUserRole);

        payloadString = objectMapper.writeValueAsString(payload);
        var result = this.sendAndRead(payloadString);
        var user_result = objectMapper.readValue(result, User.class);
        System.out.println("Info about user by role: " + result);
        return user_result;
    }

    public List<Product> returnOrder(Order order) throws IOException {
        data = objectMapper.writeValueAsString(order);
        payload.setArgument(data);
        payload.setMethod(Method.ReturnOrder);

        payloadString = objectMapper.writeValueAsString(payload);
        var result = this.sendAndRead(payloadString);
        var productTab_result = objectMapper.readValue(result, Product[].class);
        System.out.println("Returning order");
        return List.of(productTab_result);
    }
}
