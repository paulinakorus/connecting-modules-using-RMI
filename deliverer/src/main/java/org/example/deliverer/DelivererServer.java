package org.example.deliverer;

import org.example.service.Server;
import org.example.service.model.OrderOld;
import org.example.service.model.enums.Method;
import org.example.service.model.Product;
import org.example.service.model.enums.OrderStatus;
import org.example.service.model.enums.ProductStatus;

public class DelivererServer extends Server {
    @Override
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
    }
}
