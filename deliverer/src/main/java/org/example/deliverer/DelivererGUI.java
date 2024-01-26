package org.example.deliverer;

import org.example.service.clients.KeeperClientImpl;
import org.example.service.clientsInterfaces.KeeperClient;
import org.example.service.model.OrderOld;
import org.example.service.model.Product;
import org.example.service.model.User;
import org.example.service.model.enums.OrderStatus;
import org.example.service.model.enums.ProductStatusAtSeller;
import org.example.service.model.enums.Role;
import org.example.service.model.tables.OrderTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DelivererGUI extends JFrame{
    private String host = "localhost";
    private DelivererServer delivererServer = null;
    private KeeperClient keeperClient = new KeeperClientImpl(host, 2137);
    private User user;
    private OrderTable orderTableModel;

    private JPanel delivererPanel;
    private JLabel delivererLabel;
    private JTextField hostTextField;
    private JTextField portTextField;
    private JButton registerButton;
    private JButton unregisterButton;
    private JLabel portLabel;
    private JLabel hostLabel;
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

    private void setUpButtons(){
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == registerButton){
                    if(delivererServer == null){
                        user = new User();
                        user.setRole(Role.Deliver);
                        user.setHost(host);
                        user.setPort(Integer.valueOf(portTextField.getText()));

                        try {
                            keeperClient.register(user);
                            setUpOrderTable();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Thread thread = new Thread(() -> {
                            delivererServer = new DelivererServer();
                            try {
                                delivererServer.start(user.getHost(), user.getPort());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        thread.start();
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
                        setUpOrderTable();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
