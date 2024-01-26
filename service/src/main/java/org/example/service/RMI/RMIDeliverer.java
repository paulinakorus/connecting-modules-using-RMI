package org.example.service.RMI;

import org.example.shop.ICallback;
import org.example.shop.IDeliverer;
import org.example.shop.Item;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RMIDeliverer extends UnicastRemoteObject implements IDeliverer {

    private Consumer<List<Item>> returnCallback;
    private BiConsumer<ICallback, List<Item>> responseCallback;
    protected RMIDeliverer() throws RemoteException {
    }

    @Override
    public void returnOrder(List<Item> itemList) throws RemoteException {
        returnCallback.accept(itemList);
    }

    @Override
    public void response(ICallback ic, List<Item> itemList) throws RemoteException {
        responseCallback.accept(ic, itemList);
    }

    public Consumer<List<Item>> getReturnCallback() {
        return returnCallback;
    }

    public void setReturnCallback(Consumer<List<Item>> returnCallback) {
        this.returnCallback = returnCallback;
    }

    public BiConsumer<ICallback, List<Item>> getResponseCallback() {
        return responseCallback;
    }

    public void setResponseCallback(BiConsumer<ICallback, List<Item>> responseCallback) {
        this.responseCallback = responseCallback;
    }
}
