package org.example.seller;

import org.example.service.Server;
import org.example.service.model.OrderOld;
import org.example.service.model.enums.Method;
import org.example.service.model.Product;
import org.example.service.model.enums.ProductStatus;
import org.example.service.model.enums.ProductStatusAtSeller;

public class SellerServer extends Server {
    @Override
    protected String execute(Method method, Object object) {
        try{
            Object obj = switch(method){
                case AcceptOrder -> acceptOrder((OrderOld) object);
                default -> throw new RuntimeException("Unexcepted method");
            };
            return objectMapper.writeValueAsString(obj);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    private OrderOld acceptOrder(OrderOld order){
        for (Product product : order.getProductList()) {
            if(product.getProductStatusAtSeller() == ProductStatusAtSeller.ToBought)
                product.setProductStatus(ProductStatus.Bought);
            else if(product.getProductStatusAtSeller() == ProductStatusAtSeller.ToReturn)
                product.setProductStatus(ProductStatus.Returned);
            else
                System.out.println("Status at the seller do not exist");
        }
        return order;
    }
}
