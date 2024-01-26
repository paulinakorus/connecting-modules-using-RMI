package org.example.deliverer;

import org.example.service.Server;
import org.example.service.model.OrderOld;
import org.example.service.model.enums.Method;
import org.example.service.model.Product;
import org.example.service.model.enums.OrderStatus;
import org.example.service.model.enums.ProductStatus;
import org.example.shop.IKeeper;
import org.example.shop.Item;

import java.rmi.RemoteException;
import java.util.List;

public class DelivererServer /*extends Server*/ {
    private IKeeper keeperServer;

    /*@Override
    protected String execute(Method method, Object object) {
        try{
            Object obj = switch(method){
                case ReturnOrder -> returnOrder((OrderOld) object);
                default -> throw new RuntimeException("Unexcepted method");
            };
            return objectMapper.writeValueAsString(obj);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    private OrderOld returnOrder(OrderOld order){
        for (Product product : order.getProductList()) {
            product.setProductStatus(ProductStatus.Returned);
        }
        order.setOrderStatus(OrderStatus.Returned);
        return order;
    }*/

    public void returnOrderCallback(List<Item> items) throws RemoteException {
        keeperServer.returnOrder(items);
    }
}
