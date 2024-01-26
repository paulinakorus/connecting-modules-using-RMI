package org.example.service.clientsInterfaces;

import org.example.service.model.Order;
import org.example.service.model.Receipt;

import java.io.IOException;

public interface CustomerClient {
    Receipt returnReceipt(Receipt receipt) throws IOException;
    Order putOrder(Order order) throws IOException;
}
