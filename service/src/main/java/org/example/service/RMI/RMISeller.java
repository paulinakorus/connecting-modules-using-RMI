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
    protected RMISeller() throws RemoteException {
    }

    @Override
    public void acceptOrder(ICustomer ic, List<Item> boughtItemList, List<Item> returnedItemList) throws RemoteException {
        acceptOrderCallback.apply(ic, boughtItemList, returnedItemList);
    }

    @Override
    public void response(ICallback ic, List<Item> itemList) throws RemoteException {
        responseCallback.accept(ic, itemList);
    }
}
