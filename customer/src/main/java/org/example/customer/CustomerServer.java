package org.example.customer;

import org.example.service.Server;
import org.example.service.model.*;
import org.example.service.model.enums.Method;
import org.example.service.model.enums.OrderStatus;
import org.example.service.model.enums.ProductStatus;

import java.util.ArrayList;
import java.util.List;

public class CustomerServer extends Server {

    @Override
    protected String execute(Method method, Object object) {
        try{
            Object obj = switch(method){
                case Test -> test((Product) object);
                case PutOrder -> putOrder((Order) object);
                case ReturnReceipt -> returnReceipt((Receipt) object);
                default -> throw new RuntimeException("Unexcepted method");
            };
            return objectMapper.writeValueAsString(obj);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    private String test(Product product){
        return product.getName();
    }

    private Order putOrder(Order order){
        for (Product product : order.getProductList()) {
            product.setProductStatus(ProductStatus.Bought);
        }
        order.setOrderStatus(OrderStatus.Bought);
        return order;//?
    }

    private Receipt returnReceipt(Receipt receipt){
        return receipt;
    }
}
