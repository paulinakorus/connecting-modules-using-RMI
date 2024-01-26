package org.example.deliverer;

import org.example.service.Server;
import org.example.service.model.Order;
import org.example.service.model.User;
import org.example.service.model.enums.Method;
import org.example.service.model.Product;
import org.example.service.model.enums.OrderStatus;
import org.example.service.model.enums.ProductStatus;

import java.util.List;

public class DelivererServer extends Server {
    @Override
    protected String execute(Method method, Object object) {
        try{
            Object obj = switch(method){
                case ReturnOrder -> returnOrder((Order) object);
                default -> throw new RuntimeException("Unexcepted method");
            };
            return objectMapper.writeValueAsString(obj);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    private Order returnOrder(Order order){
        for (Product product : order.getProductList()) {
            product.setProductStatus(ProductStatus.Returned);
        }
        order.setOrderStatus(OrderStatus.Returned);
        return order;
    }
}
