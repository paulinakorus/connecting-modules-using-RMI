package org.example.service.clientsInterfaces;

import org.example.service.model.Order;

import java.io.IOException;

public interface SellerClient {
    Order acceptOrder(Order order) throws IOException;
}
