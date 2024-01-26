package org.example.service.RMI;

import org.example.shop.ICallback;
import org.example.shop.ICustomer;
import org.example.shop.ISeller;
import org.example.shop.Item;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMISeller extends UnicastRemoteObject implements ISeller {
    protected RMISeller() throws RemoteException {
    }

    @Override
    public void acceptOrder(ICustomer ic, List<Item> boughtItemList, List<Item> returnedItemList) throws RemoteException {

    }

    @Override
    public void response(ICallback ic, List<Item> itemList) throws RemoteException {

    }
}
