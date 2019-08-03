package com.mashibing.tank;

import com.mashibing.tank.net.Client;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args){
        TankFrame.INSTANCE.setVisible(true);

//        new Thread(()->new Audio("audio/war1.wav").loop()).start();

        new Thread(() -> {
            for (; ; ){
                try {
                    TimeUnit.MICROSECONDS.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //repaint()自动调用paint()方法
                TankFrame.INSTANCE.repaint();
            }
        }).start();

        Client.INSTANCE.connect();
    }
}
