package org.example.service.clientsInterfaces;

import org.example.service.model.OrderOld;

import java.io.IOException;

public interface SellerClient {
    OrderOld acceptOrder(OrderOld order) throws IOException;
}
