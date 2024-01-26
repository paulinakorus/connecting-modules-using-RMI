package org.example.service.clientsInterfaces;

import org.example.service.model.OrderOld;
import org.example.service.model.Receipt;

import java.io.IOException;

public interface CustomerClient {
    Receipt returnReceipt(Receipt receipt) throws IOException;
    OrderOld putOrder(OrderOld order) throws IOException;
}
