package org.example.service.RMI;

import org.example.shop.ICallback;
import org.example.shop.ICustomer;
import org.example.shop.ISeller;
import org.example.shop.Item;

import org.example.service.TriConsumer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.function.BiConsumer;

public class RMISeller extends UnicastRemoteObject implements ISeller {

    private TriConsumer<ICustomer, List<Item>, List<Item>> acceptOrderCallback;
    private BiConsumer<ICallback, List<Item>> responseCallback;
    public RMISeller() throws RemoteException {
    }

    @Override
    public void acceptOrder(ICustomer ic, List<Item> boughtItemList, List<Item> returnedItemList) throws RemoteException {
        acceptOrderCallback.apply(ic, boughtItemList, returnedItemList);
    }

    @Override
    public void response(ICallback ic, List<Item> itemList) throws RemoteException {
        responseCallback.accept(ic, itemList);
    }

    public TriConsumer<ICustomer, List<Item>, List<Item>> getAcceptOrderCallback() {
        return acceptOrderCallback;
    }

    public void setAcceptOrderCallback(TriConsumer<ICustomer, List<Item>, List<Item>> acceptOrderCallback) {
        this.acceptOrderCallback = acceptOrderCallback;
    }

    public BiConsumer<ICallback, List<Item>> getResponseCallback() {
        return responseCallback;
    }

    public void setResponseCallback(BiConsumer<ICallback, List<Item>> responseCallback) {
        this.responseCallback = responseCallback;
    }
}
