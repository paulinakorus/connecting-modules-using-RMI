package org.example.deliverer;

import org.example.service.RMI.RMICustomer;
import org.example.service.RMI.RMIDeliverer;
import org.example.service.clients.KeeperClientImpl;
import org.example.service.clientsInterfaces.KeeperClient;
import org.example.service.model.OrderOld;
import org.example.service.model.Product;
import org.example.service.model.User;
import org.example.service.model.enums.OrderStatus;
import org.example.service.model.enums.ProductStatusAtSeller;
import org.example.service.model.enums.Role;
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
    private String host = "localhost";
    private KeeperClient keeperClient = new KeeperClientImpl(host, 2137);
    private User user;
    private OrderTable orderTableModel;
    public IKeeper keeperServer;
    public IDeliverer deliverer = new RMIDeliverer();
    public int delivererID;

    private JPanel delivererPanel;
    private JLabel delivererLabel;
    private JTextField hostTextField;
    private JButton registerButton;
    private JButton unregisterButton;
    private JButton buyAllProductsButton;
    private JTable orderTable;
    private JButton getOrderButton;

    public DelivererGUI() throws IOException {
        this.setTitle("Deliverer");                                     // set title of frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           // exit out off application
        this.setResizable(false);                                      // preventing frame from being resized
        this.setSize(1100, 700);                            // setting size
        this.setVisible(true);                                         // making frame visible
        this.add(delivererPanel);

        hostTextField.setText(host);
        setUpButtons();
    }

    private void setUpOrderTable() throws IOException {
        List<OrderOld> orderList = new ArrayList<>();
        orderList.add(keeperClient.getOrder());
        orderTableModel = new OrderTable(orderList);
        orderTable.setModel(orderTableModel);
        orderTable.setAutoCreateRowSorter(false);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void resResponse(ICallback user, List<Item> itemList) {
        System.out.println(itemList.size());
    }

    public void resReturnOrder(List<Item> itemList){
        System.out.println("Returning order");
    }
    public void connect() {
        try {
            Registry registry = LocateRegistry.getRegistry();                       // połączenie do serwera
            keeperServer = (IKeeper) registry.lookup("Keeper");                                         // połączenie do serwera
            delivererID = keeperServer.register(deliverer);

            ((RMIDeliverer) deliverer).setResponseCallback(this::resResponse);
            ((RMIDeliverer) deliverer).setReturnCallback(this::resReturnOrder);

            System.out.println(delivererID);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setUpButtons(){
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == registerButton){
                    if(deliverer == null){
                        connect();
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
                    OrderOld order = keeperClient.getOrder();
                    for (Product product : order.getProductList()) {
                        product.setProductStatusAtSeller(ProductStatusAtSeller.ToBought);
                    }
                    order.setOrderStatus(OrderStatus.Delivered);
                    keeperClient.updateOrder(order);
                } catch (IOException ex) {
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

    public void returnOrderCallback(List<Item> items) throws RemoteException {
        keeperServer.returnOrder(items);
    }
}
