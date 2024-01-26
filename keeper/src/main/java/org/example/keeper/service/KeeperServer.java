package org.example.keeper.service;

import org.example.service.model.*;
import org.example.service.Server;
import org.example.service.model.enums.Method;
import org.example.service.model.enums.OrderStatus;
import org.example.service.model.enums.ProductStatus;
import org.example.service.model.enums.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.stream;


public class KeeperServer extends Server {
    private List<User> wholeUserList;
    private List<Product> wholeProductList;
    private List<OrderOld> wholeOrderList;

    public KeeperServer(){
        wholeProductList = new ArrayList<>();
        wholeUserList = new ArrayList<>();
        wholeOrderList = new ArrayList<>();

        generateProductList();
    }

    private void generateProductList(){
        var product1 = new Product();
        product1.setName("Product 1");
        product1.setProductStatus(ProductStatus.Available);

        var product2 = new Product();
        product2.setName("Product 2");
        product2.setProductStatus(ProductStatus.Available);

        var product3 = new Product();
        product3.setName("Product 3");
        product3.setProductStatus(ProductStatus.Available);

        wholeProductList.add(product1);
        wholeProductList.add(product2);
        wholeProductList.add(product3);
    }

    @Override
    protected String execute(Method method, Object object) {
        try{
            Object obj = switch(method){
                case Test -> test((Product) object);
                case Register -> register((User) object);
                case Unregister -> unregister((UUID) object);
                case GetOffer -> getOffer();
                case PutOrder -> putOrder((OrderOld) object);
                case ReturnOrder -> returnOrder((OrderOld) object);
                case GetInfo -> getInfo((Integer) object);
                case GetInfoByUserRole -> getInfoByUserRole((Role) object);
                case GetOrder -> getOrder();
                case GetOrders -> getOrders();
                case UpdateOrder -> updateOrder((OrderOld) object);

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

    private User register(User user){
        wholeUserList.add(user);
        System.out.println("Registering user with id: " + user.getId());
        System.out.println("Numbers of users: " + wholeUserList.size());

        return user;
    }

    private User unregister(UUID id){
        for (User user : wholeUserList) {
            if (user.getId().equals(id)) {
                User userToView = user;
                wholeUserList.remove(user);
                System.out.println("Unregistering user with id: " + id);
                System.out.println("Numbers of users: " + wholeUserList.size());
                return userToView;
            }
        }
        System.out.println("Unregistering faild. User with id: " + id + " does not exist");
        return null;
    }

    private List<Product> getOffer(){
        List<Product> offerList = wholeProductList.stream()
                .filter(product -> (product.getProductStatus() == ProductStatus.Available))
                .toList();
        return offerList;
    }

    private OrderOld putOrder(OrderOld order){
        wholeOrderList.add(order);
        order.setOrderStatus(OrderStatus.NotServed);

        List<UUID> puttedID = order.getProductList().stream()
                .filter(product -> product.getProductStatus() == ProductStatus.Ordered)
                .map(Product::getId)
                .toList();
        for (Product product : wholeProductList) {
            for (UUID id : puttedID) {
                if(product.getId().equals(id))
                    product.setProductStatus(ProductStatus.Ordered);
            }
        }

        return order;       //?
    }

    private OrderOld getOrder(){
        return wholeOrderList.stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.NotServed)
                .findFirst()
                .orElse(null);
    }

    private List<OrderOld> getOrders(){
        return wholeOrderList;
    }

    private OrderOld updateOrder(OrderOld order) {
        var ord = wholeOrderList.stream().filter(o -> o.getOrderID().equals(order.getOrderID())).findFirst().orElse(null);
        var index = wholeOrderList.indexOf(ord);
        wholeOrderList.set(index, order);
        return ord;
    }

    private List<Product> returnOrder(OrderOld order){
        List<UUID> returnedID = order.getProductList().stream()
                .filter(product -> product.getProductStatus() == ProductStatus.Returned)
                .map(Product::getId)
                .toList();
        var ord = wholeOrderList.stream().filter(o -> o.getOrderID().equals(order.getOrderID())).findFirst().orElse(null);
        for (Product product : wholeProductList) {
            for (UUID id : returnedID) {
                if(product.getId().equals(id)) {
                    var originalProduct = ord.getProductList().stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
                    product.setProductStatus(ProductStatus.Available);
                    originalProduct.setProductStatus(ProductStatus.Returned);
                }
            }
        }
        updateOrder(order);
        return wholeProductList;
    }

    private User getInfo(int id2){
        User userID2;
        if(id2 == 0){
            return wholeUserList.stream().findFirst().orElse(null);
        } else {
            if(id2 < wholeUserList.size()){
                return userID2 = wholeUserList.get(id2);
            }
        }
        System.out.println("User with id: " + id2 + " do not exist");
        return null;
    }

    private User getInfoByUserRole(Role role){
        return wholeUserList.stream()
                .filter(user -> user.getRole() == role)
                .findFirst()
                .orElse(null);
    }
}
