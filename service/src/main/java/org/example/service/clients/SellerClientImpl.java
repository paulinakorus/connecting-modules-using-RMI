package org.example.service.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.Client;
import org.example.service.model.OrderOld;
import org.example.service.model.Payload;
import org.example.service.model.enums.Method;

import java.io.IOException;

public class SellerClientImpl extends Client implements org.example.service.clientsInterfaces.SellerClient {
    private ObjectMapper objectMapper = new ObjectMapper();
    private Payload payload = new Payload();
    private String data;
    private String payloadString;
    public SellerClientImpl(String host, int port) {
        super(host, port);
    }

    public OrderOld acceptOrder(OrderOld order) throws IOException {
        data = objectMapper.writeValueAsString(order);
        payload.setArgument(data);
        payload.setMethod(Method.AcceptOrder);

        payloadString = objectMapper.writeValueAsString(payload);
        var result = this.sendAndRead(payloadString);
        var order_result = objectMapper.readValue(result, OrderOld.class);
        System.out.println("Accepting order");
        return order_result;
    }
}
