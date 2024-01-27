package org.example.service.RMI;

import org.example.service.model.Order;
import org.example.service.model.enums.OrderStatus;
import org.example.shop.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RMIKeeper implements IKeeper {

    Map<Integer, ICallback> userMap = new HashMap<>();
    List<Item> itemList = new ArrayList<>();
    List<Order> orderList = new ArrayList<>();
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
        List<Item> products = itemList.stream()
                .filter(product -> product.getQuantity()>0)
                .toList();

        callback.response(null, products);
    }

    @Override
    public void putOrder(int idc, List<Item> itemList) throws RemoteException {
        var callback = (ICustomer) userMap.get(idc);
        Order order = new Order();
        order.setUser(callback);
        List<Item> itemOrderList = new ArrayList<>();

        for (Item item : itemList){
            var itemInWarehause = ifTheSameProducts(item);
            if(itemInWarehause == null)
                continue;

            int quantity = itemInWarehause.getQuantity() - item.getQuantity();
            if(quantity < 0){
                item.setQuantity(itemInWarehause.getQuantity());
                itemInWarehause.setQuantity(0);
            }else{
                item.setQuantity(quantity);
                itemInWarehause.setQuantity(itemInWarehause.getQuantity() - quantity);
            }
            itemOrderList.add(item);
        }
        order.setItemList(itemOrderList);
        orderList.add(order);
    }

    public Item ifTheSameProducts(Item ourItem){
        for (Item item : itemList) {
            if(item.getDescription().equals(ourItem.getDescription()))
                return item;
        }
        return null;
    }

    @Override
    public List<ISeller> getSellers() throws RemoteException {
        List<ISeller> sellerList = userMap.values().stream().filter(user -> user instanceof ISeller).map(user -> (ISeller) user).collect(Collectors.toList());
        return sellerList;
    }

    @Override
    public void getOrder(int idd) throws RemoteException {
        var callback = (IDeliverer) userMap.get(idd);
        Order firstAvaiableOrder = orderList.stream()
                .findFirst()
                .orElse(null);
        if(firstAvaiableOrder == null)
            return;
        callback.response(firstAvaiableOrder.getUser(), firstAvaiableOrder.getItemList());
    }

    @Override
    public void returnOrder(List<Item> itemReturnedList) throws RemoteException {
        for (Item itemToReturn : itemReturnedList) {
            var itemInWarehouse = ifTheSameProducts(itemToReturn);
            if(itemInWarehouse != null){
                itemInWarehouse.setQuantity(itemInWarehouse.getQuantity() + itemToReturn.getQuantity());
            }
        }
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public List<Item> getItemList() {
        return itemList;
    }
}
