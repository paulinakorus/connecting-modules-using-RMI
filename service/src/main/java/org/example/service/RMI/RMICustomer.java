package org.example.service.RMI;

import org.example.shop.ICallback;
import org.example.shop.ICustomer;
import org.example.shop.Item;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Currency;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RMICustomer extends UnicastRemoteObject implements ICustomer {

    private BiConsumer<ICallback, List<Item>> responseCallback;                         // klasa ktora jest metoda
    private Consumer<String> returnReceiptCallback;
    private BiConsumer<ICallback, List<Item>> putOrderCallback;
    public RMICustomer() throws RemoteException {
    }

    @Override
    public void putOrder(ICallback idd, List<Item> itemList) throws RemoteException {
        putOrderCallback.accept(idd, itemList);
    }

    @Override
    public void returnReceipt(String receipt) throws RemoteException {
        returnReceiptCallback.accept(receipt);
    }

    @Override
    public void response(ICallback ic, List<Item> itemList) throws RemoteException {
        responseCallback.accept(ic, itemList);
    }

    public void setResponseCallback(BiConsumer<ICallback, List<Item>> responseCallback) {
        this.responseCallback = responseCallback;
    }

    public void setReturnReceiptCallback(Consumer<String> returnReceiptCallback) {
        this.returnReceiptCallback = returnReceiptCallback;
    }

    public void setPutOrderCallback(BiConsumer<ICallback, List<Item>> putOrderCallback) {
        this.putOrderCallback = putOrderCallback;
    }
}
