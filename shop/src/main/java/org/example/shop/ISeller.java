package org.example.shop;

import java.rmi.RemoteException;
import java.util.List;

public interface ISeller extends ICallback {
    public void acceptOrder(ICustomer ic, List<Item> boughtItemList, List<Item> returnedItemList) throws RemoteException;
}
