package org.example.service.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.Client;
import org.example.service.model.Order;
import org.example.service.model.Payload;
import org.example.service.model.enums.Method;

import java.io.IOException;

public class DelivererClientImpl extends Client implements org.example.service.clientsInterfaces.DelivererClient {
    private ObjectMapper objectMapper = new ObjectMapper();
    private Payload payload = new Payload();
    private String data;
    private String payloadString;

    public DelivererClientImpl(String host, int port) {
        super(host, port);
    }

    public Order returnOrder(Order order) throws IOException {
        data = objectMapper.writeValueAsString(order);
        payload.setArgument(data);
        payload.setMethod(Method.ReturnOrder);

        payloadString = objectMapper.writeValueAsString(payload);
        var result = this.sendAndRead(payloadString);
        var order_result = objectMapper.readValue(result, Order.class);
        System.out.println("Returning all products order");
        return order_result;
    }
}

