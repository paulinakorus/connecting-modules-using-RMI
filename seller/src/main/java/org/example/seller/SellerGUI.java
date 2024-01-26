package org.example.seller;

import org.example.service.RMI.RMIDeliverer;
import org.example.service.RMI.RMISeller;
import org.example.service.TriConsumer;
import org.example.service.clients.KeeperClientImpl;
import org.example.service.clientsInterfaces.KeeperClient;
import org.example.service.model.User;
import org.example.service.model.enums.Role;
import org.example.shop.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Random;

public class SellerGUI extends JFrame{
    private String host = "localhost";
    private KeeperClient keeperClient = new KeeperClientImpl(host, 2137);
    private User user;
    public IKeeper keeperServer;
    public ISeller seller = new RMISeller();
    public int sellerId;

    private JPanel sellerPanel;
    private JLabel sellerLabel;
    private JTextField hostTextField;
    private JTextField portTextField;
    private JButton unregisterButton;
    private JButton registerButton;
    private JLabel hostLabel;
    private JLabel portLabel;

    public SellerGUI() throws RemoteException {
        this.setTitle("Seller");                                     // set title of frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           // exit out off application
        this.setResizable(false);                                      // preventing frame from being resized
        this.setSize(1100, 700);                            // setting size
        this.setVisible(true);                                         // making frame visible
        this.add(sellerPanel);

        hostTextField.setText(host);
        setUpButtons();
    }

    public void resResponse(ICallback callback, List<Item> itemList) {
        System.out.println(itemList.size());
    }

    public void resAcceptOrder(ICustomer iCustomer, List<Item> itemList, List<Item> itemList1) {
        System.out.println("Accepting order");
    }
    public void connect() {
        try {
            Registry registry = LocateRegistry.getRegistry();                       // połączenie do serwera
            keeperServer = (IKeeper) registry.lookup("Keeper");                                         // połączenie do serwera
            sellerId = keeperServer.register(seller);

            ((RMISeller) seller).setResponseCallback(this::resResponse);
            ((RMISeller) seller).setAcceptOrderCallback(this::resAcceptOrder);

            System.out.println(sellerId);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setUpButtons(){
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == registerButton){
                    if(seller == null){
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
                        if(seller != null)
                            keeperServer.unregister(sellerId);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void acceptOrderCallback(ICustomer customer, List<Item> boughtItems, List<Item> returnedItems) throws RemoteException {
        keeperServer.returnOrder(returnedItems);
        customer.returnReceipt("Quantity: " + boughtItems.size() + " Cost: " + new Random().nextInt(1, 100)*10);
    }
}
