package org.example.service.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.Client;
import org.example.service.clientsInterfaces.CustomerClient;
import org.example.service.model.Order;
import org.example.service.model.Payload;
import org.example.service.model.Receipt;
import org.example.service.model.enums.Method;

import java.io.IOException;

public class CustomerClientImpl extends Client implements CustomerClient {
        private ObjectMapper objectMapper = new ObjectMapper();
        private Payload payload = new Payload();
        private String data;
        private String payloadString;

        public CustomerClientImpl(String host, int port) {
            super(host, port);
        }

        public Receipt returnReceipt(Receipt receipt) throws IOException {
                data = objectMapper.writeValueAsString(receipt);
                payload.setArgument(data);
                payload.setMethod(Method.ReturnReceipt);

                payloadString = objectMapper.writeValueAsString(payload);
                var result = this.sendAndRead(payloadString);
                var receipt_result = objectMapper.readValue(result, Receipt.class);
                System.out.println("Returning receipt");
                return receipt;
        }

        public Order putOrder(Order order) throws IOException {
                data = objectMapper.writeValueAsString(order);
                payload.setArgument(data);
                payload.setMethod(Method.PutOrder);

                payloadString = objectMapper.writeValueAsString(payload);
                var result = this.sendAndRead(payloadString);
                var order_result = objectMapper.readValue(result, Order.class);
                System.out.println("Buying all order products");
                return order_result;
        }
}

