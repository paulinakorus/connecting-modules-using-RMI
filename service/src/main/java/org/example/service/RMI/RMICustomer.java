package org.example.service.RMI;

import org.example.shop.ICallback;
import org.example.shop.ICustomer;
import org.example.shop.Item;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.function.BiConsumer;

public class RMICustomer extends UnicastRemoteObject implements ICustomer {

    private BiConsumer<ICallback, List<Item>> responseCallback;                         // klasa ktora jest metoda

    public RMICustomer() throws RemoteException {
    }

    @Override
    public void putOrder(ICallback idd, List<Item> itemList) throws RemoteException {

    }

    @Override
    public void returnReceipt(String receipt) throws RemoteException {

    }

    @Override
    public void response(ICallback ic, List<Item> itemList) throws RemoteException {
        responseCallback.accept(ic, itemList);
    }

    public void setResponseCallback(BiConsumer<ICallback, List<Item>> responseCallback) {
        this.responseCallback = responseCallback;
    }
}
