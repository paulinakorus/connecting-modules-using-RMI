package org.example.seller;

import org.example.service.clients.KeeperClientImpl;
import org.example.service.clientsInterfaces.KeeperClient;
import org.example.service.model.User;
import org.example.service.model.enums.Role;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SellerGUI extends JFrame{
    private String host = "localhost";
    private SellerServer sellerServer = null;
    private KeeperClient keeperClient = new KeeperClientImpl(host, 2137);
    private User user;

    private JPanel sellerPanel;
    private JLabel sellerLabel;
    private JTextField hostTextField;
    private JTextField portTextField;
    private JButton unregisterButton;
    private JButton registerButton;
    private JLabel hostLabel;
    private JLabel portLabel;

    public SellerGUI(){
        this.setTitle("Seller");                                     // set title of frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           // exit out off application
        this.setResizable(false);                                      // preventing frame from being resized
        this.setSize(1100, 700);                            // setting size
        this.setVisible(true);                                         // making frame visible
        this.add(sellerPanel);

        hostTextField.setText(host);
        setUpButtons();
    }

    private void setUpAccept(){

    }

    private void setUpButtons(){
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getSource() == registerButton){
                    if(sellerServer == null){
                        user = new User();
                        user.setRole(Role.Seller);
                        user.setHost(host);
                        user.setPort(Integer.valueOf(portTextField.getText()));

                        try {
                            keeperClient.register(user);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Thread thread = new Thread(() -> {
                            sellerServer = new SellerServer();
                            try {
                                sellerServer.start(user.getHost(), user.getPort());
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
    }
}
