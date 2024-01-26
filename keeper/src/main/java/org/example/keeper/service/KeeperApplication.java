package org.example.keeper.service;

import org.example.keeper.service.KeeperGUI;

public class KeeperApplication {
    public static void main(String[] args){
        try{
            KeeperGUI keeperGUI = new KeeperGUI();
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}