package org.example.deliverer;

import org.example.service.RMI.RMIDeliverer;
import org.example.service.model.Order;
import org.example.service.model.tables.OrderTable;
import org.example.shop.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class DelivererGUI extends JFrame{
    private OrderTable orderTableModel;
    private IKeeper keeperServer;
    private IDeliverer deliverer = new RMIDeliverer();
    private int delivererID;
    private Order currentOrder;

    private JPanel delivererPanel;
    private JLabel delivererLabel;
    private JButton registerButton;
    private JButton unregisterButton;
    private JButton buyAllProductsButton;
    private JTable orderTable;
    private JButton getOrderButton;

    public DelivererGUI() throws IOException, NotBoundException {
        this.setTitle("Deliverer");                                     // set title of frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           // exit out off application
        this.setResizable(false);                                      // preventing frame from being resized
        this.setSize(1100, 700);                            // setting size
        this.setVisible(true);                                         // making frame visible
        this.add(delivererPanel);

        setUpButtons();
    }

    private void setUpOrderTable() throws IOException {
        List<Order> orderList = new ArrayList<>();
        orderList.add(currentOrder);
        orderTableModel = new OrderTable(orderList);
        orderTable.setModel(orderTableModel);
        orderTable.setAutoCreateRowSorter(false);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void returnOrderCallback(List<Item> items){
        try {
            keeperServer.returnOrder(items);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void getOrderCallaback(ICallback callback, List<Item> itemList){
        var order = new Order();
        order.setUser(callback);
        order.setItemList(itemList);

        currentOrder = order;
        try {
            setUpOrderTable();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void connect() throws IOException {
        try {
            Registry registry = LocateRegistry.getRegistry();                       // połączenie do serwera
            keeperServer = (IKeeper) registry.lookup("Keeper");                                         // połączenie do serwera
            delivererID = keeperServer.register(deliverer);

            ((RMIDeliverer) deliverer).setResponseCallback(this::getOrderCallaback);
            ((RMIDeliverer) deliverer).setReturnCallback(this::returnOrderCallback);

            System.out.println(delivererID);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setUpButtons() throws NotBoundException, RemoteException {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == registerButton){
                    try {
                        connect();
                        registerButton.setDefaultCapable(false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        unregisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == unregisterButton){
                    try {
                        if(deliverer != null)
                            keeperServer.unregister(delivererID);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        buyAllProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ICustomer customer = (ICustomer) currentOrder.getUser();
                    customer.putOrder(deliverer, currentOrder.getItemList());
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        getOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == getOrderButton){
                    try {
                        keeperServer.getOrder(delivererID);
                        setUpOrderTable();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
