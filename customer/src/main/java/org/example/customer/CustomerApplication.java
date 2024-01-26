package org.example.customer;

public class CustomerApplication {
    public static void main(String[] args){
        try{
            CustomerGUI customerGUI = new CustomerGUI();
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}