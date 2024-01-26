package org.example.customer;

import org.example.service.RMI.RMICustomer;
import org.example.service.clients.DelivererClientImpl;
import org.example.service.clients.KeeperClientImpl;
import org.example.service.clients.SellerClientImpl;
import org.example.service.clientsInterfaces.DelivererClient;
import org.example.service.clientsInterfaces.KeeperClient;
import org.example.service.clientsInterfaces.SellerClient;
import org.example.service.model.OrderOld;
import org.example.service.model.Product;
import org.example.service.model.enums.ProductStatus;
import org.example.service.model.enums.ProductStatusAtSeller;
import org.example.service.model.tables.OrderTable;
import org.example.service.model.tables.ProductsTable;
import org.example.service.model.User;
import org.example.service.model.enums.Role;
import org.example.shop.ICallback;
import org.example.shop.ICustomer;
import org.example.shop.IKeeper;
import org.example.shop.Item;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerGUI extends JFrame {
    private String host = "localhost";
    private CustomerServer customerServer = null;
    private KeeperClient keeperClient = new KeeperClientImpl(host, 2137);
    private SellerClient sellerClient;
    private DelivererClient delivererClient;
    private User user;
    private ProductsTable productsTableModel;
    private OrderTable orderTableModel;
    public IKeeper keeperServer;
    public ICustomer customer = new RMICustomer();
    public int customerID;

    private JPanel customerPanel;
    private JTabbedPane tabbedPane1;
    private JPanel productsPanel;
    private JPanel ordersPanel;
    private JTextField hostTextField;
    private JTextField portTextField;
    private JButton registerButton;
    private JButton unregisterButton;
    private JList productsList;
    private JButton orderButton;
    private JButton returnButton;
    private JButton payButton;
    private JLabel hostLabel;
    private JLabel portLabel;
    private JLabel customerLabel;
    private JTable productsTable;
    private JTable ordersTable;
    private JTable productsOrderTable;
    private JButton returnAllButton;
    private JButton readProductsButton;
    private JButton refreashTablesButton;

    public CustomerGUI() throws IOException {
        this.setTitle("Customer");                                     // set title of frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           // exit out off application
        this.setResizable(false);                                      // preventing frame from being resized
        this.setSize(1100, 700);                            // setting size
        this.setVisible(true);                                         // making frame visible
        this.add(customerPanel);

        hostTextField.setText(host);
        setUpButtons();
    }

    private void setUpProductsTable() throws IOException {
        productsTableModel = new ProductsTable(keeperClient.getOffer());
        productsTable.setModel(productsTableModel);
        productsTable.setAutoCreateRowSorter(false);
        productsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private void setUpOrderTable() throws IOException {
        orderTableModel = new OrderTable(keeperClient.getOrders());
        ordersTable.setModel(orderTableModel);
        ordersTable.setAutoCreateRowSorter(false);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setUpOrderProductsTable(List<Product> productsTable) throws IOException{
        productsTableModel = new ProductsTable(productsTable);
        productsOrderTable.setModel(productsTableModel);
        productsOrderTable.setAutoCreateRowSorter(false);
        productsOrderTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public void resResponse(ICallback callback, List<Item> itemList) {
        System.out.println(itemList.size());
    }
    public void resReceipt(String receipt){
        System.out.println(receipt);
    }

    public void resPutOrder(ICallback callback, List<Item> itemList){
        System.out.println(itemList.size());
    }

    public void connect() {
        try {
            Registry registry = LocateRegistry.getRegistry();                       // połączenie do serwera
            keeperServer = (IKeeper) registry.lookup("Keeper");                                         // połączenie do serwera
            customerID = keeperServer.register(customer);

            ((RMICustomer) customer).setResponseCallback(this::resResponse);
            ((RMICustomer) customer).setReturnReceiptCallback(this::resReceipt);
            ((RMICustomer) customer).setPutOrderCallback(this::resPutOrder);

            System.out.println(customerID);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setUpButtons(){
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == registerButton){
                    if(customerServer == null){
                        connect();
                        /*user = new User();
                        user.setRole(Role.Customer);
                        user.setHost(host);
                        user.setPort(Integer.valueOf(portTextField.getText()));

                        try {
                            keeperClient.register(user);
                            setUpProductsTable();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }*/

                        /*Thread thread = new Thread(() -> {
                            customerServer = new CustomerServer();
                            try {
                                customerServer.start(user.getHost(), user.getPort());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        thread.start();*/
                    }
                }
            }
        });

        unregisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == unregisterButton){
                    try {
                        if(user != null)
                            keeperClient.unregister(user.getId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == orderButton){
                    OrderOld order = new OrderOld();
                    order.setUserID(user.getId());

                    List<Product> sellectedProducts = new ArrayList<>();
                    int[] sellectedRows = productsTable.getSelectedRows();
                    for(int row : sellectedRows){
                        Product product = new Product();
                        product.setId((UUID) productsTableModel.getValueAt(row, 0));
                        product.setName((String) productsTableModel.getValueAt(row, 1));
                        product.setProductStatus(ProductStatus.Ordered);
                        sellectedProducts.add(product);
                    }
                    order.setProductList(sellectedProducts);
                    try {
                        keeperClient.putOrder(order);
                        setUpProductsTable();
                        setUpOrderTable();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        readProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == readProductsButton){
                    int sellectedRow = ordersTable.getSelectedRow();
                    var orderID = (UUID) ordersTable.getValueAt(sellectedRow, 0);

                    try {
                        List<OrderOld> orderList = keeperClient.getOrders();
                        for (OrderOld oneOrder : orderList) {
                            if(oneOrder.getOrderID().equals(orderID)){
                                setUpOrderProductsTable(oneOrder.getProductList());
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == returnButton){
                    int sellectedRowProduct = productsOrderTable.getSelectedRow();
                    var productID = (UUID) productsOrderTable.getValueAt(sellectedRowProduct, 0);

                    int sellectedRowOrder = ordersTable.getSelectedRow();
                    var orderID = (UUID) ordersTable.getValueAt(sellectedRowOrder, 0);

                    try {
                        List<OrderOld> orderList = keeperClient.getOrders();
                        for (OrderOld oneOrder : orderList) {
                            if(oneOrder.getOrderID().equals(orderID)){
                                for (Product product : oneOrder.getProductList()) {
                                    if(product.getId().equals(productID)){
                                        product.setProductStatusAtSeller(ProductStatusAtSeller.ToReturn);
                                        sellerClient = new SellerClientImpl(host, keeperClient.getInfoByUserRole(Role.Seller).getPort());
                                        oneOrder = sellerClient.acceptOrder(oneOrder);
                                        keeperClient.returnOrder(oneOrder);
                                        setUpProductsTable();
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == payButton){
                    int sellectedRowProduct = productsOrderTable.getSelectedRow();
                    var productID = (UUID) productsOrderTable.getValueAt(sellectedRowProduct, 0);

                    int sellectedRowOrder = ordersTable.getSelectedRow();
                    var orderID = (UUID) ordersTable.getValueAt(sellectedRowOrder, 0);

                    try {
                        List<OrderOld> orderList = keeperClient.getOrders();
                        for (OrderOld oneOrder : orderList) {
                            if(oneOrder.getOrderID().equals(orderID)){
                                for (Product product : oneOrder.getProductList()) {
                                    if(product.getId().equals(productID)){
                                        if(product.getProductStatusAtSeller() == null) {
                                            return;
                                        }
                                        if(product.getProductStatusAtSeller().equals(ProductStatusAtSeller.ToBought)){
                                            sellerClient =  new SellerClientImpl(host, keeperClient.getInfoByUserRole(Role.Seller).getPort());
                                            oneOrder = sellerClient.acceptOrder(oneOrder);
                                            keeperClient.updateOrder(oneOrder);
                                            setUpOrderProductsTable(oneOrder.getProductList());
                                            setUpOrderTable();
                                        }
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        returnAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() == returnAllButton){
                    int sellectedRow = ordersTable.getSelectedRow();
                    var orderID = (UUID) ordersTable.getValueAt(sellectedRow, 0);

                    try {
                        List<OrderOld> orderList = keeperClient.getOrders();
                        for (OrderOld oneOrder : orderList) {
                            if(oneOrder.getOrderID().equals(orderID)){
                                delivererClient = new DelivererClientImpl(host, keeperClient.getInfoByUserRole(Role.Deliver).getPort());
                                oneOrder = delivererClient.returnOrder(oneOrder);
                                keeperClient.returnOrder(oneOrder);
                                setUpProductsTable();
                                break;
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        refreashTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() == refreashTablesButton){
                    try {
                        setUpOrderTable();
                        setUpProductsTable();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
