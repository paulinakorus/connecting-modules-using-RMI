package org.example.service.RMI;

import org.example.shop.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RMIKeeper implements IKeeper {

    Map<Integer, ICallback> userMap = new HashMap<>();
    private static int count = 0;
    public RMIKeeper() throws RemoteException {
    }

    @Override
    public int register(ICallback iCallback) throws RemoteException {
        userMap.put(++count, iCallback);
        return count;
    }

    @Override
    public boolean unregister(int id) throws RemoteException {
        var callback = userMap.get(id);

        if(callback == null)
            return false;

        userMap.remove(id);
        return true;
    }

    @Override
    public void getOffer(int idc) throws RemoteException {
        var callback = (ICustomer) userMap.get(idc);
        callback.response(null, new ArrayList<Item>(){{ new Item("a", 1);}});
    }

    @Override
    public void putOrder(int idc, List<Item> itemList) throws RemoteException {

    }

    @Override
    public List<ISeller> getSellers() throws RemoteException {
        return null;
    }

    @Override
    public void getOrder(int idd) throws RemoteException {

    }

    @Override
    public void returnOrder(List<Item> itemList) throws RemoteException {

    }
}
