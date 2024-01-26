package org.example.service.clientsInterfaces;

import org.example.service.model.OrderOld;

import java.io.IOException;

public interface DelivererClient {

    public OrderOld returnOrder(OrderOld order) throws IOException;
}
