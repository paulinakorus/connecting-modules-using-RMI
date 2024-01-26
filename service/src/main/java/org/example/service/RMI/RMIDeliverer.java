package org.example.service.RMI;

import org.example.shop.ICallback;
import org.example.shop.IDeliverer;
import org.example.shop.Item;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIDeliverer extends UnicastRemoteObject implements IDeliverer {

    protected RMIDeliverer() throws RemoteException {
    }

    @Override
    public void returnOrder(List<Item> itemList) throws RemoteException {

    }

    @Override
    public void response(ICallback ic, List<Item> itemList) throws RemoteException {

    }
}
