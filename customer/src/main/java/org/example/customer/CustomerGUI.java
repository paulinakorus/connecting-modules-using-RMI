package org.example.customer;

import org.example.service.RMI.RMICustomer;
import org.example.service.model.Order;
import org.example.service.model.enums.ProductStatus;
import org.example.service.model.tables.OrderTable;
import org.example.service.model.tables.ProductsTable;
import org.example.service.model.enums.Role;
import org.example.shop.*;

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
import java.util.stream.Collectors;

public class CustomerGUI extends JFrame {
    private ProductsTable productsTableModel;
    private OrderTable orderTableModel;

    private IKeeper keeperServer;
    private ICustomer customer = new RMICustomer();
    private int customerID;
    private List<Item> localItemList = new ArrayList<>();
    private List<Item> itemToBuy = new ArrayList<>();
    private List<IDeliverer> delivererList = new ArrayList<>();

    private JPanel customerPanel;
    private JTabbedPane tabbedPane1;
    private JPanel productsPanel;
    private JPanel ordersPanel;
    private JButton registerButton;
    private JButton unregisterButton;
    private JList productsList;
    private JButton orderButton;
    private JButton returnButton;
    private JButton payButton;
    private JLabel customerLabel;
    private JTable productsTable;
    private JTable ordersTable;
    private JTable productsOrderTable;
    private JButton returnAllButton;
    private JButton refreashTablesButton;

    public CustomerGUI() throws IOException {
        this.setTitle("Customer");                                     // set title of frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           // exit out off application
        this.setResizable(false);                                      // preventing frame from being resized
        this.setSize(1100, 700);                            // setting size
        this.setVisible(true);                                         // making frame visible
        this.add(customerPanel);

        setUpButtons();
    }

    private void setUpProductsTable(List<Item> itemList) throws IOException {
        localItemList.clear();
        localItemList.addAll(itemList.stream().map(item -> new Item(item.getDescription(), item.getQuantity())).collect(Collectors.toList()));
        productsTableModel = new ProductsTable(localItemList);
        productsTable.setModel(productsTableModel);
        productsTable.setAutoCreateRowSorter(false);
        productsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private void setUpOrderProductsTable(List<Item> itemTable) throws IOException{
        productsTableModel = new ProductsTable(itemTable);
        productsOrderTable.setModel(productsTableModel);
        productsOrderTable.setAutoCreateRowSorter(false);
        productsOrderTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public void callbackConsumer(ICallback callback, List<Item> itemList) {
        try {
            setUpProductsTable(itemList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void putOrderCallback(ICallback callback, List<Item> items) {
        localItemList.addAll(items.stream().map(item -> new Item(item.getDescription(), item.getQuantity())).collect(Collectors.toList()));
        delivererList.add((IDeliverer) callback);
        try {
            setUpOrderProductsTable(localItemList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void returnReceiptCallback(String receipt) {
        System.out.println(receipt);
    }


    public void connect() {
        try {
            Registry registry = LocateRegistry.getRegistry();                       // połączenie do serwera
            keeperServer = (IKeeper) registry.lookup("Keeper");                                         // połączenie do serwera
            customerID = keeperServer.register(customer);

            ((RMICustomer) customer).setResponseCallback(this::callbackConsumer);
            ((RMICustomer) customer).setReturnReceiptCallback(this::returnReceiptCallback);
            ((RMICustomer) customer).setPutOrderCallback(this::putOrderCallback);

            keeperServer.getOffer(customerID);
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
                    if(customer == null){
                        connect();
                        try {
                            keeperServer.getOffer(customerID);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        unregisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == unregisterButton){
                    try {
                        if(customer != null)
                            keeperServer.unregister(customerID);
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
                    List<Item> sellectedItems = new ArrayList<>();
                    int[] sellectedRows = productsTable.getSelectedRows();
                    for(int row : sellectedRows){
                        Item item = new Item();
                        item.setDescription((String) productsTableModel.getValueAt(row, 0));
                        item.setQuantity((int) productsTableModel.getValueAt(row, 1));
                        sellectedItems.add(item);
                    }

                    try {
                        keeperServer.putOrder(customerID, sellectedItems);
                        //setUpProductsTable(keeperServer.getOffer(customerID));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        /*readProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == readProductsButton){
                    int sellectedRow = ordersTable.getSelectedRow();
                    var orderID = (UUID) ordersTable.getValueAt(sellectedRow, 0);

                    try {
                        List<Order> orderList = keeperServer.getOrderList();
                        for (Order oneOrder : orderList) {
                            if(oneOrder.getOrderID().equals(orderID)){
                                setUpOrderProductsTable(oneOrder.getItemList());
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });*/

        /*returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == returnButton) {
                    int sellectedRowProduct = productsOrderTable.getSelectedRow();
                    var itemID = (String) productsOrderTable.getValueAt(sellectedRowProduct, 0);

                    for (Item item : localItemList) {
                        if (item.getDescription().equals(itemID)) {
                            item.setProductStatus(ProductStatus.ToReturn);
                            itemToReturn.add(item);
                            break;
                        }
                    }

                    try {
                        var sellers = keeperServer.getSellers();
                        if (sellers.isEmpty()) {
                            System.out.println("No sellers available");
                        } else {
                            sellers.get(0).acceptOrder(customer, itemToBuy, itemToReturn);
                            itemToReturn = null;
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });*/

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == payButton) {
                    int sellectedRowProduct = productsOrderTable.getSelectedRow();
                    var itemID = (String) productsOrderTable.getValueAt(sellectedRowProduct, 0);

                    for (Item item : localItemList) {
                        if (item.getDescription().equals(itemID)) {
                            itemToBuy.add(item);
                            localItemList.remove(item);
                            break;
                        }
                    }

                    try {
                        var sellers = keeperServer.getSellers();
                        if (sellers.isEmpty()) {
                            System.out.println("No sellers available");
                        } else {
                            sellers.get(0).acceptOrder(customer, itemToBuy, localItemList);
                            itemToBuy = null;
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        returnAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() == returnAllButton){
                    try {
                        if(!delivererList.isEmpty()){
                            IDeliverer deliverer = delivererList.get(0);
                            deliverer.returnOrder(localItemList);
                            localItemList.clear();
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
                        setUpProductsTable(localItemList);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

}
