package org.example.seller;

public class SellerApplication {
    public static void main(String[] args){
        try{
            SellerGUI sellerGUI = new SellerGUI();
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}