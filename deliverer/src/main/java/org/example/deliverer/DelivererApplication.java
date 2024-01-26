package org.example.deliverer;

public class DelivererApplication {
    public static void main(String[] args){
        try{
            DelivererGUI deliverGUI = new DelivererGUI();
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}